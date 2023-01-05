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
	private ArrayList<Level> lvls;
	private int lvlIndex = 0;

	public LevelManager(Play play) {
		this.play = play;
		importOutsideSprite();
		lvls = new ArrayList<>();
		buildAllLevels();
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
		for (int j = 0; j < lvls.get(lvlIndex).getLevelData().length; j++) {
			for (int i = 0; i < lvls.get(lvlIndex).getLevelData()[0].length; i++) {
				int index = lvls.get(lvlIndex).getSpriteIndex(i, (j));
				g.drawImage(levelSprite[index], GamePanel.TILE_SIZE * i - xLvlOffset,
						j * GamePanel.TILE_SIZE - yLvlOffset, GamePanel.TILE_SIZE, GamePanel.TILE_SIZE, null);
			}
		}
	}

	public void update() {

	}

	public Level getCurrentLevel() {
		return lvls.get(lvlIndex);
	}

	public int getPixelHeight() {
		return lvls.get(lvlIndex).getLevelData().length;
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

	public void loadNextLevel() {
		lvlIndex++;
		if (lvlIndex >= lvls.size()) {
			lvlIndex = 0;
			System.out.println("All Levels Completed");
			GameState.state = GameState.MENU;
		}
		Level newLevel = lvls.get(lvlIndex);
		play.getEM().LoadEnemies(newLevel);
		play.getPlayer().loadlvlData(newLevel.getLevelData());
		play.setLvlOffset(newLevel.getLvlOffsetX(), newLevel.getLvlOffsetY());
		play.getOM().loadObjects(newLevel);
	}
}
