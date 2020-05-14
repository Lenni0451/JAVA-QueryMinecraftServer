package com.tekgator.queryminecraftserver.internal;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.StreamCorruptedException;

import com.tekgator.queryminecraftserver.api.Protocol;
import com.tekgator.queryminecraftserver.api.Status;

/**
 * @author Patrick Weiss <info@tekgator.com>
 * @see http://wiki.vg/Server_List_Ping#Ping_Process
 */
public class QueryStatusTcpDepreciated extends QueryStatusTcpBase {

    public QueryStatusTcpDepreciated(final ServerDNS serverDNS, final int timeOut) {
        super(serverDNS, timeOut);
    }

    @Override
    public Status getStatus() throws IOException {
        
        try {
            // connect to the server
            connect();

            // send handshake
            sendHandShake();

            // receive status response from the server
            final String response = receiveStatusResponse();

            // finally build the status object
            this.status = new StatusBuilder()
                                .setProtocol(Protocol.TCP_DEPRECIATED)
                                .setLatency(calculateLatency())
                                .setServerDNS(this.serverDNS)
                                .setData(response)
                                .build();
        } finally {
            // disconnect from server
            disconnect();
        }

        return this.status;
    }

    private void sendHandShake() throws IOException {

        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream handshake = new DataOutputStream(b);

        final String mcPingStr = "MC|PingHost";

        handshake.writeByte(0xFE); // packet identifier for a server list ping
        handshake.writeByte(0x01); // server list ping's payload (always 1)
        handshake.writeByte(0xFA); // packet identifier for a plugin message
        handshake.writeShort(mcPingStr.length()); // length of following string
        handshake.writeBytes(new String(mcPingStr.getBytes("UTF-8"), "UTF-16BE")); // the string "MC|PingHost" encoded as a UTF-16BE string
        handshake.writeShort(7 + (2 * this.serverDNS.getTargetHostName().length())); // length of the rest of the data, as a short. Compute as 7 + 2*len(hostname)
        handshake.writeByte(Protocol.TCP_DEPRECIATED.getValue()); // protocol version
        handshake.writeShort(this.serverDNS.getTargetHostName().length()); // length of the hostname
        handshake.writeBytes(new String(this.serverDNS.getTargetHostName().getBytes("UTF-8"), "UTF-16BE")); // hostname the client is connecting to
        handshake.writeInt(this.serverDNS.getPort()); // port the client is connecting to, as an int.

        this.pingStart = System.currentTimeMillis();

        dataOutputStream.write(b.toByteArray()); //write handshake packet
        dataOutputStream.flush();
    }

    private String receiveStatusResponse() throws IOException {

        final int id = dataInputStream.readUnsignedByte();

        if (id == -1) {
            throw new StreamCorruptedException("Premature end of stream");
        } else if  (id != 0xFF) {
            //we want a status response
            throw new StreamCorruptedException(String.format("Invalid packetID: %d", id));
        }

        // set time when the first message is received for latency calculation
        this.pingEnd = System.currentTimeMillis();

        int length = this.dataInputStream.readUnsignedShort();

        if (length == -1) {
            throw new StreamCorruptedException("Premature end of stream");
        } else if (length == 0) {
            throw new StreamCorruptedException("Invalid length of response");
        }

        if (length != this.dataInputStream.available()) {
            // Server responded with smaller string length than what
            // is available on the socket, using this amount
            length = dataInputStream.available();
        }

        final byte[] byteStream = new byte[length];
        if (this.dataInputStream.read(byteStream, 0, length) != length) {
            throw new StreamCorruptedException("Premature end of stream");
        }

        return new String((new String(byteStream, "UTF-16BE")).getBytes("UTF-8"));
    }
 
    
}