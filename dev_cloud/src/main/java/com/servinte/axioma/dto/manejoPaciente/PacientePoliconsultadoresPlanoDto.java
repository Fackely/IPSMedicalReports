/**
 * 
 */
package com.servinte.axioma.dto.manejoPaciente;

import net.sf.jasperreports.engine.JRDataSource;




public class PacientePoliconsultadoresPlanoDto {
	
	private String fechaInicial;
	
	private String fechaFinal;
	
	private int codigoConvenio;
	
	private String convenio;
	
	private int codigoPaciente;
	
	private String paciente;
	
	private String documento;
	
	private String cantidadIngresos;
	
	private int codigoViasIngreso;
	
	private String nombreViasIngreso;
	
	private String tipoServicio;
	
	private int codigoEspecialidad;
	
	private String nombreEspecialidad;
	
	private int codigoUnidadAgenda;
	
	private String nombreUnidadAgenda;
	
	private String cantidadServicios;
	
	private JRDataSource JRDPacientes;

	public String getFechaInicial() {
		return fechaInicial;
	}

	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}

	public String getFechaFinal() {
		return fechaFinal;
	}

	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	public String getConvenio() {
		return convenio;
	}

	public void setConvenio(String convenio) {
		this.convenio = convenio;
	}

	public String getPaciente() {
		return paciente;
	}

	public void setPaciente(String paciente) {
		this.paciente = paciente;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getCantidadIngresos() {
		return cantidadIngresos;
	}

	public void setCantidadIngresos(String cantidadIngresos) {
		this.cantidadIngresos = cantidadIngresos;
	}

	public String getNombreViasIngreso() {
		return nombreViasIngreso;
	}

	public void setNombreViasIngreso(String nombreViasIngreso) {
		this.nombreViasIngreso = nombreViasIngreso;
	}

	public String getTipoServicio() {
		return tipoServicio;
	}

	public void setTipoServicio(String tipoServicio) {
		this.tipoServicio = tipoServicio;
	}

	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}

	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}

	public String getNombreUnidadAgenda() {
		return nombreUnidadAgenda;
	}

	public void setNombreUnidadAgenda(String nombreUnidadAgenda) {
		this.nombreUnidadAgenda = nombreUnidadAgenda;
	}

	public String getCantidadServicios() {
		return cantidadServicios;
	}

	public void setCantidadServicios(String cantidadServicios) {
		this.cantidadServicios = cantidadServicios;
	}

	public int getCodigoConvenio() {
		return codigoConvenio;
	}

	public void setCodigoConvenio(int codigoConvenio) {
		this.codigoConvenio = codigoConvenio;
	}

	public int getCodigoPaciente() {
		return codigoPaciente;
	}

	public void setCodigoPaciente(int codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public int getCodigoViasIngreso() {
		return codigoViasIngreso;
	}

	public void setCodigoViasIngreso(int codigoViasIngreso) {
		this.codigoViasIngreso = codigoViasIngreso;
	}

	public int getCodigoEspecialidad() {
		return codigoEspecialidad;
	}

	public void setCodigoEspecialidad(int codigoEspecialidad) {
		this.codigoEspecialidad = codigoEspecialidad;
	}

	public int getCodigoUnidadAgenda() {
		return codigoUnidadAgenda;
	}

	public void setCodigoUnidadAgenda(int codigoUnidadAgenda) {
		this.codigoUnidadAgenda = codigoUnidadAgenda;
	}

	public JRDataSource getJRDPacientes() {
		return JRDPacientes;
	}

	public void setJRDPacientes(JRDataSource jRDPacientes) {
		JRDPacientes = jRDPacientes;
	}


	
	
	
}
