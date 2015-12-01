package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ISubCuentasMundo;
import com.servinte.axioma.orm.SubCuentas;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ISubCuentasServicio;



public class SubCuentasServicio implements ISubCuentasServicio {
	
	ISubCuentasMundo mundo;
	
	
	public SubCuentasServicio() {
		mundo = ManejoPacienteFabricaMundo.crearSubCuentasMundo();
	}


	@Override
	public SubCuentas crearSubCuentas(
			DtoInformacionBasicaIngresoPaciente dtoInfoBasica) {
		return mundo.crearSubCuentas(dtoInfoBasica);
	}


	@Override
	public ArrayList<SubCuentas> listarCuentasPorPaciente(int paciente) {
		return mundo.listarCuentasPorPaciente(paciente);
	}

	@Override
	public SubCuentas cargarSubCuenta(int codigoResponsable) {
		return mundo.cargarSubCuenta(codigoResponsable);
	}
	
	/**
	 * Carga una subcuenta por su id y su detalle monto
	 * @param id de la subcuenta
	 * @return SubCuentas
	 */
	@Override
	public SubCuentas cargarSubcuentaDetalleMonto(int codigoResponsable){
		return mundo.cargarSubcuentaDetalleMonto(codigoResponsable);
	}


}
