package com.servinte.axioma.servicio.interfaz.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoFiltroReportePromocionesOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReportePromocionesOdontologicas;
import com.servinte.axioma.orm.PromocionesOdontologicas;


public interface IPromocionesOdontologicasServicio {
	/**
	 * 
	 * 
	 */
	public ArrayList<PromocionesOdontologicas> listarPromocionesOdontologicas();

	public ArrayList<PromocionesOdontologicas> listarTodosPorCiudad(String codigoCiudad, String codigoPais, String codigoDto); 
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYCiudad(int centroAtencion, String codigoCiudad, String codigoPais, String codigoDto );
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYRegion(int centroAtencion, long codigoRegion);
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorRegion(long codigoRegion );
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencion(int centroAtencion);
	
	
	/**
	 * Este método se encarga de consultar las promociones odontológicas en el sistema
	 * y ordenarlas para generar el reporte de promociones odontológicas en formato pdf y excel
	 * @param filtroPromocion
	 * @return ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consolidarInfoReportePromociones(
			DtoFiltroReportePromocionesOdontologicas filtroPromocion);
	
	/**
	 * Este método se encarga de consultar las promociones odontológicas en el sistema
	 * y ordenarlas para generar el reporte de promociones odontológicas en archivo plano
	 * @param filtroPromocion
	 * @return ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consolidarInfoReportePromocionesPlano(
			DtoFiltroReportePromocionesOdontologicas filtroPromocion);
	
}
