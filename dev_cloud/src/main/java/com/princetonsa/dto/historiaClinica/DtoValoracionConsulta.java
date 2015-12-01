/*
 * Abr 29, 2008
 */
package com.princetonsa.dto.historiaClinica;

import java.io.Serializable;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.InfoDatosString;
import util.Utilidades;

/**
 * Data Transfer Object: Valoración Consulta
 * @author Sebastián Gómez R.
 *
 */
public class DtoValoracionConsulta implements Serializable
{
	private Boolean primeraVez;
	private InfoDatosString finalidadConsulta;
	private InfoDatosInt tipoDiagnostico;
	private InfoDatosInt tipoRecargo;
	private String fechaProximoControl;
	private String numeroDiasIncapacidad;
	private String observacionesIncapacidad;
	/**
	 * Maneja las llaves:
	 * adjOriginalValoracion_<pos>
	 * adjValoracion_<pos>
	 * numRegistros
	 */
	//Atributos que manejan los archivos adjuntos
	private HashMap<String, Object> archivosAdjuntos;
	
	/**
	 * Constructor
	 *
	 */
	public DtoValoracionConsulta()
	{
		this.clean();
	}
	
	/**
	 * Método que resetea los datos 
	 *
	 */
	public void clean()
	{
		this.primeraVez = null;
		this.finalidadConsulta = new InfoDatosString("","");
		this.tipoDiagnostico = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.tipoRecargo = new InfoDatosInt(ConstantesBD.codigoTipoRecargoSinRecargo,"");
		this.fechaProximoControl = "";
		this.numeroDiasIncapacidad = "";
		this.observacionesIncapacidad = "";
		
		//Atributos que manejan los archivos adjuntos
		this.archivosAdjuntos = null;
	}

	/**
	 * @return the fechaProximoControl
	 */
	public String getFechaProximoControl() {
		return fechaProximoControl;
	}

	/**
	 * @param fechaProximoControl the fechaProximoControl to set
	 */
	public void setFechaProximoControl(String fechaProximoControl) {
		this.fechaProximoControl = fechaProximoControl;
	}

	/**
	 * @return the finalidadConsulta
	 */
	public String getCodigoFinalidadConsulta() {
		return finalidadConsulta.getCodigo();
	}

	/**
	 * @param finalidadConsulta the finalidadConsulta to set
	 */
	public void setCodigoFinalidadConsulta(String finalidadConsulta) {
		this.finalidadConsulta.setCodigo(finalidadConsulta);
	}
	
	/**
	 * @return the finalidadConsulta
	 */
	public String getNombreFinalidadConsulta() {
		return finalidadConsulta.getNombre();
	}

	/**
	 * @param finalidadConsulta the finalidadConsulta to set
	 */
	public void setNombreFinalidadConsulta(String finalidadConsulta) {
		this.finalidadConsulta.setNombre(finalidadConsulta);
	}

	/**
	 * @return the primeraVez
	 */
	public Boolean getPrimeraVez() {
		return primeraVez;
	}

	/**
	 * @param primeraVez the primeraVez to set
	 */
	public void setPrimeraVez(Boolean primeraVez) {
		this.primeraVez = primeraVez;
	}

	/**
	 * @return the tipoDiagnostico
	 */
	public int getCodigoTipoDiagnostico() {
		return tipoDiagnostico.getCodigo();
	}

	/**
	 * @param tipoDiagnostico the tipoDiagnostico to set
	 */
	public void setCodigoTipoDiagnostico(int tipoDiagnostico) {
		this.tipoDiagnostico.setCodigo(tipoDiagnostico);
	}
	
	/**
	 * @return the tipoDiagnostico
	 */
	public String getNombreTipoDiagnostico() {
		return tipoDiagnostico.getNombre();
	}

	/**
	 * @param tipoDiagnostico the tipoDiagnostico to set
	 */
	public void setNombreTipoDiagnostico(String tipoDiagnostico) {
		this.tipoDiagnostico.setNombre(tipoDiagnostico);
	}

	/**
	 * @return the tipoRecargo
	 */
	public int getCodigoTipoRecargo() {
		return tipoRecargo.getCodigo();
	}

	/**
	 * @param tipoRecargo the tipoRecargo to set
	 */
	public void setCodigoTipoRecargo(int tipoRecargo) {
		this.tipoRecargo.setCodigo(tipoRecargo);
	}
	
	/**
	 * @return the tipoRecargo
	 */
	public String getNombreTipoRecargo() {
		return tipoRecargo.getNombre();
	}

	/**
	 * @param tipoRecargo the tipoRecargo to set
	 */
	public void setNombreTipoRecargo(String tipoRecargo) {
		this.tipoRecargo.setNombre(tipoRecargo);
	}

	/**
	 * @return the numeroDiasIncapacidad
	 */
	public String getNumeroDiasIncapacidad() {
		return numeroDiasIncapacidad;
	}

	/**
	 * @param numeroDiasIncapacidad the numeroDiasIncapacidad to set
	 */
	public void setNumeroDiasIncapacidad(String numeroDiasIncapacidad) {
		this.numeroDiasIncapacidad = numeroDiasIncapacidad;
	}

	/**
	 * @return the observacionesIncapacidad
	 */
	public String getObservacionesIncapacidad() {
		return observacionesIncapacidad;
	}

	/**
	 * @param observacionesIncapacidad the observacionesIncapacidad to set
	 */
	public void setObservacionesIncapacidad(String observacionesIncapacidad) {
		this.observacionesIncapacidad = observacionesIncapacidad;
	}

	/**
	 * @return the finalidadConsulta
	 */
	public InfoDatosString getFinalidadConsulta() {
		return finalidadConsulta;
	}

	/**
	 * @param finalidadConsulta the finalidadConsulta to set
	 */
	public void setFinalidadConsulta(InfoDatosString finalidadConsulta) {
		this.finalidadConsulta = finalidadConsulta;
	}

	/**
	 * @return the archivosAdjuntos
	 */
	public HashMap<String, Object> getArchivosAdjuntos() {
		if(archivosAdjuntos == null)
		{
			archivosAdjuntos = new HashMap<String, Object>();
		}
		return archivosAdjuntos;
	}

	/**
	 * @param archivosAdjuntos the archivosAdjuntos to set
	 */
	public void setArchivosAdjuntos(HashMap<String, Object> archivosAdjuntos) {
		this.archivosAdjuntos = archivosAdjuntos;
	}
	
	/**
	 * @return the archivosAdjuntos
	 */
	public Object getArchivosAdjuntos(String key) {
		return getArchivosAdjuntos().get(key);
	}

	/**
	 * @param archivosAdjuntos the archivosAdjuntos to set
	 */
	public void setArchivosAdjuntos(String key,Object obj) {
		this.getArchivosAdjuntos().put(key,obj);
	}
	
	/**
	 * Método para obtener el número de archivos adjuntos
	 * @return
	 */
	public int getNumArchivosAdjuntos()
	{
		return Utilidades.convertirAEntero(this.getArchivosAdjuntos("numRegistros")+"",true);
	}
	
	/**
	 * Método para asignar el tamaño de un archivo adjunto
	 * @param numRegistros
	 */
	public void setNumArchivosAdjuntos(int numRegistros)
	{
		this.setArchivosAdjuntos("numRegistros", numRegistros);
	}

	
	
	
	
}
