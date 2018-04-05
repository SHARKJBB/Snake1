package snake;

import javax.swing.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Random;

class SnakeModel extends Observable implements Runnable
{
	//标识该位置是否有食物或蛇体
	boolean[][] matrix;
	//蛇
	LinkedList nodeArray = new LinkedList();
	Node food;
	int maxX;
	int maxY;
	//蛇的运行方向
	int direction = 2;
	//运行状态
	boolean running = false;
	//时间间隔，毫秒
	int timeInterval = 200;
	//每次的速度变化率
	double speedChangeRate = 0.75;
	//暂停标志
	boolean paused = false;
	//得分
	int score = 0;
	//吃到食物前移动的次数
	int countMove = 0;
	
	//上下为偶，左右为奇
	public static final int UP = 2;
	public static final int DOWN = 4;
	public static final int LEFT = 1;
	public static final int RIGHT = 3;
	
	public SnakeModel(int maxX,int maxY)
	{
		this.maxX = maxX;
		this.maxY = maxY;
		
		reset();
	}
	
	public void reset()
	{
		//蛇运行的方向
		direction = SnakeModel.UP;
		//时间间隔，毫秒
		timeInterval = 200;
		//暂停标志
		paused = false;
		//得分
		score = 0;
		//吃到食物前移动的次数
		countMove = 0;
		
		//全部清0
		matrix = new boolean[maxX][];
		for(int i = 0;i < maxX;i++)
		{
			matrix[i] = new boolean[maxY];
			Arrays.fill(matrix[i], false);
		}
		
		//初始化蛇，如果横向位置超过20个，长度为10，否则为横向位置的一半
		int initArrayLength = maxX > 20 ? 10 : maxX / 2;
		nodeArray.clear();
		for(int i = 0;i < initArrayLength;i++)
		{
			//maxX被初始化为20，maxY被初始化为30
			int x = maxX / 2 + i;
			int y = maxY / 2;
			nodeArray.addLast(new Node(x,y));
			matrix[x][y] = true;
		}
		
		//创建食物
		food = createFood();
		matrix[food.x][food.y] = true;
	}
	
	public void changeDirection(int newDirection)
	{
		//改变的方向不能与原来方向同向或反向
		if(direction % 2 != newDirection % 2)
		{
			direction = newDirection;
		}
	}
	
	public boolean moveOn()
	{
		Node n = (Node)nodeArray.getFirst();
		int x = n.x;
		int y = n.y;
		
		//根据方向增减坐标值
		switch(direction)
		{
		case UP:
			y--;
			break;
		case DOWN:
			y++;
			break;
		case LEFT:
			x--;
			break;
		case RIGHT:
			x++;
			break;
		}
		
		//如果新坐标落在有效范围内，则进行处理
		if((0 <= x && x < maxX) && (0 <= y && y < maxY))
		{
			//如果新坐标的点上有东西
			if(matrix[x][y])
			{
				if(x == food.x && y == food.y)
				{
					//从蛇头增长
					nodeArray.addFirst(food);
					//分数规则，与移动改变方向的次数和速度两个元素有关
					int scoreGet = (10000 - 200 * countMove) / timeInterval;
					score += scoreGet > 0 ? scoreGet : 10;
					countMove = 0;
					
					//创建新的食物
					food = createFood();
					matrix[food.x][food.y] = true;
					
					return true;
				}
				else return false;
			}
			//如果新坐标点没有东西，移动蛇
			else 
			{
				nodeArray.addFirst(new Node(x,y));
				matrix[x][y] = true;
				n = (Node)nodeArray.removeLast();
				matrix[n.x][n.y] = false;
				countMove++;
				return true;
			}
		}
		return false;
	}
	
	public void run()
	{
		running = true;
		while(running)
		{
			try
			{
				Thread.sleep(timeInterval);
			}
			catch(Exception e)
			{
				break;
			}
			if(!paused)
			{
				if(moveOn())
				{
					setChanged();
					notifyObservers();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "you failed","Game over",JOptionPane.INFORMATION_MESSAGE);
					break;
				}
			}
		}
		running = false;
	}
	
	private Node createFood()
	{
		int x = 0;
		int y = 0;
		//随机获取一个有效区域内的与蛇和食物不重叠的位置
		do
		{
			Random r = new Random();
			x = r.nextInt(maxX);
			y = r.nextInt(maxY);
		}
		while(matrix[x][y]);
		
		return new Node(x,y);
	}
	
	public void speedUp()
	{
		timeInterval *= speedChangeRate;
	}
	
	public void speedDown()
	{
		timeInterval /= speedChangeRate;
	}
	
	public void changePauseState()
	{
		paused = !paused;
	}
	
	public String toString()
	{
		String result = "";
		for(int i = 0;i < nodeArray.size();i++)
		{
			Node n = (Node)nodeArray.get(i);
			result += "[" + n.x + "," + n.y + "]";
		}
		return result;
	}
}

class Node
{
	int x;
	int y;
	
	Node(int x,int y)
	{
		this.x = x;
		this.y = y;
	}
}