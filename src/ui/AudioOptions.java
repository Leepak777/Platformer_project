package ui;

import static utilz.Constants.UI.PauseButtons.SOUND_SIZE;
import static utilz.Constants.UI.VolumeButtons.SLIDER_WIDTH;
import static utilz.Constants.UI.VolumeButtons.VOLUME_HEIGHT;

import java.awt.Graphics;
import java.awt.event.MouseEvent;

import gamestates.GameState;
import main.GamePanel;

public class AudioOptions {
	private VolumeButton volume;
	private SoundButton musicButton, sfxButton;
	
	public AudioOptions() {
		createSoundButton();
		createVolumeButton();
	}
	private void createVolumeButton() {
		int vX = (int) (309 * GamePanel.SCALE);
		int vY = (int) (278 * GamePanel.SCALE);
		volume = new VolumeButton(vX, vY, SLIDER_WIDTH, VOLUME_HEIGHT);
	}
	private void createSoundButton() {
		int soundX = (int) (450 * GamePanel.SCALE);
		int musicY = (int) (140 * GamePanel.SCALE);
		int sfxY = (int) (186 * GamePanel.SCALE);
		musicButton = new SoundButton(soundX, musicY, SOUND_SIZE, SOUND_SIZE);
		sfxButton = new SoundButton(soundX, sfxY, SOUND_SIZE, SOUND_SIZE);
	}
	
	public void update() {
		musicButton.update();
		sfxButton.update();
		volume.update();
	}
	
	public void draw(Graphics g) {
		// SoundButton
		musicButton.draw(g);
		// sfxButton
		sfxButton.draw(g);
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
		} 

		musicButton.resetBools();
		sfxButton.resetBools();

		volume.resetBools();
	}

	public void mouseMoved(MouseEvent e) {
		musicButton.setMouseOver(false);
		sfxButton.setMouseOver(false);
		volume.setMouseOver(false);
		if (isIn(e, musicButton)) {
			musicButton.setMouseOver(true);
		} else if (isIn(e, sfxButton)) {
			sfxButton.setMouseOver(true);
		} else if (isIn(e, volume)) {
			volume.setMouseOver(true);
		}

	}

	private boolean isIn(MouseEvent e, PauseButton b) {
		return (b.getBounds().contains(e.getX(), e.getY()));

	}
}
