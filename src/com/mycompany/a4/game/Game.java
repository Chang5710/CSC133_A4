package com.mycompany.a4.game;

import com.codename1.ui.geom.Point;
import com.codename1.ui.layouts.BorderLayout;
import com.codename1.ui.layouts.BoxLayout;
import com.codename1.ui.layouts.FlowLayout;
import com.codename1.ui.util.UITimer;
import com.mycompany.a4.game.command.CommandAboutInfo;
import com.mycompany.a4.game.command.CommandAccelerate;
import com.mycompany.a4.game.command.CommandBrake;
import com.mycompany.a4.game.command.CommandChangeStrategies;
import com.mycompany.a4.game.command.CommandCheatMod;
import com.mycompany.a4.game.command.CommandExit;
import com.mycompany.a4.game.command.CommandHelpInfo;
import com.mycompany.a4.game.command.CommandLeftTurn;
import com.mycompany.a4.game.command.CommandPause;
import com.mycompany.a4.game.command.CommandPosition;
import com.mycompany.a4.game.command.CommandRightTurn;
import com.mycompany.a4.game.command.CommandSound;
import com.mycompany.a4.game.gameWorld.GameWorld;
import com.mycompany.a4.game.gameWorld.MapView;
import com.mycompany.a4.game.gameWorld.ScoreView;
import com.codename1.ui.Toolbar;
import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Button;
import com.codename1.ui.CheckBox;
import com.codename1.ui.Container;
import com.codename1.ui.Form;

public class Game extends Form implements Runnable{
	private GameWorld gw;
	private MapView mv;
	private ScoreView sv;
	private boolean pause = false;
	
	//set up Command Patterns
	private CommandAccelerate cmdAccelerate;
	private CommandBrake cmdBrake;
	private CommandLeftTurn cmdLeftTurn;
	private CommandRightTurn cmdRightTurn;
	private CommandExit cmdExit;
	private CommandChangeStrategies cmdChangeStrategies;
	private CommandSound cmdSound;
	private CommandAboutInfo cmdAboutInfo;
	private CommandHelpInfo cmdHelpInfo;
	private CommandPosition cmdPosition;
	private CommandPause cmdPause;
	private CommandCheatMod cmdCheat;
	
	Button EmptyButton;
	Button AccelerateButton;
	Button LeftButton;
	Button ChangeStrategiesButton;
 	Button PositionButton;
 	Button PauseButton;
 	Button BrakeButton;
 	Button RightButton;
 	CheckBox SoundCB;
 	Button CheatButton;
 	Toolbar titleBar;
	
	private UITimer timer;
	
