package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.RevisionCuentaDao;

public class RevisionCuenta
{
	
	
	/**
	 * Instancia el Dao
	 * */
	public static RevisionCuentaDao getRevisionCuentaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRevisionCuentaDao();
	}
	
		
	/**
	 * Consulta los responsables (convenios) de la subcuenta
	 * @param Connection con
	 * @param HashMap parametros
	 * */
	public static HashMap consultarResponsables(Connection con,
												HashMap parametros)
	{
		return getRevisionCuentaDao().consultarResponsables(con, parametros);
	}
	
	
	/**
	 * Consulta los requisitos de ingreso y egreso por convenio
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap consultarRequisitosConvenio(Connection con,
													HashMap parametros)
	{
		return getRevisionCuentaDao().consultarRequisitosConvenio(con, parametros);
	}
	
	
	
	/**
	 * Actualiza los requisitos de ingreso y egreso por convenio
	 * @param Connection con
	 * @param String requisitopaciente
	 * @param String subcuenta
	 * @param String cumplido
	 * */	
	public static boolean actualizarRequisitosConvenio(Connection con,
													   String requisitopaciente,
													   String subcuenta,
											  		   String cumplido)
	{
		return getRevisionCuentaDao().actualizarRequisitosConvenio(con, requisitopaciente, subcuenta, cumplido);
	}
	
	
	/**
	 * Consulta de las solicitudes por el Responsable
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static HashMap consultarListadoSolicitudes(Connection con,
														HashMap parametros)
	{
		return getRevisionCuentaDao().consultarListadoSolicitudes(con, parametros);
	}
	

	/**
	 * Consulta los estados de la solicitud en la historia clinica 
	 * @param Connection con  
	 * */
	public static HashMap consultarEstadosSolicitudHistoriaC(Connection con)
	{
		return getRevisionCuentaDao().consultarEstadosSolicitudHistoriaC(con);
	}
	
	
	/**
	 * Consultar los tipos de solicitud 
	 * @param Connection con  
	 * */
	public static HashMap consultarTiposSolicitud(Connection con)
	{
		return getRevisionCuentaDao().consultarTiposSolicitud(con);	
	}
	
	
	/**
	 * Consultar Detalle de la solicitud   
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDetalleSolicitud(Connection con, HashMap parametros)
	{
		return getRevisionCuentaDao().consultarDetalleSolicitud(con, parametros);
	}	
	
	
	/**
	 * Actualiza los registros del detalle de la solicitud
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public static boolean actualizarDetalleSolicitud(Connection con,
											 HashMap parametros)
	{
		return getRevisionCuentaDao().actualizarDetalleSolicitud(con, parametros);
	}
	
	/**
	 * Consultar Detalle de la Cirugia  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDetalleCirugia(Connection con, 
												  HashMap parametros)
	{
		return getRevisionCuentaDao().consultarDetalleCirugia(con, parametros);
	}
	
	
	/**
	 * Consultar el listado de Pooles pendientes  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static HashMap consultarListadoPooles(Connection con, 
									      HashMap parametros)
	{
		return getRevisionCuentaDao().consultarListadoPooles(con, parametros);
	}
	
	
	/**
	 * Verifica la existencia de pooles pendientes  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static int verificarPoolesPendientes(Connection con, 
										HashMap parametros)
	{	
		return getRevisionCuentaDao().verificarPoolesPendientes(con, parametros);
	}
	
	
	/**
	 * Consultar el listado de Pooles para el medico   
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static HashMap consultarListadoPoolesMedico(Connection con, 
												HashMap parametros)
	{
		return getRevisionCuentaDao().consultarListadoPoolesMedico(con, parametros);
	}
	
	
	/**
	 * Actualiza los Pooles por Medico diferentes de tipo Solicitud Cirugia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */	
	public static boolean actualizarPoolesMedico(Connection con,
												HashMap parametros)
	{
		return getRevisionCuentaDao().actualizarPoolesMedico(con, parametros);
	}
	
	
	/**
	 * Consultar Detalle Pooles Pendientes de la Cirugia  
	 * @param Connection con  
	 * @param HashMap parametros
	 * */
	public static HashMap consultarDetalleCirugiaPool(Connection con, 
													  HashMap parametros)
	{
		return getRevisionCuentaDao().consultarDetalleCirugiaPool(con, parametros);
	}	
	
	
	/**
	 * Actualiza los Pooles por Medico, tipo Solicitud Cirugia 
	 * @param Connection con
	 * @param HashMap parametros
	 * */	
	public static boolean actualizarPoolesMedicoCirugia(Connection con,
														HashMap parametros)
	{
		return getRevisionCuentaDao().actualizarPoolesMedicoCirugia(con, parametros);
	}

	/**
	 * Actualiza los Estados de una Solicitud de Excenta a Cargada y Viseversa
	 * @param con
	 * @param nuevoEstadoCargo
	 * @param numeroSolicitud
	 */
	public static boolean actualizarEstadoCargo(Connection con, int nuevoEstadoCargo, String numeroSolicitud) 
	{
		return getRevisionCuentaDao().actualizarEstadoCargo(con, nuevoEstadoCargo, numeroSolicitud);
	}

	/**
	 * Metodo para consultar todos los detalles de la cuenta y los estados de una solicitud
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap detalleEstadoSolicitud(Connection con, String numeroSolicitud) 
	{
		return getRevisionCuentaDao().detalleEstadoSolicitud(con, numeroSolicitud);
	}

	
	/**
	 * Metodo para insertar el registro del cambio en la tabla log_estad_cargo_revis_cuenta
	 * @param con
	 * @param codigoDetalleCargo
	 * @param estadoAnterior
	 * @param estadoActual
	 * @param loginUsuario
	 */
	public static void insertarLogEstadoCargo(Connection con, String codigoDetalleCargo, String estadoAnterior, String estadoActual, String loginUsuario) 
	{
		getRevisionCuentaDao().insertarLogEstadoCargo(con, codigoDetalleCargo, estadoAnterior, estadoActual, loginUsuario);
	}


	/**
	 * 
	 * @param con
	 * @return
	 */
	public static HashMap consultarEstadosSolFactura(Connection con)
	{
		return getRevisionCuentaDao().consultarEstadosSolFactura(con);
	}
}