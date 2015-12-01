package com.servinte.axioma.servicio.interfaz.facturacion.convenio;

import java.util.ArrayList;

import com.princetonsa.dto.facturacion.DtoContrato;
import com.princetonsa.dto.facturacion.DtoEsquemasTarifarios;
import com.servinte.axioma.orm.Contratos;

/**
 * @author Cristhian Murillo
 *
 */
public interface IContratoServicio
{
	
	/**
	 * Valida la vigencia de un contrato
	 * @return true en caso de estar vigente
	 * @author Juan David Ramírez
	 * @since 10 Septiembre 2010
	 */
	public boolean esVigenteContrato(DtoContrato contrato);

	/**
	 * Obtener el esquema tarifario de procedimientos vigente para el contrato
	 * @param contrato código del contrato a consultar
	 * @return DtoEsquemasTarifarios
	 */
	public DtoEsquemasTarifarios obtenerEsquemaTarifarioProcedimientosVigente(int contrato);
	
	
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
}
