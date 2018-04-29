package music;

import java.util.Iterator;
import java.util.ArrayList;

import java.awt.event.*;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Color;
import java.awt.Font;
import java.awt.dnd.*;

import javax.swing.*;

import gui.Drawable;
import gui.BasicGUI;
import gui.ImageLoader;
import gui.DrawImage;
import gui.CenterFrame;

public class MP3Player implements Drawable, ActionListener
{
	private String[] artists;
	private BasicGUI mp3_gui;
	private CD[] cds;				// list of CDs for the current artist
	private JComboBox n_box;		// we make these instance variables so we can frequently update their contents
	private JComboBox s_box;	    // as well as access their contents
	private SongsList song_list;
	private Music mus;
	private CD current_album;		// the current album to display
	private boolean highlight;
	
	public MP3Player(Music mc)
	{
		mus = mc;
		highlight = false;
		
		JPanel east_panel = new JPanel();				// create the panels
		JPanel north_panel = new JPanel();
		JPanel south_panel = new JPanel();
		
		int x = 0;
		artists = new String[mc.size()];
		Iterator<Artist> iter = mc.iterator();		
		while(iter.hasNext())
		{
			artists[x] = iter.next().getArtist();		// populate the artists list
			x++;
		}
		
		int y = 0;
		Artist first_artist = mc.retrieveArtist(artists[0]);
		cds = new CD[first_artist.getNumAlbums()];
		Iterator<CD> cd_iter = first_artist.iterator();
		
		while(cd_iter.hasNext())
		{
			cds[y] = cd_iter.next();			// populate the albums list
			y++;
		}
		
		GridLayout east_layout = new GridLayout(1,1);
		GridLayout north_layout = new GridLayout(1,2);
		GridLayout south_layout = new GridLayout(1,1);
		east_panel.setLayout(east_layout);				
		north_panel.setLayout(north_layout);
		south_panel.setLayout(south_layout);

		s_box = new JComboBox(artists);					// 2 combo boxes
		n_box = new JComboBox(cds);
		
		s_box.setBackground(Color.gray);
		n_box.setBackground(Color.gray);
		
		s_box.addActionListener(this);
		n_box.addActionListener(new ChangeSongs());
		n_box.setActionCommand("north_box");
		
		song_list = new SongsList();
		CurrentSong current_song = new CurrentSong();
		song_list.setBackground(Color.gray);
		current_song.setBackground(Color.lightGray);
		song_list.setSelectionForeground(Color.lightGray);
		song_list.setSelectionBackground(Color.darkGray);
		//song_list.setFixedCellWidth(230);
		song_list.setFont(new Font("Roboto Mono Medium", Font.PLAIN, 14));
		updateSongs(cds[0]);
		
		Dimension dim = new Dimension(250, 250);
		current_album = cds[0];
	
		JScrollPane e_pane = new JScrollPane(song_list);
		e_pane.setPreferredSize(dim);
		east_panel.add(e_pane);
		south_panel.add(current_song);
		north_panel.add(s_box);
		north_panel.add(n_box);
		
		DragSource drag_source = DragSource.getDefaultDragSource();
		DragGestureRecognizer drag_gesture = drag_source.createDefaultDragGestureRecognizer(song_list, DnDConstants.ACTION_COPY, song_list);
		DropTarget drop_target = new DropTarget(current_song, current_song);

		mp3_gui = new BasicGUI(900, 600, "Music Player", this, "images/headphones2-32.png");
		mp3_gui.setVisible(true);
		mp3_gui.add(east_panel, BorderLayout.EAST);
		mp3_gui.add(north_panel, BorderLayout.NORTH);
		mp3_gui.add(south_panel, BorderLayout.SOUTH);
		//mp3_gui.setResizable(true);
	}
	
	public void draw(Graphics g, int width, int height)
	{
		current_album.draw(g, width, height, highlight);
		mp3_gui.repaint();
	}
	
	public void mouseClicked(int x, int y)
	{
		if(x > 200 && x < 400 && y > 150 && y < 350)
		{
			highlight = true;
		}
		else
		{
			highlight = false;
		}
	}
	
	// when a new artist is selected we need to populate the other combo box with their albums	
	public void actionPerformed(ActionEvent e)
	{
		JComboBox cb = (JComboBox)e.getSource();		// get the source of the event
		cb.removeActionListener(this);				

		String name = (String)cb.getSelectedItem();		// get the selected item
		Artist temp = mus.retrieveArtist(name);	
		updateAlbums(temp);	

	}
	
	private void updateAlbums(Artist current)
	{
		int y = 0;
		n_box.removeAllItems(); 						// clear out the current contents
		Iterator<CD> cd_iter = current.iterator();
		while(cd_iter.hasNext())
		{
			//cds[y] = cd_iter.next().getTitle();
			n_box.addItem(cd_iter.next());				// repopulate with updated contents
			y++;
		}
		s_box.addActionListener(this);
		updateSongs((CD)n_box.getItemAt(0));			// update song list to first album
	}
	
	private void updateSongs(CD curr_album)
	{
		current_album = curr_album;
		song_list.setCD(curr_album);
	}
	
	public void keyPressed(char key)
	{
	}	
	
private class ChangeSongs implements ActionListener 
{
	public void actionPerformed(ActionEvent ae)
	{
		JComboBox cb = (JComboBox)ae.getSource();
		try{
		CD cd = (CD)cb.getSelectedItem();
		updateSongs(cd);
		System.out.println(cd.toString());
		System.out.println(cb.getActionCommand() + "!");
		}
		catch(NullPointerException npe)
		{}
	}
}
	
}		