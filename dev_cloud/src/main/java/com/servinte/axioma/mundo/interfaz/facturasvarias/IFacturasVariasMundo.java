package com.servinte.axioma.mundo.interfaz.facturasvarias;

import java.sql.Connection;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.consultaExterna.DtoConceptoFacturaVaria;
import com.princetonsa.dto.facturasVarias.DtoFacturaVaria;
import com.princetonsa.dto.facturasVarias.DtoFacturaVariaGenerico;
import com.princetonsa.mundo.UsuarioBasico;
import com.servinte.axioma.orm.FacturasVarias;
import com.servinte.axioma.orm.HistoricoEncabezado;

public interface IFacturasVariasMundo {

	/**
	 * Generación factura varia
	 * @param dtoFacturaVaria
	 * @param deudor TODO
	 * @author Juan David Ramírez
	 * @since 12 Septiembre 2010
	 */
	public void generarFacturaVaria(DtoFacturaVaria dtoFacturaVaria);

	/**
	 * Listar conceptos de facturas varias
	 * @author Juan David Ramírez
	 * @return {@link ArrayList} Lista de conceptos {@link DtoConceptoFacturaVaria}
	 * @since 12 Septiembre 2010
	 */
	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasVarias();
	
	/**
	 * Método que se encarga de generar la impresión de facturas varias.
	 * @param con
	 * @param dto
	 * @param request
	 */
	public boolean generarReporte(Connection con, DtoFacturaVariaGenerico dto, HttpServletRequest request);
	
	
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
	 * 
	 * @return DtoConceptoFacturaVaria
	 */

	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasV();

	/**
	 * Método que se encarga de consultar los estados de facturas varias
	 * 
	 * 
	 * @return DtoIntegridadDominio
	 */
	public ArrayList<DtoIntegridadDominio> listarEstadoFacturasVarias();
	
	/**
	 * Método que determina de donde se debe obtener la información para
	 * la generación de la factura.
	 * 
	 * @param usuario
	 * @return
	 */
	public int obtenerTipoFuenteDatosFacturaVaria(UsuarioBasico usuario);
	
	/**
	 * Método que se encarga de consultar la información necesaria para la
	 * generación de la Factura Varia dependiendo de la parametrización del sistema.
	 * 
	 * tipoFuenteDatos = 0 ----> Centro de Atención
	 * tipoFuenteDatos = 1 ----> Empresa Institución
	 * tipoFuenteDatos = 2 ----> Institución
	 * 
	 * @param historicoEncabezado 
	 * @param tipoFuenteDatos
	 * @param codigoFuenteInformacion
	 */
	public void obtenerDatosParametrizacionParaFacturaVaria (HistoricoEncabezado historicoEncabezado, int tipoFuenteDatos, int codigoFuenteInformacion);
	
	/**
	 * Metódo que se encarga de obtener el HistoricoEncabezado asociado a una FacturaVaria
	 * pasando como parametro el consecutivo de la factura varia
	 * @param consecutivo
	 * @return HistoricoEncabezado
	 */
	public HistoricoEncabezado obtenerHistoricoEncabezadoFacturaVaria(long consecutivo);
	
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
