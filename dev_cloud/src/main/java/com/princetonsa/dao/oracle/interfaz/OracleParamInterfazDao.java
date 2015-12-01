/*
 * Creado el 27/04/2006
 * Juan David Ramírez López
 */
package com.princetonsa.dao.oracle.interfaz;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import util.InfoDatosInt;

import com.princetonsa.dao.interfaz.ParamInterfazDao;
import com.princetonsa.dao.sqlbase.interfaz.SqlBaseParamInterfazDao;

public class OracleParamInterfazDao implements ParamInterfazDao
{
	/**
	 * Método que consulta los campos parametrizados en la funcionalidad Parametrización Campos Interfaz
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarCamposInterfaz(Connection con, int institucion)
	{
		return SqlBaseParamInterfazDao.consultarCamposInterfaz(con, institucion); 
	}

	/**
	 * Método para consultar las opciones de los selectores de la funcionalidad
	 * @param con
	 * @param numeroConsulta
	 * @return
	 */
	public Collection consultarTipos(Connection con, int numeroConsulta)
	{
		return SqlBaseParamInterfazDao.consultarTipos(con, numeroConsulta);
	}

	/**
	 * Método para ingresar la parametrización del registro de interfaz
	 * @param con
	 * @param tipoInterfaz
	 * @param descripcion
	 * @param login
	 * @return
	 */
	public int ingresarRegistroInterfaz(Connection con, int tipoInterfaz, String descripcion, String login, int tipoRegistro, int codigoModificacion)
	{
		return SqlBaseParamInterfazDao.ingresarRegistroInterfaz(con, tipoInterfaz, descripcion, login, tipoRegistro, codigoModificacion);
	}

	/**
	 * Método que ingresa el detalle del registro de interfaz
	 * @param con
	 * @param codigoRegistroInterfaz
	 * @param campoRegistroInterfaz
	 * @param nombre
	 * @param valor
	 * @param valorSecundario
	 * @param indicativoExiste
	 * @param tamanio
	 * @return
	 */
	public int ingresarDetalleRegistroInterfaz(Connection con, int codigoRegistroInterfaz, int campoRegistroInterfaz, String nombre, InfoDatosInt valor, InfoDatosInt valorSecundario, InfoDatosInt indicativoExiste, InfoDatosInt tamanio)
	{
		return SqlBaseParamInterfazDao.ingresarDetalleRegistroInterfaz(con, codigoRegistroInterfaz, campoRegistroInterfaz, nombre, valor, valorSecundario, indicativoExiste, tamanio);
	}
	
	/**
	 * Método para consultar la parametrización del registro de interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap consultarRegistroInterfaz(Connection con, int codigoRegistro)
	{
		return SqlBaseParamInterfazDao.consultarRegistroInterfaz(con, codigoRegistro);
	}
	
	/**
	 * Método para consultar el detalle del registro de interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap consultarDetalleRegistroInterfaz(Connection con, int codigoRegistro)
	{
		return SqlBaseParamInterfazDao.consultarDetalleRegistroInterfaz(con, codigoRegistro);
	}

	/**
	 * Método para consultar los valores de un detalle especifico de registro de interfaz
	 * @param con
	 * @param codigoDetalle
	 * @return
	 */
	public HashMap consultarValoresRegistroInterfaz(Connection con, int codigoDetalle)
	{
		return SqlBaseParamInterfazDao.consultarValoresRegistroInterfaz(con, codigoDetalle);
	}

	/**
	 * Método que lista los registros de parametrización de interfaz 
	 * @param con
	 * @param codigoInstitucion
	 * @param tipoINterfaz
	 * @param esOrdenado
	 * @return
	 */
	public Collection listadoRegistrosInterfaz(Connection con, int codigoInstitucion, int tipoInterfaz,boolean esOrdenado)
	{
		return SqlBaseParamInterfazDao.listadoRegistrosInterfaz(con, codigoInstitucion, tipoInterfaz,esOrdenado);
	}

}
