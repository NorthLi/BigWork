package game;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import Chess.UI;

public class ChatClient extends Thread {
	
	UI ui;
	Socket client;
    BufferedReader reader;
    PrintWriter writer;
    
    public ChatClient(UI ui){
        this.ui = ui;
    }
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			client = new Socket("127.0.0.1", 4567);
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
	        writer = new PrintWriter(client.getOutputStream(), true);
	        String mString = new String();
	        while(true)
	        {
	        	mString = reader.readLine();
	        	if(mString != null && mString != "")
	        		ui.textArea.setText(ui.textArea.getText() + "ºÚ£º " + mString + '\n');
	        }
		} catch (Exception e) {
			// TODO Auto-generated catch block
			new MyDialog(ui, "Á¬½Ó¶Ï¿ª£¡", true).setVisible(true);
		}
        
		
	}
	
	public void sendmsg(String msg)
	{
		writer.println(msg);
		ui.textArea.setText(ui.textArea.getText() + "°×£º " + msg + '\n');
	}

}
