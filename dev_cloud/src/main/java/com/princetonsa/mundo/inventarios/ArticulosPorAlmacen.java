package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ArticulosPorAlmacenDao;

/**
 * 
 * Mundo de Articulos por almacen
 * @author garias@princetonsa.com
 *
 */
public class ArticulosPorAlmacen {
	
	// --------------- 	ATRIBUTOS
	
	private static Logger logger = Logger.getLogger(Secciones.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ArticulosPorAlmacenDao articulosPorAlmacenDao;
	
	/**
	 * Consecutivo del registro de activos por almacen
	 */
	private int codigoPk;
	
	/**
	 * Codigo del centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * Codigo del almacen
	 */
	private int almacen;
	
	/**
	 * Codigo de la seccion
	 */
	private String seccion;
	
	/**
	 * Codigo de la subseccion
	 */
	private int subseccion;
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	/**
	 * Codigo del articulo
	 */
	private int codigoArticulo;
	
	/**
	 * Descripcion del articulo
	 */
	private String descripcionArticulo;
	
	/**
	 * Lugar de ubicacion
	 */
	private String ubicacion;
	
	/**
	 * Usuario activo
	 */
	private String usuarioModifica;
	
	/**
	 * Codigo del detalle de articulos por almacen
	 */
	private int codigoDetArt;
	
	// ----------------	METODOS
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap consultar(Connection con, ArticulosPorAlmacen a)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticulosPorAlmacenDao().consultar(con, a);
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap consultarUbicaciones(Connection con, ArticulosPorAlmacen a)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticulosPorAlmacenDao().consultarUbicaciones(con, a);
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static boolean guardarNuevo(Connection con, ArticulosPorAlmacen a)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticulosPorAlmacenDao().guardarNuevo(con, a);
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static boolean modificarRegistroDet(Connection con, ArticulosPorAlmacen a)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticulosPorAlmacenDao().modificarRegistroDet(con, a);
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static boolean eliminarRegistroDet(Connection con, ArticulosPorAlmacen a)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getArticulosPorAlmacenDao().eliminarRegistroDet(con, a);
	}

	/**
	 * 
	 */
	public ArticulosPorAlmacen()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	/**
	 *
	 */
	private void reset() 
	{
		this.codigoPk = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.almacen = ConstantesBD.codigoNuncaValido;
		this.seccion = "";
		this.subseccion = ConstantesBD.codigoNuncaValido;
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.codigoArticulo = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica="";
		this.ubicacion = "";
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
			articulosPorAlmacenDao = myFactory.getArticulosPorAlmacenDao();
			wasInited = (articulosPorAlmacenDao != null);
		}
		return wasInited;
	}

	
	
	
	//	 ------------------	SET Y GET
	
	/**
	 * @return the articulosPorAlmacenDao
	 */
	public static ArticulosPorAlmacenDao getArticulosPorAlmacenDao() {
		return articulosPorAlmacenDao;
	}

	/**
	 * @param articulosPorAlmacenDao the articulosPorAlmacenDao to set
	 */
	public static void setArticulosPorAlmacenDao(
			ArticulosPorAlmacenDao articulosPorAlmacenDao) {
		ArticulosPorAlmacen.articulosPorAlmacenDao = articulosPorAlmacenDao;
	}

	/**
	 * @return the logger
	 */
	public static Logger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public static void setLogger(Logger logger) {
		ArticulosPorAlmacen.logger = logger;
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
	 * @return the seccion
	 */
	public String getSeccion() {
		return seccion;
	}

	/**
	 * @param seccion the seccion to set
	 */
	public void setSeccion(String seccion) {
		this.seccion = seccion;
	}

	/**
	 * @return the subseccion
	 */
	public int getSubseccion() {
		return subseccion;
	}

	/**
	 * @param subseccion the subseccion to set
	 */
	public void setSubseccion(int subseccion) {
		this.subseccion = subseccion;
	}

	/**
	 * @return the codigoArticulo
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * @param codigoArticulo the codigoArticulo to set
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * @return the descripcionArticulo
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	/**
	 * @param descripcionArticulo the descripcionArticulo to set
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
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
	 * @return the ubicacion
	 */
	public String getUbicacion() {
		return ubicacion;
	}

	/**
	 * @param ubicacion the ubicacion to set
	 */
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	/**
	 * @return the codigoDetArt
	 */
	public int getCodigoDetArt() {
		return codigoDetArt;
	}

	/**
	 * @param codigoDetArt the codigoDetArt to set
	 */
	public void setCodigoDetArt(int codigoDetArt) {
		this.codigoDetArt = codigoDetArt;
	}
}