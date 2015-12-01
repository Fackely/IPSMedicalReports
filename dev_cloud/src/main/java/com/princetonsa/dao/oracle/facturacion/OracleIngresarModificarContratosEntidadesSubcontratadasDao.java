package com.princetonsa.dao.oracle.facturacion;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.facturacion.IngresarModificarContratosEntidadesSubcontratadasDao;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao;
/**
 * 
 * @author Julio Hernandez jfhernandez@axioma-md.com
 *
 */
public class OracleIngresarModificarContratosEntidadesSubcontratadasDao implements IngresarModificarContratosEntidadesSubcontratadasDao {
	
	public ArrayList obtenerEntidades(Connection con) {
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.obtenerEntidades(con);
	}
	
	public ArrayList obtenerClaseInventarios(Connection con) {
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.obtenerClaseInventarios(con);
	}
	
	public ArrayList obtenerEsquemas(Connection con) {
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.obtenerEsquemas(con);
	}
	
	public ArrayList obtenerGruposServicio(Connection con, String activos) {
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.obtenerGruposServicio(con, activos);
	}
	
	public ArrayList obtenerEsquemasProcedimientos(Connection con) {
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.obtenerEsquemasProcedimientos(con);
	}
	
	public HashMap obtenerInfoXEntidadInv(Connection con, String entidad) {
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.obtenerInfoXEntidadInv(con, entidad);
	}
	
	public HashMap obtenerInfoXEntidadServ(Connection con, String entidad) {
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.obtenerInfoXEntidadServ(con, entidad);
	}
	
	public HashMap consultaInfoXEntidadEncabezado(Connection con, String entidad) {
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.consultaInfoXEntidadEncabezado(con, entidad);
	}

	public long guardarEncabezado(Connection con, HashMap encabezadoEntidad){
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.guardarEncabezado(con, encabezadoEntidad);
	}
	
	public boolean guardarEsquemasInventarios(Connection con, HashMap esquemasInventarios){
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.guardarEsquemasInventarios(con, esquemasInventarios);
	}
	
	public boolean guardarEsquemasServicios(Connection con, HashMap esquemasServicios){
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.guardarEsquemasServicios(con, esquemasServicios);
	}
	
	public HashMap consultaContratos(Connection con, HashMap encabezado, HashMap inventarios, HashMap servicios)
	{
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.consultaContratos(con, encabezado,inventarios,servicios);
	}
	
	public HashMap consultaEsquemasInventarios(Connection con)
	{
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.consultarEsquemasInventarios(con);
	}
	
	public HashMap consultaEsquemasServicios(Connection con)
	{
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.consultaEsquemasServicios(con);
	}
	
	public boolean actualizarEncabezado(Connection con, HashMap encabezado)
	{
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.actualizarEncabezado(con, encabezado);
	}
	
	public boolean eliminarEsquemasInventarios(Connection con, HashMap datos)
	{
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.eliminarEsquemasInventarios(con, datos);
	}
	
	public boolean eliminarEsquemasServicios(Connection con, HashMap datos)
	{
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.eliminarEsquemasServicios(con, datos);
	}
	
	public boolean verificarTraslapeContratos(Connection con, HashMap encabezado)
	{
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.verificarTraslapeContratos(con, encabezado);
	}
	
	public boolean comprobarExistenciaInventario(Connection con, HashMap inventarios, int indice)
	{
		return SqlBaseIngresarModificarContratosEntidadesSubcontratadasDao.comprobarExistenciaInventario(con,inventarios,indice);
	}
}