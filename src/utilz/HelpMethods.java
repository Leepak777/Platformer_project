package utilz;

import static utilz.Constants.EnemyConstants.CRABBY;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Rectangle2D.Float;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import objects.Canon;
import objects.GameContainer;
import objects.Potion;
import objects.Projectile;
import objects.Spike;
import entities.Crabby;
import entities.Enemy;
import entities.Player;
import gamestates.Play;
import main.Game;
import main.GamePanel;
import objects.Projectile;

import static utilz.Constants.ObjectsConstants.*;

public class HelpMethods {
	public static boolean CanMoveHere(float x, float y, float width, float height, int[][] lvlData) {
		if (!isSolid(x, y, lvlData)) {
			if (!isSolid(x + width, y + height, lvlData)) {
				if (!isSolid(x + width, y, lvlData)) {
					if (!isSolid(x, y + height, lvlData)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private static boolean isSolid(float x, float y, int[][] lvlData) {
		int maxWidth = lvlData[0].length * GamePanel.TILE_SIZE;
		int maxHeight = lvlData.length * GamePanel.TILE_SIZE;
		if (x < 0 || x >= maxWidth) {
			return true;
		}
		if (y < 0 || y >= maxHeight) {
			return true;
		}
		float xIndex = x / GamePanel.TILE_SIZE;
		float yIndex = y / GamePanel.TILE_SIZE;
		return isTileSolid(xIndex, yIndex, lvlData);
	}

	public static boolean IsProjectileHittingLevel(Projectile p, int[][] lvlData) {
		return isSolid(p.getHitbox().x + p.getHitbox().width / 2, p.getHitbox().y + p.getHitbox().height / 2, lvlData);
	}

	public static boolean IsEntityInWater(Rectangle2D.Float hitbox, int[][] lvlData) {
		// Will only check if entity touch top water. Can't reach bottom water if not
		// touched top water.
		if (GetTileValue(hitbox.x, hitbox.y + hitbox.height, lvlData) != 48)
			if (GetTileValue(hitbox.x + hitbox.width, hitbox.y + hitbox.height, lvlData) != 48)
				return false;
		return true;
	}

	private static int GetTileValue(float xPos, float yPos, int[][] lvlData) {
		int xCord = (int) (xPos / GamePanel.TILE_SIZE);
		int yCord = (int) (yPos / GamePanel.TILE_SIZE);
		return lvlData[yCord][xCord];
	}

	public static boolean isEntityX(Rectangle2D.Float playerBox, float increase, ArrayList<Enemy> crabs) {
		for (Enemy c : crabs) {
			if (c.isActive()) {
				Rectangle2D.Float hit = c.getHitbox();
				float diff = (float) (playerBox.height - hit.height);
				if (increase > 0) {
					if (hit.contains(playerBox.x + playerBox.width + increase, playerBox.y + diff * 1.2)) {
						return true;
					}
				} else {
					if (hit.contains(playerBox.x + increase, playerBox.y + diff * 1.2)) {
						return true;
					}
				}

			}
		}

		return false;
	}

	public static boolean isOnEntity(Rectangle2D.Float playerBox, ArrayList<Enemy> crabs) {
		for (Enemy c : crabs) {
			if (c.isActive()) {
				Rectangle2D.Float hit = c.getHitbox();
				if (hit.contains(playerBox.x, playerBox.y + playerBox.height + 1)
						|| hit.contains(playerBox.x + playerBox.width, playerBox.y + playerBox.height + 1)) {
					return true;
				}

			}
		}

		return false;
	}

	public static boolean IsBallHittingLevel(Projectile b, int[][] levelData) {
		return isSolid(b.getHitbox().x + b.getHitbox().width / 2, b.getHitbox().y + b.getHitbox().height / 2,
				levelData);
	}

	private static boolean isTileSolid(float x, float y, int[][] lvlData) {

		int value = lvlData[(int) y][(int) x];
		/*
		 * switch (value) { case 11, 48, 49: return false; default: return true; }
		 */
		if (value >= 48 || value < 0 || value != 11) {
			return true;
		}

		return false;

	}

	public static float GetEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed, boolean hitEn) {
		int currentTile = (int) (hitbox.x / GamePanel.TILE_SIZE);

		if (xSpeed > 0) {
			int tileXPos = currentTile * GamePanel.TILE_SIZE;
			int xOffset = (int) (GamePanel.TILE_SIZE - hitbox.width);
			return tileXPos + xOffset - 1;
		} else {
			if (hitEn) {
				return (float) ((currentTile + 0.5) * GamePanel.TILE_SIZE);
			}
			return currentTile * GamePanel.TILE_SIZE;
		}
	}

	public static float GetEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
		int currentTile = (int) (hitbox.y / GamePanel.TILE_SIZE);
		if (airSpeed > 0) {
			int tileYPos = currentTile * GamePanel.TILE_SIZE;
			int yOffset = (int) (GamePanel.TILE_SIZE - hitbox.height);
			return tileYPos + yOffset - 1;
		} else {
			return currentTile * GamePanel.TILE_SIZE;
		}

	}

	public static boolean onFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData)) {
			if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData)) {
				return false;
			}
		}
		return true;
	}

	public static boolean IsFloor(Rectangle2D.Float hitbox, float xSpeed, int[][] lvlData) {
		if (xSpeed > 0) {
			return isSolid(hitbox.x + hitbox.width + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		} else {
			return isSolid(hitbox.x + xSpeed, hitbox.y + hitbox.height + 1, lvlData);
		}
	}

	public static boolean isFloor(Rectangle2D.Float hitbox, int[][] lvlData) {
		if (!isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height + 1, lvlData))
			if (!isSolid(hitbox.x, hitbox.y + hitbox.height + 1, lvlData))
				return false;
		return true;
	}

	public static boolean CanonSeePlayer(int[][] lvlData, Rectangle2D.Float hitbox, Rectangle2D.Float hitbox2,
			int tileY) {
		int firstXTile = (int) (hitbox.x / GamePanel.TILE_SIZE);
		int secondXTile = (int) (hitbox2.x / GamePanel.TILE_SIZE);
		if (firstXTile > secondXTile) {
			return isAllTileClear(secondXTile, firstXTile, tileY, lvlData);
		} else {
			return isAllTileClear(firstXTile, secondXTile, tileY, lvlData);

		}
	}

	public static boolean isAllTileClear(int xStart, int xEnd, int y, int[][] lvlData) {
		for (int x = 0; x < xEnd - xStart; x++) {
			if (isTileSolid(x + xStart, y, lvlData)) {
				return false;
			}
		}
		return true;
	}

	public static boolean isAllTileWalkable(int xStart, int xEnd, int y, int[][] lvlData) {
		if (isAllTileClear(xStart, xEnd, y, lvlData))
			for (int x = 0; x < xEnd - xStart; x++) {
				if (y + 1 <= GamePanel.TILES_IN_HEIGHT) {
					if (!isTileSolid(x + xStart, y + 1, lvlData)) {
						return false;
					}
				}
			}
		return true;
	}

	public static boolean isSightClear(int[][] lvlData, Rectangle2D.Float hitbox, Rectangle2D.Float hitbox2,
			int tileY) {
		int firstXTile = (int) (hitbox.x / GamePanel.TILE_SIZE);
		int secondXTile = (int) (hitbox2.x / GamePanel.TILE_SIZE);
		if (firstXTile > secondXTile) {
			return isAllTileWalkable(secondXTile, firstXTile, tileY, lvlData);
		} else {
			return isAllTileWalkable(firstXTile, secondXTile, tileY, lvlData);

		}
	}

	
}
