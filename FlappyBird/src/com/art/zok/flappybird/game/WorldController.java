package com.art.zok.flappybird.game;

import com.art.zok.flappybird.FlappyBirdScreen;
import com.art.zok.flappybird.game.UI.GameDialog;
import com.art.zok.flappybird.game.object.AbstractGameObject;
import com.art.zok.flappybird.game.object.Bird;
import com.art.zok.flappybird.game.object.Land;
import com.art.zok.flappybird.game.object.Pipe;
import com.art.zok.flappybird.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class WorldController extends InputAdapter {
	private static final String TAG = WorldController.class.getName();

	public FlappyBirdScreen screen;			// main clas object
	
	public Bird bird; 				// player character
	public Land land; 				// only one land game object
	private Pipe pipe; 				// last add pipe game object
	public Array<Pipe> pipes; 		// all pipes game object
	public AtlasRegion background; 	// random background texture

	public World world; 			// only one box2d world
	float viewWidth; 				// last view port width (because view height is constant
									// but width is automatic adjustment according screen
									// size)
	public int score;				// player current score
	public boolean isStart;			// denote game start
	public boolean isGameOver; 		// denote game over
	public GameDialog gameDialog;	// often show information dialog（such pause button、
									// tutorial、die information、restart button）
	private Preferences prefs;				// reserve permanently
	public InputMultiplexer multiplexer;   // multiple inputer
	public WorldController(FlappyBirdScreen flappybird) {
		this.screen = flappybird;
		viewWidth = Constants.VIEWPORT_HEIGHT * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		prefs = Gdx.app.getPreferences(Constants.PERMANENT_PRESERVATION_FILE_NAME);  
		init();
	}

	public void init() {
		score = 0;
		isStart = false;
		isGameOver = false;
		screen.paused = false;
		if(gameDialog != null) gameDialog.dispose();
		gameDialog = new GameDialog(this);
		background = Assets.instance.decoration.bg.random();
		
		initWorld();
		initBird();
		initLand();
		initPipe();
		
		// multiple inputs object (include dialog and world control)
		multiplexer = new InputMultiplexer(gameDialog, this);
		Gdx.input.setInputProcessor(multiplexer);
	}
	
	// create bo2d world
	private void initWorld() {
		if (world != null) world.dispose();
		world = new World(new Vector2(0, -120f), true);
		
		// add collision listener
		world.setContactListener(new CollisionListener());
	}

	// initialization Bird
	private void initBird() {
		bird = new Bird((int) (Math.random() * 3), world);
		bird.dimension.set(3.72f, 2.64f);
		bird.position.set(-bird.dimension.x * 1.5f, bird.dimension.y / 2);
		bird.initWave();
	}

	// initialization Land
	private void initLand() {
		land = new Land(world);
	}

	// initialization Pipe
	private void initPipe() {
		pipes = new Array<Pipe>();
		addPipe();
	}

	// helper method
	private void addPipe() {
		pipe = new Pipe();
		pipe.position.set(viewWidth, Constants.LAND_HEIGHT - Constants.VIEWPORT_HEIGHT / 2);
		pipe.dimension.set(Constants.PIPE_WIDTH, Constants.VIEWPORT_HEIGHT - Constants.LAND_HEIGHT);
		pipe.origin.set(pipe.dimension.x / 2, pipe.dimension.y / 2);
		pipe.createBody(world);
		pipes.add(pipe);
	}

	// helper method
	private void removePipe() {
		if (pipes != null && pipes.size > 1) {
			pipes.removeIndex(0);
		}
	}
	
	// test and change pipe number
	private void testPipeAmountTooLarge(int amount) {
		if (pipes != null && pipes.size > amount) {
			removePipe();
		}
	}
	
	// add pipe if necessary
	private void wrapPipe() {
		if (pipe.position.x <= viewWidth - Constants.PIPE_DISTANCE) {
			addPipe();
		}
	}

	// calculate Score
	private void calculateScore () {
		for(Pipe pipe : pipes) {
			if(pipe.position.x < bird.position.x) {
				score += pipe.getScore();
			}
		}
	}
	
	// save best score
	private void saveBestScore() {
		int bestScore = prefs.getInteger(Constants.BEST_SCORE_KEY);
		if(bestScore < score) {
			prefs.putInteger(Constants.BEST_SCORE_KEY, score);
			prefs.flush();
		}
	}
	
	// dialog operation
	public void setPause() {
		if(screen.paused) {
			screen.paused = false;
		} else {
			screen.paused = true;
		}
	}
	
	// restart
	public void reStart() {
		init();
	}
	
	// game update
	public void update(float deltaTime) {
		if(!isGameOver) {
			if(isGameOver = bird.isGameOver()) {
				saveBestScore();				// save best score
				gameDialog.popDiedInfomation(); // Pop-up game over information
				Gdx.app.debug(TAG, "Game Over!");
			}
			if (bird.isContacted()) return;		// if game is not over but happen collision
		} else {
			return ;
		}
		
		wrapPipe(); 					// translate pipe if necessary 
		
		if(isStart) {						
			world.step(deltaTime, 3, 3);	// box2d simulation
		}
		
		bird.update(deltaTime); 		// 更新角色
		for (Pipe pipe : pipes) {		// 更新管道	
			pipe.update(deltaTime); 
		} 	
		land.update(deltaTime); 		// 跟新地面
		
		testPipeAmountTooLarge(6);		// 5(view_witdh) / 1(pipe_width) + 1() = 6				
		calculateScore();				// 计算分数
	}
	
	// handle game object collision event
	private void collisionDetection(AbstractGameObject a, AbstractGameObject b) {
		if (a instanceof Bird) {
			((Bird) a).contacted();
		} else if (b instanceof Bird) {
			((Bird) b).contacted();							// if game over
		}													// 1. don't handle game control input
		Gdx.app.debug(TAG, "Player Character Contected!");  // 2. don't handle game update
															// 3. Popup dialog
	}
	
	// game input handle
	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		if (bird.isContacted()) return true;
		if (button == Buttons.LEFT) {
			if(!isStart) {
				isStart = true;
				bird.startSimulate();
				land.StartSimulate();			// 開始仿真模擬
				gameDialog.removeTutorial();	// 移除教程并添加暂停按钮
			}
			bird.setJumping();
		}
		return true;
	}

	private class CollisionListener implements ContactListener {
		public void preSolve(Contact arg0, Manifold arg1) {}
		public void postSolve(Contact arg0, ContactImpulse arg1) {}
		public void endContact(Contact arg0) {}
		public void beginContact(Contact arg0) {
			AbstractGameObject a = (AbstractGameObject) arg0.getFixtureA().getBody().getUserData();
			AbstractGameObject b = (AbstractGameObject) arg0.getFixtureB().getBody().getUserData();
			collisionDetection(a, b);
		}
	}
	
	public void dispose() {
		if(world != null) {
			world.dispose();
			world = null;
		} 
		if(gameDialog != null) {
			gameDialog.dispose();
			gameDialog = null;
		}
	}
	
}
