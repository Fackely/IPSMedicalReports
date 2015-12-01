/**
 * 
 */
package com.servinte.axioma.dto.salascirugia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jeilones
 * @created 17/06/2013
 *
 */
public class InformeQxDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4414427162501421451L;

	private List<EspecialidadDto>especialidadesParticipan;
	
	/**
	 * Dto que permite almacenar la informacion que se debe mostrar en pantalla 
	 */
	private InformeQxEspecialidadDto informeQxEspecialidad;
	
	private List<ProfesionalHQxDto>otrosProfesionales;
	
	private List<NotaAclaratoriaDto> notasAclaratorias;
	
	private List<ProfesionalHQxDto>otrosProfesionalesAEliminarse=new ArrayList<ProfesionalHQxDto>(0);

	private Boolean finalizadaHQx;
	
	/**
	 * @return the otrosProfesionalesAEliminarse
	 */
	public List<ProfesionalHQxDto> getOtrosProfesionalesAEliminarse() {
		return otrosProfesionalesAEliminarse;
	}

	/**
	 * @param otrosProfesionalesAEliminarse the otrosProfesionalesAEliminarse to set
	 */
	public void setOtrosProfesionalesAEliminarse(
			List<ProfesionalHQxDto> otrosProfesionalesAEliminarse) {
		this.otrosProfesionalesAEliminarse = otrosProfesionalesAEliminarse;
	}

	public List<EspecialidadDto> getEspecialidadesParticipan() {
		return especialidadesParticipan;
	}

	public void setEspecialidadesParticipan(
			List<EspecialidadDto> especialidadesParticipan) {
		this.especialidadesParticipan = especialidadesParticipan;
	}

	public InformeQxEspecialidadDto getInformeQxEspecialidad() {
		return informeQxEspecialidad;
	}

	public void setInformeQxEspecialidad(
			InformeQxEspecialidadDto informeQxEspecialidad) {
		this.informeQxEspecialidad = informeQxEspecialidad;
	}

	/**
	 * @return the otrosProfesionales
	 */
	public List<ProfesionalHQxDto> getOtrosProfesionales() {
		return otrosProfesionales;
	}

	/**
	 * @param otrosProfesionales the otrosProfesionales to set
	 */
	public void setOtrosProfesionales(List<ProfesionalHQxDto> otrosProfesionales) {
		this.otrosProfesionales = otrosProfesionales;
	}

	/**
	 * @return the finalizadaHQx
	 */
	public Boolean getFinalizadaHQx() {
		return finalizadaHQx;
	}

	/**
	 * @param finalizadaHQx the finalizadaHQx to set
	 */
	public void setFinalizadaHQx(Boolean finalizadaHQx) {
		this.finalizadaHQx = finalizadaHQx;
	}

	/**
	 * @return the notasAclaratorias
	 */
	public List<NotaAclaratoriaDto> getNotasAclaratorias() {
		return notasAclaratorias;
	}

	/**
	 * @param notasAclaratorias the notasAclaratorias to set
	 */
	public void setNotasAclaratorias(List<NotaAclaratoriaDto> notasAclaratorias) {
		this.notasAclaratorias = notasAclaratorias;
	}
}
