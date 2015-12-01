package com.princetonsa.dao.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.sqlbase.facturacion.DtoVerficacionDerechos;

import util.InfoDatosInt;

/**
 * 
 * @author wilson
 *
 */
public interface ValidacionesFacturaDao 
{
	/**
	 * Implementacion del metodo para buscar el ultimo
	 * consecutico de dactura utilizado dentro de la empresainstitucion
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public double obtenerSiguientePosibleNumeroFacturaMultiempresa(Connection con, double empresaInstitucion);
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public boolean existeVerificacionDerechosConvenioIngreso( Connection con, String idIngreso, int codigoConvenio);
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param subCuentas
	 * @param facturado
	 * @return
	 */
	public Vector obtenerContratosSubCuentas(Connection con, String idIngreso, Vector subCuentas, String facturado);
	
	/**
	 * metodo que obtiene el listado de solicitudes de interconsulta - procedimientos - evoluciones que tienen asociado
	 * un servicio que requiere interpretacion y la solicitud tiene estado respondida
	 * @param con
	 * @param cuentas
	 * @param subcuentas
	 * @return
	 */
	public HashMap obtenerSolicitudesHCInvalidas( Connection con, Vector cuentas, Vector subCuentas, boolean validarInterpretadas);
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subcuentas
	 * @return
	 */
	public HashMap obtenerSolicitudesHCInvalidasMedicamentos( Connection con, Vector cuentas, Vector subCuentas);
	
	/**
	 * metodo apra obtener las solicitudes q no tienen pool
	 * @param con
	 * @param cuentas
	 * @return
	 */
	public HashMap obtenerSolicitudesSinPool(Connection con, Vector cuentas, Vector subCuentas);
	
	/**
	 * Método que busca las solicitudes cuyos médicos no esten en ningún pool 
	 * @param con
	 * @param cuentas
	 * @return
	 */
	public HashMap obtenerSolicitudesSinMedicoEnPool (Connection con, Vector cuentas, Vector subCuentas);
	
	/**
	 * Método que busca las solicitudes de cirugía las cuales no tienen pool asignado
     * @param con Conexión con la fuente de datos
	 * @param cuentas 
	 * @return
	 */
	public HashMap obtenerSolicitudesCxSinPool(Connection con, Vector cuentas, Vector subCuentas);
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param subcuenta
	 * @param facturado
	 * @return
	 */
	public InfoDatosInt obtenerNaturalezaPacienteSubCuenta(Connection con, String idIngreso, double subCuenta, String facturado);
	
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @param codigoConvenio
	 * @return
	 */
	public DtoVerficacionDerechos obtenerValorCuotaVerificacion(Connection con, String idIngreso, int codigoConvenio);
	
	/**
  	 * Metodo para consultar si una factura existe en BD.
  	 * @param con Connection, conexión con la fuente de datos
  	 * @param codigoFact in, código de la factura
  	 * @return int, 0 si no existe
  	 */
  	public int existeCodigoFactura (Connection con, int codigoFact);
  	
	/**
	 * Consultar el consecutivo de la primera factura
	 * generada por el sistema
	 * @param con Connection, conexión con la fuente de datos	 
	 * @return int, -1 si hay error
	 * @see com.princetonsa.dao.FacturaBORRAMEDao#primeraFacturaGeneradaSistema(Connection)
	 * @author jarloc
	 */
	public int primeraFacturaGeneradaSistema (Connection con);
		
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public int esCuentaValidaProcesoFacturacion(Connection con, int codigoCuenta);
	
	/**
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public boolean facturaTieneAjustesPendientes(Connection con, int codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public boolean facturaTienePagosPendientes(Connection con, int numeroFactura);
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public boolean facturaTieneSaldoPendiente(Connection con, int numeroFactura);
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param codigoAjuste
	 * @return
	 */
	public boolean facturaTieneAjustesPendientesDiferentesAjusteActual(Connection con, int codigoFactura, double codigoAjuste);
	
	/**
	 * @param numeroFactura
	 * @return
	 */
	public boolean esFacturaCerrada(Connection con, int numeroFactura);
	
