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

public class AloneGame implements Runnable  {
	private UI frame;
	public int time = 0;
	public volatile boolean exit = false;
	private int count;
	private JLabel l1, l2, l3, l4;

	public AloneGame(UI frame, JLabel l1, JLabel l2, JLabel l3, JLabel l4) {
		// TODO Auto-generated constructor stub
		this.frame = frame;
		Chessboard.subTime = 0;
		this.l1 = l1;
		this.l2 = l2;
		this.l3 = l3;
		this.l4 = l4;
		count = 0;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		frame.setTitle("黑白棋-单机模式");
		frame.paint(frame.getGraphics());
		MyListener police = new MyListener(frame, 1);
		Graphics g = frame.getGraphics();
		timer1();
		while(!exit)
		{
			System.out.println(Chessboard.canSetCount());
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
			
			
			frame.addMouseListener(police);
			while(Chessboard.getX() == -1)
			{
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
			for(int i=0;i<8;++i)
			{
				for(int j=0;j<8;++j)
				{
					Chessboard.beforeHasChess[60 - Chessboard.canSetCount()][i][j] = Chessboard.getOneChess(i, j);
				}
			}
			frame.repaint();
			g.setColor(Chessboard.getWhoTurn() == 1 ? Color.black : Color.white);
			g.fillOval(x * 100 + 25, y * 100 + 85, 90, 90);
			//Chessboard.sub();
			Chessboard.subTime = 0;
			frame.removeMouseListener(police);
			Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
			
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
				frame.repaint();
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
