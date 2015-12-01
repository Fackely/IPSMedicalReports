/**
 * 
 */
package com.servinte.axioma.mundo.fabrica.odontologia.administracion;

import com.servinte.axioma.mundo.impl.administracion.CentroAtencionMundo;
import com.servinte.axioma.mundo.impl.administracion.CentroCostoMundo;
import com.servinte.axioma.mundo.impl.administracion.ParametrizacionSemaforizacionMundo;
import com.servinte.axioma.mundo.impl.odontologia.administracion.EmisionBonosMundo;
import com.servinte.axioma.mundo.impl.odontologia.administracion.EmisionTarjetaClienteMundo;
import com.servinte.axioma.mundo.impl.odontologia.tipoTarjetaCliente.TipoTarjetaClienteMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroAtencionMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroCostoMundo;
import com.servinte.axioma.mundo.interfaz.administracion.IParametrizacionSemaforizacionMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.administracion.IEmisionBonosMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.administracion.IEmisionTarjetaClienteMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.tipoTarjeta.ITipoTarjetaClienteMundo;

/**
 * F�brica para crear el mundo de Emisi�n de tarjeta cliente 
 * @author Juan David Ram�rez
 * @since 06 Septiembre 2010
 * @version 1.0
 */
public abstract class AdministracionFabricaMundo
{
	/**
	 * M�todo para construir la instancia de EmisionTarjetaClienteMundo
	 * @return {@link IEmisionTarjetaClienteMundo} Instancia concreta
	 */
	public static final IEmisionTarjetaClienteMundo crearEmisionTarjetaClienteMundo()
	{
		return new EmisionTarjetaClienteMundo();
	}

	/**
	 * M�todo para construir la instancia de Tipo Tarjeta Mundo
	 * @return {@link ITipoTarjetaClienteMundo} Instancia concreta
	 */
	public static final ITipoTarjetaClienteMundo crearTipoTarjetaClienteMundo(){
		return new TipoTarjetaClienteMundo();
	}
	
	/**
	 * M�todo para construir la instancia de CentroCostoMundo
	 * @return {@link ICentroCostoMundo} Instancia concreta
	 */
	public static final ICentroCostoMundo crearCentroCostoMundo(){
		return new CentroCostoMundo();
	}

	/**
	 * M�todo para construir la instancia de EmisionBonosMundo
	 * @return {@link IEmisionBonosMundo} Instancia concreta
	 */
	public static IEmisionBonosMundo crearEmisionBonosMundo()
	{
		return new EmisionBonosMundo();
	}
	
	

	
	/**
	 * M�todo para construir la instancia de CentroCostoMundo
	 * @return {@link ICentroCostoMundo} Instancia concreta
	 */
	public static final ICentroAtencionMundo crearCentroAtencionMundo(){
		return new CentroAtencionMundo();
	}
	
}
