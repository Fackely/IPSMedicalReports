package com.princetonsa.dao.cargos;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.cargos.DtoCargoDirecto;
import com.princetonsa.dto.cargos.DtoCargoDirectoHC;
import com.princetonsa.dto.ordenes.DtoProcedimiento;

/**
 * @author Sebastián Gómez 
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * Cargos Directos
 */
public interface CargosDirectosDao 
{
	/**
	 * Método implementado para insertar regitros de cargos directos incluyendo su información de historia clínica
	 * @param con
	 * @param cargoDirecto
	 * @param cargoDirectoHC
	 * @return
	 */
	public int insertar(Connection con,ArrayList<DtoCargoDirecto> arregloCargoDirecto,DtoCargoDirectoHC cargoDirectoHC);
	
	/**
	 * Método para cargar la información e historia clínica de un cargo directo
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public DtoCargoDirectoHC consultarInformacionHC(Connection con,String numeroSolicitud);
	
	/**
	 * M&eacute;todo encargado de buscar los servicios asociados a las 
	 * solicitudes generadas de cargos directos de sevicios
	 * @author Diana Carolina G
	 */
	public DtoProcedimiento buscarServiciosCargosDirectos(Connection con, int numeroSolicitud, int codigoTarifario);
	
}
