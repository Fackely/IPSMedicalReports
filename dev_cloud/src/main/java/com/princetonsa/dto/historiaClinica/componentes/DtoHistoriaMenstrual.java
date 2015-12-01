/*
 * Junio 09, 2008
 */
package com.princetonsa.dto.historiaClinica.componentes;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import com.princetonsa.mundo.historiaClinica.Valoraciones;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadFecha;
import util.Utilidades;

/**
 * Data Transfer Object: Usado para el manejo de la seccion
 * Historia Menstrual de la valoración
 * @author Sebastián Gómez R.
 *
 *Nota * Se contemplan las tablas
 *ant_gineco_obst
 *ant_gineco_histo
 */
public class DtoHistoriaMenstrual implements Serializable
{
	/**
	 * Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(DtoHistoriaMenstrual.class);
	
	/**
	 * Código del paciente
	 */
	private String codigoPaciente;
	
	/**
	 * Informacion de la edad menarquia
	 */
	private InfoDatosInt edadMenarquia;
	private String otraEdadMenarquia;
	private boolean existeEdadMenarquia;
	
	/**
	 * Información de la edad menpausia
	 */
	private InfoDatosInt edadMenopausia;
	private String otraEdadMenopausia;
	private boolean existeEdadMenopausia;
	
	private String cicloMenstrual;
	private String cicloMenstrualAnterior;
	private String duracionMenstruacion;
	private String duracionMenstruacionAnterior;
	private String fechaUltimaRegla;
	private String fechaUltimaReglaAnterior;
	private Boolean dolorMenstruacion;
	private Boolean dolorMenstruacionAnterior;
	private InfoDatosInt conceptoMenstruacion;
	private InfoDatosInt conceptoMenstruacionAnterior;
	private String observacionesMenstruales;
	private String observacionesMenstrualesAnteriores;
	
	
	/**
	 * Reset de la informacion
	 *
	 */
	public void clean()
	{
		this.codigoPaciente = "";
		
		this.edadMenarquia = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.otraEdadMenarquia = "";
		this.existeEdadMenarquia = false;
		
		this.edadMenopausia = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.otraEdadMenopausia = "";
		this.existeEdadMenopausia = false;
		
		
		this.cicloMenstrual = "";
		this.cicloMenstrualAnterior = "";
		this.duracionMenstruacion = "";
		this.duracionMenstruacionAnterior = "";
		this.fechaUltimaRegla = "";
		this.fechaUltimaReglaAnterior = "";
		this.dolorMenstruacion = null;
		this.dolorMenstruacionAnterior = null;
		this.conceptoMenstruacion = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.conceptoMenstruacionAnterior = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.observacionesMenstruales = "";
		this.observacionesMenstrualesAnteriores = "";
	}
	
	/**
	 * Constructor
	 *
	 */
	public DtoHistoriaMenstrual()
	{
		this.clean();
	}

	/**
	 * @return the cicloMenstrual
	 */
	public String getCicloMenstrual() {
		return cicloMenstrual;
	}

	/**
	 * @param cicloMenstrual the cicloMenstrual to set
	 */
	public void setCicloMenstrual(String cicloMenstrual) {
		this.cicloMenstrual = cicloMenstrual;
	}

	/**
	 * @return the cicloMenstrualAnterior
	 */
	public String getCicloMenstrualAnterior() {
		return cicloMenstrualAnterior;
	}

	/**
	 * @param cicloMenstrualAnterior the cicloMenstrualAnterior to set
	 */
	public void setCicloMenstrualAnterior(String cicloMenstrualAnterior) {
		this.cicloMenstrualAnterior = cicloMenstrualAnterior;
	}

	/**
	 * @return the codigoPaciente
	 */
	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	/**
	 * @param codigoPaciente the codigoPaciente to set
	 */
	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	/**
	 * @return the conceptoMenstruacion
	 */
	public int getCodigoConceptoMenstruacion() {
		return conceptoMenstruacion.getCodigo();
	}

	/**
	 * @param conceptoMenstruacion the conceptoMenstruacion to set
	 */
	public void setCodigoConceptoMenstruacion(int conceptoMenstruacion) {
		this.conceptoMenstruacion.setCodigo(conceptoMenstruacion);
	}
	
