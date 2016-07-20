package com.art.zok.flappybird;

import com.art.zok.flappybird.game.Assets;
import com.art.zok.flappybird.transitions.ScreenTransitionFade;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;

public class FlappyBirdGame extends DirectedGame {
    
    public static WelcomeScreen    welcomeScreen;
    public static StartScreen      startScreen;
    public static FlappyBirdScreen flappyBirdScreen;
    
    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        
        welcomeScreen = new WelcomeScreen(this);
        startScreen = new StartScreen(this);
        flappyBirdScreen = new FlappyBirdScreen(this);
        
        setScreen(welcomeScreen, ScreenTransitionFade.init(1f));
    }
    
    @Override
    public void dispose() {
        super.dispose();
        
        flappyBirdScreen.dispose();
        flappyBirdScreen = null;
        
        startScreen.dispose();
        startScreen = null;
        
        welcomeScreen.dispose();
        welcomeScreen = null;
        
        Assets.instance.dispose();
    }
}
