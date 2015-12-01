package com.servinte.axioma.dao.interfaz.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoFiltroReportePromocionesOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReportePromocionesOdontologicas;
import com.servinte.axioma.orm.PromocionesOdontologicas;

public interface IPromocionesOdontologicasDAO {
	
	/**
	 * 
	 * 
	 */
	public ArrayList<PromocionesOdontologicas> listarPromocionesOdontologicas();

	public ArrayList<PromocionesOdontologicas> listarTodosPorCiudad(
			String codigoCiudad, String codigoPais, String codigoDto);
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYCiudad(
			int centroAtencion, String codigoCiudad, String codigoPais, String codigoDto );

	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYRegion(
			int centroAtencion, long codigoRegion);
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorRegion(long codigoRegion );
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencion(int centroAtencion);
	
	/**
	 * Este m�todo se encarga de consultar las promociones odontol�gicas en el sistema
	 * y ordenarlas para generar el reporte de promociones odontol�gicas
	 * @param filtroPromocion
	 * @return ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consultarPromocionesOdontologicas(
			DtoFiltroReportePromocionesOdontologicas filtroPromocion);
}
