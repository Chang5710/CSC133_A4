package com.mycompany.a4.game.gameWorld.gameObject;

public interface ICollider {
	boolean collidesWith (GameObject otherObject);
	void handleCollision (GameObject otherObject);
}
