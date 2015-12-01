/*
 * Creado el 27/04/2006
 * Juan David Ram�rez L�pez
 */
package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import util.InfoDatosInt;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.ParamInterfazDao;

public class ParamInterfaz
{
	/**
	 * Acceso a la BD
	 */
	ParamInterfazDao pamarInterfazDao;
	
	public ParamInterfaz()
	{
		pamarInterfazDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getParamInterfazDao();
	}
	
	/**
	 * M�todo que consulta los campos parametrizados en la funcionalidad Parametrizaci�n Campos Interfaz
	 * @param con
	 * @param institucion
	 * @return
	 */
	public HashMap consultarCamposInterfaz(Connection con, int institucion)
	{
		 return pamarInterfazDao.consultarCamposInterfaz(con, institucion);
	}

	/**
	 * M�todo para consultar las opciones de los selectores de la funcionalidad
	 * @param con
	 * @param numeroConsulta
	 * @return
	 */
	public Collection consultarTipos(Connection con, int numeroConsulta)
	{
		 return pamarInterfazDao.consultarTipos(con, numeroConsulta);
	}
	
	/**
	 * M�todo para ingresar la parametrizaci�n del registro de interfaz
	 * @param con
	 * @param tipoInterfaz
	 * @param descripcion
	 * @param login
	 * @param tipoRegistro
	 * @param codigoModificacion @todo
	 * @return
	 */
	public int ingresarRegistroInterfaz(Connection con, int tipoInterfaz, String descripcion, String login, int tipoRegistro, int codigoModificacion)
	{
		return pamarInterfazDao.ingresarRegistroInterfaz(con, tipoInterfaz, descripcion, login, tipoRegistro, codigoModificacion);
	}

	/**
	 * M�todo que ingresa el detalle del registro de interfaz
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
		return pamarInterfazDao.ingresarDetalleRegistroInterfaz(con, codigoRegistroInterfaz, campoRegistroInterfaz, nombre, valor, valorSecundario, indicativoExiste, tamanio);
	}

	/**
	 * M�todo para consultar la parametrizaci�n del registro de interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap consultarRegistroInterfaz(Connection con, int codigoRegistro)
	{
		return pamarInterfazDao.consultarRegistroInterfaz(con, codigoRegistro);
	}

	/**
	 * M�todo para consultar el detalle del registro de interfaz
	 * @param con
	 * @param codigoRegistro
	 * @return
	 */
	public HashMap consultarDetalleRegistroInterfaz(Connection con, int codigoRegistro)
	{
		return pamarInterfazDao.consultarDetalleRegistroInterfaz(con, codigoRegistro);
	}
	
	/**
	 * M�todo para consultar los valores de un detalle especifico de registro de interfaz
	 * @param con
	 * @param codigoDetalle
	 * @return
	 */
	public HashMap consultarValoresRegistroInterfaz(Connection con, int codigoDetalle)
	{
		return pamarInterfazDao.consultarValoresRegistroInterfaz(con, codigoDetalle);
	}
	
	/**
	 * M�todo que lista los registros de parametrizaci�n de interfaz 
	 * @param con
	 * @param codigoInstitucion
	 * @param tipoInterfaz 
	 * @param esOrdenado
	 * @return
	 */
	public Collection listadoRegistrosInterfaz(Connection con, int codigoInstitucion, int tipoInterfaz, boolean esOrdenado)
	{
		return pamarInterfazDao.listadoRegistrosInterfaz(con, codigoInstitucion, tipoInterfaz, esOrdenado);
	}

}
