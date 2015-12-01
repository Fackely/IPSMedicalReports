package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 
 * @author Julio Hernandez jfhernandez@axioma-md.com
 *
 */

public interface GeneracionTarifasPendientesEntSubDao
{
	public HashMap buscarAutorizaciones(Connection con, HashMap filtros);
	
	public HashMap buscarServiciosAutorizacion(Connection con, String autorizacion);
	
	public HashMap obtenerErroresServicioAut(Connection con, String autorizacion);
	
	public HashMap buscarArticulosPedidos(Connection con, HashMap filtros);
	
	public HashMap buscarArticulosSolicitudes(Connection con, HashMap filtros);
	
	public HashMap obtenerErroresPedidos(Connection con, String codigo);
	
	public HashMap obtenerErroresSolicitudes(Connection con, String codigo);
	
	public HashMap obtenerServiciosTarifados(Connection con, HashMap servicios);
	
	public HashMap obtenerPedidosTarifados(Connection con, HashMap pedidos);
	
	public HashMap obtenerSolicitudesTarifados(Connection con, HashMap solicitudes);
}