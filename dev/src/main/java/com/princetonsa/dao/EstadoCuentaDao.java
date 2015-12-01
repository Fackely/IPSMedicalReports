/*
 * @(#)EstadoCuentaDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Esta <i>interface</i> define el contrato de operaciones que debe implementar la clase que presta el servicio
 * de acceso a datos para el objeto <code>EstadoCuenta</code>.
 *
 *	@version 1.0, 5/08/2004
 */
public interface EstadoCuentaDao 
{
	/**
	 * Método que carga los datos necesarios para
	 * el estado de cuenta para todas las cuentas
	 * de un paciente dado
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigoPaciente Código del paciente
	 * al que se desean revisar sus cuentas
	 * @return
	 */
	public ArrayList<HashMap<String, Object>>  cargarTodasCuentaPaciente (Connection con, int codigoPaciente);
	
	/**
	 * Método que consulta todos los convenios de un ingreso
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public  ArrayList<HashMap<String, Object>> cargarTodosConvenioIngreso (Connection con, String idIngreso) ;
	
	/**
	  * Método que consulta las solicitudes de un convenio
	  * @param con
	  * @param campos
	  * @return
	  */
	public HashMap<String, Object> cargarTodasSolicitudesSubCuenta (Connection con, HashMap campos);
	
	/**
	 * Método que realiza la busqueda avanzada de las solicitudes de una sub cuenta
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> buscarSolicitudesSubCuenta(Connection con,HashMap campos);
	
	/**
	 * Valor total de los cargos (Independiente convenio o paciente)
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param cuenta Código de la cuenta en la que
	 * se esta buscando el total
	 * @return
	 */
	public double valorTotalCargos(Connection con, String idSubCuenta,boolean incluyePyp);
	
	/**
	 * Método que carga la informacion del monto de cobro para un convenio específico
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public HashMap cargarMontoCobroSubCuenta(Connection con,String idSubCuenta);
	
	/***
	 * Metodo para consulta los abonos de una cuenta evaluando cada uno de sus casos
	 * @param con
	 * @param idCuenta
	 * @param codigoPaciente
	 * @param ingreso Id del ingreso para filtrar los abonos
	 * @return
	 * @throws SQLException
	 */
	public double consultarAbonos (Connection con, int idCuenta, int codigoPaciente, Integer ingreso) ;
	
	/**
	 * Método que consulta el detalle del cargo de un solicitud de servicio/articulo
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleCargoServicioArticulo(Connection con,HashMap campos);
	/**
	 * 
	 * M&eacute;todo para mostrar el detalle de la solicitud con despacho de equivalentes 
	 * @author Diana Ruiz
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleCargoArticuloConEquivalente(Connection con,HashMap campos);
	
	/**
	 * Método que carga el detalle del cargo de una cirugía
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleCargoCirugia(Connection con, HashMap campos);
	
	/**
	 * Metodo que obtiene los numeros de carnet de un paciente
	 * @param con
	 * @param convenio
	 * @param ingreso
	 * @return
	 */
	public String obtenerCarnet(Connection con, int convenio, int ingreso);
	
	/**
	 * Metodo que consulta la Fecha de Egreso segun Estado Cuenta
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public HashMap<String, Object> fechaEgreso(Connection con, int ingreso);
	
	/**
	 * Metodo que consulta los codigos de las facturas asociadas a un cargo
	 * @param con
	 * @param subCuenta
	 * @param solicitud
	 * @return
	 */
	public String codigosFacturas(Connection con, int subCuenta, int solicitud);

	/**
	 * 
	 * @param con
	 * @param idSubCuenta
	 * @return
	 */
	public String obtenerFechaValidacionEsquemas(Connection con,String idSubCuenta);
	
	/**
	 * Metodo para verificar sin una Consulta arroja resultados
	 * @param con
	 * @param consulta
	 * @return
	 */
	public boolean resultadosConsulta(Connection con, String consulta);
	
	/**
	 * Metodo que consulta el Tipo Monto y Valores Paciente
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap tipoMontoValoresPaciente(Connection con, int subCuenta);
	
	/**
	 * Método que carga el detalle de una solicitud
	 * @param con
	 * @param campos
	 * @return
	 */
	public HashMap<String, Object> cargarDetalleServicioArticuloSolicitud(Connection con,HashMap campos);
	
	/**
	 * M&eacute;todo que carga el detalle de la solicitud en el pop up 
	 * @param con
	 * @param campos
	 * @return
	 * @author Diana Ruiz
	 */
	public HashMap<String, Object> cargarDetalleServicioArticuloSolicitudPopUp(Connection con,HashMap campos);
	
	/**
	 * M&eacute;todo para verificar si para la solicitud hay despacho de equivalentes  
	 * @param con
	 * @param campos
	 * @return
	 * @author Diana Ruiz
	 */
	public boolean despachoEquivalentes(Connection con,HashMap campos);	
	
}