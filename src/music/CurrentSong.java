package music;

import java.awt.dnd.*;
import java.awt.datatransfer.*;
import javax.swing.JList;
import java.lang.Thread.*;

public class CurrentSong extends JList implements MP3Listener, DropTargetListener
{
	private static String empty = new String("Drag a song here");
	//private static String empty2 = new String("unable to play MP3 - Try another!");
	private Thread song_thread;
	private MP3 cur_song; 
	
	public CurrentSong()
	{
		super();
		setObject(empty);
  		song_thread = new NullThread();
		song_thread.start();
	}
	
	public CurrentSong(Object obj)
	{
		super();
		setObject(obj);
	}
	
	public void setObject(Object obj)
	{
		Object[] item = {obj};
		setListData(item);
	}
	
	public void songDone()
	{
		setObject(empty);
	}
	
	public void drop(DropTargetDropEvent dpe)
	{
		Transferable transfer = dpe.getTransferable();
		DataFlavor[] flavors = transfer.getTransferDataFlavors();
		Song drop_song;
		try
		{
			drop_song = (Song)transfer.getTransferData(flavors[0]);
		}
		catch(UnsupportedFlavorException ufe )
		{
			dpe.rejectDrop();
			return;
		}
		catch (java.io.IOException ioe)
		{
			dpe.rejectDrop();
			return;
		}
		dpe.acceptDrop(DnDConstants.ACTION_COPY);
		dpe.dropComplete(true);
		if(song_thread.getState().equals(Thread.State.TIMED_WAITING))
		{
			System.out.println(song_thread.getState());	// if a song is playing, stop that song 
			mp3Done();
		}
		setObject(drop_song);
		cur_song = new MP3(drop_song, this);
		song_thread = new Thread(cur_song);
		song_thread.start();
	}
	
	public void	dragEnter(DropTargetDragEvent dtde){};
	public void	dragExit(DropTargetEvent dte){};
	public void	dragOver(DropTargetDragEvent dtde){};
	public void	dropActionChanged(DropTargetDragEvent dtde){};
	
	public void run()
	{
		cur_song.play();
	}
	
	public void mp3Done()
	{
		songDone();	
		cur_song.stop();
	}
}