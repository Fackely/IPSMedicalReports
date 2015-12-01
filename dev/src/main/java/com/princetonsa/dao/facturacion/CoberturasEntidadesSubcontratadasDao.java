package com.princetonsa.dao.facturacion;

import java.util.HashMap;

import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCoberturasEntidadesSubcontratadasDao;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */

public interface CoberturasEntidadesSubcontratadasDao
{
	/**
	 * Metodo para consultar las coberturas por entidad subcontratada y contrato
	 * @param criterios
	 * @return
	 */
	public HashMap consultaCoberturasEntiSub(HashMap criterios);
	
	/**
	 * Metodo para consultar las excepciones coberturas por entidad subcontratada y contrato
	 * @param criterios
	 * @return
	 */
	public HashMap consultaExcCoberturas(HashMap criterios);
	
	/**
	 * Metodo para insertar un nuevo registro de excepcion de cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public int guardarNuevoRegistroExCobertura(HashMap criterios);
	
	/**
	 * Metodo para insertar un nuevo registro de cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public int guardarNuevoRegistroCobertura(HashMap criterios);
	
	/**
	 * Metodo para insertar un nuevo registro de cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public int modificarRegistroCobertura(HashMap criterios);
	
	/**
	 * Metodo que elimina un registro de cobertura por entidad subcontratada
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistroCobertura(int consecutivo, String usuario);
	
	/**
	 * Metodo que elimina un registro de excepcion cobertura por entidad subcontratada
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistroExCobertura(int consecutivo, String usuario);
	
	/**
	 * Metodo para consultar las clases inventarios definidas en el sistema
	 * @param criterios
	 * @return
	 */
	public HashMap consultaClasesInventarios();
	
	/**
	 * Metodo para consultar los grupos segun clase de inventario seleccionada
	 * @param criterios
	 * @return
	 */
	public HashMap consultaGrupoInventario(String clase);
	
	/**
	 * Metodo para consultar los grupos segun clase de inventario seleccionada
	 * @param criterios
	 * @return
	 */
	public HashMap consultaSubGrupoInventario(String grupo, String clase);
	
	/**
	 * Metodo para consultar los grupos servicios en el sistema
	 * @param criterios
	 * @return
	 */
	public HashMap consultaGruposServicios();
	
	/**
	 * Metodo para consultar los tipos servicios en el sistema
	 * @param criterios
	 * @return
	 */
	public HashMap consultaTiposServicio();
	
	/**
	 * Metodo para consultar las especialidades definidas en el sistema
	 * @return
	 */
	public HashMap consultaEspecialidades();
	
	/**
	 * Metodo para insertar un nuevo registro Servicios Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarExCoberServEntiSub(HashMap criterios);

/**
	 * Metodo para insertar un nuevo registro Servicios Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarAgruServExCober(HashMap criterios);

/**
	 * Metodo para insertar un nuevo registro Articulo de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarExCoberArtiEntiSub(HashMap criterios);

/**
	 * Metodo para insertar un nuevo registro Articulos Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarAgruArtiExCober(HashMap criterios);
	
	/**
	 * Metodo para insertar un registro en el log de cobertura el realizar una modificacion
	 * @param criterios
	 * @return
	 */
	public boolean guardarRegCoberturaLog(HashMap criterios);
	
	/**
	 * Metodo para consultar los articulos agrupados
	 * @return
	 */
	public HashMap consultaAgruArtiEntiSub(int consecutivo);
	
	/**
	 * Metodo para consultar los servicios agrupados
	 * @return
	 */
	public HashMap consultaAgruServEntiSub(int consecutivo);
	
	/**
	 * Metodo que elimina un registro de articulo agrupado
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarArtiAgru(int consecutivo, String usuario);
	
	/**
	 * Metodo que elimina un registro de articulo agrupado
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarServAgru(int consecutivo, String usuario);
	
	/**
	 * Metodo para insertar un registro de servicio especifico
	 * @param criterios
	 * @return
	 */
	public int guardarServEsp(HashMap criterios);
	
	/**
	 * Metodo que elimina un registro de servicio especifico
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarServEsp(int consecutivo, String usuario);
	
	/**
	 * Metodo para insertar un registro de articulo especifico
	 * @param criterios
	 * @return
	 */
	public int guardarArtiEsp(HashMap criterios);
	
	/**
	 * Metodo que elimina un registro de articulo especifico
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarArtiEsp(int consecutivo, String usuario);
	
	/**
	 * Metodo para actualizar un registro de excepcion cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public int modificarRegExCober (HashMap criterios);
	
	/**
	 * Metodo para insertar un registro en el log de excepciones de cobertura el realizar una modificacion
	 * @param criterios
	 * @return
	 */
	public boolean guardarRegExCoberturaLog(HashMap criterios);
	
	/**
	 * Metodo para consultar los articulos especificos
	 *  agrupados
	 * @return
	 */
	public HashMap consultaArtiEsp(int exCoberEntSub);
	
	/**
	 * Metodo para consultar los servicios especificos
	 *  agrupados
	 * @return
	 */
	public HashMap consultaServEsp(int exCoberEntSub);
}