package com.princetonsa.actionform.inventarios;

import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

/**
 * Clase para la preparación de la toma de inventario
 * Date: 2008-01-29
 * @author garias@princetonsa.com
 */
public class PreparacionTomaInventarioForm extends ValidatorForm 
{

	
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
	 * Decripcion del articulo
	 */
	private String descripcionArticulo;
	
	/**
	 * indica una posicion en el mapa
	 * */
	private String indexMap;
	
	/**
	 * Fecha de la preparacion
	 */
	private String fecha;
	
	/**
	 * Hora de la preparacion
	 */
	private String hora;
	
	/**
	 * Mapa con los articulos filtrados por los parametros ingresados
	 */
	private HashMap articulosFiltradosMap;
	
	/**
	 * Campo por el cual se ordena
	 */
	private String patronOrdenar;
	
	/**
	 * Rompimiento por el cual se ordena
	 */
	private String rompimiento;
	
// --------------------- 	 RESET
	
	public void reset( int codigoInstitucion, int centroAtencion) {
		this.centroAtencion=centroAtencion;
		this.centrosAtencionMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion);
		this.almacen = ConstantesBD.codigoNuncaValido;
		this.almacenesMap = UtilidadInventarios.listadoAlmacensActivos(codigoInstitucion, false);
		this.seccion = "";
		this.seccionesMap = new HashMap();
		this.seccionesMap.put("numRegistros", "0");
		this.seccionesElegidasMap = new HashMap();
		this.seccionesElegidasMap.put("numRegistros", "0");
		this.subseccion = ConstantesBD.codigoNuncaValido;
		this.subseccionesMap = new HashMap();
		this.subseccionesMap.put("numRegistros", "0");
		this.clase = ConstantesBD.codigoNuncaValido;
		this.clasesMap = new HashMap();
		this.clasesMap.put("numRegistros", "0");
		this.grupo=ConstantesBD.codigoNuncaValido;
		this.gruposMap = new HashMap();
		this.gruposMap.put("numRegistros", "0");
		this.subgrupo=ConstantesBD.codigoNuncaValido;
		this.subgruposMap = new HashMap();
		this.subgruposMap.put("numRegistros", "0");		
		this.articulosMap = new HashMap();
		this.articulosMap.put("numRegistros", "0");
		this.articulosMap.put("codigosArticulos", "");
		this.articulosFiltradosMap = new HashMap();
		this.articulosFiltradosMap.put("numRegistros", "0");
		this.codigoArticulo=ConstantesBD.codigoNuncaValido;
		this.descripcionArticulo="";
		this.patronOrdenar="";
		this.rompimiento="";
		this.fecha = UtilidadFecha.getFechaActual();
		this.hora= UtilidadFecha.getHoraActual();
		this.ultimoPatron="";
	}
	
	
	
//	 ----------------------   SET Y GET

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
	 * @param articulosMap the articulosMap to set
	 */
	public void setArticulosMap(String key, Object value) {
		this.articulosMap.put(key, value);
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
	 * @return the indexMap
	 */
	public String getIndexMap() {
		return indexMap;
	}

	/**
	 * @param indexMap the indexMap to set
	 */
	public void setIndexMap(String indexMap) {
		this.indexMap = indexMap;
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
	 * @param clasesMap the clasesMap to set
	 */
	public void setClasesMap(String key, Object value) {
		this.clasesMap.put(key, value);
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
	 * @param gruposMap the gruposMap to set
	 */
	public void setGruposMap(String key, Object value) {
		this.gruposMap.put(key, value);
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
	 * @param subgruposMap the subgruposMap to set
	 */
	public void setSubgruposMap(String key, Object value) {
		this.subgruposMap.put(key, value);
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
	 * @return the fecha
	 */
	public String getFecha() {
		return fecha;
	}



	/**
	 * @param fecha the fecha to set
	 */
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}



	/**
	 * @return the hora
	 */
	public String getHora() {
		return hora;
	}



	/**
	 * @param hora the hora to set
	 */
	public void setHora(String hora) {
		this.hora = hora;
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
	 * @return the rompimiento
	 */
	public String getRompimiento() {
		return rompimiento;
	}



	/**
	 * @param rompimiento the rompimiento to set
	 */
	public void setRompimiento(String rompimiento) {
		this.rompimiento = rompimiento;
	}
	
	/**
	 * @param seccionesMap the seccionesMap to set
	 */
	public void setArticulosFiltradosMap(String key,Object obj) {
		this.articulosFiltradosMap.put(key,obj);
	}

	/**
	 * @return the seccionesMap
	 */
	public Object getArticulosFiltradosMap(String key) {
		return articulosFiltradosMap.get(key);
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
}
