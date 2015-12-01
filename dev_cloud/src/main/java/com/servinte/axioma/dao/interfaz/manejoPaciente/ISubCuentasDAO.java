package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.orm.SubCuentas;



/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */
public interface ISubCuentasDAO {
	
	
	/**
	 * Lista todas las subcuentas del paciente dado
	 * @param paciente
	 * @return ArrayList<SubCuentas>
	 */
	public ArrayList<SubCuentas> listarCuentasPorPaciente (int paciente);
	
	SubCuentas cargarSubCuenta(int codigoResponsable);

	/**
	 * Carga una subcuenta por su id y su detalle monto
	 * @param id de la subcuenta
	 * @return SubCuentas
	 */
	public SubCuentas cargarSubcuentaDetalleMonto(int codigoResponsable);
	
}