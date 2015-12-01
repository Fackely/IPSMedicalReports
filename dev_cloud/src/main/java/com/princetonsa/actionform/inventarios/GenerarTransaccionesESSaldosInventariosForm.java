/**
 * 
 */
package com.princetonsa.actionform.inventarios;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

/**
 * @author axioma
 *
 */
public class GenerarTransaccionesESSaldosInventariosForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String centroAtencion;
	
	/**
	 * 
	 */
	private String almacen;
	
	/**
	 * 
	 */
	private String transaccionEntrada;
	
	/**
	 * 
	 */
	private String transaccionSalida;
	
	/**
	 * 
	 */
	private String observaciones;
	
	/**
	 * 
	 */
	private HashMap<String, Object> transaccionCC;

	/**
	 * 
	 */
	private String consecutivoTranEnt;

	/**
	 * 
	 */
	private String consecutivoTranSal;
	
	/**
	 * 
	 */
	private HashMap clasesInventario;
	
	/**
	 * 
	 */
	private HashMap gruposInventario;
	
	/**
	 * 
	 */
	private String claseInventario;
	
	
	/**
	 * 
	 */
	private String grupoInventario;
	
	public void reset()
	{
		this.centroAtencion="";
		this.almacen="";
		this.transaccionEntrada="";
		this.transaccionSalida="";
		this.observaciones="";
		this.transaccionCC=new HashMap<String, Object>();
		this.transaccionCC.put("numRegistros", "0");
		this.consecutivoTranEnt="";
		this.consecutivoTranSal="";
		this.clasesInventario=new HashMap<String, Object>();
		this.clasesInventario.put("numRegistros", "0");
		this.gruposInventario=new HashMap<String, Object>();
		this.gruposInventario.put("numRegistros", "0");
		this.claseInventario="";
		this.grupoInventario="";
	}

	public String getAlmacen() {
		return almacen;
	}

	public void setAlmacen(String almacen) {
		this.almacen = almacen;
	}

	public String getCentroAtencion() {
		return centroAtencion;
	}

	public void setCentroAtencion(String centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getTransaccionEntrada() {
		return transaccionEntrada;
	}

	public void setTransaccionEntrada(String transaccionEntrada) {
		this.transaccionEntrada = transaccionEntrada;
	}

	public String getTransaccionSalida() {
		return transaccionSalida;
	}

	public void setTransaccionSalida(String transaccionSalida) {
		this.transaccionSalida = transaccionSalida;
	}

	public HashMap<String, Object> getTransaccionCC() {
		return transaccionCC;
	}

	public void setTransaccionCC(HashMap<String, Object> transaccionCC) {
		this.transaccionCC = transaccionCC;
	}
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("generar"))
		{
			if(this.centroAtencion.trim().equals(""))
			{
				errores.add("falta campo", new ActionMessage("errors.required","Centro Atención"));
			}
			if(this.almacen.trim().equals(""))
			{
				errores.add("falta campo", new ActionMessage("errors.required","Almacén"));
			}
			if(this.transaccionEntrada.trim().equals(""))
			{
				errores.add("falta campo", new ActionMessage("errors.required","Tipo Transaccion Entrada"));
			}
			if(this.transaccionSalida.trim().equals(""))
			{
				errores.add("falta campo", new ActionMessage("errors.required","Tipo Transaccion Salida"));
			}
		}
		return errores;
	}

	public String getConsecutivoTranEnt() {
		return consecutivoTranEnt;
	}

	public void setConsecutivoTranEnt(String consecutivoTranEnt) {
		this.consecutivoTranEnt = consecutivoTranEnt;
	}

	public String getConsecutivoTranSal() {
		return consecutivoTranSal;
	}

	public void setConsecutivoTranSal(String consecutivoTranSal) {
		this.consecutivoTranSal = consecutivoTranSal;
	}

	public HashMap getClasesInventario() {
		return clasesInventario;
	}

	public void setClasesInventario(HashMap clasesInventario) {
		this.clasesInventario = clasesInventario;
	}

	public HashMap getGruposInventario() {
		return gruposInventario;
	}

	public void setGruposInventario(HashMap gruposInventario) {
		this.gruposInventario = gruposInventario;
	}

	public String getClaseInventario() {
		return claseInventario;
	}

	public void setClaseInventario(String claseInventario) {
		this.claseInventario = claseInventario;
	}

	public String getGrupoInventario() {
		return grupoInventario;
	}

	public void setGrupoInventario(String grupoInventario) {
		this.grupoInventario = grupoInventario;
	}
}
