package com.princetonsa.mundo.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import util.odontologia.InfoPromocionPresupuestoServPrograma;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoPromocionesOdontologicas;

/**
 * 
 * @author axioma
 *
 */
public class PromocionesOdontologicas {
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoPromocionesOdontologicas dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPromocionesOdontologicasDao().guardar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardarLog(DtoPromocionesOdontologicas dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPromocionesOdontologicasDao().guardarLog(dto);
	}
	
	
	
	/**
	 * GUARDA EL LOG CONSULTA
	 * @param dto
	 * @return
	 */
	public static double guardarLogConsulta(DtoPromocionesOdontologicas dto, String tipoReporte, String ruta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPromocionesOdontologicasDao().guardarLogConsulta(dto, tipoReporte, ruta);
	}
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPromocionesOdontologicas> cargar(DtoPromocionesOdontologicas dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPromocionesOdontologicasDao().cargar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
    
	public static boolean  modificar(DtoPromocionesOdontologicas dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPromocionesOdontologicasDao().modificar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  eliminar(DtoPromocionesOdontologicas dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPromocionesOdontologicasDao().eliminar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @param codigoPkNotIn
	 * @return
	 */
	public static boolean   existeCruceFechas(DtoPromocionesOdontologicas dto, double codigoPkNotIn) {
	
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPromocionesOdontologicasDao().existeCruceFechas(dto, codigoPkNotIn);
	}

	/**
	 * Metodo para obtener los desucnetos de promociones para aplicarlos a los calculos de los cargos de odontologia
	 * 
	 * key= fechabd, hora, programa, servicio, regionCentroAntencion, paisCentroAtencion, ciudadCentroAtencion
	 * deptoCentroAtencion, centroAtencion, convenio, edad, sexo, estadoCivil, numeroHijos, ocupacion
	 * fechainiciopromocion
	 * 
	 * 
	 * @param vo
	 * @return
	 */
	public static ArrayList<InfoPromocionPresupuestoServPrograma> obtenerValorDescuentoPromociones(HashMap<String, String> vo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPromocionesOdontologicasDao().obtenerValorDescuentoPromociones(vo);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoPromocionesOdontologicas> consultaAvanzadaPromociones(DtoPromocionesOdontologicas dto) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPromocionesOdontologicasDao().consultaAvanzadaPromociones(dto)	;
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static String consultaAvanzadaPromocionesReportes(DtoPromocionesOdontologicas dto, boolean aplicaProgramas) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getPromocionesOdontologicasDao().consultaAvanzadaPromocionesReporte(dto,aplicaProgramas);
	}
	
	
}