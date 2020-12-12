package com.tekgator.queryminecraftserver.api;

import java.nio.charset.StandardCharsets;

public class BedrockPong {

    private String edition;
    private String motd;
    private int protocolVersion = -1;
    private String version;
    private int playerCount = -1;
    private int maximumPlayerCount = -1;
    private long serverId;
    private String subMotd;
    private String gameType;
    private boolean nintendoLimited;
    private int ipv4Port = -1;
    private int ipv6Port = -1;
    private String[] extras; //Unknown

    public static BedrockPong fromRakNet(byte[] userData) {
        String info = new String(userData, StandardCharsets.UTF_8);
        BedrockPong bedrockPong = new BedrockPong();
        String[] infos = info.split(";");
        switch (infos.length) {
            default:
                bedrockPong.extras = new String[infos.length - 12];
                System.arraycopy(infos, 12, bedrockPong.extras, 0, bedrockPong.extras.length);
            case 12:
                try {
                    bedrockPong.ipv6Port = Integer.parseInt(infos[11]);
                } catch (NumberFormatException ignored) {
                }
            case 11:
                try {
                    bedrockPong.ipv4Port = Integer.parseInt(infos[10]);
                } catch (NumberFormatException ignored) {
                }
            case 10:
                bedrockPong.nintendoLimited = !"1".equalsIgnoreCase(infos[9]);
            case 9:
                bedrockPong.gameType = infos[8];
            case 8:
                bedrockPong.subMotd = infos[7];
            case 7:
                try {
                    bedrockPong.serverId = Long.parseLong(infos[6]);
                } catch (NumberFormatException ignored) {
                }
            case 6:
                try {
                    bedrockPong.maximumPlayerCount = Integer.parseInt(infos[5]);
                } catch (NumberFormatException ignored) {
                }
            case 5:
                try {
                    bedrockPong.playerCount = Integer.parseInt(infos[4]);
                } catch (NumberFormatException ignored) {
                }
            case 4:
                bedrockPong.version = infos[3];
            case 3:
                try {
                    bedrockPong.protocolVersion = Integer.parseInt(infos[2]);
                } catch (NumberFormatException ignored) {
                }
            case 2:
                bedrockPong.motd = infos[1];
            case 1:
                bedrockPong.edition = infos[0];
            case 0:
                return bedrockPong;
        }
    }

    public String getEdition() {
        return this.edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getMotd() {
        return this.motd;
    }

    public void setMotd(String motd) {
        this.motd = motd;
    }

    public int getProtocolVersion() {
        return this.protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getPlayerCount() {
        return this.playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getMaximumPlayerCount() {
        return this.maximumPlayerCount;
    }

    public void setMaximumPlayerCount(int maximumPlayerCount) {
        this.maximumPlayerCount = maximumPlayerCount;
    }

    public String getSubMotd() {
        return this.subMotd;
    }

    public void setSubMotd(String subMotd) {
        this.subMotd = subMotd;
    }

    public String getGameType() {
        return this.gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }

    public boolean isNintendoLimited() {
        return this.nintendoLimited;
    }

    public void setNintendoLimited(boolean nintendoLimited) {
        this.nintendoLimited = nintendoLimited;
    }

    public int getIpv4Port() {
        return this.ipv4Port;
    }

    public void setIpv4Port(int ipv4Port) {
        this.ipv4Port = ipv4Port;
    }

    public int getIpv6Port() {
        return this.ipv6Port;
    }

    public void setIpv6Port(int ipv6Port) {
        this.ipv6Port = ipv6Port;
    }

    public String[] getExtras() {
        return this.extras;
    }

    public void setExtras(String[] extras) {
        this.extras = extras;
    }

    public long getServerId() {
        return this.serverId;
    }

}
