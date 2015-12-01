/*
 * @(#)EsquemaTarifarioDao.java
 * 
 * Created on 03-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import util.ResultadoBoolean;
import util.ResultadoCollectionDB;

import com.princetonsa.mundo.cargos.EsquemaTarifario;
import com.servinte.axioma.fwk.exception.BDException;

/**
 *  Interfaz para el acceder a la fuente de datos de un esquema tarifario 
 * 
 * @version 1.0, Mayo 03 / 2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public interface EsquemaTarifarioDao
{
	/**
	 * Inserta un esquema tarifario
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param nombre. String, nombre del esquema tarifario
	 * @param codigoTarifarioOficial. int, código del tarifario oficial (tipo de manual) asociado
	 * 				al esquema tarifario
	 * @param acronimoMetodoAjuste. String, método de ajuste asociado al esquema tarifario
	 * @param esInventario. boolean, si el esquema es de inventarios o de servicios
	 * @param codigoInstitucion Código de la institución para el que se va a insertar este
	 * esquema tarifario
	 * @param activo Boolean que me indica si el esquema va a quedar activo o no
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
															float cantidad) throws SQLException;

	/**
	 * Consulta los datos de un esquema tarifario dado su código
	 * @param con, Connection, conexión abierta con la fuente de datos
	 * @param codigo. int, código del esquema tarifario
	 * @return ResultadoCollectionDB, true y con la colección de HashMap con el resultado si 
	 * fue exitosa la consulta, false y con la descripción de lo contrario
	 */
	public ResultadoCollectionDB consultar(	Connection con,
																		int codigo, int institucion	) throws SQLException, BDException;
	
	/**
	 * Método que modifica un esquema tarifario
	 * 
	 * @param con Conexión con la fuente de datos
	 * @param codigo Código del esquema tarifario a modificar
	 * @param nombre Nombre del esquema tarifario a modificar
	 * @param acronimoMetodoAjuste Acrónimo del método de 
	 * ajuste del esquema tarifario a modificar
	 * @param activo Me dice si este esquema tarifario
	 * va a quedar activo o no
	 * @return
	 * @throws SQLException
	 */														
	public ResultadoBoolean modificar(	Connection con,
																int codigo,
																String nombre,
																String acronimoMetodoAjuste,
																float cantidad,
																boolean activo
																) throws SQLException;

	
	public Collection busqueda(Connection con, int codigo, String nombre, int tarifarioOficial, String metodoAjuste, boolean esInventario, int institucion, char inventarioAux);
	
	/**
	 * metodo q obtiene el tarifario oficial
	 * @param con
	 * @param codigoEsquemaTarifario
	 * @return
	 */
	public int obtenerTarifarioOficialXCodigoEsquemaTar(Connection con, int codigoEsquemaTarifario) throws BDException;
	
	
	/**
	 * Método para obtener el esquema tarifario que aplica segun el contrato de la entidad subcontratada
	 * @param con
	 * @param codigoContrato
	 * @param servArt
	 * @param fechaCalculoVigencia
	 * @param esServicio
	 * @return
	 */
	public EsquemaTarifario obtenerEsquemaTarifarioServicioArticuloEntidadSub(Connection con,String codigoContrato,int servArt,String fechaCalculoVigencia,boolean esServicio);
}
