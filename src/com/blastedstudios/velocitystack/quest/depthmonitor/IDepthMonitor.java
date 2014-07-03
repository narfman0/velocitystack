package com.blastedstudios.velocitystack.quest.depthmonitor;

import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public interface IDepthMonitor extends Plugin{
	public CompletionEnum setDepth(short depth);
}
