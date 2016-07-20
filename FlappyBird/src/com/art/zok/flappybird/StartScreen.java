package com.art.zok.flappybird;

import com.art.zok.flappybird.game.Assets;
import com.art.zok.flappybird.game.UI.CustomButton;
import com.art.zok.flappybird.game.object.Bird;
import com.art.zok.flappybird.game.object.Land;
import com.art.zok.flappybird.transitions.ScreenTransitionSlide;
import com.art.zok.flappybird.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class StartScreen extends AbstractGameScreen {
    
    Bird               bird;              // bird object
    Land               land;              // land object
    Stage              stage;             // stage
    Image              title;             // text title image
    Image              copyRight;         // text copyright image
    float              viewWidth;         // last viewport width
    SpriteBatch        batch;             // sprite render batch
    AtlasRegion        background;        // random background texture region
    OrthographicCamera camera;            // 正交投影相机
    CustomButton       play, score, rate; // three buttons
    
    public StartScreen(DirectedGame game) {
        super(game);
    }
    
    @Override
	public void show() {
		batch = new SpriteBatch();
		viewWidth = Constants.VIEWPORT_HEIGHT * Gdx.graphics.getWidth() / Gdx.graphics.getHeight(); 
		
		// bird and land
		bird = new Bird((int) (Math.random()*3), null);
		bird.dimension.set(3.72f, 2.64f);
		bird.position.set(0,2f);
		bird.initWave();
		land = new Land(null);
		
		// camera
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		
		// stage
		stage = new Stage(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,Constants.VIEWPORT_GUI_HEIGHT));
		
		// background texture region
		background = Assets.instance.decoration.bg.random();
		
		// title image
		title = new Image(Assets.instance.assetUI.textTitle);
		title.setBounds(93, 553, 295, 90);
		
		// copyright image
		copyRight = new Image(Assets.instance.assetUI.copyRight);
		copyRight.setBounds(137, 110, 206, 21);
		
		// three buttons
		play = new CustomButton(Assets.instance.assetUI.buttonPlay);
		play.setBounds(43, 175, 173, 108);
		
		score = new CustomButton(Assets.instance.assetUI.buttonScore);
		score.setBounds(263, 175, 173, 108);
		
		rate = new CustomButton(Assets.instance.assetUI.buttonRate);
		rate.setBounds(189, 313, 102, 65);
		
		stage.addActor(title);
		stage.addActor(copyRight);
		stage.addActor(play);
		stage.addActor(score);
		stage.addActor(rate);
		
		//input handler
//		Gdx.input.setInputProcessor(stage);
		
		// touch up event (touchDown must be return true)
		play.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
			    Assets.instance.sounds.wing.play();
				return true;
			};
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
			    game.setScreen(FlappyBirdGame.flappyBirdScreen, ScreenTransitionSlide.init(2f,
		                ScreenTransitionSlide.LEFT, false, Interpolation.elasticOut));
			}
		});
	}
    
    private void update(float deltaTime) {
        bird.update(deltaTime); // wave animation
        land.update(deltaTime); // move animation
    }
    
    @Override
    public void render(float delta) {
        update(Gdx.graphics.getDeltaTime());
        
        Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        
        batch.draw(background, -viewWidth / 2, -Constants.VIEWPORT_HEIGHT / 2,
                viewWidth, Constants.VIEWPORT_HEIGHT);
        bird.render(batch);
        land.render(batch);
        
        batch.end();
        
        stage.act();
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = Constants.VIEWPORT_HEIGHT * width / height;
        camera.update();
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
        if (batch != null) {
            batch.dispose();
            batch = null;
        }
        if (stage != null) {
            stage.dispose();
            stage = null;
        }
    }
    
    @Override
    public InputProcessor getInputProcessor() {
        // TODO Auto-generated method stub
        return stage;
    }
}
