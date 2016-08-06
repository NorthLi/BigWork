package game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import Chess.UI;

public class ChatServer extends Thread {

	UI ui;
	ServerSocket serverSocket;
	Socket client;
    BufferedReader reader;
    PrintWriter writer;
	
    public ChatServer(UI ui) {
		// TODO Auto-generated constructor stub
    	this.ui = ui;
	}
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			serverSocket = new ServerSocket(4567);
			Socket client = serverSocket.accept();
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        writer = new PrintWriter(client.getOutputStream(), true);
	        String mString = new String();
	        while(true)
	        {
	        	mString = reader.readLine();
	        	if(mString != null && mString != "")
	        		ui.textArea.setText(ui.textArea.getText() + "°×£º " + mString + '\n');
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			new MyDialog(ui, "Á¬½Ó¶Ï¿ª£¡", true).setVisible(true);
		}
		
		
	}
	
	public void sendmsg(String msg)
	{
		writer.println(msg);
		ui.textArea.setText(ui.textArea.getText() + "ºÚ£º " + msg + '\n');
	}

}
