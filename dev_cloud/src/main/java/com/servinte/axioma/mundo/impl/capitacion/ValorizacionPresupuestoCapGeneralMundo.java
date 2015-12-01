package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IValorizacionPresupuestoCapDAO;
import com.servinte.axioma.dto.capitacion.DtoTotalProceso;
import com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo;
import com.servinte.axioma.orm.ValorizacionPresCapGen;

/**
 * Implementaci&oacute;n de la interfaz {@link IValorizacionPresupuestoCapGeneralMundo}
 * @author diecorqu
 * @see 
 */
public class ValorizacionPresupuestoCapGeneralMundo implements
		IValorizacionPresupuestoCapGeneralMundo {

	
	private IValorizacionPresupuestoCapDAO valorizacionPresupuestoCapDAO;
		
	public ValorizacionPresupuestoCapGeneralMundo() {
		valorizacionPresupuestoCapDAO = 
				CapitacionFabricaDAO.crearValorizacionPresupuestoCapDAO();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo#findById(long)
	 */
	@Override
	public ValorizacionPresCapGen findById(long codValorizacion) {
		return valorizacionPresupuestoCapDAO.findById(codValorizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo#valoracionPresupuestoCap(long)
	 */
	@Override
	public ArrayList<ValorizacionPresCapGen> valoracionPresupuestoCap(
			long codigoParametrizacion) {
		return valorizacionPresupuestoCapDAO.valoracionPresupuestoCap(codigoParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo#guardarValorizacionPresupuestoCapitado(java.util.ArrayList)
	 */
	@Override
	public boolean guardarValorizacionPresupuestoCapitado(
			ArrayList<ValorizacionPresCapGen> valoracionPresupuesto) {
		return valorizacionPresupuestoCapDAO.guardarValorizacionPresupuestoCapitado(valoracionPresupuesto);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo#modificarValorizacionPresupuestoCapitado(com.servinte.axioma.orm.ValorizacionPresCapGen)
	 */
	@Override
	public ValorizacionPresCapGen modificarValorizacionPresupuestoCapitado(
			ValorizacionPresCapGen parametrizacionPresupuesto) {
		return valorizacionPresupuestoCapDAO.modificarValorizacionPresupuestoCapitado(parametrizacionPresupuesto);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo#eliminarValorizacionPresupuestoCapitado(com.servinte.axioma.orm.ValorizacionPresCapGen)
	 */
	@Override
	public void eliminarValorizacionPresupuestoCapitado(ValorizacionPresCapGen valorizacionGeneral) {
		valorizacionPresupuestoCapDAO.eliminarValorizacionPresupuestoCapitado(valorizacionGeneral);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo#obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(long, long, int, java.lang.String)
	 */
	@Override
	public ValorizacionPresCapGen obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(
			long codigoParametrizacionGeneral, long consecutivoNivel, int mes,
			String grupoClase) {
		return valorizacionPresupuestoCapDAO.obtenerValorizacionGeneralxNivelAtencionSubSeccionMes(
				codigoParametrizacionGeneral, consecutivoNivel, mes, grupoClase);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo#obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(int, long, java.util.Calendar, java.lang.String)
	 */
	@Override
	public DtoTotalProceso obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(
			int codigoContrato, long consecutivoNivel, Calendar mesAnio,
			String grupoClase) {
		return valorizacionPresupuestoCapDAO.obtenerPresupuestoGrupoClasePorNivelAtencionPorContrato(
												codigoContrato, consecutivoNivel, mesAnio, grupoClase);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IValorizacionPresupuestoCapGeneralMundo#existeValorizacionGeneralxNivelAtencionSubSeccion(long, long, java.lang.String)
	 */
	@Override
	public boolean existeValorizacionGeneralxNivelAtencionSubSeccion(
			long codigoParametrizacionGeneral, long consecutivoNivel,
			String grupoClase) {
		valorizacionPresupuestoCapDAO.existeValorizacionGeneralxNivelAtencionSubSeccion(
				codigoParametrizacionGeneral, consecutivoNivel, grupoClase);
		return false;
	}

}
