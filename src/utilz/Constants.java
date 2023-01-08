package utilz;

import main.Game;

import main.GamePanel;

public class Constants {
	public static final float GRAVITY = 0.04f * GamePanel.SCALE;
	public static final int ANISPEED = 25;
	public static class Dialogue {
		public static final int QUESTION = 0;
		public static final int EXCLAMATION = 1;

		public static final int DIALOGUE_WIDTH = (int) (14 * GamePanel.SCALE);
		public static final int DIALOGUE_HEIGHT = (int) (12 * GamePanel.SCALE);

		public static int GetSpriteAmount(int type) {
			switch (type) {
			case QUESTION, EXCLAMATION:
				return 5;
			}

			return 0;
		}
	}

	public static class Projectiles{
		public static final int BALL_WIDTH_DEFAULT = 15;
		public static final int BALL_WIDTH = (int) (BALL_WIDTH_DEFAULT * GamePanel.SCALE);
		public static final int BALL_HEIGHT_DEFAULT = 15;
		public static final int BALL_HEIGHT = (int) (BALL_HEIGHT_DEFAULT * GamePanel.SCALE);
		
		public static final float SPEED = 0.5f * GamePanel.SCALE;
	}
	public static class ObjectsConstants {

		public static final int RED_POTION = 0;
		public static final int BLUE_POTION = 1;
		public static final int BARREL = 2;
		public static final int BOX = 3;
		public static final int SPIKE = 4;
		public static final int CANON_LEFT = 5;
		public static final int CANON_RIGHT= 6;
		public static final int TREE_ONE = 7;
		public static final int TREE_TWO = 8;
		public static final int TREE_THREE = 9;


		public static final int RED_POTION_VALUE = 15;
		public static final int BLUE_POTION_VALUE = 10;

		public static final int CONTAINER_WIDTH_DEFAULT = 40;
		public static final int CONTAINER_WIDTH = (int) (CONTAINER_WIDTH_DEFAULT * GamePanel.SCALE);
		public static final int CONTAINER_HEIGHT_DEFAULT = 30;
		public static final int CONTAINER_HEIGHT = (int) (CONTAINER_HEIGHT_DEFAULT * GamePanel.SCALE);

		public static final int POTION_WIDTH_DEFAULT = 12;
		public static final int POTION_WIDTH = (int) (POTION_WIDTH_DEFAULT * GamePanel.SCALE);
		public static final int POTION_HEIGHT_DEFAULT = 16;
		public static final int POTION_HEIGHT = (int) (POTION_HEIGHT_DEFAULT * GamePanel.SCALE);
		
		public static final int SPIKE_WIDTH_DEFAULT = 32;
		public static final int SPIKE_WIDTH = (int) (SPIKE_WIDTH_DEFAULT * GamePanel.SCALE);
		public static final int SPIKE_HEIGHT_DEFAULT = 32;
		public static final int SPIKE_HEIGHT = (int) (SPIKE_HEIGHT_DEFAULT * GamePanel.SCALE);

		public static final int CANON_WIDTH_DEFAULT = 40;
		public static final int CANON_WIDTH = (int) (CANON_WIDTH_DEFAULT * GamePanel.SCALE);
		public static final int CANON_HEIGHT_DEFAULT = 26;
		public static final int CANON_HEIGHT = (int) (CANON_HEIGHT_DEFAULT * GamePanel.SCALE);
		
		public static int GetSpriteAmount(int object_type) {
			switch (object_type) {
			case RED_POTION, BLUE_POTION:
				return 7;
			case BARREL, BOX:
				return 8;
			case CANON_LEFT, CANON_RIGHT:
				return 7;
			}
			return 1;
		}
		public static int GetTreeOffsetX(int treeType) {
			switch (treeType) {
			case TREE_ONE:
				return (GamePanel.TILE_SIZE / 2) - (GetTreeWidth(treeType) / 2);
			case TREE_TWO:
				return (int) (GamePanel.TILE_SIZE / 2.5f);
			case TREE_THREE:
				return (int) (GamePanel.TILE_SIZE / 1.65f);
			}

			return 0;
		}

		public static int GetTreeOffsetY(int treeType) {

			switch (treeType) {
			case TREE_ONE:
				return -GetTreeHeight(treeType) + GamePanel.TILE_SIZE * 2;
			case TREE_TWO, TREE_THREE:
				return -GetTreeHeight(treeType) + (int) (GamePanel.TILE_SIZE / 1.25f);
			}
			return 0;

		}

		public static int GetTreeWidth(int treeType) {
			switch (treeType) {
			case TREE_ONE:
				return (int) (39 * GamePanel.SCALE);
			case TREE_TWO:
				return (int) (62 * GamePanel.SCALE);
			case TREE_THREE:
				return -(int) (62 * GamePanel.SCALE);

			}
			return 0;
		}

		public static int GetTreeHeight(int treeType) {
			switch (treeType) {
			case TREE_ONE:
				return (int) (int) (92 * GamePanel.SCALE);
			case TREE_TWO, TREE_THREE:
				return (int) (54 * GamePanel.SCALE);

			}
			return 0;
		}
	}

	public static class ENVIRONMENT {
		public static final int BIG_CLOUD_WIDTH_DEFAULT = 101;
		public static final int BIG_CLOUD_WIDTH = (int) (BIG_CLOUD_WIDTH_DEFAULT * GamePanel.SCALE);
		public static final int BIG_CLOUD_HEIGHT_DEFAULT = 440;
		public static final int BIG_CLOUD_HEIGHT = (int) (BIG_CLOUD_HEIGHT_DEFAULT * GamePanel.SCALE);

