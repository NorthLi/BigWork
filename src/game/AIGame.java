package game;
import Chess.*;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import AI.AIPlayer;
import AI.Position;

public class AIGame implements Runnable {
	private UI frame;
	public int time = 0;
	public volatile boolean exit = false;
	private int count;
	private JLabel l1, l2, l3, l4;
	private int man;
	
	public AIGame(UI frame, JLabel l1, JLabel l2, JLabel l3, JLabel l4, int man) {
		// TODO Auto-generated constructor stub
		this.frame = frame;
		Chessboard.subTime = 0;
		this.l1 = l1;
		this.l2 = l2;
		this.l3 = l3;
		this.l4 = l4;
		count = 0;
		this.man = man;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		new MyDialog(frame, true).setVisible(true);
		man = Chessboard.isFirst;
		int hard = Chessboard.hard;
		
		frame.setTitle("黑白棋-人机模式");
		frame.paint(frame.getGraphics());
		MyListener police = new MyListener(frame, man);
		Graphics g = frame.getGraphics();
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
				{
					new MyDialog(frame, 1).setVisible(true);
				}
				else
				{
					new MyDialog(frame, 2).setVisible(true);
				}
				
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
				int cc = 0;
				for(int i=0;i<8;++i)
				{
					for(int j=0;j<8;++j)
					{
						if(Chessboard.canSetChess(i, j, (Chessboard.getWhoTurn() == 1 ? 2 : 1)))
							cc ++;
					}
				}
				if(cc == 0)
				{
					int a = Chessboard.chessCount(1);
					int b = Chessboard.chessCount(2);
					Chessboard.subTime = 0;
					if(a > b)
						new MyDialog(frame, 1).setVisible(true);
					else
						new MyDialog(frame, 2).setVisible(true);
				}
				Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
				frame.repaint();
				continue;
			}
			
			if(Chessboard.getWhoTurn() == man)
			{
				int t = 0;
				while(Chessboard.getX() == -1)
				{
					if(Chessboard.getWhoTurn() != man)
					{
						t = 1;
						break;
					}
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				if(t== 1)
				{
					t = 0;
					l2.setText("当前黑子个数：  " + Chessboard.chessCount(1));
					l4.setText("当前白子个数：  " + Chessboard.chessCount(2));
					continue;
				}
				int x = Chessboard.getX();
				int y = Chessboard.getY();
				Chessboard.setX(-1);
				Chessboard.setY(-1);
				Chessboard.setOneChess(x, y, Chessboard.getWhoTurn());
				Chessboard.change(x, y, Chessboard.getWhoTurn());
				Chessboard.lastX = x;
				Chessboard.lastY = y;
				l2.setText("当前黑子个数：  " + Chessboard.chessCount(1));
				l4.setText("当前白子个数：  " + Chessboard.chessCount(2));
				frame.repaint();
				for(int i=0;i<8;++i)
				{
					for(int j=0;j<8;++j)
					{
						Chessboard.beforeHasChess[60 - Chessboard.canSetCount()][i][j] = Chessboard.getOneChess(i, j);
					}
				}
				Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
				Chessboard.subTime = 0;
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else
			{
				AIPlayer aiPlayer = new AIPlayer((man == 1 ? 2 : 1), man, hard);
				Position position = aiPlayer.step();
				Chessboard.setOneChess(position.getX(), position.getY(), Chessboard.getWhoTurn());
				Chessboard.change(position.getX(), position.getY(), Chessboard.getWhoTurn());
				Chessboard.lastX = position.getX();
				Chessboard.lastY = position.getY();
				l2.setText("当前黑子个数：  " + Chessboard.chessCount(1));
				l4.setText("当前白子个数：  " + Chessboard.chessCount(2));
				frame.repaint();
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
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}
	
	
	
	
	public void timer1()
	{
		Chessboard.t1 = new Timer();
		Chessboard.t1.schedule(new TimerTask() {	
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Chessboard.subTime ++;
				if(Chessboard.getWhoTurn() == 1)
				{
					l3.setText("剩余时间：  " + Chessboard.getTime());
					l1.setText("剩余时间：  " + (Chessboard.getTime() - Chessboard.subTime));
				}
				else
				{
					l1.setText("剩余时间：  " + Chessboard.getTime());
					l3.setText("剩余时间：  " + (Chessboard.getTime() - Chessboard.subTime));
				}
				//frame.repaint();
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


