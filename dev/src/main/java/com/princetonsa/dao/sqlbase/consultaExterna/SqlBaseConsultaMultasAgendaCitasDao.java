package com.princetonsa.dao.sqlbase.consultaExterna;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.UsuarioBasico;

public class SqlBaseConsultaMultasAgendaCitasDao {

	private static Logger logger=Logger.getLogger(SqlBaseConsultaMultasAgendaCitasDao.class);
	UsuarioBasico usuario;
	
	private static  String ConsultaMultasAgendaCitasStr="  SELECT mult.consecutivo                    ,"+
													  "to_char(mult.fecha_generacion, 'YYYY-MM-DD')  AS fecha_generacion , "+
													  "mult.estado                            AS estado           , "+
													  "mult.valor                             AS valor            , "+
													  "getnombrepersona2(cit.codigo_paciente) AS paciente         , "+
													  "getidpaciente(cit.codigo_paciente)     AS idd              , "+
													  "uni.descripcion                        AS unidad           , "+
													  "to_char(age.fecha, 'YYYY-MM-DD')       AS fecha            , "+
													  "age.hora_inicio                        AS hora             , "+
													  "est.nombre                             AS estadocita       , "+
													  "getnombrepersona2(age.codigo_medico)   AS medico			  , "+
													  "centr.descripcion 					  AS descripcion      , "+
													  "conv.nombre 							  AS nombreconvenio   , "+
													  "mult.fecha_anu_cond 					  AS motivofecha      , "+
													  "mult.usuario_anu_cond  				  AS usuariomotivo    , "+
													  "mult.observaciones 					  AS observaciones    , "+
													  "mot.descripcion 						  AS motivo           , "+
													  "getnombreservicio(sc.servicio,?)       AS nombreservicio   , "+
													  "getcodigopropservicio2(sc.servicio,?)  AS codservicio        "+
													  " FROM consultaexterna.multas_citas mult "+
													"INNER JOIN cita cit "+
													     "ON (cit.codigo= mult.cita) "+
													"INNER JOIN unidades_consulta uni "+
													     "ON(cit.unidad_consulta=uni.codigo) "+
													"INNER JOIN agenda age "+
													     "ON(age.codigo=cit.codigo_agenda) "+
													"INNER JOIN consultaexterna.estados_cita est "+
													     "ON(est.codigo=cit.estado_cita) "+
													"INNER JOIN centro_atencion centr "+
													     "ON (centr.consecutivo=age.centro_atencion) "+
													"LEFT OUTER JOIN convenios conv "+ 
													     " ON (conv.codigo=cit.convenio) "+
													"LEFT OUTER JOIN consultaexterna.mot_anu_cond_multas mot "+ 
													     "ON (mot.consecutivo=mult.mot_anu_cond_multa) " +
													"INNER JOIN consultaexterna.servicios_cita sc "+ 
													     "ON (sc.codigo_cita=cit.codigo) " +  	     	
													     "WHERE to_char(mult.fecha_generacion, 'YYYY-MM-DD') BETWEEN ? and ?" ;
	
	private static  String ConsultaMultasAgendaCitasPacienteStr="  SELECT mult.consecutivo                    ,"+
															    "mult.fecha_generacion                  AS fecha_generacion , "+
															    "mult.estado                            AS estado           , "+
															    "mult.valor                             AS valor            , "+
															    "getnombrepersona2(cit.codigo_paciente) AS paciente         , "+
															    "getidpaciente(cit.codigo_paciente)     AS idd              , "+
															    "uni.descripcion                        AS unidad           , "+
															    "age.fecha                              AS fecha            , "+
															    "age.hora_inicio                        AS hora             , "+
															    "est.nombre                             AS estadocita       , "+
															    "getnombrepersona2(age.codigo_medico)   AS medico			, "+
															    "centr.descripcion 					  AS descripcion      	, "+
															    "conv.nombre 							  AS nombreconvenio  , "+
															    "mult.fecha_anu_cond 					  AS motivofecha     , "+
															    "mult.usuario_anu_cond  				  AS usuariomotivo   , "+
															    "mult.observaciones 					  AS observaciones   , "+
															    "mot.descripcion 						  AS motivo          , "+
															    "getnombreservicio(sc.servicio,?)       AS nombreservicio   , "+
															    "getcodigopropservicio2(sc.servicio,?)  AS codservicio        "+
															    " FROM consultaexterna.multas_citas mult "+
															    "INNER JOIN cita cit "+
															    	"ON (cit.codigo= mult.cita) "+
															     "INNER JOIN unidades_consulta uni "+
															     	"ON(cit.unidad_consulta=uni.codigo) "+
															   	"INNER JOIN agenda age "+
															     "ON(age.codigo=cit.codigo_agenda) "+
															    "INNER JOIN consultaexterna.estados_cita est "+
															     "ON(est.codigo=cit.estado_cita) "+
															    "INNER JOIN centro_atencion centr "+
															     "ON (centr.consecutivo=age.centro_atencion) "+
															    "left outer JOIN convenios conv "+ 
															     " ON (conv.codigo=cit.convenio) "+
															    "left outer JOIN consultaexterna.mot_anu_cond_multas mot "+ 
															     "ON (mot.consecutivo=mult.mot_anu_cond_multa) " + 
															    "INNER JOIN consultaexterna.servicios_cita sc "+ 
															     "ON (sc.codigo_cita=cit.codigo) " +      
															    "WHERE cit.codigo_paciente=? ";
      
