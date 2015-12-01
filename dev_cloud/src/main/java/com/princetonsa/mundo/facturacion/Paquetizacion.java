/*
 * Jun 15, 2007
 * Proyect axioma
 * Paquete com.princetonsa.mundo.facturacion
 * @author Jorge Armando Osorio Velasquez
 * Compilador Java 1.5.0_07-b03
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.PaquetizacionDao;
import com.princetonsa.dto.manejoPaciente.DtoSolicitudesSubCuenta;

/**
 * @author Jorge Armando Osorio Velasquez
 *
 */
public class Paquetizacion 
{
	/**
	 * Objeto que maneja la conexion con la fuente de datos.
	 */
	PaquetizacionDao objetoDao;

	/**
	 * 
	 */
	public Paquetizacion() 
	{
		this.init(System.getProperty("TIPOBD"));
	}

	/**
	 * 
	 */
	private void init(String tipoBD) 
	{
		if(objetoDao==null)
		{
			DaoFactory myFactory=DaoFactory.getDaoFactory(tipoBD);
			objetoDao=myFactory.getPaquetizacionDao();
		}
	}

	/**
	 * Metodo que retorna los paquetes que ya han sido asignado al responsable.
	 * @param con 
	 * @param subCuenta
	 * @return
	 */
	public HashMap consultarPaquetesAsocioadosResponsableSubcuenta(Connection con, String subCuenta) 
	{
		return objetoDao.consultarPaquetesAsocioadosResponsableSubcuenta(con,subCuenta);
	}

	/**
	 * 
	 * @param con
	 * @param codigoPaquetizaciones
	 * @return
	 */
	public HashMap<String, Object> consultarServiciosPaquetes(Connection con, String[] codigoPaquetizaciones) 
	{
		return objetoDao.consultarServiciosPaquetes(con,codigoPaquetizaciones);
	}

	/**
	 * Metodo que retorna las solicitudes aptas para paquetizar
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public ArrayList<DtoSolicitudesSubCuenta> obtenerSolicitudesSubCuentaPaquetizar(Connection con, String subCuenta) 
	{
		return objetoDao.obtenerSolicitudesSubCuentaPaquetizar(con,subCuenta);
	}

	/**
	 * Metodo que retorna un mapa con las propiedades necesarias de un servicios, para verificar en las componentes del paquete
	 * @param con
	 * @param codServicio
	 * @return
	 */
	public HashMap obtenerParametrosServicio(Connection con, String codServicio) 
	{
		return objetoDao.obtenerParametrosServicio(con, codServicio);
	}

	/**
	 * 
	 * @param con
	 * @param servArti
	 * @return
	 */
	public HashMap obtenerParametrosArticulos(Connection con, String codArticulo) 
	{
		return objetoDao.obtenerParametrosArticulos(con, codArticulo);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public int insertarEncabezadoPaquetizacion(Connection con, HashMap vo) 
	{
		return objetoDao.insertarEncabezadoPaquetizacion(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param numeroSolicitud 
	 * @param string
	 * @return
	 */
	public boolean eliminarPaquetizacion(Connection con, String codPaquetizacion, String numeroSolicitud,boolean eliminarEncabezado) 
	{
		return objetoDao.eliminarPaquetizacion(con,codPaquetizacion,numeroSolicitud,eliminarEncabezado);
	}

	/**
	 * 
	 * @param con
	 * @param vo
	 * @return
	 */
	public boolean guardarDetallePaquete(Connection con, HashMap vo) 
	{
		return objetoDao.guardarDetallePaquete(con,vo);
	}

	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public HashMap consultarLiquidacionPaquete(Connection con, String subCuenta) 
	{
		return objetoDao.consultarLiquidacionPaquete(con,subCuenta);
	}

	/**
	 * 
	 * @param con
	 * @param evento 
	 * @param numeroSolicitud
	 * @param codServArt
	 * @param subCuenta 
	 * @param codigoDetPadre
	 * @param esServicio
	 */
	public boolean actualizarCantidadDetCargo(Connection con, int cantidad, String subCuenta,String numeroSolicitud, String codServArt, int codigoDetPadre, boolean esServicio,String facturado) 
	{
		return objetoDao.actualizarCantidadDetCargo(con,cantidad,subCuenta,numeroSolicitud,codServArt,codigoDetPadre,esServicio,facturado);
	}
	

	/**
	 * 
	 * @param con
	 * @param evento 
	 * @param numeroSolicitud
	 * @param codServArt
	 * @param subCuenta 
	 * @param codigoDetPadre
	 * @param esServicio
	 */
	public boolean actualizarCantidadDetCargoServicioCx(Connection con, int cantidad, String subCuenta,String numeroSolicitud, String servicio,String servicioCX,int tipoAsocio, int codigoDetPadre,String facturado,int detcxhonorarios,int detasicxsalasmat) 
	{
		return objetoDao.actualizarCantidadDetCargoServicioCx(con,cantidad,subCuenta,numeroSolicitud,servicio,servicioCX,tipoAsocio,codigoDetPadre,facturado,detcxhonorarios,detasicxsalasmat);
	}
	
	/**
	 * 
	 * @param con
	 * @param aplicarValidaciones 
	 * @param cuenta
	 * @return
	 */
	public HashMap<String, Object> consultarIngresos(Connection con, int codigoPersona, boolean aplicarValidaciones)
	{
		
		return objetoDao.consultarIngresos(con,codigoPersona,aplicarValidaciones);
		
	}
	
	
	

}
