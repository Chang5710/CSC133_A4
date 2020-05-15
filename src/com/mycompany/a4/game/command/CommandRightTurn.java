package com.mycompany.a4.game.command;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a4.game.gameWorld.GameWorld;

public class CommandRightTurn extends Command {
	private GameWorld gw;
	
	public CommandRightTurn(GameWorld gw) {
		super("Right");
		this.gw = gw;
	}
	
	public void actionPerformed(ActionEvent e) {
		gw.turnRight();
	}

}
