package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GameplayHUD {
	private final Label cashLabel;
	private final GameplayScreen screen;
	
	public GameplayHUD(GameplayScreen screen){
		this.screen = screen;
		Window bankWindow = new Window("Bank", screen.getSkin());
		bankWindow.add(new Label(screen.getBank()+"$", screen.getSkin()));
		bankWindow.pack();
		bankWindow.setY(Gdx.graphics.getHeight() - bankWindow.getHeight());
		screen.getStage().addActor(bankWindow);
		
		Window currentWindow = new Window("Current", screen.getSkin());
		currentWindow.add(cashLabel = new Label("---------$", screen.getSkin()));
		currentWindow.pack();
		currentWindow.setY(Gdx.graphics.getHeight() - currentWindow.getHeight() - bankWindow.getHeight());
		screen.getStage().addActor(currentWindow);
		
		float width = Math.max(currentWindow.getWidth(), bankWindow.getWidth());
		currentWindow.setWidth(width);
		bankWindow.setWidth(width);
	}
	
	public void render(float dt){
		cashLabel.setText(" " + screen.getCash()+"$");
	}
}
