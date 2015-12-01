package com.servinte.axioma.servicio.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorServMedic;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.mundo.fabrica.capitacion.CapitacionFabricaMundo;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorServMedicMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.servicio.interfaz.capitacion.INivelAutorServMedicServicio;

public class NivelAutorServMedicServicio implements INivelAutorServMedicServicio {

	
	INivelAutorServMedicMundo mundo;
	
	/**
	 * 
	 * M�todo constructor de la clase
	 * @author, Fabian Becerra
	 */
	public NivelAutorServMedicServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorServMedicMundo();
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
		return mundo.buscarNivelAutorizacionPorServicioEspecifico(codigoNivelAutorizacion, codigoServicio);
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
		return mundo.buscarNivelAutorizacionPorAgrupacionServicios(servicio, codigoNivelAutorizacion);
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
		return mundo.buscarNivelAutorizacionPorArticuloEspecifico(codigoNivelAutorizacion, codigoArticulo);
	}
	
	
	/**
	 * 
	 * Este M�todo se encarga de consultar el nivel de autorizaci�n por agrupaci�n articulos
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorAgrupacionMedicamentos(Articulo articulo, int codigoNivelAutorizacion,GrupoInventario grupoArticulo){
		return mundo.buscarNivelAutorizacionPorAgrupacionMedicamentos(articulo, codigoNivelAutorizacion,grupoArticulo);
	}
	
}
