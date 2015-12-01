package com.princetonsa.dto.facturacion;

import java.io.Serializable;
import java.util.Date;

/**
 * Clase utilizada para enviar los datos de la consulta del proceso rips 
 * de entidades subcontratadas
 * @author Fabián Becerra
 */
public class DtoFiltroConsultaProcesoRipsEntidadesSub implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public DtoFiltroConsultaProcesoRipsEntidadesSub() {
		this.setLoginUsuario("");
	}
	
	/**
	 * Atributo que almacena la fecha a partir de la cual se buscará información
	 */
	private Date fechaInicial;
	
	/**
	 * Atributo que almacena la fecha hasta la cual se buscará información
	 */
	private Date fechaFinal;

	/**
	 * Atributo que almacena el login de
	 * usuario seleccionado
	 */
	private String loginUsuario;
	
	/**
	 * Atributo que almacena el codigo pk de la entidad subcontratada seleccionada
	 */
	private long codigoPkEntidadSub;
	
	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo fechaInicial
	 * 
	 * @param  valor para el atributo fechaInicial 
	 */
	
	public Date getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaInicial
	 * 
	 * @return  Retorna la variable fechaInicial
	 */
	
	public void setFechaInicial(Date fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo fechaFinal
	 * 
	 * @param  valor para el atributo fechaFinal 
	 */
	public void setFechaFinal(Date fechaFinal) {
		this.fechaFinal = fechaFinal;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo fechaFinal
	 * 
	 * @return  Retorna la variable fechaFinal
	 */
	public Date getFechaFinal() {
		return fechaFinal;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo loginUsuario
	 * 
	 * @param  valor para el atributo loginUsuario 
	 */
	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo loginUsuario
	 * 
	 * @return  Retorna la variable loginUsuario
	 */
	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setCodigoPkEntidadSub(long codigoPkEntidadSub) {
		this.codigoPkEntidadSub = codigoPkEntidadSub;
	}

	public long getCodigoPkEntidadSub() {
		return codigoPkEntidadSub;
	}

	


}
