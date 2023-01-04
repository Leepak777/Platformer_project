package gamestates;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import objects.ObjectManager;
import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.GamePanel;
import ui.GameOverOverLay;
import ui.LevelCompletedOverLay;
import ui.PauseOverLay;
import utilz.LoadSave;
import static utilz.Constants.ENVIRONMENT.*;

public class Play extends State implements StateMethods {
	private Player player;
	private LevelManager levelM;
	private EnemyManager EM;
	private ObjectManager OM;
	private boolean paused = false;
	private PauseOverLay pauseOverlay;
	private GameOverOverLay gameoverOverlay;
	private LevelCompletedOverLay levelcompletedOverLay;
	private int xLvlOffset;
	private int yLvlOffset;
	private int leftBorder = (int) (0.2 * GamePanel.GAME_WIDTH);
	private int downBorder = (int) (0.2 * GamePanel.GAME_HEIGHT);
	private int rightBorder = (int) (0.8 * GamePanel.GAME_WIDTH);
	private int upBorder = (int) (0.8 * GamePanel.GAME_HEIGHT);
	private int maxlvlOffsetX;
	private int maxlvlOffsetY;
	private BufferedImage background, big_cloud, small_cloud;
	private int[] smallCloudPos;
	private Random rnd = new Random();
	private boolean gameOver;
	private boolean lvlCompleted = false;
	private boolean playerDying = false;

