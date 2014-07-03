package com.blastedstudios.velocitystack.quest.depthmonitor;

import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class DepthMonitorManifestation extends AbstractQuestManifestation{
	private static final long serialVersionUID = 1L;
	public static DepthMonitorManifestation DEFAULT = new DepthMonitorManifestation();
	private short depth = 1;
	
	public DepthMonitorManifestation(){}
	public DepthMonitorManifestation(short depth){
		this.depth = depth;
	}
	
	@Override public CompletionEnum execute() {
		for(IDepthMonitor handler : PluginUtil.getPlugins(IDepthMonitor.class))
			if(handler.setDepth(depth) == CompletionEnum.COMPLETED)
				return CompletionEnum.COMPLETED;
		return CompletionEnum.EXECUTING;
	}

	@Override public DepthMonitorManifestation clone() {
		return new DepthMonitorManifestation(depth);
	}

	@Override public String toString() {
		return "[DepthMonitorManifestation]";
	}
	
	public short getDepth() {
		return depth;
	}
	
	public void setDepth(short depth) {
		this.depth = depth;
	}
}
