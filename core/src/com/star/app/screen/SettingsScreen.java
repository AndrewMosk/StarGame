package com.star.app.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.StringBuilder;
import com.star.app.game.Background;
import com.star.app.screen.utils.Assets;
import com.star.app.screen.utils.OptionsUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

public class SettingsScreen extends AbstractScreen {
    private Background background;
    private BitmapFont font72;
    private BitmapFont font24;
    private Stage stage;

    public SettingsScreen(SpriteBatch batch) {
        super(batch);
    }

    @Override
    public void show() {
        this.background = new Background(null);
        this.stage = new Stage(ScreenManager.getInstance().getViewport(), batch);
        this.font72 = Assets.getInstance().getAssetManager().get("fonts/font72.ttf");
        this.font24 = Assets.getInstance().getAssetManager().get("fonts/font24.ttf");

        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin();
        skin.addRegions(Assets.getInstance().getAtlas());

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.getDrawable("simpleButton");
        textButtonStyle.font = font24;

        skin.add("simpleSkin", textButtonStyle);
        //skin.add("simpleSkinText", textFieldStyle);

        // получаю текущие свойства
        Properties prop = OptionsUtils.loadProperties();
        String backward = "PLAYER1_BACKWARD";
        String right = "PLAYER1_RIGHT";
        String fire = "PLAYER1_FIRE";
        String forward = "PLAYER1_FORWARD";
        String left = "PLAYER1_LEFT";

        Button btnBackward = new TextButton("BACKWARD", textButtonStyle);
        Button btnRight = new TextButton("RIGHT", textButtonStyle);
        Button btnFire = new TextButton("FIRE", textButtonStyle);
        Button btnForward = new TextButton("FORWARD", textButtonStyle);
        Button btnLeft = new TextButton("LEFT", textButtonStyle);

        final TextButton btnBackwardValue = new TextButton(Input.Keys.toString(Integer.parseInt(prop.getProperty(backward))), textButtonStyle);
        btnBackwardValue.setWidth(150);
        final TextButton btnRightValue = new TextButton(Input.Keys.toString(Integer.parseInt(prop.getProperty(right))), textButtonStyle);
        btnRightValue.setWidth(150);
        final TextButton btnFireValue = new TextButton(Input.Keys.toString(Integer.parseInt(prop.getProperty(fire))), textButtonStyle);
        btnFireValue.setWidth(150);
        final TextButton btnForwardValue = new TextButton(Input.Keys.toString(Integer.parseInt(prop.getProperty(forward))), textButtonStyle);
        btnForwardValue.setWidth(150);
        final TextButton btnLeftValue = new TextButton(Input.Keys.toString(Integer.parseInt(prop.getProperty(left))), textButtonStyle);
        btnLeftValue.setWidth(150);

        Button btnSave = new TextButton("Save", textButtonStyle);
        Button btnReturnToMenu = new TextButton("Return to menu", textButtonStyle);

        btnBackward.setPosition(280, 580);      btnBackwardValue.setPosition(680, 580);
        btnRight.setPosition(280, 480);         btnRightValue.setPosition(680, 480);
        btnFire.setPosition(280, 380);          btnFireValue.setPosition(680, 380);
        btnForward.setPosition(280, 280);       btnForwardValue.setPosition(680, 280);
        btnLeft.setPosition(280, 180);          btnLeftValue.setPosition(680, 180);

        btnSave.setPosition(280, 80);
        btnReturnToMenu.setPosition(680, 80);

        // слушатели для кнопок управления
        btnBackward.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        btnRight.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        btnFire.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        btnForward.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });
        btnLeft.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

            }
        });


        btnSave.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Properties properties = new Properties();
                properties.put("PLAYER1_FORWARD", Input.Keys.valueOf(String.valueOf(btnForwardValue.getLabel().getText())));
                properties.put("PLAYER1_LEFT", Input.Keys.valueOf(String.valueOf(btnLeftValue.getLabel().getText())));
                properties.put("PLAYER1_RIGHT", Input.Keys.valueOf(String.valueOf(btnRightValue.getLabel().getText())));
                properties.put("PLAYER1_BACKWARD", Input.Keys.valueOf(String.valueOf(btnBackwardValue.getLabel().getText())));
                properties.put("PLAYER1_FIRE", Input.Keys.valueOf(String.valueOf(btnFireValue.getLabel().getText())));
                try {
                    properties.store(Gdx.files.local("options.properties").write(false), null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        btnReturnToMenu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().changeScreen(ScreenManager.ScreenType.MENU);
            }
        });

        stage.addActor(btnBackward);    stage.addActor(btnBackwardValue);
        stage.addActor(btnRight);       stage.addActor(btnRightValue);
        stage.addActor(btnFire);        stage.addActor(btnFireValue);
        stage.addActor(btnForward);     stage.addActor(btnForwardValue);
        stage.addActor(btnLeft);        stage.addActor(btnLeftValue);
        stage.addActor(btnSave);
        stage.addActor(btnReturnToMenu);

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
        font72.draw(batch, "Settings", 0, 900, 1280, 1, false);
        batch.end();
        stage.draw();
    }

    @Override
    public void dispose() {
        background.dispose();
    }
}
