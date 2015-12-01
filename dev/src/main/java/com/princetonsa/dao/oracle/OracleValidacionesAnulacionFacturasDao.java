/*
 * @(#)OracleValidacionesAnulacionFacturasDao.java
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 *
 * Lenguaje   : Java
 * Compilador : J2SDK 1.4.1_01
 *
 */
package com.princetonsa.dao.oracle;

import java.math.BigDecimal;
import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturacion.DtoConsultaFacturasAnuladas;

import java.sql.SQLException;
import java.util.Vector;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.princetonsa.dao.ValidacionesAnulacionFacturasDao;
import com.princetonsa.dao.sqlbase.SqlBaseValidacionesAnulacionFacturasDao;

/**
 * Implementación Oracle de las funciones de acceso a la fuente de datos
 * para las validaciones de anulacion de facturas
 *
 * @version 1.0, Agosto 04 / 2005
 * @author wrios
 */
public class OracleValidacionesAnulacionFacturasDao implements ValidacionesAnulacionFacturasDao
{
    /**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(OracleValidacionesAnulacionFacturasDao.class);
    
	/**
	 * Metodo que verifica la existencia de otras cuentas abiertas, abiertasDistribuidas, asociadas, asociadasDistribuidas 
	 * asociadasFacturadaParcial, facturadaParcial de un paciente y una cuenta dado el idFactura
	 * @param con
	 * @param codigofactura
	 * @return
	 */
	public  boolean existenOtrasCuentasAbiertas(Connection con, int codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.existenOtrasCuentasAbiertas(con, codigoFactura);
	}
	
