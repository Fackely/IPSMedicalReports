package com.princetonsa.dao.facturasVarias;
import java.sql.Connection;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dto.facturasVarias.DtoDeudor;

/**
 * 
 * @author Juan Sebastian Castaño C.
 *
 *Interfaz utilizada para gestionar los métodos DAO de la funcionalidad
 *DEUDORES
 */
public interface DeudoresDao {

	
	/**
	 * Funcionalidad de carga de los terceros
	 * @param con
	 * @return
	 */
	public HashMap<String, Object> cargarTerceros(Connection con, int institucion);
	


	
	/**
	 * Metodo de consulta de deudores por un tipo seleccionado
	 * @param con
	 * @param tipoDeudorSeleccionado
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> consultarDeudoresPorTipoSelec(Connection con, String tipoDeudorSeleccionado, int institucion);
	
	
	/**
	 * Método para cargar información del nuevo deudor
	 * @param con
	 * @param codigo
	 * @param tipoDeudor
	 * @return
	 */
	public DtoDeudor cargarInformacionNuevoDeudor(Connection con,String codigo,String tipoDeudor);
	
	/**
	 * Método usado para ingresar un nuevo deudor
	 * @param con
	 * @param deudor
	 * @return
	 */
	public ResultadoBoolean ingresar(Connection con,DtoDeudor deudor);
	
	/**
	 * Método implementado para cargar la información del deudor
	 * @param con
	 * @param campos
	 * @return
	 */
	public DtoDeudor cargar(Connection con,HashMap<String,Object> campos);
	
	/**
	 * Método que realiza una modificación del deudor
	 * @param con
	 * @param deudor
	 * @return
	 */
	public ResultadoBoolean modificar(Connection con,DtoDeudor deudor);
	
	/**
	 * 
	 * @param con
	 * @param deudor
	 * @return
	 */
	public  ResultadoBoolean modificarDeudor(Connection con,DtoDeudor deudor);

	/**
	 * Método para cargar un deudor
	 * @param con
	 * @param tipoId
	 * @param numeroId
	 * @param tipo
	 * @return DtoDeudor deudor cargado
	 */
	public DtoDeudor cargar(Connection con, String tipoId, String numeroId, String tipo);
		
}
