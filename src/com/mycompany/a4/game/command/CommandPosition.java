package com.mycompany.a4.game.command;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a4.game.gameWorld.GameWorld;
import com.codename1.ui.Button;

public class CommandPosition extends Command{
	private GameWorld gw;
	
	public CommandPosition(GameWorld gw) {
		super("Position");
		this.gw = gw;
	}
	
	public void actionPerformed(ActionEvent e) {
		gw.ChangePosition();
		if(gw.getPauseGame()) {
			if(gw.getPositionToggle()) {
				this.setCommandName("Position : ON ");
			}else {
				this.setCommandName("Position : OFF ");
			}
			((Button) e.getComponent()).setCommand(this);
			((Button) e.getComponent()).setSize(((Button) e.getComponent()).getPreferredSize());
			e.getComponent().getParent().revalidate();
		}
	}
	
}
