package com.princetonsa.actionform.glosas;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;

import com.princetonsa.dto.glosas.DtoConceptoRespuesta;
import com.princetonsa.dto.glosas.DtoDetalleAsociosGlosa;
import com.princetonsa.dto.glosas.DtoDetalleFacturaGlosa;
import com.princetonsa.dto.glosas.DtoFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaSolicitudGlosa;


public class RegistrarConciliacionForm extends ValidatorForm
{
	/**
	 * Para manejo de Logs
	 */
	private Logger logger = Logger.getLogger(RegistrarConciliacionForm.class);
	
	/**
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	
	/**
	 * Arreglo para almacenar los Convenios
	 */
	private ArrayList<HashMap<String, Object>> arregloConvenios = new ArrayList<HashMap<String,Object>>();
	
	private ArrayList<DtoDetalleFacturaGlosa> arregloDetFactGlosa = new ArrayList<DtoDetalleFacturaGlosa>();
	
	private ArrayList<DtoFacturaGlosa> arregloFacturasGlosa = new ArrayList<DtoFacturaGlosa>();
	private ArrayList<DtoRespuestaFacturaGlosa> arregloFactResp = new ArrayList<DtoRespuestaFacturaGlosa>();
	private ArrayList<DtoRespuestaSolicitudGlosa> arregloSolResp = new ArrayList<DtoRespuestaSolicitudGlosa>();
	private ArrayList<DtoDetalleAsociosGlosa> arregloAsociosGlosa= new ArrayList<DtoDetalleAsociosGlosa>();
	private ArrayList<DtoFacturaGlosa> conceptosGlosaFac = new ArrayList<DtoFacturaGlosa>();	
	
	/**
	 * Arreglo para almacenar los Conceptos de Respuestas
	 */
	private ArrayList<DtoConceptoRespuesta> arregloConceptosResp;
	
	/**
	 * Atributos para la busqueda de Conciliaciones
	 */
	private String convenioSel;
	private String conciliacion;
	private String fechaIni;
	private String fechaFin;
	private String conceptoSel;
	private String acta;
	
	/**
	 * Atributos para la Conciliacion selccionada en la busqueda o nueva 
	 */
	private String convenioC;
	private String fechaC;
	private String horaC;
	private String nroActa;
	private String representanteConvenio;
	private String cargoRepConv;
	private String representanteInst;
	private String cargoRepInst;
	private String soportado;
	private String aceptado;
	private String conceptoC;
	private String observaciones;
	private String conciliacionC;
	private String codconciliacionC;
	private String codConvenioC;
	private String codConvenioBusq;
	private String nomConvenioC;
	
	
	/**
	 * Atributos para la busqueda de las Facturas de Glosa
	 */
	private String glosa;
	private String glosaEnt;
	private String facturaIni;
	private String facturaFin;
	private String fechaElabIni;
	private String fechaElabFin;
		
	private boolean modificable;
	private boolean modificable1;
	private boolean modificable2;
	
	private boolean permiteImprimir;
	
	private int posDetFactura;
	private String codFacturaDet;
	public String getCodFacturaDet() {
		return codFacturaDet;
	}


	public void setCodFacturaDet(String codFacturaDet) {
		this.codFacturaDet = codFacturaDet;
	}

