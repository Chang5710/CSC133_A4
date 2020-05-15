package com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject;

import com.mycompany.a4.game.gameWorld.GameObjectCollection;

public interface IStrategy{
	
	public abstract void apply(NonPlayerCyborg npc, GameObjectCollection gameObjects);
}
