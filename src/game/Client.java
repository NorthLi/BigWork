package game;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.sound.midi.Instrument;
import javax.swing.*;
import Chess.*;

public class Client implements Runnable {
	private Socket client;
	private int port;
	private UI frame;
	private String ip;
	private InputStream is;
	private OutputStream os;
	private BufferedReader reader;
	private PrintWriter writer;
	private String name;
	private byte[] order;
	private ListenThread listenThread;
	private JLabel label1, label2, label3, label4;
	private JButton b;
	private JTextField field;
	public static JTextArea area;
	
	public Client(UI f, JLabel l1, JLabel l2, JLabel l3, JLabel l4, JButton b, JTextField field, JTextArea area) {
		// TODO Auto-generated constructor stub
		frame = f;
		Chessboard.setMode(3);
		init();
		label1 = l1;
		label2 = l2;
		label3 = l3;
		label4 = l4;
		this.area = area;
		this.b = b;
		this.field = field;
		order = new byte[1];
	}
	
	public boolean confirm() throws IOException 
	{
		byte[] buf = new byte[64];
		int n = is.read(buf);
		name = new String(buf, 0, n);
		return (name.length() > 0) ? true : false;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		timer1();
			try 
			{
				if(ip.equals("localhost"))
				{
					client = new Socket("localhost", port);
				}
				else
				{
					InetAddress address = InetAddress.getByName(ip);
					client = new Socket(address, port);
				}
				is = client.getInputStream();
				os = client.getOutputStream();
				if (!confirm()) {
					new MyDialog(frame, "连接服务器失败", true).setVisible(true);
					return;
				}
				frame.setTitle("黑白棋 " + "已连接:" + name + " 的游戏.本机端口号:" + client.getLocalPort());
				MyListener police = new MyListener(frame, 2);
				listenThread = new ListenThread(is, frame, os, reader);
				new Thread(listenThread).start();
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
					
					
					if(Chessboard.getWhoTurn() == 2)
					{
						int t = 0;
						
						while(Chessboard.getX() == -1)
						{
							if(Chessboard.getWhoTurn() == 1)
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
							label2.setText("当前黑子个数：  " + Chessboard.chessCount(1));
							label4.setText("当前白子个数：  " + Chessboard.chessCount(2));
							continue;
						}
						if(Chessboard.getAgree() == 1)
						{
							Chessboard.setAgree(0);
							label2.setText("当前黑子个数：  " + Chessboard.chessCount(1));
							label4.setText("当前白子个数：  " + Chessboard.chessCount(2));
							continue;
						}
						int x = Chessboard.getX();
						int y = Chessboard.getY();
						Chessboard.setX(-1);
						Chessboard.setY(-1);
						Chessboard.setOneChess(x, y, Chessboard.getWhoTurn());
						Chessboard.change(x, y, Chessboard.getWhoTurn());
						label2.setText("当前黑子个数：  " + Chessboard.chessCount(1));
						label4.setText("当前白子个数：  " + Chessboard.chessCount(2));
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
								Chessboard.setAskUndo(0);
								order[0] = 3;
								os.write(order);
								
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
							if(Chessboard.getWhoTurn() == 2)
							{
								Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
								label2.setText("当前黑子个数：  " + Chessboard.chessCount(1));
								label4.setText("当前白子个数：  " + Chessboard.chessCount(2));
								break;
							}
							if(Chessboard.getReadOver() == 1)
							{
								//System.out.println(Chessboard.getReadOver());
								Chessboard.setReadOver(0);
								label2.setText("当前黑子个数：  " + Chessboard.chessCount(1));
								label4.setText("当前白子个数：  " + Chessboard.chessCount(2));
								break;
							}
							Thread.sleep(20);
						}
						label2.setText("当前黑子个数：  " + Chessboard.chessCount(1));
						label4.setText("当前白子个数：  " + Chessboard.chessCount(2));
						Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
						frame.repaint();
						Chessboard.subTime = 0;
					}
				}
			} catch (IOException | InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
	
	public void Clear() throws Exception{
		is.close();
		os.close();
		client.close();
	}
	
	public void chat() throws Exception
	{
		String string = field.getText();
		order = new byte[1];
		order[0] = 7;
		os.write(order);
		writer.println(string);
	}
	
	public void init()
	{
		String[][] initString = new String[][]{{"IP地址： ", "127.0.0.1"}, {"端口： ", "7897"}};
		MyDialog initDialog = new MyDialog(frame, "输入对方IP地址与端口", true, initString);
		initDialog.setVisible(true);
		String[] IPAndPort = initDialog.getResult();
		if(IPAndPort == null)
			return ;
		this.ip = IPAndPort[0];
		this.port = Integer.valueOf(IPAndPort[1]);
		if(port <= 0 || port >= 65536)
		{
			String errorM = "端口号错误，请重新输入";
			JOptionPane.showMessageDialog(frame, errorM, "端口号错误", JOptionPane.ERROR_MESSAGE);
			return;
		}
		Chessboard.clear();
		Chessboard.setPort(port);
		frame.paint(frame.getGraphics());
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
					label3.setText("剩余时间：  " + Chessboard.getTime());
					label1.setText("剩余时间：  " + (Chessboard.getTime() - Chessboard.subTime));
				}
				else
				{
					label1.setText("剩余时间：  " + Chessboard.getTime());
					label3.setText("剩余时间：  " + (Chessboard.getTime() - Chessboard.subTime));
				}
				if(Chessboard.subTime >= Chessboard.getTime())
				{
					
					Chessboard.subTime = 0;
					new MyDialog(frame, (Chessboard.getWhoTurn() == 1 ? "黑方" : "白方") + "超时！", true).setVisible(true);
					Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
					
				}
			}
		}, 1000, 1000);
	}
	
	
}
