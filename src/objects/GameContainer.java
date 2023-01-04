package objects;

import static utilz.Constants.ObjectsConstants.*;

import main.GamePanel;

public class GameContainer extends GameObject {

	public GameContainer(int x, int y, int objType) {
		super(x, y, objType);
		createHitbox();
	}

	private void createHitbox() {
		if (objType == BOX) {
			initHitbox(28, 18);

			xDrawOffset = (int) (7 * GamePanel.SCALE);
			yDrawOffset = (int) (12 * GamePanel.SCALE);
		} else {
			initHitbox(23, 25);

			xDrawOffset = (int) (8 * GamePanel.SCALE);
			yDrawOffset = (int) (5 * GamePanel.SCALE);
		}
		hitbox.y += yDrawOffset + (int) (GamePanel.SCALE *2);
		hitbox.x += xDrawOffset / 2;
	}

	public void update() {
		if (doAnimation) {
			updateAnimationTick();
		}
	}
}
