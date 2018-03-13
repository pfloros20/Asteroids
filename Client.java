import java.io.ObjectOutputStream;
import java.io.IOException;
import java.io.EOFException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.Scanner;
public class Client{
	private ObjectOutputStream output;
	private String message=null;
	private String serverIP;
	private Socket connection;

	public Client(String host,String message){
		serverIP=host;
		this.message=message;
	}

	//connect to server
	public boolean startRunning(){
		boolean ok=true;
		try{
			connectToServer();
			setupStreams();
			doAction();
		}catch(EOFException eof){
			showMessage("Client terminated the connection");
		}catch(IOException io){
			ok=false;
			io.printStackTrace();
		}finally{
			if(ok)
				closeCrap();
		}
		return ok;
	}

	private void connectToServer() throws IOException{
		showMessage("Attempting connection to server...");
		connection=new Socket();
		connection.connect(new InetSocketAddress(InetAddress.getByName(serverIP),6789),2000);
		showMessage("Connected to server: "+connection.getInetAddress().getHostName());
	}

	private void setupStreams() throws IOException{
		output=new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		showMessage("Streams are now set-up!");
	}

	private void doAction() throws IOException{
		sendMessage(message);
	}

	private void closeCrap(){
		try{
			showMessage("Closing connection");
			output.close();
			connection.close();
		}catch(IOException io){
			io.printStackTrace();
		}
	}

	private void sendMessage(String message){
		try{
			output.writeObject(message);
			output.flush();
		}catch(IOException io){
			io.printStackTrace();
		}
	}

	private void showMessage(String str){
		System.out.println(str);
	}
}
