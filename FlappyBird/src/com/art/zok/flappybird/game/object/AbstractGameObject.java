package com.art.zok.flappybird.game.object;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public abstract class AbstractGameObject {
	
	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	public Body body;
	
	public AbstractGameObject() {
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
	}
	
	public void update(float deltaTime) {
		if(body != null) {
			position.set(body.getPosition());
			rotation = body.getAngle() * MathUtils.radiansToDegrees;
		}
	}
	
	public abstract void render(SpriteBatch batch);
}
 




















