package util.Busqueda;

import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;

public class BusquedaCondicionTmExamGenericaForm extends ValidatorForm 
{
	
	//--- Atributos 
	
	/**
	 * Estado de la forma enviada al Action
	 * */
	private String estado;
	
	/**
	 * Codigo de Condicion de Toma de Examen (consulta)
	 * */
	private String codigoC;
	
	/**
	 * Descripcion de la Condicion de Toma de Examen (consulta)
	 * */
	private String descripcionC;
	
	/**
	 * Estado de la condicion de toma de examen (consulta)
	 * */
	private String activoC;	
	
	/**
	 * Mapa de Consulta  
	 */
	HashMap consultaMap;
	
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
	
	
	//Atributos que envia el usuario
	private String hagoSubmit="";
	private String nombreForma="";
	private String nombreMapa="";
	private String idNumFilasMapaExamenes="";
	private String codigosExamenesInsertados="";
	
	//--- Fin Atributos 
	
	//--- Metodos 
	
	/**
	 * Inicializa los atributos de la forma
	 * */
	public void reset()
	{
		consultaMap = new HashMap();
		consultaMap.put("numRegistros",0);		
		this.codigoC="";
		this.descripcionC="";
		this.activoC="";
		this.estado="";
		this.ultimoPatron="";
		this.patronOrdenar="";
		this.linkSiguiente="";
		
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
	 * @return the consultaMap
	 */
	public HashMap getConsultaMap() {
		return consultaMap;
	}

	/**
	 * @param consultaMap the consultaMap to set
	 */
	public void setConsultaMap(HashMap consultaMap) {
		this.consultaMap = consultaMap;
	}
	
	/**
	 * @return the Object
	 */
	public Object getConsultaMap(String key) {
		return consultaMap.get(key);
	}

	/**
	 * @param String key
	 * @param Object value
	 */
	public void setConsultaMap(String key, Object value) {
		this.consultaMap.put(key, value);
	}

	/**
	 * @return the activoC
	 */
	public String getActivoC() {
		return activoC;
	}

	/**
	 * @param activoC the activoC to set
	 */
	public void setActivoC(String activoC) {
		this.activoC = activoC;
	}

	/**
	 * @return the codigoExamenCtC
	 */
	public String getCodigoC() {
		return codigoC;
	}

	/**
	 * @param codigoExamenCtC the codigoExamenCtC to set
	 */
	public void setCodigoC(String codigoC) {
		this.codigoC = codigoC;
	}

	/**
	 * @return the descripcionExamenCtC
	 */
	public String getDescripcionC() {
		return descripcionC;
	}

	/** 
	 * @param descripcionExamenCtC the descripcionExamenCtC to set
	 */
	public void setDescripcionC(String descripcionC) {
		this.descripcionC = descripcionC;
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
	 * @return the codigosExamenesInsertados
	 */
	public String getCodigosExamenesInsertados() {
		return codigosExamenesInsertados;
	}

	/**
	 * @param codigosExamenesInsertados the codigosExamenesInsertados to set
	 */
	public void setCodigosExamenesInsertados(String codigosExamenesInsertados) {
		this.codigosExamenesInsertados = codigosExamenesInsertados;
	}

	public String getHagoSubmit() {
		return hagoSubmit;
	}

	public void setHagoSubmit(String hagoSubmit) {
		this.hagoSubmit = hagoSubmit;
	}

	public String getIdNumFilasMapaExamenes() {
		return idNumFilasMapaExamenes;
	}

	public void setIdNumFilasMapaExamenes(String idNumFilasMapaExamenes) {
		this.idNumFilasMapaExamenes = idNumFilasMapaExamenes;
	}

	public String getNombreForma() {
		return nombreForma;
	}

	public void setNombreForma(String nombreForma) {
		this.nombreForma = nombreForma;
	}

	public String getNombreMapa() {
		return nombreMapa;
	}

	public void setNombreMapa(String nombreMapa) {
		this.nombreMapa = nombreMapa;
	}

	//--- Fin Metodos	
}