package com.servinte.axioma.dao.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorServMedic;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorServMedicDAO;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.delegate.capitacion.NivelAutorServMedicDelegate;

public class NivelAutorServMedicHibernateDao implements INivelAutorServMedicDAO{
	
	NivelAutorServMedicDelegate delegate;
	
	public NivelAutorServMedicHibernateDao() {
		delegate = new NivelAutorServMedicDelegate();
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar el nivel de autorizaci�n por un servicio especifico
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorServicioEspecifico(int codigoNivelAutorizacion,int codigoServicio){
		return delegate.buscarNivelAutorizacionPorServicioEspecifico(codigoNivelAutorizacion,codigoServicio);
	}

	
	/**
	 * 
	 * Este M�todo se encarga de consultar el nivel de autorizaci�n por agrupaci�n de servicios
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorAgrupacionServicios(DtoServicios servicio, int codigoNivelAutorizacion){
		return delegate.buscarNivelAutorizacionPorAgrupacionServicios(servicio, codigoNivelAutorizacion);
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar el nivel de autorizaci�n por un articulo especifico
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorArticuloEspecifico(int codigoNivelAutorizacion,int codigoArticulo){
		return delegate.buscarNivelAutorizacionPorArticuloEspecifico(codigoNivelAutorizacion, codigoArticulo);
	}
	
	/**
	 * 
	 * Este M�todo se encarga de consultar el nivel de autorizaci�n 
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorAgrupacionMedicamentos(Articulo articulo, int codigoNivelAutorizacion,GrupoInventario grupoArticulo){
		return delegate.buscarNivelAutorizacionPorAgrupacionMedicamentos(articulo, codigoNivelAutorizacion,grupoArticulo);
	}
}
