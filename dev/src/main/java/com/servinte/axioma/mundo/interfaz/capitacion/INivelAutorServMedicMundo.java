package com.servinte.axioma.mundo.interfaz.capitacion;


import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorServMedic;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.GrupoInventario;

public interface INivelAutorServMedicMundo {
	
	/**
	 * 
	 * Este M�todo se encarga de consultar el nivel de autorizaci�n por un servicio especifico
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorServicioEspecifico(int codigoNivelAutorizacion,int codigoServicio);
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar el nivel de autorizaci�n por agrupaci�n de servicios
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorAgrupacionServicios(DtoServicios servicio, int codigoNivelAutorizacion);
	
	/**
	 * 
	 * Este M�todo se encarga de consultar el nivel de autorizaci�n por un articulo especifico
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorArticuloEspecifico(int codigoNivelAutorizacion,int codigoArticulo);
	
	/**
	 * 
	 * Este M�todo se encarga de consultar el nivel de autorizaci�n 
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorAgrupacionMedicamentos(Articulo articulo, int codigoNivelAutorizacion,GrupoInventario grupoArticulo);
		
}
