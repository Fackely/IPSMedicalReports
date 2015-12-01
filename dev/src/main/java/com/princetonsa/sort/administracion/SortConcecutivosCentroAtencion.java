package com.princetonsa.sort.administracion;

import java.util.Comparator;
import com.princetonsa.dto.administracion.DtoConsecutivoCentroAtencion;

/**
 * 
 * @author axioma
 *
 */
public class SortConcecutivosCentroAtencion implements Comparator<DtoConsecutivoCentroAtencion>{
	/**
	 * 
	 */
	public int compare(DtoConsecutivoCentroAtencion o1,	DtoConsecutivoCentroAtencion o2) 
	{
		return o1.getAnio().concat(o1.getConsecutivo().toEngineeringString()).toLowerCase().compareTo(o2.getAnio().concat(o2.getConsecutivo().toEngineeringString()).toLowerCase());
	}

}
