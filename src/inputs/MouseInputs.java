package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.GameState;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

	private GamePanel gp;

	public MouseInputs(GamePanel gp) {
		this.gp = gp;
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		switch (GameState.state) {
		case PLAYING:
			gp.getPlay().mouseDragged(e);
			break;
		case MENU:
		case OPTIONS:
			gp.getGameOp().mouseDragged(e);
			break;
		default:
			break;

		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			gp.getMenu().mouseMoved(e);
			break;
		case PLAYING:
			gp.getPlay().mouseMoved(e);
			break;
		case OPTIONS:
			gp.getGameOp().mouseMoved(e);
			break;
		default:
			break;

		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			gp.getMenu().mouseClicked(e);
			break;
		case PLAYING:
			gp.getPlay().mouseClicked(e);
			break;
		case OPTIONS:
			gp.getGameOp().mouseClicked(e);
			break;
		default:
			break;

		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			gp.getMenu().mousePressed(e);
			break;
		case PLAYING:
			gp.getPlay().mousePressed(e);
			break;
		case OPTIONS:
			gp.getGameOp().mousePressed(e);
			break;
		default:
			break;

		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (GameState.state) {
		case MENU:
			gp.getMenu().mouseReleased(e);
			break;
		case PLAYING:
			gp.getPlay().mouseReleased(e);
			break;
		case OPTIONS:
			gp.getGameOp().mouseReleased(e);
			break;
		default:
			break;

		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
