package com.princetonsa.mundo.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;
import util.UtilidadBD;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoAgrupHonorariosPool;
import com.princetonsa.dto.odontologia.DtoHonorarioPoolServicio;
import com.princetonsa.dto.odontologia.DtoHonorariosPool;


/**
 * Anexo961
 * @author axioma
 *cargarAgrupacionHonorariosPool
 */
public class HonoriariosPoolEsquemaTarifario 
{
	/**
	 * 
	 */
	public static Logger logger = Logger.getLogger(HonoriariosPoolEsquemaTarifario.class);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardarHonorariosPool(final DtoHonorariosPool dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().guardarHonorariosPool(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static ArrayList<DtoHonorariosPool> cargarHonorariosPool( final DtoHonorariosPool dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().cargarHonorariosPool( dto);
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	private static double guardarHonoriarioPoolServicio( Connection con, final DtoHonorarioPoolServicio dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().guardarHonoriarioPoolServicio(con , dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  static  ArrayList<DtoHonorarioPoolServicio> cargarHonorariosPoolServ( final DtoHonorarioPoolServicio dto, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().cargarHonorariosPoolServ(dto, institucion);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	private  static  double guardarGruposHonorarioPool(Connection con, final DtoAgrupHonorariosPool dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().guardarGruposHonorarioPool(con , dto);
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  ArrayList<DtoAgrupHonorariosPool> cargarAgrupacionHonorariosPool( DtoAgrupHonorariosPool dto){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().cargarAgrupacionHonorariosPool(dto);
	}
	
	
	/**
	 * 
	 * @param errores
	 * @param dto
	 * @return
	 */
	public static ActionErrors guardarAgrupacion(ActionErrors errores, DtoHonorariosPool dto )
	{
		logger.info("////////////////////////////////////////////////////////////////////////////////");
		logger.info("	GUARDANDO AGRUPACION DE HONORARIOS	");
		Connection con = UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		/**
		 * GUARDAR  AGRUPACION POOLES
		 */
		if( dto.getCodigoPk().doubleValue()>0)
		{
			logger.info("\n\n\n	EXISTE AGRUPACIONES POOLES ");
			dto.getDtoAgrupacionPool().setHonorarioPool(dto.getCodigoPk());
			if(guardarGruposHonorarioPool(con, dto.getDtoAgrupacionPool())<=0)
			{
				  UtilidadBD.abortarTransaccion(con);
				  UtilidadBD.closeConnection(con);
				  errores.add("", new ActionMessage("errors.notEspecific", " "));
			}
		}
		
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return errores;
	}
	
	/**
	 * 
	 * @param errores
	 * @param dto
	 * @return
	 */
	public static ActionErrors guardarPoolServicios(ActionErrors errores,DtoHonorariosPool dto)
	{
		Connection con = UtilidadBD.abrirConexion();
		UtilidadBD.iniciarTransaccion(con);
		
		if(dto.getCodigoPk().doubleValue()>0)
		{
			logger.info(" \n\n\n\n\n\n EXISTE HONORARIO");
			dto.getDtoHonorarioPoolServicio().setHonorarioPool(dto.getCodigoPk());
			if( guardarHonoriarioPoolServicio(con, dto.getDtoHonorarioPoolServicio())<=0 )
			{
				UtilidadBD.finalizarTransaccion(con);
				UtilidadBD.closeConnection(con);
			}
		}
		UtilidadBD.finalizarTransaccion(con);
		UtilidadBD.closeConnection(con);
		return errores;
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean modicarAgrupacionServicios(DtoAgrupHonorariosPool dto){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().modicarAgrupacionServicios(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean eliminarAgrupacionHonorario(DtoAgrupHonorariosPool dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().eliminarAgrupacionHonorario(dto);
	}
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  boolean eliminarHonorariosPoolServicio(DtoHonorarioPoolServicio dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().eliminarHonorariosPoolServicio(dto);
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	
	public static  boolean modicarHonorarioPoolServicios(DtoHonorarioPoolServicio dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().modicarHonorarioPoolServicios(dto);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean modificarHonorarioPool(DtoHonorariosPool dto) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().modificarHonorarioPool(dto);
	}
	
	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public static boolean eliminarHonorarioPool (BigDecimal codigoPk)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().eliminarHonorarioPool(codigoPk);
	}

	/**
	 * 
	 * @param dtoHonorariosPool
	 * @return
	 */
	public static boolean existenDetalles(BigDecimal codigoPkHonorarioPool) 
	{
		boolean retorna=false;
		DtoAgrupHonorariosPool dtoAgru= new DtoAgrupHonorariosPool();
		dtoAgru.setHonorarioPool(codigoPkHonorarioPool);
		retorna=cargarAgrupacionHonorariosPool(dtoAgru).size()>0;
		
		if(!retorna)
		{
			DtoHonorarioPoolServicio dtoServ= new DtoHonorarioPoolServicio();
			dtoServ.setHonorarioPool(codigoPkHonorarioPool);
			retorna= cargarHonorariosPoolServ(dtoServ, ConstantesBD.codigoNuncaValido).size()>0;
		}
		return retorna;
	}
	
	/**
	 * 
	 * @param pool
	 * @param convenioEXCLUYENTE
	 * @param esquemaEXCLUYENTE
	 * @param centroAtencion
	 * @param codigoPkNOT_IN
	 * @return
	 */
	public static boolean existeHonorarioPool(int pool, int convenioEXCLUYENTE, int esquemaEXCLUYENTE, int centroAtencion, BigDecimal codigoPkNOT_IN)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getHonorariosPoolDao().existeHonorarioPool(pool, convenioEXCLUYENTE, esquemaEXCLUYENTE, centroAtencion, codigoPkNOT_IN);
	}
}