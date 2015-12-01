package com.servinte.axioma.servicio.impl.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoFiltroReportePromocionesOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReportePromocionesOdontologicas;
import com.servinte.axioma.mundo.fabrica.odontologia.PromocionesOdontologicasFabricaMundo;
import com.servinte.axioma.mundo.interfaz.odontologia.IPromocionesOdontologicasMundo;
import com.servinte.axioma.orm.PromocionesOdontologicas;
import com.servinte.axioma.servicio.interfaz.odontologia.IPromocionesOdontologicasServicio;

public class PromocionesOdontologicasServicio implements IPromocionesOdontologicasServicio {
	
IPromocionesOdontologicasMundo mundo;
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Javier Gonzalez
	 */
	public PromocionesOdontologicasServicio() {
		mundo = PromocionesOdontologicasFabricaMundo.crearPromocionesOdontologicasMundo();
	}
	
	@Override
	public ArrayList<PromocionesOdontologicas> listarPromocionesOdontologicas() {
		return mundo.listarPromocionesOdontologicas();
	}

	@Override
	public ArrayList<PromocionesOdontologicas> listarTodosPorCiudad(String codigoCiudad, String codigoPais, String codigoDto) {
		return mundo.listarTodosPorCiudad(codigoCiudad,codigoPais,codigoDto);
	}

	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYCiudad(int centroAtencion, String codigoCiudad, String codigoPais, String codigoDto ){
		return mundo.listarTodosPorCentroAtencionYCiudad(centroAtencion, codigoCiudad, codigoPais, codigoDto);
	}
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYRegion(int centroAtencion, long codigoRegion){
		return mundo.listarTodosPorCentroAtencionYRegion(centroAtencion, codigoRegion);
	}

	@Override
	public ArrayList<PromocionesOdontologicas> listarTodosPorRegion(
			long codigoRegion) {
		return mundo.listarTodosPorRegion(codigoRegion);
	}
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencion(int centroAtencion){
		return mundo.listarTodosPorCentroAtencion(centroAtencion);
	}
	
	/**
	 * Este método se encarga de consultar las promociones odontológicas en el sistema
	 * y ordenarlas para generar el reporte de promociones odontológicas en formato pdf y excel
	 * @param filtroPromocion
	 * @return ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consolidarInfoReportePromociones(
			DtoFiltroReportePromocionesOdontologicas filtroPromocion){
		return mundo.consolidarInfoReportePromociones(filtroPromocion);
	}
	
	/**
	 * Este método se encarga de consultar las promociones odontológicas en el sistema
	 * y ordenarlas para generar el reporte de promociones odontológicas en archivo plano
	 * @param filtroPromocion
	 * @return ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consolidarInfoReportePromocionesPlano(
			DtoFiltroReportePromocionesOdontologicas filtroPromocion){
		return mundo.consolidarInfoReportePromocionesPlano(filtroPromocion);
	}
}
