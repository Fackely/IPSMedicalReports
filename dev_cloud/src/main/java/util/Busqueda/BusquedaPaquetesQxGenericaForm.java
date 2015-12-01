package util.Busqueda;

import java.util.HashMap;

import org.apache.struts.action.ActionForm;

public class BusquedaPaquetesQxGenericaForm extends ActionForm 
{
	/**
	 * Manejo del estado
	 */
	private String estado;
	
	//****************ATRIBUTOS DE BUSQUEDA (se deben pasar por request en la busqueda)******************************
	private String codigoServicio = "";
	private String parejasClaseGrupo = "";
	private String consecutivosPaquetesInsertados = "";
	
	private String nombreMapa = "";
	private int numeroFilasMapa = 0;
	private String nombreDiv = "";
	//**********************************************************************
	
	/**
	 * Mapa que almacena los paquetes de la busqueda
	 */
	private HashMap<String,Object> paquetes = new HashMap<String, Object>();
	
	
	/**
	 * Número de registros del mapa paquetes
	 */
	private int numRegistros;
	
	/**
	 * Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * Patron por el Cual ordenar
	 * */
	private String patronOrdenar;
	
	/**
	 * Proximo link en el Pager
	 * */
	private String linkSiguiente;
	
	/**
	 * Max page items
	 */
	private int maxPageItems;
	
	/**
	 * Variable para permitir eliminar paquetes (aplica para consumo de materiales)
	 */
	private boolean permitirEliminar;
	
	/**
	 * Variable para permitir la seleccion de paquetes ya seleccionados
	 */
	private boolean permitirRepetir;
	
	/**
	 * Método para limpiar los datos de la forma
	 *
	 */
	public void reset()
	{
		this.estado = "";
		this.numRegistros = 0;
		this.paquetes = new HashMap<String, Object>();
		this.ultimoPatron = "";
		this.patronOrdenar = "";
		this.linkSiguiente = "";
		this.maxPageItems = 0;
		
		this.permitirEliminar = false;
		this.permitirRepetir = false;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * @return the numRegistros
	 */
	public int getNumRegistros() {
		return numRegistros;
	}


	/**
	 * @param numRegistros the numRegistros to set
	 */
	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}


	/**
	 * @return the paquetes
	 */
	public HashMap<String, Object> getPaquetes() {
		return paquetes;
	}


	/**
	 * @param paquetes the paquetes to set
	 */
	public void setPaquetes(HashMap<String, Object> paquetes) {
		this.paquetes = paquetes;
	}
	
	/**
	 * @return the paquetes
	 */
	public Object getPaquetes(String key) {
		return paquetes.get(key);
	}


	/**
	 * @param paquetes the paquetes to set
	 */
	public void setPaquetes(String key,Object obj) {
		this.paquetes.put(key, obj);
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
	 * @return the codigoServicio
	 */
	public String getCodigoServicio() {
		return codigoServicio;
	}


	/**
	 * @param codigoServicio the codigoServicio to set
	 */
	public void setCodigoServicio(String codigoServicio) {
		this.codigoServicio = codigoServicio;
	}


	/**
	 * @return the parejasClaseGrupo
	 */
	public String getParejasClaseGrupo() {
		return parejasClaseGrupo;
	}


	/**
	 * @param parejasClaseGrupo the parejasClaseGrupo to set
	 */
	public void setParejasClaseGrupo(String parejasClaseGrupo) {
		this.parejasClaseGrupo = parejasClaseGrupo;
	}


	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}


	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}


	/**
	 * @return the nombreDiv
	 */
	public String getNombreDiv() {
		return nombreDiv;
	}


	/**
	 * @param nombreDiv the nombreDiv to set
	 */
	public void setNombreDiv(String nombreDiv) {
		this.nombreDiv = nombreDiv;
	}


	/**
	 * @return the nombreMapa
	 */
	public String getNombreMapa() {
		return nombreMapa;
	}


	/**
	 * @param nombreMapa the nombreMapa to set
	 */
	public void setNombreMapa(String nombreMapa) {
		this.nombreMapa = nombreMapa;
	}


	/**
	 * @return the numeroFilasMapa
	 */
	public int getNumeroFilasMapa() {
		return numeroFilasMapa;
	}


	/**
	 * @param numeroFilasMapa the numeroFilasMapa to set
	 */
	public void setNumeroFilasMapa(int numeroFilasMapa) {
		this.numeroFilasMapa = numeroFilasMapa;
	}


	/**
	 * @return the consecutivosPaquetesInsertados
	 */
	public String getConsecutivosPaquetesInsertados() {
		return consecutivosPaquetesInsertados;
	}


	/**
	 * @param consecutivosPaquetesInsertados the consecutivosPaquetesInsertados to set
	 */
	public void setConsecutivosPaquetesInsertados(
			String consecutivosPaquetesInsertados) {
		this.consecutivosPaquetesInsertados = consecutivosPaquetesInsertados;
	}


	/**
	 * @return the permitirEliminar
	 */
	public boolean isPermitirEliminar() {
		return permitirEliminar;
	}


	/**
	 * @param permitirEliminar the permitirEliminar to set
	 */
	public void setPermitirEliminar(boolean permitirEliminar) {
		this.permitirEliminar = permitirEliminar;
	}


	/**
	 * @return the permitirRepetir
	 */
	public boolean isPermitirRepetir() {
		return permitirRepetir;
	}


	/**
	 * @param permitirRepetir the permitirRepetir to set
	 */
	public void setPermitirRepetir(boolean permitirRepetir) {
		this.permitirRepetir = permitirRepetir;
	}
}
