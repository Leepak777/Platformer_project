package entities;

import main.Game;

import main.GamePanel;
import objects.Projectile;

import static utilz.Constants.*;
import static utilz.Constants.Directions.LEFT;
import static utilz.Constants.Directions.RIGHT;
import static utilz.Constants.Directions.UP;
import static utilz.HelpMethods.*;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import audio.AudioPlayer;
import gamestates.Play;

import static utilz.Constants.PlayerConstants.*;

public class Player extends Entity {
	// Sprite
	private BufferedImage[][] animations;
	private BufferedImage[][] haki;
	// Action boolean
	private boolean left, right, jump;
	private boolean moving = false;
	private boolean attacking = false;
	private boolean hakiAttack = false;
	// Map drawing data
	private int[][] lvlData;
	private float xDrawOffset = 21 * GamePanel.SCALE;
	private float yDrawOffset = 4 * GamePanel.SCALE;
	// Jump
	private int jumptick = 0;
	private float jumpSpeed_base = -2.569f * GamePanel.SCALE;
	private float jumpSpeed_act = jumpSpeed_base;
	private float fallSpeedAfterCollision = 0.5f * GamePanel.SCALE;
	private boolean passTick = false;;
	// StatusBarUI
	BufferedImage statusimg;
	private int statusBar_WIDTH = (int) (192 * GamePanel.SCALE);
	private int statusBar_HEIGHT = (int) (58 * GamePanel.SCALE);
	private int statusBar_X = (int) (10 * GamePanel.SCALE);
	private int statusBar_Y = (int) (10 * GamePanel.SCALE);
	private int healthBar_WIDTH = (int) (150 * GamePanel.SCALE);
	private int healthBar_HEIGHT = (int) (4 * GamePanel.SCALE);
	private int healthBar_X = (int) (34 * GamePanel.SCALE);
	private int healthBar_Y = (int) (14 * GamePanel.SCALE);
	private int healthWidth = healthBar_WIDTH;
	// powerBar UI
	private int powerBar_WIDTH = (int) (104 * GamePanel.SCALE);
	private int powerBar_HEIGHT = (int) (2 * GamePanel.SCALE);
	private int powerBar_X = (int) (44 * GamePanel.SCALE);
	private int powerBar_Y = (int) (34 * GamePanel.SCALE);
	private int powerWidth = powerBar_WIDTH;
	private int powerMaxValue = 200000;
	private int powerValue = powerMaxValue;
	// player left right flip
	private int flipX = 0;
	private int flipW = 1;
	private int flipY = 0;
	private int flipH = 1;
	// Check if can attack
	private boolean attackCheck, hakiattackCheck;
	private int hakiDmg = 2;
	// object check
	private int spikeTick = 0;
	private int tileY = 0;
	// bounce and fall check
	private boolean bounce = false;
	private boolean fallMove = true;
	// Power Attack
	private boolean powerAttackActive;
	private int powerAttackTick;
	private int powerGrowSpeed = 15;
	private int powerGrowTick;

	public Player(float x, float y, int width, int height, Play play) {
		super(x, y, width, height, play);
		this.state = IDLE;
		this.maxHealth = 100;
		this.currentHealth = maxHealth;
		this.walkSpeed = 1.000069f * GamePanel.SCALE;
		loadAnimations();
		inithitBox((int) (20), (int) (27));
		initAttackBox();
	}

	// update
	public void update() {
		spikeTick++;
		updateHealthBar();
		updatePowerBar();
		if (currentHealth <= 0) {
			if (state != DEAD) {
				state = DEAD;
				aniTick = 0;
				aniIndex = 0;
				play.setPlayerDying(true);
				play.getGame().getAudioPlay().playEffect(AudioPlayer.DIE);
				if (!onFloor(hitbox, lvlData)) {
					inAir = true;
					airSpeed = 0;
				}
			} else if (aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANISPEED - 1) {
				play.setGameOver(true);
				play.getGame().getAudioPlay().stopSong();
				play.getGame().getAudioPlay().playEffect(AudioPlayer.GAMEOVER);
			} else {
				updateAnimationTick();
				if (inAir)
					if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
						hitbox.y += airSpeed;
						airSpeed += GRAVITY;
					} else
						inAir = false;

			}

			return;
		}

