package com.blastedstudios.velocitystack.android;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.amazon.ags.api.AmazonGamesClient;
import com.amazon.ags.api.whispersync.GameDataMap;
import com.blastedstudios.velocitystack.util.ISaveUtility;

@PluginImplementation
public class WhisperSyncSaveUtility implements ISaveUtility {
	private final GameDataMap gameDataMap;
	
	public WhisperSyncSaveUtility(){
		gameDataMap = AmazonGamesClient.getWhispersyncClient().getGameData();
	}

	@Override public void set(String key, String value) {
		gameDataMap.getLatestString(key).set(value);
	}

	@Override public String get(String key) {
		return gameDataMap.getLatestString(key).getValue();
	}
}
