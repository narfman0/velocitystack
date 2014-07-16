package com.blastedstudios.velocitystack.util;

import com.blastedstudios.velocitystack.ui.GameplayScreen;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class ZoomProvider implements IZoomProvider{
	public static final float MAX_ZOOM = .08f;
	
	@Override public float getZoom(GameplayScreen screen) {
		return Math.min(MAX_ZOOM, GameplayScreen.SPRITE_SCALE + screen.getCar().getVelocity().len()/1000f);
	}

	@Override public void initialize(Object object) {}
}
