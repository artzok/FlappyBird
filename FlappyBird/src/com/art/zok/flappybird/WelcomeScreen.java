package com.art.zok.flappybird;

import com.art.zok.flappybird.game.Assets;
import com.art.zok.flappybird.transitions.ScreenTransition;
import com.art.zok.flappybird.transitions.ScreenTransitionSlice;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class WelcomeScreen extends AbstractGameScreen {
    Stage        stage;
    Image        background;
    AssetManager am;
    boolean      isLoad = false;
    
    public WelcomeScreen(DirectedGame game) {
        super(game);
    }
    
    @Override
    public void show() {
        background = new Image(
                new Texture(Gdx.files.internal("images/splash.png")));
        background.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new StretchViewport(Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight()));
        stage.addActor(background);
        am = new AssetManager();
        Assets.instance.init(am);
    }
    
    private void update() {
        ScreenTransition transition = game.getScreenTransition();
        if (am.update() && !isLoad && transition == null) {
            Assets.instance.createObject();
            game.setScreen(FlappyBirdGame.startScreen,
                    ScreenTransitionSlice.init(3f, ScreenTransitionSlice.DOWN,
                            5, Interpolation.elastic));
            isLoad = true;
        }
    }
    
    @Override
    public void render(float delta) {
        update();
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        
    }
    
    @Override
    public void hide() {
    }
    
    @Override
    public void pause() {
    }
    
    @Override
    public void resume() {
    }
    
    @Override
    public void dispose() {
        stage.dispose();
        stage = null;
    }
    
    @Override
    public InputProcessor getInputProcessor() {
        // TODO Auto-generated method stub
        return null;
    }
    
}
