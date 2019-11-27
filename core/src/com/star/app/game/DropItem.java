package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.star.app.game.helpers.Poolable;
import com.star.app.screen.ScreenManager;
import com.star.app.screen.utils.Assets;

public class DropItem implements Poolable {
    private GameController gc;
    private TextureRegion texture;
    private Vector2 position;
    private Vector2 velocity;
    private boolean active;
    private int hp;
    private Circle hitArea;
    private String name;

    private final float BASE_SIZE = 64.0f;
    private final float BASE_RADIUS = BASE_SIZE / 2.0f;

    public Vector2 getPosition() {
        return position;
    }

    public String getName() {
        return name;
    }

    public Circle getHitArea() {
        return hitArea;
    }

    @Override
    public boolean isActive() {
        return active;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void deactivate() {
        active = false;
    }

    public DropItem(GameController gc, String name) {
        this.gc = gc;
        this.position = new Vector2(0, 0);
        this.velocity = new Vector2(0, 0);
        this.hitArea = new Circle(0, 0, 0);
        this.active = false;
        this.name = name;
        this.texture = Assets.getInstance().getAtlas().findRegion(name);
        this.hp = 1;
    }

    public void activate(float x, float y, float vx) {
        this.position.set(x, y);
        this.velocity.set(vx, 0);
        this.active = true;
        this.hitArea.setPosition(position);
        this.hitArea.setRadius(BASE_RADIUS * 0.9f);
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - 32, position.y - 32, BASE_RADIUS, BASE_RADIUS, BASE_SIZE, BASE_SIZE, 1, 1, 0);
    }

    public void update(float dt) {
        position.mulAdd(velocity, dt);
        if (position.x < 0.0f || position.x > ScreenManager.SCREEN_WIDTH || position.y < 0.0f || position.y > ScreenManager.SCREEN_HEIGHT) {
            deactivate();
        }
        hitArea.setPosition(position);
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            deactivate();
        }
    }
}