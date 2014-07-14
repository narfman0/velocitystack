package com.blastedstudios.velocitystack.util;

import java.util.Map;
import java.util.Map.Entry;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.WheelJoint;
import com.blastedstudios.gdxworld.ui.GDXRenderer;
import com.blastedstudios.gdxworld.util.ISerializer;
import com.blastedstudios.gdxworld.util.PluginUtil;
import com.blastedstudios.gdxworld.util.Properties;
import com.blastedstudios.gdxworld.world.group.GDXGroupExportStruct;
import com.blastedstudios.velocitystack.ui.GameplayScreen;

public class Car {
	public static String[] UPGRADES = Properties.get("car.upgrades", "Torque,Speed,Traction").split(",");
	private final Body body, fWheel, rWheel;
	private final Sprite fWheelSprite, rWheelSprite, bodySprite;
	private final WheelJoint rWheelJoint, fWheelJoint;
	private final Map<String, Body> bodies;
	
	public Car(World world, Vector2 position, FileHandle carFile, GDXRenderer renderer, Preferences preferences){
		GDXGroupExportStruct group = null;
		for(ISerializer serializer : PluginUtil.getPlugins(ISerializer.class))
			if(serializer.getFileFilter().accept(carFile.file())){
				try {
					group = (GDXGroupExportStruct) serializer.load(carFile);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		bodies = group.instantiate(world, position);
		body = bodies.get("body");
		fWheel = bodies.get("fWheel");
		rWheel = bodies.get("rWheel");
		rWheelJoint = (WheelJoint) rWheel.getJointList().first().joint;
		fWheelJoint = (WheelJoint) fWheel.getJointList().first().joint;
		
		applyUpgrades(preferences, carHandleToName(carFile));

		rWheelSprite = new Sprite(new Texture("data/textures/car/" + group.getShape("rWheel").getResource()));
		rWheelSprite.setScale(GameplayScreen.SPRITE_SCALE);
		fWheelSprite = new Sprite(new Texture("data/textures/car/" + group.getShape("fWheel").getResource()));
		fWheelSprite.setScale(GameplayScreen.SPRITE_SCALE);
		bodySprite = new Sprite(new Texture("data/textures/car/" + group.getShape("body").getResource()));
		bodySprite.setScale(GameplayScreen.SPRITE_SCALE);
		for(Entry<String,Body> entries : bodies.entrySet())
			entries.getValue().setUserData(this);
	}
	
	private void applyUpgrades(Preferences preferences, String prettyName){
		for(String upgrade : UPGRADES){
			float modifier = 1f+(preferences.getFloat(prettyName + "." + upgrade)/100f);
			if(upgrade.equals("Torque")){
				fWheelJoint.setMaxMotorTorque(fWheelJoint.getMaxMotorTorque() * modifier);
				rWheelJoint.setMaxMotorTorque(rWheelJoint.getMaxMotorTorque() * modifier);
			}
			if(upgrade.equals("Speed")){
				fWheelJoint.setMotorSpeed(fWheelJoint.getMotorSpeed() * modifier);
				rWheelJoint.setMotorSpeed(rWheelJoint.getMotorSpeed() * modifier);
			}
			if(upgrade.equals("Traction"))
				for(Body body : new Body[]{fWheel,rWheel})
					for(Fixture fixture : body.getFixtureList())
						fixture.setFriction(fixture.getFriction() * modifier);
		}
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
	
	public Vector2 getVelocity(){
		return body.getLinearVelocity();
	}
	
	public float getMaxMotorSpeed(){
		return (fWheelJoint.getMotorSpeed() + rWheelJoint.getMotorSpeed()) / 2f;
	}
	
	public float getCurrentMotorSpeed(){
		return (fWheel.getAngularVelocity() + rWheel.getAngularVelocity()) / 2f;
	}
	
	public float getMotorPitch(){
		return MathUtils.clamp(MathUtils.lerp(.5f, 2f, getCurrentMotorSpeed()/getMaxMotorSpeed()), .5f, 2f);
	}
	
	public void gas(boolean enable, boolean reverse){
		float reverseMod = reverse ? 1 : -1;
		fWheelJoint.enableMotor(enable);
		fWheelJoint.setMotorSpeed(reverseMod * Math.abs(fWheelJoint.getMotorSpeed()));
		rWheelJoint.enableMotor(enable);
		rWheelJoint.setMotorSpeed(reverseMod * Math.abs(rWheelJoint.getMotorSpeed()));
	}
	
	public void brake(boolean on){
		fWheel.setFixedRotation(on);
		rWheel.setFixedRotation(on);
	}
	
	public static String carHandleToName(FileHandle handle){
		return handle.nameWithoutExtension().replaceAll("_", " ");
	}
}
