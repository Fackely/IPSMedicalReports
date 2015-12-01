package com.servinte.axioma.servicio.interfaz.odontologia.unidadagendaserviciotipocitaodonto;

import java.util.ArrayList;

import com.servinte.axioma.dto.consultaExterna.DtoUnidadesConsulta;
import com.servinte.axioma.orm.UnidadesConsulta;


/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public interface IUnidadesConsultaServicio {
	
	/**
	 * Metodo que cargar  la unidades de consulta por codigoPk
	 * Recibe el codigoPk de la unidad de Consulta
	 * @author Edgar Carvajal Ruiz
	 * @param codigoUnidadConsulta
	 * @return
	 */
	public UnidadesConsulta buscarUnidadConsultaId(int codigoUnidadConsulta);

	/**
	 * 
	 * @return
	 */
	public ArrayList<DtoUnidadesConsulta> cargarUnidadesConsultaTipoEspecialidad(String tipo, int codigoEspecialidad,int codigoCentroAtencion,boolean filtrarActivas);

}
