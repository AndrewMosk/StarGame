package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.app.game.GameController;
import com.star.app.game.WorldRenderer;
import com.star.app.screen.utils.Assets;

public class GameScreen extends AbstractScreen {
    private GameController gameController;
    private WorldRenderer worldRenderer;
    private Stage stage;
    private BitmapFont font24;
    private BitmapFont font32;

    public GameController getGameController() {
        return gameController;
    }

    public GameScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        Assets.getInstance().loadAssets(ScreenManager.ScreenType.GAME);
        this.gameController = new GameController();
        this.worldRenderer = new WorldRenderer(gameController, batch);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);

        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnPause = new TextButton("Pause", textButtonStyle);
        Button btnExitMenu = new TextButton("Exit to Menu", textButtonStyle);
        btnPause.setPosition(950, 630);
        btnExitMenu.setPosition(950, 530);

        btnPause.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (gameController.isPaused()) {
                    gameController.setPaused(false);
                }else {
                    gameController.setPaused(true);
                }
            }
        });

        btnExitMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        stage.addActor(btnPause);
        stage.addActor(btnExitMenu);
        skin.dispose();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        gameController.update(delta);
        worldRenderer.render();
        batch.begin();
        String pause;
        if (gameController.isPaused()) {
            pause = "Paused";
        }else {
            pause = "";
        }
        font32.draw(batch, pause, 0, 400, 1280, 1, false);
        batch.end();
        stage.draw();

    }

    @Override
    public void dispose() {
        gameController.dispose();
    }
}
