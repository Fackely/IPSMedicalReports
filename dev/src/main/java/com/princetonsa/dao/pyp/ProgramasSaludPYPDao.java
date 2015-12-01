/*
 * @author armando
 */
package com.princetonsa.dao.pyp;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.util.HashMap;

/**
 * 
 * @author armando
 *
 */
public interface ProgramasSaludPYPDao 
{

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public abstract HashMap cargarInfomacionBD(Connection con, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public abstract boolean eliminarRegistro(Connection con, String codigo, int institucion);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public abstract ResultSetDecorator cargarPrograma(Connection con, String codigo, int institucion);

	
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
	 * @param b
	 * @param formato
	 * @param archivo
	 * @param activo
	 * @return
	 */
	public abstract boolean modificarRegistro(Connection con, String codigo, int institucion, String descripcion, String tipoPrograma, String grupoEtareo, boolean embarazo, String formato, String archivo, boolean activo);

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
	public abstract boolean insertarRegistro(Connection con, String codigo, int institucion, String descripcion, String tipoPrograma, String grupoEtareo, boolean embarazo, String formato, String archivo, boolean activo);

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public abstract boolean guardarDiagnostico(Connection con, String codigo, int institucion, String acronimo, String cie);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @param acronimo
	 * @param cie
	 * @return
	 */
	public abstract boolean eliminarDiagnostico(Connection con, String codigo, int institucion, String acronimo, String cie);

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public abstract HashMap cargarDiagnosticosPrograma(Connection con, String codigo, int institucion);
	
	/**
	 * Método para saber si puedo eliminar un programa
	 * @param con
	 * @param codigo
	 * @param institucion
	 * @return
	 */
	public abstract boolean puedoEliminarPrograma(Connection con,String codigo,int institucion);

}
