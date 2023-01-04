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
	private SoundButton musicButton, sfxButton;
	private URMButton menuB, replayB, unpauseB;
	private Play play;
	private VolumeButton volume;

	public PauseOverLay(Play play) {
		this.play = play;
		createSoundButton();
		createURMButtons();
		createVolumeButton();
		loadBackground();
	}

	private void createVolumeButton() {
		int vX = (int) (309 * GamePanel.SCALE);
		int vY = (int) (278 * GamePanel.SCALE);
		volume = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
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

	private void createSoundButton() {
		int soundX = (int) (450 * GamePanel.SCALE);
		int musicY = (int) (140 * GamePanel.SCALE);
		int sfxY = (int) (186 * GamePanel.SCALE);
		musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
		sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);

	}

	private void loadBackground() {
		background = LoadSave.GetSpriteAtlas(LoadSave.PAUSE_BACKGROUND);
		bgW = (int) (background.getWidth() * GamePanel.SCALE);
		bgH = (int) (background.getHeight() * GamePanel.SCALE);
		bgX = GamePanel.GAME_WIDTH / 2 - bgW / 2;
		bgY = (int) (25 * GamePanel.SCALE);
	}

	public void update() {
		musicButton.update();
		sfxButton.update();
		replayB.update();
		menuB.update();
		unpauseB.update();
		volume.update();
	}

	public void draw(Graphics g) {
		g.drawImage(background, bgX, bgY, bgW, bgH, null);
		// SoundButton
		musicButton.draw(g);
		// sfxButton
		sfxButton.draw(g);
		// URMS
		replayB.draw(g);
		menuB.draw(g);
		unpauseB.draw(g);
		// volume
		volume.draw(g);
	}

	public void mouseDragged(MouseEvent e) {
		if (volume.isMousePressed()) {
			volume.changeX(e.getX());
		}
	}

	public void mousePressed(MouseEvent e) {
		if (isIn(e, musicButton)) {
			musicButton.setMousePressed(true);
		} else if (isIn(e, sfxButton)) {
			sfxButton.setMousePressed(true);
		} else if (isIn(e, menuB)) {
			menuB.setMousePressed(true);
		} else if (isIn(e, replayB)) {
			replayB.setMousePressed(true);
		} else if (isIn(e, unpauseB)) {
			unpauseB.setMousePressed(true);
		} else if (isIn(e, volume)) {
			volume.setMousePressed(true);
		}

	}

	public void mouseReleased(MouseEvent e) {
		if (isIn(e, musicButton)) {
			if (musicButton.isMousePressed()) {
				musicButton.setMuted(!musicButton.isMuted());
			}
		} else if (isIn(e, sfxButton)) {
			if (sfxButton.isMousePressed()) {
				sfxButton.setMuted(!sfxButton.isMuted());
			}
		} else if (isIn(e, menuB)) {
			if (menuB.isMousePressed()) {
				GameState.state = GameState.MENU;
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

		musicButton.resetBools();
		sfxButton.resetBools();
		unpauseB.resetBools();
		replayB.resetBools();
		menuB.resetBools();
		volume.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		menuB.setMouseOver(false);
		replayB.setMouseOver(false);
		unpauseB.setMouseOver(false);
		volume.setMouseOver(false);
		if (isIn(e, musicButton)) {
			musicButton.setMouseOver(true);
		} else if (isIn(e, sfxButton)) {
			sfxButton.setMouseOver(true);
		} else if (isIn(e, menuB)) {
			menuB.setMouseOver(true);
		} else if (isIn(e, replayB)) {
			replayB.setMouseOver(true);
		} else if (isIn(e, unpauseB)) {
			unpauseB.setMouseOver(true);
		} else if (isIn(e, volume)) {
			volume.setMouseOver(true);
		}

	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return (b.getBounds().contains(e.getX(), e.getY()));

	}
}
