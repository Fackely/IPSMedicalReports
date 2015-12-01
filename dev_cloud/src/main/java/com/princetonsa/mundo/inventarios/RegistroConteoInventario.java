package com.princetonsa.mundo.inventarios;

import java.sql.Connection;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.inventarios.RegistroConteoInventarioDao;

/**
 * 
 * @author axioma
 *
 */
public class RegistroConteoInventario {
	
	
	private static Logger logger = Logger.getLogger(Secciones.class);
	

	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static RegistroConteoInventarioDao registroConteoInventarioDao;
	
	/**
	 * Campo por el cual se ordena
	 */
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * String Link Siguiente
	 * */
	private String linkSiguiente;	
	
	
	/**
	 * Estado del formulario
	 */
	private String estado;
	
	/**
	 * Codigo del centro de atencion
	 */
	private int centroAtencion;

	/**
	 * Mapa de los centros de atencion
	 */
	private HashMap centrosAtencionMap;
	
	/**
	 * Codigo del almacen
	 */
	private int almacen;
	
	/**
	 * Mapa de los almacenes
	 */
	private HashMap almacenesMap;
	
	/**
	 * Codigo de la seccion
	 */
	private String seccion;
	
	/**
	 * Codigos de las preparaciones
	 */
	private String codigosPreparaciones;
	
	/**
	 * Mapa de las secciones
	 */
	private HashMap seccionesMap;
	
	/**
	 * Mapa de las secciones elegidas por el usuario
	 */
	private HashMap seccionesElegidasMap;
	
	/**
	 * Codigo de la subsecc-,ion
	 */
	private int subseccion;
	
	/**
	 * Mapa de las subsecciones
	 */
	private HashMap subseccionesMap;
	
	/**
	 * Mapa de los articulos
	 */
	private HashMap articulosMap;
	
	/**
	 * Codigo de la Clase
	 */
	private int clase;
	
	/**
	 * Mapa de las clases
	 */
	private HashMap clasesMap;
	
	/**
	 * Codigo del grupo
	 */
	private int grupo;
	
	/**
	 * Mapa de los grupos
	 */
	private HashMap gruposMap;
	
	/**
	 * Codigo del subgrupo
	 */
	private int subgrupo;
	
	/**
	 * Mapa de los subgrupos
	 */
	private HashMap subgruposMap;
	
	/**
	 * Codigo del articulo
	 */
	private int codigoArticulo;
	
	/**
	 * Mapa con los articulos filtrados por los parametros ingresados
	 */
	private HashMap articulosFiltradosMap;
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	
	/**
	 * Descripcion del articulo
	 */
	private String descripcionArticulo;
	
	/**
	 * Cadena con codigos de articulos separado por comas
	 */
	private String codigosArticulos;
	
	/**
	 * 
	 */
	private String usuarioModifica;
	
