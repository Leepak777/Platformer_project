package utilz;

import javax.imageio.ImageIO;

import entities.Crabby;
import main.GamePanel;
import static utilz.Constants.EnemyConstants.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

public class LoadSave {
	public static final String PLAYER_ATLAS = "player_sprites.png";
	public static final String HAKI_ATLAS = "haki.png";
	public static final String HAKI_2_ATLAS = "haki_.png";
	public static final String LEVEL_ATLAS = "outside_sprites.png";
	public static final String LEVEL_ONE_DATA = "lvls/level_one_data_edited.png";
	public static final String LEVEL_TWO_DATA = "lvls/1.png";
	public static final String LEVEL_THREE_DATA = "lvls/2.png";
	public static final String LEVEL_FOUR_DATA = "lvls/3.png";
	public static final String MENU_BUTTONS = "button_atlas.png";
	public static final String MENU_BACKGROUNDS = "menu_background.png";
	public static final String MENU_BACKGROUNDS_IMAGE = "background_menu.png";
	public static final String PAUSE_BACKGROUND = "pause_menu.png";
	public static final String SOUND_BUTTONS = "sound_button.png";
	public static final String URM_BUTTONS = "urm_buttons.png";
	public static final String VOLUME_BUTTONS = "volume_buttons.png";
	public static final String PLAYING_BACKGROUND_IMAGE = "playing_bg_img_rotated.png";
	public static final String BIG_CLOUDS = "big_clouds_rotated.png";
	public static final String SMALL_CLOUDS = "small_clouds_rotated.png";
	public static final String CRABBY_SPRITE = "crabby_sprite.png";
	public static final String STATUS_BAR = "health_power_bar.png";
	public static final String LEVEL_COMPLETED = "completed_sprite.png";
	public static final String POTION_ATLAS = "potions_sprites.png";
	public static final String CONTAINER_ATLAS = "objects_sprites.png";
	public static final String TRAP_ATLAS = "trap_atlas.png";
	public static final String CANON_ATLAS = "cannon_atlas.png";
	public static final String BALL_ATLAS = "ball.png";
	public static final String DEATH_SCREEN = "death_screen.png";
	
	public static BufferedImage GetSpriteAtlas(String filename) {
		BufferedImage img = null;
		InputStream is = LoadSave.class.getResourceAsStream("/" + filename);
		try {
			img = ImageIO.read(is);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return img;
	}

	public static BufferedImage[] GetAllLevels() {
		URL url = LoadSave.class.getResource("/lvls");
		File file = null;
		try {
			file = new File(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		File[] files = file.listFiles();

		File[] sortedF = new File[files.length];
		for (int i = 0; i < sortedF.length; i++) {
			for (int j = 0; j < files.length; j++) {
				if (files[j].getName().equals(i + ".png")) {
					sortedF[i] = files[j];
				}
			}
		}

		BufferedImage[] imgs = new BufferedImage[sortedF.length];
		for (int i = 0; i < sortedF.length; i++) {
			try {
				imgs[i] = ImageIO.read(sortedF[i]);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return imgs;
	}
}
