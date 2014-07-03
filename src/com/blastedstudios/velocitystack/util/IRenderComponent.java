package com.blastedstudios.velocitystack.util;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.blastedstudios.velocitystack.ui.GameplayScreen;

@PluginImplementation
public interface IRenderComponent extends Plugin {
	void render(float dt, GameplayScreen screen, Batch batch, Car car);
}
