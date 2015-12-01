package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoCitaOdontologica;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

public class SortCitasOdontologicas implements Comparator<DtoCitaOdontologica> 
{
	/**
	 * 
	 */
	private String patronOrdenar; 
	
	
	@Override
	public int compare(DtoCitaOdontologica one, DtoCitaOdontologica two) 
	{
		if(this.patronOrdenar.equals("nombrepaciente")){
			return new String(one.getNombrePaciente()).compareTo(new String(two.getNombrePaciente()));
		}else if(this.patronOrdenar.equals("numeroidentificacionpac")){
			return new String(one.getNumeroIdentificacionPac()).compareTo(new String(two.getNumeroIdentificacionPac()));
		}else if(this.patronOrdenar.equals("centroatencion")){
			return new String(one.getAgendaOdon().getDescripcionCentAten()).compareTo(new String(two.getAgendaOdon().getDescripcionCentAten()));
		}else if(this.patronOrdenar.equals("unidadagenda")){
			return new String(one.getAgendaOdon().getDescripcionUniAgen()).compareTo(new String(two.getAgendaOdon().getDescripcionUniAgen()));
		}else if(this.patronOrdenar.equals("profesional")){
			return new String(one.getAgendaOdon().getNombreMedico()).compareTo(new String(two.getAgendaOdon().getNombreMedico()));
		}else if(this.patronOrdenar.equals("fecha")){
			return new String(one.getAgendaOdon().getFecha()).compareTo(new String(two.getAgendaOdon().getFecha()));
		}else if(this.patronOrdenar.equals("horainifin")){
			return new String(one.getHoraInicio()+" - "+one.getHoraFinal()).compareTo(new String(two.getHoraInicio()+" - "+two.getHoraFinal()));
		}else if(this.patronOrdenar.equals("duracion")){
			return new Integer(one.getDuracion()).compareTo(new Integer(two.getDuracion()));
		}else if(this.patronOrdenar.equals("estado")){
			return new String(one.getEstado()).compareTo(new String(two.getEstado()));
		}else if(this.patronOrdenar.equals("horainicio")){
			return new String(one.getHoraInicio()).compareTo(new String(two.getHoraInicio()));
		}else if(this.patronOrdenar.equals("nombretipocita")){
			return new String(one.getNombreTipo()).compareTo(new String(two.getNombreTipo()));
		}else if(this.patronOrdenar.equals("telefonos")){
			return new String(one.getTelefonoPaciente()).compareTo(new String(two.getTelefonoPaciente()));
		}
		else if(this.patronOrdenar.equals("nombrepaciente_descendente")){
			return new String(two.getNombrePaciente()).compareTo(new String(one.getNombrePaciente()));
		}else if(this.patronOrdenar.equals("numeroidentificacionpac_descendente")){
			return new String(two.getNumeroIdentificacionPac()).compareTo(new String(one.getNumeroIdentificacionPac()));
		}else if(this.patronOrdenar.equals("centroatencion_descendente")){
			return new String(two.getAgendaOdon().getDescripcionCentAten()).compareTo(new String(one.getAgendaOdon().getDescripcionCentAten()));
		}else if(this.patronOrdenar.equals("unidadagenda_descendente")){
			return new String(two.getAgendaOdon().getDescripcionUniAgen()).compareTo(new String(one.getAgendaOdon().getDescripcionUniAgen()));
		}else if(this.patronOrdenar.equals("profesional_descendente")){
			return new String(two.getAgendaOdon().getNombreMedico()).compareTo(new String(one.getAgendaOdon().getNombreMedico()));
		}else if(this.patronOrdenar.equals("fecha_descendente")){
			return new String(two.getAgendaOdon().getFecha()).compareTo(new String(one.getAgendaOdon().getFecha()));
		}else if(this.patronOrdenar.equals("horainifin_descendente")){
			return new String(two.getHoraInicio()+" - "+two.getHoraFinal()).compareTo(new String(one.getHoraInicio()+" - "+one.getHoraFinal()));
		}else if(this.patronOrdenar.equals("duracion_descendente")){
			return new Integer(two.getDuracion()).compareTo(new Integer(one.getDuracion()));
		}else if(this.patronOrdenar.equals("estado_descendente")){
			return new String(two.getEstado()).compareTo(new String(one.getEstado()));
		}else if(this.patronOrdenar.equals("horainicio_descendente")){
			return new String(two.getHoraInicio()).compareTo(new String(one.getHoraInicio()));
		}else if(this.patronOrdenar.equals("nombretipocita_descendente")){
			return new String(two.getNombreTipo()).compareTo(new String(one.getNombreTipo()));
		}else if(this.patronOrdenar.equals("telefonos_descendente")){
			return new String(two.getTelefonoPaciente()).compareTo(new String(one.getTelefonoPaciente()));
		}

		
		
		else{
			return new String(one.getAgendaOdon().getDescripcionCentAten()).compareTo(new String(two.getAgendaOdon().getDescripcionCentAten()));
		}
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
}
