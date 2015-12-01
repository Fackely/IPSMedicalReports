package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.capitacion.DtoNivelAtencion;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO;
import com.servinte.axioma.dto.capitacion.DtoNivelReporte;
import com.servinte.axioma.orm.NivelAtencion;
import com.servinte.axioma.orm.delegate.capitacion.NivelAtencionDelegate;

public class NivelAtencionHibernateDAO implements INivelAtencionDAO{
	
	NivelAtencionDelegate delegate;
	
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public NivelAtencionHibernateDAO(){
		delegate = new NivelAtencionDelegate();
	}
	

	@Override
	public NivelAtencion findById(long id) {
		return delegate.findById(id);
	}


	@Override
	public ArrayList<NivelAtencion> obtenerNivelesAtencion() {
		return delegate.obtenerNivelesAtencion();
	}
	
	/**
	 * Este método se encarga de consultar los niveles de atencion activos 
	 * en el sistema 
	 * @param 
	 * @return ArrayList<NivelAtencion>
	 */
	public  ArrayList<NivelAtencion> obtenerNivelesAtencionActivos(){
		return delegate.obtenerNivelesAtencionActivos();
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO#listarNivelesAtencionConParametrizacionPresupuestoPorContrato(int, java.util.Calendar)
	 */
	@Override
	public List<DtoNivelReporte> listarNivelesAtencionConParametrizacionPresupuestoPorContrato(
			int codigoContrato, Calendar mesAnio) {
		return delegate.listarNivelesAtencionConParametrizacionPresupuestoPorContrato(
				codigoContrato, mesAnio);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO#listarNivelesAtencionContrato(int)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionContrato(
			int codContrato) {
		return delegate.listarNivelesAtencionContrato(codContrato);
	}
	
	


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO#listarNivelesAtencionConParametrizacionDetalladaPresupuestoPorContrato(int, java.util.Calendar)
	 */
	@Override
	public List<DtoNivelAtencion> listarNivelesAtencionConParametrizacionDetalladaPresupuestoPorContrato(
			int codigoContrato, Calendar mesAnio) {
		return delegate.listarNivelesAtencionConParametrizacionDetalladaPresupuestoPorContrato(codigoContrato, mesAnio);
	}


	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO#listarNivelesAtencionParametrizacionPresupuesto(int)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionParametrizacionPresupuesto(
			long codParametrizacion) {
		return delegate.listarNivelesAtencionParametrizacionPresupuesto(codParametrizacion);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO#listarNivelesAtencionServiciosPorConvenio(int, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionServiciosPorConvenio(
			int codigoConvenio, String proceso, List<Calendar> meses) {
		return delegate.listarNivelesAtencionServiciosPorConvenio(codigoConvenio, proceso, meses);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO#listarNivelesAtencionServiciosPorContrato(int, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionServiciosPorContrato(
			int codigoContrato, String proceso, List<Calendar> meses) {
		return delegate.listarNivelesAtencionServiciosPorContrato(codigoContrato, proceso, meses);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO#listarNivelesAtencionArticulosPorConvenio(int, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionArticulosPorConvenio(
			int codigoConvenio, String proceso, List<Calendar> meses) {
		return delegate.listarNivelesAtencionArticulosPorConvenio(codigoConvenio, proceso, meses);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO#listarNivelesAtencionArticulosPorContrato(int, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionArticulosPorContrato(
			int codigoContrato, String proceso, List<Calendar> meses) {
		return delegate.listarNivelesAtencionArticulosPorContrato(codigoContrato, proceso, meses);
	}

}
