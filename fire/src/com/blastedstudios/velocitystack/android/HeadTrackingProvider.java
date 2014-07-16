package com.blastedstudios.velocitystack.android;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import android.app.Activity;

import com.amazon.headtracking.HeadTrackingEvent;
import com.amazon.headtracking.HeadTrackingManager;
import com.amazon.headtracking.HeadTrackingPoller;
import com.badlogic.gdx.math.MathUtils;
import com.blastedstudios.velocitystack.ui.GameplayScreen;
import com.blastedstudios.velocitystack.util.ICarController;
import com.blastedstudios.velocitystack.util.IZoomProvider;
import com.blastedstudios.velocitystack.util.ZoomProvider;

@PluginImplementation
public class HeadTrackingProvider implements IZoomProvider, ICarController{
	private static final float PHONE_DISTANCE_AVERAGE = 304.8f;
	private HeadTrackingPoller poller;
	private HeadTrackingEvent event;
	private boolean success;

	@Override public float getZoom(GameplayScreen screen) {
		success = poller.sample(event);
		if(valid()){
			float distanceSafe = MathUtils.clamp(event.z_mm, PHONE_DISTANCE_AVERAGE/2f, PHONE_DISTANCE_AVERAGE*2f);
			return MathUtils.lerp(GameplayScreen.SPRITE_SCALE, ZoomProvider.MAX_ZOOM, distanceSafe);
		}
		return Math.min(ZoomProvider.MAX_ZOOM, GameplayScreen.SPRITE_SCALE + screen.getCar().getVelocity().len()/1000f);
	}

	@Override public void initialize(Object object) {
		HeadTrackingManager headTrackingManager = HeadTrackingManager.createInstance((Activity)object);
		poller = headTrackingManager.registerPoller();
		event = HeadTrackingEvent.obtain();
	}

	@Override public boolean gas() {
		if(valid())
			return isThreshold(-event.x_mm, event.z_mm);
		return false;
	}

	@Override public boolean brake() {
		if(valid())
			return isThreshold(event.y_mm, event.z_mm);
		return false;
	}

	@Override public boolean reverse() {
		if(valid())
			return isThreshold(event.x_mm, event.z_mm);
		return false;
	}
	
	/**
	 * return true if the first number greater than the other by a certain amount
	 */
	private boolean isThreshold(float initial, float target){
		return initial/2f >= target;
	}
	
	private boolean valid(){
		return success && event.isFaceDetected && event.isTracking;
	}
}
