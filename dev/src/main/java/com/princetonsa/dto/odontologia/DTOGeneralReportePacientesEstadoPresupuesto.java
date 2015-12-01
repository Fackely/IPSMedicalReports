package com.princetonsa.dto.odontologia;

import java.io.Serializable;

import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 * Esta clase se encarga de almacenar la información
 * general del reporte de pacientes por estado del
 * presupuesto odontológico
 * 
 * @author Angela Maria Aguirre
 * @since 22/10/2010
 */
public class DTOGeneralReportePacientesEstadoPresupuesto implements
		Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fechaInicial;
	private String fechaFinal;
	private String edadPaciente;
	private String sexoPaciente;
	private String paqueteOdontologico;
	private String programaeOdontologico;
	private String usuarioProceso;
	private String razonSocial;
	private String logoDerecha;
	private String logoIzquierda;
	private String nombreCentroAtencion;
	private String nombreEstadoGeneracionReporteDetalle;
	private JRBeanCollectionDataSource dsListaInstitucion;
	private JRBeanCollectionDataSource dsListaPacientes;
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaInicial
	
	 * @return retorna la variable fechaInicial 
	 * @author Angela Maria Aguirre 
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaInicial
	
	 * @param valor para el atributo fechaInicial 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaFinal
	
	 * @return retorna la variable fechaFinal 
	 * @author Angela Maria Aguirre 
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaFinal
	
	 * @param valor para el atributo fechaFinal 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo edadPaciente
	
	 * @return retorna la variable edadPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getEdadPaciente() {
		return edadPaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo edadPaciente
	
	 * @param valor para el atributo edadPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setEdadPaciente(String edadPaciente) {
		this.edadPaciente = edadPaciente;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo sexoPaciente
	
	 * @return retorna la variable sexoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getSexoPaciente() {
		return sexoPaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo sexoPaciente
	
	 * @param valor para el atributo sexoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setSexoPaciente(String sexoPaciente) {
		this.sexoPaciente = sexoPaciente;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo paqueteOdontologico
	
	 * @return retorna la variable paqueteOdontologico 
	 * @author Angela Maria Aguirre 
	 */
	public String getPaqueteOdontologico() {
		return paqueteOdontologico;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo paqueteOdontologico
	
	 * @param valor para el atributo paqueteOdontologico 
	 * @author Angela Maria Aguirre 
	 */
	public void setPaqueteOdontologico(String paqueteOdontologico) {
		this.paqueteOdontologico = paqueteOdontologico;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo programaeOdontologico
	
	 * @return retorna la variable programaeOdontologico 
	 * @author Angela Maria Aguirre 
	 */
	public String getProgramaeOdontologico() {
		return programaeOdontologico;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo programaeOdontologico
	
	 * @param valor para el atributo programaeOdontologico 
	 * @author Angela Maria Aguirre 
	 */
	public void setProgramaeOdontologico(String programaeOdontologico) {
		this.programaeOdontologico = programaeOdontologico;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsListaInstitucion
	
	 * @return retorna la variable dsListaInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public JRBeanCollectionDataSource getDsListaInstitucion() {
		return dsListaInstitucion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsListaInstitucion
	
	 * @param valor para el atributo dsListaInstitucion 
	 * @author Angela Maria Aguirre 
	 */
	public void setDsListaInstitucion(JRBeanCollectionDataSource dsListaInstitucion) {
		this.dsListaInstitucion = dsListaInstitucion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo usuarioProceso
	
	 * @return retorna la variable usuarioProceso 
	 * @author Angela Maria Aguirre 
	 */
	public String getUsuarioProceso() {
		return usuarioProceso;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo usuarioProceso
	
	 * @param valor para el atributo usuarioProceso 
	 * @author Angela Maria Aguirre 
	 */
	public void setUsuarioProceso(String usuarioProceso) {
		this.usuarioProceso = usuarioProceso;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo razonSocial
	
	 * @return retorna la variable razonSocial 
	 * @author Angela Maria Aguirre 
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo razonSocial
	
	 * @param valor para el atributo razonSocial 
	 * @author Angela Maria Aguirre 
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo logoDerecha
	
	 * @return retorna la variable logoDerecha 
	 * @author Angela Maria Aguirre 
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo logoDerecha
	
	 * @param valor para el atributo logoDerecha 
	 * @author Angela Maria Aguirre 
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo logoIzquierda
	
	 * @return retorna la variable logoIzquierda 
	 * @author Angela Maria Aguirre 
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo logoIzquierda
	
	 * @param valor para el atributo logoIzquierda 
	 * @author Angela Maria Aguirre 
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo dsListaPacientes
	
	 * @return retorna la variable dsListaPacientes 
	 * @author Angela Maria Aguirre 
	 */
	public JRBeanCollectionDataSource getDsListaPacientes() {
		return dsListaPacientes;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo dsListaPacientes
	
	 * @param valor para el atributo dsListaPacientes 
	 * @author Angela Maria Aguirre 
	 */
	public void setDsListaPacientes(JRBeanCollectionDataSource dsListaPacientes) {
		this.dsListaPacientes = dsListaPacientes;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreCentroAtencion
	
	 * @return retorna la variable nombreCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreCentroAtencion
	
	 * @param valor para el atributo nombreCentroAtencion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreEstadoGeneracionReporteDetalle
	
	 * @return retorna la variable nombreEstadoGeneracionReporteDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreEstadoGeneracionReporteDetalle() {
		return nombreEstadoGeneracionReporteDetalle;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreEstadoGeneracionReporteDetalle
	
	 * @param valor para el atributo nombreEstadoGeneracionReporteDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreEstadoGeneracionReporteDetalle(
			String nombreEstadoGeneracionReporteDetalle) {
		this.nombreEstadoGeneracionReporteDetalle = nombreEstadoGeneracionReporteDetalle;
	}
	
	

}
