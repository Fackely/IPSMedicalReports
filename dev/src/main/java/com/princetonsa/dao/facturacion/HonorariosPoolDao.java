package com.princetonsa.dao.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoAgrupHonorariosPool;
import com.princetonsa.dto.odontologia.DtoHonorarioPoolServicio;
import com.princetonsa.dto.odontologia.DtoHonorariosPool;

public interface  HonorariosPoolDao {
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  double guardarHonorariosPool(final DtoHonorariosPool dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoHonorariosPool> cargarHonorariosPool( final DtoHonorariosPool dto);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardarHonoriarioPoolServicio( Connection con, final DtoHonorarioPoolServicio dto);
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  ArrayList<DtoHonorarioPoolServicio> cargarHonorariosPoolServ( final DtoHonorarioPoolServicio dto, int institucion);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardarGruposHonorarioPool(Connection con, final DtoAgrupHonorariosPool dto);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoAgrupHonorariosPool> cargarAgrupacionHonorariosPool( DtoAgrupHonorariosPool dto);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean modicarAgrupacionServicios(DtoAgrupHonorariosPool dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean eliminarAgrupacionHonorario(DtoAgrupHonorariosPool dto);
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean eliminarHonorariosPoolServicio(DtoHonorarioPoolServicio dto);
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	
	public  boolean modicarHonorarioPoolServicios(DtoHonorarioPoolServicio dto);
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean modificarHonorarioPool(DtoHonorariosPool dto);
	
	/**
	 * 
	 * @param codigoPk
	 * @return
	 */
	public boolean eliminarHonorarioPool (BigDecimal codigoPk);
	
	/**
	 * 
	 * @param pool
	 * @param convenioEXCLUYENTE
	 * @param esquemaEXCLUYENTE
	 * @param centroAtencion
	 * @param codigoPkNOT_IN
	 * @return
	 */
	public boolean existeHonorarioPool(int pool, int convenioEXCLUYENTE, int esquemaEXCLUYENTE, int centroAtencion, BigDecimal codigoPkNOT_IN);
}
