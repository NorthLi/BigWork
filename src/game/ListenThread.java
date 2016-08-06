package game;

import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import javax.swing.JFrame;

import Chess.Chessboard;

public class ListenThread implements Runnable {
	private byte[] order;
	private InputStream is;
	private OutputStream os;
	private BufferedReader reader;
	JFrame frame;
	
	public ListenThread(InputStream inputStream, JFrame f, OutputStream outputStream, BufferedReader reader) {
		// TODO Auto-generated constructor stub
		is = inputStream;
		frame = f;
		os = outputStream;
		this.reader = reader; 
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		order = new byte[1];
		while(true)
		{
			try {
				is.read(order);
				switch (order[0]) {
				case 1:
					byte[][] buf = new byte[8][8];
					for(int i=0;i<8;++i)
					{
						is.read(buf[i]);
					}
					for(int i=0;i<8;++i)
					{
						for(int j=0;j<8;++j)
						{
							Chessboard.setOneChess(i, j, buf[i][j]);
						}
					}
					for(int i=0;i<8;++i)
					{
						for(int j=0;j<8;++j)
						{
							Chessboard.beforeHasChess[60 - Chessboard.canSetCount()][i][j] = Chessboard.getOneChess(i, j);
						}
					}
					Chessboard.setReadOver(1);
					break;
				case 3:
					new MyDialog(frame, "你的对手想要悔棋，是否同意？", os).setVisible(true);
					break;
				case 4:
					Chessboard.setHasChess(Chessboard.beforeHasChess[59 - Chessboard.canSetCount()]);
					Chessboard.subTime = 0;
					new MyDialog(frame, "对手同意你的悔棋", true).setVisible(true);
					Chessboard.setReadOver(1);
					Chessboard.setAgree(1);
					frame.repaint();
					break;
				case 5:
					new MyDialog(frame, "对手拒绝你的悔棋", true).setVisible(true);
					Chessboard.setReadOver(1);
					frame.repaint();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				Chessboard.clear();
				new MyDialog(frame, "已断开连接！", true).setVisible(true);
				frame.repaint();
				break;
			}
			
		}
	}

}
