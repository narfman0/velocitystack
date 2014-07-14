package com.blastedstudios.velocitystack.android;

import java.net.URI;

import net.xeoh.plugins.base.util.uri.ClassURI;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.blastedstudios.velocitystack.VelocityStack;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		//need to list each uri individually so android doesn't search forever and a day
		URI[] uris = new URI[]{
			//serializers
			ClassURI.PLUGIN(com.blastedstudios.gdxworld.plugin.serializer.serializable.JavaSerializable.class),
			ClassURI.PLUGIN(com.blastedstudios.gdxworld.plugin.serializer.xml.XMLSerializer.class),
			
			//quest manifestations
			ClassURI.PLUGIN(com.blastedstudios.velocitystack.quest.handlers.BeingSpawnHandler.class),
			ClassURI.PLUGIN(com.blastedstudios.velocitystack.quest.handlers.EndLevelHandler.class),
			ClassURI.PLUGIN(com.blastedstudios.velocitystack.quest.handlers.MoneyBagHandler.class),
			
			//zoom
			ClassURI.PLUGIN(com.blastedstudios.velocitystack.util.ZoomProvider.class),
		};
		initialize(new VelocityStack(false, uris), config);
	}
}
