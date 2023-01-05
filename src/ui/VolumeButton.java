package ui;

import static utilz.Constants.UI.VolumeButtons.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;

public class VolumeButton extends PauseButton {
	private BufferedImage[] buttonImgs;
	private BufferedImage sliderImg;
	private int index = 0;
	private boolean mouseOver, mousePressed;
	private int buttonX, minX, maxX;
	private float floatVal;
	public VolumeButton(int x, int y, int width, int height) {
		super(x + width / 2, y, VOLUME_WIDTH, height);
		bounds.x -= VOLUME_WIDTH / 2;
		this.buttonX = x + width / 2;
		this.x = x;
		this.width = width;
		minX = x + VOLUME_WIDTH / 2;
		maxX = x + width - VOLUME_WIDTH / 2;
		loadImages();
		// TODO Auto-generated constructor stub
	}

	private void loadImages() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.VOLUME_BUTTONS);
		buttonImgs = new BufferedImage[3];
		for (int j = 0; j < buttonImgs.length; j++) {
			buttonImgs[j] = temp.getSubimage(j * VOLUME_WIDTH_DEFAULT, 0, VOLUME_WIDTH_DEFAULT, VOLUME_HEIGHT_DEFAULT);
		}
		sliderImg = temp.getSubimage(3 * VOLUME_WIDTH_DEFAULT, 0, SLIDER_WIDTH_DEFAULT, VOLUME_HEIGHT_DEFAULT);

	}

	public void update() {
		index = 0;
		if (mouseOver) {
			index = 1;
		}
		if (mousePressed) {
			index = 2;
		}
	}

	public void draw(Graphics g) {
		g.drawImage(sliderImg, x, y, width, height, null);
		g.drawImage(buttonImgs[index], buttonX - VOLUME_WIDTH / 2, y, VOLUME_WIDTH, height, null);
	}

	public void changeX(int x) {
		if (x <= minX) {
			buttonX = minX;
		} else if (x >= maxX) {
			buttonX = maxX;
		} else {
			buttonX = x;
		}
		updateFloatVal();
		bounds.x = buttonX - VOLUME_WIDTH / 2;
	}

	private void updateFloatVal() {
		float range = maxX - minX;
		float value = buttonX - minX;
		floatVal = value / range;
		
	}

	public void resetBools() {
		mouseOver = false;
		mousePressed = false;
	}

	public boolean isMouseOver() {
		return mouseOver;
	}

	public void setMouseOver(boolean mouseOver) {
		this.mouseOver = mouseOver;
	}

	public boolean isMousePressed() {
		return mousePressed;
	}

	public void setMousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}

	public float getFloatval() {
		return floatVal;
	}
}
