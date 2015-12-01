package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoDetalleAgrupacionHonorarios;


public class SortDetalleAgrupacionHonorario implements Comparator<DtoDetalleAgrupacionHonorarios> {

	
	/**
	 * 
	 */
	private String patronOrdenar;

	/**
	 * 
	 * @param patronOrden
	 */
	public SortDetalleAgrupacionHonorario(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	
	public int compare(DtoDetalleAgrupacionHonorarios one, DtoDetalleAgrupacionHonorarios two) {
		if (this.getPatronOrdenar().equals("tipo_servicio")) {
			return (one.getTipoServicio().getNombre()).compareTo(two.getTipoServicio().getNombre());
		}
		if (this.getPatronOrdenar().equals("tipo_servicio_descendente")) {
			return (two.getTipoServicio().getNombre()).compareTo(one.getTipoServicio().getNombre());
		}
		
		
		if (this.getPatronOrdenar().equals("grupo_servicio")) {
			return (one.getGrupoServicio().getDescripcion()).compareTo(two.getGrupoServicio().getDescripcion());
		}
		if (this.getPatronOrdenar().equals("grupo_servicio_descendente")) {
			return (two.getGrupoServicio().getDescripcion()).compareTo(one.getGrupoServicio().getDescripcion());
		}
		
		
		
	
		if (this.getPatronOrdenar().equals("especialidad")) {
				return (one.getEspecialidad().getNombre()).compareTo(two.getEspecialidad().getNombre());
		}
		if (this.getPatronOrdenar().equals("especialidad_descendente")) {
			return (two.getEspecialidad().getNombre()).compareTo(one.getEspecialidad().getNombre());
       	}
		
		
		
		if (this.getPatronOrdenar().equals("valor_participacion")) {
			return (new Double(one.getValorParticipacion())).compareTo(new Double(two.getValorParticipacion()));
	     }	
		
		if (this.getPatronOrdenar().equals("valor_participacion_descendente")) {
			return (new Double(two.getValorParticipacion())).compareTo(new Double(one.getValorParticipacion()));
	     }	
		
		if (this.getPatronOrdenar().equals("porcentaje_participacion")) {
			return (new Double(one.getPorcentajeParticipacion())).compareTo(new Double(two.getPorcentajeParticipacion()));
	     }	
		if (this.getPatronOrdenar().equals("porcentaje_participacion_descendente")) {
			return (new Double(two.getPorcentajeParticipacion())).compareTo(new Double(one.getPorcentajeParticipacion()));
	     }	
		
		

		else {
			return new Double(one.getCodigo()).compareTo(new Double(two
					.getCodigo()));
		}
	}
	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return this.patronOrdenar;
	}

	/**
	 * @param patronOrdenar
	 *            the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}
}
