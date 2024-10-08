package objects;

import static utilz.Constants.ObjectsConstants.*;
import static utilz.Constants.PlayerConstants.*;
import static utilz.HelpMethods.*;
import static utilz.Constants.Projectiles.*;

import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import entities.Enemy;
import entities.Player;
import gamestates.Play;
import levels.Level;
import main.Game;
import main.GamePanel;
import utilz.LoadSave;

public class ObjectManager {
	private Play play;
	private BufferedImage[][] potionImgs, containerImgs;
	private BufferedImage[] canonImgs, grassImgs;
	private BufferedImage[][] treeImgs;
	private BufferedImage spikeImg, ballImg;
	private ArrayList<Potion> potions;
	private ArrayList<GameContainer> containers;
	private ArrayList<Projectile> balls = new ArrayList<>();
	private Level currentLevel;

	public ObjectManager(Play play) {
		this.play = play;
		currentLevel = play.getLevelM().getCurrentLevel();
		loagImgs();

	}

	public void checkSpikesTouch(Player player) {
		for (Spike s : play.getLevelM().getCurrentLevel().getSpikes()) {
			if (s.getHitbox().intersects(player.getHitbox())) {
				player.changeHealthint(-1, false);
			}
		}
	}

	public void checkSpikesTouched(Enemy e) {
		for (Spike s : play.getLevelM().getCurrentLevel().getSpikes())
			if (s.getHitbox().intersects(e.getHitbox()))
				e.hurt(200);
	}

	public void checkObjectTouched(Rectangle2D.Float hitbox) {
		for (Potion p : potions) {
			if (p.isActive()) {
				if (hitbox.intersects(p.getHitbox())) {
					p.setActive(false);
					applyEffectToPlayer(p);
				}
			}
		}
	}

	public void applyEffectToPlayer(Potion p) {
		if (p.getObjType() == RED_POTION) {
			play.getPlayer().changeHealthint(RED_POTION_VALUE,false);
		} else if (p.getObjType() == BLUE_POTION) {
			play.getPlayer().changePower(BLUE_POTION_VALUE);
		}
	}

	public void checkObjectHit(Rectangle2D.Float attackbox, Player player) {
		for (GameContainer gc : containers) {
			if (gc.isActive() && !gc.doAnimation) {
				if (gc.getHitbox().intersects(attackbox)) {
					gc.setDoAnimation(true);
					int type = 0;
					if (gc.getObjType() == BARREL) {
						type = 1;
					}
					potions.add(new Potion((int) (gc.getHitbox().x + gc.getHitbox().width / 2),
							(int) (gc.getHitbox().y - gc.getHitbox().height / 3), type));
					return;
				}
			}
		}
		for (Projectile p : balls) {
			if (p.isActive()) {
				if (p.getHitbox().intersects(attackbox) && player.getState() == ATTACK) {
					p.changeDir();
					return;
				}
			}
		}
	}

	public ArrayList<GameContainer> getContainers() {
		return containers;
	}

	public ArrayList<Projectile> getBalls() {
		return balls;
	}

	private void loagImgs() {
		BufferedImage potionSprite = LoadSave.GetSpriteAtlas(LoadSave.POTION_ATLAS);
		potionImgs = new BufferedImage[2][7];
		for (int j = 0; j < potionImgs.length; j++) {
			for (int i = 0; i < potionImgs[j].length; i++) {
				potionImgs[j][i] = potionSprite.getSubimage(i * 12, j * 16, 12, 16);
			}
		}

		BufferedImage containerSprite = LoadSave.GetSpriteAtlas(LoadSave.CONTAINER_ATLAS);
		containerImgs = new BufferedImage[2][8];
		for (int j = 0; j < containerImgs.length; j++) {
			for (int i = 0; i < containerImgs[j].length; i++) {
				containerImgs[j][i] = containerSprite.getSubimage(i * 40, j * 30, 40, 30);
			}
		}
		spikeImg = LoadSave.GetSpriteAtlas(LoadSave.TRAP_ATLAS);
		canonImgs = new BufferedImage[7];
		BufferedImage canonSprite = LoadSave.GetSpriteAtlas(LoadSave.CANON_ATLAS);
		for (int i = 0; i < canonImgs.length; i++) {
			canonImgs[i] = canonSprite.getSubimage(i * 40, 0, 40, 26);
		}
		ballImg = LoadSave.GetSpriteAtlas(LoadSave.BALL_ATLAS);

		treeImgs = new BufferedImage[2][4];
		BufferedImage treeOneImg = LoadSave.GetSpriteAtlas(LoadSave.TREE_ONE_ATLAS);
		for (int i = 0; i < 4; i++)
			treeImgs[0][i] = treeOneImg.getSubimage(i * 39, 0, 39, 92);

		BufferedImage treeTwoImg = LoadSave.GetSpriteAtlas(LoadSave.TREE_TWO_ATLAS);
		for (int i = 0; i < 4; i++)
			treeImgs[1][i] = treeTwoImg.getSubimage(i * 62, 0, 62, 54);

		BufferedImage grassTemp = LoadSave.GetSpriteAtlas(LoadSave.GRASS_ATLAS);
		grassImgs = new BufferedImage[2];
		for (int i = 0; i < grassImgs.length; i++)
			grassImgs[i] = grassTemp.getSubimage(32 * i, 0, 32, 32);
	}

