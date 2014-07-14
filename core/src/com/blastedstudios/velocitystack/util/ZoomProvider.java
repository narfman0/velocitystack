package com.blastedstudios.velocitystack.util;

import com.blastedstudios.velocitystack.ui.GameplayScreen;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class ZoomProvider implements IZoomProvider{
	@Override public float getZoom(GameplayScreen screen) {
		return Math.min(.08f, GameplayScreen.SPRITE_SCALE + screen.getCar().getVelocity().len()/1000f);
	}
}
