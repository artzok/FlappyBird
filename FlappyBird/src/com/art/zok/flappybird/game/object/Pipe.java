package com.art.zok.flappybird.game.object;

import com.art.zok.flappybird.game.Assets;
import com.art.zok.flappybird.util.Constants;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Pipe extends AbstractGameObject {
	public static final float MIN_PIPE_HEIGHT = 5.27f;
	public static final float CHANNEL_HEIGHT = 10.56f;
	
	private AtlasRegion pipe;
	private float pipeHeight;
	private boolean isCollected;
	public Pipe() {
		isCollected = false;
		pipe = Assets.instance.pipe.pipeDownGreen;
	}
	
	public void createBody(World world) {
		pipeHeight = MathUtils.random(dimension.y - 
				2 * MIN_PIPE_HEIGHT - CHANNEL_HEIGHT) + MIN_PIPE_HEIGHT;
		
		// down
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;
		bodyDef.position.set(position);
		Body b = world.createBody(bodyDef);
		
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(dimension.x / 2, pipeHeight / 2,
				new Vector2(dimension.x / 2, pipeHeight / 2), 0);
		shape.setRadius(0.1f);
		FixtureDef fixtureDefDown = new FixtureDef();
		fixtureDefDown.shape = shape;
		b.createFixture(fixtureDefDown);
		b.setLinearVelocity(-15, 0);
		body = b;
		
		// up
		bodyDef.position.set(position.x, position.y + pipeHeight + CHANNEL_HEIGHT);
		b = world.createBody(bodyDef);
		shape.setAsBox(dimension.x / 2, (dimension.y - pipeHeight - CHANNEL_HEIGHT) / 2, 
				      new Vector2(dimension.x / 2, (dimension.y - pipeHeight - CHANNEL_HEIGHT) / 2), 0);
		shape.setRadius(0.1f);
		FixtureDef fixtureDefUp = new FixtureDef();
		fixtureDefUp.shape = shape;
		b.setLinearVelocity(-15, 0);
		b.createFixture(fixtureDefUp);
	}
	
	public int getScore() {
		if(isCollected) return 0;
		else {
			isCollected = true;
			Assets.instance.sounds.point.play();
			return 1;
		}
	}
	
	@Override
	public void render(SpriteBatch batch) {
		
		// down
		batch.draw(pipe.getTexture(), position.x, position.y + pipeHeight - Constants.VIEWPORT_HEIGHT/1.5f,
				origin.x, origin.y, dimension.x, Constants.VIEWPORT_HEIGHT/1.5f, scale.x, scale.y,
				rotation, pipe.getRegionX(), pipe.getRegionY(), pipe.getRegionWidth(), 
				pipe.getRegionHeight(), false, true);
		
		// up
		batch.draw(pipe.getTexture(), position.x, position.y + pipeHeight + CHANNEL_HEIGHT,
				origin.x, origin.y, dimension.x, Constants.VIEWPORT_HEIGHT/1.5f,
				scale.x, scale.y, rotation, pipe.getRegionX(), pipe.getRegionY(),
				pipe.getRegionWidth(), pipe.getRegionHeight(), false, false);
	}
	
	
}
