
package com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto;

import java.util.List;

import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.orm.UnidadAgendaServCitaOdonto;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de {@link UnidadAgendaServCitaOdonto}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IUnidadAgendaServCitaOdontoDAO {

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
