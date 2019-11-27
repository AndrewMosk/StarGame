package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private DropItemController dropItemController;
    private Hero hero;
    private Vector2 tmpVec;
    private String[] dropItems;

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public Background getBackground() {
        return background;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public DropItemController getDropItemController() {
        return dropItemController;
    }

    public Hero getHero() {
        return hero;
    }

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.dropItemController = new DropItemController(this);
        particleController = new ParticleController();
        this.tmpVec = new Vector2(0.0f, 0.0f);
        for (int i = 0; i < 2; i++) {
            this.asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH), MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), 1.0f);
        }
        this.dropItems = new String[3];
        this.dropItems[0] = "kit";
        this.dropItems[1] = "ammo";
        this.dropItems[2] = "coin";
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        dropItemController.update(dt);
        checkCollisions();
    }

    public void checkCollisions() {
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            if (a.getHitArea().overlaps(hero.getHitArea())) {
                float dst = a.getPosition().dst(hero.getPosition());
                float halfOverLen = (a.getHitArea().radius + hero.getHitArea().radius - dst) / 2.0f;
                tmpVec.set(hero.getPosition()).sub(a.getPosition()).nor();
                hero.getPosition().mulAdd(tmpVec, halfOverLen);
                a.getPosition().mulAdd(tmpVec, -halfOverLen);

                float sumScl = hero.getHitArea().radius * 2 + a.getHitArea().radius;

                hero.getVelocity().mulAdd(tmpVec, 400.0f * halfOverLen * a.getHitArea().radius / sumScl);
                a.getVelocity().mulAdd(tmpVec, 400.0f * -halfOverLen  * hero.getHitArea().radius / sumScl);

                if(a.takeDamage(2)) {
                    hero.addScore(a.getHpMax() * 10);
                }
                hero.takeDamage(2);
            }
        }

        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);

                if (a.getHitArea().contains(b.getPosition())) {

                    particleController.setup(
                            b.getPosition().x + MathUtils.random(-4, 4), b.getPosition().y + MathUtils.random(-4, 4),
                            b.getVelocity().x * -0.3f + MathUtils.random(-30, 30), b.getVelocity().y * -0.3f + MathUtils.random(-30, 30),
                            0.2f,
                            2.2f, 1.7f,
                            1.0f, 1.0f, 1.0f, 1.0f,
                            0.0f, 0.0f, 1.0f, 0.0f
                    );

                    b.deactivate();
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                        //генерация бонусного предмета
                        //бонусный предмет создается не при каждом уничтожении астеройда, а с вероятностью 10% нужно добавить эту вероятность
                        int index = MathUtils.random(0,2);
                        this.dropItemController.setup(a.getPosition().x, a.getPosition().y, -100.0f, dropItems[index]);
                    }
                    break;
                }
            }
        }

        //столкновение с выпавшим призом
        for (int i = 0; i < dropItemController.getActiveList().size(); i++) {
            DropItem dropItem = dropItemController.getActiveList().get(i);
            if (dropItem.getHitArea().overlaps(hero.getHitArea())) {
                dropItem.takeDamage(1);

                String itemName = dropItem.getName();
                //получение бонуса
                switch (itemName) {
                    case "kit":
                        hero.addHp(10);
                        break;
                    case "coin":
                        hero.addScore(1000);
                        break;
                    case "ammo":
                        hero.getCurrentWeapon().addBullets(15);
                        break;
                }
            }
        }
    }
}