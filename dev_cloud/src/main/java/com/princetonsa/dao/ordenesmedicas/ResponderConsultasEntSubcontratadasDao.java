package com.princetonsa.dao.ordenesmedicas;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;

public interface ResponderConsultasEntSubcontratadasDao {

	ArrayList<DtoSolicitudesSubCuenta> obtenerListadoSolicitudes(int codigoPersona, int entidadSubcontratada, String tar, String especialidadesProf);

	ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesXRango(HashMap parametrosBusqueda, String tar);

	
	
}
