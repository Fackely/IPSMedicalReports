package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.PaquetesDao;


/**
 * 
 * @author axioma
 *
 */

public class Paquetes 
{
	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	
	private PaquetesDao objetoDao;
	
	/**
	 * Mapa de paquetes
	 */
	private HashMap paquetesMap;
	
	
	
		
	/**
	 *  Constructor de la clase
	 */
	
	public Paquetes() 
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
			objetoDao=myFactory.getPaquetesDao();
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
		paquetesMap=new HashMap();
    	paquetesMap.put("numRegistros","0");
   	}

	
	
	/**
	 * 
	 * @return the Map
	 */

	public HashMap getPaquetesMap() {
		return paquetesMap;
	}
	
	
	
	/**
	 * 
	 * @param paquetesMap
	 */

	public void setPaquetesMap(HashMap paquetesMap) {
		this.paquetesMap = paquetesMap;
	}

	
		
	/**
	 * Cunsulta de Paquete
	 * @param con
	 * @param codigoInstitucionInt
	 */
	
	
	public HashMap consultarPaqueteEspecifico(Connection con,int institucion,String codigoPaquete)
	{
		HashMap vo=new HashMap();
		vo.put("institucion", institucion);
		vo.put("codigoPaquete", codigoPaquete);
		return (HashMap)objetoDao.consultarPaquetesExistentes(con,vo).clone();
	}	
	
	
	
	/**
	 * Consulta de Paquetes
	 * @param con
	 * @param institucion
	 */
	
	public void consultarPaquetesExistentes(Connection con, int institucion) 
	{
		HashMap vo=new HashMap();
		vo.put("institucion", institucion);
		this.paquetesMap=objetoDao.consultarPaquetesExistentes(con,vo);
	}
	
	
	
	/**
	 * Agrega un Nuevo paquete
	 * @param con
	 * @param institucion
	 */
	
	public boolean insertar(Connection con,HashMap vo)
	{
		return objetoDao.insertar(con, vo);
	}
	

	
	
	/**
	 * Modifica un paquete Existente 
	 * @param con
	 * @param vo
	 * @return
	 */
	
	public boolean modificar(Connection con,HashMap vo)
	{
		return objetoDao.modificar(con, vo);
	}
	
	
	
	/**
	 * Elimina un Paquete
	 * @param con
	 * @param institucion
	 * @param codigoPaquete
	 * @return
	 */
	
	public boolean eliminarRegistro(Connection con, int institucion, String codigoPaquete)
	{
		return objetoDao.eliminarRegistro(con,institucion,codigoPaquete);
	}

}
