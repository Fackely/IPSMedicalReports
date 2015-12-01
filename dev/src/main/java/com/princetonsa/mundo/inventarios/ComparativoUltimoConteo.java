package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.ArticulosPorAlmacenDao;

/**
 * 
 * Mundo de Preparacion Toma de inventarios
 * @author garias@princetonsa.com
 *
 */
public class ComparativoUltimoConteo {
	
	// --------------- 	ATRIBUTOS
	
	private static Logger logger = Logger.getLogger(Secciones.class);
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ArticulosPorAlmacenDao articulosPorAlmacenDao;
	
	/**
	 * Codigo del centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * Codigo del almacen
	 */
	private int almacen;
	
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
	 * Cadena con codigos de articulos separado por comas
	 */
	private String codigosArticulos;
	
	/**
	 * Mapa con los articulos filtrados
	 */
	private HashMap articulosFiltradosMap;
	
	/**
	 * Fecha de toma de la preparación
	 */
	private String fecha_toma;
	
	/**
	 * Hora de la toma de la preparacion
	 */
	private String hora_toma;
	
	/**
	 * Codigo de la preparación
	 */
	private int codigoPreparacion;
	
	/**
	 * Codigo de la preparación
	 */
	private String patronOrdenar;
	
	/**
	 * codigo Lote
	 */
	private String codigoLote;
	
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
	 *
	 */
	public ComparativoUltimoConteo()
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
		this.clase = ConstantesBD.codigoNuncaValido;
		this.grupo = ConstantesBD.codigoNuncaValido;
		this.subgrupo = ConstantesBD.codigoNuncaValido;
		this.codigoArticulo = ConstantesBD.codigoNuncaValido;
		this.codigoPreparacion = ConstantesBD.codigoNuncaValido;
		this.usuarioModifica="";
		this.ubicacion = "";
		this.codigosArticulos="";
		this.fecha_toma="";
		this.hora_toma="";
		this.patronOrdenar="";
		this.codigoLote="";
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
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap consultarSubgrupos(Connection con, int clase, int grupo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPreparacionTomaInventarioDao().consultarSubgrupos(con, clase, grupo);
	}
	
	/**
	 * 
	 * @param con
	 * @param seccion
	 * @return
	 */
	public static HashMap consultarGrupos(Connection con, int a)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPreparacionTomaInventarioDao().consultarGrupos(con, a);
	}
	
	/**
	 * 
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public static HashMap filtrarArticulos(Connection con, ComparativoUltimoConteo cuc)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getComparativoUltimoConteoDao().filtrarArticulos(con, cuc);
	}
	
	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public static HashMap consultarConteosArticulo(Connection con, ComparativoUltimoConteo cuc)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getComparativoUltimoConteoDao().consultarConteosArticulo(con, cuc);
	}

	/**
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public static int CodigoPreparacionMax(Connection con, PreparacionTomaInventario pti)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPreparacionTomaInventarioDao().CodigoPreparacionMax(con,pti);
	}
	
	/**
	 * 
	 * @param articulos
	 * @param almacen
	 * @return
	 */
	public String sqlComparativoUltimoConteo(String articulos, int almacen) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getComparativoUltimoConteoDao().sqlComparativoUltimoConteo(articulos,almacen);
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
		ComparativoUltimoConteo.articulosPorAlmacenDao = articulosPorAlmacenDao;
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
		ComparativoUltimoConteo.logger = logger;
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
	 * @return the articulosFiltradosMap
	 */
	public HashMap getArticulosFiltradosMap() {
		return articulosFiltradosMap;
	}

	/**
	 * @param articulosFiltradosMap the articulosFiltradosMap to set
	 */
	public void setArticulosFiltradosMap(HashMap articulosFiltradosMap) {
		this.articulosFiltradosMap = articulosFiltradosMap;
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
	 * @return the codigosArticulos
	 */
	public String getCodigosArticulos() {
		return codigosArticulos;
	}

	/**
	 * @param codigosArticulos the codigosArticulos to set
	 */
	public void setCodigosArticulos(String codigosArticulos) {
		this.codigosArticulos = codigosArticulos;
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
	 * @return the fecha_toma
	 */
	public String getFecha_toma() {
		return fecha_toma;
	}

	/**
	 * @param fecha_toma the fecha_toma to set
	 */
	public void setFecha_toma(String fecha_toma) {
		this.fecha_toma = fecha_toma;
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
	 * @return the hora_toma
	 */
	public String getHora_toma() {
		return hora_toma;
	}

	/**
	 * @param hora_toma the hora_toma to set
	 */
	public void setHora_toma(String hora_toma) {
		this.hora_toma = hora_toma;
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
	 * @return the codigoPreparacion
	 */
	public int getCodigoPreparacion() {
		return codigoPreparacion;
	}

	/**
	 * @param codigoPreparacion the codigoPreparacion to set
	 */
	public void setCodigoPreparacion(int codigoPreparacion) {
		this.codigoPreparacion = codigoPreparacion;
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the codigoLote
	 */
	public String getCodigoLote() {
		return codigoLote;
	}

	/**
	 * @param codigoLote the codigoLote to set
	 */
	public void setCodigoLote(String codigoLote) {
		this.codigoLote = codigoLote;
	}

}