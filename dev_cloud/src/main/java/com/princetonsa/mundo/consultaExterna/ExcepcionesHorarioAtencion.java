package com.princetonsa.mundo.consultaExterna;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.administracion.DtoEnvioEmailAutomatico;
import com.princetonsa.dto.consultaExterna.DtoExcepcionesHorarioAtencion;

public class ExcepcionesHorarioAtencion {
	
	
	public static boolean insertar(DtoExcepcionesHorarioAtencion listaDtoExcepciones , Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesHorarioAtencionDao().insertar(listaDtoExcepciones, con);
	}
	
	/**
	 * Listar las excepciones del horario de atención para los parámetros dados
	 * @param con Conexión con la BD
	 * @param codigoInstitucion Código de la institución
	 * @param centroAtencion Código del centro de atención seleccionado
	 * @param profesional Código del profesional seleccionado
	 * @return ArrayList Listado de excepciones de tipo {@link DtoExcepcionesHorarioAtencion}.
	 */
	public static ArrayList<DtoExcepcionesHorarioAtencion> listar(Connection con, int codigoInstitucion, int centroAtencion, int profesional)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesHorarioAtencionDao().listar(con, codigoInstitucion, centroAtencion, profesional);
	}
	
	public static boolean eliminar(DtoExcepcionesHorarioAtencion listaDtoExcepciones , Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesHorarioAtencionDao().eliminar(listaDtoExcepciones, con);
	}
	
	public static boolean modificar (DtoExcepcionesHorarioAtencion listaDtoExcepciones , Connection con)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesHorarioAtencionDao().modificar(listaDtoExcepciones, con);
	}


	public static void cargar(Connection con,
			DtoExcepcionesHorarioAtencion listaDtoExcepciones) {
		
		 DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getExcepcionesHorarioAtencionDao().cargar(con,listaDtoExcepciones);
		
	}
	
	
	
	
	
	
	
	

}