		updateAttackBox(play.getxLvlOffset(), play.getyLvlOffset());
		if (state == HIT) {
			if (aniIndex <= GetSpriteAmount(state) - 3)
				pushBack(pushBackDir, lvlData, 0.69f);
			updatePushBackDrawOffset();
		} else {
			updatePos();
		}
		if (moving) {
			checkPotionTouched();
			checkInsideWater();
			tileY = (int) (hitbox.y / GamePanel.TILE_SIZE);
			if (powerAttackActive) {
				powerAttackTick++;
				if (powerAttackTick >= 35) {
					powerAttackTick = 0;
					powerAttackActive = false;
				}
			}
		}
		if (spikeTick >= 100) {
			checkSpikesTouch();
			spikeTick = 0;
		}
		if (attacking || powerAttackActive) {
			checkAttack();
		}
		if (hakiAttack) {
			checkHakiAttack();
		}
		updateAnimationTick();
		updateHakiTick();
		setAnimation();

	}

	// update Position
	private void updatePos() {
		moving = false;
		if (!onFloor(hitbox, lvlData)) {
			inAir = true;
		}
		if (onFloor(hitbox, lvlData)) {
			if (!fallMove)
				fallMove = true;
		}
		if (isOnEntity(hitbox, play.getEnemylst()) && fallMove) {
			jump = true;
		}
		if (jump) {
			jump();
		}
		if (!inAir) {
			if (!powerAttackActive) {
				if ((!left && !right) || (right && left)) {
					return;
				}
			}
		}

		float xSpeed = 0;
		if (left && !right) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right && !left) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}
		if (powerAttackActive) {
			if ((!left && !right) || (left && right)) {
				if (flipW == -1) {
					xSpeed = -walkSpeed;
				} else {
					xSpeed = walkSpeed;
				}
			}
			xSpeed *= 3;
		}
		if (!inAir) {
			if (!onFloor(hitbox, lvlData)) {
				inAir = true;
			}
		}
		if (inAir && !powerAttackActive) {
			resetJumpTick();
			if (CanMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, lvlData)) {
				hitbox.y += airSpeed;
				airSpeed += GRAVITY;
				updateXPos(xSpeed);
			} else {
				hitbox.y = GetEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
				if (airSpeed > 0) {
					resetInAir();
				} else {
					airSpeed = fallSpeedAfterCollision;
				}
				updateXPos(xSpeed);
			}

		} else {
			updateXPos(xSpeed);
		}
		moving = true;
	}

	private void updateXPos(float xSpeed) {
		if (state == FALLING && !fallMove) {
			return;
		}
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)
				&& (!isEntityX(hitbox, xSpeed, play.getEnemylst()) || powerAttackActive)) {
			hitbox.x += xSpeed;
		} else {
			if (!CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData) && powerAttackActive) {
				powerAttackActive = false;
				fallMove = false;
				powerAttackTick = 0;
			}
			checkBounce(xSpeed);
			if (isEntityX(hitbox, xSpeed, play.getEnemylst())) {
				hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed, true);
			} else {
				hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed, false);
			}
		}
	}

	// update misc
	private void updateAttackBox(int xLvlOffset, int yLvlOffset) {
		if (right && left) {
			if (flipW == 1) {
				setAttackBoxOnRightSide();
			} else {
				setAttackBoxOnLeftSide();
			}

		} else if (right || (powerAttackActive && flipW == 1))
			setAttackBoxOnRightSide();
		else if (left || (powerAttackActive && flipW == -1))
			setAttackBoxOnLeftSide();
		hakibox.x = (int) (hitbox.x - xDrawOffset / 1.5);
		hakibox.y = (int) (hitbox.y - yDrawOffset * 6.9);
		jumpbox.x = (int) (hitbox.x + hitbox.width / 8);
		jumpbox.y = (int) (hitbox.y + hitbox.height + 5);

		attackbox.y = hitbox.y + (int) (GamePanel.SCALE * 10);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBar_WIDTH);

	}

	private void updatePowerBar() {
		powerWidth = (int) ((powerValue / (float) powerMaxValue) * powerBar_WIDTH);
		powerGrowTick++;
		if (powerGrowTick >= powerGrowSpeed) {
			powerGrowTick = 0;
			changePower(1);
		}
	}

	// update tick
	public void updateJumpTick() {
		increaseJumpTick();
		jumpSpeed_act -= 0.0569420f;
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANISPEED) {
			aniTick = 0;
			aniIndex++;
			if (fallMove && (state == FALLING || state == IDLE)) {
				play.checkEnemyHit(jumpbox, 5);
				play.checkObjectHit(jumpbox);
			}
			if (aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				if (state == FALLING) {
					fallingCheck();
				}
				jump = false;
				attackCheck = false;
				if (state == HIT) {
					newState(IDLE);
					airSpeed = 0f;
					if (!IsFloor(hitbox, 0, lvlData))
						inAir = true;
				}
			}
		}
	}

	private void updateHakiTick() {
		hakiTick++;
		if (hakiTick >= 30) {
			hakiTick = 0;
			hakiIndex++;
			if (hakiIndex >= 3) {
				hakiIndex = 0;
				hakiattackCheck = false;
			}
		}
	}

	// draw
	public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
		if (hakiattackCheck) {
			drawHaki(g, xLvlOffset, yLvlOffset);
		}

		g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - xLvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset) - yLvlOffset + flipY, width * flipW, height * flipH, null);
		drawAttackBox(g, xLvlOffset, yLvlOffset);
		drawBox(g, xLvlOffset, yLvlOffset, jumpbox);
		// drawHitBox(g, 0, xLvlOffset, yLvlOffset);
		drawUI(g);
	}

	private void drawHaki(Graphics g, int xLvlOffset, int yLvlOffset) {
		if (jumptick > 0 && jumptick < 7) {
			hakiDmg = 2;
			g.drawImage(haki[0][hakiIndex], (int) (hitbox.x - xDrawOffset / 1.5) - xLvlOffset,
					(int) (hitbox.y - yDrawOffset * 6.9) - yLvlOffset, (int) (hitbox.width * 2.5),
					(int) (hitbox.height * 2), null);
		}
		if (jumptick > 7 && jumptick < 14) {
			hakiDmg = 3;
			g.drawImage(haki[1][hakiIndex], (int) (hitbox.x - xDrawOffset / 1.5) - xLvlOffset,
					(int) (hitbox.y - yDrawOffset * 6.9) - yLvlOffset, (int) (hitbox.width * 2.5),
					(int) (hitbox.height * 2), null);
		}
		if (jumptick > 14 && jumptick < 21) {
			hakiDmg = 4;
			g.drawImage(haki[2][hakiIndex], (int) (hitbox.x - xDrawOffset / 1.5) - xLvlOffset,
					(int) (hitbox.y - yDrawOffset * 6.9) - yLvlOffset, (int) (hitbox.width * 2.5),
					(int) (hitbox.height * 2), null);
		}
		if (jumptick > 21) {
			hakiDmg = 5;
			g.drawImage(haki[3][hakiIndex], (int) (hitbox.x - xDrawOffset / 1.5) - xLvlOffset,
					(int) (hitbox.y - yDrawOffset * 6.9) - yLvlOffset, (int) (hitbox.width * 2.5),
					(int) (hitbox.height * 2), null);
		}
	}

	private void drawUI(Graphics g) {
		g.drawImage(statusimg, statusBar_X, statusBar_Y, statusBar_WIDTH, statusBar_HEIGHT, null);
		g.setColor(Color.red);
		g.fillRect(healthBar_X + statusBar_X, healthBar_Y + statusBar_Y, healthWidth, healthBar_HEIGHT);

		g.setColor(Color.blue);
		g.fillRect(powerBar_X + statusBar_X, powerBar_Y + statusBar_Y, powerWidth, powerBar_HEIGHT);
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	// load, init
	private void initAttackBox() {
		attackbox = new Rectangle2D.Float(x, y, (int) (20 * GamePanel.SCALE), (int) (20 * GamePanel.SCALE));
		hakibox = new Rectangle2D.Float(x, y, (int) (hitbox.width * 2.5), (int) (hitbox.height * 3));
		jumpbox = new Rectangle2D.Float(x + hitbox.width / 6, y + 5, (int) (hitbox.width / 1.1),
				(int) (hitbox.height / 1.6));
		resetAttackbox();
	}

	private void loadAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.PLAYER_ATLAS);
		animations = new BufferedImage[7][8];
		for (int j = 0; j < animations.length; j++) {
			for (int i = 0; i < animations[j].length; i++) {
				animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
			}
		}
		img = LoadSave.GetSpriteAtlas(LoadSave.HAKI_2_ATLAS);
		haki = new BufferedImage[4][3];
		for (int j = 0; j < haki.length; j++) {
			for (int i = 0; i < haki[j].length; i++) {
				haki[j][i] = img.getSubimage(i * 64, j * 64, 64, 64);
			}
		}
		statusimg = LoadSave.GetSpriteAtlas(LoadSave.STATUS_BAR);

	}

	public void loadlvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!onFloor(hitbox, lvlData)) {
			inAir = true;
		}
	}

	// check
	private void checkHakiAttack() {
		if (hakiattackCheck) {
			return;
		}
		hakiattackCheck = true;
		play.checkEnemyHit(hakibox, hakiDmg);
		play.checkObjectHit(hakibox);
	}

	private void checkAttack() {
		if (attackCheck || aniIndex != 1) {
			return;
		}
		attackCheck = true;
		Rectangle2D.Float box = attackbox;
		int damage = 25;
		if (powerAttackActive) {
			// box = hakibox;
			damage = 100;
			attackCheck = false;
		}

		play.checkEnemyHit(box, damage);
		play.checkObjectHit(box);
		play.getGame().getAudioPlay().playAttack();
	}

	private void fallingCheck() {
		if ((play.getCode2() > 0 && jump && (right || left)) || bounce) {
			bounce = false;
			if (left) {
				left = false;
			}
			if (right) {
				right = false;
			}
		}
	}

	// set
	private void setAttackBoxOnRightSide() {
		attackbox.x = hitbox.x + hitbox.width + (int) (GamePanel.SCALE * 10);
	}

	private void setAttackBoxOnLeftSide() {
		attackbox.x = hitbox.x - hitbox.width - (int) (GamePanel.SCALE * 10);
	}

	private void setAnimation() {
		int startAni = state;
		if (state == HIT)
			return;
		if (moving) {
			state = RUNNING;
		} else {
			state = IDLE;
			if (startAni == FALLING)
				fallingCheck();
		}
		if (inAir) {
			if (airSpeed < 0) {
				state = JUMP;
			} else {
				state = FALLING;
			}
		}
		if (powerAttackActive) {
			state = ATTACK;
			aniIndex = 1;
			aniTick = 0;
		}
		if (attacking) {
			state = ATTACK;
			if (startAni != ATTACK) {
				aniIndex = 1;
				aniTick = 0;
				return;
			}
		}
		if (startAni != state) {
			resetAniTick();
		}
	}

	

	private void checkInsideWater() {
		if (IsEntityInWater(hitbox, play.getLevelM().getCurrentLevel().getLevelData()))
			currentHealth = 0;
	}

	private void checkSpikesTouch() {
		play.checkSpikesTouched();

	}

	private void checkPotionTouched() {
		play.checkPotionTouched(hitbox);
	}

	private void jump() {
		if (inAir) {
			return;
		}
		play.getGame().getAudioPlay().playEffect(AudioPlayer.JUMP);
		inAir = true;
		airSpeed = jumpSpeed_act;
	}
	

	private void checkBounce(float xSpeed) {
		if (!CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData) && state == JUMP) {
			jump = true;
			bounce = true;
			fallMove = false;
			if (left && !right) {
				left = false;
				right = true;
			} else if (right && !left) {
				left = true;
				right = false;
			}
			return;
		}
	}

	public void changeHealthint(int value, Boolean isEn) {
		if (value < 0 && isEn) {
			if (state == HIT)
				return;
			else
				newState(HIT);
		}

		currentHealth += value;
		currentHealth = Math.max(Math.min(currentHealth, maxHealth), 0);

	}

	public void changeHealth(int value, Entity e, Projectile p) {
		if (state == HIT)
			return;
		changeHealthint(value, true);
		pushBackOffsetDir = UP;
		pushDrawOffset = 0;
		if (e != null) {
			if (e.getHitbox().x < hitbox.x)
				pushBackDir = RIGHT;
			else
				pushBackDir = LEFT;
		}
		if (p != null) {
			if (p.getHitbox().x < hitbox.x)
				pushBackDir = RIGHT;
			else
				pushBackDir = LEFT;
		}
	}

	public void changePower(int value) {
		powerValue += value;
		if (powerValue >= powerMaxValue) {
			powerValue = powerMaxValue;
		} else if (powerValue <= 0) {
			powerValue = 0;
		}
	}

	public void loadLvlData(int[][] lvlData) {
		this.lvlData = lvlData;
		if (!onFloor(hitbox, lvlData))
			inAir = true;
	}

	public void setAttacking(boolean b) {
		attacking = b;
	}

	public void setFall() {
		state = FALLING;
	}

	public void flip() {
		flipY = 0;
		flipH = 1;
	}

	public void setDirection(int code, boolean directionOn) {
		switch (code) {
		case KeyEvent.VK_A: {
			left = directionOn;
			break;
		}
		case KeyEvent.VK_D: {
			right = directionOn;
			break;
		}
		case KeyEvent.VK_SPACE: {
			jump = directionOn;
			break;
		}
		}

	}

	public void powerAttack() {
		if (powerAttackActive) {
			return;
		} else if (powerValue >= 60) {
			powerAttackActive = true;
			changePower(-60);
		}

	}

	public void setHakiAttack(boolean attack) {
		hakiAttack = attack;
	}

	public void setFallMove(boolean fallMove) {
		this.fallMove = fallMove;
	}

	public void setHakiattackCheck(boolean hakiattackCheck) {
		this.hakiattackCheck = hakiattackCheck;
	}

	public void setpassTick(boolean passTick) {
		this.passTick = passTick;
	}

	public int increaseJumpTick() {
		jumptick++;
		return jumptick;
	}
	//reset
		private void resetAniTick() {
			aniTick = 0;
			aniIndex = 0;
		}
		private void resetInAir() {
			inAir = false;
			airSpeed = 0;
			jumpSpeed_act = jumpSpeed_base;
		}
	public void resetJumpTick() {
		jumptick = 0;
	}

	public void resetDirBooleans() {
		left = false;
		right = false;
		jump = false;
	}

	public void resetAll() {
		resetDirBooleans();
		inAir = false;
		attacking = false;
		hakiAttack = false;
		moving = false;
		airSpeed = 0f;
		state = IDLE;
		currentHealth = maxHealth;
		powerAttackActive = false;
		powerAttackTick = 0;
		powerValue = powerMaxValue;
		fallMove = true;
		bounce = false;
		hitbox.x = x;
		hitbox.y = y;
		if (!onFloor(hitbox, lvlData) && !isOnEntity(hitbox, play.getEnemylst())) {
			inAir = true;
		}
		resetAttackbox();

	}

	private void resetAttackbox() {
		if (flipW == 1)
			setAttackBoxOnRightSide();
		else
			setAttackBoxOnLeftSide();
	}

	public boolean isMove() {
		return moving;
	}

	public boolean isHakiattackCheck() {
		return hakiattackCheck;
	}

	public boolean isLeft() {
		return left;
	}

	public boolean isRight() {
		return right;
	}

	public boolean isJump() {
		return jump;
	}

	public int getJumpTick() {
		return jumptick;
	}

	public float getJumpSpeed() {
		return jumpSpeed_act;
	}

	public boolean getpassTick() {
		return passTick;
	}

	public int getTileY() {
		return tileY;
	}

	public boolean isHakiAttack() {
		return hakiAttack;
	}

}
