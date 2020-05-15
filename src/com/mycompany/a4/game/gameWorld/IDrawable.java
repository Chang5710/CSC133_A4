package com.mycompany.a4.game.gameWorld;

import com.codename1.ui.Graphics;
import com.codename1.ui.geom.Point;

public interface IDrawable {
	public void draw(Graphics g, Point pCmpRelPrnt, Point pCmpRelscrn);
}
