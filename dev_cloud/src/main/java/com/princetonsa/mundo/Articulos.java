package com.princetonsa.mundo;

import java.sql.Connection;
import java.util.Collection;
import com.princetonsa.dao.ArticulosDao;
import com.princetonsa.dao.DaoFactory;

/**
 * @author rcancino
 *
 */
public class Articulos
{
	/**
	 * Comunicacion con la BD
	 */
	private ArticulosDao articulosDao;
	
	/**
	 * Constructor Vacío
	 */
	public Articulos()
	{
		this.init(System.getProperty("TIPOBD"));
	}
	
	/**
	 * Obtener el Daofactory
	 * @param tipoBD
	 */
	public void init (String tipoBD) 
	{
		if (articulosDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			articulosDao = myFactory.getArticulosDao();
		}
	}

	/**
	 * Listar los articulos
	 * @param con
	 * @return
	 */
	public Collection listarArticulos(Connection con)
	{		
		return articulosDao.listarArticulos(con);
	}
	
	/**
	 * @param criteriosBusqueda
	 */
	public Collection realizarBusqueda(Connection con,String[] criteriosBusqueda, Articulo articulo)
	{
		return articulosDao.buscarArticulos(con,criteriosBusqueda,articulo.getClase(),articulo.getGrupo(),articulo.getSubgrupo(),articulo.getCodigo(),articulo.getDescripcion(),articulo.getNaturaleza(),articulo.getMinsalud(),articulo.getFormaFarmaceutica(),articulo.getUnidadMedida(),articulo.getConcentracion(), articulo.getEstadoArticulo());
	}
}
