package com.star.app.game;

public class HeroSettings {
    private int score;
    private int hp;
    private int money;
    private Weapon weapon;
    private Hero.Skill[] skills;
    private Shop shop;

    public HeroSettings(int score, int hp, int money, Weapon weapon, Hero.Skill[] skills, Shop shop) {
        this.score = score;
        this.hp = hp;
        this.money = money;
        this.weapon = weapon;
        this.skills = skills;
        this.shop = shop;
    }

    public Hero.Skill[] getSkills() {
        return skills;
    }

    public Shop getShop() {
        return shop;
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
