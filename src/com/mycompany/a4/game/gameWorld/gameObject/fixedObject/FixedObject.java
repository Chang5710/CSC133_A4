package com.mycompany.a4.game.gameWorld.gameObject.fixedObject;

import com.codename1.ui.geom.Point;
import com.codename1.ui.geom.Point2D;
import com.mycompany.a4.game.gameWorld.ISelectable;
import com.mycompany.a4.game.gameWorld.gameObject.GameObject;

public abstract class FixedObject extends GameObject implements ISelectable{
	
	private boolean isSelected;
	
	//update color for EnergyStation
	public FixedObject(int Color) {
		super(Color);
	}

	//update color and location for Base
	public FixedObject(int Color, double x, double y) {
		super(Color, x, y);
	}
	
	@Override
	public void setLocation(Point2D location) {
		super.setLocation(location);
	}

	public boolean isSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean flag) {
		isSelected = flag;
	}
	
	public abstract boolean contains(Point pPtrRelPrnt, Point pCmpRelPrnt);
}
