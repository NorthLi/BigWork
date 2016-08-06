package Chess;
import java.awt.*;
import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JLabel;

import game.MyDialog;
public class Chessboard {
	private static int[][] hasChess;
	private static int[] notify = {-1, -1};
	private static int whoTurn = 1;
	private static int mode;
	private static int time;
	private static int canSetChess = 60;
	private static int port;
	private static boolean hasStop;
	public static int subTime;
	public static int[][][] beforeHasChess;
	private static int undo1, undo2;
	private static int readOver;
	private static int askUndo;
	private static int agree;
	public static int hard, isFirst;
	public static int lastX, lastY;
	public static Timer t1;
	public static JLabel l1, l2, l3, l4;
	public static UI frame;
	
	public Chessboard() {
		// TODO Auto-generated constructor stub
		hasChess = new int[8][8];
		time = 30;
		beforeHasChess = new int [61][8][8];
		Chessboard.t1 = new Timer();
		clear();
	}
	
	public static void timer1()
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
	
	public static void setUndo1(int undo1) {
		Chessboard.undo1 = undo1;
	}
	
	public static void setUndo2(int undo2) {
		Chessboard.undo2 = undo2;
	}
	
	public static int getUndo1() {
		return undo1;
	}
	
	public static int getUndo2() {
		return undo2;
	}
	
	public static void sub()
	{
		canSetChess --;
	}
	
	public static int canSetCount()
	{
		int m = 0;
		for(int i=0;i<8;++i)
		{
			for(int j=0;j<8;++j)
			{
				if(hasChess[i][j] == 0)
					m ++;
			}
		}
		return m;
	}
	
	public static void clear()
	{
		for(int i=0;i<8;++i)
		{
			for(int j=0;j<8;++j)
			{
				hasChess[i][j] = 0;
				beforeHasChess[0][i][j] = 0;
			}
		}
		hasChess[3][3] = 2;
		hasChess[4][4] = 2;
		hasChess[3][4] = 1;
		hasChess[4][3] = 1;
		beforeHasChess[0][3][3] = 2;
		beforeHasChess[0][4][4] = 2;
		beforeHasChess[0][3][4] = 1;
		beforeHasChess[0][4][3] = 1;
		canSetChess = 60;
		whoTurn = 1;
		hasStop = false;
		undo1 = 0;
		undo2 = 0;
		readOver = 0;
		subTime = 0;
		askUndo = 0;
		agree = 0;
		lastX = -1;
		lastY = -1;
	}
	
	public static void setReadOver(int readOver) {
		Chessboard.readOver = readOver;
	}
	
	public static int getReadOver() {
		return readOver;
	}
	
	public static void setAgree(int agree) {
		Chessboard.agree = agree;
	}
	
	public static int getAgree() {
		return agree;
	}
	
	public static void setAskUndo(int askUndo) {
		Chessboard.askUndo = askUndo;
	}
	
	public static int getAskUndo() {
		return askUndo;
	}
	
	public static void setHasStop(boolean hasStop) {
		Chessboard.hasStop = hasStop;
	}
	
	public static boolean getHasStop()
	{
		return hasStop;
	}
	
	public static void setPort(int port) {
		Chessboard.port = port;
	}
	
	public static int getPort() {
		return port;
	}
	
	public static int getCanSetChess() {
		return canSetChess;
	}
	
	public static void setCanSetChess(int canSetChess) {
		Chessboard.canSetChess = canSetChess;
	}
	
	public static int[][] getHasChess() {
		return hasChess;
	}
	
	public static void setHasChess(int[][] hasChess) {
		for(int i=0;i<8;++i)
		{
			for(int j=0;j<8;++j)
			{
				Chessboard.hasChess[i][j] = hasChess[i][j];
			}
		}
	}
	
	public static void setOneChess(int i, int j, int who)
	{
		hasChess[i][j] = who;
	}
	
	public static int getOneChess(int i, int j)
	{
		return hasChess[i][j];
	}
	
	public static void setTime(int time) {
		Chessboard.time = time;
	}
	
	public static int getTime() {
		return time;
	}
	
	public static void setMode(int mode) {
		Chessboard.mode = mode;
	}
	
	public static int getMode() {
		return mode;
	}
	
	public static void setX(int x)
	{
		notify[0] = x;
	}
	
	public static void setY(int y)
	{
		notify[1] = y;
	}
	
	public static int getX()
	{
		return notify[0];
	}
	
	public static int getY()
	{
		return notify[1];
	}
	
	public static void setWhoTurn(int whoTurn) {
		Chessboard.whoTurn = whoTurn;
	}
	
	public static int getWhoTurn() {
		return whoTurn;
	}
	
