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
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.velocitystack.ui.MainCarTable.IBuyListener;
import com.blastedstudios.velocitystack.util.IRemovedListener;

class MainWindow extends Window{
	private final Label cashLabel;
	private final Preferences preferences = Gdx.app.getPreferences("VelocityStackPrefs");
	private Window helpWindow;
	private FileHandle carFileHandle = null;
	private long cash;
	
	public MainWindow(final Skin skin, final GDXGame game, final GDXWorld gdxWorld, 
			final File worldFile, final GDXRenderer gdxRenderer, Stage stage) {
		super("Velocity Stack", skin);
		cash = preferences.getLong("cash", 0);
		cashLabel = new Label("-----------", skin);
		rebuildUI(skin, game, gdxWorld, worldFile, gdxRenderer, stage);
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
		setMovable(false);
	}
	
	private void rebuildUI(final Skin skin, final GDXGame game, final GDXWorld gdxWorld, 
			final File worldFile, final GDXRenderer gdxRenderer, final Stage stage){
		clear();
		add(cashLabel);
		updateCashLabel();
		row();
		
		final HashMap<String, Integer> carCashMap = new HashMap<>();
		for(String carCash : Properties.get("car.cash.map", "Truck,100;Dune Buggy,7500;Monster,15000").split(";"))
			carCashMap.put(carCash.split(",")[0], Integer.parseInt(carCash.split(",")[1]));
		String owned = preferences.getString("cars.owned", "Truck");
		add(new Label("Choose car:", skin));
		row();
		FileHandle[] cars = Gdx.files.internal("data/world/cars/").list();
		Array<MainCarTable> carTableArray = new Array<>();
		IBuyListener buyListener = new IBuyListener() {
			@Override public void buy(String name, int cost) {
				preferences.putString("cars.owned", preferences.getString("cars.owned", "Truck") + "," + name);
				addCash(-cost);
				rebuildUI(skin, game, gdxWorld, worldFile, gdxRenderer, stage);
			}
		};
		for(final FileHandle carFile : cars){
			if(!carFile.extension().equals("json"))
				continue;
			final String carPretty = carFile.nameWithoutExtension().replaceAll("_", " ");
			boolean own = owned.contains(carPretty);
			if(own && carFileHandle == null)
				carFileHandle = carFile;
			carTableArray.add(new MainCarTable(skin, carPretty, carCashMap.get(carPretty), carFile, 
					own, buyListener, cash));
		}
		List<MainCarTable> carList = new List<>(skin);
		carList.setItems(carTableArray);
		add(carList);
		row();
		
		add(new Label("Choose level:", skin));
		row();
		Table levelTable = new Table();
		for(final GDXLevel level : gdxWorld.getLevels()){
			final TextButton button = new TextButton(level.getName(), skin);
			button.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					game.pushScreen(new GameplayScreen(game, skin, level, gdxRenderer, 
							worldFile, gdxWorld, carFileHandle, MainWindow.this, cash));
				}
			});
			levelTable.add(button);
		}
		add(levelTable);
		row();
		
		final TextButton exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		Table controlsTable = new Table();
		controlsTable.add(createHelpButton(skin, stage));
		controlsTable.add(exitButton);
		add(controlsTable);
	}
	
	private TextButton createHelpButton(final Skin skin, final Stage stage){
		final IRemovedListener listener = new IRemovedListener() {
			@Override public void removed() {
				if(helpWindow != null)
					helpWindow.remove();
				helpWindow = null;
			}
		};
		final TextButton controlsButton = new TextButton("Help", skin);
		controlsButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				if(helpWindow == null)
					stage.addActor(helpWindow = new MainHelp(skin, listener));
			}
		});
		return controlsButton;
	}

	public void addCash(long cashGained) {
		preferences.putLong("cash", cash += cashGained);
		updateCashLabel();
		preferences.flush();
	}

	private void updateCashLabel() {
		cashLabel.setText("Current cash: " + cash + "$");
	}
}