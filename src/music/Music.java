package music;

import java.util.Iterator;
import table.TableInterface;
import table.TableFactory;

public class Music
{
   private TableInterface<Artist, String> artists; // table of artists
   private Artist artist;
   
   public Music(String file_name)
   {
	   readMusic(file_name);
   }

   private void readMusic(String file_name)
   {
      util.ReadTextFile rf = new util.ReadTextFile(file_name);
      String art = rf.readLine();
	  CompareArtists comp_keys = new CompareArtists(true);
	  artists = TableFactory.createTable(comp_keys);
      while (!rf.EOF())
      {
         String title = rf.readLine();
         //System.out.println(title);
         int year = Integer.parseInt(rf.readLine());
         int rating = Integer.parseInt(rf.readLine());
         int numTracks = Integer.parseInt(rf.readLine());
         CD cd = new CD(title, art, year, rating, numTracks);
	
         int tracks = 1;

         while (tracks <= numTracks)
         {
            String temp = rf.readLine();
            String[] line = temp.split(",");
            String len = line[0];
            String song_title = line[1];
			Song song = new Song(song_title, len, art, cd.getTitle(), tracks);
            cd.addSong(song);
            tracks++;
         }
		 //DO THIS
    	 //if the artist isn't already present in the table, create a new artist and insert it
		 if(artists.tableRetrieve(art) == null)
		 {
			 artist = new Artist(art);
			 artists.tableInsert(artist);
			 artist.addCD(cd); // add the CD to this artists table of CDs
		 }	 
		 else 
		 {
			 artist = artists.tableRetrieve(art);
			 artist.addCD(cd);
		 }
         art = rf.readLine();
      }
	  
	  rf.close();
   }

   public Iterator<Artist> iterator()
   {
	   return artists.iterator();
   }
   
   public Artist retrieveArtist(String s_key)
   {
	   return artists.tableRetrieve(s_key);
   }
   
   public int size()
   {
	   return artists.tableSize();
   }
   
   public static void main(String[] args)
   {
      Music mc = new Music("resources/cds.txt");
	  MP3Player mp3 = new MP3Player(mc); 
      //instantiate your GUI here
   }
}