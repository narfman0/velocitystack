package com.blastedstudios.velocitystack.android;

import java.util.LinkedList;

import android.app.Activity;

import com.amazon.device.home.GroupedListHeroWidget;
import com.amazon.device.home.HomeManager;
import com.amazon.device.home.GroupedListHeroWidget.Group;
import com.amazon.device.home.GroupedListHeroWidget.IllegalIncrementalChangeException;
import com.amazon.device.home.GroupedListHeroWidget.ListEntry;
import com.amazon.device.home.GroupedListHeroWidget.VisualStyle;
import com.badlogic.gdx.Gdx;
import com.blastedstudios.velocitystack.util.IAssetChangeComponent;

public class HeroWidgetComponent implements IAssetChangeComponent {
	private Activity activity = null;
	
	@Override public void change(long cash, String cars) {
		if(activity == null){
			Gdx.app.error("HeroWidgetComponent.render", "Activity null, aborting");
			return;
		}
		GroupedListHeroWidget listWidget = new GroupedListHeroWidget();
		ListEntry cashListEntry = new ListEntry(activity);
		cashListEntry.setVisualStyle(VisualStyle.SIMPLE);
		cashListEntry.setPrimaryText("Cash");
		cashListEntry.setSecondaryText(cash+"");
		
		ListEntry carsListEntry = new ListEntry(activity);
		carsListEntry.setVisualStyle(VisualStyle.SIMPLE);
		carsListEntry.setPrimaryText("Cars Owned");
		carsListEntry.setSecondaryText(cars);
		
		LinkedList<ListEntry> listEntries = new LinkedList<ListEntry>();
		listEntries.add(cashListEntry);
		listEntries.add(carsListEntry);

		Group group = new Group();
		group.setListEntries(listEntries);
		try {
			listWidget.addGroup(0, group);
		} catch (IllegalIncrementalChangeException e) {
			e.printStackTrace();
		}
		
		HomeManager manager = HomeManager.getInstance(activity);
		manager.updateWidget(listWidget);
	}

	@Override public void initialize(Object object) {
		activity = ((Activity)object);
	}
}
