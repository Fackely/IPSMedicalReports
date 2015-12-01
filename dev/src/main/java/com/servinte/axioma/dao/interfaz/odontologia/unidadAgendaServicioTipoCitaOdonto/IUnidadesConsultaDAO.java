
package com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.orm.UnidadesConsulta;

/**
 * Interfaz donde se define el comportamiento de los
 * DAO's para las funciones de {@link UnidadesConsulta}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public interface IUnidadesConsultaDAO {

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
	 * @param codigoTarifario
	 * @param estado
	 * @return List<{@link DtoUnidadAgendaServCitaOdonto}> listado con los Servicios asociados a una Unidad de Agenda espec&iacute;fica.
	 */
	public List<DtoUnidadAgendaServCitaOdonto> listarServiciosPorUnidadAgendaPorTarifarioPorEstado (int codigoUnidadAgenda, int codigoTarifario, boolean estado);
	
	
	/**
	 * M&eacute;todo que retorna un listado con los Servicios asociados a una Unidad de Agenda espec&iacute;fica
	 * pasada como par&aacute;metro y teniendo en cuenta el estado.
	 * 
	 * @param codigoUnidadAgenda
	 * @param estado
	 * @return List<{@link DtoUnidadAgendaServCitaOdonto}> listado con los Servicios asociados a una Unidad de Agenda espec&iacute;fica.
	 */
	public List<DtoUnidadAgendaServCitaOdonto> listarServiciosPorUnidadAgendaPorEstado (int codigoUnidadAgenda, boolean estado);
	
	
	/**
	 * M&eacute;todo que retorna un listado de Servicios con la informaci&oacute;n de la descripci&oacute;n 
	 * y el c&oacute;digo propietario por cada uno de los servicios asociados a un tipo tarifario espec&iacute;fico
	 * y a una Unidad de Agenda.
	 * 
	 * @param codigoUnidadAgenda
	 * @param codigoTarifario
	 * @param codigosServicio
	 * @return List<{@link DtoUnidadAgendaServCitaOdonto}> listado de Servicios con informaci&oacute;n espec&iacute;fica de c&oacute;digo y descripci&oacute;n
	 */
	public List<DtoUnidadAgendaServCitaOdonto> obtenerInformacionServiciosPorUnidadAgendaPorTipoTarifario (int codigoUnidadAgenda, int codigoTarifario, ArrayList<Integer> codigosServicio);
		
	/**
	 * Metodo que cargar  la unidades de consulta por codigoPk
	 * Recibe el codigoPk de unidad de Consulta
	 * @author Edgar Carvajal Ruiz
	 * @param codigoUnidadConsulta
	 * @return
	 */
	public UnidadesConsulta buscarUnidadConsultaId(int codigoUnidadConsulta);
	
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta de las unidades de consulta
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return List
	 * @author Diana Ruiz
	 * @since 23/06/2011
	 * 
	 */
	
	public List<DtoUnidadesConsulta> listaUnidadesConsulta(int codServicio);
	
	/**
	 * 
	 * Lista todas las unidades de Consulta
	 * @return ArrayList
	 * @author Cesar Gomez
	 * @since 02/03/2012
	 * 
	 */
	
	public ArrayList<DtoUnidadesConsulta> listaTodoUnidadesConsulta();
	
}
