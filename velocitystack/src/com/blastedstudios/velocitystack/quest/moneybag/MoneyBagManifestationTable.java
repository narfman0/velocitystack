package com.blastedstudios.velocitystack.quest.moneybag;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.blastedstudios.gdxworld.plugin.mode.quest.ManifestationTable;
import com.blastedstudios.gdxworld.ui.leveleditor.VertexTable;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class MoneyBagManifestationTable extends ManifestationTable{
	private final VertexTable positionTable;
	private final TextField amountField;

	public MoneyBagManifestationTable(Skin skin, MoneyBagManifestation manifestation) {
		super(skin);
		positionTable = new VertexTable(manifestation.getPosition(), skin);
		amountField = new TextField(manifestation.getAmount()+"", skin);
		add(new Label("Position: ", skin));
		add(positionTable);
		row();
		add(new Label("Amount: ", skin));
		add(amountField);
	}

	@Override public AbstractQuestManifestation apply() {
		return new MoneyBagManifestation(positionTable.getVertex(), Long.parseLong(amountField.getText()));
	}
	
	@Override public void touched(Vector2 pos){
		positionTable.setVertex(pos.x, pos.y);
	}
}
