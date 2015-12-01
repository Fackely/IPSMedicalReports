package com.princetonsa.dao.sqlbase.carteraPaciente;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoDocumentosGarantia;
import com.princetonsa.dto.carteraPaciente.DtoExtractosDeudoresCP;
import com.princetonsa.dto.carteraPaciente.DtoReporteDocumentosCarteraPaciente;

public class SqlBaseReporteDocumentosCarteraPacienteDao
{
	private static Logger logger = Logger.getLogger(SqlBaseReporteDocumentosCarteraPacienteDao.class);
	
	private static String consultarDocumentos	=	"SELECT " +
														"dg.ingreso AS ingresodocumento," +
														"dg.consecutivo AS codigogarantia," +
														"dg.anio_consecutivo AS aniodocumento," +
														"getintegridaddominio(dg.tipo_documento) AS tipodocumento," +
														"to_char(dg.fecha_generacion,'dd/mm/yyyy') AS fechagendocumento," +
														"coalesce(dg.valor,0) AS valordocumento," +
														"getintegridaddominio(dg.estado) AS estado, " +
														"f.consecutivo_factura AS consecutivofactura," +
														"d.tipo_identificacion AS tipoiddeudor," +
														"d.numero_identificacion AS numiddeudor, " +
														"coalesce(d.primer_nombre,' ') AS primernomdeudor," +
														"coalesce(d.segundo_nombre,' ') AS segundonomdeudor," +
														"coalesce(d.primer_apellido,' ') AS primerapedeudor," +
														"coalesce(d.segundo_apellido,' ') AS segundoapedeudor," +
														"getnombrepersona(d.codigo_paciente) AS nombrepaciente, " +
														"d.telefono_reside AS telefonodeudor," +
														"d.direccion_reside AS direcciondeudor," +
														"p.tipo_identificacion AS tipoidpaciente," +
														"p.numero_identificacion AS numidpaciente," +
														"dcrc.numero_recibo_caja AS nrorc, " +
														"(df.dias_por_couta*df.nro_coutas) AS diasvigenciadocumento," +
														"to_char((dg.fecha_generacion+(df.dias_por_couta * df.nro_coutas)),'dd/mm/yyyy') AS fechavencimiento,  " +
														"coalesce(dg.valor-coalesce((" +
														"SELECT " +
															"SUM(apcp.valor)" +
														"FROM " +
															"carterapaciente.aplicac_pagos_cartera_pac apcp " +
														"INNER JOIN " +
															"carterapaciente.datos_financiacion df ON(df.codigo_pk =apcp.datos_financiacion) " +
														"INNER JOIN " +
															"carterapaciente.deudores_datos_finan ddf ON(ddf.datos_financiacion=df.codigo_pk) " +
														"WHERE " +
															"ddf.codigo_pk_deudor=d.codigo_pk" +
														"),0),0) AS saldodocumento," +
														"i.centro_atencion AS codigocentroatencion," +
														"ca.descripcion AS nombrecentroatencion " +
													"FROM " +
														"carterapaciente.deudorco d " +
													"INNER JOIN " +
														"administracion.personas p ON (p.codigo=d.codigo_paciente) " +
													"INNER JOIN " +
														"manejopaciente.ingresos i ON (i.id=d.ingreso) " +
													"INNER JOIN " +
														"centro_atencion ca ON (ca.consecutivo=i.centro_atencion) " +
													"INNER JOIN " +
														"carterapaciente.deudores_datos_finan ddf ON (ddf.codigo_pk_deudor=d.codigo_pk) " +
													"INNER JOIN " +
														"carterapaciente.datos_financiacion df ON (df.codigo_pk=ddf.datos_financiacion) " +
													"INNER JOIN " +
														"carterapaciente.documentos_garantia dg ON (dg.codigo_pk=df.codigo_pk_docgarantia) " +
													"INNER JOIN " +
														"facturacion.facturas f ON (f.codigo=df.codigo_factura) " +
													"INNER JOIN " +
														"tesoreria.detalle_conceptos_rc dcrc ON (dcrc.ingreso=d.ingreso AND dcrc.inst_deudor=d.institucion AND dcrc.clase_deudorco=d.clase_deudorco AND dcrc.num_id_deudorco=d.numero_identificacion) ";
														
													
	
