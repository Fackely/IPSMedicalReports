
/*
 * Creado   28/09/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.5.0_03
 */
package com.princetonsa.dao.postgresql;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;

import com.princetonsa.dao.RecibosCajaDao;
import com.princetonsa.dao.sqlbase.SqlBaseRecibosCajaDao;
import com.princetonsa.dao.sqlbase.util.SqlBaseConsultasBirtDao;

/**
 * Esta clase implementa el contrato estipulado en <code>RecibosCajaDao</code>, proporcionando los servicios
 * de acceso a una base de datos PostgreSQL requeridos por la clase <code>RecibosCaja</code>
 *
 * @version 1.0, 28/09/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class PostgresqlRecibosCajaDao implements RecibosCajaDao 
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
														            String valorConcepto)
    {
        return SqlBaseRecibosCajaDao.generarConsultaFacturas(con,consecutivoFact,fechaFact,numeroIdentif,tipoIdentif,institucion,valorConcepto);
    }
    
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
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#generarConsultaPacientes(java.sql.Connection,String,String,String,String,String,int)
     * @author jarloc
     * @return
     */
    public ResultSetDecorator generarConsultaPacientes(Connection con,String primerNombre,
														            String segundoNombre,String primerApellido,
														            String segundoApellido,String tipoIdentificacion,
														            String numeroIdentificacion,int institucion, boolean controlarAbonoPaciente)
    {
        return SqlBaseRecibosCajaDao.generarConsultaPacientes(con,primerNombre,segundoNombre,primerApellido,segundoApellido,tipoIdentificacion,numeroIdentificacion,institucion, controlarAbonoPaciente);
    }
    /**
     * Metodo para consultar el tipo detalle
     * de formas de pago 
     * @param con
     * @return
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#generarConsultaFormaPago(java.sql.Connection,int)
     */    
    public ResultSetDecorator generarConsultaFormaPago(Connection con,int consecutivo)
    {
        return SqlBaseRecibosCajaDao.generarConsultaFormaPago(con,consecutivo);
    }
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
     * @return true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarReciboCaja(java.sql.Connection,String,int,String,String,String,String,String,String)
     */
    public int  insertarReciboCaja(Connection con,
											            String numeroReciboCaja,int institucion,
											            String usuario,String caja,
											            String fecha,String hora,
											            String recibidoDe,String observaciones, int codigoCentroAtencion, int estadoRC)
    {
        return SqlBaseRecibosCajaDao.insertarReciboCaja(con,numeroReciboCaja,institucion,usuario,caja,fecha,hora,recibidoDe,observaciones,codigoCentroAtencion,estadoRC);  
    }
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
    public boolean insertarDetallePagoRC (Connection con,String numeroRecibo,int institucion,String formaPago,String valor)
    {
        /*
         * query para insertar el detalle del pago en la tabla detalle_pagos_rc
         */
        String insertarDetallePagosRCStr="INSERT " +
													  "INTO detalle_pagos_rc " +
													  "(consecutivo,numero_recibo_caja,institucion,forma_pago,valor) " +
													  "VALUES (NEXTVAL('seq_detalle_pagos_rc'),?,?,?,?)";
        return SqlBaseRecibosCajaDao.insertarDetallePagoRC(con,numeroRecibo,institucion,formaPago,valor,insertarDetallePagosRCStr);       
    }

    @Override
    public boolean insertarDetallePagoRC (Connection con, int codigoDetallePagoRC,String numeroRecibo,int institucion,String formaPago,String valor)
    {
        /*
         * query para insertar el detalle del pago en la tabla detalle_pagos_rc
         */
        String insertarDetallePagosRCStr="INSERT " +
													  "INTO detalle_pagos_rc " +
													  "(consecutivo,numero_recibo_caja,institucion,forma_pago,valor) " +
													  "VALUES (?,?,?,?,?)";
        return SqlBaseRecibosCajaDao.insertarDetallePagoRC(con,codigoDetallePagoRC,numeroRecibo,institucion,formaPago,valor,insertarDetallePagosRCStr);       
    }

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
														            String telefono,String observaciones,String codPaisPlaza,String codPaisGirador, String autorizacion)
    {
        /**
         * query para insertar en la tabla movimientos_cheques
         */
        final String insertarMovimientosChequesStr="INSERT " +
        														  "INTO movimientos_cheques " +
        														  "(codigo,det_pago_rc,numero_cheque,entidad_financiera," +
        														  "numero_cuenta,ciudad_plaza,departamento_plaza," +
        														  "fecha_giro,valor,girador,direccion,ciudad_girador," +
        														  "departamento_girador,telefono,observaciones,pais_plaza,pais_girador,autorizacion) "+
        														  "VALUES (NEXTVAL('seq_mov_cheques'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return SqlBaseRecibosCajaDao.insertarMovimientoCheque(con,detPago,numeroCheque,codigoBanco,numeroCuenta,codCiudadPlaza,codDeptoPlaza,fechaGiro,valor,girador,direccion,codCiudadGirador,codDeptoGirador,telefono,observaciones,codPaisPlaza,codPaisGirador,autorizacion,insertarMovimientosChequesStr); 
    }
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
															            String fechaVencimiento, String codigoSeguridad,
															            int entidadFinanciera)
    {
        /**
         * query para insertar en la tabla movimientos_tarjetas
         */
        final String insertarMovimientosTarjetasStr="INSERT " +
        														   "INTO movimientos_tarjetas " +
        														   "(codigo,det_pago_rc,codigo_tarjeta,numero_tarjeta," +
        														   "numero_comprobante,numero_autorizacion,fecha," +
        														   "valor,girador,direccion,ciudad,departamento,telefono,observaciones,pais,fecha_vencimiento,codigo_seguridad,entidad_financiera) " +
        														   "VALUES (NEXTVAL('seq_mov_tarjetas'),?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        return SqlBaseRecibosCajaDao.insertarMovimientosTarjetas(con,detPago,codTarjeta,numTarjeta,numComprobante,numAutorizacion,fecha,valor,girador,direccion,codCiudad,codDepto,telefono,observaciones,codPais,fechaVencimiento,codigoSeguridad,insertarMovimientosTarjetasStr, entidadFinanciera);  
    }
    /**
     * metodo para consultar el ultimo codigo
     * del recibo de caja insertado
     * @param con Connection
     * @param institucion int
     * @return int, codigo del recibo de caja
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#ultimoReiboCajaInsertado(java.sql.Connection,int)
     */
    public int ultimoReiboCajaInsertado(Connection con,int institucion)
    {
        return SqlBaseRecibosCajaDao.ultimoReiboCajaInsertado(con,institucion); 
    }
    /**
     * metodo para consultar el ultimo codigo insertado 
     * de detalle de pagos
     * @param con Connection
     * @param institucion
     * @return int, c�digo del detalle
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#ultimoCodigoDetallePagos(java.sql.Connection,int)
     */
    public int ultimoCodigoDetallePagos(Connection con,int institucion)
    {
        return SqlBaseRecibosCajaDao.ultimoCodigoDetallePagos(con,institucion); 
    }
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
     * @see com.princetonsa.dao.RecibosCajaDao#insertarDetalleConceptosRecibosCaja(java.sql.Connection,String,String,String,String,String,String,String,String)
     */
    public boolean insertarDetalleConceptosRecibosCaja(Connection con,
																            String numReciboCaja,
																            String institucion,String concepto,
																            String docSoporte,String valor,
																            String tipoIdBeneficiario,String numIdBeneficiario,
																            String nombreBeneficiario,
																            String deudor, String clase_deudorco,String num_id_deudorco,
																            int inst_deudor, int ingreso, String codPaciente,  String codigoTipoConcepto)
    {
             return SqlBaseRecibosCajaDao.insertarDetalleConceptosRecibosCaja(con,numReciboCaja,institucion,concepto,docSoporte,valor,tipoIdBeneficiario,numIdBeneficiario,nombreBeneficiario,deudor,clase_deudorco,num_id_deudorco,inst_deudor,ingreso, codPaciente, codigoTipoConcepto);  
    }    
    /**     
     * metodo para actualizar el documento
     * de detalle conceptos     
     * @param con Connection 
     * @param documento String
     * @param consecutivo int 
     * @param institucion int 
     * @return boolean, true cuando es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarDetalleConceptosRecibosCaja(java.sql.Connection,String,int,int)
     */  
    public boolean actualizarDocumentoDetalleConceptos(Connection con,String documento,int consecutivo,int institucion)
    {
        return SqlBaseRecibosCajaDao.actualizarDocumentoDetalleConceptos(con,documento,consecutivo,institucion);
    }
    /**
     * metodo para actualizar el estado paciente de 
     * la factura a cancelado.
     * @param con Connection
     * @param institucion int
     * @param codigoFact int 
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#actualizarEstadoPacienteFactura(java.sql.Connection,int,int)
     */
    public boolean actualizarEstadoPacienteFactura(Connection con,int institucion, int codigoFact)
    {
        return SqlBaseRecibosCajaDao.actualizarEstadoPacienteFactura(con,institucion,codigoFact);
    }
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
														              String valor,int estado,int institucion)
    {
        /**
         * query para insertar el pago de facturas pacientes
         */
        final String insertarPagosFacturaPacienteStr="INSERT " +
    													    		"INTO " +
    													    		"pagos_facturas_paciente (codigo,tipo_doc,documento,factura,valor,estado,institucion) " +
    													    		"VALUES (NEXTVAL('seq_pagos_fac_paciente'),?,?,?,?,?,?)"; 
        return SqlBaseRecibosCajaDao.insertarPagosFacturaPaciente(con,tipoDoc,documento,factura,valor,estado,institucion,insertarPagosFacturaPacienteStr);
    }
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
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarPagosGeneralEmpresa(java.sql.Connection,int,int,String,int,String,int)
     */
    public boolean insertarPagosGeneralEmpresa(Connection con,
												             int convenio,int tipoDoc,
												             String documento,int estado,
												             String valor,int institucion,String fechaDocumento,
												             String deudor, int codigoContrato, int codigoPk)
    {
        /**
         * query para insertar en pagos_general_empresa
         */
    	String secuencia = "nextval('seq_pagos_general_empresa')";
        final String insertarPagosGeneralEmpresaStr="INSERT " +
    			    								"INTO pagos_general_empresa(codigo,convenio,tipo_doc,documento,estado,valor,institucion,fecha_documento,deudor,contrato) " +
    			    								"VALUES ("+secuencia+",?,?,?,?,?,?,?,?,?)";
        return SqlBaseRecibosCajaDao.insertarPagosGeneralEmpresa(con,insertarPagosGeneralEmpresaStr,convenio,tipoDoc,documento,estado,valor,institucion,fechaDocumento,deudor,codigoContrato  /*, codigoPk*/);
    }
    
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
     * @return boolean, true si es efectivo
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#insertarMovimientosAbonos(java.sql.Connection,String,String,String,String,String,String,int)
     */
    public boolean insertarMovimientosAbonos(Connection con,
												           String paciente,String documento,
												           String tipo,String valor,
												           String fecha,String hora,int institucion, int centroAtencionDuenio, 
												           Integer ingreso, int codigoCentroAtencion)
    {
        /**
         * query para insertar los movimientos de abonos
         */
        final String insertarMovimientosAbonosStr="INSERT " +
    												    		 "INTO movimientos_abonos(codigo,paciente,codigo_documento,tipo,valor,fecha,hora,institucion, centro_atencion_duenio, ingreso, centro_atencion) " +
    												    		 "VALUES (NEXTVAL('seq_movimientos_abonos'),?,?,?,?,?,?,?,?,?,?)";
        return SqlBaseRecibosCajaDao.insertarMovimientosAbonos(con,insertarMovimientosAbonosStr,paciente,documento,tipo,valor,fecha,hora,institucion, centroAtencionDuenio, ingreso, codigoCentroAtencion);
    }
    /**
     * metodo para consultar el ultimo codigo
     * de movimientos abonos insertado
     * @param con Connection
     * @param institucion
     * @return int, c�digo del detalle
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#ultimoCodigoMovimientoAbonos(java.sql.Connection,int)
     */
    public int ultimoCodigoMovimientoAbonos(Connection con,int institucion)
    {
        return SqlBaseRecibosCajaDao.ultimoCodigoMovimientoAbonos(con,institucion); 
    }
    /**
     * metodo para consultar el ultimo codigo 
     * detalle de conceptos.
     * @param con Connection
     * @param institucion
     * @return int, c�digo del detalle
     * @see com.princetonsa.dao.SqlBase.SqlBaseRecibosCajaDao#ultimoCodigoDetalleConceptos(java.sql.Connection,int)
     */
    public int ultimoCodigoDetalleConceptos(Connection con,int institucion)
    {
        return SqlBaseRecibosCajaDao.ultimoCodigoDetalleConceptos(con,institucion);  
    }
    
    /**
     * @param con
     * @param numeroReciboCaja
     * @param institucion
     * @param nuevoEstado
     * @return
     */
    public boolean actualizarEstadoReciboCaja(Connection con, String numeroReciboCaja, int institucion, int nuevoEstado)
    {
        return SqlBaseRecibosCajaDao.actualizarEstadoReciboCaja(con,numeroReciboCaja,institucion,nuevoEstado);
    }

    /**
     * 
     */
	public String obtenerCajaCajeroRC(Connection con, String reciboCaja, int codigoInstitucionInt) 
	{
		 return SqlBaseRecibosCajaDao.obtenerCajaCajeroRC(con,reciboCaja,codigoInstitucionInt);
	}
	
	
	  /**
   * Metodo encargado de verificar si un recibo de caja
   * se encuentra registrado en aplicacion de pagos de facturas varias.
   * @param connection
   * @param codigoResivoCaja
   * @param institucion
   * @return
   */
  public boolean estaRegistradoEnAplicacionPagosFacturasVarias (Connection connection, String codigoResivoCaja,String institucion)
  {
  	return SqlBaseRecibosCajaDao.estaRegistradoEnAplicacionPagosFacturasVarias(connection, codigoResivoCaja, institucion);
  }
  
  /**
	 * Busqueda
	 * @param con
	 * @param HashMap parametros
	 * @return ArrayList<DtoRecibosCaja>
	 */
	public ArrayList<DtoRecibosCaja> facturasVarias(Connection con, HashMap parametros)
	{
		return SqlBaseRecibosCajaDao.facturasVarias(con, parametros);
	}
	
	
	/**
	 * 
	 * @param con
	 * @param parametros
	 * @return
	 */
	public boolean tipoConceptoIsMulta(Connection con, HashMap parametros)
	{
		return SqlBaseRecibosCajaDao.tipoConceptoIsMulta(con, parametros);
	}
	
	/**
	 * Actualizar deudor
     * @param con
     * @param HashMap parametros
     * @return boolean
     */
    public boolean actualizarDeudorPagosGenEmp(Connection con, HashMap parametros)
    {
    	return SqlBaseRecibosCajaDao.actualizarDeudorPagosGenEmp(con, parametros);
    }
    
    /**
	 * valor concepto ingreso tesoreria
	 * @param con
     * @param parametros
     * @return
	 */
	public String getValorConceptoIngresoTesoreria(Connection con, HashMap parametros)
	{
		return SqlBaseRecibosCajaDao.getValorConceptoIngresoTesoreria(con, parametros);
	}
	
	/**
	 * metodo que recalcula el valor de anticipo disponible
	 * @param con
	 * @param usuario
	 * @param codigoContrato
	 * @return
	 */
	public boolean recalcularValorAnticipoDisponible(Connection con, String usuario, int codigoContrato, String valorAnticipo)
	{
		return SqlBaseRecibosCajaDao.recalcularValorAnticipoDisponible(con, usuario, codigoContrato, valorAnticipo);
	}
	
	public boolean actualizarEstadoTarjeta(Connection con, String tipoconcepto, String codFacturaVaria)
	{
		return SqlBaseRecibosCajaDao.actualizarEstadoTarjeta(con, tipoconcepto, codFacturaVaria);
	}
	
	/**
	 * 
	 */
	public String impresionConceptosReciboCaja(HashMap parametros)
	{
		return SqlBaseConsultasBirtDao.impresionConceptosReciboCaja(parametros);
	}
	
	@Override
	public boolean insertarMovimientoBono(Connection con, String serial, String consecutivoDetallePagosRC, String observaciones)
	{
		return SqlBaseRecibosCajaDao.insertarMovimientoBono(con, serial, consecutivoDetallePagosRC, observaciones);
	}


	@Override
	public boolean recibosGeneradoCorrectamente(Connection con,
			String numReciboCaja) {
		return SqlBaseRecibosCajaDao.recibosGeneradoCorrectamente(con, numReciboCaja);
	}
	
	/** (non-Javadoc)
	 * @see com.princetonsa.dao.RecibosCajaDao#obtenerIngresoPacientePorConsecutivoReciboCaja(int, java.lang.String)
	 */
	@Override
	public Integer obtenerIngresoPacientePorConsecutivoReciboCaja(String consecutivoReciboCaja) {
		return SqlBaseRecibosCajaDao.obtenerIngresoPacientePorConsecutivoReciboCaja(consecutivoReciboCaja) ;
	}
	
}
