package com.servinte.axioma.servicio.fabrica.odontologia.presupuesto;

import com.servinte.axioma.servicio.impl.odontologia.presupuesto.AutorizacionPresuDctoOdonServicio;
import com.servinte.axioma.servicio.impl.odontologia.presupuesto.PresupuestoExclusionesInclusionesServicio;
import com.servinte.axioma.servicio.impl.odontologia.presupuesto.PresupuestoOdontologicoServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoExclusionesInclusionesServicio;
import com.servinte.axioma.servicio.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoServicio;

/**
 * @author Yennifer Guerrero
 * @since  16/09/2010
 *
 */
public abstract class PresupuestoFabricaServicio {
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	public PresupuestoFabricaServicio() {
	}
	
	/**
	 * 
	 * Este Método se encarga de crear una instancia para
	 * la entidad IPresupuestoServicio
	 * @return IPresupuestoServicio
	 * 
	 * @author Yennifer Guerrero
	 *
	 */
	public static IPresupuestoOdontologicoServicio crearPresupuestoOdontologicoServicio()
	{
		return new PresupuestoOdontologicoServicio();
	}

	/**
	 * Crea una instancia para la entidad IPresupuestoExclusionesInclusionesServicio
	 * @return {@link IPresupuestoExclusionesInclusionesServicio}
	 * @author Juan David Ramírez
	 */
	public static IPresupuestoExclusionesInclusionesServicio crearPresupuestoExclusionesInclusionesServicio()
	{
		return new PresupuestoExclusionesInclusionesServicio();
	}

	
	/**
	 * Crea una instancia para la entidad AutorizacionPresuDctoOdon
	 * 
	 * @return {@link IAutorizacionPresuDctoOdonServicio}
	 * @author Jorge Armando Agudelo Quintero
	 */
	public static IAutorizacionPresuDctoOdonServicio crearAutorizacionPresuDctoOdonServicio()
	{
		return new AutorizacionPresuDctoOdonServicio();
	}
	
}
