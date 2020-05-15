package com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject;

import java.util.Random;

import com.codename1.util.MathUtil;
import com.mycompany.a4.game.gameWorld.GameObjectCollection;
import com.mycompany.a4.game.gameWorld.IIterator;
import com.mycompany.a4.game.gameWorld.gameObject.GameObject;

public class AttackStrategy implements IStrategy{
	
	private int lastSD = -999;
	private double SD = -99.9;

	@Override
	public void apply(NonPlayerCyborg npc, GameObjectCollection gameObjects) {
		setSteeringDirection(npc,gameObjects);
	}
	
	public void setSteeringDirection(NonPlayerCyborg npc, GameObjectCollection gameObjects) {
		IIterator iter = gameObjects.getIterator();
		while(iter.hasNext()) {
			GameObject obj = iter.getNext();
			if(obj instanceof PlayerCyborg) {
				double dx = Math.abs(obj.getX() - npc.getX());
				double dy = Math.abs(obj.getY() - npc.getY());
				
				double newSD; //because use integer to calculate angle off too much, I create another double SteeringDirection to calculate angle
				int newSteeringDirection;
				
				if(dx != 0 && dy != 0) {
					newSteeringDirection = (int)MathUtil.floor(Math.toDegrees(MathUtil.atan(dy/dx)));
					newSD = Math.toDegrees(MathUtil.atan(dy/dx));
				}else {
					//System.out.println("SD set to 0");
					newSteeringDirection = 0;
					newSD = 0;
				}
				if(npc.getY() > obj.getY()) { //NPC on top of Player
					
					//if Player is on the right side 
					if(obj.getX() > npc.getX()) {
						newSD += 90;
						newSteeringDirection += 90;
					}else if(obj.getX()< npc.getX()){//if Player is on the left side
						newSD = 270 - newSD;
						newSteeringDirection = 270 - newSteeringDirection;
					}else{
						newSD = 180;
						newSteeringDirection = 180;
					}
				}else if(npc.getY() < obj.getY()) {//Player on top of NPC 
					//Player on the right side 
					if(obj.getX() > npc.getX()) {
						newSD = 90-newSD;
						newSteeringDirection = 90 - newSteeringDirection; 
					}else if(obj.getX() < npc.getX()) {  //Player on left side
						newSD = -1*(90 - newSD);
						newSteeringDirection = -1*(90 - newSteeringDirection);
					}else {
						newSD = 0;
						newSteeringDirection = 0;
					}
				}else {
					//SAME LEVEL 
					//Player on right side 
					if(obj.getX() > npc.getX()) {
						newSD = 90;
						newSteeringDirection = 90;
					} else if(obj.getX() < npc.getX()) { //Player on left side 
						newSD = 270;
						newSteeringDirection = 270;
					}else {
						newSD = 0;
						newSteeringDirection = 0;
					}
				}
				
				//first time
				if(lastSD == -999) {
					lastSD = newSteeringDirection;
					 SD = newSD;
				}
				else if (Math.abs(newSD - SD) <= 5) { //here is the Angular deviation, I allow 5 degree
					newSteeringDirection = 0; //newSteeringDirection should show 0, here I ignore the deviation
					npc.setHeading((int)newSD); //set the precise double SteeringDirection to NPC heading
				}else { //if the Angular deviation > 5, update both of SteeringDirection
					lastSD = newSteeringDirection;
					SD = newSD;
				}
				
				//setSteeringDirection
				npc.setSteeringDirection(newSteeringDirection);
				npc.setSD(SD);
				
				//calculate the distance
				double distance = Math.sqrt(dx*dx+dy*dy); 
				
				//when the distance between NPC and PlayerCyborg is least then 50
				if(distance <= 50 ) {
					//double speed = Math.sqrt(distance/50); //NPC will keep update speed make sure it run pass the base
					npc.setSpeed(1);
				}else {
					Random rn = new Random();
					int speed= rn.nextInt((5-3)+1)+3;
					npc.setSpeed(speed); //set speed to Full speed
				}
				return;
			}
		}
	}
}
