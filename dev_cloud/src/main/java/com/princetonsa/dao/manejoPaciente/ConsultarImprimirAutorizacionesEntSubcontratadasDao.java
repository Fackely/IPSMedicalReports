package com.princetonsa.dao.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;

public interface ConsultarImprimirAutorizacionesEntSubcontratadasDao {

	ArrayList<DtoAutorizacionEntSubContratada> listadoAutorizacionesEntSub(int codPaciente, int codInstitucion);

	HashMap listaEntidadesSubcontratadas();

	ArrayList<DtoAutorizacionEntSubContratada> obtenerAutorizacionesEntSubContrXRango(HashMap parametrosBusqueda, int codInstitucion);

	
	
	
	
	
}
