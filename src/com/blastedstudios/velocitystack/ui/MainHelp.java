package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.velocitystack.util.IRemovedListener;

public class MainHelp extends Window {
	public MainHelp(final Skin skin, final IRemovedListener listener) {
		super("Help", skin);
		String[] directions = new String[]{
				"Objective: Collect cash bundles by running over them, reaching the finish line. You may quit early by " +
						"hitting the top right exit button or pressing escape, with a 50% cash penality",
				"Accelerate: hit bottom right pedal, D key, or right arrow",
				"Brake: hit bottom middle pedal, S key, space key, or down arrow",
				"Reverse: hit bottom left pedal, A key, or left arrow"
		};
		Table directionTable = new Table();
		for(String direction : directions){
			Label label = new Label(direction, skin);
			label.setAlignment(Align.center);
			label.setWrap(true);
			directionTable.add(label).width(Gdx.graphics.getWidth());
			directionTable.row();
		}
        add(directionTable);
		row();
		final TextButton backButton = new TextButton("Back", skin);
		backButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				listener.removed();
			}
		});
		add(backButton);
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
	}
}
