package com.princetonsa.mundo.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.manejoPaciente.TiposUsuarioCamaDao;

/**
 * Mundo tipos usuario cama
 * @author Julián Pacheco Jiménez
 * jpacheco@princetonsa.com
 */
public class TiposUsuarioCama
{

//	-----------------Atributos
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static TiposUsuarioCamaDao tiposUsuarioCamaDao;
	
	/**
	 * Codigo de tipos usuario cama
	 */
	private int codigo;
	
	/**
	 * Nombre asignado por la institucion a los tipos de usuario cama
	 */
	private String nombre;
	
	/**
	 * Asigna el sexo respectivo
	 */
	private int codigoSexo;
	
	/**
	 * Campo de seleccion de "Si" o "No"
	 */
	private String indSexoRestrictivo;
	
	/**
	 * Campo que asigna la edad inicial
	 */
	private int edadInicial;
	
	/**
	 * Campo q asigna la edad final
	 */
	private int edadFinal;
	
	/**
	 * Usuario quien modifico por ultima vez o creo los tipos de usuario cama
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private HashMap tiposUsuarioCamaMap;
	
	
//	--------------------Metodos
	
	/**
	 * resetea los atributos del objeto 
	 *
	 */
	
	public void reset()
	{
		this.codigo=-1;
		this.nombre="";
		this.codigoSexo=-1;
		this.indSexoRestrictivo="";
		this.edadInicial=-1;
		this.edadFinal=-1;
		this.usuarioModifica="";
		
		tiposUsuarioCamaMap=new HashMap();
    	tiposUsuarioCamaMap.put("numRegistros","0");

	}
	
	
	/**
	 * constructor de la clase
	 *
	 */
	public TiposUsuarioCama() 
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
			tiposUsuarioCamaDao = myFactory.getTiposUsuarioCamaDao();
			wasInited = (tiposUsuarioCamaDao != null);
		}
		return wasInited;
	}
	
	public static TiposUsuarioCamaDao tiposUsuarioCamaDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getTiposUsuarioCamaDao();
	}
	
	/**
	 * Insertar un registro de tipos usuario cama 
	 * @param con
	 * @param TiposUsuarioCama tiposusuariocama
	 */
	public boolean insertarTiposUsuarioCama(Connection con, TiposUsuarioCama tiposusuariocama, int codigoInstitucion)
	{
		return tiposUsuarioCamaDao.insertarTiposUsuarioCama(con, tiposusuariocama, codigoInstitucion);
	}
	
	/**
	 * Modifica tipos de usuario cama registrados
	 * @param con
	 * @param TiposUsuarioCama tiposusuariocama
	 */
	public boolean modificarTiposUsuarioCama(Connection con, TiposUsuarioCama tiposusuariocama,int codigoAntesMod, int codigoInstitucion)
	{
		return tiposUsuarioCamaDao.modificarTiposUsuarioCama(con, tiposusuariocama, codigoAntesMod, codigoInstitucion);
	}
	
	/**
	 * Elimina tipos de usuario cama registrados
	 * @param con
	 * @param String codigo, int institucion
	 */
	public boolean eliminarTiposUsuarioCama(Connection con, int codigo, int institucion)
	{
		return tiposUsuarioCamaDao.eliminarTiposUsuarioCama(con, codigo, institucion);
	}
	
	/**
	 * Consulta de tipos usuario cama
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultarTiposUsuarioCama(Connection con, int codigoInstitucion)
	{
		return tiposUsuarioCamaDao.consultarTiposUsuarioCama(con, codigoInstitucion);
	}
	
	/**
	 * Consulta de los tipos usuario cama por codigo
	 */
	public HashMap consultarTiposUsuarioCamaEspecifico(Connection con, int codigoInstitucion,int codigo)
	{
		return tiposUsuarioCamaDao.consultarTiposUsuarioCamaEspecifico(con, codigoInstitucion, codigo); 
	}

//	--------------------Getters And Setters
	/**
	 * @return the tiposUsuarioCamaDao
	 */
	public static TiposUsuarioCamaDao getTiposUsuarioCamaDao() {
		return tiposUsuarioCamaDao;
	}


	/**
	 * @param tiposUsuarioCamaDao the tiposUsuarioCamaDao to set
	 */
	public static void setTiposUsuarioCamaDao(
			TiposUsuarioCamaDao tiposUsuarioCamaDao) {
		TiposUsuarioCama.tiposUsuarioCamaDao = tiposUsuarioCamaDao;
	}


	/**
	 * @return the codigoSexo
	 */
	public int getCodigoSexo() {
		return codigoSexo;
	}


	/**
	 * @param codigoSexo the codigoSexo to set
	 */
	public void setCodigoSexo(int codigoSexo) {
		this.codigoSexo = codigoSexo;
	}


	/**
	 * @return the edadFinal
	 */
	public int getEdadFinal() {
		return edadFinal;
	}


	/**
	 * @param edadFinal the edadFinal to set
	 */
	public void setEdadFinal(int edadFinal) {
		this.edadFinal = edadFinal;
	}


	/**
	 * @return the edadInicial
	 */
	public int getEdadInicial() {
		return edadInicial;
	}


	/**
	 * @param edadInicial the edadInicial to set
	 */
	public void setEdadInicial(int edadInicial) {
		this.edadInicial = edadInicial;
	}


	/**
	 * @return the indSexoRestrictivo
	 */
	public String getIndSexoRestrictivo() {
		return indSexoRestrictivo;
	}


	/**
	 * @param indSexoRestrictivo the indSexoRestrictivo to set
	 */
	public void setIndSexoRestrictivo(String indSexoRestrictivo) {
		this.indSexoRestrictivo = indSexoRestrictivo;
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
	 * @return the tiposUsuarioCamaMap
	 */
	public HashMap getTiposUsuarioCamaMap() {
		return tiposUsuarioCamaMap;
	}


	/**
	 * @param tiposUsuarioCamaMap the tiposUsuarioCamaMap to set
	 */
	public void setTiposUsuarioCamaMap(HashMap tiposUsuarioCamaMap) {
		this.tiposUsuarioCamaMap = tiposUsuarioCamaMap;
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
