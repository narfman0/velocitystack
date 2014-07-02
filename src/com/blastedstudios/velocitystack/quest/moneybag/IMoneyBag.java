package com.blastedstudios.velocitystack.quest.moneybag;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;

import net.xeoh.plugins.base.Plugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public interface IMoneyBag extends Plugin{
	public CompletionEnum spawnMoney(Vector2 position, long amount, short depth);
}
