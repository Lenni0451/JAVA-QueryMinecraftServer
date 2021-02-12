package com.tekgator.queryminecraftserver.api.status.forge;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Objects;

public class ForgeData implements IModContainer {

    @SerializedName("channels")
    private Channel[] channels;
    @SerializedName("mods")
    private Mod[] mods;
    @SerializedName("fmlNetworkVersion")
    private int fmlNetworkVersion;

    public Channel[] getChannels() {
        return this.channels;
    }

    @Override
    public Mod[] getMods() {
        return this.mods;
    }

    public int getFmlNetworkVersion() {
        return this.fmlNetworkVersion;
    }

    @Override
    public String toString() {
        return "ForgeData{" +
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

}
