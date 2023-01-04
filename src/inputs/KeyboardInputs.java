package inputs;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

import gamestates.GameState;
import main.GamePanel;
import static utilz.Constants.Directions.*;

public class KeyboardInputs implements KeyListener {

	private GamePanel gp;

	public KeyboardInputs(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		switch (GameState.state) {
		case MENU:
			gp.getMenu().keyTyped(e);
			break;
		case PLAYING:
			gp.getPlay().keyTyped(e);
			break;
		default:
			break;

		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (GameState.state) {
		case MENU:
			gp.getMenu().keyReleased(e);
			break;
		case PLAYING:
			gp.getPlay().keyReleased(e);
			break;
		default:
			break;

		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (GameState.state) {
		case MENU:
			gp.getMenu().keyPressed(e);
			break;
		case PLAYING:
			gp.getPlay().keyPressed(e);
			break;
		default:
			break;

		}

	}

}
