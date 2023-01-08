package gamestates;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.Game;
import main.GamePanel;
import ui.MenuButton;
import utilz.LoadSave;

public class Menu extends State implements StateMethods {
	private MenuButton[] buttons = new MenuButton[4];
	private BufferedImage backgroundImg;
	private BufferedImage background;
	private int menuX, menuY, menuWIDTH, menuHEIGHT;

	public Menu(GamePanel gp) {
		super(gp);
		loadButtons();
		loadBackGround();
		background = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUNDS_IMAGE);
	}

	private void loadBackGround() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUNDS);
		menuWIDTH = (int) (backgroundImg.getWidth() * GamePanel.SCALE);
		menuHEIGHT = (int) (backgroundImg.getHeight() * GamePanel.SCALE);
		menuX = GamePanel.GAME_WIDTH / 2 - menuWIDTH / 2;
		menuY = (int) (25 * GamePanel.SCALE);

	}

	private void loadButtons() {
		buttons[0] = new MenuButton(GamePanel.GAME_WIDTH / 2, (int) (130 * GamePanel.SCALE), 0, GameState.PLAYING);
		buttons[1] = new MenuButton(GamePanel.GAME_WIDTH / 2, (int) (200 * GamePanel.SCALE), 1, GameState.OPTIONS);
		buttons[2] = new MenuButton(GamePanel.GAME_WIDTH / 2, (int) (270 * GamePanel.SCALE), 3, GameState.CREDIT);
		buttons[3] = new MenuButton(GamePanel.GAME_WIDTH / 2, (int) (340 * GamePanel.SCALE), 2, GameState.QUIT);
	}

	@Override
	public void update() {
		for (MenuButton mb : buttons) {
			mb.update();
		}

	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT, null);

		g.drawImage(backgroundImg, menuX, menuY, menuWIDTH, menuHEIGHT, null);

		for (MenuButton mb : buttons) {
			mb.draw(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMousePressed(true);
				break;
			}
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				if (mb.isMousePressed()) {
					mb.applyGameState();
					if(mb.getState() == GameState.PLAYING) {
						gp.getAudioPlay().setLevelSong(gp.getPlay().getLevelM().getLvlIndex());
					}
					break;
				}
			}
		}
		resetButtons();
	}

	private void resetButtons() {
		for (MenuButton mb : buttons) {
			mb.resetBools();
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		for (MenuButton mb : buttons) {
			mb.setMouseOver(false);
		}
		for (MenuButton mb : buttons) {
			if (isIn(e, mb)) {
				mb.setMouseOver(true);
				break;
			}
		}

	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			GameState.state = GameState.PLAYING;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
