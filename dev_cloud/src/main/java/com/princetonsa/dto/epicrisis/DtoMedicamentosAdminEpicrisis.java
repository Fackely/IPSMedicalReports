package com.princetonsa.dto.epicrisis;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 
 * @author wilson
 *
 */
public class DtoMedicamentosAdminEpicrisis implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 
	 */
	private String fechaAdmin;
	
	/**
	 * 
	 */
	private String horaAdmin;
	
	/**
	 * 
	 */
	private String responsableAdmin;
	
	/**
	 * 
	 */
	private String fechaOrden;
	
	/**
	 * 
	 */
	private String horaOrden;
	
	/**
	 * 
	 */
	private String medicoOrdena;
	
	/**
	 * 
	 */
	private String especialidadOrdena;
	
	/**
	 * 
	 */
	private String centroCostoOrdena;
	
	/**
	 * 
	 */
	private String centroCostoDespacha;
	
	/**
	 * 
	 */
	private String centroCostoAdmin;
	
	/**
	 * 
	 */
	private ArrayList<DtoMedicamentoEpicrisis> medicamentos;

	/**
	 * 
	 *
	 */
	public DtoMedicamentosAdminEpicrisis() 
	{
		this.fechaAdmin="";
		this.horaAdmin="";
		this.responsableAdmin="";
		this.fechaOrden="";
		this.horaOrden="";
		this.medicoOrdena="";
		this.especialidadOrdena="";
		this.centroCostoOrdena="";
		this.centroCostoDespacha="";
		this.centroCostoAdmin="";
		this.medicamentos= new ArrayList<DtoMedicamentoEpicrisis>();
	}

	/**
	 * @return the centroCostoAdmin
	 */
	public String getCentroCostoAdmin() {
		return centroCostoAdmin;
	}

	/**
	 * @param centroCostoAdmin the centroCostoAdmin to set
	 */
	public void setCentroCostoAdmin(String centroCostoAdmin) {
		this.centroCostoAdmin = centroCostoAdmin;
	}

	/**
	 * @return the centroCostoDespacha
	 */
	public String getCentroCostoDespacha() {
		return centroCostoDespacha;
	}

	/**
	 * @param centroCostoDespacha the centroCostoDespacha to set
	 */
	public void setCentroCostoDespacha(String centroCostoDespacha) {
		this.centroCostoDespacha = centroCostoDespacha;
	}

	/**
	 * @return the centroCostoOrdena
	 */
	public String getCentroCostoOrdena() {
		return centroCostoOrdena;
	}

	/**
	 * @param centroCostoOrdena the centroCostoOrdena to set
	 */
	public void setCentroCostoOrdena(String centroCostoOrdena) {
		this.centroCostoOrdena = centroCostoOrdena;
	}

	/**
	 * @return the especialidadOrdena
	 */
	public String getEspecialidadOrdena() {
		return especialidadOrdena;
	}

	/**
	 * @param especialidadOrdena the especialidadOrdena to set
	 */
	public void setEspecialidadOrdena(String especialidadOrdena) {
		this.especialidadOrdena = especialidadOrdena;
	}

	/**
	 * @return the fechaAdmin
	 */
	public String getFechaAdmin() {
		return fechaAdmin;
	}

	/**
	 * @param fechaAdmin the fechaAdmin to set
	 */
	public void setFechaAdmin(String fechaAdmin) {
		this.fechaAdmin = fechaAdmin;
	}

	/**
	 * @return the fechaOrden
	 */
	public String getFechaOrden() {
		return fechaOrden;
	}

	/**
	 * @param fechaOrden the fechaOrden to set
	 */
	public void setFechaOrden(String fechaOrden) {
		this.fechaOrden = fechaOrden;
	}

	/**
	 * @return the horaAdmin
	 */
	public String getHoraAdmin() {
		return horaAdmin;
	}

	/**
	 * @param horaAdmin the horaAdmin to set
	 */
	public void setHoraAdmin(String horaAdmin) {
		this.horaAdmin = horaAdmin;
	}

	/**
	 * @return the horaOrden
	 */
	public String getHoraOrden() {
		return horaOrden;
	}

	/**
	 * @param horaOrden the horaOrden to set
	 */
	public void setHoraOrden(String horaOrden) {
		this.horaOrden = horaOrden;
	}

	/**
	 * @return the medicamentos
	 */
	public ArrayList<DtoMedicamentoEpicrisis> getMedicamentos() {
		return medicamentos;
	}

	/**
	 * @param medicamentos the medicamentos to set
	 */
	public void setMedicamentos(ArrayList<DtoMedicamentoEpicrisis> medicamentos) {
		this.medicamentos = medicamentos;
	}

	/**
	 * @return the medicoOrdena
	 */
	public String getMedicoOrdena() {
		return medicoOrdena;
	}

	/**
	 * @param medicoOrdena the medicoOrdena to set
	 */
	public void setMedicoOrdena(String medicoOrdena) {
		this.medicoOrdena = medicoOrdena;
	}

	/**
	 * @return the responsableAdmin
	 */
	public String getResponsableAdmin() {
		return responsableAdmin;
	}

	/**
	 * @param responsableAdmin the responsableAdmin to set
	 */
	public void setResponsableAdmin(String responsableAdmin) {
		this.responsableAdmin = responsableAdmin;
	}
	
	
	
}
