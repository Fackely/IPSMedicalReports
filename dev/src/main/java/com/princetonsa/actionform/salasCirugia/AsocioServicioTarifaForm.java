package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author Jose Eduardo Arias Doncel
 *
 * Clase que almacena y carga la información utilizada para la funcionalidad
 * Asocios Servicios de Tarifa
 */

public class AsocioServicioTarifaForm extends ValidatorForm 
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private Logger logger = Logger.getLogger(ExTarifasAsociosForm.class);
	
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Código del convenio
	 */
	private int convenio;
	
	/**
	 * Nombre Convenio
	 * */
	private String nombreConvenio;
	
	/**
	 * Código de la institucion
	 */
	private String institucion;
	
	/**
	 * Seccion de Busqueda Avanzada
	 * */
	private boolean seccionBusquedaAvanzada;
	
	/**
	 * HashMap busquedaAvanzada
	 * */
	private HashMap busquedaAvanzadaMap;
	
	/**
	 * String Indicador de Ingreso
	 * */
	private String indicadorIngreso;
	
	/**
	 * Int numRegistros (pager)	  
	 * */
	private int numRegistros;
	
	/**
	 * int OffsetHash (page)
	 * */
	private int offsetHash;
	
	/**
	 * Link Siguiende del pager
	 * */
	private String linkSiguiente;
	
	
	/**
	 * String indice
	 * */
	private String indice;
	
	/**
	 * String ultimo indice
	 * */
	private String ultimoIndice;
	
	
	//**************Atributos del Encabezado
	
	/**
	 * ArrayList de esquemas Tarifarios
	 * */
	private ArrayList esquemasTarifariosArray;
	
	/**
	 * ArrayList de Convenio
	 * */
	private ArrayList convenioArray;
	
	/**
	 * HashMap del convenio 
	 * */
	private HashMap convenioMap;
	
	
	/**
	 * HashMap del Esquema Tarifario 
	 * */
	private HashMap esquemaTarifarioMap;
	
	/**
	 * String indice de la Vigencia
	 * */
	private String indiceVigencias;
	
	/**
	 * HashMap vigenciasMap
	 * */
	private HashMap vigenciasMap;
	
	
	/**
	 * String fechaInicial
	 * */
	private String fechaInicial;
	
	/**
	 * String fechaFinal
	 * */
	private String fechaFinal;

	
	//**************Atributos del Detalle	

	/**
	 * ArrayList tiposServicioArray
	 * */
	private ArrayList tiposServicioArray;
	
	/**
	 * HashMap detalle
	 * */
	private HashMap detalleMap;
	
	/**
	 * String indicador detalle
	 * */
	private String indicadorDetalle;	
	
	/**
	 * Arrat Asocios 
	 * */
	private ArrayList asociosArray;
	
	/**
	 * Array Grupo Servicio Array
	 * */
	private ArrayList grupoServicioArray;
	
	/**
	 * HashMap especialidad Map
	 * */
	private HashMap especialidadMap;

	
	
	
	//******************Metodos*********************


	public void reset()
	{
		this.estado="";
		this.esquemasTarifariosArray = new ArrayList();
		this.convenioArray = new ArrayList();
		this.convenioMap = new HashMap();
		this.esquemaTarifarioMap = new HashMap();
		this.indiceVigencias = "";
		this.vigenciasMap = new HashMap();
		this.tiposServicioArray = new ArrayList();
		this.detalleMap = new HashMap();
		this.detalleMap.put("numRegistros","0");
		this.indicadorDetalle = "";
		this.asociosArray = new ArrayList();
		this.fechaFinal = "";
		this.fechaInicial = "";
		this.busquedaAvanzadaMap= new HashMap ();
	}
	
	
	/**
	 * @return the tiposServicioArray
	 */
	public ArrayList getTiposServicioArray() {
		return tiposServicioArray;
	}


	/**
	 * @param tiposServicioArray the tiposServicioArray to set
	 */
	public void setTiposServicioArray(ArrayList tiposServicioArray) {
		this.tiposServicioArray = tiposServicioArray;
	}
	
	/**
	 * @return the busquedaAvanzadaMap
	 */
	public HashMap getBusquedaAvanzadaMap() {
		return busquedaAvanzadaMap;
	}

	/**
	 * @param busquedaAvanzadaMap the busquedaAvanzadaMap to set
	 */
	public void setBusquedaAvanzadaMap(HashMap busquedaAvanzadaMap) {
		this.busquedaAvanzadaMap = busquedaAvanzadaMap;
	}
	
	/**
	 * @return the busquedaAvanzadaMap
	 */
	public Object getBusquedaAvanzadaMap(String key) {
		return busquedaAvanzadaMap.get(key);
	}

	/**
	 * @param busquedaAvanzadaMap the busquedaAvanzadaMap to set
	 */
	public void setBusquedaAvanzadaMap(String key, Object value) {
		this.busquedaAvanzadaMap.put(key, value);
	}

	/**
	 * @return the convenio
	 */
	public int getConvenio() {
		return convenio;
	}

	/**
	 * @param convenio the convenio to set
	 */
	public void setConvenio(int convenio) {
		this.convenio = convenio;
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
	 * @return the institucion
	 */
	public String getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	/**
	 * @return the nombreConvenio
	 */
	public String getNombreConvenio() {
		return nombreConvenio;
	}

	/**
	 * @param nombreConvenio the nombreConvenio to set
	 */
	public void setNombreConvenio(String nombreConvenio) {
		this.nombreConvenio = nombreConvenio;
	}

	/**
	 * @return the seccionBusquedaAvanzada
	 */
	public boolean isSeccionBusquedaAvanzada() {
		return seccionBusquedaAvanzada;
	}

	/**
	 * @param seccionBusquedaAvanzada the seccionBusquedaAvanzada to set
	 */
	public void setSeccionBusquedaAvanzada(boolean seccionBusquedaAvanzada) {
		this.seccionBusquedaAvanzada = seccionBusquedaAvanzada;
	}


	/**
	 * @return the esquemasTarifariosArray
	 */
	public ArrayList getEsquemasTarifariosArray() {
		return esquemasTarifariosArray;
	}


	/**
	 * @param esquemasTarifariosArray the esquemasTarifariosArray to set
	 */
	public void setEsquemasTarifariosArray(ArrayList esquemasTarifariosArray) {
		this.esquemasTarifariosArray = esquemasTarifariosArray;
	}


	/**
	 * @return the convenioArray
	 */
	public ArrayList getConvenioArray() {
		return convenioArray;
	}


	/**
	 * @param convenioArray the convenioArray to set
	 */
	public void setConvenioArray(ArrayList convenioArray) {
		this.convenioArray = convenioArray;
	}


	/**
	 * @return the convenioMap
	 */
	public HashMap getConvenioMap() {
		return convenioMap;
	}


	/**
	 * @param convenioMap the convenioMap to set
	 */
	public void setConvenioMap(HashMap convenioMap) {
		this.convenioMap = convenioMap;
	}
	
	
	/**
	 * @return the convenioMap
	 */
	public Object getConvenioMap(String key) {
		return convenioMap.get(key);
	}


	/**
	 * @param convenioMap the convenioMap to set
	 */
	public void setConvenioMap(String key, Object value) {
		this.convenioMap.put(key, value);
	}


	/**
	 * @return the esquemaTarifarioMap
	 */
	public HashMap getEsquemaTarifarioMap() {
		return esquemaTarifarioMap;
	}


	/**
	 * @param esquemaTarifarioMap the esquemaTarifarioMap to set
	 */
	public void setEsquemaTarifarioMap(HashMap esquemaTarifarioMap) {
		this.esquemaTarifarioMap = esquemaTarifarioMap;
	}
	
	
	/**
	 * @return the esquemaTarifarioMap
	 */
	public Object  getEsquemaTarifarioMap(String key) {
		return esquemaTarifarioMap.get(key);
	}


	/**
	 * @param esquemaTarifarioMap the esquemaTarifarioMap to set
	 */
	public void setEsquemaTarifarioMap(String key, Object value) {
		this.esquemaTarifarioMap.put(key, value);
	}


	/**
	 * @return the indicadorIngreso
	 */
	public String getIndicadorIngreso() {
		return indicadorIngreso;
	}


	/**
	 * @param indicadorIngreso the indicadorIngreso to set
	 */
	public void setIndicadorIngreso(String indicadorIngreso) {
		this.indicadorIngreso = indicadorIngreso;
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
	 * @return the offsetHash
	 */
	public int getOffsetHash() {
		return offsetHash;
	}


	/**
	 * @param offsetHash the offsetHash to set
	 */
	public void setOffsetHash(int offsetHash) {
		this.offsetHash = offsetHash;
	}


	/**
	 * @return the indiceVigencias
	 */
	public String getIndiceVigencias() {
		return indiceVigencias;
	}


	/**
	 * @param indiceVigencias the indiceVigencias to set
	 */
	public void setIndiceVigencias(String indiceVigencias) {
		this.indiceVigencias = indiceVigencias;
	}


	/**
	 * @return the vigenciasMap
	 */
	public HashMap getVigenciasMap() {
		return vigenciasMap;
	}


	/**
	 * @param vigenciasMap the vigenciasMap to set
	 */
	public void setVigenciasMap(HashMap vigenciasMap) {
		this.vigenciasMap = vigenciasMap;
	}
	
	/**
	 * @return the vigenciasMap
	 */
	public Object getVigenciasMap(String key) {
		return vigenciasMap.get(key);
	}


	/**
	 * @param vigenciasMap the vigenciasMap to set
	 */
	public void setVigenciasMap(String key, Object value) {
		this.vigenciasMap.put(key, value);
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
	 * @return the indice
	 */
	public String getIndice() {
		return indice;
	}


	/**
	 * @param indice the indice to set
	 */
	public void setIndice(String indice) {
		this.indice = indice;
	}


	/**
	 * @return the ultimoIndice
	 */
	public String getUltimoIndice() {
		return ultimoIndice;
	}


	/**
	 * @param ultimoIndice the ultimoIndice to set
	 */
	public void setUltimoIndice(String ultimoIndice) {
		this.ultimoIndice = ultimoIndice;
	}


	/**
	 * @return the detalleMap
	 */
	public HashMap getDetalleMap() {
		return detalleMap;
	}


	/**
	 * @param detalleMap the detalleMap to set
	 */
	public void setDetalleMap(HashMap detalleMap) {
		this.detalleMap = detalleMap;
	}	
	
	
	/**
	 * @return the detalleMap
	 */
	public Object getDetalleMap(String key) {
		return detalleMap.get(key);
	}


	/**
	 * @param detalleMap the detalleMap to set
	 */
	public void setDetalleMap(String key, Object value) {
		this.detalleMap.put(key, value);
	}


	/**
	 * @return the indicadorDetalle
	 */
	public String getIndicadorDetalle() {
		return indicadorDetalle;
	}


	/**
	 * @param indicadorDetalle the indicadorDetalle to set
	 */
	public void setIndicadorDetalle(String indicadorDetalle) {
		this.indicadorDetalle = indicadorDetalle;
	}


	/**
	 * @return the asociosArray
	 */
	public ArrayList getAsociosArray() {
		return asociosArray;
	}


	/**
	 * @param asociosArray the asociosArray to set
	 */
	public void setAsociosArray(ArrayList asociosArray) {
		this.asociosArray = asociosArray;
	}


	/**
	 * @return the grupoServicioArray
	 */
	public ArrayList getGrupoServicioArray() {
		return grupoServicioArray;
	}


	/**
	 * @param grupoServicioArray the grupoServicioArray to set
	 */
	public void setGrupoServicioArray(ArrayList grupoServicioArray) {
		this.grupoServicioArray = grupoServicioArray;
	}


	/**
	 * @return the especialidadMap
	 */
	public HashMap getEspecialidadMap() {
		return especialidadMap;
	}


	/**
	 * @param especialidadMap the especialidadMap to set
	 */
	public void setEspecialidadMap(HashMap especialidadMap) {
		this.especialidadMap = especialidadMap;
	}
	
	
	/**
	 * @return the especialidadMap
	 */
	public Object getEspecialidadMap(String key) {
		return especialidadMap.get(key);
	}


	/**
	 * @param especialidadMap the especialidadMap to set
	 */
	public void setEspecialidadMap(String key, Object value) {
		this.especialidadMap.put(key, value);
	}


	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}


	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}


	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
}