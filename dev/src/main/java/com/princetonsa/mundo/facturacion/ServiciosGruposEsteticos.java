package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ServiciosGruposEsteticosDao;



public class ServiciosGruposEsteticos 
{
	
	/**
	 * Intefaz para acceder y comunicarse con la fuente de datos
	 */
	
	private ServiciosGruposEsteticosDao objetoDao;
	
	/**
	 * Mapa de paquetes
	 */
	private HashMap serviciosEsteticosMap;
	
	private HashMap gruposEsteticosMap;
	
	
	
		
	public HashMap getGruposEsteticosMap() {
		return gruposEsteticosMap;
	}


	public void setGruposEsteticosMap(HashMap gruposEsteticosMap) {
		this.gruposEsteticosMap = gruposEsteticosMap;
	}


	/**
	 *  Constructor de la clase
	 */
	
	public ServiciosGruposEsteticos() 
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
			objetoDao=myFactory.getServiciosGruposEsteticosDao();
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
		serviciosEsteticosMap=new HashMap();
    	serviciosEsteticosMap.put("numRegistros","0");
    	gruposEsteticosMap=new HashMap();
    	gruposEsteticosMap.put("numRegistros", "0");
   	}

	
	
	
	/**
	 * 
	 * @return
	 */
	public HashMap getServiciosEsteticosMap() {
		return serviciosEsteticosMap;
	}

	
	/**
	 * 
	 * @param serviciosEsteticosMap
	 */
	public void setServiciosEsteticosMap(HashMap serviciosEsteticosMap) {
		this.serviciosEsteticosMap = serviciosEsteticosMap;
	}

	
		
	/**
	 * Cunsulta del Servicio por Grupo Estetico
	 * @param con
	 * @param codigoInstitucionInt
	 */
	
	
	public HashMap consultarServicioEsteticoEspecifico(Connection con,int institucion,String grupos_esteticos, int servicio)
	{
		HashMap vo=new HashMap();
		vo.put("institucion", institucion);
		vo.put("grupos_esteticos", grupos_esteticos);
		vo.put("servicio", servicio);
		return (HashMap)objetoDao.consultarServiciosEsteticosExistentes(con,vo).clone();
	}	
	
	
	
	/**
	 * Consulta de Servicios por Grupos Esteticos
	 * @param con
	 * @param institucion
	 */
	
	public void consultarServiciosEsteticosExistentes(Connection con, HashMap vo) 
	{
		this.serviciosEsteticosMap=objetoDao.consultarServiciosEsteticosExistentes(con,vo);
	}
	
	
	
	/**
	 * Agrega un Nuevo servicio estetico
	 * @param con
	 * @param institucion
	 */
	
	public boolean insertarServicio(Connection con,HashMap vo)
	{
		return objetoDao.insertarServicio(con, vo);
	}
	

	
	
	/**
	 * Modifica un Servicio Estetico Existente 
	 * @param con
	 * @param vo
	 * @return
	 */
	
	/*public boolean modificarServicio(Connection con,HashMap vo)
	{
		return objetoDao.modificarServicio(con, vo);
	}*/
	
	
	
	/**
	 * Elimina un Servicio Estetico
	 * @param con
	 * @param institucion
	 * @param codigo
	 * @return
	 */
	
	public boolean eliminarServicio(Connection con, int institucion, int servicio)
	{
		return objetoDao.eliminarServicio(con,institucion,servicio);
	}

	
	
	
	
	
	///////////////////////////////////////////////////////////////////////////////
	public boolean eliminarRegistro(Connection con, int institucion, String codigo)
	{
		return objetoDao.eliminarRegistro(con,institucion,codigo);
	}
	
	
	public boolean insertar(Connection con,HashMap vo)
	{
		return objetoDao.insertar(con, vo);
	}
	
	public boolean modificar(Connection con,HashMap vo)
	{
		return objetoDao.modificar(con, vo);
	}
	
	public HashMap consultarGrupoEsteticoEspecifico(Connection con,int institucion,String codigo)
	{
		HashMap vo=new HashMap();
		vo.put("institucion", institucion);
		vo.put("codigo", codigo);
		return (HashMap)objetoDao.consultarGruposEsteticosExistentes(con,vo).clone();
	}
	
	
	public void consultarGruposEsteticosExistentes(Connection con, int institucion) 
	{
		HashMap vo=new HashMap();
		vo.put("institucion", institucion);
		this.gruposEsteticosMap=objetoDao.consultarGruposEsteticosExistentes(con,vo);
	}
	
	
	

}
