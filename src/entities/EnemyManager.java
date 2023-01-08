package entities;

import java.awt.Graphics;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import static utilz.Constants.EnemyConstants.*;
import static utilz.HelpMethods.*;

import entities.Crabby;
import gamestates.Play;
import levels.Level;
import utilz.LoadSave;

public class EnemyManager {
	private Play play;
	private BufferedImage[][] crabbyArr, pinkstarArr, sharkArr;
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private Level currentLevel;
	public EnemyManager(Play play) {
		this.play = play;
		loadEnemyAnimations();
	}

	public void LoadEnemies(Level lvl) {
		this.currentLevel = lvl;
		enemies = lvl.getEnemieslst();
	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		for (Crabby c : currentLevel.getCrabs())
			if (c.isActive()) {
				c.update(lvlData, player);
				isAnyActive = true;
			}

		for (Roll p : currentLevel.getPinkstars())
			if (p.isActive()) {
				p.update(lvlData, player);
				isAnyActive = true;
			}

		for (Shark s : currentLevel.getSharks())
			if (s.isActive()) {
				s.update(lvlData, play);
				isAnyActive = true;
			}

		if (!isAnyActive)
			play.setLevelCompleted(true);
	}

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawCrabs(g, xLvlOffset, yLvlOffset);
		drawPinkstars(g, xLvlOffset, yLvlOffset);
		drawSharks(g, xLvlOffset, yLvlOffset);
	}

	private void drawSharks(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Shark s : currentLevel.getSharks())
			if (s.isActive()) {
				g.drawImage(sharkArr[s.getState()][s.getAniIndex()],
						(int) s.getHitbox().x - xLvlOffset - SHARK_DRAWOFFSET_X + s.flipX(),
						(int) s.getHitbox().y - yLvlOffset - SHARK_DRAWOFFSET_Y + (int) s.getPushDrawOffset(),
						SHARK_WIDTH * s.flipW(), SHARK_HEIGHT, null);
				s.drawHitBox(g, xLvlOffset,yLvlOffset);
//				s.drawAttackBox(g, xLvlOffset);
			}
	}

	private void drawPinkstars(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Roll p : currentLevel.getPinkstars())
			if (p.isActive()) {
				g.drawImage(pinkstarArr[p.getState()][p.getAniIndex()],
						(int) p.getHitbox().x - xLvlOffset - PINKSTAR_DRAWOFFSET_X + p.flipX(),
						(int) p.getHitbox().y - yLvlOffset - PINKSTAR_DRAWOFFSET_Y + (int) p.getPushDrawOffset(),
						PINKSTAR_WIDTH * p.flipW(), PINKSTAR_HEIGHT, null);
				p.drawHitBox(g, xLvlOffset, yLvlOffset);
			}
	}

	private void drawCrabs(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Crabby c : currentLevel.getCrabs())
			if (c.isActive()) {

				g.drawImage(crabbyArr[c.getState()][c.getAniIndex()],
						(int) c.getHitbox().x - xLvlOffset - CRABBY_DRAWOFFSET_X + c.flipX(),
						(int) c.getHitbox().y - yLvlOffset - CRABBY_DRAWOFFSET_Y + (int) c.getPushDrawOffset(),
						CRABBY_WIDTH * c.flipW(), CRABBY_HEIGHT, null);

				c.drawHitBox(g, xLvlOffset, yLvlOffset);
//				c.drawAttackBox(g, xLvlOffset);
			}

	}

	public void CheckEnemyHit(Rectangle2D.Float attackbox, int amount) {
		for (Enemy c : enemies) {
			if (c.isActive()) {
				if (attackbox.intersects(c.getHitbox()) || attackbox.contains(c.getHitbox())) {
					c.hurt(amount);
					// System.out.println(amount);
				}
			}
		}
	}

	private void loadEnemyAnimations() {
		crabbyArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE), 9, 5, CRABBY_WIDTH_DEFAULT,
				CRABBY_HEIGHT_DEFAULT);
		pinkstarArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.PINKSTAR_ATLAS), 8, 5, PINKSTAR_WIDTH_DEFAULT,
				PINKSTAR_HEIGHT_DEFAULT);
		sharkArr = getImgArr(LoadSave.GetSpriteAtlas(LoadSave.SHARK_ATLAS), 8, 5, SHARK_WIDTH_DEFAULT,
				SHARK_HEIGHT_DEFAULT);

	}

	private BufferedImage[][] getImgArr(BufferedImage atlas, int xSize, int ySize, int spriteW, int spriteH) {
		BufferedImage[][] tempArr = new BufferedImage[ySize][xSize];
		for (int j = 0; j < tempArr.length; j++)
			for (int i = 0; i < tempArr[j].length; i++)
				tempArr[j][i] = atlas.getSubimage(i * spriteW, j * spriteH, spriteW, spriteH);
		return tempArr;
	}

	public void resetAllEnemy() {
		for (Crabby c : currentLevel.getCrabs())
			c.resetEnemy();
		for (Roll p : currentLevel.getPinkstars())
			p.resetEnemy();
		for (Shark s : currentLevel.getSharks())
			s.resetEnemy();

	}
}
