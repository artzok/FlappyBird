package com.art.zok.flappybird;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;


public class Main {
	private static boolean rebuildAtlas = false;
	private static boolean drawDebugOutline = false;
	
	public static void main(String[] args) {
		if(rebuildAtlas) {
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = true;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images", 
					"../FlappyBird-android/assets/images",
					"flappy-bird.pack");
		}
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "FlappyBird";
		cfg.width = 394;
		cfg.height = 700;
		cfg.resizable = false;
		cfg.addIcon("images/ic_launcher.png", FileType.Internal);
		new LwjglApplication(new FlappyBirdGame(), cfg);
	}

}