	/**
	 * @return the conceptoMenstruacion
	 */
	public String getNombreConceptoMenstruacion() {
		return conceptoMenstruacion.getNombre();
	}

	/**
	 * @param conceptoMenstruacion the conceptoMenstruacion to set
	 */
	public void setNombreConceptoMenstruacion(String conceptoMenstruacion) {
		this.conceptoMenstruacion.setNombre(conceptoMenstruacion);
	}
	
	

	/**
	 * @return the conceptoMenstruacionAnterior
	 */
	public int getCodigoConceptoMenstruacionAnterior() {
		return conceptoMenstruacionAnterior.getCodigo();
	}

	/**
	 * @param conceptoMenstruacionAnterior the conceptoMenstruacionAnterior to set
	 */
	public void setCodigoConceptoMenstruacionAnterior(
			int conceptoMenstruacionAnterior) {
		this.conceptoMenstruacionAnterior.setCodigo(conceptoMenstruacionAnterior);
	}
	
	/**
	 * @return the conceptoMenstruacionAnterior
	 */
	public String getNombreConceptoMenstruacionAnterior() {
		return conceptoMenstruacionAnterior.getNombre();
	}

	/**
	 * @param conceptoMenstruacionAnterior the conceptoMenstruacionAnterior to set
	 */
	public void setNombreConceptoMenstruacionAnterior(
			String conceptoMenstruacionAnterior) {
		this.conceptoMenstruacionAnterior.setNombre(conceptoMenstruacionAnterior);
	}

	/**
	 * @return the dolorMenstruacion
	 */
	public Boolean getDolorMenstruacion() {
		return dolorMenstruacion;
	}

	/**
	 * @param dolorMenstruacion the dolorMenstruacion to set
	 */
	public void setDolorMenstruacion(Boolean dolorMenstruacion) {
		this.dolorMenstruacion = dolorMenstruacion;
	}

	/**
	 * @return the dolorMenstruacionAnterior
	 */
	public Boolean getDolorMenstruacionAnterior() {
		return dolorMenstruacionAnterior;
	}

	/**
	 * @param dolorMenstruacionAnterior the dolorMenstruacionAnterior to set
	 */
	public void setDolorMenstruacionAnterior(Boolean dolorMenstruacionAnterior) {
		this.dolorMenstruacionAnterior = dolorMenstruacionAnterior;
	}

	/**
	 * @return the duracionMenstruacion
	 */
	public String getDuracionMenstruacion() {
		return duracionMenstruacion;
	}

	/**
	 * @param duracionMenstruacion the duracionMenstruacion to set
	 */
	public void setDuracionMenstruacion(String duracionMenstruacion) {
		this.duracionMenstruacion = duracionMenstruacion;
	}

	/**
	 * @return the duracionMenstruacionAnterior
	 */
	public String getDuracionMenstruacionAnterior() {
		return duracionMenstruacionAnterior;
	}

	/**
	 * @param duracionMenstruacionAnterior the duracionMenstruacionAnterior to set
	 */
	public void setDuracionMenstruacionAnterior(String duracionMenstruacionAnterior) {
		this.duracionMenstruacionAnterior = duracionMenstruacionAnterior;
	}

	/**
	 * @return the edadMenarquia
	 */
	public int getCodigoEdadMenarquia() {
		return edadMenarquia.getCodigo();
	}

	/**
	 * @param edadMenarquia the edadMenarquia to set
	 */
	public void setCodigoEdadMenarquia(int edadMenarquia) {
		this.edadMenarquia.setCodigo(edadMenarquia);
	}
	
	/**
	 * @return the edadMenarquia
	 */
	public String getNombreEdadMenarquia() {
		return edadMenarquia.getNombre();
	}

	/**
	 * @param edadMenarquia the edadMenarquia to set
	 */
	public void setNombreEdadMenarquia(String edadMenarquia) {
		this.edadMenarquia.setNombre(edadMenarquia);
	}
	

	/**
	 * @return the edadMenopausia
	 */
	public int getCodigoEdadMenopausia() {
		return edadMenopausia.getCodigo();
	}

	/**
	 * @param edadMenopausia the edadMenopausia to set
	 */
	public void setCodigoEdadMenopausia(int edadMenopausia) {
		this.edadMenopausia.setCodigo(edadMenopausia);
	}
	
	/**
	 * @return the edadMenopausia
	 */
	public String getNombreEdadMenopausia() {
		return edadMenopausia.getNombre();
	}

