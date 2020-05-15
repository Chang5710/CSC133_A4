package com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Point;
import com.mycompany.a4.game.gameWorld.GameWorld;
import com.mycompany.a4.game.gameWorld.IDrawable;
import com.mycompany.a4.game.gameWorld.gameObject.GameObject;
import com.mycompany.a4.game.gameWorld.gameObject.fixedObject.Base;
import com.mycompany.a4.game.gameWorld.gameObject.fixedObject.EnergyStation;

public class PlayerCyborg extends Cyborg implements ISteerable, IDrawable{
	
	private static double initialX=200;
	private static double initialY=200;
	
	//Singleton design pattern 
	private volatile static PlayerCyborg playerCyborg;
	public static PlayerCyborg getInstance() {
		if(playerCyborg == null) {
			synchronized(GameWorld.class) {
				if(playerCyborg == null)
					playerCyborg = new PlayerCyborg();
					playerCyborg.Translation((float)initialX, (float)initialY);
					playerCyborg.Rotation(90-playerCyborg.getHeading()-90);
					
			}
		}
		return playerCyborg;
	}
	
	private PlayerCyborg() {
		super(ColorUtil.BLUE,0,0,initialX,initialY); //Color,speed,heading(90 degree to the northward),xLocation,yLocation
		this.setLife(3);
		this.setLastBaseReached(1);
		this.setDamageLevel(0);
		this.setEnergyLevel(40);
		this.setSize(50);
		this.setMaxDamageLevel(40);
		this.setMaxEnergyLevel(40);
		this.setSteeringDirection(0);
	}
	
	//turn left by number amount of degree
	public void turnLeft() {
		this.setSteeringDirection(getSteeringDirection()-5);
		}
	
	//turn right by number amount of degree
	public void turnRight() {
		this.setSteeringDirection(getSteeringDirection()+5);
	}
	
	@Override
	public void respawn(double newX,double newY) {
		this.setX(newX);
		this.setY(newY);
		this.setSteeringDirection(90); //90=0 to the north
		this.setSpeed(0);
		this.setEnergyLevel(40);
		this.setDamageLevel(0);
		this.setColor(ColorUtil.BLUE);
		this.setLife(getLife()-1);
	}
	
	//call by accelerate function to increase Speed
	public void increaseSpeed() {
		
		if(this.getSpeed() < this.getMaximumSpeed()) {
			this.setSpeed(this.getSpeed() + 5);
			System.out.println("increase speed by 5 \n");
		}else {
			System.out.println("You already reach at the max speed " + this.getMaximumSpeed() +"\n");
		}

	}
	
	//call by brake function to decrease speed
	public void decreaseSpeed() {
		if(this.getSpeed() > 0) {
			this.setSpeed(this.getSpeed() - 5);
			System.out.println("decrease speed by 5\n");
		}else {
			System.out.println("You already reach at the mimimum speed 0\n");
		}
	}
	
	//to string information about player Cyborg
	public String toString() {
		return (
				"PlayerCyborg: loc=" + Math.round(this.getX()*10)/10 + "," + Math.round(this.getY()*10)/10 +
				" color=" + this.getColorToString() + " heading=" + this.getHeading() + 
				" speed=" + this.getSpeed() + "\n        size=" + this.getSize() + 
				" maxSpeed=" +this.getMaximumSpeed() + "  steeringDegree="+ this.getSteeringDirection() + 
				" energyLevel=" + this.getEnergyLevel() + " damageLevel=" + this.getDamageLevel()
				);
	}

	@Override
	public void draw(Graphics g, Point pCmpRelPrnt, Point pCmpRelscrn) {
		g.setColor(this.getColor());
		Transform gXform = Transform.makeIdentity();
		g.getTransform(gXform);
		Transform backup = gXform.copy();
		gXform.translate(pCmpRelscrn.getX(),pCmpRelscrn.getY());
		// LT begin here 
	    gXform.translate(myTranslation.getTranslateX(), myTranslation.getTranslateY());
	    gXform.concatenate(myRotation);
		gXform.scale(myScale.getScaleX(), myScale.getScaleY());
		// LT end here 
		gXform.translate(-pCmpRelscrn.getX(),-pCmpRelscrn.getY());
		g.setTransform(gXform);

		int x = (int)pCmpRelPrnt.getX();
		int y = (int)pCmpRelPrnt.getY();
		g.drawRect(x, y, this.getSize(), this.getSize());
		g.fillRect(x, y, this.getSize(), this.getSize());
		
		//set a Line in the middle easy to see which direction heading
		double angle = Math.toRadians(90- getHeading());
		double dx = 70*Math.cos(angle);
		double dy = 70*Math.sin(angle);

		g.setColor(ColorUtil.BLACK);
		g.drawLine(x+25 , y+25 , (int)(x+25+dx) , (int)(y+25+dy));
		
		g.setTransform(backup);
		
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
			System.out.println("PlayerCyborg collided with a Drone cause 1 damage\n");
			this.setDamageLevel(this.getDamageLevel()+1);
		}
		else if(otherObject instanceof NonPlayerCyborg) {
			System.out.println("PlayerCyborg collided with another cyborg cause 2 damage\n");
			this.setDamageLevel(this.getDamageLevel()+2);
		}
		else if(otherObject instanceof Base) {
			int BaseID = ((Base) otherObject).getBaseID();
			if(this.getLastBaseReached()+1 == BaseID){
				int newBaseID = this.getLastBaseReached()+1;
				this.setLastBaseReached(newBaseID);
				if(this.getLastBaseReached()+1 < 4){
					System.out.println("PlayerCyborg reach to base " + newBaseID + "\n");
				}
			}else {
				System.out.println("Please collide base in sequential!\n" + 
						   "The next sequential is " + (this.getLastBaseReached()+1) + "\n");
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
				System.out.println("PlayerCyborg collided with a EnergyStation refuel " + Math.abs(this.getEnergyLevel()-beforeRuel) + " Energy\n");
			}
		}
	}
}
