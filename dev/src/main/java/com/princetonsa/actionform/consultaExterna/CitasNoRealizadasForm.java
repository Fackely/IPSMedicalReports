package com.princetonsa.actionform.consultaExterna;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.consultaExterna.DtoCitasNoRealizadas;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.consultaExterna.DtoServiciosCitas;
import com.princetonsa.mundo.consultaExterna.Multas;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

/**
 * @author Jairo Gómez Fecha Junio de 2009
 */

public class CitasNoRealizadasForm extends ValidatorForm {
	// *************** Declaracion de variables ***************

	private String estado;
	
	private String estadoDetalle;

	private ResultadoBoolean mensaje;

	private HashMap<String, Object> centrosAtencionAutorizados = new HashMap<String, Object>();

	private HashMap unidadesAgenda = new HashMap();

	private ArrayList convenios = new ArrayList();

	String[] indicesCriterios = Multas.indicesCriterios;

	private HashMap criterios = new HashMap();

	private ArrayList<HashMap<String, Object>> estados = new ArrayList<HashMap<String, Object>>();

	private ArrayList<HashMap<String, Object>> profesionales = new ArrayList<HashMap<String, Object>>();

	private ArrayList<DtoCitasNoRealizadas> resultado = new ArrayList<DtoCitasNoRealizadas>();
	
	private ArrayList<DtoServiciosCitas> arrayServicios = new ArrayList<DtoServiciosCitas>();
	
	private ArrayList<DtoConceptoFacturaVaria> arrayConcepFact = new ArrayList<DtoConceptoFacturaVaria>();

	private int index;

	private String linkSiguiente;

	private String centroAtencion;

	private String estadoActualizarCita;

	private String seleccionarTodasEst = ConstantesBD.acronimoNo;

	private String seleccionarTodasMult = ConstantesBD.acronimoNo;

	private String seleccionarTodasFact = ConstantesBD.acronimoNo;

	private String seleccionarTodasPagEst = ConstantesBD.acronimoNo;

	private String seleccionarTodasPagMult = ConstantesBD.acronimoNo;

	private String seleccionarTodasPagFact = ConstantesBD.acronimoNo;
	
	private int posArray;
	
	private boolean tieneRolGenModFactVar = false;
	
	private boolean tieneRolAproAnulFactVar = false;
	
	private boolean aproAnulFactVar = false;
	
	private String motivoNoAtencion;
	
	private String conceptoFacturaVaria;
	
	private String activoConceptoFacturaVaria = ConstantesBD.acronimoNo;
	
	private String ultimaPropiedadOrdenada;
	
	private String propiedadOrdenar;
	
	private String opcionRango;
	
	private String institucionManejaMulta;
	
	private boolean readOnlyMotivoNoAtencion;

	// ************ Fin Declaracion de variables **************

	/**
	 * Metodo que inicializa todas las variables.
	 */
	public void reset() {
		this.estado = "";
		this.setEstadoDetalle("");
		this.mensaje = new ResultadoBoolean(false);
		this.centrosAtencionAutorizados = new HashMap<String, Object>();
		this.unidadesAgenda = new HashMap<String, Object>();
		this.criterios = new HashMap();
		this.setConvenios(new ArrayList());
		this.setEstados(new ArrayList<HashMap<String, Object>>());
		this.setProfesionales(new ArrayList<HashMap<String, Object>>());
		this.setResultado(new ArrayList<DtoCitasNoRealizadas>());
		this.setArrayServicios(new ArrayList<DtoServiciosCitas>());
		this.setArrayConcepFact(new ArrayList<DtoConceptoFacturaVaria>());
		this.setLinkSiguiente("");
		this.setCentroAtencion("");
		this.setEstadoActualizarCita("");
		this.setSeleccionarTodasEst(new String(ConstantesBD.acronimoNo));
		this.setSeleccionarTodasMult(new String(ConstantesBD.acronimoNo));
		this.setSeleccionarTodasFact(new String(ConstantesBD.acronimoNo));
		this.setSeleccionarTodasPagEst(new String(ConstantesBD.acronimoNo));
		this.setSeleccionarTodasPagMult(new String(ConstantesBD.acronimoNo));
		this.setSeleccionarTodasPagFact(new String(ConstantesBD.acronimoNo));
		this.setTieneRolGenModFactVar(false);
		this.setTieneRolAproAnulFactVar(false);
		this.setAproAnulFactVar(false);
		this.setMotivoNoAtencion("");
		this.setConceptoFacturaVaria("");
		this.setActivoConceptoFacturaVaria(new String(ConstantesBD.acronimoNo));
		this.setUltimaPropiedadOrdenada("unidadAgenda_");
		this.setPropiedadOrdenar("");
		this.setOpcionRango(ConstantesBD.acronimoNo);
		this.institucionManejaMulta = "";
		this.readOnlyMotivoNoAtencion = true;
	}

