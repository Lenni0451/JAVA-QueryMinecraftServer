package com.tekgator.queryminecraftserver.api;

import com.tekgator.queryminecraftserver.api.status.Status;
import com.tekgator.queryminecraftserver.internal.*;

import java.net.Proxy;

/**
 * @author Patrick Weiss <info@tekgator.com>
 */
public final class QueryStatus {

    private final ServerDNS serverDNS;
    private final int timeOut;
    private final Protocol protocol;
    private final Proxy proxy;

    private Status status;

    private QueryStatus(Builder builder) throws QueryException {
        this.serverDNS = (builder.skipResolve || builder.protocol.equals(Protocol.TCP_1_2)) ? new ServerDNS.NoResolveServerDNS(builder.hostName, builder.port) : new ServerDNS(builder.hostName, builder.port);
        this.timeOut = builder.timeOut;
        this.protocol = builder.protocol;
        this.proxy = builder.proxy;
    }

    /**
     * Return last queried status of the Minecraft server without invoking a refresh
     * (at least if loaded already once)
     *
     * @return Status of the Minecraft Server
     * @throws QueryException
     */
    public Status getStatus()
            throws QueryException {

        if (this.status != null)
            return this.status;

        return refreshStatus();
    }

    /**
     * Queried status of the Minecraft server
     *
     * @return Status of the Minecraft Server
     * @throws QueryException
     */
    public Status refreshStatus() throws QueryException {
        switch (this.protocol) {
            case TCP_1_2:
            case TCP_1_3:
                this.status = new QueryStatusTcpLegacy(this.protocol, this.serverDNS, this.timeOut, this.proxy).getStatus();
                break;
            case TCP_1_6:
            case TCP_1_5:
                this.status = new QueryStatusTcpDepreciated(this.protocol, this.serverDNS, this.timeOut, this.proxy).getStatus();
                break;
            case TCP:
                this.status = new QueryStatusTcp(this.protocol, this.serverDNS, this.timeOut, this.proxy).getStatus();
                break;
            case UDP_BASIC:
            case UDP_FULL:
                this.status = new QueryStatusUdp(this.protocol, this.serverDNS, this.timeOut).getStatus();
                break;
            case UDP_BEDROCK:
                this.status = new QueryStatusUdpBedrock(this.protocol, this.serverDNS, this.timeOut).getStatus();
                break;
            default:
                break;
        }
        return this.status;
    }


    /**
     * Builder class to get an instance of StatusQuery
     *
     * @author Patrick Weiss <info@tekgator.com>
     * @see QueryStatus
     */
    public static final class Builder {

        private final String hostName;
        private int port = 0;
        private boolean skipResolve;
        private int timeOut = 1000;
        private Protocol protocol = Protocol.TCP;
        private Proxy proxy = Proxy.NO_PROXY;

        /**
         * Constructor of the builder class
         *
         * @param hostName or ip address of the Minecraft Server
         */
        public Builder(String hostName) {
            String[] parts = hostName.split(":");
            this.hostName = parts[0];
            try {
                this.port = Integer.parseInt(parts[1]);
            } catch (Throwable ignored) {
            }
        }

        /**
         * Set port of the minecraft server for the query. If not ommited it will be loaded via SRV
         * record and in case no SRV revord is found the default Minecraft port (=25565) is used
         *
         * @param port of the Minecraft Server
         */
        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        /**
         * Skip resolving the server srv
         * Automatically set if using mc <= 1.2.5
         *
         * @return port of the Minecraft Server
         */
        public Builder setSkipResolve() {
            this.skipResolve = true;
            return this;
        }

        /**
         * Set protocol to be used for the Query
         *
         * @param protocol The used Minecraft Protocol version for invoking the query
         * @see Protocol
         */
        public Builder setProtocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        /**
         * Use Minecraft Protocol TCP (Minecraft >= v1.7) for
         * Query (method can be used instead setProtocol to
         * drop dependency of Protocol enum
         */
        public Builder setProtocolTcp() {
            return this.setProtocol(Protocol.TCP);
        }


        /**
         * Use Minecraft Protocol TCP depreciated (Minecraft v1.6.4 - v1.6.0)
         * for Query (method can be used instead setProtocol to
         * drop dependency of Protocol enum
         */
        public Builder setProtocolTcp1_6() {
            return this.setProtocol(Protocol.TCP_1_6);
        }

        /**
         * Use Minecraft Protocol TCP depreciated (Minecraft v1.5.2 - v1.4.0)
         * for Query (method can be used instead setProtocol to
         * drop dependency of Protocol enum
         */
        public Builder setProtocolTcp1_5() {
            return this.setProtocol(Protocol.TCP_1_5);
        }

        /**
         * Use Minecraft Protocol TCP depreciated (Minecraft v1.3.2 - v1.3.1)
         * for Query (method can be used instead setProtocol to
         * drop dependency of Protocol enum
         */
        public Builder setProtocolTcp1_3() {
            return this.setProtocol(Protocol.TCP_1_3);
        }

        /**
         * Use Minecraft Protocol TCP depreciated (Minecraft v1.2.5 - b1.8)
         * for Query (method can be used instead setProtocol to
         * drop dependency of Protocol enum
         */
        public Builder setProtocolTcp1_2() {
            return this.setProtocol(Protocol.TCP_1_2);
        }


        /**
         * Use Minecraft Protocol UDP basic for Query (method can be
         * used instead setProtocol to drop dependency of Protocol enum
         */
        public Builder setProtocolUdpBasic() {
            return this.setProtocol(Protocol.UDP_BASIC);
        }


        /**
         * Use Minecraft Protocol UDP full for Query (method can be
         * used instead setProtocol to drop dependency of Protocol enum
         */
        public Builder setProtocolUdpFull() {
            return this.setProtocol(Protocol.UDP_FULL);
        }

        /**
         * Use UDP Bedrock protocol
         */
        public Builder setProtocolUdpBedrock() {
            return this.setProtocol(Protocol.UDP_BEDROCK);
        }


        /**
         * Set socket timeout
         *
         * @param timeOut in MS
         */
        public Builder setTimeout(int timeOut) {
            this.timeOut = timeOut;
            return this;
        }

        /**
         * Set the proxy used to connect
         *
         * @param proxy The proxy to use for the connections
         */
        public Builder setProxy(Proxy proxy) {
            this.proxy = proxy;
            return this;
        }

        /**
         * @return New instance of the QueryStatus class
         */
        public QueryStatus build() throws QueryException {
            return new QueryStatus(this);
        }

    }

}