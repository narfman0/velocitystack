package com.blastedstudios.velocitystack.ui;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MainCarTable extends Table{
	private final String name;
	
	public MainCarTable(final Skin skin, final String name, final int cost, 
			final FileHandle carFile, boolean owned, final IBuyListener buyListener,
			final long cash){
		this.name = name;
		add(GameplayHUD.createButton(carFile.pathWithoutExtension()+"_Buy.png", 
				carFile.pathWithoutExtension()+"_Buy.png"));
		if(!owned){
			row();
			final TextButton buyButton = new TextButton("Buy", skin);
			buyButton.addListener(new ClickListener() {
				@Override public void clicked(InputEvent event, float x, float y) {
					buyListener.buy(name, cost);
				}
			});
			if(cash < cost)
				buyButton.setTouchable(Touchable.disabled);
			add(buyButton);
		}
	}
	
	@Override public String toString(){
		return name;
	}
	
	public interface IBuyListener{
		void buy(String name, int cost);
	}
}
