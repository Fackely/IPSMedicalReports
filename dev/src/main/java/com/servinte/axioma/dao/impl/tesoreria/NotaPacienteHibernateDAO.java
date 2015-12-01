package com.servinte.axioma.dao.impl.tesoreria;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO;
import com.servinte.axioma.dto.tesoreria.DTONotaPaciente;
import com.servinte.axioma.dto.tesoreria.DtoBusquedaNotasPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoConsultaNotasDevolucionAbonosPacientePorRango;
import com.servinte.axioma.dto.tesoreria.DtoResumenNotasPaciente;
import com.servinte.axioma.orm.NotaPaciente;
import com.servinte.axioma.orm.delegate.tesoreria.NotaPacienteDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link INotaPacienteDAO}.
 * 
 * @author diecorqu
 * @see NotaPacienteDelegate	
 */
public class NotaPacienteHibernateDAO implements INotaPacienteDAO {

	NotaPacienteDelegate delegate;
	
	public NotaPacienteHibernateDAO() {
		delegate = new NotaPacienteDelegate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO#guardarNotaPaciente(com.servinte.axioma.orm.NotaPaciente)
	 */
	@Override
	public boolean guardarNotaPaciente(NotaPaciente notaPaciente) {
		return delegate.guardarNotaPaciente(notaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO#eliminarNotaPaciente(com.servinte.axioma.orm.NotaPaciente)
	 */
	@Override
	public boolean eliminarNotaPaciente(NotaPaciente notaPaciente) {
		return eliminarNotaPaciente(notaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO#modificarNotaPaciente(com.servinte.axioma.orm.NotaPaciente)
	 */
	@Override
	public NotaPaciente modificarNotaPaciente(NotaPaciente notaPaciente) {
		return delegate.modificarNotaPaciente(notaPaciente);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO#findById(long)
	 */
	@Override
	public NotaPaciente findById(long codigo) {
		return delegate.findById(codigo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO#obtenerDevolucionAbonoPacienteConsecutivo(int, java.math.BigDecimal, java.lang.String)
	 */
	@Override
	public ArrayList<DTONotaPaciente> obtenerDevolucionAbonoPacienteConsecutivo(
			int codigoPaciente, BigDecimal consecutivo, String naturaleza,
			String parametroManejoEspecialInstiOdontologicas) {
		return delegate.obtenerDevolucionAbonoPacienteConsecutivo(codigoPaciente, consecutivo, naturaleza, 
				parametroManejoEspecialInstiOdontologicas);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO#obtenerDevolucionAbonoPorInstitucion(int)
	 */
	@Override
	public ArrayList<DTONotaPaciente> obtenerDevolucionAbonoPorInstitucion(
			int codigoInstitucion) {
		return delegate.obtenerDevolucionAbonoPorInstitucion(codigoInstitucion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO#buscarNotaDevolucionAbonosPacienteRango(com.servinte.axioma.dto.tesoreria.DtoConsultaNotasDevolucionAbonosPacientePorRango, java.util.ArrayList)
	 */
	@Override
	public ArrayList<DtoConsultaNotasDevolucionAbonosPacientePorRango> buscarNotaDevolucionAbonosPacienteRango(
			DtoConsultaNotasDevolucionAbonosPacientePorRango dtoFiltro,
			ArrayList<Integer> listaConsecutivosCA) {
		return delegate.buscarNotaDevolucionAbonosPacienteRango(dtoFiltro, listaConsecutivosCA);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO#consultarNotasPacientePorRango(com.servinte.axioma.mundo.impl.tesoreria.DtoBusquedaNotasPacientePorRango)
	 */
	@Override
	public ArrayList<DtoResumenNotasPaciente> consultarNotasPacientePorRango(
			DtoBusquedaNotasPacientePorRango dtoBusqueda, 
			boolean esInstitucionMultiempresa,
			boolean controlaAbonoPacientePorNumIngreso) {
		return delegate.consultarNotasPacientePorRango(dtoBusqueda, 
				esInstitucionMultiempresa, controlaAbonoPacientePorNumIngreso);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.tesoreria.INotaPacienteDAO#consultarNotasPacientePorPaciente(long, boolean)
	 */
	@Override
	public ArrayList<DtoResumenNotasPaciente> consultarNotasPacientePorPaciente(
			int codigoPaciente, boolean esMultiInstitucion, 
			boolean controlaAbonoPacientePorNumIngreso) {
		return delegate.consultarNotasPacientePorPaciente(codigoPaciente, esMultiInstitucion, 
				controlaAbonoPacientePorNumIngreso);
	}

}
