package com.servinte.axioma.servicio.impl.administracion;

import java.util.List;

import com.princetonsa.dto.facturacion.DTOEstanciaViaIngCentroCosto;
import com.princetonsa.dto.manejoPaciente.DtoCentroCostosVista;
import com.servinte.axioma.mundo.fabrica.odontologia.administracion.AdministracionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.administracion.ICentroCostoMundo;
import com.servinte.axioma.orm.CentrosCosto;
import com.servinte.axioma.servicio.interfaz.administracion.ICentroCostosServicio;


/**
 * @author Cristhian Murillo
 */
public class CentroCostosServicio implements ICentroCostosServicio {


	ICentroCostoMundo mundo;
	

	public CentroCostosServicio() 
	{
		mundo = AdministracionFabricaMundo.crearCentroCostoMundo();
	}
	
	
	@Override
	public List<DtoCentroCostosVista> busquedaCentroCostos(
			DtoCentroCostosVista dto) {
		return mundo.busquedaCentroCostos(dto);
	}

	
	@Override
	public CentrosCosto findById(int id) {
		return mundo.findById(id);
	}
	
	
	@Override
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorViaIngreso(int consecutivoViaIngreso ){
		return mundo.obtenerCentrosCostoPorViaIngreso(consecutivoViaIngreso);
	}


	@Override
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorCentroAtencion(int consecutivoCentroAttencion, int tipoArea) {
		return mundo.obtenerCentrosCostoPorCentroAtencion(consecutivoCentroAttencion, tipoArea);
	}


	@Override
	public List<DtoCentroCostosVista> obtenerCentrosCostoPorInstitucionTipoEntidad(int institucion, String[] tipoentidadesEjecuta) {
		return mundo.obtenerCentrosCostoPorInstitucionTipoEntidad(institucion, tipoentidadesEjecuta);
	}


	@Override
	public List<DtoCentroCostosVista> listaCentroCostoActivoXrEntidadesSub(long codentidadSubcontratada) {
		return mundo.listaCentroCostoActivoXrEntidadesSub(codentidadSubcontratada);
	}


	@Override
	public CentrosCosto obtenerCentrosCostoPorViaIngresoEntSub(DTOEstanciaViaIngCentroCosto parametros) {
		return mundo.obtenerCentrosCostoPorViaIngresoEntSub(parametros);
	}
	

}
