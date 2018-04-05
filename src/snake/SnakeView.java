package snake;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Observer;

public class SnakeView implements Observer{
	SnakeControl control = null;
	SnakeModel model = null;
	
	JFrame mainFrame;
	Canvas paintCanvas;
	JLabel labelScore;
	
	public static final int canvasWidth = 500;
	public static final int canvasHeight = 500;
	
	public static final int nodeWidth = 20;
	public static final int nodeHeight = 20;
	
	public SnakeView(SnakeModel model,SnakeControl control)
	{
		this.model = model;
		this.control = control;
		mainFrame = new JFrame("GreedSnake");
		
		Container cp = mainFrame.getContentPane();
		
		//创建顶部分数显示
		labelScore = new JLabel("Score:");
		cp.add(labelScore, BorderLayout.NORTH);
		
		//创建中间的游戏显示区域
		paintCanvas = new Canvas();
		paintCanvas.setSize(canvasWidth + 1,canvasHeight + 1);
		paintCanvas.addKeyListener(control);
		cp.add(paintCanvas,BorderLayout.CENTER);
		
		//创建底部帮助栏
		JPanel panelButtom = new JPanel();
		panelButtom.setLayout(new BorderLayout());
		JLabel labelHelp;
		labelHelp = new JLabel("PageUp,PageDown for speed;",JLabel.CENTER);
		panelButtom.add(labelHelp, BorderLayout.SOUTH);
		cp.add(panelButtom,BorderLayout.SOUTH);
		
		mainFrame.addKeyListener(control);
		mainFrame.pack();
		mainFrame.setLocationRelativeTo(null);
		mainFrame.setResizable(false);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setVisible(true);
	}
	
	void repaint()
	{
		Graphics g = paintCanvas.getGraphics();
		
		//背景
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, canvasWidth, canvasHeight);
		
		//蛇
		g.setColor(Color.BLACK);
		LinkedList na = model.nodeArray;
		Iterator it = na.iterator();
		while(it.hasNext())
		{
			Node n = (Node)it.next();
			drawNode(g,n);
		}
		
		//食物
		g.setColor(Color.RED);
		Node n = model.food;
		drawNode(g,n);
		
		updateScore();
	}
	
	private void drawNode(Graphics g,Node n)
	{
		g.fillRect(n.x * nodeWidth,n.y * nodeHeight,nodeWidth - 1,nodeHeight - 1);
	}
	
	public void updateScore()
	{
		String s = "Score:" + model.score;
		labelScore.setText(s);
	}
	
	public void update(Observable o,Object arg)
	{
		repaint();
	}
}
