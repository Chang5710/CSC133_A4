package com.mycompany.a4.game.gameWorld;

import java.util.Observable;
import java.util.Random;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Display;
import com.mycompany.a4.game.gameWorld.gameObject.GameObject;
import com.mycompany.a4.game.gameWorld.gameObject.ICollider;
import com.mycompany.a4.game.gameWorld.gameObject.fixedObject.Base;
import com.mycompany.a4.game.gameWorld.gameObject.fixedObject.EnergyStation;
import com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject.AttackStrategy;
import com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject.BaseStrategy;
import com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject.Cyborg;
import com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject.Drone;
import com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject.IStrategy;
import com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject.MoveableObject;
import com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject.NonPlayerCyborg;
import com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject.PlayerCyborg;
import com.mycompany.a4.game.sound.BGSound;
import com.mycompany.a4.game.sound.Sound;

public class GameWorld extends Observable{
	
	private static int gameHeight = 1000;
	private static int gameWidth = 1500;
	private int gameClock = 0;
	private int numberOfDrone;
	private int numberOfEnergyStation;
	private int numberOfTurnLeft;
	private int numberOfTurnRight;
	private int numberOfBase = 4;
	private int realTime = 0;
	private GameObjectCollection gameObjects;
	private String Sounds = "ON";
	private boolean positionToggle = false;
	Random rn = new Random();
	private PlayerCyborg cyborg; //to hold player cyborg
	NonPlayerCyborg cyborgNPC; //to hold temporary NPC cyborg
	EnergyStation energyStation;
	Base base;
	IStrategy strategy; 
	private BGSound bgm;
	private Sound crashSound;
	private Sound chargeSound;
	private Sound explosionSound;
	private boolean GamePause;
	private boolean Cheat = false;
	
	//setter and getter
	public int getGameClock() {
		return gameClock;
	}
	public void setGameClock(int gameClock) {
		this.gameClock = gameClock;
	}
	
	public int getRealTime() {
		return realTime;
	}
	
	public PlayerCyborg getPlayerCyborg() {
		return cyborg;
	}
	
	public GameObjectCollection getGameObjects() {
		return gameObjects;
	}

	//Singleton design pattern 
	private volatile static GameWorld gw;
	private GameWorld(){};
	public static GameWorld getInstance() {
		if(gw == null) {
			synchronized(GameWorld.class) {
				if(gw == null)
					gw = new GameWorld();
			}
		}
		return gw;
	}
	
	//initialize the game. Set up base,drone,energyStation and create player Cyborg
	public void init() {
		gameObjects = new GameObjectCollection();
		gameObjects.add(new Base(1,200,200));
		gameObjects.add(new Base(2,100,980));
		gameObjects.add(new Base(3,800,200));
		gameObjects.add(new Base(4,1300,900));
		gameObjects.add(cyborg = PlayerCyborg.getInstance());
		gameObjects.add(cyborgNPC = new NonPlayerCyborg(400,200));
		
		cyborgNPC.setStrategy(new BaseStrategy());
		gameObjects.add(cyborgNPC = new NonPlayerCyborg(200,0));
		cyborgNPC.setStrategy(new AttackStrategy());
		gameObjects.add(cyborgNPC = new NonPlayerCyborg(0,200));
		cyborgNPC.setStrategy(new AttackStrategy());
		
		this.numberOfDrone = 1;
		for(int i =0;i<numberOfDrone;i++) {
			Drone newDrone = new Drone();
			((GameObject) newDrone).Translation((float)newDrone.getX(),(float)newDrone.getY());
			((GameObject) newDrone).Rotation(90-newDrone.getHeading()-90);
			((GameObject) newDrone).Scale(5,5);
			gameObjects.add(newDrone);
		}
		this.numberOfEnergyStation = rn.nextInt((4-2)+1)+2;//range between 2 and 4
		for(int i =0;i<numberOfEnergyStation;i++) {
			gameObjects.add(new EnergyStation());
		}
		this.map();

		bgm = new BGSound("background.wav");
		crashSound = new Sound("crash.wav");
		chargeSound = new Sound("charge.wav");
		explosionSound = new Sound("explosion.wav");
		setSound(Sounds);
	
		this.setChanged();
		this.notifyObservers(this);
	}
	
