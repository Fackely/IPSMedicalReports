package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.facturacion.SqlBaseRevisionCuenta;

public interface RevisionCuentaDao 
{
	/**
	 * Consulta los responsables (convenios) de la subcuenta
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public HashMap consultarResponsables(Connection con,
										HashMap parametros);	
	
	/**
	 * Consulta los requisitos de ingreso y egreso por convenio
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarRequisitosConvenio(Connection con,
													HashMap parametros);
	
	/**
	 * Actualiza los requisitos de ingreso y egreso por convenio
	 * @param Connection con
	 * @param String requisitopaciente
	 * @param String subcuenta
	 * @param String cumplido
	 * */
	
	public boolean actualizarRequisitosConvenio(Connection con,
													   String requisitopaciente,
													   String subcuenta,
											  		   String cumplido);
	
	/**
	 * Consulta de las solicitudes por el Responsable
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultarListadoSolicitudes(Connection con,
														HashMap parametros);
	
	/**
	 * Consulta los estados de la solicitud en la historia clinica 
	 * @param Connection con  
	 * */
	public HashMap consultarEstadosSolicitudHistoriaC(Connection con);
	
	
	/**
	 * Consultar los tipos de solicitud 
	 * @param Connection con  
	 * */
	public HashMap consultarTiposSolicitud(Connection con);
	
	
	/**
	 * Consultar Detalle de la solicitud   
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public HashMap consultarDetalleSolicitud(Connection con, 
											 HashMap parametros);	
	
	/**
	 * Actualiza los registros del detalle de la solicitud
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarDetalleSolicitud(Connection con,
											 HashMap parametros);	
	
	/**
	 * Consultar Detalle de la Cirugia  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public HashMap consultarDetalleCirugia(Connection con, 
										   HashMap parametros);
	
	/**
	 * Consultar el listado de Pooles pendientes  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public HashMap consultarListadoPooles(Connection con, 
										  HashMap parametros);
	
	
	/**
	 * Verifica la existencia de pooles pendientes  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public int verificarPoolesPendientes(Connection con, 
										HashMap parametros);
	
	
	/**
	 * Consultar el listado de Pooles para el medico   
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public HashMap consultarListadoPoolesMedico(Connection con, 
												HashMap parametros);
	
	
	/**
	 * Actualiza los Pooles por Medico diferentes de tipo Solicitud Cirugia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */	
	public boolean actualizarPoolesMedico(Connection con,
										  HashMap parametros);
	
	
	/**
	 * Consultar Detalle Pooles Pendientes de la Cirugia  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public HashMap consultarDetalleCirugiaPool(Connection con, 
			  							  	   HashMap parametros);
	
	
	/**
	 * Actualiza los Pooles por Medico, tipo Solicitud Cirugia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */	
	public boolean actualizarPoolesMedicoCirugia(Connection con,
			  									 HashMap parametros);

	/**
	 * Actualiza los Estados de una Solicitud de Excenta a Cargada y Viseversa
	 * @param con
	 * @param nuevoEstadoCargo
	 * @param numeroSolicitud
	 */
	public boolean actualizarEstadoCargo(Connection con, int nuevoEstadoCargo, String numeroSolicitud);

	
	/**
	 * Metodo para consultar todos los detalles de la cuenta y los estados de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public HashMap detalleEstadoSolicitud(Connection con, String numeroSolicitud);

	/**
	 * Metodo para insertar el registro del cambio en la tabla log_estad_cargo_revis_cuenta
	 * @param con
	 * @param codigoDetalleCargo
	 * @param estadoAnterior
	 * @param estadoActual
	 * @param loginUsuario
	 */
	public void insertarLogEstadoCargo(Connection con, String codigoDetalleCargo, String estadoAnterior, String estadoActual, String loginUsuario);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultarEstadosSolFactura(Connection con);	
}