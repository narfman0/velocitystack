package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.velocitystack.util.IHelpProvider;
import com.blastedstudios.velocitystack.util.IHelpProvider.HelpStruct;

public class HelpScreen extends AbstractScreen{
	public HelpScreen(final GDXGame game, Skin skin) {
		super(game, skin);
		HelpStruct current = new HelpStruct("D key, or right arrow", "S key, space key, or down arrow", "A key, or left arrow");
		for(IHelpProvider provider : PluginUtil.getPlugins(IHelpProvider.class))
			current = provider.getHelp();
		String[] directions = new String[]{
				"Objective: Collect cash bundles by running over them, reaching the finish line. You may quit early by " +
						"hitting the top right exit button or pressing escape, with a 50% cash penality",
				"Accelerate: " + current.accel,//hit bottom right pedal, D key, or right arrow",
				"Brake: " + current.brake,//hit bottom middle pedal, S key, space key, or down arrow",
				"Reverse: " + current.reverse,//hit bottom left pedal, A key, or left arrow"
		};
		Table directionTable = new Table();
		for(String direction : directions){
			Label label = new Label(direction, skin);
			label.setAlignment(Align.center);
			label.setWrap(true);
			directionTable.add(label).width(Gdx.graphics.getWidth());
			directionTable.row();
		}
		Window window = new Window("Help", skin);
		window.add(directionTable);
		window.row();
		final TextButton backButton = new TextButton("Back", skin);
		backButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				game.popScreen();
			}
		});
		window.add(backButton);
		window.pack();
		window.setX(Gdx.graphics.getWidth()/2 - window.getWidth()/2);
		window.setY(Gdx.graphics.getHeight()/2 - window.getHeight()/2);
		window.setMovable(false);
		stage.addActor(window);
	}
	
	@Override public boolean keyDown(int key) {
		switch(key){
		case Keys.BACK:
		case Keys.ESCAPE:
			game.popScreen();
			break;
		}
		return false;
	}

	@Override public void render(float delta){
		super.render(delta);
		stage.draw();
	}
}
