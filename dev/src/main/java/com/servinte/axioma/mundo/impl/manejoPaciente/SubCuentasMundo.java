package com.servinte.axioma.mundo.impl.manejoPaciente;

import java.util.ArrayList;

import util.ConstantesIntegridadDominio;

import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.servinte.axioma.dao.fabrica.ManejoPacienteDAOFabrica;
import com.servinte.axioma.dao.interfaz.manejoPaciente.ISubCuentasDAO;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.ISubCuentasMundo;
import com.servinte.axioma.orm.NaturalezaPacientes;
import com.servinte.axioma.orm.NaturalezaPacientesHome;
import com.servinte.axioma.orm.SubCuentas;
import com.servinte.axioma.orm.delegate.manejoPaciente.UtilidadIngresosDelegate;


public class SubCuentasMundo implements ISubCuentasMundo {
	
	private ISubCuentasDAO subCuentasDAO;
	
	
	public SubCuentasMundo() {
		inicializar();
	}
	
	
	private void inicializar() {
		subCuentasDAO	= ManejoPacienteDAOFabrica.crearSubCuentasDAO();
	}

	
	@SuppressWarnings("static-access")
	@Override
	public SubCuentas crearSubCuentas(DtoInformacionBasicaIngresoPaciente dtoInfoBasica) {
		
		SubCuentas subCuenta;
		subCuenta = new SubCuentas();
		
		subCuenta.setConvenios(dtoInfoBasica.getConvenio());
		subCuenta.setContratos(dtoInfoBasica.getContrato());
		subCuenta.setIngresos(dtoInfoBasica.getIngreso());
		subCuenta.setPacientes(dtoInfoBasica.getPaciente());
		subCuenta.setUsuarios(dtoInfoBasica.getUsuario());
		subCuenta.setFechaModifica(dtoInfoBasica.getFechaActual());
		
		NaturalezaPacientes naturalezaPacientes = new NaturalezaPacientes();
		NaturalezaPacientesHome npH = new NaturalezaPacientesHome();
		naturalezaPacientes = npH.findById(dtoInfoBasica.getNaturalezaPacienteNinguno());
		subCuenta.setNaturalezaPacientes(naturalezaPacientes);
		
		subCuenta.setFacturado(dtoInfoBasica.getAcronimoNo().charAt(0));
		subCuenta.setHoraModifica(dtoInfoBasica.getHoraActual());
		
		subCuenta.setNroPrioridad(dtoInfoBasica.getPrioridad());

		subCuenta.setTipoCobroPaciente(ConstantesIntegridadDominio.acronimoTipoPacienteNoManejaMontos);
		UtilidadIngresosDelegate utilIngreso = new UtilidadIngresosDelegate();
		subCuenta.setPorcentajeMontoCobro(utilIngreso.obtenerPorcentajeMontoCobro(dtoInfoBasica.getContrato().getCodigo()));
		
		return subCuenta;
	}


	@Override
	public ArrayList<SubCuentas> listarCuentasPorPaciente(int paciente) {
		return subCuentasDAO.listarCuentasPorPaciente(paciente);
	}


	@Override
	public SubCuentas cargarSubCuenta(int codigoResponsable) 
	{
		return subCuentasDAO.cargarSubCuenta(codigoResponsable);
	}

	/**
	 * Carga una subcuenta por su id y su detalle monto
	 * @param id de la subcuenta
	 * @return SubCuentas
	 */
	@Override
	public SubCuentas cargarSubcuentaDetalleMonto(int codigoResponsable){
		return subCuentasDAO.cargarSubcuentaDetalleMonto(codigoResponsable);
	}
	
}
