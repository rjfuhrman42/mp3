package music;

import javax.sound.sampled.*;
import java.lang.Thread;
import java.io.*;

public class MP3 implements Runnable
{
   private Song mp3;
   private CurrentSong cur_song;
   private boolean stop;

   //DO THIS  
   //write a constructor that allows the Observer design pattern to be used when an mp3 is finished
   public MP3(Song song, CurrentSong cur_song)
   {
	   mp3 = song;
	   this.cur_song = cur_song;
	   stop = false;
   }
   //DO THIS
   //set up the MP3 class to be run in a Thread

	
   public void run()
   {
	   play();
   }   

   public void play()
   {
      try
      {
         File file = new File(mp3.getMP3());
         AudioInputStream in = AudioSystem.getAudioInputStream(file);
         AudioInputStream din = null;
         AudioFormat baseFormat = in.getFormat();
         AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(), baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
         din = AudioSystem.getAudioInputStream(decodedFormat, in);

         //play
         rawplay(decodedFormat, din);  //the main part of the thread
         in.close();
       }
       catch (Exception e)
       {
          System.out.println("Unable to play mp3.");
		  cur_song.mp3Done();
       }
   }

   private void rawplay(AudioFormat targetFormat, AudioInputStream din) throws IOException, LineUnavailableException
   {
      byte[] data = new byte[4096];
      SourceDataLine line = getLine(targetFormat);
      if (line != null)
      {
         //start
         line.start();
         int nBytesRead = 0, nBytesWritten = 0;

         while (nBytesRead != -1)  
         {
            nBytesRead = din.read(data, 0, data.length);
            if (nBytesRead != -1)
            {
               nBytesWritten = line.write(data, 0, nBytesRead);
            }
			if(stop == true)
			{
				line.drain();
				line.stop();
				line.close();
				din.close();
				//continue;
			}
         }

         //stop
         line.drain();
         line.stop();
         line.close();
         din.close();
      }
   }

   public void stop()
   {
	   stop = true;
   }
   
   private SourceDataLine getLine(AudioFormat audioFormat) throws LineUnavailableException
   {
      SourceDataLine res = null;
      DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
      res = (SourceDataLine) AudioSystem.getLine(info);
      res.open(audioFormat);
      return res;
   }
}
