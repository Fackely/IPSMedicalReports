package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.servinte.axioma.orm.Cuentas;



/**
 * Interfaz donde se define el comportamiento del DAO
 * 
 * @author Cristhian Murillo
 *
 */
public interface ICuentasDAO {
	
	
	/**
	 * Cuentas de todas las vías de ingreso.
	 * @param viaIngreso
	 * @return ArrayList<Cuentas>
	 */
	public ArrayList<Cuentas> verificarCuentasPorIngreso (int viaIngreso);
	
	
	/**
	 * Cuentas con estado abierto por paciente
	 * @param codCuenta
	 * @return rrayList<Cuentas>
	 */
	public ArrayList<Cuentas> verificarEstadoCuenta (int codCuenta);
	
	
	/**
	 * Retorna los tipos de estado por su actividad asignada
	 * @param listaEstadosCuenta
	 * @param codPaciente
	 * @return  ArrayList<Cuentas>
	 */
	public ArrayList<Cuentas> listarTodosXEstadoCuenta(String[] listaEstadosCuenta, int codPaciente);
	
	/**
	 * Retorna la cuenta por el codigo de la persona
	 * @param codPersona
	 * @return  Cuentas
	 */
	public Cuentas obtenerCuentaPorCodigoPersona(int codPersona);
}