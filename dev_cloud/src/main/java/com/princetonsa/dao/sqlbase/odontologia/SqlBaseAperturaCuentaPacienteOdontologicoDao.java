package com.princetonsa.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.ResultadoBoolean;
import util.UtilidadBD;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.manejoPaciente.DtoCuentas;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;


public class SqlBaseAperturaCuentaPacienteOdontologicoDao
{
	private static String consultaConveniosStr	=	"SELECT " +
														"con.codigo AS codigo, " +
														"con.nombre AS nombre, " +
														"con.es_conv_tar_cli AS es_conv_tar_cli, " +
														"con.ing_pac_val_bd AS ing_pac_val_bd, " +
														"con.ing_pac_req_aut AS ing_pac_req_aut, " +
														"con.req_ing_val_auto AS req_ing_val_auto, " +
														"con.tipo_atencion AS tipo_atencion, " +
														
														"con.req_bono_ing_pac AS req_bono_ing_pac," +
														"con.maneja_bonos " +
												"FROM " +
													"facturacion.convenios con " +
												"WHERE " +
													"con.activo='"+ValoresPorDefecto.getValorTrueParaConsultas()+"' " +
												"AND " +
													"con.tipo_atencion='"+ConstantesIntegridadDominio.acronimoTipoAtencionConvenioOdontologico+"'" +
												"ORDER BY con.nombre";
	
	public static ArrayList<DtoConvenio> consultaConvenios()
	{
		ArrayList<DtoConvenio> listadoConvenios = new ArrayList<DtoConvenio>();
		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps = new PreparedStatementDecorator(con,consultaConveniosStr);
//			logger.info(ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoConvenio dto= new DtoConvenio();
				dto.setCodigo(rs.getInt("codigo"));
				dto.setDescripcion(rs.getString("nombre"));
				dto.setIngresarPacienteValidacionBD(rs.getString("ing_pac_val_bd"));
				dto.setEsConvenioTarjetaCliente(rs.getString("es_conv_tar_cli"));
				dto.setIngresoPacienteRequiereAutorizacion(rs.getString("ing_pac_req_aut"));
				dto.setRequiereIngresoValidacionAuto(rs.getString("req_ing_val_auto"));
				
				dto.setRequiereBonoIngresoPaciente(rs.getString("req_bono_ing_pac"));
				dto.setManejaBonos(rs.getString("maneja_bonos"));
				listadoConvenios.add(dto);
			}
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
			Log4JManager.error("ERROR EN consultaConvenios==> ",e);
		}
		return listadoConvenios;
	}
	
	public static ArrayList<DtoConvenio> consultaConveniosTarjetaPaciente(Connection con, DtoPaciente paciente)
	{
		String consultaConveniosTarjetaPacienteStr = "SELECT "+ 
					"ttc.convenio AS convenio, " +
					"con.nombre AS nombre, " +
					"con.maneja_bonos AS maneja_bonos, " +
					"con.req_ing_val_auto AS req_ing_val_auto "+
				"FROM "+ 
					"odontologia.tipos_tarj_cliente ttc "+ 
					
				"INNER JOIN "+
					" odontologia.venta_tarjeta_cliente vtc ON (vtc.tipo_tarjeta=ttc.codigo_pk) "+
					
				"INNER JOIN "+
					"odontologia.beneficiario_tarjeta_cliente btc ON (btc.venta_tarjeta_cliente=vtc.codigo_pk) " +
					
				"INNER JOIN "+
					"odontologia.beneficiario_tc_paciente btcpaciente ON (btc.codigo_pk=btcpaciente.codigo_beneficiario) " +
					
				"INNER JOIN " +
					"convenios con ON (con.codigo=ttc.convenio)" +
					
				"WHERE " +
						//"btcpaciente.codigo_persona=? " +
						"btcpaciente.codigo_paciente=? " +
						
					"AND " +
						"vtc.estado_venta='"+ConstantesIntegridadDominio.acronimoEstadoFacturado+"' " +
					"AND " +
						"btc.estado_tarjeta='"+ConstantesIntegridadDominio.acronimoEstadoActivo+"'";
		try {
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consultaConveniosTarjetaPacienteStr);
			psd.setInt(1, paciente.getCodigo());
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			ArrayList<DtoConvenio> listaConvenios=new ArrayList<DtoConvenio>();
			while(rsd.next())
			{
				int convenio=rsd.getInt("convenio");
				DtoConvenio dto=new DtoConvenio();
				dto.setCodigo(convenio);
				dto.setDescripcion(rsd.getString("nombre"));
				dto.setManejaBonos(rsd.getString("maneja_bonos"));
				dto.setRequiereIngresoValidacionAuto(rsd.getString("req_ing_val_auto"));
				listaConvenios.add(dto);
			}
			rsd.close();
			psd.close();
			return listaConvenios;
		} catch (SQLException e) {
			Log4JManager.error("Error consultando beneficiario tarjeta cliente", e);
		}
		return null;
	}

	public static boolean guardarCuentas(Connection con, ArrayList<DtoSubCuentas> listaCuentas, UsuarioBasico usuario, PersonaBasica paciente, int codigoArea)
	{
		int idIngreso=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_ingresos");
		
		String consecutivo=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoIngresos, usuario.getCodigoInstitucionInt());
		String anioConsecutivo=UtilidadBD.obtenerAnioActualTablaConsecutivos(con, ConstantesBD.nombreConsecutivoIngresos, usuario.getCodigoInstitucionInt());
		
		try 
		{
			String sentencia=
				"INSERT INTO " +
					"manejopaciente.ingresos(" +
						"id, " +					//1
						"codigo_paciente, " +		//2
						"institucion, " +			//3
						"estado, " +				//4
						"fecha_ingreso, " +
						"hora_ingreso, " +
						"usuario_modifica, " +		//5
						"fecha_modifica, " +
						"hora_modifica, " +
						"centro_atencion, " +		//6
						"consecutivo, " +			//7
						"anio_consecutivo," +		//8
						"tipo_ingreso " +			//9
					") VALUES" +
					"(?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?, ?)";
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, idIngreso);
			psd.setInt(2, paciente.getCodigoPersona());
			psd.setInt(3, usuario.getCodigoInstitucionInt());
			psd.setString(4, ConstantesIntegridadDominio.acronimoEstadoAbierto);
			psd.setString(5, usuario.getLoginUsuario());
			psd.setInt(6, usuario.getCodigoCentroAtencion());
			psd.setString(7, consecutivo);
			psd.setString(8, anioConsecutivo);
			psd.setString(9, ConstantesIntegridadDominio.acronimoTipoEspecialidadOdontologica);
