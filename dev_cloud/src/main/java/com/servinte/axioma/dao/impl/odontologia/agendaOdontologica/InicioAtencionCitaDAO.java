package com.servinte.axioma.dao.impl.odontologia.agendaOdontologica;

import com.servinte.axioma.dao.interfaz.odontologia.agendaOdontologica.IInicioAtencionCitaDAO;
import com.servinte.axioma.orm.InicioAtencionCita;
import com.servinte.axioma.orm.delegate.odontologia.InicioAtencionCitaDelegate;

public class InicioAtencionCitaDAO implements IInicioAtencionCitaDAO{

	private InicioAtencionCitaDelegate delegate;
	public InicioAtencionCitaDAO() {
		delegate = new InicioAtencionCitaDelegate();
	}
	
	@Override
	public boolean guardarInicioAtencionCitaOdonto(InicioAtencionCita iac) {
		return delegate.guardarInicioAtencionCitaOdonto(iac);
	}
	
	@Override
	public boolean actualizarInicioAtencionCitaOdonto(InicioAtencionCita iac) {
		return delegate.actualizarInicioAtencionCitaOdonto(iac);
	}
	
	@Override
	public InicioAtencionCita buscarRegistroInicioAtencionCitaOdontoPorID(
			long codigoCita) {
		
		return delegate.buscarRegistroInicioAtencionCitaOdontoPorID(codigoCita);
	}
}
