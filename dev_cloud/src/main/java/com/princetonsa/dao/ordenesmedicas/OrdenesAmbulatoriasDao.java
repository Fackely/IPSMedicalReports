/*
 * 
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 * 
 */
package com.princetonsa.dao.ordenesmedicas;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import com.princetonsa.dto.manejoPaciente.DtoDiagnostico;
import com.princetonsa.dto.ordenes.InfoArticuloOrdenAmbulatoriaDto;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 * @author Wilson Rios.
 * 
 */
public interface OrdenesAmbulatoriasDao 
{

	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param institucion
	 * @param estado
	 * @param idIngreso 
	 * @return
	 */
	public abstract HashMap consultarOrdenesAmbulatoriasPaciente(Connection con, int codigoPersona, String institucion, int estado, int idIngreso);

	/**
	 * Metodo que realiza las consultas de las ordenes ambulatorias x codigo
	 * @param con
	 * @param codigoPersona
	 * @param codigoInstitucion
	 * @param i
	 */
	public abstract HashMap consultarOrdenesAmbulatoriasXCodigoOrden(Connection con, String codigoOrden , String institucion);
	
	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @return
	 */
	public abstract HashMap cargarDetalleOrdenArticulos(Connection con, String codigoOrden);


	/**
	 * 
	 * @param con
	 * @param mapaInfoGeneral
	 * @return
	 */
	public abstract boolean guardarInformacionGeneralAmbulatorios(Connection con, HashMap vo);

	

	/**
	 * 
	 * @param con
	 * @param detalle
	 * @return
	 */
	public abstract boolean guardarInformacionDetalleOrdenAmbulatorioServicio(Connection con, HashMap vo);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean guardarInformacionDetalleOrdenAmbulatorioArticulo(Connection con, HashMap vo);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean ingresarResultado(Connection con, HashMap vo);


	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public abstract boolean anularOrden(Connection con, HashMap vo);


	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param estado
	 * @return
	 */
	public abstract boolean actualizarEstadoOrdenAmbulatoria(Connection con, String codigo, int estado);


	/**
	 * 
	 * @param con
	 * @param codOrden
	 * @param estado
	 * @param comentario 
	 * @param usuario 
	 */
	public abstract boolean actualizarEstadoActividadProgramaPYPPAcienteNumOrden(Connection con, String codOrden, String estado, String usuario, String comentario);
	
	/**
	 * Método implementado para actualizar el estado y el numero de solicitud
	 * de una orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	public abstract int actualizarSolicitudEnOrdenAmbulatoria(Connection con,HashMap campos);


	/**
	 * Método implementado para actualizar el estado y el numero de solicitud
	 * de una orden ambulatoria para la reserva de una cita
	 * @param con
	 * @param campos
	 * @return
	 */
	public abstract int confirmarReservaCitaEnOrdenAmbulatoria(Connection con,HashMap campos);
	
	/**
	 * 
	 * @param con
	 * @param ordenAmbulatoria
	 * @param institucion 
	 * @return
	 * Formato: urgente | observaciones | centro_atencion_solicita | usuario_solicita | especialidad_solicita | servicio | finalidad
	 */
	public abstract String obtenerInfoServicioProcOrdenAmbulatoria(Connection con, String ordenAmbulatoria, int institucion);


	/**
	 * 
	 * @param con
	 * @param campos
	 * @return
	 */
	public abstract int confirmarOrdenAmbulatoria(Connection con, HashMap campos);
	
	/**
	 * Método implementado para ingresar informacion de referencia de consulta
	 * externa a la orden ambulatoria
	 * @param con
	 * @param campos
	 * @return
	 */
	public abstract int ingresarInformacionReferenciaExterna(Connection con,HashMap campos);

	/**
	 *   
	 * @param con
	 * @param numeroOrden
	 */
	public abstract HashMap consultarServiciosOrdenAmbulatoria(Connection con, String numeroOrden, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param articulosMap
	 * @param codigoOrden
	 * @return
	 */
	public abstract boolean updateOrdenAmbulatoriaArticulos(Connection con, HashMap articulosMap, String codigoOrden);
	
	/**
	 * 
	 * @param con
	 * @param codigoOA
	 * @return
	 * @throws SQLException 
	 */ 
	public abstract String consultarCentroCostoSolicitante(Connection con, String codigoOA) throws SQLException;
	
	
	/**
	 * Metodo para consultar los centros de costo asociados a las unidades de agenda del servicio
	 * @param criterios
	 * @return
	 */
	public HashMap consultaCentrosCostoXUnidadAgendaServ(Connection con, int codigoServicio);
	
	public HashMap consultarCodigoOrdenAmb(String consecutivo);

	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @return
	 */
	public abstract int otenerUltimaCuentaPacienteValidaParaOrden(Connection con, int codigoPersona);
	

	/**
	 * 
	 * @param con
	 * @param codigoOrden
	 * @return
	 */
	public abstract DtoDiagnostico consultarDiagnosticoOrden(Connection con,int codigoOrden);
	
	
	/**
	 * Este m&eacute;todo se encarga de consultar el 
	 * diagn&oacute;stico asociado a la orden ambulatoria
	 * @param Connection con, String consecutivoOrden
	 * @return DtoDiagnostico
	 * @author Diana Carolina G
	 */
	public abstract DtoDiagnostico consultarDiagnosticoOrdenAmbulatoria(Connection con, int codigoOrdenAmbulatoria);
	
	/**
	 * Método que consulta los resultados asociados a las órdenes ambulatorias.
	 * 
	 * @param con
	 * @param numeroOrden
	 * 
	 * @return resultado
	 */
	public abstract String consultarResultadoOrdenesAmbulatorias(Connection con, String numeroOrden);
	
	/**
	 * Metodo que retorna {@link InfoArticuloOrdenAmbulatoriaDto} con el detalle de los articulos
	 * de la una orden ambulatoria 
	 * @param con
	 * @param codigoOrden
	 * @return
	 * @author javrammo
	 */
	List<InfoArticuloOrdenAmbulatoriaDto> detalleToolTipArticulosOrdenAmbulatoria(Connection con, int codigoOrden, boolean esMedicamento) throws IPSException;
}
