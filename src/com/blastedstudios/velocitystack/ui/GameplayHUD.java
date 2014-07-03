package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.blastedstudios.gdxworld.util.Properties;

public class GameplayHUD {
	private static final long HUD_UP_TIME = Properties.getLong("money.hud.up.time", 2000),
			HUD_FADE_TIME = Properties.getLong("money.hud.fade.time", 2000);
	private final Label cashLabel;
	private final GameplayScreen screen;
	private final Window bankWindow, currentWindow;
	private long lastCash, timeLastCash;
	
	public GameplayHUD(GameplayScreen screen){
		this.screen = screen;
		bankWindow = new Window("Bank", screen.getSkin());
		bankWindow.add(new Label(screen.getBank()+"$", screen.getSkin()));
		bankWindow.pack();
		bankWindow.setY(Gdx.graphics.getHeight() - bankWindow.getHeight());
		screen.getStage().addActor(bankWindow);
		
		currentWindow = new Window("Current", screen.getSkin());
		currentWindow.add(cashLabel = new Label("---------$", screen.getSkin()));
		cashLabel.setText((lastCash = screen.getCash()) + "$");
		currentWindow.pack();
		currentWindow.setY(Gdx.graphics.getHeight() - currentWindow.getHeight() - bankWindow.getHeight());
		screen.getStage().addActor(currentWindow);
		
		float width = Math.max(currentWindow.getWidth(), bankWindow.getWidth());
		currentWindow.setWidth(width);
		bankWindow.setWidth(width);
		timeLastCash = System.currentTimeMillis();
	}
	
	public void render(float dt){
		if(lastCash != screen.getCash()){
			cashLabel.setText((lastCash = screen.getCash()) + "$");
			timeLastCash = System.currentTimeMillis();
		}
		
		Color newColor = bankWindow.getColor();
		newColor.a = 1f;
		if(System.currentTimeMillis() - timeLastCash < HUD_UP_TIME)
			newColor.a = 1f;
		else{
			long timePastHUDUp = System.currentTimeMillis() - timeLastCash - HUD_UP_TIME;
			if(timePastHUDUp > HUD_FADE_TIME)
				newColor.a = 0f;
			else
				newColor.a = MathUtils.lerp(1f, 0f, ((float)timePastHUDUp)/((float)HUD_FADE_TIME));
		}
		bankWindow.setColor(newColor);
		currentWindow.setColor(newColor);
			
	}
}
