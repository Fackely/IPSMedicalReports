package com.servinte.axioma.mundo.fabrica.odontologia.presupuesto;

import com.servinte.axioma.mundo.impl.odontologia.presupuesto.AutorizacionPresuDctoOdonMundo;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.ExcluPresuEncabezadoMundo;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.IncluDctoOdontologicoMundo;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.IncluPresuEncabezadoMundo;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.IncluServicioConvenioMundo;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.PresupuestoContratadoMundo;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.PresupuestoExclusionesInclusionesMundo;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.PresupuestoOdontologicoMundo;
import com.servinte.axioma.mundo.impl.odontologia.presupuesto.ViewPresupuestoTotalesConvMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IExcluPresuEncabezadoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluDctoOdontologicoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IIncluServicioConvenioMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoContratadoMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IPresupuestoOdontologicoMundo;
import com.servinte.axioma.mundo.interfaz.presupuesto.IViewPresupuestoTotalesConvMundo;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonServicio;


/**
 * 
 * Fabrica para construir objetos mundo para la l&oacute;gica
 * de Presupuesto.
 *
 * @author Yennifer Guerrero
 * @since  07/09/2010
 *
 */
public abstract class PresupuestoFabricaMundo {

	/**
	 * Atributo que instancia la clase IViewPresupuestoTotalesConvMundo
	 */
	private static IViewPresupuestoTotalesConvMundo viewPresu;
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de crear una instancia de la
	 * clase IViewPresupuestoTotalesConvMundo
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public static final IViewPresupuestoTotalesConvMundo crearViewPresupuestoTotalesConvMundo(){
		
		if(viewPresu==null)
		{
			return  new ViewPresupuestoTotalesConvMundo();
		}
		return viewPresu; 
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una instancia de la clase
	 * IPresupuestoContratadoMundo
	 * 
	 * @return IPresupuestoContratadoMundo
	 * @author Yennifer Guerrero
	 *
	 */
	public static IPresupuestoContratadoMundo crearPresupuestoContratadoMundo(){
		return new PresupuestoContratadoMundo();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una instancia de la clase
	 * IPresupuestoOdontologicoMundo
	 * 
	 * @return IPresupuestoOdontologicoMundo
	 * @author Yennifer Guerrero
	 *
	 */
	public static IPresupuestoOdontologicoMundo crearPresupuestoOdontologicoMundo(){
		return new PresupuestoOdontologicoMundo();
	}

	/**
	 * 
	 * Crea una instancia de la clase {@link IPresupuestoExclusionesInclusionesMundo}
	 * 
	 * @return {@link IPresupuestoExclusionesInclusionesMundo}
	 */
	public static IPresupuestoExclusionesInclusionesMundo crearPresupuestoExclusionesInclusionesMundo()
	{
		return new PresupuestoExclusionesInclusionesMundo();
	}
	
	
	/**
	 * 
	 * Crea una instancia de la clase {@link IExcluPresuEncabezadoMundo}
	 * 
	 * @return {@link IExcluPresuEncabezadoMundo}
	 */
	public static IExcluPresuEncabezadoMundo crearExcluPresuEncabezadoMundo()
	{
		return new ExcluPresuEncabezadoMundo();
	}
	
	
	/**
	 * 
	 * Crea una instancia de la clase {@link IIncluPresuEncabezadoMundo}
	 * 
	 * @return {@link IIncluPresuEncabezadoMundo}
	 */
	public static IIncluPresuEncabezadoMundo crearIncluPresuEncabezadoMundo()
	{
		return new IncluPresuEncabezadoMundo();
	}

	/**
	 * Crea una instancia de la clase {@link IIncluDctoOdontologicoMundo}
	 * @return {@link IIncluDctoOdontologicoMundo}
	 */
	public static IIncluDctoOdontologicoMundo crearIncluDctoOdontologico()
	{
		return new IncluDctoOdontologicoMundo();
	}
	
	
	/**
	 * Crea una instancia para la entidad AutorizacionPresuDctoOdon
	 * 
	 * @return {@link IAutorizacionPresuDctoOdonServicio}
	 * @author Jorge Armando Agudelo Quintero
	 */
	public static IAutorizacionPresuDctoOdonMundo crearAutorizacionPresuDctoOdonMundo()
	{
		return new AutorizacionPresuDctoOdonMundo();
	}
	
	/**
	 * 
	 * Crea una instancia de la clase {@link IIncluServicioConvenioMundo}
	 * 
	 * @return {@link IIncluServicioConvenioMundo}
	 */
	public static IIncluServicioConvenioMundo crearIncluServicioConvenioMundo()
	{
		return new IncluServicioConvenioMundo();
	}
	
}
