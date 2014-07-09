package com.blastedstudios.velocitystack.quest.moneybag;

import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.gdxworld.world.quest.manifestation.AbstractQuestManifestation;

public class MoneyBagManifestation extends AbstractQuestManifestation{
	private static final long serialVersionUID = 1L;
	public static MoneyBagManifestation DEFAULT = new MoneyBagManifestation();
	private Vector2 position = new Vector2();
	private long amount;
	
	public MoneyBagManifestation(){}
	public MoneyBagManifestation(Vector2 position, long amount){
		this.position = position;
		this.amount = amount;
	}
	
	@Override public CompletionEnum execute() {
		for(IMoneyBag handler : PluginUtil.getPlugins(IMoneyBag.class))
			if(handler.spawnMoney(position, amount) == CompletionEnum.COMPLETED)
				return CompletionEnum.COMPLETED;
		return CompletionEnum.EXECUTING;
	}

	@Override public MoneyBagManifestation clone() {
		return new MoneyBagManifestation(position.cpy(), amount);
	}

	@Override public String toString() {
		return "[MoneyBagManifestation]";
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public void setPosition(Vector2 position) {
		this.position = position;
	}
	
	public long getAmount() {
		return amount;
	}
	
	public void setAmount(long amount) {
		this.amount = amount;
	}
}
