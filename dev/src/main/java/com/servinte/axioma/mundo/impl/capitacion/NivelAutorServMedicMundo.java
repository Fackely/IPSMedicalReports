package com.servinte.axioma.mundo.impl.capitacion;

import java.util.ArrayList;

import com.princetonsa.dto.capitacion.DTONivelAutorServMedic;
import com.princetonsa.dto.facturacion.DtoServicios;
import com.servinte.axioma.dao.fabrica.capitacion.CapitacionFabricaDAO;
import com.servinte.axioma.dao.interfaz.capitacion.INivelAutorServMedicDAO;
import com.servinte.axioma.mundo.interfaz.capitacion.INivelAutorServMedicMundo;
import com.servinte.axioma.orm.Articulo;
import com.servinte.axioma.orm.GrupoInventario;

public class NivelAutorServMedicMundo implements INivelAutorServMedicMundo {
	
	INivelAutorServMedicDAO dao;
	
	/**
	 * 
	 * Método constructor de la clase
	 * @author, Fabian Becerra
	 */
	public NivelAutorServMedicMundo() {
		dao = CapitacionFabricaDAO.crearNivelAutorServMedicDAO();
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
		return dao.buscarNivelAutorizacionPorServicioEspecifico(codigoNivelAutorizacion, codigoServicio);
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
		return dao.buscarNivelAutorizacionPorAgrupacionServicios(servicio, codigoNivelAutorizacion);
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
		return dao.buscarNivelAutorizacionPorArticuloEspecifico(codigoNivelAutorizacion, codigoArticulo);
	}
	
	/**
	 * 
	 * Este Método se encarga de consultar el nivel de autorización por agrupación de articulos
	 * 
	 * @return DTONivelAutorServMedic
	 * @author, Fabian Becerra
	 *
	 */
	public ArrayList<DTONivelAutorServMedic> buscarNivelAutorizacionPorAgrupacionMedicamentos(Articulo articulo, int codigoNivelAutorizacion, GrupoInventario grupoArticulo){
		return dao.buscarNivelAutorizacionPorAgrupacionMedicamentos(articulo, codigoNivelAutorizacion, grupoArticulo);
	}
}
