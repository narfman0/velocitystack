package com.blastedstudios.velocitystack.android;

import java.net.URI;

import net.xeoh.plugins.base.util.uri.ClassURI;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.velocitystack.VelocityStack;
import com.blastedstudios.velocitystack.util.IAssetChangeComponent;
import com.blastedstudios.velocitystack.util.IZoomProvider;

public class FireLauncher extends AndroidApplication {
	@Override protected void onCreate (Bundle savedInstanceState) {
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
			
			//android specific
			ClassURI.PLUGIN(com.blastedstudios.velocitystack.android.HelpProviderPlugin.class),
			
			//fire specific
			ClassURI.PLUGIN(com.blastedstudios.velocitystack.android.WhisperSyncSaveUtility.class),
			ClassURI.PLUGIN(com.blastedstudios.velocitystack.android.HeadTrackingProvider.class),
			ClassURI.PLUGIN(com.blastedstudios.velocitystack.android.HeroWidgetComponent.class),
		};
		initialize(new VelocityStack(false, true, uris), config);
		for(IZoomProvider provider : PluginUtil.getPlugins(IZoomProvider.class))
			provider.initialize(this);
		for(IAssetChangeComponent component : PluginUtil.getPlugins(IAssetChangeComponent.class))
			component.initialize(this);
	}
}
