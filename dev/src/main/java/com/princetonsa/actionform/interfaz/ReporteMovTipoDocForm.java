package com.princetonsa.actionform.interfaz;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.interfaz.DtoMovimientoTipoDocumento;
import com.princetonsa.mundo.interfaz.ReporteMovTipoDoc;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadFecha;

/**
 * @author Jairo Gómez Fecha Junio de 2009
 */

public class ReporteMovTipoDocForm extends ValidatorForm {
	// *************** Declaracion de variables ***************
	private ReporteMovTipoDoc mundoReporteMovTipoDoc = new ReporteMovTipoDoc();
	
	private String estado;

	private ResultadoBoolean mensaje;

	String[] indicesCriterios = mundoReporteMovTipoDoc.indicesCriterios;

	private HashMap criterios = new HashMap();
	
	private ArrayList<HashMap<String, Object>> tipoDoc = new ArrayList<HashMap<String, Object>>();
	
	private ArrayList<DtoMovimientoTipoDocumento> arrayFacturasPacientes;
	
	private ArrayList<DtoMovimientoTipoDocumento> arrayIngresos;
	
	private ArrayList<DtoMovimientoTipoDocumento> arrayRecibosCaja;
	
	private ArrayList<DtoMovimientoTipoDocumento> arrayFacturasVarias;
	
	private int tamanioPacientesIngresos;
	
	private int tamanioArrayFacturasPacientes;
	
	private int tamanioArrayIngresos;
	
	private int tamanioArrayRecibosCaja;
	
	private int tamanioArrayFacturasVarias;
	
	private String pathArchivoPlano;
	
	private String urlArchivoPlano;
	
	private boolean existeArchivo;
	
	private boolean operacionTrue;
	
	private String criteriosConsulta;
	
	private String[] tipoDocumento;

	// ************ Fin Declaracion de variables **************

	/**
	 * Metodo que inicializa todas las variables.
	 */
	public void reset() {
		
		String fechaSistema = UtilidadFecha.getFechaActual();
		this.estado = "";
		this.mensaje = new ResultadoBoolean(false);
		this.criterios = new HashMap();
		this.tipoDoc = new ArrayList<HashMap<String, Object>>();
		this.setCriterios(indicesCriterios[0], fechaSistema);
		this.setCriterios(indicesCriterios[1], fechaSistema);
		this.arrayFacturasPacientes = new ArrayList<DtoMovimientoTipoDocumento>();
		this.arrayIngresos = new ArrayList<DtoMovimientoTipoDocumento>();
		this.arrayRecibosCaja = new ArrayList<DtoMovimientoTipoDocumento>();
		this.arrayFacturasVarias = new ArrayList<DtoMovimientoTipoDocumento>();
		this.tamanioPacientesIngresos = 0;
		this.tamanioArrayFacturasPacientes = 0;
		this.tamanioArrayIngresos = 0;
		this.tamanioArrayRecibosCaja = 0;
		this.tamanioArrayFacturasVarias = 0;
		this.pathArchivoPlano = "";
		this.urlArchivoPlano = "";
		this.existeArchivo = false;
		this.operacionTrue = false;
		this.criteriosConsulta = "";
		this.tipoDocumento = new String[]{};
		
	}

	/**
	 * Validate
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		return null;
	}

	// *************** Declaracion de Metodos Get y Set ***************

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado
	 *            the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * @return the mensaje
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje
	 *            the mensaje to set
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * @return the indicesCriterios
	 */
	public String[] getIndicesCriterios() {
		return indicesCriterios;
	}

	/**
	 * @param indicesCriterios
	 *            the indicesCriterios to set
	 */
	public void setIndicesCriterios(String[] indicesCriterios) {
		this.indicesCriterios = indicesCriterios;
	}

	/**
	 * @return the criterios
	 */
	public Object getCriterios(String key) {
		return criterios.get(key);
	}

	/**
	 * @param criterios
	 *            the criterios to set
	 */
	public void setCriterios(String key, Object value) {
		this.criterios.put(key, value);
	}

	/**
	 * @return the criterios
	 */
	public HashMap getCriterios() {
		return criterios;
	}

	/**
	 * @param criterios
	 *            the criterios to set
	 */
	public void setCriterios(HashMap criterios) {
		this.criterios = criterios;
	}

	/**
	 * @return the tipoDoc
	 */
	public ArrayList<HashMap<String, Object>> getTipoDoc() {
		return tipoDoc;
	}

