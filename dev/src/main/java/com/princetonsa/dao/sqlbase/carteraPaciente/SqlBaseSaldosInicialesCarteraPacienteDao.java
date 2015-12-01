/**
 * 
 */
package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoPaciente;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoSaldosInicialesCarteraPaciente;
import com.princetonsa.mundo.carteraPaciente.DatosFinanciacion;

/**
 * @author armando
 *
 */
public class SqlBaseSaldosInicialesCarteraPacienteDao 
{

	private static Logger logger=Logger.getLogger(SqlBaseSaldosInicialesCarteraPacienteDao.class);
	/**
	 * 
	 */
	private static String consultaSaldosIniciales=" SELECT DISTINCT" +
																" dg.tipo_documento as tipodocumento," +
																" getintegridaddominio(dg.tipo_documento) as desctipodocumento," +
																" dg.consecutivo as consecutivo," +
																" to_char(dg.fecha_generacion,'dd/mm/yyyy') as fechaelaboracion," +
																" dg.valor as valordocumento," +
																" deu.codigo_pk as codigodeudor," +
																" deu.primer_nombre||' '||coalesce(deu.segundo_nombre,' ')||' '||deu.primer_apellido||' '||coalesce(deu.segundo_apellido,' ') as nombredeudor," +
																" coalesce(dg.codigo_paciente,"+ConstantesBD.codigoNuncaValido+") as codigopaciente," +
																" per.primer_nombre||' '||coalesce(per.segundo_nombre,' ')||' '||per.primer_apellido||' '||coalesce(per.segundo_apellido,' ') as nombrepaciente," +
																" df.consecutivo_factura as consfactura," +
																" coalesce(fac.convenio) as codconvenio," +
																" coalesce(getnombreconvenio(fac.convenio),' ') as nomconvenio," +
																" coalesce(df.codigo_factura,"+ConstantesBD.codigoNuncaValido+") as codigofactura " +
													" from deudorco deu " +
													" inner join deudores_datos_finan ddf on(ddf.codigo_pk_deudor=deu.codigo_pk) " +
													" inner join datos_financiacion df on(df.codigo_pk = ddf.datos_financiacion) " +
													" inner join documentos_garantia dg on(df.codigo_pk_docgarantia=dg.codigo_pk) " +
													" left outer join personas per on(per.codigo=dg.codigo_paciente)" +
													" left outer join  facturas fac on(fac.codigo=df.codigo_factura)" +
													" where 1=1 ";

	/**
	 * 
	 */
	private static String insertarPersona="INSERT INTO administracion.personas (" +
																				" codigo," +
																				" tipo_identificacion," +
																				" numero_identificacion," +
																				" primer_nombre," +
																				" segundo_nombre," +
																				" primer_apellido," +
																				" segundo_apellido," +
																				" direccion," +
																				" telefono" +
																			") values(?,?,?,?,?,?,?,?,?)";
	
	/**
	 * 
	 */
	private static String insertarPaciente="INSERT INTO manejopaciente.pacientes (codigo_paciente,esta_vivo) values (?,?)";
	
	/**
	 * 
	 */
	private static String actualizarPersona="update administracion.personas SET direccion = ?,telefono=? where codigo=?";
	
