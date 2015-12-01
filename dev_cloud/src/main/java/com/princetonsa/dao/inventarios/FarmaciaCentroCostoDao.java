package com.princetonsa.dao.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;


import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.mundo.inventarios.FarmaciaCentroCosto;

/**
 * Interfaz de secciones x almacen
 * @author axioma
 *
 */
public interface FarmaciaCentroCostoDao 
{
	/**
	 * 
	 * @param con
	 * @param codigo
	 */
	public boolean insertar(Connection con, FarmaciaCentroCosto farmaciacentrocosto);
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public boolean eliminar(Connection con, int codigo, int farmacia);
	
	/** 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public HashMap consultar(Connection con, FarmaciaCentroCosto farmaciacentrocosto) ;
	
	
	/**
	 * 
	 * @param con
	 * @param farmaciacentrocosto
	 * @return
	 */
	public boolean insertartrans(Connection con, FarmaciaCentroCosto farmaciacentrocosto);
	
	public boolean insertardet(Connection con, FarmaciaCentroCosto farmaciacentrocosto);
	
	
}
