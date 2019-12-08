package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Bot implements Poolable {

    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private int hp;
    private int hpMax;
    private float angle;
    private float enginePower;
    private float fireTimer;
    private BotWeapon currentWeapon;
    private Vector2 tmpVector;
    private Circle hitArea;
    private boolean active;
    private Hero hero;

    @Override
    public boolean isActive() {
        return active;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public int getHpMax() {
        return hpMax;
    }

    public float getAngle() {
        return angle;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public Bot(GameController gc) {
        this.gc = gc;
        this.texture = Assets.getInstance().getAtlas().findRegion("bot");
        this.active = false;
        this.hitArea = new Circle(0, 0, 0);
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.angle = 0.0f;

        this.tmpVector = new Vector2(0, 0);

    }

    public void activate(float x, float y, float vx, float vy, Hero hero) {
        this.position.set(x, y);
        this.velocity.set(0.0f, 0.0f);
        this.hpMax = 15;
        this.hp = this.hpMax;
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.hitArea.setPosition(position);
        this.active = true;
        this.hitArea.setRadius(26);
        this.enginePower = 500.0f;
        this.hero = hero;

        this.currentWeapon = new BotWeapon(
                gc, this, "Laser", 0.2f, 1, 500.0f, 320,
                new Vector3[]{
                        new Vector3(24, 90, 0),
                        new Vector3(24, -90, 0)
                }
        );
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, 32, 32, 64, 64, 1, 1, angle);
        if(position.x > GameController.SPACE_WIDTH - ScreenManager.HALF_SCREEN_WIDTH) {
            batch.draw(texture, position.x - 32 - GameController.SPACE_WIDTH, position.y - 32, 32, 32, 32, 64, 1, 1, angle);
        }
        if(position.x < ScreenManager.HALF_SCREEN_WIDTH) {
            batch.draw(texture, position.x - 32 + GameController.SPACE_WIDTH, position.y - 32, 32, 32, 32, 64, 1, 1, angle);
        }
    }

    public void update(float dt) {
        fireTimer += dt;

        if (velocity.len() > 300.0f) {
            velocity.nor().scl(300.0f);
        }

        // поведение бота
        tmpVector.set(hero.getPosition()).sub(position);
        if (tmpVector.len()<2000) {
            angle = tmpVector.angle();
            tryToFire();
        }

        float dst = hero.getPosition().dst(position);

        if (dst < 300) {
            velocity.x = 0.0f;
            velocity.y = 0.0f;
        } else {
            velocity.x += (float) Math.cos(Math.toRadians(angle)) * enginePower * dt;
            velocity.y += (float) Math.sin(Math.toRadians(angle)) * enginePower * dt;
        }

        position.mulAdd(velocity, dt);
        hitArea.setPosition(position);

        // боту тоже нужно пламя из движка :-)
        if (velocity.len() > 50.0f) {
            float bx, by;
            bx = position.x - 28.0f * (float) Math.cos(Math.toRadians(angle));
            by = position.y - 28.0f * (float) Math.sin(Math.toRadians(angle));
            for (int i = 0; i < 2; i++) {
                gc.getParticleController().setup(
                        bx + MathUtils.random(-4, 4), by + MathUtils.random(-4, 4),
                        velocity.x * -0.3f + MathUtils.random(-20, 20), velocity.y * -0.3f + MathUtils.random(-20, 20),
                        0.5f,
                        1.2f, 0.2f,
                        1.0f, 0.5f, 0.0f, 1.0f,
                        1.0f, 1.0f, 1.0f, 0.0f
                );
            }
        }
        checkSpaceBorders();
    }

    public void tryToFire() {
        if (fireTimer > currentWeapon.getFirePeriod()) {
            fireTimer = 0.0f;
            currentWeapon.fire();
        }
    }

    public void checkSpaceBorders() {
        if (position.x < hitArea.radius) {
            position.x += GameController.SPACE_WIDTH;
        }
        if (position.x > GameController.SPACE_WIDTH - hitArea.radius) {
            position.x -= GameController.SPACE_WIDTH;
        }
        if (position.y < hitArea.radius) {
            position.y = GameController.SPACE_HEIGHT - hitArea.radius - 1;
        }
        if (position.y > GameController.SPACE_HEIGHT - hitArea.radius) {
            position.y = hitArea.radius + 1;
        }
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            return true;
        }
        return false;
    }

    public void deactivate() {
        active = false;
    }
}
