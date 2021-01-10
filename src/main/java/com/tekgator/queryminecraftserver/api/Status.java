package com.tekgator.queryminecraftserver.api;

import com.google.gson.Gson;

import java.util.Arrays;

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

        }

    }

    public static class ModInfo {

        private String type;
        private Mod[] modList;

        public String getType() {
            return this.type;
        }

        public Mod[] getMod() {
            return this.modList;
        }

        @Override
        public String toString() {
            return "Mods{" +
                    "type='" + type + '\'' +
                    ", modList=" + Arrays.toString(modList) +
                    '}';
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

        }

        public static class Mod {

            private String modId;
            private String modmarker;

            public String getModId() {
                return this.modId;
            }

            public String getVersion() {
                return this.modmarker;
            }

            @Override
            public String toString() {
                return "Mod{" +
                        "modId='" + modId + '\'' +
                        ", modmarker='" + modmarker + '\'' +
                        '}';
            }

        }

    }

}