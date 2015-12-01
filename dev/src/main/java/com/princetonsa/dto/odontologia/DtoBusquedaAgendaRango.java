
package com.princetonsa.dto.odontologia;

import org.axioma.util.log.Log4JManager;

import com.servinte.axioma.orm.TiposAfiliado;

import util.ConstantesBD;

/**
 * 
 * Atributos necesarios para la realizaci�n de la b�squeda por 
 * Rango en la Agenda Odontol�gica
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class DtoBusquedaAgendaRango {

	
	/**
	 * C�digo del Pais seleccionado
	 */
	private String pais;
	
	/**
	 * C�digo de la ciudad seleccionada
	 */
	private String ciudad;
	
	/**
	 * C�digo del centro de atenci�n seleccionado
	 */
	private int centroAtencion;
	
	/**
	 * C�digo de la Unidad de Agenda seleccionada
	 */
	private String unidadAgenda;
	
	/**
	 * C�digo del profesional de la salud seleccionado
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
	 * Indicativo de confirmaci�n de la cita.
	 */
	private String[] indicativoConf;
	
	/**
	 * Fecha Inicial de B�squeda
	 */
	private String fechaInicial;
	
	/**
	 * Fecha Final de B�squeda
	 */
	private String fechaFinal;
	
	/**
	 * Almacena el tipo de cancelaci�n realizado sobre la cita 
	 * odontol�gica. Por paciente, por instituci�n o por ambos.
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
	 * Limpia el indicativo de confirmaci�n para que se dejen deschekear los elementos
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
	 * M�todo que se encarga de obtener el 
	 * valor del atributo  tipoCancelacion
	 *
	 * @return retorna la variable tipoCancelacion
	 */
	public String getTipoCancelacion() {
		return tipoCancelacion;
	}


	/**
	 * M�todo que se encarga de establecer el valor
	 * del atributo tipoCancelacion
	 * @param tipoCancelacion es el valor para el atributo tipoCancelacion 
	 */
	public void setTipoCancelacion(String tipoCancelacion) {
		this.tipoCancelacion = tipoCancelacion;
	}
}
