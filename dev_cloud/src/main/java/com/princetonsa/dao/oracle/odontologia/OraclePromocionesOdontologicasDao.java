package com.princetonsa.dao.oracle.odontologia;

import java.util.ArrayList;
import java.util.HashMap;

import util.odontologia.InfoPromocionPresupuestoServPrograma;

import com.princetonsa.dao.odontologia.PromocionesOdontologicasDao;
import com.princetonsa.dto.odontologia.DtoPromocionesOdontologicas;
import com.princetonsa.dao.sqlbase.odontologia.SqlBasePromocionesOdontologicasDao;


/**
 * 
 * @author axioma
 *
 */
public class OraclePromocionesOdontologicasDao implements PromocionesOdontologicasDao {

	@Override
	public ArrayList<DtoPromocionesOdontologicas> cargar(
			DtoPromocionesOdontologicas dto) {
		return SqlBasePromocionesOdontologicasDao.cargar(dto);
	}

	@Override
	public boolean eliminar(DtoPromocionesOdontologicas dto) {
		return SqlBasePromocionesOdontologicasDao.eliminar(dto);
	}

	@Override
	public double guardar(DtoPromocionesOdontologicas dto) {
		return SqlBasePromocionesOdontologicasDao.guardar(dto);
	}

	@Override
	/**
	 * 
	 */
	public boolean modificar(DtoPromocionesOdontologicas dto) {
		return SqlBasePromocionesOdontologicasDao.modificar(dto);
	}

	@Override
	public boolean existeCruceFechas(DtoPromocionesOdontologicas dto,
			double codigoPkNotIn) {
		return SqlBasePromocionesOdontologicasDao.existeCruceFechas(dto, codigoPkNotIn);
	}

	@Override
	public double guardarLog(DtoPromocionesOdontologicas dto) {
		return  SqlBasePromocionesOdontologicasDao.guardarLog(dto);
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
	public ArrayList<InfoPromocionPresupuestoServPrograma> obtenerValorDescuentoPromociones(HashMap<String, String> vo)
	{
		return SqlBasePromocionesOdontologicasDao.obtenerValorDescuentoPromociones(vo);
	}

	
	
	@Override
	public ArrayList<DtoPromocionesOdontologicas> consultaAvanzadaPromociones(
			DtoPromocionesOdontologicas dto) {
		
		return SqlBasePromocionesOdontologicasDao.consultaAvanzadaPromociones(dto);
	}

	@Override
	public String consultaAvanzadaPromocionesReporte(DtoPromocionesOdontologicas dto, boolean aplicaProgramas) {
			return SqlBasePromocionesOdontologicasDao.consultaAvanzadaPromocionesReporte(dto, aplicaProgramas);
	}
	
	@Override
	public double guardarLogConsulta(DtoPromocionesOdontologicas dto, String tipoReporte, String ruta) {
		
		return SqlBasePromocionesOdontologicasDao.guardarLogConsulta(dto, tipoReporte, ruta);
	}
	
	
	
}
