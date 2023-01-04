package ui;

import static utilz.Constants.UI.URMButtons.URM_SIZE;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import gamestates.GameState;
import gamestates.Play;
import main.GamePanel;
import utilz.LoadSave;

public class GameOverOverLay {

	private Play play;
	private BufferedImage img;
	private int imgX, imgY, imgW, imgH;
	private URMButton menu, replay;
	public GameOverOverLay(Play play) {
		this.play = play;
		createimg();
		initButton();
	}
	private void initButton() {
		int menuX = (int) (330 * GamePanel.SCALE);
		int nextX = (int) (445 * GamePanel.SCALE);
		int y = (int) (195 * GamePanel.SCALE);
		menu = new URMButton(menuX, y, URM_SIZE, URM_SIZE, 2);
		replay = new URMButton(nextX, y, URM_SIZE, URM_SIZE, 0);

	}
	public void update() {
		menu.update();
		replay.update();
	}

	private void createimg() {
		img = LoadSave.GetSpriteAtlas(LoadSave.DEATH_SCREEN);
		imgW = (int) (img.getWidth() * GamePanel.SCALE);
		imgH = (int) (img.getHeight() * GamePanel.SCALE);
		imgX = (int) (GamePanel.GAME_WIDTH /2 - imgW/2);
		imgY = (int) (100 * GamePanel.SCALE);
	}

	public void draw(Graphics g) {
		g.setColor(new Color(0, 0, 0, 200));
		g.fillRect(0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT);
		
		g.drawImage(img, imgX, imgY, imgW, imgH, null);
		menu.draw(g);
		replay.draw(g);
		/*g.setColor(Color.white);
		g.drawString("GAME OVER", GamePanel.GAME_WIDTH / 2, 150);
		g.drawString("Press esc to enter Main Menu", GamePanel.GAME_WIDTH / 2, 300);*/
	}

	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			play.resetAll();
			GameState.state = GameState.MENU;
		}
	}
	private boolean isIn(MouseEvent e, URMButton b) {
		return (b.getBounds().contains(e.getX(), e.getY()));

	}

	public void mouseMoved(MouseEvent e) {
		replay.setMouseOver(false);
		menu.setMouseOver(false);
		if (isIn(e, replay)) {
			replay.setMouseOver(true);
		} else if (isIn(e, menu)) {
			menu.setMouseOver(true);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, replay)) {
			if (replay.isMousePressed()) {
				play.resetAll();
				play.unPause();
			}
		} else if (isIn(e, menu)) {
			if (menu.isMousePressed()) {
				play.resetAll();
				GameState.state = GameState.MENU;
				System.out.println("MENU");
			}
		}
		menu.resetBools();
		replay.resetBools();
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, replay)) {
			replay.setMousePressed(true);
		} else if (isIn(e, menu)) {
			menu.setMousePressed(true);
		}

	}
}
