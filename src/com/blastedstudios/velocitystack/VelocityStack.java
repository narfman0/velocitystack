package com.blastedstudios.velocitystack;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.blastedstudios.gdxworld.GDXWorldEditor;
import com.blastedstudios.gdxworld.util.GDXGame;
import com.blastedstudios.velocitystack.ui.MainScreen;

public class VelocityStack extends GDXGame {
	@Override public void create () {
		pushScreen(new MainScreen(this));
	}
	
	public static void main (String[] argv) {
		new LwjglApplication(new VelocityStack(), GDXWorldEditor.generateConfiguration("Velocity Stack"));
	}
}
