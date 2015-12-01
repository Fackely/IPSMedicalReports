package com.princetonsa.dao.sqlbase.carteraPaciente;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.carterapaciente.PazYSalvoCarteraPacienteDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoPazYSalvoCarteraPaciente;
import com.princetonsa.mundo.PersonaBasica;


public class SqlBasePazYSalvoCarteraPaciente{
	
	/**
	 * Para el manejo de logs
	 */
	private static Logger logger = Logger.getLogger(SqlBasePazYSalvoCarteraPaciente.class);
	
	/**
	 * Consulta los tipos de identificacion
	 */
	private static String consultarTiposId="SELECT acronimo, nombre, cod_interfaz AS codinterfaz " +
											"FROM administracion.tipos_identificacion ";
	
	/**
	 * Consulta los documentos por deudor segun criterios ingresados
	 */
	private static String consultarDocPorDeudor="SELECT DISTINCT dg.ingreso, "+
													"dg.tipo_documento AS tipodoc, "+
													"dg.consecutivo, "+
													"dg.anio_consecutivo AS aniocons, "+
													"dg.codigo_paciente AS codpaciente, "+
													"carterapaciente.getnomdeudoringreso(d.ingreso) AS deudor, "+
													"dg.valor AS valordoc, "+
													"dg.estado, " +
													"df.codigo_pk AS datosfin, " +
													"pys.codigo_pk AS codigopkpazysalvo, " +
													"pys.consecutivo AS consecutivopys, " +
													"pys.anio_consecutivo AS anioconspys " +
												"FROM carterapaciente.deudorco d " +
												"INNER JOIN carterapaciente.deudores_datos_finan ddf ON(ddf.codigo_pk_deudor=d.codigo_pk) " +
												"INNER JOIN carterapaciente.datos_financiacion df ON(ddf.datos_financiacion=df.codigo_pk) "+
												"INNER JOIN carterapaciente.documentos_garantia dg ON(df.codigo_pk_docgarantia=dg.codigo_pk) " +
												"LEFT OUTER JOIN carterapaciente.paz_y_salvo pys ON(pys.datos_financiacion=df.codigo_pk) ";

	private static String insertarPazYSalvo="INSERT INTO carterapaciente.paz_y_salvo " +
											"(codigo_pk, consecutivo, anio_consecutivo, " +
											"datos_financiacion, fecha_modifica, hora_modifica, " +
											"usuario_modifica) " +
											"VALUES (?,?,?,?,CURRENT_DATE,?,?) ";
	
	private static String consultarPazYSalvo="SELECT ps.codigo_pk AS codigo, " +
												"ps.consecutivo AS conspys, "+
												"ps.fecha_modifica AS fecha, "+ 
												"ps.hora_modifica AS hora, " +
												"dg.tipo_documento AS tipodocumento, "+ 
												"dg.consecutivo AS codgarantia, "+ 
												"dg.valor, "+ 
												"carterapaciente.getnomdeudoringreso(d.ingreso) AS deudor, "+ 
												"d.numero_identificacion AS iddeudor, "+ 
												"administracion.getnombrepersona(p.codigo) AS paciente, "+ 
												"p.numero_identificacion AS idpaciente, "+ 
												"f.consecutivo_factura AS consecutivofactura "+
												"FROM carterapaciente.paz_y_salvo ps "+
												"INNER JOIN carterapaciente.datos_financiacion df ON(ps.datos_financiacion=df.codigo_pk) "+
												"INNER JOIN carterapaciente.documentos_garantia dg ON(df.codigo_pk_docgarantia=dg.codigo_pk) "+
												"INNER JOIN carterapaciente.deudores_datos_finan ddf ON(ddf.datos_financiacion=df.codigo_pk) "+
												"INNER JOIN carterapaciente.deudorco d ON(d.codigo_pk=ddf.codigo_pk_deudor) "+
												"INNER JOIN personas p ON(d.codigo_paciente=p.codigo) "+
												"INNER JOIN facturas f ON(df.codigo_factura=f.codigo) " +
												"WHERE ps.codigo_pk=?";
	
	private static String actualizarDocumentoGarantia="UPDATE carterapaciente.documentos_garantia SET estado='"+ConstantesIntegridadDominio.acronimoEstadoEntregado+"' " +
														"WHERE (ingreso=? AND consecutivo=? " +
														"AND anio_consecutivo= "+ConstantesBD.separadorSplit+" "+
														"AND tipo_documento=?)";
	

