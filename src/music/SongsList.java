package music;

import java.awt.dnd.*;
import javax.swing.JList;

class SongsList extends JList<Song> implements DragGestureListener
{
	public SongsList()
	{
		super();
	}
	
	public void setCD(CD cd)
	{
		int num_tracks = cd.getNumTracks();
		
		Song[] track_list = new Song[num_tracks];
		for(int i = 0; i < num_tracks; i++)
		{
			track_list[i] = cd.getSong(i);
		}
		setListData(track_list);
	}
	
	public void dragGestureRecognized(DragGestureEvent dge)
	{
		SongTransferable transferable = new SongTransferable((Song)getSelectedValue());
		DragSource dragSource = dge.getDragSource();
		dragSource.startDrag(dge,DragSource.DefaultCopyDrop, transferable, null);
	}
}