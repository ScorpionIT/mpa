package mpa.core.logic.building;

public class Mill extends Thread
{

	// TODO TUTTO
	private int food;
	private int capacity;
	private int time;
	private int quantityToAdd;

	public Mill(int capacity, int time, int quantityToAdd) throws Exception
	{
		this.food = 0;
		this.capacity = capacity;
		this.time = time;
		this.quantityToAdd = quantityToAdd;
	}

	public boolean addFood(int quantity)
	{
		if ((this.food + quantity) <= this.capacity)
		{
			this.food = this.food + quantity;
			return true;
		}

		else
			return false;
	}

	@Override
	public void run()
	{
		try
		{
			while (true)
			{
				try
				{
					sleep(this.time);
				} catch (final InterruptedException e)
				{

				}
				this.food += this.quantityToAdd;
			}
		} catch (final Exception e)
		{}
		super.run();
	}

	public void setCapacity(int capacity)
	{
		this.capacity = capacity;
	}

	public void setTime(int time)
	{
		this.time = time;
	}

	public void setQuantityToAdd(int quantityToAdd)
	{
		this.quantityToAdd = quantityToAdd;
	}

}
