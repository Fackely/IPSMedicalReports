package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.action.ActionErrors;

import util.InfoDatosInt;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacion;

public interface ListadoIngresosDao {
	
	
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
	public HashMap cargarListadoIngresos (Connection con, HashMap criterios);
	
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
	public ArrayList<HashMap<String, Object>> ObtenerConvenios (Connection connection,HashMap criterios);
	
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
	public  HashMap cargarServiciosArticulos (Connection con, HashMap criterios);
	
	
	
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
	public  boolean actualizarAutorizacionServicioArticulo (Connection connection, HashMap datos);
	
	
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
	public boolean actualizarAutorizacionAdmision (Connection connection, HashMap datos);
	
	/**
	 * M?todo encargando de consultar las vias de ingreso
	 * @param con
	 * @param codInstitucion
	 * @return
	 */
	public HashMap consultarViaIngreso(Connection con, int codInstitucion);
	
	/**
	 * M?todo encargado de Obtener Todos Los Convenios
	 * @param con
	 * @return
	 */
	public HashMap obtenerTodosLosConvenios(Connection con);
	
	/**
	 * M?todo encargado de Obtener Todos Los Estados de Solicitud
	 * @param con
	 * @return
	 */
	public HashMap obtenerEstadoSolicitud(Connection con);
	
	/**
	 * M?todo encargado de Obtener Todos Los pisos de una instituci?n
	 * @param con
	 * @param codInstitucion
	 * @param String centroDeAtencion
	 * @return HashMap
	 */
	public HashMap obtenerPisosXInstitucion(Connection con, int codInstitucion, String centroDeAtencion);
	
	/**
	 * M?todo encargado de consultar la actualizacion de autorizacion medica por rangos
	 * @param con
	 * @param HashMap criterios
	 * @return HashMap
	 */
	public HashMap consultaXRangos(Connection con, HashMap criterios);
	
	/**
	 * M?todo encargado de obtener la consulta para la actualizacion de autorizacion medica por rangos
	 * Parta la generaci?n del reporte
	 * @param con
	 * @param HashMap criterios
	 * @return String consulta
	 */
	public String obtenerConsulta(Connection con, HashMap criterios);
	
	/**
	 * M?todo encargado de obtener los centros de atenci?n
	 * @param Coneection con
	 * @param int codInstitucion Me define el filtro
	 * @return HashMap con los resultados de la obtenci?n de los centros de atenci?n
	 */
	public HashMap obtenerCentrosDeAtencion(Connection con, int codInstitucion);
	
	/**
	 * M?todo encargado de consultar la actualizacion de autorizacion medica por rangos para la vista
	 * @param con
	 * @param HashMap criterios
	 * @return HashMap
	 */
	public HashMap consultaXRangosParaVista(Connection con, HashMap criterios);
	
	/**
	 * M?todo para cargar la autorizacion de una orden ambulatoria o solicitud
	 * @param con
	 * @param parametros
	 * @return
	 */
	public DtoAutorizacion cargarAutorizacion(Connection con,HashMap parametros);
	
	/**
	 * M?todo para obtener el origen de atencion verificando
	 * la informacion de la cuenta del paciente
	 * @param con
	 * @param idCuenta
	 * @param viaIngreso
	 * @param tipoEvento
	 * @return
	 */
	public InfoDatosInt obtenerOrigenAtencionXCuenta(Connection con,String idCuenta,int viaIngreso,String tipoEvento);
	
}
