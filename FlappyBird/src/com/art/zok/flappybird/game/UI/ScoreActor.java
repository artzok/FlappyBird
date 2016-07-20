package com.art.zok.flappybird.game.UI;

import com.art.zok.flappybird.game.Assets;
import com.art.zok.flappybird.util.Tools;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;

public class ScoreActor extends Actor {
	private int score;
	private Array<AtlasRegion> allRegs;
	private float duration;
	private int curScore;
	private int[] curScores;
	private boolean isStatic;
	private float x, y;
	public ScoreActor(int i, float x, float y, boolean isSatic) {
		this.score = i;
		this.x = x;
		this.y = y;
		this.isStatic = isSatic;
		allRegs = Assets.instance.number.numbers_font;
		curScores = Tools.splitInteger(i);
	}
	
	public void update () {
		duration += Gdx.graphics.getDeltaTime();
		if(duration > 0.1f) {
			duration = 0;
			curScore += 1;
			if(curScore > score) {
				return;
			} else {
				curScores = Tools.splitInteger(curScore);
			}
		}
		
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		if(!isStatic) {
			update();
		}
		float w = 0, all = 0;
		for(int i = curScores.length - 1; i >= 0; i--) {
			AtlasRegion reg = allRegs.get(curScores[i]);
			w = 45 * reg.getRegionWidth() / reg.getRegionHeight();	
			all += w;
			batch.draw(reg, x - all, y, w, 45);
		}
		
	}
}
