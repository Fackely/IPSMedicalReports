package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.IndicativoSolicitudGrupoServiciosDao;

public class IndicativoSolicitudGrupoServicios
{
	
	private IndicativoSolicitudGrupoServiciosDao objetoDao;
	
	/**
	 * 
	 *
	 */
	public IndicativoSolicitudGrupoServicios()
	{
		this.reset();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * 
	 * @param property
	 */
	private void init(String tipoBD)
	{
		if (objetoDao == null) 
		{
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao = myFactory.getIndicativoSolicitudGrupoServiciosDao();
		}
	}
	
	/**
	 * 
	 *
	 */
	private void reset()
	{
	
	}
	
	/**
	 * Metodo para consultar los grupos de servicios de una institucion, dado el tipo.
	 * en caso de que se requieran todos los tipos de la institucion, se puede enviar el tipo="". 
	 * @param con
	 * @param institucion
	 * @param tipo
	 * @return
	 */
	public HashMap consultarGruposServiciosInstitucion(Connection con,String institucion,String tipo)
	{
		return objetoDao.consultarGruposServiciosInstitucion(con,institucion,tipo);
	}
	
	/**
	 * Metotodo para consultar los servicios, de un grupo de servicio, específico.
	 * Se puede enviar el tipo de servicio específico, en caso de que se requieran todos los servicios,
	 * independientes del tipo, entonces, enviar tipo="";
	 * @param con
	 * @param grupoServicio
	 * @param tipoServicio
	 * @return
	 */
	public HashMap consultarServiciosGrupoServicioTipo(Connection con,String grupoServicio,String tipoServicio)
	{
		return objetoDao.consultarServiciosGrupoServicioTipo(con,grupoServicio,tipoServicio);
	}
	
	/**
	 * Metotodo para actualizar los atributos, toma_muestra, y respuesta_multiple, de un servicio, dado su condigo.
	 * @param con
	 * @param codigo
	 * @param tomaMuestra
	 * @param respuestaMultiple
	 * @return
	 */
	public boolean actualizarServicioProcedimiento(Connection con,String codigoServicio,String tomaMuestra,String respuestaMultiple)
	{
		return objetoDao.actualizarServicioProcedimiento(con,codigoServicio,tomaMuestra,respuestaMultiple);
	}
}
