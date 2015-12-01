package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;


import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;

import com.princetonsa.dao.inventarios.ImpListaConteoDao;


/**
 * 
 * Mundo de Articulos por almacen
 * @author lgchavez@princetonsa.com
 *
 */
public class ImpListaConteo {
	
	// --------------- 	ATRIBUTOS
	
	private static Logger logger = Logger.getLogger(Secciones.class);
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ImpListaConteoDao impListaConteoDao;
	
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
	 * Codigo de la clase de inventario
	 */
	private int clase;
	
	/**
	 * Codigo del grupo
	 */
	private int grupo;
	
	/**
	 * Codigo del subgrupo
	 */
	private int subgrupo;
	
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
	
	
	/**
	 * 
	 */
	private String indArticulo;
	
	/**
	 * 
	 */
	private String ordArticulo;
	
	/**
	 * 
	 */
	private HashMap articulosPreparacion;
	
	
	/**
	 * Mapa de los articulos
	 */
	private HashMap articulosMap;
	
	/**
	 * Registro de los codigos de articulos insertados
	 */
	private String codigosArticulosInsertados;
	
	/**
	 * 
	 */
	private HashMap seccionestemp;
	

	
	// ----------------	METODOS
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */

	
	public ImpListaConteo()
	{
		this.reset();
		this.init (System.getProperty("TIPOBD"));
	}
	
	
	/**
	 *
	 */
	private void reset() 
	{
		
		this.institucion = ConstantesBD.codigoNuncaValido;
		this.centroAtencion = ConstantesBD.codigoNuncaValido;
		this.almacen = ConstantesBD.codigoNuncaValido;
		this.seccion = "";
		this.subseccion = ConstantesBD.codigoNuncaValido;
		this.clase = ConstantesBD.codigoNuncaValido;
		this.grupo = ConstantesBD.codigoNuncaValido;
		this.subgrupo = ConstantesBD.codigoNuncaValido;
		this.codigoArticulo = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica="";
		this.ubicacion = "";
		this.indArticulo="";
		this.ordArticulo ="";
		this.articulosPreparacion=new HashMap();
		this.articulosMap = new HashMap();
		this.articulosMap.put("numRegistros", "0");
		this.codigosArticulosInsertados="";
		this.seccionestemp=new HashMap();
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
			impListaConteoDao = myFactory.getImpListaConteoDao();
			wasInited = (impListaConteoDao != null); 
		}
		return wasInited;
	}
	

	
	public static HashMap consultar(Connection con, ImpListaConteo a)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpListaConteoDao().consultar(con, a);
	}
	
	/**
	 * 
	 * @param con
	 * @param ordArticulo 
	 * @param indArticulo 
	 * @param almacen 
	 * @param codigos 
	 * @param cadenaPreparada 
	 * @param subseccion 
	 * @param secciones 
	 * @param institucion 
	 * @param a
	 * @return
	 */
	public static String sqlReporteListadoConteo(Connection con, HashMap secciones, int subseccion, String cadenaPreparada, HashMap codigos, int almacen, String indArticulo, String ordArticulo, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getImpListaConteoDao().sqlReporteListadoConteo(con,secciones,subseccion,cadenaPreparada,codigos,almacen,indArticulo,ordArticulo, institucion);
	}

	
	//	 ------------------	SET Y GET



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
		ImpListaConteo.logger = logger;
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
	 * @return the clase
	 */
	public int getClase() {
		return clase;
	}

	/**
	 * @param clase the clase to set
	 */
	public void setClase(int clase) {
		this.clase = clase;
	}

	/**
	 * @return the grupo
	 */
	public int getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(int grupo) {
		this.grupo = grupo;
	}

	/**
	 * @return the subgrupo
	 */
	public int getSubgrupo() {
		return subgrupo;
	}

	/**
	 * @param subgrupo the subgrupo to set
	 */
	public void setSubgrupo(int subgrupo) {
		this.subgrupo = subgrupo;
	}

	/**
	 * @return the impListaConteoDao
	 */
	public static ImpListaConteoDao getImpListaConteoDao() {
		return impListaConteoDao;
	}

	/**
	 * @param impListaConteoDao the impListaConteoDao to set
	 */
	public static void setImpListaConteoDao(ImpListaConteoDao impListaConteoDao) {
		ImpListaConteo.impListaConteoDao = impListaConteoDao;
	}


	/**
	 * @return the articulosPreparacion
	 */
	public HashMap getArticulosPreparacion() {
		return articulosPreparacion;
	}


	/**
	 * @param articulosPreparacion the articulosPreparacion to set
	 */
	public void setArticulosPreparacion(HashMap articulosPreparacion) {
		this.articulosPreparacion = articulosPreparacion;
	}


	/**
	 * @return the indArticulo
	 */
	public String getIndArticulo() {
		return indArticulo;
	}


	/**
	 * @param indArticulo the indArticulo to set
	 */
	public void setIndArticulo(String indArticulo) {
		this.indArticulo = indArticulo;
	}


	/**
	 * @return the ordArticulo
	 */
	public String getOrdArticulo() {
		return ordArticulo;
	}


	/**
	 * @param ordArticulo the ordArticulo to set
	 */
	public void setOrdArticulo(String ordArticulo) {
		this.ordArticulo = ordArticulo;
	}


	/**
	 * @return the articulosMap
	 */
	public HashMap getArticulosMap() {
		return articulosMap;
	}


	/**
	 * @param articulosMap the articulosMap to set
	 */
	public void setArticulosMap(HashMap articulosMap) {
		this.articulosMap = articulosMap;
	}


	/**
	 * @return the codigosArticulosInsertados
	 */
	public String getCodigosArticulosInsertados() {
		return codigosArticulosInsertados;
	}


	/**
	 * @param codigosArticulosInsertados the codigosArticulosInsertados to set
	 */
	public void setCodigosArticulosInsertados(String codigosArticulosInsertados) {
		this.codigosArticulosInsertados = codigosArticulosInsertados;
	}


	/**
	 * @return the seccionestemp
	 */
	public HashMap getSeccionestemp() {
		return seccionestemp;
	}


	/**
	 * @param seccionestemp the seccionestemp to set
	 */
	public void setSeccionestemp(HashMap seccionestemp) {
		this.seccionestemp = seccionestemp;
	}
	
	public Object getSeccionestemp(String key) {
		return seccionestemp.get(key);
	}
	
	public void setSeccionesTemp(String key,Object obj) {
		this.seccionestemp.put(key, obj);

	}
	

}