package com.star.app.screen;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Align;
import com.star.app.game.*;
import com.star.app.screen.utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController gameController;
    private WorldRenderer worldRenderer;
    private int level;
    private int score;
    private int hp;
    private int money;
    private Weapon weapon;
    private Hero.Skill[] skills;
    private Shop shop;
    private BitmapFont font24;

    public void setShop(Shop shop) {
        this.shop = shop;
    }

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

    public void setSkills(Hero.Skill[] skills) {
        this.skills = skills;
    }

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.gameController = new GameController(batch, level, score, hp, money, weapon, skills, shop, this);
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
