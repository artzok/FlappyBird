package com.art.zok.flappybird.game;

import com.art.zok.flappybird.game.object.Pipe;
import com.art.zok.flappybird.util.Constants;
import com.art.zok.flappybird.util.Tools;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.TextBounds;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {
	
	private SpriteBatch batch;
	private WorldController worldController;
	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private OrthographicCamera backGroundCamera;
	private Box2DDebugRenderer debugRenderer;
	BitmapFont font;
	public WorldRenderer(WorldController worldController) { 
		this.worldController = worldController;
		init();
	}
	
	private void init() { 
		batch = new SpriteBatch();
		debugRenderer = new Box2DDebugRenderer();
		font = Assets.instance.fonts.defaultBig;
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		cameraGUI.position.set(0, 0, 0);
		cameraGUI.setToOrtho(true); 
		cameraGUI.update();
		
		backGroundCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		backGroundCamera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
		backGroundCamera.update();
	}
	
	// ��Ļ��Ⱦ
	public void render() {
		renderBackGround(batch);	// ������Ⱦ����
		renderWorld(batch);			// Ȼ����Ⱦ����
		if(!worldController.isGameOver) {
			renderGui(batch);			// �����ȾGUI
		}
		// ��ʾ�Ի���
		worldController.gameDialog.act();
		worldController.gameDialog.draw();
		
		if(Constants.DEBUG_DRAW) {
			debugRenderer.render(worldController.world, camera.combined);
		}
	}
	
	private void renderBackGround(SpriteBatch batch) {
		batch.setProjectionMatrix(backGroundCamera.combined);
		batch.begin();
		batch.draw(worldController.background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch.end();
	}
	
	// ��Ϸ������Ⱦ����
	private void renderWorld (SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(Pipe pipe : worldController.pipes) {
			pipe.render(batch);
		}
		worldController.land.render(batch);
		worldController.bird.render(batch);
		batch.end();
	}

	// ��ϷGUI��Ⱦ����
	private void renderGui(SpriteBatch batch) {
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		renderScore(batch);
		batch.end();
		if(Constants.DEBUG_FPS)
			renderFps(batch);
	}
	
	// ����
	private void renderScore(SpriteBatch batch) {
		int[] score = Tools.splitInteger(worldController.score);
		float h = 4.5f;
		float w = 0, totalWidth = 0;
		for(int i = 0; i < score.length; i++) {
			AtlasRegion reg = Assets.instance.number.numbers_font.get(score[i]);
			w = h * reg.getRegionWidth() / reg.getRegionHeight();			
			totalWidth += w;
		}
		
		float x = -totalWidth / 2;
		float y = Constants.VIEWPORT_HEIGHT / 2 * 0.6f;
		w = 0;
		for(int i = 0; i < score.length; i++) {
			AtlasRegion reg = Assets.instance.number.numbers_font.get(score[i]);
			w = h * reg.getRegionWidth() / reg.getRegionHeight();	
			batch.draw(reg.getTexture(), x, y, w/2, h/2, w, h, 1, 1, 0, 
					reg.getRegionX(), reg.getRegionY(), 
					reg.getRegionWidth()-(i != (score.length - 1) ? 3 : 0), 
					reg.getRegionHeight(),false, false);
			x += w; 
			}
	}
	
	private void renderFps(SpriteBatch batch) {
		int fps = Gdx.graphics.getFramesPerSecond();
		batch.setProjectionMatrix(cameraGUI.combined);
		if(fps >= 45) {
			font.setColor(0, 1, 0, 1);
		} else if(fps > 30 || fps < 45) {
			font.setColor(1, 1, 0, 1);
		} else {
			font.setColor(1, 0, 0, 1);
		}
		TextBounds size = font.getBounds("FPS:"+fps);
		batch.begin();
		font.draw(batch, "FPS:" + fps, 
			cameraGUI.viewportWidth - size.width * 1.2f, cameraGUI.viewportHeight - size.height * 2);
		batch.end();
		font.setColor(1, 1, 1, 1);
	}
	
	public void resize(int width, int height) {
		// ����camera�ӿڳߴ�
		camera.viewportWidth = Constants.VIEWPORT_HEIGHT * width / height; 
		camera.update();

		// ������ϷGUI���
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = Constants.VIEWPORT_GUI_HEIGHT * (float)width / (float)height;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
	}
	
	@Override
	public void dispose() { 
		if(batch != null) {
			batch.dispose();
		}
	}
}
