package com.tekgator.queryminecraftserver.internal;

import com.tekgator.queryminecraftserver.api.Protocol;
import com.tekgator.queryminecraftserver.api.QueryException;
import com.tekgator.queryminecraftserver.api.Status;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author Patrick Weiss <info@tekgator.com>
 * @see http://wiki.vg/Server_List_Ping#Ping_Process
 */
public class QueryStatusTcpLegacy extends QueryStatusTcpBase {

    public QueryStatusTcpLegacy(final Protocol protocol, final ServerDNS serverDNS, final int timeOut) {
        super(protocol, serverDNS, timeOut);
    }

    @Override
    public Status getStatus() throws QueryException {
        try {
            // connect to the server
            connect();

            // send handshake
            sendHandShake();

            // receive status response from the server
            String response = receiveStatusResponse();

            // finally build the status object
            this.status = new StatusBuilder()
                    .setProtocol(this.protocol)
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

    @Override
    protected void sendHandShake() throws QueryException {
        final ByteArrayOutputStream b = new ByteArrayOutputStream();
        final DataOutputStream handshake = new DataOutputStream(b);

        try {

            handshake.writeByte(0xFE); // packet identifier for a server list ping

            this.pingStart = System.currentTimeMillis();

            dataOutputStream.write(b.toByteArray()); //write handshake packet
            dataOutputStream.flush();
        } catch (final IOException e) {
            throw new QueryException(QueryException.ErrorType.SEND_FAILED, "Failed to write handshake to socket");
        }
    }

    @Override
    protected String receiveStatusResponse() throws QueryException {
        String response;

        try {
            int id = dataInputStream.readUnsignedByte();

            if (id == -1) {
                throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM, "Premature end of stream");
            } else if (id != 0xFF) {
                //we want a status response
                throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE, String.format("Invalid packetID: %d", id));
            }

            // set time when the first message is received for latency calculation
            this.pingEnd = System.currentTimeMillis();

            int length = this.dataInputStream.readUnsignedShort();

            if (length == -1) {
                throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM, "Premature end of stream");
            } else if (length == 0) {
                throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE, "Invalid length of response");
            }

            if (length != this.dataInputStream.available()) {
                // Server responded with smaller string length than what
                // is available on the socket, using this amount
                length = dataInputStream.available();
            }

            byte[] byteStream = new byte[length];
            if (this.dataInputStream.read(byteStream, 0, length) != length) {
                throw new QueryException(QueryException.ErrorType.NETWORK_PROBLEM, "Premature end of stream");
            }

            response = new String((new String(byteStream, StandardCharsets.UTF_16BE)).getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new QueryException(QueryException.ErrorType.INVALID_RESPONSE);
        }

        return response;
    }


}