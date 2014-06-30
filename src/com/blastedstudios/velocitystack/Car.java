package com.blastedstudios.velocitystack;

import java.util.Map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.ISerializer;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.group.GDXGroupExportStruct;

public class Car {
	private static final float SPRITE_SCALE = Properties.getFloat("sprite.scale", .02f);
	private final Body body, fWheel, rWheel;
	private final Sprite fWheelSprite, rWheelSprite, bodySprite;
	private final WheelJoint rWheelJoint, fWheelJoint;
	
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
		rWheelJoint = (WheelJoint) rWheel.getJointList().first().joint;
		fWheelJoint = (WheelJoint) fWheel.getJointList().first().joint;

		rWheelSprite = new Sprite(renderer.getTexture(group.getShape("rWheel").getResource()));
		rWheelSprite.setScale(SPRITE_SCALE);
		fWheelSprite = new Sprite(renderer.getTexture(group.getShape("fWheel").getResource()));
		fWheelSprite.setScale(SPRITE_SCALE);
		bodySprite = new Sprite(renderer.getTexture(group.getShape("body").getResource()));
		bodySprite.setScale(SPRITE_SCALE);
	}
	
	public void render(float dt, Batch batch){
		bodySprite.setPosition(body.getWorldCenter().x - bodySprite.getWidth()/2f, 
				body.getWorldCenter().y - bodySprite.getHeight()/2f);
		bodySprite.setRotation((float)Math.toDegrees(body.getAngle()));
		bodySprite.draw(batch);
		rWheelSprite.setPosition(rWheel.getWorldCenter().x - rWheelSprite.getWidth()/2f, 
				rWheel.getWorldCenter().y - rWheelSprite.getHeight()/2f);
		rWheelSprite.setRotation((float)Math.toDegrees(rWheel.getAngle()));
		rWheelSprite.draw(batch);
		fWheelSprite.setPosition(fWheel.getWorldCenter().x - fWheelSprite.getWidth()/2f, 
				fWheel.getWorldCenter().y - fWheelSprite.getHeight()/2f);
		fWheelSprite.setRotation((float)Math.toDegrees(fWheel.getAngle()));
		fWheelSprite.draw(batch);
	}
	
	public Vector2 getPosition(){
		return body.getWorldCenter();
	}
	
	public void gas(boolean enable, boolean reverse){
		fWheelJoint.enableMotor(enable);
		fWheelJoint.setMotorSpeed(reverse ? -Math.abs(fWheelJoint.getMotorSpeed()) : Math.abs(fWheelJoint.getMotorSpeed()));
		rWheelJoint.enableMotor(enable);
		rWheelJoint.setMotorSpeed(reverse ? -Math.abs(fWheelJoint.getMotorSpeed()) : Math.abs(fWheelJoint.getMotorSpeed()));
	}
	
	public void brake(boolean on){
		fWheel.setFixedRotation(on);
	}
}