//			Log4JManager.info("Sentencia ingreso "+psd);
			psd.executeUpdate();
			Log4JManager.info("*************INSERTE INGRESOS*********"+psd);
			psd.close();
			UtilidadBD.cambiarUsoFinalizadoConsecutivo(con, ConstantesBD.nombreConsecutivoIngresos, usuario.getCodigoInstitucionInt(), consecutivo, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		} 
		catch (SQLException e) 
		{
			Log4JManager.error("Error creando el ingreso", e);
			return false;
		}

		int idCuenta=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_cuentas");
		
		try {
			String sentencia="INSERT INTO " +
					"manejopaciente.cuentas(" +
						"id, " +						//1
						"codigo_paciente, " +			//2
						"fecha_apertura, " +			
						"hora_apertura, " +				
						"estado_cuenta, " +				//3
						"tipo_paciente, " +				//4
						"indicativo_acc_transito, " +	//5
						"id_ingreso, " +				//6
						"usuario_modifica, " +			//7
						"area, " +						//8
						"tipo_evento, " +				//9
						"origen_admision, " +			//10
						"via_ingreso, " +				//11
						"desplazado," +					//12
						"hospital_dia, " +				//13
						"fecha_modifica, " +			
						"hora_modifica" +
					") VALUES (" +
						"?, " +							//1
						"?, " +							//2
						"CURRENT_DATE, " +
						ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
						"?, " +							//3
						"?, " +							//4
						"?, " +							//5
						"?, " +							//6
						"?, " +							//7
						"?, " +							//8
						"?, " +							//9
						"?, " +							//10
						"?, " +							//11
						"?, " +							//12
						"?, " +							//13
						"CURRENT_DATE, " +
						ValoresPorDefecto.getSentenciaHoraActualBD() +
					")";

			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
			psd.setInt(1, idCuenta);
			psd.setInt(2, paciente.getCodigoPersona());
			psd.setInt(3, ConstantesBD.codigoEstadoCuentaActiva);
			psd.setString(4, ConstantesBD.tipoPacienteAmbulatorio);
			psd.setBoolean(5, false);
			psd.setInt(6, idIngreso);
			psd.setString(7, usuario.getLoginUsuario());
			psd.setInt(8, codigoArea);/*Centro de costo*/ 
			psd.setString(9, "");
			psd.setInt(10, ConstantesBD.codigoOrigenAdmisionHospitalariaEsConsultaExterna);
			psd.setInt(11, ConstantesBD.codigoViaIngresoConsultaExterna);
			psd.setString(12, ConstantesBD.acronimoNo);
			psd.setString(13, ConstantesBD.acronimoNo);
//			Log4JManager.info("Sentencia cuenta "+psd);
			psd.executeUpdate();
			Log4JManager.info("*************INSERTE CUENTAS*********"+psd);
			psd.close();
		}catch (SQLException e) {
			Log4JManager.error("Error ingresando la cuenta de odontología", e);
			return false;
		}
		boolean resultado=false;
		for(int i=0; i<listaCuentas.size(); i++)
		{
			int idSubcuenta=UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_sub_cuentas");
			if(idSubcuenta<1)
			{
				return false;
			}
			DtoSubCuentas subCuenta=listaCuentas.get(i);
			String sentencia="INSERT INTO " +
								"manejopaciente.sub_cuentas(" +
									"sub_cuenta, " +			//1
									"convenio, " +				//2
									"naturaleza_paciente, " +	//3
									"monto_cobro, " +			//4
									"fecha_modifica, " +
									"usuario_modifica, " +		//5
									"contrato, " +				//6
									"ingreso, " +				//7
									"codigo_paciente, " +		//8
									"nro_prioridad, " +			//9
									"facturado, " +				//10
									"hora_modifica, " +	
									"bono, " +					//11		
									"valor_autorizacion, " +	//12
									"medio_autorizacion" +		//13
								")" +
								"VALUES(?, ?, ?, ?, CURRENT_DATE, ?, ?, ?, ?, ?, ?, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?)";
			try {
				PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentencia);
				psd.setInt(1, idSubcuenta);
				psd.setInt(2, subCuenta.getConvenio().getCodigo());
				psd.setInt(3, ConstantesBD.codigoNaturalezaPacientesNinguno);
				psd.setNull(4, Types.INTEGER);
				psd.setString(5, usuario.getLoginUsuario());
				psd.setInt(6, subCuenta.getContrato());
				psd.setInt(7, idIngreso);
				psd.setInt(8, paciente.getCodigoPersona());
				psd.setInt(9, i+1);
				psd.setString(10, ConstantesBD.acronimoNo);
				
				
				//Tarea 6549- El bono llegaba nulo al momento de hacer las iteraciones, por ello se debe preguntar si esta nulo para luego preguntar por sus valores y que no arroje nullpointer exception
				if (subCuenta.getBono()!=null)
				{
					if(subCuenta.getBono().intValue()>0)
						psd.setInt(11, subCuenta.getBono().intValue());
					else
						psd.setNull(11, Types.INTEGER);
				}
				else
					psd.setNull(11, Types.INTEGER);
				
				psd.setDouble(12, subCuenta.getValorAutorizacion());
				if(subCuenta.getMedioAutorizacion().equals(""))
				{
					psd.setNull(13, Types.VARCHAR);
				}
				else
				{
					psd.setString(13, subCuenta.getMedioAutorizacion());
				}
				Log4JManager.info("ingreso subcuenta "+psd);
				psd.executeUpdate();
				Log4JManager.info("*************INSERTE SUBCUENTAS*********"+psd);
				psd.close();
				resultado=true;
			}
			catch (SQLException e) {
				Log4JManager.error("Error ingresando la subcuenta", e);
				return false;
			}
			
			try{
				String sentenciaIngresoBeneficiarioPersona=
					"SELECT " +
						"btc.codigo_pk AS codigo_beneficiario, " +
						"per.codigo AS codigo_persona " +
					"FROM " +
						"odontologia.beneficiario_tarjeta_cliente btc " +
					"INNER JOIN " +
						"odontologia.venta_tarjeta_cliente vtc " +
							"ON(btc.venta_tarjeta_cliente=vtc.codigo_pk) " +
					"INNER JOIN " +
						"odontologia.tipos_tarj_cliente ttc " +
							"ON(ttc.codigo_pk=vtc.tipo_tarjeta) " +
					"INNER JOIN " +
						"personas per " +
							"ON(per.codigo=btc.codigo_persona) " +
					"WHERE " +
						"ttc.convenio=? " +
					"AND " +
						"btc.codigo_persona=? ";
				PreparedStatementDecorator psd=new PreparedStatementDecorator(con, sentenciaIngresoBeneficiarioPersona);
				psd.setInt(1, subCuenta.getConvenio().getCodigo());
				psd.setInt(2, paciente.getCodigoPersona());
				ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
				if(rsd.next())
				{
					int codigoBeneficiario=rsd.getInt("codigo_beneficiario");
					int codigoPersona=rsd.getInt("codigo_persona");
					String busquedaRegistroBeneficiario=
								"SELECT " +
									"codigo_beneficiario AS codigo_beneficiario, " +
									"codigo_paciente AS codigo_paciente "+
								"FROM " +
									"odontologia.beneficiario_tc_paciente btcp " +
								"WHERE " +
									"codigo_beneficiario=? " +
								"AND " +
									"codigo_paciente=?";
								
					PreparedStatementDecorator psdBusqueda=new PreparedStatementDecorator(con, busquedaRegistroBeneficiario);
					psdBusqueda.setInt(1, codigoBeneficiario);
					psdBusqueda.setInt(2, codigoPersona);
					ResultSetDecorator rsdBusqueda=new ResultSetDecorator(psdBusqueda.executeQuery());
					if(!rsdBusqueda.next())
					{
						String sentenciaIngresoDatos=
								"INSERT INTO " +
									"odontologia.beneficiario_tc_paciente" +
									"(" +
										"codigo_beneficiario, " +
										"codigo_paciente" +
									") " +
								"VALUES(?, ?)";
						PreparedStatementDecorator psdIngreso=new PreparedStatementDecorator(con, sentenciaIngresoDatos);
						psdIngreso.setInt(1, codigoBeneficiario);
						psdIngreso.setInt(2, codigoPersona);
//						Log4JManager.info(psdIngreso);
						resultado=psdIngreso.executeUpdate()>0;
						psdIngreso.close();
					}
					psdBusqueda.close();
					rsdBusqueda.close();
				}
				rsd.close();
				psd.close();
				
			}
			catch (SQLException e) {
				Log4JManager.error("Error ingresando beneficiario tarjeta cliente", e);
				return false;
			}
			try{
				String sentenciaDocumentosAdjuntos=
					"INSERT INTO " +
						"manejopaciente.adjuntos_subcuenta" +
						"(" +
							"codigo_pk, " +
							"subcuenta, " +
							"nombre_original, " +
							"nombre_generado" +
						") " +
						"VALUES (?, ?, ?, ?)";
			
				int numDocs=subCuenta.getDocumentosAdjuntos().size();
				for(int j=0; j<numDocs; j++)
				{
					int idDocAdjunto=UtilidadBD.obtenerSiguienteValorSecuencia(con, "manejopaciente.seq_adjuntos_subcuenta");
					PreparedStatementDecorator psDocumentosAdjuntos=new PreparedStatementDecorator(con, sentenciaDocumentosAdjuntos);
					psDocumentosAdjuntos.setInt(1, idDocAdjunto);
					psDocumentosAdjuntos.setInt(2, idSubcuenta);
					String[] nombres=subCuenta.getDocumentoAdjunto(j).split(ConstantesBD.separadorSplit);
					psDocumentosAdjuntos.setString(3, nombres[1]);
					psDocumentosAdjuntos.setString(4, nombres[0]);
					int resultadoDoc=psDocumentosAdjuntos.executeUpdate();
					if(resultadoDoc<1)
					{
						psDocumentosAdjuntos.close();
						return false;
					}
					psDocumentosAdjuntos.close();
				}
			}
			catch (SQLException e) {
				Log4JManager.error("Error ongresando los documentos adjuntos ",e);
				return false;
			}
		}
		return resultado;
	}
	
	
	/**
	 * Método para cargar datos de la cuenta del ingreso abierto del paciente
	 * @param con
	 * @param paciente
	 * @return
	 */
	public static DtoCuentas cargarCuentaIngresoAbiertoPaciente(Connection con,DtoPaciente paciente)
	{
		DtoCuentas dtoCuenta = new DtoCuentas();
		try
		{
			String consulta = "SELECT " +
				"c.id as id_cuenta, " +
				"c.id_ingreso as id_ingreso," +
				"c.area as codigo_area," +
				"administracion.getnomcentrocosto(c.area) as nombre_area, " +
				"c.codigo_paciente as codigo_paciente  " +
				"from cuentas c " +
				"inner join ingresos i on (i.id = c.id_ingreso) " +
				"WHERE " +
				"c.codigo_paciente = ? and i.estado = ? order by c.id";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,paciente.getCodigo());
			pst.setString(2, ConstantesIntegridadDominio.acronimoEstadoAbierto);
			
			Log4JManager.info("La consulta es: "+pst);
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			
			if(rs.next())
			{
				dtoCuenta.setIdCuenta(rs.getString("id_cuenta"));
				dtoCuenta.setIdIngreso(rs.getString("id_ingreso"));
				dtoCuenta.setCodigoArea(rs.getInt("codigo_area"));
				dtoCuenta.setDescripcionArea(rs.getString("nombre_area"));
				dtoCuenta.setCodigoPaciente(rs.getString("codigo_paciente"));
			}
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en cargarCuentaIngresoAbiertoPaciente: ",e);
		}
		return dtoCuenta;
	}
	
	/**
	 * Método usado para cargar las subcuentas existentes del ingreso del paciente
	 * @param con
	 * @param cuenta
	 * @return
	 */
	public static ArrayList<DtoSubCuentas> cargarSubcuentas(Connection con,DtoCuentas cuenta)
	{
		ArrayList<DtoSubCuentas> subCuentas = new ArrayList<DtoSubCuentas>();
		try
		{
			String consulta = "SELECT "+ 
				"sc.sub_cuenta as sub_cuenta, "+
				"sc.convenio as codigo_convenio, "+ 
				"con.nombre as nombre_convenio, "+
				"sc.naturaleza_paciente as naturaleza_paciente, "+
				"sc.monto_cobro as monto_cobro, "+
				"sc.contrato as codigo_contrato, " +
				"facturacion.getnumerocontrato(sc.contrato) as numero_contrato, "+
				"sc.ingreso as id_ingreso, "+
				"sc.codigo_paciente as codigo_paciente, "+
				"sc.nro_prioridad as nro_prioridad, "+
				"sc.facturado as facturado, "+
				"coalesce(sc.bono,"+ConstantesBD.codigoNuncaValido+") as bono, "+
				"coalesce(sc.valor_autorizacion,"+ConstantesBD.codigoNuncaValido+") as valor_autorizacion, "+
				"sc.medio_autorizacion as medio_autorizacion, "+ 
				"con.es_conv_tar_cli AS es_conv_tar_cli, "+ 
				"con.ing_pac_val_bd AS ing_pac_val_bd, "+ 
				"con.ing_pac_req_aut AS ing_pac_req_aut, "+ 
				"con.req_ing_val_auto AS req_ing_val_auto, "+ 
				"con.tipo_atencion AS tipo_atencion, "+ 
				"con.req_bono_ing_pac AS req_bono_ing_pac, "+
				"con.maneja_bonos "+ 
				"FROM sub_cuentas sc "+ 
				"INNER JOIN facturacion.convenios con ON(con.codigo = sc.convenio) "+
				"WHERE sc.ingreso = ? order by sc.nro_prioridad ";
			
			PreparedStatementDecorator pst = new PreparedStatementDecorator(con,consulta);
			pst.setInt(1,Integer.parseInt(cuenta.getIdIngreso()));
			
			ResultSetDecorator rs = new ResultSetDecorator(pst.executeQuery());
			while(rs.next())
			{
				DtoSubCuentas subCuenta = new DtoSubCuentas();
				subCuenta.setSubCuenta(rs.getString("sub_cuenta"));
				subCuenta.getConvenio().setCodigo(rs.getInt("codigo_convenio"));
				subCuenta.getConvenio().setNombre(rs.getString("nombre_convenio"));
				subCuenta.setNaturalezaPaciente(rs.getInt("naturaleza_paciente"));
				subCuenta.setMontoCobro(rs.getInt("monto_cobro"));
				subCuenta.setContrato(rs.getInt("codigo_contrato"));
				subCuenta.setNumeroContrato(rs.getString("numero_contrato"));
				subCuenta.setIngreso(rs.getInt("id_ingreso"));
				subCuenta.setCodigoPaciente(rs.getInt("codigo_paciente"));
				subCuenta.setNroPrioridad(rs.getInt("nro_prioridad"));
				subCuenta.setFacturado(rs.getString("facturado"));
				subCuenta.setBono( rs.getBigDecimal("bono"));
				subCuenta.setValorAutorizacion(rs.getDouble("valor_autorizacion")==ConstantesBD.codigoNuncaValido?null:rs.getDouble("valor_autorizacion"));
				subCuenta.setMedioAutorizacion(rs.getString("medio_autorizacion"));
				subCuenta.getDatosConvenio().setEsConvenioTarjetaCliente(rs.getString("es_conv_tar_cli"));
				subCuenta.getDatosConvenio().setIngresarPacienteValidacionBD(rs.getString("ing_pac_val_bd"));
				subCuenta.getDatosConvenio().setIngresoPacienteRequiereAutorizacion(rs.getString("ing_pac_req_aut"));
				subCuenta.getDatosConvenio().setRequiereIngresoValidacionAuto(rs.getString("req_ing_val_auto"));
				subCuenta.getDatosConvenio().setTipoAtencion(rs.getString("tipo_atencion"));
				subCuenta.getDatosConvenio().setRequiereBonoIngresoPaciente(rs.getString("req_bono_ing_pac"));
				
				//*************SE CONSULTA LOS ADJUNTOS DE LA SUBCUENTA*************************************
				consulta = "SELECT " +
					"codigo_pk as codigo_pk, " +
					"subcuenta as subcuenta, " +
					"nombre_original as nombre_original, " +
					"nombre_generado as nombre_generado " +
					"FROM manejopaciente.adjuntos_subcuenta " +
					"WHERE subcuenta = ?";
				PreparedStatementDecorator pst1 = new PreparedStatementDecorator(con,consulta);
				pst1.setInt(1,Integer.parseInt(subCuenta.getSubCuenta()));
				
				ResultSetDecorator rs1 = new ResultSetDecorator(pst1.executeQuery());
				while(rs1.next())
				{
					subCuenta.getDocumentosAdjuntos().add(rs1.getString("nombre_generado")+ConstantesBD.separadorSplit+rs1.getString("nombre_original")+ConstantesBD.separadorSplit+rs1.getString("codigo_pk"));
				}
				UtilidadBD.cerrarObjetosPersistencia(pst1, rs1, null);
				//*******************************************************************************************
				
				subCuentas.add(subCuenta);
				
			}
			UtilidadBD.cerrarObjetosPersistencia(pst, rs, null);
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en cargarSubcuentas: ",e);
		}
		return subCuentas;
	}
	
	
	
	/**
	 * M&eacute;todo implementado para realizar la modificaci&oacute;n de la cuenta
	 * @param con
	 * @param listaCuentas
	 * @param usuario
	 * @param paciente
	 * @param dtoCuenta
	 * @return
	 */
	public static ResultadoBoolean modificarCuenta(Connection con,ArrayList<DtoSubCuentas> listaCuentas, UsuarioBasico usuario, PersonaBasica paciente, DtoCuentas dtoCuenta)
	{
		ResultadoBoolean resultado = new ResultadoBoolean(true,"");
		try
		{
			//***********SE ACTUALIZAN LOS DATOS DE CADA SUBCUENTA******************************************++
			
			
			String sentencia = "";
			int nroPrioridad = 1;
			
			for(DtoSubCuentas subCuenta:listaCuentas)
			{
				if(resultado.isTrue())
				{
					PreparedStatementDecorator pst = null;
					if(!subCuenta.getSubCuenta().equals(""))
					{
						if(subCuenta.getEliminar())
						{
							sentencia="DELETE FROM manejopaciente.sub_cuentas WHERE sub_cuenta = ?";
							pst=new PreparedStatementDecorator(con,sentencia);
							pst.setInt(1, Utilidades.convertirAEntero(subCuenta.getSubCuenta()));
						}
						else
						{
							sentencia = "UPDATE manejopaciente.sub_cuentas SET " +
								"convenio = ?, " + //1
								"monto_cobro = ?," + //2
								"fecha_modifica = current_date," + 
								"hora_modifica = "+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
								"usuario_modifica = ?," + //3
								"contrato = ?," + //4
								"nro_prioridad = ?," + //5
								"bono = ?," + //6
								"valor_autorizacion = ?," + //7
								"medio_autorizacion = ? " + //8
								"WHERE sub_cuenta = ? "; //9
							pst = new PreparedStatementDecorator(con,sentencia);
							pst.setInt(1,subCuenta.getConvenio().getCodigo());
							pst.setInt(2,subCuenta.getMontoCobro());
							pst.setString(3,usuario.getLoginUsuario());
							pst.setInt(4,subCuenta.getContrato());
							pst.setInt(5,nroPrioridad);
							if(subCuenta.getBono()!=null&&subCuenta.getBono().intValue()>0)
							{
								pst.setInt(6,subCuenta.getBono().intValue());
							}
							else
							{
								pst.setNull(6,Types.INTEGER);
							}
							
							if(subCuenta.getValorAutorizacion()!=null&&subCuenta.getValorAutorizacion().doubleValue()>0)
							{
								pst.setDouble(7,subCuenta.getValorAutorizacion());
							}
							else
							{
								pst.setNull(7, Types.NUMERIC);
							}
							if(!subCuenta.getMedioAutorizacion().equals(""))
							{
								pst.setString(8,subCuenta.getMedioAutorizacion());
							}
							else
							{
								pst.setNull(8,Types.VARCHAR);
							}
							pst.setInt(9,Integer.parseInt(subCuenta.getSubCuenta()));
						}
					}
					else
					{
						sentencia ="INSERT INTO " +
							"manejopaciente.sub_cuentas(" +
							"sub_cuenta, " +			//1
							"convenio, " +				//2
							"naturaleza_paciente, " +	//3
							"monto_cobro, " +			//4
							"fecha_modifica, " +
							"usuario_modifica, " +		//5
							"contrato, " +				//6
							"ingreso, " +				//7
							"codigo_paciente, " +		//8
							"nro_prioridad, " +			//9
							"facturado, " +				//10
							"hora_modifica, " +	
							"bono, " +					//11		
							"valor_autorizacion, " +	//12
							"medio_autorizacion" +		//13
						")" +
						"VALUES(?, ?, ?, ?, CURRENT_DATE, ?, ?, ?, ?, ?, ?, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?)";
						pst = new PreparedStatementDecorator(con,sentencia);
						
						subCuenta.setSubCuenta(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_sub_cuentas")+"");
						pst.setLong(1,(long)subCuenta.getSubCuentaDouble());
						pst.setInt(2,subCuenta.getConvenio().getCodigo());
						pst.setInt(3,ConstantesBD.codigoNaturalezaPacientesNinguno);
						pst.setInt(4,subCuenta.getMontoCobro());
						pst.setString(5,usuario.getLoginUsuario());
						pst.setInt(6,subCuenta.getContrato());
						pst.setInt(7,Integer.parseInt(dtoCuenta.getIdIngreso()));
						pst.setInt(8,Integer.parseInt(dtoCuenta.getCodigoPaciente()));
						pst.setInt(9,nroPrioridad);
						pst.setString(10,ConstantesBD.acronimoNo);
						if(subCuenta.getBono()!=null&&subCuenta.getBono().intValue()>0)
						{
							pst.setInt(11, subCuenta.getBono().intValue());
						}
						else
						{
							pst.setNull(11,Types.INTEGER);
						}
						if(subCuenta.getValorAutorizacion()!=null&&subCuenta.getValorAutorizacion().doubleValue()>0)
						{
							pst.setDouble(12,subCuenta.getValorAutorizacion());
						}
						else
						{
							pst.setNull(12,Types.NUMERIC);
						}
						
						if(!subCuenta.getMedioAutorizacion().equals(""))
						{
							pst.setString(13,subCuenta.getMedioAutorizacion());
						}
						else
						{
							pst.setNull(13,Types.VARCHAR);
						}
						
						
						
						
					}
					
					if(pst.executeUpdate()<=0)
					{
						resultado.setResultado(false);
						resultado.setDescripcion("Problemas internos registrando los datos de la sub cuenta del ingreso. Por favor contactar al adminsitrador del sistema");
						
						
					}
					UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
					
					nroPrioridad++;
					
					if(resultado.isTrue())
					{
						//*************SE REGISTRAN LOS ADJUNTOS D ELA SUBCUENTA**********************
						//Primero se elimina lo que tenía
						sentencia = "DELETE FROM manejopaciente.adjuntos_subcuenta WHERE subcuenta = ?";
						pst = new PreparedStatementDecorator(con,sentencia);
						pst.setDouble(1, subCuenta.getSubCuentaDouble());
						
						
						pst.executeUpdate();
						
						
						//Se registran los que hayan en el arreglo
						sentencia=
							"INSERT INTO " +
								"manejopaciente.adjuntos_subcuenta" +
								"(" +
									"codigo_pk, " +
									"subcuenta, " +
									"nombre_original, " +
									"nombre_generado" +
								") " +
								"VALUES (?, ?, ?, ?)";
					
						int idDocAdjunto=UtilidadBD.obtenerSiguienteValorSecuencia(con, "manejopaciente.seq_adjuntos_subcuenta");
						pst=new PreparedStatementDecorator(con, sentencia);
						pst.setInt(1, idDocAdjunto);
						pst.setDouble(2, subCuenta.getSubCuentaDouble());
						int numDocs=subCuenta.getDocumentosAdjuntos().size();
						for(int j=0; j<numDocs; j++)
						{
							if(resultado.isTrue())
							{
								String[] nombres=subCuenta.getDocumentoAdjunto(j).split(ConstantesBD.separadorSplit);
								pst.setString(3, nombres[1]);
								pst.setString(4, nombres[0]);
								if(pst.executeUpdate()<1)
								{
									resultado.setResultado(false);
									resultado.setDescripcion("Problemas tratando de ingresar un archivo adjunto de la subcuenta");
								}
							}
						}
						UtilidadBD.cerrarObjetosPersistencia(pst, null, null);
						//*******************************************************************************
					}
				}
				
				
			}//Fin for
			
			
			//********************************************************************************************
		}
		catch(SQLException e)
		{
			Log4JManager.error("Error en modificarCuenta: ",e);
		}
		return resultado;
	}
	
}