package entities;

import main.GamePanel;

import static utilz.Constants.Directions.*;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import gamestates.Play;

public class Crabby extends Enemy {

	private int attackboxOffsetX;

	public Crabby(float x, float y, Play play) {
		super(x, y, CRABBY_WIDTH, CRABBY_HEIGHT, CRABBY, play);
		inithitBox((int) (22), (int) (19));
		initAttackBox();
	}

	private void initAttackBox() {
		attackbox = new Rectangle2D.Float(x, y, (int) (82 * GamePanel.SCALE), (int) (19 * GamePanel.SCALE));
		attackboxOffsetX = (int) (GamePanel.SCALE * 30);
	}

	public void update(int[][] lvlData, Player player) {
		updateBehaviour(lvlData, player);
		updateAnimationTick();
		updateAttackBox();
	}

	private void updateAttackBox() {
		attackbox.x = hitbox.x - attackboxOffsetX;
		attackbox.y = hitbox.y;

	}

	private void updateBehaviour(int[][] lvlData, Player player) {
		if (firstUpdate) {
			firstUpdateCheck(lvlData);
		}
		if (inAir) {
			updateInAir(lvlData);

		} else {
			switch (state) {
			case IDLE:
				newState(RUNNING);
				break;
			case RUNNING: {
				if (canSeePlayer(lvlData, player)) {
					moveTowardPlayer(player);
					if (isPlayerCloseAttack(player)) {
						state = ATTACK;
					}
				}
				move(lvlData);
				break;
			}
			case ATTACK:
				if (aniIndex == 0) {
					attackCheck = false;
				}
				if (aniIndex == 3 && !attackCheck) {
					checkPlayerHit(attackbox, player);
				}
				break;
			case HIT:
				break;
			}
		}
	}

	public int flipX() {
		if (walkDir == RIGHT) {
			return width;
		} else {
			return 0;
		}
	}

	public int flipW() {
		if (walkDir == RIGHT) {
			return -1;
		} else {
			return 1;
		}
	}

}
