package com.servinte.axioma.mundo.fabrica.facturasvarias;

import com.servinte.axioma.mundo.impl.facturasvarias.FacturasVariasMundo;
import com.servinte.axioma.mundo.interfaz.facturasvarias.IFacturasVariasMundo;

/**
 * 
 * Esta clase se encarga de crear las instancias necesarias
 * para las entidades del m�dulo de facturas varias
 * 
 * @author Juan David Ram�rez
 * @since 11 Septiembre 2010
 */
public abstract class FacturasVariasMundoFabrica
{
	/**
	 * 
	 * Este M�todo se encarga de de crear una instancia para 
	 * la entidad IFacturasVariasMundo
	 * 
	 * @return IFacturasVariasMundo
	 *
	 */
	public static IFacturasVariasMundo crearFacturasVariasMundo()
	{
		return new FacturasVariasMundo();
	}

}
