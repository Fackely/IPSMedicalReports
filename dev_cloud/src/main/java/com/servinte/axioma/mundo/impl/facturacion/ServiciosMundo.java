package com.servinte.axioma.mundo.impl.facturacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.dao.fabrica.facturacion.FacturacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.facturacion.IServiciosDAO;
import com.servinte.axioma.mundo.interfaz.facturacion.IServiciosMundo;
import com.servinte.axioma.orm.Servicios;

public class ServiciosMundo implements IServiciosMundo
{
	
	IServiciosDAO dao;
	
	public ServiciosMundo(){
		dao=FacturacionFabricaDAO.crearServiciosDAO();
	}

	/**
	 * Obtener un servicio por su id
	 * @param id
	 * @return Servicios
	 */
	public Servicios obtenerServicioPorId(int id) {
		return dao.obtenerServicioPorId(id);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el tipo, especialidad y grupo de
	 * un servicio para comparar con la agrupación de servicios
	 * 
	 * @return DtoServicios
	 * @author, Fabian Becerra
	 *
	 */
	public DtoServicios obtenerTipoEspecialidadGrupoServicioPorID(int codigoServicio){
		return dao.obtenerTipoEspecialidadGrupoServicioPorID(codigoServicio);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar los servicios dependiendo
	 * del tipo tarifario oficial
	 * 
	 * @return ArrayList<DtoServicios>
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DtoServicios> obtenerServiciosXTipoTarifarioOficial(int codigoServicio,int codigoTarifarioOficial){
		return dao.obtenerServiciosXTipoTarifarioOficial(codigoServicio,codigoTarifarioOficial);
	}
	
	/**
	 * Este Método se encarga de consultar la tarifa vigente ISS de un servicio
	 * @return DtoServicios Dto que almacena la información del servicio y su tarifa ISS
	 * @author, Fabian Becerra
	 */
	public DtoServicios obtenerTarifaISSVigenteServicios(int codigoServicio,int esquemaTarifario){
		return dao.obtenerTarifaISSVigenteServicios(codigoServicio, esquemaTarifario);
	}
	
	/**
	 * Este Método se encarga de consultar la tarifa vigente SOAT de un servicio
	 * @return DtoServicios Dto que almacena la información del servicio y su tarifa SOAT
	 * @author, Fabian Becerra
	 */
	public DtoServicios obtenerTarifaSOATVigenteServicios(int codigoServicio,int esquemaTarifario){
		return dao.obtenerTarifaSOATVigenteServicios(codigoServicio, esquemaTarifario);
	}
	
	@Override
	public ArrayList<Servicios> buscarServiciosCierrePorNivelPorConvenioPorProceso(
			int codigoConvenio, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return dao.buscarServiciosCierrePorNivelPorConvenioPorProceso(
				codigoConvenio, consecutivoNivel, proceso, meses);
	}

	@Override
	public ArrayList<Servicios> buscarServiciosCierrePorNivelPorContratoPorProceso(
			int codigoContrato, long consecutivoNivel, String proceso,
			List<Calendar> meses) {
		return dao.buscarServiciosCierrePorNivelPorContratoPorProceso(
				codigoContrato, consecutivoNivel, proceso, meses);
	}

	@Override
	public ArrayList<DtoServicios> obtenerServicioLiquidacionPorEsquema(
			int codigoServicio, int codigoEsquemaTarifario,
			List<Integer> listaTiposLiquidacion, List<String> listaTiposServicio) {
		return dao.obtenerServicioLiquidacionPorEsquema(codigoServicio, codigoEsquemaTarifario, listaTiposLiquidacion, listaTiposServicio);
	}
	
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
	@Override
	public DtoServicios obtenerTipoGrupoServicio(int codigoServicio) {
		return dao.obtenerTipoGrupoServicio(codigoServicio);
	}

	/**
	 * Retorna la tarifaIss vigente
	 * @param con
	 * @param codServicio
	 * @return DtoServicios
	 * @autor Sandra Milena Barreto 
	 * Mt6587
	*/
		public DtoServicios obtenerTarifaIssVigente( int esquemaTarifario, int codServicio){
			return dao.obtenerTarifaIssVigente( esquemaTarifario, codServicio);
		}

		/**
		 * Retorna la tarifaIss vigente
		 * @param con
		 * @param con
		 * @param codServicio
		 * @return DtoServicios
		 * @autor Sandra Milena Barreto 
		 * Mt6587
		*/
		
		public DtoServicios obtenerTarifaSoatVigente(int esquemaTarifario, int codServicio){
			return dao.obtenerTarifaSoatVigente(esquemaTarifario, codServicio);
		}
}
