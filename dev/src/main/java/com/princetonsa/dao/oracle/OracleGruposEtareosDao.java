/*
 * @(#)OracleGruposEtareosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.GruposEtareosDao;
import com.princetonsa.dao.sqlbase.SqlBaseGruposEtareosDao;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 26 /May/ 2006
 */
public class OracleGruposEtareosDao implements GruposEtareosDao 
{
	
	/**
	 * Cadena con el statement necesario para insertar un grupo eatero
	 */
	private static final String insertarGrupoEtareoStr=" INSERT INTO grupos_etareos_x_convenio " +
													   " (codigo, " +
													   " convenio, " +
													   " institucion, " +
													   " edad_inicial, " +
													   " edad_final, " +
													   " sexo, " +
													   " fecha_inicial, " +
													   " fecha_final, " +
													   " valor, " +
													   " porcentaje_pyp) " +
													   " VALUES (seq_grupos_etareos_x_convenio.nextval, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	
	/**
	 * Método para consultar los grupos etareos en la base de datos
	 * segun unos parametros de busqueda
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoConvenio
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarGruposEtareos (Connection con, String fechaIncial, String fechaFinal, int codigoConvenio, int institucion) throws SQLException
	{
		return SqlBaseGruposEtareosDao.consultarGruposEtareos(con, fechaIncial, fechaFinal, codigoConvenio, institucion);
	}
	
	/**
	 * Método para insertar un grupo etareo determinado. Admas antes de insertarlo verifica si existe, en 
	 * caso de que exista lo que hace es modificarlo de lo contrario lo inserta
	 * @param con
	 * @param codigo
	 * @param codigoConvenio
	 * @param institucion
	 * @param edadInicial
	 * @param edadFinal
	 * @param sexo
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param valor
	 * @param porcentajePyP
	 * @return
	 * @throws SQLException
	 */
	public int insertarGruposEtareos(Connection con, String codigo, int codigoConvenio, int institucion , String edadInicial, String edadFinal, int sexo, String fechaInicial, String fechaFinal, String valor, String porcentajePyP) throws SQLException
	{
		return SqlBaseGruposEtareosDao.insertarGruposEtareos(con, codigo, codigoConvenio, institucion, edadInicial, edadFinal, sexo, fechaInicial, fechaFinal, valor, porcentajePyP, insertarGrupoEtareoStr);
	}
	
	/**
	 * Método para eliminar un grupo etareo dado su codigo
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public int eliminarGrupoEtareo(Connection con, int codigo) throws SQLException
	{
		return SqlBaseGruposEtareosDao.eliminarGrupoEtareo(con, codigo);
	}
}