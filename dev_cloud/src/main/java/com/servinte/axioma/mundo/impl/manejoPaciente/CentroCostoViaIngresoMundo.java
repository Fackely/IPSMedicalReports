package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoCentroCostoViaIngreso;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ICentroCostoViaIngresoDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ICentroCostoViaIngresoMundo;

public class CentroCostoViaIngresoMundo implements ICentroCostoViaIngresoMundo{
	
	ICentroCostoViaIngresoDAO centroCostoViaIngresoDAO;
	
	
	public CentroCostoViaIngresoMundo(){
		centroCostoViaIngresoDAO= ManejoPacienteDAOFabrica.crearCentroCostoViaIngreso();
	}

	@Override
	public ArrayList<DtoCentroCostoViaIngreso> obtenerCentrosCostoViaIngreso(
			int institucion) {
		return centroCostoViaIngresoDAO.obtenerCentrosCostoViaIngreso(institucion);
	}

	
	/**
	 * M&eacute;todo encargado de obtener los centros de costo por 
	 * v&iacute;a de ingreso
	 * @author Camilo Gomez
	 * @param viaIngreso
	 * @return ArrayList<DtoCentroCostoViaIngreso>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoCentroCostoViaIngreso> listarCentrosCostoXViaIngreso(int viaIngreso){
		return centroCostoViaIngresoDAO.listarCentrosCostoXViaIngreso(viaIngreso);
	}
}
