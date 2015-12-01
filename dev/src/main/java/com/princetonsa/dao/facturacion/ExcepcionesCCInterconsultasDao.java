package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

public interface ExcepcionesCCInterconsultasDao{
	
	/**
	 * Método para consultar las excepciones CC de Interconsultas
	 * @param con
	 * @return centroAtencion
	 */

	public HashMap consultarXCentroAtencion(Connection con,String centroAtencion);
	
	public boolean guardarExcepcion(Connection con, HashMap listadoXCentroAtencion, String login, String institucion, String centroAtencion);
	
	public boolean eliminarExcepcion(Connection con, int indice);
	
	public HashMap obtenerMedicos(Connection con,int institucion);
	
	public boolean modificarExcepcion(Connection con, HashMap listadoXCentroAtencion, String login, String institucion, String centroAtencion);
	
	public HashMap obtenerCentrosCosto(Connection con, String centroAtencion, int tipoAreaDirecto, int institucion);
	
	public HashMap obtenerCentrosCostoEjecutan(Connection con, String centroAtencion, int tipoAreaDirecto, int institucion);
}