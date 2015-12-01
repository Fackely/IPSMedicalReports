/*
 * @(#)ValidacionesAnulacionFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Vector;

import com.princetonsa.dto.facturacion.DtoConsultaFacturasAnuladas;

/**
 *  Interfaz para el acceder a la fuente de datos de un contrato
 *
 * @version 1.0, Agosto 04 2005
 * @author <a href="mailto:wilson@PrincetonSA.com">Wilson Ríos</a>
 */
public interface ValidacionesAnulacionFacturasDao 
{
    /**
	 * Metodo que verifica la existencia de otras cuentas abiertas, abiertasDistribuidas, asociadas, asociadasDistribuidas 
	 * asociadasFacturadaParcial, facturadaParcial de un paciente y una cuenta dado el idFactura
	 * @param con
	 * @param codigofactura
	 * @return
	 */
	public  boolean existenOtrasCuentasAbiertas(Connection con, int codigoFactura);
    
	/**
	 * Método que devuelve el estado de la cuenta dado su id
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int getEstadoCuenta(Connection con, int idCuenta);
	
	/**
	 * Metodo que indica si una factura esta  en un cierre inicial de cartera
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean estaFacturaEnCierreInicialCartera(Connection con, int codigoFactura);
	
	/**
	 * Metodo que indica si la facura pertenece a un particular o a un convenio
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean esFacturaResponsableParticular(Connection con, int codigoFactura);
	
	/**
	 * Metodo que indica si una factura pertenece a un responsable CAPITADO
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean facturaPerteneceAConvenioCapitado(Connection con, int codigoFactura);
	
	/**
	 * Método que indica si una factura esta asociada a un pagare
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String estaFacturaAsociadaAPagare(Connection con, int codigoFactura);
	
	/**
	 * Metodo que indica si la factura pertenece a una cuenta de cobro en caso de ser asi devuelve el
	 * numero de cuenta de cobro
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String getCuentaCobroDeFactura(Connection con, int codigoFactura);
	
	/**
	 * Método que devuelve  'naturalezaAjuste - numeroAjuste' ya sea por factura o por cuenta de cobro, 
	 * toma los ajustes que estan en estado generado, es decir, los que estan pendientes de anular o aprobar,
	 * en caso de ser "" entonces es que no existen. 
	 * @param con
	 * @param codigoFactura
	 * @param cuentaCobro
	 * @return
	 */
	public String facturaTieneAjustesPendientesEnFacturaYCuentaCobro(Connection con, int codigoFactura, String cuentaCobro);
	
	/**
	 * Validacion de la sumatoria del movimiento en cartera = 0, es decir (ajustes_debito - ajustes_credito) = 0 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean sumatoriaMovCarteraEsCero(Connection con, int codigoFactura);
	
	/**
	 * Método que indica si una factura tiene un valor de abono
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean existeValorAbonoFactura (Connection con, int codigoFactura);
	
	/**
	 * Método que indica si una factura tiene un valor bruto paciente
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean existeValorBrutoPaciente (Connection con, int codigoFactura);
	
	/**
	 * Método que indica si una cuenta tiene o no asocio
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean cuentaTieneAsocio(Connection con, int codigoCuenta);
	
	/**
	 * Método que indica si la cuenta asociada esta en una sola factura
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCuentaAsociada
	 * @return
	 */
	public boolean esCuentaAsociadaEnUnaSolaFactura(Connection con, int codigoCuenta, int codigoCuentaAsociada);
	
	/**
	 * Método que devuelve le codigo de la cuenta asociada dado un id de cuenta
	 * @param con
	 * @param codigoCuenta
	 * @param valorTrueSegunBD
	 * @return
	 */
	public Vector<String> getCodigoCuentasAsociadas(Connection con, int ingreso);
	
	/**
	 * Método que indica si una cuenta distribuida es unida, de lo contrario es independiente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean cuentaDistribuidaEsUnida(Connection con, int codigoCuenta);
	
	/**
	 * Metodo que indica si una cuenta  tiene mi9nimo otra factura facturada
	 * @param con
	 * @param codigoFactura
	 * @param codigoCuenta
	 * @return
	 */
	public boolean cuentaTieneMinimoOtraFacturaFacturada (Connection con, int codigoFactura, int codigoCuenta);
	
	/**
	 * Método que devuelve le codigo de la cuenta asociada dado un id de cuenta
	 * @param con
	 * @param codigoCuenta
	 * @param valorTrueSegunBD
	 * @return
	 */
	public int getCodigoCuentaDadaFactura(Connection con, int codigoFactura);
	
	/**
	 * Método que evalua si existe la parametrizacion de lod motivos de anulacion
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean existenMotivosAnulacion(Connection con, int codigoInstitucion);
	
	/**
	 * Método que devuelve le codigo de la subcuenta dado un id de cuenta
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public int getSubCuentaDadaFactura(Connection con, int codigoFactura);
	
	/**
	 * Metodo que verifica si el valor neto a cargo del paciente es mayor a cero o no
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean esValorNetoACargoPacienteMayorCero(Connection con, String codigoFactura);
	
	/**
	 * metodo que retorna el codigo del estado de pago del paciente
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int getCodigoEstadoPagoPaciente(Connection con, String codigoFactura);

	/**
	 * obtiene el valor de bruto paciente menos el valor de descuento
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public double getValorBrutoPacMenosValDescuento(Connection con, String codigoFactura);

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int getCodigoCuentaPrincipalDadaFactura(Connection con, int codigoFactura);
	
	/**
	 * Restricciones del reporte
	 * @param dto
	 * @return
	 */
	public String obtenerRestriccionesReporte(DtoConsultaFacturasAnuladas dto);
	
	/**
	 * Metodo para validar si la factura viene o no de un ingreso odontologico
	 * @param codigoFactura
	 * @return
	 */
	public boolean esFacturaIngresoOdontologico(BigDecimal codigoFactura);
}
