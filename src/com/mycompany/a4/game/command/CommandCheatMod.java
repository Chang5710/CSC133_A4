package com.mycompany.a4.game.command;

import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a4.game.gameWorld.GameWorld;

public class CommandCheatMod extends Command {
private GameWorld gw;
	
	public CommandCheatMod(GameWorld gw) {
		super("CheatMod");
		this.gw = gw;
	}
	
	public void actionPerformed(ActionEvent e) {
		gw.CheatMod();
	}
}
