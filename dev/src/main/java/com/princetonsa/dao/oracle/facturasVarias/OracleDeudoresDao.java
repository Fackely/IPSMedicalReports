package com.princetonsa.dao.oracle.facturasVarias;
/**
 * Juan Sebastian castaño
 */
import java.sql.Connection;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dao.facturasVarias.DeudoresDao;
import com.princetonsa.dao.sqlbase.facturasVarias.SqlBaseDeudoresDao;
import com.princetonsa.dto.facturasVarias.DtoDeudor;

public class OracleDeudoresDao implements DeudoresDao {

	/**
	 * Metodo de carga de los terceros
	 */
	public HashMap<String, Object> cargarTerceros(Connection con,  int institucion) {
		return SqlBaseDeudoresDao.cargarTerceros(con, institucion);
	}
	

	
	
	/**
	 * Metodo de consulta de deudores por un tipo seleccionado
	 * @param con
	 * @param tipoDeudorSeleccionado
	 * @param institucion
	 * @return
	 */
	public HashMap<String, Object> consultarDeudoresPorTipoSelec(Connection con, String tipoDeudorSeleccionado, int institucion)
	{
		return SqlBaseDeudoresDao.consultarDeudoresPorTipoSelec(con, tipoDeudorSeleccionado, institucion);
	}
	
	
	
	/**
	 * Método para cargar información del nuevo deudor
	 * @param con
	 * @param codigo
	 * @param tipoDeudor
	 * @return
	 */
	public DtoDeudor cargarInformacionNuevoDeudor(Connection con,String codigo,String tipoDeudor)
	{
		return SqlBaseDeudoresDao.cargarInformacionNuevoDeudor(con, codigo, tipoDeudor);
	}
	
	/**
	 * Método usado para ingresar un nuevo deudor
	 * @param con
	 * @param deudor
	 * @return
	 */
	public ResultadoBoolean ingresar(Connection con,DtoDeudor deudor)
	{
		return SqlBaseDeudoresDao.ingresar(con, deudor);
	}
	
	/**
	 * Método implementado para cargar la información del deudor
	 * @param con
	 * @param campos
	 * @return
	 */
	public DtoDeudor cargar(Connection con,HashMap<String,Object> campos)
	{
		return SqlBaseDeudoresDao.cargar(con, campos);
	}
	
	/**
	 * Método que realiza una modificación del deudor
	 * @param con
	 * @param deudor
	 * @return
	 */
	public ResultadoBoolean modificar(Connection con,DtoDeudor deudor)
	{
		return SqlBaseDeudoresDao.modificar(con, deudor);
	}




	@Override
	public ResultadoBoolean modificarDeudor(Connection con, DtoDeudor deudor) {
		return SqlBaseDeudoresDao.modificarDeudor(con, deudor);
	}

	@Override
	public DtoDeudor cargar(Connection con, String tipoId, String numeroId, String tipo)
	{
		return SqlBaseDeudoresDao.cargar(con, tipoId, numeroId, tipo);
	}

}
