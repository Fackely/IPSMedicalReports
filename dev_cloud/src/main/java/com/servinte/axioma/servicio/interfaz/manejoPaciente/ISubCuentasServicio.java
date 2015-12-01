package com.servinte.axioma.servicio.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.servinte.axioma.orm.SubCuentas;


public interface ISubCuentasServicio {
	

	/**
	 * Crea un objeto de tipo SubCuentas
	 * @param dtoInfoBasica
	 * @return SubCuentas
	 */
	public SubCuentas crearSubCuentas(DtoInformacionBasicaIngresoPaciente dtoInfoBasica);

	/**
	 * 
	 * @param codigoResponsable
	 * @return
	 */
	public SubCuentas cargarSubCuenta(int codigoResponsable);
	
	
	
	/**
	 * Lista todas las subcuentas del paciente dado
	 * @param paciente
	 * @return ArrayList<SubCuentas>
	 */
	public ArrayList<SubCuentas> listarCuentasPorPaciente (int paciente);
	
	/**
	 * Carga una subcuenta por su id y su detalle monto
	 * @param id de la subcuenta
	 * @return SubCuentas
	 */
	public SubCuentas cargarSubcuentaDetalleMonto(int codigoResponsable);
	
}
