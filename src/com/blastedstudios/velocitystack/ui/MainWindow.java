package com.blastedstudios.velocitystack.ui;

import java.io.File;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.ScreenLevelPanner;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.velocitystack.ui.MainCarTable.ICarTableListener;
import com.blastedstudios.velocitystack.util.Car;
import com.blastedstudios.velocitystack.util.IRemovedListener;

class MainWindow extends Window{
	private final Label cashLabel;
	private final Preferences preferences = Gdx.app.getPreferences("VelocityStackPrefs");
	private final ScreenLevelPanner panner;
	
	public MainWindow(final Skin skin, final GDXGame game, final GDXWorld gdxWorld, 
			final File worldFile, final GDXRenderer gdxRenderer, Stage stage, ScreenLevelPanner panner) {
		super("Velocity Stack", skin);
		this.panner = panner;
		preferences.getLong("cash", 0);//set default if not existing
		cashLabel = new Label("-----------", skin);
		rebuildUI(skin, game, gdxWorld, worldFile, gdxRenderer, stage, "Truck");
		repack();
		setMovable(false);
	}
	
	private void rebuildUI(final Skin skin, final GDXGame game, final GDXWorld gdxWorld, 
			final File worldFile, final GDXRenderer gdxRenderer, final Stage stage, final String activeCar){
		clear();
		add(cashLabel);
		updateCashLabel();
		row();
		Table levelTable = new Table();
		
		final HashMap<String, Integer> carCashMap = new HashMap<>();
		for(String carCash : Properties.get("car.cash.map", "Truck,100;Dune Buggy,7500;Monster,25000").split(";"))
			carCashMap.put(carCash.split(",")[0], Integer.parseInt(carCash.split(",")[1]));
		final String owned = preferences.getString("cars.owned", "Truck");
		add(new Label("Choose car:", skin));
		row();
		FileHandle[] cars = Gdx.files.internal("data/world/cars/").list();
		Array<MainCarTable> carTableArray = new Array<>();
		ICarTableListener buyListener = new ICarTableListener() {
			@Override public void buy(String name, int cost) {
				preferences.putString("cars.owned", preferences.getString("cars.owned", "Truck") + "," + name);
				addCash(-cost);
				rebuildUI(skin, game, gdxWorld, worldFile, gdxRenderer, stage, name);
			}
			@Override public void upgrade(final String name) {
				IRemovedListener listener = new IRemovedListener() {
					@Override public void removed() {
						rebuildUI(skin, game, gdxWorld, worldFile, gdxRenderer, stage, name);
					}
				};
				game.pushScreen(new UpgradeScreen(game, skin, name, preferences, listener, panner));
			}
		};
		int selectedIndex = 0, i=0;
		for(final FileHandle carFile : cars){
			if(!carFile.extension().equals("json"))
				continue;
			final String carPretty = Car.carHandleToName(carFile);
			if(carPretty.equals(activeCar))
				selectedIndex = i;
			else i++;
			carTableArray.add(new MainCarTable(skin, carPretty, carCashMap.get(carPretty), carFile, 
					owned.contains(carPretty), buyListener, preferences.getLong("cash")));
		}
		final List<MainCarTable> carList = new List<>(skin);
		carList.setItems(carTableArray);
		final Table carTable = new Table();
		carList.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				carTable.clear();
				carTable.add(carList.getSelected());
				repack();
			}
		});
		add(carList);
		carList.setSelectedIndex(selectedIndex);
		carTable.add(carList.getSelected());
		row();
		add(carTable);
		row();
		
		add(new Label("Choose level:", skin));
		row();
		for(final GDXLevel level : gdxWorld.getLevels()){
			final TextButton button = new TextButton(level.getName(), skin);
			button.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					if(owned.contains(carList.getSelected().name))
						game.pushScreen(new GameplayScreen(game, skin, level, gdxRenderer, 
								worldFile, gdxWorld, carList.getSelected().handle, MainWindow.this,
								preferences.getLong("cash"), preferences));
				}
			});
			levelTable.add(button);
		}
		add(levelTable);
		row();
		
		Table controlsTable = new Table();
		final TextButton helpButton = new TextButton("Help", skin);
		helpButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				game.pushScreen(new HelpScreen(game, skin));
			}
		});
		controlsTable.add(helpButton);
		final TextButton exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		controlsTable.add(exitButton);
		add(controlsTable);
	}
	
	private void repack(){
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
	}

	public void addCash(long cashGained) {
		preferences.putLong("cash", preferences.getLong("cash") + cashGained);
		preferences.flush();
		updateCashLabel();
	}

	private void updateCashLabel() {
		cashLabel.setText("Current cash: " + preferences.getLong("cash") + "$");
	}
}