package com.tekgator.queryminecraftserver.api.status.players;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.Objects;

public class Players {

    @SerializedName("max")
    private int max;
    @SerializedName("online")
    private int online;
    @SerializedName("sample")
    private Player[] sample;

    public int getMax() {
        return this.max;
    }

    public int getOnline() {
        return this.online;
    }

    public Player[] getSample() {
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

}
