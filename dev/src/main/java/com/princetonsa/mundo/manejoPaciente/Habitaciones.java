package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.HabitacionesDao;

/**
 * Mundo tipos de convenios
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */
public class Habitaciones 
{

	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static HabitacionesDao habitacionesDao;
	
	/**
	 * pk
	 */
	private int codigo;
	
	/**
	 * Campo de seleccion de acuerdo a los pisos creados
	 */
	private int piso;
	
	/**
	 * Campo de seleccion de acuerdo a los tipos de habitaciones creadas
	 */
	private String codigotipohabitac;
	
	/**
	 * Codigo de la habitacion 
	 */
	private String codigohabitac;
	
	/**
	 * Nombre de la habitacion
	 */
	private String nombre;
	
	/**
	 * centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * Usuario quien modifico por ultima vez o creo la habitacion
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private HashMap habitacionesMap;
	
	
//	--------------------Metodos
	
	/**
	 * resetea los atributos del objeto 
	 *
	 */
	public void reset()
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.centroAtencion=ConstantesBD.codigoNuncaValido;
		this.piso=-1;
		this.codigotipohabitac="";
		this.codigohabitac="";
		this.nombre="";
		this.usuarioModifica="";
		
		habitacionesMap=new HashMap();
    	habitacionesMap.put("numRegistros","0");
		
		
	}
	
	/**
	 * constructor de la clase
	 *
	 */
	public Habitaciones() 
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
			habitacionesDao = myFactory.getHabitacionesDao();
			wasInited = (habitacionesDao != null);
		}
		return wasInited;
	}

	/**
	 * Consulta las n habitaciones x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap habitacionesXCentroAtencionTipo(Connection con, int centroAtencion,int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHabitacionesDao().habitacionesXCentroAtencionTipo(con, centroAtencion, codigoInstitucion);
	}
	
	/**
	 * Consulta las n habitaciones x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap habitacionesXCentroAtencionTipo(int centroAtencion,int codigoInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHabitacionesDao().habitacionesXCentroAtencionTipo(con, centroAtencion, codigoInstitucion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultarHabitaciones(Connection con, int codigoInstitucion)
	{
		return habitacionesDao.consultarHabitaciones(con, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigopiso
	 * @return
	 */
	public HashMap consultarHabitacionesEspecifico(Connection con, int codigo)
	{
		return habitacionesDao.consultarHabitacionesEspecifico(con, codigo);
	}
	
	/**
	 * Consulta avanzada de habitaciones por cada uno de los campos
	 * @param con
	 * @param condicion 
	 * */
	
	/*public static HashMap consultaHabitacionesAvanzada(Connection con,HashMap condicion)
	{
		HashMap temp = new HashMap();
		temp = habitacionesDao().consultaHabitacionesAvanzada(con, condicion);
		
		for(int i=0; i<Integer.parseInt(temp.get("numRegistros").toString());i++)
		{
			temp.put("codigoantesmod_"+i,temp.get("codigohabitac_"+i).toString().trim());
		}	
		
		return temp;				
	}*/	
	
	/**
	 * 
	 * @return
	 */
	public static HabitacionesDao habitacionesDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHabitacionesDao();
	}
	
	/**
	 * Insertar un registro de habitaciones
	 * @param con
	 * @param Habitaciones habitaciones
	 */
	public boolean insertarHabitaciones(Connection con, Habitaciones habitaciones, int codigoInstitucion)
	{
		return habitacionesDao.insertarHabitaciones(con, habitaciones, codigoInstitucion);
	}
	
	/**
	 * Modifica una habitacion registrada
	 * @param con
	 * @param habitaciones habitaciones
	 */
	public boolean modificarHabitaciones(Connection con, Habitaciones habitaciones)
	{
		return habitacionesDao.modificarHabitaciones(con, habitaciones);
	}
	
	/**
	 * Elimina una habitacion registrada
	 * @param con
	 * @param int codigo, int institucion
	 */
	public boolean eliminarHabitaciones(Connection con, int codigo)
	{
		return habitacionesDao.eliminarHabitaciones(con, codigo);
	}
	
	
//	---------------------Getter and Setters
	
	/**
	 * @return the habitacionesDao
	 */
	public static HabitacionesDao getHabitacionesDao() {
		return habitacionesDao;
	}

	/**
	 * @param habitacionesDao the habitacionesDao to set
	 */
	public static void setHabitacionesDao(HabitacionesDao habitacionesDao) {
		Habitaciones.habitacionesDao = habitacionesDao;
	}

	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the codigohabitac
	 */
	public String getCodigohabitac() {
		return codigohabitac;
	}

	/**
	 * @param codigohabitac the codigohabitac to set
	 */
	public void setCodigohabitac(String codigohabitac) {
		this.codigohabitac = codigohabitac;
	}

	/**
	 * @return the codigotipohabitac
	 */
	public String getCodigotipohabitac() {
		return codigotipohabitac;
	}

	/**
	 * @param codigotipohabitac the codigotipohabitac to set
	 */
	public void setCodigotipohabitac(String codigotipohabitac) {
		this.codigotipohabitac = codigotipohabitac;
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
	 * @return the habitacionesMap
	 */
	public HashMap getHabitacionesMap() {
		return habitacionesMap;
	}

	/**
	 * @param habitacionesMap the habitacionesMap to set
	 */
	public void setHabitacionesMap(HashMap habitacionesMap) {
		this.habitacionesMap = habitacionesMap;
	}

	/**
	 * @return the piso
	 */
	public int getPiso() {
		return piso;
	}

	/**
	 * @param piso the piso to set
	 */
	public void setPiso(int piso) {
		this.piso = piso;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
}
