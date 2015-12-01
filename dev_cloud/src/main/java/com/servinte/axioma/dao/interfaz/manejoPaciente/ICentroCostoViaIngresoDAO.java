package com.servinte.axioma.dao.interfaz.manejoPaciente;

import java.util.ArrayList;

import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostoViaIngreso;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de Centros de costo por 
 * via de via ingreso
 * @author Diana Carolina G
 */
public interface ICentroCostoViaIngresoDAO 
{
	
	/**
	 * M&eacute;todo encargado de obtener los centros de costo por 
	 * v&iacute;a de ingreso existentes en el sistema
	 * @param institucion
	 * @return ArrayList<DtoCentroCostoViaIngreso>
	 * @author Diana Carolina G
	 */
	public ArrayList<DtoCentroCostoViaIngreso> obtenerCentrosCostoViaIngreso(int institucion);
	
	
	/**
	 * M&eacute;todo encargado de obtener los centros de costo por 
	 * v&iacute;a de ingreso
	 * @author Camilo Gomez
	 * @param viaIngreso
	 * @return ArrayList<DtoCentroCostoViaIngreso>
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<DtoCentroCostoViaIngreso> listarCentrosCostoXViaIngreso(int viaIngreso);
}
