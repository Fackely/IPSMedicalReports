/*
 * @(#)GruposEtareosDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2004. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.2_04
 *
 */

package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 *	@version 1.0, 26 /May/ 2006
 */
public interface GruposEtareosDao 
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
	public HashMap consultarGruposEtareos (Connection con, String fechaIncial, String fechaFinal, int codigoConvenio, int institucion) throws SQLException;
	
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
	public int insertarGruposEtareos(Connection con, String codigo, int codigoConvenio, int institucion , String edadInicial, String edadFinal, int sexo, String fechaInicial, String fechaFinal, String valor, String porcentajePyP) throws SQLException;
	
	/**
	 * Método para eliminar un grupo etareo dado su codigo
	 * @param con
	 * @param codigo
	 * @return
	 * @throws SQLException
	 */
	public  int eliminarGrupoEtareo(Connection con, int codigo) throws SQLException;
	
	
}