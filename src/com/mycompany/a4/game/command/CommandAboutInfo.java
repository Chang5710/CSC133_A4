package com.mycompany.a4.game.command;

import com.codename1.ui.Command;
import com.codename1.ui.Dialog;
import com.codename1.ui.events.ActionEvent;
import com.mycompany.a4.game.gameWorld.GameWorld;

public class CommandAboutInfo extends Command {
	
	public CommandAboutInfo(GameWorld gw) {
		super("About Information");
	}
	
	public void actionPerformed(ActionEvent e) {
		Dialog.show("About Infornation","Name: ChanglongLi\n" + 
					"CourseName: CSC133-06 \n" + 
					"VersionNumber: 3","OK","Cancel");
	}
}
