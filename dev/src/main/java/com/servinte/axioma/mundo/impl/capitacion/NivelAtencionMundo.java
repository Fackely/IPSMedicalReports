package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.princetonsa.dto.capitacion.DtoNivelAtencion;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAtencionDAO;
import com.servinte.axioma.dto.capitacion.DtoNivelReporte;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo;
import com.servinte.axioma.orm.NivelAtencion;

public class NivelAtencionMundo implements INivelAtencionMundo{

	INivelAtencionDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabián Becerra
	 */
	public NivelAtencionMundo(){
		dao = CapitacionFabricaDAO.crearNivelAtencionDAO();
	}
	
	
	/**
	 * Este método se encarga de consultar los niveles de atencion activos 
	 * en el sistema 
	 * @param 
	 * @return ArrayList<NivelAtencion>
	 */
	public  ArrayList<NivelAtencion> obtenerNivelesAtencionActivos(){
		return dao.obtenerNivelesAtencionActivos();
	}
	
	/**
	 * Este método obtiene el nivel de atención por articulo por su id
	 * @param id
	 * @return NivelAtencion
	 */
	public NivelAtencion obtenerNivelAtencionPorId(long id) {
		return dao.findById(id);
	}

	/**
	 * Este método se encarga de consultar los niveles de atencion  
	 * en el sistema 
	 * @param 
	 * @return ArrayList<NivelAtencion>
	 */
	public ArrayList<NivelAtencion> obtenerNivelesAtencion() {
		return dao.obtenerNivelesAtencion();
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo#listarNivelesAtencionConParametrizacionPresupuestoPorContrato(int, java.util.Calendar)
	 */
	@Override
	public List<DtoNivelReporte> listarNivelesAtencionConParametrizacionPresupuestoPorContrato(
			int codigoContrato, Calendar mesAnio) {
		return dao.listarNivelesAtencionConParametrizacionPresupuestoPorContrato(
					codigoContrato, mesAnio);
	}


	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionContrato(
			int codContrato) {
		return dao.listarNivelesAtencionContrato(codContrato);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo#listarNivelesAtencionConParametrizacionDetalladaPresupuestoPorContrato(int, java.util.Calendar)
	 */
	@Override
	public List<DtoNivelAtencion> listarNivelesAtencionConParametrizacionDetalladaPresupuestoPorContrato(
			int codigoContrato, Calendar mesAnio) {
		return dao.listarNivelesAtencionConParametrizacionDetalladaPresupuestoPorContrato(codigoContrato, mesAnio);
	}

	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo#listarNivelesAtencionParametrizacionPresupuesto(int)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionParametrizacionPresupuesto(
			long codParametrizacion) {
		return dao.listarNivelesAtencionParametrizacionPresupuesto(codParametrizacion);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo#listarNivelesAtencionServiciosPorConvenio(int, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionServiciosPorConvenio(
			int codigoConvenio, String proceso, List<Calendar> meses) {
		return dao.listarNivelesAtencionServiciosPorConvenio(codigoConvenio, proceso, meses);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo#listarNivelesAtencionServiciosPorContrato(int, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionServiciosPorContrato(
			int codigoContrato, String proceso, List<Calendar> meses) {
		return dao.listarNivelesAtencionServiciosPorContrato(codigoContrato, proceso, meses);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo#listarNivelesAtencionArticulosPorConvenio(int, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionArticulosPorConvenio(
			int codigoConvenio, String proceso, List<Calendar> meses) {
		return dao.listarNivelesAtencionArticulosPorConvenio(codigoConvenio, proceso, meses);
	}


	/* (non-Javadoc)
	 * @see com.servinte.axioma.mundo.interfaz.capitacion.INivelAtencionMundo#listarNivelesAtencionArticulosPorContrato(int, java.lang.String, java.util.List)
	 */
	@Override
	public ArrayList<NivelAtencion> listarNivelesAtencionArticulosPorContrato(
			int codigoContrato, String proceso, List<Calendar> meses) {
		return dao.listarNivelesAtencionArticulosPorContrato(codigoContrato, proceso, meses);
	}

}
