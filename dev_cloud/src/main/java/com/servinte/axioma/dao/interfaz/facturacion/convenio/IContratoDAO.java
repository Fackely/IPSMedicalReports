package com.servinte.axioma.dao.interfaz.facturacion.convenio;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoEsquemasTarifarios;
import com.servinte.axioma.orm.Contratos;

/**
 * Interfaz manejo de contratos
 * @author Juan David Ramírez
 * @since 10 Septiembre 2010
 */
public interface IContratoDAO
{

	/**
	 * Valida la vigencia de un contrato
	 * @return true en caso de estar vigente
	 * @author Juan David Ramírez
	 * @since 10 Septiembre 2010
	 */
	boolean esVigenteContrato(DtoContrato contrato);

	
	/**
	 * Obtener el esquema tarifario de procedimientos vigente para el contrato
	 * @param contrato código del contrato a consultar
	 * @return DtoEsquemasTarifarios
	 */
	public DtoEsquemasTarifarios obtenerEsquemaTarifarioProcedimientosVigente(int contrato);
	
	
	/**
	 * Carga el contrato por su id
	 * @param id
	 * @return Contratos
	 */
	public Contratos findById(int id);
	
	
	/**
	 * Lista los contratos asociados al convenio
	 * 
	 * @param convenio
	 * @return ArrayList<Contratos>
	 */
	public ArrayList<Contratos> listarContratosPorConvenio(int convenio);
	
	
	/**
	 * Lista los contratos vigentes asociados al convenio
	 * 
	 * @param convenio
	 * @return ArrayList<Contratos>
	 */
	public ArrayList<Contratos> listarContratosVigentesPorConvenio(int convenio);
	
	
	/**
	 * Lista todos los contratos vigentes asociados al convenio
	 * 
	 * @param convenio
	 * @return ArrayList<Contratos>
	 * @author diecorqu
	 */
	public ArrayList<Contratos> listarTodosContratosVigentesPorConvenio(int convenio);
	
	
	/**
	 * Lista los Contratos del sistema
	 * 
	 * @author Cristhian Murillo
	 * @return ArrayList<Contratos>
	 */
	public ArrayList<Contratos> listarContratos(int codInstitucion);
	
	/**
	 * Lista los contratos asociados al convenio cuyo 
	 * campo de fecha sea menor a determinada fecha
	 * 
	 * @param fechaComparacion
	 * @param campoComparacion
	 * @param convenio
	 * @return ArrayList<Contratos>
	 */
	public ArrayList<Contratos> listarContratosPorConvenioPorFechaMenor(int convenio, 
									String campoComparacion, Date fechaComparacion);
	
	/**
	 * Lista los contratos a un convenio y que tengan una
	 * parametrización de presupuesto para un mes determinado. 
	 * 
	 * @author Ricardo Ruiz
	 * @param codigoConvenio
	 * @param mesAnio
	 * @return ArrayList<Convenios>
	*/
	public ArrayList<Contratos> listarContratosConParametrizacionPresupuestoPorConvenio(int codigoConvenio, Calendar mesAnio);

}
