package music;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import gui.ImageLoader;
import gui.DrawRectangle;
import gui.DrawImage;
import gui.DrawFont;

public class CD
{
	//pick one of these items to be the sort "key"
   private String title;
   private String artist;
   private String img_file;
   private int year;
   private int count;
   private int rating;
   private int i = 0;
   private Song[] songs;
   
   private int x_loc, y_loc;
   private int image = 0;
   
   private java.awt.Image img0;
   private java.awt.Image img1;

   private final gui.ImageLoader il = gui.ImageLoader.getImageLoader();
   private static final int x_off = 100;  //the offset will change during a rotation
   private static final int y_off = 100;
   private static final java.awt.Font verdana_12 = new java.awt.Font("Verdana", java.awt.Font.BOLD, 14);

   public CD (String title, String artist, int year, int rating, int tracks)
   {
	  this.title = title;
	  this.artist = artist;
	  
      this.year = year;
	  img_file = "resources/art/" + artist + " - " + title + ".jpg";
      count = 0;
      songs = new Song[tracks];

      if (rating > 0 && rating <= 10)
      {
         this.rating = rating;
      }
      else
      {
         rating = 5;
      }
   }
   
   public int getNumTracks()
   {
	   return songs.length;
   }
   
   public String getArtist()
   {
      return artist;
   }
   
   public String getTitle()
   {
      return title;
   }

   public int getYear()
   {
      return year;
   }

   public int getRating()
   {
      return rating;
   }

   public Song getSong(int index)
   {
      if (index >= 0 && index < songs.length)
      {
         return songs[index];
      }
      else
      {
         return null;
      }
   }

   public void addSong(Song song)
   {
      if (song != null && count < songs.length)
      {
         songs[count] = song;
         count++;
      }
   }

   public String toString()
   {
      return title + "  " + year + "  " + rating + "  " + songs.length;
   }

   public void draw(Graphics g, int width, int height, boolean hilight)
   {
	   ImageLoader il = ImageLoader.getImageLoader();
	   img0 = il.getImage(img_file);
	   Color color = new Color(28, 23, 23);
	 //DrawImage dr = new DrawImage(img0, img_file, width, height);	
	   DrawFont txt = new DrawFont("Segoe UI", "bold", 20, Color.black, 256, 120);
	   DrawRectangle rect = new DrawRectangle(200, 200);
	   
	   rect.draw(g, color, 305, 254);	// shadow effect
	   txt.draw(g, "Rating: " + getRating());
	 //dr.draw(g);
	   g.drawImage(img0.getScaledInstance(200, 200, Image.SCALE_SMOOTH), 200, 150, null); // Scaling the image allows for a consistent shadow effect
	   if(hilight == true)
	   {
		  img1 = il.getHighLightImage(img0);
		  g.drawImage(img1.getScaledInstance(200, 200, Image.SCALE_SMOOTH), 200, 150, null);
	   }
   }
   
}