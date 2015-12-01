package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.TiposAmbulanciaDao;

/**
 * Mundo tipos ambulancia
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */
public class TiposAmbulancia 
{
//	-----------------Atributos
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static TiposAmbulanciaDao tiposAmbulanciaDao;
	
	/**
	 * Codigo de tipos ambulancia
	 */
	private String codigo;
	
	/**
	 * Descripcion asignado por la institucion a cada uno de los tipos de ambulancia
	 */
	private String descripcion;
	
	/**
	 * Asigna el servicio respectivo
	 */
	private int codigoServicio;
	
	/**
	 * Usuario quien modifico por ultima vez o creo los tipos de ambulancia
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private HashMap tiposAmbulanciaMap;
	
//	--------------------Metodos
	
	/**
	 * resetea los atributos del objeto 
	 *
	 */
	
	public void reset()
	{
		this.codigo="";
		this.descripcion="";
		this.codigoServicio=-1;
		this.usuarioModifica="";
		
		tiposAmbulanciaMap=new HashMap();
    	tiposAmbulanciaMap.put("numRegistros","0");
	}
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public TiposAmbulancia() 
	{
		reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			tiposAmbulanciaDao = myFactory.getTiposAmbulanciaDao();
			wasInited = (tiposAmbulanciaDao != null);
		}
		return wasInited;
	}
	
	public static TiposAmbulanciaDao tiposambulanciaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiposAmbulanciaDao();
	}

	/**
	 * Insertar un registro de tipos ambulancia
	 * @param con
	 * @param TiposAmbulancia tiposambulancia
	 */
	public boolean insertarTiposAmbulancia(Connection con, TiposAmbulancia tiposambulancia, int codigoInstitucion)
	{
		return tiposAmbulanciaDao.insertarTiposAmbulancia(con, tiposambulancia, codigoInstitucion);
	}
	
	/**
	 * Modifica tipos ambulancia registrada
	 * @param con
	 * @param TiposAmbulancia tiposambulancia
	 */
	public boolean modificarTiposAmbulancia(Connection con, TiposAmbulancia tiposambulancia,String codigoAntesMod, int codigoInstitucion)
	{
		return tiposAmbulanciaDao.modificarTiposAmbulancia(con, tiposambulancia, codigoAntesMod, codigoInstitucion);
	}
	
	/**
	 * Elimina tipos de ambulancia registradas
	 * @param con
	 * @param String codigo, int institucion
	 */
	public boolean eliminarTiposAmbulancia(Connection con, String codigo, int institucion)
	{
		return tiposAmbulanciaDao.eliminarTiposAmbulancia(con, codigo, institucion);
	}
	
	/**
	 * Consulta de tipos ambulancia
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposAmbulancia(Connection con, int codigoInstitucion)
	{
		return tiposAmbulanciaDao.consultarTiposAmbulancia(con, codigoInstitucion);
	}
	
	/**
	 * Consulta de tipos ambulancia por codigo
	 */
	public HashMap consultarTiposAmbulanciaEspecifico(Connection con, int codigoInstitucion,String codigo)
	{
		return tiposAmbulanciaDao.consultarTiposAmbulanciaEspecifico(con, codigoInstitucion, codigo); 
	}
	
//	--------------------Getters And Setters
	/**
	 * @return the tiposAmbulanciaDao
	 */
	public static TiposAmbulanciaDao getTiposAmbulanciaDao() {
		return tiposAmbulanciaDao;
	}


	/**
	 * @param tiposAmbulanciaDao the tiposAmbulanciaDao to set
	 */
	public static void setTiposAmbulanciaDao(TiposAmbulanciaDao tiposAmbulanciaDao) {
		TiposAmbulancia.tiposAmbulanciaDao = tiposAmbulanciaDao;
	}


	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}


	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	/**
	 * @return the codigoServicio
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}


	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}


	/**
	 * @return the usuarioModifica
	 */
	public String getUsuarioModifica() {
		return usuarioModifica;
	}


	/**
	 * @param usuarioModifica the usuarioModifica to set
	 */
	public void setUsuarioModifica(String usuarioModifica) {
		this.usuarioModifica = usuarioModifica;
	}


	/**
	 * @return the descripcion
	 */
	public String getDescripcion() {
		return descripcion;
	}


	/**
	 * @param descripcion the descripcion to set
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}


	/**
	 * @return the tiposAmbulanciaMap
	 */
	public HashMap getTiposAmbulanciaMap() {
		return tiposAmbulanciaMap;
	}


	/**
	 * @param tiposAmbulanciaMap the tiposAmbulanciaMap to set
	 */
	public void setTiposAmbulanciaMap(HashMap tiposAmbulanciaMap) {
		this.tiposAmbulanciaMap = tiposAmbulanciaMap;
	}
}
