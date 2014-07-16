package com.blastedstudios.velocitystack.android;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import android.app.Activity;

import com.amazon.headtracking.HeadTrackingEvent;
import com.amazon.headtracking.HeadTrackingManager;
import com.amazon.headtracking.HeadTrackingPoller;
import com.badlogic.gdx.math.MathUtils;
import com.blastedstudios.velocitystack.ui.GameplayScreen;
import com.blastedstudios.velocitystack.util.IZoomProvider;
import com.blastedstudios.velocitystack.util.ZoomProvider;

@PluginImplementation
public class HeadTrackingZoomProvider implements IZoomProvider {
	private static final float PHONE_DISTANCE_AVERAGE = 304.8f;
	private HeadTrackingPoller poller;
	private HeadTrackingEvent event;

	@Override public float getZoom(GameplayScreen screen) {
		boolean success = poller.sample(event);
		if(success && event.isFaceDetected && event.isTracking){
			float distanceSafe = MathUtils.clamp(event.z_mm, PHONE_DISTANCE_AVERAGE/2f, PHONE_DISTANCE_AVERAGE*2f);
			return MathUtils.lerp(GameplayScreen.SPRITE_SCALE, ZoomProvider.MAX_ZOOM, distanceSafe);
		}
		return Math.min(ZoomProvider.MAX_ZOOM, GameplayScreen.SPRITE_SCALE + screen.getCar().getVelocity().len()/1000f);
	}

	@Override
	public void initialize(Object object) {
		HeadTrackingManager headTrackingManager = HeadTrackingManager.createInstance((Activity)object);
		poller = headTrackingManager.registerPoller();
		event = HeadTrackingEvent.obtain();
	}
}
