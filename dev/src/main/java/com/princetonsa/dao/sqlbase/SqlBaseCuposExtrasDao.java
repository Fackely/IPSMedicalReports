
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author <a href="mailto:cperalta@PrincetonSA.com">Carlos Peralta</a>
 * Clase para las transacciones de Cupos Extras
 * @version 1.0  08 /May/ 2006
 */
public class SqlBaseCuposExtrasDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseCuposExtrasDao.class);
	
	
	/**
	 * Statement para consultar las agendas generadas para unos parametros en especifico
	 */
	private final static String consultaAgendasGeneradasStr=" SELECT a.codigo as codigoagenda, " +
															" a.fecha as fecha, " +
															" a.hora_inicio as hora, " +
															" a.unidad_consulta as codigounidadconsulta, " +
															" uc.descripcion as unidadconsulta, " +
															" a.consultorio as codigoconsultorio,  " +
							 								" con.descripcion as consultorio, " +
															" a.cupos as cuposdisponibles," +
															" '' AS check2,  " +
															" '' AS cuposextra " +
															" FROM agenda a " +
															" INNER JOIN unidades_consulta uc ON(a.unidad_consulta=uc.codigo) " +
															" INNER JOIN consultorios con ON(a.consultorio=con.codigo) " +
															" WHERE 1=1 ";
	
	
	/**
	 * Cadena con el statement necesario para actualziar los cupos de una agenda
	 */
	private final static String actualizarCuposEnAgendaStr=" UPDATE agenda" +
														   " SET cupos = cupos + ? " +
														   " WHERE codigo = ? ";
	
	
	/**
	 * M�todo para consultar las agendas disponibles segun unos parametros
	 * de b�squeda especificados
	 * @param con
	 * @param fechaIncial
	 * @param fechaFinal
	 * @param codigoUnidadConsulta
	 * @param codigoConsultorio
	 * @param codigoDiaSemana
	 * @param codigoMedico
	 * @param centroAtencion 
	 * @return
	 */
	public static HashMap busquedaAgendaGenerada (Connection con, String fechaIncial, String fechaFinal, int codigoUnidadConsulta, int codigoConsultorio, int codigoDiaSemana, int codigoMedico, String minutosEspera, int centroAtencion, String centrosAtencion, String unidadesAgenda) throws SQLException
	{
		try
		{
			PreparedStatementDecorator ps = null;
		
			if (con == null || con.isClosed()) 
			{
				DaoFactory myFactory = DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}  
		
			String avanzadaStr = "";
			String[] fechaEvaluar ;
			
			if(!fechaIncial.equals("") && !fechaFinal.equals(""))
			{
				//avanzadaStr+=" AND a.fecha between to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaIncial)+"','YY/MM/DD') and  to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"','YY/MM/DD') ";
				avanzadaStr+=" AND a.fecha between to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaIncial)+"','YYYY/MM/DD') and  to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"','YYYY/MM/DD') ";
			}
			
			if(codigoUnidadConsulta != -1)
				avanzadaStr+=" AND a.unidad_consulta="+codigoUnidadConsulta;
			else
				avanzadaStr+=" AND a.unidad_consulta IN ("+unidadesAgenda+") ";
			
			if(codigoConsultorio != -1)
			{
				avanzadaStr+=" AND a.consultorio="+codigoConsultorio;
			}
			if(codigoDiaSemana != -1)
			{
				avanzadaStr+=" AND a.dia="+codigoDiaSemana;
			}
			if(codigoMedico != -1)
			{
				if(codigoMedico == ConstantesBD.codigoProfesionalSaludNoAsignado)
				{
					avanzadaStr+=" AND a.codigo_medico IS null";
				}
				else
				{
					avanzadaStr+=" AND a.codigo_medico="+codigoMedico;
				}
				
			}
			
			avanzadaStr+=" AND a.activo="+ValoresPorDefecto.getValorTrueParaConsultas();
			
			if(minutosEspera.equals("") || minutosEspera.equals("null"))
			{
				minutosEspera="0";
			}
			if(minutosEspera.equals("0"))
			{
				fechaEvaluar = UtilidadFecha.incrementarMinutosAFechaHora( UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual(), Integer.parseInt(minutosEspera),true);
			}
			else
			{
				fechaEvaluar = UtilidadFecha.incrementarMinutosAFechaHora( UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()),UtilidadFecha.getHoraActual(), Integer.parseInt(minutosEspera)*-1,true);
			}
			//avanzadaStr+=" AND (a.fecha >to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaEvaluar[0])+"','YY/MM/DD') or a.fecha=to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaEvaluar[0])+"','YY/MM/DD') and a.hora_inicio>='"+fechaEvaluar[1]+"')";
			avanzadaStr+=" AND (a.fecha >to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaEvaluar[0])+"','YYYY/MM/DD') or a.fecha=to_date('"+UtilidadFecha.conversionFormatoFechaABD(fechaEvaluar[0])+"','YYYY/MM/DD') and a.hora_inicio>='"+fechaEvaluar[1]+"')";
			
			//-- Verificar si ingresaron Centro de Atencion en la Busqueda.
			if(centroAtencion != -1)
				avanzadaStr+=" AND con.centro_atencion = " + centroAtencion;
			else
				avanzadaStr+=" AND con.centro_atencion  IN (" + centrosAtencion+") ";
			
			
			
			
			
			String consulta= consultaAgendasGeneradasStr + avanzadaStr+" ORDER BY a.unidad_consulta asc,  a.fecha asc, a.hora_inicio asc ";
			logger.info("\n\n\n\n consulta: "+consulta);
			ps=  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()),true,false);
			ps.close();
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en busquedaAgendaGenerada : [SqlBaseCuposExtrasDao] "+e.toString() );
			return null;
		}	    
	}
	
	/**
	 * M�todo para actualizar el numero de cupos de una agenda determinada
	 * @param con
	 * @param numeroCupos
	 * @param codigoAgenda
	 * @return
	 */
	public static int actualizarCuposEnAgenda(Connection con , int numeroCupos, int codigoAgenda)throws SQLException
	{
		int temp=0;
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarCuposEnAgendaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroCupos);
			ps.setInt(2, codigoAgenda);
			ps.executeUpdate();
			temp = 1;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en actualizarCuposEnAgenda : [SqlBaseCuposExtrasDao] "+e.toString() );
			temp = -1;
		}	 
		
		return temp;
	}
	
	/**
	 * M�todo para insertar
	 * @param con
	 * @param mapaCuposExtra
	 * @param usuario
	 * @param cadenaInsertar
	 * @return
	 */
	public static int insertarCuposExtra(Connection con, HashMap mapaCuposExtra, String usuario, String cadenaInsertar) throws SQLException
	{
		int temp = 0;
		String temporal = "";
		String cuposExtras = "";
		String check = "";
		try
		{
			for(int i = 0 ; i < Integer.parseInt(mapaCuposExtra.get("numRegistros").toString()) ; i++)
			{
				temporal = mapaCuposExtra.get("cuposextra_"+i).toString();
				if(!temporal.equals(""))
				{
					cuposExtras = temporal;
				}
				check = mapaCuposExtra.get("check2_"+i).toString();
				if(Utilidades.convertirAEntero(cuposExtras) > 0 && check.equals("true"))
				{
					PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(cadenaInsertar,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual())));
					ps.setString(2, UtilidadFecha.getHoraActual());
					ps.setInt(3, Integer.parseInt(mapaCuposExtra.get("codigoagenda_"+i).toString()));
					ps.setString(4, mapaCuposExtra.get("cuposdisponibles_"+i).toString());
					ps.setString(5, cuposExtras);
					ps.setString(6, usuario);
					ps.executeUpdate();
				}
			}
			temp = 1;
		}
		catch(SQLException e)
		{
			logger.warn(e+"Error en insertarCuposExtra : [SqlBaseCuposExtrasDao] "+e.toString() );
			temp = -1;
		}	 
		
		return temp;
	}
	
	
}
