package com.servinte.axioma.bl.ordenes.impl;

import java.util.Date;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;

import com.servinte.axioma.bl.ordenes.interfaz.IOrdenesAmbulatoriasMundo;
import com.servinte.axioma.delegate.ordenes.OrdenesAmbulatoriasDelegate;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.ordenes.AnulacionOrdenAmbulatoriaDto;
import com.servinte.axioma.dto.ordenes.OrdenAmbulatoriaDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.DetOrdenAmbArticulo;
import com.servinte.axioma.orm.DetOrdenAmbServicio;

/**
 * Clase que implementa los servicios de Negocio necesarios para la lógica de negocio
 * de las Ordenes Ambulatorias
 * 
 * @author diaRuiPe
 * @version 1.0
 * @created 18-jul-2012 11:45:00 a.m.
 */
public class OrdenesAmbulatoriasMundo implements IOrdenesAmbulatoriasMundo{
	
	
	/**
	 * Método que se encarga de asociar el detalle de las ordenes generadas con las autorizaciones de 
	 * entidad subcontratad generadas por la capita.
	 * @param autorizacionesEntidadesSub
	 * @param esServicio
	 * @param codigoServiArti
	 * @param codOrdenAmbulatoria
	 * @throws IPSException
	 */
	@Override
	public void asociarOrdenAmbulatoriaAutorizaciones(AutorizacionesEntidadesSub autorizacionesEntidadesSub, boolean esServicio, int codigoServiArti, long codOrdenAmbulatoria, boolean requiereTransaccion)throws IPSException 
	{
		try{
			if (requiereTransaccion){
				HibernateUtil.beginTransaction();
			}
						
			if(esServicio){
				asociarServicioAutorizacion(autorizacionesEntidadesSub,codOrdenAmbulatoria, codigoServiArti);
			}else{
				asociarMedicamentosAutorizacion(autorizacionesEntidadesSub, codigoServiArti, codOrdenAmbulatoria);
			}
			if (requiereTransaccion){
				HibernateUtil.endTransaction();
			}
		}
		catch (IPSException ipsme) {
			if (requiereTransaccion){
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		}
		catch (Exception e) {
			if (requiereTransaccion){
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}

	/**
	 * Método que se encarga de asociar la solicitud de Procedimientos generada con la autorización de 
	 * Entidad Subcontratada generada por la capita.
	 * @param dtoAsociaSolAutorOrdAmbu
	 * @throws IPSException 
	 */
	private void asociarServicioAutorizacion(AutorizacionesEntidadesSub autorizacionesEntidadesSub, long codOrdenAmbulatoria, int codigoServiArti) throws IPSException
	{
		OrdenesAmbulatoriasDelegate delegate=null;
		DetOrdenAmbServicio detalleOrdenAmbServicio	=null; 
		try{
			delegate 					= new OrdenesAmbulatoriasDelegate();
			detalleOrdenAmbServicio		= new DetOrdenAmbServicio();
			detalleOrdenAmbServicio		= delegate.obtenerDetalleOrdenAmbServiciosPorId(codOrdenAmbulatoria);
			detalleOrdenAmbServicio.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			delegate.asociarOrdenAmbServiciosAutorizacion(detalleOrdenAmbServicio);
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	/**
	 * Método que se encarga de asociar la solicitud de Medicamentos generada con la autorización de
	 * Entidad Subcontratada generada por la capita.
	 * @param dtoAsociaSolAutorOrdAmbu
	 * @throws IPSException
	 */
	private void asociarMedicamentosAutorizacion(AutorizacionesEntidadesSub autorizacionesEntidadesSub, int codigoServiArti, long codOrdenAmbulatoria)throws IPSException
	{
		OrdenesAmbulatoriasDelegate delegate=null;
		DetOrdenAmbArticulo detalleOrdenAmbMedicamento	=null; 
		try{
			delegate 					= new OrdenesAmbulatoriasDelegate();
			detalleOrdenAmbMedicamento	= new DetOrdenAmbArticulo();
			detalleOrdenAmbMedicamento	= delegate.obtenerDetalleOrdenAmbMedicamentosPorId(codOrdenAmbulatoria, codigoServiArti);
			detalleOrdenAmbMedicamento.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			delegate.asociarOrdenAmbMedicamentosAutorizacion(detalleOrdenAmbMedicamento);
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO, e);
		}
	}
	
	/**
	 * Consultar el estado de una orden ambulatoria dado su codigo
	 * 
	 * @param codigoOrden
	 * @return ordenAmbultaria
	 * @throws IPSException
	 * @author jeilones
	 * @created 16/08/2012
	 */
	public OrdenAmbulatoriaDto obtenerEstadoOrdenesAmbulatoriasPorId(long codigoOrden)throws IPSException{
		OrdenesAmbulatoriasDelegate delegate= null;
		OrdenAmbulatoriaDto estadoOrdenesAmbulatorias=null;
		try{
			delegate	= new OrdenesAmbulatoriasDelegate();
						
			estadoOrdenesAmbulatorias=delegate.obtenerEstadoOrdenesAmbulatoriasPorId(codigoOrden);		
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		return estadoOrdenesAmbulatorias;
	}

	@Override
	public ContratoDto obtenerConvenioContratoPorOrdenAmbulatoria(
			Long codigoOrden, int tipoOrden) throws IPSException {
		OrdenesAmbulatoriasDelegate delegate= null;
		ContratoDto contratoDto=null;
		try{
			delegate	= new OrdenesAmbulatoriasDelegate();
			HibernateUtil.beginTransaction();			
			contratoDto=delegate.obtenerConvenioContratoPorOrdenAmbulatoria(codigoOrden, tipoOrden);
			HibernateUtil.endTransaction();
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
		return contratoDto;
	}
	
	/**
	 * Metodo que se encarga de Anular la Orden Ambulatoria 
	 * 
	 * @author Camilo Gómez 
	 * @param codigoOrden
	 * @param usuarioAnula
	 * @param fechaAnula
	 * @param horaAnula
	 * @param motivoAnula
	 * @param isPyp
	 * @throws IPSException
	 */
	public void anularOrdenAmbulatoria(long codigoOrden,String usuarioAnula, Date fechaAnula, String horaAnula,
			String motivoAnula, boolean isPyp)throws IPSException
	{
		AnulacionOrdenAmbulatoriaDto anulacionDto = null;
		OrdenesAmbulatoriasDelegate delegate = null;
		try{
			delegate	= new OrdenesAmbulatoriasDelegate();
			anulacionDto= new AnulacionOrdenAmbulatoriaDto();
			anulacionDto.setCodigoOrden(codigoOrden);
			anulacionDto.setUsuario(usuarioAnula);
			anulacionDto.setFecha(fechaAnula);
			anulacionDto.setHora(horaAnula);
			anulacionDto.setMotivoAnulacion(motivoAnula);
			anulacionDto.setCodigoEstadoOrden(Byte.parseByte(ConstantesBD.codigoEstadoOrdenAmbulatoriaAnulada+""));
						
			//Se Guarda en Anulaciones_Solicitud
			delegate.anularOrdenAmbulatoria(anulacionDto);
			
			if(isPyp){
				//Se actualiza estado en Actividad Programa PyP Paciente
				anulacionDto.setCodigoEstadoOrden(Byte.parseByte(ConstantesBD.codigoEstadoProgramaPYPCancelado+""));
				delegate.actualizarActividadProgramaPyPPaciente(anulacionDto);
			}
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
}
