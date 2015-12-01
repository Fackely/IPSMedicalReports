package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.NaturalezaArticulosDao;


public class NaturalezaArticulos {
	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	
	private NaturalezaArticulosDao objetoDao;
	
	/**
	 * Mapa de Naturaleza de Articulos
	 */
	private HashMap naturalezaArticulosMap;
	
	
	/**
	 *  Constructor de la clase
	 */
	
	public NaturalezaArticulos() 
	{
		init(System.getProperty("TIPOBD"));
		this.reset();
	}
	
	/**
	 * Inicializa el acceso a la base de datos de este objeto, obteniendo su respectivo DAO.
	 * param tipoBD el tipo de bases de datos que va a usar este objeto.
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores validos para tipoBD.
	 * son los nombres y constantes definidos en <code>DaoFactory</code>
	 * @return <b>true</b> si la inicializacion fue exitosa, <code>false</code> si no.
	 */
	
	private boolean init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getNaturalezaArticulosDao();
			if(objetoDao!=null)
				return true;
		}
		return false;
			
		
	}
	
	/**
	 * resetea los atributos del objeto.
	 *
	 */
	
	private void reset() 
	{
		naturalezaArticulosMap=new HashMap();
    	naturalezaArticulosMap.put("numRegistros","0");
   	}
	
	
	/**
	 * COnsultar naturaleza de articulo especifico
	 * */
	public HashMap consultarNaturalezaArticulosEspecifico(Connection con,String acronimo)
	{
		HashMap vo=new HashMap();
		vo.put("acronimo", acronimo);
		return (HashMap)objetoDao.consultarNaturalezaArticulosExistentes(con,vo).clone();
	}	
	
	
	/**
	 * Consulta de Naturaleza de Articulos
	 * @param con
	 * @param institucion
	 */
	
	public void consultarNaturalezaArticulosExistentes(Connection con) 
	{
		HashMap vo=new HashMap();
		//vo.put("acronimo", acronimo);
		this.naturalezaArticulosMap=objetoDao.consultarNaturalezaArticulosExistentes(con,vo);
	}
	
	/**
	 * 
	 * @param acronimoNaturaleza
	 * @return
	 */
	public static boolean esMedicamento(String acronimoNaturaleza)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getNaturalezaArticulosDao().esMedicamento(acronimoNaturaleza);
	}
	
	/**
	 * Modifica una naturaleza de articulo Existente 
	 * @param con
	 * @param vo
	 * @return
	 */
	
	public boolean modificar(Connection con,HashMap vo)
	{
		return objetoDao.modificar(con, vo);
	}
	
	
	/**
	 * Agrega una Nueva naturaleza de articulo
	 * @param con
	 * @param institucion
	 */
	
	public boolean insertar(Connection con,HashMap vo)
	{
		return objetoDao.insertar(con, vo);
	}
	
	/**
	 * Elimina una naturaleza de articulo
	 * @param con
	 * @param institucion
	 * @param acronimo naturaleza de articulos
	 * @return
	 */
	
	public boolean eliminarRegistro(Connection con, int institucion, String acronimo)
	{
		return objetoDao.eliminarRegistro(con,institucion,acronimo);
	}

	public HashMap getNaturalezaArticulosMap() {
		return naturalezaArticulosMap;
	}

	public void setNaturalezaArticulosMap(HashMap naturalezaArticulosMap) {
		this.naturalezaArticulosMap = naturalezaArticulosMap;
	}

}
