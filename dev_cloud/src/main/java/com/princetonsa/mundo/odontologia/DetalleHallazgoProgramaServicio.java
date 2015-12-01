package com.princetonsa.mundo.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseDetalleHallazgoVsProgramaServicioDao;
import com.princetonsa.dto.odontologia.DtoDetalleHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoEquivalentesHallazgoProgramaServicio;

public class DetalleHallazgoProgramaServicio {


	public static boolean eliminar(DtoDetalleHallazgoProgramaServicio dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().eliminar(dtoWhere);
	}

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoDetalleHallazgoProgramaServicio> cargar(DtoDetalleHallazgoProgramaServicio dtoWhere, int codigoInstitucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().cargar(dtoWhere, codigoInstitucion);
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardar(DtoDetalleHallazgoProgramaServicio dto , Connection con) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().guardar(dto,con);
	}

	/**
	 * 
	 * @param dtoNuevo
	 * @param dtoWhere
	 * @return
	 */
	public static boolean modificar(DtoDetalleHallazgoProgramaServicio dtoNuevo,DtoDetalleHallazgoProgramaServicio dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().modificar(dtoNuevo, dtoWhere);
	}
	
	/**
	 * 
	 * 
	 */
	
	
	public static boolean eliminarEquivalentes(DtoEquivalentesHallazgoProgramaServicio dtoWhere) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().eliminarEquivalentes(dtoWhere);
	}

	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public static ArrayList<DtoEquivalentesHallazgoProgramaServicio> cargarEquivalentes(DtoEquivalentesHallazgoProgramaServicio dtoWhere , String tipo) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().cargarEquivalentes(dtoWhere, tipo );
	}
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static  double guardarEquivalente(DtoEquivalentesHallazgoProgramaServicio dto ,  Connection con) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().guardarEquivalente(dto , con);
	}

	/**
	 * 
	 * @param programaServicio
	 * @param hallazgo
	 * @param utilizaPrograma
	 * @return
	 */
	public static ArrayList<Double> obtenerEquivalentesProgServ(BigDecimal programaServicio, double hallazgo, boolean utilizaPrograma)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().obtenerEquivalentesProgServ(programaServicio, hallazgo, utilizaPrograma);
	}
	
	public static boolean programasServiciosEquivalentes(BigDecimal programaServicio1, BigDecimal programaServicio2, double hallazgo, boolean utilizaPrograma)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().programasServiciosEquivalentes(programaServicio1, programaServicio2, hallazgo, utilizaPrograma);
	}

	/**
	 * Carga el n&uacute;mero de superficies a las cuales aplica el programa en el hallazgo seleccionado
	 * @param detalleHallazgoProgramaServicio {@link DtoDetalleHallazgoProgramaServicio} Información con el hallazgo &oacute; el codigo_pk del DetalleHallazgoProgramaServicio
	 * @param codigoHallazgo int C&oacute;digo del hallazgo buscado
	 * @return N&uacute;mero de superficies para las que aplica el programa
	 * @author Juan David Ram&iacute;rez
	 * @since 2010-05-14
	 */
	public static int consultarNumeroSuperficiesAplica(DtoDetalleHallazgoProgramaServicio detalleHallazgoProgramaServicio, int codigoHallazgo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().consultarNumeroSuperficiesAplica(detalleHallazgoProgramaServicio, codigoHallazgo);
	}
	
	
	
	/**
	 * METODO QUE VALIDA SI EXISTE UN HALLAZGO EN EL  PLAN TRATAMIENTO
	 * @param dto
	 * @return
	 * @deprecated
	 */
	public static  boolean existenHallazgosPlanTratamiento(	DtoDetalleHallazgoProgramaServicio dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().existenHallazgosPlanTratamiento(dto);
	}
	
	
	
	
	/**
	 * METODO QUE VALIDA SI EXISTE UN HALLAZGO EN EL  PLAN TRATAMIENTO
	 * @param dto
	 * @return
	 */
	public static  boolean existenDetalleHallazgoPlanTratamiento(DtoDetalleHallazgoProgramaServicio dto, boolean aplicaProgramas){ 
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDetalleHallazgoProgramaServicioDao().existeDetalleAsociadoPlanTratamiento(dto, aplicaProgramas);
	}
	
	
	

	

	
}
