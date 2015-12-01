
package com.princetonsa.dto.odontologia;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.TiposAfiliado;

import util.ConstantesBD;

/**
 * 
 * Atributos necesarios para la realización de la búsqueda por 
 * Rango en la Agenda Odontológica
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class DtoBusquedaAgendaRango {

	
	/**
	 * Código del Pais seleccionado
	 */
	private String pais;
	
	/**
	 * Código de la ciudad seleccionada
	 */
	private String ciudad;
	
	/**
	 * Código del centro de atención seleccionado
	 */
	private int centroAtencion;
	
	/**
	 * Código de la Unidad de Agenda seleccionada
	 */
	private String unidadAgenda;
	
	/**
	 * Código del profesional de la salud seleccionado
	 */
	private String profesionalSalud;
	
	/**
	 * Tipo de la cita seleccionada
	 */
	private String tipoCita;
	
	/**
	 * Estado de la cita seleccionado
	 */
	private String estadoCita;
	
	/**
	 * Indicativo de confirmación de la cita.
	 */
	private String[] indicativoConf;
	
	/**
	 * Fecha Inicial de Búsqueda
	 */
	private String fechaInicial;
	
	/**
	 * Fecha Final de Búsqueda
	 */
	private String fechaFinal;
	
	/**
	 * Almacena el tipo de cancelación realizado sobre la cita 
	 * odontológica. Por paciente, por institución o por ambos.
	 */
	private String tipoCancelacion;
	
	/**
	 * 
	 */
	public DtoBusquedaAgendaRango() {
		
		pais = "";
		ciudad = "";
		centroAtencion = ConstantesBD.codigoNuncaValido;
		unidadAgenda = "";
		profesionalSalud = "";
		tipoCita = "";
		estadoCita = "";
		indicativoConf=new String[0];
		tipoCancelacion = "";
		
	}


	/**
	 * @return the pais
	 */
	public String getPais() {
		return pais;
	}


	/**
	 * @param pais the pais to set
	 */
	public void setPais(String pais) {
		this.pais = pais;
	}


	/**
	 * @return the ciudad
	 */
	public String getCiudad() {
		return ciudad;
	}


	/**
	 * @param ciudad the ciudad to set
	 */
	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}


	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}


	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}


	/**
	 * @return the unidadAgenda
	 */
	public String getUnidadAgenda() {
		return unidadAgenda;
	}


	/**
	 * @param unidadAgenda the unidadAgenda to set
	 */
	public void setUnidadAgenda(String unidadAgenda) {
		this.unidadAgenda = unidadAgenda;
	}


	/**
	 * @return the profesionalSalud
	 */
	public String getProfesionalSalud() {
		return profesionalSalud;
	}


	/**
	 * @param profesionalSalud the profesionalSalud to set
	 */
	public void setProfesionalSalud(String profesionalSalud) {
		this.profesionalSalud = profesionalSalud;
	}


	/**
	 * @return the tipoCita
	 */
	public String getTipoCita() {
		return tipoCita;
	}


	/**
	 * @param tipoCita the tipoCita to set
	 */
	public void setTipoCita(String tipoCita) {
		this.tipoCita = tipoCita;
	}


	/**
	 * @return the estadoCita
	 */
	public String getEstadoCita() {
		return estadoCita;
	}


	/**
	 * @param estadoCita the estadoCita to set
	 */
	public void setEstadoCita(String estadoCita) {
		this.estadoCita = estadoCita;
	}
	
	/**
	 * @return the indicativoConf
	 */
	public String[] getIndicativoConf() {
		return indicativoConf;
	}

	/**
	 * @param indicativoConf the indicativoConf to set
	 */
	public void setIndicativoConf(String[] indicativoConf) {
		this.indicativoConf = indicativoConf;
	}
	
	/**
	 * @return the indicativoConf
	 */
	public boolean getIndicativoConfirmacion(String valor) {
		if(indicativoConf==null)
		{
			return false;
		}
		boolean respuesta=false;
		for(int i=0; i<indicativoConf.length; i++)
		{
			if(indicativoConf[i].equals(valor))
			{
				respuesta=true;
			}
		}
		return respuesta;
	}
	
	/**
	 * Limpia el indicativo de confirmación para que se dejen deschekear los elementos
	 * @return
	 */
	public boolean getLimpiarIndicativoConfirmacion()
	{
		indicativoConf=new String[0];
		return true;
	}


	/**
	 * @param fechaInicial the fechaInicial to set
	 */
	public void setFechaInicial(String fechaInicial) {
		this.fechaInicial = fechaInicial;
	}


	/**
	 * @return the fechaInicial
	 */
	public String getFechaInicial() {
		return fechaInicial;
	}


	/**
	 * @param fechaFinal the fechaFinal to set
	 */
	public void setFechaFinal(String fechaFinal) {
		this.fechaFinal = fechaFinal;
	}


	/**
	 * @return the fechaFinal
	 */
	public String getFechaFinal() {
		return fechaFinal;
	}


	/**
	 * Método que se encarga de obtener el 
	 * valor del atributo  tipoCancelacion
	 *
	 * @return retorna la variable tipoCancelacion
	 */
	public String getTipoCancelacion() {
		return tipoCancelacion;
	}


	/**
	 * Método que se encarga de establecer el valor
	 * del atributo tipoCancelacion
	 * @param tipoCancelacion es el valor para el atributo tipoCancelacion 
	 */
	public void setTipoCancelacion(String tipoCancelacion) {
		this.tipoCancelacion = tipoCancelacion;
	}
}
