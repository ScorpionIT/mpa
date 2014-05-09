package mpa.core.logic;


public abstract class AbstractObject implements InterfaceAbstractObject {

	private int X;
	private int Y;

	

	public AbstractObject(int x, int y) {
		X = x;
		Y = y;
	}

	@Override
	public int getX() {
		return this.X;
	}

	@Override
	public int getY() {
		return this.Y;
	}

	@Override
	public void setX(int X) {
		this.X = X;

	}

	@Override
	public void setY(int Y) {
		this.Y = Y;

	}

}
