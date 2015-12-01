package util;

/**
* @version 1.0, Abril 10, 2003
*/
public class InfoDatosBD extends InfoDatos
{
	/**
	* Tiene 3 posible valores validos:
	* 't' := true,
	* 'f' :=false,
	* ' ' :=se desconoce
	*/
	private char	estaEnBD;
	private boolean	boolEstaEnBD;

	/** Constructor de una pareja de datos */
	public InfoDatosBD(String id, String value)
	{
		super(id, value);
		estaEnBD = ' ';
	}

	/** Constructor de un trio de datos */
	public InfoDatosBD(String id, String value, String descripcion)
	{
		super(id,value,descripcion);
		estaEnBD = ' ';
	}

	public InfoDatosBD(int codigo, String nombre, String descripcion)
	{
		super(codigo,nombre,descripcion);
		estaEnBD = ' ';
		boolEstaEnBD = false;
	}

	public InfoDatosBD(String id, String value, String descripcion, char estaEnBD)
	{
		super(id,value,descripcion);
		setEstaEnBD(estaEnBD);
	}

	public InfoDatosBD(int codigo, String nombre, String descripcion, boolean estaEnBD)
	{
		super(codigo,nombre,descripcion);
		setEstaEnBD(estaEnBD);
	}

	public char getEstaEnBD()
	{
		return estaEnBD;
	}

	public boolean getEstaEnBDBoolean()
	{
		return boolEstaEnBD;
	}

	public void setEstaEnBD(char ac_estaEnBD)
	{
		boolEstaEnBD = ( (estaEnBD = ac_estaEnBD) == 't');
	}

	public void setEstaEnBD(boolean ab_estaEnBD)
	{
		boolEstaEnBD = ab_estaEnBD;

		if(boolEstaEnBD)
			estaEnBD = 't';
		else
			estaEnBD = 'f';
	}
}