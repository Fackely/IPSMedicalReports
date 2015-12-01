package com.princetonsa.dao.oracle.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.manejoPaciente.ProrrogarAnularAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseProrrogarAnularAutorizacionesEntSubcontratadasDao;
import com.princetonsa.dto.manejoPaciente.DtoAutorizacionEntSubContratada;

public class OracleProrrogarAnularAutorizacionesEntSubcontratadasDao implements
		ProrrogarAnularAutorizacionesEntSubcontratadasDao {

/**
 * 
 */
	public ArrayList<DtoAutorizacionEntSubContratada> listadoAutorizacionesEntSub(int codPaciente, int codInstitucion) {
		
		return SqlBaseProrrogarAnularAutorizacionesEntSubcontratadasDao.listadoAutorizacionesEntSub(codPaciente, codInstitucion);
	}
    
	/**
	 * 
	 */
	public HashMap guardarProrroga(Connection con, HashMap parametrosProrroga) {
		
		return SqlBaseProrrogarAnularAutorizacionesEntSubcontratadasDao.guardarProrroga(con, parametrosProrroga);
	}

	/**
	 * 
	 */
	public HashMap guardarAnulacion(Connection con, HashMap parametrosAnulacion) {
		
		return SqlBaseProrrogarAnularAutorizacionesEntSubcontratadasDao.guardarAnulacion(con, parametrosAnulacion);
	}

	@Override
	public ArrayList<DtoAutorizacionEntSubContratada> obtenerAutorizacionesEntSubContrXRango(
			HashMap parametrosBusqueda, int codigoInstitucionInt) {
		
		return SqlBaseProrrogarAnularAutorizacionesEntSubcontratadasDao.obtenerAutorizacionesEntSubContrXRango(parametrosBusqueda, codigoInstitucionInt);
	}

}
