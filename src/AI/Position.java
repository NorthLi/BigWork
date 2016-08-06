package AI;

import java.util.concurrent.PriorityBlockingQueue;

public class Position implements Comparable<Object>{

	private int x;
	private int y;
	private int [][] priority = { 
			{9,2,8,6,6,8,2,9},   
	        {2,1,3,4,4,3,1,2},   
	        {8,3,7,5,5,7,3,8},   
	        {6,4,5,10,10,5,4,6},   
	        {6,4,5,10,10,5,4,6},   
	        {8,3,7,5,5,7,3,8},   
	        {2,1,3,4,4,3,1,2},   
	        {9,2,8,6,6,8,2,9}
	};
	
	
	
	public Position(int x, int y) {
		// TODO Auto-generated constructor stub
		this.x = x;
		this.y = y;
		
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		Position position = (Position)obj;
		if(this.x == position.x && this.y == position.y)
			return true;
		return false;
	}
	
	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if(priority[x][y] < priority[((Position)o).getX()][((Position)o).getY()])
		{
			return 1;
		}
		else if(priority[x][y] == priority[((Position)o).getX()][((Position)o).getY()])
			return 0;
		else
			return -1;
	}

}
