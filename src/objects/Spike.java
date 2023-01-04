package objects;

import main.GamePanel;

public class Spike extends GameObject{

	public Spike(int x, int y, int objType) {
		super(x, y, objType);
		initHitbox(32,16);
		xDrawOffset = 0;
		yDrawOffset = (int) (GamePanel.SCALE * 16);
		hitbox.y += yDrawOffset;
	}

}
