package gamestates;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import main.GamePanel;
import ui.AudioOptions;
import ui.PauseButton;
import ui.URMButton;
import utilz.LoadSave;

import static utilz.Constants.UI.URMButtons.*;

public class GameOptions extends State implements StateMethods {

	private AudioOptions audioOp;
	private BufferedImage backgroundImg, optionsBackgroundImg;
	private int bgX, bgY, bgH, bgW;
	private URMButton menuB;

	public GameOptions(GamePanel gp) {
		super(gp);
		loadImgs();
		loadButtons();
		audioOp = gp.getAudioOp();
	}

	private void loadImgs() {
		backgroundImg = LoadSave.GetSpriteAtlas(LoadSave.MENU_BACKGROUNDS_IMAGE);
		optionsBackgroundImg = LoadSave.GetSpriteAtlas(LoadSave.OPTIONS_BACKGROUND_IMAGE);
		bgW = (int) (optionsBackgroundImg.getWidth() * GamePanel.SCALE);
		bgH = (int) (optionsBackgroundImg.getHeight() * GamePanel.SCALE);
		bgX = GamePanel.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (33 * GamePanel.SCALE);
	}

	private void loadButtons() {
		int menuX = (int) (387 * GamePanel.SCALE);
		int menuY = (int) (325 * GamePanel.SCALE);
		menuB = new URMButton(menuX, menuY, URM_SIZE, URM_SIZE, 2);

	}

	@Override
	public void update() {
		menuB.update();
		audioOp.update();
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(backgroundImg, 0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT, null);
		g.drawImage(optionsBackgroundImg, bgX, bgY, bgW, bgH, null);
		menuB.draw(g);
		audioOp.draw(g);
	}

	public void mouseDragged(MouseEvent e) {
		audioOp.mouseDragged(e);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (isIn(e, menuB)) {
			menuB.setMousePressed(true);
		} else {
			audioOp.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (isIn(e, menuB)) {
			if (menuB.isMousePressed()) {
				GameState.state = GameState.MENU;
			}
		} 
		else {
			audioOp.mouseReleased(e);
		}

		menuB.resetBools();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		menuB.setMouseOver(false);
		
		if (isIn(e, menuB)) {
			menuB.setMouseOver(true);
		} else {
			audioOp.mouseMoved(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			GameState.state = GameState.MENU;
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}
	
	private boolean isIn(MouseEvent e, PauseButton b) {
		return (b.getBounds().contains(e.getX(), e.getY()));

	}
}
