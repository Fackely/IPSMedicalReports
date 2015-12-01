package com.princetonsa.dao.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Julio Hernandez jfhernandez@axioma-md.com
 *
 */

public interface IngresarModificarContratosEntidadesSubcontratadasDao
{
	public ArrayList obtenerEntidades(Connection con);
	
	public ArrayList obtenerClaseInventarios(Connection con);
	
	public ArrayList obtenerEsquemas(Connection con);
	
	public ArrayList obtenerGruposServicio(Connection con, String activos);
	
	public ArrayList obtenerEsquemasProcedimientos(Connection con);
	
	public HashMap obtenerInfoXEntidadInv(Connection con, String entidad);
	
	public HashMap obtenerInfoXEntidadServ(Connection con, String entidad);
	
	public HashMap consultaInfoXEntidadEncabezado(Connection con, String entidad);
	
	public long guardarEncabezado(Connection con, HashMap encabezadoEntidad);
	
	public boolean guardarEsquemasInventarios(Connection con, HashMap esquemasInventarios);
	
	public boolean guardarEsquemasServicios(Connection con, HashMap esquemasServicios);
	
	public HashMap consultaContratos(Connection con, HashMap encabezado, HashMap inventarios, HashMap servicios);
	
	public HashMap consultaEsquemasInventarios(Connection con);
	
	public HashMap consultaEsquemasServicios(Connection con);
	
	public boolean actualizarEncabezado(Connection con, HashMap encabezado);
	
	public boolean eliminarEsquemasInventarios(Connection con, HashMap datos);
	
	public boolean eliminarEsquemasServicios(Connection con, HashMap datos);
	
	public boolean verificarTraslapeContratos(Connection con, HashMap encabezadoEntidad);
	
	public boolean comprobarExistenciaInventario(Connection con, HashMap inventarios, int indice);
}