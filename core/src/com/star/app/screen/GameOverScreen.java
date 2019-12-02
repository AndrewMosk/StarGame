package com.star.app.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.star.app.game.Background;
import com.star.app.game.GameController;
import com.star.app.game.Hero;
import com.star.app.screen.utils.Assets;
import com.star.app.screen.utils.OptionsUtils;

public class GameOverScreen extends AbstractScreen {
    private Background background;
    private BitmapFont font72;
    private BitmapFont font32;
    private BitmapFont font24;
    private Stage stage;
    private GameScreen gameScreen;

    public GameOverScreen(SpriteBatch batch, GameScreen gameScreen) {
        super(batch);
        this.gameScreen = gameScreen;
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font32 = Assets.getInstance().getAssetManager().get("fonts/font32.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;
        skin.add("simpleSkin", textButtonStyle);

        Button btnRestartGame = new TextButton("Restart Game", textButtonStyle);
        Button btnExitGame = new TextButton("Exit Game", textButtonStyle);
        btnRestartGame.setPosition(480, 210);
        btnExitGame.setPosition(480, 110);

        btnRestartGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.GAME);
            }
        });

        btnExitGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        stage.addActor(btnRestartGame);
        stage.addActor(btnExitGame);
        skin.dispose();

        if (!OptionsUtils.isOptionsExists()) {
            OptionsUtils.createDefaultProperties();
        }
    }

    public void update(float dt) {
        background.update(dt);
        stage.act(dt);
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        background.render(batch);
        font72.draw(batch, "GAME OVER", 0, 600, 1280, 1, false);
        font32.draw(batch, "SCORE: " + gameScreen.getGameController().getHero().getScore(), 0, 400, 1280, 1, false);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
