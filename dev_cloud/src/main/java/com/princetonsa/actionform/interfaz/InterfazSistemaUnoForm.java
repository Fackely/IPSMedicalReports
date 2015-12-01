package com.princetonsa.actionform.interfaz;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.facturacion.DtoFactura;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadFileUpload;

/**
 * 
 * @author Andrés Silva Monsalve
 *
 */

public class InterfazSistemaUnoForm extends ValidatorForm
{
	//	--------------------Atributos
	private Logger logger = Logger.getLogger(InterfazSistemaUnoForm.class);
	
	private String estado;
	
	// HashMap que contiene todos los datos para generar el plano
	private HashMap facturas;
	
//	 HashMap que contiene todos los datos para generar el plano
	private HashMap recibosCaja;
	
	//String para capturar el tipo de Interfaz (por Facturacion o Recibos de Caja o Todos)
	private String tipo;
	
	// String para capturar el tipo estado del check box
	private String reproceso;
	
	//String para capturar la fecha inicial
	private String fechaInicial;
	
	//String para capturar la fecha Final
	private String fechaFinal;
	
	//String para capturar el nombre del archivo
	private String nombre;
	
	// String para capturar el nombre del Archivo Backup
	private String backupArchivo;
	
	//String para capturar la ruta del archivo
	private String path;
	
	private ArrayList<DtoFactura> facturaDto;
	
	private HashMap<String, Object> contenidoArchivo = new HashMap<String, Object>();
	
	// CREAR UN ARRAY LIST PARA LLENARLO CON LA INFO DEL DTO FACTURA
	
	


	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
		{
	    	ActionErrors errores = new ActionErrors();
	    	errores = super.validate(mapping, request);
		 		    	
		 	if(this.estado.equals("consultar"))
		 	{
		 		if (this.getTipo().equals("Seleccione"))
    				errores.add("descripcion",new ActionMessage("errors.required","Tipo de Interfaz ")); 
		 				 		
		 		if(this.getFechaInicial().equals("") )
		 			errores.add("descripcion",new ActionMessage("errors.required","La fecha Inicial"));
		 		
		 		if(this.getFechaFinal().equals(""))
		 			errores.add("descripcion",new ActionMessage("errors.required","La fecha Final"));
		 		
		 			
		 		if(!this.fechaInicial.equals("") || !this.getFechaFinal().equals("") )
		 		{
		 			if(!UtilidadFecha.validarFecha(this.getFechaInicial().toString()))
		 			{
		 				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido","Inicial"+this.fechaInicial.toString()+" "));
		 			}
		 			if(!UtilidadFecha.validarFecha(this.getFechaFinal().toString()))
		 			{
		 				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido","Final"+this.fechaFinal.toString()+" "));
		 			}
		 			if(!UtilidadFecha.compararFechas(this.fechaFinal, "00:00", this.fechaInicial, "00:00").isTrue())
		 			{
		 				errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Inicial "+this.fechaInicial," Final "+this.fechaFinal ));
		 			}else
		 			{
		 				if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), "00:00", this.fechaInicial, "00:00").isTrue())
		 					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual", this.fechaInicial,"Final"+this.getFechaFinal()));
		 				
		 				if(!UtilidadFecha.compararFechas(UtilidadFecha.getFechaActual(), "00:00", this.fechaFinal, "00:00").isTrue())
		 					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual", this.fechaInicial,"Final",UtilidadFecha.getFechaActual()));
		 			}
		 			if(UtilidadFecha.numeroDiasEntreFechas(this.fechaInicial, this.fechaFinal)>15)
		 			{
		 				errores.add("descripcion", new ActionMessage("errors.fechaSuperaOtraPorDias", "Final","15", "Inicial"));
		 			}	 
		 		}
		 		if (this.getNombre().equals(""))
    				errores.add("descripcion",new ActionMessage("errors.required","Nombre del Archivo ")); 
		 		
		 		if(!UtilidadFileUpload.validarExistePath(this.path))
					errores.add("descripcion",new ActionMessage("errors.invalid","Verifique que Exista la Ruta. Ruta "));
	
		 	}
		 	return errores;
		}

	 public void reset()
	 {
		 this.facturas=new HashMap ();
		 this.recibosCaja=new HashMap ();
		 this.tipo = "";
		 this.reproceso = ConstantesBD.acronimoNo;
		 this.fechaInicial = "";
		 this.fechaFinal = "";
		 this.nombre = "";
		 this.path = "";
		 this.facturaDto = new ArrayList<DtoFactura>();
		 this.contenidoArchivo = new HashMap<String, Object>();
	 }
	 

	public String getEstado() {
		return estado;
	}




	public void setEstado(String estado) {
		this.estado = estado;
	}




	public HashMap getFacturas() {
		return facturas;
	}


	public Object getFacturas(String key) {
		return facturas.get(key);
	}


	public void setFacturas(HashMap facturas) {
		this.facturas = facturas;
	}
	
	public void setFacturas(String key, Object value) {
		this.facturas.put(key, value);
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getReproceso() {
		return reproceso;
	}

	public void setReproceso(String reproceso) {
		this.reproceso = reproceso;
	}

	public ArrayList<DtoFactura> getFacturaDto() {
		return facturaDto;
	}

	public void setFacturaDto(ArrayList<DtoFactura> facturaDto) {
		this.facturaDto = facturaDto;
	}

	public HashMap<String, Object> getContenidoArchivo() {
		return contenidoArchivo;
	}

	public void setContenidoArchivo(HashMap<String, Object> contenidoArchivo) {
		this.contenidoArchivo = contenidoArchivo;
	}

	public HashMap getRecibosCaja() {
		return recibosCaja;
	}

	public void setRecibosCaja(HashMap recibosCaja) {
		this.recibosCaja = recibosCaja;
	}

	public void setRecibosCaja(String key, Object value) {
		this.recibosCaja.put(key, value);
	}

	public String getBackupArchivo() {
		return backupArchivo;
	}

	public void setBackupArchivo(String backupArchivo) {
		this.backupArchivo = backupArchivo;
	}
	
	
}