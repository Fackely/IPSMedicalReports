package com.princetonsa.actionform.ordenesmedicas;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;

import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;

public class ResponderConsultasEntSubcontratadasForm extends ValidatorForm
{
	private static Logger logger = Logger.getLogger(ResponderConsultasEntSubcontratadasForm.class);
	
	
 private String estado;
 private ArrayList<DtoEntidadSubcontratada> entidadesSubcontratadas;
 private ArrayList<DtoSolicitudesSubCuenta> solicitudesaResponder;
 private HashMap parametrosBusqueda;
 private int numEntidades;
 private String propiedadOrdenar;
 private String ultimaPropiedad;
 private int posSolicitud;
 private String opcionListado;
 private String linkSiguiente;
 
 public ResponderConsultasEntSubcontratadasForm()
 {
	 this.reset();
	 
 }
	
public void reset()
{
	this.estado=new String("");
	this.solicitudesaResponder= new ArrayList<DtoSolicitudesSubCuenta>();
	this.parametrosBusqueda=new HashMap();
	this.parametrosBusqueda.put("entidadSubcontratada","");
	this.parametrosBusqueda.put("fechaInicial",UtilidadFecha.getFechaActual());
	this.parametrosBusqueda.put("fechaFinal",UtilidadFecha.getFechaActual());
	this.numEntidades=ConstantesBD.codigoNuncaValido;
	this.opcionListado=new String("");
	this.linkSiguiente=new String("");
}




public String getEstado() {
	return estado;
}

public void setEstado(String estado) {
	this.estado = estado;
}


public void resetArraySolicitudesaResponder()
{
	this.solicitudesaResponder= new ArrayList<DtoSolicitudesSubCuenta>();
	
}

public void resetArryEntidadesSubcontratadas()
{
	this.entidadesSubcontratadas=new ArrayList<DtoEntidadSubcontratada>();	
}


public ArrayList<DtoEntidadSubcontratada> getEntidadesSubcontratadas() {
	return entidadesSubcontratadas;
}

public void setEntidadesSubcontratadas(
		ArrayList<DtoEntidadSubcontratada> entidadesSubcontratadas) {
	this.entidadesSubcontratadas = entidadesSubcontratadas;
}

public HashMap getParametrosBusqueda() {
	return parametrosBusqueda;
}

public void setParametrosBusqueda(HashMap parametrosBusqueda) {
	this.parametrosBusqueda = parametrosBusqueda;
}

public int getNumEntidades() {
	return numEntidades;
}

public void setNumEntidades(int numEntidades) {
	this.numEntidades = numEntidades;
}

public ArrayList<DtoSolicitudesSubCuenta> getSolicitudesaResponder() {
	return solicitudesaResponder;
}

public void setSolicitudesaResponder(
		ArrayList<DtoSolicitudesSubCuenta> solicitudesaResponder) {
	this.solicitudesaResponder = solicitudesaResponder;
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

public int getPosSolicitud() {
	return posSolicitud;
}

public void setPosSolicitud(int posSolicitud) {
	this.posSolicitud = posSolicitud;
}

public String getOpcionListado() {
	return opcionListado;
}

public void setOpcionListado(String opcionListado) {
	this.opcionListado = opcionListado;
}

public String getLinkSiguiente() {
	return linkSiguiente;
}

public void setLinkSiguiente(String linkSiguiente) {
	this.linkSiguiente = linkSiguiente;
}

 


 
}
