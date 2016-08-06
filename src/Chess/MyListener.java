package Chess;
import java.awt.*;
import game.*;
import game.MyDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.management.openmbean.OpenDataException;
import javax.swing.*;

import org.omg.CORBA.CurrentHelper;
public class MyListener implements MouseListener {
	private UI frame;
	private int turn;
	
	public MyListener(UI f, int turn) {
		// TODO Auto-generated constructor stub
		frame = f;
		this.turn = turn;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		frame.repaint();
		int x = e.getX();
		int y = e.getY();
		x = (x - 20)/100;
		y = (y - 80)/100;
		Chessboard.setX(-1);
		Chessboard.setY(-1);
		if(x < 0 || x > 7 || y < 0 || y > 7)
		{
			return;
		}
		if(Chessboard.canSetChess(x, y, Chessboard.getWhoTurn()))
		{
			if(Chessboard.getMode() == 1)
			{
				Chessboard.setX(x);
				Chessboard.setY(y);
			}
			else
			{
				if(Chessboard.getWhoTurn() == turn)
				{
					Chessboard.setX(x);
					Chessboard.setY(y);
				}
			}
				
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
}

class MenuListener implements ActionListener {
	
	private JMenuItem newGame;
	private JMenuItem openFile;
	private JMenuItem saveGame;
	private JMenuItem undoMove;
	private JMenuItem settings;
	private JMenuItem newGame2;
	private JMenuItem joinGame;
	private JMenuItem aiGame;
	private UI frame;
	private Server serverThread;
	private Client clientThread;
	private AIGame aiThread;
	private AloneGame aloneThread;
	private Thread start, join;
	private static int first = 0;
	private JLabel label1, label2, label3, label4;
	private JButton b1, b2;
	private JTextField t1, t2;
	private JTextArea t3;
	ChatClient c;
	ChatServer s;

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object object = e.getSource();
		JMenuItem item = (JMenuItem)object;
		if(item.equals(newGame))
		{
			Chessboard.setMode(1);
			Chessboard.clear();
			b1.setEnabled(false);
			b2.setEnabled(false);
			frame.repaint();
			if(first != 0)
			{
				start.stop();
				Chessboard.t1.cancel();
				aloneThread = new AloneGame(frame, label1, label2, label3, label4);
				start = new Thread(aloneThread);
				start.start();
				new MyDialog(frame, "游戏已重新开始", true).setVisible(true);
			}
			if(first == 0)
			{
				aloneThread = new AloneGame(frame, label1, label2, label3, label4);
				start = new Thread(aloneThread);
				start.start();
			}
			first ++;
			joinGame.setEnabled(false);
			newGame2.setEnabled(false);
			aiGame.setEnabled(false);
			openFile.setEnabled(true);
			saveGame.setEnabled(true);
			undoMove.setEnabled(true);
		}
		else if(item.equals(newGame2))
		{
			Chessboard.setMode(2);
			Chessboard.clear();
			b1.setEnabled(true);
			frame.repaint();
			s.start();
			serverThread = new Server(frame, label1, label2, label3, label4, b1, t1, t3);
			//ButtonListener.server = null;
			//ButtonListener.server = serverThread;
			start = new Thread(serverThread);
			start.start();
			newGame.setEnabled(false);
			openFile.setEnabled(false);
			saveGame.setEnabled(false);
			aiGame.setEnabled(false);
			undoMove.setEnabled(true);
		}
		else if(item.equals(joinGame))
		{
			Chessboard.setMode(2);
			Chessboard.clear();
			b2.setEnabled(true);
			frame.repaint();
			c.start();
			clientThread = new Client(frame, label1, label2, label3, label4, b2, t2, t3);
			//ButtonListener.client = clientThread;
			start = new Thread(clientThread);
			start.start();
			newGame.setEnabled(false);
			openFile.setEnabled(false);
			saveGame.setEnabled(false);
			aiGame.setEnabled(false);
			undoMove.setEnabled(true);
		}
		else if(item.equals(saveGame))
		{
			Chessboard.t1.cancel();
			frame.repaint();
			String[][] saveName = new String[][]{{"棋局名称", "XXXX"}};
			MyDialog saveDialog = new MyDialog(frame, "请输入想要保存的棋局名称", true, saveName);
			saveDialog.setVisible(true);
			String[] name = saveDialog.getResult();
			if(name == null)
				return ;
			File file = new File(name[0]);
			try {
				OutputStream outputStream = new FileOutputStream("Map/" + file);
				byte[][] chess = new byte[8][8];
				for(int i=0;i<8;++i)
				{
					for(int j=0;j<8;++j)
					{
						chess[i][j] = (byte) Chessboard.getOneChess(i, j);
					}
				}
				for(int i=0;i<8;++i)
					outputStream.write(chess[i]);
				byte[] info = new byte[2];
				info[0] = (byte)Chessboard.getWhoTurn();
				info[1] = (byte)aloneThread.time;
				outputStream.write(info);
				frame.repaint();
				outputStream.close();
				Chessboard.timer1();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if(item.equals(openFile))
		{
			Chessboard.setMode(1);
			frame.repaint();
			MyDialog openDialog = new MyDialog(frame);
			openDialog.setVisible(true);
			b1.setEnabled(false);
			b2.setEnabled(false);
			frame.repaint();
			if(first == 0)
			{
				aloneThread = new AloneGame(frame, label1, label2, label3, label4);
				start = new Thread(aloneThread);
				start.start();
			}
			first ++;
			undoMove.setEnabled(true);
			joinGame.setEnabled(false);
		}
		else if(item.equals(settings))
		{
			frame.repaint();
			String[][] saveName = new String[][]{{"每步时间", "30"}};
			MyDialog settingDialog = new MyDialog(frame, "请输入想要设置的每步可思考时间", true, saveName);
			settingDialog.setVisible(true);
			String[] time = settingDialog.getResult();
			if(time == null)
				return ;
			if(Integer.parseInt(time[0]) < 0)
			{
				new MyDialog(frame, "请输入正确的时间！", true).setVisible(true);
			}
			else
			{
				Chessboard.setTime(Integer.parseInt(time[0]));
				MyDialog warnDialog = new MyDialog(frame, "每步思考时间已设置为" + time[0] + "秒", true);
				warnDialog.setVisible(true);
			}
		}
		else if(item.equals(undoMove))
		{
			Chessboard.lastX = -1;
			if(Chessboard.getMode() == 1 && Chessboard.getMode() != 3)
			{
				if((Chessboard.getWhoTurn() == 1 ? Chessboard.getUndo1() : Chessboard.getUndo2()) > 1)
					new MyDialog(frame, "每人最多两次悔棋机会！", true).setVisible(true);
				else if(Chessboard.canSetCount() < 60)
				{
					Chessboard.setHasChess(Chessboard.beforeHasChess[59 - Chessboard.canSetCount()]);
					Chessboard.subTime = 0;
					if(Chessboard.getWhoTurn() == 1)
						Chessboard.setUndo1(Chessboard.getUndo1() + 1);
					else
						Chessboard.setUndo2(Chessboard.getUndo2() + 1);
					Chessboard.setWhoTurn(Chessboard.getWhoTurn() == 1 ? 2 : 1);
				}
				frame.repaint();
			}
			else
			{
				Chessboard.setAskUndo(1);
			}
		}
		else if(item.equals(aiGame))
		{
			Chessboard.setMode(3);
			Chessboard.clear();
			b1.setEnabled(false);
			b2.setEnabled(false);
			frame.repaint();
			if(first != 0)
			{
				start.stop();
				aiThread = new AIGame(frame, label1, label2, label3, label4, 1);
				start = new Thread(aiThread);
				start.start();
				new MyDialog(frame, "游戏已重新开始", true).setVisible(true);
			}
			if(first == 0)
			{
				aiThread = new AIGame(frame, label1, label2, label3, label4, 1);
				start = new Thread(aiThread);
				start.start();
			}
			first ++;
			newGame.setEnabled(false);
			newGame2.setEnabled(false);
			joinGame.setEnabled(false);
			//openFile.setEnabled(true);
			//saveGame.setEnabled(true);
			undoMove.setEnabled(false);
		}
		
	}
	
	public MenuListener(JMenuItem item1, JMenuItem item2, JMenuItem item3, JMenuItem item4, JMenuItem item5, JMenuItem item6, JMenuItem item7, JMenuItem item8, UI ff, JLabel label1, JLabel label2, JLabel label3, JLabel label4, JButton b1, JButton b2, JTextField t1, JTextField t2, JTextArea t3, ChatClient chatClient, ChatServer chatServer ) {
		// TODO Auto-generated constructor stub
		newGame = item1;
		openFile = item2;
		saveGame = item3;
		undoMove = item4;
		settings = item5;
		newGame2 = item6;
		joinGame = item7;
		aiGame = item8;
		frame = ff;
		this.label1 = label1;
		this.label2 = label2;
		this.label3 = label3;
		this.label4 = label4;
		this.b1 = b1;
		this.b2 = b2;
		this.t1 = t1;
		this.t2 = t2;
		this.t3 = t3;
		c = chatClient;
		s = chatServer;
		undoMove.setEnabled(false);
	}
	
}

//class ButtonListener implements ActionListener{
//
//	private JButton b1, b2;
//	public static Server server;
//	public static Client client;
//	
//	@Override
//	public void actionPerformed(ActionEvent e) {
//		// TODO Auto-generated method stub
//		if(((JButton)e.getSource()).equals(b1))
//		{
//			
//		}
//		if(((JButton)e.getSource()).equals(b2))
//		{
//			
//		}
//	}
//	
//	public ButtonListener(JButton b1, JButton b2) {
//		// TODO Auto-generated constructor stub
//		this.b1 = b1;
//		this.b2 = b2;
//	}
//	
//}

