
/*
 * Creado   28/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;

/**
 * Interfaz para el acceder a la fuente de datos de los recibos de caja
 *
 * @version 1.0, 28/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public interface RecibosCajaDao 
{
    /**
     * metodo para realizar la consulta de facturas
     * @param con
     * @param consecutivoFact
     * @param fechaFact
     * @param numeroIdentif
     * @param tipoIdentif
     * @param institucion
     * @param valorConcepto
     * @return ResultSet
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#generarConsultaFacturas(java.sql.Connection,int,String,String,String,int,String)
     * @author jarloc
     */
    public ResultSetDecorator generarConsultaFacturas(Connection con,int consecutivoFact,
														            String fechaFact,String numeroIdentif,
														            String tipoIdentif,int institucion,
														            String valorConcepto);
    /**
     * metodo para generar la consulta de pacientes
     * @param con
     * @param primerNombre
     * @param segundoNombre
     * @param primerApellido
     * @param segundoApellido
     * @param tipoIdentificacion
     * @param numeroIdentificacion
     * @param institucion
     * @param controlarAbonoPaciente
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#generarConsultaPacientes(java.sql.Connection,String,String,String,String,String,int)
     * @author jarloc
     * @return
     */
    public ResultSetDecorator generarConsultaPacientes(Connection con,String primerNombre,
														            String segundoNombre,String primerApellido,
														            String segundoApellido,String tipoIdentificacion,
														            String numeroIdentificacion,int institucion, boolean controlarAbonoPaciente);
    /**
     * Metodo para consultar el tipo detalle
     * de formas de pago 
     * @param con
     * @return
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#generarConsultaFormaPago(java.sql.Connection,int)
     */    
    public ResultSetDecorator generarConsultaFormaPago(Connection con,int consecutivo);
    
    /**
     * metodo para realizar el insert de recibos de caja
     * @param con Connection
     * @param numeroReciboCaja String, consecutivo del recibo de caja
     * @param institucion int 
     * @param usuario String
     * @param caja String 
     * @param fecha String 
     * @param hora String 
     * @param recibidoDe String 
     * @param observaciones String
     * @param estadoRC 
     * @return true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarReciboCaja(java.sql.Connection,String,int,String,String,String,String,String,String)
     */
    public int  insertarReciboCaja(Connection con,
											            String numeroReciboCaja,int institucion,
											            String usuario,String caja,
											            String fecha,String hora,
											            String recibidoDe,String observaciones, int codigoCentroAtencion, int estadoRC);
    

    /**
     * metodo para insertar el detalle de pagos
     * @param con Connection     
     * @param numeroRecibo String 
     * @param institucion int
     * @param formaPago String 
     * @param valor String
     * @return true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarDetallePagoRC(java.sql.Connection,String,int,String,String)
     */
    public boolean insertarDetallePagoRC (Connection con,String numeroRecibo,int institucion,String formaPago,String valor);
    
    /**
     * metodo para insertar el detalle de pagos
     * @param con Connection      
     * @param numeroRecibo String 
     * @param institucion int
     * @param formaPago String 
     * @param valor String
     * @return true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarDetallePagoRC(java.sql.Connection,String,String,int,String,String)
     */
    public boolean insertarDetallePagoRC (Connection con, int codigoDetallePagoRC,String numeroRecibo,int institucion,String formaPago,String valor);

    /**
     * metodo para insertar los movimientos de cheques
     * @param con Connection     
     * @param detPago  String,
     * @param numeroCheque String,
     * @param codigoBanco String,
     * @param numeroCuenta String,
     * @param codCiudadPlaza String,
     * @param codDeptoPlaza String,
     * @param fechaGiro String,
     * @param valor String,
     * @param girador String,
     * @param direccion String,
     * @param codCiudadGirador String,
     * @param codDeptoGirador String,
     * @param telefono String,
     * @param observaciones String,
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarMovimientoCheque(java.sql.Connection,String,String,String,String,String,String,String,String,String,String,String,String,String,String)
     */
    public boolean insertarMovimientoCheque(Connection con,
														            String detPago,
														            String numeroCheque,String codigoBanco,
														            String numeroCuenta,String codCiudadPlaza,
														            String codDeptoPlaza,String fechaGiro,
														            String valor,String girador,String direccion,
														            String codCiudadGirador,String codDeptoGirador,
														            String telefono,String observaciones,String codPaisPlaza,String codPaisGirador, String autorizacion);
    /**
     * metodo para insertar los movimientos de tarjetas de
     * credito
     * @param con Connection     
     * @param detPago String,
     * @param codTarjeta String,
     * @param numTarjeta String,
     * @param numComprobante String,
     * @param numAutorizacion String,
     * @param fecha String,
     * @param valor String,
     * @param girador String,
     * @param direccion String,
     * @param codCiudad String,
     * @param codDepto String,
     * @param telefono String,
     * @param observaciones String,
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarMovimientosTarjetas(java.sql.Connection,String,String,String,String,String,String,String,String,String,String,String,String,String,String)
     */
    public boolean insertarMovimientosTarjetas (Connection con,
															            String detPago,
															            String codTarjeta,String numTarjeta,
															            String numComprobante,String numAutorizacion,
															            String fecha, String valor,String girador,
															            String direccion,String codCiudad,String codDepto,
															            String telefono,String observaciones,String codPais,
															            String fechaVencimiento, String codigoSeguridad,int entidadFinanciera);
    /**
     * metodo para consultar el ultimo codigo
     * del recibo de caja insertado
     * @param con Connection
     * @param institucion int
     * @return int, codigo del recibo de caja
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#ultimoReiboCajaInsertado(java.sql.Connection,int)
     */
    public int ultimoReiboCajaInsertado(Connection con,int institucion);
    
    /**
     * metodo para consultar el ultimo codigo insertado 
     * de detalle de pagos
     * @param con Connection
     * @param institucion
     * @return int, cï¿½digo del detalle
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#ultimoCodigoDetallePagos(java.sql.Connection,int)
     */
    public int ultimoCodigoDetallePagos(Connection con,int institucion);
    /**
     * metodo para insertar el detalle del concepto
     * del recibo de caja
     * @param con Connection     
     * @param numReciboCaja String
     * @param institucion String
     * @param concepto String
     * @param docSoporte String
     * @param valor String
     * @param tipoIdBeneficiario String
     * @param numIdBeneficiario String
     * @param nombreBeneficiario String
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarDetalleConceptosRecibosCaja(java.sql.Connection,String,String,String,String,String,String,String,String)
     */
    public boolean insertarDetalleConceptosRecibosCaja(Connection con,
																            String numReciboCaja,
																            String institucion,String concepto,
																            String docSoporte,String valor,
																            String tipoIdBeneficiario,String numIdBeneficiario,
																            String nombreBeneficiario,
																            String deudorm,String clase_deudorco,String num_id_deudorco,
																            int inst_deudor, int ingreso, String codPaciente,  String codigoTipoConcepto);
    /**
     * /**
     * metodo para actualizar el documento
     * de detalle conceptos     
     * @param con Connection 
     * @param documento String
     * @param consecutivo int 
     * @param institucion int 
     * @return boolean, true cuando es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarDetalleConceptosRecibosCaja(java.sql.Connection,String,int,int)
     */  
    public boolean actualizarDocumentoDetalleConceptos(Connection con,String documento,int consecutivo,int institucion);
    /**
     * metodo para actualizar el estado paciente de 
     * la factura a cancelado.
     * @param con Connection
     * @param institucion int
     * @param codigoFact int 
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#actualizarEstadoPacienteFactura(java.sql.Connection,int,int)
     */
    public boolean actualizarEstadoPacienteFactura(Connection con,int institucion, int codigoFact);
    /**
     * metodo para insertar los pagos de realizados
     * de facturas paciente
     * @param con Connection
     * @param tipoDoc String
     * @param documento String
     * @param factura String
     * @param valor String
     * @param estado String
     * @param institucion String 
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#actualizarEstadoPacienteFactura(java.sql.Connection,String,String,String,String,int,int)
     */
    public boolean insertarPagosFacturaPaciente(Connection con,String tipoDoc,
														              String documento,String factura,
														              String valor,int estado,int institucion);
    /**
     * metodo para insertar los pagos generales
     * empresa
     * @param con Connection
     * @param convenio int
     * @param tipoDoc int
     * @param documento String
     * @param estado int 
     * @param valor String
     * @param institucion int
     * @param codigoPk TODO
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarPagosGeneralEmpresa(java.sql.Connection,int,int,String,int,String,int)
     */
    public boolean insertarPagosGeneralEmpresa(Connection con,
												             int convenio,int tipoDoc,
												             String documento,int estado,
												             String valor,int institucion,String fechaDocumento,String deudor, int codigoContrato, int codigoPk);
    
    /**
     * metodo para insertar movimientos
     * de abonos
     * @param con Connection
     * @param paciente String
     * @param documento String 
     * @param tipo String
     * @param valor String
     * @param fecha String
     * @param hora String 
     * @param institucion int
     * @param ingreso
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarMovimientosAbonos(java.sql.Connection,String,String,String,String,String,String,int)
     */
    public boolean insertarMovimientosAbonos(Connection con,
												           String paciente,String documento,
												           String tipo,String valor,
												           String fecha,String hora,int institucion, int centroAtencionDuenio, Integer ingreso, int codigoCentroAtencion);
    
    /**
     * metodo para consultar el ultimo codigo
     * de movimientos abonos insertado
     * @param con Connection
     * @param institucion
     * @return int, cï¿½digo del detalle
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#ultimoCodigoMovimientoAbonos(java.sql.Connection,int)
     */
    public int ultimoCodigoMovimientoAbonos(Connection con,int institucion);
    /**
     * metodo para consultar el ultimo codigo 
     * detalle de conceptos.
     * @param con Connection
     * @param institucion
     * @return int, cï¿½digo del detalle
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#ultimoCodigoDetalleConceptos(java.sql.Connection,int)
     */
    public int ultimoCodigoDetalleConceptos(Connection con,int institucion);
    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @param nuevoEstado
     * @return
     */
    public boolean actualizarEstadoReciboCaja(Connection con, String numeroReciboCaja, int institucion, int nuevoEstado);
    
    /**
     * 
     * @param con
     * @param reciboCaja
     * @param codigoInstitucionInt
     * @return
     */
	public String obtenerCajaCajeroRC(Connection con, String reciboCaja, int codigoInstitucionInt);

	  /**
     * Metodo encargado de verificar si un recibo de caja
     * se encuentra registrado en aplicacion de pagos de facturas varias.
     * @param connection
     * @param codigoResivoCaja
     * @param institucion
     * @return
     */
    public boolean estaRegistradoEnAplicacionPagosFacturasVarias (Connection connection, String codigoResivoCaja,String institucion);
    
    
    /**
	 * Busqueda
	 * @param con
     * @param HashMap parametros
     * @return ArrayList<DtoRecibosCaja>
	 */
	public ArrayList<DtoRecibosCaja> facturasVarias(Connection con, HashMap parametros);
	
	
	/**
	 * 
	 * @param con
     * @param parametros
     * @return
	 */
	public boolean tipoConceptoIsMulta(Connection con, HashMap parametros);
 
	/**
	 * Actualizar deudor
     * @param con
     * @param HashMap parametros
     * @return boolean
     */
    public boolean actualizarDeudorPagosGenEmp(Connection con, HashMap parametros);
    
    /**
	 * valor concepto ingreso tesoreria
	 * @param con
     * @param parametros
     * @return
	 */
	public String getValorConceptoIngresoTesoreria(Connection con, HashMap parametros);
	
	/**
	 * metodo que recalcula el valor de anticipo disponible
	 * @param con
	 * @param usuario
	 * @param codigoContrato
	 * @author Ricardo Ruiz
	 * @return
	 */
	public boolean recalcularValorAnticipoDisponible(Connection con, String usuario, int codigoContrato, String valorAnticipo);
	
	public boolean actualizarEstadoTarjeta(Connection con, String tipoconcepto, String codFacturaVaria);
	
	public boolean insertarMovimientoBono(Connection con, String serial, String consecutivoDetallePagosRC, String observaciones);
	public boolean recibosGeneradoCorrectamente(Connection con,
			String numReciboCaja);
	
	/**
	 * Método que obtiene el ingreso del paciente asociado al recibo de caja generado
	 * @param consecutivoReciboCaja
	 * @return idIngreso
	 */
	public Integer obtenerIngresoPacientePorConsecutivoReciboCaja(String consecutivoReciboCaja);
    
}