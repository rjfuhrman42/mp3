package music;

import java.util.Iterator;
import table.TableInterface;
import table.TableFactory;


public class Artist 
{
	private TableInterface<CD, String> cds;	// table of CDs
	private CompareCDTitles comp_keys;
	private String name;
	
	public Artist(String artist)
	{	
		name = artist;
		comp_keys = new CompareCDTitles(true);
		cds = TableFactory.createTable(comp_keys);
	}
	
	public void addCD(CD cd)
	{
		cds.tableInsert(cd);
	}
	
	public int getNumAlbums()
	{
		return cds.tableSize();
	}
	
	public CD getCD(String s_key)
	{
		return cds.tableRetrieve(s_key);
	}
	
	public String getArtist()
	{
		return name;
	}
	
   public Iterator<CD> iterator()
   {
	   return cds.iterator();
   }
}