package com.blastedstudios.velocitystack.quest;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.world.quest.manifestation.IQuestManifestationExecutor;
import com.blastedstudios.velocitystack.ui.GameplayScreen;

public class QuestManifestationExecutor implements IQuestManifestationExecutor{
	private final GameplayScreen screen;
	
	public QuestManifestationExecutor(GameplayScreen screen){
		this.screen = screen;
		for(IGameplayScreenConsumer consumer : PluginUtil.getPlugins(IGameplayScreenConsumer.class))
			consumer.setScreen(screen);
	}

	@Override public Joint getPhysicsJoint(String name) {
		Array<Joint> joints = new Array<>(screen.getWorld().getJointCount());
		screen.getWorld().getJoints(joints);
		for(Joint joint : joints)
			if(joint.getUserData() != null && joint.getUserData().equals(name))
				return joint;
		return null;
	}

	@Override public Body getPhysicsObject(String name) {
		Array<Body> bodyArray = new Array<>(screen.getWorld().getBodyCount());
		screen.getWorld().getBodies(bodyArray);
		for(Body body : bodyArray)
			if(body.getUserData().equals(name))
				return body;
		return null;
	}

	@Override public World getWorld() {
		return screen.getWorld();
	}
}
