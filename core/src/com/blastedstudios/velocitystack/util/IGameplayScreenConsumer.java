package com.blastedstudios.velocitystack.util;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.velocitystack.ui.GameplayScreen;

@PluginImplementation
public interface IGameplayScreenConsumer extends Plugin{
	void setScreen(GameplayScreen screen);
}
