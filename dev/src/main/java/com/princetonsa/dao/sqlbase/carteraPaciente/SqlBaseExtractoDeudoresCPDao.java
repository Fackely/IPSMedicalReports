package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.Utilidades;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoExtractosDeudoresCP;
import com.princetonsa.dto.odontologia.DtoPrograma;

public class SqlBaseExtractoDeudoresCPDao
{
	private static Logger logger = Logger.getLogger(SqlBaseExtractoDeudoresCPDao.class);
	
	private static String strConsultaDatosDeudor	=	"SELECT DISTINCT " +
																	"d.institucion," +
																	"d.tipo_identificacion AS tipoiddeudor," +
																	"d.numero_identificacion AS numiddeudor, " +
																	"d.primer_nombre AS primernomdeudor," +
																	"d.segundo_nombre AS segundonomdeudor," +
																	"d.primer_apellido AS primerapedeudor," +
																	"d.segundo_apellido AS segundoapedeudor," +
																	"d.ingreso AS ingresodeudor " +
																"FROM " +
																	"deudorco d " +
																"INNER JOIN " +
																	"personas ps ON (d.codigo_paciente=ps.codigo) " +
																"INNER JOIN " +
																	"ingresos i ON (i.id=d.ingreso) " +
																"INNER JOIN " +
																	"centro_atencion ca ON (ca.consecutivo=i.centro_atencion) " +
																"INNER JOIN " +
																	"deudores_datos_finan ddf ON (ddf.codigo_pk_deudor=d.codigo_pk) " +
																"INNER JOIN " +
																	"datos_financiacion df ON (df.codigo_pk=ddf.datos_financiacion) " +
																"INNER JOIN " +
																	"documentos_garantia dg ON (dg.codigo_pk=df.codigo_pk_docgarantia) " +
																"INNER JOIN " +
																	"facturas f ON (f.codigo=df.codigo_factura) ";
	
	private static String strConsultaExtractosDeudor	=		"SELECT DISTINCT " +
																 " d.institucion                			   ,"+
																 " d.tipo_identificacion    AS tipoiddeudor    ,"+
																 " d.numero_identificacion  AS numiddeudor     ,"+
																 " coalesce(d.primer_nombre,'')          AS primernomdeudor ,"+
																 " coalesce(d.segundo_nombre,'')         AS segundonomdeudor,"+
																 " coalesce(d.primer_apellido,'')        AS primerapedeudor ,"+
																 " coalesce(d.segundo_apellido,'')       AS segundoapedeudor,"+
																 " d.ingreso                AS ingresodeudor   ,"+
																 " ps.tipo_identificacion   AS tipoidpac       ,"+
																 " ps.numero_identificacion AS numidpac        ,"+
																 " coalesce(ps.primer_nombre,'')         AS primernompac    ,"+
																 " coalesce(ps.segundo_nombre,'')        AS segundonompac   ,"+
																 " coalesce(ps.primer_apellido,'')       AS primerapepac    ,"+
																 " coalesce(ps.segundo_apellido,'')      AS segundoapepac   ,"+
																 " ca.consecutivo           AS centroatencion  ," +
																 " ca.descripcion           AS nombrecentro    ,"+
																 " df.codigo_pk             AS codigodatofin   ,"+
																 " df.codigo_factura        AS codigofac       ,"+
																 " f.consecutivo_factura    AS consecutivofac  ,"+
																 " dg.ingreso               AS ingresodg       ,"+
																 " dg.consecutivo           AS consecutivodg   ,"+
																 " dg.anio_consecutivo      AS aniodg          ,"+
																 " getintegridaddominio(dg.tipo_documento)        AS tipodocdg,"+
																 " dg.estado                AS estadodg		   ,"+
																 " dg.valor AS valordocgarantia                ,"+
																 " to_char(dg.fecha_generacion,'dd/mm/yyyy') AS fechamovdoc,"+
																 " ap.valor AS valoraplicacion,"+
																 " to_char(ap.fecha_aplicacion,'dd/mm/yyyy') AS fechaplicacion," +
																 " ap.consecutivo AS consecutivoap," +
																 " ap.numero_documento AS rc "+
																 " FROM deudorco d "+
																 " INNER JOIN personas ps ON (d.codigo_paciente=ps.codigo) "+
																 " INNER JOIN ingresos i ON (i.id=d.ingreso) "+
																 " INNER JOIN centro_atencion ca ON (ca.consecutivo=i.centro_atencion) "+
																 " INNER JOIN deudores_datos_finan ddf ON (d.codigo_pk=ddf.codigo_pk_deudor) "+
																 " INNER JOIN datos_financiacion df ON (df.codigo_pk=ddf.datos_financiacion) "+
																 " INNER JOIN documentos_garantia dg ON (dg.codigo_pk=df.codigo_pk_docgarantia) "+
																 " INNER JOIN facturas f ON (f.codigo  =df.codigo_factura) "+
																 " INNER JOIN aplicac_pagos_cartera_pac ap ON (ap.datos_financiacion=df.codigo_pk) ";
																 
																 
		
