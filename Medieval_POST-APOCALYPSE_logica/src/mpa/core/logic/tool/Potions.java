package mpa.core.logic.tool;

public enum Potions implements Weapon
{
	HP, MP, GRANADE, FLASH_BANG;

	private static final int GRANADE_DAMAGE = 10;

	public static int granadeDamage()
	{
		return GRANADE_DAMAGE;
	}
}