	public void setSound(String sound) {
			this.Sounds = sound;
			if(Sounds == "ON") {
				bgm.play();
			}else {
				bgm.pause();
			}
			this.setChanged();
			this.notifyObservers(this);
	}
	
	public String getSound() {
		return Sounds;
	}

	public static int getGameHeight() {
		return gameHeight; 
	}
	public static int getGameWidth() {
		return gameWidth;
	}
	//exit the game
	public void exit() {
		Display.getInstance().exitApplication();
	}
	
	//increase speed
	public void accelerate() {
		cyborg.increaseSpeed();
	}
	
	//decrease speed
	public void brake() {
		cyborg.decreaseSpeed();
	}
	
	//save number of time turn left and execute when gameClock increase
	public void turnLeft() {
		if(numberOfTurnLeft >= 8) {
			System.out.println("Only allow to turn left maximum 40 degree\n");
		}else {
			cyborg.turnLeft();
			numberOfTurnLeft++;
			numberOfTurnRight--;
			System.out.println("turn left by 5 degree\n");
		}	
	}
	
	//save number of time turn right and execute when gameClock increase
	public void turnRight() {
		if(numberOfTurnRight >= 8) {
			System.out.println("Only allow to turn Right maximum 40 degree\n");
		}else {
			cyborg.turnRight();
			numberOfTurnLeft--;
			numberOfTurnRight++;
			System.out.println("turn Right by 5 degree\n");
		}
	}
	
	//check if cyborg have enough life to respawn, if end the game
	public void respawnCyborg(Cyborg cyborgT){ 
			
		if(cyborgT.getLife()>1) {
			Base base = findBase(cyborgT); 
			cyborgT.respawn(base.getX(),base.getY());
			if(Sounds == "ON")
				explosionSound.play();
		}else {
			if(cyborgT instanceof PlayerCyborg) {
				System.out.println("PlayerCyborg Lose! \n");
				System.exit(0);
			}else {
				System.out.println("NPC Dead! \n");
			}
	    }	
	}
	
	public void findCyborg() {
		IIterator iter = gameObjects.getIterator();
		while(iter.hasNext()) {//update cyborg information on GameWorld
			GameObject obj = iter.getNext();
			if(obj instanceof PlayerCyborg) {
				this.cyborg= (PlayerCyborg)obj;		
				break;
			}
		}
	}
	
	//find the last base reached and update the location
	public Base findBase(Cyborg cyborgT) {;
		Base base = null;
		IIterator iter = gameObjects.getIterator();
		while(iter.hasNext()) {
			GameObject obj = iter.getNext();
			if(obj instanceof Base) {
				base = (Base) obj;
				if(base.getBaseID()==cyborgT.getLastBaseReached()) {
					this.base = base;
					break;
				}
			}
		}
		return base;
	}
	
	//check energy level after each move
	public Boolean checkEnergy(int currEnergy, Cyborg cyborgT){
		if(currEnergy>0) {
			return true;
		}else {
			System.out.println("You run out of Energy\n");
			respawnCyborg(cyborgT);
			return false;
		}
	}
	
	//check Damage level and change color
	public Boolean checkDamage(int currDamage, Cyborg cyborgT) {
		
		if(currDamage<cyborgT.getMaxDamageLevel()) {
			if(currDamage>cyborgT.getMaxDamageLevel()*0.3 && currDamage<cyborgT.getMaxDamageLevel()*0.7) {
				cyborgT.setColor(ColorUtil.rgb(255, 204, 203)); //light red
			}else if(currDamage>cyborgT.getMaxDamageLevel()*0.7) {
				cyborgT.setColor(ColorUtil.rgb(255, 64, 64)); //more close to red rgb(255,0,0)
			}
			cyborgT.setDamageLevel(currDamage);
			return true;
		}
		else {
			if(cyborgT instanceof PlayerCyborg) {
				System.out.println("PlayerCybory is breaked\n");
			}else {
				System.out.println("NPC is breaked\n");
			}
			
			respawnCyborg(cyborgT);
			return false;
		}
	}
	
