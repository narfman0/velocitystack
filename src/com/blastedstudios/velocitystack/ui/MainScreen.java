package com.blastedstudios.velocitystack.ui;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.GDXWorld;

public class MainScreen extends AbstractScreen {
	public static final Color WINDOW_ALPHA_COLOR = new Color(1, 1, 1, .7f);
	private static final File WORLD_FILE = Gdx.files.internal(Properties.get("world.path")).file();
	private final GDXWorld gdxWorld = GDXWorld.load(WORLD_FILE);
	private final GDXRenderer gdxRenderer;
	private MainWindow mainWindow;

	public MainScreen(final GDXGame game){
		super(game, Properties.get("screen.skin","data/ui/uiskin.json"));
		gdxRenderer = new GDXRenderer(true, true);
		stage.addActor(mainWindow = new MainWindow(skin, game, gdxWorld, WORLD_FILE, gdxRenderer));
	}

	@Override public void render(float delta){
		super.render(delta);
		stage.draw();
	}

	@Override public boolean keyDown(int key) {
		switch(key){
		case Keys.ESCAPE:
			Gdx.app.exit();
			break;
		}
		return super.keyDown(key);
	}
}
