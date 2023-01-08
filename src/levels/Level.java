package levels;

import java.awt.Color;

import java.awt.Point;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import objects.BackgroundTree;
import objects.Canon;
import objects.GameContainer;
import objects.Grass;
import objects.Potion;
import objects.Spike;

import static utilz.Constants.EnemyConstants.CRABBY;
import static utilz.Constants.EnemyConstants.PINKSTAR;
import static utilz.Constants.EnemyConstants.SHARK;
import static utilz.Constants.ObjectsConstants.*;

import static utilz.HelpMethods.*;
import entities.Crabby;
import entities.Enemy;
import entities.Roll;
import entities.Shark;
import gamestates.Play;
import main.Game;
import main.GamePanel;

public class Level {
	private Play play;
	private BufferedImage img;
	private ArrayList<Crabby> crabs = new ArrayList<>();
	private ArrayList<Roll> pinkstars = new ArrayList<>();
	private ArrayList<Shark> sharks = new ArrayList<>();
	private ArrayList<Enemy> enemies = new ArrayList<>();
	private ArrayList<Potion> potions = new ArrayList<>();
	private ArrayList<Spike> spikes = new ArrayList<>();
	private ArrayList<Canon> canons = new ArrayList<>();
	private ArrayList<GameContainer> containers = new ArrayList<>();
	private ArrayList<BackgroundTree> trees = new ArrayList<>();
	private ArrayList<Grass> grass = new ArrayList<>();
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
		lvlData = new int[img.getHeight()][img.getWidth()];
		loadLevel();
		calLvlOffset();
	}

	private void loadLevel() {

		// Looping through the image colors just once. Instead of one per
		// object/enemy/etc..
		// Removed many methods in HelpMethods class.

		for (int y = 0; y < img.getHeight(); y++)
			for (int x = 0; x < img.getWidth(); x++) {
				Color c = new Color(img.getRGB(x, y));
				int red = c.getRed();
				int green = c.getGreen();
				int blue = c.getBlue();

				loadLevelData(red, x, y);
				loadEntities(green, x, y);
				loadObjects(blue, x, y);
			}
	}

	private void loadLevelData(int redValue, int x, int y) {
		if (redValue >= 50) {
			lvlData[y][x] = 0;
		} else {
			lvlData[y][x] = redValue;
		}
		switch (redValue) {
		case 0, 1, 2, 3, 30, 31, 33, 34, 35, 36, 37, 38, 39 -> grass.add(new Grass((int) (x * GamePanel.TILE_SIZE),
				(int) (y * GamePanel.TILE_SIZE) - GamePanel.TILE_SIZE, getRndGrassType(x)));
		}
	}

	private int getRndGrassType(int xPos) {
		return xPos % 2;
	}

	private void loadEntities(int greenValue, int x, int y) {
		switch (greenValue) {
		case CRABBY -> {
			Crabby crab = new Crabby(x * GamePanel.TILE_SIZE, y * GamePanel.TILE_SIZE, play);
			crabs.add(crab);
			enemies.add(crab);
		}
		case PINKSTAR -> {
			Roll roll = new Roll(x * GamePanel.TILE_SIZE, y * GamePanel.TILE_SIZE, play);
			pinkstars.add(roll);
			enemies.add(roll);
		}
		case SHARK -> {
			Shark shark = new Shark(x * GamePanel.TILE_SIZE, y * GamePanel.TILE_SIZE, play);
			sharks.add(shark);
			enemies.add(shark);
		}
		case 100 -> playerSpawn = new Point(x * GamePanel.TILE_SIZE, y * GamePanel.TILE_SIZE);
		}
	}

	private void loadObjects(int blueValue, int x, int y) {
		switch (blueValue) {
		case RED_POTION, BLUE_POTION ->
			potions.add(new Potion(x * GamePanel.TILE_SIZE, y * GamePanel.TILE_SIZE, blueValue));
		case BOX, BARREL ->
			containers.add(new GameContainer(x * GamePanel.TILE_SIZE, y * GamePanel.TILE_SIZE, blueValue));
		case SPIKE -> spikes.add(new Spike(x * GamePanel.TILE_SIZE, y * GamePanel.TILE_SIZE, SPIKE));
		case CANON_LEFT, CANON_RIGHT ->
			canons.add(new Canon(x * GamePanel.TILE_SIZE, y * GamePanel.TILE_SIZE, blueValue));
		case TREE_ONE, TREE_TWO, TREE_THREE ->
			trees.add(new BackgroundTree(x * GamePanel.TILE_SIZE, y * GamePanel.TILE_SIZE, blueValue));
		}
	}

	private void calLvlOffset() {
		lvlTilesWide = img.getWidth();
		maxTilesOffsetX = lvlTilesWide - GamePanel.TILES_IN_WIDTH;
		maxlvlOffsetX = GamePanel.TILE_SIZE * maxTilesOffsetX;
		lvlTilesHeight = img.getHeight();
		maxTilesOffsetY = lvlTilesHeight - GamePanel.TILES_IN_HEIGHT;
		maxlvlOffsetY = GamePanel.TILE_SIZE * maxTilesOffsetY;
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

	public ArrayList<Crabby> getCrabs() {
		return crabs;
	}

	public ArrayList<Shark> getSharks() {
		return sharks;
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

	public ArrayList<Canon> getCannons() {
		return canons;
	}

	public ArrayList<Roll> getPinkstars() {
		return pinkstars;
	}

	public ArrayList<BackgroundTree> getTrees() {
		return trees;
	}

	public ArrayList<Grass> getGrass() {
		return grass;
	}

}
