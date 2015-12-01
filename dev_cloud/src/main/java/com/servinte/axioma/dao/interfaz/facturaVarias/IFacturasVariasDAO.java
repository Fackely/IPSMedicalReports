package com.servinte.axioma.dao.interfaz.facturaVarias;

import java.util.ArrayList;

import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.servinte.axioma.orm.FacturasVarias;


/**
 * Interfaz para la persitencia de facturas varias
 * @author Edgar Carvajal
 *
 */
public interface IFacturasVariasDAO {

	/**
	 * Generaci�n factura varia
	 * @author Juan David Ram�rez
	 * @param factua {@link FacturasVarias}
	 * @since 12 Septiembre 2010
	 */
	public void generarFacturaVaria(FacturasVarias factua);

	/**
	 * 
	 * @return {@link ArayList} Lista de conceptos para la generaci�n de facturas varias
	 */
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasVarias();

	
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
	 * M�todo que se encarga de obtener el c�digo de una factura varia
	 * espec�fica teniendo el consecutivo de la factura.
	 * 
	 * @param codigoFacVar
	 * @return
	 */
	public long obtenerCodigoFacturaVaria(long consecutivo);
	
	/**
	 * M�todo que se encarga de obtener el consecutivo de una factura varia
	 * espec�fica teniendo el c�digo de la misma.
	 * 
	 * @param codigoFacVar
	 * @return long - consecutivo Factura Varia
	 */
	public long obtenerConsecutivoFacturaVaria(long codigoFacVar);
	
}
