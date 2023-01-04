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
	private BufferedImage[][] crab;
	private ArrayList<Enemy> enemies = new ArrayList<>();

	public EnemyManager(Play play) {
		this.play = play;
		loadEnemyAnimations();
	}

	public void LoadEnemies(Level lvl) {
		enemies = lvl.getEnemieslst();
		System.out.println("Crabs num: " + enemies.size());
	}

	public void update(int[][] lvlData, Player player) {
		boolean isAnyActive = false;
		if (!play.isGameOver()) {
			for (Enemy c : enemies)
				if (c.isActive()) {
					isAnyActive = true;
					switch (c.enemyType) {
					case (CRABBY):
						Crabby crab = (Crabby) c;
						crab.update(lvlData, player);
						break;
					}
				}
		}
		if (!isAnyActive) {
			play.setLevelCompleted(true);
		}
	}

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawCrabs(g, xLvlOffset, yLvlOffset);
	}

	private void drawCrabs(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Enemy c : enemies) {
			if (c.isActive()) {
				if (c.isActive()) {
					switch (c.enemyType) {
						case (CRABBY):
							Crabby crabby = (Crabby) c;
							g.drawImage(crab[crabby.getState()][crabby.getAniIndex()],
									(int) (crabby.getHitbox().x) - xLvlOffset - CRABBY_DRAWOFFSET_X + crabby.flipX(),
									(int) (crabby.getHitbox().y) - yLvlOffset - CRABBY_DRAWOFFSET_Y, CRABBY_WIDTH * crabby.flipW(),
									CRABBY_HEIGHT, null);
							// c.drawHitBox(g, 0, xLvlOffset, yLvlOffset);
						}
				}
			}

		}
	}

	public void CheckEnemyHit(Rectangle2D.Float attackbox, int amount) {
		for (Enemy c : enemies) {
			if (c.isActive()) {
				if (attackbox.intersects(c.getHitbox()) || attackbox.contains(c.getHitbox())) {
					c.hurt(amount);
					System.out.println(amount);
				}
			}
		}
	}

	private void loadEnemyAnimations() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.CRABBY_SPRITE);
		crab = new BufferedImage[5][9];
		for (int j = 0; j < crab.length; j++) {
			for (int i = 0; i < crab[j].length; i++) {
				crab[j][i] = img.getSubimage(i * CRABBY_WIDTH_DEFAULT, j * CRABBY_HEIGHT_DEFAULT, CRABBY_WIDTH_DEFAULT,
						CRABBY_HEIGHT_DEFAULT);
			}
		}

	}

	public void resetAllEnemy() {
		for (Enemy c : enemies) {
			c.resetEnemy();
		}

	}
}
