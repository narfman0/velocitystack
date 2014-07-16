package com.blastedstudios.velocitystack.ui;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.util.ScreenLevelPanner;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.velocitystack.ui.MainCarTable.ICarTableListener;
import com.blastedstudios.velocitystack.ui.MainCarTable.MainCarTableCostComparator;
import com.blastedstudios.velocitystack.util.Car;
import com.blastedstudios.velocitystack.util.IRemovedListener;
import com.blastedstudios.velocitystack.util.ISaveUtility;

class MainWindow extends Window{
	public static final String CARS_OWNED_PREF = "cars.owned", CASH_PREF = "cash";
	private final Label cashLabel;
	private final Preferences preferences = Gdx.app.getPreferences("VelocityStackPrefs");
	private final ScreenLevelPanner panner;
	private final Skin skin;
	private final GDXGame game;
	private final GDXWorld gdxWorld; 
	private final FileHandle worldFile;
	private final GDXRenderer gdxRenderer;
	private final boolean horizontal;
	
	public MainWindow(final Skin skin, final GDXGame game, final GDXWorld gdxWorld, 
			final FileHandle worldFile, final GDXRenderer gdxRenderer, ScreenLevelPanner panner,
			boolean horizontal) {
		super("Velocity Stack", skin);
		this.skin = skin;
		this.game = game;
		this.gdxWorld = gdxWorld;
		this.worldFile = worldFile;
		this.gdxRenderer = gdxRenderer;
		this.panner = panner;
		this.horizontal = horizontal;
		initializePreferences();
		cashLabel = new Label("-----------", skin);
		rebuildUI("Truck", 0);
		repack();
		setMovable(false);
	}
	
	private void initializePreferences(){
		preferences.putString(CARS_OWNED_PREF, preferences.getString(CARS_OWNED_PREF, "Truck"));
		for(ISaveUtility saveUtility : PluginUtil.getPlugins(ISaveUtility.class)){
			try{
				String cars = saveUtility.get(CARS_OWNED_PREF);
				if(cars.length() > preferences.getString(CARS_OWNED_PREF, "Truck").length())
					preferences.putString(CARS_OWNED_PREF, cars);
			}catch(Exception e){
				Gdx.app.error("MainWindow.<init>", e.getMessage());
			}
		}
		preferences.putLong(CASH_PREF, preferences.getLong(CASH_PREF, 0));
		for(ISaveUtility saveUtility : PluginUtil.getPlugins(ISaveUtility.class)){
			long saveLong = 0;
			try{
				saveLong = Long.parseLong(saveUtility.get(CASH_PREF));
			}catch(Exception e){
				Gdx.app.error("MainWindow.<init>", e.getMessage());
			}
			preferences.putLong(CASH_PREF, Math.max(preferences.getLong(CASH_PREF,  0), saveLong));
		}
		preferences.flush();
	}
	
	private void rebuildUI(final String activeCar, int currentLevelIndex){
		clear();
		add(cashLabel);
		updateCashLabel();
		row();
		final List<LevelNameContainer> levelList = new List<>(skin);
		final List<MainCarTable> carList = new List<>(skin);
		Table container = new Table();
		container.add(createCarTable(levelList, carList, activeCar));
		if(!horizontal)
			container.row();
		container.add(createLevelTable(levelList, carList, currentLevelIndex));
		if(!horizontal)
			container.row();
		container.add(createControlsTable());
		add(container);
	}
	
