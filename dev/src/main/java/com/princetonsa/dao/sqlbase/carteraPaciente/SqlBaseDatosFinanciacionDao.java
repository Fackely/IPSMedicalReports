
package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;

//import org.axioma.util.fechas.UtilidadesFecha;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDeudoresDatosFinan;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.facturacion.DtoFactura;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author V�cto G�mez L.
 *
 */
public class SqlBaseDatosFinanciacionDao 
{
	/**
	 * mensajes de arror
	 * */
	static Logger logger = Logger.getLogger(SqlBaseDatosFinanciacionDao.class);

	/**
	 * cadena para la insercion datos financiacion
	 */
	private static final String strInsertDatosFinanciacion = "INSERT INTO carterapaciente.datos_financiacion(" +
			"codigo_pk, "+// 1
			"detalle_pago_rc, "+// 2
			"detalle_concepto_rc, "+// 3
			"ingreso, "+// 4
			"consecutivo, "+// 5
			"anio_consecutivo, "+// 6
			"tipo_documento, "+// 7
			"codigo_factura, "+// 8
			"fecha_modifica, "+// 
			"hora_modifica, "+//  
			"usuario_modifica, "+// 9 
			"fecha_inicio, "+// 10 
			"dias_por_couta, "+// 11 
			"observaciones, "+// 12
			"nro_coutas," +
			"codigo_pk_docgarantia," +
			"consecutivo_factura," +
			"fecha_elaboracion_factura) "+// 13
			"VALUES (?,?,?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?,?,?,?,?,?) ";

	/**
	 * cadena de insercion el deudor y/o codeudor 
	 */
	private static final String strInsertDeudorDatosFinan = "INSERT  INTO carterapaciente.deudores_datos_finan(" +
			"datos_financiacion, " +
			"codigo_pk_deudor)" +
			"VALUES (?,?) "; 
	
