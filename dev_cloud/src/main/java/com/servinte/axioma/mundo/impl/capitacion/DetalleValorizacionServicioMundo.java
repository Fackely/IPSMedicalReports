package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.IDetalleValorizacionServicioDAO;
import com.servinte.axioma.dto.capitacion.DtoProductoServicioReporte;
import com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo;
import com.servinte.axioma.orm.DetalleValorizacionServ;

/**
 * Implementaci&oacute;n de la interfaz {@link IDetalleValorizacionServicioMundo}
 * @author diecorqu
 * @see IDetalleValorizacionServicioMundo
 */
public class DetalleValorizacionServicioMundo implements
		IDetalleValorizacionServicioMundo {
	
	IDetalleValorizacionServicioDAO detalleValorizacionServicioDAO; 
	
	public DetalleValorizacionServicioMundo() {
		detalleValorizacionServicioDAO = CapitacionFabricaDAO.crearDetalleValorizacionServicioDAO();
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo#findById(int)
	 */
	@Override
	public DetalleValorizacionServ findById(int codDetalle) {
		return detalleValorizacionServicioDAO.findById(codDetalle);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo#detalleValorizacionServicio(long, long)
	 */
	@Override
	public ArrayList<DetalleValorizacionServ> detalleValorizacionServicio(
			long codigoParametrizacion, long nivelAtencion) {
		return detalleValorizacionServicioDAO.detalleValorizacionServicio(
				codigoParametrizacion, nivelAtencion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo#guardarValorizacionDetalleServicio(java.util.ArrayList)
	 */
	@Override
	public boolean guardarValorizacionDetalleServicio(
			ArrayList<DetalleValorizacionServ> detallesValorizacionServicio) {
		return detalleValorizacionServicioDAO.guardarValorizacionDetalleServicio(
				detallesValorizacionServicio);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo#modificarValorizacionDetalleServicio(com.servinte.axioma.orm.DetalleValorizacionServ)
	 */
	@Override
	public DetalleValorizacionServ modificarValorizacionDetalleServicio(
			DetalleValorizacionServ detallesValorizacionServicio) {
		return detalleValorizacionServicioDAO.modificarValorizacionDetalleServicio(
				detallesValorizacionServicio);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo#eliminarValorizacionDetalleServicio(int)
	 */
	@Override
	public boolean eliminarValorizacionDetalleServicio(int codigo) {
		return detalleValorizacionServicioDAO.eliminarValorizacionDetalleServicio(codigo);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo#existeValorizacionDetalleGrupoServicio(long, long)
	 */
	@Override
	public boolean existeValorizacionDetalleGrupoServicio(
			long codParametrizacion, long consecutivoNivelAtencion) {
		return detalleValorizacionServicioDAO.existeValorizacionDetalleGrupoServicio(
				codParametrizacion, consecutivoNivelAtencion);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo#obtenerValorizacionDetalleGrupoServicio(long, long)
	 */
	@Override
	public ArrayList<DetalleValorizacionServ> obtenerValorizacionDetalleGrupoServicio(
			long codParametrizacion, long consecutivoNivelAtencion) {
		return detalleValorizacionServicioDAO.obtenerValorizacionDetalleGrupoServicio(
				codParametrizacion, consecutivoNivelAtencion);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo#existeValorizacionDetalleGrupoServicio(long, java.util.Calendar)
	 */
	@Override
	public boolean existeValorizacionDetalleGrupoServicio(int codigoContrato,
			Calendar mesAnio) {
		return detalleValorizacionServicioDAO.existeValorizacionDetalleGrupoServicio(codigoContrato, mesAnio);
	}

	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.IDetalleValorizacionServicioMundo#obtenerServiciosPresupuestadosPorNivelAtencionPorContrato(int, long, java.util.Calendar)
	 */
	@Override
	public List<DtoProductoServicioReporte> obtenerServiciosPresupuestadosPorNivelAtencionPorContrato(
			int codigoContrato, long consecutivoNivel, Calendar mesAnio) {
		return detalleValorizacionServicioDAO.obtenerServiciosPresupuestadosPorNivelAtencionPorContrato(
												codigoContrato, consecutivoNivel, mesAnio);
	}

}
