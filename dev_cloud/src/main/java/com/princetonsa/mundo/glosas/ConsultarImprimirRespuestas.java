package com.princetonsa.mundo.glosas;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.glosas.ConsultarImprimirRespuestasDao;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.dto.glosas.DtoRespuestaFacturaGlosa;

public class ConsultarImprimirRespuestas
{
	
	//---------------------ATRIBUTOS
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ConsultarImprimirRespuestasDao consultarImprimirRespuestasDao;
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	
	// ----------------	METODOS
	/**
	 * Inicializa el acceso a bases de datos de este objeto, obteniendo su respectivo DAO.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 * @return <b>true</b> si la inicialización fue exitosa, <code>false</code> si no.
	 */
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			consultarImprimirRespuestasDao = myFactory.getConsultarImprimirRespuestasDao();
			wasInited = (consultarImprimirRespuestasDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @param con
	 * @param listadoGlosasMap
	 * @return
	 */
	public static ArrayList<DtoGlosa> listarGlosas(Connection con,HashMap<String, Object> listadoGlosasMap) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarImprimirRespuestasDao().listarGlosas(con, listadoGlosasMap);
	}
	
	/**
	 * Método para consultar las respuestas de las facturas de una glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public static DtoGlosa consultarRespuestaFacturasGlosa(Connection con, DtoGlosa glosa) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarImprimirRespuestasDao().consultarRespuestaFacturasGlosa(con, glosa);
	}
	
	/**
	 * 
	 * @param con
	 * @param dtoRespuestaFactura
	 * @return
	 */
	public static DtoRespuestaFacturaGlosa consultarRespuestaSolicitudesGlosa(Connection con, DtoRespuestaFacturaGlosa dtoRespuestaFactura, int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarImprimirRespuestasDao().consultarRespuestaSolicitudesGlosa(con, dtoRespuestaFactura, institucion);
	}

	/**
	 * @return the institucion
	 */
	public int getInstitucion() {
		return institucion;
	}

	/**
	 * @param institucion the institucion to set
	 */
	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}
	
}