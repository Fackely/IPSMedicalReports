package com.princetonsa.dao.postgresql.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ConsultarImprimirAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseConsultarImprimirAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;

public class PostgresqlConsultarImprimirAutorizacionesEntSubcontratadasDao
		implements ConsultarImprimirAutorizacionesEntSubcontratadasDao {

	@Override
	public ArrayList<DtoAutorizacionEntSubContratada> listadoAutorizacionesEntSub(int codPaciente, int codInstitucion) {
		
		return SqlBaseConsultarImprimirAutorizacionesEntSubcontratadasDao.listadoAutorizacionesEntSub(codPaciente, codInstitucion);
	}

	@Override
	public HashMap listaEntidadesSubcontratadas() {
		
		return SqlBaseConsultarImprimirAutorizacionesEntSubcontratadasDao.listaEntidadesSubcontratadas();
	}

	@Override
	public ArrayList<DtoAutorizacionEntSubContratada> obtenerAutorizacionesEntSubContrXRango(
			HashMap parametrosBusqueda, int codInstitucion) {
		
		return SqlBaseConsultarImprimirAutorizacionesEntSubcontratadasDao.obtenerAutorizacionesEntSubContrXRango(parametrosBusqueda, codInstitucion);
	}

	
	 
}
