package com.blastedstudios.velocitystack;

import java.net.URI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.velocitystack.ui.LoadScreen;
import com.blastedstudios.velocitystack.ui.MainScreen;

public class VelocityStack extends GDXGame {
	private final boolean usePanner;
	private final URI[] uris;
	
	public VelocityStack(boolean usePanner, URI... uris){
		this.usePanner = usePanner;
		this.uris = uris;
	}
	
	@Override public void create () {
		PluginUtil.initialize(uris);
		Skin skin = new Skin(Gdx.files.internal(Properties.get("screen.skin","data/ui/uiskinGame.json")));
		pushScreen(new LoadScreen(this, skin, MainScreen.class, this, skin, usePanner));
	}
}
