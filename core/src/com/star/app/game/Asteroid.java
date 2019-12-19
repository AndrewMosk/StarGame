package com.star.app.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class Asteroid implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private int hpMax;
    private int hp;
    private float scale;
    private float angle;
    private float rotationSpeed;
    private boolean active;
    private Circle hitArea;
    private boolean superAsteroid;

    private final float BASE_SIZE = 256.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2.0f;

    public boolean isSuperAsteroid() {
        return superAsteroid;
    }

    public int getHp() {
        return hp;
    }

    public int getHpMax() {
        return hpMax;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public float getScale() {
        return scale;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public void deactivate() {
        active = false;
    }

    public Asteroid(GameController gc) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.texture = Assets.getInstance().getAtlas().findRegion("asteroid");
    }

    public boolean takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
            if (!superAsteroid) {
                if (scale > 0.9f) {
                    gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f, false);
                    gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f, false);
                } else if (scale > 0.25f) {
                    gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f, false);
                    gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f, false);
                    gc.getAsteroidController().setup(position.x, position.y, MathUtils.random(-150.0f, 150.0f), MathUtils.random(-150.0f, 150.0f), scale - 0.2f, false);
                }
            }
            return true;
        }
        return false;
    }

    public void activate(float x, float y, float vx, float vy, float scale, boolean superAsteroid) {
        this.position.set(x, y);
        this.velocity.set(vx, vy);
        if (this.velocity.len() < 50.0f) {
            this.velocity.nor().scl(50.0f);
        }
        this.hpMax = (int) ((10 + gc.getLevel() * 4) * scale);
        this.hp = this.hpMax;
        this.angle = MathUtils.random(0.0f, 360.0f);
        this.hitArea.setPosition(position);
        this.rotationSpeed = MathUtils.random(-60.0f, 60.0f);
        this.active = true;
        this.scale = scale;
        this.hitArea.setRadius(BASE_RADIUS * scale * 0.9f);
        this.superAsteroid = superAsteroid;
    }

    public void render(SpriteBatch batch) {
        if (superAsteroid) {
            batch.setColor(0.0f, 1.0f, 0.0f, 1.0f);
        }

        batch.draw(texture, position.x - 128, position.y - 128, 128, 128, 256, 256, scale, scale, angle);
        if(position.x > GameController.SPACE_WIDTH - ScreenManager.HALF_SCREEN_WIDTH) {
            batch.draw(texture, position.x - 128 - GameController.SPACE_WIDTH, position.y - 128, 128, 128, 256, 256, scale, scale, angle);
        }
        if(position.x < ScreenManager.HALF_SCREEN_WIDTH) {
            batch.draw(texture, position.x - 128 + GameController.SPACE_WIDTH, position.y - 128, 128, 128, 256, 256, scale, scale, angle);
        }

        if (superAsteroid) {
            batch.setColor(new Color(255.0f, 255.0f, 255.0f, 1.0f));
        }
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        angle += rotationSpeed * dt;
        if (position.x < -BASE_RADIUS * scale) {
            position.x = GameController.SPACE_WIDTH + BASE_RADIUS * scale;
        }
        if (position.x > GameController.SPACE_WIDTH + BASE_RADIUS * scale) {
            position.x = -BASE_RADIUS * scale;
        }
        if (position.y < -BASE_RADIUS * scale) {
            position.y = GameController.SPACE_HEIGHT + BASE_RADIUS * scale;
        }
        if (position.y > GameController.SPACE_HEIGHT + BASE_RADIUS * scale) {
            position.y = -BASE_RADIUS * scale;
        }
        hitArea.setPosition(position);
    }
}