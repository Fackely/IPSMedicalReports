/*
 * PostgresqlCoberturaAccidentesTransitoDao.java 
 * Autor			:  mdiaz
 * Creado el	:  25-nov-2004
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.CoberturaAccidentesTransitoDao;
import com.princetonsa.dao.sqlbase.SqlBaseCoberturaAccidentesTransitoDao;



/**
 * Implementación del Dao de cobertura de accidentes de transito para PostgresQL
 *
 * @version 1.0, 25-nov-2004
 * @author <a href="mailto:miguel@PrincetonSA.com">Miguel Arturo Diaz</a>
 */
public class PostgresqlCoberturaAccidentesTransitoDao implements CoberturaAccidentesTransitoDao
{
	/**
	 * Método para insertar responsables en la BD
	 * @param con
	 * @param codInstitucion
	 * @param cobertura
	 * @return numero de registrs insertados
	 */
	public int insertar(Connection con, int codInstitucion, double[] cobertura)
	{
		return SqlBaseCoberturaAccidentesTransitoDao.insertar(con, codInstitucion, cobertura);
	}
	
	/**
	 * Método para eliminar la cobertura de accidentes de transito
	 * @param con
	 * @param codInstitucion
	 * @return numero de elementos eliminados
	 */
	public int eliminar(Connection con, int codInstitucion)
	{
		return SqlBaseCoberturaAccidentesTransitoDao.eliminar(con, codInstitucion);
	}
	
	/**
	 * Método para listar la cobertura de accidentes para una institución dada
	 * @param con
	 * @param insitucion
	 * @return Mapa con los datos de la cobertura para la institución dada
	 */
	public HashMap listar(Connection con, int institucion)
	{
		return SqlBaseCoberturaAccidentesTransitoDao.listar(con, institucion);
	}
}
