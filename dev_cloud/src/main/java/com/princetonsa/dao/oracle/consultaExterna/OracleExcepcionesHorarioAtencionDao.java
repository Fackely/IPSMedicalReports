package com.princetonsa.dao.oracle.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.consultaExterna.ExcepcionesHorarioAtencionDao;
import com.princetonsa.dao.sqlbase.consultaExterna.SqlBaseExcepcionesHorarioAtencionDao;
import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;





public class OracleExcepcionesHorarioAtencionDao implements  ExcepcionesHorarioAtencionDao{

	@Override
	public boolean eliminar(DtoExcepcionesHorarioAtencion listaDtoExcepciones,
			Connection con) {
		
		return SqlBaseExcepcionesHorarioAtencionDao.eliminar(listaDtoExcepciones, con);
	}

	@Override
	public boolean insertar(DtoExcepcionesHorarioAtencion listaDtoExcepciones,
			Connection con) {
		
		return SqlBaseExcepcionesHorarioAtencionDao.insertar(listaDtoExcepciones, con);
	}

	@Override
	public ArrayList<DtoExcepcionesHorarioAtencion> listar(Connection con,
			int codigo_institucion, int centroAtencion, int profesional) {
		
		return SqlBaseExcepcionesHorarioAtencionDao.listar(con, codigo_institucion, centroAtencion, profesional);
	}

	@Override
	public boolean modificar(DtoExcepcionesHorarioAtencion listaDtoExcepciones,
			Connection con) {
		
		return SqlBaseExcepcionesHorarioAtencionDao.modificar(listaDtoExcepciones, con);
	}

	@Override
	public void cargar(Connection con,
			DtoExcepcionesHorarioAtencion listaDtoExcepciones) {
		
		SqlBaseExcepcionesHorarioAtencionDao.cargar(con,listaDtoExcepciones);
	}

	
	
	
	
}