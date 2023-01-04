package Objects;

import static utilz.Constants.ObjectsConstants.*;
import static utilz.Constants.ANISPEED;


import java.awt.Graphics;
import java.awt.geom.Rectangle2D;

import main.GamePanel;

public class GameObject {
	protected int x, y, objType;
	protected Rectangle2D.Float hitbox;
	protected boolean doAnimation, active = true;
	protected int aniTick, aniIndex;
	protected int xDrawOffset, yDrawOffset;
	
	public GameObject(int x, int y, int objType) {
		this.x = x;
		this.y = y;
		this.objType = objType;
	}
	protected void updateAnimationTick() {
		aniTick++;
		if (aniTick >= ANISPEED) {
			aniTick = 0;
			aniIndex++;
			if (aniIndex >= GetSpriteAmount(objType)) {
				aniIndex = 0;
				if(objType == BARREL || objType == BOX) {
					doAnimation = false;
					active = false;
				}
				else if(objType == CANON_LEFT || objType == CANON_RIGHT) {
					doAnimation = false;
				}
				

			}
		}
	}

	public void reset() {
		aniTick = 0;
		aniIndex = 0;
		active = true;
		if(objType == BARREL || objType == BOX || objType == CANON_LEFT|| objType == CANON_RIGHT) {
			doAnimation = false;
		}
		else {
			doAnimation = true;
		}
	}
	protected void initHitbox(int width, int height) {
		hitbox = new Rectangle2D.Float(x,y,(int)(width*GamePanel.SCALE), (int)(height * GamePanel.SCALE));
	}
	
	public void drawHitBox(Graphics g, int grade, int xLvlOffset, int yLvlOffset) {
		g.drawRect((int) hitbox.x - xLvlOffset, (int) hitbox.y - yLvlOffset, (int) hitbox.width, (int) hitbox.height);
	}
	public int getObjType() {
		return objType;
	}

	public Rectangle2D.Float getHitbox() {
		return hitbox;
	}

	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	public void setDoAnimation(boolean doAnimation) {
		this.doAnimation = doAnimation;
	}
	public int getxDrawOffset() {
		return xDrawOffset;
	}

	public int getyDrawOffset() {
		return yDrawOffset;
	}
	public int getAniIndex() {
		return aniIndex;
	}
	public int getAnimationTick() {
		return aniTick;
	}
	
	
}
