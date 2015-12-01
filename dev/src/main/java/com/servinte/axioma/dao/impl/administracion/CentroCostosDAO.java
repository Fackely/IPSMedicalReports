package com.servinte.axioma.dao.impl.administracion;

import java.util.ArrayList;
import java.util.List;

import org.axioma.util.log.Log4JManager;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.servinte.axioma.dao.interfaz.administracion.ICentroCostosDAO;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.orm.delegate.administracion.CentroCostoGrupoSerDelegate;
import com.servinte.axioma.orm.delegate.administracion.CentroCostosDelegate;


/**
 * @author axioma
 */
public class CentroCostosDAO implements ICentroCostosDAO 
{

	
	private CentroCostosDelegate delegateCentro;
	private CentroCostoGrupoSerDelegate centroCostoGrupoSerDelegate;
	
	
	public CentroCostosDAO()
	{
		delegateCentro = new CentroCostosDelegate();
		centroCostoGrupoSerDelegate = new CentroCostoGrupoSerDelegate();
	}
	
	
	@Override
	public List<DtoCentroCostosVista> busquedaCentroCostos(	DtoCentroCostosVista dto) 
	{
		return delegateCentro.busquedaCentroCostos(dto);
	}

	
	@Override
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorCentroAtencion(
			int consecutivoCentroAttencion, int tipoArea) {
		return delegateCentro.obtenerCentrosCostoPorCentroAtencion(consecutivoCentroAttencion, tipoArea);
	}
	
	
	@Override
	public CentrosCosto findById(int id) {
		return delegateCentro.findById(id);
	}
	
	
	
	@Override
	public DtoCentroCostosVista buscarxId(Number id) {
		// TODO Auto-generated method stub
		Log4JManager.info("este metodo esta retornando siempre null. IMPLEMENTAR");
		return null;
	}

	@Override
	public void eliminar(DtoCentroCostosVista objeto) {
		// TODO Auto-generated method stub
		Log4JManager.info("este metodo esta retornando siempre null. IMPLEMENTAR");
	}

	@Override
	public void insertar(DtoCentroCostosVista objeto) {
		// TODO Auto-generated method stub
		Log4JManager.info("este metodo esta retornando siempre null. IMPLEMENTAR");
	}

	@Override
	public void modificar(DtoCentroCostosVista objeto) {
		// TODO Auto-generated method stub
		Log4JManager.info("este metodo esta retornando siempre null. IMPLEMENTAR");
	}
	
	
	
	@Override
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorViaIngreso(int consecutivoViaIngreso){
		return delegateCentro.obtenerCentrosCostoPorViaIngreso(consecutivoViaIngreso);
	}

	
	@Override
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorInstitucionTipoEntidad(int institucion, String[] tipoentidadesEjecuta) {
		return delegateCentro.obtenerCentrosCostoPorInstitucionTipoEntidad(institucion, tipoentidadesEjecuta);
	}


	@Override
	public List<DtoCentroCostosVista> listaCentroCostoActivoXrEntidadesSub(long codentidadSubcontratada) {
		return delegateCentro.listaCentroCostoActivoXrEntidadesSub(codentidadSubcontratada);
	}


	@Override
	public CentrosCosto obtenerCentrosCostoPorViaIngresoEntSub(DTOEstanciaViaIngCentroCosto parametros) {
		return delegateCentro.obtenerCentrosCostoPorViaIngresoEntSub(parametros);
	}

	
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta de los centros de costo
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return ArrayList
	 * @author Diana Ruiz
	 * @since 23/06/2011
	 * 
	 */	
	@Override
	public ArrayList<DtoCentroCosto> listaCentroCostoGrupoServicio(int grupoServicio, int codCentroAtencion){
		return delegateCentro.listaCentroCostoGrupoServicio(grupoServicio, codCentroAtencion);
	}
	
	/**
	 * 
	 * M&eacute;todo que permite realizar la consulta de los centros de costo por unidad de agenda
	 * para un servicio espec&iacute;fico. 
	 * @param codServicio
	 * @return ArrayList
	 * @author Diana Ruiz
	 * @since 24/06/2011
	 * 
	 */	
	public ArrayList<DtoCentroCosto> listaCentroCostoUnidadConsulta(DtoCentroCosto centroCosto){
		return delegateCentro.listaCentroCostoUnidadConsulta(centroCosto);		
	}


	@Override
	public ArrayList<DtoCentroCosto> obtenerCentrosCostoPorGrupoServicio(Integer grupoServicio) {
		return centroCostoGrupoSerDelegate.obtenerCentrosCostoPorGrupoServicio(grupoServicio);
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
		return delegateCentro.obtenerCentroAtencionXCentroCosto(centroCosto);
	}
	
	
	/**
	 * Metodo que permite listar los centros de costo de la funcionalidad FarmaciasXCentrosCosto para el centro de atencion 
	 * del paciente
	 * @param codigoCentroAtencion
	 * @return
	 * 
	 * @author Diana Ruiz
	 * 
	 */

	@Override
	public ArrayList<DtoCentroCosto> listaCentroCostoSubAlmacenXCentroAtencion(int centroAtencion){
		return delegateCentro.listaCentroCostoSubAlmacenXCentroAtencion(centroAtencion);		
	}


	/*
	 * (non-Javadoc)
	 * @see com.servinte.axioma.dao.interfaz.administracion.ICentroCostosDAO#listaCentroCostoTipoArea(int, boolean)
	 */
	@Override
	public ArrayList<DtoCentroCosto> listaCentroCostoTipoArea(int tipoArea,
			boolean estado) {
		return delegateCentro.listaCentroCostoTipoArea(tipoArea, estado);
	}
	
	
	
}
