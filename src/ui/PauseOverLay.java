package ui;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.GameState;
import gamestates.Play;

import static utilz.Constants.UI.PauseButtons.*;
import static utilz.Constants.UI.URMButtons.*;
import static utilz.Constants.UI.VolumeButtons.*;

import main.GamePanel;
import utilz.LoadSave;

public class PauseOverLay {

	private BufferedImage background;
	private int bgX, bgY, bgW, bgH;
	private URMButton menuB, replayB, unpauseB;
	private Play play;
	private AudioOptions audioOp;

	public PauseOverLay(Play play) {
		this.play = play;
		createURMButtons();
		audioOp = play.getGame().getAudioOp();
		loadBackground();
	}

	private void createURMButtons() {
		int menuX = (int) (313 * GamePanel.SCALE);
		int replayX = (int) (387 * GamePanel.SCALE);
		int unpauseX = (int) (462 * GamePanel.SCALE);
		int bY = (int) (325 * GamePanel.SCALE);
		menuB = new URMButton(menuX, bY, URM_SIZE, URM_SIZE, 2);
		replayB = new URMButton(replayX, bY, URM_SIZE, URM_SIZE, 1);
		unpauseB = new URMButton(unpauseX, bY, URM_SIZE, URM_SIZE, 0);

	}

	private void loadBackground() {
		background = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgW = (int) (background.getWidth() * GamePanel.SCALE);
		bgH = (int) (background.getHeight() * GamePanel.SCALE);
		bgX = GamePanel.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (25 * GamePanel.SCALE);
	}

	public void update() {

		replayB.update();
		menuB.update();
		unpauseB.update();
		audioOp.update();
	}

	public void draw(Graphics g) {
		g.drawImage(background, bgX, bgY, bgW, bgH, null);
		// URMS
		replayB.draw(g);
		menuB.draw(g);
		unpauseB.draw(g);
		audioOp.draw(g);
	}

	public void mouseDragged(MouseEvent e) {
		audioOp.mouseDragged(e);
	}

	public void mousePressed(MouseEvent e) {

		if (isIn(e, menuB)) {
			menuB.setMousePressed(true);
		} else if (isIn(e, replayB)) {
			replayB.setMousePressed(true);
		} else if (isIn(e, unpauseB)) {
			unpauseB.setMousePressed(true);
		} else {
			audioOp.mousePressed(e);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed()) {
				play.setGameState(GameState.MENU);
				play.unPause();
			}
		} else if (isIn(e, replayB)) {
			if (replayB.isMousePressed()) {
				play.resetAll();
				play.unPause();
			}
		} else if (isIn(e, unpauseB)) {
			if (unpauseB.isMousePressed()) {
				play.unPause();
			}
		}
		else {
			audioOp.mouseReleased(e);
		}

		unpauseB.resetBools();
		replayB.resetBools();
		menuB.resetBools();
	}

	public void mouseMoved(MouseEvent e) {

		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);
		if (isIn(e, menuB)) {
			menuB.setMouseOver(true);
		} else if (isIn(e, replayB)) {
			replayB.setMouseOver(true);
		} else if (isIn(e, unpauseB)) {
			unpauseB.setMouseOver(true);
		}
		else {
			audioOp.mouseMoved(e);
		}
	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return (b.getBounds().contains(e.getX(), e.getY()));

	}
}
