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

	public int getNumberOfSubalterns(Level level)
	{
		switch (level)
		{
			case NEWBIE:
				return 4;
			case SER:
				return 8;
			case LORD:
				return 12;
			case WAR_LORD:
				return 16;
			case KING:
				return 20;

		}
		return 0;
	}

}
