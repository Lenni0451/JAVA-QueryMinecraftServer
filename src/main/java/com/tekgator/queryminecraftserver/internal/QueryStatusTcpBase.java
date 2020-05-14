package com.tekgator.queryminecraftserver.internal;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;

import com.tekgator.queryminecraftserver.api.Status;

/**
 * @author Patrick Weiss <info@tekgator.com>
 */
public abstract class QueryStatusTcpBase {

    protected Status status;

    protected final ServerDNS serverDNS;
    protected final int timeOut;

    protected long pingStart = 0;
    protected long pingEnd = 0;

    protected Socket socket = null;
    protected OutputStream outputStream;
    protected DataOutputStream dataOutputStream;

    protected InputStream inputStream;
    protected DataInputStream dataInputStream;

    public QueryStatusTcpBase(ServerDNS serverDNS, int timeOut) {
        this.serverDNS = serverDNS;
        this.timeOut = timeOut;
    }

    public abstract Status getStatus() throws SocketException, IOException;

    protected void connect() throws IOException {
        this.socket = new Socket();

        // connect to server
        this.socket.setSoTimeout(this.timeOut);
        this.socket.connect(serverDNS.getInetSocketAddress(), this.timeOut);

        // retrieve the output stream from the socket
        this.outputStream = this.socket.getOutputStream();
        this.dataOutputStream = new DataOutputStream(this.outputStream);

        // retrieve the input stream from the socket
        this.inputStream = this.socket.getInputStream();
        this.dataInputStream = new DataInputStream(this.inputStream);
    }

    protected void disconnect () {
        try {
            // close all streams and socket connection to server
            if (this.dataOutputStream != null)
                this.dataOutputStream.close();

            if (this.outputStream != null) 
                this.outputStream.close();

            if (this.dataInputStream != null)
                this.dataInputStream.close();

            if (this.inputStream != null)
                this.inputStream.close();

            if (this.socket != null)
                this.socket.close();
        } catch (IOException e) {
            // failed to disconnect?!, just keep going then
        } finally {
            this.dataOutputStream = null;
            this.outputStream = null;
            this.dataInputStream = null;
            this.inputStream = null;
            this.socket = null;
        }
    }

    protected int calculateLatency() {
        return (int) (this.pingEnd - this.pingStart);
    }

    protected void writeVarInt(DataOutputStream out, int paramInt) throws IOException {
        while (true) {
            if ((paramInt & 0xFFFFFF80) == 0) {
                out.writeByte(paramInt);
                return;
            }

            out.writeByte(paramInt & 0x7F | 0x80);
            paramInt >>>= 7;
        }
    }

    protected int readVarInt(DataInputStream in) throws IOException, NumberFormatException {
        int i = 0;
        int j = 0;
        int k;
        while (true) {
            k = in.readByte();
            i |= (k & 0x7F) << j++ * 7;
            if (j > 5) throw new NumberFormatException("VarInt too big");
            if ((k & 0x80) != 128) break;
        }
        return i;
    }

}