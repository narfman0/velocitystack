package com.blastedstudios.velocitystack.quest.handlers;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.plugin.quest.manifestation.beingspawn.IBeingSpawnHandler;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.velocitystack.ui.GameplayScreen;
import com.blastedstudios.velocitystack.util.IGameplayScreenConsumer;

@PluginImplementation
public class BeingSpawnHandler implements IBeingSpawnHandler, IGameplayScreenConsumer{
	private GameplayScreen screen;
	
	@Override public void setScreen(GameplayScreen screen){
		this.screen = screen;
	}

	@Override public CompletionEnum beingSpawn(String being, Vector2 coordinates, String path) {
		screen.getStartLocation().set(coordinates);
		return CompletionEnum.COMPLETED;
	}
}
