package com.princetonsa.dto.manejoPaciente;

import java.io.Serializable;
import java.util.Date;

import util.ConstantesBD;

/**
 * Esta clase se encarga de contener los datos
 * de búsqueda por rango para las autorizaciones de capitacion
 * e ingreso estancia
 * 
 * @author Angela Maria Aguirre
 * @since 6/01/2011
 */
public class DTOBusquedaAutorizacionCapitacionRango implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date fechaIncioBusqueda;
	private Date fechaFinBusqueda;
	private Long consecutivoIncioAutorizacion; 
	private Long consecutivoFinAutorizacion;
	private Long codigoEntidadSub;
	private String estadoAutorizacion;
	private boolean esConsulta;
	private String codigoTipoAutorizacion;
	private boolean administracioCapitacion;
	private String tipoConsecutivoAutorizacion=""+ConstantesBD.codigoNuncaValido;
	
	private boolean consultableConsecutivoAutorizacion;
	
	private int codigoInstitucion;
	
		
	public DTOBusquedaAutorizacionCapitacionRango() {
		this.fechaIncioBusqueda = null;
		this.fechaFinBusqueda = null;
		this.consecutivoIncioAutorizacion = null;
		this.consecutivoFinAutorizacion = null;
		this.codigoEntidadSub = ConstantesBD.codigoNuncaValidoLong;
		this.estadoAutorizacion = "";
		this.codigoTipoAutorizacion = "";
		this.administracioCapitacion = false;
		this.esConsulta = false;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaIncioBusqueda
	
	 * @return retorna la variable fechaIncioBusqueda 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaIncioBusqueda() {
		return fechaIncioBusqueda;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaIncioBusqueda
	
	 * @param valor para el atributo fechaIncioBusqueda 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaIncioBusqueda(Date fechaIncioBusqueda) {
		this.fechaIncioBusqueda = fechaIncioBusqueda;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo fechaFinBusqueda
	
	 * @return retorna la variable fechaFinBusqueda 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaFinBusqueda() {
		return fechaFinBusqueda;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo fechaFinBusqueda
	
	 * @param valor para el atributo fechaFinBusqueda 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaFinBusqueda(Date fechaFinBusqueda) {
		this.fechaFinBusqueda = fechaFinBusqueda;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo consecutivoIncioAutorizacion
	
	 * @return retorna la variable consecutivoIncioAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public Long getConsecutivoIncioAutorizacion() {
		return consecutivoIncioAutorizacion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo consecutivoIncioAutorizacion
	
	 * @param valor para el atributo consecutivoIncioAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoIncioAutorizacion(Long consecutivoIncioAutorizacion) {
		this.consecutivoIncioAutorizacion = consecutivoIncioAutorizacion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo consecutivoFinAutorizacion
	
	 * @return retorna la variable consecutivoFinAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public Long getConsecutivoFinAutorizacion() {
		return consecutivoFinAutorizacion;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo consecutivoFinAutorizacion
	
	 * @param valor para el atributo consecutivoFinAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setConsecutivoFinAutorizacion(Long consecutivoFinAutorizacion) {
		this.consecutivoFinAutorizacion = consecutivoFinAutorizacion;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoEntidadSub
	
	 * @return retorna la variable codigoEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public Long getCodigoEntidadSub() {
		return codigoEntidadSub;
	}

	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoEntidadSub
	
	 * @param valor para el atributo codigoEntidadSub 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoEntidadSub(Long codigoEntidadSub) {
		this.codigoEntidadSub = codigoEntidadSub;
	}
	
	/**
	 *Metodo para obtener el estado de la autorizacion 
	 * @return estado estadoAutorizacion
	 * @author Camilo Gomez
	 */
	public String getEstadoAutorizacion() {
		return estadoAutorizacion;
	}

	/**
	 * Estable el estado de la autorizacion
	 * @param estadoAutorizacion
	 * @author Camilo Gomez
	 */
	public void setEstadoAutorizacion(String estadoAutorizacion) {
		this.estadoAutorizacion = estadoAutorizacion;
	}

	/**
	 * Estable si es consulta por funcionalidad 'Consultar Autorizaciones por Rango'
	 * @param estadoAutorizacion
	 * @author Camilo Gomez
	 */
	public void setEsConsulta(boolean esConsulta) {
		this.esConsulta = esConsulta;
	}

	/**
	 * Metodo para cononcer si es consulta por funcionalidad 'Consultar Autorizaciones por Rango'
	 * @return estado estadoAutorizacion
	 * @author Camilo Gomez
	 */
	public boolean isEsConsulta() {
		return esConsulta;
	}
	
	/**
	 * @return the codigoTipoAutorizacion
	 */
	public String getCodigoTipoAutorizacion() {
		return codigoTipoAutorizacion;
	}

	/**
	 * @param codigoTipoAutorizacion the codigoTipoAutorizacion to set
	 */
	public void setCodigoTipoAutorizacion(String codigoTipoAutorizacion) {
		this.codigoTipoAutorizacion = codigoTipoAutorizacion;
	}

	/**
	 * @return the administracioCapitacion
	 */
	public boolean isAdministracioCapitacion() {
		return administracioCapitacion;
	}

	/**
	 * @param administracioCapitacion the administracioCapitacion to set
	 */
	public void setAdministracioCapitacion(boolean administracioCapitacion) {
		this.administracioCapitacion = administracioCapitacion;
	}

	/**
	 * @return the tipoConsecutivoAutorizacion
	 */
	public String getTipoConsecutivoAutorizacion() {
		return tipoConsecutivoAutorizacion;
	}

	/**
	 * @param tipoConsecutivoAutorizacion the tipoConsecutivoAutorizacion to set
	 */
	public void setTipoConsecutivoAutorizacion(String tipoConsecutivoAutorizacion) {
		this.tipoConsecutivoAutorizacion = tipoConsecutivoAutorizacion;
	}

	/**
	 * @return the consultableConsecutivoAutorizacion
	 */
	public boolean isConsultableConsecutivoAutorizacion() {
		this.consultableConsecutivoAutorizacion=!this.tipoConsecutivoAutorizacion.equals(""+ConstantesBD.codigoNuncaValido);
		return consultableConsecutivoAutorizacion;
	}

	/**
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	
}