	/**
	 * @param tipoDoc the tipoDoc to set
	 */
	public void setTipoDoc(ArrayList<HashMap<String, Object>> tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getTipoDoc(int key) {
		return tipoDoc.get(key);
	}

	/**
	 * 
	 * @param mapa
	 */
	public void setTipoDoc(HashMap<String, Object> mapa) {
		this.tipoDoc.add(mapa);
	}

	/**
	 * @return the arrayFacturasPacientes
	 */
	public ArrayList<DtoMovimientoTipoDocumento> getArrayFacturasPacientes() {
		return arrayFacturasPacientes;
	}

	/**
	 * @param arrayFacturasPacientes the arrayFacturasPacientes to set
	 */
	public void setArrayFacturasPacientes(
			ArrayList<DtoMovimientoTipoDocumento> arrayFacturasPacientes) {
		this.arrayFacturasPacientes = arrayFacturasPacientes;
	}

	/**
	 * @return the arrayIngresos
	 */
	public ArrayList<DtoMovimientoTipoDocumento> getArrayIngresos() {
		return arrayIngresos;
	}

	/**
	 * @param arrayIngresos the arrayIngresos to set
	 */
	public void setArrayIngresos(ArrayList<DtoMovimientoTipoDocumento> arrayIngresos) {
		this.arrayIngresos = arrayIngresos;
	}

	/**
	 * @return the arrayRecibosCaja
	 */
	public ArrayList<DtoMovimientoTipoDocumento> getArrayRecibosCaja() {
		return arrayRecibosCaja;
	}

	/**
	 * @param arrayRecibosCaja the arrayRecibosCaja to set
	 */
	public void setArrayRecibosCaja(
			ArrayList<DtoMovimientoTipoDocumento> arrayRecibosCaja) {
		this.arrayRecibosCaja = arrayRecibosCaja;
	}

	/**
	 * @return the arrayFacturasVarias
	 */
	public ArrayList<DtoMovimientoTipoDocumento> getArrayFacturasVarias() {
		return arrayFacturasVarias;
	}

	/**
	 * @param arrayFacturasVarias the arrayFacturasVarias to set
	 */
	public void setArrayFacturasVarias(
			ArrayList<DtoMovimientoTipoDocumento> arrayFacturasVarias) {
		this.arrayFacturasVarias = arrayFacturasVarias;
	}

	/**
	 * @return the tamanioArrayFacturasPacientes
	 */
	public int getTamanioArrayFacturasPacientes() {
		return tamanioArrayFacturasPacientes;
	}

	/**
	 * @param tamanioArrayFacturasPacientes the tamanioArrayFacturasPacientes to set
	 */
	public void setTamanioArrayFacturasPacientes(int tamanioArrayFacturasPacientes) {
		this.tamanioArrayFacturasPacientes = tamanioArrayFacturasPacientes;
	}

	/**
	 * @return the tamanioArrayIngresos
	 */
	public int getTamanioArrayIngresos() {
		return tamanioArrayIngresos;
	}

	/**
	 * @param tamanioArrayIngresos the tamanioArrayIngresos to set
	 */
	public void setTamanioArrayIngresos(int tamanioArrayIngresos) {
		this.tamanioArrayIngresos = tamanioArrayIngresos;
	}

	/**
	 * @return the tamanioArrayRecibosCaja
	 */
	public int getTamanioArrayRecibosCaja() {
		return tamanioArrayRecibosCaja;
	}

	/**
	 * @param tamanioArrayRecibosCaja the tamanioArrayRecibosCaja to set
	 */
	public void setTamanioArrayRecibosCaja(int tamanioArrayRecibosCaja) {
		this.tamanioArrayRecibosCaja = tamanioArrayRecibosCaja;
	}

	/**
	 * @return the tamanioArrayFacturasVarias
	 */
	public int getTamanioArrayFacturasVarias() {
		return tamanioArrayFacturasVarias;
	}

	/**
	 * @param tamanioArrayFacturasVarias the tamanioArrayFacturasVarias to set
	 */
	public void setTamanioArrayFacturasVarias(int tamanioArrayFacturasVarias) {
		this.tamanioArrayFacturasVarias = tamanioArrayFacturasVarias;
	}

	/**
	 * @return the tamanioPacientesIngresos
	 */
	public int getTamanioPacientesIngresos() {
		return tamanioPacientesIngresos;
	}

	/**
	 * @param tamanioPacientesIngresos the tamanioPacientesIngresos to set
	 */
	public void setTamanioPacientesIngresos(int tamanioPacientesIngresos) {
		this.tamanioPacientesIngresos = tamanioPacientesIngresos;
	}

	/**
	 * @return the ruta
	 */
	public String getPathArchivoPlano() {
		return pathArchivoPlano;
	}

	/**
	 * @param ruta the ruta to set
	 */
	public void setPathArchivoPlano(String ruta) {
		this.pathArchivoPlano = ruta;
	}

	/**
	 * @return the urlArchivo
	 */
	public String getUrlArchivoPlano() {
		return urlArchivoPlano;
	}

	/**
	 * @param urlArchivo the urlArchivo to set
	 */
	public void setUrlArchivoPlano(String urlArchivo) {
		this.urlArchivoPlano = urlArchivo;
	}

	/**
	 * @return the existeArchivo
	 */
	public boolean isExisteArchivo() {
		return existeArchivo;
	}

	/**
	 * @param existeArchivo the existeArchivo to set
	 */
	public void setExisteArchivo(boolean existeArchivo) {
		this.existeArchivo = existeArchivo;
	}

	/**
	 * @return the operacionTrue
	 */
	public boolean isOperacionTrue() {
		return operacionTrue;
	}

	/**
	 * @param operacionTrue the operacionTrue to set
	 */
	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}

	/**
	 * @return the criteriosConsulta
	 */
	public String getCriteriosConsulta() {
		return criteriosConsulta;
	}

	/**
	 * @param criteriosConsulta the criteriosConsulta to set
	 */
	public void setCriteriosConsulta(String criteriosConsulta) {
		this.criteriosConsulta = criteriosConsulta;
	}

	/**
	 * @return the tipoDocumento
	 */
	public String[] getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento the tipoDocumento to set
	 */
	public void setTipoDocumento(String[] tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	// ************* Fin Declaracion de Metodos Get y Set *************
}
