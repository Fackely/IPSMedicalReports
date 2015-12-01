/*
 * Created on Apr 6, 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
/**
 * @author Administrator
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public interface MantenimientoTablasDao {
	
	public  HashMap getDatosTabla(Connection con, String consulta)throws SQLException;

	public  int updateDatosTabla(Connection con, String consulta)throws SQLException;
	
	public  int buscarDatosTabla(Connection con, String consulta)throws SQLException;

	public boolean existeEspecialidad(Connection con, String nombreEspecialidad);

	public boolean existeOcupacionMedica(Connection con, String nombreOcupacion);

	public boolean existeInterconsultaPermitida(Connection con,	String ocupacion, String especialidad);

	public boolean existeExcepcionAgenda(Connection con, String fecha,String centroAtencion);
}