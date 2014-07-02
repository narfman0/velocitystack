package com.blastedstudios.velocitystack;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.blastedstudios.velocitystack.quest.MoneyBagHandler.MoneyBag;
import com.blastedstudios.velocitystack.ui.GameplayScreen;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener {
	private final GameplayScreen screen;
	public static final Object REMOVE_USER_DATA = "r";
	
	public ContactListener(GameplayScreen screen){
		this.screen = screen;
	}
	
	@Override public void preSolve(Contact contact, Manifold arg1) {
		Body a = contact.getFixtureA().getBody(), b = contact.getFixtureB().getBody();
		MoneyBag moneyBag = (MoneyBag) (a.getUserData() instanceof MoneyBag ? a.getUserData() :
			b.getUserData() instanceof MoneyBag ? b.getUserData() : null);
		if(moneyBag != null){
			screen.receiveMoney(moneyBag.amount);
			moneyBag.body.setUserData(ContactListener.REMOVE_USER_DATA);
			contact.setEnabled(false);
		}
		if(ContactListener.REMOVE_USER_DATA.equals(a.getUserData()) || 
				ContactListener.REMOVE_USER_DATA.equals(b.getUserData()))
			contact.setEnabled(false);
	}

	@Override public void beginContact(Contact contact) {}
	@Override public void endContact(Contact contact) {}
	@Override public void postSolve(Contact contact, ContactImpulse impulse) {}
}
