package com.blastedstudios.velocitystack.quest.depthmonitor;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class DepthMonitorManifestationPlugin implements IQuestComponentManifestation{
	@Override public String getBoxText() {
		return "Depth Monitor";
	}

	@Override public ICloneable getDefault() {
		return DepthMonitorManifestation.DEFAULT;
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new DepthMonitorManifestationTable(skin, (DepthMonitorManifestation) object);
	}
}
