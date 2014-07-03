package com.blastedstudios.velocitystack.quest.handlers;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.blastedstudios.gdxworld.plugin.quest.manifestation.endlevel.IEndLevelHandler;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.velocitystack.ui.GameplayScreen;
import com.blastedstudios.velocitystack.util.IGameplayScreenConsumer;

@PluginImplementation
public class EndLevelHandler implements IEndLevelHandler, IGameplayScreenConsumer {
	private GameplayScreen screen;
	
	@Override public void setScreen(GameplayScreen screen){
		this.screen = screen;
	}
	
	@Override public CompletionEnum endLevel(boolean success) {
		screen.exit(false);
		return CompletionEnum.COMPLETED;
	}
}
