package com.blastedstudios.velocitystack.android;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.velocitystack.util.IHelpProvider;

@PluginImplementation
public class HelpProviderPlugin implements IHelpProvider {
	@Override public HelpStruct getHelp() {
		return new HelpStruct("hit bottom right pedal", "hit bottom middle pedal", "hit bottom left pedal");
	}
}
