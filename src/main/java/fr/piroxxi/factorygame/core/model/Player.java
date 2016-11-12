package fr.piroxxi.factorygame.core.model;

import javax.persistence.*;

@Entity
@Table
public class Player {
    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private int money;

    @ManyToOne
    private Game game;

    public Player() {
    }

    public Player(Game game, String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public Long getId() {
        return id;
    }

    public void pay(int montant) {
        this.money -= montant;
    }

    public void receive(int montant) {
        this.money += montant;
    }
}
