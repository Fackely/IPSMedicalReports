package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import java.io.Serializable;

import util.Utilidades;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoReporteIngresoOdonto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String razonSocial;
	private String fechaInicial;
	private String fechaFinal;
	private String sexo;
	private String edad;
	private String nombreProfesional;
	private String estadoPresupuesto;
	private String indicativoContrato;
	private String nombreEspecialidad;
	private String nombrePaquete;
	private String nombrePrograma;
	private String logoDerecha;
	private String logoIzquierda;
	private String usuarioProcesa;
	
	/** Objeto jasper para el subreporte del consolidado */
    transient private JRDataSource dsResultadoConsulta;
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo razonSocial
	 * 
	 * @return  Retorna la variable razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo razonSocial
	 * 
	 * @param  valor para el atributo razonSocial 
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaInicial
	 * 
	 * @return  Retorna la variable fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaInicial
	 * 
	 * @param  valor para el atributo fechaInicial 
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo fechaFinal
	 * 
	 * @return  Retorna la variable fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo fechaFinal
	 * 
	 * @param  valor para el atributo fechaFinal 
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo sexo
	 * 
	 * @return  Retorna la variable sexo
	 */
	public String getSexo() {
		return sexo;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo sexo
	 * 
	 * @param  valor para el atributo sexo 
	 */
	public void setSexo(String sexo) {
		this.sexo = sexo;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreProfesional
	 * 
	 * @return  Retorna la variable nombreProfesional
	 */
	public String getNombreProfesional() {
		return nombreProfesional;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreProfesional
	 * 
	 * @param  valor para el atributo nombreProfesional 
	 */
	public void setNombreProfesional(String nombreProfesional) {
		this.nombreProfesional = nombreProfesional;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo estadoPresupuesto
	 * 
	 * @return  Retorna la variable estadoPresupuesto
	 */
	public String getEstadoPresupuesto() {
		return estadoPresupuesto;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo estadoPresupuesto
	 * 
	 * @param  valor para el atributo estadoPresupuesto 
	 */
	public void setEstadoPresupuesto(String estadoPresupuesto) {
		this.estadoPresupuesto = estadoPresupuesto;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo indicativoContrato
	 * 
	 * @return  Retorna la variable indicativoContrato
	 */
	public String getIndicativoContrato() {
		return indicativoContrato;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo indicativoContrato
	 * 
	 * @param  valor para el atributo indicativoContrato 
	 */
	public void setIndicativoContrato(String indicativoContrato) {
		this.indicativoContrato = indicativoContrato;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombreEspecialidad
	 * 
	 * @return  Retorna la variable nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombreEspecialidad
	 * 
	 * @param  valor para el atributo nombreEspecialidad 
	 */
	public void setNombreEspecialidad(String nombreEspecialidad) {
		this.nombreEspecialidad = nombreEspecialidad;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombrePaquete
	 * 
	 * @return  Retorna la variable nombrePaquete
	 */
	public String getNombrePaquete() {
		return nombrePaquete;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombrePaquete
	 * 
	 * @param  valor para el atributo nombrePaquete 
	 */
	public void setNombrePaquete(String nombrePaquete) {
		this.nombrePaquete = nombrePaquete;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo nombrePrograma
	 * 
	 * @return  Retorna la variable nombrePrograma
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo nombrePrograma
	 * 
	 * @param  valor para el atributo nombrePrograma 
	 */
	public void setNombrePrograma(String nombrePrograma) {
		this.nombrePrograma = nombrePrograma;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo edad
	 * 
	 * @return  Retorna la variable edad
	 */
	public String getEdad() {
		return edad;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo edad
	 * 
	 * @param  valor para el atributo edad 
	 */
	public void setEdad(String edad) {
		this.edad = edad;
	}
	
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo logoDerecha
	 * 
	 * @return  Retorna la variable logoDerecha
	 */
	public String getLogoDerecha() {
		return logoDerecha;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo logoDerecha
	 * 
	 * @param  valor para el atributo logoDerecha 
	 */
	public void setLogoDerecha(String logoDerecha) {
		this.logoDerecha = logoDerecha;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo logoIzquierda
	 * 
	 * @return  Retorna la variable logoIzquierda
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo logoIzquierda
	 * 
	 * @param  valor para el atributo logoIzquierda 
	 */
	public void setLogoIzquierda(String logoIzquierda) {
		this.logoIzquierda = logoIzquierda;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dsResultadoConsulta
	 * 
	 * @return  Retorna la variable dsResultadoConsulta
	 */
	public JRDataSource getDsResultadoConsulta() {
		return dsResultadoConsulta;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo dsResultadoConsulta
	 * 
	 * @param  valor para el atributo dsResultadoConsulta 
	 */
	public void setDsResultadoConsulta(JRDataSource dsResultadoConsulta) {
		this.dsResultadoConsulta = dsResultadoConsulta;
	}
	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo usuarioProcesa
	 * 
	 * @return  Retorna la variable usuarioProcesa
	 */
	public String getUsuarioProcesa() {
		
		return usuarioProcesa;
	}
	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo usuarioProcesa
	 * 
	 * @param  valor para el atributo usuarioProcesa 
	 */
	public void setUsuarioProcesa(String usuarioProcesa) {
		this.usuarioProcesa = usuarioProcesa;
	}
}
