/**
 * 
 */
package com.princetonsa.actionform.historiaClinica;

import java.util.HashMap;

import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

/**
 * @author axioma
 *
 */
public class ConsultaIngresosHospitalDiaForm extends ValidatorForm 
{
	
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * 
	 */
	private HashMap<String, Object> ingresosHospitalDia;
	
	/**
	 * 
	 */
	private HashMap<String, Object> detalleIngreso;
	
	
	/**
	 * 
	 */
	private int indiceSeleccionado;
	
	/**
	 * 
	 */
	private int centroAtencionfiltro;
	
	/**
	 * 
	 */
	private String fechaInicialFiltro;
	
	/**
	 * 
	 */
	private String fechaFinalFiltro;
	
	/**
	 * 
	 */
	private String usuarioFiltro;
	
	/**
	 * 
	 *
	 */
	public void reset() 
	{
		this.ingresosHospitalDia=new HashMap<String, Object>();
		this.ingresosHospitalDia.put("numRegistros", "0");
		this.indiceSeleccionado=ConstantesBD.codigoNuncaValido;
		this.detalleIngreso=new HashMap<String, Object>();
		this.detalleIngreso.put("numRegistros", "0");
		this.centroAtencionfiltro=ConstantesBD.codigoNuncaValido;
		this.fechaInicialFiltro="";
		this.fechaFinalFiltro="";
		this.usuarioFiltro="";
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap<String, Object> getIngresosHospitalDia() {
		return ingresosHospitalDia;
	}

	public void setIngresosHospitalDia(HashMap<String, Object> ingresosHospitalDia) {
		this.ingresosHospitalDia = ingresosHospitalDia;
	}
	public Object getIngresosHospitalDia(String key) {
		return ingresosHospitalDia.get(key);
	}

	public void setIngresosHospitalDia(String key,Object value) {
		this.ingresosHospitalDia.put(key, value);
	}

	public int getIndiceSeleccionado() {
		return indiceSeleccionado;
	}

	public void setIndiceSeleccionado(int indiceSeleccionado) {
		this.indiceSeleccionado = indiceSeleccionado;
	}

	public HashMap<String, Object> getDetalleIngreso() {
		return detalleIngreso;
	}

	public void setDetalleIngreso(HashMap<String, Object> detalleIngreso) {
		this.detalleIngreso = detalleIngreso;
	}
	
	public Object getDetalleIngreso(String key) 
	{
		return detalleIngreso.get(key);
	}

	public void setDetalleIngreso(String key,Object value) 
	{
		this.detalleIngreso.put(key, value);
	}

	public int getCentroAtencionfiltro() {
		return centroAtencionfiltro;
	}

	public void setCentroAtencionfiltro(int centroAtencionfiltro) {
		this.centroAtencionfiltro = centroAtencionfiltro;
	}

	public String getFechaFinalFiltro() {
		return fechaFinalFiltro;
	}

	public void setFechaFinalFiltro(String fechaFinalFiltro) {
		this.fechaFinalFiltro = fechaFinalFiltro;
	}

	public String getFechaInicialFiltro() {
		return fechaInicialFiltro;
	}

	public void setFechaInicialFiltro(String fechaInicialFiltro) {
		this.fechaInicialFiltro = fechaInicialFiltro;
	}

	public String getUsuarioFiltro() {
		return usuarioFiltro;
	}

	public void setUsuarioFiltro(String usuarioFiltro) {
		this.usuarioFiltro = usuarioFiltro;
	}
	
}
