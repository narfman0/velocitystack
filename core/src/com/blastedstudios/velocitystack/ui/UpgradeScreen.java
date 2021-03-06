package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.ScreenLevelPanner;
import com.blastedstudios.velocitystack.util.Car;
import com.blastedstudios.velocitystack.util.IRemovedListener;
import com.blastedstudios.velocitystack.util.ISaveUtility;

public class UpgradeScreen extends AbstractScreen{
	private final Preferences preferences;
	private final Window window;
	private final String name;
	private final IRemovedListener listener;
	private final ScreenLevelPanner panner;
	
	public UpgradeScreen(final GDXGame game, Skin skin, final String name, 
			final Preferences preferences, final IRemovedListener listener, ScreenLevelPanner panner) {
		super(game, skin);
		this.name = name;
		this.preferences = preferences;
		this.listener = listener;
		this.panner = panner;
		window = new Window("Upgrade " + name, skin);
		rebuildUI(window, name, listener);
		window.pack();
		window.setX(Gdx.graphics.getWidth()/2f - window.getWidth()/2f);
		window.setY(Gdx.graphics.getHeight()/2f - window.getHeight()/2f);
		stage.addActor(window);
	}
	
	public void rebuildUI(final Window window, final String name, final IRemovedListener listener){
		window.clear();
		Label cashLabel = new Label("------", skin);
		cashLabel.setText("Current cash: " + preferences.getLong(MainWindow.CASH_PREF) + "$");
		window.add(cashLabel).colspan(3);
		window.row();
		for(final String upgrade : Car.UPGRADES){
			window.add(new Label(upgrade+": ", skin));
			final String key = name + "." + upgrade;
			final long upgradeLevel = getUpgradeLevel(key, preferences);
			final Label level = new Label(upgradeLevel+" ", skin);
			window.add(level);
			final TextButton upgradeButton = new TextButton("+ " + levelCost(upgradeLevel) + "$", skin);
			upgradeButton.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					addCash(-levelCost(upgradeLevel));
					long levelLong = preferences.getLong(key) + 1;
					level.setText(levelLong+"");
					preferences.putLong(name + "." + upgrade, levelLong);
					preferences.flush();
					for(ISaveUtility saveUtility : PluginUtil.getPlugins(ISaveUtility.class))
						saveUtility.set(key, levelLong+"");
					rebuildUI(window, name, listener);
				}
			});
			if(levelCost(upgradeLevel) > preferences.getLong(MainWindow.CASH_PREF))
				upgradeButton.setTouchable(Touchable.disabled);
			window.add(upgradeButton);
			window.row();
		}
		final TextButton okButton = new TextButton("OK", skin);
		okButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				game.popScreen();
				listener.removed();
			}
		});
		window.add(okButton).colspan(3);
	}
	
	private long getUpgradeLevel(String name, Preferences prefs){
		long upgrade = preferences.getLong(name, 0);
		for(ISaveUtility saveUtility : PluginUtil.getPlugins(ISaveUtility.class))
			try{
				upgrade = Long.parseLong(saveUtility.get(name));
			}catch(Exception e){
				Gdx.app.error("UpgradeScreen.getUpgradeLevel", e.getMessage());
			}
		return upgrade;
	}
	
	private long levelCost(long level){
		return Properties.getLong("upgrade.cost.scalar", 500) * (level+1);
	}

	private void addCash(long cashGained) {
		preferences.putLong(MainWindow.CASH_PREF, preferences.getLong(MainWindow.CASH_PREF) + cashGained);
		preferences.flush();
		for(ISaveUtility saveUtility : PluginUtil.getPlugins(ISaveUtility.class))
			saveUtility.set(MainWindow.CASH_PREF, preferences.getLong(MainWindow.CASH_PREF)+"");
		rebuildUI(window, name, listener);
	}

	@Override public void render(float delta){
		super.render(delta);
		if(panner != null)
			panner.render();
		stage.draw();
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
}
