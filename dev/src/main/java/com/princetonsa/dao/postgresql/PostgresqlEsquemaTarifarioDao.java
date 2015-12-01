/*
 * @(#)PostgresqlEsquemaTarifarioDao.java
 * 
 * Created on 03-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

import com.princetonsa.dao.EsquemaTarifarioDao;
import com.princetonsa.dao.sqlbase.SqlBaseEsquemaTarifarioDao;
import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.servinte.axioma.fwk.exception.BDException;

/**
 * Implementación postgresql de las funciones de acceso a la fuente de datos
 * para un esquema tarifario
 *
 * @version 1.0, Abril 29 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class PostgresqlEsquemaTarifarioDao implements EsquemaTarifarioDao
{

	/**
	 * Cadena constante con el <i>statement</i> necesario para insertar 
	 * una nueva entrada en la esquemas_tarifarios para una BD Postgresql
	*/
	private static final String insertarEsquemaTarifarioStr="INSERT INTO esquemas_tarifarios (codigo, nombre, tarifario_oficial, metodo_ajuste, es_inventario, institucion, activo, cantidad) VALUES (nextval('seq_esquemas_tarifarios'), ?, ?, ?, ?, ?, ?, ?)";

	/**
	 * Inserta un esquema tarifario
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param nombre. String, nombre del esquema tarifario
	 * @param codigoTarifarioOficial. int, código del tarifario oficial (tipo de manual) asociado
	 * 				al esquema tarifario
	 * @param acronimoMetodoAjuste. String, método de ajuste   14 asociado al esquema tarifario
	 * @param esInventario. boolean, si el esquema es de inventarios o de servicios
	 * @return ResultadoBoolean, true si la inserción fue exitosa, false y con la descripción 
	 * de lo contrario
	 */
	public ResultadoBoolean insertar(	Connection con,
			String nombre,
			int codigoTarifarioOficial,
			String acronimoMetodoAjuste,
			boolean esInventario,
			String codigoInstitucion,
			boolean activo,
			float cantidad) throws SQLException
	{

		return SqlBaseEsquemaTarifarioDao.insertar(	con,
				nombre,
				codigoTarifarioOficial,
				acronimoMetodoAjuste,
				esInventario,
				codigoInstitucion,
				activo,
				cantidad,
				insertarEsquemaTarifarioStr) ;
		
	}

	/**
	 * Consulta los datos de un esquema tarifario dado su código
	 * @param con, Connection, conexión abierta con la fuente de da0tos
	 * @param codigo. int, código del esquema tarifario  
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si 
	 * fue exitosa la consulta, false y con la descripción de lo contrario
	 * @see com.princetonsa.dao.EsquemaTarifarioDao#consultar(java.sql.Connection, int)
	 */
	public ResultadoCollectionDB consultar(Connection con, int codigo, int institucion) throws SQLException, BDException
	{
		return SqlBaseEsquemaTarifarioDao.consultar(con, codigo, institucion);
	}

	/**
	 * Método que implementa la modificación de un esquema tarifario
	 * en una BD Postgresql
	 * 
	 * @see com.princetonsa.dao.EsquemaTarifarioDao#modificar(	Connection , int , String , String )
	 */
	public ResultadoBoolean modificar(	Connection con,
																int codigo,
																String nombre,
																String acronimoMetodoAjuste,
																float cantidad,
																boolean activo) throws SQLException
	{
	    
		return SqlBaseEsquemaTarifarioDao.modificar(	con, codigo, nombre, acronimoMetodoAjuste, cantidad, activo);
	}

	public Collection busqueda(Connection con, int codigo, String nombre, int tarifarioOficial, String metodoAjuste, boolean esInventario, int institucion, char inventarioAux)
	{
		return SqlBaseEsquemaTarifarioDao.busqueda(con, codigo, nombre, tarifarioOficial, metodoAjuste, esInventario, institucion, inventarioAux);
	}
	
	/**
	 * metodo q obtiene el tarifario oficial
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public int obtenerTarifarioOficialXCodigoEsquemaTar(Connection con, int codigoEsquemaTarifario) throws BDException
	{
		return SqlBaseEsquemaTarifarioDao.obtenerTarifarioOficialXCodigoEsquemaTar(con, codigoEsquemaTarifario);
	}
	
	/**
	 * Método para obtener el esquema tarifario que aplica segun el contrato de la entidad subcontratada
	 * @param con
	 * @param codigoContrato
	 * @param servArt
	 * @param fechaCalculoVigencia
	 * @param esServicio
	 * @return
	 */
	public EsquemaTarifario obtenerEsquemaTarifarioServicioArticuloEntidadSub(Connection con,String codigoContrato,int servArt,String fechaCalculoVigencia,boolean esServicio)
	{
		return SqlBaseEsquemaTarifarioDao.obtenerEsquemaTarifarioServicioArticuloEntidadSub(con, codigoContrato, servArt, fechaCalculoVigencia, esServicio);
	}
}
