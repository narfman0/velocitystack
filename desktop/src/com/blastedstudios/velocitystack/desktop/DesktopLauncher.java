package com.blastedstudios.velocitystack.desktop;

import net.xeoh.plugins.base.util.uri.ClassURI;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.blastedstudios.gdxworld.GDXWorldEditor;
import com.blastedstudios.velocitystack.VelocityStack;

public class DesktopLauncher {
	public static void main (String[] arg) {
		new LwjglApplication(new VelocityStack(true, ClassURI.CLASSPATH), GDXWorldEditor.generateConfiguration("Velocity Stack"));
	}
}
