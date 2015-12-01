
package com.servinte.axioma.dao.impl.odontologia.unidadagendaserviciotipocitaodonto;

import java.util.List;

import com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto;
import com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoDAO;
import com.servinte.axioma.orm.delegate.odontologia.unidadAgendaServicioTipoCitaOdonto.UnidadAgendaServCitaOdontoDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IUnidadAgendaServCitaOdontoDAO}.
 * 
 * @author Jorge Armando Agudelo Quintero
 * @see UnidadAgendaServCitaOdontoDelegate
 */


public class UnidadAgendaServCitaOdontoHibernateDAO implements IUnidadAgendaServCitaOdontoDAO{

	UnidadAgendaServCitaOdontoDelegate unidadAgendaServCitaOdontoDelegate;
	
	/**
	 * Constructor de la Clase
	 */
	public UnidadAgendaServCitaOdontoHibernateDAO() {
		
		unidadAgendaServCitaOdontoDelegate = new UnidadAgendaServCitaOdontoDelegate();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoDAO#guardarRegistroUnidadAgendaPorServicioCitaOdonto(com.princetonsa.dto.odontologia.DtoUnidadAgendaServCitaOdonto)
	 */
	@Override
	public long guardarRegistroUnidadAgendaPorServicioCitaOdonto(DtoUnidadAgendaServCitaOdonto unidadAgendaServCitaOdonto) {
		
		return unidadAgendaServCitaOdontoDelegate.guardarRegistroUnidadAgendaPorServicioCitaOdonto(unidadAgendaServCitaOdonto);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoDAO#eliminarUnidAgenServCitaOdonRegistrada(int)
	 */
	@Override
	public boolean eliminarUnidAgenServCitaOdonRegistrada(long codigoRegistro) {
		
		return unidadAgendaServCitaOdontoDelegate.eliminarUnidAgenServCitaOdonRegistrada(codigoRegistro);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoDAO#obtenerListadoUnidAgenServCitaOdonRegistrados()
	 */
	@Override
	public List<DtoUnidadAgendaServCitaOdonto> obtenerListadoUnidAgenServCitaOdonRegistrados() {
		
		return unidadAgendaServCitaOdontoDelegate.obtenerListadoUnidAgenServCitaOdonRegistrados();
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.odontologia.unidadAgendaServicioTipoCitaOdonto.IUnidadAgendaServCitaOdontoDAO#consultarParametricaPorTipoCita(java.lang.String)
	 */
	@Override
	public DtoUnidadAgendaServCitaOdonto consultarParametricaPorTipoCita(String acronimoTipoCita) {
		
		return unidadAgendaServCitaOdontoDelegate.consultarParametricaPorTipoCita(acronimoTipoCita);
	}

}