	public Play(GamePanel gp) {
		super(gp);
		initClasses();
		background = LoadSave.GetSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMAGE);
		big_cloud = LoadSave.GetSpriteAtlas(LoadSave.BIG_CLOUDS);
		small_cloud = LoadSave.GetSpriteAtlas(LoadSave.SMALL_CLOUDS);
		smallCloudPos = new int[8];
		for (int i = 0; i < 8; i++) {
			smallCloudPos[i] = (int) (250 * GamePanel.SCALE + rnd.nextFloat(150 * GamePanel.SCALE));
		}
		calcLvlOffset();
		loadStartLevel();
	}

	public void loadNextLevel() {
		resetAll();
		levelM.loadNextLevel();
		player.setSpawn(levelM.getCurrentLevel().getPlayerPoint());
	}

	private void loadStartLevel() {
		EM.LoadEnemies(levelM.getCurrentLevel());
		OM.loadObjects(levelM.getCurrentLevel());
	}

	private void calcLvlOffset() {
		maxlvlOffsetX = levelM.getCurrentLevel().getLvlOffsetX();
		maxlvlOffsetY = levelM.getCurrentLevel().getLvlOffsetY();
	}

	private void initClasses() {
		levelM = new LevelManager(this);
		EM = new EnemyManager(this);
		OM = new ObjectManager(this);
		player = new Player((levelM.getPixelWidth() - 8) * GamePanel.TILE_SIZE,
				(levelM.getPixelHeight() - 8) * GamePanel.TILE_SIZE, (int) (64 * GamePanel.SCALE),
				(int) (40 * GamePanel.SCALE), this);
		player.setSpawn(levelM.getCurrentLevel().getPlayerPoint());
		player.loadlvlData(levelM.getCurrentLevel().getLevelData());
		pauseOverlay = new PauseOverLay(this);
		gameoverOverlay = new GameOverOverLay(this);
		levelcompletedOverLay = new LevelCompletedOverLay(this);
	}

	@Override
	public void update() {
		if (paused) {
			pauseOverlay.update();
		} else if (lvlCompleted) {
			levelcompletedOverLay.update();
		}
		else if(gameOver) {
			gameoverOverlay.update();
		}
		else if(playerDying) {
			player.update();
		}
		else if (!gameOver) {
			levelM.update();
			player.update();
			EM.update(levelM.getCurrentLevel().getLevelData(), player);
			OM.update();
			checkCloseBorder();
		}

	}

	private void checkCloseBorder() {
		int playerX = (int) player.getHitbox().x;
		int playerY = (int) player.getHitbox().y;

		int diffx = playerX - xLvlOffset;
		int diffy = playerY - yLvlOffset;

		if (diffx > rightBorder) {
			xLvlOffset += diffx - rightBorder;
		} else if (diffx < leftBorder) {
			xLvlOffset += diffx - leftBorder;
		}

		if (diffy > upBorder) {
			yLvlOffset += diffy - upBorder;
		} else if (diffy < downBorder) {
			yLvlOffset += diffy - downBorder;
		}

		if (xLvlOffset > maxlvlOffsetX) {
			xLvlOffset = maxlvlOffsetX;
		} else if (xLvlOffset < 0) {
			xLvlOffset = 0;
		}
		if (yLvlOffset > maxlvlOffsetY) {
			yLvlOffset = maxlvlOffsetY;
		} else if (yLvlOffset < 0) {
			yLvlOffset = 0;
		}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT, null);
		drawClouds(g);
		levelM.draw(g, xLvlOffset, yLvlOffset);
		player.render(g, xLvlOffset, yLvlOffset);
		EM.draw(g, xLvlOffset, yLvlOffset);
		OM.draw(g, xLvlOffset, yLvlOffset);
		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver) {
			gameoverOverlay.draw(g);
		} else if (lvlCompleted) {
			levelcompletedOverLay.draw(g);
		}
	}

	private void drawClouds(Graphics g) {
		for (int i = 0; i < 3; i++) {
			g.drawImage(big_cloud, (int) (465 * GamePanel.SCALE), 0 + i * BIG_CLOUD_WIDTH - (int) (yLvlOffset * 0.3),
					BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
		}
		for (int i = 0; i < smallCloudPos.length; i++) {
			g.drawImage(small_cloud, (int) (smallCloudPos[i]), 4 * i * SMALL_CLOUD_WIDTH - (int) (yLvlOffset * 0.7),
					SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (!gameOver) {
			if (e.getButton() == MouseEvent.BUTTON1) {
				player.setAttacking(true);
			}
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused) {
				pauseOverlay.mousePressed(e);
			} else if (lvlCompleted) {
				levelcompletedOverLay.mousePressed(e);
			}
		}
		else {
			gameoverOverlay.mousePressed(e);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (!gameOver) {
			if (paused) {
				pauseOverlay.mouseReleased(e);
			} else if (lvlCompleted) {
				levelcompletedOverLay.mouseReleased(e);
			}
		}
		else {
			gameoverOverlay.mouseReleased(e);
		}
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (!gameOver) {
			if (paused) {
				pauseOverlay.mouseMoved(e);
			} else if (lvlCompleted) {
				levelcompletedOverLay.mouseMoved(e);
			}
		}
		else {
			gameoverOverlay.mouseMoved(e);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (gameOver) {
			gameoverOverlay.keyPressed(e);
		} else {
			int code = e.getKeyCode();
			if (code == KeyEvent.VK_A || code == KeyEvent.VK_D) {
				player.setHakiAttack(false);
				player.setDirection(code, true);
			}
			if (code == KeyEvent.VK_ESCAPE) {
				paused = true;
			}
			if (code == KeyEvent.VK_SPACE) {
				player.setHakiAttack(true);

				player.updateJumpTick();
				if (player.getJumpTick() > 30) {
					player.setDirection(code, true);
					player.resetJumpTick();
					System.out.println(player.getJumpSpeed());
					player.setpassTick(true);
				}
				if (player.getpassTick()) {
					player.setHakiAttack(false);

					player.resetJumpTick();
				}

			}
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (!gameOver) {
			if (code == KeyEvent.VK_A || code == KeyEvent.VK_D) {
				player.setDirection(code, false);
			} else if (code == KeyEvent.VK_SPACE) {

				player.setHakiAttack(false);
				if (!player.getpassTick() && player.getJumpTick() > 0) {

					player.setDirection(code, true);
					player.resetJumpTick();
					System.out.println(player.getJumpSpeed());
				} else {
					player.resetJumpTick();
					player.setpassTick(false);
				}
			}
		}
	}

	public Player getPlayer() {
		return player;
	}

	public LevelManager getLevelM() {
		return levelM;
	}

	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void unPause() {
		paused = false;
	}

	public void mouseDragged(MouseEvent e) {
		if (!gameOver) {
			if (paused) {
				pauseOverlay.mouseDragged(e);
			}
		}
	}

	public void resetAll() {
		lvlCompleted = false;
		gameOver = false;
		paused = false;
		playerDying = false;
		player.resetAll();
		EM.resetAllEnemy();
		OM.resetAllObject();

	}
	public void checkObjectHit(Rectangle2D.Float attackbox) {
		OM.checkObjectHit(attackbox);		
	}
	public void checkEnemyHit(Rectangle2D.Float attackbox, int amount) {
		EM.CheckEnemyHit(attackbox, amount);
	}
	public void checkPotionTouched(Rectangle2D.Float hitbox) {
		OM.checkObjectTouched(hitbox);
		
	}
	public void checkSpikesTouched() {
		OM.checkSpikesTouch(player);
		
	}

	public int getxLvlOffset() {
		return xLvlOffset;
	}

	public int getyLvlOffset() {
		return yLvlOffset;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;

	}

	public boolean isGameOver() {
		return gameOver;
	}

	public EnemyManager getEM() {
		return EM;
	}

	public ArrayList<Enemy> getEnemylst() {
		return levelM.getCurrentLevel().getEnemieslst();
	}

	public void setLvlOffset(int lvlOffsetX, int lvlOffsetY) {
		this.maxlvlOffsetX = lvlOffsetX;
		this.maxlvlOffsetY = lvlOffsetY;

	}

	public void setLevelCompleted(boolean b) {
		this.lvlCompleted = b;

	}

	public ObjectManager getOM() {
		return OM;
	}

	public void setPlayerDying(boolean b) {
		this.playerDying  = b;
		
	}

	

	

	

}
