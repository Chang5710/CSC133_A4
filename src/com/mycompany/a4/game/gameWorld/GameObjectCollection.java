
package com.mycompany.a4.game.gameWorld;

import java.util.ArrayList;

import com.mycompany.a4.game.gameWorld.gameObject.GameObject;

public class GameObjectCollection implements ICollection {

	private ArrayList<GameObject> gameObjects;
	
	public GameObjectCollection() {
		gameObjects = new ArrayList<GameObject>();
	}
	
	@Override
	public void add(GameObject newObject) {
		gameObjects.add(newObject);
		
	}

	@Override
	public IIterator getIterator() {
		return new GameObjectIterator(gameObjects);
	}

	
	/**
	 * Inner Class Iterator 
	 */
	private class GameObjectIterator implements IIterator {

		private ArrayList<GameObject> gameObjects;
		private int pos = 0;
		
		public GameObjectIterator(ArrayList<GameObject> gameObjects) {
			this.gameObjects = gameObjects;
		}
		
		@Override
		public boolean hasNext() {
			if(pos >= gameObjects.size()) return false;
			if(gameObjects.get(pos) == null) return false;
			return true;
		}

		@Override
		public GameObject getNext() {
			GameObject obj = gameObjects.get(pos);
			pos += 1;
			return obj;
		}
	}
}
