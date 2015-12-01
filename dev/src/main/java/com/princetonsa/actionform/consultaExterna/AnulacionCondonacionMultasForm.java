package com.princetonsa.actionform.consultaExterna;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import com.princetonsa.dto.consultaExterna.DtoMultasCitas;

public class AnulacionCondonacionMultasForm extends ValidatorForm{

	private String estado;
	
	private ArrayList unidadesAgenda;
	
	private String unidadesAgendaStr;
	
	private DtoMultasCitas citasconMultas;
	
	private ArrayList< DtoMultasCitas> arrayCitasconMulta;
	
	private ArrayList listadoConvenio; 
	
	private HashMap listadoCentroAtencion;
	
	private HashMap listadoUnidadesAgenda;
	
	private ArrayList profesionalesSalud;
	
	private boolean esAnular;
	
	private boolean esCondonar;
	
	private String propiedadOrdenar;
	
	private String ultimaPropiedad;

	private HashMap parametrosFiltros;
	
	private HashMap motivosAnulacionCondonacion;
	
	private String codMotivoAnulacionCondonacion;
	
	private String observaciones;
	
	private String confirmarAnular;
	
	private String confirmarCondonar;
	
	private String estadoMulta;
	
	private int posMulta;
	
	private HashMap parametrosBusqueda;
	
	private String centroAtencionFiltro;
	
	private String linkSiguiente;
	
	private String errorPermisos;
	

	public AnulacionCondonacionMultasForm()
	{
		this.reset();
	}
	
	public void reset()
	{
		this.estado=new String("");
		this.unidadesAgenda=new ArrayList();
		this.citasconMultas=new DtoMultasCitas();
		this.esAnular=false;
		this.esCondonar=false;
		this.unidadesAgendaStr=new String("");
		this.propiedadOrdenar=new String("");
		this.ultimaPropiedad=new String("multa_");
		this.arrayCitasconMulta=new ArrayList<DtoMultasCitas>();
		this.parametrosFiltros= new HashMap();
		this.parametrosFiltros.put("operacionExitosa",ConstantesBD.acronimoNo);
		this.motivosAnulacionCondonacion=new HashMap();
		this.motivosAnulacionCondonacion.put("numRegistros", "0");
		this.listadoConvenio=new ArrayList();
		this.listadoCentroAtencion=new HashMap();
		this.listadoCentroAtencion.put("numRegistros", "0");
		this.listadoUnidadesAgenda=new HashMap();
		this.listadoUnidadesAgenda.put("numRegistros", "0");
		this.parametrosBusqueda=new HashMap();
		this.parametrosBusqueda.put("numRegistros", "0");
		this.profesionalesSalud=new ArrayList();
		this.posMulta= ConstantesBD.codigoNuncaValido;
		this.codMotivoAnulacionCondonacion=new String("");
		this.observaciones=new String("");
		this.confirmarAnular=new String("");
		this.confirmarCondonar=new String("");
		this.estadoMulta=new String("");
		this.centroAtencionFiltro=new String("");
		this.linkSiguiente=new String("");
		this.errorPermisos= new String(ConstantesBD.acronimoSi);
	}
	
	public void resetArrayMultas()
	{
		this.arrayCitasconMulta=new ArrayList<DtoMultasCitas>();
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ArrayList getUnidadesAgenda() {
		return unidadesAgenda;
	}

	public void setUnidadesAgenda(ArrayList unidadesAgenda) {
		this.unidadesAgenda = unidadesAgenda;
	}

	public boolean isEsAnular() {
		return esAnular;
	}

	public void setEsAnular(boolean esAnular) {
		this.esAnular = esAnular;
	}

	public boolean isEsCondonar() {
		return esCondonar;
	}

	public void setEsCondonar(boolean esCondonar) {
		this.esCondonar = esCondonar;
	}

	public DtoMultasCitas getCitasconMultas() {
		return citasconMultas;
	}

	public void setCitasconMultas(DtoMultasCitas citasconMultas) {
		this.citasconMultas = citasconMultas;
	}

	public String getUnidadesAgendaStr() {
		return unidadesAgendaStr;
	}

	public void setUnidadesAgendaStr(String unidadesAgendaStr) {
		this.unidadesAgendaStr = unidadesAgendaStr;
	}

	public ArrayList<DtoMultasCitas> getArrayCitasconMulta() {
		return arrayCitasconMulta;
	}

	public void setArrayCitasconMulta(ArrayList<DtoMultasCitas> arrayCitasconMulta) {
		this.arrayCitasconMulta = arrayCitasconMulta;
	}

	public String getPropiedadOrdenar() {
		return propiedadOrdenar;
	}

	public void setPropiedadOrdenar(String propiedadOrdenar) {
		this.propiedadOrdenar = propiedadOrdenar;
	}

	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}

	public void setUltimaPropiedad(String ultimaPropiedad) {
		this.ultimaPropiedad = ultimaPropiedad;
	}
	
