package com.star.app.game;

public class HeroSettings {
    private int score;
    private int hp;
    private int money;
    private Weapon weapon;

    public HeroSettings(int score, int hp, int money, Weapon weapon) {
        this.score = score;
        this.hp = hp;
        this.money = money;
        this.weapon = weapon;
    }

    public int getScore() {
        return score;
    }

    public int getHp() {
        return hp;
    }

    public int getMoney() {
        return money;
    }

    public Weapon getWeapon() {
        return weapon;
    }
}
