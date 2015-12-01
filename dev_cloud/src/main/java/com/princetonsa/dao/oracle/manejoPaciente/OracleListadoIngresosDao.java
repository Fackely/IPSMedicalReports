package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosInt;

import com.princetonsa.dao.manejoPaciente.ListadoIngresosDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseListadoIngresosDao;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;

public class OracleListadoIngresosDao implements ListadoIngresosDao {
	
	
	/**
	 * Metodo encargado de consultar el listado de ingresos (cerrados, abiertos)
	 * y cuentas (no importa el estado) de un paciente. 
	 * @author Jhony Alexander Duque A.
	 * @param con
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------
	 * -- paciente --> Requerido
	 * -- institucion --> Requerido
	 * @return Mapa
	 * -----------------------------
	 * KEY'S DEL MAPA QUE RETORNA
	 * -----------------------------
	 * idIngreso0_,consecutivoIngreso1_,centroAtencion2_,
	 * nombreCentroAtencion3_,estadoIngreso4_,
	 * fechaAperturaIngreso5_,viaIngreso6_,nombreViaIngreso7_,
	 * fechaCierreIngreso8_,numeroCuenta9_,estadoCuenta10_,
	 * nombreEstadoCuenta11_
	 */
	public HashMap cargarListadoIngresos (Connection con, HashMap criterios)
	{
		return SqlBaseListadoIngresosDao.cargarListadoIngresos(con, criterios);
	}
	
	
	/**
	 * Metodo encargado de consultar los convenios de un ingreso en 
	 * la tabla sub_cuentas
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * --------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * --------------------------------
	 * --ingreso --> Requerido
	 * @return ArrayList<HashMap>
	 * ----------------------------------------------
	 * KEY'S DE LOS MAPAS DEL ARRAYLIST DE RESULTADO
	 * ----------------------------------------------
	 * -- codigo
	 * -- nombre
	 * -- contrato
	 */
	public ArrayList<HashMap<String, Object>> ObtenerConvenios (Connection connection,HashMap criterios)
	{
		return SqlBaseListadoIngresosDao.ObtenerConvenios(connection, criterios);
	}
	
	
	/**
	 * Metodo encargado de consultar los datos de servicios
	 * o articulos con el numero de autorizacion.
	 * @author Jhony Alexander Duque A.
	 * @param con 
	 * @param criterios
	 * ---------------------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * ---------------------------------------
	 * -- subCuenta --> Requerido
	 * -- solicitud --> Opcional
	 * -- articulo --> Opcional
	 * -- servicio --> Opcional
	 * -- fechaSolicitud --> Opcional
	 * -- centroCostoSolicita --> Opcional
	 * -- centroCostoEjecuta --> Opcional
	 * --numeroAutorizacion --> Opcional
	 * @return Mapa
	 * --------------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * --------------------------------------
	 * solicitud0_,consecutivoOrdenes1_,tipoSolicitud2_,
	 * nombreTipoSolicitud3_,estadoHistoriaClinica4_,
	 * nombreEstadoHistoriaClinica5_,centroCostoSolicita6_,
	 * nombreCentroCostoSolicita7_,centroCostoEjecuta8_,
	 * nombreCentroCostoEjecuta9_,porcentajeAutorizado10_,
	 * montoAutorizado11_,valorUnitario12_,valorTotal13_,
	 * estadoCargo14_,nombreEstadoCargo15_,fechaSolicitud16_,
	 * servArticulo17_,cantidad18_,requiereAutorizacion19_,
	 * numeroAutorizacion20_
	 */
	public HashMap cargarServiciosArticulos (Connection con, HashMap criterios)
	{
		return SqlBaseListadoIngresosDao.cargarServiciosArticulos(con, criterios);
	}

	/**
	 * Metodo encargado de actualizar el numero de autorizacion 
	 * de un servicio o articulo en la tabla det_cargos
	 * @author Jhony Alexander Duque.
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------------
	 * -- numeroAutorizacion --> Requerido
	 * -- codigoDetalleCargo --> Requerido
	 * @return true/false
	 */
	public boolean actualizarAutorizacionServicioArticulo (Connection connection, HashMap datos)
	{
		return SqlBaseListadoIngresosDao.actualizarAutorizacionServicioArticulo(connection, datos);
	}
	
