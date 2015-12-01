package com.princetonsa.dao.glosas;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;

import util.ResultadoBoolean;

import com.princetonsa.dto.glosas.DtoDetalleAsociosGlosa;
import com.princetonsa.dto.glosas.DtoGlosa;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * Anexo 686 Registro Auditor�a
 * Creado el 6 Enero de 2009
 * @author Felipe P�rez Granda - Sebasti�n G�mez
 * @mail lfperez@princetonsa.com - sgomez@princetonsa.com
 */

public interface GlosasDao
{
	/**
	 * M�todo encargado de consultar las facturas
	 * @author Felipe P�rez Granda
	 * @param connection
	 * @param criterios
	 */
	public HashMap<String, Object> consultarFacturas (Connection connection, HashMap<String, Object> criterios);
	
	/**
	 * M�todo que realiza la inserci�n de la glosa
	 * @param con
	 * @param glosa
	 * @return
	 */
	public ResultadoBoolean guardarGlosa(Connection con,DtoGlosa glosa, String remite);
	
	/**
	 * M�todo implementado para cargar los datos de una glosa
	 * @author Sebasti�n G�mez R. - Felipe P�rez Granda
	 * @param connection
	 * @param criterios
	 * @return DtoGlosa
	 */
	public DtoGlosa cargarGlosa (Connection connection, HashMap<String, Object> criterios, UsuarioBasico usuario);
	
	/**
	 * M�todo para precargar la glosa cuando se piensa hacer una devolucion
	 * @param con
	 * @param glosa
	 * @return
	 */
	public DtoGlosa cargarGlosaDevolucion(Connection con,DtoGlosa glosa, String forma);
	
	/**
	 * M�todo para cargar las solicitudes de la factura
	 * @param con
	 * @param criterios
	 * @return
	 */
	public HashMap<String, Object> cargarSolicitudesFactura(Connection con,HashMap<String, Object> criterios);
	
	/**
	 * M�todo para cargar los asocios del detalle de la factura
	 * @param con
	 * @param codigoDetalleFactura
	 * @param codigoTarifarioOficial
	 * @return
	 */
	public ArrayList<DtoDetalleAsociosGlosa> cargarAsociosDetalleFactura(Connection con,String codigoDetalleFactura,int codigoTarifarioOficial);
	
	/**
	 * M�todo para consultar las facturas auditadas
	 */
	public HashMap<String, Object> consultarFacturasAuditadas(Connection con,HashMap<String, Object> criterios, ArrayList<Integer> listaConvenios);

	/**
	 * 
	 * @param codigoAuditoria
	 * @param observaciones
	 * @return
	 */
	public boolean actualizarObservacionAuditoria(String codigoAuditoria,String observaciones);
	
}