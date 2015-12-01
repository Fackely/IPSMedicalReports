package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;

public interface ProrrogarAnularAutorizacionesEntSubcontratadasDao {

	ArrayList<DtoAutorizacionEntSubContratada> listadoAutorizacionesEntSub(int codPaciente, int codInstitucion);

	HashMap guardarProrroga(Connection con,HashMap parametrosProrroga);

	HashMap guardarAnulacion(Connection con, HashMap parametrosAnulacion);

	ArrayList<DtoAutorizacionEntSubContratada> obtenerAutorizacionesEntSubContrXRango(HashMap parametrosBusqueda, int codigoInstitucionInt);

}
