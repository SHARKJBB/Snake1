package snake;

public class GreedSnake 
{
	public static void main(String[] args) 
	{
		SnakeModel model = new SnakeModel(25,25);
		SnakeControl control = new SnakeControl(model);
		SnakeView view = new SnakeView(model,control);
		model.addObserver(view);
		(new Thread(model)).start();
	}

}
