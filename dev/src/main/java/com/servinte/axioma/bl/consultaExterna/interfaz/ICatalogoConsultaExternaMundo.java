package com.servinte.axioma.bl.consultaExterna.interfaz;

import java.util.List;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.dto.consultaExterna.ValorEstandarOrdenadoresDto;
import com.servinte.axioma.fwk.exception.IPSException;





public interface ICatalogoConsultaExternaMundo {
	/**
	 * Servicio que obtiene los tipos de orden segun el filtro de 
	 * busqueda seleccionados por el usuario (Servicios, medicamentos)
	 * @param tipoOrden
	 * @return
	 * @throws IPSException
	 */
	List<ValorEstandarOrdenadoresDto> obtenerParametrica(int tipoOrden) throws IPSException;
	/**
	 * Servicio que obtiene las unidades de agenda
	 * 
	 * @param 
	 * @return
	 * @throws IPSException
	 */

	List<DtoUnidadesConsulta> consultarUnidadAgenda() throws IPSException;
	
	/**
	 * Servicio que guarda parametrica nueva 
	 *
	 * @param valor
	 * @return
	 * @throws IPSException
	 */

	void ingresarOrden(ValorEstandarOrdenadoresDto valor)throws IPSException;
	/**
	 * Servicio que modifica parametrica  
	 *
	 * @param valor
	 * @return
	 * @throws IPSException
	 */
	
	void modificarOrden(ValorEstandarOrdenadoresDto valor)throws IPSException;
	/**
	 * Servicio que elimina parametrica  
	 *
	 * @param valor
	 * @return
	 * @throws IPSException
	 */
	
	void eliminarOrden (ValorEstandarOrdenadoresDto valor )throws IPSException;
	
	/**
	 * Consulta para realizar validacion de parametrica servicios
	 * 
	 * @param codigoParametrica
	 * @param codigoGrupoServicio
	 * @param codigoUnidadAgenda
	 * @return
	 * @throws IPSException
	 */
	
	boolean consultarValidacionServicio (Integer codigoParametrica, int codigoGrupoServicio, int codigoUnidadAgenda)throws IPSException;

	/**
	 * Consulta para realizar validacion de parametrica medicamentos
	 * 
	 * @param codigoParametrica
	 * @param codigoClaseInventario
	 * @param codigoUnidadAgenda
	 * @return
	 * @throws IPSException
	 */
	
	boolean consultarValidacionMedicamento(Integer codigoParametrica, int codigoClaseInventario, int codigoUnidadAgenda)throws IPSException;
	
}