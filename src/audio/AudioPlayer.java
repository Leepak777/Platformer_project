package audio;

import java.util.Random;
import java.io.IOException;
import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.BooleanControl;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class AudioPlayer {

	public static int MENU_1 = 0;
	public static int LEVEL_1 = 1;
	public static int LEVEL_2 = 2;

	public static int DIE = 0;
	public static int JUMP = 1;
	public static int GAMEOVER = 2;
	public static int LVL_COMPLETED = 3;
	public static int ATTACK_ONE = 4;
	public static int ATTACK_TWO = 5;
	public static int ATTACK_THREE = 6;

	private Clip[] songs, effects;
	private int currentSongid;
	private float volume = 0.5f;
	private boolean songMute, effectMute;
	private Random rand = new Random();

	public AudioPlayer() {
		loadSongs();
		loadEffects();
		playSong(MENU_1);
	}

	private void loadSongs() {
		String[] names = { "menu", "level1", "level2" };
		songs = new Clip[names.length];
		for (int i = 0; i < songs.length; i++) {
			songs[i] = getClip(names[i]);
		}
	}

	private void loadEffects() {
		String[] names = { "die", "jump", "gameover", "lvlcompleted", "attack1", "attack2", "attack2" };
		effects = new Clip[names.length];
		for (int i = 0; i < effects.length; i++) {
			effects[i] = getClip(names[i]);
		}
		updateEffectVolume();
	}

	private Clip getClip(String name) {
		URL url = getClass().getResource("/audio/" + name + ".wav");
		AudioInputStream audio;
		try {
			audio = AudioSystem.getAudioInputStream(url);
			Clip c = AudioSystem.getClip();
			c.open(audio);
			return c;
		} catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void setVolume(Float volume) {
		this.volume = volume;
		updateSongVolume();
		updateEffectVolume();
	}

	public void stopSong() {
		if (songs[currentSongid].isActive()) {
			songs[currentSongid].stop();
		}
	}

	public void setLevelSong(int lvlIndex) {
		if(lvlIndex % 2 == 0) {
			playSong(LEVEL_1);
		}else {
			playSong(LEVEL_2);
		}
	}

	public void lvlCompleted() {
		stopSong();
		playEffect(LVL_COMPLETED);
	}

	public void playAttack() {
		int start = 4;
		start += rand.nextInt(3);
		playEffect(start);
	}

	public void playEffect(int effect) {
		effects[effect].setMicrosecondPosition(0);
		effects[effect].start();

	}

	public void playSong(int song) {
		stopSong();
		currentSongid = song;
		updateSongVolume();
		songs[currentSongid].setMicrosecondPosition(0);
		songs[currentSongid].loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void toggleSong() {
		this.songMute = !songMute;
		for (Clip c : songs) {
			BooleanControl boolcon = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			boolcon.setValue(songMute);
		}
	}

	public void toggleEffect() {
		this.effectMute = !effectMute;
		for (Clip c : effects) {
			BooleanControl boolcon = (BooleanControl) c.getControl(BooleanControl.Type.MUTE);
			boolcon.setValue(effectMute);
		}
		if (!effectMute) {
			playEffect(JUMP);
		}
	}

	private void updateSongVolume() {
		FloatControl gainControl = (FloatControl) songs[currentSongid].getControl(FloatControl.Type.MASTER_GAIN);
		float range = gainControl.getMaximum() - gainControl.getMinimum();
		float gain = (range * volume) + gainControl.getMinimum();
		gainControl.setValue(gain);
	}

	private void updateEffectVolume() {
		for (Clip c : effects) {
			FloatControl gainControl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			float range = gainControl.getMaximum() - gainControl.getMinimum();
			float gain = (range * volume) + gainControl.getMinimum();
			gainControl.setValue(gain);
		}
	}
}
