package com.mycompany.a4.game.command;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a4.game.gameWorld.GameWorld;

public class CommandChangeStrategies extends Command{
	private GameWorld gw;
	
	public CommandChangeStrategies(GameWorld gw) {
		super("Change Strategies");
		this.gw = gw;
	}
	
	public void actionPerformed(ActionEvent e) {
		gw.ChangeStrategies();
	}

}
