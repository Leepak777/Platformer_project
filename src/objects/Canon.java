package objects;

import main.GamePanel;

public class Canon extends GameObject{

	private int tileY;
	public Canon(int x, int y, int objType) {
		super(x, y, objType);
		tileY = y/GamePanel.TILE_SIZE;
		initHitbox(40,26);
		hitbox.x -= (int)(4*GamePanel.SCALE);
		hitbox.y += (int) (6*GamePanel.SCALE);
	}
	
	public void update() {
		if(doAnimation) {
			updateAnimationTick();
		}
	}

	public int getTileY() {
		return tileY;
	}

	
	
	
}
