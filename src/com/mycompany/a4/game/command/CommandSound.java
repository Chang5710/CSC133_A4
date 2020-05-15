package com.mycompany.a4.game.command;

import com.codename1.ui.CheckBox;
import com.codename1.ui.Command;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a4.game.gameWorld.GameWorld;

public class CommandSound extends Command{
	private GameWorld gw;
	
	public CommandSound(GameWorld gw) {
		super("Sound");
		this.gw = gw;
	}
	
	public void actionPerformed(ActionEvent e) {
		if(((CheckBox)e.getComponent()).isSelected()) {
			gw.setSound("ON");
		}else {
			gw.setSound("OFF");
		}
	}
}
