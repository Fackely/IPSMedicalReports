package com.princetonsa.mundo.facturacion;

import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.CoberturasEntidadesSubcontratadasDao;

/**
 * 
 * @author Angela María Angel amangel@axioma-md.com
 *
 */


public class CoberturasEntidadesSubcontratadas
{
	static Logger logger = Logger.getLogger(CoberturasEntidadesSubcontratadas.class);
	
	public static String[] indicesCoberturasEntiSub = {
		"descViaIngreso_",
		"viaIngreso_",
		"descTipoPaciente_",
		"tipoPaciente_",
		"naturaleza_",
		"activo_",
		"descNaturaleza_"
	};
	
	private static CoberturasEntidadesSubcontratadasDao getCoberturasEntidadesSubcontratadasDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getCoberturasEntidadesSubcontratadasDao();
	}
	
	/**
	 * Metodo para consultar las coberturas por entidad subcontratada y contrato
	 * @param criterios
	 * @return
	 */
	public HashMap consultaCoberturasEntiSub(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaCoberturasEntiSub(criterios);
	}
	
	/**
	 * Metodo para consultar las excepciones coberturas por entidad subcontratada y contrato
	 * @param criterios
	 * @return
	 */
	public HashMap consultaExcCoberturas(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaExcCoberturas(criterios);
	}
	
	/**
	 * Metodo para insertar un nuevo registro de excepcion de cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public static int guardarNuevoRegistroExCobertura(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().guardarNuevoRegistroExCobertura(criterios);
	}
	
	/**
	 * Metodo para guardar un nuevo registro o una actualizacion de cobertura por entidad subcontratada
	 * @return
	 */
	public static int guardarRegistroCobertura(HashMap criterios)
	{		
		if((criterios.get("nuevo")+"").equals("S"))
		{				
			return getCoberturasEntidadesSubcontratadasDao().guardarNuevoRegistroCobertura(criterios);
		}
		else
			return getCoberturasEntidadesSubcontratadasDao().modificarRegistroCobertura(criterios);		
	}
	
	/**
	 * Metodo que elimina un registro de cobertura por entidad subcontratada
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistroCobertura(int consecutivo, String usuario)
	{
		return getCoberturasEntidadesSubcontratadasDao().eliminarRegistroCobertura(consecutivo, usuario);
	}
	
	/**
	 * Metodo que elimina un registro de excepcion cobertura por entidad subcontratada
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarRegistroExCobertura(int consecutivo, String usuario)
	{
		return getCoberturasEntidadesSubcontratadasDao().eliminarRegistroExCobertura(consecutivo, usuario);
	}
	
	/**
	 * Metodo para consultar las clases inventarios definidas en el sistema
	 * @param criterios
	 * @return
	 */
	public HashMap consultaClasesInventarios()
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaClasesInventarios();
	}
	
	/**
	 * Metodo para consultar los grupos segun clase de inventario seleccionada
	 * @param criterios
	 * @return
	 */
	public HashMap consultaGrupoInventario(String clase)
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaGrupoInventario(clase);
	}
	
	/**
	 * Metodo para consultar los grupos segun clase de inventario seleccionada
	 * @param criterios
	 * @return
	 */
	public HashMap consultaSubGrupoInventario(String grupo, String clase)
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaSubGrupoInventario(grupo, clase);
	}
	
	/**
	 * Metodo para consultar los grupos servicios en el sistema
	 * @param criterios
	 * @return
	 */
	public HashMap consultaGruposServicios()
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaGruposServicios();
	}
	
	/**
	 * Metodo para consultar los tipos servicios en el sistema
	 * @param criterios
	 * @return
	 */
	public HashMap consultaTiposServicio()
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaTiposServicio();
	}
	
	/**
	 * Metodo para consultar las especialidades definidas en el sistema
	 * @return
	 */
	public HashMap consultaEspecialidades()
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaEspecialidades();
	}
	
	/**
	 * Metodo para insertar un nuevo registro Servicios Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarExCoberServEntiSub(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().guardarExCoberServEntiSub(criterios);
	}

/**
	 * Metodo para insertar un nuevo registro Servicios Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarAgruServExCober(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().guardarAgruServExCober(criterios);
	}

/**
	 * Metodo para insertar un nuevo registro Articulo de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarExCoberArtiEntiSub(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().guardarExCoberArtiEntiSub(criterios);
	}

/**
	 * Metodo para insertar un nuevo registro Articulos Agrupados de Excepcion de Cobertura
	 * @param criterios
	 * @return
	 */
	public int guardarAgruArtiExCober(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().guardarAgruArtiExCober(criterios);
	}
	
	/**
	 * Metodo para insertar un registro en el log de cobertura el realizar una modificacion
	 * @param criterios
	 * @return
	 */
	public boolean guardarRegCoberturaLog(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().guardarRegCoberturaLog(criterios);
	}
	
	/**
	 * Metodo para consultar los articulos agrupados
	 * @return
	 */
	public HashMap consultaAgruArtiEntiSub(int consecutivo)
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaAgruArtiEntiSub(consecutivo);
	}
	
	/**
	 * Metodo para consultar los servicios agrupados
	 * @return
	 */
	public HashMap consultaAgruServEntiSub(int consecutivo)
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaAgruServEntiSub(consecutivo);
	}
	
	/**
	 * Metodo que elimina un registro de articulo agrupado
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarArtiAgru(int consecutivo, String usuario)
	{
		return getCoberturasEntidadesSubcontratadasDao().eliminarArtiAgru(consecutivo, usuario);
	}
	
	/**
	 * Metodo que elimina un registro de articulo agrupado
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarServAgru(int consecutivo, String usuario)
	{
		return getCoberturasEntidadesSubcontratadasDao().eliminarServAgru(consecutivo, usuario);
	}
	
	/**
	 * Metodo para insertar un registro de servicio especifico
	 * @param criterios
	 * @return
	 */
	public int guardarServEsp(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().guardarServEsp(criterios);
	}
	
	/**
	 * Metodo que elimina un registro de servicio especifico
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarServEsp(int consecutivo, String usuario)
	{
		return getCoberturasEntidadesSubcontratadasDao().eliminarServEsp(consecutivo, usuario);
	}
	
	/**
	 * Metodo para insertar un registro de articulo especifico
	 * @param criterios
	 * @return
	 */
	public int guardarArtiEsp(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().guardarArtiEsp(criterios);
	}
	
	/**
	 * Metodo que elimina un registro de articulo especifico
	 * @param consecutivo
	 * @return
	 */
	public boolean eliminarArtiEsp(int consecutivo, String usuario)
	{
		return getCoberturasEntidadesSubcontratadasDao().eliminarArtiEsp(consecutivo, usuario);
	}
	
	/**
	 * Metodo para actualizar un registro de excepcion cobertura por entidad subcontratada
	 * @param criterios
	 * @return
	 */
	public int modificarRegExCober (HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().modificarRegExCober(criterios);
	}
	
	/**
	 * Metodo para insertar un registro en el log de excepciones de cobertura el realizar una modificacion
	 * @param criterios
	 * @return
	 */
	public boolean guardarRegExCoberturaLog(HashMap criterios)
	{
		return getCoberturasEntidadesSubcontratadasDao().guardarRegExCoberturaLog(criterios);
	}
	
	/**
	 * Metodo para consultar los articulos especificos
	 *  agrupados
	 * @return
	 */
	public HashMap consultaArtiEsp(int exCoberEntSub)
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaArtiEsp(exCoberEntSub);
	}
	
	/**
	 * Metodo para consultar los servicios especificos
	 *  agrupados
	 * @return
	 */
	public HashMap consultaServEsp(int exCoberEntSub)
	{
		return getCoberturasEntidadesSubcontratadasDao().consultaServEsp(exCoberEntSub);
	}
}