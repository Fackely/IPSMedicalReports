package com.princetonsa.dto.cargos;

import java.io.Serializable;

import com.princetonsa.mundo.parametrizacion.CentroAtencion;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.Cuentas;

/**
 * Esta clase se encarga de contener los datos
 * para el cálculo de la tarifa de cargo modificado 
 * de un servicio o artículo determinado
 * 
 * @author Angela Maria Aguirre
 * @since 3/12/2010
 */
public class DTOCalcularTarifaCargoModificado implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int codigoArticuloServicio;
	private int institucionID;
	private boolean esServicio;
	private Convenios convenio;
	private Cuentas cuenta;
	private CentroAtencion centroAtencionSolicitante;
	private String fechaCalculoVigencia;
	
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoArticuloServicio
	
	 * @return retorna la variable codigoArticuloServicio 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoArticuloServicio() {
		return codigoArticuloServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoArticuloServicio
	
	 * @param valor para el atributo codigoArticuloServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoArticuloServicio(int codigoArticuloServicio) {
		this.codigoArticuloServicio = codigoArticuloServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo esServicio
	
	 * @return retorna la variable esServicio 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isEsServicio() {
		return esServicio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo esServicio
	
	 * @param valor para el atributo esServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setEsServicio(boolean esServicio) {
		this.esServicio = esServicio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo convenio
	
	 * @return retorna la variable convenio 
	 * @author Angela Maria Aguirre 
	 */
	public Convenios getConvenio() {
		return convenio;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo convenio
	
	 * @param valor para el atributo convenio 
	 * @author Angela Maria Aguirre 
	 */
	public void setConvenio(Convenios convenio) {
		this.convenio = convenio;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo cuenta
	
	 * @return retorna la variable cuenta 
	 * @author Angela Maria Aguirre 
	 */
	public Cuentas getCuenta() {
		return cuenta;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo cuenta
	
	 * @param valor para el atributo cuenta 
	 * @author Angela Maria Aguirre 
	 */
	public void setCuenta(Cuentas cuenta) {
		this.cuenta = cuenta;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo centroAtencionSolicitante
	
	 * @return retorna la variable centroAtencionSolicitante 
	 * @author Angela Maria Aguirre 
	 */
	public CentroAtencion getCentroAtencionSolicitante() {
		return centroAtencionSolicitante;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo centroAtencionSolicitante
	
	 * @param valor para el atributo centroAtencionSolicitante 
	 * @author Angela Maria Aguirre 
	 */
	public void setCentroAtencionSolicitante(
			CentroAtencion centroAtencionSolicitante) {
		this.centroAtencionSolicitante = centroAtencionSolicitante;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo institucionID
	
	 * @return retorna la variable institucionID 
	 * @author Angela Maria Aguirre 
	 */
	public int getInstitucionID() {
		return institucionID;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo institucionID
	
	 * @param valor para el atributo institucionID 
	 * @author Angela Maria Aguirre 
	 */
	public void setInstitucionID(int institucionID) {
		this.institucionID = institucionID;
	}
	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaCalculoVigencia
	
	 * @return retorna la variable fechaCalculoVigencia 
	 * @author Angela Maria Aguirre 
	 */
	public String getFechaCalculoVigencia() {
		return fechaCalculoVigencia;
	}
	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaCalculoVigencia
	
	 * @param valor para el atributo fechaCalculoVigencia 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaCalculoVigencia(String fechaCalculoVigencia) {
		this.fechaCalculoVigencia = fechaCalculoVigencia;
	}
	
}
