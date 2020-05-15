package com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject;

import java.util.Random;
import com.codename1.ui.geom.Point2D;
import com.mycompany.a4.game.gameWorld.MapView;
import com.mycompany.a4.game.gameWorld.gameObject.GameObject;

public abstract class MoveableObject extends GameObject{
	private int speed;
	private int heading;
	
	//Add speed and steeringDirection for non-player like Drone
	public MoveableObject(int Color) {
		super(Color); //update color
		randomized();
	}
	
	//randomize the speed and direction for Drone
	public void randomized() {
		Random rn = new Random();
		this.speed= rn.nextInt((10-5)+1)+5; //range between 5 and 10
		this.heading= rn.nextInt(360); //range within 360 degree
	}
	
	//Add speed and steeringDirection for player Cybory
	public MoveableObject(int Color, int speed, int heading, double x, double y) {
		super(Color,x,y); //update color and location
		this.speed=speed;
		this.heading=  heading;
	}


	//getter and setter
	public void setHeading(int x) {
		this.heading= x;
	}
	
	public void setSpeed(int x) {
		if(this instanceof Drone) {
			System.out.println("asdasd");
		}
		this.speed = x;
	}

	public int getSpeed() {
		return this.speed;
	}

	public int getHeading() {
		return heading;
	}

	//to get new location once the tick is increase
	public void move() {
		if(this instanceof Drone) {
			System.out.println("asdasd");
		}
		float newX = (float) (this.getX() + (float)Math.cos( Math.toRadians(90-this.heading)  )* speed);
 		float newY = (float) (this.getY() + (float)Math.sin( Math.toRadians(90-this.heading)  )* speed);		
 		int offset = this.getSize();
 		int border = 2; // See mapview for border size and adjust here.
 		
 		int orginX = (int)MapView.getMapViewOrigin().getX();
 		int orginY = (int)MapView.getMapViewOrigin().getY();

		//Bounce off right wall 
		if(orginX + newX + offset >=   MapView.getMapViewWidth() + orginX - border
				&& (heading >= 0 && heading <= 180)) {
			if(heading == 0 || heading == 180) {
				setHeading(270);
			}else {
				setHeading(360-heading);
			}
			//System.out.println("Bounced off right wall");
			 newX = (float) (this.getX() + (float)Math.cos( Math.toRadians(90-this.heading)  )* speed);
	 		 newY = (float) (this.getY() + (float)Math.sin( Math.toRadians(90-this.heading)  )* speed);	
		}
		//Bounce off left wall 
		else if(orginX + newX  <= orginX + border) {
			if(heading == 0 || heading == 180) {
				setHeading(90);
			}else {
				setHeading(360-heading);
			}
			//System.out.println("Bounded off left wall");
			 newX = (float) (this.getX() + (float)Math.cos( Math.toRadians(90-this.heading)  )* speed);
	 		 newY = (float) (this.getY() + (float)Math.sin( Math.toRadians(90-this.heading)  )* speed);	
		}
		//Bounce off bottom wall
		else if(orginY + newY + offset >= orginY + MapView.getMapViewHeight() - border ) {
			setHeading((360-heading+180)%360);
			 newX = (float) (this.getX() + (float)Math.cos( Math.toRadians(90-this.heading)  )* speed);
	 		 newY = (float) (this.getY() + (float)Math.sin( Math.toRadians(90-this.heading)  )* speed);	
		}		
		else if(orginY + newY   <= orginY + border  ) {
			setHeading((360-heading+180)%360);
			 newX = (float) (this.getX() + (float)Math.cos( Math.toRadians(90-this.heading)  )* speed);
	 		 newY = (float) (this.getY() + (float)Math.sin( Math.toRadians(90-this.heading)  )* speed);	
		}	

		if(this instanceof Drone) {
			System.out.println("newX = " + newX +  "  newY = " + newY);
		}
		// Set Location 
		this.setLocation(new Point2D(newX,newY));
		//this.resetTranslation();
		
		this.resetLT();
		
		this.Translation(newX,newY);
		this.Rotation(90-this.heading-90);
	}
}
