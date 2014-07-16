package com.blastedstudios.velocitystack.util;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public interface ISaveUtility extends Plugin{
	void set(String key, String value);
	String get(String key);
}
