package com.princetonsa.dao.cargos;

import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.cargos.DtoCargoDirecto;
import com.princetonsa.dto.cargos.DtoCargoDirectoHC;
import com.princetonsa.dto.ordenes.DtoProcedimiento;

/**
 * @author Sebasti�n G�mez 
 *
 * Interface utilizada para gestionar los m�todos DAO de la funcionalidad
 * Cargos Directos
 */
public interface CargosDirectosDao 
{
	/**
	 * M�todo implementado para insertar regitros de cargos directos incluyendo su informaci�n de historia cl�nica
	 * @param con
	 * @param cargoDirecto
	 * @param cargoDirectoHC
	 * @return
	 */
	public int insertar(Connection con,ArrayList<DtoCargoDirecto> arregloCargoDirecto,DtoCargoDirectoHC cargoDirectoHC);
	
	/**
	 * M�todo para cargar la informaci�n e historia cl�nica de un cargo directo
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
