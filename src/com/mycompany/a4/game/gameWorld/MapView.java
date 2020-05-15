package com.mycompany.a4.game.gameWorld;

import java.util.Observable;
import java.util.Observer;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Graphics;
import com.codename1.ui.Transform;
import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.plaf.Border;
import com.mycompany.a4.game.gameWorld.gameObject.GameObject;
import com.mycompany.a4.game.gameWorld.gameObject.fixedObject.EnergyStation;

public class MapView extends Container implements Observer{

	static private int mapViewHeight;
	static private int mapViewWidth;
	private GameWorld gw = null;
	static private Point mapViewOrigin;
	
	Transform worldToND, ndToDisplay, theVTM;
	private float winLeft , winBottom , winRight , winTop;
	private float winWidth;
	private float winHeight;
	
	
	/** 
	 * Constructor 
	 */
	public MapView(GameWorld gw) {
		this.gw = gw;
		MapView.mapViewHeight = this.getHeight();
		MapView.mapViewWidth = this.getWidth();
		
		//static private Point2D mapViewOrigin;
		this.setLayout(new BorderLayout());
		this.getAllStyles().setFgColor(ColorUtil.BLUE);
		this.getAllStyles().setBorder(Border.createLineBorder(2));

		MapView.mapViewHeight = this.getHeight();
		MapView.mapViewWidth = this.getWidth();
//		setWidth(1000);
//		setHeight(1000);
		
		/**
		 *  World Window Setup 
		 */
		winLeft = 0;
		winBottom = 0;
		winRight = this.getWidth();
		winTop = this.getHeight();
		
		winWidth = winRight - winLeft;
		winHeight = winTop - winBottom;

	}
	
	public static int getMapViewHeight() { return mapViewHeight; }
	public static int getMapViewWidth() { return mapViewWidth; }
	public static void setMapViewHeight(int height) { mapViewHeight = height; }
	public static void setMapViewWidth(int width) { mapViewWidth = width; }
	
	public void setMapViewOrigin(Point p) { MapView.mapViewOrigin = p; }
	public static Point getMapViewOrigin() { return mapViewOrigin; }
	
	@Override
	public void update(Observable observable, Object data) {
		this.gw = (GameWorld) data;
		this.repaint();
	}
	
	/**
	 * @description take win width,height,left,bottom which is window coordinate system -> ND coordinate system 
	 * @param winWidth
	 * @param winHeight
	 * @param winLeft
	 * @param winBottom
	 * @return Transform 
	 */
	private Transform buildWorldToNDXform(float winWidth, float winHeight, float winLeft, float winBottom) {
		Transform tmpXfrom = Transform.makeIdentity();
		tmpXfrom.scale((1/winWidth), (1/winHeight));
		tmpXfrom.translate(-winLeft, -winBottom);
		return tmpXfrom;
	}
	
	/**
	 * @description take display coordinate system's info , from ND -> display
	 * @param displayWidth
	 * @param displayHeight
	 * @return
	 */
	private Transform buildNDToDisplayXform( float displayWidth, float displayHeight) {
		Transform tmpXform = Transform.makeIdentity();
		tmpXform.translate(0,  displayHeight);
		tmpXform.scale(displayWidth, -displayHeight);
		return tmpXform;
	}
	
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// World -> ND -> Display 
		
		winWidth = this.getWidth() - winLeft;
		winHeight = this.getHeight() - winBottom;
		
		System.out.println("winWidth = " + winWidth + "   winHeight = " + winHeight);
		
		worldToND = buildWorldToNDXform(winWidth, winHeight, winLeft, winBottom);
		ndToDisplay = buildNDToDisplayXform(this.getWidth(), this.getHeight());
		theVTM = ndToDisplay.copy();
		theVTM.concatenate(worldToND);
		
		Transform gXform = Transform.makeIdentity();
		g.getTransform(gXform);
		gXform.translate(getAbsoluteX(), getAbsoluteY());
		gXform.concatenate(theVTM);
		gXform.translate(-getAbsoluteX(), -getAbsoluteY());
		
		
		
		//pCmpRelPrnt is the start point of MapView
		Point pCmpRelPrnt = new Point(this.getX(), this.getY());
		Point pCmpRelscrn = new Point(getAbsoluteX(), getAbsoluteY());
		IIterator itr = gw.getGameObjects().getIterator();
		while(itr.hasNext()) {
			GameObject tempObject = itr.getNext();
			if (tempObject instanceof IDrawable) {
				g.setTransform(gXform);
				((IDrawable) tempObject).draw(g, pCmpRelPrnt, pCmpRelscrn);
				g.resetAffine();
			}
		}
	}
	
	@Override
	public void pointerPressed(int x, int y) {
		
		boolean move = false; //sign flag if object is selected

		x = x - getParent().getAbsoluteX();
		y = y - getParent().getAbsoluteY();
		Point pPtrRelPrnt = new Point(x,y);
		Point pCmpRelPrnt = new Point(getX() , getY());
		x -= getX(); //X relative to mapview 
		y -= getY();
		System.out.println("Clicked " + "(" + x + "," + y + ")");
		IIterator iter2 = gw.getGameObjects().getIterator();
		while(iter2.hasNext()) {
			GameObject obj = iter2.getNext();
			if(obj instanceof ISelectable) {
				if(((ISelectable)obj).isSelected()) {
				//	System.out.println("Valid Move");
					if(!gw.getPositionToggle()) break;
					
					if(obj instanceof EnergyStation) {
						obj.setLocation(new Point2D(x-obj.getSize()/2,y-obj.getSize()/2));
					}else {
						//BASE 
						obj.setLocation(new Point2D(x,y));
					}

					move = true;
					repaint();
					((ISelectable)obj).setSelected(false);
					return;

				}else {
				//	System.out.println("Invalid Move");
					((ISelectable)obj).setSelected(false);
				}
			}
		}
		
		if(!move) {
			IIterator iter = gw.getGameObjects().getIterator();
			while(iter.hasNext()) {
				GameObject obj = iter.getNext();
				if(obj instanceof ISelectable) {
					//pPtrRelPrnt is screen origin
					if(((ISelectable)obj).contains(pPtrRelPrnt, pCmpRelPrnt)) {
						((ISelectable)obj).setSelected(true);
						System.out.println("Object Selected");
					}else {
						//Not selected
						((ISelectable)obj).setSelected(false);
					}
				}
				repaint();
			}
		}
	}
}
