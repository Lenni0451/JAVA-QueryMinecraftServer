package com.tekgator.queryminecraftserver.api.status.forge;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Objects;

public class ModInfo implements IModContainer {

    @SerializedName("type")
    private String type;
    @SerializedName("modList")
    private Mod[] modList;

    public String getType() {
        return this.type;
    }

    @Override
    public Mod[] getModList() {
        return this.modList;
    }

    @Override
    public String toString() {
        return "ModInfo{" +
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
