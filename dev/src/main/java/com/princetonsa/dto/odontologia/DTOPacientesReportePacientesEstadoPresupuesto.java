package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.Date;

import util.UtilidadTexto;

/**
 * Esta clase se encarga de
 * @author Angela Maria Aguirre
 * @since 15/10/2010
 */
public class DTOPacientesReportePacientesEstadoPresupuesto implements
		Serializable {	
	
	private static final long serialVersionUID = 1L;	
	private String tipoIdentificacion;
	private String nombrePaciente;
	private String apellidoPaciente;
	private String centroAtencionDueno;
	private long numeroPresupuesto;
	private int numeroContrato;
	private String valorTotalPresupuesto;
	private String numeroIdentificacion;
	private String segundoNombrePaciente;
	private String segundoApellidoPaciente;
	private long codigoPresupuesto; 
	private Date fechaModificacionPresupuesto;
	private Date fechaNacimiento;
	private String edadPaciente;
	private String sexoPaciente;
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoIdentificacion
	
	 * @return retorna la variable tipoIdentificacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoIdentificacion() {
		return tipoIdentificacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoIdentificacion
	
	 * @param valor para el atributo tipoIdentificacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoIdentificacion(String tipoIdentificacion) {
		this.tipoIdentificacion = tipoIdentificacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombrePaciente
	
	 * @return retorna la variable nombrePaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombrePaciente() {
		return nombrePaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombrePaciente
	
	 * @param valor para el atributo nombrePaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombrePaciente(String nombrePaciente) {
		this.nombrePaciente = nombrePaciente;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo apellidoPaciente
	
	 * @return retorna la variable apellidoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getApellidoPaciente() {
		return apellidoPaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo apellidoPaciente
	
	 * @param valor para el atributo apellidoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setApellidoPaciente(String apellidoPaciente) {
		this.apellidoPaciente = apellidoPaciente;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo centroAtencionDueno
	
	 * @return retorna la variable centroAtencionDueno 
	 * @author Angela Maria Aguirre 
	 */
	public String getCentroAtencionDueno() {
		return centroAtencionDueno;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo centroAtencionDueno
	
	 * @param valor para el atributo centroAtencionDueno 
	 * @author Angela Maria Aguirre 
	 */
	public void setCentroAtencionDueno(String centroAtencionDueno) {
		if(centroAtencionDueno==null){
			this.centroAtencionDueno = "";
		}else{
			this.centroAtencionDueno = centroAtencionDueno;
		}
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo numeroPresupuesto
	
	 * @return retorna la variable numeroPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public long getNumeroPresupuesto() {
		return numeroPresupuesto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo numeroPresupuesto
	
	 * @param valor para el atributo numeroPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroPresupuesto(long numeroPresupuesto) {
		this.numeroPresupuesto = numeroPresupuesto;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo numeroContrato
	
	 * @return retorna la variable numeroContrato 
	 * @author Angela Maria Aguirre 
	 */
	public int getNumeroContrato() {
		return numeroContrato;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo numeroContrato
	
	 * @param valor para el atributo numeroContrato 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroContrato(int numeroContrato) {
		this.numeroContrato = numeroContrato;
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
		if(UtilidadTexto.isEmpty(valorTotalPresupuesto)){
			this.valorTotalPresupuesto ="";
		}else{
			this.valorTotalPresupuesto = valorTotalPresupuesto;
		}
		
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo numeroIdentificacion
	
	 * @return retorna la variable numeroIdentificacion 
	 * @author Angela Maria Aguirre 
	 */
	public String getNumeroIdentificacion() {
		return numeroIdentificacion;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo numeroIdentificacion
	
	 * @param valor para el atributo numeroIdentificacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroIdentificacion(String numeroIdentificacion) {
		this.numeroIdentificacion = numeroIdentificacion;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo segundoNombrePaciente
	
	 * @return retorna la variable segundoNombrePaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getSegundoNombrePaciente() {
		return segundoNombrePaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo segundoNombrePaciente
	
	 * @param valor para el atributo segundoNombrePaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setSegundoNombrePaciente(String segundoNombrePaciente) {
		
		if(segundoNombrePaciente==null){
			this.segundoNombrePaciente = "";
		}else{
			this.segundoNombrePaciente = segundoNombrePaciente;
		}
		
	}
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoPresupuesto
	
	 * @return retorna la variable codigoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoPresupuesto() {
		return codigoPresupuesto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoPresupuesto
	
	 * @param valor para el atributo codigoPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPresupuesto(long codigoPresupuesto) {
		this.codigoPresupuesto = codigoPresupuesto;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo segundoApellidoPaciente
	
	 * @return retorna la variable segundoApellidoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getSegundoApellidoPaciente() {
		return segundoApellidoPaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo segundoApellidoPaciente
	
	 * @param valor para el atributo segundoApellidoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setSegundoApellidoPaciente(String segundoApellidoPaciente) {		
		if(segundoApellidoPaciente==null){
			this.segundoApellidoPaciente ="";
		}else{
			this.segundoApellidoPaciente = segundoApellidoPaciente;
		}
			
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaModificacionPresupuesto
	
	 * @return retorna la variable fechaModificacionPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaModificacionPresupuesto() {
		return fechaModificacionPresupuesto;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaModificacionPresupuesto
	
	 * @param valor para el atributo fechaModificacionPresupuesto 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaModificacionPresupuesto(Date fechaModificacionPresupuesto) {
		this.fechaModificacionPresupuesto = fechaModificacionPresupuesto;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaNacimiento
	
	 * @return retorna la variable fechaNacimiento 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaNacimiento
	
	 * @param valor para el atributo fechaNacimiento 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
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

}
