
package com.servinte.axioma.servicio.impl.odontologia.unidadagendaserviciotipocitaodonto;

import java.util.List;

import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.mundo.fabrica.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdonMundoFabrica;
import com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaMundo;
import com.servinte.axioma.orm.UnidadesConsulta;
import com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto.IUnidadAgendaServTipoCitaOdontoServicio;

/**
 * Implementaci&oacute;n de la interfaz {@link IUnidadAgendaServTipoCitaOdontoServicio}
 * 
 * @author Jorge Armando Agudelo Quintero
 *
 */
public class UnidadAgendaServTipoCitaOdontoServicio implements IUnidadAgendaServTipoCitaOdontoServicio{

	IUnidadesConsultaMundo unidadesConsultaMundo;
	IUnidadAgendaServCitaOdontoMundo unidadAgendaServCitaOdontoMundo;
	
	/**
	 * Constructor de la clase
	 */
	public UnidadAgendaServTipoCitaOdontoServicio() {
		
		unidadesConsultaMundo = UnidadAgendaServTipoCitaOdonMundoFabrica.crearUnidadesConsultaMundo();
		unidadAgendaServCitaOdontoMundo = UnidadAgendaServTipoCitaOdonMundoFabrica.crearUnidadAgendaServCitaOdontoMundo();
	}
	
	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaServicio#listarUnidadesAgendaPorTipoPorEstado(java.lang.String, boolean)
	 */
	@Override
	public List<UnidadesConsulta> listarUnidadesAgendaPorTipoPorEstado(
			String tipoAtencion, boolean estado) {
	
		return unidadesConsultaMundo.listarUnidadesAgendaPorTipoPorEstado(tipoAtencion, estado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaServicio#obtenerListaServiciosPorUnidadAgenda(int)
	 */
	@Override
	public List<DtoUnidadAgendaServCitaOdonto> listarServiciosPorUnidadAgendaPorTarifarioPorEstado(int codigoUnidadAgenda, int codigoInstitucion, boolean estado) {
	
		return unidadesConsultaMundo.listarServiciosPorUnidadAgendaPorTarifarioPorEstado(codigoUnidadAgenda, codigoInstitucion, estado);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaServicio#guardarRegistroUnidadAgendaPorServicioCitaOdonto(com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto)
	 */
	@Override
	public long guardarRegistroUnidadAgendaPorServicioCitaOdonto(DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto) {
	
		return unidadAgendaServCitaOdontoMundo.guardarRegistroUnidadAgendaPorServicioCitaOdonto(unidadAgendaServCitaOdonto);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaServicio#eliminarUnidAgenServCitaOdonRegistrada(int)
	 */
	@Override
	public boolean eliminarUnidAgenServCitaOdonRegistrada(long codigoRegistro) {
		
		return unidadAgendaServCitaOdontoMundo.eliminarUnidAgenServCitaOdonRegistrada(codigoRegistro);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadesConsultaServicio#obtenerListadoUnidAgenServCitaOdonRegistrados()
	 */
	@Override
	public List<DtoUnidadAgendaServCitaOdonto> obtenerListadoUnidAgenServCitaOdonRegistrados() {
		
		return unidadAgendaServCitaOdontoMundo.obtenerListadoUnidAgenServCitaOdonRegistrados();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.servicio.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoServicio#consultarParametricaPorTipoCita(java.lang.String)
	 */
	@Override
	public DtoUnidadAgendaServCitaOdonto consultarParametricaPorTipoCita(String acronimoTipoCita) {
	
		return unidadAgendaServCitaOdontoMundo.consultarParametricaPorTipoCita(acronimoTipoCita);
	}

}
