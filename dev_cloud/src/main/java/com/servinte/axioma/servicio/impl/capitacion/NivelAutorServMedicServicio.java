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
	 * Método constructor de la clase
	 * @author, Fabian Becerra
	 */
	public NivelAutorServMedicServicio(){
		mundo = CapitacionFabricaMundo.crearNivelAutorServMedicMundo();
	}
	/**
	 * 
	 * Este Método se encarga de consultar el nivel de autorización por un servicio especifico
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
	 * Este Método se encarga de consultar el nivel de autorización por agrupación de servicios
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
	 * Este Método se encarga de consultar el nivel de autorización por un articulo especifico
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
	 * Este Método se encarga de consultar el nivel de autorización por agrupación articulos
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorAgrupacionMedicamentos(Articulo articulo, int codigoNivelAutorizacion,GrupoInventario grupoArticulo){
		return mundo.buscarNivelAutorizacionPorAgrupacionMedicamentos(articulo, codigoNivelAutorizacion,grupoArticulo);
	}
	
}
