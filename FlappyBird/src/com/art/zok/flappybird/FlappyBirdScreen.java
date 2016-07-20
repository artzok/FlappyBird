package com.art.zok.flappybird;

import com.art.zok.flappybird.game.WorldController;
import com.art.zok.flappybird.game.WorldRenderer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;

public class FlappyBirdScreen extends AbstractGameScreen {

	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	public boolean paused;
	
	
	public FlappyBirdScreen(DirectedGame game) {
	    super(game);
	}
	
	@Override
	public void show() {
		// 初始化控制器和渲染器
		worldController = new WorldController(this);
		worldRenderer = new WorldRenderer(worldController);
		// 启动时激活游戏
		paused = false;
	}
	
	@Override
	public void render(float delta) {
		// 当游戏暂停时不更新游戏世界
		if(!paused) {
			// 根据最后一帧到当前帧的增量时间更新游戏世界
			float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f/60);
			worldController.update(deltaTime);
		}
		
		// 设置清屏颜色为：浅蓝色  清屏
		Gdx.gl.glClearColor(1.0f,1.0f, 1.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// 将游戏世界渲染到屏幕上
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}


	@Override
	public void hide() {
		
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public void dispose() {
		if(worldRenderer != null) {
			worldRenderer.dispose();
			worldRenderer = null;
		}
		if(worldController != null) {
			worldController.dispose();
			worldController = null;
		}
	}


    @Override
    public InputProcessor getInputProcessor() {
        // TODO Auto-generated method stub
        return worldController.multiplexer;
    }
}
