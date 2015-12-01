package com.princetonsa.dao.postgresql.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosInt;

import com.princetonsa.dao.facturacion.ValidacionesFacturaDao;
import com.princetonsa.dao.sqlbase.facturacion.DtoVerficacionDerechos;
import com.princetonsa.dao.sqlbase.facturacion.SqlBaseValidacionesFacturaDao;

/**
 * 
 * @author wilson
 *
 */
public class PostgresqlValidacionesFacturaDao implements ValidacionesFacturaDao 
{
	
	/**
	 * Implementacion del metodo para buscar el ultimo
	 * consecutico de dactura utilizado dentro de la empresainstitucion
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public double obtenerSiguientePosibleNumeroFacturaMultiempresa(Connection con, double empresaInstitucion)
	{
		return SqlBaseValidacionesFacturaDao.obtenerSiguientePosibleNumeroFacturaMultiempresa(con, empresaInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean existeVerificacionDerechosConvenioIngreso( Connection con, String idIngreso, int codigoConvenio)
	{
		return SqlBaseValidacionesFacturaDao.existeVerificacionDerechosConvenioIngreso(con, idIngreso, codigoConvenio);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param subCuentas
	 * @param facturado
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Vector obtenerContratosSubCuentas(Connection con, String idIngreso, Vector subCuentas, String facturado)
	{
		return SqlBaseValidacionesFacturaDao.obtenerContratosSubCuentas(con, idIngreso, subCuentas, facturado);
	}
	
	
	/**
	 * metodo que obtiene el listado de solicitudes de interconsulta - procedimientos - evoluciones que tienen asociado
	 * un servicio que requiere interpretacion y la solicitud tiene estado respondida
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap obtenerSolicitudesHCInvalidas( Connection con, Vector cuentas, Vector subCuentas, boolean validarInterpretadas)
	{
		return SqlBaseValidacionesFacturaDao.obtenerSolicitudesHCInvalidas(con, cuentas, subCuentas, validarInterpretadas);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @return
	 */
	public HashMap obtenerSolicitudesHCInvalidasMedicamentos( Connection con, Vector cuentas, Vector subCuentas)
	{
		return SqlBaseValidacionesFacturaDao.obtenerSolicitudesHCInvalidasMedicamentos(con, cuentas, subCuentas);
	}
	
	/**
	 * metodo apra obtener las solicitudes q no tienen pool
	 * @param con
	 * @param cuentas
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HashMap obtenerSolicitudesSinPool(Connection con, Vector cuentas, Vector subCuentas)
	{
		return SqlBaseValidacionesFacturaDao.obtenerSolicitudesSinPool(con, cuentas, subCuentas);
	}
	
	/**
	 * Método que busca las solicitudes cuyos médicos no esten en ningún pool 
	 * @param con
	 * @param cuentas
	 * @return
	 */
	public HashMap obtenerSolicitudesSinMedicoEnPool (Connection con, Vector cuentas, Vector subCuentas)
	{
		return SqlBaseValidacionesFacturaDao.obtenerSolicitudesSinMedicoEnPool(con, cuentas, subCuentas);
	}
	
