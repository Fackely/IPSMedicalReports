/*
 * @(#)OracleGrupoEtareoCrecimientoDesarrolloDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.oracle.pyp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import com.princetonsa.dao.pyp.GrupoEtareoCrecimientoDesarrolloDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseGrupoEtareoCrecimientoDesarrolloDao;


/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * @version 1.0, 28 /Jul/ 2006
 */
public class OracleGrupoEtareoCrecimientoDesarrolloDao implements GrupoEtareoCrecimientoDesarrolloDao 
{
	
	/**
	 * Cadena con el statement necesario para insertar un grupo eatero
	 */
	private static final String insertarGrupoEtareoStr = " INSERT INTO grup_etareo_creci_desa " +
													     " (codigo, " +
													     " consecutivo, " +
													     " descripcion, " +
													     " unidad_medida, " +
													     " rango_inicial, " +
													     " rango_final, " +
													     " sexo, " +
													     " activo, " +
													     " institucion) " +
													     " VALUES (seq_grup_etareo_creci_desa.nextval, ?, ?, ?, ?, ?, ?, ?, ?) ";
	
	
	/**
	 * Método para consultar los grupos etareos en la base de datos
	 * segun la institucion del usuario cargado en session
	 * @param con
	 * @param institucion
	 * @return
	 * @throws SQLException
	 */
	public HashMap consultarGruposEtareos (Connection con, int institucion) throws SQLException
	{
		return SqlBaseGrupoEtareoCrecimientoDesarrolloDao.consultarGruposEtareos(con, institucion);
	}
	
	/**
	 * Método para eliminar un grupo etareo dado su codigo
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public boolean eliminarGrupoEtareo(Connection con, int codigo) throws SQLException
	{
		return SqlBaseGrupoEtareoCrecimientoDesarrolloDao.eliminarGrupoEtareo(con, codigo);
	}
	
	/**
	 * Modifica un grupo etareo transaccional
	 * @param con
	 * @param codigoInterno
	 * @param codigo
	 * @param descripcion
	 * @param unidadMedida
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param codigoSexo
	 * @param activo
	 * @return
	 * @throws SQLException
	 */
	public boolean modificarGrupoEtareo (Connection con, int codigoInterno, int codigo, String descripcion, int unidadMedida, int rangoInicial, int rangoFinal, int codigoSexo, String activo) throws SQLException
	{
		return SqlBaseGrupoEtareoCrecimientoDesarrolloDao.modificarGrupoEtareo(con, codigoInterno, codigo, descripcion, unidadMedida, rangoInicial, rangoFinal, codigoSexo, activo );
	}
	
	/**
	 * Metodo que inserta los Grupos Etareos de Crecimiento y desarrollo
	 * @param con
	 * @param estado
	 * @param codigo
	 * @param descripcion
	 * @param unidadMedida
	 * @param rangoInicial
	 * @param rangoFinal
	 * @param codigoSexo
	 * @param activo
	 * @return
	 * @throws SQLException
	 */
	public int insertarGrupoEtareo(Connection con, int codigo, String descripcion, int unidadMedida, int rangoInicial, int rangoFinal, int codigoSexo, String activo, int institucion) throws SQLException
	{
		return SqlBaseGrupoEtareoCrecimientoDesarrolloDao.insertarGrupoEtareo(con, codigo, descripcion, unidadMedida, rangoInicial, rangoFinal, codigoSexo, activo, institucion, insertarGrupoEtareoStr);
	}
}