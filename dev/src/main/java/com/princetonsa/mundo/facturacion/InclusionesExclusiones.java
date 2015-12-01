package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosString;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.InclusionesExclusionesDao;

public class InclusionesExclusiones 
{
	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	
	private InclusionesExclusionesDao objetoDao;
	
	/**
	 * Mapa de paquetes
	 */
	private HashMap incluExcluMap;
	
	
	
		
	/**
	 *  Constructor de la clase
	 */
	
	public InclusionesExclusiones() 
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
			objetoDao=myFactory.getInclusionesExclusionesDao();
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
		incluExcluMap=new HashMap();
    	incluExcluMap.put("numRegistros","0");
   	}

	
	
		
		
	/**
	 * Cunsulta de Paquete
	 * @param con
	 * @param codigoInstitucionInt
	 */
	
	
	public HashMap consultarIncluExcluEspecifico(Connection con,int institucion,String codigo)
	{
		HashMap vo=new HashMap();
		vo.put("institucion", institucion);
		vo.put("codigo", codigo);
		return (HashMap)objetoDao.consultarIncluExcluExistentes(con,vo).clone();
	}	
	
	
	
	/**
	 * Consulta de Paquetes
	 * @param con
	 * @param institucion
	 */
	
	public void consultarIncluExcluExistentes(Connection con, int institucion) 
	{
		HashMap vo=new HashMap();
		vo.put("institucion", institucion);
		this.incluExcluMap=objetoDao.consultarIncluExcluExistentes(con,vo);
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
	
	public boolean eliminarRegistro(Connection con, int institucion, String codigo)
	{
		return objetoDao.eliminarRegistro(con,institucion,codigo);
	}


	public HashMap getIncluExcluMap() {
		return incluExcluMap;
	}


	public void setIncluExcluMap(HashMap incluExcluMap) {
		this.incluExcluMap = incluExcluMap;
	}

	

	/**
	 * 
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static Vector<InfoDatosString> consultarInclusionesExclusiones(Connection con, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInclusionesExclusionesDao().consultarIncluExcluExistentes(con, institucion);
	}

}