	public void update() {
		updateBackgroundTrees();
		for (Potion p : potions) {
			if (p.isActive()) {
				p.update();
			}
		}
		for (GameContainer gc : containers) {
			if (gc.isActive()) {
				gc.update();
			}
		}
		updateCanons(play.getLevelM().getCurrentLevel().getLevelData(), play.getPlayer());
		updateProjectiles(play.getLevelM().getCurrentLevel().getLevelData(), play.getPlayer(), play.getEnemylst());
	}

	private void updateBackgroundTrees() {
		for (BackgroundTree bt : currentLevel.getTrees())
			bt.update();
	}

	private void checkHitEn(Projectile b, ArrayList<Enemy> en) {
		for (Enemy e : en) {
			if (e.isActive()) {
				if (e.getHitbox().intersects(b.getHitbox())) {
					e.hurt(100);
					b.setActive(false);
				}
			}
		}
	}

	private void updateProjectiles(int[][] levelData, Player player, ArrayList<Enemy> en) {
		for (Projectile b : balls) {
			if (b.isActive()) {
				b.updatePos();
				if (b.getHitbox().intersects(player.getHitbox())) {
					player.changeHealth(-10, null, b);
					player.setFallMove(false);
					player.setFall();
					b.setActive(false);
				} else if (IsBallHittingLevel(b, levelData)) {
					b.setActive(false);
				} else {
					checkHitEn(b, en);
				}
			}

		}

	}

	private void updateCanons(int[][] lvlData, Player player) {
		for (Canon c : play.getLevelM().getCurrentLevel().getCannons()) {
			if (!c.doAnimation) {
				if (c.getTileY() == player.getTileY()) {
					if (isPlayerInRange(c, player)) {
						if (isPlayerFront(c, player)) {
							if (CanonSeePlayer(lvlData, player.getHitbox(), c.getHitbox(), c.getTileY())) {
								c.setDoAnimation(true);
							}
						}
					}
				}
			}
			c.update();
			if (c.getAniIndex() == 4 && c.getAnimationTick() == 0) {
				shoot(c);
			}
		}

	}

	private void shoot(Canon c) {
		c.setDoAnimation(true);
		int dir = 1;
		if (c.getObjType() == CANON_LEFT) {
			dir = -1;
		}
		balls.add(new Projectile((int) c.getHitbox().x, (int) c.getHitbox().y, dir));
	}

	private boolean isPlayerFront(Canon c, Player player) {
		if (c.getObjType() == CANON_LEFT) {
			if (c.getHitbox().x > player.getHitbox().x) {
				return true;
			}
		} else if (c.getHitbox().x < player.getHitbox().x) {
			return true;
		}
		return false;
	}

