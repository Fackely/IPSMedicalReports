package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionArticuloDAO;
import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;
import com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo;
import com.servinte.axioma.orm.DetalleValorizacionArt;

/**
 * Implementaci&oacute;n de la interfaz {@link IDetalleValorizacionArticuloMundo}
 * @author diecorqu
 *
 */
public class DetalleValorizacionArticuloMundo implements
		IDetalleValorizacionArticuloMundo {
	
	IDetalleValorizacionArticuloDAO detalleValorizacionArticuloDAO;
	
	public DetalleValorizacionArticuloMundo() {
		detalleValorizacionArticuloDAO = CapitacionFabricaDAO.crearDetalleValorizacionArticuloDAO();
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo#findById(long)
	 */
	@Override
	public DetalleValorizacionArt findById(long codDetalle) {
		return detalleValorizacionArticuloDAO.findById(codDetalle);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo#detalleValorizacionArticulo(long, long)
	 */
	@Override
	public ArrayList<DetalleValorizacionArt> detalleValorizacionArticulo(
			long codigoParametrizacion, long nivelAtencion) {
		return detalleValorizacionArticuloDAO.detalleValorizacionArticulo(
				codigoParametrizacion, nivelAtencion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo#guardarValorizacionDetalleArticulo(java.util.ArrayList)
	 */
	@Override
	public boolean guardarValorizacionDetalleArticulo(
			ArrayList<DetalleValorizacionArt> detallesValorizacionArticulo) {
		return detalleValorizacionArticuloDAO.guardarValorizacionDetalleArticulo(
				detallesValorizacionArticulo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo#modificarValorizacionDetalleClaseInventario(com.servinte.axioma.orm.DetalleValorizacionArt)
	 */
	@Override
	public DetalleValorizacionArt modificarValorizacionDetalleClaseInventario(
			DetalleValorizacionArt detallesValorizacionClaseInventario) {
		return detalleValorizacionArticuloDAO.modificarValorizacionDetalleClaseInventario(
				detallesValorizacionClaseInventario);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo#eliminarValorizacionDetalleArticulo(int)
	 */
	@Override
	public boolean eliminarValorizacionDetalleArticulo(int codigo) {
		return detalleValorizacionArticuloDAO.eliminarValorizacionDetalleArticulo(codigo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo#existeValorizacionDetalleClaseInventario(long, long)
	 */
	@Override
	public boolean existeValorizacionDetalleClaseInventario(
			long codParametrizacion, long consecutivoNivelAtencion) {
		return detalleValorizacionArticuloDAO.existeValorizacionDetalleClaseInventario(
				codParametrizacion, consecutivoNivelAtencion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo#obtenerValorizacionDetalleClaseInventario(long, long)
	 */
	@Override
	public ArrayList<DetalleValorizacionArt> obtenerValorizacionDetalleClaseInventario(
			long codParametrizacion, long consecutivoNivelAtencion) {
		return detalleValorizacionArticuloDAO.obtenerValorizacionDetalleClaseInventario(
				codParametrizacion, consecutivoNivelAtencion); 
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo#existeValorizacionDetalleClaseInventario(int, java.util.Calendar)
	 */
	@Override
	public boolean existeValorizacionDetalleClaseInventario(int codigoContrato,
			Calendar mesAnio) {
		return detalleValorizacionArticuloDAO.existeValorizacionDetalleClaseInventario(codigoContrato, mesAnio);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionArticuloMundo#obtenerArticulosPresupuestadosPorNivelAtencionPorContrato(int, long, java.util.Calendar)
	 */
	@Override
	public List<DtoProductoServicioReporte> obtenerArticulosPresupuestadosPorNivelAtencionPorContrato(
			int codigoContrato, long consecutivoNivel, Calendar mesAnio) {
		return detalleValorizacionArticuloDAO.
			obtenerArticulosPresupuestadosPorNivelAtencionPorContrato(codigoContrato, consecutivoNivel, mesAnio);
	}

}