	//update speed after DamageLevel too high
	public void setNewSpeed(Cyborg cyborgT){ 
		double ratio = cyborgT.getDamageLevel() / cyborgT.getMaxDamageLevel();
		if(ratio != 0) {
		cyborgT.setSpeed((int)(cyborgT.getSpeed()*ratio)); //set Speed limit needs to be limited by cyborg's damage.
		checkDamage(cyborgT.getDamageLevel(),cyborgT); //set check Damage Level
		}
	}
	
	//check if cyborg have enough Energy for next Tick
	public void consumeEnergy(Cyborg cyborgT) {
		int currentEnergy = cyborgT.getEnergyLevel() - cyborgT.getEnergyConsumptionRate();
		if(checkEnergy(currentEnergy,cyborgT)) {
			cyborgT.setEnergyLevel(currentEnergy);
		}
		if(cyborgT instanceof NonPlayerCyborg) {
			if(cyborgT.getEnergyLevel()<10) {
				cyborgT.setEnergyLevel(30);
			}
		}
	}
	
	//check Heading within 360 degree and set Heading 
	public void checkHeading(Cyborg cyborgT) {
		if(cyborgT.getSteeringDirection() >= 0) {
			cyborgT.setHeading((cyborgT.getHeading() + cyborgT.getSteeringDirection()) % 360);
		}else {
			if(cyborgT.getHeading() == 0) {
				cyborgT.setHeading(360 + cyborgT.getSteeringDirection());
			}else {
				cyborgT.setHeading(cyborgT.getHeading() + cyborgT.getSteeringDirection());
			}
		}
	}
	
	//game clock has ticked
	public void tick() {
		
		gameClock++;
		if(gameClock % 50 == 0) {
			realTime += 1;
			//check Energy
			consumeEnergy(cyborg);
		}
		
		if(Cheat == true) {
			cyborg.setDamageLevel(0);
			cyborg.setEnergyLevel(40);
		}
		
		//check heading
		checkHeading(cyborg);
		
		IIterator iter2 = gameObjects.getIterator();
		while(iter2.hasNext()){
			GameObject obj = iter2.getNext();
			if(obj instanceof NonPlayerCyborg) {
				cyborgNPC = (NonPlayerCyborg) obj;
				if (cyborgNPC.getCurStrategy() instanceof BaseStrategy) {
					cyborgNPC.invokeStrategy(gameObjects);
					findBase(cyborgNPC);
				}else { //AttackStrategy
					cyborgNPC.invokeStrategy(gameObjects);
					findCyborg();
				}
				checkHeading(cyborgNPC);
				setNewSpeed(cyborgNPC);
				consumeEnergy(cyborgNPC);
				((NonPlayerCyborg) obj).move();
			}else if(obj instanceof PlayerCyborg) {
				((MoveableObject) obj).move();
			}else if(obj instanceof Drone) {
				((MoveableObject) obj).move();
			}
		
		}
		
		checkCollision();
		
		numberOfTurnLeft = 0;
		numberOfTurnRight = 0;
		cyborg.setSteeringDirection(0);
		//System.out.println("gameClock increase by 1\n");
		//map();
		this.setChanged();
		this.notifyObservers(this);
	}
	
	//checkCollision
	private void checkCollision() {
		IIterator iter3 = gameObjects.getIterator();
		while(iter3.hasNext()) {
			ICollider curObj = (ICollider)iter3.getNext();

			IIterator iter4 = gameObjects.getIterator();
			while(iter4.hasNext()) {
				ICollider otherObj = (ICollider) iter4.getNext();
				if(otherObj != curObj) {
					if(curObj.collidesWith((GameObject) otherObj) && !((GameObject) curObj).isThere((GameObject)otherObj)) {
						((GameObject) curObj).addCollided((GameObject) otherObj);
						curObj.handleCollision((GameObject) otherObj);
						if(curObj instanceof PlayerCyborg || curObj instanceof NonPlayerCyborg) {
							if(otherObj instanceof PlayerCyborg || 
									otherObj instanceof NonPlayerCyborg ||
									otherObj instanceof Drone) { //if PlayerCyborg or NPC collided PlayerCyborg, NPC or Drone
								checkDamage(((Cyborg) curObj).getDamageLevel(),(Cyborg) curObj); //set check Damage Level of PlayerCyborg
								if(Sounds == "ON")
									crashSound.play();
							}
							else if(otherObj instanceof Base) { //if PlayerCyborg or NPC collided with base
								curObj.handleCollision((GameObject) otherObj);
								if(((Cyborg) curObj).getLastBaseReached() ==numberOfBase) { //check WIN
									if(curObj instanceof PlayerCyborg) {
										System.out.println("You Won!!! You had reach to the last Base!\n");
										System.exit(0);
									}
									else {
										System.out.println("NonPlayerCyborg Won!!! NonPlayerCyborg had reach to the last Base!\n");
										System.exit(0);
									}
								}
							}
							else if(otherObj instanceof EnergyStation) {
								if(((EnergyStation) otherObj).getCapacity()!=0) {
									gameObjects.add(new EnergyStation());
									if(Sounds == "ON")
										chargeSound.play();
								}
										
							}
						}
	
					} else {//if otherObj is not collided with curObj and its in curObj's vector
						if (!curObj.collidesWith((GameObject) otherObj) && ((GameObject)curObj).isThere((GameObject)otherObj)){
							((GameObject)curObj).delNotCollided((GameObject)otherObj); //remove it
						}
					}
				}
			}
		}

	}
	