	/**
	 * Método que devuelve el estado de la cuenta dado su id
	 * @param con
	 * @param idCuenta
	 * @return
	 */
	public int getEstadoCuenta(Connection con, int idCuenta)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.getEstadoCuenta(con, idCuenta);
	}
	
	/**
	 * Metodo que indica si una factura esta  en un cierre inicial de cartera
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean estaFacturaEnCierreInicialCartera(Connection con, int codigoFactura)
	{
	    String verificarStr=	"SELECT factura_cerrada AS esFacturaCerrada FROM facturas WHERE codigo=?";
	    try
		{
		    int  resp=ConstantesBD.codigoNuncaValido;
			PreparedStatementDecorator pst= new PreparedStatementDecorator(con.prepareStatement(verificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigoFactura);
			ResultSetDecorator rs=new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			    resp= rs.getInt("esFacturaCerrada");
			if(resp==1)
			    return true;
			else
			    return false;
		}
		catch(SQLException e)
		{
			logger.error("Error en esFacturaDeCarteraEmpresas de OracleUtilidadValidacionDao: "+e);
			return false;
		}
	}
	
	/**
	 * Metodo que indica si la facura pertenece a un particular o a un convenio
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean esFacturaResponsableParticular(Connection con, int codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.esFacturaResponsableParticular(con, codigoFactura);
	}
	
	/**
	 * Metodo que indica si una factura pertenece a un responsable CAPITADO
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean facturaPerteneceAConvenioCapitado(Connection con, int codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.facturaPerteneceAConvenioCapitado(con, codigoFactura);
	}
	
	/**
	 * Método que indica si una factura esta asociada a un pagare
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String estaFacturaAsociadaAPagare(Connection con, int codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.estaFacturaAsociadaAPagare(con, codigoFactura);
	}
	
	/**
	 * Metodo que indica si la factura pertenece a una cuenta de cobro en caso de ser asi devuelve el
	 * numero de cuenta de cobro
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String getCuentaCobroDeFactura(Connection con, int codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.getCuentaCobroDeFactura(con, codigoFactura);
	}
	
	/**
	 * Método que devuelve  'naturalezaAjuste - numeroAjuste' ya sea por factura o por cuenta de cobro, 
	 * toma los ajustes que estan en estado generado, es decir, los que estan pendientes de anular o aprobar,
	 * en caso de ser "" entonces es que no existen. 
	 * @param con
	 * @param codigoFactura
	 * @param cuentaCobro
	 * @return
	 */
	public String facturaTieneAjustesPendientesEnFacturaYCuentaCobro(Connection con, int codigoFactura, String cuentaCobro)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.facturaTieneAjustesPendientesEnFacturaYCuentaCobro(con, codigoFactura, cuentaCobro);
	}

	/**
	 * Validacion de la sumatoria del movimiento en cartera = 0, es decir (ajustes_debito - ajustes_credito) = 0 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean sumatoriaMovCarteraEsCero(Connection con, int codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.sumatoriaMovCarteraEsCero(con, codigoFactura);
	}
	
	/**
	 * Método que indica si una factura tiene un valor de abono
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean existeValorAbonoFactura (Connection con, int codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.existeValorAbonoFactura(con, codigoFactura);
	}
	
	/**
	 * Método que indica si una factura tiene un valor bruto paciente
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean existeValorBrutoPaciente (Connection con, int codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.existeValorBrutoPaciente(con, codigoFactura);
	}
	
	/**
	 * Método que indica si una cuenta tiene o no asocio
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean cuentaTieneAsocio(Connection con, int codigoCuenta)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.cuentaTieneAsocio(con, codigoCuenta, "1");
	}
	
	/**
	 * Método que indica si la cuenta asociada esta en una sola factura
	 * @param con
	 * @param codigoCuenta
	 * @param codigoCuentaAsociada
	 * @return
	 */
	public boolean esCuentaAsociadaEnUnaSolaFactura(Connection con, int codigoCuenta, int codigoCuentaAsociada)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.esCuentaAsociadaEnUnaSolaFactura(con, codigoCuenta, codigoCuentaAsociada);
	}
	
	/**
	 * Método que devuelve le codigo de la cuenta asociada dado un id de cuenta
	 * @param con
	 * @param codigoCuenta
	 * @param valorTrueSegunBD
	 * @return
	 */
	public Vector<String> getCodigoCuentasAsociadas(Connection con, int ingreso)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.getCodigoCuentasAsociadas(con, ingreso);
	}
	
	/**
	 * Método que indica si una cuenta distribuida es unida, de lo contrario es independiente
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean cuentaDistribuidaEsUnida(Connection con, int codigoCuenta)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.cuentaDistribuidaEsUnida(con, codigoCuenta);
	}
	
	/**
	 * Metodo que indica si una cuenta tiene mi9nimo otra factura facturada
	 * @param con
	 * @param codigoFactura
	 * @param codigoCuenta
	 * @return
	 */
	public boolean cuentaTieneMinimoOtraFacturaFacturada (Connection con, int codigoFactura, int codigoCuenta)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.cuentaTieneMinimoOtraFacturaFacturada(con, codigoFactura, codigoCuenta);
	}
	
	/**
	 * Método que devuelve le codigo de la cuenta asociada dado un id de cuenta
	 * @param con
	 * @param codigoCuenta
	 * @param valorTrueSegunBD
	 * @return
	 */
	public int getCodigoCuentaDadaFactura(Connection con, int codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.getCodigoCuentaDadaFactura(con, codigoFactura);
	}
	
	/**
	 * Método que evalua si existe la parametrizacion de lod motivos de anulacion
	 * @param con
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean existenMotivosAnulacion(Connection con, int codigoInstitucion)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.existenMotivosAnulacion(con, codigoInstitucion);
	}
	
	/**
	 * Método que devuelve le codigo de la subcuenta dado un id de cuenta
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public int getSubCuentaDadaFactura(Connection con, int codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.getSubCuentaDadaFactura(con, codigoFactura);
	}
	
	/**
	 * Metodo que verifica si el valor neto a cargo del paciente es mayor a cero o no
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean esValorNetoACargoPacienteMayorCero(Connection con, String codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.esValorNetoACargoPacienteMayorCero(con, codigoFactura);
	}
	
	/**
	 * metodo que retorna el codigo del estado de pago del paciente
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int getCodigoEstadoPagoPaciente(Connection con, String codigoFactura)
	{
	    return SqlBaseValidacionesAnulacionFacturasDao.getCodigoEstadoPagoPaciente(con, codigoFactura);
	}
	
	/**
	 * obtiene el valor de bruto paciente menos el valor de descuento
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public double getValorBrutoPacMenosValDescuento(Connection con, String codigoFactura)
	{
		return SqlBaseValidacionesAnulacionFacturasDao.getValorBrutoPacMenosValDescuento(con, codigoFactura);
	}

	/**
	 * 
	 */
	public int getCodigoCuentaPrincipalDadaFactura(Connection con, int codigoFactura) 
	{
		return SqlBaseValidacionesAnulacionFacturasDao.getCodigoCuentaPrincipalDadaFactura(con, codigoFactura);
	}

	@Override
	public String obtenerRestriccionesReporte(DtoConsultaFacturasAnuladas dto) {
		return SqlBaseValidacionesAnulacionFacturasDao.obtenerRestriccionesReporte(dto);
	}

	@Override
	public boolean esFacturaIngresoOdontologico(BigDecimal codigoFactura) {
		return SqlBaseValidacionesAnulacionFacturasDao.esFacturaIngresoOdontologico(codigoFactura);
	}
}