	private String valorGlosaDet;
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.arregloConvenios = new ArrayList<HashMap<String,Object>>();
		this.convenioSel="";
		this.conciliacion="";
		this.fechaIni="";
		this.fechaFin="";
		this.arregloConceptosResp= new ArrayList<DtoConceptoRespuesta>();
		this.conceptoSel="";
		this.acta="";
		this.convenioC="";
		this.fechaC="";
		this.horaC="";
		this.nroActa="";
		this.representanteConvenio="";
		this.cargoRepConv="";
		this.representanteInst="";
		this.cargoRepInst="";
		this.aceptado="";
		this.soportado="";
		this.conceptoC="";
		this.observaciones="";
		this.conciliacionC="";
		this.codConvenioC="";
		this.codConvenioBusq="";
		this.nomConvenioC="";
		this.glosa="";
		this.glosaEnt="";
		this.facturaIni="";
		this.facturaFin="";
		this.fechaElabIni="";
		this.fechaElabFin="";
		this.arregloFacturasGlosa= new ArrayList<DtoFacturaGlosa>();
		this.arregloFactResp= new ArrayList<DtoRespuestaFacturaGlosa>();
		this.codconciliacionC="";
		this.modificable=false;
		this.modificable1=false;
		this.modificable2=false;
		this.arregloDetFactGlosa= new ArrayList<DtoDetalleFacturaGlosa>();
		this.permiteImprimir=false;
		this.posDetFactura=0;
		this.arregloSolResp=new ArrayList<DtoRespuestaSolicitudGlosa>();
		this.codFacturaDet="";
		this.valorGlosaDet="";
		this.arregloAsociosGlosa= new ArrayList<DtoDetalleAsociosGlosa>();
		this.conceptosGlosaFac= new ArrayList<DtoFacturaGlosa>();	
	}



	public String getCodConvenioBusq() {
		return codConvenioBusq;
	}


	public void setCodConvenioBusq(String codConvenioBusq) {
		this.codConvenioBusq = codConvenioBusq;
	}


	public ArrayList<DtoFacturaGlosa> getConceptosGlosaFac() {
		return conceptosGlosaFac;
	}


	public void setConceptosGlosaFac(ArrayList<DtoFacturaGlosa> o) {
		this.conceptosGlosaFac = o;
	}


	public boolean isModificable2() {
		return modificable2;
	}


	public void setModificable2(boolean modificable2) {
		this.modificable2 = modificable2;
	}


	public ArrayList<DtoDetalleAsociosGlosa> getArregloAsociosGlosa() {
		return arregloAsociosGlosa;
	}


	public void setArregloAsociosGlosa(
			ArrayList<DtoDetalleAsociosGlosa> arregloAsociosGlosa) {
		this.arregloAsociosGlosa = arregloAsociosGlosa;
	}


	public String getValorGlosaDet() {
		return valorGlosaDet;
	}


	public void setValorGlosaDet(String valorGlosaDet) {
		this.valorGlosaDet = valorGlosaDet;
	}


	public ArrayList<DtoRespuestaSolicitudGlosa> getArregloSolResp() {
		return arregloSolResp;
	}


	public void setArregloSolResp(
			ArrayList<DtoRespuestaSolicitudGlosa> arregloSolResp) {
		this.arregloSolResp = arregloSolResp;
	}


	public int getPosDetFactura() {
		return posDetFactura;
	}


	public void setPosDetFactura(int posDetFactura) {
		this.posDetFactura = posDetFactura;
	}


	public boolean isModificable1() {
		return modificable1;
	}


	public void setModificable1(boolean modificable1) {
		this.modificable1 = modificable1;
	}


	public boolean isPermiteImprimir() {
		return permiteImprimir;
	}


	public void setPermiteImprimir(boolean permiteImprimir) {
		this.permiteImprimir = permiteImprimir;
	}


	public ArrayList<DtoDetalleFacturaGlosa> getArregloDetFactGlosa() {
		return arregloDetFactGlosa;
	}


	public void setArregloDetFactGlosa(
			ArrayList<DtoDetalleFacturaGlosa> arregloDetFactGlosa) {
		this.arregloDetFactGlosa = arregloDetFactGlosa;
	}


	public boolean isModificable() {
		return modificable;
	}

	public void setModificable(boolean modificable) {
		this.modificable = modificable;
	}


	public String getCodconciliacionC() {
		return codconciliacionC;
	}


	public void setCodconciliacionC(String codconciliacionC) {
		this.codconciliacionC = codconciliacionC;
	}


	public ArrayList<DtoRespuestaFacturaGlosa> getArregloFactResp() {
		return arregloFactResp;
	}


	public void setArregloFactResp(
			ArrayList<DtoRespuestaFacturaGlosa> arregloFactResp) {
		this.arregloFactResp = arregloFactResp;
	}


	public String getGlosa() {
		return glosa;
	}


	public ArrayList<DtoFacturaGlosa> getArregloFacturasGlosa() {
		return arregloFacturasGlosa;
	}


	public void setArregloFacturasGlosa(
			ArrayList<DtoFacturaGlosa> arregloFacturasGlosa) {
		this.arregloFacturasGlosa = arregloFacturasGlosa;
	}


	public void setGlosa(String glosa) {
		this.glosa = glosa;
	}


	public String getGlosaEnt() {
		return glosaEnt;
	}


	public void setGlosaEnt(String glosaEnt) {
		this.glosaEnt = glosaEnt;
	}


	public String getFacturaIni() {
		return facturaIni;
	}


	public void setFacturaIni(String facturaIni) {
		this.facturaIni = facturaIni;
	}


	public String getFacturaFin() {
		return facturaFin;
	}


	public void setFacturaFin(String facturaFin) {
		this.facturaFin = facturaFin;
	}


	public String getFechaElabIni() {
		return fechaElabIni;
	}


	public void setFechaElabIni(String fechaElabIni) {
		this.fechaElabIni = fechaElabIni;
	}


	public String getFechaElabFin() {
		return fechaElabFin;
	}


	public void setFechaElabFin(String fechaElabFin) {
		this.fechaElabFin = fechaElabFin;
	}


	public String getCodConvenioC() {
		return codConvenioC;
	}


	public void setCodConvenioC(String codConvenioC) {
		this.codConvenioC = codConvenioC;
	}


	public String getNomConvenioC() {
		return nomConvenioC;
	}


	public void setNomConvenioC(String nomConvenioC) {
		this.nomConvenioC = nomConvenioC;
	}


	public String getConciliacionC() {
		return conciliacionC;
	}


	public void setConciliacionC(String conciliacionC) {
		this.conciliacionC = conciliacionC;
	}


	public String getConceptoC() {
		return conceptoC;
	}


	public void setConceptoC(String conceptoC) {
		this.conceptoC = conceptoC;
	}


	public String getObservaciones() {
		return observaciones;
	}


	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	public String getSoportado() {
		return soportado;
	}


	public void setSoportado(String soportado) {
		this.soportado = soportado;
	}


	public String getAceptado() {
		return aceptado;
	}


	public void setAceptado(String aceptado) {
		this.aceptado = aceptado;
	}


	public String getCargoRepInst() {
		return cargoRepInst;
	}


	public void setCargoRepInst(String cargoRepInst) {
		this.cargoRepInst = cargoRepInst;
	}


	public String getRepresentanteInst() {
		return representanteInst;
	}


	public void setRepresentanteInst(String representanteInst) {
		this.representanteInst = representanteInst;
	}


	public String getCargoRepConv() {
		return cargoRepConv;
	}


	public void setCargoRepConv(String cargoRepConv) {
		this.cargoRepConv = cargoRepConv;
	}


	public String getRepresentanteConvenio() {
		return representanteConvenio;
	}


	public void setRepresentanteConvenio(String representanteConvenio) {
		this.representanteConvenio = representanteConvenio;
	}


	public String getNroActa() {
		return nroActa;
	}


	public void setNroActa(String nroActa) {
		this.nroActa = nroActa;
	}


	public String getHoraC() {
		return horaC;
	}


	public void setHoraC(String horaC) {
		this.horaC = horaC;
	}


	public String getFechaC() {
		return fechaC;
	}


	public void setFechaC(String fechaC) {
		this.fechaC = fechaC;
	}


	public String getConvenioC() {
		return convenioC;
	}


	public void setConvenioC(String convenioC) {
		this.convenioC = convenioC;
	}


	public String getActa() {
		return acta;
	}


	public void setActa(String acta) {
		this.acta = acta;
	}


	public String getConceptoSel() {
		return conceptoSel;
	}


	public void setConceptoSel(String conceptoSel) {
		this.conceptoSel = conceptoSel;
	}


	public ArrayList<DtoConceptoRespuesta> getArregloConceptosResp() {
		return arregloConceptosResp;
	}


	public void setArregloConceptosResp(
			ArrayList<DtoConceptoRespuesta> arregloConceptosResp) {
		this.arregloConceptosResp = arregloConceptosResp;
	}


	public String getFechaIni() {
		return fechaIni;
	}


	public void setFechaIni(String fechaIni) {
		this.fechaIni = fechaIni;
	}


	public String getFechaFin() {
		return fechaFin;
	}


	public void setFechaFin(String fechaFin) {
		this.fechaFin = fechaFin;
	}


	public String getConciliacion() {
		return conciliacion;
	}


	public void setConciliacion(String conciliacion) {
		this.conciliacion = conciliacion;
	}


	public String getConvenioSel() {
		return convenioSel;
	}


	public void setConvenioSel(String convenioSel) {
		this.convenioSel = convenioSel;
	}


	public ArrayList<HashMap<String, Object>> getArregloConvenios() {
		return arregloConvenios;
	}


	public void setArregloConvenios(
			ArrayList<HashMap<String, Object>> arregloConvenios) {
		this.arregloConvenios = arregloConvenios;
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
	
	/**
	 * Metodo encargado de validar..
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		 ActionErrors errores= new ActionErrors();
		 if(this.getEstado().equals("guardarConciliacion"))
	    {	  	
	  	   if(this.getCodConvenioC().equals("-1"))
	  		 errores.add("descripcion",new ActionMessage("errors.required","El Convenio "));
	  	   if(this.getNroActa().equals(""))
	  		 errores.add("descripcion",new ActionMessage("errors.required","El numero de acta "));
	  	   if(this.getRepresentanteConvenio().equals(""))
	  		 errores.add("descripcion",new ActionMessage("errors.required","El Representante Convenio "));
	  	   if(this.getCargoRepConv().equals(""))
	  		 errores.add("descripcion",new ActionMessage("errors.required","El Cargo del Representante del Convenio "));
	  	   if(this.getRepresentanteInst().equals(""))
	  		 errores.add("descripcion",new ActionMessage("errors.required","El Representante de la Institucion "));
	  	   if(this.getCargoRepInst().equals(""))
	  		 errores.add("descripcion",new ActionMessage("errors.required","El Cargo del Representante de la Institucion "));
	  	   if(this.getSoportado().equals(""))
	  		 errores.add("descripcion",new ActionMessage("errors.required","El Porcentaje Soportado "));
	  	   if(this.getAceptado().equals(""))
	  		 errores.add("descripcion",new ActionMessage("errors.required","El Porcentaje Aceptado "));
	  	   if(this.getConceptoC().equals("-1"))
	  		 errores.add("descripcion",new ActionMessage("errors.required","El Concepto de Conciliacion "));
	  	   if(this.getHoraC().equals(""))
	  		 errores.add("descripcion",new ActionMessage("errors.required","La Hora de Conciliacion "));
	  	   else
	  	   {
	  		   if(this.getConciliacionC().equals(""))
	  		   {
	  			   if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),UtilidadFecha.getHoraActual(),this.getFechaC(), this.getHoraC()).isTrue())
	  		   		errores.add("descripcion",new ActionMessage("errors.horaPosteriorAOtraDeReferencia",this.getHoraC().toString(),UtilidadFecha.getHoraActual()));
	  		   }
	  	   }
	  	   if(this.getFechaC().equals(""))
	  	       errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Conciliacion "));
	  	   else
	  	   {
	  		   if(this.getConciliacionC().equals(""))
	  		   {
		  		   if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(),"00:00",this.getFechaC().toString(), "00:00").isTrue())
						errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual",this.getFechaC().toString(),UtilidadFecha.getFechaActual()));
	  		   }
	  	   }
	  	   if(!errores.isEmpty())
	  		   this.setEstado("empezar");
	     }
		
	    return errores;
    }
}