	public static boolean actualizarDocumentoGarantia(int ingreso,String consecutivo, String anioConsecutivo, String tipoDocumento) 
	{
		Connection con;		
		con= UtilidadBD.abrirConexion();
		String cadena = "";
		
		try {
			
			cadena = actualizarDocumentoGarantia;
			if(anioConsecutivo.equals(""))
				cadena = cadena.replace(ConstantesBD.separadorSplit ,"' '");
			else
				cadena = cadena.replace(ConstantesBD.separadorSplit ,anioConsecutivo);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadena, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));
						
			logger.info("\n\nquery:: "+actualizarDocumentoGarantia+"  ingreso: "+ingreso+"  conse: "+consecutivo+"  anio conse: "+anioConsecutivo+" tipo doc: "+tipoDocumento);
			
			ps.setInt(1,ingreso);
			ps.setString(2,consecutivo);
			ps.setString(3,tipoDocumento);		

			if(ps.executeUpdate()>0)
			{
				ps.close();
				UtilidadBD.cerrarConexion(con);
				return true;
			}
			ps.close();
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e) 
		{	
			logger.info("ERROR ACTUALIZANDO DOCUMENTO DE GARANTIA "+e);		
		}
		
		return false;
	}
	
	/**
	 * Consulta el paz y salvo 
	 */
	public static DtoPazYSalvoCarteraPaciente consultarPazYSalvo(int codigopk)
	{		
		DtoPazYSalvoCarteraPaciente dto= new DtoPazYSalvoCarteraPaciente();
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
	
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarPazYSalvo, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ps.setInt(1, codigopk);
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
						
			while(rs.next())
			{				
				dto.setConsecutivo(rs.getString("conspys"));
				dto.setCodigoPk(rs.getString("codigo"));
				dto.setFechaModifica(rs.getString("fecha"));
				dto.setHoraModifica(rs.getString("hora"));
				dto.getDocumentosGarantia().setConsecutivo(rs.getString("codgarantia"));				
				dto.getDocumentosGarantia().setValor(rs.getString("valor"));
				dto.getDatosFinanciacion().setNomDeudor(rs.getString("deudor"));
				dto.getDatosFinanciacion().setIdDeudor(rs.getString("iddeudor"));	
				dto.getDocumentosGarantia().setNomPaciente(rs.getString("paciente"));
				dto.getDocumentosGarantia().setIdPaciente(rs.getString("idpaciente"));
			}
		}
		catch (Exception e) {
			logger.info("error en consultar Paz y Salvo >> "+e+" "+consultarPazYSalvo);
		}
		return dto;
	}
	
	
	public static int insertarPazYSalvo(DtoPazYSalvoCarteraPaciente dto, String usuario) 
	{
		
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(insertarPazYSalvo, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
				int seq=UtilidadBD.obtenerSiguienteValorSecuencia(con, "carterapaciente.seq_paz_y_salvo");
				ps.setInt(1,seq);
				ps.setString(2,dto.getConsecutivo());
				ps.setString(3,dto.getAnioConsecutivo());
				ps.setInt(4,dto.getDatosFinanciacion().getCodigoPk());
				ps.setString(5,UtilidadFecha.getHoraActual());
				ps.setString(6, usuario);
				
				logger.info("\n\nquery::: "+insertarPazYSalvo+" seq: "+seq+" consec: "+dto.getDocumentosGarantia().getConsecutivo()+" anioC:"+dto.getDocumentosGarantia().getAnioConsecutivo()+"datos fin: "+dto.getDatosFinanciacion().getCodigoPk());
				
				if(ps.executeUpdate()>0)
				{	
					ps.close();
					UtilidadBD.cerrarConexion(con);
					return seq;
				}
				ps.close();
								
			}
			catch (SQLException e) 
			{	
				logger.info("ERROR AL INGRESAR PAZ Y SALVO---> "+e);
			}		
		return 0;
	}
	
	/**
	 * Consulta los documentos por deudor segun criterios ingresados 
	 */
	public static ArrayList<DtoPazYSalvoCarteraPaciente> consultarDocPorDeudor(HashMap criterios)
	{
		ArrayList<DtoPazYSalvoCarteraPaciente> array = new ArrayList<DtoPazYSalvoCarteraPaciente>();
		Connection con;		
		con= UtilidadBD.abrirConexion();

		String consultaWhere=consultarDocPorDeudor;
		
		if(!(criterios.get("tipoIdPaciente")+"").equals("-1") || !(criterios.get("numIdPaciente")+"").equals("") || !(criterios.get("nombrePaciente")+"").equals("") || !(criterios.get("apellidoPaciente")+"").equals(""))
			consultaWhere+="INNER JOIN personas p ON(dg.codigo_paciente=p.codigo) ";
		if(!(criterios.get("numFactura")+"").equals(""))
			consultaWhere+="INNER JOIN facturas f ON(df.codigo_factura=f.codigo) ";
		
		String consulta="WHERE 1=1 AND d.clase_deudorco='"+ConstantesIntegridadDominio.acronimoDeudor+"' ";
				
		if(!(criterios.get("tipoIdDeudor")+"").equals("-1"))
			consulta+= "AND d.tipo_identificacion='"+criterios.get("tipoIdDeudor")+"' ";
		if(!(criterios.get("numIdDeudor")+"").equals(""))
			consulta+="AND d.numero_identificacion='"+criterios.get("numIdDeudor")+"' ";
		if(!(criterios.get("nombreDeudor")+"").equals(""))
			consulta+="AND d.primer_nombre='"+criterios.get("nombreDeudor")+"' ";
		if(!(criterios.get("apellidoDeudor")+"").equals(""))
			consulta+="AND d.primer_apellido='"+criterios.get("apellidoDeudor")+"' ";
		if(!(criterios.get("tipoIdPaciente")+"").equals("-1"))
			consulta+="AND p.tipo_identificacion='"+criterios.get("tipoIdPaciente")+"' ";
		if(!(criterios.get("numIdPaciente")+"").equals(""))
			consulta+="AND p.numero_identificacion='"+criterios.get("numIdPaciente")+"' ";
		if(!(criterios.get("nombrePaciente")+"").equals(""))
			consulta+="AND p.primer_nombre='"+criterios.get("nombrePaciente")+"' ";
		if(!(criterios.get("apellidoPaciente")+"").equals(""))
			consulta+="AND p.primer_apellido='"+criterios.get("apellidoPaciente")+"' ";
		if(!(criterios.get("codGarantia")+"").equals(""))
			consulta+="AND dg.numero_documento='"+criterios.get("codGarantia")+"' ";
		if((criterios.get("estadoGarantia")+"").equals("-1"))
			consulta+="AND (dg.estado='"+ConstantesIntegridadDominio.acronimoEstadoEntregado+"' OR dg.estado='"+ConstantesIntegridadDominio.acronimoEstadoCancelado+"')";
		else	
			consulta+="AND dg.estado='"+criterios.get("estadoGarantia")+"' ";
		if(!(criterios.get("numFactura")+"").equals(""))
			consulta+="AND f.consecutivo_factura='"+criterios.get("numFactura")+"' ";
		
		
		consultaWhere+=consulta;
		
		logger.info("\n\nconsulta:: "+consultaWhere+" criterios: "+criterios);
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaWhere, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
					
			while(rs.next())
			{
				DtoPazYSalvoCarteraPaciente dto= new DtoPazYSalvoCarteraPaciente();
				dto.getDocumentosGarantia().setTipoDocumento(rs.getString("tipodoc"));
				dto.getDocumentosGarantia().setValor(rs.getString("valordoc"));
				dto.getDocumentosGarantia().setEstado(rs.getString("estado"));
				dto.getDatosFinanciacion().setNomDeudor(rs.getString("deudor"));
				dto.getDocumentosGarantia().setIngreso(rs.getInt("ingreso"));
				dto.getDocumentosGarantia().setConsecutivo(rs.getString("consecutivo"));
				dto.getDocumentosGarantia().setAnioConsecutivo(rs.getString("aniocons"));
				dto.getDatosFinanciacion().setCodigoPk(rs.getInt("datosfin"));
				dto.setCodigoPk(rs.getString("codigopkpazysalvo"));
				dto.setConsecutivo(rs.getString("consecutivopys"));
				dto.setAnioConsecutivo(rs.getString("anioconspys"));
				array.add(dto);				
			}
		}
		catch (Exception e) {
			logger.info("error en consultar Doc por deudor >> "+e+" "+consultarDocPorDeudor);
		}
		return array;
	}
		
	
	/**
	 * Consulta los tipod de identificacion
	 */
	public static ArrayList<HashMap> consultarTiposId()
	{
		ArrayList<HashMap> array = new ArrayList<HashMap>();
		Connection con;		
		con= UtilidadBD.abrirConexion();
		
		try {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultarTiposId, ConstantesBD.typeResultSet, ConstantesBD.concurrencyResultSet));		
			ResultSetDecorator rs = new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				HashMap mapa = new HashMap();
				mapa.put("acronimo",rs.getString("acronimo"));
				mapa.put("nombre",rs.getString("nombre"));
				mapa.put("codinterfaz",rs.getString("codinterfaz"));
				array.add(mapa);
			}
		}
		catch (Exception e) {
			logger.info("error en consultar Tipos Id >> "+e+" "+consultarTiposId);
		}
		return array;
	}
}