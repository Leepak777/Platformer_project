package ui;

import static utilz.Constants.UI.URMButtons.*;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.GameState;
import gamestates.Play;
import main.GamePanel;
import utilz.LoadSave;

public class LevelCompletedOverLay {

	private Play play;
	private URMButton menuB, nextB;
	private BufferedImage img;
	private int bgX, bgY, bgW, bgH;

	public LevelCompletedOverLay(Play play) {
		this.play = play;
		initImg();
		initButton();
	}

	private void initButton() {
		int menuX = (int) (330 * GamePanel.SCALE);
		int nextX = (int) (445 * GamePanel.SCALE);
		int y = (int) (195 * GamePanel.SCALE);
		menuB = new URMButton(menuX, y, URM_SIZE, URM_SIZE, 2);
		nextB = new URMButton(nextX, y, URM_SIZE, URM_SIZE, 0);

	}

	private void initImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_COMPLETED);
		bgW = (int) (img.getWidth() * GamePanel.SCALE);
		bgH = (int) (img.getHeight() * GamePanel.SCALE);
		bgX = (GamePanel.GAME_WIDTH / 2) - bgW / 2;
		bgY = (int) (75 * GamePanel.SCALE);

	}

	public void update() {
		nextB.update();
		menuB.update();
	}

	public void draw(Graphics g) {
		g.drawImage(img, bgX, bgY, bgW, bgH, null);
		menuB.draw(g);
		nextB.draw(g);
	}

	private boolean isIn(MouseEvent e, URMButton b) {
		return (b.getBounds().contains(e.getX(), e.getY()));

	}

	public void mouseMoved(MouseEvent e) {
		nextB.setMouseOver(false);
		menuB.setMouseOver(false);
		if (isIn(e, nextB)) {
			nextB.setMouseOver(true);
		} else if (isIn(e, menuB)) {
			menuB.setMouseOver(true);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, nextB)) {
			if (nextB.isMousePressed()) {
				play.loadNextLevel();
				play.getGame().getAudioPlay().setLevelSong(play.getLevelM().getLvlIndex());
			}
		} else if (isIn(e, menuB)) {
			if (menuB.isMousePressed()) {
				play.resetAll();
				play.setGameState(GameState.MENU);
			}
		}
		menuB.resetBools();
		nextB.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, nextB)) {
			nextB.setMousePressed(true);
		} else if (isIn(e, menuB)) {
			menuB.setMousePressed(true);
		}
	}
}
