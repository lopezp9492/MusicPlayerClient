package Stream;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.DatagramPacket;

import com.google.gson.Gson;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class StreamPlayer implements Runnable{

	private boolean running;
    private Integer i; //Segment Counter
    private Gson gson = new Gson();
    private Music[] music = null;
    
    
	StreamPlayer()
	{    
		//Try Reading the songs json file
        try 
        {
            music = gson.fromJson(new FileReader("music.json"), Music[].class);

            //For Each Segment of the Song 
            for (i=0; i < music.length; i++)
            {
                if (music[i].song.title.startsWith("The Imperial March from"))
                {
                    System.out.println("Playing " + music[i].song.title);
                    //Main player = new Main();
                    mp3play(music[i].song.id);
                    System.out.println("End of the song");
                }
            }
        }
        catch (FileNotFoundException ex)
        {
            System.out.println("Cannot read Json file.");
            ex.printStackTrace();
        }
	}
	
	@Override
	public void run() {
		
		
		running = true;
		System.out.println("Player running...");
		while (running) {
			
			
		}

		System.out.println("Player stopped.");
		
	}
	
	// Returns True if the thread is running
	public boolean isRunning() {
		return running;
	}
	
	// stop the service
	public void stop() {
		running = false;
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
