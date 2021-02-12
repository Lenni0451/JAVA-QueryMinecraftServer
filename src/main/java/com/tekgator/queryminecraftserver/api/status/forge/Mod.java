package com.tekgator.queryminecraftserver.api.status.forge;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Mod {

    @SerializedName(value = "modId", alternate = "modid")
    private String modId;
    @SerializedName(value = "modmarker", alternate = "version")
    private String modmarker;

    public String getModId() {
        return this.modId;
    }

    public String getModmarker() {
        return this.modmarker;
    }

    @Override
    public String toString() {
        return "Mod{" +
                "modId='" + modId + '\'' +
                ", modmarker='" + modmarker + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Mod mod = (Mod) o;
        return Objects.equals(modId, mod.modId) && Objects.equals(modmarker, mod.modmarker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(modId, modmarker);
    }

}
