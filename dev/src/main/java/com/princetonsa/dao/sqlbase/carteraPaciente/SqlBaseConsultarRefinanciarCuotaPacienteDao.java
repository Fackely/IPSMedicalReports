package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDetApliPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDeudoresDatosFinan;
import com.princetonsa.dto.carteraPaciente.DtoHistoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoIngresosFacturasAtenMedica;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseConsultarRefinanciarCuotaPacienteDao {

 private static	Logger logger = Logger.getLogger(SqlBaseConsultarRefinanciarCuotaPacienteDao.class);
	
 
 /**
  * Cadena Sql que realiza la consulta de Ingresos asociados a Facturas de Atencion Medica de un Paciente
  */
  private static String consultaIngresosFactAtencionMedica="SELECT DISTINCT " +
				   "ing.id AS idingreso,  " +
	               "ing.consecutivo AS numingreso, " +
	               "ing.centro_atencion AS codcentroatencion, " +
	               "getnombrecentatenxing(ing.id) AS nomcentroatencion, " +
	               "ing.codigo_paciente AS codigopaciente," +
	               "getNombrePersona(ing.codigo_paciente) AS nombrepaciente, " +
	               "fac.consecutivo_factura AS consecutivofactura, " +
	               "to_char(fac.fecha, 'YYYY-MM-DD') AS fechafactura, " +
	               "docg.tipo_documento AS tipodocumento, " +
	               "docg.consecutivo AS codgarantia," +
	               "docg.valor AS valor, " +
	               "docg.estado AS estado, " +
	               "docg.cartera AS cartera," +
	               "df.codigo_pk AS coddatosfinanciacion,  " +
	               "df.consecutivo AS numpagare, " +
	               "df.anio_consecutivo AS anioconspagare, " +
	               "df.nro_coutas AS nrocuotas, " +
	               "df.dias_por_couta AS diasporcuota, " +
	               "to_char(df.fecha_inicio, 'YYYY-MM-DD') AS fechainicio, " +
	               "(SELECT SUM(aplpagos.valor) FROM carterapaciente.aplicac_pagos_cartera_pac aplpagos WHERE aplpagos.datos_financiacion = df.codigo_pk) AS valorestar " +
	               "FROM ingresos ing " +
	               "INNER JOIN carterapaciente.datos_financiacion df ON (df.ingreso = ing.id ) " +
	               "INNER JOIN carterapaciente.documentos_garantia docg ON (docg.codigo_pk=df.codigo_pk_docgarantia ) " +
	               "INNER JOIN facturacion.facturas fac ON (df.codigo_factura = fac.codigo ) " +
	               "WHERE 1=1 AND ing.codigo_paciente = ? AND  docg.cartera = '"+ConstantesBD.acronimoSi+"' ORDER BY ing.consecutivo "; 
	
	
  /**
   * Cadena Sql para consultar el detalle de la Aplicacion de Pagos
   */
	private static String consultaDetalleAplicacionPagos="SELECT " +
					"aplipagos.consecutivo AS numaplicacion, " +
				    "aplipagos.fecha_aplicacion AS fechaplicacion, " +
				    "aplipagos.valor AS valoraplicacion, " +
				    "aplipagos.usuario AS usuarioaplicacion " +		
				    "FROM carterapaciente.aplicac_pagos_cartera_pac aplipagos " +
				    "WHERE aplipagos.datos_financiacion = ? ";
	
	
	/**
	 * Cadena Sql para consultar los datos de Deudor
	 */
	private static String consultarDatosDeudor =" SELECT " +
                        "datdeu.datos_financiacion AS datosfin, " +
                        "datdeu.clase_deudor AS clasedeudor, " +
                        "datdeu.num_id_deudor AS iddeudor," +
                        "deud.codigo_paciente AS codpaciente,  " +
                        "deud.clase_deudorco AS claseduedor, " +
                        "deud.numero_identificacion AS numidentificacion, " +
                        "deud.tipo_identificacion AS tipoidentificacion, " +
                        "administracion.getnombretipoidentificacion(deud.tipo_identificacion) AS nomtipoidentificacion, " +
                        "deud.primer_nombre AS primernombre, " +
                        "deud.segundo_nombre AS segundonombre, " +
                        "deud.primer_apellido AS primerapellido, " +
                        "deud.segundo_apellido AS segundoapellido " +
                        "FROM carterapaciente.deudores_datos_finan datdeu " +
                        "INNER JOIN carterapaciente.deudorco deud  ON (datdeu.codigo_pk_deudor=deud.codigo_pk) " +
                        "WHERE datdeu.datos_financiacion = ?  AND datdeu.clase_deudor = '"+ConstantesBD.separadorSplit+"' ";

	
	
	
	
	/**
	 * Cadena Sql para actualizar la tabla datos financiacion
	 */
	private static String updateDatosFinanciacionStr ="UPDATE carterapaciente.datos_financiacion SET " +
						  "fecha_inicio = ?, " +
						  "dias_por_couta = ?, " +
						  "observaciones = ?, " +
						  "nro_coutas = ? " +
						  "WHERE  codigo_pk = ? " ;
	
	
	
	private static String consultarHistoricoRefinanciacion="SELECT " +
						  "consecutivo AS consecutivo, " +
						  "datos_financiacion AS coddatosfinanciacion, " +
						  "nro_cuotas_anteriores AS cuotasanteriores, " +
						  "nro_cuotas_asignadas AS cuotasasignadas, " +
						  "dias_por_cuota AS diasporcuota, " +
						  "valor_refinanciar AS valorefinanciar, " +
						  "valor_cuota AS valorcuota, " +
						  "observaciones AS observaciones, " +
						  "fecha_inicio AS fechainicio, " +
						  "fecha_modificacion AS fechamodificacion, " +
						  "hora_modificacion AS horamodificacion, " +
						  "usuario_modificacion AS usuario " +
						  "FROM carterapaciente.his_datos_financiacion " +
						  "WHERE datos_financiacion = ? ";
	
	
	/**
	 * Cadena Sql para realizar la insercion del Historico de los datos Finananciacion
	 */
	private static String insertHistoricoDatosFinanciacionStr= "INSERT INTO carterapaciente.his_datos_financiacion ( " +
			              "codigo_pk, " +      //1
			              "consecutivo, " + // 2
			              "nro_cuotas_anteriores, " +//3 
			              "nro_cuotas_asignadas, " +//4
			              "dias_por_cuota, " + //5
			              "valor_refinanciar, " +//6
			              "valor_cuota, " + //7
			              "fecha_inicio, " + //8
			              "observaciones, " +//9
			              "fecha_modificacion, " +//--
			              "hora_modificacion, " +//--
			              "usuario_modificacion, " +//10
			              "datos_financiacion " +//11
			              ") " +
			              "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?) ";
	
	
	
	/**
	 * Cadena Sql para realizar el insert de la informacion de las cuotas de Refinanciacion
	 */
	private static String insertCuotasDatosFinanciacion= "INSERT INTO carterapaciente.cuotas_datos_financiacion  ( " +
			              "codigo_pk, " +    //1
			              "dato_financiacion, " +//2
			              "numero_documento, " +//3
			              "valor_couta, " + //4
			              "fecha_modifica, " +//--
			              "hora_modifica, " + //--
			              "usuario_modifica, " + //5
			              "activo," +
			              "nro_cuota ) " + // -- 
			              "VALUES (?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, '"+ConstantesBD.acronimoSi+"', ? ) ";
	
	
	/**
	 * Cadena Sql que pone en estado inactivo las cuotas de Datos Financiacion Asociados a una Financiacion
	 */
	private static String inactivarCuotasDatosFinanciacion= " UPDATE carterapaciente.cuotas_datos_financiacion SET " +
			              "activo = '"+ConstantesBD.acronimoNo+"' "+
			              "WHERE dato_financiacion = ? ";
	
	
	/**
	 * Metodo para Realizar la consulta de los Ingresos Asociados a Facturas de Atención Médica
	 * @param codigoPaciente
	 * @return
	 */
	public static ArrayList<DtoIngresosFacturasAtenMedica> listarIngresosFacAtenMedica(int codigoPaciente)
	{
		ArrayList<DtoIngresosFacturasAtenMedica> lista = new ArrayList<DtoIngresosFacturasAtenMedica>();
		String cadenaConsulta = consultaIngresosFactAtencionMedica;
		String cadenaConsultaDetalle= consultaDetalleAplicacionPagos;
		int cantCuotas=0;
		int codigoPersona = codigoPaciente;
		double saldo=0;
		cadenaConsulta= cadenaConsulta.replace(ConstantesBD.separadorSplitComplejo, " AND docg.estado <> '"+ConstantesIntegridadDominio.acronimoEstadoPendiente+"' AND docg.tipo_documento IN ('"+ConstantesIntegridadDominio.acronimoTipoDocumentoLetra+"', '"+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+"') ");
		
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codigoPersona);

			logger.info("\n CADENA CONSULTA INGRESOS >>  cadena >> "+cadenaConsulta+"  CodigoPaciente >> "+codigoPersona);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoIngresosFacturasAtenMedica dto= new DtoIngresosFacturasAtenMedica();
				dto.setCodigoIngreso(rs.getInt("idingreso")+"");
				dto.setConsecutivoIngreso(rs.getString("numingreso"));
				dto.setCodDatosFinanciacion(rs.getInt("coddatosfinanciacion")+"");
				dto.setNomPaciente(rs.getString("nombrepaciente"));
				dto.setCodCentroAtencion(rs.getInt("codcentroatencion")+"");
				dto.setNomCentroAtencion(rs.getString("nomcentroatencion"));
				dto.setNumFactura(rs.getString("consecutivofactura"));
				dto.setFechaFactura(rs.getString("fechafactura"));
				dto.setTipoDocumentoCartera(rs.getString("tipodocumento"));
				dto.setEstadoDocCartera(rs.getString("estado"));
				dto.setCodGarantia(rs.getString("codgarantia"));
				dto.setNumPagare(rs.getString("numpagare"));
				dto.setNumCuotasFinanciacion(rs.getInt("nrocuotas"));
				dto.setFechaInicioFinanciacion(rs.getString("fechainicio"));
				dto.setDiaporcuotaFinanciacion(rs.getInt("diasporcuota"));
				dto.setAnioConsecPagare(rs.getString("anioconspagare"));
				dto.setValorDocGarantia(rs.getDouble("valor")+"");
				
				saldo = rs.getDouble("valor") - rs.getDouble("valorestar");			
				
				dto.setSaldo(saldo+""); 
				
				try
				{			
					PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
					ps2.setInt(1,rs.getInt("coddatosfinanciacion"));
					
					logger.info("\n CADENA CONSULTA DETALLE APLICACION PAGOS >>  cadena >>  "+cadenaConsultaDetalle+"  Codigo Datos Financiacion >> "+rs.getInt("coddatosfinanciacion"));
					ResultSetDecorator rs2= new ResultSetDecorator(ps2.executeQuery());
					while(rs2.next())
					{	
					 DtoDetApliPagosCarteraPac dtoDetalleAplicacion= new DtoDetApliPagosCarteraPac();
					 dtoDetalleAplicacion.setConsecutivo(rs2.getInt("numaplicacion")+"");
					 dtoDetalleAplicacion.setFechaPago(rs2.getString("fechaplicacion")+"");
					 dtoDetalleAplicacion.setValor(rs2.getBigDecimal("valoraplicacion"));
					 dtoDetalleAplicacion.setUsuario(rs2.getString("usuarioaplicacion"));
					 dto.getDetalleAplicacion().add(dtoDetalleAplicacion);
					 cantCuotas++;
					}
					ps2.close();
					rs2.close();
					
				}catch (Exception e) {			
					e.printStackTrace();
					logger.info("\n error en  consulta Detalle Aplicacion Pagos >>  cadena >> "+cadenaConsultaDetalle+" ");
					
				}
				dto.setCantidadCuotasPagas(cantCuotas);
				dto.setDeudor(datosDuedorFinanciacion(rs.getInt("coddatosfinanciacion"),ConstantesIntegridadDominio.acronimoDeudor));
				dto.setCoDeudor(datosDuedorFinanciacion(rs.getInt("coddatosfinanciacion"),ConstantesIntegridadDominio.acronimoCoDeudor));
				
				lista.add(dto);
			}
			ps.close();
			rs.close();
		
		}catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Ingresos Facturas Atencion Medica >>  cadena >> "+cadenaConsulta+" ");
			
		}
		UtilidadBD.closeConnection(con);
		return lista;
	}
	
	
	/**
	 * 
	 * @param parametros
	 * @return
	 */
	public static ArrayList<DtoIngresosFacturasAtenMedica> consultarDocsCarteraPacienteXRango(HashMap parametros)
	{
		ArrayList<DtoIngresosFacturasAtenMedica> lista = new ArrayList<DtoIngresosFacturasAtenMedica>();
		String cadenaConsulta = consultaIngresosFactAtencionMedica;
		String cadenaConsultaDetalle= consultaDetalleAplicacionPagos;
		String cadenaParametros = new String("");
		int cantCuotas=0;
		double saldo=0;
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		cadenaConsulta= cadenaConsulta.replace(ConstantesBD.separadorSplitComplejo,"");
			
		// Parametro Centro Atencion
		if(parametros.containsKey("centroatencion") && !parametros.get("centroatencion").toString().equals(""))
		{
			cadenaParametros = " AND  ing.centro_atencion = "+parametros.get("centroatencion").toString()+" ";
		}
		
		
		// Parametro Fechas
		if(parametros.containsKey("fechaGrantiaInicial") && 
				parametros.containsKey("fechaGrantiaFinal") && 
					!parametros.get("fechaGrantiaInicial").toString().equals("") && 
						!parametros.get("fechaGrantiaFinal").toString().equals(""))
		    {
			cadenaParametros += " AND  to_char(docg.fecha_generacion,'YYYY-MM-DD') BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaGrantiaInicial").toString())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaGrantiaFinal").toString())+"'  ";
			
		    }
		
		// Parametro Tipo Documento
	    if(parametros.containsKey("tipoDocumento") && !parametros.get("tipoDocumento").toString().equals(""))
		  {
	    	  if(parametros.get("tipoDocumento").toString().equals(ConstantesIntegridadDominio.acronimoAmbos)){
	    		
	    		  cadenaParametros += " AND docg.tipo_documento IN ('"+ConstantesIntegridadDominio.acronimoTipoDocumentoPagare+"','"+ConstantesIntegridadDominio.acronimoTipoDocumentoLetra+"') ";
	    	  }
	    	  else
	    	  {
			  cadenaParametros += " AND docg.tipo_documento = '"+parametros.get("tipoDocumento").toString()+"' ";
	    	  }
		  }
	    
	    //parametro estado
	    if(parametros.containsKey("estado") && !parametros.get("estado").toString().equals(""))
		  {
			  cadenaParametros += " AND docg.estado = '"+parametros.get("estado").toString()+"' ";
		  }
	    
	    //parametro Codigo Garantia
	    if(parametros.containsKey("codigoGarantia") && !parametros.get("codigoGarantia").toString().equals(""))
		  {
			  cadenaParametros += " AND docg.consecutivo = '"+parametros.get("codigoGarantia").toString()+"' ";
		  }
	    
	    //parametro Numero Factura	    
	    if(parametros.containsKey("numFactura") && !parametros.get("numFactura").toString().equals(""))
		  {
			  cadenaParametros += " AND fac.consecutivo_factura = '"+parametros.get("numFactura").toString()+"' ";
		  }
		
	    cadenaConsulta=cadenaConsulta.replace("AND ing.codigo_paciente = ?",cadenaParametros);
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			logger.info("\n CADENA CONSULTA INGRESOS >>  cadena >> "+cadenaConsulta);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoIngresosFacturasAtenMedica dto= new DtoIngresosFacturasAtenMedica();
				dto.setCodigoIngreso(rs.getInt("idingreso")+"");
				dto.setConsecutivoIngreso(rs.getString("numingreso"));
				dto.setCodDatosFinanciacion(rs.getInt("coddatosfinanciacion")+"");
				dto.setNomPaciente(rs.getString("nombrepaciente"));
				dto.setCodCentroAtencion(rs.getInt("codcentroatencion")+"");
				dto.setNomCentroAtencion(rs.getString("nomcentroatencion"));
				dto.setNumFactura(rs.getString("consecutivofactura"));
				dto.setFechaFactura(rs.getString("fechafactura"));
				dto.setTipoDocumentoCartera(rs.getString("tipodocumento"));
				dto.setEstadoDocCartera(rs.getString("estado"));
				dto.setCodGarantia(rs.getString("codgarantia"));
				dto.setNumPagare(rs.getString("numpagare"));
				dto.setNumCuotasFinanciacion(rs.getInt("nrocuotas"));
				dto.setFechaInicioFinanciacion(rs.getString("fechainicio"));
				dto.setDiaporcuotaFinanciacion(rs.getInt("diasporcuota"));
				dto.setAnioConsecPagare(rs.getString("anioconspagare"));
				dto.setValorDocGarantia(rs.getDouble("valor")+"");
				
				saldo = rs.getDouble("valor") - rs.getDouble("valorestar");			
				
				dto.setSaldo(saldo+""); 
				
				try
				{			
					PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaDetalle,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
					ps2.setInt(1,rs.getInt("coddatosfinanciacion"));
					
					logger.info("\n CADENA CONSULTA DETALLE APLICACION PAGOS >>  cadena >>  "+cadenaConsultaDetalle+"  Codigo Datos Financiacion >> "+rs.getInt("coddatosfinanciacion"));
					ResultSetDecorator rs2= new ResultSetDecorator(ps2.executeQuery());
					while(rs2.next())
					{	
					 DtoDetApliPagosCarteraPac dtoDetalleAplicacion= new DtoDetApliPagosCarteraPac();
					 dtoDetalleAplicacion.setConsecutivo(rs2.getInt("numaplicacion")+"");
					 dtoDetalleAplicacion.setFechaPago(rs2.getString("fechaplicacion")+"");
					 dtoDetalleAplicacion.setValor(rs2.getBigDecimal("valoraplicacion"));
					 dtoDetalleAplicacion.setUsuario(rs2.getString("usuarioaplicacion"));
					 dto.getDetalleAplicacion().add(dtoDetalleAplicacion);
					 cantCuotas++;
					}
					ps2.close();
					rs2.close();
					
				}catch (Exception e) {			
					e.printStackTrace();
					logger.info("\n error en  consulta Detalle Aplicacion Pagos >>  cadena >> "+cadenaConsultaDetalle+" ");
					
				}
				dto.setCantidadCuotasPagas(cantCuotas);
				dto.setDeudor(datosDuedorFinanciacion(rs.getInt("coddatosfinanciacion"),ConstantesIntegridadDominio.acronimoDeudor));
				dto.setCoDeudor(datosDuedorFinanciacion(rs.getInt("coddatosfinanciacion"),ConstantesIntegridadDominio.acronimoCoDeudor));
				
				lista.add(dto);
			}
			ps.close();
			rs.close();
		
		}catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en  consulta Ingresos Facturas Atencion Medica >>  cadena >> "+cadenaConsulta+" ");
			
		}
		UtilidadBD.closeConnection(con);
		return lista;
		
	}
	
	/**
	 * Metodo que consulta los datos de un deudor
	 * @param datosFinanciacion
	 * @return
	 */
	public static DtoDeudoresDatosFinan datosDuedorFinanciacion(int datosFinanciacion,String claseDeudor)
	{
		String cadenaConsulta=consultarDatosDeudor;
		cadenaConsulta=cadenaConsulta.replace(ConstantesBD.separadorSplit, claseDeudor);
		DtoDeudoresDatosFinan deudor=new DtoDeudoresDatosFinan();
		Connection con = null;
		con = UtilidadBD.abrirConexion();
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,datosFinanciacion);

			logger.info("\n CADENA CONSULTA DATOS DEUDOR >>  cadena >> "+cadenaConsulta+"  codDatosFinanciacion >> "+datosFinanciacion);
			ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
			
			if (rs.next())
			{
				deudor.setDatosFinanciacion(rs.getInt("datosfin"));
				deudor.setClaseDeudor(rs.getString("clasedeudor"));
				deudor.setPrimerNombre(rs.getString("primernombre"));
				deudor.setSegundoNombre(rs.getString("segundonombre"));
				deudor.setPrimerApellido(rs.getString("primerapellido"));
				deudor.setSegundoApellido(rs.getString("segundoapellido"));
				deudor.setNumeroIdentificacion(rs.getString("numidentificacion"));
				deudor.setTipoIdentificacion(rs.getString("tipoidentificacion"));
				deudor.setNombreTipoIdentificacion(rs.getString("nomtipoidentificacion"));
				deudor.setExiteDeudor(ConstantesBD.acronimoSi);
			}
		
		}catch (Exception e) {			
			e.printStackTrace();
			logger.info("\n error en Consulta Datos Deudor/Codeudor >>  cadena >> "+cadenaConsulta+" Clase Deudor "+claseDeudor );
		}
		UtilidadBD.closeConnection(con);
		return deudor;
	}
	
	
	/**
	 *  Metodo para realizar el proceso de guardar los Datos de la refinanciacion
	 * @param con
	 * @param datos
	 * @return
	 */
	public static HashMap guardarDatosRefinanciacion(Connection con, HashMap datos)
	{
	 HashMap respuesta= new HashMap();
	 respuesta.put("codigoFin", ConstantesBD.codigoNuncaValido);
	 String cadenaActualizacionDatFinanciacion=updateDatosFinanciacionStr;
	 DtoIngresosFacturasAtenMedica dtoIngreso=(DtoIngresosFacturasAtenMedica)datos.get("dtoIngresoPaciente");
	 ArrayList<DtoCuotasDatosFinanciacion> listaCuotas= new ArrayList<DtoCuotasDatosFinanciacion>();
	 listaCuotas= (ArrayList<DtoCuotasDatosFinanciacion>)datos.get("listaCuotasRefinanciacion");
	 int cuotasAsignadas= Utilidades.convertirAEntero(datos.get("numCuotas").toString());
	 int diasporcuota= Utilidades.convertirAEntero(datos.get("diasporCuota").toString());
	 int codDatosFinanciacion=Utilidades.convertirAEntero(dtoIngreso.getCodDatosFinanciacion());
	 
	 try
		{	
		  // Actualizar los datos de Datos Financiacion 
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaActualizacionDatFinanciacion,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setDate(1,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get("fechaInicio").toString())));
			ps.setInt(2, diasporcuota);
			ps.setString(3, datos.get("observaciones").toString());
			ps.setInt(4,cuotasAsignadas);
			ps.setInt(5, codDatosFinanciacion);
			
			if(ps.executeUpdate() > 0)
			{
				//Inactiva las cuotas existentes asociadas a datos financiacion
				if(inactivarCuotasDatosFinanciacion(codDatosFinanciacion)>0)
				{
					//Guarda los datos de las cuotas
					 if(guardarNuevosDatosCuotas(con,listaCuotas,codDatosFinanciacion,datos.get("usuario").toString()) > 0)
					 {
						 // Guarda Historico de la Refinanciacion
						 if( guardarHistoricoDatosFinanciacion(con , datos) > 0)
						 {
							 respuesta.put("codigoFin",codDatosFinanciacion);
							 return respuesta;
							 
						 }
					 }
				}
			}
		}
  	    catch(Exception e)
  	     {
  	      e.printStackTrace();	
  		 logger.info("\n\n Error en el Proceso de Refinanciacion  >>  cadena >> "+cadenaActualizacionDatFinanciacion);
  		 
        }	 
	 return respuesta;
	}
	
	
	
	
	
	public static  ArrayList<DtoHistoDatosFinanciacion> consultarHistoricoRefinanciacion(String codDatosFinanciacion)
	{
		 ArrayList<DtoHistoDatosFinanciacion> array = new  ArrayList<DtoHistoDatosFinanciacion>();
		 String cadenaConsulta=consultarHistoricoRefinanciacion;
		 int codigoFinanciacion= Utilidades.convertirAEntero(codDatosFinanciacion);
		 Connection con = null;
		 con = UtilidadBD.abrirConexion();
		 
		 try
			{			
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				ps.setInt(1,codigoFinanciacion);

				logger.info("\n CADENA CONSULTA HISTORICOS FINANCIACION >>  cadena >> "+cadenaConsulta+"  CodigoDatFinanciacion >> "+codigoFinanciacion);
				ResultSetDecorator rs= new ResultSetDecorator(ps.executeQuery());
				
				while(rs.next())
				{
					DtoHistoDatosFinanciacion dtohisto= new DtoHistoDatosFinanciacion();
					dtohisto.setConsecutivo(rs.getInt("consecutivo")+"");
					dtohisto.setNroCuotasAnteriores(rs.getInt("cuotasanteriores")+""); 
					dtohisto.setNroCuotasAsignadas(rs.getInt("cuotasasignadas")+"");
					dtohisto.setDiasPorCuotas(rs.getInt("diasporcuota")+"");
					dtohisto.setValorRefinanciar(rs.getDouble("valorefinanciar")+"");
					dtohisto.setValorCuota(rs.getDouble("valorcuota")+"");
					dtohisto.setFechaModificacion(UtilidadFecha.conversionFormatoFechaAAp(rs.getString("fechamodificacion")));
					dtohisto.setHoraModificacion(rs.getString("horamodificacion")+"");
					dtohisto.setUsuarioModificacion(rs.getString("usuario"));
					array.add(dtohisto);
					
				}
				rs.close();
				ps.close();
				
			}catch(Exception e)
	  	    {
		  	      e.printStackTrace();	
		  		 logger.info("\n\n Error en Consulta Historico Financiacion >>  cadena >> "+cadenaConsulta);
		  		 
		    }	
		 UtilidadBD.closeConnection(con);
		 return array;
		 
	}
	
	/**
	 * Metodo para inactivar ( activo = N ) las cuotas de Datos Financiacion 
	 * @param codDatosFinanciacion
	 * @return
	 */
	public static int  inactivarCuotasDatosFinanciacion(int codDatosFinanciacion)
	{
		String cadena=inactivarCuotasDatosFinanciacion;
		int resp=1;
		Connection con = null;
		con = UtilidadBD.abrirConexion();
     	try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codDatosFinanciacion);
			ps.executeUpdate();
		}
     	catch(Exception e)
     	{
     		e.printStackTrace();
     		logger.info("\n\n Error en Actualizacion ( Inactivacion ) de Cuotas Datos Financiacion >>  cadena >> "+cadena+" codDatosFinanciacion :" +codDatosFinanciacion);
     		resp=0;
     	}
     	UtilidadBD.closeConnection(con);
		return resp;
	}
	
	
	/**
	 * Metodo para guardar Historico de Datos Financiacion
	 * @param con
	 * @param datos
	 * @return
	 */
	public static int guardarHistoricoDatosFinanciacion (Connection con, HashMap datos)
	{
		String cadena=insertHistoricoDatosFinanciacionStr;
		DtoIngresosFacturasAtenMedica dtoIngreso=(DtoIngresosFacturasAtenMedica)datos.get("dtoIngresoPaciente");
		int cuotasAsignadas= Utilidades.convertirAEntero(datos.get("numCuotas").toString());
		int cuotasAnteriores = Utilidades.convertirAEntero(datos.get("numCuotasAnt").toString()); 
		int diasporcuota= Utilidades.convertirAEntero(datos.get("diasporCuota").toString());
		int codDatosFinanciacion=Utilidades.convertirAEntero(dtoIngreso.getCodDatosFinanciacion());
		double valorRefinanciar = Utilidades.convertirADouble(dtoIngreso.getSaldo());
		double valorCuota = Utilidades.convertirADouble(datos.get("valorCuota").toString());
		int resp=0;
		int codPk = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_his_datos_financiacion");	
		int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_his_fin_con");
		
		try
		{			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			ps.setInt(1,codPk);
			ps.setInt(2,consecutivo);
			ps.setInt(3, cuotasAnteriores);
			ps.setInt(4, cuotasAsignadas);
			ps.setInt(5, diasporcuota);
			ps.setDouble(6, valorRefinanciar);
			ps.setDouble(7, valorCuota);
			ps.setDate(8,Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(datos.get("fechaInicio").toString())));
			ps.setString(9,datos.get("observaciones").toString());
			ps.setString(10,datos.get("usuario").toString());
			ps.setInt(11,codDatosFinanciacion);
			
			if(ps.executeUpdate()>0)
			{
				resp=1;
			}	
			
		}
     	catch(Exception e)
     	{
     		e.printStackTrace();
     		logger.info("\n\n Error el la Insercion de Historico Datos Financiacion >>  cadena >> "+cadena );
     		resp=0;
     	}
     	
		return resp; 	
	
	}
	
	/**
	 * 
	 * @param con
	 * @param datosCuotas
	 * @param codDatoFinanciacion
	 * @return
	 */
	public static int guardarNuevosDatosCuotas(Connection con, ArrayList<DtoCuotasDatosFinanciacion> listaDatosCuotas, int codDatoFinanciacion, String usuario)
	{
		int numRegistros= listaDatosCuotas.size(), resp=0;
		int consecutivo;
		String cadenaInsert=insertCuotasDatosFinanciacion;
		
		
		for(int i=0; i<numRegistros;i++)
		{
			DtoCuotasDatosFinanciacion dto= new DtoCuotasDatosFinanciacion();
			dto=listaDatosCuotas.get(i);
			consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_cuotas_datos_fin");
			try
			{			
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsert,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
				ps.setInt(1,consecutivo);
				ps.setInt(2,codDatoFinanciacion);
				ps.setString(3, dto.getNumeroDocumento());
				ps.setDouble(4, Utilidades.convertirADouble(dto.getValorCuota().toString()));
				ps.setString(5, usuario);
				ps.setInt(6, i);
				
				if(ps.executeUpdate()>0)
				{
					resp=1;
				}	
				
			}
	     	catch(Exception e)
	     	{
	     		e.printStackTrace();
	     		logger.info("\n\n Error en Actualizacion ( INSERT ) Cuotas >>  cadena >> "+cadenaInsert );
	     		resp=0;
	     	}
	     	
	     	if(resp==0)
	     	{
	     		return resp;
	     	}
			
		}
		
		return resp;
	}
	
}
