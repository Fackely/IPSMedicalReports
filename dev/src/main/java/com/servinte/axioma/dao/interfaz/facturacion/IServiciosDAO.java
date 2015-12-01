package com.servinte.axioma.dao.interfaz.facturacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.orm.Servicios;

public interface IServiciosDAO 
{
	
	/**
	 * Obtener un servicio por su id
	 * @param id
	 * @return Servicios
	 */
	public Servicios obtenerServicioPorId(int id);
	
	/**
	 * 
	 * Este Método se encarga de consultar el tipo, especialidad y grupo de
	 * un servicio para comparar con la agrupación de servicios
	 * 
	 * @return DtoServicios
	 * @author, Fabian Becerra
	 *
	 */
	public DtoServicios obtenerTipoEspecialidadGrupoServicioPorID(int codigoServicio);
	
	/**
	 * 
	 * Este Método se encarga de consultar los servicios dependiendo
	 * del tipo tarifario oficial
	 * 
	 * @return DtoServicios
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DtoServicios> obtenerServiciosXTipoTarifarioOficial(int codigoServicio,int codigoTarifarioOficial);
	
	/**
	 * Este Método se encarga de consultar la tarifa vigente ISS de un servicio
	 * @return DtoServicios Dto que almacena la información del servicio y su tarifa ISS
	 * @author, Fabian Becerra
	 */
	public DtoServicios obtenerTarifaISSVigenteServicios(int codigoServicio,int esquemaTarifario);
	
	/**
	 * Este Método se encarga de consultar la tarifa vigente SOAT de un servicio
	 * @return DtoServicios Dto que almacena la información del servicio y su tarifa SOAT
	 * @author, Fabian Becerra
	 */
	public DtoServicios obtenerTarifaSOATVigenteServicios(int codigoServicio,int esquemaTarifario);
	
	
	/**
	 * Este Método se encarga de obtener los distintos servicios
	 * que se encuentran en el cierre de presupuesto
	 * para un nivel de atención para un convenio.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoConvenio
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<Servicios>
	 * 
	 */
	public ArrayList<Servicios> buscarServiciosCierrePorNivelPorConvenioPorProceso(int codigoConvenio, 
										long consecutivoNivel, String proceso, List<Calendar> meses);
	
	/**
	 * Este Método se encarga de obtener los distintos servicios
	 * que se encuentran en el cierre de presupuesto
	 * para un nivel de atención para un contrato.
	 * 
	 * @author, Ricardo Ruiz
	 *
	 * @param codigoContrato
	 * @param consecutivoNivel
	 * @param proceso
	 * @param meses
	 * @return ArrayList<Servicios>
	 * 
	 */
	public ArrayList<Servicios> buscarServiciosCierrePorNivelPorContratoPorProceso(int codigoContrato, 
										long consecutivoNivel, String proceso, List<Calendar> meses);
	
	
	/**
	 * Retorna un servicio y su tipo de liquidación de acuerdo al esquema tarifario enviado.
	 * Este método solo esta implementado para los esquemas tárifarios 1 y 2, es decir SOAT e ISS
	 * @param codigoServicio
	 * @param esquemaTarifario
	 * @param listaTiposLiquidacion
	 * @param listaTiposServicio
	 * @return ArrayList<DtoServicios>
	 *
	 * @autor Cristhian Murillo
	*/
	public ArrayList<DtoServicios> obtenerServicioLiquidacionPorEsquema(int codigoServicio, int codigoEsquemaTarifario, List<Integer> listaTiposLiquidacion, List<String> listaTiposServicio);	
	
	
	/**
	 * 
	 * M&eacute;todo que permite consultar el tipo de servicio y grupo 
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return ArrayList
	 * @author Diana Ruiz
	 * @since 07/07/2011
	 * 
	 */
	
	public DtoServicios obtenerTipoGrupoServicio(int codigoServicio);
	
	/**
	 * Retorna la tarifaIss vigente
	 * @param con
	 * @param con
	 * @param codServicio
	 * @return DtoServicios
	 * @autor Sandra Milena Barreto 
	 * Mt6587
	*/
  public DtoServicios obtenerTarifaIssVigente( int esquemaTarifario, int codServicio);
  
  /**
	 * Retorna la tarifaIss vigente
	 * @param con
	 * @param codServicio
	 * @return DtoServicios
	 * @autor Sandra Milena Barreto 
	 *  Mt6587
	*/		
  public DtoServicios obtenerTarifaSoatVigente( int esquemaTarifario, int codServicio);
}