	/**
	 * Metodo encargado de actualizar el numero de autorizacion 
	 * de la admision
	 * @author Jhony Alexander Duque.
	 * @param connection
	 * @param datos
	 * --------------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------------
	 * -- numeroAutorizacion --> Requerido
	 * -- subCuenta --> Requerido
	 * @return true/false
	 */
	public boolean actualizarAutorizacionAdmision (Connection connection, HashMap datos)
	{
		return SqlBaseListadoIngresosDao.actualizarAutorizacionAdmision(connection, datos);
	}
	
	/**
	 * Método encargado de consultar las vías de ingreso
	 * @param Conexion
	 * @param codInstitucion
	 */
	public HashMap consultarViaIngreso(Connection con, int codInstitucion) 
	{
		return SqlBaseListadoIngresosDao.consultarViaIngreso(con, codInstitucion);
	}
	
	/**
	 * Método encargado de Obtener Todos Los Convenios
	 * @param con
	 * @return HashMap
	 */
	public HashMap obtenerTodosLosConvenios(Connection con) 
	{
		return SqlBaseListadoIngresosDao.obtenerTodosLosConvenios(con);
	}
	
	/**
	 * Método encargado de Obtener Todos Los estados de solicitud
	 * @param con
	 * @return HashMap
	 */
	public HashMap obtenerEstadoSolicitud(Connection con) 
	{
		return SqlBaseListadoIngresosDao.obtenerEstadoSolicitud(con);
	}
	
	/**
	 * Método encargado de Obtener Todos Los pisos de una institución
	 * @param con
	 * @param codInstitucion
	 * @return HashMap
	 */
	public HashMap obtenerPisosXInstitucion(Connection con, int codInstitucion, String centroDeAtencion) 
	{
		return SqlBaseListadoIngresosDao.obtenerPisosXInstitucion(con, codInstitucion, centroDeAtencion);
	}
	
	/**
	 * Método encargado de consultar la actualizacion de autorizacion medica por rangos
	 * @param con
	 * @return HashMap
	 */
	public HashMap consultaXRangos(Connection con, HashMap criterios)
	{
		return SqlBaseListadoIngresosDao.consultaXRangos(con, criterios);
	}
	
	
	/**
	 * Método encargado de obtener la consulta para la actualizacion de autorizacion medica por rangos
	 * Para la generación del reporte
	 * @param con
	 * @param HashMap criterios
	 * @return String cadena
	 */
	public String obtenerConsulta(Connection con, HashMap criterios)
	{
		return SqlBaseListadoIngresosDao.obtenerConsulta(con, criterios);
	}
	
	/**
	 * Método encargado de obtener los centros de atención
	 * @param Coneection con
	 * @param int codInstitucion
	 * @return HashMap con los resultados de la obtención de los centros de atención
	 */
	public HashMap obtenerCentrosDeAtencion(Connection con, int codInstitucion)
	{
		return SqlBaseListadoIngresosDao.obtenerCentrosDeAtencion(con, codInstitucion);
	}
	
	/**
	 * Método encargado de consultar la actualizacion de autorizacion medica por rangos para la vista
	 * @param con
	 * @param HashMap criterios
	 * @return HashMap
	 */
	public HashMap consultaXRangosParaVista(Connection con, HashMap criterios)
	{
		return SqlBaseListadoIngresosDao.consultaXRangosParaVista(con, criterios);
	}
	
	/**
	 * Método para cargar la autorizacion de una orden ambulatoria o solicitud
	 * @param con
	 * @param parametros
	 * @return
	 */
	public DtoAutorizacion cargarAutorizacion(Connection con,HashMap parametros)
	{
		return SqlBaseListadoIngresosDao.cargarAutorizacion(con, parametros);
	}
	
	/**
	 * Método para obtener el origen de atencion verificando
	 * la informacion de la cuenta del paciente
	 * @param con
	 * @param idCuenta
	 * @param viaIngreso
	 * @param tipoEvento
	 * @return
	 */
	public InfoDatosInt obtenerOrigenAtencionXCuenta(Connection con,String idCuenta,int viaIngreso,String tipoEvento)
	{
		return SqlBaseListadoIngresosDao.obtenerOrigenAtencionXCuenta(con, idCuenta, viaIngreso, tipoEvento);
	}
}
