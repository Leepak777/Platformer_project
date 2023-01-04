package entities;

import main.GamePanel;


import static utilz.Constants.*;
import static utilz.Constants.PlayerConstants;
import static utilz.HelpMethods.*;
import utilz.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

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
	// player left right flip
	private int flipX = 0;
	private int flipW = 1;
	// Check if can attack
	private boolean attackCheck, hakiattackCheck;
	private int hakiDmg = 2;
	
	private int spikeTick = 0;
	private int tileY = 0;
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

	public void update() {
		spikeTick++;
		updateHealthBar();
		if (currentHealth <= 0) {
			if(state != DEAD) {
				state = DEAD;
				aniTick = 0;
				aniIndex = 0;
				play.setPlayerDying(true);
			}
			else if(aniIndex == GetSpriteAmount(DEAD) - 1 && aniTick >= ANISPEED -1) {
				play.setGameOver(true);
			}
			else {
				updateAnimationTick();
			}
			return;
		}
		if (!onFloor(hitbox, lvlData)) {
			inAir = true;
		}
		updateAttackBox(play.getxLvlOffset(), play.getyLvlOffset());
		if (isOnEntity(hitbox, play.getEnemylst())) {
			jump = true;
		}

		updatePos();
		if(moving) {
			tileY = (int) (hitbox.y / GamePanel.TILE_SIZE);
			checkPotionTouched();
		}
		if(spikeTick >=100) {
			checkSpikesTouch();
			spikeTick = 0;
		}
		if (attacking) {
			checkAttack();
		}
		if (hakiAttack) {
			checkHakiAttack();
		}
		updateAnimationTick();
		updateHakiTick();
		setAnimation();

	}

	private void checkSpikesTouch() {
		play.checkSpikesTouched();
		
	}

	private void checkPotionTouched() {
		play.checkPotionTouched(hitbox);
	}

	public void render(Graphics g, int xLvlOffset, int yLvlOffset) {
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

		g.drawImage(animations[state][aniIndex], (int) (hitbox.x - xDrawOffset) - xLvlOffset + flipX,
				(int) (hitbox.y - yDrawOffset) - yLvlOffset, width * flipW, height, null);
		// drawAttackBox(g,xLvlOffset,yLvlOffset);
		// drawBox(g, xLvlOffset, yLvlOffset, jumpbox);
		// drawHitBox(g, 0, xLvlOffset, yLvlOffset);
		drawUI(g);
	}

	public void setSpawn(Point spawn) {
		this.x = spawn.x;
		this.y = spawn.y;
		hitbox.x = x;
		hitbox.y = y;
	}

	private void initAttackBox() {
		attackbox = new Rectangle2D.Float(x, y, (int) (20 * GamePanel.SCALE), (int) (20 * GamePanel.SCALE));
		hakibox = new Rectangle2D.Float(x, y, (int) (hitbox.width * 2.5), (int) (hitbox.height * 2));
		jumpbox = new Rectangle2D.Float(x, y, (int) (hitbox.width), (int) (hitbox.height / 1.5));
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
		play.checkEnemyHit(attackbox, 50);
		play.checkObjectHit(attackbox);
	}

	private void updateAttackBox(int xLvlOffset, int yLvlOffset) {
		if (right) {
			attackbox.x = hitbox.x + hitbox.width + (int) (GamePanel.SCALE * 10);
		} else if (left) {
			attackbox.x = hitbox.x - hitbox.width - (int) (GamePanel.SCALE * 10);
		}
		hakibox.x = (int) (hitbox.x - xDrawOffset / 1.5);
		hakibox.y = (int) (hitbox.y - yDrawOffset * 6.9);
		jumpbox.x = (int) (hitbox.x);
		jumpbox.y = (int) (hitbox.y + hitbox.height);

		attackbox.y = hitbox.y + (int) (GamePanel.SCALE * 10);
	}

	private void updateHealthBar() {
		healthWidth = (int) ((currentHealth / (float) maxHealth) * healthBar_WIDTH);

	}

	public void updateJumpTick() {
		increaseJumpTick();
		jumpSpeed_act -= 0.0569420f ;
	}

	private void drawUI(Graphics g) {
		g.drawImage(statusimg, statusBar_X, statusBar_Y, statusBar_WIDTH, statusBar_HEIGHT, null);
		g.setColor(Color.red);
		g.fillRect(healthBar_X + statusBar_X, healthBar_Y + statusBar_Y, healthWidth, healthBar_HEIGHT);
	}

	private void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANISPEED) {
			aniTick = 0;
			aniIndex++;
			if (state == FALLING || state == IDLE) {
				play.checkEnemyHit(jumpbox, 5);
				play.checkObjectHit(jumpbox);
			}
			if (aniIndex >= GetSpriteAmount(state)) {
				aniIndex = 0;
				attacking = false;
				jump = false;
				attackCheck = false;
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

	private void setAnimation() {
		int startAni = state;
		if (moving) {
			state = RUNNING;
		} else {
			state = IDLE;
		}
		if (inAir) {
			if (airSpeed < 0) {
				state = JUMP;
			} else {
				state = FALLING;
			}
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

	private void resetAniTick() {
		aniTick = 0;
		aniIndex = 0;
	}

	private void updatePos() {
		moving = false;
		if (jump) {
			jump();
		}
		if (!inAir) {
			if ((!left && !right) || (right && left)) {
				return;
			}
		}
		float xSpeed = 0;
		if (left) {
			xSpeed -= walkSpeed;
			flipX = width;
			flipW = -1;
		}
		if (right) {
			xSpeed += walkSpeed;
			flipX = 0;
			flipW = 1;
		}
		if (!inAir) {
			if (!onFloor(hitbox, lvlData)) {
				inAir = true;
			}
		}
		if (inAir) {
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

	private void jump() {
		if (inAir) {
			return;
		}
		inAir = true;
		airSpeed = jumpSpeed_act;
	}

	private void resetInAir() {
		inAir = false;
		airSpeed = 0;
		jumpSpeed_act = jumpSpeed_base;
	}

	private void updateXPos(float xSpeed) {
		if (CanMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, lvlData)
				&& !isEntityX(hitbox, xSpeed, play.getEnemylst())) {
			hitbox.x += xSpeed;
		} else { 
			if(isEntityX(hitbox, xSpeed, play.getEnemylst())){
				hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed,true);
			}
			else {
			hitbox.x = GetEntityXPosNextToWall(hitbox, xSpeed, false);}
		}
	}

	public void changeHealthint(int value) {
		currentHealth += value;
		if (currentHealth <= 0) {
			currentHealth = 0;
		} else if (currentHealth >= maxHealth) {
			currentHealth = maxHealth;
		}

	}
	public void changePower(int bluePotionValue) {
		// TODO Auto-generated method stub
		
	}
	public void setAttacking(boolean b) {
		attacking = b;
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


	public void setHakiAttack(boolean attack) {
		hakiAttack = attack;
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
		state = IDLE;
		currentHealth = maxHealth;
		hitbox.x = x;
		hitbox.y = y;
		if (!onFloor(hitbox, lvlData) && !isOnEntity(hitbox, play.getEnemylst())) {
			inAir = true;
		}
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

	

}
