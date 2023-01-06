package entities;

import static utilz.Constants.Directions.DOWN;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.UP;
import static utilz.HelpMethods.CanMoveHere;

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
	protected int pushBackDir;
	protected float pushDrawOffset;
	protected int pushBackOffsetDir = UP;

	public Entity(float x, float y, int width, int height, Play play) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.play = play;
	}

	protected void updatePushBackDrawOffset() {
		float speed = 0.95f;
		float limit = -30f;

		if (pushBackOffsetDir == UP) {
			pushDrawOffset -= speed;
			if (pushDrawOffset <= limit)
				pushBackOffsetDir = DOWN;
		} else {
			pushDrawOffset += speed;
			if (pushDrawOffset >= 0)
				pushDrawOffset = 0;
		}
	}

	protected void pushBack(int pushBackDir, int[][] lvlData, float speedMulti) {
		float xSpeed = 0;
		if (pushBackDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed * speedMulti, hitbox.y, hitbox.width, hitbox.height, lvlData))
			hitbox.x += xSpeed * speedMulti;
	}

	protected void drawHitBox(Graphics g, int xLvlOffset, int yLvlOffset) {
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
	protected void newState(int state) {
		this.state = state;
		aniTick = 0;
		aniIndex = 0;
	}
}