	/**
	 * Validate
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionErrors errores = new ActionErrors();
		if (this.estado.equals("buscar")) {
			if (this.criterios.get("fechaIniCita2").equals("")) {
				errores.add("Campo Fecha Cita Inicial requerido",
						new ActionMessage("errors.required",
								"La Fecha Cita Inicial "));
			} else if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
					this.criterios.get("fechaIniCita2").toString(),
					UtilidadFecha.getFechaActual())) {
				errores.add("Campo Fecha Cita Inicial requerido",
						new ActionMessage("errors.fechaPosteriorIgualActual",
								"Cita Inicial ", "Actual "));
			}
			if (this.criterios.get("fechaFinCita3").equals("")) {
				errores.add("Campo Fecha Cita Final requerido",
						new ActionMessage("errors.required",
								"La Fecha Cita Final "));
			} else if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(
					this.criterios.get("fechaFinCita3").toString(),
					UtilidadFecha.getFechaActual())) {
				errores.add("Campo Fecha Cita Final requerido",
						new ActionMessage("errors.fechaPosteriorIgualActual",
								"Cita Final ", "Actual "));
			}
			if(!this.criterios.get("fechaFinCita3").equals("") && !this.criterios.get("fechaIniCita2").equals(""))
			{
				if (UtilidadFecha.esFechaMenorQueOtraReferencia(this.criterios.get("fechaFinCita3").toString(), this.criterios.get("fechaIniCita2").toString()))
				{
					errores.add("Campo Fecha Cita Final requerido",
							new ActionMessage("errors.fechaAnteriorIgualActual",
									"Cita Final ", "Cita Inicial "));
				}
			}
		}
		return errores;
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
	 * @return the centrosAtencionAutorizados
	 */
	public HashMap getCentrosAtencionAutorizados() {
		return centrosAtencionAutorizados;
	}

