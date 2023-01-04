package entities;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import gamestates.Play;
import main.GamePanel;
import static utilz.Constants.*;

public abstract class Enemy extends Entity {
	protected int enemyType;
	protected boolean firstUpdate = true;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = GamePanel.TILE_SIZE;

	protected boolean active = true;
	protected boolean attackCheck;

	public Enemy(float x, float y, int width, int height, int enemyType, Play play) {
		super(x, y, width, height, play);
		this.enemyType = enemyType;
		this.maxHealth = GetMaxHealth(enemyType);
		this.currentHealth = maxHealth;
		this.walkSpeed = 0.5f * GamePanel.SCALE;
	}

	protected void firstUpdateCheck(int[][] lvlData) {
		if (!onFloor(hitbox, lvlData)) {
			inAir = true;
		}
		firstUpdate = false;
	}

	protected void updateInAir(int[][] lvlData) {
		if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
			hitbox.y += airSpeed;
			airSpeed += GRAVITY;
		} else {
			inAir = false;
			hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
			tileY = (int) (hitbox.y / GamePanel.TILE_SIZE);
		}
	}

	protected void move(int[][] lvlData) {
		float xSpeed = 0;
		if (walkDir == LEFT) {
			xSpeed = -walkSpeed;
		} else {
			xSpeed = walkSpeed;
		}
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)) {
			if (IsFloor(hitbox, xSpeed, lvlData)) {
				hitbox.x += xSpeed;
				return;
			}
		}
		changeWalkDir();
	}

	protected void newState(int enemyState) {
		this.state = enemyState;
		aniTick = 0;
		aniIndex = 0;
	}

	public void hurt(int amount) {
		currentHealth -= amount;
		if (currentHealth <= 0) {
			newState(DEAD);
		} else {
			newState(HIT);
		}
	}

	protected void checkPlayerHit(Rectangle2D attackbox, Player player) {
		if (attackbox.intersects(player.hitbox)) {
			player.changeHealthint(-GetEnemyDamage(enemyType));
		}
		attackCheck = true;

	}

	protected void moveTowardPlayer(Player player) {
		if (player.hitbox.x > hitbox.x) {
			walkDir = RIGHT;
		} else {
			walkDir = LEFT;
		}
	}

	protected boolean canSeePlayer(int[][] lvlData, Player player) {
		int playerTileY = (int) (player.getHitbox().y / GamePanel.TILE_SIZE);
		if (playerTileY == tileY) {
			if (playerInRange(player)) {
				if (isSightClear(lvlData, hitbox, player.hitbox, tileY)) {
					return true;

				}
			}
		}

		return false;
	}

	protected boolean playerInRange(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - this.hitbox.x);
		return absValue <= attackDistance * 4;
	}

	protected boolean isPlayerCloseAttack(Player player) {
		int absValue = (int) Math.abs(player.hitbox.x - this.hitbox.x);
		return absValue <= attackDistance;
	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANISPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, state)) {
				aniIndex = 0;
				switch (state) {
				case ATTACK, HIT -> state = IDLE;
				case DEAD -> active = false;
				}

			}
		}
	}

	protected void changeWalkDir() {
		if (walkDir == LEFT) {
			walkDir = RIGHT;
		} else {
			walkDir = LEFT;
		}

	}

	public boolean isActive() {
		return active;
	}

	public void resetEnemy() {
		hitbox.x = x;
		hitbox.y = y;
		firstUpdate = true;
		currentHealth = maxHealth;
		newState(IDLE);
		active = true;
		airSpeed = 0;
	}
}