	/**
	 * @param edadMenopausia the edadMenopausia to set
	 */
	public void setNombreEdadMenopausia(String edadMenopausia) {
		this.edadMenopausia.setNombre(edadMenopausia);
	}

	/**
	 * @return the existeEdadMenarquia
	 */
	public boolean isExisteEdadMenarquia() {
		return existeEdadMenarquia;
	}

	/**
	 * @param existeEdadMenarquia the existeEdadMenarquia to set
	 */
	public void setExisteEdadMenarquia(boolean existeEdadMenarquia) {
		this.existeEdadMenarquia = existeEdadMenarquia;
	}

	/**
	 * @return the existeEdadMenopausia
	 */
	public boolean isExisteEdadMenopausia() {
		return existeEdadMenopausia;
	}

	/**
	 * @param existeEdadMenopausia the existeEdadMenopausia to set
	 */
	public void setExisteEdadMenopausia(boolean existeEdadMenopausia) {
		this.existeEdadMenopausia = existeEdadMenopausia;
	}

	/**
	 * @return the fechaUltimaRegla
	 */
	public String getFechaUltimaRegla() {
		return fechaUltimaRegla;
	}

	/**
	 * @param fechaUltimaRegla the fechaUltimaRegla to set
	 */
	public void setFechaUltimaRegla(String fechaUltimaRegla) {
		this.fechaUltimaRegla = fechaUltimaRegla;
	}

	/**
	 * @return the fechaUltimaReglaAnterior
	 */
	public String getFechaUltimaReglaAnterior() {
		return fechaUltimaReglaAnterior;
	}

	/**
	 * @param fechaUltimaReglaAnterior the fechaUltimaReglaAnterior to set
	 */
	public void setFechaUltimaReglaAnterior(String fechaUltimaReglaAnterior) {
		this.fechaUltimaReglaAnterior = fechaUltimaReglaAnterior;
	}

	/**
	 * @return the observacionesMenstruales
	 */
	public String getObservacionesMenstruales() {
		return observacionesMenstruales;
	}

	/**
	 * @param observacionesMenstruales the observacionesMenstruales to set
	 */
	public void setObservacionesMenstruales(String observacionesMenstruales) {
		this.observacionesMenstruales = observacionesMenstruales;
	}

	/**
	 * @return the observacionesMenstrualesAnteriores
	 */
	public String getObservacionesMenstrualesAnteriores() {
		return observacionesMenstrualesAnteriores;
	}

	/**
	 * @param observacionesMenstrualesAnteriores the observacionesMenstrualesAnteriores to set
	 */
	public void setObservacionesMenstrualesAnteriores(
			String observacionesMenstrualesAnteriores) {
		this.observacionesMenstrualesAnteriores = observacionesMenstrualesAnteriores;
	}

	/**
	 * @return the otraEdadMenarquia
	 */
	public String getOtraEdadMenarquia() {
		return otraEdadMenarquia;
	}

	/**
	 * @param otraEdadMenarquia the otraEdadMenarquia to set
	 */
	public void setOtraEdadMenarquia(String otraEdadMenarquia) {
		this.otraEdadMenarquia = otraEdadMenarquia;
	}

	/**
	 * @return the otraEdadMenopausia
	 */
	public String getOtraEdadMenopausia() {
		return otraEdadMenopausia;
	}

	/**
	 * @param otraEdadMenopausia the otraEdadMenopausia to set
	 */
	public void setOtraEdadMenopausia(String otraEdadMenopausia) {
		this.otraEdadMenopausia = otraEdadMenopausia;
	}
	
	/**
	 * Se realizan validaciones de campos del historico menstrual
	 * @param errores
	 * @return
	 */
	public ActionErrors validate(ActionErrors errores)
	{
		if(!this.cicloMenstrual.equals("")&&Utilidades.convertirAEntero(this.cicloMenstrual)==ConstantesBD.codigoNuncaValido)
			errores.add("", new ActionMessage("errors.integer","El campo ciclo menstrual (Historia Menstrual)"));
		
		if(!this.duracionMenstruacion.equals("")&&Utilidades.convertirAEntero(this.duracionMenstruacion)==ConstantesBD.codigoNuncaValido)
			errores.add("", new ActionMessage("errors.integer","El campo duración menstrual (Historia Menstrual)"));
		
		if(!this.fechaUltimaRegla.equals("")&&!UtilidadFecha.validarFecha(this.fechaUltimaRegla))
			errores.add("", new ActionMessage("errors.formatoFechaInvalido","última regla 'FUR' (Historia Menstrual)"));
		
		return errores;
	}
	
