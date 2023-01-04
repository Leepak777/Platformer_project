package gamestates;

import java.awt.event.MouseEvent;

import main.Game;
import main.GamePanel;
import ui.MenuButton;

public class State {
	protected GamePanel gp;

	public State(GamePanel gp) {
		this.gp = gp;
	}

	public GamePanel getGame() {
		return gp;
	}

	public boolean isIn(MouseEvent e, MenuButton mb) {
		return mb.getBounds().contains(e.getX(), e.getY());
	}
}
