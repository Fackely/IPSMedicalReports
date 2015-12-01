package com.princetonsa.dao.facturacion;

import java.math.BigDecimal;
import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import util.InfoDatosInt;
import util.ResultadoInteger;

import com.princetonsa.dto.facturacion.DtoAsociosDetalleFactura;
import com.princetonsa.dto.facturacion.DtoDetalleFactura;
import com.princetonsa.dto.facturacion.DtoFactura;

/**
 * 
 * @author wilson
 *
 */
public interface FacturaDao 
{
	/**
	 * 
	 * @param con
	 * @param idIngreso
	 * @return
	 */
	public HashMap obtenerSubCuentasAFacturar(Connection con, String idIngreso);
	
	/**
	 * metodo para obtener el cargo total a facturar x responsable
	 * @param con
	 * @param cuentas
	 * @param codigoConvenios
	 * @return
	 */
	public double obtenerValorCargoTotalAFacturarXSubCuenta(Connection con, Vector cuentas, double subCuenta);
	
	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public ArrayList<DtoDetalleFactura> proponerDetalleFacturaDesdeCargos(Connection con, DtoFactura dtoFactura, int estadoCargo);
	
	/**
	 * 
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public ResultadoInteger insertar(	Connection con, DtoFactura dtoFactura) ;
	
	/**
	 * 
	 * @param con
	 * @param idCuenta
	 * @param loginUsuario
	 * @param estado
	 * @return
	 */
	public int empezarProcesoFacturacionTransaccional(	Connection con,	int idCuenta, 
	        											String loginUsuario, String estado, String idSesion); 
	
	/**
	 * Método para terminar el proceso de facturación
	 * @param con
	 * @param idCuenta
	 * @return true si se finalizó correctamente
	 */
	public int finalizarProcesoFacturacionTransaccional(Connection con, int idCuenta, String estado, String idSesion);
	
	/**
	 * Método para cancelar el proceso de facturación (transaccional)
	 * @param con
	 * @param idCuenta
	 * @param estado
	 * @return numero mayor que cero (0) si se realizó correctamente la cancelación
	 */
	public int cancelarProcesoFacturacionTransaccional(Connection con, int idCuenta, String estado, String idSesion);
	
	/**
	 * metodo para cargar los detalles de la prefactura
	 * @param con
	 * @param dtoFactura
	 * @return
	 */
	public HashMap proponerPreFactura(Connection con, DtoFactura dtoFactura);
	
	/**
	 *  metodo para la actualización del estado de facturacion y el estado del paciente de una factura dada
	 * @param con
	 * @param estadoFacturacion
	 * @param estadoPaciente
	 * @param codigoFactura
	 * @return
	 */
	public boolean actualizarEstadosFactura(Connection con, int estadoFacturacion, int estadoPaciente, int codigoFactura);
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de una factura dada
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public InfoDatosInt obtenerCentroAtencionFactura( Connection con, String consecutivoFactura, int codigoInstitucion);
	
	
	/**
	 * obtiene el codigo - nombre del centro de atencion de una factura dada
	 * @param codigoFactura
	 * @return
	 */
	public InfoDatosInt obtenerCentroAtencionFactura( Connection con, int codigoFactura);
	
	
	/**
	 * Adición Sebastián
	 * Método que consulta el número de cuenta de cobro teniendo como referencia el
	 * consecutivo de la factura
	 * @param con
	 * @param consecutivoFactura
	 * @param institucion
	 * @return numero de cuenta de cobro
	 */
	public double obtenerCuentaCobro(Connection con, int consecutivoFactura, int institucion);
	
	/**
	 * Método que consulta el número de cuenta de cobro teniendo como referencia el codigo factura
	 * @param con
	 * @param codigoFactura
	 * @return numero de cuenta de cobro
	 */
	public double obtenerCuentaCobro(Connection con, int codigoFactura);
	
	/**
	 * Metodo que realiza la busqueda de una factura por su consecutivo y cod de institucion
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 * @throws SQLException
	 */
	public  ResultSetDecorator busquedaPorConsecutivoDianEInstitucion(		Connection con,
																	String consecutivoFactura,
																	int codigoInstitucion,
																	String restricciones);
	
	/**
	 * obtiene el valor neto paciente
	 * @param con
	 * @param consecutivoFactura
	 * @param codigoInstitucion
	 * @return
	 */
	public double obtenerValorNetoPaciente(Connection con, String consecutivoFactura, int codigoInstitucion);
	
	/**
	 * Método que cancela todos los procesos de facturación en proceso
	 * 
	 * @param con
	 *            Conexión con la BD
	 * @return numero de cancelaciones
	 */
	public int cancelarTodosLosProcesosDeFacturacion(Connection con) ;
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerContratoFactura(Connection con, int codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param cargarDetalles
	 * @return
	 */
	public DtoFactura cargarFactura( Connection con, String codigoFactura, boolean cargarDetalles);
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public double obtenerValorConvenioFactura(Connection con, String codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerCodigoPacienteFactura(Connection con, String codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public String obtenerIdIngresoFactura( Connection con, String codigoFactura);
	
	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @return
	 */
	public int obtenerFormatoFacturaXCodigoFact(Connection con, String codigoFactura);

	/**
	 * actualiza el numero de la cuenta de cobro  en la factura
	 * @param con
	 * @param numeroCuentaCobroCapitada
	 * @param codigoFactura
	 * @return
	 */
	public boolean updateNumeroCuentaCobroCapitadaEnFactura(Connection con, String numeroCuentaCobroCapitada, String codigoFactura);
	
	/**
	 * Adición Sebastián Método usado para desasignar el numero de cuenta de
	 * cobro de una factura en el caso de que se haya hehco una inactivación de
	 * factura
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param institucion
	 * @return
	 */
	public int desasignarCuentaCobro(Connection con, String codigoFactura,int institucion);
	
	/**
	 * 
	 * @param con
	 * @param subCuenta
	 * @return
	 */
	public boolean insertarHistoricoSubCuenta(Connection con, double subCuenta, double codigoFactura);

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param restricciones
	 * @return
	 */
	public ResultSetDecorator busquedaPorCodigo(Connection con, int codigoFactura, String restricciones);
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public ArrayList<DtoAsociosDetalleFactura> cargarAsociosDetalleFactura(Connection con, int codigoDetalle);

	/**
	 * 
	 * @param con
	 * @param codigoFactura
	 * @param consecutivo
	 * @return
	 */
	public boolean actualizarConsecutivoFactura(Connection con, int codigoFactura, String consecutivo);

	/**
	 * 
	 * @param con
	 * @param cuenta
	 * @param idSesion
	 * @return
	 */
	public boolean eliminarCuentaProcesoFacturacion(Connection con, BigDecimal cuenta, String idSesion);
	
	

	/**
	 * @param con
	 * @param numeroFactura
	 * @return si tiene  autorizados
	 */
	public  Boolean  tieneSolicitudesSinAutorizar(Connection con, Integer numeroSolicitud);
}
