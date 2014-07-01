package com.blastedstudios.velocitystack.quest.moneybag;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.blastedstudios.gdxworld.plugin.mode.quest.IQuestComponent.IQuestComponentManifestation;
import com.blastedstudios.gdxworld.world.quest.ICloneable;

@PluginImplementation
public class MoneyBagManifestationPlugin implements IQuestComponentManifestation{
	@Override public String getBoxText() {
		return "Money Bag";
	}

	@Override public ICloneable getDefault() {
		return MoneyBagManifestation.DEFAULT;
	}

	@Override public Table createTable(Skin skin, Object object) {
		return new MoneyBagManifestationTable(skin, (MoneyBagManifestation) object);
	}
}