	/**
	 * Método para verificar si se ingresó información de la historia menstrual
	 * @return
	 */
	public boolean ingresoInformacion()
	{
		boolean inserto = false;
		
		if(
				//Se verifica si se ingresó edad menarquia
				(!this.isExisteEdadMenarquia()&&this.getCodigoEdadMenarquia()!=ConstantesBD.codigoNuncaValido)
				||
				//Se verifica si se ingresó edad menopausia
				(!this.isExisteEdadMenopausia()&&this.getCodigoEdadMenopausia()!=ConstantesBD.codigoNuncaValido)
				||
				//Se verifica si se modificó el ciclo menstrual
				(!this.getCicloMenstrual().equals(this.getCicloMenstrualAnterior()))
				||
				//Se verifica si se modificó la duración menstrual
				(!this.getDuracionMenstruacion().equals(this.getDuracionMenstruacionAnterior()))
				||
				//Se verifica si se modificó la FUR
				(!this.getFechaUltimaRegla().equals(this.getFechaUltimaReglaAnterior()))
				||
				//Se verifica si se modificó el dolor menstrual
				(this.getDolorMenstruacion()!=this.getDolorMenstruacionAnterior())
				||
				//Se verifica si se modificó el concepto menstrual
				(this.getCodigoConceptoMenstruacion()!=this.getCodigoConceptoMenstruacionAnterior())
				||
				//Se verifica si se modificarion las observaciones menstruales
				(this.getObservacionesMenstruales().equals(this.getObservacionesMenstrualesAnteriores()))
				
			)
		{
			inserto = true;
		}
		
		return inserto;
	}
	
	/**
	 * Método que verifica si el DTO está cargado
	 * @return
	 */
	public boolean estaCargado()
	{
		boolean inserto = false;
		
		
		logger.info("existe edad menarquia: "+this.isExisteEdadMenarquia());
		logger.info("existe edad menopausia: "+this.isExisteEdadMenopausia());
		logger.info("existe observaciones: "+!this.getObservacionesMenstruales().equals(""));
		if(
				//Se verifica si existe edad menarquia
				this.isExisteEdadMenarquia()
				||
				//Se verifica si existe edad menopausia
				this.isExisteEdadMenopausia()
				||
				//Se verifica si se tiene el ciclo menstrual
				!this.getCicloMenstrual().equals("")
				||
				//Se verifica si se tiene la duración menstrual
				!this.getDuracionMenstruacion().equals("")
				||
				//Se verifica si se tiene la FUR
				!this.getFechaUltimaRegla().equals("")
				||
				//Se verifica si se tiene el dolor menstrual
				this.getDolorMenstruacion()!=null
				||
				//Se verifica si se tiene el concepto menstrual
				this.getCodigoConceptoMenstruacion()>0
				||
				//Se verifica si se tiene las observaciones menstruales
				!this.getObservacionesMenstruales().equals("")
				
			)
		{
			inserto = true;
		}
		
		return inserto;
	}
	
	
	/**
	 * Método que verifica si existe menarquia o menopausia
	 * @return
	 */
	public boolean isExisteMenarquiaMenopausia()
	{
		boolean existe = false;
		
		if(this.isExisteEdadMenarquia()||
			!this.getOtraEdadMenarquia().trim().equals("")||
			this.isExisteEdadMenopausia()||
			!this.getOtraEdadMenopausia().trim().equals(""))
			existe = true;
		
		return existe;
	}
	
	/**
	 * Método que verifica si existe informacion de Ciclo, FUR,  duracionMenstruacion y dolormenstruacion
	 * @return
	 */
	public boolean isExisteSubseccionMenstrual()
	{
		boolean existe = false;
		
		if(!this.getCicloMenstrual().trim().equals("")||!this.getDuracionMenstruacion().trim().equals("")||!this.getFechaUltimaRegla().trim().equals("")||this.getDolorMenstruacion()!=null)
			existe = true;
		
		return existe;
	}
	
	
}
