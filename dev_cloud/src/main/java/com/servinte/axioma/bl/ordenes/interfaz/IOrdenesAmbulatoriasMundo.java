package com.servinte.axioma.bl.ordenes.interfaz;

import java.util.Date;

import com.servinte.axioma.dto.facturacion.ContratoDto;
import com.servinte.axioma.dto.ordenes.OrdenAmbulatoriaDto;
import com.servinte.axioma.fwk.exception.IPSException;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;

/**
 * Interface que expone los servicios para la lógica de negocio de las Ordenes Ambulatorias
 * 
 * @author diaRuiPe
 * @version 1.0
 * @created 18-jul-2012 11:45:00 a.m.
 */
public interface IOrdenesAmbulatoriasMundo {
	
	/**
	 * Método que se encarga de asociar el detalle de las ordenes ambulatorias generadas, con la autorización de capitación subcontratada. 
	 * @param autorizacionesEntidadesSub
	 * @param esServicio
	 * @param codigoServiArti
	 * @param codOrdenAmbulatoria
	 * @throws IPSException
	 */
	void asociarOrdenAmbulatoriaAutorizaciones(AutorizacionesEntidadesSub autorizacionesEntidadesSub, boolean esServicio, 
			int codigoServiArti, long codOrdenAmbulatoria, boolean requiereTransaccion)throws IPSException;
	
	/**
	 * Consultar el estado de una orden ambulatoria dado su codigo
	 * 
	 * @param codigoOrden
	 * @return ordenAmbultaria
	 * @throws IPSException
	 * @author jeilones
	 * @created 16/08/2012
	 */
	OrdenAmbulatoriaDto obtenerEstadoOrdenesAmbulatoriasPorId(long codigoOrden)throws IPSException;
	
	/**
	 * Metodo encargado de obtener la información del Convenio/Contrato capitado asociado a la orden ambulatoria
	 * 
	 * @param codigoOrden
	 * @param tipoOrden
	 * @return
	 * @throws IPSException
	 * @author jeilones
	 * @created 30/08/2012
	 */
	ContratoDto obtenerConvenioContratoPorOrdenAmbulatoria(Long codigoOrden, int tipoOrden) throws IPSException;
	
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
			String motivoAnula, boolean isPyp)throws IPSException;
	
}
