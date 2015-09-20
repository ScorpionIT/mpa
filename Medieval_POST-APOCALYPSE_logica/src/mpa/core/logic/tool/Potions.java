package mpa.core.logic.tool;

import mpa.core.logic.Level;

public enum Potions
{
	HP, MP, GRANADE, FLASH_BANG;

	private static final int HP_RESTORING = 25;
	private static final int MP_RESTORING = 25;
	private static final int GRANADE_DAMAGE = 10;

	public static int granadeDamage()
	{
		return GRANADE_DAMAGE;
	}

	public static int getMPRestoring( Level l )
	{
		return MP_RESTORING + l.ordinal() * 2;
	}

	public static int getHPRestoring( Level l )
	{
		return HP_RESTORING + l.ordinal() * 2;
	}
}
