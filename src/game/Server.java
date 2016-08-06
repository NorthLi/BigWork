package game;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.Instrument;
import javax.swing.*;
import Chess.*;
public class Server implements Runnable {
	private ServerSocket serverSocket;
	private Socket client;
	private UI frame;
	private int port;
	private String name;
	private InputStream is;
	private OutputStream os;
	private BufferedReader reader;
	private PrintWriter writer;
	private boolean isMyTurn = true;
	private ListenThread listenThread;
	private byte[] order;
	private JLabel label1, label2, label3, label4;
	private JButton b;
	private JTextField field;
	public static JTextArea area;
	
	public Server(UI f, JLabel l1, JLabel l2, JLabel l3, JLabel l4, JButton b, JTextField field, JTextArea area) {
		// TODO Auto-generated constructor stub
		frame = f;
		isMyTurn = true;
		label1 = l1;
		label2 = l2;
		label3 = l3;
		label4 = l4;
		this.area = area;
		this.b = b;
		this.field = field;
		order = new byte[1];
	}
	
	public void Clear() throws Exception{
		is.close();
		os.close();
		client.close();
		serverSocket.close();
	}
	
	private void confirm() throws IOException {
		os.write(name.getBytes());
	}
	
	public void sendTime() throws IOException{
		os.write(Chessboard.getTime());
	}
	
	
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		init();
		try {
			serverSocket = new ServerSocket(Chessboard.getPort());
			client = serverSocket.accept();
			is = client.getInputStream();
			os = client.getOutputStream();
			reader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            writer = new PrintWriter(client.getOutputStream(), true);
			confirm();
			MyListener police = new MyListener(frame, 1);
			Graphics g = frame.getGraphics();
			listenThread = new ListenThread(is, frame, os, reader);
			new Thread(listenThread).start();
			timer1();
			frame.addMouseListener(police);
			while(true)
			{
				if(Chessboard.chessCount(1) == 0)
				{
					new MyDialog(frame, 2).setVisible(true);
					Chessboard.subTime = 0;
				}
				if(Chessboard.chessCount(2) == 0)
				{
					new MyDialog(frame, 1).setVisible(true);
					Chessboard.subTime = 0;
				}
				if(Chessboard.canSetCount() == 0)
				{
					int a = Chessboard.chessCount(1);
					int b = Chessboard.chessCount(2);
					Chessboard.subTime = 0;
					if(a > b)
						new MyDialog(frame, 1).setVisible(true);
					else
						new MyDialog(frame, 2).setVisible(true);
					
				}
				int ccc = 0;
				for(int i=0;i<8;++i)
				{
					for(int j=0;j<8;++j)
					{
						if(Chessboard.canSetChess(i, j, Chessboard.getWhoTurn()))
							ccc ++;
					}
				}
				if(ccc == 0 && Chessboard.getCanSetChess() != 0)
				{
					Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
					frame.repaint();
					continue;
				}
				
				if(Chessboard.getWhoTurn() == 1)
				{
					int t = 0;
					while(Chessboard.getX() == -1)
					{
						if(Chessboard.getWhoTurn() == 2)
						{
							t = 1;
							break;
						}
						if(Chessboard.getAgree() == 1)
						{
							frame.repaint();
							break;
						}
						Thread.sleep(20);
					}
					if(t== 1)
					{
						t = 0;
						label2.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(1));
						label4.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(2));
						continue;
					}
					if(Chessboard.getAgree() == 1)
					{
						Chessboard.setAgree(0);
						label2.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(1));
						label4.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(2));
						continue;
					}
					int x = Chessboard.getX();
					int y = Chessboard.getY();
					Chessboard.setX(-1);
					Chessboard.setY(-1);
					Chessboard.setOneChess(x, y, Chessboard.getWhoTurn());
					Chessboard.change(x, y, Chessboard.getWhoTurn());
					label2.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(1));
					label4.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(2));
					frame.repaint();
					order[0] = 1;
					os.write(order);
					byte[][] chess = new byte[8][8];
					for(int i=0;i<8;++i)
					{
						for(int j=0;j<8;++j)
						{
							chess[i][j] = (byte) Chessboard.getOneChess(i, j);
						}
					}
					for(int i=0;i<8;++i)
					{
						os.write(chess[i]);
					}
					for(int i=0;i<8;++i)
					{
						for(int j=0;j<8;++j)
						{
							Chessboard.beforeHasChess[60 - Chessboard.canSetCount()][i][j] = Chessboard.getOneChess(i, j);
						}
					}
					Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
					frame.repaint();
					Chessboard.subTime = 0;
				}
				else
				{
					while(true)
					{
						if(Chessboard.getAskUndo() == 1)
						{
							order[0] = 3;
							os.write(order);
							Chessboard.setAskUndo(0);
							while(true)
							{
								if(Chessboard.getReadOver() == 1)
								{
									Chessboard.setReadOver(0);
									break;
								}
								Thread.sleep(20);
							}
							if(Chessboard.getAgree() == 1)
							{
								Chessboard.setAgree(0);
								break;
							}
						}
						if(Chessboard.getWhoTurn() == 1)
						{
							Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
							label2.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(1));
							label4.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(2));
							break;
						}
						if(Chessboard.getReadOver() == 1)
						{
							Chessboard.setReadOver(0);
							label2.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(1));
							label4.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(2));
							break;
						}
						Thread.sleep(20);
					}
					label2.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(1));
					label4.setText("��ǰ���Ӹ�����  " + Chessboard.chessCount(2));
					Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
					frame.repaint();
					Chessboard.subTime = 0;
				}
					
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void init()
	{
		String[][] initString = new String[][]{{"�û���", "XXX"}, {"�˿�", "7897"}};
		MyDialog initDialog = new MyDialog(frame, "�����û�����˿�", true, initString);
		initDialog.setVisible(true);
		String[] nameAndPort = initDialog.getResult();
		if(nameAndPort == null)
			return;
		this.name = nameAndPort[0];
		this.port = Integer.valueOf(nameAndPort[1]);
		if(port <= 0 || port >= 65536)
		{
			String errorM = "�˿ںŴ�������������";
			JOptionPane.showMessageDialog(frame, errorM, "�˿ںŴ���", JOptionPane.ERROR_MESSAGE);
			return;
		}
		frame.setTitle("�ڰ���  ��ң� " + name + "�˿ںţ�" + port);
		Chessboard.clear();
		Chessboard.setPort(port);
		frame.paint(frame.getGraphics());
		
	}
	
	public void chat() throws Exception
	{
		String string = field.getText();
		order = new byte[1];
		order[0] = 6;
		os.write(order);
		writer.println(string);
	}
	
	public void timer1()
	{
		Chessboard.t1 = new Timer();
		Chessboard.t1.schedule(new TimerTask() {	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Chessboard.subTime ++;
				//frame.repaint();
				if(Chessboard.getWhoTurn() == 1)
				{
					label3.setText("ʣ��ʱ�䣺  " + Chessboard.getTime());
					label1.setText("ʣ��ʱ�䣺  " + (Chessboard.getTime() - Chessboard.subTime));
				}
				else
				{
					label1.setText("ʣ��ʱ�䣺  " + Chessboard.getTime());
					label3.setText("ʣ��ʱ�䣺  " + (Chessboard.getTime() - Chessboard.subTime));
				}
				if(Chessboard.subTime >= Chessboard.getTime())
				{
					
					Chessboard.subTime = 0;
					new MyDialog(frame, (Chessboard.getWhoTurn() == 1 ? "�ڷ�" : "�׷�") + "��ʱ��", true).setVisible(true);
					Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
					
				}
			}
		}, 1000, 1000);
	}
	
}
