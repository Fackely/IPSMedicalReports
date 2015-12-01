package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.Collection;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dao.sqlbase.facturacion.SqlBaseEntidadesSubContratadasDao;


public interface EntidadesSubContratadasDao
{
	/**
	 * 
	 * @author Jhony Alexander Duque A.
	 * jduque@princetonsa.com
	 * 02/01/2008
	 */
	
	
	
	/**
	 * Metodo encargado de insertar los datos en 
	 * la tabla entidades_subcontratadas
	 * @param connection
	 * @param datos
	 * ---------------------------------
	 * 	 	KEY'S DEL HASHMAP DATOS
	 * ---------------------------------
	 * -- codigo1_ --> Requerido
	 * -- institucion2_ --> Requerido 
	 * -- razonSocial3_ --> Requerido
	 * -- tercero4_ --> Requerido
	 * -- codigoMinsalud5_ --> Requerido
	 * -- direccion6_ --> Opcional
	 * -- telefono7_ --> Opcional
	 * -- personaContactar8_ --> Opcional
	 * -- observaciones9_ --> Opcional
	 * -- usuarioModifica11_ --> Requerido
	 * @return
	 */
	public ResultadoBoolean insertar0 (Connection connection,HashMap datos);
	
	
	/**
	 * Busqueda del tercero, 
	 * este metodo busca por numero de identificacion
	 * o por la descripcion 
	 */
	public Collection buscarEntidad(Connection con, String numIdent, String descripcionEntidad);
	
	
	/**
	 * Metodo encargado de Buscar los detalles de
	 * vigencias
	 * @param connection
	 * @param criterios
	 * ----------------------------------------
	 * 
	 * ----------------------------------------
	 * @return
	 */
	public HashMap buscar1 (Connection connection,HashMap criterios);
	
	/**
	 * Metodo encargado de insertar de los datos en 
	 * la tabla det_entidades_subcontratadas
	 * @param connection
	 * @param datos
	 * -----------------------------------
	 * KEY'S DEL HASHMAP DATOS
	 * -----------------------------------
	 * -- codigoDet14_ --> Requerido
	 * -- codigoEntSub15_ --> Requerido
	 * -- fechaInicial16_ --> Requerido
	 * -- fechaFinal17_ --> Requerido
	 * -- esqTarServ18_ --> Requerido
	 * -- esqTarInv19_ --> Requerido
	 * 
	 * @return
	 */
	public boolean insertar1 (Connection connection, HashMap datos);
	
	/**
	 * Metodo encargado de buscar
	 * los datos de la tabla entidades_subcontratadas
	 * @param connection
	 * @param criterios
	 * ------------------------------
	 * 	KEY DEL MAPA CRITERIOS
	 * ------------------------------
	 * -- codigoPk0_
	 * -- institucion2_
	 * -- codigo1_
	 * @return
	 * ----------------------------------------------
	 * EL MAPA QUE RETORNA TIENE LOS SIGUIENTES KEY
	 * ----------------------------------------------
	 * -- codigoPk0_
	 * -- codigo1_
	 * -- institucion2_
	 * -- razonSocial3_
	 * -- tercero4_
	 * -- codigoMinsalud5_
	 * -- direccion6_
	 * -- telefono7_
	 * -- personaContactar8_
	 * -- observaciones9_
	 * -- estaBd10_
	 */
	public  HashMap buscar0 (Connection connection,HashMap criterios);
	
	

	/**
	 * Metodo encargado de eliminar los datos de la 
	 * tabla det_entidades_subcontratadas	
	 * @param connection
	 * @param criterios
	 * ----------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * ----------------------------
	 *  -- codigoDet14_
	 * @return
	 */
	public  boolean eliminar1 (Connection connection, HashMap criterios);
	
	
	/**
	 * Metodo encargado de modificar los datos del 
	 * detalle 
	 * @param connection
	 * @param criterios
	 * -------------------------------------------
	 * 		KEY'S DEL HASHMAP CRITERIOS
	 * -------------------------------------------
	 * -- fechaInicial16_
	 * -- fechaFinal17_
	 * -- esqTarServ18_
	 * -- esqTarInv19_
	 * -- usuarioModifica11_0
	 * -- codigoDet14_
	 * @return
	 */
	public boolean modificar1 (Connection connection, HashMap criterios);
	
	/**
	 * metodo encargado de modificar los datos 
	 * de la tabla entidades_subcontratadas
	 * @param connection
	 * @param datos
	 * -----------------------------------------
	 * 		  KEY'S DEL HASHMAP DATOS
	 * -----------------------------------------
	 * -- codigo1_ --> Requerido
	 * -- institucion2_ --> Requerido 
	 * -- razonSocial3_ --> Requerido
	 * -- tercero4_ --> Requerido
	 * -- codigoMinsalud5_ --> Requerido
	 * -- direccion6_ --> Opcional
	 * -- telefono7_ --> Opcional
	 * -- personaContactar8_ --> Opcional
	 * -- observaciones9_ --> Opcional
	 * -- usuarioModifica11_ --> Requerido
	 * -- codigoPk0_  --> Requerido
	 * @return
	 */
	public boolean modificar0 (Connection connection,HashMap datos);
	
	
	
	
	/**
	 * Metodo encargado de eliminar los datos de la tabla
	 * entidades_subcontratadas y sus respectivos detalles.
	 * @param connection
	 * @param criterios
	 * ---------------------------------
	 * KEY'S DEL HASHMAP CRITERIOS
	 * -----------------------------------
	 * -- codigoPk0_
	 * -- institucion2_
	 * -- codigoDet14_
	 * 
	 * @return
	 */
	public boolean eliminarEntidadConDetalle (Connection connection, HashMap criterios);
	
	/**
	 * Metodo encargado de consultar la descripcion
	 * de la entidad subcontratada filtrando por el
	 * ingreso
	 * @param connection
	 * @param ingreso
	 * @return Descripcion entidad subcontratada.
	 */
	public String getDescripcionEntidadSubXIngreso (Connection connection, String ingreso);
	
	public boolean insertarUsuarioEntidadSub(Connection connection, HashMap criterios);


	public HashMap<String, Object> usuariosEntidadSub(Connection connection,
			int consecutivo);


	public boolean eliminarusu(Connection connection, String descripcion);


	
}