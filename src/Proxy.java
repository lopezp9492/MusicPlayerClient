/**
* The Proxy implements ProxyInterface class. The class is incomplete 
* 
* @author  Oscar Morales-Ponce
* @version 0.15
* @since   2019-01-24 
*/

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import Interfaces.ProxyInterface;
import SharedSources.RPCDescriptor;
import SharedSources.Sender;

    
public class Proxy implements ProxyInterface {
	private static final int SERVER_PORT = 2048;
	int requestId = 1;


	
    //Sender is part of the "CommunicationModule"
	Sender sender;
	String local_address;
    public Proxy(Sender sender, String local_address)
    {
        this.sender = sender;   
        this.local_address = local_address;
    }
    
    /*
    * Executes the  remote method "remoteMethod". The method blocks until
    * it receives the reply of the message. 
    */
    public JsonObject synchExecution(String remoteMethod, String[] param)
    {
        JsonObject jsonRequest = new JsonObject();
        JsonObject jsonParam = new JsonObject();
        
        jsonRequest.addProperty("remoteMethod", remoteMethod);
        jsonRequest.addProperty("objectName", "SongServices");
        
        // It is hardcoded. Instead it should be dynamic using  RemoteRef
        if (remoteMethod.equals("getSongChunk"))
        {
            
            jsonParam.addProperty("song", param[0]);
            jsonParam.addProperty("fragment", param[1]);       
        
        }
        if (remoteMethod.equals("getFileSize"))
        {
            jsonParam.addProperty("song", param[0]);        
        }
        jsonRequest.add("param", jsonParam);
        
        JsonParser parser = new JsonParser();
        
        //TODO: WHAT IS THIS LINE SUPOSED TO DO?
        //Was supposed to make the communicationsModule to Dispatch Requests
        //String strRet =  this.comms.dispatch(jsonRequest.toString());
        
        String strRet = ":(";
        return parser.parse(strRet).getAsJsonObject();
    }

    /*
    * Executes the  remote method remoteMethod and returns without waiting
    * for the reply. It does similar to synchExecution but does not 
    * return any value
    * 
    */
    public void asynchExecution(String remoteMethod, String[] param)
    {
        return;
    }
    
    public void login(String username, String password)
    {
		JsonObject execute = new JsonObject();
		execute.addProperty("serviceName", "LoginService");
		execute.addProperty("methodName", "login");
		JsonObject param = new JsonObject();
		param.addProperty("username", username);
		param.addProperty("password", password);
		execute.add("param", param);
		RPCDescriptor rpc = new RPCDescriptor(this.local_address, SERVER_PORT,
				RPCDescriptor.REQUEST, requestId++, "", execute);
		Sender.insert(rpc);
    }
    
    //To use no filter just send an empty string, eg. filter = ""
    
    public void getSongs(int count, String filter)
    {
		JsonObject execute = new JsonObject();
		execute.addProperty("serviceName", "CatalogService");
		execute.addProperty("methodName", "getSongs");
		JsonObject param = new JsonObject();
		param.addProperty("startIndex", count);
		param.addProperty("count", count);
		param.addProperty("filter", filter);
		execute.add("param", param);
		RPCDescriptor rpc = new RPCDescriptor(this.local_address, SERVER_PORT,
				RPCDescriptor.REQUEST, requestId++, "", execute);
		Sender.insert(rpc);
    }
    
	public void createPlayList(String userID, String playlistName)
	{
		JsonObject execute = new JsonObject();
		execute.addProperty("serviceName", "UserService");
		execute.addProperty("methodName", "createPlaylist");
		JsonObject param = new JsonObject();
		param.addProperty("userId", userID);
		param.addProperty("name", playlistName);
		execute.add("param", param);
		RPCDescriptor rpc = new RPCDescriptor(this.local_address, SERVER_PORT,
				RPCDescriptor.REQUEST, requestId++, "", execute);
		Sender.insert(rpc);
	}
	
	public void deletePlayList(String userID, String playlistName)
	{
		JsonObject execute = new JsonObject();
		execute.addProperty("serviceName", "UserService");
		execute.addProperty("methodName", "deletePlaylist");
		JsonObject param = new JsonObject();
		param.addProperty("userId", userID);
		param.addProperty("name", playlistName);
		execute.add("param", param);
		RPCDescriptor rpc = new RPCDescriptor(this.local_address, SERVER_PORT,
				RPCDescriptor.REQUEST, requestId++, "", execute);
		Sender.insert(rpc);
	}
	
	public void getPlaylists(String userID)
	{
		JsonObject execute = new JsonObject();
		execute.addProperty("serviceName", "UserService");
		execute.addProperty("methodName", "getPlaylists");
		JsonObject param = new JsonObject();
		param.addProperty("userId", userID);
		execute.add("param", param);
		RPCDescriptor rpc = new RPCDescriptor(this.local_address, SERVER_PORT,
				RPCDescriptor.REQUEST, requestId++, "", execute);
		Sender.insert(rpc);
	}
	
	public void addSongToPlaylist(String userID, String playlist,String id,  String title, String album, String artist)
	{		
		JsonObject execute = new JsonObject();
		execute.addProperty("serviceName", "UserService");
		execute.addProperty("methodName", "addSongToPlaylist");
		JsonObject param = new JsonObject();
		param.addProperty("userId", userID);
		param.addProperty("name", playlist);
		JsonObject song = new JsonObject();
		song.addProperty("id", id);
		song.addProperty("title", title);
		song.addProperty("album", album);
		song.addProperty("artist", artist);
		param.add("song", song);
		execute.add("param", param);
		RPCDescriptor rpc = new RPCDescriptor(this.local_address, SERVER_PORT,
				RPCDescriptor.REQUEST, requestId++, "", execute);
		Sender.insert(rpc);
	}
	
	public void deleteSongFromPlaylist(String userID, String playlist, String songID)
	{
		JsonObject execute = new JsonObject();
		execute.addProperty("serviceName", "UserService");
		execute.addProperty("methodName", "deletePlaylistSong");
		JsonObject param = new JsonObject();
		param.addProperty("userId", userID);
		param.addProperty("name", playlist);					
		param.addProperty("songId", songID);
		execute.add("param", param);
		RPCDescriptor rpc = new RPCDescriptor(this.local_address, SERVER_PORT,
				RPCDescriptor.REQUEST, requestId++, "", execute);
		Sender.insert(rpc);
	}
	
	public void getSongsFromPlaylist(String userID, String playlist)
	{
		JsonObject execute = new JsonObject();
		execute = new JsonObject();
		execute.addProperty("serviceName", "UserService");
		execute.addProperty("methodName", "getPlaylistSongs");
		JsonObject param = new JsonObject();
		param = new JsonObject();
		param.addProperty("userId", "1");
		param.addProperty("name", "Jazz");
		execute.add("param", param);
		RPCDescriptor rpc = new RPCDescriptor(this.local_address, SERVER_PORT,
				RPCDescriptor.REQUEST, requestId++, "", execute);
		Sender.insert(rpc);
	}
}


