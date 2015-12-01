package com.servinte.axioma.servicio.impl.tesoreria;

import java.util.ArrayList;
import java.util.Date;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.servinte.axioma.mundo.fabrica.TesoreriaFabricaMundo;
import com.servinte.axioma.mundo.interfaz.tesoreria.ISolicitudTrasladoCajaMundo;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.servicio.interfaz.tesoreria.ISolicitudTrasladoCajaServicio;

/**
 * Esta clase se encarga de definir los m&eacute;todos de negocio
 * de la entidad Solicitud Traslado Caja 
 * 
 * @author Angela Maria Aguirre
 * @since 6/08/2010
 */
public class SolicitudTrasladoCajaServicio implements
		ISolicitudTrasladoCajaServicio {
	
	private ISolicitudTrasladoCajaMundo solicitudTrasladoCajaMundo;
	
	/**
	 * 
	 * M&eacute;todo constructor de la clase
	 * @author, Angela Maria Aguirre
	 */
	public SolicitudTrasladoCajaServicio(){
		solicitudTrasladoCajaMundo = TesoreriaFabricaMundo.crearSolicitudTrasladoCajaMundo();
	}
	

	/**
	 * 
	 * Este m&eacute;todo se encarga de consultar los estados de una solicitud
	 * de traslado de caja
	 * 
	 * @param String[], filtro
	 * @return  ArrayList<DtoIntegridadDominio> 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DtoIntegridadDominio> listarEstadoSolicitudTrasladoCaja(String[] filtro) {
		return solicitudTrasladoCajaMundo.listarEstadoSolicitudTrasladoCaja(filtro);		
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar los registros de las 
	 * solicitudes de traslado de caja recaudo
	 * 
	 * @param DTOConsultaSolicitudTrasladoCaja
	 * @return ArrayList<DTOConsultaSolicitudTrasladoCaja>
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ArrayList<DtoConsultaTrasladosCajasRecaudo> consultarRegistrosSolicitudTrasladoCaja(
			DtoConsultaTrasladosCajasRecaudo dto){
		return solicitudTrasladoCajaMundo.consultarRegistrosSolicitudTrasladoCaja(dto);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.tesoreria.ISolicitudTrasladoCajaServicio#obtenerFechaUltimoMovimientoSolicitudAcept(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoSolicitudAcept(TurnoDeCaja turnoDeCaja) {
		
		return solicitudTrasladoCajaMundo.obtenerFechaUltimoMovimientoSolicitudAcept(turnoDeCaja);
	}

}
