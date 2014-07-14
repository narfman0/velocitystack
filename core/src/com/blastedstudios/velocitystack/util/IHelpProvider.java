package com.blastedstudios.velocitystack.util;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public interface IHelpProvider extends Plugin{
	public HelpStruct getHelp();
	
	public class HelpStruct{
		public final String accel, brake, reverse;
		
		public HelpStruct(String accel, String brake, String reverse){
			this.accel = accel;
			this.brake = brake;
			this.reverse = reverse;
		}
	}
}
