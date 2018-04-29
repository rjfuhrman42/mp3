package music;

import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;

public class SongTransferable implements Transferable
{
	static DataFlavor song_flavor = null;
	
	static
	{
		try
		{
			song_flavor = new DataFlavor(Class.forName("music.Song"),"Song");
		}
		catch(ClassNotFoundException e){}
	}
	
	private Song song;
	
	public SongTransferable(Song sg)
	{
		song = sg;
	}
	
	public Song getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
	{
		if(flavor.equals(song_flavor))
		{
			return song;
		}
		else
		{
		throw new UnsupportedFlavorException(flavor);
		}	
	}
	
	public DataFlavor[] getTransferDataFlavors()
	{
		DataFlavor[] flvrs = {song_flavor};
		return flvrs;
	}	
	
	public boolean isDataFlavorSupported(DataFlavor flavor)
	{
		if(flavor.equals(song_flavor))
		{
			return true;
		}
		return false;
	}
}
