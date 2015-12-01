package com.princetonsa.dto.odontologia;

import java.io.Serializable;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 2/11/2010
 */
public class DTOReportePlanoDetallePacientesEstadoPresupuesto implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String fechaModificacion;
	private String estadoPresupuesto;
	private String edadPaciente;
	private String sexoPaciente;
	private String nombreCentroAtencion;
	private String nombreCiudad;
	private String nombrePais;
	private String nombreRegion;
	private String tipoID;
	private String numeroID;
	private String primerApellido;
	private String segundoApellido;
	private String primerNombre;
	private String segundoNombre;
	private String nombreCentroAtencionDuenio;
	private String numeroPresupuesto;	
	private String valorTotalPresupuesto;
	
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaModificacion
	
	 * @return retorna la variable fechaModificacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaModificacion
	
	 * @param valor para el atributo fechaModificacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estadoPresupuesto
	
	 * @return retorna la variable estadoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstadoPresupuesto() {
		return estadoPresupuesto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estadoPresupuesto
	
	 * @param valor para el atributo estadoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstadoPresupuesto(String estadoPresupuesto) {
		this.estadoPresupuesto = estadoPresupuesto;
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
	 * del atributo nombreCiudad
	
	 * @return retorna la variable nombreCiudad 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreCiudad() {
		return nombreCiudad;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreCiudad
	
	 * @param valor para el atributo nombreCiudad 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreCiudad(String nombreCiudad) {
		this.nombreCiudad = nombreCiudad;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombrePais
	
	 * @return retorna la variable nombrePais 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombrePais() {
		return nombrePais;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombrePais
	
	 * @param valor para el atributo nombrePais 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombrePais(String nombrePais) {
		this.nombrePais = nombrePais;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreRegion
	
	 * @return retorna la variable nombreRegion 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreRegion() {
		return nombreRegion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreRegion
	
	 * @param valor para el atributo nombreRegion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreRegion(String nombreRegion) {
		this.nombreRegion = nombreRegion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoID
	
	 * @return retorna la variable tipoID 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoID() {
		return tipoID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoID
	
	 * @param valor para el atributo tipoID 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoID(String tipoID) {
		this.tipoID = tipoID;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo numeroID
	
	 * @return retorna la variable numeroID 
	 * @author Angela Maria Aguirre 
	 */
	public String getNumeroID() {
		return numeroID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo numeroID
	
	 * @param valor para el atributo numeroID 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroID(String numeroID) {
		this.numeroID = numeroID;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo primerApellido
	
	 * @return retorna la variable primerApellido 
	 * @author Angela Maria Aguirre 
	 */
	public String getPrimerApellido() {
		return primerApellido;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo primerApellido
	
	 * @param valor para el atributo primerApellido 
	 * @author Angela Maria Aguirre 
	 */
	public void setPrimerApellido(String primerApellido) {
		this.primerApellido = primerApellido;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo segundoApellido
	
	 * @return retorna la variable segundoApellido 
	 * @author Angela Maria Aguirre 
	 */
	public String getSegundoApellido() {
		return segundoApellido;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo segundoApellido
	
	 * @param valor para el atributo segundoApellido 
	 * @author Angela Maria Aguirre 
	 */
	public void setSegundoApellido(String segundoApellido) {
		this.segundoApellido = segundoApellido;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo primerNombre
	
	 * @return retorna la variable primerNombre 
	 * @author Angela Maria Aguirre 
	 */
	public String getPrimerNombre() {
		return primerNombre;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo primerNombre
	
	 * @param valor para el atributo primerNombre 
	 * @author Angela Maria Aguirre 
	 */
	public void setPrimerNombre(String primerNombre) {
		this.primerNombre = primerNombre;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo segundoNombre
	
	 * @return retorna la variable segundoNombre 
	 * @author Angela Maria Aguirre 
	 */
	public String getSegundoNombre() {
		return segundoNombre;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo segundoNombre
	
	 * @param valor para el atributo segundoNombre 
	 * @author Angela Maria Aguirre 
	 */
	public void setSegundoNombre(String segundoNombre) {
		this.segundoNombre = segundoNombre;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreCentroAtencionDuenio
	
	 * @return retorna la variable nombreCentroAtencionDuenio 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreCentroAtencionDuenio() {
		return nombreCentroAtencionDuenio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreCentroAtencionDuenio
	
	 * @param valor para el atributo nombreCentroAtencionDuenio 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreCentroAtencionDuenio(String nombreCentroAtencionDuenio) {
		this.nombreCentroAtencionDuenio = nombreCentroAtencionDuenio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo numeroPresupuesto
	
	 * @return retorna la variable numeroPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public String getNumeroPresupuesto() {
		return numeroPresupuesto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo numeroPresupuesto
	
	 * @param valor para el atributo numeroPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroPresupuesto(String numeroPresupuesto) {
		this.numeroPresupuesto = numeroPresupuesto;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo valorTotalPresupuesto
	
	 * @return retorna la variable valorTotalPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public String getValorTotalPresupuesto() {
		return valorTotalPresupuesto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo valorTotalPresupuesto
	
	 * @param valor para el atributo valorTotalPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setValorTotalPresupuesto(String valorTotalPresupuesto) {
		this.valorTotalPresupuesto = valorTotalPresupuesto;
	}	

}
