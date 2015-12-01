package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;

import com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO;
import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.orm.ValorizacionPresCapGen;
import com.servinte.axioma.orm.delegate.capitacion.ValorizacionPresupuestoCapGeneralDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IValorizacionPresupuestoCapDAO}
 * @author diecorqu
 * @see ValorizacionPresupuestoCapGeneralDelegate
 */
public class ValorizacionPresupuestoCapGeneralHibernateDAO implements
		IValorizacionPresupuestoCapDAO {

	ValorizacionPresupuestoCapGeneralDelegate delegate;
	
	public ValorizacionPresupuestoCapGeneralHibernateDAO() {
		delegate = new ValorizacionPresupuestoCapGeneralDelegate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO#findById(long)
	 */
	@Override
	public ValorizacionPresCapGen findById(long codValorizacion) {
		return delegate.findById(codValorizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO#valoracionPresupuestoCap(long)
	 */
	@Override
	public ArrayList<ValorizacionPresCapGen> valoracionPresupuestoCap(long codigoParametrizacion) {
		return delegate.valoracionPresupuestoCap(codigoParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO#guardarValorizacionPresupuestoCapitado(java.util.ArrayList)
	 */
	@Override
	public boolean guardarValorizacionPresupuestoCapitado(
			ArrayList<ValorizacionPresCapGen> valoracionPresupuesto) {
		return delegate.guardarValorizacionPresupuestoCapitado(valoracionPresupuesto);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO#modificarValorizacionPresupuestoCapitado(com.servinte.axioma.orm.ValorizacionPresCapGen)
	 */
	@Override
	public ValorizacionPresCapGen modificarValorizacionPresupuestoCapitado(
			ValorizacionPresCapGen parametrizacionPresupuesto) {
		return delegate.modificarValorizacionPresupuestoCapitado(parametrizacionPresupuesto);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO#eliminarValorizacionPresupuestoCapitado(com.servinte.axioma.orm.ValorizacionPresCapGen)
	 */
	@Override
	public void eliminarValorizacionPresupuestoCapitado(ValorizacionPresCapGen valoracionGeneral) {
		delegate.eliminarValorizacionPresupuestoCapitado(valoracionGeneral);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO#obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(long, long, int, java.lang.String)
	 */
	@Override
	public ValorizacionPresCapGen obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(
			long codigoParametrizacionGeneral, long consecutivoNivel, int mes,
			String grupoClase) {
		return delegate.obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(
				codigoParametrizacionGeneral, consecutivoNivel, mes, grupoClase);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO#obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(int, long, java.util.Calendar, java.lang.String)
	 */
	@Override
	public DtoTotalProceso obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(
			int codigoContrato, long consecutivoNivel, Calendar mesAnio,
			String grupoClase) {
		return delegate.obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(
							codigoContrato, consecutivoNivel, mesAnio, grupoClase);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO#existeValorizacionGeneralxNivelAtencionSubSeccion(long, long, java.lang.String)
	 */
	@Override
	public boolean existeValorizacionGeneralxNivelAtencionSubSeccion(
			long codigoParametrizacionGeneral, long consecutivoNivel,
			String grupoClase) {
		return delegate.existeValorizacionGeneralxNivelAtencionSubSeccion(
				codigoParametrizacionGeneral, consecutivoNivel, grupoClase);
	}

}
