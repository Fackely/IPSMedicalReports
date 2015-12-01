package com.princetonsa.dao.postgresql.enfermeria;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.enfermeria.ConsultaProgramacionCuidadosAreaDao;
import com.princetonsa.dao.sqlbase.enfermeria.SqlBaseConsultaProgramacionCuidadosAreaDao;
import com.princetonsa.dto.enfermeria.DtoCuidadosEnfermeria;
import com.princetonsa.dto.enfermeria.DtoFrecuenciaCuidadoEnferia;

public class PostgresqlConsultaProgramacionCuidadosAreaDao implements
		ConsultaProgramacionCuidadosAreaDao 
{
	
	
  /**
   * 
   */
	@Override
	public HashMap<String, Object> listaAreas(Connection con, int centroAtencion) {
		
		return SqlBaseConsultaProgramacionCuidadosAreaDao.listarAreas(con,centroAtencion);
	}
	
	@Override
	/**
	 * 
	 */
	public HashMap<String, Object> listaHabitaciones(Connection con, int piso) {
		
		return SqlBaseConsultaProgramacionCuidadosAreaDao.listarHabitaciones(con,piso);
	}
	/**
	 * 
	 */
	@Override
	public HashMap<String, Object> listaPisos(Connection con, int centroAtencion) {
		
		return SqlBaseConsultaProgramacionCuidadosAreaDao.listarPisos(con,centroAtencion);
	}
    /**
     * 
     */
	@Override
	public HashMap consultarListadoPacientes(Connection con, String areaFiltro, String pisoFiltro, String habitacionFiltro, String fechaProg, String horaProg) 
	{
		return SqlBaseConsultaProgramacionCuidadosAreaDao.listarPacientes(con, areaFiltro, pisoFiltro, habitacionFiltro, fechaProg, horaProg);
	}
    /** 
     * 
     */
	@Override
	public ArrayList<DtoFrecuenciaCuidadoEnferia> consultarFrecuenciaCuidado(Connection con, HashMap parametros) {
		
		return SqlBaseConsultaProgramacionCuidadosAreaDao.consultarFrecuenciaCuidado(con, parametros);
	}
    /**
     * 
     */
	@Override
	public ArrayList<HashMap<String, Object>> consultarTipoFrecuenciaInst(Connection con, HashMap parametros) {
		
		return SqlBaseConsultaProgramacionCuidadosAreaDao.consultarTipoFrecuenciaInst(con,parametros);
	}

	@Override
	public String cadenaConsultaCuidados(Connection con, HashMap parametros) {
		
		return SqlBaseConsultaProgramacionCuidadosAreaDao.cadenaConsultaCuidados(con, parametros);
	}


	

}
