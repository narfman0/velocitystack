package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameplayEndLevelWindow extends Window {
	public GameplayEndLevelWindow(Skin skin, final GameplayScreen screen, boolean early, long cash) {
		super("End level", skin);
		Label finishLabel = new Label("You have finished the level " + 
				(early?"early with penalty.":"successfully!"), skin);
		finishLabel.setAlignment(Align.center);
		finishLabel.setWrap(true);
		add(finishLabel).width(Gdx.graphics.getWidth()/3f);
		row();
		Label cashLabel = new Label("You have earned: " + cash + "$", skin);
		cashLabel.setAlignment(Align.center);
		cashLabel.setWrap(true);
		add(cashLabel).width(Gdx.graphics.getWidth()/3f);
		row();
		final TextButton exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				screen.getGame().popScreen();
			}
		});
		add(exitButton);
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
	}

}
