package com.princetonsa.dao.oracle.facturacion;

import java.util.HashMap;

import com.princetonsa.dao.facturacion.CoberturasEntidadesSubcontratadasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseCoberturasEntidadesSubcontratadasDao;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */


public class OracleCoberturasEntidadesSubcontratadasDao implements CoberturasEntidadesSubcontratadasDao
{
	/**
	 * Metodo para consultar las coberturas por entidad subcontratada y contrato
	 * @param criterios
	 * @return
	 */
	public HashMap consultaCoberturasEntiSub(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaCoberturasEntiSub(criterios);
	}
	
	/**
	 * Metodo para consultar las excepciones coberturas por entidad subcontratada y contrato
	 * @param criterios
	 * @return
	 */
	public HashMap consultaExcCoberturas(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaExcCoberturas(criterios);
	}
	
	/**
	 * Metodo para insertar un nuevo registro de excepcion de cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public int guardarNuevoRegistroExCobertura(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.guardarNuevoRegistroExCobertura(criterios);
	}
	
	/**
	 * Metodo para insertar un nuevo registro de cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public int guardarNuevoRegistroCobertura(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.guardarNuevoRegistroCobertura(criterios);
	}
	
	/**
	 * Metodo para insertar un nuevo registro de cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public int modificarRegistroCobertura(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.modificarRegistroCobertura(criterios);		
	}
	
	/**
	 * Metodo que elimina un registro de cobertura por entidad subcontratada
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistroCobertura(int consecutivo, String usuario)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.eliminarRegistroCobertura(consecutivo, usuario);
	}
	
	/**
	 * Metodo que elimina un registro de excepcion cobertura por entidad subcontratada
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistroExCobertura(int consecutivo, String usuario)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.eliminarRegistroExCobertura(consecutivo, usuario);
	}
	
	/**
	 * Metodo para consultar las clases inventarios definidas en el sistema
	 * @param criterios
	 * @return
	 */
	public HashMap consultaClasesInventarios()
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaClasesInventarios();
	}
	
	/**
	 * Metodo para consultar los grupos segun clase de inventario seleccionada
	 * @param criterios
	 * @return
	 */
	public HashMap consultaGrupoInventario(String clase)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaGrupoInventario(clase);
	}
	
	/**
	 * Metodo para consultar los grupos segun clase de inventario seleccionada
	 * @param criterios
	 * @return
	 */
	public HashMap consultaSubGrupoInventario(String grupo, String clase)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaSubGrupoInventario(grupo, clase);
	}
	
	/**
	 * Metodo para consultar los grupos servicios en el sistema
	 * @param criterios
	 * @return
	 */
	public HashMap consultaGruposServicios()
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaGruposServicios();
	}
	
	/**
	 * Metodo para consultar los tipos servicios en el sistema
	 * @param criterios
	 * @return
	 */
	public HashMap consultaTiposServicio()
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaTiposServicio();
	}
	
	/**
	 * Metodo para consultar las especialidades definidas en el sistema
	 * @return
	 */
	public HashMap consultaEspecialidades()
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaEspecialidades();
	}
	
	/**
	 * Metodo para insertar un nuevo registro Servicios Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarExCoberServEntiSub(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.guardarExCoberServEntiSub(criterios);
	}

/**
	 * Metodo para insertar un nuevo registro Servicios Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarAgruServExCober(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.guardarAgruServExCober(criterios);
	}

/**
	 * Metodo para insertar un nuevo registro Articulo de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarExCoberArtiEntiSub(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.guardarExCoberArtiEntiSub(criterios);
	}

/**
	 * Metodo para insertar un nuevo registro Articulos Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarAgruArtiExCober(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.guardarAgruArtiExCober(criterios);
	}
	
	/**
	 * Metodo para insertar un registro en el log de cobertura el realizar una modificacion
	 * @param criterios
	 * @return
	 */
	public boolean guardarRegCoberturaLog(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.guardarRegCoberturaLog(criterios);
	}
	
	/**
	 * Metodo para consultar los articulos agrupados
	 * @return
	 */
	public HashMap consultaAgruArtiEntiSub(int consecutivo)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaAgruArtiEntiSub(consecutivo);
	}
	
	/**
	 * Metodo para consultar los servicios agrupados
	 * @return
	 */
	public HashMap consultaAgruServEntiSub(int consecutivo)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaAgruServEntiSub(consecutivo);
	}
	
	/**
	 * Metodo que elimina un registro de articulo agrupado
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarArtiAgru(int consecutivo, String usuario)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.eliminarArtiAgru(consecutivo, usuario);
	}
	
	/**
	 * Metodo que elimina un registro de articulo agrupado
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarServAgru(int consecutivo, String usuario)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.eliminarServAgru(consecutivo, usuario);
	}
	
	/**
	 * Metodo para insertar un registro de servicio especifico
	 * @param criterios
	 * @return
	 */
	public int guardarServEsp(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.guardarServEsp(criterios);
	}
	
	/**
	 * Metodo que elimina un registro de servicio especifico
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarServEsp(int consecutivo, String usuario)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.eliminarServEsp(consecutivo, usuario);
	}
	
	/**
	 * Metodo para insertar un registro de articulo especifico
	 * @param criterios
	 * @return
	 */
	public int guardarArtiEsp(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.guardarArtiEsp(criterios);
	}
	
	/**
	 * Metodo que elimina un registro de articulo especifico
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarArtiEsp(int consecutivo, String usuario)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.eliminarArtiEsp(consecutivo, usuario);
	}
	
	/**
	 * Metodo para actualizar un registro de excepcion cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public int modificarRegExCober (HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.modificarRegExCober(criterios);
	}
	
	/**
	 * Metodo para insertar un registro en el log de excepciones de cobertura el realizar una modificacion
	 * @param criterios
	 * @return
	 */
	public boolean guardarRegExCoberturaLog(HashMap criterios)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.guardarRegExCoberturaLog(criterios);
	}
	
	/**
	 * Metodo para consultar los articulos especificos
	 *  agrupados
	 * @return
	 */
	public HashMap consultaArtiEsp(int exCoberEntSub)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaArtiEsp(exCoberEntSub);
	}
	
	/**
	 * Metodo para consultar los servicios especificos
	 *  agrupados
	 * @return
	 */
	public HashMap consultaServEsp(int exCoberEntSub)
	{
		return SqlBaseCoberturasEntidadesSubcontratadasDao.consultaServEsp(exCoberEntSub);
	}
}