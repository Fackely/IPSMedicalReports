package com.servinte.axioma.mundo.impl.tesoreria;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladoRecaudoMayorEnCierre;
import com.princetonsa.dto.tesoreria.DtoConsultaTrasladosCajasRecaudo;
import com.princetonsa.dto.tesoreria.DtoDetalleDocSopor;
import com.princetonsa.dto.tesoreria.DtoSolicitudTrasladoPendiente;
import com.princetonsa.dto.tesoreria.DtoTrasladoCaja;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.dao.fabrica.TesoreriaFabricaDAO;
import com.servinte.axioma.dao.interfaz.tesoreria.ISolicitudTrasladoCajaDAO;
import com.servinte.axioma.mundo.interfaz.tesoreria.ISolicitudTrasladoCajaMundo;
import com.servinte.axioma.orm.Cajas;
import com.servinte.axioma.orm.TurnoDeCaja;
import com.servinte.axioma.servicio.fabrica.odontologia.administracion.AdministracionFabricaServicio;
import com.servinte.axioma.servicio.interfaz.administracion.IUsuariosServicio;


/**
 * Contiene la l&oacute;gica de Negocio para todo lo relacionado solicitudes
 * 
 * 
 * @author Cristhian Murillo
 * @see ISolicitudTrasladoCajaMundo
 */

public class SolicitudTrasladoCajaMundo implements ISolicitudTrasladoCajaMundo{

	private ISolicitudTrasladoCajaDAO solicitudTrasladoCajaDAO;

	
	public SolicitudTrasladoCajaMundo() {
		inicializar();
	}

