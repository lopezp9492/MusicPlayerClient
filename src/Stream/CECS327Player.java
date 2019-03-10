package Stream;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import com.google.gson.Gson;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class CECS327Player implements Runnable{
	
    public enum State {
		WAITING, PLAYING, PAUSED, STOPED
    }
    
	private boolean running;
	
    private String songID; //Song id
    private String songName = "empty";
    
    private Gson gson = new Gson();
    private Music[] music = null;
    
    private State state = State.WAITING;
    
	CECS327Player()
	{    
		//songName = "The Imperial March from";
		songName = "Boomerang";
		//songName = "You Got Me";

		
		readCatalog(); 	//Try Reading the music.json file
		findSongID();	//Try finding the song ID in the catalog
	}
	
	private void readCatalog()
	{
		//Try Reading the music.json file
        try 
        {
            music = gson.fromJson(new FileReader("music.json"), Music[].class);
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Cannot read Json file.");
            ex.printStackTrace();
        }
	}
	
	private void findSongID()
	{
		//For Find Song in Catalog 
        for (int i=0; i < music.length; i++)
        {
        	//Get Songs ID
            if (music[i].song.title.startsWith(songName))
            {
                System.out.println("Found the song: " + music[i].song.title);
            	songID = music[i].song.id;
            	
                //System.out.println("Playing " + music[i].song.title);
                //mp3play(music[i].song.id);
                //System.out.println("End of the song");
            }
        }
	}
	
	@Override
	public void run() {
		
		running = true;
		
		System.out.println("Player running...");
		while (state == State.WAITING) {
			//DO NOTHING
		}
		
		switch(state){
			case PLAYING:
			{
				
				break;

			}
				
		}	
		
		while(state ==State.PLAYING)
		{
			mp3play(songID);
			
			//When the song finishes go back to waiting state
			state = State.WAITING;
		}
		
		
		
		

		System.out.println("Player stopped.");
		
	}
	
	// Returns True if the thread is running
	public boolean isRunning() {
		return running;
	}
	
	// stop the service
	public void stopService() {
		running = false;
	}
	
	public void play()
	{
		state = State.PLAYING;
	}
	
	// stop the service
	public void stop() {
		state = State.STOPED;
	}
	// stop the service
	public void pause() {
		state = State.PAUSED;
	}
	
	static void mp3play(String file) {
		  String folder = "raw\\";
		  String full_path = folder + file;
		  
		  try {
	          // It uses CECS327InputStream as InputStream to play the song 
	           InputStream is = new CECS327InputStream(full_path);
	           Player mp3player = new Player(is);
	           mp3player.play();
		}
		catch (JavaLayerException ex) {
		    ex.printStackTrace();
		}
		catch (IOException ex) {
		      System.out.println("Error playing the audio file.");
		      ex.printStackTrace();
		  }
	}

}
