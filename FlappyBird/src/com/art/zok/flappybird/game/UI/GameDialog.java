package com.art.zok.flappybird.game.UI;

import com.art.zok.flappybird.game.Assets;
import com.art.zok.flappybird.game.WorldController;
import com.art.zok.flappybird.util.Constants;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class GameDialog extends Stage {
    WorldController controller;
    CustomButton    play;
    CustomButton    score;
    Button          pause;
    Image           textGameOver;
    Image           scorePanel;
    Image           medal;
    Image           tutorial;
    Image           textReady;
    ScoreActor      scoreActor;
    ScoreActor      bestScoreActor;
    
    public GameDialog(WorldController worldController) {
        super(new StretchViewport(Constants.VIEWPORT_GUI_WIDTH,
                Constants.VIEWPORT_GUI_HEIGHT));
        controller = worldController;
        init();
    }
    
    private void init() {
        // pause 锟斤拷钮
        pause = new Button(
                new TextureRegionDrawable(Assets.instance.assetUI.buttonPause),
                new TextureRegionDrawable(Assets.instance.assetUI.buttonResume),
                new TextureRegionDrawable(
                        Assets.instance.assetUI.buttonResume));
        pause.setBounds(410, 784, 50, 50);
        pause.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                    int pointer, int button) {
                return true;
            }
            
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer,
                    int button) {
                controller.setPause();
            }
        });
        
        // play 锟斤拷钮
        play = new CustomButton(Assets.instance.assetUI.buttonPlay);
        play.setBounds(43, -108, 173, 108);
        Action playMoveAction = Actions.moveTo(43, 175, 0);
        DelayAction playDelay = Actions.delay(1f);
        Action playSeq = Actions.sequence(playDelay, playMoveAction);
        play.addAction(playSeq);
        
        play.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y,
                    int pointer, int button) {
                Assets.instance.sounds.wing.play();
                return true;
            }
            
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer,
                    int button) {
                controller.reStart();
            }
        });
        
        // score 锟斤拷钮
        score = new CustomButton(Assets.instance.assetUI.buttonScore);
        score.setBounds(263, -108, 173, 108);
        Action scoreMoveAction = Actions.moveTo(263, 175, 0);
        DelayAction scoreDelay = Actions.delay(1f);
        Action scoreSeq = Actions.sequence(scoreDelay, scoreMoveAction);
        score.addAction(scoreSeq);
        
        // score 锟斤拷锟�
        scorePanel = new Image(Assets.instance.assetUI.scorePanel);
        scorePanel.setBounds(51, -215, 378, 215);
        Action scorePanelAction = Actions.moveTo(51, 320, 0.3f);
        DelayAction scorePanelDelay = Actions.delay(0.5f);
        Action scorePanRun = Actions.run(new Runnable() {
            public void run() {
                addInfo();
            }
        });
        Action scorePanelSeq = Actions.sequence(scorePanelDelay,
                scorePanelAction, scorePanRun);
        scorePanel.addAction(scorePanelSeq);
        
        // game over 锟侥憋拷
        textGameOver = new Image(Assets.instance.assetUI.textGameOver);
        textGameOver.setBounds(80, 563, 321, 80);
        Action textGameAlpha1 = Actions.alpha(0, 0);
        Action textGameAlpha2 = Actions.alpha(1, 0.3f);
        Action textGameOverMoveUp = Actions.moveTo(80, 580, 0.1f);
        Action textGameOverMoveDown = Actions.moveTo(80, 563, 0.1f);
        Action textGameOverDelay = Actions.delay(0.3f);
        Action textGameOverSeq = Actions.sequence(textGameOverMoveUp,
                textGameOverMoveDown);
        Action textGameOverPar = Actions.parallel(textGameOverSeq,
                textGameAlpha2);
        Action textGameAction = Actions.sequence(textGameAlpha1,
                textGameOverDelay, textGameOverPar);
        textGameOver.addAction(textGameAction);
        
        // tutorial
        tutorial = new Image(Assets.instance.assetUI.tutorial);
        tutorial.setBounds(134, 327, 213, 185);
        
        // get ready
        textReady = new Image(Assets.instance.assetUI.textReady);
        textReady.setBounds(89, 553, 302, 90);
        
        // init
        popTutorial();
    }
    
    public void popDiedInfomation() {
        pause.remove();
        addActor(play);
        addActor(score);
        addActor(scorePanel);
        addActor(textGameOver);
    }
    
    public void popTutorial() {
        addActor(tutorial);
        addActor(textReady);
    }
    
    public void removeTutorial() {
        addActor(pause);
        tutorial.remove();
        textReady.remove();
    }
    
    public void addInfo() {
        // 锟斤拷锟斤拷
        int medalLevel = (controller.score < 10) ? 0
                : (controller.score < 100) ? 1
                        : (controller.score < 1000) ? 2 : 3;
        medal = new Image(Assets.instance.assetUI.medals.get(medalLevel));
        medal.setBounds(93, 375, 75, 72);
        
        // 锟斤拷锟斤拷
        scoreActor = new ScoreActor(controller.score, 393, 430, false);
        
        // 锟斤拷叻锟斤拷锟�
        int bestScore = Gdx.app
                .getPreferences(Constants.PERMANENT_PRESERVATION_FILE_NAME)
                .getInteger(Constants.BEST_SCORE_KEY);
        bestScoreActor = new ScoreActor(bestScore, 393, 350, true);
        
        // 锟斤拷咏锟斤拷锟�
        addActor(medal);
        // 锟斤拷臃锟斤拷锟�
        addActor(scoreActor);
        // 锟斤拷锟斤拷锟竭成硷拷
        addActor(bestScoreActor);
    }
    
    @Override
    public void dispose() {
        controller = null;
        super.dispose();
    }
}
