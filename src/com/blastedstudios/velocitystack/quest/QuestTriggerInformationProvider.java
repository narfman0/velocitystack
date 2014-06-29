package com.blastedstudios.velocitystack.quest;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.world.quest.trigger.IQuestTriggerInformationProvider;
import com.blastedstudios.velocitystack.ui.GameplayScreen;

public class QuestTriggerInformationProvider implements IQuestTriggerInformationProvider{
	private final GameplayScreen screen;
	
	public QuestTriggerInformationProvider(GameplayScreen screen){
		this.screen = screen;
	}

	@Override public Vector2 getPlayerPosition() {
		return screen.getPlayerPosition();
	}

	@Override public boolean isDead(String name) {
		return false;
	}

	@Override public boolean isNear(String origin, String target, float distance) {
		Body originBody = null, targetBody = null;
		Array<Body> bodyArray = new Array<>(screen.getWorld().getBodyCount());
		screen.getWorld().getBodies(bodyArray);
		for(Body body : bodyArray){
			if(body.getUserData().equals(origin))
				originBody = body;
			if(body.getUserData().equals(target))
				targetBody = body;
		}
		return originBody.getPosition().dst(targetBody.getPosition()) < distance;
	}

	@Override public Body getPhysicsObject(String name) {
		Array<Body> bodyArray = new Array<>(screen.getWorld().getBodyCount());
		screen.getWorld().getBodies(bodyArray);
		for(Body body : bodyArray)
			if(body.getUserData().equals(name))
				return body;
		return null;
	}

	@Override public boolean isAction() {
		return Gdx.input.isKeyPressed(Keys.E);
	}
}
