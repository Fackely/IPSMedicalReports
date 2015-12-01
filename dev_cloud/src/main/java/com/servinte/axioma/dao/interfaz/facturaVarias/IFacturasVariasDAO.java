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
	 * Generación factura varia
	 * @author Juan David Ramírez
	 * @param factua {@link FacturasVarias}
	 * @since 12 Septiembre 2010
	 */
	public void generarFacturaVaria(FacturasVarias factua);

	/**
	 * 
	 * @return {@link ArayList} Lista de conceptos para la generación de facturas varias
	 */
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasVarias();

	
	/**
	 * Método que se encarga de consultar una factura varia conociendo su código
	 * 
	 * @param codigoFacVar
	 */
	public FacturasVarias obtenerFacturaVaria(long codigoFacVar);
	
	/**
	 * Método que se encarga de obtener el prefijo y el consecutivo
	 * de una factura varia específica.
	 * 
	 * @param codigoFacVar
	 * @return
	 */
	public String obtenerPrefijoConsecutivo(long codigoFacVar);

	/**
	 * Método que se encarga de consultar los conceptos de facturas varias
	 * 
	 * @param 
	 * @return
	 */
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasV();

	/**
	 * Método que se encarga de obtener el código de una factura varia
	 * específica teniendo el consecutivo de la factura.
	 * 
	 * @param codigoFacVar
	 * @return
	 */
	public long obtenerCodigoFacturaVaria(long consecutivo);
	
	/**
	 * Método que se encarga de obtener el consecutivo de una factura varia
	 * específica teniendo el código de la misma.
	 * 
	 * @param codigoFacVar
	 * @return long - consecutivo Factura Varia
	 */
	public long obtenerConsecutivoFacturaVaria(long codigoFacVar);
	
}
