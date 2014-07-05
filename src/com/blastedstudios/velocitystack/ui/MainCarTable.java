package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainCarTable extends Table{
	public final String name;
	public final FileHandle handle;
	
	public MainCarTable(final Skin skin, final String name, final int cost, 
			final FileHandle carFile, boolean owned, final ICarTableListener buyListener,
			final long cash){
		this.name = name;
		this.handle = carFile;
		add(GameplayHUD.createButton(carFile.pathWithoutExtension()+"_Buy.png", 
				carFile.pathWithoutExtension()+"_Buy.png"));
		row();
		if(!owned){
			final TextButton buyButton = new TextButton("Buy for " + cost + "$", skin);
			buyButton.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					buyListener.buy(name, cost);
				}
			});
			if(cash < cost)
				buyButton.setTouchable(Touchable.disabled);
			add(buyButton);
		}else{
			final TextButton upgradeButton = new TextButton("Upgrade", skin);
			upgradeButton.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					buyListener.upgrade(name);
				}
			});
			add(upgradeButton);
		}
	}
	
	@Override public String toString(){
		return name;
	}
	
	public interface ICarTableListener{
		void buy(String name, int cost);
		void upgrade(String name);
	}
}
