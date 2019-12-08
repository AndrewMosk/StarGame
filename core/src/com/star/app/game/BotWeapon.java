package com.star.app.game;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.star.app.screen.utils.Assets;

public class BotWeapon {
    private GameController gc;
    private Bot bot;
    private String title;
    private float firePeriod;
    private int damage;
    private float bulletSpeed;
    private int maxBullets;
    private int curBullets;
    private Sound shootSound;

    // Когда мы описываем слот Vector3[] slots:
    //   x - это то на сколько пикселей он смещен относительно центра
    //   y - угол смещения относильно центра корабля
    //   z - угол смещения вылета пуль относительно направления корабля
    private Vector3[] slots;

    public float getFirePeriod() {
        return firePeriod;
    }

    public int getMaxBullets() {
        return maxBullets;
    }

    public int getCurBullets() {
        return curBullets;
    }

    public void addAmmos(int amount) {
        curBullets += amount;
        if (curBullets > maxBullets) {
            curBullets = maxBullets;
        }
    }

    public BotWeapon(GameController gc, Bot bot, String title, float firePeriod, int damage, float bulletSpeed, int maxBullets, Vector3[] slots) {
        this.gc = gc;
        this.bot = bot;
        this.title = title;
        this.firePeriod = firePeriod;
        this.damage = damage;
        this.bulletSpeed = bulletSpeed;
        this.maxBullets = maxBullets;
        this.curBullets = this.maxBullets;
        this.slots = slots;
        this.shootSound = Assets.getInstance().getAssetManager().get("audio/Shoot.mp3");
    }

    public void fire() {
        if (curBullets > 0) {
            curBullets--;
            shootSound.play();

            for (int i = 0; i < slots.length; i++) {
                float x, y, vx, vy;
                x = bot.getPosition().x + slots[i].x * MathUtils.cosDeg(bot.getAngle() + slots[i].y);
                y = bot.getPosition().y + slots[i].x * MathUtils.sinDeg(bot.getAngle() + slots[i].y);
                vx = bot.getVelocity().x + bulletSpeed * MathUtils.cosDeg(bot.getAngle() + slots[i].z);
                vy = bot.getVelocity().y + bulletSpeed * MathUtils.sinDeg(bot.getAngle() + slots[i].z);
                gc.getBulletController().setup(x, y, vx, vy, bot.getAngle() + slots[i].z, false);
            }
        }
    }
}
