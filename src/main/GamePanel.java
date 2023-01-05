package main;

import java.awt.*;
import javax.swing.JPanel;

import gamestates.*;
import gamestates.Menu;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import ui.AudioOptions;

public class GamePanel extends JPanel {

	private MouseInputs mouseInputs;
	private KeyboardInputs key;
	private Game game;
	private Play play;
	private Menu menu;
	private AudioOptions audioOp;
	private GameOptions gameOp;
	public final static int TILE_DEFAULT_SIZE = 32;
	public static float SCALE = 1.5f;
	public static final int TILES_IN_WIDTH = 26;
	public static final int TILES_IN_HEIGHT = 14;
	public final static int TILE_SIZE = (int) (TILE_DEFAULT_SIZE * SCALE);
	public final static int GAME_WIDTH = TILE_SIZE * TILES_IN_WIDTH;
	public static final int GAME_HEIGHT = TILE_SIZE * TILES_IN_HEIGHT;

	public GamePanel(Game game) {
		audioOp = new AudioOptions();
		this.game = game;
		play = new Play(this);
		menu = new Menu(this);
		mouseInputs = new MouseInputs(this);
		key = new KeyboardInputs(this);
		gameOp = new GameOptions(this);
		setPanelSize();
		addKeyListener(key);
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
		// initClasses();
	}

	private void setPanelSize() {
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
		System.out.println("size: " + GAME_WIDTH + ", " + GAME_HEIGHT);
	}

	public void updateGame() {

		switch (GameState.state) {
		case MENU:
			menu.update();
			break;
		case PLAYING:
			play.update();
			break;
		case OPTIONS:
			gameOp.update();
			break;
		case QUIT:
		default:
			System.exit(0);
			break;

		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		switch (GameState.state) {
		case MENU:
			menu.draw(g);
			break;
		case PLAYING:
			play.draw(g);
			break;
		case OPTIONS:
			gameOp.draw(g);
			break;
		default:
			break;

		}

	}

	public Game getGame() {
		return game;
	}

	public KeyboardInputs getKey() {
		return key;
	}

	public MouseInputs getMouse() {
		return mouseInputs;
	}

	public void windowFocusLost() {
		if (GameState.state == GameState.PLAYING) {
			play.getPlayer().resetDirBooleans();
		}

	}

	public GameOptions getGameOp() {
		return gameOp;
	}

	public AudioOptions getAudioOp() {
		return audioOp;
	}

	public Play getPlay() {
		return this.play;
	}

	public gamestates.Menu getMenu() {
		return menu;
	}

}