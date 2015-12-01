package com.servinte.axioma.dao.impl.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoFiltroReportePromocionesOdontologicas;
import com.princetonsa.dto.odontologia.DtoResultadoConsultaReportePromocionesOdontologicas;
import com.servinte.axioma.dao.interfaz.odontologia.IPromocionesOdontologicasDAO;
import com.servinte.axioma.orm.PromocionesOdontologicas;
import com.servinte.axioma.orm.delegate.odontologia.PromocionesOdontologicasDelegate;

public class PromocionesOdontologicasHibernateDAO implements IPromocionesOdontologicasDAO{
	
private PromocionesOdontologicasDelegate delegate;
	
	/**
	 * M&eacute;todo constructor de la clase 
	 *
	 * @author Javier Gonzalez
	 */
	public PromocionesOdontologicasHibernateDAO() {
		delegate = new PromocionesOdontologicasDelegate();
	}
	
	@Override
	public ArrayList<PromocionesOdontologicas> listarPromocionesOdontologicas() {
		return delegate.listarPromocionesOdontologicas();
	}

	@Override
	public ArrayList<PromocionesOdontologicas> listarTodosPorCiudad(
			String codigoCiudad, String codigoPais, String codigoDto) {
		return delegate.listarTodosPorCiudad(codigoCiudad, codigoPais, codigoDto);
	}	

	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYCiudad(int centroAtencion, String codigoCiudad, String codigoPais, String codigoDto ){
		return delegate.listarTodosPorCentroAtencionYCiudad(centroAtencion, codigoCiudad, codigoPais, codigoDto);
	}
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencionYRegion(int centroAtencion, long codigoRegion){
		return delegate.listarTodosPorCentroAtencionYRegion(centroAtencion, codigoRegion);
	}
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorRegion(long codigoRegion ){
		return delegate.listarTodosPorRegion(codigoRegion);
	}
	
	public ArrayList<PromocionesOdontologicas> listarTodosPorCentroAtencion(int centroAtencion){
		return delegate.listarTodosPorCentroAtencion(centroAtencion);
	}
	
	/**
	 * Este método se encarga de consultar las promociones odontológicas en el sistema
	 * y ordenarlas para generar el reporte de promociones odontológicas
	 * @param filtroPromocion
	 * @return ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas>
	 * @author Fabian Becerra
	 */
	public ArrayList<DtoResultadoConsultaReportePromocionesOdontologicas> consultarPromocionesOdontologicas(
			DtoFiltroReportePromocionesOdontologicas filtroPromocion){
		return delegate.consultarPromocionesOdontologicas(filtroPromocion);
	}
}
