package com.blastedstudios.velocitystack.util;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public interface ICarController extends Plugin {
	boolean gas();
	boolean brake();
	boolean reverse();
}
