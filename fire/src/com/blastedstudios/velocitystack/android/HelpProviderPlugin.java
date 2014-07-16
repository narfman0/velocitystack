package com.blastedstudios.velocitystack.android;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.velocitystack.util.IHelpProvider;

@PluginImplementation
public class HelpProviderPlugin implements IHelpProvider {
	@Override public HelpStruct getHelp() {
		return new HelpStruct("hit bottom right pedal or twist device right",
				"hit bottom middle pedal or twist device down",
				"hit bottom left pedal or twist device left");
	}
}
