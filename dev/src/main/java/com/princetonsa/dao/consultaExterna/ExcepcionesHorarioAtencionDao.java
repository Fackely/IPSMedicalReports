package com.princetonsa.dao.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoEnvioEmailAutomatico;
import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;

public interface ExcepcionesHorarioAtencionDao {
	
	
	
	public boolean insertar(DtoExcepcionesHorarioAtencion listaDtoExcepciones, Connection con);
	public ArrayList<DtoExcepcionesHorarioAtencion> listar ( Connection con, int codigo_institucion, int centroAtencion, int profesional);
	public boolean eliminar(DtoExcepcionesHorarioAtencion listaDtoExcepciones, Connection con);
	public boolean modificar (DtoExcepcionesHorarioAtencion listaDtoExcepciones, Connection con);
	public void cargar(Connection con,DtoExcepcionesHorarioAtencion listaDtoExcepciones);
	
	
	
}
