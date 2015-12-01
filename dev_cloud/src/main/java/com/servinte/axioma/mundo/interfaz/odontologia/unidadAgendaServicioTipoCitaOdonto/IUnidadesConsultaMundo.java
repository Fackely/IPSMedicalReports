
package com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.mundo.impl.odontologia.unidadagendaserviciotipocitaodonto.UnidadesConsultaMundo;
import com.servinte.axioma.orm.UnidadesConsulta;

/**
 * Define la l&oacute;gica de negocio relacionada con las {@link UnidadesConsulta}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see UnidadesConsultaMundo
 */

public interface IUnidadesConsultaMundo {

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
	 * Metodo que cargar  la unidades de consulta por codigoPk
	 * Recibe el codigoPk de unidad de Consulta
	 * @author Edgar Carvajal Ruiz
	 * @param codigoUnidadConsulta
	 * @return
	 */
	public UnidadesConsulta buscarUnidadConsultaId(int codigoUnidadConsulta);


	public ArrayList<DtoUnidadesConsulta> cargarUnidadesConsultaTipoEspecialidad(
			String tipo, int codigoEspecialidad,int codigoCentroAtencion,boolean filtrarActivas);
}
