package com.servinte.axioma.bl.salasCirugia.impl;

import java.util.Date;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.bl.salasCirugia.interfaz.IPeticionesMundo;
import com.servinte.axioma.delegate.ordenes.PeticionesDelegate;
import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.salascirugia.AnulacionPeticionQxDto;
import com.servinte.axioma.dto.salascirugia.PeticionQxDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.fwk.exception.util.CODIGO_ERROR_NEGOCIO;
import com.servinte.axioma.hibernate.HibernateUtil;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.PeticionesServicio;


/**
 * Clase que implementa los servicios de Negocio necesarios para la lógica de negocio
 * de las Peticiones Qx.
 * 
 * @author diaRuiPe
 * @version 1.0
 * @created 18-jul-2012 11:45:00 a.m.
 */
public class PeticionesMundo implements IPeticionesMundo {

		
	/**
	 * Método que se encarga de asociar el detalle de la petición con la autorización de 
	 * Entidad Subcontratada generada por la capita.
	 * @param autorizacionesEntidadesSub
	 * @param codPeticion
	 * @param codigoServi
	 * @throws IPSException 
	 */
	@Override
	public void asociarServicioPeticionAutorizacion(AutorizacionesEntidadesSub autorizacionesEntidadesSub, int codPeticion, int codigoServi) throws IPSException
	{
		PeticionesDelegate delegate=null;
		PeticionesServicio peticionesServicio	=null; 
		try{
			delegate 				= new PeticionesDelegate();
			peticionesServicio		= new PeticionesServicio();
			peticionesServicio		= delegate.obtenerDetallePeticionPorId(codPeticion, codigoServi);
			peticionesServicio.setAutorizacionesEntidadesSub(autorizacionesEntidadesSub);
			delegate.asociarPeticionAutorizacion(peticionesServicio);
		}
		catch (IPSException ipsme) {
			HibernateUtil.abortTransaction();
			throw ipsme;
		}
		catch (Exception e) {
			HibernateUtil.abortTransaction();
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO);
		}
	}

	@Override
	public PeticionQxDto obtenerEstadoPeticionQxPorId(long codigoPeticion)
			throws IPSException {
		PeticionesDelegate delegate= null;
		PeticionQxDto estadoPeticion=null;
		try{
			delegate	= new PeticionesDelegate();
						
			estadoPeticion=delegate.obtenerEstadoPeticionQxPorId(codigoPeticion);		
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
		return estadoPeticion;
	}

	@Override
	public ContratoDto obtenerConvenioContratoPorPeticion(int codigoOrden)
			throws IPSException {
		PeticionesDelegate delegate= null;
		ContratoDto contratoDto=null;
		try{
			delegate	= new PeticionesDelegate();
			HibernateUtil.beginTransaction();
			contratoDto=delegate.obtenerConvenioContratoPorPeticion(codigoOrden);		
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
	 * Metodo que se encarga de Anular la Peticion 
	 * 
	 * @author Camilo Gómez 
	 * @param codigoOrden
	 * @param usuarioAnula
	 * @param fechaAnula
	 * @param horaAnula
	 * @param motivoAnula
	 * @param comentarioAnulacion
	 * @throws IPSException
	 */
	public void anularPeticion(int codigoOrden,String usuarioAnula, Date fechaAnula, String horaAnula, 
			int motivoAnula, String comentarioAnulacion, boolean manejaTransaccion)throws IPSException
	{
		AnulacionPeticionQxDto anulacionPeticionQxDto = null;
		PeticionesDelegate delegate = null;
		try{
			if(manejaTransaccion){
				HibernateUtil.beginTransaction();
			}
			delegate				= new PeticionesDelegate();
			anulacionPeticionQxDto 	= new AnulacionPeticionQxDto();
			anulacionPeticionQxDto.setCodigoPeticion(codigoOrden);
			anulacionPeticionQxDto.setUsuario(usuarioAnula);
			anulacionPeticionQxDto.setFecha(fechaAnula);
			anulacionPeticionQxDto.setHora(horaAnula);
			anulacionPeticionQxDto.setMotivoAnulacion(motivoAnula);
			anulacionPeticionQxDto.setComentarioAnulacion(comentarioAnulacion);
						
			//Se Guarda en Anulaciones_PeticionQx
			delegate.anularPeticion(anulacionPeticionQxDto);
			if(manejaTransaccion){
				HibernateUtil.endTransaction();
			}
		}
		catch (IPSException ipsme) {
			if(manejaTransaccion){
				HibernateUtil.abortTransaction();
			}
			throw ipsme;
		}
		catch (Exception e) {
			if(manejaTransaccion){
				HibernateUtil.abortTransaction();
			}
			Log4JManager.error(e.getMessage(),e);
			throw new IPSException(CODIGO_ERROR_NEGOCIO.ERROR_NO_CONTROLADO,e);
		}
	}
	
}