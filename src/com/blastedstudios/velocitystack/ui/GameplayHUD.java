package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class GameplayHUD {
	private final Label cashLabel;
	private final GameplayScreen screen;
	
	public GameplayHUD(GameplayScreen screen){
		this.screen = screen;
		Window cashWindow = new Window("", screen.getSkin());
		cashWindow.add(cashLabel = new Label("$", screen.getSkin()));
		cashWindow.pack();
		cashWindow.setY(Gdx.graphics.getHeight() - cashWindow.getHeight());
		screen.getStage().addActor(cashWindow);
	}
	
	public void render(float dt){
		cashLabel.setText(screen.getCash()+"$");
	}
}
