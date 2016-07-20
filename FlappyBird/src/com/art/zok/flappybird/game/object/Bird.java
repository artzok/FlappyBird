package com.art.zok.flappybird.game.object;

import com.art.zok.flappybird.game.Assets;
import com.art.zok.flappybird.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

public class Bird extends AbstractGameObject {

	protected static final float BIRD_MAX_FLAP_ANGLE = 20; // �������Ƕ�
	protected static final float BIRD_MAX_DROP_ANGLE = -90; // �������Ƕ�
	protected static final float FLAP_ANGLE_DRAG = 9.0f; // ����Ƕ������ٶ�
	protected static final float BIRD_FLAP_ANGLE_POWER = 6.0f; // ����Ƕ������ٶ�
	protected static final float DIED_DROP_DRAG = 0.5f; // �����������ٶ�

	protected enum WAVE_STATE {
		WAVE_FALLING, WAVE_RISE
	}

	private TextureRegion currentFrame;
	private Array<AtlasRegion> birds;
	private Animation birdAnimation;
	private float animDuration;
	private boolean isContacted;
	private boolean hasBeenFlashing;
	private World world;
	private float max_wave_height;
	private float min_wave_height;
	private WAVE_STATE waveState = WAVE_STATE.WAVE_RISE;

	public Bird(int selected, World world) {
		this.world = world;
		init(selected);
	}

	// ��ʼ��
	private void init(int selected) {
		isContacted = false;
		if (selected == 1) {
			birds = Assets.instance.bird.bird1;
		} else if (selected == 2) {
			birds = Assets.instance.bird.bird2;
		} else {
			birds = Assets.instance.bird.bird0;
		}
		birdAnimation = new Animation(0.1f, birds);
		birdAnimation.setPlayMode(Animation.PlayMode.LOOP);
	}

	// ����box2d����
	private void createBody(World world) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = BodyType.DynamicBody; // must be dynamic body
		bodyDef.fixedRotation = true; 		 // fixed angle
		bodyDef.position.set(position);

		body = world.createBody(bodyDef);
		body.setUserData(this);

		PolygonShape shape = new PolygonShape();
		shape.setAsBox(dimension.x / 2, dimension.y / 2); // position is
														  // rectangle bounds
														  // center
		shape.setRadius(-0.4f);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.density = 0.1f;
		fixtureDef.friction = 0f;
		fixtureDef.shape = shape;
		body.createFixture(fixtureDef).setUserData(this);
	}
	
	//��ʼģ��
	public void startSimulate() {
		createBody(world);
	}
	
	//��ʼ�������� 
	public void initWave() {
		max_wave_height = position.y + 0.7f;
		min_wave_height = position.y - 0.7f;
	}

	
	// ������Ծ
	public void setJumping() {
		if(body != null) {
			body.setLinearVelocity(0, 35f);
			Assets.instance.sounds.wing.play();
		}
	}

	// ��ײ�����ص�����
	public void contacted() {
		isContacted = true;
		Assets.instance.sounds.hit.play();
		Assets.instance.sounds.die.play();
	}
	
	public boolean isContacted() {
		return isContacted;
	}

	@Override
	public void update(float deltaTime) {
		if (body != null && position.y > Constants.VIEWPORT_HEIGHT / 2 - dimension.y / 2) {
			body.setLinearVelocity(0, -5);
		}

		if (body == null) {
			if (waveState == WAVE_STATE.WAVE_FALLING)
				position.y -= 0.05f;
			else if (waveState == WAVE_STATE.WAVE_RISE) {
				position.y += 0.05f;
			}
			if (position.y < min_wave_height) {
				waveState = WAVE_STATE.WAVE_RISE;
			} else if (position.y > max_wave_height) {
				waveState = WAVE_STATE.WAVE_FALLING;
			}
		} else {
			super.update(deltaTime);
			// �����ٶȼ���������ת�Ƕ�
			if (body.getLinearVelocity().y < -20) {
				rotation -= BIRD_FLAP_ANGLE_POWER;
			} else {
				rotation += FLAP_ANGLE_DRAG;
			}
			// limit rotation range -20 to 90
			rotation = MathUtils.clamp(rotation, BIRD_MAX_DROP_ANGLE, BIRD_MAX_FLAP_ANGLE);
			body.setTransform(position, rotation * MathUtils.degreesToRadians);
		}
	}
	
	public boolean isGameOver() {
		return position.y <= Constants.LAND_HEIGHT - Constants.VIEWPORT_HEIGHT / 2 + dimension.y / 2;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		if (!isContacted) {
			// ��ö�����ǰ֡
			animDuration += Gdx.graphics.getDeltaTime();
			currentFrame = birdAnimation.getKeyFrame(animDuration);
		} else {
			position.y -= DIED_DROP_DRAG;
			rotation -= BIRD_FLAP_ANGLE_POWER;
			position.y = Math.max(position.y, Constants.LAND_HEIGHT -
				Constants.VIEWPORT_HEIGHT / 2 + dimension.y / 2);
			rotation = Math.max(rotation, BIRD_MAX_DROP_ANGLE);		
		}

		batch.draw(currentFrame.getTexture(), position.x - dimension.x / 2, position.y - dimension.y / 2,
				dimension.x / 2, dimension.y / 2, dimension.x, dimension.y, scale.x, scale.y, rotation,
				currentFrame.getRegionX(), currentFrame.getRegionY(), currentFrame.getRegionWidth(),
				currentFrame.getRegionHeight(), false, false);

		if (isContacted && !hasBeenFlashing) {
			hasBeenFlashing = true;
			float w = Gdx.graphics.getWidth();
			float h = Gdx.graphics.getHeight();
			batch.draw(Assets.instance.decoration.white, -w / 2, -h / 2, w, h);
		}
	}
}
