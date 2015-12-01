/*
 * Creado en 18/08/2004
 *
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dto.administracion.DtoAreaAperturaCuentaAutoPYP;
import com.princetonsa.dto.administracion.DtoFirmasValoresPorDefecto;

/**
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public interface ValoresPorDefectoDao
{
	/**
	 * Método para modificar un parametro de valores por defecto
	 * @param con Conexión con la fuente de datos
	 * @param parametro
	 * @param nombre
	 * @param valor
	 * @param institucion
	 * @return true si se modifico correctamente, false de lo contrario
	 */
	public boolean modificar(Connection con, String parametro, String nombre, String valor, int institucion);

	/**
	 * Método para cargar todos los valores por defecto
	 * @param con Conexión con la fuente de datos 
	 * @return Collection con todos los valores por defecto
	 */
	public Collection cargar(Connection con);
	
	/**
	 * Método que que carga el código cie válido 
	 * en la aplicación
	 * 
	 * @param con Conexión con la fuente de datos
	 * @return
	 */
	public int cargarCodigoTipoCieActual (Connection con);
	/**
	 * Metodo para cargar las etiquetas de
	 * parametros generales
	 * @param con Connection
	 * @param modulo int
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap cargarEtiquetas(Connection con,int modulo);
	/**
	 * metodo para generar el resumen de parametros
	 * @param con Connection
	 * @param modulo int
	 * @param institucion int 
	 * @return HashMap
	 * @author jarloc
	 */
	public HashMap cargarResumenValores(Connection con,int modulo,int institucion);

	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap cargarIntegridadDominio(Connection con);
	
	/**
	 * @param con
	 * @return
	 */
	public HashMap consultarCentrosCostoTerceros(Connection con);
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarCentrosCostoTercero(Connection con, HashMap mapa);
	
	/**
	 * 
	 * @param con
	 * @return
	 */
	public HashMap consultarHorasReproceso(Connection con);
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarHorasReproceso(Connection con, HashMap mapa);
	
	/**
	 * Método encargado de ejecutar al consulta consultarSiExisteFacturaVaria
	 * y retorna el value object si hay registros en la tabla: facturas_varias
	 * @author Felipe Pérez Granda
	 * @param con
	 * @return HashMap Cargar Value Object
	 */
	public HashMap consultarFacturasVarias(Connection con);

	/**
	 * @param con
	 * @return
	 */
	public HashMap consultarClasesInventariosPaqMatQx(Connection con);

	/**
	 * @param con
	 * @param mapa
	 * @return
	 */
	public boolean insertarClasesInventariosPaqMatQx(Connection con, HashMap mapa);

	/**
	 * Método encargado de consultar si existe el datos seHanUtilizadoMedicamentosPosEnElTratamientoDelPaciente
	 * @author Felipe Pérez Granda
	 * @param con
	 * @param numSolicitud
	 * @param codArticulo
	 * @param esOrdenAmbulatoria
	 * @return HashMap
	 */
	public HashMap<String,Object> utilizaMedicamentosTratamientoPaciente(Connection con, String numSolicitud, String codArticulo, boolean esOrdenAmbulatoria);
	
	//Anexo 922
	/**
	 * 
	 */
	public ResultadoBoolean insertarFirmasValoresPorDefecto(DtoFirmasValoresPorDefecto dto);
	
	/**
	 * 
	 */
	public ArrayList<DtoFirmasValoresPorDefecto> consultarFirmasValoresPorDefecto(DtoFirmasValoresPorDefecto dto);
	
	/*
	 * 
	 */
	public ResultadoBoolean eliminarFirma(DtoFirmasValoresPorDefecto dto);
	
	/**
	 * 
	 */
	public boolean existeAgendaOdon(); 
	
	/**
	 * 
	 */
	public boolean existePresupesto();
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean guardarAreaAperturaCuentaAutoPYP(DtoAreaAperturaCuentaAutoPYP dto);

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean eliminarAreaAperturaCuentaAutoPYP(DtoAreaAperturaCuentaAutoPYP dto);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoAreaAperturaCuentaAutoPYP> consultarAreasAperturaCuentaAutoPYP(int institucion, int centroAtencion);

	/**
	 * 
	 * @param institucion
	 * @param con
	 * @return
	 */
	public ArrayList<Integer> consultarServiciosManejoTransPrimario(int institucion,Connection con);

	/**
	 * 
	 * @param institucion
	 * @param con
	 * @return
	 */
	public ArrayList<Integer> consultarServiciosManejoTransSecundario(int institucion,Connection con);

	
	/**
	 * 
	 * @param con
	 * @param servicios
	 * @return
	 */
	public boolean insertarServiciosManejoTransPrimario(Connection con,ArrayList<Integer> servicios,int institucion);

	/**
	 * 
	 * @param con
	 * @param servicios
	 * @return
	 */
	public boolean insertarServiciosManejoTransSecundario(Connection con,ArrayList<Integer> servicios,int institucion);

	
}