	private Table createCarTable(final List<LevelNameContainer> levelList, final List<MainCarTable> carList, String activeCar){
		final HashMap<String, Integer> carCashMap = new HashMap<>();
		for(String carCash : Properties.get("car.cash.map", "Truck,0;Dune Buggy,2500;Monster,5000").split(";"))
			carCashMap.put(carCash.split(",")[0], Integer.parseInt(carCash.split(",")[1]));
		final String carsOwned = preferences.getString(CARS_OWNED_PREF);
		FileHandle[] cars = Gdx.files.internal("data/world/cars/").list();
		ICarTableListener buyListener = new ICarTableListener() {
			@Override public void buy(String name, int cost) {
				if(cost <= preferences.getLong(CASH_PREF)){
					String newCarsOwned = carsOwned + "," + name;
					preferences.putString(CARS_OWNED_PREF, newCarsOwned);
					for(ISaveUtility saveUtility : PluginUtil.getPlugins(ISaveUtility.class))
						saveUtility.set(CARS_OWNED_PREF, newCarsOwned);
					addCash(-cost);
					rebuildUI(carList.getSelected().name, levelList.getSelectedIndex());
				}
			}
			@Override public void upgrade(final String name) {
				IRemovedListener listener = new IRemovedListener() {
					@Override public void removed() {
						rebuildUI(carList.getSelected().name, levelList.getSelectedIndex());
					}
				};
				game.pushScreen(new UpgradeScreen(game, skin, name, preferences, listener, panner));
			}
		};
		MainCarTable[] carTableArray = new MainCarTable[cars.length/2];
		int i=0;
		for(final FileHandle carFile : cars){
			if(!carFile.extension().equals("xml"))
				continue;
			final String carPretty = Car.carHandleToName(carFile);
			carTableArray[i++] = new MainCarTable(skin, carPretty, carCashMap.get(carPretty), carFile, 
					carsOwned.contains(carPretty), buyListener, preferences.getLong(CASH_PREF));
		}
		Arrays.sort(carTableArray, new MainCarTableCostComparator());
		carList.setItems(new Array<>(carTableArray));
		final Table table = new Table(), carTable = new Table();
		carList.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				carTable.clear();
				carTable.add(carList.getSelected());
				repack();
			}
		});
		table.add(new Label("Choose car:", skin));
		table.row();
		table.add(carList);
		table.row();
		for(i=0; i<carTableArray.length; i++)
			if(carTableArray[i].name.equals(activeCar))
				carList.setSelectedIndex(i);
		carTable.add(carList.getSelected());
		table.add(carTable);
		return table;
	}
	
	private Table createControlsTable(){
		Table table = new Table();
		final TextButton volumeButton = new TextButton(getVolumeLabel(), skin);
		volumeButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				preferences.putInteger("volume", (preferences.getInteger("volume", 5)+1) % 11);
				preferences.flush();
				volumeButton.setText(getVolumeLabel());
			}
		});
		table.add(volumeButton);
		if(horizontal)
			table.row();
		final TextButton helpButton = new TextButton("Help", skin);
		helpButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				game.pushScreen(new HelpScreen(game, skin));
			}
		});
		table.add(helpButton);
		if(horizontal)
			table.row();
		final TextButton exitButton = new TextButton("Exit", skin);
		exitButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		table.add(exitButton);
		return table;
	}
	
	private Table createLevelTable(final List<LevelNameContainer> levelList, final List<MainCarTable> carList, int currentLevelIndex){
		Table table = new Table();
		table.add(new Label("Choose level:", skin));
		table.row();
		LevelNameContainer[] levelNameContainerArray = new LevelNameContainer[gdxWorld.getLevels().size()];
		for(int j=0; j<gdxWorld.getLevels().size(); j++)
			levelNameContainerArray[j] = new LevelNameContainer(gdxWorld.getLevels().get(j).getName(), gdxWorld.getLevels().get(j),
					Long.parseLong(gdxWorld.getLevels().get(j).getProperties().get("Cost")));
		Arrays.sort(levelNameContainerArray, new LevelNameContainerCostComparator());
		levelList.setItems(new Array<>(levelNameContainerArray));
		levelList.setSelectedIndex(currentLevelIndex);
		table.add(levelList);
		table.row();
		final Table startBuyLevelTable = new Table();
		final TextButton startButton = new TextButton("Start", skin);
		final String levelsOwned = preferences.getString("levels.owned", "Plains");
		levelList.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				startBuyLevelTable.clear();
				if(levelsOwned.contains(levelList.getSelected().name))
					startBuyLevelTable.add(startButton);
				else{
					final long cost = levelList.getSelected().cost;
					TextButton buyLevelButton = new TextButton("Buy for " + cost + "$", skin);
					buyLevelButton.addListener(new ClickListener() {
						@Override public void clicked(InputEvent event, float x, float y) {
							if(cost <= preferences.getLong(CASH_PREF)){
								preferences.putString("levels.owned", levelsOwned + "," + levelList.getSelected().name);
								addCash(-cost);
								rebuildUI(carList.getSelected().name, levelList.getSelectedIndex());
							}
						}
					});
					startBuyLevelTable.add(buyLevelButton);
				}
			}
		});
		startButton.addListener(new ClickListener() {
			@Override public void clicked(InputEvent event, float x, float y) {
				if(preferences.getString(CARS_OWNED_PREF).contains(carList.getSelected().name))
					game.pushScreen(new LoadScreen(game, skin, GameplayScreen.class, 
							game, skin, levelList.getSelected().level, gdxRenderer, 
							worldFile, gdxWorld, carList.getSelected().handle, MainWindow.this,
							preferences.getLong(CASH_PREF), preferences));
			}
		});
		if(levelsOwned.contains(levelList.getSelected().name))
			startBuyLevelTable.add(startButton);
		else{
			final long cost = levelList.getSelected().cost;
			TextButton buyLevelButton = new TextButton("Buy for " + cost + "$", skin);
			buyLevelButton.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					if(cost <= preferences.getLong(CASH_PREF)){
						preferences.putString("levels.owned", levelsOwned + "," + levelList.getSelected().name);
						addCash(-cost);
						rebuildUI(carList.getSelected().name, levelList.getSelectedIndex());
					}
				}
			});
			startBuyLevelTable.add(buyLevelButton);
		}
		table.add(startBuyLevelTable);
		return table;
	}
	
	private void repack(){
		pack();
		setX(Gdx.graphics.getWidth()/2 - getWidth()/2);
		setY(Gdx.graphics.getHeight()/2 - getHeight()/2);
	}

	public void addCash(long cashGained) {
		preferences.putLong(CASH_PREF, preferences.getLong(CASH_PREF) + cashGained);
		preferences.flush();
		for(ISaveUtility saveUtility : PluginUtil.getPlugins(ISaveUtility.class))
			saveUtility.set(CASH_PREF, preferences.getLong(CASH_PREF)+"");
		updateCashLabel();
	}
	
	private String getVolumeLabel(){
		return "Volume: " + preferences.getInteger("volume", 5);
	}

	private void updateCashLabel() {
		cashLabel.setText("Current cash: " + preferences.getLong(CASH_PREF) + "$");
	}
	
	private class LevelNameContainer{
		public final String name;
		public final GDXLevel level;
		public final long cost;
		
		LevelNameContainer(String name, GDXLevel level, long cost){
			this.name = name;
			this.level = level;
			this.cost = cost;
		}
		
		@Override public String toString(){
			return name;
		}
	}
	
	public class LevelNameContainerCostComparator implements Comparator<LevelNameContainer>{
		@Override public int compare(LevelNameContainer o1, LevelNameContainer o2) {
			return ((Long)o1.cost).compareTo(o2.cost);
		}
		
	}
}