package Objects;

import main.GamePanel;

public class Potion extends GameObject {

	private float hoverOffset;
	private int maxhoverOffset, hoverDir=1;
	public Potion(int x, int y, int objType) {
		super(x, y, objType);
		doAnimation = true;
		initHitbox(7, 14);
		xDrawOffset = (int) (3 * GamePanel.SCALE);
		yDrawOffset = (int) (2 * GamePanel.SCALE);
		maxhoverOffset = (int) (10*GamePanel.SCALE);
		
	}

	public void update() {
		updateAnimationTick();
		updateHover();
	}

	private void updateHover() {
		hoverOffset += (0.075f * GamePanel.SCALE * hoverDir);
		if(hoverOffset >= maxhoverOffset) {
			hoverDir = -1;
		}
		else if(hoverOffset < 0) {
			hoverDir = 1;
		}
		hitbox.y = y+ hoverOffset;
	}

}
