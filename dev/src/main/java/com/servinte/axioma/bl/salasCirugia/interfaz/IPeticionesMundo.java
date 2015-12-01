package com.servinte.axioma.bl.salasCirugia.interfaz;

import java.util.Date;

import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.salascirugia.PeticionQxDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;

/**
 * Interface que expone los servicios para la lógica de negocio de las Peticiones Cx
 * 
 * @author diaRuiPe
 * @version 1.0
 * @created 18-jul-2012 11:45:00 a.m.
 */
public interface IPeticionesMundo {
	
	/**
	 * Método que se encarga de asociar el detalle de la petición con la autorización de 
	 * Entidad Subcontratada generada por la capita.
	 * 
	 * @param autorizacionesEntidadesSub
	 * @param codPeticion
	 * @param codigoServi
	 * @throws IPSException
	 */
	void asociarServicioPeticionAutorizacion(AutorizacionesEntidadesSub autorizacionesEntidadesSub, int codPeticion, int codigoServi) throws IPSException;
	

	/**
	 * Consultar el estado de una peticion qx dado su codigo
	 * 
	 * @param codigoPeticion
	 * @return dtoPeticion
	 * @throws IPSException
	 * @author jeilones
	 * @created 17/08/2012
	 */
	PeticionQxDto obtenerEstadoPeticionQxPorId(long codigoPeticion)throws IPSException;
	
	/**
	 * Metodo encargado de obtener la información del Convenio/Contrato capitado asociado a la Petición
	 * 
	 * @param codigoOrden
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/08/2012
	 */
	ContratoDto obtenerConvenioContratoPorPeticion(int codigoOrden) throws IPSException;
	
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
			int motivoAnula, String comentarioAnulacion, boolean manejaTransaccion)throws IPSException;
}