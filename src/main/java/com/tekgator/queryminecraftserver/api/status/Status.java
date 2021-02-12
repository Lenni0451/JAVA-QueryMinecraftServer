package com.tekgator.queryminecraftserver.api.status;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.tekgator.queryminecraftserver.api.status.forge.ForgeData;
import com.tekgator.queryminecraftserver.api.status.forge.ModInfo;
import com.tekgator.queryminecraftserver.api.status.players.Players;

import java.util.Objects;

public class Status {

    @SerializedName("server")
    private Server server;
    @SerializedName("version")
    private Version version;
    @SerializedName("players")
    private Players players;
    @SerializedName("description")
    private String description;
    @SerializedName("favicon")
    private String favicon;
    @SerializedName("modinfo")
    private ModInfo modinfo;
    @SerializedName("forgeData")
    private ForgeData forgeData;
    @SerializedName("gametype")
    private String gametype;
    @SerializedName("map")
    private String map;
    @SerializedName("nintendoLimited")
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

    public String getFavicon() {
        return this.favicon;
    }

    public ModInfo getModinfo() {
        return this.modinfo;
    }

    public ForgeData getForgeData() {
        return this.forgeData;
    }

    public String getGametype() {
        return this.gametype;
    }

    public String getMap() {
        return this.map;
    }

    public Boolean getNintendoLimited() {
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

}
