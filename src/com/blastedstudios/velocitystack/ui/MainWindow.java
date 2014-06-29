package com.blastedstudios.velocitystack.ui;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;

class MainWindow extends Window{
	public MainWindow(final Skin skin, final GDXGame game, 
			final GDXWorld gdxWorld, final File worldFile, final GDXRenderer gdxRenderer) {
		super("", skin);
		setColor(MainScreen.WINDOW_ALPHA_COLOR);
		final TextButton exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		add(new Label("Velocity Stack", skin));
		row();
		add(new Label("Choose level:", skin));
		row();
		for(final GDXLevel level : gdxWorld.getLevels()){
			final TextButton button = new TextButton(level.getName(), skin);
			button.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					game.pushScreen(new GameplayScreen(game, skin, level, gdxRenderer, worldFile, gdxWorld));
				}
			});
			add(button);
			row();
		}
		add(exitButton);
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
	}
}