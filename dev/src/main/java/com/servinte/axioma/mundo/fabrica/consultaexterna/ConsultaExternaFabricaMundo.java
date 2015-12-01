/*
 * Mayo 18, 2010
 */
package com.servinte.axioma.mundo.fabrica.consultaexterna;

import com.servinte.axioma.mundo.impl.consultaexterna.ConsultoriosMundo;
import com.servinte.axioma.mundo.impl.consultaexterna.MultasCitasMundo;
import com.servinte.axioma.mundo.impl.consultaexterna.MultasFacturasVariasMundo;
import com.servinte.axioma.mundo.interfaz.consultaexterna.IConsultoriosMundo;
import com.servinte.axioma.mundo.interfaz.consultaexterna.IMultasCitasMundo;
import com.servinte.axioma.mundo.interfaz.consultaexterna.IMultasFacturasVariasMundo;

/**
 * Fábrica para contruir objetos para el módulo de Consulta Externa.
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public abstract class ConsultaExternaFabricaMundo {
	
	private ConsultaExternaFabricaMundo(){}
	
	/**
	 * Crea y retorna un objeto que es implementación de 
	 * {@link IMultasFacturasVariasMundo}.
	 * 
	 * @return objeto que es implementación de {@link IMultasFacturasVariasMundo}.
	 */
	public static IMultasFacturasVariasMundo crearMultasFacturasVariasMundo()
	{
		return new MultasFacturasVariasMundo();
	}
	
	
	/**
	 * Crea y retorna un objeto que es implementación de 
	 * {@link IMultasCitasMundo}.
	 * 
	 * @return objeto que es implementación de {@link IMultasCitasMundo}.
	 */
	public static IMultasCitasMundo crearMultasCitasMundo()
	{
		return new MultasCitasMundo();
	}

	/**
	 * Crea y retorna un objeto que es implementación de 
	 * {@link IConsultoriosMundo}.
	 * 
	 * @return objeto que es implementación de {@link IConsultoriosMundo}.
	 */
	public static IConsultoriosMundo crearConsultorioMundo()
	{
		return new ConsultoriosMundo();
	}
}
