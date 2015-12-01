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
	 * Generaci�n factura varia
	 * @param dtoFacturaVaria
	 * @param deudor TODO
	 * @author Juan David Ram�rez
	 * @since 12 Septiembre 2010
	 */
	public void generarFacturaVaria(DtoFacturaVaria dtoFacturaVaria);

	/**
	 * Listar conceptos de facturas varias
	 * @author Juan David Ram�rez
	 * @return {@link ArrayList} Lista de conceptos {@link DtoConceptoFacturaVaria}
	 * @since 12 Septiembre 2010
	 */
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
	 * 
	 * @return DtoConceptoFacturaVaria
	 */

	public ArrayList<DtoConceptoFacturaVaria> listarConceptosFacturasV();

	/**
	 * M�todo que se encarga de consultar los estados de facturas varias
	 * 
	 * 
	 * @return DtoIntegridadDominio
	 */
	public ArrayList<DtoIntegridadDominio> listarEstadoFacturasVarias();
	
	/**
	 * M�todo que determina de donde se debe obtener la informaci�n para
	 * la generaci�n de la factura.
	 * 
	 * @param usuario
	 * @return
	 */
	public int obtenerTipoFuenteDatosFacturaVaria(UsuarioBasico usuario);
	
	/**
	 * M�todo que se encarga de consultar la informaci�n necesaria para la
	 * generaci�n de la Factura Varia dependiendo de la parametrizaci�n del sistema.
	 * 
	 * tipoFuenteDatos = 0 ----> Centro de Atenci�n
	 * tipoFuenteDatos = 1 ----> Empresa Instituci�n
	 * tipoFuenteDatos = 2 ----> Instituci�n
	 * 
	 * @param historicoEncabezado 
	 * @param tipoFuenteDatos
	 * @param codigoFuenteInformacion
	 */
	public void obtenerDatosParametrizacionParaFacturaVaria (HistoricoEncabezado historicoEncabezado, int tipoFuenteDatos, int codigoFuenteInformacion);
	
	/**
	 * Met�do que se encarga de obtener el HistoricoEncabezado asociado a una FacturaVaria
	 * pasando como parametro el consecutivo de la factura varia
	 * @param consecutivo
	 * @return HistoricoEncabezado
	 */
	public HistoricoEncabezado obtenerHistoricoEncabezadoFacturaVaria(long consecutivo);
	
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
