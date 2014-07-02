package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class GameplayMenuWindow extends Window{
	public GameplayMenuWindow(final GameplayScreen screen, Skin skin, final IRemovedListener listener) {
		super("Pause", skin);
		final TextButton okButton = new TextButton("OK", skin);
		okButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				screen.exit(true);
				listener.removed();
			}
		});
		final TextButton cancelButton = new TextButton("Cancel", skin);
		cancelButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				GameplayMenuWindow.this.remove();
				listener.removed();
			}
		});
		add(new Label("Are you sure you want to quit? You will lose half of what you gained in the level!", skin)).colspan(2);
		row();
		add(okButton);
		add(cancelButton);
		pack();
		setX(Gdx.graphics.getWidth()/2f - getWidth()/2f);
		setY(Gdx.graphics.getHeight()/2f - getHeight()/2f);
	}
	
	interface IRemovedListener{
		void removed();
	}
}
