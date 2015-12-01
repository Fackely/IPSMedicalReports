package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

/**
 * Esta clase se encarga de contener los datos del 
 * paciente, necesarios para generar el reporte de autorización
 * de artículos con el formato de Versalles
 * 
 * @author Angela Maria Aguirre
 * @since 27/12/2010
 */
public class DTOReporteAutorizacionSeccionPaciente implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String nombrePaciente;
	private String tipoDocPaciente;
	private String numeroDocPaciente;
	private String edadPaciente;
	private String convenioPaciente;
	private String tipoContratoPaciente;
	private String categoriaSocioEconomica;
	private String tipoAfiliado;
	private String recobro;
	private String entidadRecobro;
	private String montoCobro;
	
	
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
	 * del atributo tipoDocPaciente
	
	 * @return retorna la variable tipoDocPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoDocPaciente() {
		return tipoDocPaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoDocPaciente
	
	 * @param valor para el atributo tipoDocPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoDocPaciente(String tipoDocPaciente) {
		this.tipoDocPaciente = tipoDocPaciente;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo numeroDocPaciente
	
	 * @return retorna la variable numeroDocPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getNumeroDocPaciente() {
		return numeroDocPaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo numeroDocPaciente
	
	 * @param valor para el atributo numeroDocPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setNumeroDocPaciente(String numeroDocPaciente) {
		this.numeroDocPaciente = numeroDocPaciente;
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
	 * del atributo convenioPaciente
	
	 * @return retorna la variable convenioPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getConvenioPaciente() {
		return convenioPaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo convenioPaciente
	
	 * @param valor para el atributo convenioPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setConvenioPaciente(String convenioPaciente) {
		this.convenioPaciente = convenioPaciente;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoContratoPaciente
	
	 * @return retorna la variable tipoContratoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoContratoPaciente() {
		return tipoContratoPaciente;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoContratoPaciente
	
	 * @param valor para el atributo tipoContratoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoContratoPaciente(String tipoContratoPaciente) {
		this.tipoContratoPaciente = tipoContratoPaciente;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo categoriaSocioEconomica
	
	 * @return retorna la variable categoriaSocioEconomica 
	 * @author Angela Maria Aguirre 
	 */
	public String getCategoriaSocioEconomica() {
		return categoriaSocioEconomica;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo categoriaSocioEconomica
	
	 * @param valor para el atributo categoriaSocioEconomica 
	 * @author Angela Maria Aguirre 
	 */
	public void setCategoriaSocioEconomica(String categoriaSocioEconomica) {
		this.categoriaSocioEconomica = categoriaSocioEconomica;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo tipoAfiliado
	
	 * @return retorna la variable tipoAfiliado 
	 * @author Angela Maria Aguirre 
	 */
	public String getTipoAfiliado() {
		return tipoAfiliado;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo tipoAfiliado
	
	 * @param valor para el atributo tipoAfiliado 
	 * @author Angela Maria Aguirre 
	 */
	public void setTipoAfiliado(String tipoAfiliado) {
		this.tipoAfiliado = tipoAfiliado;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo recobro
	
	 * @return retorna la variable recobro 
	 * @author Angela Maria Aguirre 
	 */
	public String getRecobro() {
		return recobro;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo recobro
	
	 * @param valor para el atributo recobro 
	 * @author Angela Maria Aguirre 
	 */
	public void setRecobro(String recobro) {
		this.recobro = recobro;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo entidadRecobro
	
	 * @return retorna la variable entidadRecobro 
	 * @author Angela Maria Aguirre 
	 */
	public String getEntidadRecobro() {
		return entidadRecobro;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo entidadRecobro
	
	 * @param valor para el atributo entidadRecobro 
	 * @author Angela Maria Aguirre 
	 */
	public void setEntidadRecobro(String entidadRecobro) {
		this.entidadRecobro = entidadRecobro;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo montoCobro
	
	 * @return retorna la variable montoCobro 
	 * @author Angela Maria Aguirre 
	 */
	public String getMontoCobro() {
		return montoCobro;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo montoCobro
	
	 * @param valor para el atributo montoCobro 
	 * @author Angela Maria Aguirre 
	 */
	public void setMontoCobro(String montoCobro) {
		this.montoCobro = montoCobro;
	}
	
	

}
