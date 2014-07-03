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
	private short depth = 1;
	
	public MoneyBagManifestation(){}
	public MoneyBagManifestation(Vector2 position, long amount, short depth){
		this.position = position;
		this.amount = amount;
		this.depth = depth;
	}
	
	@Override public CompletionEnum execute() {
		for(IMoneyBag handler : PluginUtil.getPlugins(IMoneyBag.class))
			if(handler.spawnMoney(position, amount, depth) == CompletionEnum.COMPLETED)
				return CompletionEnum.COMPLETED;
		return CompletionEnum.EXECUTING;
	}

	@Override public MoneyBagManifestation clone() {
		return new MoneyBagManifestation(position.cpy(), amount, depth);
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
	
	public short getDepth() {
		return depth;
	}
	
	public void setDepth(short depth) {
		this.depth = depth;
	}
}
