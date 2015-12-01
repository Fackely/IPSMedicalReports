/*
 * Creado el Jun 1, 2006
 * por Julian Montoya
 */
package com.princetonsa.dao.oracle.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.dao.historiaClinica.SignosSintomasDao;
import com.princetonsa.dao.sqlbase.historiaClinica.SqlBaseSignosSintomasDao;

public class OracleSignosSintomasDao implements SignosSintomasDao {

	/**
	 * Metodo para cargar la informacion de los signos y sintomas.
	 * @param con
	 */
	public HashMap cargarSignosSintomas(Connection con, int institucion)
	{
		return SqlBaseSignosSintomasDao.cargarSignosSintomas(con, institucion);
	}

	/**
	 * Metodo para insertar datos de los signos y sintomas. 
	 * @param con
	 * @param codigo
	 * @param descripcion
	 * @return
	 */
	public int insertar(Connection con, int consecutivo, String codigo, String descripcion,int codigoInstitucion)
	{
		int sec = UtilidadBD.obtenerSiguienteValorSecuencia(con,"historiaclinica.seq_signos_sintomas");
		 
		return SqlBaseSignosSintomasDao.insertar(con, consecutivo,  sec+"", codigo, descripcion, codigoInstitucion);
	}

	/**
	 * Metodo para eliminar un sintoma o diagnostico.
	 * @param con
	 * @param nroRegEliminar
	 * @return
	 */
	public int eliminar(Connection con, int nroRegEliminar)
	{
		return SqlBaseSignosSintomasDao.eliminar(con, nroRegEliminar);
	}

	
}