	//if NPC's Energy running low add some more
	public void checkNPCEnergy() {
		IIterator iter = gameObjects.getIterator();
		while(iter.hasNext()) {
			GameObject obj = iter.getNext();
			if(obj instanceof NonPlayerCyborg) {
				cyborgNPC = (NonPlayerCyborg) obj;
				if(cyborgNPC.getEnergyLevel()<10) {
					cyborgNPC.setEnergyLevel(30);
				}
			}
		}
	}
	
	//display player-cyborg state:life, gameClock,last reached base, energy level, and damage  level
	public void displayStates() {
		if( checkEnergy(cyborg.getEnergyLevel(),cyborg) && checkDamage(cyborg.getDamageLevel(),cyborg) == true) {//check if energyLevel>0 and DamageLevel<5
			System.out.println("Life=" + cyborg.getLife() + " GameClock=" + this.gameClock + " LastBaseReached=" + cyborg.getLastBaseReached() +
							   " EnergLevel=" + cyborg.getEnergyLevel() + " DamageLevel=" + cyborg.getDamageLevel() +"\n"
			);
		}
	}
	
	//show a set of lines describing the current object in the world 
	public void map() {
			
		IIterator iter = gameObjects.getIterator();
		while(iter.hasNext()) {
			System.out.println(iter.getNext().toString());
		}
		System.out.println("\n");
	}
	
	//Change starategy for each NPC
	public void ChangeStrategies() {
		System.out.println("All NPC changed strategy");
		IIterator iter = gameObjects.getIterator();
		while(iter.hasNext()) {
			GameObject obj = iter.getNext();
			if(obj instanceof NonPlayerCyborg) {
				cyborgNPC = (NonPlayerCyborg) obj;
				if(cyborgNPC.getCurStrategy() instanceof BaseStrategy) {
					cyborgNPC.setStrategy(new AttackStrategy());
				}else {// not BaseStrategy must be AttackStrategy
					cyborgNPC.setStrategy(new BaseStrategy());
				}
			}
		}
	}
	
	//Posistion
	public void ChangePosition() {
		if(GamePause) {
			positionToggle = !positionToggle;
			if(positionToggle) {
				System.out.println("Position :  ON" );
			}else{
				System.out.println("Position : OFF");
			}
		}
	}
	
	public void setPauseGame(boolean flag) {
		GamePause = flag;
		positionToggle = false;
	}
	
	public boolean getPauseGame() {
		return GamePause;
	}
	
	public boolean getPositionToggle() {
		return positionToggle;
	}
	
	//reset selectable
	public void clearSelectable() {
		IIterator iter = gameObjects.getIterator();
		while(iter.hasNext()) {
			GameObject obj = iter.getNext();
			if(obj instanceof ISelectable) {
				((ISelectable) obj).setSelected(false);
			}
		}
	}
	
	//Cheat Mod
	public void CheatMod() {
		if(Cheat == false) {
			Cheat = true;
			cyborg.setColor(ColorUtil.BLUE);
			System.out.println("CheatMod ON");
		}else {
			Cheat = false;
			System.out.println("CheatMod OFF");
		}
	}
}
 
