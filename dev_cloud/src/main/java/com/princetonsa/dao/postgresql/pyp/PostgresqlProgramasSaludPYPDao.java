/*
 * @author armando
 */
package com.princetonsa.dao.postgresql.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

import com.princetonsa.dao.pyp.ProgramasSaludPYPDao;
import com.princetonsa.dao.sqlbase.pyp.SqlBaseProgramasSaludPYPDao;

/**
 * 
 * @author armando
 *
 */
public class PostgresqlProgramasSaludPYPDao implements ProgramasSaludPYPDao 
{
	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap cargarInfomacionBD(Connection con, int institucion)
	{
		return SqlBaseProgramasSaludPYPDao.cargarInfomacionBD(con,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean eliminarRegistro(Connection con, String codigo, int institucion)
	{
		return SqlBaseProgramasSaludPYPDao.eliminarRegistro(con,codigo,institucion);
	}
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public ResultSetDecorator cargarPrograma(Connection con, String codigo, int institucion)
	{
		return SqlBaseProgramasSaludPYPDao.cargarPrograma(con,codigo,institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param tipoPrograma
	 * @param grupoEtareo
	 * @param acronimoDiag
	 * @param tipoCieDiag
	 * @param embarazo
	 * @param formato
	 * @param archivo
	 * @param activo
	 * @return
	 */
	public boolean modificarRegistro(Connection con, String codigo, int institucion, String descripcion, String tipoPrograma, String grupoEtareo,  boolean embarazo, String formato, String archivo, boolean activo)
	{
		return SqlBaseProgramasSaludPYPDao.modificarRegistro(con,codigo,institucion,descripcion,tipoPrograma,grupoEtareo,embarazo,formato,archivo,activo);
	}

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param descripcion
	 * @param tipoPrograma
	 * @param grupoEtareo
	 * @param acronimoDiag
	 * @param tipoCieDiag
	 * @param embarazo
	 * @param formato
	 * @param archivo
	 * @param activo
	 * @return
	 */
	public boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, String tipoPrograma, String grupoEtareo,  boolean embarazo, String formato, String archivo, boolean activo)
	{
		return SqlBaseProgramasSaludPYPDao.insertarRegistro(con,codigo,institucion,descripcion,tipoPrograma,grupoEtareo,embarazo,formato,archivo,activo);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public boolean guardarDiagnostico(Connection con, String codigo, int institucion, String acronimo, String cie)
	{
		return SqlBaseProgramasSaludPYPDao.guardarDiagnostico(con,codigo,institucion,acronimo,cie);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public boolean eliminarDiagnostico(Connection con, String codigo, int institucion, String acronimo, String cie)
	{
		return SqlBaseProgramasSaludPYPDao.eliminarDiagnostico(con,codigo,institucion,acronimo,cie);
	}
	

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public HashMap cargarDiagnosticosPrograma(Connection con, String codigo, int institucion)
	{
		return SqlBaseProgramasSaludPYPDao.cargarDiagnosticosPrograma(con,codigo,institucion);
	}
	
	
	/**
	 * Método para saber si puedo eliminar un programa
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public boolean puedoEliminarPrograma(Connection con,String codigo,int institucion)
	{
		return SqlBaseProgramasSaludPYPDao.puedoEliminarPrograma(con, codigo, institucion);
	}
}
