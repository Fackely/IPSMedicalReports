package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoDetalleServicioHonorarios;


public class SortDetalleServicioHonorario  implements Comparator<DtoDetalleServicioHonorarios>{

    
	/**
	 * 
	 */
	private String patronOrdenar;

	/**
	 * 
	 * @param patronOrden
	 */
	public SortDetalleServicioHonorario(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public int compare(DtoDetalleServicioHonorarios one, DtoDetalleServicioHonorarios two) {
		if (this.getPatronOrdenar().equals("servicio")) {
			return (one.getServicio().getNombre()).compareTo(two.getServicio().getNombre());
		}
		if (this.getPatronOrdenar().equals("servicio_descendente")) {
			return (two.getServicio().getNombre()).compareTo(one.getServicio().getNombre());
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
