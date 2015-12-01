package com.servinte.axioma.mundo.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoInformacionBasicaIngresoPaciente;
import com.servinte.axioma.orm.Cuentas;


/**
 * Define la l&oacute;gica de negocio relacionada con los ingresos
 * 
 * @author Cristhian Murillo
 * @see com.servinte.axioma.mundo.impl.tesoreria.CuentasMundo
 */
public interface ICuentasMundo {
	
	
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
	 * Crea un objeto de tipo Cuentas
	 * @param dtoInfoBasica
	 * @return Cuentas
	 */
	public Cuentas crearCuentas(DtoInformacionBasicaIngresoPaciente dtoInfoBasica);
	
	/**
	 * Retorna la cuenta por el codigo de la persona
	 * @param codPersona
	 * @return  Cuentas
	 */
	public Cuentas obtenerCuentaPorCodigoPersona(int codPersona);
	
}