	/**
	 * 
	 * @param parametros
	 * @return
	 */
	public static ArrayList<DtoSaldosInicialesCarteraPaciente> busquedaAvanzadaSaldosIncialesCP(HashMap<String, String> parametros) 
	{
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoSaldosInicialesCarteraPaciente> resultado=new ArrayList<DtoSaldosInicialesCarteraPaciente>();
		try
		{
			PreparedStatementDecorator ps=new PreparedStatementDecorator(con,construirConsulta(parametros));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				DtoSaldosInicialesCarteraPaciente dto=new DtoSaldosInicialesCarteraPaciente();
				dto.setCodigoDeudor(rs.getInt("codigodeudor"));
				dto.setCodigoFactura(rs.getInt("codigofactura"));
				dto.setCodigoPaciente(rs.getInt("codigopaciente"));
				dto.setConsecutivoDocumento(rs.getString("consecutivo"));
				dto.setConsecutivoFactura(rs.getInt("consfactura"));
				dto.setConvenio(new InfoDatosString(rs.getInt("codconvenio")+"",rs.getString("nomconvenio")));
				dto.setFechaElaboracion(rs.getString("fechaelaboracion"));
				dto.setNombreDeudor(rs.getString("nombredeudor"));
				dto.setNombresPaciente(rs.getString("nombrepaciente"));
				dto.setTipoDocumento(new InfoDatosString(rs.getString("tipodocumento"),rs.getString("desctipodocumento")));
				dto.setValorDocumento(new BigDecimal(rs.getDouble("valordocumento")));
				resultado.add(dto);
			}
		}
		catch(SQLException e)
		{
			logger.error("ERROR: ",e);
		}
		UtilidadBD.closeConnection(con);
		logger.info("numero de registros encontrados: ---> "+resultado.size());
		return resultado;
	}

	
	/**
	 * 
	 * @param parametros
	 * @return
	 */
	private static String construirConsulta(HashMap<String, String> parametros) 
	{
		String whereStr=" ";
		
		//variables del documento de garantia.
		if(!UtilidadTexto.isEmpty(parametros.get("tipoDocGarantia")))
		{
			whereStr+=" and dg.tipo_documento='"+parametros.get("tipoDocGarantia")+"'";
		}
		if(!UtilidadTexto.isEmpty(parametros.get("numDocGarantia")))
		{
			whereStr+=" and dg.consecutivo='"+parametros.get("numDocGarantia")+"'";
		}
		if(!UtilidadTexto.isEmpty(parametros.get("fechaDocGarantia")))
		{
			whereStr+=" and dg.fecha_generacion='"+UtilidadFecha.conversionFormatoFechaABD(parametros.get("fechaDocGarantia")+"")+"'";
		}
		
		//variables del deudor
		if(!UtilidadTexto.isEmpty(parametros.get("tipoBusqueda")))
		{
			if((parametros.get("tipoBusqueda")+"").equals("PAC"))
			{
				if(!UtilidadTexto.isEmpty(parametros.get("tipoId")))
				{
					whereStr+=" and per.tipo_identificacion='"+parametros.get("tipoId")+"'";
				}
				if(!UtilidadTexto.isEmpty(parametros.get("numeroId")))
				{
					whereStr+=" and per.numero_identificacion='"+parametros.get("numeroId")+"'";
				}
				if(!UtilidadTexto.isEmpty(parametros.get("nombrePersona")))
				{
					whereStr+=" and (upper(per.primer_nombre) like upper('%"+parametros.get("nombrePersona")+"%') or upper(per.segundo_nombre) like upper('%"+parametros.get("nombrePersona")+"%') )";
				}
				if(!UtilidadTexto.isEmpty(parametros.get("apellidoPersona")))
				{
					whereStr+=" and (upper(per.primer_apellido) like upper('%"+parametros.get("apellidoPersona")+"%') or upper(per.segundo_apellido) like upper('%"+parametros.get("apellidoPersona")+"%') )";
				}
			}
			else if((parametros.get("tipoBusqueda")+"").equals("DEU"))
			{
				if(!UtilidadTexto.isEmpty(parametros.get("tipoId")))
				{
					whereStr+=" and deu.tipo_identificacion='"+parametros.get("tipoId")+"'";
				}
				if(!UtilidadTexto.isEmpty(parametros.get("numeroId")))
				{
					whereStr+=" and deu.numero_identificacion='"+parametros.get("numeroId")+"'";
				}
				if(!UtilidadTexto.isEmpty(parametros.get("nombrePersona")))
				{
					whereStr+=" and (upper(deu.primer_nombre) like upper('%"+parametros.get("nombrePersona")+"%') or upper(deu.segundo_nombre) like upper('%"+parametros.get("nombrePersona")+"%') )";
				}
				if(!UtilidadTexto.isEmpty(parametros.get("apellidoPersona")))
				{
					whereStr+=" and (upper(deu.primer_apellido) like upper('%"+parametros.get("apellidoPersona")+"%') or upper(deu.segundo_apellido) like upper('%"+parametros.get("apellidoPersona")+"%') )";
				}
			}
			
		}
		
		//variables de facutra
		if(!UtilidadTexto.isEmpty(parametros.get("convenio")))
		{
			whereStr+=" and fac.convenio="+parametros.get("convenio")+"";
		}
		if(!UtilidadTexto.isEmpty(parametros.get("docInicial")))
		{
			if(!UtilidadTexto.isEmpty(parametros.get("docFinal")))
			{
				whereStr+=" and fac.consecutivo_factura between "+parametros.get("docInicial")+" and "+parametros.get("docFinal");
			}
			else
			{
				whereStr+=" and fac.consecutivo_factura between "+parametros.get("docInicial")+" and "+parametros.get("docInicial");
			}
		}
		return (consultaSaldosIniciales+whereStr);
	}

	/**
	 * metodo para insertar y actualizar la informacion de la persona y del paciente.
	 * @param paciente
	 * @return
	 */
	public static int insertarActualizarPersona(Connection con,DtoPaciente paciente) 
	{
		int resultado=ConstantesBD.codigoNuncaValido;
		
		logger.info("Paciente: "+paciente.getCodigo());
		try
		{
			if(paciente.getCodigo()<=0)//insertar
			{
				logger.info("insertando");
				int  codPAciente=UtilidadBD.obtenerSiguienteValorSecuencia(con, "administracion.seq_personas");
				PreparedStatementDecorator ps=new PreparedStatementDecorator(con,insertarPersona);
				ps.setInt(1, codPAciente);
				ps.setString(2, paciente.getTipoId());
				ps.setString(3, paciente.getNumeroId());
				ps.setString(4, paciente.getPrimerNombre());
				if(UtilidadTexto.isEmpty(paciente.getSegundoNombre()))
					ps.setObject(5,null);
				else
					ps.setString(5, paciente.getSegundoNombre());
				ps.setString(6, paciente.getSegundoNombre());
				if(UtilidadTexto.isEmpty(paciente.getSegundoApellido()))
					ps.setObject(7,null);
				else
					ps.setString(7, paciente.getSegundoApellido());
				ps.setString(8, paciente.getDireccion());
				ps.setString(9, paciente.getTelefono());
				logger.info(ps.toString());
				//si se inserto la persona, se inserta el paciente.
				if(ps.executeUpdate()>0)
				{
					PreparedStatementDecorator psInterno=new PreparedStatementDecorator(con,insertarPaciente);
					psInterno.setInt(1, codPAciente);
					psInterno.setString(2, ValoresPorDefecto.getValorTrueParaConsultas());
					if(psInterno.executeUpdate()>0)
					{
						resultado=codPAciente;
					}
					logger.info(psInterno.toString());
					logger.info("INSERTO PACIENTE -->"+resultado);
				}
			}
			else
			{
				logger.info("ACTUALIZANDO PACIENTE");
				PreparedStatementDecorator ps=new PreparedStatementDecorator(con,actualizarPersona);
				ps.setString(1, paciente.getDireccion());
				ps.setString(2, paciente. getTelefono());
				ps.setInt(3, paciente.getCodigo());
				if(ps.executeUpdate()>0)
				{
					resultado=paciente.getCodigo();
				}
				logger.info("ACTUALIZA PACIENTE -->"+resultado);
			}
		}
		catch(SQLException e)
		{
			logger.error("ERROR: ",e);
		}
		return resultado;
	}


	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static DtoDatosFinanciacion cargarDatosFinanciacion(Connection con, DtoDatosFinanciacion dto) {
		HashMap mapa = new HashMap<String, Object>();
		String codigoDatosFinanciacion="";
		String sql = 	"SELECT " +
							"getintegridaddominio(dg.tipo_documento) as tipo_documento, " +
							"df.consecutivo as numero_documento," +
							"to_char(dg.fecha_documento, 'DD/MM/YYYY') as fecha_documento, " +
							"dg.valor as valor_documento, " +
							"df.consecutivo_factura as consecutivo_factura, " +
							"df.fecha_elaboracion_factura as fecha_elaboracion_factura," +
							"df.nro_coutas as nro_cuotas," +
							"df.observaciones, " +
							"df.codigo_pk " +
						"FROM " +
							"carterapaciente.deudores_datos_finan ddf " +
						"INNER JOIN " +
							"carterapaciente.datos_financiacion df ON (df.codigo_pk=ddf.datos_financiacion) " +
						"INNER JOIN " +
							"carterapaciente.documentos_garantia dg ON (dg.codigo_pk=df.codigo_pk_docgarantia) " +
						"WHERE " +
							"ddf.codigo_pk_deudor="+dto.getDeudor().getCodigoPK();
		logger.info("Consulta: "+sql);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),false,true);
			
			Utilidades.imprimirMapa(mapa);
			if (Utilidades.convertirAEntero(mapa.get("numRegistros")+"") == 1){
				dto.setTipoDocumento(mapa.get("tipoDocumento")+"");
				dto.setConsecutivo(mapa.get("numeroDocumento")+"");
				dto.setFechaInicio(mapa.get("fechaDocumento")+"");
				dto.setValorTotalDocumento(Utilidades.convertirADouble(mapa.get("valorDocumento")+""));
				dto.setConsecutivoFactura(mapa.get("consecutivoFactura")+"");
				dto.setFechaElaboracionFactura(UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechaElaboracionFactura")+""));
				dto.setNroCoutas(Utilidades.convertirAEntero(mapa.get("nroCuotas")+""));
				dto.setObservaciones(mapa.get("observaciones")+"");
				dto.setCodigo(Utilidades.convertirAEntero(mapa.get("codigoPk")+""));
			}
			ps.close();
		} catch (Exception e) {
			logger.error("ERROR", e);
		}
		
		//Consultar Cuotas de financiacion
		mapa = new HashMap<String, Object>();
		sql = 	"SELECT " +
					"cdf.nro_cuota as nro_cuota, " +
					"cdf.numero_documento as numero_documento, " +
					"cdf.fecha_modifica as fecha_modifica, " +
					"cdf.valor_couta as valor_cuota " +
				"FROM " +
					"carterapaciente.cuotas_datos_financiacion cdf " +
				"WHERE " +
					"cdf.codigo_pk="+dto.getCodigo()+" " +
				"ORDER BY cdf.nro_cuota ";
		
		logger.info("Consulta: "+sql);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,true);
			DtoCuotasDatosFinanciacion dtoCuota = new DtoCuotasDatosFinanciacion();
			
			Utilidades.imprimirMapa(mapa);
			int numRegistros=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");
			for(int i=0; i<numRegistros; i++){
				dtoCuota = new DtoCuotasDatosFinanciacion();
				dtoCuota.setNroCuota(Utilidades.convertirAEntero(mapa.get("nroCuota_"+i)+""));
				dtoCuota.setNumeroDocumento(mapa.get("numeroDocumento_"+i)+"");
				dtoCuota.setFechaVigencia(UtilidadFecha.conversionFormatoFechaAAp(mapa.get("fechaModifica_"+i)+""));
				dtoCuota.setValorCuota(new BigDecimal(mapa.get("valorCuota_"+i).toString()));
				dtoCuota.setUsuarioModifica(dto.getUsuarioModifica());
				dto.getCuotasDatosFinan().add(dtoCuota);
			}
			ps.close();
		} catch (Exception e) {
			logger.error("ERROR", e);
		}
		return dto;
	}

	/**
	 * 
	 * @param con
	 * @param dto
	 * @return
	 */
	public static boolean actualizarDatosFinanciacion(Connection con, DtoDatosFinanciacion dto) {
		boolean exito=true;
		
		String sql = "UPDATE " +
							"carterapaciente.datos_financiacion " +
						"SET " +
							"consecutivo='"+dto.getConsecutivoFactura()+"', "+
							"fecha_elaboracion_factura='"+UtilidadFecha.conversionFormatoFechaABD(dto.getFechaElaboracionFactura())+"', " +
							"observaciones='"+dto.getObservaciones()+"' "+
						"WHERE " +
							"codigo_pk="+dto.getCodigo();
		
		logger.info("Consulta: "+sql);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			if(ps.executeUpdate()<=0)
				exito=false;
		} catch (Exception e) {
			logger.error("ERROR", e);
		}
		
		sql = "DELETE FROM cuotas_datos_financiacion WHERE dato_financiacion="+dto.getCodigo();
		logger.info("Consulta: "+sql);
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(sql,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			if(ps.executeUpdate()<=0)
				exito=false;
		} catch (Exception e) {
			logger.error("ERROR", e);
		}
		
		if (!SqlBaseDatosFinanciacionDao.insertCuotasDatosFinanciacion(con, dto.getCuotasDatosFinan(), Utilidades.convertirAEntero(dto.getCodigo()+"")))
			exito=false;
			
		return exito;
	}

}
