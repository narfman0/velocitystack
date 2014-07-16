package com.blastedstudios.velocitystack.util;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public interface IAssetChangeComponent extends Plugin {
	void change(long cash, String cars);
	void initialize(Object object);
}
