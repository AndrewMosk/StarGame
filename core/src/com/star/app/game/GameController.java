package com.star.app.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.star.app.screen.ScreenManager;

import static java.lang.Math.*;

public class GameController {
    private int level;
    private Background background;
    private AsteroidController asteroidController;
    private BulletController bulletController;
    private ParticleController particleController;
    private PowerUpsController powerUpsController;
    private Hero hero;
    private Vector2 tmpVec;
    private Stage stage;

    public Stage getStage() {
        return stage;
    }

    public AsteroidController getAsteroidController() {
        return asteroidController;
    }

    public BulletController getBulletController() {
        return bulletController;
    }

    public Background getBackground() {
        return background;
    }

    public PowerUpsController getPowerUpsController() {
        return powerUpsController;
    }

    public ParticleController getParticleController() {
        return particleController;
    }

    public Hero getHero() {
        return hero;
    }

    public GameController(SpriteBatch batch) {
        this.background = new Background(this);
        this.hero = new Hero(this, "PLAYER1");
        this.asteroidController = new AsteroidController(this);
        this.bulletController = new BulletController(this);
        this.particleController = new ParticleController();
        this.powerUpsController = new PowerUpsController(this);
        this.tmpVec = new Vector2(0.0f, 0.0f);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.stage.addActor(hero.getShop());
        this.level = 1;
        Gdx.input.setInputProcessor(stage);
        for (int i = 0; i < 2; i++) {
            this.asteroidController.setup(MathUtils.random(0, ScreenManager.SCREEN_WIDTH), MathUtils.random(0, ScreenManager.SCREEN_HEIGHT),
                    MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), 1.0f);
        }
    }

    public void update(float dt) {
        background.update(dt);
        hero.update(dt);
        asteroidController.update(dt);
        bulletController.update(dt);
        particleController.update(dt);
        powerUpsController.update(dt);
        checkCollisions();
        if(!hero.isAlive()) {
            ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAMEOVER, hero);
        }
        stage.act(dt);
    }

    public void hit(Hero h, Asteroid a) {
        // h - 1
        // a - 2
        float v1 = h.getVelocity().len();
        float v2 = a.getVelocity().len();

        float m1 = 0.1f;
        float m2 = a.getScale();

        float th1 = h.getVelocity().angleRad();
        float th2 = a.getVelocity().angleRad();

        float phi1 = tmpVec.set(a.getPosition()).sub(h.getPosition()).angleRad();
        float phi2 = tmpVec.set(h.getPosition()).sub(a.getPosition()).angleRad();

        float v1xN = (float) (((v1 * cos(th1 - phi1) * (m1 - m2) + 2 * m2 * v2 * cos(th2 - phi1)) / (m1 + m2)) * cos(phi1) + v1 * sin(th1 - phi1) * cos(phi1 + PI / 2.0f));
        float v1yN = (float) (((v1 * cos(th1 - phi1) * (m1 - m2) + 2 * m2 * v2 * cos(th2 - phi1)) / (m1 + m2)) * sin(phi1) + v1 * sin(th1 - phi1) * sin(phi1 + PI / 2.0f));

        float v2xN = (float) (((v2 * cos(th2 - phi2) * (m2 - m1) + 2 * m1 * v1 * cos(th1 - phi2)) / (m2 + m1)) * cos(phi2) + v2 * sin(th2 - phi2) * cos(phi2 + PI / 2.0f));
        float v2yN = (float) (((v2 * cos(th2 - phi2) * (m2 - m1) + 2 * m1 * v1 * cos(th1 - phi2)) / (m2 + m1)) * sin(phi2) + v2 * sin(th2 - phi2) * sin(phi2 + PI / 2.0f));

        h.getVelocity().set(v1xN, v1yN);
        a.getVelocity().set(v2xN, v2yN);
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
                hit(hero, a);
                if (a.takeDamage(2)) {
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
                        for (int k = 0; k < 3; k++) {
                            powerUpsController.setup(a.getPosition().x, a.getPosition().y, a.getScale() / 4.0f);
                        }
                    }
                    break;
                }
            }
        }
        // при приближении к power-ups они начинали лететь в сторону игрока
        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            float dst = p.getPosition().dst(hero.getPosition());
            if (dst<100) {
                Vector2 vector2 = hero.getPosition().cpy().sub(p.getPosition());
                p.getPosition().mulAdd(vector2, 0.05f);
            }
        }

        for (int i = 0; i < powerUpsController.getActiveList().size(); i++) {
            PowerUp p = powerUpsController.getActiveList().get(i);
            if (hero.getHitArea().contains(p.getPosition())) {
                hero.consume(p);
                particleController.getEffectBuilder().takePowerUpEffect(p.getPosition().x, p.getPosition().y, p.getType().index);
                p.deactivate();
            }
        }
    }

    public void dispose() {
        background.dispose();
    }
}