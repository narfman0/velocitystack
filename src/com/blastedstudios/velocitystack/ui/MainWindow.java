package com.blastedstudios.velocitystack.ui;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;

class MainWindow extends Window{
	private final Label cashLabel;
	private final Preferences preferences = Gdx.app.getPreferences("VelocityStackPrefs");
	private FileHandle carFileHandle = null;
	private long cash;
	
	public MainWindow(final Skin skin, final GDXGame game, final GDXWorld gdxWorld, 
			final File worldFile, final GDXRenderer gdxRenderer) {
		super("Velocity Stack", skin);
		setColor(MainScreen.WINDOW_ALPHA_COLOR);
		final TextButton exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		cash = preferences.getLong("cash", 0);
		add(cashLabel = new Label("-----------", skin));
		updateCashLabel();
		row();
		add(new Label("Choose car:", skin));
		row();
		Table carTable = new Table(), levelTable = new Table();
		FileHandle[] cars = Gdx.files.internal("data/world/cars/").list();
		carFileHandle = cars[0];
		for(final FileHandle carFile : cars){
			final TextButton button = new TextButton(carFile.nameWithoutExtension(), skin);
			button.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					carFileHandle = carFile;
				}
			});
			carTable.add(button);
		}
		add(carTable);
		row();
		add(new Label("Choose level:", skin));
		row();
		for(final GDXLevel level : gdxWorld.getLevels()){
			final TextButton button = new TextButton(level.getName(), skin);
			button.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					game.pushScreen(new GameplayScreen(game, skin, level, gdxRenderer, 
							worldFile, gdxWorld, carFileHandle, MainWindow.this));
				}
			});
			levelTable.add(button);
		}
		add(levelTable);
		row();
		add(exitButton);
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
	}

	public void levelComplete(long cashGained) {
		preferences.putLong("cash", cash += cashGained);
		updateCashLabel();
		preferences.flush();
	}

	private void updateCashLabel() {
		cashLabel.setText("Current cash: " + cash + "$");
	}
}