package com.princetonsa.actionform.manejoPaciente;

import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import com.lowagie.text.pdf.AcroFields;

import util.ConstantesBD;
import util.ResultadoBoolean;

import java.util.HashMap;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

public class ViasIngresoForm extends ValidatorForm {
	
	private HashMap viasIngresoMap;
	
	private int noTipPacViaIngreso;
	
	private HashMap garantiaPaciente;
	
	private String estado="";
		
	private HashMap viasIngresoEliminadosMap;
	
	private String linkSiguiente;
	
	/**
	 *  Para la navegacion del pager, cuando se ingresa
	 *  un registro nuevo.
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	private int indexSeleccionado;
	
	
	/**
	 *  para controlar la página actual
     *  del pager.
	 */
	 private int offset;
	 
	 
	/**
	 * Posicion del registro que se eliminara
	 */
	 private int posEliminar;
	 
	 private ArrayList convenios;
	 
	 
	 private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	 
	 public void reset(){
			
			this.viasIngresoMap=new HashMap();
			viasIngresoMap.put("numRegistros","0");
			
			this.viasIngresoEliminadosMap=new HashMap();
			viasIngresoEliminadosMap.put("numRegistros","0");
			
			this.garantiaPaciente=new HashMap();
			this.garantiaPaciente.put("numRegistros", "0");
			
			linkSiguiente="";
	       	this.maxPageItems=10;
	    	this.patronOrdenar="";
	    	this.ultimoPatron="";
	    	this.offset=0;
	    	this.posEliminar=ConstantesBD.codigoNuncaValido;
			this.convenios=new ArrayList();
			this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
			
		}
	 
	public int getNoTipPacViaIngreso() {
		return noTipPacViaIngreso;
	}



	public void setNoTipPacViaIngreso(int noTipPacViaIngreso) {
		this.noTipPacViaIngreso = noTipPacViaIngreso;
	}



	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
		{
			ActionErrors errores= new ActionErrors();
			
			if(this.estado.equals("guardar"))
			{
				int numReg=Integer.parseInt(this.viasIngresoMap.get("numRegistros")+"");
				for(int i=0;i<numReg;i++)
				{
					if((this.viasIngresoMap.get("codigo_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El Codigo del registro "+(i+1)));
					}
					else
					{
						for(int j=0;j<i;j++)
						{
							if((this.viasIngresoMap.get("codigo_"+i)+"").equalsIgnoreCase(this.viasIngresoMap.get("codigo_"+j)+""))
							{
								errores.add("", new ActionMessage("errors.yaExiste","El código "+this.viasIngresoMap.get("codigo_"+i)));
							}
						}
					}
					if((this.viasIngresoMap.get("nombre_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El nombre del registro "+(i+1)));
					}
					if((this.viasIngresoMap.get("paciente_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El campo Responsable de Paciente del registro "+(i+1)));
					}
					if((this.viasIngresoMap.get("verificacion_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El campo Verificacion de Derechos del registro "+(i+1)));
					}
					if((this.viasIngresoMap.get("recibo_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El campo Recibo Automatico del registro "+(i+1)));
					}
					if((this.viasIngresoMap.get("corte_facturacion_"+i)+"").trim().equals(""))
					{
						errores.add("codigo", new ActionMessage("errors.required","El campo Corte de Factuación del registro "+(i+1)));
					}
				}
			}
			return errores;
		}

	public ArrayList getConvenios() {
		return convenios;
	}

	public void setConvenios(ArrayList convenios) {
		this.convenios = convenios;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public int getMaxPageItems() {
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public int getPosEliminar() {
		return posEliminar;
	}

	public void setPosEliminar(int posEliminar) {
		this.posEliminar = posEliminar;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public HashMap getViasIngresoEliminadosMap() {
		return viasIngresoEliminadosMap;
	}

	public Object getViasIngresoEliminadosMap(String key) {
		return viasIngresoEliminadosMap.get(key);
	}
	
	public void setViasIngresoEliminadosMap(String key, Object Value) {
		this.viasIngresoMap.put(key,Value);
	}
	
	public void setViasIngresoEliminadosMap(HashMap viasIngresoEliminadosMap) {
		this.viasIngresoEliminadosMap = viasIngresoEliminadosMap;
	}

	public HashMap getViasIngresoMap() {
		return viasIngresoMap;
	}
	
	public Object getViasIngresoMap(String key) {
		return viasIngresoMap.get(key);
	}

	public void setViasIngresoMap(String key, Object Value) {
		this.viasIngresoMap.put(key,Value);
	}

	public void setViasIngresoMap(HashMap viasIngresoMap) {
		this.viasIngresoMap = viasIngresoMap;
	}

	public int getIndexSeleccionado() {
		return indexSeleccionado;
	}

	public void setIndexSeleccionado(int indexSeleccionado) {
		this.indexSeleccionado = indexSeleccionado;
	}

	public HashMap getGarantiaPaciente() {
		return garantiaPaciente;
	}

	public void setGarantiaPaciente(HashMap garantiaPaciente) {
		this.garantiaPaciente = garantiaPaciente;
	}
	
	public Object getGarantiaPaciente(String key)
	{
		return garantiaPaciente.get(key);
	}
	
	public void setGarantiaPaciente(String key,Object value)
	{
		this.garantiaPaciente.put(key, value);
	}
	
	

}
