/**
 * Juan David Ram�rez 31/05/2006
 * Princeton S.A.
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ArticulosXMezclaDao;

/**
 * @author Juan David Ram�rez
 *
 */
public class ArticulosXMezcla
{
	/**
	 * Interfaz con la BD
	 */
	ArticulosXMezclaDao articulosXMezclaDao;
	
	/**
	 * Inicializador de la interfaz con la BD
	 *
	 */
	private void init()
	{
		articulosXMezclaDao=DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticulosXMezclaDao();
	}
	
	/**
	 * Constructor de la clase
	 *
	 */
	public ArticulosXMezcla()
	{
		this.init();
	}

	/**
	 * M�todo para consultar lkas opciones de los selectores de la funcionalidad
	 * @param con
	 * @param codigoInstitucion
	 * @param tipoConsulta
	 * @return
	 */
	public Collection consultarTipos(Connection con, int codigoInstitucion, int tipoConsulta)
	{
		return articulosXMezclaDao.consultarTipos(con, codigoInstitucion, tipoConsulta);
	}
	
	/**
	 * M�todo para ingresar o modificar los datos de
	 * los art�culos por mezcla
	 * @param con
	 * @param articulos
	 * @param mezcla
	 * @return numero de registros modificados 
	 */
	public int ingresarModificar(Connection con, Vector articulosIngresados, Vector articulosEliminados, int mezcla)
	{
		return articulosXMezclaDao.ingresarModificar(con, articulosIngresados, articulosEliminados, mezcla);
	}
	
	/**
	 * M�todo para ingresar o modificar los datos de
	 * los art�culos por mezcla
	 * @param con
	 * @param codigoMezcla @todo
	 * @param articulos
	 * @param mezcla
	 * @return numero de registros modificados 
	 */
	public HashMap consultar(Connection con, int codigoInstitucion, int codigoMezcla)
	{
		/*
		 * Aqui paso los registros a entero
		 */
		HashMap mapaArticulos=articulosXMezclaDao.consultar(con, codigoInstitucion, codigoMezcla); 
		int numRegistros=Integer.parseInt(mapaArticulos.get("numRegistros").toString());
		mapaArticulos.put("numRegistros", numRegistros);
		return mapaArticulos;
	}

}