	/**
	 * Método que busca las solicitudes de cirugía las cuales no tienen pool asignado
     * @param con Conexión con la fuente de datos
	 * @param cuentas 
	 * @return
	 */
	public HashMap obtenerSolicitudesCxSinPool(Connection con, Vector cuentas, Vector subCuentas)
	{
		return SqlBaseValidacionesFacturaDao.obtenerSolicitudesCxSinPool(con, cuentas, subCuentas);
	}
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param subcuenta
	 * @param facturado
	 * @return
	 */
	public InfoDatosInt obtenerNaturalezaPacienteSubCuenta(Connection con, String idIngreso, double subCuenta, String facturado)
	{
		return SqlBaseValidacionesFacturaDao.obtenerNaturalezaPacienteSubCuenta(con, idIngreso, subCuenta, facturado);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public DtoVerficacionDerechos obtenerValorCuotaVerificacion(Connection con, String idIngreso, int codigoConvenio)
	{
		return SqlBaseValidacionesFacturaDao.obtenerValorCuotaVerificacion(con, idIngreso, codigoConvenio);
	}
	
	/**
  	 * Metodo para consultar si una factura existe en BD.
  	 * @param con Connection, conexión con la fuente de datos
  	 * @param codigoFact in, código de la factura
  	 * @return int, 0 si no existe
  	 * @author jarloc
  	 */
  	public int existeCodigoFactura (Connection con, int codigoFact)
  	{
  		return SqlBaseValidacionesFacturaDao.existeCodigoFactura(con, codigoFact);
  	}
  	
	/**
	 * Consultar el consecutivo de la primera factura
	 * generada por el sistema
	 * @param con Connection, conexión con la fuente de datos	 
	 * @return int, -1 si hay error
	 * @see com.princetonsa.dao.FacturaBORRAMEDao#primeraFacturaGeneradaSistema(Connection)
	 * @author jarloc
	 */
	public int primeraFacturaGeneradaSistema (Connection con)
	{
		return SqlBaseValidacionesFacturaDao.primeraFacturaGeneradaSistema(con);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public int esCuentaValidaProcesoFacturacion(Connection con, int codigoCuenta)
	{
		return SqlBaseValidacionesFacturaDao.esCuentaValidaProcesoFacturacion(con, codigoCuenta);
	}
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean facturaTieneAjustesPendientes(Connection con, int codigoFactura)
	{
		return SqlBaseValidacionesFacturaDao.facturaTieneAjustesPendientes(con, codigoFactura);
	}
	
	
	/**
	 * 
	 */
	public boolean facturaTieneCastigoCartera(Connection con, int codigoFactura)
	{
		return SqlBaseValidacionesFacturaDao.facturaTieneCastigoCartera(con, codigoFactura);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public boolean facturaTienePagosPendientes(Connection con, int numeroFactura)
	{
		return SqlBaseValidacionesFacturaDao.facturaTienePagosPendientes(con, numeroFactura);
	}
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public boolean facturaTieneSaldoPendiente(Connection con, int numeroFactura)
	{
		String consulta="SELECT  getSaldoFacturaAjustes(?) as saldo ";
		return SqlBaseValidacionesFacturaDao.facturaTieneSaldoPendiente(con,numeroFactura,consulta);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param codigoAjuste
	 * @return
	 */
	public boolean facturaTieneAjustesPendientesDiferentesAjusteActual(Connection con, int codigoFactura, double codigoAjuste)
	{
		return SqlBaseValidacionesFacturaDao.facturaTieneAjustesPendientesDiferentesAjusteActual(con, codigoFactura, codigoAjuste);
	}
	
	/**
	 * @param numeroFactura
	 * @return
	 */
	public boolean esFacturaCerrada(Connection con, int numeroFactura)
	{
		return SqlBaseValidacionesFacturaDao.esFacturaCerrada(con, numeroFactura);
	}
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public boolean esFacturaExterna(Connection con, int numeroFactura) 
	{
		return SqlBaseValidacionesFacturaDao.esFacturaExterna(con, numeroFactura);
	}
	
	/**
	 * Metodo implementado para consultar los pagos
	 * realizados a una factura, que no se encuentren
	 * en estado pendiente y que esten en un rango de
	 * fecha 
	 * @param con Connection
	 * @param numeroFactura int
	 * @param fecha String 
	 * @return ResultSet
	 * @author jarloc
	 */
	public ArrayList facturaTienePagos (Connection con, int numeroFactura, String fecha)
	{
		return SqlBaseValidacionesFacturaDao.facturaTienePagos(con, numeroFactura, fecha);
	}
	
	/**
	 * Metodo para consultar si una factura tiene ajustes
	 * aprobados o anulados, en un rango de fecha.
	 * @param con Connection
	 * @param numeroFactura int 
	 * @param fecha String 
	 * @return ResultSet
	 * @author jarloc
	 */
	public ArrayList facturaAjustesAprobadosAnulados(Connection con, int numeroFactura,String fecha)
	{
		return SqlBaseValidacionesFacturaDao.facturaAjustesAprobadosAnulados(con, numeroFactura, fecha);
	}
	
	/**
	 * Metodo que obtiene las solicitudes de cx que no estan liquidadas
	 * @param con
	 * @param cuentas
	 * @param subcuentas
	 * @return
	 */
	public HashMap obtenerSolicitudesCxSinLiquidacion( Connection con, Vector cuentas, Vector subCuentas)
	{
		return SqlBaseValidacionesFacturaDao.obtenerSolicitudesCxSinLiquidacion(con, cuentas, subCuentas);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean viaIngresoPermiteCorteFactura(Connection con, int codigoCuenta)
	{
		return SqlBaseValidacionesFacturaDao.viaIngresoPermiteCorteFactura(con, codigoCuenta);
	}
	
	/**
	 * Metodo que evalua los posibles errores de las solicitudes PYP, hasta el momento solo se verifica que las solicitudes 
	 * esten cubiertas por el convenio
	 * @param con
	 * @param cuentas
	 * @parma subCuentas
	 * @return
	 */
	public Vector analisisSolicitudesPYP(Connection con, Vector cuentas, Vector subCuentas)
	{
		return SqlBaseValidacionesFacturaDao.analisisSolicitudesPYP(con, cuentas, subCuentas);
	}

	/**
	 * metodo que evalua si todas las solicitudes son de pyp para no cobrarle nada al paciente
	 * @param con
	 * @param cuentas
	 * @param subCuenta
	 * @return
	 */
	public boolean existenUnicamenteSolicitudesPYP(Connection con, Vector cuentas, double subCuenta) 
	{
		return SqlBaseValidacionesFacturaDao.existenUnicamenteSolicitudesPYP(con, cuentas, subCuenta);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @return
	 */
	public boolean existenSolicitudesNoFacturadas( Connection con, Vector cuentas, Vector subCuentas)
	{
		return SqlBaseValidacionesFacturaDao.existenSolicitudesNoFacturadas(con, cuentas, subCuentas);
	}

	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public Vector<String> obtenerRangoInicialFinalFacturaMultiempresa( Connection con, double empresaInstitucion )
	{
		return SqlBaseValidacionesFacturaDao.obtenerRangoInicialFinalFacturaMultiempresa(con, empresaInstitucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @param incremento
	 * @return
	 */
	public boolean incrementarConsecutivoFacturaMultiempresa(Connection con, double empresaInstitucion,int incremento)
	{
		return SqlBaseValidacionesFacturaDao.incrementarConsecutivoFacturaMultiempresa(con, empresaInstitucion, incremento);
	}
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @param cubierta
	 * @return
	 */
	public String obtenerSolicitudesSinAutorizacion( Connection con, Vector cuentas, double subCuenta, boolean cubierta)
	{
		return SqlBaseValidacionesFacturaDao.obtenerSolicitudesSinAutorizacion(con, cuentas, subCuenta, cubierta);
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param codigoContrato
	 * @return
	 */
	public boolean esPacienteCapitadoVigente(Connection con, int codigoPersona, int codigoContrato, int ingreso)
	{
		return SqlBaseValidacionesFacturaDao.esPacienteCapitadoVigente(con, codigoPersona, codigoContrato, ingreso);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public Vector<String> obtenerCuentasActivaPacFactura(Connection con, String consecutivoFactura, int institucion)
	{
		return SqlBaseValidacionesFacturaDao.obtenerCuentasActivaPacFactura(con, consecutivoFactura, institucion);
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public Vector<String> obtenerIngresosAbiertosPacFactura(BigDecimal codigoFactura)
	{
		return SqlBaseValidacionesFacturaDao.obtenerIngresosAbiertosPacFactura(codigoFactura);
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean generaCargoEnSolicitud(Connection con, int numeroSolicitud)
	{
		String consulta="SELECT ordenes.esSolCargoSolicitud(?)>0 as escargoensol ";
		return SqlBaseValidacionesFacturaDao.generaCargoEnSolicitud(con, numeroSolicitud, consulta);
	}
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public boolean  validarPreglosaRespondida(Connection con, String numeroFactura)
	{
		return SqlBaseValidacionesFacturaDao.validarPreglosaRespondida(con,numeroFactura);
	}

	@Override
	public boolean puedoAnularFacturaXGlosa(BigDecimal codigoFactura) 
	{
		return SqlBaseValidacionesFacturaDao.puedoAnularFacturaXGlosa(codigoFactura);
	}
	
	@Override
	public boolean puedoAnularFacturaXReclamaciones(BigDecimal codigoFactura)
	{
		return SqlBaseValidacionesFacturaDao.puedoAnularFacturaXReclamaciones(codigoFactura);
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<String> obtenerMedicoRespondeSinTipoLiquidacion(Connection con, Vector cuentas, Vector subCuentasSeleccionadasVector) 
	{
		return SqlBaseValidacionesFacturaDao.obtenerMedicoRespondeSinTipoLiquidacion(con, cuentas, subCuentasSeleccionadasVector);
	}
}