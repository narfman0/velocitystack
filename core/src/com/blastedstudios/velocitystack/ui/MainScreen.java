package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.ui.worldeditor.WorldEditorScreen;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.ScreenLevelPanner;
import com.blastedstudios.gdxworld.world.GDXWorld;

public class MainScreen extends AbstractScreen {
	private static final FileHandle WORLD_FILE = Gdx.files.internal("data/world/world.xml");
	private final GDXWorld gdxWorld = GDXWorld.load(WORLD_FILE);
	private final GDXRenderer gdxRenderer;
	private final ScreenLevelPanner panner;

	public MainScreen(final GDXGame game, Skin skin, boolean usePanner){
		super(game, skin);
		gdxRenderer = new GDXRenderer(true, true);
		panner = usePanner ? new ScreenLevelPanner(gdxWorld, gdxRenderer) : null;
		stage.addActor(new MainWindow(skin, game, gdxWorld, WORLD_FILE, gdxRenderer, stage, panner));
	}

	@Override public void render(float delta){
		super.render(delta);
		if(panner != null)
			panner.render();
		stage.draw();
	}

	@Override public boolean keyDown(int key) {
		switch(key){
		case Keys.E:
			if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) && Gdx.input.isKeyPressed(Keys.CONTROL_LEFT))
				game.pushScreen(new WorldEditorScreen(game, gdxWorld, WORLD_FILE));
			break;
		case Keys.ESCAPE:
			Gdx.app.exit();
			break;
		}
		return super.keyDown(key);
	}
}
