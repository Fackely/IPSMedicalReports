package com.princetonsa.actionform.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.ValoresPorDefecto;

import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoHistoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoIngresosFacturasAtenMedica;

public class ConsultarRefinanciarCuotaPacienteForm  extends ValidatorForm 
{
	Logger logger = Logger.getLogger(ConsultarRefinanciarCuotaPacienteForm.class);
	
  private String estado; 
  private String opcionListado;
  private String ingresoFiltro;
  private String propiedadOrdenar;
  private String esDescendente;
  private String fechaInicioRefinanciacion;
  private String valorCuota;
  private String observacionesRefinanciacion;
  private String linkSiguiente;
  private int nroCuotasRefinanciacion;
  private int nroCuotasRefinanciacionActual;
  private int diasporCuotaRefinanciacion;  
  private int nroMaximoCuotasRefinanciacion;
  private int posIngreso;
  private ArrayList<DtoCuotasDatosFinanciacion> cuotasRefinanciacion;
  private ArrayList<DtoHistoDatosFinanciacion> historicoRefinanciacion;
  private ArrayList<DtoIngresosFacturasAtenMedica> listaIngresos;
  private HashMap parametrosFiltros;
  private HashMap parametrosBusqueda;
  private HashMap listadoCentroAtencion;
  private boolean permisosUsuario;
  private boolean imprimePagare;
  private boolean formatoDefinido;
  
  
  public ConsultarRefinanciarCuotaPacienteForm()
  {
	this.reset();  
  }
  
  public void reset()
  {
	  this.estado= new String("");
	  this.opcionListado=new String("");
	  this.propiedadOrdenar=new String("");
	  this.esDescendente=new String("");
	  this.linkSiguiente= new String("");
	  this.valorCuota= new String("");
	  this.observacionesRefinanciacion=new String("");
	  this.fechaInicioRefinanciacion=UtilidadFecha.getFechaActual();
	  this.listaIngresos=new ArrayList<DtoIngresosFacturasAtenMedica>();
	  this.cuotasRefinanciacion= new ArrayList<DtoCuotasDatosFinanciacion>();
	  this.historicoRefinanciacion= new ArrayList<DtoHistoDatosFinanciacion>();
	  this.posIngreso=ConstantesBD.codigoNuncaValido;
	  this.ingresoFiltro=ConstantesBD.codigoNuncaValido+"";	
	  this.diasporCuotaRefinanciacion=ConstantesBD.codigoNuncaValido;
	  this.nroCuotasRefinanciacion=ConstantesBD.codigoNuncaValido;
	  this.nroMaximoCuotasRefinanciacion=ConstantesBD.codigoNuncaValido;
	  this.nroCuotasRefinanciacionActual= ConstantesBD.codigoNuncaValido;
	  this.permisosUsuario=true;
	  this.imprimePagare=true;
	  this.formatoDefinido=true;
	  this.parametrosFiltros= new HashMap();
	  this.parametrosBusqueda=new HashMap();
	  this.parametrosFiltros.put("operacionExitosa","");
	  this.listadoCentroAtencion= new HashMap();
	  this.listadoCentroAtencion.put("numRegistros","0");
	    
	  
	  
  }