	public static ArrayList<DtoExtractosDeudoresCP> consultaExtractosDeudor (DtoDeudor dto)
	{
		Connection con = UtilidadBD.abrirConexion();
		ArrayList<DtoExtractosDeudoresCP> listadoExtractos= new ArrayList<DtoExtractosDeudoresCP>();
		
		String consulta=strConsultaExtractosDeudor;
		
		consulta+="	WHERE ";
		
		if (!dto.getNumeroIdentificacionDeu().equals(""))
			consulta+=" d.numero_identificacion='"+dto.getNumeroIdentificacionDeu()+"' AND ";
		
		if (!dto.getDtoDocsGarantia().getConsecutivo().equals(""))
			consulta+=	" dg.estado='"+dto.getDtoDocsGarantia().getEstado()+"' AND ";
		else
			consulta+=	" dg.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoEntregado+"','"+ConstantesIntegridadDominio.acronimoEstadoCancelado+"') AND ";
		
		consulta+=" 1=1";
		
		consulta+=" ORDER BY f.consecutivo_factura,d.numero_identificacion, dg.consecutivo ";
		
		
		logger.info("LA CONSULTA----->"+consulta);
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoExtractosDeudoresCP dtoCta = new DtoExtractosDeudoresCP();
				dtoCta.getDtoDeudor().setTipoIdentificacionDeu(rs.getString("tipoiddeudor"));
				dtoCta.getDtoDeudor().setNumeroIdentificacionDeu(rs.getString("numiddeudor"));
				dtoCta.getDtoDeudor().setPrimerNombreDeu(rs.getString("primernomdeudor"));
				dtoCta.getDtoDeudor().setSegundoNombreDeu(rs.getString("primerapedeudor"));
				dtoCta.getDtoDeudor().setSegundoApellidoDeu(rs.getString("segundoapedeudor"));
				
				dtoCta.getDtoPersonas().setTipoId(rs.getString("tipoidpac"));
				dtoCta.getDtoPersonas().setNumeroId(rs.getString("numidpac"));
				dtoCta.getDtoPersonas().setPrimerNombre(rs.getString("primernompac"));
				dtoCta.getDtoPersonas().setSegundoNombre(rs.getString("segundonompac"));
				dtoCta.getDtoPersonas().setPrimerApellido(rs.getString("primerapepac"));
				dtoCta.getDtoPersonas().setSegundoApellido(rs.getString("segundoapepac"));
				
				dtoCta.getDtoDeudor().setCentroAtencion(rs.getInt("centroatencion"));
				dtoCta.getDtoDeudor().setNombreCentroAtencion(rs.getString("nombrecentro"));
				dtoCta.getDtoDatosFin().setCodigo(rs.getDouble("codigodatofin"));
				dtoCta.getDtoDeudor().setFactura(rs.getString("consecutivofac"));
				
				dtoCta.getDtoDocsGarantia().setIngreso(rs.getInt("ingresodg"));
				dtoCta.getDtoDocsGarantia().setConsecutivo(rs.getString("consecutivodg"));
				dtoCta.getDtoDocsGarantia().setAnioConsecutivo(rs.getString("aniodg"));
				dtoCta.getDtoDocsGarantia().setTipoDocumento(rs.getString("tipodocdg"));
				dtoCta.getDtoDocsGarantia().setEstado(rs.getString("estadodg"));
				dtoCta.getDtoDocsGarantia().setValorDoc(rs.getBigDecimal("valordocgarantia"));
				dtoCta.getDtoDocsGarantia().setFechaDoc(rs.getString("fechamovdoc"));
				
				dtoCta.getDtoAplicacion().setValorAplicacion(rs.getBigDecimal("valoraplicacion"));
				dtoCta.getDtoAplicacion().setFechaAplicacion(rs.getString("fechaplicacion"));
				dtoCta.getDtoAplicacion().setConsecutivo(rs.getDouble("consecutivoap"));
				dtoCta.getDtoAplicacion().setNumeroDocumento(rs.getString("rc"));
				
				listadoExtractos.add(dtoCta);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultaExtractosDeudor==> "+e);
		}
		
