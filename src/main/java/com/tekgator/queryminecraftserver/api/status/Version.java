package com.tekgator.queryminecraftserver.api.status;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Version {

    @SerializedName("name")
    private String name;
    @SerializedName("protocol")
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
