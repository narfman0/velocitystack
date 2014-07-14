package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.blastedstudios.gdxworld.ui.AbstractScreen;
import com.blastedstudios.gdxworld.util.GDXGame;

public class LoadScreen extends AbstractScreen {
	private int pass;
	private final Class<? extends AbstractScreen> nextScreen;
	private final Object[] args;
	
	public LoadScreen(GDXGame game, Skin skin, Class<? extends AbstractScreen> nextScreen, Object... args){
		super(game, skin);
		this.nextScreen = nextScreen;
		this.args = args;
		Window window = new Window("Loading...", skin);
		window.add(new Label("Please wait", skin));
		window.pack();
		window.setX(Gdx.graphics.getWidth()/2f - window.getWidth()/2f);
		window.setY(Gdx.graphics.getHeight()/2f - window.getHeight()/2f);
		stage.addActor(window);
	}
	
	@Override public void render(float delta){
		super.render(delta);
		stage.draw();
		pass++;
		if(pass > 1){
			//transition to next screen
			game.popScreen();
			try {
				game.pushScreen((AbstractScreen) nextScreen.getConstructors()[0].newInstance(args));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