	//判断是否可以落子
	public static boolean canSetChess(int x, int y, int turn)
	{
		if(hasChess[x][y] != 0)
			return false;
		for(int i=x-1;i>=0;--i)
		{
			if(hasChess[i][y] == 0)
				break;
			if(hasChess[i][y] == turn && i != x - 1)
				return true;
			if(hasChess[i][y] == turn && i == x - 1)
				break;
		}
		for(int i=x-1;i>=0;--i)
		{
			if(y - x + i < 0)
				break;
			if(hasChess[i][y - x + i] == 0)
				break;
			if(hasChess[i][y - x + i] == turn && i != x - 1)
				return true;
			if(hasChess[i][y - x + i] == turn && i == x - 1)
				break;
		}
		for(int i=x-1;i>=0;--i)
		{
			if(y + x - i > 7)
				break;
			if(hasChess[i][y + x - i] == 0)
				break;
			if(hasChess[i][y + x - i] == turn && i != x - 1)
				return true;
			if(hasChess[i][y + x - i] == turn && i == x - 1)
				break;
		}
		for(int i=x+1;i<8;++i)
		{
			if(hasChess[i][y] == 0)
				break;
			if(hasChess[i][y] == turn && i != x + 1)
				return true;
			if(hasChess[i][y] == turn && i == x + 1)
				break;
		}
		for(int i=x+1;i<8;++i)
		{
			if(y + i - x > 7)
				break;
			if(hasChess[i][y + i - x] == 0)
				break;
			if(hasChess[i][y + i - x] == turn && i != x + 1)
				return true;
			if(hasChess[i][y + i - x] == turn && i == x + 1)
				break;
		}
		for(int i=x+1;i<8;++i)
		{
			if(y - i + x < 0)
				break;
			if(hasChess[i][y - i + x] == 0)
				break;
			if(hasChess[i][y - i + x] == turn && i != x + 1)
				return true;
			if(hasChess[i][y - i + x] == turn && i == x + 1)
				break;
		}
		for(int i=y-1;i>=0;--i)
		{
			if(hasChess[x][i] == 0)
				break;
			if(hasChess[x][i] == turn && i != y - 1)
				return true;
			if(hasChess[x][i] == turn && i == y - 1)
				break;
		}
		for(int i=y+1;i<8;++i)
		{
			if(hasChess[x][i] == 0)
				break;
			if(hasChess[x][i] == turn && i != y + 1)
				return true;
			if(hasChess[x][i] == turn && i == y + 1)
				break;
		}
		return false;
	}
	
	//返回现在有多少个白子/黑子
	public static int chessCount(int turn)
	{
		int count = 0;
		for(int i=0;i<8;++i)
		{
			for(int j=0;j<8;++j)
			{
				if(turn == hasChess[i][j])
					count ++;
			}
		}
		return count;
	}
	
	public static void change(int x, int y, int turn)
	{
		for(int i=x-1;i>=0;--i)
		{
			if(hasChess[i][y] == 0)
				break;
			if(hasChess[i][y] == turn && i != x - 1)
			{
				for(int j=i+1;j<x;++j)
					hasChess[j][y] = turn;
				break;
			}
			if(hasChess[i][y] == turn && i == x - 1)
				break;
		}
		for(int i=x-1;i>=0;--i)
		{
			if(y - x + i < 0)
				break;
			if(hasChess[i][y - x + i] == 0)
				break;
			if(hasChess[i][y - x + i] == turn && i != x - 1)
			{
				for(int j=i+1;j<x;++j)
					hasChess[j][y - x + j] = turn;
				break;
			}
			if(hasChess[i][y - x + i] == turn && i == x - 1)
				break;
		}
		for(int i=x-1;i>=0;--i)
		{
			if(y + x - i > 7)
				break;
			if(hasChess[i][y + x - i] == 0)
				break;
			if(hasChess[i][y + x - i] == turn && i != x - 1)
			{
				for(int j=i+1;j<x;++j)
					hasChess[j][y + x - j] = turn;
				break;
			}
			if(hasChess[i][y + x - i] == turn && i == x - 1)
				break;
		}
		for(int i=x+1;i<8;++i)
		{
			if(hasChess[i][y] == 0)
				break;
			if(hasChess[i][y] == turn && i != x + 1)
			{
				for(int j=i-1;j>x;--j)
					hasChess[j][y] = turn;
				break;
			}
			if(hasChess[i][y] == turn && i == x + 1)
				break;
		}
		for(int i=x+1;i<8;++i)
		{
			if(y + i - x > 7)
				break;
			if(hasChess[i][y + i - x] == 0)
				break;
			if(hasChess[i][y + i - x] == turn && i != x + 1)
			{
				for(int j=i-1;j>x;--j)
					hasChess[j][y + j - x] = turn;
				break;
			}
			if(hasChess[i][y + i - x] == turn && i == x + 1)
				break;
		}
		for(int i=x+1;i<8;++i)
		{
			if(y - i + x < 0)
				break;
			if(hasChess[i][y - i + x] == 0)
				break;
			if(hasChess[i][y - i + x] == turn && i != x + 1)
			{
				for(int j=i-1;j>x;--j)
					hasChess[j][y - j + x] = turn;
				break;
			}
			if(hasChess[i][y - i + x] == turn && i == x + 1)
				break;
		}
		for(int i=y-1;i>=0;--i)
		{
			if(hasChess[x][i] == 0)
				break;
			if(hasChess[x][i] == turn && i != y - 1)
			{
				for(int j=i+1;j<y;++j)
					hasChess[x][j] = turn;
				break;
			}
			if(hasChess[x][i] == turn && i == y - 1)
				break;
		}
		for(int i=y+1;i<8;++i)
		{
			if(hasChess[x][i] == 0)
				break;
			if(hasChess[x][i] == turn && i != y + 1)
			{
				for(int j=i-1;j>y;--j)
					hasChess[x][j] = turn;
				break;
			}
			if(hasChess[x][i] == turn && i == y + 1)
				break;
		}
	}

}