	public static HashMap consultaMultasAgendaCitas(String fechaInicial, String fechaFinal, HashMap<String, Object> elmapa) {
	
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator ps;

		Connection con=UtilidadBD.abrirConexion();
		
		
		try
		{
			String consulta = ConsultaMultasAgendaCitasStr;
			
		
				
				if(!elmapa.get("centroAtencion").equals(ConstantesBD.codigoNuncaValido) )
				{
					consulta+=" AND  centr.consecutivo="+elmapa.get("centroAtencion");
					
					if(!elmapa.get("unidadAgenda").equals(ConstantesBD.codigoNuncaValido))
					{
						consulta+=" AND uni.codigo="+elmapa.get("unidadAgenda");
					}
					
						
					
				}
				if(!elmapa.get("convenio").equals(""))
				{
					consulta+=" AND  conv.codigo="+elmapa.get("convenio");
				}
				if(!elmapa.get("profesional").equals(""))
				{
					consulta+=" AND  age.codigo_medico="+elmapa.get("profesional");
				}
				if(!elmapa.get("estadoCita").equals(""))
				{
						consulta+=" AND  est.codigo="+elmapa.get("estadoCita");
				}
				
				consulta+=" order by mult.consecutivo";
			
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			logger.info("Consulta : "+consulta);
			logger.info("fi:"+fechaInicial+" ff:"+fechaFinal);
			
			ps.setInt(1, Utilidades.convertirAEntero(elmapa.get("institucion")+""));
			
			ps.setInt(2, Utilidades.convertirAEntero(elmapa.get("institucion")+""));
			
			ps.setString(3, UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
			
			ps.setString(4, UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
			
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);
			
			UtilidadBD.cerrarConexion(con);
		}
		catch (SQLException e)
		{
			logger.info("\n\n ERROR. CONSULTANDO MOTIVOS ANULACION SQL------>>>>>>"+e);
			
			e.printStackTrace();
		}
		return resultados;	
	}

	public static HashMap<String, Object> consultaMultasAgendaCitasPaciente(
			int codigoPersona, String codigoinstitucion) {
		
		HashMap<String, Object> resultados = new HashMap<String, Object>();
		
		PreparedStatementDecorator ps;
		
		Connection con=UtilidadBD.abrirConexion();
		
		try
		{
		
			logger.info("ConsultaMultasAgendaCitasPacienteStr-->"+ConsultaMultasAgendaCitasPacienteStr+"   persona-->"+codigoPersona);
			ps= new PreparedStatementDecorator(con.prepareStatement(ConsultaMultasAgendaCitasPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		
			ps.setString(1, Utilidades.convertirAEntero(codigoinstitucion)+"");
			
			ps.setString(2, Utilidades.convertirAEntero(codigoinstitucion)+"");
			
			ps.setInt(3, codigoPersona);
			 
			resultados=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()), true, true);

		UtilidadBD.cerrarConexion(con);
		
		}catch (SQLException e)
		{
			
			logger.info("\n\n ERROR. CONSULTANDO MOTIVOS ANULACION SQL------>>>>>>"+e);
			
			e.printStackTrace();
		}
		
		
		return resultados;	
	}

}
