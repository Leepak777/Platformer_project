package ui;

import static utilz.Constants.UI.URMButtons.*;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import utilz.LoadSave;

public class URMButton extends PauseButton{
	private BufferedImage[] imgs;
	private int rowIndex, index;
	private boolean mouseOver,mousePressed;
	
	public URMButton(int x, int y, int width, int height,int rowIndex) {
		super(x, y, width, height);
		this.rowIndex = rowIndex; 
		loadImages();
	}
	private void loadImages() {
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.URM_BUTTONS);
		imgs = new BufferedImage[3];
		for (int j = 0; j < imgs.length; j++) {
            	imgs[j]= temp.getSubimage(j * URM_SIZE_DEFAULT, rowIndex * URM_SIZE_DEFAULT, URM_SIZE_DEFAULT, URM_SIZE_DEFAULT);
        }
        
		
	}
	
	public void update() {
		index = 0;
		if(mouseOver) {
			index = 1;
		}
		if(mousePressed) {
			index= 2;
		}
	}
	
	public void draw(Graphics g) {
		g.drawImage(imgs[index], x,y, URM_SIZE, URM_SIZE, null);
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
	
}
