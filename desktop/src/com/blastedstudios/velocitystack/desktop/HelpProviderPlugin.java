package com.blastedstudios.velocitystack.desktop;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.velocitystack.util.IHelpProvider;

@PluginImplementation
public class HelpProviderPlugin implements IHelpProvider{
	@Override public HelpStruct getHelp() {
		return new HelpStruct("D key, or right arrow", "S key, space key, or down arrow", "A key, or left arrow");
	}
}
