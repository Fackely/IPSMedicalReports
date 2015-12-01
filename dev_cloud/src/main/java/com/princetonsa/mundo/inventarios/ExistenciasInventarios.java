/*
 * Creado el 3/01/2006
 * Jorge Armando Osorio Velasquez
 */
package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ExistenciasInventariosDao;

public class ExistenciasInventarios
{
	
	 /**
	 * Manejador de logs de la clase
	 */
	private Logger logger=Logger.getLogger(ExistenciasInventarios.class);
	
	/**
	 * 
	 */
	public static ExistenciasInventariosDao existenciasDao;
	
	/**
	 * Constructor
	 *
	 */
	public ExistenciasInventarios()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	*/
	public boolean init(String tipoBD)
	{
		if ( existenciasDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			existenciasDao= myFactory.getExistenciasInventariosDao();
			if( existenciasDao!= null )
				return true;
		}
			return false;
	}
	
	/**
	 * metodo que resetea los atributos.
	 *
	 */
	private void reset()
	{
	}

	/**
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen
	 * @param con
	 * @param codAlmacen
	 * @return
	 */
	public HashMap consultarArticulosAlmacen(Connection con, int codAlmacen, String mostrarExt, int institucion)
	{
		return existenciasDao.consultarArticulosAlmacen(con,codAlmacen,mostrarExt, institucion);
	}

	
	/**
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen,clase,grupo,subgrupo
	 * En caso de que clase,grupo o subgrupo sean igual a 0 no los toma en el filtro de la consulta
	 * @param con
	 * @param codAlmacen
	 * @return
	 */
	public HashMap consultarArticulosAlmacenClaseGrupoSubgrupo(
			Connection con, 
			int codAlmacen, 
			String clase, 
			String grupo, 
			String subgrupo, 
			String mostrarExt,
			int institucion)
	{
		return existenciasDao.consultarArticulosAlmacenClaseGrupoSubgrupo(con,codAlmacen,clase,grupo,subgrupo,mostrarExt,institucion);
	}

	/***
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen y codigo del articulo
	 * @param con
	 * @param codAlmacen
	 * @param codArticulo
	 * @param institucion
	 * @return
	 */
	public HashMap consultarArticulosAlmacenCodArticulo(Connection con, int codAlmacen, String codArticulo, String mostrarExt, int institucion)
	{
		return existenciasDao.consultarArticulosAlmacenCodArticulo(con, codAlmacen, codArticulo, mostrarExt, institucion);
	}

	/***
	 * Metodo que realiza la consulta de las existencias de un articulo filtrando por almacen y descripcion del articulo
	 * @param con
	 * @param codAlmacen
	 * @param codArticulo
	 * @return
	 */
	public HashMap consultarArticulosAlmacenDescArticulo(Connection con, int codAlmacen, String descArticulo, String mostrarExt, int institucion)
	{
		return existenciasDao.consultarArticulosAlmacenDescArticulo(con,codAlmacen,descArticulo, mostrarExt, institucion);
	}

}
