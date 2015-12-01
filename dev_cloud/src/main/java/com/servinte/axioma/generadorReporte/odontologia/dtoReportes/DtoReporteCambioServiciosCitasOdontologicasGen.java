package com.servinte.axioma.generadorReporte.odontologia.dtoReportes;

import java.io.Serializable;

import net.sf.jasperreports.engine.JRDataSource;

public class DtoReporteCambioServiciosCitasOdontologicasGen implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String razonSocial;
	private String fechaInicial;
	private String fechaFinal;
	private String sexo;
	private String rangoEdadConsultada;
	private String nombreEspecialidad;
	private String nombrePrograma;
	private String logoIzquierda;
	private String logoDerecha;
	private String usuarioProcesa;
	private String Paciente;
	
		
	/** Objeto jasper para el subreporte del consolidado */
    private JRDataSource dsResultadoCambioServ;

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
	 *  del atributo razonSocial
	 * 
	 * @return  Retorna la variable razonSocial
	 */
	public String getRazonSocial() {
		return razonSocial;
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
	 *  del atributo fechaInicial
	 * 
	 * @return  Retorna la variable fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
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
	 *  del atributo fechaFinal
	 * 
	 * @return  Retorna la variable fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
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
	 *  del atributo sexo
	 * 
	 * @return  Retorna la variable sexo
	 */
	public String getSexo() {
		return sexo;
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
	 *  del atributo nombreEspecialidad
	 * 
	 * @return  Retorna la variable nombreEspecialidad
	 */
	public String getNombreEspecialidad() {
		return nombreEspecialidad;
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
	 *  del atributo nombrePrograma
	 * 
	 * @return  Retorna la variable nombrePrograma
	 */
	public String getNombrePrograma() {
		return nombrePrograma;
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
	 *  del atributo logoIzquierda
	 * 
	 * @return  Retorna la variable logoIzquierda
	 */
	public String getLogoIzquierda() {
		return logoIzquierda;
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
	 *  del atributo logoDerecha
	 * 
	 * @return  Retorna la variable logoDerecha
	 */
	public String getLogoDerecha() {
		return logoDerecha;
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
	 * del atributo dsResultadoCambioServ
	 * 
	 * @param  valor para el atributo dsResultadoCambioServ 
	 */
	public void setDsResultadoCambioServ(JRDataSource dsResultadoCambioServ) {
		this.dsResultadoCambioServ = dsResultadoCambioServ;
	}


	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo dsResultadoCambioServ
	 * 
	 * @return  Retorna la variable dsResultadoCambioServ
	 */
	public JRDataSource getDsResultadoCambioServ() {
		return dsResultadoCambioServ;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo rangoEdadConsultada
	 * 
	 * @param  valor para el atributo rangoEdadConsultada 
	 */
	public void setRangoEdadConsultada(String rangoEdadConsultada) {
		this.rangoEdadConsultada = rangoEdadConsultada;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo rangoEdadConsultada
	 * 
	 * @return  Retorna la variable rangoEdadConsultada
	 */
	public String getRangoEdadConsultada() {
		return rangoEdadConsultada;
	}

	/**
	 * M&eacute;todo que se encarga de establecer el valor 
	 * del atributo paciente
	 * 
	 * @param  valor para el atributo paciente 
	 */
	public void setPaciente(String paciente) {
		Paciente = paciente;
	}

	/**
	 * M&eacute;todo que se encarga de obtener el valor 
	 *  del atributo paciente
	 * 
	 * @return  Retorna la variable paciente
	 */
	public String getPaciente() {
		return Paciente;
	}
	
	
}
