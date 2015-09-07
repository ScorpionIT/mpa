//package mpa.gui.prova;
//
//public class GraphicThread extends Thread
//{
//	private GameGui gameGui;
//	private static final long INTERVAL = 500;
//
//	public GraphicThread( GameGui gameGui )
//	{
//		super();
//		this.gameGui = gameGui;
//	}
//
//	@Override
//	public void run()
//	{
//		while( true )
//		{
//			try
//			{
//				sleep( INTERVAL );
//				gameGui.repaint();
//			} catch( InterruptedException e )
//			{
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//
//		}
//	}
//
// }
