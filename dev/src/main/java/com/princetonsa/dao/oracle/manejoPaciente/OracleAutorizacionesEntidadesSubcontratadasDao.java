package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.AutorizacionesEntidadesSubcontratadasDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseAutorizacionesEntidadesSubcontratadasDao;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.odontologia.DtoConvencionesOdontologicas;

public class OracleAutorizacionesEntidadesSubcontratadasDao implements
		AutorizacionesEntidadesSubcontratadasDao {

	@Override
	public ArrayList<DtoSolicitudesSubCuenta> obtenerListadoSolicitudes(int codigoPersona, String tar) {
		
		return SqlBaseAutorizacionesEntidadesSubcontratadasDao.obtenerListadoSolicitudes(codigoPersona, tar);
	}

	@Override
	public String nivelServicio(int servicio) {
		
		return SqlBaseAutorizacionesEntidadesSubcontratadasDao.nivelServicio(servicio);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public HashMap ingresarAutorizacion(Connection con,HashMap parametros,ArrayList<DtoSolicitudesSubCuenta> articulosServicios) {
		
		return SqlBaseAutorizacionesEntidadesSubcontratadasDao.ingresarAutorizacion(con,parametros,articulosServicios);
	}	
	
	@SuppressWarnings("rawtypes")
	@Override
	public HashMap centroCostoRango(int centroAtencion) {
		
		return SqlBaseAutorizacionesEntidadesSubcontratadasDao.centroCostoRango(centroAtencion);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesXRango(
			HashMap parametrosBusqueda, String tar) {
		
		return SqlBaseAutorizacionesEntidadesSubcontratadasDao.obtenerSolicitudesXRango(parametrosBusqueda, tar);
	}

	@Override
	public ArrayList<DtoConvencionesOdontologicas> consultarConvencionesOdontologicas() {
		
		return null;
	}

}
