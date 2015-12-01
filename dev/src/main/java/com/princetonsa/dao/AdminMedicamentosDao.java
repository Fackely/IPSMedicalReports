/*
 * @(#)AdminMedicamentosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.enfermeria.DtoAdministracionMedicamentosBasico;



/**
 *  Interfaz para el acceder a la fuente de datos de la administraci�n de medicamentos
 *
 * @version 1.0, Sept. 16 / 2004
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L�pez</a>
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson R�os</a>
 */
public interface AdminMedicamentosDao 
{
	/**
	 * Carga el Listado de las solicitudes de medicamentos para un paciente determinado
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param codigoPaciente, int, codigo del paciente
	 * @return ResulSet list
	 */
	@SuppressWarnings("rawtypes")
	public Collection listadoSolMedXPaciente(Connection con, int codigoPaciente,int institucion);

	/**
	 * Carga el Listado de las solicitudes de medicamentos por AREA determinada
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param centroCosto, int, centro de costo
	 * @return ResulSet list
	 */
	@SuppressWarnings("rawtypes")
	public Collection listadoSolMedXArea(Connection con, HashMap<String, Object> criteriosBusquedaMap);
	
	/**
	 * Carga la Informaci�n de la creaci�n de la solicitud 
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param numeroSolicitud, int, numero de la solicitud 
	 * @return ResulSet list
	 */
	public  ResultSetDecorator encabezadoSolicitudMedicamentos(Connection con, int numeroSolicitud);
	
	
	/**
	 * Carga el Listado de las solicitudes de medicamentos para un numeroSolicitud determinado
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param numeroSolicitud
	 * @param codigoInstitucion C�digo de la instituci�n del ususario
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	public HashMap listadoMedicamentos(Connection con, int numeroSolicitud, int codigoInstitucion, boolean mostrarFinalizados);
	
	/**
	 * Carga el listado de los insumos correspondientes al numero de solicitud,
	 * segun las restricciones en la consulta <code>listadoInsumosGeneralStr</code>.
	 * @param con Connection conexion con la fuente de datos.
	 * @param numeroSolicitud Codigo del numero de la solicitud.
	 * @return 
	 */
	@SuppressWarnings("rawtypes")
	public HashMap listadoInsumos(Connection con, int numeroSolicitud, boolean mostrarFinalizados);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoArticulo
	 * @return
	 */
	public int obtenerNumeroDosisAdministradas(Connection con, int numeroSolicitud, int codigoArticulo);
	
	/**
	 * Metodo empleado para insertar los datos de una Administraci&oacute;n de
	 * Medicamentos.
	 * @param con, Connection con la fuente de datos.
	 * @param numeroSolicitud, codigo del n&uacute;mero de Solicitud.
	 * @param centroCosto, Centro de costo.
	 * @param usuario, Usuario
	 * @return int 1 efectivo, 0 de lo contrario.
	 */
	public int insertarAdmin(Connection con,int numeroSolicitud, int centroCosto, String usuario);
	
	/**
	 * Metodo para insertar los datos detalle administraci&oacute;n
	 * en la tabla de <code>detalle_admin</code>.
	 * @param con Connection conexion con la fuente de datos.
	 * @param unidadesDosisSaldos 
	 * @param articulo, codigo del articulo.
	 * @param administracion, codigo de la administraci&oacute;n al que pertenece el detalle.
	 * @param fecha, fecha 
	 * @param hora, hora
	 * @param cantidad, consumo realizado por parte de la enfermeria.
	 * @param observaciones, observaciones.
	 * @param esTraidoUsuario, boolean (true) traido por el usuario, false de lo contrario.
	 * @return int 1 efectivo, 0 de lo contrario.
	 */
	public int insertarDetalleAdmin(Connection con,
									int articulo, int artppal, int administracion, 
									String fecha, String hora, int cantidad, 
									String observaciones, String esTraidoUsuario,
									String lote, String fechaVencimiento, String unidadesDosisSaldos,
									String adelantoXNecesidad, String nadaViaOral, String usuarioRechazo);
	
	/**
	 * 
	 * @param con
	 * @param acumuladoAdminDosisMap
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public boolean insertarActualizarAcumuladoAdminDosis(Connection con, HashMap acumuladoAdminDosisMap);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param articulo
	 * @param loginUsuario
	 * @return
	 */
	public boolean insertarFinalizacionAdminArticulo(Connection con, int numeroSolicitud, int articulo, String loginUsuario);
	
	/**
	 * Carga el resumen de la administraci�n de medicamentos para un n�mero de solicitud
	 * espec�fico, cuando parte1=boolean entonces carga la info b�sica (descripci�n) de los articulos, 
	 * de lo contrario carga la info de la admin de los articulos
	 * @param con, Connection, conexi�n abierta con una fuente de datos
	 * @param numeroSolicitud, int, numeroSolicitud
	 * @param parte1, boolean, indica el segmento a consultar
	 * @return ResulSet list
	 */
	public  ResultSetDecorator resumenAdmiMedicamentos(Connection con, int numeroSolicitud, boolean parte1);
	
	
		/**
	 * M�todo para conocer la fecha y la hora de la �ltima administraci�n 
	 * @param con Conecci�n con la BD
	 * @param numeroSolicitud Solicitud de la cual se quiere saber la �ltima administraci�n
	 * @return fecha y hora de la �ltima administraci�n
	 */
	public String fechaUtlimaAdministracion(Connection con, int numeroSolicitud);
	
	
	/**
	 * M�todo para consultar los medicamentos despachados para las
	 * diferentes solicitudes que pueda tener un paciente
	 * @param con
	 * @param codigoCuenta
	 * @param nroSolicitudes
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public Collection consultaMedicamentosDespachadosPaciente(Connection con, int codigoCuenta, Vector nroSolicitudes);

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public ArrayList<DtoAdministracionMedicamentosBasico> cargarResumenAdministracion(Connection con, int numeroSolicitud);

	/**
	 * M�todo que consulta el color del triage
	 * @param con
	 * @param cuentaAdmisionUrgencias
	 * @return
	 */
	public String consultarColorTriage(Connection con,int cuentaAdmisionUrgencias);

}
