package entities;

import static utilz.Constants.EnemyConstants.*;

import static utilz.HelpMethods.*;
import static utilz.Constants.Directions.*;
import static utilz.HelpMethods.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;

import gamestates.Play;
import main.Game;
import main.GamePanel;
import static utilz.Constants.*;

public abstract class Enemy extends Entity {
	protected int enemyType;
	protected boolean firstUpdate = true;
	protected int walkDir = LEFT;
	protected int tileY;
	protected float attackDistance = GamePanel.TILE_SIZE;
	protected int attackboxOffsetX;
	protected boolean active = true;
	protected boolean attackCheck;
	protected boolean dead = false;

	public Enemy(float x, float y, int width, int height, int enemyType, Play play) {
		super(x, y, width, height, play);
		this.enemyType = enemyType;
		this.maxHealth = GetMaxHealth(enemyType);
		this.currentHealth = maxHealth;
		this.walkSpeed = 0.5f * GamePanel.SCALE;
	}

	protected void updateAttackBox() {
		attackbox.x = hitbox.x - attackboxOffsetX;
		attackbox.y = hitbox.y;
	}

	protected void updateAttackBoxFlip() {
		if (walkDir == RIGHT)
			attackbox.x = hitbox.x + hitbox.width;
		else
			attackbox.x = hitbox.x - attackboxOffsetX;

		attackbox.y = hitbox.y;
	}

	protected void initAttackBox(int w, int h, int attackBoxOffsetX) {
		attackbox = new Rectangle2D.Float(x, y, (int) (w * GamePanel.SCALE), (int) (h * GamePanel.SCALE));
		this.attackboxOffsetX = (int) (GamePanel.SCALE * attackBoxOffsetX);
	}

	protected void firstUpdateCheck(int[][] lvlData) {
		if (!onFloor(hitbox, lvlData)) {
			inAir = true;
		}
		firstUpdate = false;
	}
	protected void inAirChecks(int[][] lvlData, Play playing) {
		if (state != HIT && state != DEAD) {
			updateInAir(lvlData);
			playing.getOM().checkSpikesTouched(this);
			if (IsEntityInWater(hitbox, lvlData))
				hurt(maxHealth);
		}
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


	public void hurt(int amount) {
		currentHealth -= amount;
		if (currentHealth <= 0 && !dead) {
			newState(DEAD);
			dead = true;
		} else if (currentHealth > 0) {
			newState(HIT);
			if (walkDir == LEFT)
				pushBackDir = RIGHT;
			else
				pushBackDir = LEFT;
			pushBackOffsetDir = UP;
			pushDrawOffset = 0;
		}
	}

	protected void checkPlayerHit(Rectangle2D attackbox, Player player) {
		if (attackbox.intersects(player.hitbox)) {
			player.changeHealth(-GetEnemyDamage(enemyType), this);
		} else {
			if (enemyType == SHARK)
				return;

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
		switch (enemyType) {
		case CRABBY -> {
			return absValue <= attackDistance;
		}
		case SHARK -> {
			return absValue <= attackDistance * 2;
		}
		}
		return false;	}

	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANISPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(enemyType, state)) {
				if (enemyType == CRABBY || enemyType == SHARK) {
					aniIndex = 0;

					switch (state) {
					case ATTACK, HIT -> state = IDLE;
					case DEAD -> active = false;
					}
				} else if (enemyType == PINKSTAR) {
					if (state == ATTACK)
						aniIndex = 3;
					else {
						aniIndex = 0;
						if (state == HIT) {
							state = IDLE;

						} else if (state == DEAD)
							active = false;
					}
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
		dead = false;
		pushDrawOffset = 0;
	}
	public int flipX() {
		if (walkDir == RIGHT)
			return width;
		else
			return 0;
	}

	public int flipW() {
		if (walkDir == RIGHT)
			return -1;
		else
			return 1;
	}

	public float getPushDrawOffset() {
		return pushDrawOffset;
	}
}
