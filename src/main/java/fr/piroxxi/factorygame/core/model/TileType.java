package fr.piroxxi.factorygame.core.model;

/**
 * Created by PiroXXI on 10/11/2016.
 */
public enum TileType {
    empty("EMPTY"),
    factory("FACTORY"),
    bigFactory("BIG_FACTORY"),
    houses("HOUSES"),
    store("STORE"),
    warehouse("WAREHOUSE");

    private final String title;

    TileType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
