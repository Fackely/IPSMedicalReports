package com.princetonsa.actionform.facturacion;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ResultadoBoolean;
import util.UtilidadFecha;
import util.Utilidades;

public class TotalFacturadoConvenioContratoForm extends ValidatorForm 
{

	/**
	 * 
	 */
	private String estado;
	
	private String anio;
	
	private String mes;
	
	private String excluirFacturas;
	
	private String convenio;
	
	private String contrato;
	
	private String tipoSalida;
	
	private ResultadoBoolean mensaje = new ResultadoBoolean(false);
    
	private String pathArchivoTxt;
	
	private boolean archivo;
	
	private boolean zip;
	
	private HashMap mapaConsultaFacturado;
	
	
	/**
	 * 
	 */
	public void reset()
	{
		this.estado="";
		this.anio=UtilidadFecha.getMesAnioDiaActual("anio")+"";
		this.mes="";
		this.excluirFacturas="";
		this.convenio="";
		this.contrato="";
		this.tipoSalida="";
		this.pathArchivoTxt="";
		this.archivo=false;
		this.zip=false;
		this.mapaConsultaFacturado = new HashMap();
		this.mapaConsultaFacturado.put("numRegistros", "0");
	}

	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		if(estado.equals("generar"))
		{
			if(this.anio.equals(""))
				errores.add("", new ActionMessage("errors.required","El Año "));
			
			else if(anio.length()!=4)
				errores.add("", new ActionMessage("errors.formatoAnoInvalido", "de Corte "+this.anio));
			
			else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia("01/01/"+this.anio,UtilidadFecha.getFechaActual()))
				errores.add("", new ActionMessage("errors.notEspecific","El año debe ser anterior o igual al actual."));
			if(this.tipoSalida.equals(""))
				errores.add("", new ActionMessage("errors.required","El Tipo de Salida "));
			if(!this.anio.equals("") && !this.mes.equals(""))
			{
				if(Utilidades.convertirAEntero(this.mes)<1||Utilidades.convertirAEntero(this.mes)>12)
					errores.add("", new ActionMessage("errors.notEspecific","El Mes ingresado no es un mes valido. Debe ser un numero entre 1 y 12"));
				else if (Utilidades.convertirAEntero(this.mes)<10 && Utilidades.convertirAEntero(this.mes)>0)
					this.setMes("0"+Utilidades.convertirAEntero(this.mes));
				else if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia("01/"+this.mes+"/"+this.anio,UtilidadFecha.getFechaActual()))
					errores.add("Fecha inicial mayor a la fecha del sistema",new ActionMessage("errors.fechaPosteriorIgualActual","inicial","actual"));
			}
		}
		
		return errores;
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getAnio() {
		return anio;
	}

	/**
	 * 
	 * @param anio
	 */
	public void setAnio(String anio) {
		this.anio = anio;
	}

	/**
	 * 
	 * @return
	 */
	public String getMes() {
		return mes;
	}

	/**
	 * 
	 * @param mes
	 */
	public void setMes(String mes) {
		this.mes = mes;
	}

	/**
	 * 
	 * @return
	 */
	public String getExcluirFacturas() {
		return excluirFacturas;
	}

	/**
	 * 
	 * @param excluirFacturas
	 */
	public void setExcluirFacturas(String excluirFacturas) {
		this.excluirFacturas = excluirFacturas;
	}

	/**
	 * 
	 * @return
	 */
	public String getConvenio() {
		return convenio;
	}

	/**
	 * 
	 * @param convenio
	 */
	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	/**
	 * 
	 * @return
	 */
	public String getContrato() {
		return contrato;
	}

	/**
	 * 
	 * @param contrato
	 */
	public void setContrato(String contrato) {
		this.contrato = contrato;
	}

	/**
	 * 
	 * @return
	 */
	public String getTipoSalida() {
		return tipoSalida;
	}
	
	/**
	 * 
	 * @param tipoSalida
	 */
	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 */
	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * 
	 * @return
	 */
	public String getPathArchivoTxt() {
		return pathArchivoTxt;
	}

	/**
	 * 
	 * @param pathArchivoTxt
	 */
	public void setPathArchivoTxt(String pathArchivoTxt) {
		this.pathArchivoTxt = pathArchivoTxt;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isArchivo() {
		return archivo;
	}

	/**
	 * 
	 * @param archivo
	 */
	public void setArchivo(boolean archivo) {
		this.archivo = archivo;
	}

	/**
	 * 
	 * @return
	 */
	public boolean isZip() {
		return zip;
	}

	/**
	 * 
	 * @param zip
	 */
	public void setZip(boolean zip) {
		this.zip = zip;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getMapaConsultaFacturado() {
		return mapaConsultaFacturado;
	}

	/**
	 * 
	 * @param mapaConsultaFacturado
	 */
	public void setMapaConsultaFacturado(HashMap mapaConsultaFacturado) {
		this.mapaConsultaFacturado = mapaConsultaFacturado;
	}

	
	
}
