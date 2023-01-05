package levels;

import java.awt.Point;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import objects.Canon;
import objects.GameContainer;
import objects.Potion;
import objects.Spike;

import static utilz.HelpMethods.*;
import entities.Crabby;
import entities.Enemy;
import gamestates.Play;
import main.GamePanel;

public class Level {
	private Play play;
	private BufferedImage img;
	private ArrayList<Enemy> enemies;
	private ArrayList<Potion> potions;
	private ArrayList<Spike> spikes;
	private ArrayList<Canon> canons;
	private ArrayList<GameContainer> containers;
	private int[][] lvlData;
	private int lvlTilesWide;
	private int lvlTilesHeight;
	private int maxTilesOffsetX;
	private int maxTilesOffsetY;
	private int maxlvlOffsetX;
	private int maxlvlOffsetY;
	private Point playerSpawn;

	public Level(BufferedImage img, Play play) {
		this.play = play;
		this.img = img;
		createLevelData();
		createEnemies();
		createObjects();
		createSpike();
		createCanon();
		calLvlOffset();
		calPlayerSpawn();
	}

	private void createCanon() {
		canons = getCanonLst(img);
		
	}

	private void createSpike() {
		spikes = getSpikeLst(img);
	}

	private void createObjects() {
		potions = getPotionLst(img);
		containers = getContainerLst(img);
	}

	private void calPlayerSpawn() {
		playerSpawn = getPlayerSpawn(img);

	}

	private void createEnemies() {
		enemies = getEnemies(img, play);

	}

	private void calLvlOffset() {
		lvlTilesWide = img.getWidth();
		maxTilesOffsetX = lvlTilesWide - GamePanel.TILES_IN_WIDTH;
		maxlvlOffsetX = GamePanel.TILE_SIZE * maxTilesOffsetX;
		lvlTilesHeight = img.getHeight();
		maxTilesOffsetY = lvlTilesHeight - GamePanel.TILES_IN_HEIGHT;
		maxlvlOffsetY = GamePanel.TILE_SIZE * maxTilesOffsetY;
	}

	private void createLevelData() {
		lvlData = GetLevelData(img);
	}

	public int getSpriteIndex(int x, int y) {
		return lvlData[y][x];
	}

	public int[][] getLevelData() {
		return lvlData;
	}

	public int getLvlOffsetX() {
		return maxlvlOffsetX;
	}

	public int getLvlOffsetY() {
		return maxlvlOffsetY;
	}

	public ArrayList<Enemy> getEnemieslst() {
		return enemies;
	}

	public Point getPlayerPoint() {
		return playerSpawn;
	}

	public ArrayList<Potion> getPotions() {
		return potions;
	}

	public ArrayList<GameContainer> getContainers() {
		return containers;
	}

	public ArrayList<Spike> getSpikes() {
		return spikes;
	}

	public ArrayList<Canon> getCanons() {
		return canons;
	}

}