	private boolean isPlayerInRange(Canon c, Player player) {
		int absValue = (int) Math.abs(player.getHitbox().x - c.hitbox.x);
		return absValue <= GamePanel.TILE_SIZE * 4;
	}

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		drawPotions(g, xLvlOffset, yLvlOffset);
		drawContainers(g, xLvlOffset, yLvlOffset);
		drawTraps(g, xLvlOffset, yLvlOffset);
		drawCanons(g, xLvlOffset, yLvlOffset);
		drawBalls(g, xLvlOffset, yLvlOffset);
		drawGrass(g, xLvlOffset, yLvlOffset);
		drawBackgroundTrees(g, xLvlOffset, yLvlOffset);
	}

	private void drawGrass(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Grass grass : currentLevel.getGrass())
			g.drawImage(grassImgs[grass.getType()], grass.getX() - xLvlOffset, grass.getY()- yLvlOffset, (int) (32 * GamePanel.SCALE),
					(int) (32 * GamePanel.SCALE), null);
	}

	public void drawBackgroundTrees(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (BackgroundTree bt : currentLevel.getTrees()) {

			int type = bt.getType();
			if (type == 9)
				type = 8;
			g.drawImage(treeImgs[type - 7][bt.getAniIndex()], bt.getX() - xLvlOffset + GetTreeOffsetX(bt.getType()),
					(int) (bt.getY()- yLvlOffset + GetTreeOffsetY(bt.getType())), GetTreeWidth(bt.getType()),
					GetTreeHeight(bt.getType()), null);
		}
	}

	private void drawBalls(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Projectile b : balls) {
			if (b.isActive()) {
				g.drawImage(ballImg, (int) (b.getHitbox().x - xLvlOffset), (int) (b.getHitbox().y - yLvlOffset),
						BALL_WIDTH, BALL_HEIGHT, null);
			}
		}

	}

	private void drawCanons(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Canon c : play.getLevelM().getCurrentLevel().getCannons()) {
			int x = (int) (c.getHitbox().x - xLvlOffset);
			int width = CANON_WIDTH;

			if (c.getObjType() == CANON_RIGHT) {
				x += width;
				width /= -1;
			}
			g.drawImage(canonImgs[c.getAniIndex()], x, (int) (c.getHitbox().y - c.getyDrawOffset() - yLvlOffset), width,
					CANON_HEIGHT, null);

		}

	}

	private void drawTraps(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Spike s : play.getLevelM().getCurrentLevel().getSpikes()) {
			g.drawImage(spikeImg, (int) (s.getHitbox().x - xLvlOffset),
					(int) (s.getHitbox().y - s.getyDrawOffset() - yLvlOffset), SPIKE_WIDTH, SPIKE_HEIGHT, null);
		}

	}

	public void loadObjects(Level newLevel) {
		currentLevel = newLevel;
		potions = new ArrayList<>(newLevel.getPotions());
		containers = new ArrayList<>(newLevel.getContainers());
		balls.clear();
	}

	private void drawContainers(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (GameContainer gc : containers) {
			if (gc.isActive()) {
				int type = 0;
				if (gc.getObjType() == BARREL) {
					type = 1;
				}
				g.drawImage(containerImgs[type][gc.getAniIndex()],
						(int) (gc.getHitbox().x - gc.getxDrawOffset() - xLvlOffset),
						(int) (gc.getHitbox().y - gc.getyDrawOffset() - yLvlOffset), CONTAINER_WIDTH, CONTAINER_HEIGHT,
						null);
				gc.drawHitBox(g, type, xLvlOffset, yLvlOffset);
			}
		}

	}

	private void drawPotions(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (Potion p : potions) {
			if (p.isActive()) {
				int type = 0;
				if (p.getObjType() == RED_POTION) {
					type = 1;
				}
				g.drawImage(potionImgs[type][p.getAniIndex()],
						(int) (p.getHitbox().x - p.getxDrawOffset() - xLvlOffset),
						(int) (p.getHitbox().y - p.getyDrawOffset() - yLvlOffset), POTION_WIDTH, POTION_HEIGHT, null);

			}
		}

	}

	public void resetAllObject() {
		System.out.println("Array Size: " + potions.size() + " - " + containers.size());
		loadObjects(play.getLevelM().getCurrentLevel());
		for (Potion p : potions) {
			p.reset();
		}
		for (GameContainer gc : containers) {
			gc.reset();
		}
		for (Canon c : currentLevel.getCannons()) {
			c.reset();
		}
		//System.out.println("Array Size after: " + potions.size() + " - " + containers.size());
	}

}
