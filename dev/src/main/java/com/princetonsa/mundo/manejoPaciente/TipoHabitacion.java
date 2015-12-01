package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.TipoHabitacionDao;


/**
 * Mundo tipo habitacion
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */

public class TipoHabitacion 
{
//	-----------------Atributos
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static TipoHabitacionDao tipoHabitacionDao;
	
	/**
	 * Codigo del tipo habitacion
	 */
	private String codigo;
	
	/**
	 * Nombre asigando por la institucion a cada tipo de habitacion
	 */
	private String nombre;
	
	/**
	 * Usuario quien modifico por ultima vez o creo el tipo de habitacion
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private HashMap tipoHabitacionMap;
	
	
	
	
//	--------------------Metodos
	
	/**
	 * resetea los atributos del objeto 
	 *
	 */
	
	public void reset()
	{
		this.codigo="";
		this.nombre="";
		this.usuarioModifica="";
		

		tipoHabitacionMap=new HashMap();
    	tipoHabitacionMap.put("numRegistros","0");
	}
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public TipoHabitacion() 
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
			tipoHabitacionDao = myFactory.getTipoHabitacionDao();
			wasInited = (tipoHabitacionDao != null);
		}
		return wasInited;
	}
	
	public static TipoHabitacionDao tipohabitacionDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTipoHabitacionDao();
	}
	
	/**
	 * Insertar un registro tipo habitacion
	 * @param con
	 * @param TipoHabitacion tipohabitacion
	 */
	public boolean insertarTipoHabitacion(Connection con, TipoHabitacion tipohabitacion, int codigoInstitucion)
	{
		return tipoHabitacionDao.insertarTipoHabitacion(con, tipohabitacion, codigoInstitucion);
	}
	
	/**
	 * Modifica un tipo habitacion registrada
	 * @param con
	 * @param TipoHabitacion tipohabitacion
	 */
	public boolean modificarTipoHabitacion(Connection con, TipoHabitacion tipohabitacion,String codigoAntesMod, int codigoInstitucion)
	{
		return tipoHabitacionDao.modificarTipoHabitacion(con, tipohabitacion, codigoAntesMod, codigoInstitucion);
	}

	/**
	 * Elimina un tipo de habitacion registrada
	 * @param con
	 * @param String codigo, int institucion
	 */
	public boolean eliminarTipoHabitacion(Connection con, String codigo, int institucion)
	{
		return tipoHabitacionDao.eliminarTipoHabitacion(con, codigo, institucion);
	}
	
	/**
	 * Consulta de tipo habitacion
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTipoHabitacion(Connection con, int codigoInstitucion)
	{
		return tipoHabitacionDao.consultarTipoHabitacion(con, codigoInstitucion);
	}
	
	/**
	 * Consulta de tipo habitacion por codigo
	 */
	public HashMap consultarTipoHabitacionEspecifico(Connection con, int codigoInstitucion,String codigo)
	{
		return tipoHabitacionDao.consultarTipoHabitacionEspecifico(con, codigoInstitucion, codigo); 
	}
	
	

//	--------------------Getters And Setters
	/**
	 * @return the tipoHabitacionDao
	 */
	public static TipoHabitacionDao getTipoHabitacionDao() {
		return tipoHabitacionDao;
	}


	/**
	 * @param tipoHabitacionDao the tipoHabitacionDao to set
	 */
	public static void setTipoHabitacionDao(TipoHabitacionDao tipoHabitacionDao) {
		TipoHabitacion.tipoHabitacionDao = tipoHabitacionDao;
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
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}


	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
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
	 * @return the tipoHabitacionMap
	 */
	public HashMap getTipoHabitacionMap() {
		return tipoHabitacionMap;
	}


	/**
	 * @param tipoHabitacionMap the tipoHabitacionMap to set
	 */
	public void setTipoHabitacionMap(HashMap tipoHabitacionMap) {
		this.tipoHabitacionMap = tipoHabitacionMap;
	}


	

}
