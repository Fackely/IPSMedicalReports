package com.servinte.axioma.servicio.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ICuentasMundo;
import com.servinte.axioma.orm.Cuentas;
import com.servinte.axioma.servicio.interfaz.manejoPaciente.ICuentasServicio;

public class CuentasServicio implements ICuentasServicio {
	
	ICuentasMundo mundo;
	
	public CuentasServicio() {
		mundo = ManejoPacienteFabricaMundo.crearCuentasMundo();
	}

	
	@Override
	public Cuentas crearCuentas(
			DtoInformacionBasicaIngresoPaciente dtoInfoBasica) {
		return mundo.crearCuentas(dtoInfoBasica);
	}

	@Override
	public ArrayList<Cuentas> listarTodosXEstadoCuenta(
			String[] listaEstadosCuenta, int codPaciente) {
		return mundo.listarTodosXEstadoCuenta(listaEstadosCuenta, codPaciente);
	}

	@Override
	public ArrayList<Cuentas> verificarCuentasPorIngreso(int viaIngreso) {
		return mundo.verificarCuentasPorIngreso(viaIngreso);
	}

	@Override
	public ArrayList<Cuentas> verificarEstadoCuenta(int codCuenta) {
		return mundo.verificarEstadoCuenta(codCuenta);
	}
	

}
