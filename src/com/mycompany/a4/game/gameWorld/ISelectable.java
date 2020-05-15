package com.mycompany.a4.game.gameWorld;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public interface ISelectable {
	
	
	public void setSelected(boolean flag);
	public boolean isSelected();
	public boolean contains(Point pPtrRelPrnt, Point pCmpRelPrnt);
	public void draw(Graphics g, Point pCmpRelPrnt, Point pCmpRelscrn);
}
