package com.mycompany.a4.game.gameWorld.gameObject.fixedObject;

import java.util.Random;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;
import com.mycompany.a4.game.gameWorld.IDrawable;
import com.mycompany.a4.game.gameWorld.gameObject.GameObject;
import com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject.NonPlayerCyborg;
import com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject.PlayerCyborg;

public class EnergyStation extends FixedObject implements IDrawable{
	private int capacity;
	private int size;
	
	//set capacity and size for EnergyStation
	public EnergyStation() {
		super(ColorUtil.rgb(34, 139, 34));
		Random rn = new Random();
		this.capacity = rn.nextInt((40-10)+1)+10; //range between 10 and 40
		this.size = (int) (this.capacity*3);
		//setSize(this.capacity);
	}
	
	//getter and setter
	public int getCapacity() {
		return capacity;
	}
	
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	
	public int getSize() {
		return size;
	}
	
	//to string information about EnergyStation
	public String toString() {
		return (
				"EnergyStation: loc=" + Math.round(this.getX()*10)/10 + "," + Math.round(this.getY()*10)/10+ 
				" color=" + getColorToString() + " size=" + getSize() + " capacity=" + capacity	
		);
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

		// find square of sum of radii
		int thisRadius= this.getSize() / 2;
		int otherRadius= (otherObject).getSize() / 2;

		int radiiSqr= (thisRadius * thisRadius + 2 * thisRadius * otherRadius + otherRadius * otherRadius);

		if (distBetweenCentersSqr <= radiiSqr) { result = true ; }

		return result;
	}

	@Override
	public void handleCollision(GameObject otherObject) {
		// TODO Auto-generated method stub
		if(otherObject instanceof PlayerCyborg || otherObject instanceof NonPlayerCyborg) {
			this.setCapacity(0);
			this.setColor(ColorUtil.rgb(162, 255, 159)); //set Empty EnergyStation color to light green
			}
	}

	@Override
	public boolean contains(Point pPtrRelPrnt, Point pCmpRelPrnt) {
		// TODO Auto-generated method stub
		//point from screen origin by click by user
		int px = pPtrRelPrnt.getX();
		int py = pPtrRelPrnt.getY();
		
		//switch x and y location to screen origin to compare
		int xLoc = (int) (pCmpRelPrnt.getX() + getX());
		int yLoc = (int) (pCmpRelPrnt.getY() + getY());
		
		if ( (px >= xLoc) && (px <= xLoc+size) && (py >= yLoc) && (py <= yLoc+size) ) {
		//	System.out.println("TRUE");
			return true;
		}else {
		//	System.out.println("False");
			return false;
		}
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

	@Override
	public void draw(Graphics g, Point pCmpRelPrnt, Point pCmpRelscrn) {
		// TODO Auto-generated method stub
		g.setColor(this.getColor());
		int xLoc = (int)this.getLocation().getX() + pCmpRelPrnt.getX() ;
		int yLoc = (int)this.getLocation().getY() + pCmpRelPrnt.getY();
		
		g.drawArc(xLoc, yLoc, size,size, 0, 360);
		
		if(!isSelected()) {
			g.fillArc(xLoc, yLoc, size,size, 0, 360);;
		}
		
		g.setColor(ColorUtil.BLACK);
		g.drawString(Integer.toString(capacity),
						xLoc + size/2 - 16,
						yLoc + size/2 - 16 );
	}
}