	/**
	 * @param centrosAtencionAutorizados
	 *            the centrosAtencionAutorizados to set
	 */
	public void setCentrosAtencionAutorizados(HashMap centrosAtencionAutorizados) {
		this.centrosAtencionAutorizados = centrosAtencionAutorizados;
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
	 * @param unidadesAgenda
	 *            the unidadesAgenda to set
	 */
	public void setUnidadesAgenda(HashMap unidadesAgenda) {
		this.unidadesAgenda = unidadesAgenda;
	}

	/**
	 * @return the unidadesAgenda
	 */
	public HashMap getUnidadesAgenda() {
		return unidadesAgenda;
	}

	/**
	 * @return the unidadesAgenda
	 */
	public Object getUnidadesAgenda(String key) {
		return unidadesAgenda.get(key);
	}

	/**
	 * @param criterios
	 *            the unidadesAgenda to set
	 */
	public void setUnidadesAgenda(String key, Object value) {
		this.unidadesAgenda.put(key, value);
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param convenios
	 *            the convenios to set
	 */
	public void setConvenios(ArrayList convenios) {
		this.convenios = convenios;
	}

	/**
	 * @return the convenios
	 */
	public ArrayList getConvenios() {
		return convenios;
	}

	/**
	 * @param estados
	 *            the estados to set
	 */
	public void setEstados(ArrayList<HashMap<String, Object>> estados) {
		this.estados = estados;
	}

	/**
	 * @return the estados
	 */
	public ArrayList<HashMap<String, Object>> getEstados() {
		return estados;
	}

	/**
	 * @param profesionales
	 *            the profesionales to set
	 */
	public void setProfesionales(
			ArrayList<HashMap<String, Object>> profesionales) {
		this.profesionales = profesionales;
	}

	/**
	 * @return the profesionales
	 */
	public ArrayList<HashMap<String, Object>> getProfesionales() {
		return profesionales;
	}

	/**
	 * @param linkSiguiente
	 *            the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	/**
	 * @param resultado
	 *            the resultado to set
	 */
	public void setResultado(ArrayList<DtoCitasNoRealizadas> resultado) {
		this.resultado = resultado;
	}

	/**
	 * @return the resultado
	 */
	public ArrayList<DtoCitasNoRealizadas> getResultado() {
		return resultado;
	}

	/**
	 * @param centroAtencion
	 *            the centroAtencion to set
	 */
	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centroAtencion
	 */
	public String getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param estadoActualizarCita
	 *            the estadoActualizarCita to set
	 */
	public void setEstadoActualizarCita(String estadoActualizarCita) {
		this.estadoActualizarCita = estadoActualizarCita;
	}

	/**
	 * @return the estadoActualizarCita
	 */
	public String getEstadoActualizarCita() {
		return estadoActualizarCita;
	}

	/**
	 * @param seleccionarTodasEst
	 *            the seleccionarTodasEst to set
	 */
	public void setSeleccionarTodasEst(String seleccionarTodasEst) {
		this.seleccionarTodasEst = seleccionarTodasEst;
	}

	/**
	 * @return the seleccionarTodasEst
	 */
	public String getSeleccionarTodasEst() {
		return seleccionarTodasEst;
	}

	/**
	 * @return the seleccionarTodasMult
	 */
	public String getSeleccionarTodasMult() {
		return seleccionarTodasMult;
	}

	/**
	 * @param seleccionarTodasMult
	 *            the seleccionarTodasMult to set
	 */
	public void setSeleccionarTodasMult(String seleccionarTodasMult) {
		this.seleccionarTodasMult = seleccionarTodasMult;
	}

	/**
	 * @return the seleccionarTodasFact
	 */
	public String getSeleccionarTodasFact() {
		return seleccionarTodasFact;
	}

	/**
	 * @param seleccionarTodasFact
	 *            the seleccionarTodasFact to set
	 */
	public void setSeleccionarTodasFact(String seleccionarTodasFact) {
		this.seleccionarTodasFact = seleccionarTodasFact;
	}

	/**
	 * @return the seleccionarTodasPagEst
	 */
	public String getSeleccionarTodasPagEst() {
		return seleccionarTodasPagEst;
	}

	/**
	 * @param seleccionarTodasPagEst the seleccionarTodasPagEst to set
	 */
	public void setSeleccionarTodasPagEst(String seleccionarTodasPagEst) {
		this.seleccionarTodasPagEst = seleccionarTodasPagEst;
	}

	/**
	 * @return the seleccionarTodasPagMult
	 */
	public String getSeleccionarTodasPagMult() {
		return seleccionarTodasPagMult;
	}

	/**
	 * @param seleccionarTodasPagMult the seleccionarTodasPagMult to set
	 */
	public void setSeleccionarTodasPagMult(String seleccionarTodasPagMult) {
		this.seleccionarTodasPagMult = seleccionarTodasPagMult;
	}

	/**
	 * @return the seleccionarTodasPagFact
	 */
	public String getSeleccionarTodasPagFact() {
		return seleccionarTodasPagFact;
	}

	/**
	 * @param seleccionarTodasPagFact the seleccionarTodasPagFact to set
	 */
	public void setSeleccionarTodasPagFact(String seleccionarTodasPagFact) {
		this.seleccionarTodasPagFact = seleccionarTodasPagFact;
	}

	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
	}

	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}

	/**
	 * @param estadoDetalle the estadoDetalle to set
	 */
	public void setEstadoDetalle(String estadoDetalle) {
		this.estadoDetalle = estadoDetalle;
	}

	/**
	 * @return the estadoDetalle
	 */
	public String getEstadoDetalle() {
		return estadoDetalle;
	}

	/**
	 * @param arrayServicios the arrayServicios to set
	 */
	public void setArrayServicios(ArrayList<DtoServiciosCitas> arrayServicios) {
		this.arrayServicios = arrayServicios;
	}

	/**
	 * @return the arrayServicios
	 */
	public ArrayList<DtoServiciosCitas> getArrayServicios() {
		return arrayServicios;
	}

	/**
	 * @param tieneRolGenModFactVar the tieneRolGenModFactVar to set
	 */
	public void setTieneRolGenModFactVar(boolean tieneRolGenModFactVar) {
		this.tieneRolGenModFactVar = tieneRolGenModFactVar;
	}

	/**
	 * @return the tieneRolGenModFactVar
	 */
	public boolean isTieneRolGenModFactVar() {
		return tieneRolGenModFactVar;
	}

	/**
	 * @param tieneRolAproAnulFactVar the tieneRolAproAnulFactVar to set
	 */
	public void setTieneRolAproAnulFactVar(boolean tieneRolAproAnulFactVar) {
		this.tieneRolAproAnulFactVar = tieneRolAproAnulFactVar;
	}

