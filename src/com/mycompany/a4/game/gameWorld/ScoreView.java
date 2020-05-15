package com.mycompany.a4.game.gameWorld;

import java.util.Observable;
import java.util.Observer;

import com.codename1.charts.util.ColorUtil;
import com.codename1.ui.Container;
import com.codename1.ui.Label;
import com.codename1.ui.layouts.BoxLayout;

public class ScoreView extends Container implements Observer {

	/**
	 * Fields
	 */
	private Label TimeValueLabel;
	private Label lifeValueLabel;
	private Label LastBaseReachedLabel;
	private Label EnergyLevelLabel;
	private Label DamageLevelLabel;
	private Label SoundLabel;

	/**
	 * Constructor 
	 */
	public ScoreView(GameWorld gw) {

		//Set layout 
		setLayout(new BoxLayout(BoxLayout.X_AXIS));
		
		//Time 
		Label TimeTextLabel = new Label("Time:");
		TimeTextLabel.getAllStyles().setFgColor(ColorUtil.rgb(0,0,255));
		TimeValueLabel = new Label("");
		
		//Lives
		Label lifeTextLabel = new Label("Lives Left:");
		lifeTextLabel.getAllStyles().setFgColor(ColorUtil.rgb(0,0,255));
		lifeValueLabel = new Label(Integer.toString(gw.getPlayerCyborg().getLife()));
		
		//Last Base Reached
		Label BaseTextLabel = new Label("Player Last Reached:");
		BaseTextLabel.getAllStyles().setFgColor(ColorUtil.rgb(0,0,255));
		LastBaseReachedLabel = new Label(Integer.toString(gw.getPlayerCyborg().getLastBaseReached()));
		
		//Energy Level
		Label EnergyLevelTextLabel = new Label("Player Energy Level:");
		EnergyLevelTextLabel.getAllStyles().setFgColor(ColorUtil.rgb(0, 0, 255));
		EnergyLevelLabel = new Label(Integer.toString(gw.getPlayerCyborg().getEnergyLevel()));
		
		//Damage Level
		Label DamageLevelTextLabel = new Label("Player Damage Level:");
		DamageLevelTextLabel.getAllStyles().setFgColor(ColorUtil.rgb(0, 0, 255));
		DamageLevelLabel = new Label("");
		
		//Sound
		Label SoundTextLabel = new Label("Sound:");
		SoundTextLabel.getAllStyles().setFgColor(ColorUtil.rgb(0,0,255));
		SoundLabel = new Label("");
		
		//Add Component to container 
		add(TimeTextLabel);
		add(TimeValueLabel);
		add(lifeTextLabel);
		add(lifeValueLabel);
		add(BaseTextLabel);
		add(LastBaseReachedLabel);
		add(EnergyLevelTextLabel);
		add(EnergyLevelLabel);
		add(DamageLevelTextLabel);
		add(DamageLevelLabel);
		add(SoundTextLabel);
		add(SoundLabel);
		this.repaint();
	}
	
	@Override
	public void update(Observable observable, Object data) {
		GameWorld gw = (GameWorld)data;
		this.TimeValueLabel.setText(Integer.toString(gw.getRealTime()));
		this.lifeValueLabel.setText(Integer.toString(gw.getPlayerCyborg().getLife()));
		this.LastBaseReachedLabel.setText(Integer.toString(gw.getPlayerCyborg().getLastBaseReached()));
		this.EnergyLevelLabel.setText(Integer.toString(gw.getPlayerCyborg().getEnergyLevel()));
		this.DamageLevelLabel.setText(Integer.toString(gw.getPlayerCyborg().getDamageLevel()));
		this.SoundLabel.setText((gw.getSound()));
		this.revalidate();
		this.repaint();
	}
}
