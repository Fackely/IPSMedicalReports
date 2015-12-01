/*
 * Nov 30, 2009
 */
package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;

import util.UtilidadFecha;

import com.princetonsa.dto.historiaClinica.parametrizacion.DtoPlantilla;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * 
 * DTO usado para encapsular la ifnormacion de la pantalla
 * de atención de citas
 * @author Sebastián gómez R.
 *
 */
public class DtoInfoAtencionCitaOdo implements Serializable 
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;

	private ArrayList<DtoPlantilla> plantillas ;
	
	private UsuarioBasico usuarioConfirma;
	
	private String fechaConfirma;
	
	private String horaConfirma;
	
	private UsuarioBasico usuarioRegistra;
	
	private String fechaRegistra;
	
	private String horaRegistra;
	
	//*********ATRIBUTOS PARA LA PARTE DE CONFIRMACION*************************+
	private boolean mismoProfesional;
	//****************************************************************************
	
	/**
	 * CONSTRUCTOR
	 */
	public DtoInfoAtencionCitaOdo()
	{
		this.reset();
	}
	
	/**
	 * Método para limpiar la informacion
	 */
	public void reset()
	{
		this.plantillas = new ArrayList<DtoPlantilla>();
		this.usuarioConfirma = new UsuarioBasico();
		this.fechaConfirma = "";
		this.horaConfirma = "";
		this.usuarioRegistra = new UsuarioBasico();
		this.fechaRegistra = "";
		this.horaRegistra = "";
		
		this.mismoProfesional = false;
	}

	/**
	 * @return the plantillas
	 */
	public ArrayList<DtoPlantilla> getPlantillas() {
		return plantillas;
	}

	/**
	 * @param plantillas the plantillas to set
	 */
	public void setPlantillas(ArrayList<DtoPlantilla> plantillas) {
		this.plantillas = plantillas;
	}

	/**
	 * @return the usuarioConfirma
	 */
	public UsuarioBasico getUsuarioConfirma() {
		return usuarioConfirma;
	}

	/**
	 * @param usuarioConfirma the usuarioConfirma to set
	 */
	public void setUsuarioConfirma(UsuarioBasico usuarioConfirma) {
		this.usuarioConfirma = usuarioConfirma;
	}

	/**
	 * @return the fechaConfirma
	 */
	public String getFechaConfirma() {
		return fechaConfirma;
	}
	

	/**
	 * @param fechaConfirma the fechaConfirma to set
	 */
	public void setFechaConfirma(String fechaConfirma) {
		this.fechaConfirma = fechaConfirma;
	}

	/**
	 * @return the horaConfirma
	 */
	public String getHoraConfirma() {
		return horaConfirma;
	}

	/**
	 * @param horaConfirma the horaConfirma to set
	 */
	public void setHoraConfirma(String horaConfirma) {
		this.horaConfirma = horaConfirma;
	}

	/**
	 * @return the usuarioRegistra
	 */
	public UsuarioBasico getUsuarioRegistra() {
		return usuarioRegistra;
	}

	/**
	 * @param usuarioRegistra the usuarioRegistra to set
	 */
	public void setUsuarioRegistra(UsuarioBasico usuarioRegistra) {
		this.usuarioRegistra = usuarioRegistra;
	}

	/**
	 * @return the fechaRegistra
	 */
	public String getFechaRegistra() {
		return fechaRegistra;
	}
	

	/**
	 * 
	 * @return
	 */
	public String getFechaRegistraAp()
	{
		return UtilidadFecha.conversionFormatoFechaAAp(this.fechaRegistra);
	}

	/**
	 * @param fechaRegistra the fechaRegistra to set
	 */
	public void setFechaRegistra(String fechaRegistra) {
		this.fechaRegistra = fechaRegistra;
	}

	/**
	 * @return the horaRegistra
	 */
	public String getHoraRegistra() {
		return horaRegistra;
	}

	/**
	 * @param horaRegistra the horaRegistra to set
	 */
	public void setHoraRegistra(String horaRegistra) {
		this.horaRegistra = horaRegistra;
	}

	/**
	 * @return the mismoProfesional
	 */
	public boolean isMismoProfesional() {
		return mismoProfesional;
	}

	/**
	 * @param mismoProfesional the mismoProfesional to set
	 */
	public void setMismoProfesional(boolean mismoProfesional) {
		this.mismoProfesional = mismoProfesional;
	}
	
	/**
	 * Método que retorna el número de plantillas
	 * @return
	 */
	public int getNumPlantillas()
	{
		return this.plantillas.size();
	}
	
	/**
	 * Método para verificar si existe una plantilla en proceso
	 * @return
	 */
	public boolean isExistenPlantillasEnProceso()
	{
		boolean existe = false;
		
		for(DtoPlantilla plantilla:this.plantillas)
		{
			if(plantilla.isPlantillaEnProceso())
			{
				existe = true;
			}
		}
		
		return existe;
	}

	
	
}
