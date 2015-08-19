package mpa.gui.gameGui.listener;

import com.jme3.input.RawInputListener;
import com.jme3.input.event.JoyAxisEvent;
import com.jme3.input.event.JoyButtonEvent;
import com.jme3.input.event.KeyInputEvent;
import com.jme3.input.event.MouseButtonEvent;
import com.jme3.input.event.MouseMotionEvent;
import com.jme3.input.event.TouchEvent;

public class MyButtonListener implements RawInputListener
{

	@Override
	public void beginInput()
	{
	}

	@Override
	public void endInput()
	{
	}

	@Override
	public void onJoyAxisEvent( JoyAxisEvent evt )
	{
	}

	@Override
	public void onJoyButtonEvent( JoyButtonEvent evt )
	{
	}

	@Override
	public void onMouseMotionEvent( MouseMotionEvent evt )
	{
	}

	private static final int MOUSE_BUTTON_COUNT = 3;
	private static final long DOUBLE_CLICK_INTERVAL = 250;
	private int clickCount;
	private long eventTime;
	private MouseButtonEvent recentMouseButtonStatus = new MouseButtonEvent( -1, false, -1, -1 );
	private final MouseButtonEvent[] mouseButtonStatus = new MouseButtonEvent[MOUSE_BUTTON_COUNT];

	@Override
	public void onMouseButtonEvent( MouseButtonEvent evt )
	{
		long currentTime = System.currentTimeMillis();

		if( evt.isPressed() && recentMouseButtonStatus.getButtonIndex() != evt.getButtonIndex()
				|| currentTime - eventTime > DOUBLE_CLICK_INTERVAL )
		{

			clickCount = 0;

		}

		if( evt.isReleased() )
		{

			clickCount++;

		}

		eventTime = currentTime;

		recentMouseButtonStatus = evt;

		mouseButtonStatus[evt.getButtonIndex()] = evt;

		dispatchEvent( evt );
		System.out.println();
		System.out.println( "num click " + clickCount );
		System.out.println();
	}

	private void dispatchEvent( MouseButtonEvent evt )
	{
	}

	@Override
	public void onKeyEvent( KeyInputEvent evt )
	{
	}

	@Override
	public void onTouchEvent( TouchEvent evt )
	{
	}

}
