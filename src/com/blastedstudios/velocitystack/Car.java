package com.blastedstudios.velocitystack;

import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.ISerializer;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.group.GDXGroupExportStruct;

public class Car {
	private static final float SPRITE_SCALE = Properties.getFloat("sprite.scale", .02f);
	private final Body body, fWheel, rWheel;
	private final Sprite fWheelSprite, rWheelSprite, bodySprite;
	
	public Car(World world, Vector2 position, FileHandle carFile, GDXRenderer renderer){
		GDXGroupExportStruct group = null;
		for(ISerializer serializer : PluginUtil.getPlugins(ISerializer.class))
			if(serializer.getFileFilter().accept(carFile.file())){
				try {
					group = (GDXGroupExportStruct) serializer.load(carFile.file());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		Map<String, Body> bodies = group.instantiate(world, position);
		body = bodies.get("body");
		fWheel = bodies.get("fWheel");
		rWheel = bodies.get("rWheel");

		rWheelSprite = new Sprite(renderer.getTexture("tire1.png"));
		rWheelSprite.setScale(SPRITE_SCALE);
		fWheelSprite = new Sprite(renderer.getTexture("tire1.png"));
		fWheelSprite.setScale(SPRITE_SCALE);
		bodySprite = new Sprite(renderer.getTexture("jeep.png"));
		bodySprite.setScale(SPRITE_SCALE);
	}
	
	public void render(float dt, Batch batch){
		rWheelSprite.setPosition(rWheel.getWorldCenter().x, rWheel.getWorldCenter().y);
		rWheelSprite.setRotation((float)Math.toDegrees(rWheel.getAngle()));
		rWheelSprite.draw(batch);
		fWheelSprite.setPosition(fWheel.getWorldCenter().x, fWheel.getWorldCenter().y);
		fWheelSprite.setRotation((float)Math.toDegrees(fWheel.getAngle()));
		fWheelSprite.draw(batch);
		bodySprite.setPosition(body.getWorldCenter().x, body.getWorldCenter().y);
		bodySprite.setRotation((float)Math.toDegrees(body.getAngle()));
		bodySprite.draw(batch);
	}
	
	public Vector2 getPosition(){
		return body.getWorldCenter();
	}
	
	public void gas(float dt, boolean reverse){
		float directionModifier = reverse ? -1f : 1f;
		float torque = Properties.getFloat("car.torque", 9999f) * dt * directionModifier;
		rWheel.applyTorque(torque, true);
		fWheel.applyTorque(torque, true);
	}
	
	public void brake(boolean on){
		fWheel.setFixedRotation(on);
	}
}
