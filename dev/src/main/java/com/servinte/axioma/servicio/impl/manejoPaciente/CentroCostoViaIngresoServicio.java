package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoCentroCostoViaIngreso;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ICentroCostoViaIngresoMundo;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ICentroCostoViaIngresoServicio;

public class CentroCostoViaIngresoServicio implements ICentroCostoViaIngresoServicio {
	
	ICentroCostoViaIngresoMundo centroCostoViaIngresoMundo;
	
	public CentroCostoViaIngresoServicio(){
		centroCostoViaIngresoMundo = ManejoPacienteFabricaMundo.crearCentroCostoViaIngresoMundo();
	}

	@Override
	public ArrayList<DtoCentroCostoViaIngreso> obtenerCentrosCostoViaIngreso(
			int institucion) {
		return centroCostoViaIngresoMundo.obtenerCentrosCostoViaIngreso(institucion);
	}

}
