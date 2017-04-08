package breakout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class NetworkManager {
	public static boolean isServer = false;
	public static final int PORT = 8080;

	private ServerSocket serverSocket;
	private InetAddress addr;
	private Socket socket;
	public BufferedReader in;
	public PrintWriter out;
	public NetworkManager(){
		this(true);
	}

	public NetworkManager(boolean isServer, String... host){
		this.isServer = isServer;
		if(this.isServer){
			try{
				serverSocket = new ServerSocket(PORT);
				System.out.println("Started: " + serverSocket);
				socket = serverSocket.accept();
				System.out.println("Connection accepted: " + socket);

			}catch(IOException e){
				e.printStackTrace();
			}
		}
		//クライアントモードなのに接続先ホスト名が指定されていないとき
		else if(host.length == 0){
			System.out.println("ERR: Netowork mangaer has no distination host name.");
			return;
		}else{
			try{
				addr = InetAddress.getByName(host[0]);
				socket = new Socket(addr,  PORT);
				System.out.println("Connection established: " + socket);
			}catch(IOException e){
				e.printStackTrace();
			}
		}

		try{
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()) );
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public void disconect(){
		try{
			socket.close();
			if(isServer) serverSocket.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}




}
