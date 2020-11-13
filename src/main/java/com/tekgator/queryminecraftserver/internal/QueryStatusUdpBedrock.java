package com.tekgator.queryminecraftserver.internal;

import com.tekgator.queryminecraftserver.api.Protocol;
import com.tekgator.queryminecraftserver.api.QueryException;
import com.tekgator.queryminecraftserver.api.Status;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

public class QueryStatusUdpBedrock extends QueryStatusBase {

    private static final byte[] RAKNET_UNCONNECTED_MAGIC = new byte[]{0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120};

    private final long sessionId;
    private DatagramSocket socket;

    public QueryStatusUdpBedrock(final Protocol protocol, final ServerDNS serverDNS, final int timeOut) {
        super(protocol, serverDNS, timeOut);
        this.sessionId = ThreadLocalRandom.current().nextLong();
    }

    @Override
    public Status getStatus() throws QueryException {
        try {
            socket = new DatagramSocket();
            socket.setSoTimeout(this.timeOut);

            long latency = System.currentTimeMillis();

            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                DataOutputStream dos = new DataOutputStream(baos);
                dos.writeByte(1);
                dos.writeLong(System.currentTimeMillis());
                dos.write(RAKNET_UNCONNECTED_MAGIC);
                dos.writeLong(this.sessionId);
                InetAddress inetAddress = InetAddress.getByName(this.serverDNS.getTargetHostName());
                DatagramPacket datagramPacket = new DatagramPacket(baos.toByteArray(), baos.toByteArray().length, inetAddress, this.serverDNS.getPort());
                socket.send(datagramPacket);
            }

            final byte[] packetBuffer = new byte[32676];
            final DatagramPacket datagramPacket = new DatagramPacket(packetBuffer, packetBuffer.length);
            socket.receive(datagramPacket);
            latency = System.currentTimeMillis() - latency;

            final DataInputStream dis = new DataInputStream(new ByteArrayInputStream(datagramPacket.getData()));
            final int packetId = dis.readUnsignedByte();
            if (packetId == 28) {
                dis.readLong();
                dis.readLong();
                final byte[] readMagic = new byte[RAKNET_UNCONNECTED_MAGIC.length];
                dis.read(readMagic);
                if (!Arrays.equals(readMagic, RAKNET_UNCONNECTED_MAGIC)) {
                    throw new IllegalStateException("Invalid response (Magic does not match)");
                }

                byte[] userData = null;
                if (dis.available() > 0) {
                    userData = new byte[dis.readUnsignedShort()];
                    dis.read(userData);
                }

                this.status = new StatusBuilder()
                        .setProtocol(this.protocol)
                        .setLatency(latency)
                        .setServerDNS(this.serverDNS)
                        .setData(userData)
                        .build();
            } else {
                throw new IllegalStateException("Invalid response (" + packetId + ")");
            }
        } catch (Throwable t) {

        } finally {
            if (socket != null) {
                socket.close();
            }
        }

        return this.status;
    }

}
