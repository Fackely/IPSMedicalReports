package com.princetonsa.actionform.facturacion;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadTexto;

public class ParamArchivoPlanoIndCalidadForm extends ValidatorForm
{
	Logger logger = Logger.getLogger(ParamArchivoPlanoIndCalidadForm.class);
	
	/**
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Mapa para la Consulta de Ind Especialidad
	 */
	private HashMap especialidadMap;
	
	/**
	 * Mapa para la Consulta de Ind Centro Costo
	 */
	private HashMap centroCostoMap;
	
	/**
	 * Mapa para la Consulta de Ind Diagnostico
	 */
	private HashMap diagnosticoMap;
	
	/**
	 * Codigo del Indicador
	 */
	private String codigo;
	
	/**
	 * Mapa para la Consulta de los codigos de los Indicadores
	 */
	private HashMap codigosMap;
	
	/**
	 * Mapa para la Consulta de las Especialidades
	 */
	private HashMap especialidadesMap;
	
	/**
	 * Mapa para almacenar N Especialidades seleccionadas
	 */
	private HashMap especialidadesGeneradasMap;
	
	/**
	 * Numero de Especialidades Generadas
	 */
	private int numEspecialidades;
	
	/**
	 * Mapa para la Consulta de los Centros de Costo
	 */
	private HashMap centrosCostoMap;
	
	/**
	 * Mapa para almacenar N Centros de Costo seleccionados
	 */
	private HashMap centrosGeneradosMap;
	
	/**
	 * Numero de Centros Generados
	 */
	private int numCentros;
	
	/**
	 * Mapa para la Consulta de los Diagnosticos
	 */
	private HashMap diagnosticosMap;
	
	/**
	 * Mapa para almacenar N Diagnosticos Generedos
	 */
	private HashMap diagnosticosGeneradosMap;
	
	/**
	 * Numero de Diagnosticos Generados
	 */
	private int numDiagnosticos;
	
	/**
	 * Para manejar los diagnosticos definitivos principal y relacionados. El
	 * valor viene de forma 'codigo- nombre'
	 */
	private Map diagnosticosDefinitivos = new HashMap();
	
	/**
	 * Entero para saber cuantos diagnosticos definitivos se 
	 * generaron dinámicamente 
	 */
	private int numDiagnosticosDefinitivos = 0;
	
	/**
	 * Mapa para la Consulta de los Generales
	 */
	private HashMap generalesMap;
	
	/**
	 * Atributo para el codigo del Indicador
	 */
	private String acronimo;
	
	/**
	 * Atributo para la descripcion del Indicador
	 */
	private String descripcion;
	
	/**
	 * Atributo para la Especialidad
	 */
	private String especialidad;
	
	/**
	 * Atributo para el Centro de Costo
	 */
	private String centroCosto;
	
	/**
	 * Atributo para el Diagnostico
	 */
	private String diagnostico;
	
	/**
	 * Atributo para el Tad
	 */
	private String tad;
	
	/**
	 * Atributo para el Tas
	 */
	private String tas;
	
	/**
	 * Atributo para el MTad
	 */
	private String mtad;
	
	/**
	 * Atributo para el MTas
	 */
	private String mtas;
	
	/**
	 * Atributo para el Reporte de Cero
	 */
	private String cero;
	
	/**
	 * Atributo Cie del Diagnostico
	 */
	private String cie;
	
	/**
	 * Index posicion del Select Codigos Ind
	 */
	private String index;
	
	/**
	 * Index del registro del mapa Consulta P 
	 */
	private String indexMap;
	
	/**
	 * Guia de nuevo registro Generales
	 */
	private String guiaG; 
	
	
	
	/**
	 * Metodo para resetaer los valores de la forma 
	 */
	public void reset( int codigoInstitucion)
	{
		this.acronimo="";
		this.descripcion="";
		this.especialidad="";
		this.centroCosto="";
		this.diagnostico="";
		this.cie="";
		this.especialidadMap= new HashMap();
		especialidadMap.put("numRegistros", 0);
		this.especialidadesMap= new HashMap();
		especialidadesMap.put("numRegistros", 0);
		this.codigosMap= new HashMap();
		codigosMap.put("numRegistros", 0);		
		this.centroCostoMap= new HashMap();
		centroCostoMap.put("numRegistros", 0);
		this.centrosCostoMap= new HashMap();
		centrosCostoMap.put("numRegistros", 0);
		this.diagnosticoMap= new HashMap();
		diagnosticoMap.put("numRegistros", 0);
		this.diagnosticosMap= new HashMap();
		diagnosticosMap.put("numRegistros", 0);
		this.generalesMap= new HashMap();
		generalesMap.put("numRegistros", 0);
		this.especialidadesGeneradasMap=new HashMap();
		especialidadesGeneradasMap.put("numRegistros", 0);
		this.centrosGeneradosMap=new HashMap();
		centrosGeneradosMap.put("numRegistros", 0);
		this.diagnosticosGeneradosMap=new HashMap();
		diagnosticosGeneradosMap.put("numRegistros", 0);
		diagnosticosDefinitivos = new HashMap();
		this.numEspecialidades=0;
		this.numCentros=0;
		this.numDiagnosticos=0;
		this.tad="";
		this.tas="";
		this.mtad="";
		this.mtas="";
		this.cero="";
		
		this.index="";
		this.indexMap="";
		this.guiaG="n";
	}
	
