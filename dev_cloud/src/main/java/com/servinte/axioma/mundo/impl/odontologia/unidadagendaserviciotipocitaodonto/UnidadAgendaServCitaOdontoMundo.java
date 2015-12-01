
package com.servinte.axioma.mundo.impl.odontologia.unidadagendaserviciotipocitaodonto;

import java.util.List;

import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.dao.fabrica.odontologia.unidadagendaserviciotipocitaodonto.UnidadAgendaServTipoCitaOdonDAOFabrica;
import com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoDAO;
import com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoMundo;
import com.servinte.axioma.orm.UnidadAgendaServCitaOdonto;

/**
 * Define la l&oacute;gica de negocio relacionada con los objetos {@link UnidadAgendaServCitaOdonto}
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see IUnidadAgendaServCitaOdontoMundo
 */

public class UnidadAgendaServCitaOdontoMundo implements IUnidadAgendaServCitaOdontoMundo{

	
	IUnidadAgendaServCitaOdontoDAO unidadAgendaServCitaOdontoDAO;
	
	/**
	 * Constructor de la clase
	 */
	public UnidadAgendaServCitaOdontoMundo() {
		inicializar();
	}
	
	/**
	 * M&eacute;todo que se encarga de inicializar el objeto DAO encargado de manejar 
	 * la capa de integraci&oacute;n de los objetos {@link UnidadAgendaServCitaOdonto}
	 */
	private void inicializar() {
		unidadAgendaServCitaOdontoDAO = UnidadAgendaServTipoCitaOdonDAOFabrica.crearUnidadAgendaServCitaOdontoDAO();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoMundo#guardarRegistroUnidadAgendaPorServicioCitaOdonto(com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto)
	 */
	@Override
	public long guardarRegistroUnidadAgendaPorServicioCitaOdonto(DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto) {
	
		return unidadAgendaServCitaOdontoDAO.guardarRegistroUnidadAgendaPorServicioCitaOdonto(unidadAgendaServCitaOdonto);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoMundo#eliminarUnidAgenServCitaOdonRegistrada(int)
	 */
	@Override
	public boolean eliminarUnidAgenServCitaOdonRegistrada(long codigoRegistro) {
		
		return unidadAgendaServCitaOdontoDAO.eliminarUnidAgenServCitaOdonRegistrada(codigoRegistro);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoMundo#obtenerListadoUnidAgenServCitaOdonRegistrados()
	 */
	@Override
	public List<DtoUnidadAgendaServCitaOdonto> obtenerListadoUnidAgenServCitaOdonRegistrados() {
		
		return unidadAgendaServCitaOdontoDAO.obtenerListadoUnidAgenServCitaOdonRegistrados();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoMundo#consultarParametricaPorTipoCita(java.lang.String)
	 */
	@Override
	public DtoUnidadAgendaServCitaOdonto consultarParametricaPorTipoCita(String acronimoTipoCita) {
		
		return unidadAgendaServCitaOdontoDAO.consultarParametricaPorTipoCita(acronimoTipoCita);
	}
	
}
