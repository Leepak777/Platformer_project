package entities;

import static utilz.Constants.Dialogue.*;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import gamestates.Play;

public class Shark extends Enemy {

	public Shark(float x, float y, Play play) {
		super(x, y, SHARK_WIDTH, SHARK_HEIGHT, SHARK, play);
		inithitBox(18, 22);
		initAttackBox(20, 20, 20);
	}

	public void update(int[][] lvlData, Play playing) {
		updateBehavior(lvlData, playing);
		updateAnimationTick();
		updateAttackBoxFlip();
	}

	private void updateBehavior(int[][] lvlData, Play playing) {
		if (firstUpdate)
			firstUpdateCheck(lvlData);

		if (inAir)
			inAirChecks(lvlData, playing);
		else {
			switch (state) {
			case IDLE:
				if (isFloor(hitbox, lvlData))
					newState(RUNNING);
				else
					inAir = true;
				break;
			case RUNNING:
				if (canSeePlayer(lvlData, playing.getPlayer())) {
					moveTowardPlayer(playing.getPlayer());
					if (isPlayerCloseAttack(playing.getPlayer()))
						newState(ATTACK);
				}

				move(lvlData);
				break;
			case ATTACK:
				if (aniIndex == 0)
					attackCheck = false;
				else if (aniIndex == 3) {
					if (!attackCheck)
						checkPlayerHit(attackbox, playing.getPlayer());
					attackMove(lvlData, playing);
				}

				break;
	 		case HIT:
				if (aniIndex <= GetSpriteAmount(enemyType, state) - 2)
					pushBack(pushBackDir, lvlData, 2f);
				updatePushBackDrawOffset();
				break;
			}
		}
	}

	protected void attackMove(int[][] lvlData, Play playing) {
		float xSpeed = 0;

		if (walkDir == LEFT)
			xSpeed = -walkSpeed;
		else
			xSpeed = walkSpeed;

		if (CanMoveHere(hitbox.x + xSpeed * 4, hitbox.y, hitbox.width, hitbox.height, lvlData))
			if (IsFloor(hitbox, xSpeed * 4, lvlData)) {
				hitbox.x += xSpeed * 4;
				return;
			}
		newState(IDLE);
		playing.addDialogue((int) hitbox.x, (int) hitbox.y, EXCLAMATION);
	}
}
