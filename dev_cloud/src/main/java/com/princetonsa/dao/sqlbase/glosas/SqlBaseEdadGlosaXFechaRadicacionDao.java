package com.princetonsa.dao.sqlbase.glosas;

import java.nio.charset.CodingErrorAction;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadBD;
import util.UtilidadFecha;

import com.princetonsa.dao.sqlbase.SqlBaseUtilidadesBDDao;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.glosas.DtoGlosa;

public class SqlBaseEdadGlosaXFechaRadicacionDao
{
	private static Logger logger = Logger.getLogger(SqlBaseEdadGlosaXFechaRadicacionDao.class);
	
	private static String	consultarGlosas	=	"SELECT "+ 
												  "rg.codigo AS codigoglosa,"+ 
												  "rg.convenio AS convenioglosa ,"+
												  "rg.fecha_registro_glosa AS fecharegglosa,"+
												  "reg.estado AS estadorespuesta,"+
												  "reg.valor_respuesta AS valorrespuesta," +
												  "reg.codigo AS codigorespuesta," +
												  "reg.fecha_radicacion AS fecharadrespuesta, "+
												  "tc.codigo AS codigotipoconvenio,"+
												  "tc.descripcion AS desctipoconvenio," +
												  "(to_date('"+UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())+"','yyyy-mm-dd')-reg.fecha_radicacion) AS edadglosa "+
												"FROM "+
												  "glosas.registro_glosas rg "+
												"LEFT OUTER JOIN "+
												  "glosas.respuesta_glosa reg ON (reg.glosa=rg.codigo) "+
												"INNER JOIN "+
												  "facturacion.convenios c ON (c.codigo=rg.convenio) "+
												"INNER JOIN "+
												  "facturacion.tipos_convenio tc ON (tc.codigo=c.tipo_convenio AND tc.institucion=c.institucion) ";
												

	public static ArrayList<DtoGlosa> consultarGlosas(DtoGlosa dto, boolean radicadas)
	{
		ArrayList<DtoGlosa> listado = new ArrayList<DtoGlosa>();
		
		String consulta=consultarGlosas;
		
		consulta+=" WHERE ";
				
		if (!dto.getFechaRegistroGlosa().equals(""))
			consulta+="(to_char(rg.fecha_registro_glosa,'"+ConstantesBD.formatoFechaAp+"') between '"+dto.getFechaRegistroGlosa()+"' AND '"+UtilidadFecha.getFechaActual()+"') AND ";
		
		if (!dto.getTipoConvenio().equals("-1"))
			consulta+="	tc.codigo="+dto.getTipoConvenio()+" AND ";
		
		if (dto.getConvenio().getCodigo()!=ConstantesBD.codigoNuncaValido)
			consulta+="	rg.convenio="+dto.getConvenio().getCodigo()+" AND ";
		
		if (radicadas)
			consulta+="	reg.estado='"+ConstantesIntegridadDominio.acronimoRadicado+"' AND ";
		else
			consulta+="	reg.estado='"+ConstantesIntegridadDominio.acronimoEstadoAprobado+"' AND ";
			
		consulta+=" 1=1 ORDER BY tc.codigo";
			
		Connection con = UtilidadBD.abrirConexion();
		
		try 
		{	
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consulta);
			logger.info("\n\nconsulta---> "+ps);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
				
			while(rs.next())
			{
				DtoGlosa dtoGlosa=new DtoGlosa();
				
				dto.setCodigo(rs.getString("codigoglosa"));
				dto.getConvenio().setCodigo(rs.getInt("convenioglosa"));
				dto.setFechaRegistroGlosa(rs.getString("fecharegglosa"));
				dto.getDtoRespuestaGlosa().setCodigoPk(rs.getInt("codigorespuesta"));
				dto.getDtoRespuestaGlosa().setValorRespuesta(rs.getDouble("valorrespuesta"));
				dto.getDtoRespuestaGlosa().setEstadoRespuesta(rs.getString("estadorespuesta"));
				dto.getDtoRespuestaGlosa().setFechaRadicacion(rs.getString("fecharadrespuesta"));
				dto.setTipoConvenio(rs.getString("codigotipoconvenio"));
				dto.setDescripcionTConvenio(rs.getString("desctipoconvenio"));
				listado.add(dtoGlosa);
			}
		SqlBaseUtilidadesBDDao.cerrarObjetosPersistencia(ps, rs, con);
		}
		catch (SQLException e) 
		{
		logger.error("ERROR EN consultarGlosas==> "+e);
		}
		
		
		return listado;
	}
}