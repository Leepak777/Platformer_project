package levels;

import main.Game;
import main.GamePanel;
import utilz.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import gamestates.GameState;
import gamestates.Play;

public class LevelManager {
	private Play play;
	private BufferedImage[] levelSprite;
	private BufferedImage[] waterSprite;
	private ArrayList<Level> lvls;
	private int lvlIndex = 0, aniTick, aniIndex;

	public LevelManager(Play play) {
		this.play = play;
		importOutsideSprite();
		createWater();
		lvls = new ArrayList<>();
		buildAllLevels();
	}

	private void createWater() {
		waterSprite = new BufferedImage[5];
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.WATER_TOP);
		for (int i = 0; i < 4; i++)
			waterSprite[i] = img.getSubimage(i * 32, 0, 32, 32);
		waterSprite[4] = LoadSave.GetSpriteAtlas(LoadSave.WATER_BOTTOM);
	}

	private void buildAllLevels() {
		BufferedImage[] alllvls = LoadSave.GetAllLevels();
		for (BufferedImage img : alllvls) {
			lvls.add(new Level(img, play));
		}

	}

	private void importOutsideSprite() {
		BufferedImage img = LoadSave.GetSpriteAtlas(LoadSave.LEVEL_ATLAS);
		levelSprite = new BufferedImage[48];
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 12; i++) {
				int index = j * 12 + i;
				levelSprite[index] = img.getSubimage(i * 32, j * 32, 32, 32);
			}
		}
	}

	public void draw(Graphics g, int xLvlOffset, int yLvlOffset) {
		for (int j = 0; j < GamePanel.TILES_IN_HEIGHT; j++)
			for (int i = 0; i < lvls.get(lvlIndex).getLevelData()[0].length; i++) {
				int index = lvls.get(lvlIndex).getSpriteIndex(i, j);
				int x = GamePanel.TILE_SIZE * i - xLvlOffset;
				int y = GamePanel.TILE_SIZE * j - yLvlOffset;
				if (index == 48)
					g.drawImage(waterSprite[aniIndex], x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
				else if (index == 49)
					g.drawImage(waterSprite[4], x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
				else
					g.drawImage(levelSprite[index], x, y, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
			}
	}

	public void update() {
		updateWaterAnimation();
	}

	private void updateWaterAnimation() {
		aniTick++;
		if (aniTick >= 40) {
			aniTick = 0;
			aniIndex++;

			if (aniIndex >= 4)
				aniIndex = 0;
		}
	}

	public Level getCurrentLevel() {
		return lvls.get(lvlIndex);
	}

	public int getPixelHeight() {
		return lvls.get(lvlIndex).getLevelData().length;
	}

	public void setLevelIndex(int lvlIndex) {
		this.lvlIndex = lvlIndex;
	}

	public int getPixelWidth() {
		return lvls.get(lvlIndex).getLevelData()[0].length;
	}

	public int getLvlAmount() {
		return lvls.size();
	}

	public int getLvlIndex() {
		return lvlIndex;
	}

	public int getAmountOfLevels() {
		return lvls.size();
	}

	public void loadNextLevel() {
		// lvlIndex++;
		/*
		 * if (lvlIndex >= lvls.size()) { lvlIndex = 0;
		 * System.out.println("All Levels Completed"); GameState.state = GameState.MENU;
		 * }
		 */
		Level newLevel = lvls.get(lvlIndex);
		play.getEM().LoadEnemies(newLevel);
		play.getPlayer().loadlvlData(newLevel.getLevelData());
		play.setLvlOffset(newLevel.getLvlOffsetX(), newLevel.getLvlOffsetY());
		play.getOM().loadObjects(newLevel);
	}
}
