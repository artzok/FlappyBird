package com.art.zok.flappybird.game.object;

import com.art.zok.flappybird.game.Assets;
import com.art.zok.flappybird.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Land extends AbstractGameObject {
	private static final float LAND_VELOCITY = -15; 	// �㶨������15m/s���ٶ��ƶ�
	
	float viewWidth;
	TextureRegion land;
	private World world;
	private float initPosX;

	public Land(World world) {
		this.world = world; // box2d����
		land = Assets.instance.land.land;

		viewWidth = Constants.VIEWPORT_HEIGHT * Gdx.graphics.getWidth() / Gdx.graphics.getHeight();
		
		initPosX = -viewWidth / 2;
		dimension.set(viewWidth, Constants.LAND_HEIGHT);
		position.set(initPosX, -Constants.VIEWPORT_HEIGHT / 2);
		
	}

	public void createBody(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.KinematicBody;			// �˶�����
		bodyDef.position.set(position);					// ��ʼλ��
		body = world.createBody(bodyDef);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(dimension.x * 1.5f, dimension.y / 2, 
			new Vector2(dimension.x * 1.5f, dimension.y / 2), 0);	// ���α߽�
		shape.setRadius(-0.1f);								        // ��ֹ��ײ��������
		
		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef);
		body.setLinearVelocity(LAND_VELOCITY, 0);
	}
	
	public void wrapLand() {
		if (position.x <= initPosX - viewWidth + 0.4f) {
			if (body != null) {
				body.setTransform(initPosX, -Constants.VIEWPORT_HEIGHT / 2, 0);
			} else {
				position.x = initPosX;
			}
		}
	}

	@Override
	public void update(float deltaTime) {
		wrapLand(); // ת��land
		super.update(deltaTime); // ����Ӧ��body����
		if (body == null) { // ���û��body�����Զ�����
			position.x += LAND_VELOCITY * deltaTime;
		}
	}

	public void StartSimulate() {
		initPosX = position.x;
		createBody(world);
	}

	@Override
	public void render(SpriteBatch batch) {
		batch.draw(land.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
				scale.y, rotation, land.getRegionX(), land.getRegionY(), land.getRegionWidth(), land.getRegionHeight(),
				false, false);
		batch.draw(land.getTexture(), position.x + viewWidth, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
				scale.y, rotation, land.getRegionX(), land.getRegionY(), land.getRegionWidth(), land.getRegionHeight(),
				false, false);
		batch.draw(land.getTexture(), position.x + 2 * viewWidth, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x,
				scale.y, rotation, land.getRegionX(), land.getRegionY(), land.getRegionWidth(), land.getRegionHeight(),
				false, false);
	}
}
