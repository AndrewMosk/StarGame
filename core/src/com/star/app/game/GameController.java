package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.screen.ScreenManager;

public class GameController {
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private Hero hero;
    private boolean update;
    private String gameOver;

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public String getGameOver() {
        return gameOver;
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
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController();
        this.update = true;
        this.gameOver = "";
        for (int i = 0; i < 1; i++) {
            this.asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH), MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), 1.0f);
        }
    }

    public void update(float dt) {
        if (update) {
            background.update(dt);
            hero.update(dt);
            asteroidController.update(dt);
            bulletController.update(dt);
            checkCollisions();
        }else {
            gameOver = "Game Over!";
        }

    }

    public void checkCollisions() {
        // попадание пули в астеройд
        for (int i = 0; i < bulletController.getActiveList().size(); i++) {
            Bullet b = bulletController.getActiveList().get(i);
            for (int j = 0; j < asteroidController.getActiveList().size(); j++) {
                Asteroid a = asteroidController.getActiveList().get(j);
                if (a.getHitArea().contains(b.getPosition())) {
                    b.deactivate();
                    if (a.takeDamage(1)) {
                        hero.addScore(a.getHpMax() * 100);
                    }
                    break;
                }
            }
        }

        //столкновение корабля с астеройдом
        for (int i = 0; i < asteroidController.getActiveList().size(); i++) {
            Asteroid a = asteroidController.getActiveList().get(i);
            Circle asteroidHitArea = a.getHitArea();
            Circle shipHitArea = hero.getHitArea();

            if (asteroidHitArea.overlaps(shipHitArea)) {
                Vector2 asteroidPosition = a.getPosition();
                Vector2 heroPosition = hero.getPosition();
                Vector2 asteroidVelocity = a.getVelocity();
                Vector2 shipVelocity = hero.getVelocity();

                float len = asteroidPosition.sub(heroPosition).len();
                float overlap = asteroidHitArea.radius + shipHitArea.radius - len;
                asteroidVelocity.scl(-overlap/2);
                a.setVelocity(asteroidVelocity);

                shipVelocity.scl(-overlap/2);
                hero.setVelocity(shipVelocity);

                hero.takeDamage(5);
                if (hero.getHp()==0) {
                    update = false;
                }
                //сделать возврат boolean? все действия в update закатать в условие. истина - hp > 0 - все крутится, hp < 0 - работу прекращаю
            }

        }
    }
}