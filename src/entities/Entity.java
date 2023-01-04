package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import gamestates.Play;
import main.GamePanel;

public abstract class Entity {
	protected float x, y;
	protected int width, height;
	protected Rectangle2D.Float hitbox;

	protected int aniIndex, aniTick;
	protected int hakiTick, hakiIndex;
	protected int state;
	protected float airSpeed;
	protected boolean inAir = false;
	protected int maxHealth, currentHealth;
	protected Rectangle2D.Float attackbox, hakibox, jumpbox;
	protected float walkSpeed;
	protected Play play;

	public Entity(float x, float y, int width, int height, Play play) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.play = play;
	}

	protected void drawHitBox(Graphics g, int grade, int xLvlOffset, int yLvlOffset) {
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y - yLvlOffset, (int) hitbox.width, (int) hitbox.height);
	}

	protected void inithitBox(int width, int height) {
		hitbox = new Rectangle2D.Float(x, y, (int) (width * GamePanel.SCALE), (int) (height * GamePanel.SCALE));
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public int getState() {
		return state;
	}

	protected void drawAttackBox(Graphics g, int xLvlOffset, int yLvlOffset) {
		g.setColor(Color.red);
		g.drawRect((int) (attackbox.x - xLvlOffset), (int) (attackbox.y - yLvlOffset), (int) attackbox.width,
				(int) attackbox.height);
	}

	protected void drawBox(Graphics g, int xLvlOffset, int yLvlOffset, Rectangle2D.Float box) {
		g.setColor(Color.red);
		g.drawRect((int) box.x - xLvlOffset, (int) box.y - yLvlOffset, (int) box.width, (int) box.height);

	}

	public int getAniIndex() {
		return aniIndex;
	}

}
