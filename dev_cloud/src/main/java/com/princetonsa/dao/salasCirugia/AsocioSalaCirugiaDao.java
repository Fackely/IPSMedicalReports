package com.princetonsa.dao.salasCirugia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;



public interface AsocioSalaCirugiaDao
{
	/**
	 * Consulta la informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public HashMap consultaAsocioSalaCirugia(Connection con, int institucion);
	
	
	/**
	 * Consulta la informacion de los asocios de Cirugia por medio del acronimo
	 * @param Connection con
	 * @param int institucion 
	 * @param String codigoAsocio
	 * */
	public HashMap consultaAsocioSalaCirugia(Connection con, int institucion,String codigoAsocio);
	
	/**
	 * Actualiza la informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean actualizarAsocioSalaCirugia(Connection con, HashMap parametros);
	
	/**
	 * Inserta informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean insertarAsocioSalaCirugia(Connection con, HashMap parametros);
	
	/**
	 * Elimina informacion de los asocios de Cirugia
	 * @param Connection con
	 * @param HashMap parametros 
	 * */
	public boolean eliminaAsocioSalaCirugia(Connection con, int instiucion, String codigo);
	
	/**
	 * Consulta de Tipos de Servicio
	 * @param Connection con
	 * */
	public ArrayList<HashMap<String,Object>> consultaTiposServicios(Connection con);
}