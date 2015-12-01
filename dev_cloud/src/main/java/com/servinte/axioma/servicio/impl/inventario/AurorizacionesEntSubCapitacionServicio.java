/**
 * 
 */
package com.servinte.axioma.servicio.impl.inventario;

import java.util.ArrayList;

import com.princetonsa.dto.inventario.DtoArticulosAutorizaciones;
import com.princetonsa.dto.inventario.DtoAutorizacionEntSubcontratadasCapitacion;
import com.princetonsa.dto.ordenes.DtoSolicitud;
import com.servinte.axioma.mundo.fabrica.odontologia.manejopaciente.ManejoPacienteFabricaMundo;
import com.servinte.axioma.mundo.interfaz.manejoPaciente.IAurorizacionesEntSubCapitacionMundo;
import com.servinte.axioma.orm.AutorizacionesEntSubServi;
import com.servinte.axioma.orm.AutorizacionesEntidadesSub;
import com.servinte.axioma.orm.SolicitudesPosponer;
import com.servinte.axioma.servicio.interfaz.inventario.IAurorizacionesEntSubCapitacionServicio;

/**
 * @author Cristhian Murillo
 */
public class AurorizacionesEntSubCapitacionServicio implements IAurorizacionesEntSubCapitacionServicio
{
	
	IAurorizacionesEntSubCapitacionMundo aurorizacionesEntSubCapitacionMundo;
	
	
	/**
	 * Constructor
	 */
	public AurorizacionesEntSubCapitacionServicio() 
	{
		aurorizacionesEntSubCapitacionMundo = ManejoPacienteFabricaMundo.crearAutorizacionesEntidadesSubMundo();
	}


	
	@Override
	public ArrayList<AutorizacionesEntSubServi> listarAutorizacionesEntSubServiPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros) {
		return aurorizacionesEntSubCapitacionMundo.listarAutorizacionesEntSubServiPorAutoEntSub(dtoParametros);
	}


	@Override
	public ArrayList<DtoArticulosAutorizaciones> listarautorizacionesEntSubArticuPorAutoEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoParametros) {
		return aurorizacionesEntSubCapitacionMundo.listarautorizacionesEntSubArticuPorAutoEntSub(dtoParametros);
	}


	@Override
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSub(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas) {
		return aurorizacionesEntSubCapitacionMundo.obtenerAutorizacionesPorEntSub(dtoEntregaMedicamentosInsumosEntSubcontratadas);
	}


	@Override
	public void validarListaArticulos(DtoAutorizacionEntSubcontratadasCapitacion dtoAutorizacionEntSubcontratadasCapitacion) {
		aurorizacionesEntSubCapitacionMundo.validarListaArticulos(dtoAutorizacionEntSubcontratadasCapitacion);
	}



	@Override
	public AutorizacionesEntidadesSub obtenerAutorizacionesEntidadesSubPorId(long id) {
		return aurorizacionesEntSubCapitacionMundo.obtenerAutorizacionesEntidadesSubPorId(id);
	}



	@Override
	public ArrayList<DtoSolicitud> obtenerSolicitudesPorCuentaORango(DtoSolicitud parametros,String codigoEstado) {
		return aurorizacionesEntSubCapitacionMundo.obtenerSolicitudesPorCuentaORango(parametros,codigoEstado);
	}



	@Override
	public void guardarSolicitudesPosponer(SolicitudesPosponer transientInstance) {
		aurorizacionesEntSubCapitacionMundo.guardarSolicitudesPosponer(transientInstance);
	}



	@Override
	public ArrayList<DtoSolicitud> obtenerSolicitudesSubcuenta(DtoSolicitud parametros) {
		return aurorizacionesEntSubCapitacionMundo.obtenerSolicitudesSubcuenta(parametros);
	}


	@Override
	public ArrayList<DtoAutorizacionEntSubcontratadasCapitacion> obtenerAutorizacionesPorEntSubPorNumeroSolicitud(
			DtoAutorizacionEntSubcontratadasCapitacion dtoEntregaMedicamentosInsumosEntSubcontratadas) {
		return aurorizacionesEntSubCapitacionMundo.obtenerAutorizacionesPorEntSubPorNumeroSolicitud(dtoEntregaMedicamentosInsumosEntSubcontratadas);
	}

	/**
	 * @see com.servinte.axioma.servicio.interfaz.inventario.IAurorizacionesEntSubCapitacionServicio#obtenerArticulosSinAutorizar(com.princetonsa.dto.ordenes.DtoSolicitud, java.lang.String)
	 */
	public ArrayList<DtoSolicitud> obtenerArticulosSinAutorizar(DtoSolicitud parametros){
		return  aurorizacionesEntSubCapitacionMundo.obtenerArticulosSinAutorizar(parametros);
	}


}
