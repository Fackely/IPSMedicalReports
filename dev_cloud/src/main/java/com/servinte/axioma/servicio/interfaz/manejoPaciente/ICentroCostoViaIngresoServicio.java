package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoCentroCostoViaIngreso;

public interface ICentroCostoViaIngresoServicio {
	
	/**
	 * M&eacute;todo encargado de obtener los centros de costo por 
	 * v&iacute;a de ingreso existentes en el sistema
	 * @param institucion
	 * @return ArrayList<DtoCentroCostoViaIngreso>
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoCentroCostoViaIngreso> obtenerCentrosCostoViaIngreso(int institucion);

}
