import java.util.Scanner;

import com.google.gson.JsonObject;

import SharedSources.RPCDescriptor;
import SharedSources.RPCManager;
import SharedSources.Receiver;
import SharedSources.Sender;
import SharedSources.UDPConnection;

public class Client {

	private static final int SERVER_PORT = 2048;
	private static final int CLIENT_PORT = 2049;
	private static final int MAX_DATAGRAM_SIZE = 60000; // same as the client

	public static void main(String[] args) {
		// Open a connection
		UDPConnection connection = new UDPConnection();
		connection.open(CLIENT_PORT, MAX_DATAGRAM_SIZE);
		System.out.println("Server IP: " + connection.getLocalAddress() + " Server port: " + CLIENT_PORT);

		// Create the services
		Receiver receiver = new Receiver(connection);
		Sender sender = new Sender(connection);
		RPCManager rpcManager = new RPCManager();

		// Create the services
		Thread taskReceiver = new Thread(receiver);
		Thread taskSender = new Thread(sender);
		Thread taskRPCManager = new Thread(rpcManager);

		// Run the services as threads
		taskReceiver.start();
		taskSender.start();
		taskRPCManager.start();
		
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

}