		public static final int SMALL_CLOUD_WIDTH_DEFAULT = 24;
		public static final int SMALL_CLOUD_WIDTH = (int) (SMALL_CLOUD_WIDTH_DEFAULT * GamePanel.SCALE);
		public static final int SMALL_CLOUD_HEIGHT_DEFAULT = 74;
		public static final int SMALL_CLOUD_HEIGHT = (int) (SMALL_CLOUD_HEIGHT_DEFAULT * GamePanel.SCALE);
	}

	public static class UI {
		public static class Buttons {
			public static final int B_WIDTH_DEFAULT = 140;
			public static final int B_HEIGHT_DEFAULT = 56;
			public static final int B_WIDTH = (int) (B_WIDTH_DEFAULT * GamePanel.SCALE);
			public static final int B_HEIGHT = (int) (B_HEIGHT_DEFAULT * GamePanel.SCALE);
		}

		public static class PauseButtons {
			public static final int SOUND_SIZE_DEFAULT = 42;
			public static final int SOUND_SIZE = (int) (SOUND_SIZE_DEFAULT * GamePanel.SCALE);
		}

		public static class URMButtons {
			public static final int URM_SIZE_DEFAULT = 56;
			public static final int URM_SIZE = (int) (URM_SIZE_DEFAULT * GamePanel.SCALE);
		}

		public static class VolumeButtons {
			public static final int VOLUME_WIDTH_DEFAULT = 28;
			public static final int VOLUME_HEIGHT_DEFAULT = 44;
			public static final int SLIDER_WIDTH_DEFAULT = 215;
			public static final int VOLUME_WIDTH = (int) (VOLUME_WIDTH_DEFAULT * GamePanel.SCALE);
			public static final int VOLUME_HEIGHT = (int) (VOLUME_HEIGHT_DEFAULT * GamePanel.SCALE);
			public static final int SLIDER_WIDTH = (int) (SLIDER_WIDTH_DEFAULT * GamePanel.SCALE);

		}
	}

	public static class Directions {
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}

	public static class PlayerConstants {
		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int JUMP = 2;
		public static final int FALLING = 3;
		public static final int HIT = 5;
		public static final int ATTACK = 4;
		public static final int DEAD = 6;

		public static int GetSpriteAmount(int player_action) {
			switch (player_action) {
			case RUNNING:
				return 6;
			case IDLE:
				return 5;
			case HIT:
				return 4;
			case JUMP:
			case ATTACK:
				return 3;
			case DEAD:
				return 8;
			case FALLING:
			default:
				return 1;
			}
		}
	}

	public static class EnemyConstants {
		public static final int CRABBY = 0;
		public static final int PINKSTAR = 1;
		public static final int SHARK = 2;

		public static final int IDLE = 0;
		public static final int RUNNING = 1;
		public static final int ATTACK = 2;
		public static final int HIT = 3;
		public static final int DEAD = 4;

		public static final int CRABBY_WIDTH_DEFAULT = 72;
		public static final int CRABBY_HEIGHT_DEFAULT = 32;
		public static final int CRABBY_WIDTH = (int) (CRABBY_WIDTH_DEFAULT * GamePanel.SCALE);
		public static final int CRABBY_HEIGHT = (int) (CRABBY_HEIGHT_DEFAULT * GamePanel.SCALE);
		public static final int CRABBY_DRAWOFFSET_X = (int) (26 * GamePanel.SCALE);
		public static final int CRABBY_DRAWOFFSET_Y = (int) (9 * GamePanel.SCALE);
		
		public static final int PINKSTAR_WIDTH_DEFAULT = 34;
		public static final int PINKSTAR_HEIGHT_DEFAULT = 30;
		public static final int PINKSTAR_WIDTH = (int) (PINKSTAR_WIDTH_DEFAULT * GamePanel.SCALE);
		public static final int PINKSTAR_HEIGHT = (int) (PINKSTAR_HEIGHT_DEFAULT * GamePanel.SCALE);
		public static final int PINKSTAR_DRAWOFFSET_X = (int) (9 * GamePanel.SCALE);
		public static final int PINKSTAR_DRAWOFFSET_Y = (int) (7 * GamePanel.SCALE);

		public static final int SHARK_WIDTH_DEFAULT = 34;
		public static final int SHARK_HEIGHT_DEFAULT = 30;
		public static final int SHARK_WIDTH = (int) (SHARK_WIDTH_DEFAULT * GamePanel.SCALE);
		public static final int SHARK_HEIGHT = (int) (SHARK_HEIGHT_DEFAULT * GamePanel.SCALE);
		public static final int SHARK_DRAWOFFSET_X = (int) (8 * GamePanel.SCALE);
		public static final int SHARK_DRAWOFFSET_Y = (int) (6 * GamePanel.SCALE);

		public static int GetSpriteAmount(int enemyType, int enemyState) {
			switch (enemyState) {

			case IDLE: {
				if (enemyType == CRABBY)
					return 9;
				else if (enemyType == PINKSTAR || enemyType == SHARK)
					return 8;
			}
			case RUNNING:
				return 6;
			case ATTACK:
				if (enemyType == SHARK)
					return 8;
				return 7;
			case HIT:
				return 4;
			case DEAD:
				return 5;
			}

			return 0;

		}

		public static int GetMaxHealth(int enemyType) {
			switch (enemyType) {
			case CRABBY:
				return 100;
			case PINKSTAR, SHARK:
				return 75;
			default:
				return 1;
			}
		}

		public static int GetEnemyDamage(int enemyType) {
			switch (enemyType) {
			case CRABBY:
				return 10;
			case PINKSTAR:
				return 20;
			case SHARK:
				return 25;
			default:
				return 0;
			}
		}
	}

}
