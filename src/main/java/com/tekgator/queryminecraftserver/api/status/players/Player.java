package com.tekgator.queryminecraftserver.api.status.players;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Player {

    @SerializedName("name")
    private String name;
    @SerializedName("id")
    private String id;

    public String getName() {
        return this.name;
    }

    public String getId() {
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
