package com.art.zok.flappybird.game.UI;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class CustomButton extends Button {
	AtlasRegion region;
	public CustomButton(AtlasRegion reg) {
		super(new TextureRegionDrawable(reg));
		this.region = reg;
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(isPressed()) {
			batch.draw(region, getX(), getY() - 2f, getWidth(), getHeight());
		} else {
			super.draw(batch, parentAlpha);
		}
	}
}
