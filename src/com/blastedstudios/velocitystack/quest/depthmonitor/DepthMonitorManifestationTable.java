package com.blastedstudios.velocitystack.quest.depthmonitor;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class DepthMonitorManifestationTable extends ManifestationTable{
	private final TextField depthField;

	public DepthMonitorManifestationTable(Skin skin, DepthMonitorManifestation manifestation) {
		super(skin);
		depthField = new TextField(manifestation.getDepth()+"", skin);
		add(new Label("Depth: ", skin));
		add(depthField);
	}

	@Override public AbstractQuestManifestation apply() {
		return new DepthMonitorManifestation(Short.parseShort(depthField.getText()));
	}
}
