package com.servinte.axioma.mundo.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.servinte.axioma.dao.fabrica.administracion.especialidad.AdministracionDAOFabrica;
import com.servinte.axioma.dao.interfaz.administracion.ICentroCostosDAO;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroCostoMundo;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.persistencia.UtilidadTransaccion;


/**
 * 
 * @author Edgar Carvajal
 *
 */
public class CentroCostoMundo implements ICentroCostoMundo 
{

	private ICentroCostosDAO centroDAO;
	
	
	/**
	 * Constructor
	 */
	public CentroCostoMundo()
	{
		centroDAO= AdministracionDAOFabrica.crearCentroCostoDAO();
	}
	
	
	
	@Override
	public List<DtoCentroCostosVista> busquedaCentroCostos(	DtoCentroCostosVista dto) {
		
		List<DtoCentroCostosVista> listaCentroCost=null;
		
		try{
			UtilidadTransaccion.getTransaccion().begin();
			listaCentroCost=centroDAO.busquedaCentroCostos(dto);
			UtilidadTransaccion.getTransaccion().commit();
		}
		catch (Exception e) 
		{
			UtilidadTransaccion.getTransaccion().rollback();
		}
		return listaCentroCost;
	}



	@Override
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorCentroAtencion(
			int consecutivoCentroAttencion, int tipoArea) {
		return centroDAO.obtenerCentrosCostoPorCentroAtencion(consecutivoCentroAttencion, tipoArea);
	}


	@Override
	public CentrosCosto findById(int id) {
		return centroDAO.findById(id);
	}
	
	
	@Override
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorViaIngreso(int consecutivoViaIngreso ){
		return centroDAO.obtenerCentrosCostoPorViaIngreso(consecutivoViaIngreso);
	}


	@Override
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorInstitucionTipoEntidad(int institucion, String[] tipoentidadesEjecuta) {
		return centroDAO.obtenerCentrosCostoPorInstitucionTipoEntidad(institucion, tipoentidadesEjecuta);
	}


	@Override
	public List<DtoCentroCostosVista> listaCentroCostoActivoXrEntidadesSub(long codentidadSubcontratada) {
		return centroDAO.listaCentroCostoActivoXrEntidadesSub(codentidadSubcontratada);
	}


	@Override
	public CentrosCosto obtenerCentrosCostoPorViaIngresoEntSub(DTOEstanciaViaIngCentroCosto parametros) {
		return centroDAO.obtenerCentrosCostoPorViaIngresoEntSub(parametros);
	}


 
	@Override
	public ArrayList<DtoCentroCosto> listaCentroCostoGrupoServicio(int grupoServicio, int codCentroAtencion) {
		return centroDAO.listaCentroCostoGrupoServicio(grupoServicio, codCentroAtencion);		
	}


	@Override
	public ArrayList<DtoCentroCosto> listaCentroCostoUnidadConsulta(DtoCentroCosto centroCosto) {
		return centroDAO.listaCentroCostoUnidadConsulta(centroCosto);	
	}



	@Override
	public ArrayList<DtoCentroCosto> obtenerCentrosCostoPorGrupoServicio(Integer grupoServicio) {
		return centroDAO.obtenerCentrosCostoPorGrupoServicio(grupoServicio);
	}
	

	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta del centro de Atencion de acuerdo al codigo del centro de costo 
	 *  
	 * @param codigoCentroAtencion
	 * @return ArrayList
	 * @author Camilo Gómez
	 */		
	public DtoCentroCosto obtenerCentroAtencionXCentroCosto(DtoCentroCosto centroCosto){
		return centroDAO.obtenerCentroAtencionXCentroCosto(centroCosto);
	}
}
