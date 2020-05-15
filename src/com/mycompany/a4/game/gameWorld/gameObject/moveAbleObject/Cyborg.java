package com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject;

public abstract class Cyborg extends MoveableObject {
	
	private int life;
	private int maximumSpeed = 50;
	private int energyLevel;
	private int maxEnergyLevel;
	private int energyConsumptionRate =1;
	private int damageLevel;
	private int maxDamageLevel;
	private int lastBaseReached;
	private int steeringDirection;
	
	//set details date for a Player Cybory
	public Cyborg(int Color, int speed, int heading, double initialX, double initialY) {
		super(Color,speed,heading,initialX,initialY); //Color,speed,heading(90 degree to the northward),xLocation,yLocation
	}
	
	//getter and setter
	public int getLife() {
		return life;
	}

	public int getMaximumSpeed() {
		return maximumSpeed;
	}
	
	public void setMaximumSpeed(int maximumSpeed) {
		this.maximumSpeed = maximumSpeed;
	}

	public void setLife(int life) {
		this.life = life;
	}
	
	public int getEnergyConsumptionRate() {
		return energyConsumptionRate;
	}

	public int getEnergyLevel() {
		return energyLevel;
	}

	public void setEnergyLevel(int energyLevel) {
		this.energyLevel = energyLevel;
	}

	public int getDamageLevel() {
		return damageLevel;
	}

	public void setDamageLevel(int damageLevel) {
		this.damageLevel = damageLevel;
	}

	public void setMaxEnergyLevel(int maxEnergyLevel) {
		this.maxEnergyLevel = maxEnergyLevel;
	}

	public int getLastBaseReached() {
		return lastBaseReached;
	}

	public void setLastBaseReached(int lastBaseReached) {
		this.lastBaseReached = lastBaseReached;
	}
	
	public int getMaxDamageLevel() {
		return maxDamageLevel;
	}
	
	public void setMaxDamageLevel(int maxDamageLevel) {
		this.maxDamageLevel = maxDamageLevel;
	}

	public int getMaxEnergyLevel() {
		return maxEnergyLevel;
	}
	
	public void setSteeringDirection(int x) {
		this.steeringDirection = x;
	}
	
	public int getSteeringDirection() {
		return this.steeringDirection;
	}
	
	//respawn after run out of energy or the damageLevel is max
	public void respawn(double newX,double newY) {
	}
}