package com.blastedstudios.velocitystack.quest;

import java.util.Iterator;
import java.util.LinkedList;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.blastedstudios.gdxworld.physics.PhysicsHelper;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.quest.QuestStatus.CompletionEnum;
import com.blastedstudios.velocitystack.ContactListener;
import com.blastedstudios.velocitystack.VelocityStack;
import com.blastedstudios.velocitystack.quest.moneybag.IMoneyBag;
import com.blastedstudios.velocitystack.ui.GameplayScreen;

@PluginImplementation
public class MoneyBagHandler implements IMoneyBag, IGameplayScreenConsumer {
	private GameplayScreen screen;
	private LinkedList<MoneyBag> moneyBags = new LinkedList<>();
	private float dt;
	
	@Override public void setScreen(GameplayScreen screen){
		this.screen = screen;
		moneyBags.clear();
	}
	
	public void render(float dt, Batch batch, short depth){
		for(Iterator<MoneyBag> iter = moneyBags.iterator(); iter.hasNext();){
			MoneyBag bag = iter.next();
			bag.sprite.setAlpha(depth == bag.depth ? 1f : GameplayScreen.SPRITE_DEPTH_ALPHA);
			bag.sprite.draw(batch);
			bag.sprite.setRotation(30f*(float)Math.sin(this.dt*5f));
			if(ContactListener.REMOVE_USER_DATA.equals(bag.body.getUserData()))
				iter.remove();
		}
		this.dt += dt;
	}

	@Override public CompletionEnum spawnMoney(Vector2 position, long amount, short depth) {
		moneyBags.add(new MoneyBag(position, amount, depth));
		return CompletionEnum.COMPLETED;
	}
	
	public class MoneyBag{
		public final Vector2 position;
		public final long amount;
		public final short depth;
		public final Sprite sprite;
		public final Body body;
		
		public MoneyBag(Vector2 position, long amount, short depth){
			this.position = position;
			this.amount = amount;
			this.depth = depth;
			sprite = new Sprite(new Texture(Gdx.files.internal("data/textures/moneyBag.png")));
			sprite.setPosition(position.x - sprite.getWidth()/2f, position.y - sprite.getHeight()/2f);
			sprite.setScale(VelocityStack.SPRITE_SCALE);
			body = PhysicsHelper.createCircle(screen.getWorld(), Properties.getFloat("moneybag.radius", .5f), 
					position, BodyType.StaticBody, 1f, 1f, 1f, depth, depth, (short)0);
			body.setUserData(this);
		}
	}
}