	/**
	 * cadena de inserci�n cuotas datos financiacion
	 */
	private static final String strInsertCuotasDatosFinanciacion = "INSERT INTO carterapaciente.cuotas_datos_financiacion(" +
			"codigo_pk, " +
			"dato_financiacion, " +
			"numero_documento, " +
			"valor_couta, " +
			"fecha_modifica, " +
			"hora_modifica, " +
			"usuario_modifica, " +
			"activo," +
			"nro_cuota)" +
			"VALUES (?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBD()+",?,?,?)";
	
	/**
	 * cadena que consulta el deudor y/o codeudor de un documento de garantia previamente registrado
	 * por la funcionalidad de documentos de garantia
	 */
	private static final String strConsultarDeudoresCO = "SELECT " +
			"d.codigo_pk AS codigoPk, " +
			"d.tipo_deudorco AS tipo_deudorco, " +
			"d.codigo_paciente AS codigo_paciente, " +
			"d.ingreso AS ingreso, " +
			"d.institucion AS institucion, " +
			"d.clase_deudorco AS clase_deudorco, " +
			"d.tipo_identificacion AS tip_ident, " +
			"d.numero_identificacion AS num_ident, " +
			"d.primer_apellido AS p_apellido," +
			"coalesce(d.segundo_apellido,' ') AS s_apellido," +
			"d.primer_nombre AS p_nombre," +
			"coalesce(d.segundo_nombre,' ') AS s_nombre," +
			"d.direccion_reside AS direccion," +
			"coalesce(d.telefono_reside,' ') AS tel_fijo, " +
			"d.tipo_ocupacion AS tipo_ocupacion, " +
			"d.ocupacion AS ocupacion, " +
			"coalesce(d.empresa,' ') AS empresa, " +
			"coalesce(d.cargo,' ') AS cargo, " +
			"coalesce(d.antiguedad,' ') AS antiguedad, " +
			"coalesce(d.direccion_oficina,' ') AS dir_oficina, " +
			"coalesce(d.telefono_oficina,' ') AS tel_oficina " +
			"FROM deudorco d " +
			"WHERE d.ingreso = ? " +
			"AND d.institucion = ? " +
			"AND d.clase_deudorco = ? ";

	
	
	/**
	 * cadena de consulta datos deudor y/o codeudor
	 */
	private static final String strConsultarDatosPersona = "SELECT " +
			"p.codigo AS codigo_paciente, " +
			"p.tipo_identificacion AS tip_ident, " +
			"p.numero_identificacion AS num_ident, " +
			"p.primer_apellido AS p_apellido," +
			"coalesce(p.segundo_apellido,' ') AS s_apellido," +
			"p.primer_nombre AS p_nombre," +
			"coalesce(p.segundo_nombre,' ') AS s_nombre," +
			"p.direccion AS direccion," +
			"coalesce(p.telefono,' ') AS tel_fijo " +
			"FROM facturacion.facturas f " +
			"INNER JOIN administracion.personas p ON (f.cod_paciente = p.codigo) " +
			"WHERE f.consecutivo_factura = ? ";
	/**
	 * cadena de consulta datos reponsble paciente
	 */
	private static final String strConsultaDatosResponsable = "SELECT " +
			"rp.codigo AS codigo_paciente, " +
			"rp.tipo_identificacion AS tip_ident, " +
			"rp.numero_identificacion AS num_ident, " +
			"rp.primer_apellido AS p_apellido, " +
			"coalesce(rp.segundo_apellido,' ') AS s_apellido, " +
			"rp.primer_nombre AS p_nombre, " +
			"coalesce(rp.segundo_nombre,' ') AS s_nombre, " +
			"rp.direccion AS direccion, " +
			"coalesce(rp.telefono,' ') AS tel_fijo " +
			"FROM facturacion.facturas f " +
			"INNER JOIN manejopaciente.cuentas c ON (f.cuenta = c.id) " +
			"INNER JOIN manejopaciente.responsables_pacientes rp ON (rp.codigo = c.codigo_responsable_paciente) " +
			"WHERE f.consecutivo_factura = ? ";
	/**
	 * Verificaion existencia deudo y/o codeudor
	 */
	private static final String strVerificacionExistencia = "SELECT " +
			"d.codigo_paciente AS codigo_paciente, " +
			"d.ingreso AS ingreso, " +
			"d.institucion AS institucion, " +
			"d.clase_deudorco AS clase_deudorco, " +
			"d.tipo_identificacion AS tip_ident, " +
			"d.numero_identificacion AS num_ident, " +
			"d.primer_apellido AS p_apellido," +
			"coalesce(d.segundo_apellido,' ') AS s_apellido," +
			"d.primer_nombre AS p_nombre," +
			"coalesce(d.segundo_nombre,' ') AS s_nombre," +
			"d.direccion_reside AS direccion," +
			"coalesce(d.telefono_reside,' ') AS tel_fijo " +
			"FROM carterapaciente.deudorco d " +
			"WHERE d.tipo_identificacion = ? AND d.numero_identificacion = ? AND d.clase_deudorco = ?";
	/**
	 * cadena d econsulta para obtener el ingreso
	 */
	private static final String strObtenerIngreso = "SELECT " +
			"c.id_ingreso AS ingreso, " +
			"f.cod_paciente AS codigo_paciente, " +
			"f.codigo AS codigo_factura " +
			"FROM facturacion.facturas f " +
			"INNER JOIN manejopaciente.cuentas c ON (f.cuenta = c.id) " +
			"WHERE f.consecutivo_factura = ?";
	
	/**
	 * verificar existencia de deudor basados en el ingreso de la facturas
	 */
	private static final String strVerificarIngresoDeuodor = "SELECT " +
			"CASE WHEN d.ingreso IS NULL THEN '"+ConstantesBD.acronimoNo+"' ELSE d.ingreso||'' END AS existe_ingreso " +
			"FROM facturacion.facturas f " +
			"INNER JOIN manejopaciente.cuentas c ON (f.cuenta = c.id) " +
			"LEFT OUTER JOIN carterapaciente.deudorco d ON (d.ingreso = c.id_ingreso AND d.clase_deudorco = ?) " +
			"WHERE f.consecutivo_factura = ?";
	
	/**
	 * cadena que obteniene el deudor basados en el  ingreso de la factura
	 */
	private static final String strObtenerDeudorIngFac = "SELECT " +
			"d.codigo_pk AS codigoPkDeudor, " +
			"d.codigo_paciente AS codigo_paciente, " +
			"d.ingreso AS ingreso, " +
			"d.institucion AS intitucion, " +
			"d.clase_deudorco AS clase_deudorco, " +
			"d.tipo_identificacion AS tip_ident, " +
			"d.numero_identificacion AS num_ident, " +
			"d.primer_apellido AS p_apellido," +
			"coalesce(d.segundo_apellido,' ') AS s_apellido," +
			"d.primer_nombre AS p_nombre," +
			"coalesce(d.segundo_nombre,' ') AS s_nombre," +
			"d.direccion_reside AS direccion," +
			"coalesce(d.telefono_reside,' ') AS tel_fijo " +
			"FROM facturacion.facturas f " +
			"INNER JOIN manejopaciente.cuentas c ON (f.cuenta = c.id) " +
			"LEFT OUTER JOIN carterapaciente.deudorco d ON (d.ingreso = c.id_ingreso)"+ //AND d.clase_deudorco = ? ) " +
			"WHERE f.consecutivo_factura = ? ";
	
	/**
	 * cadena de actualizacion los datos persona 
	 * CASO: en el que exista el paciente y se modifique los campos de direccion y telefono
	 */
	private static final String strActualizarPaciente = "UPDATE administracion.personas SET " +
			"direccion = ?, " +
			"telefono = ? " +
			"WHERE codigo = ? ";
	
	/**
	 * cadena que actualiza los datos responsable
	 * CASO: en el que exista el responsable
	 */
	private static final String strActualizacionResponsable = "UPDATE manejopaciente.responsables_pacientes SET " +
			"direccion = ?, " +
			"telefono = ? " +
			"WHERE codigo = ? ";
	
	/**
	 * cadena que actualiza el documento de garantia
	 */
	private static final String strActualizarDocumentoGarantia = "UPDATE carterapaciente.documentos_garantia SET " +
			"cartera = ?, " +
			"valor = ?, " +
			"fecha_generacion = CURRENT_DATE, " +
			"hora_generacion = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
			"fecha_modifica = CURRENT_DATE, " + 
			"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
			"usuario_generacion = ?, " +
			"usuario_modifica = ? " +
			"WHERE ingreso = ? AND tipo_documento = ? AND consecutivo = ? AND anio_consecutivo = ? ";
	
	/**
	 * verificar existencia de documento de garantia segun el ingreso de la factura
	 */
	private static final String strVerificarDocgarantia = "SELECT " +
			"dg.ingreso AS ingreso, " + 
			"dg.consecutivo AS consecutivo,  " +
			"CASE WHEN dg.anio_consecutivo IS NULL THEN ' ' ELSE dg.anio_consecutivo END AS anio_consecutivo, " +
			"dg.tipo_documento AS tipo_documento " +
			"FROM  carterapaciente.documentos_garantia dg " +
			"WHERE dg.ingreso IN (SELECT c.id_ingreso " + 
			"FROM facturacion.facturas f " +
			"INNER JOIN manejopaciente.cuentas c ON (f.cuenta = c.id)  " + 
			"WHERE f.consecutivo_factura = ? ) " +
			"AND dg.garantia_ingreso = '"+ConstantesBD.acronimoSi+"' " +
			"AND dg.cartera = '"+ConstantesBD.acronimoNo+"' "  +
			"AND dg.estado = '"+ConstantesIntegridadDominio.acronimoPolizaVigente+"' " +
			"AND dg.tipo_documento = ? ";
	
	/**
	 * vefificar si existe responsable paciente
	 */
	private static final String strVerificarResponsablePac = "SELECT " + 
			"CASE WHEN c.codigo_responsable_paciente IS NULL THEN 'N' ELSE 'S' END AS existe_resp " +
			"FROM facturacion.facturas f " +
			"INNER JOIN manejopaciente.cuentas c ON (f.cuenta = c.id ) " +
			"WHERE f.consecutivo_factura = ? ";
	
	/**
	 * cadena de actualizacion de docuemntos de garantia, requerida en la anulaci�n de un recibo de caja de concepto paciente
	 */
	private static final String strActualizacionDocGarantiaAnuRC = "UPDATE carterapaciente.documentos_garantia SET " +
			"estado = " +
			"CASE " +
			"	WHEN  garantia_ingreso = '"+ConstantesBD.acronimoNo+"' AND  cartera = '"+ConstantesBD.acronimoSi+"' THEN '"+ConstantesIntegridadDominio.acronimoEstadoAnulado+"' " +
			"	WHEN  garantia_ingreso = '"+ConstantesBD.acronimoSi+"' AND  cartera = '"+ConstantesBD.acronimoNo+"' THEN '"+ConstantesIntegridadDominio.acronimoPolizaVigente+"' " +
			"	ELSE estado END " +
			"WHERE " +
			"ingreso = ? AND tipo_documento= ? AND  consecutivo = ? AND anio_consecutivo = ? ";
	
	private static final String strConsultaCodigoDocuGarantia="SELECT codigo_pk FROM " +
																"documentos_garantia " +
																"WHERE ingreso= ? " +
																"AND consecutivo= ? " +
																"AND anio_consecutivo= ? ";
	
	
	public static int consultaCodigoDocuGarantia(int ingreso, int consecutivo,String anioConsecutivo)	
	{
		Connection con=null;
		con=UtilidadBD.abrirConexion();
		
		try{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con,strConsultaCodigoDocuGarantia);
			pst.setInt(1, ingreso);
			pst.setString(2, consecutivo+"");
			pst.setString(3, anioConsecutivo);
			logger.info("\n\nConsulta: "+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				return rs.getInt("codigo_pk");
			}
			rs.close();
			pst.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strConsultaCodigoDocuGarantia);
		}
		return 0;
	}
	
	/**
	 * cadena de consulta documento de garantia 
	 */
	
	/**
	 * insercion datos financiacion
	 * @param con
	 * @param dto
	 * @return
	 */
	public static int insertDatosFinanciacion(Connection con, DtoDatosFinanciacion dto)
	{
		try
		{
			int codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "carterapaciente.seq_datos_financiacion");
			logger.info("codigo pk: [datos financiacion] "+codigoPK);
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertDatosFinanciacion);
			ps.setInt(1, codigoPK);
			if(dto.getDetallePagoRC()>0)
				ps.setInt(2, dto.getDetallePagoRC());
			else
				ps.setObject(2,null);
			if(dto.getDetalleConceptoRC()>0)
				ps.setInt(3, dto.getDetalleConceptoRC());
			else
				ps.setObject(3,null);
			if(dto.getIngreso()>0)
				ps.setInt(4, dto.getIngreso());
			else
				ps.setObject(4, null);
			ps.setString(5, dto.getConsecutivo());
			ps.setString(6, dto.getAnioConsecutivo());
			ps.setString(7, dto.getTipoDocumento());
			if(dto.getCodigoFactura()>0)
				ps.setInt(8, dto.getCodigoFactura());
			else
				ps.setObject(8, null);
			ps.setString(9, dto.getUsuarioModifica());
			ps.setString(10, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaInicio()));
			ps.setInt(11, dto.getDiasPorCuota());
			if(!dto.getObservaciones().equals(""))
				ps.setString(12, dto.getObservaciones());
			else
				ps.setNull(12, Types.VARCHAR);
			ps.setInt(13, dto.getNroCoutas());
			ps.setInt(14, dto.getCodigoDocumentoGarantia());
			ps.setString(15, dto.getConsecutivoFactura());
			if(dto.getFechaElaboracionFactura().equals(""))
				ps.setNull(16, Types.DATE);
			else
				ps.setString(16, UtilidadFecha.conversionFormatoFechaABD(dto.getFechaElaboracionFactura()));
			
			logger.info("\nconsulta:: "+ps);
			if(ps.executeUpdate()>0)
			{	
				// ingresar el deudor y/o codeudor
				logger.info("entra a la insercion del deudor y/o codeudor en el index");
				dto.getDeudor().setDatosFinanciacion(codigoPK);
				logger.info("Datos: "+dto.getDeudor().getCodigoPK()+"-"+dto.getDeudor().getDatosFinanciacion()+"-"+dto.getDeudor().getIngreso()+"-"+dto.getDeudor().getInstitucion()+"-"+dto.getDeudor().getClaseDeudor()+"-"+dto.getDeudor().getNumeroIdentificacion());
				logger.info("el deudor ya existe: "+dto.getDeudor().getExiteDeudor());
				
				if(!dto.getDeudor().getTipoDeudor().equals("")&&dto.getDeudor().isDatos()){
					if(insertDeudorDatosFinan(con,dto.getDeudor())){
						logger.info("insert deudor");
						dto.getCodeudor().setDatosFinanciacion(codigoPK);
						if(!dto.getCodeudor().getTipoDeudor().equals("")&&dto.getCodeudor().isDatos()){
							if(insertDeudorDatosFinan(con,dto.getCodeudor())){
								logger.info("insert codeudor");
								// insertar cuotas datos financiacion
								if(insertCuotasDatosFinanciacion(con, dto.getCuotasDatosFinan(), codigoPK))
								{	
									logger.info("se inserta correctmaente las cuotas de financiacion");
									ps.close();
									return codigoPK;
								}else{
									//logger.info("error al insertar las cuotas");
									return ConstantesBD.codigoNuncaValido;
								}
								// Fin insertar cuotas datos financiacion
							}else
								return ConstantesBD.codigoNuncaValido;
						}else{
							// insertar cuotas datos financiacion
							if(insertCuotasDatosFinanciacion(con, dto.getCuotasDatosFinan(), codigoPK))
							{	
								logger.info("se inserta correctmaente las cuotas de financiacion 1 codigo: "+codigoPK);
								ps.close();
								return codigoPK;
							}else{
								//logger.info("error al insertar las cuotas 1");
								return ConstantesBD.codigoNuncaValido;
							}
							// Fin insertar cuotas datos financiacion
						}
					}else
						return ConstantesBD.codigoNuncaValido;
				}else
					return ConstantesBD.codigoNuncaValido;
				// fin ingresar el deudor y/o codeudor
				
				
			}
			ps.close();
		}catch (Exception e) {
			logger.error("error: ",e);
		}
		return ConstantesBD.codigoNuncaValido;
	}
	
	/**
	 * Metodo que inserta el deudor y/o codeudor 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean insertDeudorDatosFinan(Connection con, DtoDeudoresDatosFinan dto)
	{
		try{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,strInsertDeudorDatosFinan);
			ps.setInt(1, dto.getDatosFinanciacion());
			ps.setInt(2, dto.getCodigoPK());
			logger.info("Consulta: \n"+ps);
			if(ps.executeUpdate()>0)
			{
				ps.close();
				return true;
			}
			ps.close();
		}catch (Exception e) {
			logger.error("error: ",e);
		}
		return false;
	}
	
	/**
	 * insertar datos financiacion
	 * @param con
	 * @param dto
	 * @param dato_financiacion
	 * @return
	 */
	public static boolean insertCuotasDatosFinanciacion(Connection con, ArrayList<DtoCuotasDatosFinanciacion> dto, int dato_financiacion)
	{
		boolean result = false;
		try{
			PreparedStatementDecorator ps = null;
			DtoCuotasDatosFinanciacion cdf = new DtoCuotasDatosFinanciacion();
			int codigoPK = ConstantesBD.codigoNuncaValido;
			for(int i=0;i<dto.size();i++)
			{
				codigoPK = UtilidadBD.obtenerSiguienteValorSecuencia(con, "carterapaciente.seq_cuotas_datos_fin");
				cdf = (DtoCuotasDatosFinanciacion) dto.get(i);
				ps =  new PreparedStatementDecorator(con,strInsertCuotasDatosFinanciacion);
				ps.setInt(1, codigoPK);
				ps.setInt(2, dato_financiacion);
				ps.setString(3, cdf.getNumeroDocumento());
				ps.setDouble(4, Double.valueOf(cdf.getValorCuota().toString()));
				ps.setString(5, cdf.getUsuarioModifica());
				ps.setString(6, cdf.getActivo());
				ps.setInt(7, i);
				logger.info("Consulta: "+ps);
				if(ps.executeUpdate()>0)
					result=true;
				else{
					ps.close();
					return false;
				}
			}
			ps.close();
		}catch (Exception e) {
			logger.error("error: ",e);
		}
		return result;
	}
	
	/**
	 * metodo que carga el deudor y/o codeudor segun el documento de garantia asociado
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoDeudoresDatosFinan obtenerDatosDeudorCO(Connection con, HashMap parametros)
	{
		DtoDeudoresDatosFinan dto = new DtoDeudoresDatosFinan();
		try{			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con,strConsultarDeudoresCO);
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("ingreso")+""));
			pst.setInt(2, Utilidades.convertirAEntero(parametros.get("institucion")+""));
			pst.setString(3, parametros.get("clase_deudor")+"");
			logger.info("\n\nConsulta datos deudor: "+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				dto.setCodigoPK(rs.getInt("codigoPk"));
				dto.setCodigoPkDeudor(rs.getInt("codigoPk"));
				dto.setTipoDeudor(rs.getString("tipo_deudorco"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setClaseDeudor(rs.getString("clase_deudorco"));
				dto.setCodigoPaciente(rs.getString("codigo_paciente"));
				dto.setTipoIdentificacion(rs.getString("tip_ident"));
				dto.setNumeroIdentificacion(rs.getString("num_ident"));
				dto.setPrimerApellido(rs.getString("p_apellido"));
				dto.setSegundoApellido(rs.getString("s_apellido"));
				dto.setPrimerNombre(rs.getString("p_nombre"));
				dto.setSegundoNombre(rs.getString("s_nombre"));
				dto.setDireccion(rs.getString("direccion"));
				dto.setTelefono(rs.getString("tel_fijo"));
				dto.setOcupacion(rs.getString("tipo_ocupacion"));
				dto.setDetalleocupacion(rs.getString("ocupacion"));
				dto.setEmpresa(rs.getString("empresa"));
				dto.setCargo(rs.getString("cargo"));
				dto.setAntiguedad(rs.getString("antiguedad"));
				dto.setDireccionOficina(rs.getString("dir_oficina"));
				dto.setTelefonoOficina(rs.getString("tel_oficina"));
				dto.setExiteDeudor(ConstantesBD.acronimoSi);
				dto.setExistePerRes(ConstantesBD.acronimoNo);
			}
			rs.close();
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error Consulta: "+strConsultarDeudoresCO+" ingreso:"+parametros.get("ingreso")+" institucion:"+parametros.get("institucion")+" clase deudor: "+parametros.get("clase_deudor"));
		}
		return dto;
	}
	
	
	/**
	 * Obtener datos deudor y/o Codeudor
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoDeudoresDatosFinan obtenerDatosDeudor(Connection con, HashMap parametros)
	{
		DtoDeudoresDatosFinan dto = new DtoDeudoresDatosFinan();
		try{
			logger.info("entra en la busqueda de deudor por paciente");
			if(verificarIngresoDeudor(con, parametros).equals(ConstantesBD.acronimoNo))
			{
				logger.info("Consulta: "+strConsultarDatosPersona+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultarDatosPersona,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, Utilidades.convertirAEntero(parametros.get("consecutivo_factura")+""));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					dto.setCodigoPaciente(rs.getString("codigo_paciente"));
					dto.setTipoIdentificacion(rs.getString("tip_ident"));
					dto.setNumeroIdentificacion(rs.getString("num_ident"));
					dto.setPrimerApellido(rs.getString("p_apellido"));
					dto.setSegundoApellido(rs.getString("s_apellido"));
					dto.setPrimerNombre(rs.getString("p_nombre"));
					dto.setSegundoNombre(rs.getString("s_nombre"));
					dto.setDireccion(rs.getString("direccion"));
					dto.setTelefono(rs.getString("tel_fijo"));
					dto.setExiteDeudor(ConstantesBD.acronimoNo);
					dto.setExistePerRes(ConstantesBD.acronimoSi);
				}
				rs.close();
				pst.close();
			}else{
				logger.info("entra a esta parte por ya existe el deudor ");
				dto = obtenerDeudorIngFac(con, parametros);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strConsultarDatosPersona+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
		}
		return dto;
	}
	
	/**
	 * Consulta los datos del responsable del paciente si existe
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoDeudoresDatosFinan obtenerDatosResponsable(Connection con, HashMap parametros)
	{
		DtoDeudoresDatosFinan dto = new DtoDeudoresDatosFinan();
		try{
			if(verificarIngresoDeudor(con, parametros).equals(ConstantesBD.acronimoNo))
			{
				logger.info("Consulta: "+strConsultaDatosResponsable+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
				PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strConsultaDatosResponsable,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				pst.setInt(1, Utilidades.convertirAEntero(parametros.get("consecutivo_factura")+""));
				ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
				if(rs.next())
				{
					dto.setCodigoPaciente(rs.getString("codigo_paciente"));
					dto.setTipoIdentificacion(rs.getString("tip_ident"));
					dto.setNumeroIdentificacion(rs.getString("num_ident"));
					dto.setPrimerApellido(rs.getString("p_apellido"));
					dto.setSegundoApellido(rs.getString("s_apellido"));
					dto.setPrimerNombre(rs.getString("p_nombre"));
					dto.setSegundoNombre(rs.getString("s_nombre"));
					dto.setDireccion(rs.getString("direccion"));
					dto.setTelefono(rs.getString("tel_fijo"));
					dto.setExiteDeudor(ConstantesBD.acronimoNo);
					dto.setExistePerRes(ConstantesBD.acronimoSi);
				}else
					dto=null;
				rs.close();
				pst.close();
			}else{
				dto = obtenerDeudorIngFac(con, parametros);
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strConsultaDatosResponsable+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
		}
		return dto;
	}

	/**
	 * verificar Existencia persona
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoDeudoresDatosFinan verificarExistenciaPer(Connection con, HashMap parametros)
	{
		DtoDeudoresDatosFinan dto = new DtoDeudoresDatosFinan();
		try{
			logger.info("Consulta: "+strVerificacionExistencia+" tipo_identificacion:"+parametros.get("tipo_identificacion")+" numero_identificacion:"+parametros.get("numero_identificacion"));
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strVerificacionExistencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, parametros.get("tipo_identificacion")+"");
			pst.setString(2, parametros.get("numero_identificacion")+"");
			pst.setString(3, parametros.get("clase_deudor")+"");
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				dto.setCodigoPaciente(rs.getString("codigo_paciente"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setInstitucion(rs.getInt("institucion"));
				dto.setClaseDeudor(rs.getString("clase_deudorco"));
				dto.setTipoIdentificacion(rs.getString("tip_ident"));
				dto.setNumeroIdentificacion(rs.getString("num_ident"));
				dto.setPrimerApellido(rs.getString("p_apellido"));
				dto.setSegundoApellido(rs.getString("s_apellido"));
				dto.setPrimerNombre(rs.getString("p_nombre"));
				dto.setSegundoNombre(rs.getString("s_nombre"));
				dto.setDireccion(rs.getString("direccion"));
				dto.setTelefono(rs.getString("tel_fijo"));
				dto.setExiteDeudor(ConstantesBD.acronimoSi);
				dto.setExistePerRes(ConstantesBD.acronimoNo);
			}else
				dto=null;
			rs.close();
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strVerificacionExistencia+" tipo_identificacion:"+parametros.get("tipo_identificacion")+" numero_identificacion:"+parametros.get("numero_identificacion"));
		}
		logger.info("valod del dto: "+dto);
		return dto;
	}
	
	/**
	 * obtener ingreso de facturas
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap obtenerIngresoFactura(Connection con, HashMap parametros)
	{
		HashMap mapa = new HashMap();
		try{
			logger.info("Consulta: "+strObtenerIngreso+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strObtenerIngreso,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("consecutivo_factura")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				mapa.put("ingreso", rs.getInt("ingreso"));
				mapa.put("codigo_paciente", rs.getInt("codigo_paciente"));
				mapa.put("codigo_factura", rs.getInt("codigo_factura"));
			}
			rs.close();
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strObtenerIngreso+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
		}
		return mapa;
	}

	/**
	 * verificar ingreso deudor
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static String verificarIngresoDeudor(Connection con, HashMap parametros)
	{
		String ingreso = ConstantesBD.acronimoNo;
		try{
			logger.info("Consulta: "+strVerificarIngresoDeuodor+" clase deudor: "+parametros.get("clase_deudor")+" codigo_consecutivo:"+parametros.get("consecutivo_factura"));
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strVerificarIngresoDeuodor,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, parametros.get("clase_deudor")+"");
			pst.setInt(2, Utilidades.convertirAEntero(parametros.get("consecutivo_factura")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				ingreso = rs.getString("existe_ingreso");
			logger.info("valor del ingreso: "+ingreso);
			rs.close();
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strVerificarIngresoDeuodor+" codigo_factura:"+parametros.get("tipo_identificacion"));
		}
		return ingreso;
	}
	
	/**
	 * obtener deudores ingresos facturas
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static DtoDeudoresDatosFinan obtenerDeudorIngFac(Connection con, HashMap parametros)
	{
		DtoDeudoresDatosFinan dto = new DtoDeudoresDatosFinan();
		try{
			logger.info("Consulta: "+strObtenerDeudorIngFac+" clase deudor: "+parametros.get("clase_deudor")+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strObtenerDeudorIngFac,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			//pst.setString(1, parametros.get("clase_deudor")+"");
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("consecutivo_factura")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				dto.setCodigoPkDeudor(rs.getInt("codigoPkDeudor"));
				dto.setCodigoPaciente(rs.getString("codigo_paciente"));
				dto.setIngreso(rs.getInt("ingreso"));
				dto.setInstitucion(rs.getInt("intitucion"));
				dto.setClaseDeudor(rs.getString("clase_deudorco"));
				dto.setTipoIdentificacion(rs.getString("tip_ident"));
				dto.setNumeroIdentificacion(rs.getString("num_ident"));
				dto.setPrimerApellido(rs.getString("p_apellido"));
				dto.setSegundoApellido(rs.getString("s_apellido"));
				dto.setPrimerNombre(rs.getString("p_nombre"));
				dto.setSegundoNombre(rs.getString("s_nombre"));
				dto.setDireccion(rs.getString("direccion"));
				dto.setTelefono(rs.getString("tel_fijo"));
				dto.setExiteDeudor(ConstantesBD.acronimoSi);
				dto.setExistePerRes(ConstantesBD.acronimoNo);
			}else
				dto=null;
			rs.close();
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strObtenerDeudorIngFac+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
		}
		logger.info("existed deudor y/o codeudor: "+dto.getExiteDeudor());
		return dto;
	}
	
	/**
	 * Actualizar deudor
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarDeudor(Connection con, DtoDeudoresDatosFinan dto)
	{
		String strActualizarDeudor = "UPDATE " +
										"carterapaciente.deudorco SET ";
		
		if(!dto.getEmpresa().equals(""))
			strActualizarDeudor += 		"empresa='"+dto.getEmpresa()+"', ";
		
		if(!dto.getCargo().equals(""))
			strActualizarDeudor += 		"cargo='"+dto.getCargo()+"', ";
		
		if(!dto.getAntiguedad().equals(""))
			strActualizarDeudor += 		"antiguedad='"+dto.getAntiguedad()+"', ";
		
		if(!dto.getDireccionOficina().equals(""))
			strActualizarDeudor += 		"direccion_oficina='"+dto.getDireccionOficina()+"', ";
		
		if(!dto.getTelefonoOficina().equals(""))
			strActualizarDeudor += 		"telefono_oficina='"+dto.getTelefonoOficina()+"', ";
		
		if(!dto.getNombresReferenciaFamiliar().equals(""))
			strActualizarDeudor += 		"nombres_referencia='"+dto.getNombresReferenciaFamiliar()+"', ";
		
		if(!dto.getDireccionReferenciaFamiliar().equals(""))
			strActualizarDeudor += 		"direccion_referencia='"+dto.getDireccionReferenciaFamiliar()+"', ";
		
		if(!dto.getTelefonoReferenciaFamiliar().equals(""))
			strActualizarDeudor += 		"telefono_referencia='"+dto.getTelefonoReferenciaFamiliar()+"', ";
		
		strActualizarDeudor += 			"direccion_reside = '"+dto.getDireccion()+"', " +
										"telefono_reside = '"+dto.getTelefono()+"' ";
		
		if(dto.getCodigoPK()!=ConstantesBD.codigoNuncaValido)
			strActualizarDeudor +=	"WHERE codigo_pk = "+dto.getCodigoPK();
		else {
			strActualizarDeudor +=	"WHERE ingreso = "+dto.getIngreso()+" AND institucion = "+dto.getInstitucion()+" AND clase_deudorco ='"+dto.getClaseDeudor()+"' AND numero_identificacion = '"+dto.getNumeroIdentificacion()+"' ";
		}
		
		try{
			logger.info("Consulta: "+strActualizarDeudor+" datos:"+dto);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strActualizarDeudor,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

			if(pst.executeUpdate()>0){
				pst.close();
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strActualizarDeudor+" datos:"+dto);
		}
		return false;
	}
	
	/**
	 * actualizar datos Paciente
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarPaciente(Connection con, DtoDeudoresDatosFinan dto)
	{
		try{
			logger.info("Consulta: "+strActualizarPaciente+" datos:"+dto);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strActualizarPaciente,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, dto.getDireccion());
			pst.setString(2, dto.getTelefono());
			pst.setInt(3, Utilidades.convertirAEntero(dto.getCodigoPaciente()));
			
			if(pst.executeUpdate()>0){
				pst.close();
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strActualizarPaciente+" datos:"+dto);
		}
		return false;
	}
	
	/**
	 * actualizar responsable paciente
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarResponsablePac(Connection con, DtoDeudoresDatosFinan dto)
	{
		try{
			logger.info("Consulta: "+strActualizacionResponsable+" datos:"+dto);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strActualizacionResponsable,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1, dto.getDireccion());
			pst.setString(2, dto.getTelefono());
			pst.setInt(3, Utilidades.convertirAEntero(dto.getCodigoPaciente()));
			
			if(pst.executeUpdate()>0){
				pst.close();
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strActualizacionResponsable+" datos:"+dto);
		}
		return false;
	}
	
	/**
	 * actualizacion de documento de garantia segun requerimiento de anulacion de recibo de caja conconcepto paciente
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean actualizarDocGarantiaAnuRC(Connection con, HashMap parametros)
	{
		try{
			Utilidades.imprimirMapa(parametros);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con,strActualizacionDocGarantiaAnuRC);
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("ingreso")+""));
			pst.setString(2, parametros.get("tipo_documento")+"");
			pst.setString(3, parametros.get("consecutivo")+"");
			pst.setString(4, parametros.get("anio_consecutivo")+"");
			logger.info("\n\nConsulta: "+pst);
			
			if(pst.executeUpdate()>0){
				pst.close();
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strActualizacionDocGarantiaAnuRC);
			Utilidades.imprimirMapa(parametros);
		}
		return false;
	}
	
	/**
	 * actualizar los datos del documento de garantia
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static boolean actualizarDocGarAsociado(Connection con, HashMap parametros)
	{
		try{
			
			Utilidades.imprimirMapa(parametros);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con,strActualizarDocumentoGarantia);
			pst.setString(1, parametros.get("cartera")+"");
			pst.setDouble(2, Utilidades.convertirADouble(parametros.get("valor")+""));
			pst.setString(3, parametros.get("usuario_genera")+"");
			pst.setString(4, parametros.get("usuario_modifica")+"");
			pst.setInt(5, Utilidades.convertirAEntero(parametros.get("ingreso")+""));
			pst.setString(6, parametros.get("tipo_documento")+"");
			pst.setString(7, parametros.get("consecutivo")+"");
			pst.setString(8, parametros.get("anio_consecutivo")+"");
			logger.info("\n\nActualiza doc garantia: "+pst);
						
			if(pst.executeUpdate()>0){
				pst.close();
				return true;
			}
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Error Consulta: "+strActualizarDocumentoGarantia);
			Utilidades.imprimirMapa(parametros);
		}
		return false;
	}
	
	/**
	 * verificar la existencia de un documento de garantia asociado al ingreso de la factura
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap verificarDocumentoGarantia(Connection con, HashMap parametros)
	{
		HashMap docgaran = new HashMap();
		try{
			logger.info("Consulta: "+strVerificarDocgarantia+" consecutivo_factura:"+parametros.get("consecutivo_factura")+" tipo documento: "+parametros.get("tipo_documento"));
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strVerificarDocgarantia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("consecutivo_factura")+""));
			pst.setString(2, parametros.get("tipo_documento")+"");
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
			{
				docgaran.put("consecutivo", rs.getString("consecutivo"));
				docgaran.put("anio_consecutivo", rs.getString2("anio_consecutivo"));
				docgaran.put("tipo_documento", rs.getString("tipo_documento"));
				docgaran.put("ingreso", rs.getInt("ingreso"));
			}
			rs.close();
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strVerificarDocgarantia+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
		}
		return docgaran;
	}
	
	/**
	 * busquesa deudores
	 * @param con
	 * @param dtoe
	 * @return
	 */
	public static ArrayList<DtoDeudor> busquedaDeudores(Connection con, DtoDeudor dtoe)
	{
		ArrayList<DtoDeudor> listDto = new ArrayList<DtoDeudor>();
		String consulta = "SELECT DISTINCT " +
				"deu.ingreso AS ingreso_deu, " +
				"deu.institucion AS institucion_deu, " +
				"deu.clase_deudorco AS clase_deu, " +
				"deu.tipo_identificacion AS tipo_id_deu, " +
				"deu.numero_identificacion AS num_id_deu, " +
				"deu.primer_apellido AS p_apellido_deu, " +
				"deu.segundo_apellido AS s_apellido_deu, " +
				"deu.primer_nombre AS p_nombre_deu, " +
				"deu.segundo_nombre AS s_nombre_deu ";
		String tabla = "";
		String where = "WHERE 1=1 ";
		String order = "";
		try{
			if(dtoe.isDatosPaciente()){
				consulta+=", per.codigo AS codigo_paciente, " +
						"administracion.getnombrepersona(per.codigo) as nombre_pac,  " +
						"per.tipo_identificacion AS tipo_id_pac,  " +
						"per.numero_identificacion AS num_id_pac ";
				tabla="FROM carterapaciente.documentos_garantia dg " +
					"INNER JOIN administracion.personas per ON (per.codigo = dg.codigo_paciente ) " +
					"INNER JOIN carterapaciente.deudorco deu ON (deu.codigo_paciente = dg.codigo_paciente AND deu.institucion = "+dtoe.getCodInstitucion()+") " ;
				if(!dtoe.getTipoIdentificacionPac().equals("") && !dtoe.getNumeroIdentificacionPac().equals(""))
					where += "AND per.tipo_identificacion = '"+dtoe.getTipoIdentificacionPac()+"' AND per.numero_identificacion = '"+dtoe.getNumeroIdentificacionPac()+"' ";
				if(!dtoe.getPrimerApellidoPac().equals(""))
					where += "AND per.primer_nombre like '%"+dtoe.getPrimerApellidoPac()+"%' ";
				if(!dtoe.getPrimerNombrePac().equals(""))
					where += "AND per.primer_apellido  like '%"+dtoe.getPrimerNombrePac()+"%' ";
				order = " ORDER BY nombre_pac, deu.primer_apellido ASC ";
			}else
				tabla = "FROM carterapaciente.deudorco deu ";
			
			if(dtoe.isDatosDeudor())
			{
				if(!dtoe.getTipoIdentificacionDeu().equals("") && !dtoe.getNumeroIdentificacionDeu().equals(""))
					where +=" AND deu.tipo_identificacion = '"+dtoe.getTipoIdentificacionDeu()+"' AND deu.numero_identificacion = '"+dtoe.getNumeroIdentificacionDeu()+"' ";
				if(!dtoe.getPrimerApellidoDeu().equals(""))
					where +=" AND deu.primer_apellido like '%"+dtoe.getPrimerApellidoDeu()+"%' ";
				if(!dtoe.getPrimerNombreDeu().equals(""))
					where +=" AND deu.primer_nombre like '%"+dtoe.getPrimerNombreDeu()+"%' ";
				where += "AND deu.institucion = "+dtoe.getCodInstitucion()+" AND deu.clase_deudorco = '"+ConstantesIntegridadDominio.acronimoDeudor+"' ";
				if(!dtoe.isDatosPaciente())
					order = " ORDER BY deu.primer_apellido ASC ";
			}
				
			consulta+=tabla+where+order;
			logger.info("Consulta: "+consulta);
			
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoDeudor dto = new DtoDeudor();
				dto.setIngreso(rs.getInt("ingreso_deu"));
				dto.setCodInstitucion(rs.getInt("institucion_deu"));
				dto.setClaseDeudor(rs.getString("clase_deu"));
				dto.setTipoIdentificacionDeu(rs.getString("tipo_id_deu"));
				dto.setNumeroIdentificacionDeu(rs.getString("num_id_deu"));
				dto.setPrimerApellidoDeu(rs.getString("p_apellido_deu"));
				dto.setSegundoApellidoDeu(rs.getString("s_apellido_deu"));
				dto.setPrimerNombreDeu(rs.getString("p_nombre_deu"));
				dto.setSegundoNombreDeu(rs.getString("s_nombre_deu"));
				if(dtoe.isDatosPaciente())
				{
					dto.setCodigoPaciente(rs.getString("codigo_paciente"));
					dto.setNombreCompletoPac(rs.getString("nombre_pac"));
					dto.setTipoIdentificacionPac(rs.getString("tipo_id_pac"));
					dto.setNumeroIdentificacionPac(rs.getString("num_id_pac"));
					dto.setRompimiento(ConstantesBD.acronimoSi);
				}
				listDto.add(dto);
			}
			rs.close();
			pst.close();
			
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+consulta);
		}
		return listDto;
	}

	/**
	 * verifica la existencia de responsable de paciente, segun el ingreso de la factura
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static String verificarResponsablePacIngFac(Connection con, HashMap parametros)
	{
		String result = ConstantesBD.acronimoNo;
		try{
			logger.info("Consulta: "+strVerificarResponsablePac+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(strVerificarResponsablePac,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1, Utilidades.convertirAEntero(parametros.get("consecutivo_factura")+""));
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			if(rs.next())
				result = rs.getString("existe_resp");
			rs.close();
			pst.close();
		}catch (Exception e) {
			e.printStackTrace();
			logger.info("Consulta: "+strVerificarResponsablePac+" consecutivo_factura:"+parametros.get("consecutivo_factura"));
		}
		return result;
	}

	public static String obtenerPrimerConsecutivoUsadoDocumento(String tipoDocGarantia,int institucion) 
	{
		String resultado="";
		Connection con=UtilidadBD.abrirConexion();
		try 
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, "select coalesce(min(convertiranumero(consecutivo)),-1) from documentos_garantia where institucion=? and tipo_documento=? and saldos_iniciales='"+ConstantesBD.acronimoNo+"'");//los consecutivos disponibles se usan cuando no es registro de saldos iniciales.
			ps.setInt(1,institucion);
			ps.setString(2, tipoDocGarantia);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				resultado=rs.getInt(1)+"";
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("error consultando",e);
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	
	/**
	 * 
	 * @param tipoDocGarantia
	 * @param consecutivoDocumento
	 * @param institucion
	 * @return
	 */
	public static boolean esConsecutivoDocumentoUsado(String tipoDocGarantia,String consecutivoDocumento, int institucion) 
	{
		boolean resultado=false;
		Connection con=UtilidadBD.abrirConexion();
		try 
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, "select count(1) from documentos_garantia where institucion=? and tipo_documento=? and consecutivo=?");
			ps.setInt(1,institucion);
			ps.setString(2, tipoDocGarantia);
			ps.setString(3, consecutivoDocumento);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				resultado=rs.getInt(1)>0;
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("error consultando",e);
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}

	public static ArrayList<DtoFactura> consultarFacturas(String facturaBusqueda, String tipoIdentificacionDeudor,String numeroIdentificacionDeudor,String tipoIdentificacionCodeudor,String numeroIdentificacionCodeudor) 
	{
		ArrayList<DtoFactura> resultado=new ArrayList<DtoFactura>();
		Connection con=UtilidadBD.abrirConexion();
		try 
		{
			String consulta="SELECT " +
										" fac.via_ingreso as codigoviaingreso," +
										" getnombreviaingreso(fac.via_ingreso) as nomviaingreso," +
										" fac.codigo as codigofactura," +
										" fac.consecutivo_factura as consecutivofactura," +
										" to_char(fac.fecha,'yyyy-mm-dd') as fechafactura," +
										" fac.cod_paciente as codigopaciente, " +
										" getnombrepersona(fac.cod_paciente) as nombrespaciente," +
										" fac.valor_bruto_pac as valorpaciente," +
										" fac.valor_convenio as valorconvenio," +
										" fac.centro_aten as codigocentroatencion," +
										" getnomcentroatencion(fac.centro_aten) as nombrecentroatencion " +
							" from facturas fac";
			String complementoConsulta="";

			if(UtilidadTexto.isEmpty(facturaBusqueda))
			{
				complementoConsulta=complementoConsulta+"   inner join personas per on(fac.cod_paciente=per.codigo) where (per.tipo_identificacion='"+tipoIdentificacionDeudor+"' and per.numero_identificacion='"+numeroIdentificacionDeudor+"') or (per.tipo_identificacion='"+tipoIdentificacionCodeudor+"' and per.numero_identificacion='"+numeroIdentificacionCodeudor+"')";
			}
			else
			{
				complementoConsulta=complementoConsulta+"  where fac.consecutivo_factura="+facturaBusqueda;
			}
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con, consulta+complementoConsulta);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoFactura fac=new DtoFactura();
				fac.getViaIngreso().setCodigo(rs.getInt("codigoviaingreso"));
				fac.getViaIngreso().setNombre(rs.getString("nomviaingreso"));
				fac.setCodigo(rs.getInt("codigofactura"));
				String tempo=rs.getDouble("consecutivofactura")+"";
				if(tempo.indexOf(".")>0)
					tempo=tempo.substring(0,tempo.indexOf("."));
				fac.setConsecutivoFacturaStr(tempo);
				fac.setFecha(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechafactura")));
				fac.setCodigoPaciente(rs.getInt("codigopaciente"));
				fac.setNombrePaciente(rs.getString("nombrespaciente"));
				fac.setValorBrutoPac(rs.getDouble("valorpaciente"));
				fac.setValorConvenio(rs.getDouble("valorconvenio"));
				fac.getCentroAtencion().setCodigo(rs.getInt("codigocentroatencion"));
				fac.getCentroAtencion().setNombre(rs.getString("nombrecentroatencion"));
				resultado.add(fac);
			}
			rs.close();
			ps.close();
		} 
		catch (SQLException e) 
		{
			logger.error("error consultando",e);
		}
		UtilidadBD.closeConnection(con);
		return resultado;
	}
}