	/**
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public boolean esFacturaExterna(Connection con, int numeroFactura);
	
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
	public ArrayList facturaTienePagos (Connection con, int numeroFactura, String fecha);
	
	/**
	 * Metodo para consultar si una factura tiene ajustes
	 * aprobados o anulados, en un rango de fecha.
	 * @param con Connection
	 * @param numeroFactura int 
	 * @param fecha String 
	 * @return ResultSet
	 * @author jarloc
	 */
	public ArrayList facturaAjustesAprobadosAnulados(Connection con, int numeroFactura,String fecha);
	
	/**
	 * Metodo que obtiene las solicitudes de cx que no estan liquidadas
	 * @param con
	 * @param cuentas
	 * @param subcuentas
	 * @return
	 */
	public HashMap obtenerSolicitudesCxSinLiquidacion( Connection con, Vector cuentas, Vector subCuentas);
		
	/**
	 * 
	 * @param con
	 * @param codigoCuenta
	 * @return
	 */
	public boolean viaIngresoPermiteCorteFactura(Connection con, int codigoCuenta);
	
	/**
	 * Metodo que evalua los posibles errores de las solicitudes PYP, hasta el momento solo se verifica que las solicitudes 
	 * esten cubiertas por el convenio
	 * @param con
	 * @param cuentas
	 * @parma subCuentas
	 * @return
	 */
	public Vector analisisSolicitudesPYP(Connection con, Vector cuentas, Vector subCuentas);
	
	/**
	 * metodo que evalua si todas las solicitudes son de pyp para no cobrarle nada al paciente
	 * @param con
	 * @param cuentas
	 * @param subCuenta
	 * @return
	 */
	public boolean existenUnicamenteSolicitudesPYP(Connection con, Vector cuentas, double subCuenta); 
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @return
	 */
	public boolean existenSolicitudesNoFacturadas( Connection con, Vector cuentas, Vector subCuentas);
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @return
	 */
	public Vector<String> obtenerRangoInicialFinalFacturaMultiempresa( Connection con, double empresaInstitucion );
	
	/**
	 * 
	 * @param con
	 * @param empresaInstitucion
	 * @param incremento
	 * @return
	 */
	public boolean incrementarConsecutivoFacturaMultiempresa(Connection con, double empresaInstitucion,int incremento);
	
	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentas
	 * @param cubierta
	 * @return
	 */
	public String obtenerSolicitudesSinAutorizacion( Connection con, Vector cuentas, double subCuenta, boolean cubierta);
	
	/**
	 * 
	 * @param con
	 * @param codigoPersona
	 * @param codigoContrato
	 * @return
	 */
	public boolean esPacienteCapitadoVigente(Connection con, int codigoPersona, int codigoContrato, int ingreso);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public Vector<String> obtenerCuentasActivaPacFactura(Connection con, String consecutivoFactura, int institucion);
	
	/**
	 * 
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return
	 */
	public Vector<String> obtenerIngresosAbiertosPacFactura(BigDecimal codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public boolean generaCargoEnSolicitud(Connection con, int numeroSolicitud);
	
	
	/**
	 * 
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	public boolean validarPreglosaRespondida(Connection con,String numeroFactura);

	/**
	 * 
	 * @param codigoFactura
	 * @return
	 */
	public boolean puedoAnularFacturaXGlosa(BigDecimal codigoFactura);

	/**
	 * 
	 * @param con
	 * @param numeroFactura
	 * @return
	 */
	boolean facturaTieneCastigoCartera(Connection con, int numeroFactura);

	/**
	 * 
	 * @param con
	 * @param cuentas
	 * @param subCuentasSeleccionadasVector
	 * @return
	 */
	public ArrayList<String> obtenerMedicoRespondeSinTipoLiquidacion(Connection con,Vector cuentas, Vector subCuentasSeleccionadasVector);

	/**
	 * 
	 * @param codigoFactura
	 * @return
	 */
	public boolean puedoAnularFacturaXReclamaciones(BigDecimal codigoFactura);
}