	public Game() {
		gw = GameWorld.getInstance(); //Create observable 
		gw.init();
		
		mv = new MapView(gw); //create observer 
		sv = new ScoreView(gw); //create observer
		
		gw.addObserver(mv); //register observer 
		gw.addObserver(sv); //register observer 
		
		//Game Form 
		setLayout(new BorderLayout());
		
		
		/**
		 * West Container 
		 */
		Container westContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
		
			//Components
			EmptyButton = new Button(" "); //make it looks more close to the simple GUI
			EmptyButton.getAllStyles().setPadding(TOP, 5);
			EmptyButton.getAllStyles().setPadding(BOTTOM, 5);
			
			AccelerateButton = new Button("Accelerate");
			AccelerateButton = makePretty(AccelerateButton);
			cmdAccelerate = new CommandAccelerate(gw);
			AccelerateButton.setCommand(cmdAccelerate);
			addKeyListener('a' , cmdAccelerate);
		 	
			LeftButton = new Button("Left");
			LeftButton = makePretty(LeftButton);
			cmdLeftTurn = new CommandLeftTurn(gw);
			LeftButton.setCommand(cmdLeftTurn);
			addKeyListener('l' , cmdLeftTurn);
		 	
			ChangeStrategiesButton = new Button("Change Strategies");
			ChangeStrategiesButton = makePretty(ChangeStrategiesButton);
			cmdChangeStrategies = new CommandChangeStrategies(gw);
			ChangeStrategiesButton.setCommand(cmdChangeStrategies);
			addKeyListener('c' , cmdChangeStrategies);
			
			westContainer.add(EmptyButton);
			westContainer.add(AccelerateButton);
			westContainer.add(LeftButton);
			westContainer.add(ChangeStrategiesButton);
			
			/**
			 * Add containers to contentpane
			 */
			 add(BorderLayout.WEST , westContainer);
		 
		 
		 /**
		  * South Container
		  */

			 Container southContainer = new Container(new FlowLayout(CENTER)); 
		 	
		 	PositionButton = new Button();
		 	PositionButton = makePretty(PositionButton);
		 	cmdPosition = new CommandPosition(gw);
		 	PositionButton.setCommand(cmdPosition);
		 	PositionButton.setAutoSizeMode(true);
		 	PositionButton.setEnabled(false);
		 	
		 	PauseButton = new Button();
		 	PauseButton = makePretty(PauseButton);
		 	cmdPause = new CommandPause(this);
		 	PauseButton.setCommand(cmdPause);
		 	addKeyListener('s', cmdPause);
		 			
		 	southContainer.add(PositionButton);
		 	southContainer.add(PauseButton);
		 	
		 	add(BorderLayout.SOUTH,southContainer);
		 	
		 /**
		  * East Container
		  */
		 Container eastContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));

		 	BrakeButton = new Button();
		 	BrakeButton = makePretty(BrakeButton);
		 	cmdBrake = new CommandBrake(gw);
		 	BrakeButton.setCommand(cmdBrake);
		 	addKeyListener('b' , cmdBrake);
		 	
		 	RightButton = new Button();
		 	RightButton = makePretty(RightButton);
		 	cmdRightTurn = new CommandRightTurn(gw);
		 	RightButton.setCommand(cmdRightTurn);
		 	addKeyListener('r', cmdRightTurn);
		 	
		 	EmptyButton = new Button(" ");
		 	EmptyButton.getAllStyles().setPadding(TOP, 5);
			EmptyButton.getAllStyles().setPadding(BOTTOM, 5);
		 	eastContainer.add(EmptyButton); //make it looks more close to the simple GUI
		 	eastContainer.add(BrakeButton);
		 	eastContainer.add(RightButton);
		 	 	
		 	add(BorderLayout.EAST,eastContainer);
		 	
		 /**
		  * Adding Title Bar
		  */
		 	
		 titleBar = new Toolbar();
		 setToolbar(titleBar);
		 titleBar.setTitle("Sili-Challenge Game");
		 cmdHelpInfo = new CommandHelpInfo(gw); 
		 titleBar.addCommandToRightBar(cmdHelpInfo); //add help to the Right side
		 
		 Toolbar.setOnTopSideMenu(true);
		 
		 // adding Command to the SideMenu
		 titleBar.addCommandToSideMenu(cmdAccelerate);
		
		 SoundCB = new CheckBox();
		 SoundCB.setSelected(true);
		 cmdSound = new CommandSound(gw);
		 SoundCB.setCommand(cmdSound);
		 SoundCB.getAllStyles().setBgTransparency(255);
		 SoundCB.getUnselectedStyle().setBgColor(ColorUtil.BLUE);
		 SoundCB.getAllStyles().setFgColor(ColorUtil.rgb(255, 255, 255));
		 titleBar.addComponentToSideMenu(SoundCB);
		 
		 cmdAboutInfo = new CommandAboutInfo(gw);
		 titleBar.addCommandToSideMenu(cmdAboutInfo);
		 
		 titleBar.addCommandToSideMenu(cmdHelpInfo);
		 
		 CheatButton = new Button();
		 CheatButton = makePretty(CheatButton);
		 cmdCheat = new CommandCheatMod(gw);
	 	 CheatButton.setCommand(cmdCheat);
	 	 titleBar.addCommandToSideMenu(cmdCheat);
		 
		 cmdExit = new CommandExit(gw);
		 titleBar.addCommandToSideMenu(cmdExit);
		 
		 /**
		  * North Container 
		  */
		 Container wrapper2 = new Container(new BorderLayout(BorderLayout.CENTER_BEHAVIOR_CENTER_ABSOLUTE));
		 wrapper2.add(CENTER,sv);
		 add(BorderLayout.NORTH , wrapper2);
		 
		 /**
		  * Center Container 
		  */
		 add(BorderLayout.CENTER, mv);
		
		this.show();
		timer = new UITimer(this);
		timer.schedule(20, true, this);
		System.out.println("Game GUI Setup Completed with the following stats :");
		System.out.println("Game World size : "+ GameWorld.getGameHeight() + ","+GameWorld.getGameWidth());
		System.out.println("Form Content pane size : " + this.getWidth() + "," + this.getHeight());
		System.out.println("MapView size : " + mv.getWidth() + "," + mv.getHeight());
		System.out.println("MapView Origin : " + mv.getX() + "," + mv.getY());	

		mv.setMapViewOrigin(new Point(mv.getX() , mv.getY()));
		MapView.setMapViewWidth(mv.getWidth());
		MapView.setMapViewHeight(mv.getHeight());	
	}
	
	//set Styles for all the button
	private Button makePretty(Button obj) {
		obj.getAllStyles().setBgTransparency(255);
		obj.getUnselectedStyle().setBgColor(ColorUtil.BLUE);
		obj.getAllStyles().setFgColor(ColorUtil.rgb(255, 255, 255));
		obj.getAllStyles().setPadding(TOP, 5);
		obj.getAllStyles().setPadding(BOTTOM, 5);
		obj.getAllStyles().setMargin(BOTTOM, 2);
		obj.getAllStyles().setMargin(LEFT,2);
		obj.setDisabledStyle(obj.getStyle()); 
		return obj;
	}
	
	//Pause
	public void Pause() {
		pause = !pause;
		if(pause) {
			timer.cancel();
			gw.setSound("OFF");
			
			//Key Disable
			removeKeyListener('a' , cmdAccelerate);
			AccelerateButton.setEnabled(false);
			removeKeyListener('b', cmdBrake);
			BrakeButton.setEnabled(false);
			removeKeyListener('l', cmdLeftTurn);
			LeftButton.setEnabled(false);
			removeKeyListener('r', cmdRightTurn);
			RightButton.setEnabled(false);
			ChangeStrategiesButton.setEnabled(false);
		 	PositionButton.setEnabled(false);
		 	BrakeButton.setEnabled(false);
		 	SoundCB.setEnabled(false);
		 	PositionButton.setEnabled(true);
		 	titleBar.findCommandComponent(cmdAccelerate).setEnabled(false);
			
			// Let gameworld know game is paused 
		 	gw.setPauseGame(true);
		 	PositionButton.setText("Position : OFF");
		 	PauseButton.setText("Play");
		 	this.revalidate();
		 	
		} else {
			timer.schedule(20, true, this);
			gw.setSound("ON");
			
			//Key Enable
			addKeyListener('a', cmdAccelerate);
			AccelerateButton.setEnabled(true);
			addKeyListener('b', cmdBrake);
			BrakeButton.setEnabled(true);
			addKeyListener('l', cmdLeftTurn);
			LeftButton.setEnabled(true);
			addKeyListener('r', cmdRightTurn);
			RightButton.setEnabled(true);
			ChangeStrategiesButton.setEnabled(true);
		 	BrakeButton.setEnabled(true);
		 	SoundCB.setEnabled(true);
		 	PositionButton.setEnabled(false);
		 	titleBar.findCommandComponent(cmdAccelerate).setEnabled(true);
		 	
		 	//Restore position command label 
		 	PositionButton.setText("Position");
		 	PauseButton.setText("Pause");
		 	gw.setPauseGame(false);
		 	gw.clearSelectable();
		 	this.revalidate(); //reside fill all the text
		}
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		gw.tick();
	}
}
