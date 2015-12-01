package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoHallazgoOdontologico;

public interface HallazgosOdontologicosDao {

	/**
	 * 
	 * @param codigoInstitucionInt
	 * @return
	 */
	ArrayList<DtoHallazgoOdontologico> consultarHallazgosDentales(Connection con, int codigoInstitucionInt);
/**
 * 
 * @param con
 * @param nuevoHallazgo
 * @param loginUsuario
 * @param codigoInstitucionInt
 * @return
 */
	boolean crearNuevoHallazgoOdontologico(Connection con,DtoHallazgoOdontologico nuevoHallazgo, String loginUsuario,int codigoInstitucionInt);
/**
 * 
 * @param con
 * @param nuevoHallazgo
 * @param consecutivoHallazgo
 * @param loginUsuario
 * @return
 */
	boolean modificarHallazgoOdontologico(Connection con,DtoHallazgoOdontologico nuevoHallazgo, int consecutivoHallazgo,String loginUsuario);
/**
 * 
 * @param codigoHallazgo
 * @return
 */
	boolean eliminarHallazgoOdontologico(int codigoHallazgo);
	
	/**
	 * BUSQUEDA GENERICA DE SERVICIOS 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoHallazgoOdontologico> busquedaAvanzadaHallazgos(DtoHallazgoOdontologico dto);
	
	/**
	 * Retorna las convenciones parametrizadas dentro de los hallazgos
	 * */
	public ArrayList<DtoHallazgoOdontologico> busquedaConvencionesHallagos(DtoHallazgoOdontologico dto );
}
