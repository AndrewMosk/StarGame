package com.star.app.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.star.app.game.helpers.ObjectPool;

public class DropItemController extends ObjectPool<DropItem> {
    private GameController gc;

    @Override
    protected DropItem newObject(String name) {
        return new DropItem(gc, name);
    }

    public DropItemController(GameController gc) {
        this.gc = gc;
    }

    public void render(SpriteBatch batch) {
        for (int i = 0; i < activeList.size(); i++) {
            DropItem a = activeList.get(i);
            a.render(batch);
        }
    }

    public void setup(float x, float y, float vx, String name) {
        getActiveElement(name).activate(x, y, vx);
    }

    public void update(float dt) {
        for (int i = 0; i < activeList.size(); i++) {
            activeList.get(i).update(dt);
        }
        checkPool();
    }
}
