package mpa.core.logic;

public class Pair<T, S>
{
	private T first;
	private S second;

	public Pair(T first, S second)
	{
		this.first = first;
		this.second = second;
	}

	public T getFirst()
	{
		return first;
	}

	public S getSecond()
	{
		return second;
	}

	@Override
	public String toString()
	{
		return new String("First= " + first + " second= " + second);
	}

	public boolean equal(Pair<T, S> obj)
	{
		return this.first.equals(obj.getFirst()) && this.second.equals(obj.getSecond());
	}
}
