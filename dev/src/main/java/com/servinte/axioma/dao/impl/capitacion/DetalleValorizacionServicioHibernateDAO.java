package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO;
import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;
import com.servinte.axioma.orm.DetalleValorizacionServ;
import com.servinte.axioma.orm.delegate.capitacion.DetalleValorizacionServicioDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IDetalleValorizacionServicioDAO}
 * @author diecorqu
 * @see DetalleValorizacionServicioDelegate
 */
public class DetalleValorizacionServicioHibernateDAO implements
		IDetalleValorizacionServicioDAO {

	DetalleValorizacionServicioDelegate delegate;
	
	public DetalleValorizacionServicioHibernateDAO() {
		delegate = new DetalleValorizacionServicioDelegate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO#findById(long)
	 */
	@Override
	public DetalleValorizacionServ findById(long codDetalle) {
		return delegate.findById(codDetalle);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO#detalleValorizacionServicio(long, long)
	 */
	@Override
	public ArrayList<DetalleValorizacionServ> detalleValorizacionServicio(
			long codigoParametrizacion, long nivelAtencion) {
		return delegate.detalleValorizacionServicio(codigoParametrizacion, nivelAtencion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO#guardarValorizacionDetalleServicio(java.util.ArrayList)
	 */
	@Override
	public boolean guardarValorizacionDetalleServicio(
			ArrayList<DetalleValorizacionServ> detallesValorizacionServicio) {
		return delegate.guardarValorizacionDetalleServicio(detallesValorizacionServicio);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO#modificarValorizacionDetalleServicio(com.servinte.axioma.orm.DetalleValorizacionServ)
	 */
	@Override
	public DetalleValorizacionServ modificarValorizacionDetalleServicio(
			DetalleValorizacionServ detallesValorizacionServicio) {
		return delegate.modificarValorizacionDetalleServicio(detallesValorizacionServicio);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO#eliminarValorizacionDetalleServicio(int)
	 */
	@Override
	public boolean eliminarValorizacionDetalleServicio(int codigo) {
		return delegate.eliminarValorizacionDetalleServicio(codigo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO#existeValorizacionDetalleGrupoServicio(long, long)
	 */
	@Override
	public boolean existeValorizacionDetalleGrupoServicio(
			long codParametrizacion, long consecutivoNivelAtencion) {
		return delegate.existeValorizacionDetalleGrupoServicio(
				codParametrizacion, consecutivoNivelAtencion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO#obtenerValorizacionDetalleGrupoServicio(long, long)
	 */
	@Override
	public ArrayList<DetalleValorizacionServ> obtenerValorizacionDetalleGrupoServicio(
			long codParametrizacion, long consecutivoNivelAtencion) {
		return delegate.obtenerValorizacionDetalleGrupoServicio(
				codParametrizacion, consecutivoNivelAtencion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO#existeValorizacionDetalleGrupoServicio(long, java.util.Calendar)
	 */
	@Override
	public boolean existeValorizacionDetalleGrupoServicio(int codigoContrato,
			Calendar mesAnio) {
		return delegate.existeValorizacionDetalleGrupoServicio(codigoContrato, mesAnio);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO#obtenerServiciosPresupuestadosPorNivelAtencionPorContrato(int, long, java.util.Calendar)
	 */
	@Override
	public List<DtoProductoServicioReporte> obtenerServiciosPresupuestadosPorNivelAtencionPorContrato(
			int codigoContrato, long consecutivoNivel, Calendar mesAnio) {
		return delegate.obtenerServiciosPresupuestadosPorNivelAtencionPorContrato(
							codigoContrato, consecutivoNivel, mesAnio);
	}

}
