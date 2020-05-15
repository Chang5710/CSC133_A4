package com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.mycompany.a4.game.gameWorld.GameObjectCollection;
import com.mycompany.a4.game.gameWorld.IDrawable;
import com.mycompany.a4.game.gameWorld.gameObject.GameObject;
import com.mycompany.a4.game.gameWorld.gameObject.fixedObject.Base;
import com.mycompany.a4.game.gameWorld.gameObject.fixedObject.EnergyStation;

public class NonPlayerCyborg extends Cyborg implements IDrawable{
	
	private IStrategy curStrategy;
	private int targetBase;
	private double SD;
	
	public NonPlayerCyborg(double initialX, double initialY) {
		super(ColorUtil.rgb(0, 191, 255), 0, 0, initialX, initialY); //Color,speed,heading(90 degree to the northward),xLocation,yLocation
		this.setLife(3);
		this.setLastBaseReached(0);
		this.setDamageLevel(0);
		this.setEnergyLevel(40);
		this.setSize(50);
		this.setMaxDamageLevel(60);
		this.setMaxEnergyLevel(80);
		this.setSteeringDirection(0);
		this.targetBase=this.getLastBaseReached()+1;
	}
	
	//getter and setter
	public int getTargetBase() {
		return targetBase;
	}

	public void setTargetBase(int targetBase) {
		this.targetBase = targetBase;
	}
	
	public double getSD() {
		return SD;
	}

	public void setSD(double sD) {
		SD = sD;
	}

	public IStrategy getCurStrategy() {
		return curStrategy;
	}

	//setStrategy and invokeStrategy
	public void setStrategy(IStrategy s) {
		curStrategy = s;
	}
	
	public void invokeStrategy(GameObjectCollection gameObjects) {
		curStrategy.apply(this, gameObjects);
	}
	
	public String toStringCurStrategy() {
		String Strategy;
		if(this.getCurStrategy() instanceof BaseStrategy) {
			Strategy = "BaseStrategy";
		}else {
			Strategy = "AttackStrategy";
		}
		return Strategy;
	}
	
	@Override
	public void respawn(double newX,double newY) {
		this.setX(newX);
		this.setY(newY);
		this.setSteeringDirection(90); //90=0 to the north
		this.setSpeed(0);
		this.setEnergyLevel(80);
		this.setDamageLevel(0);
		this.setColor(ColorUtil.rgb(0, 191, 255));
		this.setLife(getLife()-1);
	}
	
	public String toString() {
		return (
				"NonPlayerCyborg: loc=" + Math.round(this.getX()*10)/10 + "," + Math.round(this.getY()*10)/10 +
				" color=" + this.getColorToString() + " heading=" + this.getHeading() + 
				" speed=" + this.getSpeed() + " TargetBase=" + this.getTargetBase()+ " LastBase=" + this.getLastBaseReached() +"\n       size=" + this.getSize() + 
				" maxSpeed=" +this.getMaximumSpeed() + "  steeringDegree="+
				" energyLevel=" + this.getEnergyLevel() + " damageLevel=" + this.getDamageLevel() +
				" currStrategy=" + 	toStringCurStrategy()
				);
	}

	@Override
	public void draw(Graphics g, Point pCmpRelPrnt, Point pCmpRelscrn) { 
		g.setColor(this.getColor());
		int x = (int)this.getX() + (int)pCmpRelPrnt.getX();
		int y = (int)this.getY() + (int)pCmpRelPrnt.getY();
		g.drawRect(x, y, this.getSize(), this.getSize(),3);
		
		// show NPC's Target Base
		if(curStrategy instanceof AttackStrategy) {
			g.drawString("p", x+5, y); //draw p if is AttackStrategy 
		}else {
			g.drawString(Integer.toString(getTargetBase()) , x ,y); //draw target bass if is BaseStrategy
		}
	}

	@Override
	public boolean collidesWith(GameObject otherObject) {
	// TODO Auto-generated method stub
	boolean result = false;
	double thisCenterX = this.getX();
	double thisCenterY = this.getY();

	double otherCenterX = (otherObject).getX();
	double otherCenterY = (otherObject).getY();

	double dx = thisCenterX - otherCenterX;
	double dy = thisCenterY - otherCenterY;

	double distBetweenCentersSqr = (dx * dx + dy * dy);

	int thisRadius= this.getSize() / 2;
	int otherRadius;
	
	if(otherObject instanceof Base) {
		otherRadius= (otherObject).getSize();
	}else {
		otherRadius= (otherObject).getSize() / 2;
	}

	int radiiSqr= (thisRadius * thisRadius + 2 * thisRadius * otherRadius + otherRadius * otherRadius);

	if (distBetweenCentersSqr <= radiiSqr) { result = true ; }

	return result;
	}


	@Override
	public void handleCollision(GameObject otherObject) {
		// TODO Auto-generated method stub
		if(otherObject instanceof Drone) {
			if(getDamageLevel()+1< getMaxDamageLevel()) {
				System.out.println("NPC collided with a Drone cause 2 damage\n");
				this.setDamageLevel(this.getDamageLevel()+1);
			}else {
				this.setDamageLevel(80);
			}
		}
		else if(otherObject instanceof PlayerCyborg) {
			if(getDamageLevel()+2< getMaxDamageLevel()) {
				System.out.println("NPC collided with PlayerCyborg cause 2 damage\n");
				this.setDamageLevel(this.getDamageLevel()+2);
			}else {
				this.setDamageLevel(80);
			}
		}
		else if(otherObject instanceof NonPlayerCyborg) {
			if(getDamageLevel()+2< getMaxDamageLevel()) {
				System.out.println("NPC collided with another NPC cause 2 damage\n");
				this.setDamageLevel(this.getDamageLevel()+2);
			}else {
				this.setDamageLevel(80);
			}
		}
		else if(otherObject instanceof Base) {
			int BaseID = ((Base) otherObject).getBaseID();
			if(this.getLastBaseReached()+1 == BaseID) {
				int newBaseID = this.getLastBaseReached()+1;
				this.setLastBaseReached(newBaseID);
				this.setTargetBase(this.targetBase+1);
			}
		}
		else if(otherObject instanceof EnergyStation) {
			if(((EnergyStation) otherObject).getCapacity()!=0) {
				int beforeRuel = this.getEnergyLevel();
				if(this.getEnergyLevel()+((EnergyStation) otherObject).getCapacity()<this.getMaxEnergyLevel()) {
					this.setEnergyLevel(this.getEnergyLevel()+((EnergyStation) otherObject).getCapacity());
				}else {
					this.setEnergyLevel(this.getMaxEnergyLevel()); //over fuel the Energy
				}
				System.out.println("NPC collided with a EnergyStation refuel " + Math.abs(this.getEnergyLevel()-beforeRuel) + " Energy\n");
			}
		}
		this.setHeading(0);
		this.setSD(0);
		this.setSteeringDirection(0);
	}

	@Override
	public void Rotation(float degrees) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Translation(float tx, float ty) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void Scale(float sx, float sy) {
		// TODO Auto-generated method stub
		
	}
}
