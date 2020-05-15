package com.mycompany.a4.game.command;

import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a4.game.gameWorld.GameWorld;

public class CommandExit extends Command{
	private GameWorld gw;
	
	public CommandExit(GameWorld gw) {
		super("Exit");
		this.gw = gw;
	}
	
	public void actionPerformed(ActionEvent e) {
		Boolean bOK = Dialog.show("Confirm quit", "Are you sure you want to quit?", "OK", "Cancel");
		if(bOK) {
			gw.exit();
		}
	}
}