	private void inicializar() {
		solicitudTrasladoCajaDAO = TesoreriaFabricaDAO.crearSolicitudTrasladoCajaDAO();
	}

	
	@Override
	public ArrayList<DtoSolicitudTrasladoPendiente> obtenerSolicitudesPendientes(Cajas caja) {
		return solicitudTrasladoCajaDAO.obtenerSolicitudesPendientes(caja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ISolicitudTrasladoCajaMundo#solicitudesAceptadasXTurnoCajaCajero(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public List<DtoTrasladoCaja> obtenerSolicitudesAceptadasXTurnoCajaCajero(TurnoDeCaja turnoDeCaja) {
		return solicitudTrasladoCajaDAO.obtenerSolicitudesAceptadasXTurnoCajaCajero(turnoDeCaja);
	}
	
	/**
	 * Este método se encarga de consultar las solicitudes de traslados a caja de 
	 * recaudo y a caja mayor realizados en el cierre
	 * @param TurnoDeCaja
	 * @return ArrayList<DtoConsultaTrasladoRecaudoMayorEnCierre>
	 */
	public ArrayList<DtoConsultaTrasladoRecaudoMayorEnCierre> consultarTrasladosCajaRecaudoMayorEnCierre(TurnoDeCaja turnoDeCaja){
		return solicitudTrasladoCajaDAO.consultarTrasladosCajaRecaudoMayorEnCierre(turnoDeCaja);
	}
	
	
	
	@Override	
	public boolean tieneSolicitudDeTraslado(Cajas caja){
		
		//solicitudTrasladoCajaDAO.obtenerSolicitudesPendientes(caja);
		boolean tieneSolicitud;
		if(Utilidades.isEmpty(solicitudTrasladoCajaDAO.obtenerSolicitudesPendientes(caja))){
			tieneSolicitud = false;
		}else{
			tieneSolicitud = true;
		}
		
		return tieneSolicitud;
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
	public ArrayList<DtoIntegridadDominio> listarEstadoSolicitudTrasladoCaja(String[] filtro){
				
		Connection con=UtilidadBD.abrirConexion();
		
		ArrayList<DtoIntegridadDominio> listaEstadoSolicitud=Utilidades.generarListadoConstantesIntegridadDominio(
				con, filtro, false);
		
		UtilidadBD.closeConnection(con);
		
		return listaEstadoSolicitud;
		
	}
	
	/**
	 * 
	 * Este M&eacute;todo se encarga de consultar los registros de las 
	 * solicitudes de traslado de caja recaudo
	 * 
	 * @param DtoConsultaTrasladosCajasRecaudo
	 * @return ArrayList<DtoConsultaTrasladosCajasRecaudo>
	 * @author, Angela Maria Aguirre
	 *
	 */
	public ArrayList<DtoConsultaTrasladosCajasRecaudo> consultarRegistrosSolicitudTrasladoCaja(DtoConsultaTrasladosCajasRecaudo dto){
		
		IUsuariosServicio usuarioServicio = AdministracionFabricaServicio.crearUsuariosServicio();		
		ArrayList<DtoConsultaTrasladosCajasRecaudo> listaSolicitud = 
			solicitudTrasladoCajaDAO.consultarRegistrosSolicitudTrasladoCaja(dto);
		String nombreCajeroSolicitante="", nombreCajeroAcepta="";
		
		if(listaSolicitud!=null && listaSolicitud.size()>0){
			for(DtoConsultaTrasladosCajasRecaudo registro : listaSolicitud){
				
				DtoUsuarioPersona cajeroSolicitante = new DtoUsuarioPersona();
				DtoUsuarioPersona cajeroAcepta = new DtoUsuarioPersona();
				
				cajeroSolicitante = usuarioServicio.obtenerDtoUsuarioPersona(registro.getLoginCajeroSolicitante());
				cajeroAcepta = usuarioServicio.obtenerDtoUsuarioPersona(registro.getLoginCajeroAcepta());
				
				if(cajeroSolicitante!=null){					
					nombreCajeroSolicitante = cajeroSolicitante.getApellido();
					nombreCajeroSolicitante += " "+cajeroSolicitante.getNombre();
					nombreCajeroSolicitante += " ( "+cajeroSolicitante.getLogin()+" )";									
				}
				if(cajeroAcepta!=null){					
					nombreCajeroAcepta = cajeroAcepta.getNombre();
					nombreCajeroAcepta += " "+cajeroAcepta.getApellido();
					nombreCajeroAcepta += " ( "+cajeroAcepta.getLogin()+" )";									
				}
					
				if(registro.getValorTotalSolicitud()==null){
					registro.setValorTotalSolicitud(new BigDecimal(0));
				}
				
				if(registro.getEstadoSolicitud().equals(
						ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaSolicitado)){
					
					registro.setEstadoSolicitud((String)ValoresPorDefecto.getIntegridadDominio(
							ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaSolicitado));					
				}
				
				if(registro.getEstadoSolicitud().equals(
						ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado)){
					
					registro.setEstadoSolicitud((String)ValoresPorDefecto.getIntegridadDominio(
							ConstantesIntegridadDominio.acronimoEstadoSolicitudTrasladoCajaAceptado));					
				}
				if(UtilidadTexto.isEmpty(registro.getHoraAceptacion())){
					registro.setHoraAceptacion("");
				}				
				registro.setLoginCajeroSolicitante(nombreCajeroSolicitante);
				registro.setLoginCajeroAcepta(nombreCajeroAcepta);
			}
		}			
		return listaSolicitud;
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ISolicitudTrasladoCajaMundo#obtenerSolicitudPorConsecutivo(long)
	 */
	@Override
	public DtoTrasladoCaja obtenerSolicitudPorConsecutivo(long consecutivo) {

		return solicitudTrasladoCajaDAO.obtenerSolicitudPorConsecutivo(consecutivo);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ISolicitudTrasladoCajaMundo#obtenerFechaUltimoMovimientoSolicitudAcept(com.servinte.axioma.orm.TurnoDeCaja)
	 */
	@Override
	public Date obtenerFechaUltimoMovimientoSolicitudAcept(TurnoDeCaja turnoDeCaja) {
		
		return solicitudTrasladoCajaDAO.obtenerFechaUltimoMovimientoSolicitudAcept(turnoDeCaja);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.tesoreria.ISolicitudTrasladoCajaMundo#obtenerDocSopSolicitudTrasladoCaja(long)
	 */
	@Override
	public List<DtoDetalleDocSopor> obtenerDocSopSolicitudTrasladoCaja(	long codigoSolicitud) {
		
		return solicitudTrasladoCajaDAO.obtenerDocSopSolicitudTrasladoCaja(codigoSolicitud);
	}
	
}
