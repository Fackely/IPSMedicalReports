package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.PisosDao;

/**
 * Mundo tipos de convenios
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */

public class Pisos
{
//-----------------Atributos
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static PisosDao pisosDao;
	
	/**
	 * pk
	 */
	private int codigo;
	
	
	/**
	 * Codigo del tipo pisos
	 */
	private String codigopiso;
	
	/**
	 * Nombre del piso
	 */
	private String nombre;
	
	/**
	 * centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * Usuario quien modifico por ultima vez o creo el piso
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private HashMap pisosMap;
	
//	--------------------Metodos
	
	/**
	 * resetea los atributos del objeto 
	 *
	 */
	public void reset()
	{
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.centroAtencion=ConstantesBD.codigoNuncaValido;
		this.codigopiso="";
		this.nombre="";
		this.usuarioModifica="";
		
		pisosMap=new HashMap();
    	pisosMap.put("numRegistros","0");
	}

	/**
	 * constructor de la clase
	 *
	 */
	public Pisos() 
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
			pisosDao = myFactory.getPisosDao();
			wasInited = (pisosDao != null);
		}
		return wasInited;
	}
	
	/**
	 * Consulta los n pisos x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap pisosXCentroAtencionTipo(Connection con, int centroAtencion,int codigoInstitucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPisosDao().pisosXCentroAtencionTipo(con, centroAtencion,codigoInstitucion);
	}

	/**
	 * Consulta los n pisos x centro atencion 
	 * @param con
	 * @param centroAtencion
	 * @return
	 */
	public static HashMap pisosXCentroAtencionTipo(int centroAtencion,int codigoInstitucion)
	{
		Connection con= UtilidadBD.abrirConexion();
		HashMap mapa= DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPisosDao().pisosXCentroAtencionTipo(con, centroAtencion,codigoInstitucion);
		UtilidadBD.closeConnection(con);
		return mapa;
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static PisosDao pisosDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPisosDao();
	}
	
	/**
	 * Insertar un registro de pisos
	 * @param con
	 * @param Pisos pisos
	 */
	public boolean insertarPisos(Connection con, Pisos pisos, int codigoInstitucion)
	{
		return pisosDao.insertarPisos(con, pisos, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public HashMap consultarPisos(Connection con, int codigoInstitucion)
	{
		return pisosDao.consultarPisos(con, codigoInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoInstitucion
	 * @param codigopiso
	 * @return
	 */
	public HashMap consultarPisosEspecifico(Connection con, int codigo)
	{
		return pisosDao.consultarPisosEspecifico(con, codigo);
	}
	
	/**
	 * Modifica un piso registrado
	 * @param con
	 * @param Pisos pisos
	 */
	public boolean modificarPisos(Connection con, Pisos pisos)
	{
		return pisosDao.modificarPisos(con, pisos);
	}
	
	/**
	 * Elimina un piso registrado
	 * @param con
	 * @param String codigo, int institucion
	 */
	public boolean eliminarPisos(Connection con, int codigo)
	{
		return pisosDao.eliminarPisos(con, codigo);
	}
	

	//---------------------Getter and Setters
	
	

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
	 * @return the codigopiso
	 */
	public String getCodigopiso() {
		return codigopiso;
	}

	/**
	 * @param codigopiso the codigopiso to set
	 */
	public void setCodigopiso(String codigopiso) {
		this.codigopiso = codigopiso;
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
	 * @return the pisosDao
	 */
	public static PisosDao getPisosDao() {
		return pisosDao;
	}

	/**
	 * @param pisosDao the pisosDao to set
	 */
	public static void setPisosDao(PisosDao pisosDao) {
		Pisos.pisosDao = pisosDao;
	}

	/**
	 * @return the pisosMap
	 */
	public HashMap getPisosMap() {
		return pisosMap;
	}

	/**
	 * @param pisosMap the pisosMap to set
	 */
	public void setPisosMap(HashMap pisosMap) {
		this.pisosMap = pisosMap;
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
