package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoReporteTiempoEsperaAtencionCitasOdontoGen implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo donde se almacena la razón social de la institución
	 */
	private String razonSocial;
	
	/**
	 * Atributo donde se almacena la fecha inicial de la generación del reporte
	 */
	private String fechaInicial;
	
	/**
	 * Atributo donde se almacena la fecha final de la generación del reporte
	 */
	private String fechaFinal;
	
	/**
	 * Atributo donde se almacena la ruta para generar el logo a la izquiera
	 */
	private String logoIzquierda;
	
	/**
	 * Atributo donde se almacena la ruta para generar el logo a la derecha
	 */
	private String logoDerecha;
	
	/**
	 * Atributo donde se almacena el usuario de la session
	 */
	private String usuarioProcesa;
	
	/**
	 * Atributo donde se almacena los servicios seleccionados por el usuario
	 */
	private String servicios;
	
	/** Objeto jasper para el subreporte del consolidado */
    private JRDataSource dsResultadoTiempoEspera;
	
    
    /**
 	 * Método que se encarga de obtener el valor 
 	 *  del atributo razonSocial
 	 * 
 	 * @return  Retorna la variable razonSocial
 	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo razonSocial
	 * 
	 * @param  valor para el atributo razonSocial
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	
	/**
 	 * Método que se encarga de obtener el valor 
 	 *  del atributo fechaInicial
 	 * 
 	 * @return  Retorna la variable fechaInicial
 	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo fechaInicial
	 * 
	 * @param  valor para el atributo fechaInicial
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	
	/**
 	 * Método que se encarga de obtener el valor 
 	 *  del atributo fechaFinal
 	 * 
 	 * @return  Retorna la variable fechaFinal
 	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo fechaFinal
	 * 
	 * @param  valor para el atributo fechaFinal
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	
	/**
 	 * Método que se encarga de obtener el valor 
 	 *  del atributo logoIzquierda
 	 * 
 	 * @return  Retorna la variable logoIzquierda
 	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo logoIzquierda
	 * 
	 * @param  valor para el atributo logoIzquierda
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}
	
	/**
 	 * Método que se encarga de obtener el valor 
 	 *  del atributo logoDerecha
 	 * 
 	 * @return  Retorna la variable logoDerecha
 	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo logoDerecha
	 * 
	 * @param  valor para el atributo logoDerecha
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}
	
	/**
 	 * Método que se encarga de obtener el valor 
 	 *  del atributo usuarioProcesa
 	 * 
 	 * @return  Retorna la variable usuarioProcesa
 	 */
	public String getUsuarioProcesa() {
		return usuarioProcesa;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo usuarioProcesa
	 * 
	 * @param  valor para el atributo usuarioProcesa
	 */
	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo dsResultadoTiempoEspera
	 * 
	 * @param  valor para el atributo dsResultadoTiempoEspera
	 */
	public void setDsResultadoTiempoEspera(JRDataSource dsResultadoTiempoEspera) {
		this.dsResultadoTiempoEspera = dsResultadoTiempoEspera;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo dsResultadoTiempoEspera
	 * 
	 * @param  valor para el atributo dsResultadoTiempoEspera
	 */
	public JRDataSource getDsResultadoTiempoEspera() {
		return dsResultadoTiempoEspera;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo servicios
	 * 
	 * @param  valor para el atributo servicios
	 */
	public void setServicios(String servicios) {
		this.servicios = servicios;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 *  del atributo servicios
	 * 
	 * @param  valor para el atributo servicios
	 */
	public String getServicios() {
		return servicios;
	}

	
}
