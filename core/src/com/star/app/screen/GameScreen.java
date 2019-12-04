package com.star.app.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.star.app.game.GameController;
import com.star.app.game.Weapon;
import com.star.app.game.WorldRenderer;
import com.star.app.screen.utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController gameController;
    private WorldRenderer worldRenderer;
    private int level;
    private int score;
    private int hp;
    private int money;
    private Weapon weapon;

    public void setScore(int score) {
        this.score = score;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public void setWeapon(Weapon weapon) {
        this.weapon = weapon;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.gameController = new GameController(batch, level, score, hp, money, weapon);
        this.worldRenderer = new WorldRenderer(gameController, batch);
    }

    @Override
    public void render(float delta) {
        gameController.update(delta);
        worldRenderer.render();
    }

    @Override
    public void dispose() {
        gameController.dispose();
    }
}
