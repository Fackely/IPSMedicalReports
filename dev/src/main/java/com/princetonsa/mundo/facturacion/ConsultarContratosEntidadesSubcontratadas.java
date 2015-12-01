package com.princetonsa.mundo.facturacion;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ConsultarContratosEntidadesSubcontratadasDao;
import com.princetonsa.dto.facturacion.DtoContratoEntidadSub;

public class ConsultarContratosEntidadesSubcontratadas
{
	/**
	 * Para manejo de Logs
	 */
	private static Logger logger = Logger.getLogger(ConsultarContratosEntidadesSubcontratadas.class);
	
	/**
	 * Interfaz para acceder y comunicarse con la fuente de datos
	 */
	private static ConsultarContratosEntidadesSubcontratadasDao consultarContratosEntidadesSubcontratadasDao;
	
	/**
	 * Codigo de la institucion
	 */
	private int institucion;
	
	private void reset() 
	{
		this.institucion = ConstantesBD.codigoNuncaValido;
	}
	
	public boolean init(String tipoBD)
	{
		boolean wasInited = false;
		DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		if (myFactory != null)
		{
			consultarContratosEntidadesSubcontratadasDao = myFactory.getConsultarContratosEntidadesSubcontratadasDao();
			wasInited = (consultarContratosEntidadesSubcontratadasDao != null);
		}
		return wasInited;
	}
	
	/**
	 * 
	 * @return
	 */
	private static ConsultarContratosEntidadesSubcontratadasDao getConsultarContratosEntidadesSubcontratadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarContratosEntidadesSubcontratadasDao();
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEntidades(Connection con)
	{
		return getConsultarContratosEntidadesSubcontratadasDao().obtenerEntidades(con);
	}

	/**
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerClaseInventarios(Connection con)
	{
		return getConsultarContratosEntidadesSubcontratadasDao().obtenerClaseInventarios(con);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEsquemas(Connection con)
	{
		return getConsultarContratosEntidadesSubcontratadasDao().obtenerEsquemas(con);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerGruposServicio(Connection con)
	{
		return getConsultarContratosEntidadesSubcontratadasDao().obtenerGruposServicio(con);
	}
	
	/**
	 * @param con
	 * @return
	 */
	public static ArrayList obtenerEsquemasProcedimientos(Connection con)
	{
		return getConsultarContratosEntidadesSubcontratadasDao().obtenerEsquemasProcedimientos(con);
	}
	
	public static HashMap consultaContratos(Connection con, HashMap encabezado, HashMap inventarios, HashMap servicios)
	{
		return getConsultarContratosEntidadesSubcontratadasDao().consultaContratos(con,encabezado,inventarios,servicios);
	}
	
	public static HashMap consultarEsquemasInventarios(Connection con)
	{
		return getConsultarContratosEntidadesSubcontratadasDao().consultaEsquemasInventarios(con);
	}
	
	public static HashMap consultarEsquemasServicios(Connection con)
	{
		return getConsultarContratosEntidadesSubcontratadasDao().consultaEsquemasServicios(con);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el tipo de de tarifa 
	 * manejado en el contrato de una entidad subcontratada, a través
	 * de la entidad subcontratada y el número de contrato
	 * 
	 * @param Connection con, DtoContratoEntidadSub dto
	 * @return DtoContratoEntidadSub
	 * @author, Angela Maria Aguirre
	 *
	 */
	public static DtoContratoEntidadSub consultarTipoTarifaEntidadSubcontratada(Connection con, DtoContratoEntidadSub dto){
		return getConsultarContratosEntidadesSubcontratadasDao().consultarTipoTarifaEntidadSubcontratada(con, dto);
	}
}