		return listadoExtractos;
	}
	
	public static ArrayList<DtoExtractosDeudoresCP> consultaDatosDeudor (DtoDeudor dto)
	{
		Connection con = UtilidadBD.abrirConexion();
		ArrayList<DtoExtractosDeudoresCP> listadoExtractos= new ArrayList<DtoExtractosDeudoresCP>();
		
		String consulta=strConsultaDatosDeudor;
		
		consulta+=" WHERE ";
		
		if (dto.getCentroAtencion()!=ConstantesBD.codigoNuncaValido)
			consulta+=	" ca.consecutivo="+dto.getCentroAtencion()+" AND ";
		
		//Validaciones Deudor
		if (!dto.getTipoIdentificacionDeu().equals(""))
			consulta+=	" d.tipo_identificacion='"+dto.getTipoIdentificacionDeu()+"' AND ";
		
		if (!dto.getNumeroIdentificacionDeu().equals(""))
			consulta+=	" d.numero_identificacion='"+dto.getNumeroIdentificacionDeu()+"' AND ";
		
		if (!dto.getPrimerNombreDeu().equals(""))
			consulta+=	" d.primer_nombre like('%"+dto.getPrimerNombreDeu()+"%)' AND ";
		
		if (!dto.getPrimerApellidoDeu().equals(""))
			consulta+=	" d.primer_apellido like('%"+dto.getPrimerApellidoDeu()+"%)' AND ";
		
		//Validaciones Paciente
		if (!dto.getTipoIdentificacionPac().equals(""))
			consulta+=	" ps.tipo_identificacion='"+dto.getTipoIdentificacionPac()+"' AND ";
		
		if (!dto.getNumeroIdentificacionPac().equals(""))
			consulta+=	" ps.numero_identificacion='"+dto.getNumeroIdentificacionPac()+"' AND ";
		
		if (!dto.getPrimerNombrePac().equals(""))
			consulta+=	" ps.primer_nombre like('%"+dto.getPrimerNombrePac()+"%)' AND ";
		
		if (!dto.getPrimerApellidoPac().equals(""))
			consulta+=	" ps.primer_apellido like('%"+dto.getPrimerApellidoPac()+"%)' AND ";
		
		//Validaciones doc garantia y factura
		if (!dto.getDtoDocsGarantia().getConsecutivo().equals(""))
			consulta+=	" dg.consecutivo='"+dto.getDtoDocsGarantia().getConsecutivo()+"' AND ";
		
		if (!dto.getDtoDocsGarantia().getConsecutivo().equals(""))
			consulta+=	" dg.estado='"+dto.getDtoDocsGarantia().getEstado()+"' AND ";
		else
			consulta+=	" dg.estado IN ('"+ConstantesIntegridadDominio.acronimoEstadoEntregado+"','"+ConstantesIntegridadDominio.acronimoEstadoCancelado+"') AND ";
		
		if (!dto.getFactura().equals(""))
			consulta+=	" f.consecutivo_factura='"+dto.getFactura()+"' AND ";
		
			
		consulta+=	"1=1";
		
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoExtractosDeudoresCP dtoCta = new DtoExtractosDeudoresCP();
				dtoCta.getDtoDeudor().setTipoIdentificacionDeu(rs.getString("tipoiddeudor"));
				dtoCta.getDtoDeudor().setNumeroIdentificacionDeu(rs.getString("numiddeudor"));
				dtoCta.getDtoDeudor().setPrimerNombreDeu(rs.getString("primernomdeudor"));
				dtoCta.getDtoDeudor().setSegundoNombreDeu(rs.getString("primerapedeudor"));
				dtoCta.getDtoDeudor().setSegundoApellidoDeu(rs.getString("segundoapedeudor"));
				listadoExtractos.add(dtoCta);
			 }
			SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultaDatosPacienteDeudor==> "+e);
		}
		return listadoExtractos;
	}

}