	/**
	 * 
	 *
	 */
	public void resetMapasGenerados()
	{
		this.especialidadesGeneradasMap=new HashMap();
		especialidadesGeneradasMap.put("numRegistros", 0);
		this.centrosGeneradosMap=new HashMap();
		centrosGeneradosMap.put("numRegistros", 0);
		this.diagnosticosGeneradosMap=new HashMap();
		diagnosticosGeneradosMap.put("numRegistros", 0);
		this.numEspecialidades=0;
		this.numCentros=0;
		this.numDiagnosticos=0;
	}
	
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public HashMap getEspecialidadMap() {
		return especialidadMap;
	}

	public void setEspecialidadMap(HashMap especialidadMap) {
		this.especialidadMap = especialidadMap;
	}
	
	public Object getEspecialidadMap(String key) {
		return especialidadMap.get(key);
	}

	public void setEspecialidadMap(String key, Object value) {
		this.especialidadMap.put(key, value);
	}

	public String getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public String getEspecialidad() {
		return especialidad;
	}

	public void setEspecialidad(String especialidad) {
		this.especialidad = especialidad;
	}

	public HashMap getCodigosMap() {
		return codigosMap;
	}

	public void setCodigosMap(HashMap codigosMap) {
		this.codigosMap = codigosMap;
	}
	
	public Object getCodigosMap(String key) {
		return codigosMap.get(key);
	}

