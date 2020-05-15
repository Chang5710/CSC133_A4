package com.mycompany.a4.game.gameWorld.gameObject;

public interface ITransform {
	public void Rotation(float degrees);
	public void Translation(float tx, float ty);
	public void Scale(float sx, float sy);
}
