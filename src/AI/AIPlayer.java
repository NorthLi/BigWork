package AI;

import java.awt.AlphaComposite;
import java.util.*;
import Chess.*;

public class AIPlayer {
	private int chessTurn;
	private int otherTurn;
	private double max;
	private int maxX = 0;
	private int maxY = 0;
	private int depthMax;
	private static double cost[][] = {
	        {4.622507 ,-1.477853, 1.409644, -0.066975, -0.305214, 1.633019 ,-1.050899 ,4.365550},   
	        {-1.329145 ,-2.245663 ,-1.060633, -0.541089 ,-0.332716, -0.475830, -2.274535 ,-0.032595},   
	        {2.681550 ,-0.906628 ,0.229372, 0.059260 ,-0.150415, 0.321982 ,-1.145060, 2.986767},   
	        {-0.746066 ,-0.317389, 0.140040, -0.045266 ,0.236595, 0.158543 ,-0.720833 ,-0.131124},   
	        {-0.305566 ,-0.328398, 0.073872 ,-0.131472 ,-0.172101 ,0.016603 ,-0.511448 ,-0.264125},   
	        {2.777411 ,-0.769551 ,0.676483 ,0.282190 ,0.007184, 0.269876 ,-1.408169 ,2.396238},   
	        {-1.566175 ,-3.049899,-0.637408, -0.077690 ,-0.648382, -0.911066 ,-3.329772 ,-0.870962},   
	        {5.046583 ,-1.468806 ,1.545046 ,-0.031175 ,0.263998 ,2.063148 ,-0.148002 ,5.781035}   
	    };
	private int perfect = 12;
	private Position resentPosition = null;
	private static double max2 = 65535.0;
	
	public AIPlayer(int chessTurn, int otherTurn, int steps) {
		// TODO Auto-generated constructor stub
		depthMax = 2 * steps;
		this.chessTurn = chessTurn;
		this.otherTurn = otherTurn;
		resentPosition = new Position(0, 0);
	}
	
	public Position step() {
		boolean perfectEnd = (Chessboard.canSetCount() <= perfect) && (depthMax <= perfect);
		ArrayList<Position> record = new ArrayList<Position>();
		alphaBeta(-max2, max2, depthMax, perfectEnd);
		return new Position(maxX, maxY);
	}
	
	public int getDepthMax() {
		return depthMax;
	}
	
	public void setDepthMax(int depthMax) {
		this.depthMax = depthMax;
	}
	
	private double alphaBeta(double alpha, double beta, int depth, boolean perfectEnd) {
		if(depth == 0 && !perfectEnd)
		{
			return evaluating(false, chessTurn, otherTurn);
		}
		
		if(depth == depthMax)
			max = - max2;
		
		double value;
		int currentPlayer, otherPlayer;
		ArrayList<Position> positions = new ArrayList<Position>();
		if((depthMax - depth) % 2 == 0)
		{
			value = max2;
			currentPlayer = chessTurn;
			otherPlayer = otherTurn;
		}
		else
		{
			value = max2;
			currentPlayer = otherTurn;
			otherPlayer = chessTurn;
		}
		
		int m = 0;
		for(int i=0;i<8;++i)
		{
			for(int j=0;j<8;++j)
			{
				if(Chessboard.canSetChess(i, j, currentPlayer))
				{
					m ++;
					positions.add(new Position(i, j));
				}
			}
		}
		if(m == 0)
		{
			ArrayList<Position> temp = new ArrayList<Position>();
			int t = 0;
			for(int i=0;i<8;++i)
			{
				for(int j=0;j<8;++j)
				{
					if(Chessboard.canSetChess(i, j, otherPlayer))
					{
						t ++;
						temp.add(new Position(i, j));
					}
				}
			}
			if(t != 0)
			{
				double s = -alphaBeta(-beta, -alpha, depth - 1, perfectEnd);
				return s;
			}
			
			double eva = evaluating(true, currentPlayer, otherPlayer);
			return eva > alpha ? eva : alpha;
		}
		
		Collections.sort(positions);
		ArrayList<Position> record = new ArrayList<Position>();
		for(Position position : positions)
		{
			Chessboard.setOneChess(position.getX(), position.getY(), currentPlayer);
			Chessboard.change(position.getX(), position.getY(), currentPlayer);
			for(int i=0;i<8;++i)
			{
				for(int j=0;j<8;++j)
				{
					Chessboard.beforeHasChess[60 - Chessboard.canSetCount()][i][j] = Chessboard.getOneChess(i, j);
				}
			}
			
			double s = -alphaBeta(-beta, -alpha, depth - 1, perfectEnd);
			if(s > alpha)
			{
				if(s >= beta)
				{
					Chessboard.setHasChess(Chessboard.beforeHasChess[59 - Chessboard.canSetCount()]);
					return s;
				}
				else
				{
					alpha = s;
					value = alpha;
				}
			}
			if(depth == depthMax && value > max)
			{
				max = value;
				maxX = position.getX();
				maxY = position.getY();
			}
			Chessboard.setHasChess(Chessboard.beforeHasChess[59 - Chessboard.canSetCount()]);
		}
		return alpha;
		
	}

	private double evaluating(boolean isLast, int currentPlayer, int otherPlayer) {
		double myValue = 0, otherValue = 0;
		if(isLast)
		{
			for(int i=0;i<8;++i)
			{
				for(int j=0;j<8;++j)
				{
					if(Chessboard.getOneChess(i, j) == currentPlayer)
						myValue ++;
					else if(Chessboard.getOneChess(i, j) == otherPlayer)
						otherValue ++;
				}
			}
			return myValue - otherValue;
		}
		
		for(int i=0;i<8;++i)
		{
			for(int j=0;j<8;++j)
			{
				if(Chessboard.getOneChess(i, j) == currentPlayer)
					myValue += cost[i][j];
				else if(Chessboard.getOneChess(i, j) == otherPlayer)
					otherValue += cost[i][j];
			}
		}
		
		if(Chessboard.canSetCount() > 42)
			return myValue - otherValue;
		
		int myMobility = 0, otherMobility = 0;
		for (int i=0;i<8;i++) {   
            for (int j=0;j<8;j++) {   
                if (Chessboard.canSetChess(i, j, currentPlayer))  
                    myMobility ++;   
                else if (Chessboard.canSetChess(i, j, otherPlayer))  
                    otherMobility ++;   
            }   
        } 
		double temp = Math.sqrt(100 * myMobility) - Math.sqrt(100 * otherMobility), result;
		if(myValue - otherValue >= 0)
			result = Math.round((Math.sqrt(myValue - otherValue) * 2 + temp ) * 100 ) / 100;
		else
			result = -Math.round((Math.sqrt(otherValue - myValue) * 2 - temp ) * 100 ) / 100;
		return result;
	}
	
}
