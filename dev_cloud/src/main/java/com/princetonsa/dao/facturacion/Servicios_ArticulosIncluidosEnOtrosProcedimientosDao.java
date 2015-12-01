package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.facturacion.SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao;
import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.facturacion.DtoServicioIncluidoSolProc;

/**
 * @author Juan Alejandro Cardona
 * Fecha: Septiembre de 2008
 */

public interface Servicios_ArticulosIncluidosEnOtrosProcedimientosDao {

	/**	 * @param (con, codigoInstitucionInt)
	 * @return	 */
	public HashMap consultarServicios_ArticulosIncluidosEnOtrosProcedimientos(Connection con, int codigoInstitucionInt, String tarifarioServicio);

	
	/** * @param (con, criterios)
	 * @return	 */
	public HashMap cargarArtIncluServiPpal(Connection con, int criterios);


	/** * @param (con, criterios)
	 * @return	 */
	public HashMap cargarServiIncluServiPpal(Connection con, int criterios, int tarifario);


	/** * @param (con, Criterios)
	 * @return  */
	public HashMap cargarArtIncluServiInclu(Connection con, int criterios);
	

	/**	 * @param (con, criterios)
	 * @return	 */
	public boolean insertarServicios_ArticulosIncluidosEnOtrosProcedimientos(Connection con, HashMap criterios);

	/**
	 * @param con
	 * @param codigoServicioPrincipal
	 * @param codigoServicioIncluido
	 * @param centroCosto
	 * @param cantidad
	 * @return
	 */
	public boolean insertarServiciosIncluidosEnServicioPrincipal(Connection con, String codigoServicioPrincipal, String codigoServicioIncluido, String centroCosto, String cantidad);
	
	/**
	 * @param con
	 * @param codigoServicioPrincipal
	 * @param codigoArticuloIncluido
	 * @param farmacia
	 * @param cantidad
	 * @return
	 */
	public boolean insertarArticulosIncluidosEnServicioPrincipal(Connection con, String codigoServicioPrincipal, String codigoArticuloIncluido, String farmacia, String cantidad);
	
	/**
	 * @param con
	 * @param codigoServicioPrincipal
	 * @param codigoServicioIncluido
	 * @param centroCosto
	 * @param cantidad
	 * @return
	 */
	public boolean actualizarServiciosIncluidosEnServicioPrincipal(Connection con, int codigoServicioIncluido, int centroCosto, int cantidad);
	
	/**
	 * @param con
	 * @param codigoServicioPrincipal
	 * @param codigoArticuloIncluido
	 * @param farmacia
	 * @param cantidad
	 * @return
	 */
	public boolean actualizarArticulosIncluidosEnServicioPrincipal(Connection con, int codigoServicioIncluido, int farmacia, int cantidad);
	
	/**
	 * @param con
	 * @param criterios
	 * @return
	 */
	public boolean actualizarServicioPrincipal(Connection con, HashMap<String, Object> criterios);
	
	/**
	 * @param con
	 * @param codigoServicioIncluido
	 * @return
	 */
	public boolean eliminarServicioIncluido(Connection con, int codigoServicioIncluido);

	/**
	 * @param con
	 * @param codigoServicioIncluido
	 * @return
	 */
	public boolean eliminarArticuloIncluido(Connection con, int codigoServicioIncluido);
	
	/**
	 * @param con
	 * @param codigoServicio
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int obtenerCodigoPkServicioIncluye(Connection con, int codigoServicio, boolean activo, int institucion);
	
	/**
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean insertarServiciosIncluidosSolicitudProcedimientos(Connection con, DtoServicioIncluidoSolProc dto);
	
	/**
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean insertarArticulosIncluidosSolicitudProcedimientos(Connection con, DtoArticuloIncluidoSolProc dto);
	
	/**
	 * 
	 * @param con
	 * @param solicitudPpal
	 * @param articulo
	 * @param cantidad
	 * @param usuario
	 * @return
	 */
	public boolean modificarCantidadArticulosIncluidosSolicitudProcedimientos(Connection con, int solicitudPpal, int articulo, int cantidad, String usuario);
	
	/**
	 * 
	 * @param con
	 * @param solicitudPpal
	 * @param articulo
	 * @param solicitudIncluida
	 * @param usuario
	 * @return
	 */
	public boolean modificarSolicitudInclArticulosIncluidosSolicitudProcedimientos(Connection con, int solicitudPpal, int articulo, int solicitudIncluida, String usuario);

	/**
	 * @param con
	 * @param numeroSolicitud
	 * @return	 */
	public boolean solicitudIncluyeServiciosArticulos(Connection con, int numeroSolicitud);



// insertar
	public boolean insertarArticulosIncluidosEnServicioIncluido(Connection con, String codigoServicioIncluido, String codigoArticuloIncluido, String farmacia, String cantidad);
	
//actualizar
	public boolean actualizarArticulosIncluidosEnServicioIncluido(Connection con, int codigoServicioIncluido, int farmacia, int cantidad);

	//eliminar
	public boolean eliminarArticuloIncluidoServinclu(Connection con, int codigoServicioIncluido);



/**
 * @param con, especifico, codpertenece
 * @return */
	public HashMap consultarServIncluUsados(Connection con, int especifico, int codpertenece);






}