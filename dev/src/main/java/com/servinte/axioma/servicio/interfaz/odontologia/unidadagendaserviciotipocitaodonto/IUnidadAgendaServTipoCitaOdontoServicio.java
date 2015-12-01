
package com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto;

import java.util.List;

import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.orm.UnidadesConsulta;

/**
 * Servicio que le delega al negocio las operaciones relacionados con las {@link UnidadesConsulta}
 * 
 * @author Jorge Armando Agudelo Quintero
 * 
 * @see com.servinte.axioma.servicio.impl.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdontoServicio
 */

public interface IUnidadAgendaServTipoCitaOdontoServicio {

	/**
	 * M&eacute;todo que lista las Unidades de Agenda de un tipo y un estado espec&iacute;fico
	 * pasado como par&aacute;metro
	 * 
	 * @param tipoAtencion
	 * @param estado
	 * @return {@link List} con las Unidades de Agenda de un tipo y estado espec&iacute;fico
	 */
	public List<UnidadesConsulta> listarUnidadesAgendaPorTipoPorEstado (String tipoAtencion, boolean estado);
	
	
	/**
	 * M&eacute;todo que retorna un listado con los Servicios asociados a una Unidad de Agenda espec&iacute;fica
	 * pasada como par&aacute;metro y teniendo en cuenta el estado. Tambi&eacute;n se involucra en la b&uacute;queda el 
	 * c&oacute;digo del tipo Tarifario definido de acuerdo al par&aacute;metro General del m&oacute;dulo de 
	 * administraci&oacute;n - C&oacute;digo Manual Est&aacute;ndar B&uacute;squeda y Presentaci&oacute;n de Servicios.
	 * 
	 * @param codigoUnidadAgenda}
	 * @param codigoInstitucion
	 * @param estado
	 * @return List<{@link DtoUnidadAgendaServCitaOdonto}> listado con los Servicios asociados a una Unidad de Agenda espec&iacute;fica.
	 */
	public List<DtoUnidadAgendaServCitaOdonto> listarServiciosPorUnidadAgendaPorTarifarioPorEstado (int codigoUnidadAgenda, int codigoInstitucion, boolean estado);
	
	/**
	 * M&eacute;todo que se encarga de realizar el registro de Unidad de Agenda - Servicio por Tipo de Cita Odontol&oacute;gica
	 * 
	 * @param unidadAgendaServCitaOdonto
	 * @return boolean indicando si el registro se realiz&oacute; satisfactoriamente 
	 */
	public long guardarRegistroUnidadAgendaPorServicioCitaOdonto(DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto);
	
	/**
	 * M&eacute;todo que obtiene un listado con las Unidades de Agenda - Servicio por Tipo Cita Odontol&oacute;ca
	 * que ya fueron registradas en el sistema
	 * 
	 * @param forma
	 * @return List<DtoUnidadAgendaServCitaOdonto>
	 */
	public List<DtoUnidadAgendaServCitaOdonto> obtenerListadoUnidAgenServCitaOdonRegistrados();
	
	/**
	 * M&eacute;todo que elimina un registro de Unidades de Agenda - Servicio por Tipo Cita Odontol&oacute;ca
	 * 
	 * @param codigoRegistro
	 * @return {@link Boolean} indicando si se realiz&oacute; la eliminaci&oacute; registro
	 */
	public boolean eliminarUnidAgenServCitaOdonRegistrada(long codigoRegistro);
	
	/**
	 * M&eacute;todo que se encarga de consultar si se encuentra parametrizada la Unidad de Agenda 
	 * - Servicio por un tipo de cita espec&iacute;fico.
	 * 
	 * @param acronimoTipoCita
	 * @return DtoUnidadAgendaServCitaOdonto con la informacion de la parametrizaci&oacute;n
	 */
	public DtoUnidadAgendaServCitaOdonto consultarParametricaPorTipoCita (String acronimoTipoCita);
	
}
