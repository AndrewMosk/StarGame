package com.star.app.game;

import com.badlogic.gdx.math.MathUtils;
import com.star.app.screen.ScreenManager;

import java.util.List;

public class GameController {
    private Background background;
    private BulletController bulletController;
    private AsteroidController asteroidController;
    private Hero hero;

    public BulletController getBulletController() {
        return bulletController;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public Background getBackground() {
        return background;
    }

    public Hero getHero() {
        return hero;
    }

    public GameController() {
        this.background = new Background(this);
        this.hero = new Hero(this);
        this.bulletController = new BulletController();
        this.asteroidController = new AsteroidController();
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        bulletController.update(dt);
        asteroidController.update(dt);

        //создаю 4 астеройда, если они все уничтожены, добавляю еще 4
        //где их лучше всего создавать? в фоне? или потом будет добавлен пакет Enemies который будет создавать врагов и астеройд будет к врагам отнесен?
        if (asteroidController.getActiveList().size() == 0) {
            for (int i = 0; i < 3; i++) {
                float x = MathUtils.random(0, ScreenManager.SCREEN_WIDTH);
                float y = MathUtils.random(0, ScreenManager.SCREEN_HEIGHT);
                float vx = MathUtils.random(-360, 360);
                float vy = MathUtils.random(-360, 360);
                asteroidController.setup(x, y, vx, vy);
            }
        }
        checkCollisions();
    }

    public void checkCollisions() {
        List<Bullet> bulletList = bulletController.getActiveList();
        List<Asteroid> asteroidList = asteroidController.getActiveList();

        for (int i = 0; i < bulletList.size(); i++) {
            Bullet b = bulletList.get(i);
            boolean bulletDestroyedAsteroid = false;

            for (int j = 0; j < asteroidList.size(); j++) {
                Asteroid asteroid = asteroidList.get(j);
                if (b.getPosition().dst(asteroid.getPosition()) < 128.0f) {
                    asteroid.deactivate();
                    bulletDestroyedAsteroid = true;
                    break;
                }
            }

            // если пуля уничтожила астеройд, сама тоже выключается
            if (bulletDestroyedAsteroid) { b.deactivate();}
        }
    }
}