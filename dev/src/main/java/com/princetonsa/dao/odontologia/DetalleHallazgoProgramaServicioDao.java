package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoDetalleHallazgoProgramaServicio;
import com.princetonsa.dto.odontologia.DtoEquivalentesHallazgoProgramaServicio;

public interface DetalleHallazgoProgramaServicioDao {

	
	/**
	 * 
	 * @param 
	 * @return
	 */
	public boolean modificar(DtoDetalleHallazgoProgramaServicio dtoNuevo,DtoDetalleHallazgoProgramaServicio dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoDetalleHallazgoProgramaServicio> cargar(DtoDetalleHallazgoProgramaServicio dtoWhere, int codigoInstitucion);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoDetalleHallazgoProgramaServicio dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @param con 
	 * @return
	 */
	public double guardar(DtoDetalleHallazgoProgramaServicio dto, Connection con) ;
	/**
	 * 
	 * 
	 */
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoEquivalentesHallazgoProgramaServicio> cargarEquivalentes(DtoEquivalentesHallazgoProgramaServicio dtoWhere , String tipo);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminarEquivalentes(DtoEquivalentesHallazgoProgramaServicio dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardarEquivalente(DtoEquivalentesHallazgoProgramaServicio dto , Connection con) ;
	
	/**
	 * 
	 * @param programaServicio
	 * @param hallazgo
	 * @param utilizaPrograma
	 * @return
	 */
	public ArrayList<Double> obtenerEquivalentesProgServ(BigDecimal programaServicio, double hallazgo, boolean utilizaPrograma);
	/**
	 * 
	 * @param programaServicio1
	 * @param programaServicio2
	 * @param hallazgo
	 * @param utilizaPrograma
	 * @return
	 */
	public boolean programasServiciosEquivalentes(BigDecimal programaServicio1, BigDecimal programaServicio2, double hallazgo, boolean utilizaPrograma);
	/**
	 * 
	 * @param detalleHallazgoProgramaServicio
	 * @param codigoHallazgo
	 * @return
	 */
	public int consultarNumeroSuperficiesAplica(DtoDetalleHallazgoProgramaServicio detalleHallazgoProgramaServicio, int codigoHallazgo);
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean existenHallazgosPlanTratamiento( DtoDetalleHallazgoProgramaServicio dto);
	
	
	
	
	/**
	 * METODO PARA VALIDAR SI UN DETALLE ESTA ASOCIADO A UN PLAN DE TRATAMIENTO
	 * @author Edgar Carvajal Ruiz
	 * @param detalleHallazgo
	 * @return
	 */
	public  boolean existeDetalleAsociadoPlanTratamiento(DtoDetalleHallazgoProgramaServicio detalleHallazgo, boolean aplicaProgramas);
	

}
