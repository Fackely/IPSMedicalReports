package com.servinte.axioma.dao.fabrica.odontologia.presupuesto;

import com.servinte.axioma.dao.impl.odontologia.presupuesto.AutorizacionPresuDctoOdonHibernateDAO;
import com.servinte.axioma.dao.impl.odontologia.presupuesto.ExcluPresuEncabezadoHibernateDAO;
import com.servinte.axioma.dao.impl.odontologia.presupuesto.IncluDctoOdontologicoDAO;
import com.servinte.axioma.dao.impl.odontologia.presupuesto.IncluPresuEncabezadoHibernateDAO;
import com.servinte.axioma.dao.impl.odontologia.presupuesto.IncluServicioConvenioHibernateDAO;
import com.servinte.axioma.dao.impl.odontologia.presupuesto.PresupuestoContratadoHibernateDAO;
import com.servinte.axioma.dao.impl.odontologia.presupuesto.ViewPresupuestoTotalesConvDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IAutorizacionPresuDctoOdonDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IExcluPresuEncabezadoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluDctoOdontologicoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluPresuEncabezadoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IIncluServicioConvenioDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IPresupuestoContratadoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IPresupuestoOdontologicoDAO;
import com.servinte.axioma.dao.interfaz.odontologia.presupuesto.IViewPresupuestoTotalesConvDAO;

/**
 * 
 * Esta clase se encarga de construir objetos DAO para
 * la l&oacute;gica de Presupuesto.
 *
 * @author Yennifer Guerrero
 * @since
 *
 */
public abstract class PresupuestoFabricaDAO {
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Yennifer Guerrero
	 */
	private PresupuestoFabricaDAO(){
	}
	
	/**
	 * Instancia de la clase IViewPresupuestoTotalesConvDAO
	 */
	private static  IViewPresupuestoTotalesConvDAO viewDao;
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de optimizar la 
	 * instanciaci&oacute;n de la clase ViewPresupuestoTotalesConvDAO.
	 * @return
	 *
	 * @author Yennifer Guerrero
	 */
	public static final IViewPresupuestoTotalesConvDAO crearViewPresupuestoTotalDAO(){
		
		if(viewDao==null)
		{
			return new ViewPresupuestoTotalesConvDAO();
		}
		
		return viewDao;
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una instancia de la clase
	 * IPresupuestoContratadoDAO
	 * 
	 * @return IPresupuestoContratadoDAO
	 * @author Yennifer Guerrero
	 *
	 */
	public static IPresupuestoContratadoDAO crearPresupuestoContratado(){
		return new PresupuestoContratadoHibernateDAO();
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de crear una instancia de la clase
	 * IPresupuestoOdontologicoDAO
	 * 
	 * @return IPresupuestoOdontologicoDAO
	 * @author Yennifer Guerrero
	 *
	 */
	public static IPresupuestoOdontologicoDAO crearPresupuestoOdontologicoDAO(){
		return new com.servinte.axioma.dao.impl.odontologia.presupuesto.PresupuestoOdontologicoHibernateDAO();
	}
	
	

	/**
	 * 
	 * Crea una instancia de la clase {@link IExcluPresuEncabezadoDAO}
	 * 
	 * @return {@link IExcluPresuEncabezadoDAO}
	 */
	public static IExcluPresuEncabezadoDAO crearExcluPresuEncabezadoDAO()
	{
		return new ExcluPresuEncabezadoHibernateDAO();
	}
	
	
	/**
	 * 
	 * Crea una instancia de la clase {@link IIncluPresuEncabezadoDAO}
	 * 
	 * @return {@link IIncluPresuEncabezadoDAO}
	 */
	public static IIncluPresuEncabezadoDAO crearIncluPresuEncabezadoDAO()
	{
		return new IncluPresuEncabezadoHibernateDAO();
	}

	/**
	 * 
	 * Crea una instancia de la clase {@link IIncluPresuEncabezadoDAO}
	 * 
	 * @return {@link IIncluPresuEncabezadoDAO}
	 */
	public static IIncluDctoOdontologicoDAO crearIncluDctoOdontologicoDAO()
	{
		return new IncluDctoOdontologicoDAO();
	}
	
	
	/**
	 * Crea una instancia para la entidad AutorizacionPresuDctoOdon
	 * 
	 * @return {@link IAutorizacionPresuDctoOdonDAO}
	 * @author Jorge Armando Agudelo Quintero
	 */
	public static IAutorizacionPresuDctoOdonDAO crearAutorizacionPresuDctoOdonDAO()
	{
		return new AutorizacionPresuDctoOdonHibernateDAO();
	}
	
	/**
	 * 
	 * Crea una instancia de la clase {@link IIncluServicioConvenioDAO}
	 * 
	 * @return {@link IIncluServicioConvenioDAO}
	 */
	public static IIncluServicioConvenioDAO crearIncluServicioConvenioDAO()
	{
		return new IncluServicioConvenioHibernateDAO();
	}
}
