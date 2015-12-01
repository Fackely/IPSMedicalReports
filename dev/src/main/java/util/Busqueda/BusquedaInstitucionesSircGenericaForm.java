package util.Busqueda;

import java.util.HashMap;
import org.apache.struts.validator.ValidatorForm;

public class BusquedaInstitucionesSircGenericaForm extends ValidatorForm 
{
	
	//--- Atributos 
	
	/**
	 * Estado de la forma enviada al Action
	 * */
	private String estado;
	
	/**
	 * Mapa de Consulta  
	 */
	HashMap consultaMap;
	
	/**
	 * Variable contiene los codigos seleccionados
	 * */
	private String codigosInstitucionesInsertados;
	
	/**
	 * Tipo de Institucion de Referencia SIRC (consulta)
	 * */
	private String tipoinstrefC;
	
	/**
	 * Tipo de Institucion Ambulancia SIRC (consulta)
	 * */
	private String tipoinstambC;
	
	/**
	 * Opcion not in, indica los codigos de institucion Sirc relacionadas con otras tablas que no seran tomadas encuenta para la 
	 * */
	private String opcionNotIn;
	
	
	//---------Paremetros de la Busqueda
	/**
	 * 
	 * */
	private String nombreForma="BusquedaInstitucionesSircGenericaForm";
	
	/**
	 * 
	 * */
	private String identificador="institucionesSirc";
	
	/**
	 * 
	 * */
	private String nombreMapa="institucionTramiteMap";
	
	/**
	 * 
	 * */
	private String nombreTabla="tablaInstitucionesSirc";
	
	/**
	 * 
	 * */
	private String nombreCampoNumInst="numeroElementosInstitucionSirc";
	
	/**
	 * 
	 * */
	private String nombreCampoCodigoInsertados = "codigosInstitucionesInsertados";
	
	/**
	 * 
	 * */
	private String onClickEvent="abrirDetalleTramite";

	
	//------------------------
	
	//----- Pager
	
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
	
   //	----- Fin Pager	
	
	//--- Fin Atributos 
	
	//--- Metodos 
	
	/**
	 * Inicializa los atributos de la forma
	 * */
	public void reset()
	{
		consultaMap = new HashMap();
		consultaMap.put("numRegistros",0);		
		this.estado="";
		this.ultimoPatron="";
		this.patronOrdenar="";
		this.linkSiguiente="";
	}

	/**
	 * @return the codigosInstitucionesInsertados
	 */
	public String getCodigosInstitucionesInsertados() {
		return codigosInstitucionesInsertados;
	}

	/**
	 * @param codigosInstitucionesInsertados the codigosInstitucionesInsertados to set
	 */
	public void setCodigosInstitucionesInsertados(
			String codigosInstitucionesInsertados) {
		this.codigosInstitucionesInsertados = codigosInstitucionesInsertados;
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
	 * @return the consultaMap
	 */
	public Object getConsultaMap(String key) {
		return consultaMap.get(key);
	}

	/**
	 * @param consultaMap the consultaMap to set
	 */
	public void setConsultaMap(String key, Object value) {
		this.consultaMap.put(key, value);
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
	 * @return the tipoinstambC
	 */
	public String getTipoinstambC() {
		return tipoinstambC;
	}

	/**
	 * @param tipoinstambC the tipoinstambC to set
	 */
	public void setTipoinstambC(String tipoinstambC) {
		this.tipoinstambC = tipoinstambC;
	}

	/**
	 * @return the tipoinstrefC
	 */
	public String getTipoinstrefC() {
		return tipoinstrefC;
	}

	/**
	 * @param tipoinstrefC the tipoinstrefC to set
	 */
	public void setTipoinstrefC(String tipoinstrefC) {
		this.tipoinstrefC = tipoinstrefC;
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
	 * @return the nombreTabla
	 */
	public String getNombreTabla() {
		return nombreTabla;
	}

	/**
	 * @param nombreTabla the nombreTabla to set
	 */
	public void setNombreTabla(String nombreTabla) {
		this.nombreTabla = nombreTabla;
	}

	/**
	 * @return the opcionNotInt
	 */
	public String getOpcionNotIn() {
		return opcionNotIn;
	}

	/**
	 * @param opcionNotInt the opcionNotInt to set
	 */
	public void setOpcionNotIn(String opcionNotIn) {
		this.opcionNotIn = opcionNotIn;
	}

	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador the identificador to set
	 */
	public void setIdentificador(String identificador) {
		this.identificador = identificador;
	}

	/**
	 * @return the nombreCampoNumInst
	 */
	public String getNombreCampoNumInst() {
		return nombreCampoNumInst;
	}

	/**
	 * @param nombreCampoNumInst the nombreCampoNumInst to set
	 */
	public void setNombreCampoNumInst(String nombreCampoNumInst) {
		this.nombreCampoNumInst = nombreCampoNumInst;
	}

	/**
	 * @return the nombreForma
	 */
	public String getNombreForma() {
		return nombreForma;
	}

	/**
	 * @param nombreForma the nombreForma to set
	 */
	public void setNombreForma(String nombreForma) {
		this.nombreForma = nombreForma;
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
	 * @return the onClickEvent
	 */
	public String getOnClickEvent() {
		return onClickEvent;
	}

	/**
	 * @param onClickEvent the onClickEvent to set
	 */
	public void setOnClickEvent(String onClickEvent) {
		this.onClickEvent = onClickEvent;
	}

	/**
	 * @return the nombreCampoCodigoInsertados
	 */
	public String getNombreCampoCodigoInsertados() {
		return nombreCampoCodigoInsertados;
	}

	/**
	 * @param nombreCampoCodigoInsertados the nombreCampoCodigoInsertados to set
	 */
	public void setNombreCampoCodigoInsertados(String nombreCampoCodigoInsertados) {
		this.nombreCampoCodigoInsertados = nombreCampoCodigoInsertados;
	}
}	