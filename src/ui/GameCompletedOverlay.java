package ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.GameState;
import gamestates.Play;
import main.Game;
import main.GamePanel;
import utilz.LoadSave;

public class GameCompletedOverlay {
	private Play playing;
	private BufferedImage img;
	private MenuButton quit, credit;
	private int imgX, imgY, imgW, imgH;

	public GameCompletedOverlay(Play playing) {
		this.playing = playing;
		createImg();
		createButtons();
	}

	private void createButtons() {
		quit = new MenuButton(GamePanel.GAME_WIDTH / 2, (int) (270 * GamePanel.SCALE), 2, GameState.MENU);
		credit = new MenuButton(GamePanel.GAME_WIDTH / 2, (int) (200 * GamePanel.SCALE), 3, GameState.CREDIT);
	}

	private void createImg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.GAME_COMPLETED);
		imgW = (int) (img.getWidth() * GamePanel.SCALE);
		imgH = (int) (img.getHeight() * GamePanel.SCALE);
		imgX = GamePanel.GAME_WIDTH / 2 - imgW / 2;
		imgY = (int) (100 * GamePanel.SCALE);

	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT);

		g.drawImage(img, imgX, imgY, imgW, imgH, null);

		credit.draw(g);
		quit.draw(g);
	}

	public void update() {
		credit.update();
		quit.update();
	}

	private boolean isIn(MenuButton b, MouseEvent e) {
		return b.getBounds().contains(e.getX(), e.getY());
	}

	public void mouseMoved(MouseEvent e) {
		credit.setMouseOver(false);
		quit.setMouseOver(false);

		if (isIn(quit, e))
			quit.setMouseOver(true);
		else if (isIn(credit, e))
			credit.setMouseOver(true);
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(quit, e)) {
			if (quit.isMousePressed()) {
				playing.resetAll();
				playing.resetGameCompleted();
				playing.setGameState(GameState.MENU);

			}
		} else if (isIn(credit, e))
			if (credit.isMousePressed()) {
				playing.resetAll();
				playing.resetGameCompleted();
				playing.setGameState(GameState.CREDIT);
			}

		quit.resetBools();
		credit.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(quit, e))
			quit.setMousePressed(true);
		else if (isIn(credit, e))
			credit.setMousePressed(true);
	}
}
