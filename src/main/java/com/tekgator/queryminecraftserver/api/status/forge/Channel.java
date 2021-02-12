package com.tekgator.queryminecraftserver.api.status.forge;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Channel {

    @SerializedName("res")
    private String res;
    @SerializedName("version")
    private String version;
    @SerializedName("required")
    private boolean required;

    public String getRes() {
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
