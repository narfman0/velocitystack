package com.blastedstudios.velocitystack.quest;

import java.util.Iterator;
import java.util.LinkedList;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.velocitystack.VelocityStack;
import com.blastedstudios.velocitystack.quest.moneybag.IMoneyBag;
import com.blastedstudios.velocitystack.ui.GameplayScreen;

@PluginImplementation
public class MoneyBagHandler implements IMoneyBag {
	private GameplayScreen screen;
	private LinkedList<MoneyBag> moneyBags = new LinkedList<>();
	
	public void setGameplayScreen(GameplayScreen screen){
		this.screen = screen;
		moneyBags.clear();
	}
	
	public void render(float dt, Vector2 position, Batch batch){
		for(Iterator<MoneyBag> iter = moneyBags.iterator(); iter.hasNext();){
			MoneyBag bag = iter.next();
			if(bag.position.dst2(position) < Properties.getFloat("money.pickup.distance", 2f)){
				iter.remove();
				screen.receiveMoney(bag.amount);
			}
			bag.sprite.draw(batch);
		}
	}

	@Override public CompletionEnum spawnMoney(Vector2 position, long amount) {
		moneyBags.add(new MoneyBag(position, amount));
		return CompletionEnum.COMPLETED;
	}
	
	public class MoneyBag{
		public final Vector2 position;
		public final long amount;
		public final Sprite sprite;
		
		public MoneyBag(Vector2 position, long amount){
			this.position = position;
			this.amount = amount;
			sprite = new Sprite(new Texture(Gdx.files.internal("data/textures/moneyBag.png")));
			sprite.setPosition(position.x - sprite.getWidth()/2f, position.y - sprite.getHeight()/2f);
			sprite.setScale(VelocityStack.SPRITE_SCALE);
		}
	}
}
