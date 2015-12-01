package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;
import com.princetonsa.dto.odontologia.DtoConvencionesOdontologicas;

public interface AutorizacionesEntidadesSubcontratadasDao {

	ArrayList<DtoSolicitudesSubCuenta> obtenerListadoSolicitudes(int codigoPersona, String tar);

	String nivelServicio(int servicio);

	HashMap ingresarAutorizacion(Connection con,HashMap parametros,ArrayList<DtoSolicitudesSubCuenta> articulosServicios);

	HashMap centroCostoRango(int centroAtencion);

	ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesXRango(HashMap parametrosBusqueda, String tar);

	ArrayList<DtoConvencionesOdontologicas> consultarConvencionesOdontologicas();
		
}