  public void resetCuotasRefinanciacion()
  {
	  this.cuotasRefinanciacion=new ArrayList<DtoCuotasDatosFinanciacion>();
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
 * @return the opcionListado
 */
public String getOpcionListado() {
	return opcionListado;
}

/**
 * @param opcionListado the opcionListado to set
 */
public void setOpcionListado(String opcionListado) {
	this.opcionListado = opcionListado;
}

/**
 * @return the listaIngresos
 */
public ArrayList<DtoIngresosFacturasAtenMedica> getListaIngresos() {
	return listaIngresos;
}

/**
 * @param listaIngresos the listaIngresos to set
 */
public void setListaIngresos(
		ArrayList<DtoIngresosFacturasAtenMedica> listaIngresos) {
	this.listaIngresos = listaIngresos;
}

/**
 * @return the propiedadOrdenar
 */
public String getPropiedadOrdenar() {
	return propiedadOrdenar;
}

/**
 * @param propiedadOrdenar the propiedadOrdenar to set
 */
public void setPropiedadOrdenar(String propiedadOrdenar) {
	this.propiedadOrdenar = propiedadOrdenar;
}


/**
 * @return the posIngreso
 */
public int getPosIngreso() {
	return posIngreso;
}

/**
 * @param posIngreso the posIngreso to set
 */
public void setPosIngreso(int posIngreso) {
	this.posIngreso = posIngreso;
}

/**
 * @return the esDescendente
 */
public String getEsDescendente() {
	return esDescendente;
}

/**
 * @param esDescendente the esDescendente to set
 */
public void setEsDescendente(String esDescendente) {
	this.esDescendente = esDescendente;
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
 * @return the ingresoFiltro
 */
public String getIngresoFiltro() {
	return ingresoFiltro;
}

/**
 * @param ingresoFiltro the ingresoFiltro to set
 */
public void setIngresoFiltro(String ingresoFiltro) {
	this.ingresoFiltro = ingresoFiltro;
}

/**
 * @return the permisosUsuario
 */
public boolean isPermisosUsuario() {
	return permisosUsuario;
}

/**
 * @param permisosUsuario the permisosUsuario to set
 */
public void setPermisosUsuario(boolean permisosUsuario) {
	this.permisosUsuario = permisosUsuario;
}

/**
 * @return the nroCuotasRefinanciacion
 */
public int getNroCuotasRefinanciacion() {
	return nroCuotasRefinanciacion;
}

/**
 * @param nroCuotasRefinanciacion the nroCuotasRefinanciacion to set
 */
public void setNroCuotasRefinanciacion(int nroCuotasRefinanciacion) {
	this.nroCuotasRefinanciacion = nroCuotasRefinanciacion;
}

/**
 * @return the fechaInicioRefinanciacion
 */
public String getFechaInicioRefinanciacion() {
	return fechaInicioRefinanciacion;
}

/**
 * @param fechaInicioRefinanciacion the fechaInicioRefinanciacion to set
 */
public void setFechaInicioRefinanciacion(String fechaInicioRefinanciacion) {
	this.fechaInicioRefinanciacion = fechaInicioRefinanciacion;
}

/**
 * @return the observacionesRefinanciacion
 */
public String getObservacionesRefinanciacion() {
	return observacionesRefinanciacion;
}

/**
 * @param observacionesRefinanciacion the observacionesRefinanciacion to set
 */
public void setObservacionesRefinanciacion(String observacionesRefinanciacion) {
	this.observacionesRefinanciacion = observacionesRefinanciacion;
}


public Object getParametrosFiltros(String key) {
	return parametrosFiltros.get(key);
}

public void setParametrosFiltros(String key, Object value) {
	this.parametrosFiltros.put(key,value);
}

/**
 * @return the parametrosFiltros
 */
public HashMap getParametrosFiltros() {
	return parametrosFiltros;
}

/**
 * @param parametrosFiltros the parametrosFiltros to set
 */
public void setParametrosFiltros(HashMap parametrosFiltros) {
	this.parametrosFiltros = parametrosFiltros;
}

/**
 * @return the cuotasRefinanciacion
 */
public ArrayList<DtoCuotasDatosFinanciacion> getCuotasRefinanciacion() {
	return cuotasRefinanciacion;
}

/**
 * @param cuotasRefinanciacion the cuotasRefinanciacion to set
 */
public void setCuotasRefinanciacion(
		ArrayList<DtoCuotasDatosFinanciacion> cuotasRefinanciacion) {
	this.cuotasRefinanciacion = cuotasRefinanciacion;
}

/**
 * @return the nroMaximoCuotasRefinanciacion
 */
public int getNroMaximoCuotasRefinanciacion() {
	return nroMaximoCuotasRefinanciacion;
}

/**
 * @param nroMaximoCuotasRefinanciacion the nroMaximoCuotasRefinanciacion to set
 */
public void setNroMaximoCuotasRefinanciacion(int nroMaximoCuotasRefinanciacion) {
	this.nroMaximoCuotasRefinanciacion = nroMaximoCuotasRefinanciacion;
}

/**
 * @return the diasporCuotaRefinanciacion
 */
public int getDiasporCuotaRefinanciacion() {
	return diasporCuotaRefinanciacion;
}

/**
 * @param diasporCuotaRefinanciacion the diasporCuotaRefinanciacion to set
 */
public void setDiasporCuotaRefinanciacion(int diasporCuotaRefinanciacion) {
	this.diasporCuotaRefinanciacion = diasporCuotaRefinanciacion;
}

/**
 * @return the valorCuota
 */
public String getValorCuota() {
	return valorCuota;
}

/**
 * @param valorCuota the valorCuota to set
 */
public void setValorCuota(String valorCuota) {
	this.valorCuota = valorCuota;
}

/**
 * @return the historicoRefinanciacion
 */
public ArrayList<DtoHistoDatosFinanciacion> getHistoricoRefinanciacion() {
	return historicoRefinanciacion;
}

/**
 * @param historicoRefinanciacion the historicoRefinanciacion to set
 */
public void setHistoricoRefinanciacion(ArrayList<DtoHistoDatosFinanciacion> historicoRefinanciacion) {
	this.historicoRefinanciacion = historicoRefinanciacion;
}

/**
 * @return the parametrosBusqueda
 */
public HashMap getParametrosBusqueda() {
	return parametrosBusqueda;
}

/**
 * @param parametrosBusqueda the parametrosBusqueda to set
 */
public void setParametrosBusqueda(HashMap parametrosBusqueda) {
	this.parametrosBusqueda = parametrosBusqueda;
}

public Object getParametrosBusqueda(String key) {
	return parametrosBusqueda.get(key);
}

public void setParametrosBusqueda(String key, Object value) {
	this.parametrosBusqueda.put(key,value);
}

/**
 * @return the listadoCentroAtencion
 */
public HashMap getListadoCentroAtencion() {
	return listadoCentroAtencion;
}

/**
 * @param listadoCentroAtencion the listadoCentroAtencion to set
 */
public void setListadoCentroAtencion(HashMap listadoCentroAtencion) {
	this.listadoCentroAtencion = listadoCentroAtencion;
}

/**
 * @return the imprimePagare
 */
public boolean isImprimePagare() {
	return imprimePagare;
}

/**
 * @param imprimePagare the imprimePagare to set
 */
public void setImprimePagare(boolean imprimePagare) {
	this.imprimePagare = imprimePagare;
}

/**
 * @return the formatoDefinido
 */
public boolean isFormatoDefinido() {
	return formatoDefinido;
}

/**
 * @param formatoDefinido the formatoDefinido to set
 */
public void setFormatoDefinido(boolean formatoDefinido) {
	this.formatoDefinido = formatoDefinido;
}

/**
 * @return the nroCuotasRefinanciacionActual
 */
public int getNroCuotasRefinanciacionActual() {
	return nroCuotasRefinanciacionActual;
}

/**
 * @param nroCuotasRefinanciacionActual the nroCuotasRefinanciacionActual to set
 */
public void setNroCuotasRefinanciacionActual(int nroCuotasRefinanciacionActual) {
	this.nroCuotasRefinanciacionActual = nroCuotasRefinanciacionActual;
}
  
}
