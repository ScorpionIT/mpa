package mpa.core.logic;

public enum Level
{
	NEWBIE, SER, LORD, WAR_LORD, KING;

	public boolean hasNext()
	{
		if (this.ordinal() < Level.values().length)
			return true;
		return false;
	}

	public boolean hasPrevious()
	{
		if (this.ordinal() > 0)
			return true;
		return false;
	}

	public Level getNext(Level current)
	{
		if (current.hasNext())
			return Level.values()[current.ordinal() + 1];
		return current;
	}

	public Level getPrevious(Level current)
	{
		if (current.hasPrevious())
			return Level.values()[current.ordinal() - 1];
		return current;
	}

}