	public void setCodigosMap(String key, Object value) {
		this.codigosMap.put(key, value);
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public HashMap getEspecialidadesMap() {
		return especialidadesMap;
	}

	public void setEspecialidadesMap(HashMap especialidadesMap) {
		this.especialidadesMap = especialidadesMap;
	}
	
	public Object getEspecialidadesMap(String key) {
		return especialidadesMap.get(key);
	}

	public void setEspecialidadesMap(String key, Object value) {
		this.especialidadesMap.put(key, value);
	}

	public String getIndexMap() {
		return indexMap;
	}
	
	public void setIndexMap(String indexMap) {
		this.indexMap = indexMap;
	}

	public HashMap getCentroCostoMap() {
		return centroCostoMap;
	}

	public void setCentroCostoMap(HashMap centroCostoMap) {
		this.centroCostoMap = centroCostoMap;
	}
	
	public Object getCentroCostoMap(String key) {
		return centroCostoMap.get(key);
	}

	public void setCentroCostoMap(String key, Object value) {
		this.centroCostoMap.put(key, value);
	}
	
	public HashMap getCentrosCostoMap() {
		return centrosCostoMap;
	}

	public void setCentrosCostoMap(HashMap centrosCostoMap) {
		this.centrosCostoMap = centrosCostoMap;
	}
	
	public Object getCentrosCostoMap(String key) {
		return centrosCostoMap.get(key);
	}

	public void setCentrosCostoMap(String key, Object value) {
		this.centrosCostoMap.put(key, value);
	}

	public String getCentroCosto() {
		return centroCosto;
	}

	public void setCentroCosto(String centroCosto) {
		this.centroCosto = centroCosto;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}
	
	public HashMap getDiagnosticoMap() {
		return diagnosticoMap;
	}

	public void setDiagnosticoMap(HashMap diagnosticoMap) {
		this.diagnosticoMap = diagnosticoMap;
	}
	
	public Object getDiagnosticoMap(String key) {
		return diagnosticoMap.get(key);
	}

	public void setDiagnosticoMap(String key, Object value) {
		this.diagnosticoMap.put(key, value);
	}

	public HashMap getDiagnosticosMap() {
		return diagnosticosMap;
	}
	
	public void setDiagnosticosMap(HashMap diagnosticosMap) {
		this.diagnosticosMap = diagnosticosMap;
	}
	
	public Object getDiagnosticosMap(String key) {
		return diagnosticosMap.get(key);
	}

	public void setDiagnosticosMap(String key, Object value) {
		this.diagnosticosMap.put(key, value);
	}

	public String getCie() {
		return cie;
	}

	public void setCie(String cie) {
		this.cie = cie;
	}

	public String getCero() {
		return cero;
	}

	public void setCero(String cero) {
		this.cero = cero;
	}

	public HashMap getGeneralesMap() {
		return generalesMap;
	}

	public void setGeneralesMap(HashMap generalesMap) {
		this.generalesMap = generalesMap;
	}
	
	public Object getGeneralesMap(String key) {
		return generalesMap.get(key);
	}

	public void setGeneralesMap(String key, Object value) {
		this.generalesMap.put(key, value);
	}

	public String getMtad() {
		return mtad;
	}

	public void setMtad(String mtad) {
		this.mtad = mtad;
	}

	public String getMtas() {
		return mtas;
	}

	public void setMtas(String mtas) {
		this.mtas = mtas;
	}

	public String getTad() {
		return tad;
	}

	public void setTad(String tad) {
		this.tad = tad;
	}

	public String getTas() {
		return tas;
	}

	public void setTas(String tas) {
		this.tas = tas;
	}
	
	public HashMap getEspecialidadesGeneradasMap() {
		return especialidadesGeneradasMap;
	}
	
	public void setEspecialidadesGeneradasMap(HashMap especialidadesGeneradasMap) {
		this.especialidadesGeneradasMap = especialidadesGeneradasMap;
	}
	
	public Object getEspecialidadesGeneradasMap(String key) {
		return especialidadesGeneradasMap.get(key);
	}

	public void setEspecialidadesGeneradasMap(String key, Object value) {
		this.especialidadesGeneradasMap.put(key, value);
	}

	public int getNumEspecialidades() {
		return numEspecialidades;
	}

	public void setNumEspecialidades(int numEspecialidades) {
		this.numEspecialidades = numEspecialidades;
	}

	public HashMap getCentrosGeneradosMap() {
		return centrosGeneradosMap;
	}

	public void setCentrosGeneradosMap(HashMap centrosGeneradosMap) {
		this.centrosGeneradosMap = centrosGeneradosMap;
	}
	
	public Object getCentrosGeneradosMap(String key) {
		return centrosGeneradosMap.get(key);
	}

	public void setCentrosGeneradosMap(String key, Object value) {
		this.centrosGeneradosMap.put(key, value);
	}

	public HashMap getDiagnosticosGeneradosMap() {
		return diagnosticosGeneradosMap;
	}

	public void setDiagnosticosGeneradosMap(HashMap diagnosticosGeneradosMap) {
		this.diagnosticosGeneradosMap = diagnosticosGeneradosMap;
	}
	
	public Object getDiagnosticosGeneradosMap(String key) {
		return diagnosticosGeneradosMap.get(key);
	}

	public void setDiagnosticosGeneradosMap(String key, Object value) {
		this.diagnosticosGeneradosMap.put(key, value);
	}

	public int getNumCentros() {
		return numCentros;
	}

	public void setNumCentros(int numCentros) {
		this.numCentros = numCentros;
	}

	public int getNumDiagnosticos() {
		return numDiagnosticos;
	}

	public void setNumDiagnosticos(int numDiagnosticos) {
		this.numDiagnosticos = numDiagnosticos;
	}
	
	/**
	 * @return the diagnosticosDefinitivos
	 */
	public Map getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}

	/**
	 * @param diagnosticosDefinitivos the diagnosticosDefinitivos to set
	 */
	public void setDiagnosticosDefinitivos(Map diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}
	
	/**
	 * @return the numDiagnosticosDefinitivos
	 */
	public int getNumDiagnosticosDefinitivos() {
		return numDiagnosticosDefinitivos;
	}

	/**
	 * @param numDiagnosticosDefinitivos the numDiagnosticosDefinitivos to set
	 */
	public void setNumDiagnosticosDefinitivos(int numDiagnosticosDefinitivos) {
		this.numDiagnosticosDefinitivos = numDiagnosticosDefinitivos;
	}
	
	/**
	 * * Asigna un diagnostico definitivo (ppal o relacionado)
	 */
	public void setDiagnosticoDefinitivo(String key, Object value) 
	{
		diagnosticosDefinitivos.put(key, value);
	}
	
	/**
	 * Retorna el diagnostico definitivo (ppal o relacionado) asociado a la
	 * llave dada
	 */
	public Object getDiagnosticoDefinitivo(String key) 
	{
		return diagnosticosDefinitivos.get(key);
	}

	public String getGuiaG() {
		return guiaG;
	}

	public void setGuiaG(String guiaG) {
		this.guiaG = guiaG;
	}
}