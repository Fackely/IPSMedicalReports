package com.princetonsa.dao.postgresql.facturacion;
import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.TrasladoSolicitudesPorTransplantesDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseTrasladoSolicitudesPorTransplantesDao;




/**
 * @author Jhony Alexander Duque A.
 * jduque@princetonsa.com
 */

public class PostgresqlTrasladoSolicitudesPorTransplantesDao implements TrasladoSolicitudesPorTransplantesDao
{

	/**
	 * Metodo encargado de consultar los Servicios o los articulos
	 * de las solicitudes de un paciente.
	 * @author Jhony Alexander Duque A.
	 * @param connection
	 * @param criterios
	 * ------------------------------
	 * KEY'S DEL MAPA  CRITERIOS
	 * ------------------------------
	 * -- fechaInicial1
	 * -- fechaFinal2
	 * -- servicio3
	 * -- articuloMed4
	 * -- cCostoEje5
	 * -- cCostoSol6
	 * -- ingreso15
	 * @return mapa
	 * -------------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * -------------------------------
	 * -- fechaSolicitud7_
	 * -- numeroOrden8_
	 * -- servArticulo9_
	 * -- estadoHisCli10_
	 * -- cenCosEje11_
	 * -- cenCosSol12_
	 * -- cantidad13_
	 * -- tiposolicitud14_
	 */
	public HashMap consultarServiciosArticulos (Connection connection, HashMap criterios)
	{
		return SqlBaseTrasladoSolicitudesPorTransplantesDao.consultarServiciosArticulos(connection, criterios);
	}
	
	/**
	 * Metodo encargado de consultar los pacientes receptores
	 * @param connection
	 * @param criterios
	 * ------------------------
	 * KEY'S DEL MAPA CRITERIOS
 	 * ------------------------
 	 * -- centroAtencion6
 	 * @return MAPA
 	 * ----------------------------------------
 	 * KEY'S DEL MAPA QUE RETORNA
 	 * ----------------------------------------
 	 * -- idingreso0_
 	 * -- consecutivo1_
 	 * -- fechaIngreso2_
 	 * -- viaIngreso3_
 	 * -- paciente4
 	 * -- identificacion5_
 	 */
	public HashMap consultarPacientesReceptores (Connection connection, HashMap criterios)
	{
		return SqlBaseTrasladoSolicitudesPorTransplantesDao.consultarPacientesReceptores(connection, criterios);
	}
	
	/**
	 * Metodo encargado de cambia el estado de actualizar
	 * el estado de la facturacion en la tabla det_cargos
	 * @param connection
	 * @param solicitud
	 * @param estado
	 * @return false/true
	 * 
	 */
	public boolean actualizarEstadoFacturacion (Connection connection, String solicitud, String estado,String codigoDetalleCargo)
	{
		return SqlBaseTrasladoSolicitudesPorTransplantesDao.actualizarEstadoFacturacion(connection, solicitud, estado,codigoDetalleCargo);
	}
	
	
	/**
	 * Metodo encargado de consultar si existe un translado
	 * @param connection
	 * @param datos
	 * --------------------------
	 * KEY'S DEL MAPA DATOS
	 * --------------------------
	 * -- donante0
	 * -- receptor1
	 * -- institucion2
	 * @return codigoTranslado (-1 si no existe)
	 */
	public int  existeTraslado (Connection connection, HashMap datos)
	{
		return SqlBaseTrasladoSolicitudesPorTransplantesDao.existeTraslado(connection, datos);
	}
	
	
	/**
	 * Metodo encargado insertar el detalle del traslado de solitides
	 * en la tabla ( det_tras_sol_transplante )
	 * @param connection
	 * @param datos
	 * ---------------------------
	 * KEYS' DEL MAPA DATOS
	 * ---------------------------
	 * -- idTraslado3
	 * -- solicitudTrasladada4
	 * -- solicitudGenerada5
	 * -- usuarioTraslado6
	 * @return false/true
	 */
	public boolean insertarDetTraslado (Connection connection, HashMap datos)
	{
		return SqlBaseTrasladoSolicitudesPorTransplantesDao.insertarDetTraslado(connection, datos);
	}
	
	
	/**
	 * Metodo encargado de insertar los datos del traslado en la tabla ( tras_sol_transplante )
	 * @param connection
	 * @param datos
	 * -----------------------
	 * KEY'S DEL MAPA DATOS
	 * -----------------------
	 * -- donante0
	 * -- receptor1
	 * -- institucion2
	 * @return false/true
	 */
	public int insertarEncabTraslado (Connection connection, HashMap datos)
	{
		return SqlBaseTrasladoSolicitudesPorTransplantesDao.insertarEncabTraslado(connection, datos);
	}
}