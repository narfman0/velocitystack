package com.blastedstudios.velocitystack.ui;

import java.io.File;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.ui.leveleditor.LevelEditorScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.TiledMeshRenderer;
import com.blastedstudios.gdxworld.world.GDXLevel;
import com.blastedstudios.gdxworld.world.GDXLevel.CreateLevelReturnStruct;
import com.blastedstudios.gdxworld.world.GDXWorld;
import com.blastedstudios.gdxworld.world.quest.GDXQuestManager;
import com.blastedstudios.gdxworld.world.shape.GDXShape;
import com.blastedstudios.velocitystack.VelocityStack;
import com.blastedstudios.velocitystack.quest.QuestManifestationExecutor;
import com.blastedstudios.velocitystack.quest.QuestTriggerInformationProvider;
import com.blastedstudios.velocitystack.util.Car;
import com.blastedstudios.velocitystack.util.ContactListener;
import com.blastedstudios.velocitystack.util.IRemovedListener;
import com.blastedstudios.velocitystack.util.IRenderComponent;
import com.blastedstudios.velocitystack.util.IZoomProvider;

public class GameplayScreen extends AbstractScreen {
	private final GDXLevel level;
	private final World world = new World(new Vector2(0, -10), true);
	private final OrthographicCamera camera;
	private final GDXRenderer gdxRenderer;
	private final CreateLevelReturnStruct createLevelStruct;
	private final Box2DDebugRenderer renderer = new Box2DDebugRenderer();
	private final TiledMeshRenderer tiledMeshRenderer;
	private final GDXQuestManager questManager = new GDXQuestManager();
	private final File selectedFile;
	private final GDXWorld gdxWorld;
	private final Car car;
	private final SpriteBatch spriteBatch = new SpriteBatch();
	private final GameplayHUD hud;
	private final MainWindow mainWindow;
	private final long bank;
	private GameplayMenuWindow gameplayMenu;
	private long cash;
	
	public GameplayScreen(GDXGame game, Skin skin, GDXLevel level, final GDXRenderer gdxRenderer, 
			File selectedFile, GDXWorld gdxWorld, FileHandle carFileHandle, MainWindow mainWindow, long bank) {
		super(game, skin);
		this.level = level;
		this.gdxRenderer = gdxRenderer;
		this.selectedFile = selectedFile;
		this.gdxWorld = gdxWorld;
		this.mainWindow = mainWindow;
		this.bank = bank;
		this.cash = 0;
		world.setContactListener(new ContactListener(this));
		hud = new GameplayHUD(this);
		car = new Car(world, new Vector2(), carFileHandle, gdxRenderer);
		
		createLevelStruct = level.createLevel(world);
		camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.zoom = VelocityStack.SPRITE_SCALE;
		questManager.initialize(new QuestTriggerInformationProvider(this), new QuestManifestationExecutor(this));
		questManager.setCurrentLevel(level);
		questManager.tick();//to get "start" quest to set respawn location
		tiledMeshRenderer = new TiledMeshRenderer(gdxRenderer, level.getPolygons());
	}
	
	@Override public void render(float dt){
		super.render(dt);
		if(Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.RIGHT) || hud.isGas())
			car.gas(true, false);
		else if(Gdx.input.isKeyPressed(Keys.A) || Gdx.input.isKeyPressed(Keys.LEFT) || hud.isReverse())
			car.gas(true, true);
		else
			car.gas(false, false);
		car.brake(Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.SPACE) || 
				Gdx.input.isKeyPressed(Keys.DOWN) || hud.isBrake());
		
		world.step(Math.min(1f/20f, dt), 10, 10);
//		Vector2 cameraOffset = calculateCameraVelocityOffset(car.getVelocity());
//		Vector2 cameraTarget = car.getPosition().cpy().add(cameraOffset);
		camera.position.set(car.getPosition(), 0);
		for(IZoomProvider provider : PluginUtil.getPlugins(IZoomProvider.class))
			camera.zoom = provider.getZoom(this);
		camera.update();
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		gdxRenderer.render(spriteBatch, level, camera, createLevelStruct.bodies.entrySet());
		for(Entry<GDXShape,Body> entry : createLevelStruct.bodies.entrySet())
			gdxRenderer.drawShape(camera, entry.getKey(), entry.getValue(), spriteBatch);
		spriteBatch.end();
		tiledMeshRenderer.render(camera);
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		car.render(dt, spriteBatch);
		for(IRenderComponent component : PluginUtil.getPlugins(IRenderComponent.class))
			component.render(dt, this, spriteBatch, car);
		spriteBatch.end();
		renderer.render(world, camera.combined);
		hud.render(dt);
		stage.draw();
		for(Body body : getBodiesIterable())
			if(body != null && body.getUserData() != null && 
					body.getUserData().equals(ContactListener.REMOVE_USER_DATA))
				world.destroyBody(body);
	}
	
	public Vector2 calculateCameraVelocityOffset(Vector2 velocity){
		return new Vector2((float)Math.min(Math.cbrt(velocity.x), Gdx.graphics.getWidth()/2f - 16), 
				(float)Math.min(Math.cbrt(velocity.y), Gdx.graphics.getHeight()/2f - 16));
	}
	
	public Iterable<Body> getBodiesIterable(){
		Array<Body> bodyArray = new Array<>(world.getBodyCount());
		world.getBodies(bodyArray);
		return bodyArray;
	}

	public World getWorld() {
		return world;
	}

	public Vector2 getPlayerPosition(){
		return car.getPosition();
	}

	@Override public boolean keyDown(int key) {
		switch(key){
		case Keys.E:
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				game.pushScreen(new LevelEditorScreen(game, gdxWorld, selectedFile, level));
			break;
		case Keys.ESCAPE:
			showGameplayMenu();
			break;
		}
		return false;
	}
	
	public void showGameplayMenu(){
		if(gameplayMenu == null){
			IRemovedListener listener = new IRemovedListener() {
				@Override public void removed() {
					gameplayMenu = null;
				}
			};
			stage.addActor(gameplayMenu = new GameplayMenuWindow(this, getSkin(), listener));
		}
	}

	public void receiveMoney(long amount) {
		cash += amount;
	}

	public long getCash() {
		return cash;
	}

	public void exit(boolean early) {
		long cashGained = early ? cash/2 : cash;
		mainWindow.levelComplete(cashGained);
		game.popScreen();
	}

	public Car getCar() {
		return car;
	}

	public long getBank() {
		return bank;
	}
}
