package com.princetonsa.actionform.inventarios;

import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.inventarios.UtilidadInventarios;

/**
 * Clase para la preparación de la toma de inventario
 * Date: 2008-01-29
 * @author garias@princetonsa.com
 */
public class ComparativoUltimoConteoForm extends ValidatorForm 
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

	/**
	 * Contiene la información de varios conteos de un articulo
	 */
	private HashMap conteosArticulo;
	
	/**
	 * Codigo de preparación
	 */
	private int codigoPreparacion;
	
	/**
	 * Rompimiento por el cual se ordena
	 */
	private String codigoLote;
	
// --------------------- 	 RESET
	
	public void reset( int codigoInstitucion, int centroAtencion) {
		this.centroAtencion=centroAtencion;
		this.centrosAtencionMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion);
		this.almacen = ConstantesBD.codigoNuncaValido;
		this.almacenesMap = UtilidadInventarios.listadoAlmacensActivos(codigoInstitucion, false);
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
		this.patronOrdenar="descripcion";
		this.rompimiento="";
		this.fecha = UtilidadFecha.getFechaActual();
		this.hora= UtilidadFecha.getHoraActual();
		this.ultimoPatron="";
		this.conteosArticulo = new HashMap();
		this.conteosArticulo.put("numRegistros", "0");
		this.codigoPreparacion=ConstantesBD.codigoNuncaValido;
		this.codigoLote="";
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
	 * @param almacenesMap the almacenesMap to set
	 */
	public void setAlmacenesMap(String key, Object value) {
		this.almacenesMap.put(key, value);
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
	public void setSubgruposMap(String Key, Object value) {
		this.subgruposMap.put(Key, value);
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
	 * @return the conteosArticulo
	 */
	public HashMap getConteosArticulo() {
		return conteosArticulo;
	}



	/**
	 * @param conteosArticulo the conteosArticulo to set
	 */
	public void setConteosArticulo(HashMap conteosArticulo) {
		this.conteosArticulo = conteosArticulo;
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
