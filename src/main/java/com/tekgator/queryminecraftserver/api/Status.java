package com.tekgator.queryminecraftserver.api;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Patrick Weiss <info@tekgator.com>
 */
public class Status {

    private Server server;
    private Version version;
    private Players players;
    private String description;
    private String favicon;
    private ModInfo modinfo;
    private ForgeData forgeData;
    private String gametype;
    private String map;
    private Boolean nintendoLimited; //Bedrock only

    public Server getServer() {
        return this.server;
    }

    public Version getVersion() {
        return this.version;
    }

    public Players getPlayers() {
        return this.players;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFavIcon() {
        return this.favicon;
    }

    public String getFavIconStripped() {
        try {
            return favicon.replace("data:image/png;base64,", "");
        } catch (Exception ex) {
            return null;
        }
    }

    public ModInfo getModinfo() {
        return this.modinfo;
    }

    public ForgeData getForgeData() {
        return this.forgeData;
    }

    public String getGameType() {
        return this.gametype;
    }

    public String getMap() {
        return this.map;
    }

    public Boolean isNintendoLimited() {
        return this.nintendoLimited;
    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    @Override
    public String toString() {
        return "Status{" +
                "server=" + server +
                ", version=" + version +
                ", players=" + players +
                ", description='" + description + '\'' +
                ", favicon='" + favicon + '\'' +
                ", modinfo=" + modinfo +
                ", forgeData=" + forgeData +
                ", gametype='" + gametype + '\'' +
                ", map='" + map + '\'' +
                ", nintendoLimited=" + nintendoLimited +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Status status = (Status) o;
        return Objects.equals(server, status.server) && Objects.equals(version, status.version) && Objects.equals(players, status.players) && Objects.equals(description, status.description) && Objects.equals(favicon, status.favicon) && Objects.equals(modinfo, status.modinfo) && Objects.equals(forgeData, status.forgeData) && Objects.equals(gametype, status.gametype) && Objects.equals(map, status.map) && Objects.equals(nintendoLimited, status.nintendoLimited);
    }

    @Override
    public int hashCode() {
        return Objects.hash(server, version, players, description, favicon, modinfo, forgeData, gametype, map, nintendoLimited);
    }


    public static class Server {

        private String targethostname;
        private String hostname;
        private String ipaddress;
        private int port;
        private int queryport;
        private int latency;
        private long serverId;

        public String getTargetHostName() {
            return this.targethostname;
        }

        public String getHostName() {
            return this.hostname;
        }

        public int getPort() {
            return this.port;
        }

        public int getQueryPort() {
            return this.queryport;
        }

        public int getLatency() {
            return this.latency;
        }

        public String getIpAddress() {
            return this.ipaddress;
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

    public static class Version {

        private String name;
        private int protocol;

        public String getName() {
            return this.name;
        }

        public int getProtocol() {
            return this.protocol;
        }

        @Override
        public String toString() {
            return "Version{" +
                    "name='" + name + '\'' +
                    ", protocol=" + protocol +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Version version = (Version) o;
            return protocol == version.protocol && Objects.equals(name, version.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, protocol);
        }

    }

    public static class Players {

        private int max;
        private int online;
        private Player[] sample;

        public int getOnlinePlayers() {
            return this.online;
        }

        public int getMaxPlayers() {
            return this.max;
        }

        public Player[] getPlayer() {
            return this.sample;
        }

        @Override
        public String toString() {
            return "Players{" +
                    "max=" + max +
                    ", online=" + online +
                    ", sample=" + Arrays.toString(sample) +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Players players = (Players) o;
            return max == players.max && online == players.online && Arrays.equals(sample, players.sample);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(max, online);
            result = 31 * result + Arrays.hashCode(sample);
            return result;
        }


        public static class Player {

            private String name;
            private String id;

            public String getName() {
                return this.name;
            }

            public String getID() {
                return this.id;
            }

            @Override
            public String toString() {
                return "Player{" +
                        "name='" + name + '\'' +
                        ", id='" + id + '\'' +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Player player = (Player) o;
                return Objects.equals(name, player.name) && Objects.equals(id, player.id);
            }

            @Override
            public int hashCode() {
                return Objects.hash(name, id);
            }

        }

    }

    public static class ModInfo {

        private String type;
        private Mod[] modList;

        public String getType() {
            return this.type;
        }

        public Mod[] getMods() {
            return this.modList;
        }

        @Override
        public String toString() {
            return "Mods{" +
                    "type='" + type + '\'' +
                    ", modList=" + Arrays.toString(modList) +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ModInfo modInfo = (ModInfo) o;
            return Objects.equals(type, modInfo.type) && Arrays.equals(modList, modInfo.modList);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(type);
            result = 31 * result + Arrays.hashCode(modList);
            return result;
        }

    }

    public static class ForgeData {

        private Channel[] channels;
        private Mod[] mods;
        private int fmlNetworkVersion;

        public Channel[] getChannels() {
            return this.channels;
        }

        public Mod[] getMods() {
            return this.mods;
        }

        public int getFmlNetworkVersion() {
            return this.fmlNetworkVersion;
        }

        @Override
        public String toString() {
            return "NewForgeData{" +
                    "channels=" + Arrays.toString(channels) +
                    ", mods=" + Arrays.toString(mods) +
                    ", fmlNetworkVersion=" + fmlNetworkVersion +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ForgeData forgeData = (ForgeData) o;
            return fmlNetworkVersion == forgeData.fmlNetworkVersion && Arrays.equals(channels, forgeData.channels) && Arrays.equals(mods, forgeData.mods);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(fmlNetworkVersion);
            result = 31 * result + Arrays.hashCode(channels);
            result = 31 * result + Arrays.hashCode(mods);
            return result;
        }


        public static class Channel {

            private String res;
            private String version;
            private boolean required;

            public String getName() {
                return this.res;
            }

            public String getVersion() {
                return this.version;
            }

            public boolean isRequired() {
                return this.required;
            }

            @Override
            public String toString() {
                return "Channel{" +
                        "res='" + res + '\'' +
                        ", version='" + version + '\'' +
                        ", required=" + required +
                        '}';
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Channel channel = (Channel) o;
                return required == channel.required && Objects.equals(res, channel.res) && Objects.equals(version, channel.version);
            }

            @Override
            public int hashCode() {
                return Objects.hash(res, version, required);
            }

        }

    }

    public static class Mod {

        private String modid;
        private String version;

        public String getVersion() {
            return this.version;
        }

        public String getModID() {
            return this.modid;
        }

        @Override
        public String toString() {
            return "Mod{" +
                    "modid='" + modid + '\'' +
                    ", version='" + version + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Mod mod = (Mod) o;
            return Objects.equals(modid, mod.modid) && Objects.equals(version, mod.version);
        }

        @Override
        public int hashCode() {
            return Objects.hash(modid, version);
        }

    }

}