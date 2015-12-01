package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO;
import com.servinte.axioma.dto.capitacion.DtoNivelesAtencionPresupuestoParametrizacionGeneral;
import com.servinte.axioma.dto.capitacion.DtoParamPresupCap;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.ParamPresupuestosCap;
import com.servinte.axioma.orm.delegate.capitacion.ParametrizacionPresupuestoCapDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IParametrizacionPresupuestosCapDAO}.
 * 
 * @author diecorqu
 * @see ParametrizacionPresupuestoCapDelegate	
 */
public class ParametrizacionPresupuestoCapHibernateDAO implements
		IParametrizacionPresupuestosCapDAO {

	private ParametrizacionPresupuestoCapDelegate delegate; 
	
	public ParametrizacionPresupuestoCapHibernateDAO() {
		delegate = new ParametrizacionPresupuestoCapDelegate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#obtenerParametrizacionPresupuestoCapitado(int, java.lang.String)
	 */
	@Override
	public ParamPresupuestosCap obtenerParametrizacionPresupuestoCapitado(
			int codContrato, String anioVigencia) {
		return delegate.obtenerParametrizacionPresupuestoCapitado(codContrato, anioVigencia);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#findById(int)
	 */
	@Override
	public ParamPresupuestosCap findById(long codParametrizacion) {
		return delegate.findById(codParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#listarParametrizacionesPresupuestoCap()
	 */
	@Override
	public ArrayList<ParamPresupuestosCap> listarParametrizacionesPresupuestoCap() {
		return delegate.listarParametrizacionesPresupuestoCap();
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#listarParametrizacionesPresupuestoCapxContrato(int)
	 */
	@Override
	public ArrayList<DtoParamPresupCap> listarParametrizacionesPresupuestoCapxContrato(
			int codContrato) {
		return delegate.listarParametrizacionesPresupuestoCapxContrato(codContrato);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#existeParametrizacionPresupuesto(int, java.lang.String)
	 */
	@Override
	public boolean existeParametrizacionPresupuesto(int codContrato,
			String anioVigencia) {
		return delegate.existeParametrizacionPresupuesto(codContrato, anioVigencia);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#guardarParametrizacionPresupuesto(com.servinte.axioma.orm.ParamPresupuestosCap)
	 */
	@Override
	public boolean guardarParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacionPresupuesto) {
		return delegate.guardarParametrizacionPresupuesto(parametrizacionPresupuesto);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#modificarParametrizacionPresupuesto(com.servinte.axioma.orm.ParamPresupuestosCap)
	 */
	@Override
	public ParamPresupuestosCap modificarParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacionPresupuesto) {
		return delegate.modificarParametrizacionPresupuesto(parametrizacionPresupuesto);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#obtenerNivelesAtencionPresupuestoParametrizacionGen(java.util.ArrayList)
	 */
	@Override
	public ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> obtenerNivelesAtencionPresupuestoParametrizacionGen(
			ArrayList<NivelAtencion> listaNivelesContrato) {
		return delegate.obtenerNivelesAtencionPresupuestoParametrizacionGen(listaNivelesContrato);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#eliminarParametrizacionPresupuesto(com.servinte.axioma.orm.ParamPresupuestosCap)
	 */
	@Override
	public void eliminarParametrizacionPresupuesto(
			ParamPresupuestosCap parametrizacion) {
		delegate.eliminarParametrizacionPresupuesto(parametrizacion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#existeParametrizacionPresupuestoConvenio(int, java.lang.String)
	 */
	@Override
	public boolean existeParametrizacionPresupuestoConvenio(int codConvenio,
			String anioVigencia) {
		return delegate.existeParametrizacionPresupuestoConvenio(codConvenio, anioVigencia);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#obtenerNivelesAtencionPresupuestoParametrizacionExistentes(java.util.ArrayList, long)
	 */
	@Override
	public ArrayList<DtoNivelesAtencionPresupuestoParametrizacionGeneral> obtenerNivelesAtencionPresupuestoParametrizacionExistentes(
			ArrayList<NivelAtencion> listaNivelesContrato,
			long codigoParametrizacion) {
		return delegate.obtenerNivelesAtencionPresupuestoParametrizacionExistentes(listaNivelesContrato, codigoParametrizacion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#existeNivelAtencionPresupuestoCapitacion(long)
	 */
	@Override
	public boolean existeNivelAtencionPresupuestoCapitacion(
			long codNivelAtencion) {
		return delegate.existeNivelAtencionPresupuestoCapitacion(codNivelAtencion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#obtenerValorParametrizacionPresupuestoDetalladoServicios(int, java.lang.String, int, int, long)
	 */
	@Override
	public Double obtenerValorParametrizacionPresupuestoDetalladoServicios(
			int codContrato, String anioVigencia, int mes,
			int codigoGrupoServicio, long consecutivoNivelAtencion) {
		return delegate.obtenerValorParametrizacionPresupuestoDetalladoServicios(codContrato, 
							anioVigencia, mes, codigoGrupoServicio, consecutivoNivelAtencion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(int, java.lang.String, int, long, java.lang.String)
	 */
	@Override
	public Double obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(
			int codContrato, String anioVigencia, int mes,
			long consecutivoNivelAtencion, String servicioArticulo) {
		return delegate.obtenerValorParametrizacionPresupuestoGeneralServiciosArticulos(codContrato, 
							anioVigencia, mes, consecutivoNivelAtencion, servicioArticulo);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IParametrizacionPresupuestosCapDAO#obtenerValorParametrizacionPresupuestoDetalladoArticulos(int, java.lang.String, int, int, long)
	 */
	@Override
	public Double obtenerValorParametrizacionPresupuestoDetalladoArticulos(
			int codContrato, String anioVigencia, int mes,
			int codigoClaseInventario, long consecutivoNivelAtencion) {
		return delegate.obtenerValorParametrizacionPresupuestoDetalladoArticulos(codContrato, 
							anioVigencia, mes, codigoClaseInventario, consecutivoNivelAtencion);
	}

}
