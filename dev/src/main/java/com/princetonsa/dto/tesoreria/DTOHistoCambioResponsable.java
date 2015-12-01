package com.princetonsa.dto.tesoreria;

import java.io.Serializable;
import java.util.Date;

import com.servinte.axioma.orm.DetFaltanteSobrante;

/**
 * Esta clase se encarga de almacenar los datos de
 * un historial de cambio responsable de un registro 
 * faltante sobrante
 * 
 * @author Angela Maria Aguirre
 * @since 19/07/2010
 */
public class DTOHistoCambioResponsable implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private long codigoPk;
	private DetFaltanteSobrante detFaltanteSobrante;
	private Date fechaModifica;
	private String horaModifica;
	private String motivo;
	private DtoUsuarioPersona personaModifica;
	private DtoUsuarioPersona personaResponsable;
	private String loginUsuarioResponsable;
	private String loginUsuarioModifica;
	private String centroAtencionDescripcion;
	
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo codigoPk
	
	 * @return retorna la variable codigoPk 
	 * @author Angela Maria Aguirre 
	 */
	public long getCodigoPk() {
		return codigoPk;
	}
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo codigoPk
	
	 * @param valor para el atributo codigoPk 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoPk(long codigoPk) {
		this.codigoPk = codigoPk;
	}
		
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo fechaModifica
	
	 * @return retorna la variable fechaModifica 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaModifica() {
		return fechaModifica;
	}
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo fechaModifica
	
	 * @param valor para el atributo fechaModifica 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaModifica(Date fechaModifica) {
		this.fechaModifica = fechaModifica;
	}
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo horaModifica
	
	 * @return retorna la variable horaModifica 
	 * @author Angela Maria Aguirre 
	 */
	public String getHoraModifica() {
		return horaModifica;
	}
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo horaModifica
	
	 * @param valor para el atributo horaModifica 
	 * @author Angela Maria Aguirre 
	 */
	public void setHoraModifica(String horaModifica) {
		this.horaModifica = horaModifica;
	}
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo motivo
	
	 * @return retorna la variable motivo 
	 * @author Angela Maria Aguirre 
	 */
	public String getMotivo() {
		return motivo;
	}
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo motivo
	
	 * @param valor para el atributo motivo 
	 * @author Angela Maria Aguirre 
	 */
	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo detFaltanteSobrante
	
	 * @return retorna la variable detFaltanteSobrante 
	 * @author Angela Maria Aguirre 
	 */
	public DetFaltanteSobrante getDetFaltanteSobrante() {
		return detFaltanteSobrante;
	}
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo detFaltanteSobrante
	
	 * @param valor para el atributo detFaltanteSobrante 
	 * @author Angela Maria Aguirre 
	 */
	public void setDetFaltanteSobrante(DetFaltanteSobrante detFaltanteSobrante) {
		this.detFaltanteSobrante = detFaltanteSobrante;
	}
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo personaModifica
	
	 * @return retorna la variable personaModifica 
	 * @author Angela Maria Aguirre 
	 */
	public DtoUsuarioPersona getPersonaModifica() {
		return personaModifica;
	}
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo personaModifica
	
	 * @param valor para el atributo personaModifica 
	 * @author Angela Maria Aguirre 
	 */
	public void setPersonaModifica(DtoUsuarioPersona personaModifica) {
		this.personaModifica = personaModifica;
	}
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo personaResponsable
	
	 * @return retorna la variable personaResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public DtoUsuarioPersona getPersonaResponsable() {
		return personaResponsable;
	}
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo personaResponsable
	
	 * @param valor para el atributo personaResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public void setPersonaResponsable(DtoUsuarioPersona personaResponsable) {
		this.personaResponsable = personaResponsable;
	}
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo loginUsuarioResponsable
	
	 * @return retorna la variable loginUsuarioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public String getLoginUsuarioResponsable() {
		return loginUsuarioResponsable;
	}
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo loginUsuarioResponsable
	
	 * @param valor para el atributo loginUsuarioResponsable 
	 * @author Angela Maria Aguirre 
	 */
	public void setLoginUsuarioResponsable(String loginUsuarioResponsable) {
		this.loginUsuarioResponsable = loginUsuarioResponsable;
	}
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo loginUsuarioModifica
	
	 * @return retorna la variable loginUsuarioModifica 
	 * @author Angela Maria Aguirre 
	 */
	public String getLoginUsuarioModifica() {
		return loginUsuarioModifica;
	}
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo loginUsuarioModifica
	
	 * @param valor para el atributo loginUsuarioModifica 
	 * @author Angela Maria Aguirre 
	 */
	public void setLoginUsuarioModifica(String loginUsuarioModifica) {
		this.loginUsuarioModifica = loginUsuarioModifica;
	}
	/**
	 * Este m&eacute;todo se encarga de obtener el valor 
	 * del atributo centroAtencionDescripcion
	
	 * @return retorna la variable centroAtencionDescripcion 
	 * @author Angela Maria Aguirre 
	 */
	public String getCentroAtencionDescripcion() {
		return centroAtencionDescripcion;
	}
	/**
	 * Este m&eacute;todo se encarga de establecer el valor 
	 * del atributo centroAtencionDescripcion
	
	 * @param valor para el atributo centroAtencionDescripcion 
	 * @author Angela Maria Aguirre 
	 */
	public void setCentroAtencionDescripcion(String centroAtencionDescripcion) {
		this.centroAtencionDescripcion = centroAtencionDescripcion;
	}	
}
