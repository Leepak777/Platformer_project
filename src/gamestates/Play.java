package gamestates;

import java.awt.Color;

import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import audio.AudioPlayer;
import effects.DialogueEffect;
import effects.Rain;
import objects.ObjectManager;
import entities.Crabby;
import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import main.Game;
import main.GamePanel;
import ui.GameCompletedOverlay;
import ui.GameOverOverLay;
import ui.LevelCompletedOverLay;
import ui.PauseOverLay;
import utilz.LoadSave;

import static utilz.Constants.Dialogue.DIALOGUE_HEIGHT;
import static utilz.Constants.Dialogue.DIALOGUE_WIDTH;
import static utilz.Constants.Dialogue.EXCLAMATION;
import static utilz.Constants.Dialogue.QUESTION;
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
	private Rain rain;
	private int xLvlOffset;
	private int yLvlOffset;
	private int leftBorder = (int) (0.2 * GamePanel.GAME_WIDTH);
	private int downBorder = (int) (0.2 * GamePanel.GAME_HEIGHT);
	private int rightBorder = (int) (0.8 * GamePanel.GAME_WIDTH);
	private int upBorder = (int) (0.8 * GamePanel.GAME_HEIGHT);
	private int maxlvlOffsetX;
	private int maxlvlOffsetY;
	private BufferedImage background, big_cloud, small_cloud, shipImgs[];
	private BufferedImage[] questionImgs, exclamationImgs;
	private ArrayList<DialogueEffect> dialogEffects = new ArrayList<>();
	private int[] smallCloudPos;
	private Random rnd = new Random();
	private boolean gameOver;
	private boolean lvlCompleted = false;
	private boolean playerDying = false;
	private boolean holdSpace = false;
	private boolean drawRain;
	private int code2;

	private boolean drawShip = true;
	private int shipAni, shipTick, shipDir = 1;
	private float shipHeightDelta, shipHeightChange = 0.05f * GamePanel.SCALE;
	private GameCompletedOverlay gameCompletedOverlay;
	private boolean gameCompleted;

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
		shipImgs = new BufferedImage[4];
		BufferedImage temp = LoadSave.GetSpriteAtlas(LoadSave.SHIP);
		for (int i = 0; i < shipImgs.length; i++)
			shipImgs[i] = temp.getSubimage(i * 78, 0, 78, 72);

		loadDialogue();
		calcLvlOffset();
		loadStartLevel();
		setDrawRainBoolean();
	}

	private void loadDialogue() {
		loadDialogueImgs();

		// Load dialogue array with premade objects, that gets activated when needed.
		// This is a simple
		// way of avoiding ConcurrentModificationException error. (Adding to a list that
		// is being looped through.

		for (int i = 0; i < 10; i++)
			dialogEffects.add(new DialogueEffect(0, 0, EXCLAMATION));
		for (int i = 0; i < 10; i++)
			dialogEffects.add(new DialogueEffect(0, 0, QUESTION));

		for (DialogueEffect de : dialogEffects)
			de.deactive();
	}

	private void loadDialogueImgs() {
		BufferedImage qtemp = LoadSave.GetSpriteAtlas(LoadSave.QUESTION_ATLAS);
		questionImgs = new BufferedImage[5];
		for (int i = 0; i < questionImgs.length; i++)
			questionImgs[i] = qtemp.getSubimage(i * 14, 0, 14, 12);

		BufferedImage etemp = LoadSave.GetSpriteAtlas(LoadSave.EXCLAMATION_ATLAS);
		exclamationImgs = new BufferedImage[5];
		for (int i = 0; i < exclamationImgs.length; i++)
			exclamationImgs[i] = etemp.getSubimage(i * 14, 0, 14, 12);
	}

	public void loadNextLevel() {
		levelM.loadNextLevel();
		player.setSpawn(levelM.getCurrentLevel().getPlayerPoint());
		resetAll();
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
		player.loadlvlData(levelM.getCurrentLevel().getLevelData());
		player.setSpawn(levelM.getCurrentLevel().getPlayerPoint());

		pauseOverlay = new PauseOverLay(this);
		gameoverOverlay = new GameOverOverLay(this);
		levelcompletedOverLay = new LevelCompletedOverLay(this);
		gameCompletedOverlay = new GameCompletedOverlay(this);

		rain = new Rain();
	}

	@Override
	public void update() {
		if (paused) {
			pauseOverlay.update();
		} else if (lvlCompleted) {
			levelcompletedOverLay.update();
		} else if (gameOver) {
			gameoverOverlay.update();
		} else if (gameCompleted)
			gameCompletedOverlay.update();
		else if (playerDying) {
			player.update();
		} else if (!gameOver) {
			updateDialogue();
			if (drawRain)
				rain.update(xLvlOffset);
			levelM.update();
			player.update();
			EM.update(levelM.getCurrentLevel().getLevelData(), player);
			OM.update();
			checkCloseBorder();
			if (drawShip)
				updateShipAni();
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

	private void updateShipAni() {
		shipTick++;
		if (shipTick >= 35) {
			shipTick = 0;
			shipAni++;
			if (shipAni >= 4)
				shipAni = 0;
		}

		shipHeightDelta += shipHeightChange * shipDir;
		shipHeightDelta = Math.max(Math.min(10 * GamePanel.SCALE, shipHeightDelta), 0);

		if (shipHeightDelta == 0)
			shipDir = 1;
		else if (shipHeightDelta == 10 * GamePanel.SCALE)
			shipDir = -1;

	}

	private void updateDialogue() {
		for (DialogueEffect de : dialogEffects)
			if (de.isActive())
				de.update();
	}

	private void drawDialogue(Graphics g, int xLvlOffset) {
		for (DialogueEffect de : dialogEffects)
			if (de.isActive()) {
				if (de.getType() == QUESTION)
					g.drawImage(questionImgs[de.getAniIndex()], de.getX() - xLvlOffset, de.getY(), DIALOGUE_WIDTH,
							DIALOGUE_HEIGHT, null);
				else
					g.drawImage(exclamationImgs[de.getAniIndex()], de.getX() - xLvlOffset, de.getY(), DIALOGUE_WIDTH,
							DIALOGUE_HEIGHT, null);
			}
	}

	public void addDialogue(int x, int y, int type) {
		// Not adding a new one, we are recycling. #ThinkGreen lol
		dialogEffects.add(new DialogueEffect(x, y - (int) (GamePanel.SCALE * 15), type));
		for (DialogueEffect de : dialogEffects)
			if (!de.isActive())
				if (de.getType() == type) {
					de.reset(x, -(int) (GamePanel.SCALE * 15));
					return;
				}
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(background, 0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT, null);
		drawClouds(g);
		if (drawRain)
			rain.draw(g, xLvlOffset);

		if (drawShip)
			g.drawImage(shipImgs[shipAni], (int) (100 * GamePanel.SCALE) - xLvlOffset,
					(int) ((288 * GamePanel.SCALE) + shipHeightDelta), (int) (78 * GamePanel.SCALE),
					(int) (72 * GamePanel.SCALE), null);

		levelM.draw(g, xLvlOffset, yLvlOffset);
		player.render(g, xLvlOffset, yLvlOffset);
		EM.draw(g, xLvlOffset, yLvlOffset);
		OM.draw(g, xLvlOffset, yLvlOffset);
		drawDialogue(g, xLvlOffset);
		if (paused) {
			g.setColor(new Color(0, 0, 0, 150));
			g.fillRect(0, 0, GamePanel.GAME_WIDTH, GamePanel.GAME_HEIGHT);
			pauseOverlay.draw(g);
		} else if (gameOver) {
			gameoverOverlay.draw(g);
		} else if (lvlCompleted) {
			levelcompletedOverLay.draw(g);
		} else if (gameCompleted)
			gameCompletedOverlay.draw(g);
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
				player.resetJumpTick();
			} else if (e.getButton() == MouseEvent.BUTTON3) {
				player.powerAttack();
				player.resetJumpTick();
			}
		}

	}

	public void setGameCompleted() {
		gameCompleted = true;
	}

	public void resetGameCompleted() {
		gameCompleted = false;
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (!gameOver) {
			if (paused) {
				pauseOverlay.mousePressed(e);
			} else if (lvlCompleted) {
				levelcompletedOverLay.mousePressed(e);
			} else if (gameCompleted)
				gameCompletedOverlay.mousePressed(e);
		} else {
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
			} else if (gameCompleted)
				gameCompletedOverlay.mouseReleased(e);
		} else {
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
			} else if (gameCompleted)
				gameCompletedOverlay.mouseMoved(e);
		} else {
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
				if (holdSpace) {
					player.updateJumpTick();
					code2 = code;
					passTickAct();
					return;
				}
				player.setDirection(code, true);
			}
			if (code == KeyEvent.VK_ESCAPE) {
				paused = true;
			}
			if (code == KeyEvent.VK_SPACE) {
				player.flip();
				holdSpace = true;
				if (code2 != 0) {
					player.setDirection(code2, false);
				}
				if (!player.isHakiAttack()) {
					player.setHakiAttack(true);
				}
				player.setHakiAttack(true);
				// System.out.println(player.getJumpTick());
				player.updateJumpTick();
				passTickAct();
				if (player.getpassTick()) {
					player.setHakiAttack(false);
					player.resetJumpTick();
				}

			}

		}

	}

	private void passTickAct() {
		if (player.getJumpTick() > 30 && !player.getpassTick()) {
			if (code2 != 0 && (code2 == KeyEvent.VK_A || code2 == KeyEvent.VK_D)) {
				player.setDirection(code2, true);
			}
			player.setDirection(KeyEvent.VK_SPACE, true);
			player.resetJumpTick();
			// System.out.println(player.getJumpSpeed());
			player.setpassTick(true);
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		int code = e.getKeyCode();
		if (!gameOver) {
			if (code == KeyEvent.VK_A || code == KeyEvent.VK_D) {
				code2 = 0;
				player.setDirection(code, false);
			}
			if (code == KeyEvent.VK_SPACE) {
				holdSpace = false;
				if (player.isHakiAttack()) {
					player.setHakiAttack(false);
				}
				if (!player.getpassTick() && player.getJumpTick() > 0) {
					if (code2 != 0 && (code2 == KeyEvent.VK_A || code2 == KeyEvent.VK_D)) {
						player.setDirection(code2, true);
					}
					player.setDirection(code, true);
					player.resetJumpTick();
					// System.out.println(player.getJumpSpeed());
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
		dialogEffects.clear();
		setDrawRainBoolean();
	}

	private void setDrawRainBoolean() {
		// This method makes it rain 20% of the time you load a level.
		if (rnd.nextFloat() >= 0.8f)
			drawRain = true;
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
		if (b) {
			lvlCompleted();
		}

	}

	private void lvlCompleted() {
		gp.getAudioPlay().stopSong();
		gp.getAudioPlay().playEffect(AudioPlayer.LVL_COMPLETED);
	}

	public ObjectManager getOM() {
		return OM;
	}

	public void setPlayerDying(boolean b) {
		this.playerDying = b;

	}

	public int getCode2() {
		return code2;
	}

}