	/**
	 * @return the tieneRolAproAnulFactVar
	 */
	public boolean isTieneRolAproAnulFactVar() {
		return tieneRolAproAnulFactVar;
	}

	/**
	 * @param motivoNoAtencion the motivoNoAtencion to set
	 */
	public void setMotivoNoAtencion(String motivoNoAtencion) {
		this.motivoNoAtencion = motivoNoAtencion;
	}

	/**
	 * @return the motivoNoAtencion
	 */
	public String getMotivoNoAtencion() {
		return motivoNoAtencion;
	}

	/**
	 * @param conceptoFacturaVaria the conceptoFacturaVaria to set
	 */
	public void setConceptoFacturaVaria(String conceptoFacturaVaria) {
		this.conceptoFacturaVaria = conceptoFacturaVaria;
	}

	/**
	 * @return the conceptoFacturaVaria
	 */
	public String getConceptoFacturaVaria() {
		return conceptoFacturaVaria;
	}

	/**
	 * @param arrayConcepFact the arrayConcepFact to set
	 */
	public void setArrayConcepFact(ArrayList<DtoConceptoFacturaVaria> arrayConcepFact) {
		this.arrayConcepFact = arrayConcepFact;
	}

	/**
	 * @return the arrayConcepFact
	 */
	public ArrayList<DtoConceptoFacturaVaria> getArrayConcepFact() {
		return arrayConcepFact;
	}

	/**
	 * @param activoConceptoFacturaVaria the activoConceptoFacturaVaria to set
	 */
	public void setActivoConceptoFacturaVaria(String activoConceptoFacturaVaria) {
		this.activoConceptoFacturaVaria = activoConceptoFacturaVaria;
	}

	/**
	 * @return the activoConceptoFacturaVaria
	 */
	public String getActivoConceptoFacturaVaria() {
		return activoConceptoFacturaVaria;
	}

	/**
	 * @param ultimaPropiedadOrdenada the ultimaPropiedadOrdenada to set
	 */
	public void setUltimaPropiedadOrdenada(String ultimaPropiedadOrdenada) {
		this.ultimaPropiedadOrdenada = ultimaPropiedadOrdenada;
	}

	/**
	 * @return the ultimaPropiedadOrdenada
	 */
	public String getUltimaPropiedadOrdenada() {
		return ultimaPropiedadOrdenada;
	}

	/**
	 * @param propiedadOrdenar the propiedadOrdenar to set
	 */
	public void setPropiedadOrdenar(String propiedadOrdenar) {
		this.propiedadOrdenar = propiedadOrdenar;
	}

	/**
	 * @return the propiedadOrdenar
	 */
	public String getPropiedadOrdenar() {
		return propiedadOrdenar;
	}

	/**
	 * @param aproAnulFactVar the aproAnulFactVar to set
	 */
	public void setAproAnulFactVar(boolean aproAnulFactVar) {
		this.aproAnulFactVar = aproAnulFactVar;
	}

	/**
	 * @return the aproAnulFactVar
	 */
	public boolean isAproAnulFactVar() {
		return aproAnulFactVar;
	}

	/**
	 * @param opcionRango the opcionRango to set
	 */
	public void setOpcionRango(String opcionRango) {
		this.opcionRango = opcionRango;
	}

	/**
	 * @return the opcionRango
	 */
	public String getOpcionRango() {
		return opcionRango;
	}

	/**
	 * @return the institucionManejaMulta
	 */
	public String getInstitucionManejaMulta() {
		return institucionManejaMulta;
	}

	/**
	 * @param institucionManejaMulta the institucionManejaMulta to set
	 */
	public void setInstitucionManejaMulta(String institucionManejaMulta) {
		this.institucionManejaMulta = institucionManejaMulta;
	}

	/**
	 * @return the readOnlyMotivoNoAtencion
	 */
	public boolean isReadOnlyMotivoNoAtencion() {
		return readOnlyMotivoNoAtencion;
	}

	/**
	 * @param readOnlyMotivoNoAtencion the readOnlyMotivoNoAtencion to set
	 */
	public void setReadOnlyMotivoNoAtencion(boolean readOnlyMotivoNoAtencion) {
		this.readOnlyMotivoNoAtencion = readOnlyMotivoNoAtencion;
	}

	// /**
	// * @return the convenios
	// */
	// public Object getConvenios(String key) {
	// return convenios.get(key);
	// }
	//
	// /**
	// * @param convenios the convenios to set
	// */
	// public void setConvenios(String key, Object value) {
	// this.convenios.put(key, value);
	// }

	// ************* Fin Declaracion de Metodos Get y Set *************
}