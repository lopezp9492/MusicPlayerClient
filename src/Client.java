/**
 * Main Class for Client. 
 * Will provide menu with test options from terminal.
 * 
 * @author Nanae Aubry
 * @author Pedro Lopez
 * @author Brian Kelly
 */

//TODO: MOVE PLAYER TO IS OWN THREAD
//TODO: 

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import SharedSources.RPCDescriptor;

import SharedSources.RPCManager;
import SharedSources.Receiver;
import SharedSources.Sender;
import SharedSources.UDPConnection;

import Stream.CECS327InputStream;
import Stream.Music;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class Client {

	private static final int SERVER_PORT = 2048;
	private static final int CLIENT_PORT = 2049;
	private static final int MAX_DATAGRAM_SIZE = 60000; // same as the client

	public static void main(String[] args) {
		// Open a connection
		UDPConnection connection = new UDPConnection();
		connection.open(CLIENT_PORT, MAX_DATAGRAM_SIZE);
		System.out.println("Client IP: " + connection.getLocalAddress() + " Client port: " + CLIENT_PORT);

		// Create the services
		Receiver receiver = new Receiver(connection);
		Sender sender = new Sender(connection);
		RPCManager rpcManager = new RPCManager();
		StreamPlayer = sPlayer  = new StreamPlayer();

		// Create the services
		Thread taskReceiver = new Thread(receiver);
		Thread taskSender = new Thread(sender);
		Thread taskRPCManager = new Thread(rpcManager);
		Thread playerThread  = new Thread(sPlayer);

		// Run the services as threads
		taskReceiver.start();
		taskSender.start();
		taskRPCManager.start();
		playerThread.start();

		
		// Create Proxy (middle ware between client and comms module (sender)
		Proxy proxy = new Proxy(sender, connection.getLocalAddress());

		// Show a Menu for interaction with the user
		//int requestId = 1;//Moved this var to Proxy
		
		
		char key;
		Scanner keyboard = new Scanner(System.in);

		do {
			System.out.println("\n\n");
			System.out.println("********** Menu Server *************");
			System.out.println("1. Test login                       ");
			System.out.println("2. Test get songs                   ");
			System.out.println("3. Test get songs with filter       ");
			System.out.println("4. Create Jazz playlist             ");
			System.out.println("5. Delete Jazz playlist             ");
			System.out.println("6. Get playlists                    ");
			System.out.println("7. Add song to Jazz                 ");
			System.out.println("8. Delete song from Jazz            ");
			System.out.println("9. Get songs from Jazz              ");
			System.out.println("------------------------------------");
			System.out.println("a .Play Local Song File             ");
			System.out.println("b .Play Remote Song File            ");
			System.out.println("------------------------------------");
			System.out.println("x. Shut down the client and quit.   ");
			System.out.println("************************************");
			System.out.println("Select an option: ");

			// read the first character from the keyboard's buffer
			String buffer = keyboard.nextLine();
			key = 0;
			if (buffer.length() > 0) {
				key = buffer.toLowerCase().charAt(0);
				switch (key) {
				
				//Login
				case '1': {
					String username = "hello@gmail.com";
					String password = "123456";
					proxy.login(username, password);
					break;
				}

				// Get Songs // No filter
				case '2': {
					int count = 10;
					String filter = "";
					proxy.getSongs(count, filter);
					break;
				}

				// Get Songs // With filter
				case '3': {
					int count = 10;
					String filter = "The box tops";
					proxy.getSongs(count, filter);
					break;
				}
				// Create Playlist
				case '4': {

					String userID = "3";
					String playlistName = "MyJamm";
					proxy.createPlayList(userID, playlistName);
					break;
				}
				
				// Delete Playlist
				case '5': {
					String userID = "2";
					String playlistName = "Jazz";
					proxy.deletePlayList(userID, playlistName);
					
					
					break;
				}
				
				// Get playlists
				case '6': {
					String userID = "3";
					proxy.getPlaylists(userID);
					break;
				}
				
				// Add song to a playlist
				case '7': {
					String userID = "3";
					String playlist = "Jazz";
					
					// Song Properties
					String id = "idFoo";
					String title = "Message In Bottle";
					String album = "Remastered";
					String artist = "The Police";
					
					proxy.addSongToPlaylist(userID, playlist, id, title, album, artist);
					break;
				}
				
				// Delete Song from Playlist
				case '8': {
					String userID = "3";
					String playlist = "Jazz";
					
					// Song Properties
					String songID = "idFoo";
					proxy.deleteSongFromPlaylist(userID, playlist, songID);
					break;
				}
				
				// Request Songs from a playlist
				case '9': {
					//TEST - Add two songs then request the playlist
					
					String userID = "3";
					String playlist = "Alternative";

					// Song Properties
					String id = "idFoo";
					String title = "Message In Bottle";
					String album = "Remastered";
					String artist = "The Police";
					
					//ADD First Song to playlist
					proxy.addSongToPlaylist(userID, playlist, id, title, album, artist);
					
					// 2nd Song Properties
					 id = "iBar";
					 title = "Disco Devil";
					 album = "Best of Lee Perry";
					 artist = "Lee Perry";
					
					//Add Second Song to playlist
					proxy.addSongToPlaylist(userID, playlist, id, title, album, artist);

					//Get Songs from playlist					
					proxy.getSongsFromPlaylist(userID, playlist);
					break;
				}
				
				case 'a': {
				       Integer i;
				        Gson gson = new Gson();
				        
				        try 
				        {
				            Music[] music = gson.fromJson(new FileReader("music.json"), Music[].class);

				            for (i=0; i<music.length; i++)
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
					break;
				}
				case 'b': {
					
					break;
				}
				
				}
			}
		} while (key != 'x'); // until it is a valid key

		// close resources
		keyboard.close();
		rpcManager.stop();
		receiver.stop();
		sender.stop();
		connection.close();
	}
	
	
	
    /**
   * Play a given audio file.
   * @param audioFilePath Path of the audio file.
   */
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
