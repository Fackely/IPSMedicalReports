/*
 * Sep 20, 2005
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @author Sebastián Gómez R
 *
 * Interface utilizada para gestionar los métodos DAO de la funcionalidad
 * parametrización de Excepciones Asocio por Tipo Sala
 */
public interface ExcepcionAsocioTipoSalaDao {
	
	/**
	 *Metodo encargado de modificar los datos de la tabla det_ex_asocio_tipo_sala
	 *Los parametros son:
	 *@Connection connection
	 *@HashMap datos
	 *------------------------------------
	 *	     KEY'S DEL HASHMAP DATOS
	 *------------------------------------
	 * -- tipoServicio --> Opcional
	 * -- tipoCirugia --> Opcional
	 * -- tipoLiquidacion --> Requerido
	 * -- cantidad --> Requerido
	 * -- liquidarSobreAsocio --> Opcional
	 * -- asocio --> Requerido
	 * -- codigo --> Requerido
	 * -- codExAsocTipSala --> Requerido
	 * -- usuarioModifica --> Requerido
	 * ------------------------------------
	 * @return boolean
	 * --true --> la operacion re realizo correctamente
	 * --false --> la operacion se aborto.		
	 **/
	public boolean modificar1 (Connection connection,HashMap datos);
	
	
	/**
	 * Metodo encargado de consultar la informacion  de las tablas
	 * det_ex_asocio_tipo_sala y  ex_asocio_tipo_sala
	 * Los parametros son :
	 * @Connection connection
	 * @HashMap Criterios
	 * ------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ------------------------------
	 * -- codigo --> Requerido
	 * -- institucion --> Requerido
	 * -- tipoServicio --> Opcional
	 * -- tipoCirugia --> Opcional
	 * -- tipoSala --> Opcional
	 * -- asocio --> Opcional
	 * -- tipoLiquidacion --> Opcional
	 * -- liquidarSobreAsocio --> Opcional
	 * 
	 * --------------------------------
	 * @return HashMap
	 * -------------------------------
	 * KEY'S DEL HASHMAP QUE RETORNA
	 * -------------------------------
	 * --codigo_
	 * --codExAsocTipSala_
	 * --tipoServicio_
	 * --tipoCirugia_
	 * --tipoLiquidacion_
	 * --cantidad_
	 * --asocio_
	 * --liquidarSobreAsocio_
	 * --estaBd_
	 * --usuarioModifica_
	 * --tipoSala_
	 **/
	public HashMap buscar (Connection connection,HashMap criterios);
	
	
	/**
	 * Metodo encargado de insertar los datos tango en el encabezado
	 * (ex_asocio_tipo_sala) como en el cuerpo (det_ex_asocio_tipo_sala) 
	 * Los Parametros son:
	 * ---------------------------------------
	 * LOS KEY'S DEL HASHMAP DATOS SON
	 * ---------------------------------------
	 * -- esqTarParticular --> Opcional
	 * -- esqTarGeneral --> Opcional
	 * -- tipoSala --> Requerido
	 * -- institucion --> Requerido
	 * -- usuarioModifica --> Requerido 
	 * -- codExAsocTipSala --> Requerido
	 * -- tipoServicio --> Opcional
	 * -- tipoCirugia --> Opcional
	 * -- tipoLiquidacion --> Requerido
	 * -- cantidad --> Requerido
	 * -- liquidarSobreAsocio --> Opcional
	 * -- asocio --> Requerido
	 * -- usuarioModifica --> Requerido
	 * @return boolean
	 *	--true --> la operacion re realizo correctamente
	 *  --false --> la operacion se aborto.			
	 **/
	public boolean insertarGeneral (Connection connection, HashMap datos); 
	
	
	/**
	 * Metodo encargado de determinar en que porte hace el borrado
	 * de los datos, ya sea en el encabezado o en el detalle
	 * @param connection
	 * @param datos
	 * ------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * ------------------------------
	 *-- codigo0_ --> Requerido
	 *-- codExAsocTipSala1_ --> Requerido
	 *-- institucion14_ --> Requerido
	 * @return
	 */
	public boolean eliminarGeneral (Connection connection, HashMap datos);
	
	
}
