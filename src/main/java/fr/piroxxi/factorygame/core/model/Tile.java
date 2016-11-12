package fr.piroxxi.factorygame.core.model;

import javax.persistence.*;

@Entity
@Table
public class Tile {
    @Id
    @GeneratedValue
    private Long id;

    private TileType type = TileType.empty;

    private int x;
    private int y;

    @ManyToOne
    private Player owner;

    public Tile() {
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return this.owner;
    }

    public TileType getType() {
        return this.type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public String toString() {
        String ownerS = " ";
        if (this.owner != null) {
            ownerS = "" + this.owner.getName().charAt(0);
        }
        String typeS = " ";
        if (this.type != TileType.empty) {
            typeS = "" + this.type.getTitle().charAt(0);
        }
        return "[" + ownerS + typeS + "]";
    }
}
