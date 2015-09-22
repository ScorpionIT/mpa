package mpa.core.util;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class PlayerMusicThread implements Runnable
{

	private Clip clip;

	public PlayerMusicThread(String path)
	{
		AudioInputStream audioIn;
		try
		{
			audioIn = AudioSystem.getAudioInputStream(new File(path));
			clip = AudioSystem.getClip();
			clip.open(audioIn);

		} catch (LineUnavailableException | UnsupportedAudioFileException | IOException e)
		{
			// TODO Blocco catch generato automaticamente
			e.printStackTrace();
		}
	}

	public void run()
	{
		clip.start();
		clip.loop(Clip.LOOP_CONTINUOUSLY);
	}

	public void stop()
	{
		clip.stop();
	}
}
