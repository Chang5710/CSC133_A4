package com.mycompany.a4.game.gameWorld.gameObject.moveAbleObject;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Point;
import com.mycompany.a4.game.gameWorld.IDrawable;
import com.mycompany.a4.game.gameWorld.gameObject.GameObject;

public class Drone extends MoveableObject implements IDrawable{
	
	private Point top, bottomLeft, bottomRight;
	
	//set a non-player Drone black color and size 10
	public Drone() {
		super(ColorUtil.BLACK);
		this.setSize(10);
		top = new Point (0, 10/2);
		bottomLeft = new Point (-10/2, -10/2);
		bottomRight = new Point (10/2, -10/2);
		
		
	}
	
	@Override
	public void setColor(int Color){ //to prevent changed in color for drone
	}
	
	//to string information about Drone
	public String toString() {
		return (
				"Drone: loc=" + Math.round(this.getX()*10)/10 + "," + Math.round(this.getY()*10)/10 +
				" color=" + this.getColorToString() + " heading=" + this.getHeading() +
				" speed=" + this.getSpeed() + " size=" + this.getSize()
		);
	}
	/**
	 * Move() -> Location -> Translation 
	 *  Location -> Translation 
	 *  (300,300) -> Translation(300,300) = p1 
	 *  (342,400) -> Translation(42,100) = p2 
	 */
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
		  
		  g.drawLine(pCmpRelPrnt.getX()+top.getX(), pCmpRelPrnt.getY()+top.getY(),
				  pCmpRelPrnt.getX()+bottomLeft.getX(), pCmpRelPrnt.getY()+ bottomLeft.getY());
		  g.drawLine(pCmpRelPrnt.getX()+bottomLeft.getX(), pCmpRelPrnt.getY()+bottomLeft.getY(),
				  pCmpRelPrnt.getX()+bottomRight.getX(), pCmpRelPrnt.getY()+ bottomRight.getY());
		  g.drawLine(pCmpRelPrnt.getX()+bottomRight.getX(), pCmpRelPrnt.getY()+bottomRight.getY(),
				  pCmpRelPrnt.getX()+top.getX(), pCmpRelPrnt.getY()+ top.getY());
		  
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
	}


}
