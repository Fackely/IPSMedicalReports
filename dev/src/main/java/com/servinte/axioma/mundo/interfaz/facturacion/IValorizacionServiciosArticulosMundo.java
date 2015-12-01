package com.servinte.axioma.mundo.interfaz.facturacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.capitacion.DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio;
import com.servinte.axioma.dto.capitacion.DtoDiaCierre;
import com.servinte.axioma.orm.Contratos;
import com.servinte.axioma.orm.Convenios;

/**
 * Define la lógica de negocio relacionada con la
 *  Consulta del total de servicios y articulos valorizados
 * 
 * @version 1.0, May 19, 2011
 * @author <a href="mailto:ricardo.ruiz@servinte.com.co">Ricardo Ruiz</a>
 * 
 */
public interface IValorizacionServiciosArticulosMundo {

	
	/**
	 * Método encargado de validar cada uno de los dias en los cuales existe un cierre de presupuesto
	 * @param Lis<DtoDiaCierre> diasCierre
	 * @return ParamPresupuestosCap
	 */
	public List<DtoDiaCierre> validarCierresDiarios(List<DtoDiaCierre> diasCierre);
	
	
	/**
	 * Método encargado de consultar el consolidado de servicios detallado por nivel de 
	 * atención para un convenio
	 * @param convenio
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
		
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorNivelPorConvenio(Convenios convenio, String proceso, List<Calendar> mesesReporte);
	
	/**
	 * Método encargado de consultar el consolidado de servicios detallado por nivel de 
	 * atención para un contrato
	 * @param contrato
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
		
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorNivelPorContrato(Contratos contrato, String proceso, List<Calendar> mesesReporte);
	
	/**
	 * Método encargado de consultar el consolidado de servicios detallado por grupo de 
	 * servicios para un convenio
	 * @param convenio
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
	
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorGrupoPorConvenio(Convenios convenio, String proceso, List<Calendar> mesesReporte);
	
	/**
	 * Método encargado de consultar el consolidado de servicios detallado por grupo de 
	 * servicios para un contrato
	 * @param contrato
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
		
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorGrupoPorContrato(Contratos contrato, String proceso, List<Calendar> mesesReporte);


	/**
	 * Método encargado de consultar el consolidado de servicios detallado por servicio
	 * para un convenio
	 * @param convenio
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
		
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorServicioPorConvenio(Convenios convenio, String proceso, List<Calendar> mesesReporte);
	
	/**
	 * Método encargado de consultar el consolidado de servicios detallado por servicio
	 * para un contrato
	 * @param contrato
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
	
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoServiciosPorServicioPorContrato(Contratos contrato, String proceso, List<Calendar> mesesReporte);
	
	
	/**
	 * Método encargado de consultar el consolidado de articulos detallado por nivel de 
	 * atención para un convenio
	 * @param convenio
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
	
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorNivelPorConvenio(Convenios convenio, String proceso, List<Calendar> mesesReporte);
	
	/**
	 * Método encargado de consultar el consolidado de articulos detallado por nivel de 
	 * atención para un contrato
	 * @param contrato
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
	
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorNivelPorContrato(Contratos contrato, String proceso, List<Calendar> mesesReporte);
	
	/**
	 * Método encargado de consultar el consolidado de articulos detallado por clase de 
	 * inventarios para un convenio
	 * @param convenio
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
	
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorClasePorConvenio(Convenios convenio, String proceso, List<Calendar> mesesReporte);
	
	/**
	 * Método encargado de consultar el consolidado de articulos detallado por clase de 
	 * inventarios para un contrato
	 * @param contrato
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
	
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorClasePorContrato(Contratos contrato, String proceso, List<Calendar> mesesReporte);


	/**
	 * Método encargado de consultar el consolidado de articulos detallado por articulo
	 * para un convenio
	 * @param convenio
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */
	
	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorArticuloPorConvenio(Convenios convenio, String proceso, List<Calendar> mesesReporte);
	
	/**
	 * Método encargado de consultar el consolidado de articulos detallado por articulo
	 * para un contrato
	 * @param contrato
	 * @param proceso
	 * @param mesesReporte
	 * @return ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio>
	 */

	public ArrayList<DtoNivelesAtencionTotalServiciosArticulosValorizadosPorConvenio> consultarConsolidadoArticulosPorArticuloPorContrato(Contratos contrato, String proceso, List<Calendar> mesesReporte);
}
