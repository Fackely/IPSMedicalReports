/*
 * @(#)GrupoEtareoCrecimientoDesarrolloDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao.pyp;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 28 /Jul/ 2006
 */
public interface GrupoEtareoCrecimientoDesarrolloDao 
{
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
	public HashMap consultarGruposEtareos (Connection con, int institucion) throws SQLException;
	
	/**
	 * Método para eliminar un grupo etareo dado su codigo
	 * @param con
	 * @param codigoInterno
	 * @return
	 * @throws SQLException
	 */
	public boolean eliminarGrupoEtareo(Connection con, int codigoInterno) throws SQLException;
	
	/**
	 * Modifica un grupo etareo transaccional
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
	public boolean modificarGrupoEtareo (Connection con, int codigoInterno, int codigo, String descripcion, int unidadMedida, int rangoInicial, int rangoFinal, int codigoSexo, String activo) throws SQLException;
	
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
	public int insertarGrupoEtareo(Connection con, int codigo, String descripcion, int unidadMedida, int rangoInicial, int rangoFinal, int codigoSexo, String activo, int institucion) throws SQLException;
	
}