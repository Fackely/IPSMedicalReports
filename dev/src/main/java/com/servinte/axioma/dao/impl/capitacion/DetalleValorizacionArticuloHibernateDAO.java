package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO;
import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;
import com.servinte.axioma.orm.DetalleValorizacionArt;
import com.servinte.axioma.orm.delegate.capitacion.DetalleValorizacionArticuloDelegate;

/**
 * Implementaci&oacute;n de la interfaz {@link IDetalleValorizacionArticuloDAO}
 * @author diecorqu
 * @see DetalleValorizacionArticuloDelegate
 */
public class DetalleValorizacionArticuloHibernateDAO implements
		IDetalleValorizacionArticuloDAO {

	DetalleValorizacionArticuloDelegate delegate;
	
	public DetalleValorizacionArticuloHibernateDAO() {
		delegate = new DetalleValorizacionArticuloDelegate();
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO#findById(long)
	 */
	@Override
	public DetalleValorizacionArt findById(long codDetalle) {
		return delegate.findById(codDetalle);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO#detalleValorizacionArticulo(long, long)
	 */
	@Override
	public ArrayList<DetalleValorizacionArt> detalleValorizacionArticulo(
			long codigoParametrizacion, long nivelAtencion) {
		return delegate.detalleValorizacionArticulo(codigoParametrizacion, nivelAtencion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO#guardarValorizacionDetalleArticulo(java.util.ArrayList)
	 */
	@Override
	public boolean guardarValorizacionDetalleArticulo(
			ArrayList<DetalleValorizacionArt> detallesValorizacionArticulo) {
		return delegate.guardarValorizacionDetalleArticulo(detallesValorizacionArticulo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO#modificarValorizacionDetalleClaseInventario(com.servinte.axioma.orm.DetalleValorizacionArt)
	 */
	@Override
	public DetalleValorizacionArt modificarValorizacionDetalleClaseInventario(
			DetalleValorizacionArt detallesValorizacionClaseInventario) {
		return delegate.modificarValorizacionDetalleClaseInventario(detallesValorizacionClaseInventario);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO#eliminarValorizacionDetalleArticulo(int)
	 */
	@Override
	public boolean eliminarValorizacionDetalleArticulo(int codigo) {
		delegate.eliminarValorizacionDetalleArticulo(codigo);
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO#existeValorizacionDetalleClaseInventario(long, long)
	 */
	@Override
	public boolean existeValorizacionDetalleClaseInventario(
			long codParametrizacion, long consecutivoNivelAtencion) {
		return delegate.existeValorizacionDetalleClaseInventario(
				codParametrizacion, consecutivoNivelAtencion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO#obtenerValorizacionDetalleClaseInventario(long, long)
	 */
	@Override
	public ArrayList<DetalleValorizacionArt> obtenerValorizacionDetalleClaseInventario(
			long codParametrizacion, long consecutivoNivelAtencion) {
		return delegate.obtenerValorizacionDetalleClaseInventario(
				codParametrizacion, consecutivoNivelAtencion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO#existeValorizacionDetalleClaseInventario(long, java.util.Calendar)
	 */
	@Override
	public boolean existeValorizacionDetalleClaseInventario(
			int codigoContrato, Calendar mesAnio) {
		return delegate.existeValorizacionDetalleClaseInventario(codigoContrato, mesAnio);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO#obtenerArticulosPresupuestadosPorNivelAtencionPorContrato(int, long, java.util.Calendar)
	 */
	@Override
	public List<DtoProductoServicioReporte> obtenerArticulosPresupuestadosPorNivelAtencionPorContrato(
			int codigoContrato, long consecutivoNivel, Calendar mesAnio) {
		return delegate.obtenerArticulosPresupuestadosPorNivelAtencionPorContrato(
							codigoContrato, consecutivoNivel, mesAnio);
	}



}
