package com.princetonsa.dao.odontologia;


import java.util.ArrayList;
import java.util.HashMap;

import util.odontologia.InfoPromocionPresupuestoServPrograma;
import com.princetonsa.dto.odontologia.DtoPromocionesOdontologicas;


/**
 * @author axioma
 */

public interface PromocionesOdontologicasDao {
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean modificar(DtoPromocionesOdontologicas dto);
	
	
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoPromocionesOdontologicas> cargar(	DtoPromocionesOdontologicas dto);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoPromocionesOdontologicas dto);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoPromocionesOdontologicas dto) ;

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardarLog(DtoPromocionesOdontologicas dto) ;

	/**
	 * 
	 * @param dto
	 * @param codigoPkNotIn
	 * @return
	 */
	public boolean existeCruceFechas(DtoPromocionesOdontologicas dto , double codigoPkNotIn);

	/**
	 * Metodo para obtener los descuentos de promociones para aplicarlos a los calculos de los cargos de odontologia
	 * 
	 * key= fechabd, hora, programa, servicio, regionCentroAntencion, paisCentroAtencion, ciudadCentroAtencion
	 * deptoCentroAtencion, centroAtencion, convenio, edad, sexo, estadoCivil, numeroHijos, ocupacion
	 * fechainiciopromocion
	 * 
	 * 
	 * @param vo
	 * @return
	 */
	public ArrayList<InfoPromocionPresupuestoServPrograma> obtenerValorDescuentoPromociones(HashMap<String, String> vo);
	
	/**
	 * 	CARGAR AVANZADO
	 */
	public  ArrayList<DtoPromocionesOdontologicas> consultaAvanzadaPromociones (DtoPromocionesOdontologicas dto);
	
	
	/**
	 * RETORNA LA CONSULTA PARA EL REPORTE 
	 * @param dto
	 * @return
	 */
	public String consultaAvanzadaPromocionesReporte (DtoPromocionesOdontologicas dto, boolean aplicaProgramas);
	
	
	/**
	 * METODO PARA GUARDAR LOS LOG DE BASE DE CONSULTAS AVANZADAS
	 * @param dto
	 * @return
	 */
	public double guardarLogConsulta(DtoPromocionesOdontologicas  dto, String tipoReporte, String ruta);
}
