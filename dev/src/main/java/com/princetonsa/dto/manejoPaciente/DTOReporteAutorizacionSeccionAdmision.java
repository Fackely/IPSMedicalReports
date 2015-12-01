package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;

/**
 * Esta clase se encarga de contener los datos de la 
 * admisión, necesarios para generar el reporte de autorización
 * de ingreso estancia con el formato de Versalles
 * 
 * @author Diana Carolina G
 * @since 01/03/2011
 */
public class DTOReporteAutorizacionSeccionAdmision implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String fechaAdmision;
	private String horaAdmision;
	private String viaIngresoAdmision;
	private String dxPrincipalAdmision;
	private String tipoCieDxPrincipalAdmision;
	private String dxComplicacionAdmision;
	private String tipoCieDxComplicacionAdmision;
	private String medicoSolicitanteAdmision;
	
	private String observacionesAdmision;
	
	
	public DTOReporteAutorizacionSeccionAdmision(){
		
	}
	
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo fechaAdmision
	 * @return retorna la variable fechaAdmision 
	 * 
	 */
	public String getFechaAdmision() {
		return fechaAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de establecer el valor
	 * del atributo fechaAdmision
	 * @param fechaAdmision
	 */
	public void setFechaAdmision(String fechaAdmision) {
		this.fechaAdmision = fechaAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo horaAdmision
	 * @return horaAdmision
	 */
	public String getHoraAdmision() {
		return horaAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de establecer el valor
	 * del atributo horaAdmision
	 * @param horaAdmision
	 */
	public void setHoraAdmision(String horaAdmision) {
		this.horaAdmision = horaAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo viaIngresoAdmision
	 * @return viaIngresoAdmision
	 */
	public String getViaIngresoAdmision() {
		return viaIngresoAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de establecer el valor
	 * del atributo viaIngresoAdmision
	 * @param viaIngresoAdmision
	 */
	public void setViaIngresoAdmision(String viaIngresoAdmision) {
		this.viaIngresoAdmision = viaIngresoAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo dxPrincipalAdmision
	 * @return dxPrincipalAdmision
	 */
	public String getDxPrincipalAdmision() {
		return dxPrincipalAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de establecer el valor
	 * del atributo  dxPrincipalAdmision
	 * @param dxPrincipalAdmision
	 */
	public void setDxPrincipalAdmision(String dxPrincipalAdmision) {
		this.dxPrincipalAdmision = dxPrincipalAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo dxComplicacionAdmision
	 * @return dxComplicacionAdmision
	 */
	public String getDxComplicacionAdmision() {
		return dxComplicacionAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de establecer el valor
	 * del atributo dxComplicacionAdmision
	 * @param dxComplicacionAdmision
	 */
	public void setDxComplicacionAdmision(String dxComplicacionAdmision) {
		this.dxComplicacionAdmision = dxComplicacionAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo medicoSolicitanteAdmision
	 * @return medicoSolicitanteAdmision
	 */
	public String getMedicoSolicitanteAdmision() {
		return medicoSolicitanteAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de establecer el valor
	 * del atributo medicoSolicitanteAdmision
	 * @param medicoSolicitanteAdmision
	 */
	public void setMedicoSolicitanteAdmision(String medicoSolicitanteAdmision) {
		this.medicoSolicitanteAdmision = medicoSolicitanteAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo observacionesAdmision
	 * @return observacionesAdmision
	 */
	public String getObservacionesAdmision() {
		return observacionesAdmision;
	}
	
	/**
	 * Este M&eacute;todo se encarga de establecer el valor
	 * del atributo observacionesAdmision
	 * @param observacionesAdmision
	 */
	public void setObservacionesAdmision(String observacionesAdmision) {
		this.observacionesAdmision = observacionesAdmision;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo tipoCieDxPrincipalAdmision
	 * @return tipoCieDxPrincipalAdmision
	 */
	public String getTipoCieDxPrincipalAdmision() {
		return tipoCieDxPrincipalAdmision;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor
	 * del atributo tipoCieDxPrincipalAdmision
	 * @param tipoCieDxPrincipalAdmision
	 */
	public void setTipoCieDxPrincipalAdmision(String tipoCieDxPrincipalAdmision) {
		this.tipoCieDxPrincipalAdmision = tipoCieDxPrincipalAdmision;
	}

	/**
	 * Este M&eacute;todo se encarga de obtener el valor 
	 * del atributo tipoCieDxComplicacionAdmision
	 * @return tipoCieDxComplicacionAdmision
	 */
	public String getTipoCieDxComplicacionAdmision() {
		return tipoCieDxComplicacionAdmision;
	}

	/**
	 * Este M&eacute;todo se encarga de establecer el valor
	 * del atributo tipoCieDxComplicacionAdmision
	 * @param tipoCieDxComplicacionAdmision
	 */
	public void setTipoCieDxComplicacionAdmision(
			String tipoCieDxComplicacionAdmision) {
		this.tipoCieDxComplicacionAdmision = tipoCieDxComplicacionAdmision;
	}
	
	
	
		
	

}
