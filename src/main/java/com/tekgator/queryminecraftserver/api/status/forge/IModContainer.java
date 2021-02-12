package com.tekgator.queryminecraftserver.api.status.forge;

public interface IModContainer {

    //I wanted both methods to exists but they need to return the same
    //You will see a stack overflow if you forget to override one ;)

    default Mod[] getModList() {
        return this.getMods();
    }

    default Mod[] getMods() {
        return this.getModList();
    }

}
