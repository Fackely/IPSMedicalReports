package com.princetonsa.dao.postgresql.facturacion;

import java.sql.Connection;
import java.util.HashMap;
import com.princetonsa.dao.facturacion.Servicios_ArticulosIncluidosEnOtrosProcedimientosDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao;
import com.princetonsa.dto.facturacion.DtoArticuloIncluidoSolProc;
import com.princetonsa.dto.facturacion.DtoServicioIncluidoSolProc;


/**
 * @author Juan Alejandro Cardona
 * @date: Septiembre de 2008
 */

/** * */
public class PostgresqlServicios_ArticulosIncluidosEnOtrosProcedimientosDao implements Servicios_ArticulosIncluidosEnOtrosProcedimientosDao {

	
	public HashMap consultarServicios_ArticulosIncluidosEnOtrosProcedimientos(Connection con, int codigoInstitucionInt, String tarifarioServicio) {
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.consultarServicios_ArticulosIncluidosEnOtrosProcedimientos(con, codigoInstitucionInt, tarifarioServicio);
	}

	/**	 *	 */
	public HashMap cargarArtIncluServiPpal(Connection con, int criterios) {
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.cargarArtIncluServiPpal(con, criterios);
	}
	
	/**	 *	 */
	public HashMap cargarServiIncluServiPpal(Connection con, int criterios, int tarifario) {
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.cargarServiIncluServiPpal(con, criterios, tarifario);
	}
	
	/**	 *	 */
	public HashMap cargarArtIncluServiInclu(Connection con, int criterios) {
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.cargarArtIncluServiInclu(con, criterios);
	}

	/**
	 * 
	 */
	public boolean insertarServicios_ArticulosIncluidosEnOtrosProcedimientos(Connection con, HashMap criterios) {
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.insertarServicios_ArticulosIncluidosEnOtrosProcedimientos(con, criterios);
	}

	/**
	 * 
	 */
	public boolean insertarServiciosIncluidosEnServicioPrincipal(Connection con, String codigoServicioPrincipal, String codigoServicioIncluido, String centroCosto, String cantidad)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.insertarServiciosIncluidosEnServicioPrincipal(con, codigoServicioPrincipal, codigoServicioIncluido, centroCosto, cantidad);
	}
	
	/**
	 * 
	 */
	public boolean insertarArticulosIncluidosEnServicioPrincipal(Connection con, String codigoServicioPrincipal, String codigoArticuloIncluido, String farmacia, String cantidad)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.insertarArticulosIncluidosEnServicioPrincipal(con, codigoServicioPrincipal, codigoArticuloIncluido, farmacia, cantidad);
	}
	
	/**
	 * 
	 */
	public boolean actualizarServiciosIncluidosEnServicioPrincipal(Connection con, int codigoServicioIncluido, int centroCosto, int cantidad)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.actualizarServiciosIncluidosEnServicioPrincipal(con, codigoServicioIncluido, centroCosto, cantidad);
	}
	
	/**
	 * 
	 */
	public boolean actualizarArticulosIncluidosEnServicioPrincipal(Connection con, int codigoServicioIncluido, int farmacia, int cantidad)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.actualizarArticulosIncluidosEnServicioPrincipal(con, codigoServicioIncluido, farmacia, cantidad);
	}
	
	/**
	 * 
	 */
	public boolean actualizarServicioPrincipal(Connection con, HashMap<String, Object> criterios)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.actualizarServicioPrincipal(con, criterios);
	}
	
	/**
	 * 
	 */
	public boolean eliminarServicioIncluido(Connection con, int codigoServicioIncluido)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.eliminarServicioIncluido(con, codigoServicioIncluido);
	}
	
	/**
	 * 
	 */
	public boolean eliminarArticuloIncluido(Connection con, int codigoServicioIncluido)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.eliminarArticuloIncluido(con, codigoServicioIncluido);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoServicio
	 * @param activo
	 * @param institucion
	 * @return
	 */
	public int obtenerCodigoPkServicioIncluye(Connection con, int codigoServicio, boolean activo, int institucion)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.obtenerCodigoPkServicioIncluye(con, codigoServicio, activo, institucion);
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean insertarServiciosIncluidosSolicitudProcedimientos(Connection con, DtoServicioIncluidoSolProc dto)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.insertarServiciosIncluidosSolicitudProcedimientos(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean insertarArticulosIncluidosSolicitudProcedimientos(Connection con, DtoArticuloIncluidoSolProc dto)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.insertarArticulosIncluidosSolicitudProcedimientos(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param solicitudPpal
	 * @param articulo
	 * @param cantidad
	 * @param usuario
	 * @return
	 */
	public boolean modificarCantidadArticulosIncluidosSolicitudProcedimientos(Connection con, int solicitudPpal, int articulo, int cantidad, String usuario)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.modificarCantidadArticulosIncluidosSolicitudProcedimientos(con, solicitudPpal, articulo, cantidad, usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param solicitudPpal
	 * @param articulo
	 * @param solicitudIncluida
	 * @param usuario
	 * @return
	 */
	public boolean modificarSolicitudInclArticulosIncluidosSolicitudProcedimientos(Connection con, int solicitudPpal, int articulo, int solicitudIncluida, String usuario)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.modificarSolicitudInclArticulosIncluidosSolicitudProcedimientos(con, solicitudPpal, articulo, solicitudIncluida, usuario);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean solicitudIncluyeServiciosArticulos(Connection con, int numeroSolicitud)
	{
		String consulta="SELECT getSolicitudTieneIncluidos(?) ";
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.solicitudIncluyeServiciosArticulos(con, numeroSolicitud, consulta);
	}





//metodo insertar articulo en servinclu
	public boolean insertarArticulosIncluidosEnServicioIncluido(Connection con, String codigoServicioIncluido, String codigoArticuloIncluido, String farmacia, String cantidad)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.insertarArticulosIncluidosEnServicioIncluido(con, codigoServicioIncluido, codigoArticuloIncluido, farmacia, cantidad);
	}

//actualiza datos
	public boolean actualizarArticulosIncluidosEnServicioIncluido(Connection con, int codigoServicioIncluido, int farmacia, int cantidad)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.actualizarArticulosIncluidosEnServicioIncluido(con, codigoServicioIncluido, farmacia, cantidad);
	}

//eliminar
	public boolean eliminarArticuloIncluidoServinclu(Connection con, int codigoServicioIncluido)
	{
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.eliminarArticuloIncluidoServinclu(con, codigoServicioIncluido);
	}


// consultar servicios incluidos del sistema o de un ppal en especifico	
	public HashMap consultarServIncluUsados(Connection con, int especifico, int codpertenece){
		return SqlBaseServicios_ArticulosIncluidosEnOtrosProcedimientosDao.consultarServIncluUsados(con, especifico, codpertenece);
	}
	
	
	
	
}