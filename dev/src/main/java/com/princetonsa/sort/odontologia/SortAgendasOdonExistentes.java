package com.princetonsa.sort.odontologia;

import java.util.Comparator;

import com.princetonsa.dto.odontologia.DtoDetalleAgrupacionHonorarios;
import com.princetonsa.dto.odontologia.DtoExistenciaAgenOdon;

/**
 * 
 * @author Víctor Hugo Gómez L.
 *
 */

public class SortAgendasOdonExistentes implements Comparator<DtoExistenciaAgenOdon>{

	/**
	 * 
	 */
	private String patronOrdenar;

	/**
	 * 
	 * @param patronOrden
	 */
	public SortAgendasOdonExistentes() {
		this.patronOrdenar = "";
	}
	
	@Override
	public int compare(DtoExistenciaAgenOdon one, DtoExistenciaAgenOdon two) 
	{
		if (this.getPatronOrdenar().equals("hora_ini")){
			return new String(one.getHoraIni()).compareTo(new String(two.getHoraIni()));
		}else if(this.getPatronOrdenar().equals("hora_fin")) {
			return new String(one.getHoraFin()).compareTo(new String(two.getHoraFin()));
		}
		
		else if(this.getPatronOrdenar().equals("hora_ini_descendente")) {
			return new String(two.getHoraIni()).compareTo(new String(one.getHoraIni()));
		}else if (this.getPatronOrdenar().equals("hora_fin_descendente")) {
			return new String(two.getHoraFin()).compareTo(new String(one.getHoraFin()));
		}

		else{
			return new String(one.getHoraIni()).compareTo(new String(two.getHoraIni()));
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