	/**
	 * Cantidad conteo
	 */
	private int conteo;
	/**
	 * Usuario responsable
	 */
	private String usuario_responsable;
	/**
	 * Numero de conteo, por articulo
	 */
	private int num_conteo;
	/**
	 * Ordenamiento
	 */
	private String ordenar;
	/**
	 * indice de diferencias de conteo.
	 */
	private String indArticulo;
	/**
	 * 
	 *
	 */
	public RegistroConteoInventario()
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
		this.codigosArticulos="";
		this.conteo=ConstantesBD.codigoNuncaValido;
		this.num_conteo=ConstantesBD.codigoNuncaValido;
		this.usuario_responsable="";
		this.codigosPreparaciones="";
		
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
			registroConteoInventarioDao = myFactory.getRegistroConteoInventarioDao();
			wasInited = (registroConteoInventarioDao != null);
		}
		return wasInited;
	}


	/**
	 * @return the registroConteoInventarioDao
	 */
	public static RegistroConteoInventarioDao getRegistroConteoInventarioDao() {
		return registroConteoInventarioDao;
	}


	/**
	 * @param registroConteoInventarioDao the registroConteoInventarioDao to set
	 */
	public static void setRegistroConteoInventarioDao(
			RegistroConteoInventarioDao registroConteoInventarioDao) {
		RegistroConteoInventario.registroConteoInventarioDao = registroConteoInventarioDao;
	}


	/**
	 * 
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public static HashMap filtrarArticulos(Connection con, RegistroConteoInventario pti)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroConteoInventarioDao().filtrarArticulos(con, pti);
	}


	
	/**
	 * 
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public static boolean guardarConteo(Connection con, RegistroConteoInventario pti)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroConteoInventarioDao().guardarConteo(con, pti);
	}

	/**
	 * 
	 * 
	 * @param con
	 * @param pti
	 * @return
	 */
	public static boolean anularConteos(Connection con, RegistroConteoInventario pti, String estado)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroConteoInventarioDao().anularConteos(con, pti, estado);
	}
	
	/**
	 * 
	 * @param seccionesElegidasMap2
	 * @param subseccion2
	 * @param cadenaPreparada
	 * @param almacen2
	 * @param ordArticulo
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static String sqlReporteRegistroConteoInventario(
			HashMap secciones, int subseccion,
			String cadenaPreparada, int almacen, String ordArticulo,
			int codigoInstitucion) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getRegistroConteoInventarioDao().sqlReporteRegistroConteoInventario(secciones, subseccion, cadenaPreparada, almacen, ordArticulo, codigoInstitucion);
		
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
	 * @return the almacenesMap
	 */
	public HashMap getAlmacenesMap() {
		return almacenesMap;
	}


	/**
	 * @param almacenesMap the almacenesMap to set
	 */
	public void setAlmacenesMap(HashMap almacenesMap) {
		this.almacenesMap = almacenesMap;
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
	 * @return the centrosAtencionMap
	 */
	public HashMap getCentrosAtencionMap() {
		return centrosAtencionMap;
	}


	/**
	 * @param centrosAtencionMap the centrosAtencionMap to set
	 */
	public void setCentrosAtencionMap(HashMap centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
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
	 * @return the clasesMap
	 */
	public HashMap getClasesMap() {
		return clasesMap;
	}


	/**
	 * @param clasesMap the clasesMap to set
	 */
	public void setClasesMap(HashMap clasesMap) {
		this.clasesMap = clasesMap;
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
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
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
	 * @return the gruposMap
	 */
	public HashMap getGruposMap() {
		return gruposMap;
	}


	/**
	 * @param gruposMap the gruposMap to set
	 */
	public void setGruposMap(HashMap gruposMap) {
		this.gruposMap = gruposMap;
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
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
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
	 * @return the seccionesElegidasMap
	 */
	public HashMap getSeccionesElegidasMap() {
		return seccionesElegidasMap;
	}


	/**
	 * @param seccionesElegidasMap the seccionesElegidasMap to set
	 */
	public void setSeccionesElegidasMap(HashMap seccionesElegidasMap) {
		this.seccionesElegidasMap = seccionesElegidasMap;
	}


	/**
	 * @return the seccionesMap
	 */
	public HashMap getSeccionesMap() {
		return seccionesMap;
	}


	/**
	 * @param seccionesMap the seccionesMap to set
	 */
	public void setSeccionesMap(HashMap seccionesMap) {
		this.seccionesMap = seccionesMap;
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
	 * @return the subgruposMap
	 */
	public HashMap getSubgruposMap() {
		return subgruposMap;
	}


	/**
	 * @param subgruposMap the subgruposMap to set
	 */
	public void setSubgruposMap(HashMap subgruposMap) {
		this.subgruposMap = subgruposMap;
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
	 * @return the subseccionesMap
	 */
	public HashMap getSubseccionesMap() {
		return subseccionesMap;
	}


	/**
	 * @param subseccionesMap the subseccionesMap to set
	 */
	public void setSubseccionesMap(HashMap subseccionesMap) {
		this.subseccionesMap = subseccionesMap;
	}


	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}


	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
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
		RegistroConteoInventario.logger = logger;
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
	 * @return the conteo
	 */
	public int getConteo() {
		return conteo;
	}


	/**
	 * @param conteo the conteo to set
	 */
	public void setConteo(int conteo) {
		this.conteo = conteo;
	}


	/**
	 * @return the num_conteo
	 */
	public int getNum_conteo() {
		return num_conteo;
	}


	/**
	 * @param num_conteo the num_conteo to set
	 */
	public void setNum_conteo(int num_conteo) {
		this.num_conteo = num_conteo;
	}


	/**
	 * @return the usuario_responsable
	 */
	public String getUsuario_responsable() {
		return usuario_responsable;
	}


	/**
	 * @param usuario_responsable the usuario_responsable to set
	 */
	public void setUsuario_responsable(String usuario_responsable) {
		this.usuario_responsable = usuario_responsable;
	}


	/**
	 * @return the ordenar
	 */
	public String getOrdenar() {
		return ordenar;
	}


	/**
	 * @param ordenar the ordenar to set
	 */
	public void setOrdenar(String ordenar) {
		this.ordenar = ordenar;
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
	 * @return the codigosPreparaciones
	 */
	public String getCodigosPreparaciones() {
		return codigosPreparaciones;
	}


	/**
	 * @param codigosPreparaciones the codigosPreparaciones to set
	 */
	public void setCodigosPreparaciones(String codigosPreparaciones) {
		this.codigosPreparaciones = codigosPreparaciones;
	}
	
}