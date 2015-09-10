package mpa.core.multiplayer.processingChain;

public class ProcessingChain
{
	protected ProcessingChain next;

	protected ProcessingChain( ProcessingChain next )
	{
		this.next = next;
	}

	public String[] processRequest( String request )
	{
		String[] reply = new String[1];
		reply[0] += "ERROR:" + reply;
		return reply;
	}

	protected boolean hasNext()
	{
		return next != null;
	}

}