	public String getMultaFiltro() {
		return this.parametrosFiltros.get("multaFiltro").toString();
	}

	public void setMultaFiltro(String valor) {
		this.parametrosFiltros.put("multaFiltro",valor);
	}

	public HashMap getParametrosFiltros() {
		return parametrosFiltros;
	}

	public void setParametrosFiltros(HashMap parametrosFiltros) {
		this.parametrosFiltros = parametrosFiltros;
	}

	public Object getParametrosFiltros(String key) {
		return parametrosFiltros.get(key);
	}

	public void setParametrosFiltros(String key, Object value) {
		this.parametrosFiltros.put(key,value);
	}
	
	

	public String getFechaFinalGeneracion() {
		return this.parametrosFiltros.get("fechaFinalGeneracion").toString();
	}

	public void setFechaFinalGeneracion(String valor) {
		this.parametrosFiltros.put("fechaFinalGeneracion",valor);
	}
	
	public String getCentroatencion() {
		return this.parametrosFiltros.get("centroatencion").toString();
	}

	public void setCentroatencion(String valor) {
		this.parametrosFiltros.put("centroatencion",valor);
	}
	
	public String getConvenio() {
		return this.parametrosFiltros.get("convenio").toString();
	}

	public void setConvenio(String valor) {
		this.parametrosFiltros.put("convenio",valor);
	}
	
	public String getEstadocita() {
		return this.parametrosFiltros.get("estadocita").toString();
	}

	public void setEstadocita(String valor) {
		this.parametrosFiltros.put("estadocita",valor);
	}
	
	
	public String getProfesionalsalud() {
		return this.parametrosFiltros.get("profesionalsalud").toString();
	}

	public void setProfesionalsalud(String valor) {
		this.parametrosFiltros.put("profesionalsalud",valor);
	}
	
	public int getPosMulta() {
		return posMulta;
	}

	public void setPosMulta(int posMulta) {
		this.posMulta = posMulta;
	}

	public HashMap getMotivosAnulacionCondonacion() {
		return motivosAnulacionCondonacion;
	}

	public void setMotivosAnulacionCondonacion(HashMap motivosAnulacionCondonacion) {
		this.motivosAnulacionCondonacion = motivosAnulacionCondonacion;
	}

	public String getCodMotivoAnulacionCondonacion() {
		return codMotivoAnulacionCondonacion;
	}

	public void setCodMotivoAnulacionCondonacion(
			String codMotivoAnulacionCondonacion) {
		this.codMotivoAnulacionCondonacion = codMotivoAnulacionCondonacion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getConfirmarAnular() {
		return confirmarAnular;
	}

	public void setConfirmarAnular(String confirmarAnular) {
		this.confirmarAnular = confirmarAnular;
	}

	public String getConfirmarCondonar() {
		return confirmarCondonar;
	}

	public void setConfirmarCondonar(String confirmarCondonar) {
		this.confirmarCondonar = confirmarCondonar;
	}

	public String getEstadoMulta() {
		return estadoMulta;
	}

	public void setEstadoMulta(String estadoMulta) {
		this.estadoMulta = estadoMulta;
	}
    
	public HashMap getParametrosBusqueda() {
		return parametrosBusqueda;
	}


	public void setParametrosBusqueda(HashMap parametrosBusqueda) {
		this.parametrosBusqueda = parametrosBusqueda;
		
	}
	
	public Object getParametrosBusqueda(String key) {
		return parametrosBusqueda.get(key);
	}

	public void setParametrosBusqueda(String key, Object value) {
		this.parametrosBusqueda.put(key,value);
	}
	

	public HashMap getListadoCentroAtencion() {
		return listadoCentroAtencion;
	}

	public void setListadoCentroAtencion(HashMap listadoCentroAtencion) {
		this.listadoCentroAtencion = listadoCentroAtencion;
	}

	public ArrayList getListadoConvenio() {
		return listadoConvenio;
	}

	public void setListadoConvenio(ArrayList listadoConvenio) {
		this.listadoConvenio = listadoConvenio;
	}

	public ArrayList getProfesionalesSalud() {
		return profesionalesSalud;
	}

	public void setProfesionalesSalud(ArrayList profesionalesSalud) {
		this.profesionalesSalud = profesionalesSalud;
	}

	public HashMap getListadoUnidadesAgenda() {
		return listadoUnidadesAgenda;
	}

	public void setListadoUnidadesAgenda(HashMap listadoUnidadesAgenda) {
		this.listadoUnidadesAgenda = listadoUnidadesAgenda;
	}

	public String getCentroAtencionFiltro() {
		return centroAtencionFiltro;
	}

	public void setCentroAtencionFiltro(String centroAtencionFiltro) {
		this.centroAtencionFiltro = centroAtencionFiltro;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getErrorPermisos() {
		return errorPermisos;
	}

	public void setErrorPermisos(String errorPermisos) {
		this.errorPermisos = errorPermisos;
	}

	
		
}
