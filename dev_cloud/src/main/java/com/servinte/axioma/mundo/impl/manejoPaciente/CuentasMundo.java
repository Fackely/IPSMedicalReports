package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ICuentasDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ICuentasMundo;
import com.servinte.axioma.orm.Cuentas;
import com.servinte.axioma.orm.EstadosCuenta;
import com.servinte.axioma.orm.EstadosCuentaHome;
import com.servinte.axioma.orm.OriAdmisionHospi;
import com.servinte.axioma.orm.OriAdmisionHospiHome;
import com.servinte.axioma.orm.TiposPaciente;
import com.servinte.axioma.orm.TiposPacienteHome;
import com.servinte.axioma.orm.ViasIngreso;
import com.servinte.axioma.orm.ViasIngresoHome;


public class CuentasMundo implements ICuentasMundo {
	
	private ICuentasDAO cuentasDAO;
	
	
	public CuentasMundo() {
		inicializar();
	}
	
	
	private void inicializar() {
		cuentasDAO	= ManejoPacienteDAOFabrica.crearCuentasDAO();
	}


	@Override
	public ArrayList<Cuentas> listarTodosXEstadoCuenta(
			String[] listaEstadosCuenta, int codPaciente) {
		return cuentasDAO.listarTodosXEstadoCuenta(listaEstadosCuenta, codPaciente);
	}


	@Override
	public ArrayList<Cuentas> verificarCuentasPorIngreso(int viaIngreso) {
		return cuentasDAO.verificarCuentasPorIngreso(viaIngreso);
	}


	@Override
	public ArrayList<Cuentas> verificarEstadoCuenta(int codCuenta) {
		return cuentasDAO.verificarEstadoCuenta(codCuenta);
	}

	
	@Override
	public Cuentas crearCuentas(DtoInformacionBasicaIngresoPaciente dtoInfoBasica) {
		
		Cuentas cuenta;
		cuenta = new Cuentas();
		
		cuenta.setPacientes(dtoInfoBasica.getPaciente());
		cuenta.setFechaApertura(dtoInfoBasica.getFechaActual());
		cuenta.setHoraApertura(dtoInfoBasica.getHoraActual());
		cuenta.setIndicativoAccTransito(false);
		cuenta.setPacienteReferido(false);	
		cuenta.setIngresos(dtoInfoBasica.getIngreso());
		cuenta.setUsuarios(dtoInfoBasica.getUsuario());				// cuenta.setUsuarioModifica(loginUsuario);
		cuenta.setDesplazado(dtoInfoBasica.getAcronimoNo());
		cuenta.setFechaModifica(dtoInfoBasica.getFechaActual());
		cuenta.setHoraModifica(dtoInfoBasica.getHoraActual());
		cuenta.setTipoEvento("");
		cuenta.setHospitalDia(dtoInfoBasica.getAcronimoNo().charAt(0));
		
		OriAdmisionHospi oriAdmisionHospi = new OriAdmisionHospi();
		OriAdmisionHospiHome oaoH = new OriAdmisionHospiHome();
		oriAdmisionHospi = oaoH.findById(dtoInfoBasica.getOrigenAdmisionConsultaExterna());	// =origenAdmisionConsultaExterna(2)
		cuenta.setOriAdmisionHospi(oriAdmisionHospi);
		
		ViasIngreso viasIngreso = new ViasIngreso();
		ViasIngresoHome viH = new ViasIngresoHome();
		viasIngreso = viH.findById(dtoInfoBasica.getViaIngresoConsultaExterna()); //=viaIngresoConsultaExterna(4)
		cuenta.setViasIngreso(viasIngreso);
		
		//TiposPaciente tiposPaciente;
		TiposPaciente tiposPaciente = new TiposPaciente();
		TiposPacienteHome tpH = new TiposPacienteHome();
		tiposPaciente = tpH.findById(dtoInfoBasica.getAronimoTipoPacienteAmbulatorios()); //=aronimoTipoPacienteAmbulatorios(A);
		cuenta.setTiposPaciente(tiposPaciente);
		
		EstadosCuenta estadosCuenta = new EstadosCuenta();
		EstadosCuentaHome ecH = new EstadosCuentaHome();
		estadosCuenta = ecH.findById(dtoInfoBasica.getCodigoCuentaActiva());
		cuenta.setEstadosCuenta(estadosCuenta);
		
		// cuenta.setArea(Integer.parseInt(codigoInterfazCentroAtencion));
		cuenta.setCentrosCosto(dtoInfoBasica.getAreaCentroCosto());
		
		//cuenta.setMigrado(Constantes.acronimoNo);
		
		return cuenta;
	}

	/**
	 * Retorna la cuenta por el codigo de la persona
	 * @param codPersona
	 * @return  Cuentas
	 */
	public Cuentas obtenerCuentaPorCodigoPersona(int codPersona){
		return cuentasDAO.obtenerCuentaPorCodigoPersona(codPersona);
	}
}
