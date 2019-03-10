// CLIENT RPC Manager

package SharedSources;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 
 * @author rTunes team
 *
 */
//THIS IS THE CLIENT RPC Manager
public class RPCManager implements Runnable {

	// Attributes
	// --------------------
	private static List<RPCDescriptor> rpcList = null; // descriptors received
	private boolean running; // True when running

	// Methods
	// --------------------

	// Initialize the manager
	public RPCManager() {
		this.running = false;

		if (rpcList == null)
			rpcList = Collections.synchronizedList(new ArrayList<RPCDescriptor>());
	}

	// Returns True if the thread is running
	public boolean isRunning() {
		return (running);
	}

	// stop the service
	public void stop() {
		running = false;
	}

	// remove a RPC from the queue
	public static void removeRPC(RPCDescriptor rpc) {
		synchronized (rpcList) {
			rpcList.remove(rpc);
		}
	}


	// Executes the process manager
	@Override
	public void run() {

		System.out.println("Process manager running.");

		running = true;
		while (running) {
			try { // Sleep for a millisecond
				Thread.sleep(1);

				// Get datagram from receiver, if null then go back to waiting
				DatagramPacket datagram = Receiver.pop();
				if (datagram == null) {
					continue;
				}

				// Try to get the RPC from the datagram
				RPCDescriptor rpc = new RPCDescriptor();
				if (!rpc.unmarshall(datagram)) {
					System.out.println("Process manager message: a invalid RPC was received");
					System.out.println("Datagram: " + new String(datagram.getData()));
					continue;
				}

				synchronized (rpcList) {
					// Discarding duplicate messages
					if (rpcList.contains(rpc)) {
						System.out.println("Process manager message: a duplicated RPC was received");
						System.out.println("Discarted execute: " + rpc.getExecute());
						continue;
					}

					// Save this rpc to avoid duplicate eventually
					rpcList.add(rpc);
				}

				// Log rpc
				Gson gson = new GsonBuilder().setPrettyPrinting().create();
				String json = gson.toJson(rpc.getExecute());
				System.out.println("Response:");
				System.out.println(json);

			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (NullPointerException e) {
				e.printStackTrace();
			}

		} // End of while loop

		// Service was stopped
		System.out.println("Process manager stopped.");

	} // End of run

}// End of Class
