package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.SeccionesDao;

/**
 * mundo de secciones x almacen
 * @author axioma
 *
 */
public class Secciones 
{
	//--- Atributos
	private static Logger logger = Logger.getLogger(Secciones.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static SeccionesDao seccionesDao;
	
	/**
	 * 
	 * */
	private int codigoPk;
	
	/**
	 * 
	 */
	private int centroAtencion;
	
	/**
	 * 
	 */
	private int almacen;
	
	/**
	 * 
	 */
	private int codigoSeccion;
	
	/**
	 * 
	 */
	private String descripcionSeccion;
	
	/**
	 * 
	 */
	private int institucion;
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	/**
	 * 
	 */
	private String codigoSubseccion;
	
	/**
	 * 
	 */
	private String descripcionSubseccion;
	
	/**
	 * 
	 */
	private String codigoPkSeccion;
	
	/**
	 * 
	 */
	private int codigo_pk_seccion;
	
	
	
	/**
	 * 
	 */
	private String codigoSubseccionTemp;
	
	
	/**
	 * 
	 *
	 */
	public Secciones()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 * 
	 *
	 */
	public void reset() 
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.centroAtencion=ConstantesBD.codigoNuncaValido;
		this.almacen=ConstantesBD.codigoNuncaValido;
		this.codigoSeccion=ConstantesBD.codigoNuncaValido;
		this.descripcionSeccion="";
		this.institucion=ConstantesBD.codigoNuncaValido;
		this.usuarioModifica="";
		this.codigoSubseccion="";
		this.codigoSubseccionTemp="";
		this.descripcionSubseccion="";
		this.codigo_pk_seccion=ConstantesBD.codigoNuncaValido;
		
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
			seccionesDao = myFactory.getSeccionesDao();
			wasInited = (seccionesDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static boolean insertar(Connection con, Secciones seccion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSeccionesDao().insertar(con, seccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static boolean modificar(Connection con, Secciones seccion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSeccionesDao().modificar(con, seccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static boolean eliminar(Connection con, int codigo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSeccionesDao().eliminar(con, codigo);
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap consultar(Connection con, Secciones seccion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSeccionesDao().consultar(con, seccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoSeccion
	 * @return
	 */
	public static HashMap consultarSubsecciones(Connection con, int  codigoseccion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSeccionesDao().consultarSubsecciones(con, codigoseccion);
	}
	/**
	 * 
	 * @param con
	 * @param subseccion
	 * @return
	 */
	public static HashMap cargarSubsecciones(Connection con, Secciones subseccion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSeccionesDao().cargarSubsecciones(con, subseccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param subseccion
	 * @return
	 */
	public static boolean modificarSubseccion(Connection con, Secciones subseccion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSeccionesDao().modificarSubseccion(con, subseccion);
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static boolean eliminarSubseccion(Connection con, Secciones secciones)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSeccionesDao().eliminarSubseccion(con, secciones);
	}
	
	/**
	 * @return the almacen
	 */
	public int getAlmacen() {
		return almacen;
	}

	/**
	 * @param almacen the almacen to set
	 */
	public void setAlmacen(int almacen) {
		this.almacen = almacen;
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
	 * @return the codigoSeccion
	 */
	public int getCodigoSeccion() {
		return codigoSeccion;
	}

	/**
	 * @param codigoSeccion the codigoSeccion to set
	 */
	public void setCodigoSeccion(int codigoSeccion) {
		this.codigoSeccion = codigoSeccion;
	}

	
	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
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
	 * @return the descripcionSeccion
	 */
	public String getDescripcionSeccion() {
		return descripcionSeccion;
	}

	/**
	 * @param descripcionSeccion the descripcionSeccion to set
	 */
	public void setDescripcionSeccion(String descripcionSeccion) {
		this.descripcionSeccion = descripcionSeccion;
	}

	/**
	 * @return the codigoPk
	 */
	public int getCodigoPk() {
		return codigoPk;
	}

	/**
	 * @param codigoPk the codigoPk to set
	 */
	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}
	
	public static boolean insertarSubseccion(Connection con, Secciones seccion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getSeccionesDao().insertarSubsecciones(con, seccion);
	}

	/**
	 * @return the codigoPkSeccion
	 */
	public String getCodigoPkSeccion() {
		return codigoPkSeccion;
	}

	/**
	 * @param codigoPkSeccion the codigoPkSeccion to set
	 */
	public void setCodigoPkSeccion(String codigoPkSeccion) {
		this.codigoPkSeccion = codigoPkSeccion;
	}

	/**
	 * @return the codigoSubseccion
	 */
	public String getCodigoSubseccion() {
		return codigoSubseccion;
	}

	/**
	 * @param codigoSubseccion the codigoSubseccion to set
	 */
	public void setCodigoSubseccion(String codigoSubseccion) {
		this.codigoSubseccion = codigoSubseccion;
	}

	/**
	 * @return the descripcionSubseccion
	 */
	public String getDescripcionSubseccion() {
		return descripcionSubseccion;
	}

	/**
	 * @param descripcionSubseccion the descripcionSubseccion to set
	 */
	public void setDescripcionSubseccion(String descripcionSubseccion) {
		this.descripcionSubseccion = descripcionSubseccion;
	}

	/**
	 * @return the codigo_pk_seccion
	 */
	public int getCodigo_pk_seccion() {
		return codigo_pk_seccion;
	}

	/**
	 * @param codigo_pk_seccion the codigo_pk_seccion to set
	 */
	public void setCodigo_pk_seccion(int codigo_pk_seccion) {
		this.codigo_pk_seccion = codigo_pk_seccion;
	}

	/**
	 * @return the codigoSubseccionTemp
	 */
	public String getCodigoSubseccionTemp() {
		return codigoSubseccionTemp;
	}

	/**
	 * @param codigoSubseccionTemp the codigoSubseccionTemp to set
	 */
	public void setCodigoSubseccionTemp(String codigoSubseccionTemp) {
		this.codigoSubseccionTemp = codigoSubseccionTemp;
	}
	
}