	public static ArrayList<DtoReporteDocumentosCarteraPaciente> consultarDocumentos (DtoDocumentosGarantia dto)
	{
		Connection con = UtilidadBD.abrirConexion();
		ArrayList<DtoReporteDocumentosCarteraPaciente> listadoDocs= new ArrayList<DtoReporteDocumentosCarteraPaciente>();
		
		String consulta=consultarDocumentos;
		
		logger.info("LO QUE LLEGA DE ESTADO------>"+dto.getEstado()+"...");
		
		consulta+=	"WHERE ";
		
		if (!dto.getCentroAtencion().equals(""))
			consulta+="	i.centro_atencion="+dto.getCentroAtencion()+" AND ";
		
		if (!dto.getFechaGen().equals("")&&!dto.getFechaGenFinal().equals(""))
			consulta+="	(dg.fecha_generacion BETWEEN '"+UtilidadFecha.conversionFormatoFechaABD(dto.getFechaGen())+"' AND '"+UtilidadFecha.conversionFormatoFechaABD(dto.getFechaGenFinal())+"') AND ";
		
		if (!dto.getTipoDocumento().equals(""))
			consulta+="	dg.tipo_documento='"+dto.getTipoDocumento()+"' AND ";
		
		if (!dto.getEstado().equals(""))
			consulta+="	dg.estado='"+dto.getEstado()+"' AND ";

		consulta+=" 1=1 ORDER BY codigocentroatencion,codigogarantia";
		
		logger.info("LA CONSULTA------->"+consulta);
	
		try 
		 {
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			 {
				DtoReporteDocumentosCarteraPaciente dtoRep=new DtoReporteDocumentosCarteraPaciente();
				dtoRep.getInfoDocsGarantia().setIngreso(rs.getInt("ingresodocumento"));
				dtoRep.getInfoDocsGarantia().setConsecutivo(rs.getString("codigogarantia"));
				dtoRep.getInfoDocsGarantia().setAnioConsecutivo(rs.getString("aniodocumento"));
				dtoRep.getInfoDocsGarantia().setTipoDocumento(rs.getString("tipodocumento"));
				dtoRep.getInfoDocsGarantia().setFechaGen(rs.getString("fechagendocumento"));
				dtoRep.getInfoDocsGarantia().setValorDoc(rs.getBigDecimal("valordocumento"));
				dtoRep.getInfoDocsGarantia().setEstado(rs.getString("estado"));
				dtoRep.getInfoDocsGarantia().setValorTotal(rs.getBigDecimal("saldodocumento"));
				dtoRep.getInfoDocsGarantia().setCentroAtencion(rs.getString("nombrecentroatencion"));
				dtoRep.getInfoDocsGarantia().setCodigoCentroAtencion(rs.getInt("codigocentroatencion"));
				dtoRep.getInfoDocsGarantia().setConsecutivoFactura(rs.getInt("consecutivofactura"));
				dtoRep.getInfoDeudor().setTipoIdentificacionDeu(rs.getString("tipoiddeudor"));
				dtoRep.getInfoDeudor().setNumeroIdentificacionDeu(rs.getString("numiddeudor"));
				dtoRep.getInfoDeudor().setPrimerNombreDeu(rs.getString("primernomdeudor"));
				dtoRep.getInfoDeudor().setSegundoNombreDeu(rs.getString("segundonomdeudor"));
				dtoRep.getInfoDeudor().setPrimerApellidoDeu(rs.getString("primerapedeudor"));
				dtoRep.getInfoDeudor().setSegundoApellidoDeu(rs.getString("segundoapedeudor"));
				dtoRep.getInfoDeudor().setNombreCompletoPac(rs.getString("nombrepaciente"));
				dtoRep.getInfoDeudor().setDireccion(rs.getString("direcciondeudor"));
				dtoRep.getInfoDeudor().setTelefono(rs.getString("telefonodeudor"));
				dtoRep.getInfoPaciente().setTipoId(rs.getString("tipoidpaciente"));
				dtoRep.getInfoPaciente().setNumeroId(rs.getString("numidpaciente"));
				dtoRep.getInfoRC().setConsecutivo(rs.getString("nrorc"));
				dtoRep.getInfoDatosFin().setDiasVencimiento(rs.getInt("diasvigenciadocumento"));
				dtoRep.getInfoDatosFin().setFechaInicio(rs.getString("fechavencimiento"));
				listadoDocs.add(dtoRep);
			 }
		 }
		catch (SQLException e) 
		{
			logger.error("ERROR EN consultarDocumentos==> "+e);
		}
		
		return listadoDocs;
	}
	
	
	
}