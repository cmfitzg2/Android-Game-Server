package client;

import java.awt.*;

public abstract class State {
	private static State currentState = null;

	public static void setState(State state) {
		currentState = state;
	}

	public static State getState()
	{
		return currentState;
	}

	public State(GameHandler handler) {

	}

	public abstract void tick();
	public abstract void render(Graphics g);
	public abstract void receiveData(Object o, String id);
}
