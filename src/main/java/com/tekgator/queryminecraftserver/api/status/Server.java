package com.tekgator.queryminecraftserver.api.status;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Server {

    @SerializedName("targethostname")
    private String targethostname;
    @SerializedName("hostname")
    private String hostname;
    @SerializedName("ipaddress")
    private String ipaddress;
    @SerializedName("port")
    private int port;
    @SerializedName("queryport")
    private int queryport;
    @SerializedName("latency")
    private int latency;
    @SerializedName("serverId")
    private long serverId;

    public String getTargethostname() {
        return this.targethostname;
    }

    public String getHostname() {
        return this.hostname;
    }

    public String getIpaddress() {
        return this.ipaddress;
    }

    public int getPort() {
        return this.port;
    }

    public int getQueryport() {
        return this.queryport;
    }

    public int getLatency() {
        return this.latency;
    }

    public long getServerId() {
        return this.serverId;
    }

    @Override
    public String toString() {
        return "Server{" +
                "targethostname='" + targethostname + '\'' +
                ", hostname='" + hostname + '\'' +
                ", ipaddress='" + ipaddress + '\'' +
                ", port=" + port +
                ", queryport=" + queryport +
                ", latency=" + latency +
                ", serverId=" + serverId +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Server server = (Server) o;
        return port == server.port && queryport == server.queryport && latency == server.latency && serverId == server.serverId && Objects.equals(targethostname, server.targethostname) && Objects.equals(hostname, server.hostname) && Objects.equals(ipaddress, server.ipaddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targethostname, hostname, ipaddress, port, queryport, latency, serverId);
    }

}
