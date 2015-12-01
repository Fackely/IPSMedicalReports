package com.servinte.axioma.servicio.interfaz.facturasvarias;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.facturasVarias.DtoFacturaVariaGenerico;
import com.servinte.axioma.orm.FacturasVarias;

public interface IFacturasVariasServicio {

	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasVarias();
	
	/**
	 * M�todo que se encarga de generar la impresi�n de facturas varias.
	 * @param con
	 * @param dto
	 * @param request
	 */
	public boolean generarReporte(Connection con, DtoFacturaVariaGenerico dto, HttpServletRequest request);

	
	/**
	 * M�todo que se encarga de consultar una factura varia conociendo su c�digo
	 * 
	 * @param codigoFacVar
	 */
	public FacturasVarias obtenerFacturaVaria(long codigoFacVar);
	
	
	/**
	 * M�todo que se encarga de obtener el prefijo y el consecutivo
	 * de una factura varia espec�fica.
	 * 
	 * @param codigoFacVar
	 * @return
	 */
	public String obtenerPrefijoConsecutivo(long codigoFacVar);
	
	/**
	 * M�todo que se encarga de consultar los conceptos de facturas varias
	 * 
	 * @param 
	 * @return
	 */
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasV();

	/**
	 * M�todo que se encarga de consultar los estados de facturas varias
	 * 
	 * @param 
	 * @return
	 */
	public ArrayList<DtoIntegridadDominio> listarEstadoFacturasVarias();
}
