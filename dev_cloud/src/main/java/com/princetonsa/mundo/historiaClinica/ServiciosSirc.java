/**
 * 
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.ServiciosSircDao;

/**
 * @author axioma
 *
 */
public class ServiciosSirc {
	
	private static Logger logger = Logger.getLogger(ServiciosSirc.class);
	
	private ServiciosSircDao objetoDao;
	
	private HashMap mapaServiciosSircTemp;
	
	public ServiciosSirc()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));		
	}


	private void init(String tipoBD)
	{
		if (objetoDao == null) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao = myFactory.getServiciosSircDao();
		}			
	}
	
	private void  reset()
	{
		this.mapaServiciosSircTemp = new HashMap();
		
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public  boolean insertarServicioSirc(Connection con,HashMap vo) 
	{
		return objetoDao.insertarServicioSirc(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public  boolean modificarServicioSirc(Connection con,HashMap vo)
	{
		logger.warn("\n\n valor del mapa vo en mundo "+vo+"\n\n");		
		return objetoDao.modificarServicioSirc(con, vo);
	}
	
	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public  HashMap consultarServicioSirc(Connection con,HashMap vo)
	{
		return objetoDao.consultarServicioSirc(con, vo);
	}

	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public  boolean eliminarRegistro(Connection con, int codigo, int institucion)
	{
		return objetoDao.eliminarRegistro(con, codigo, institucion);
	}
	
	/**
	 * Consulta de Servicios Sirc Detalle
	 * @param Connection con
	 * @param HashMap vo 
	 * **/
	public HashMap consultarServicioSircDetalle(Connection con, HashMap vo)
	{
		HashMap temp = new HashMap();
		String cadena = "";
		temp = objetoDao.consultarServicioSircDetalle(con, vo);
		for(int i=0;i<Integer.parseInt(temp.get("numRegistros").toString());i++)
			cadena+=(temp.get("servicio_"+i)+"")+",";
			
		temp.put("codServInsert",cadena);
		return temp;
	}
	
	/**
	 * Administra la cadena de codigos de servicios insertados desde la busqueda avanzada
	 * @param String cadena 
	 * @param int operacion
	 * @param String cadenaOperacion
	 * */
	public String CodigosInsertados(String cadena, int operacion, String cadenaOperacion)
	{
		if(!cadenaOperacion.equals(""))
		{
			if(operacion == 1) //adiccionar		
				cadena+=cadenaOperacion+",";	
		
			else if(operacion == 2) //eliminar
			{	
				if(cadena.equals(cadenaOperacion+","))
					cadena="";
				else	
					cadena = cadena.replaceAll(cadenaOperacion+",","");				
			}
		}		
		return cadena;
	}
	
	
	/**
	 * Inserta un registro Detalle de Servicios Sirc
	 * HashMap vo 
	 * **/
	public boolean insertarServiciosSircDetalle(Connection con, HashMap vo)
	{
		return objetoDao.insertarServiciosSircDetalle(con, vo);
	}

	
	/**
	 * Eliminar un registro Detalle de Servicios Sirc
	 * @param Connection con
	 * @param HashMap vo
	 * */
	public boolean eliminarServiciosSircDetalle(Connection con, HashMap vo)
	{
		return objetoDao.eliminarServiciosSircDetalle(con, vo);
	}
	

	public HashMap getMapaServiciosSircTemp() {
		return mapaServiciosSircTemp;
	}
	

	public void setMapaServiciosSircTemp(HashMap mapaServiciosSircTemp) {
		this.mapaServiciosSircTemp = mapaServiciosSircTemp;
	}
}