package com.princetonsa.dao.sqlbase.parametrizacion;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dto.salas.DtoTiempos;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseTiemposHojaAnestesiaDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseTiemposHojaAnestesiaDao.class);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarTiempos (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		logger.info("llega sql cargartiempos");
		HashMap<Object, Object> mapaSecciones= new HashMap<Object, Object>();
		mapaSecciones.put("numRegistros", "0");
		int tipo=1;
		
		//1. CARGAMOS LOS INSERTADOS EN BD
		while( (Utilidades.convertirAEntero(mapaSecciones.get("numRegistros").toString())==0 && tipo<=3))
		{	
			logger.info("tipo->"+tipo);
			mapaSecciones=  obtenerSubseccionesTiempos(con, numeroSolicitud, centroCosto, institucion, tipo);
			logger.info("\nmapa -->"+mapaSecciones);
			tipo++;
		}
		logger.info("retorna-->"+mapaSecciones);
		return mapaSecciones;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoEvento
	 * @return
	 */
	public static DtoTiempos cargarTiempo(Connection con, int codigoTiempo)
	{
		DtoTiempos dtoTiempo= new DtoTiempos();
		String cadena= "SELECT ta.nombre, ta.orden, ta.obligatorio from tiempos_anestesia ta WHERE ta.codigo=?";
		logger.info("cargar tiempo-->"+cadena+" ->"+codigoTiempo);
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoTiempo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				dtoTiempo.setNombre(rs.getString("nombre"));
				dtoTiempo.setObligatorio(UtilidadTexto.getBoolean(rs.getString("obligatorio")));
				dtoTiempo.setOrden(rs.getInt("orden"));
				dtoTiempo.setCodigo(codigoTiempo);
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return dtoTiempo;
	}
	
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @param tipo, 1= insertados, 2= xcentro costo especifico, 3= centro costo null
	 * @return
	 */
	private static HashMap<Object, Object> obtenerSubseccionesTiempos(Connection con, int numeroSolicitud, int centroCosto, int institucion, int tipo)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		//primero se carga los historicos, 
		String consultaStr=	"";
		
		if(tipo==1)
		{
			consultaStr=	"SELECT " +
								//"DISTINCT " +
								"taic.codigo AS codigo_tiempo_inst_cc, " +
								"taic.codigo_tiempo as evento, " +
								"coalesce(ta.nombre, '') AS nombre, " +
								"tha.minutos AS minutos, " +
								"tha.segundos AS segundos, " +
								"ta.codigo AS codigotiempo,  " +
								"tha.codigo AS codigotiempohojaanestesia, " +
								"'"+ConstantesBD.acronimoSi+"' as estabd " +
							"FROM " +
								"tiempos_hoja_anestesia tha " +
								"INNER JOIN tiempos_anest_inst_cc taic ON(tha.codigo_tiempo_inst_cc=taic.codigo) " +
								"INNER JOIN tiempos_anestesia ta ON (taic.codigo_tiempo=ta.codigo) " +
							"WHERE " +
								"tha.numero_solicitud="+numeroSolicitud+" " +
							"ORDER BY ta.orden ";
		}
		else if(tipo==2)
		{
			consultaStr=	"SELECT " +
								//"DISTINCT " +
								"taic.codigo AS codigo_tiempo_inst_cc, " +
								"taic.codigo_tiempo as evento, " +
								"coalesce(ta.nombre, '') AS nombre, " +
								"'' AS minutos, " +
								"'' AS segundos, " +
								"ta.codigo AS codigotiempo,  " +
								""+ConstantesBD.codigoNuncaValido+" AS codigotiempohojaanestesia, " +
								"'"+ConstantesBD.acronimoNo+"' as estabd " +
							"FROM " +
								"tiempos_anest_inst_cc taic " +
								"INNER JOIN tiempos_anestesia ta ON (taic.codigo_tiempo=ta.codigo) " +
							"WHERE " +
								"taic.centro_costo="+centroCosto+" " +
								"and taic.institucion="+institucion+" " +
								"and taic.activo='"+ConstantesBD.acronimoSi+"' "+
							"ORDER BY ta.orden ";
		}
		else if(tipo==3)
		{
			consultaStr=	"SELECT " +
								//"DISTINCT " +
								"taic.codigo AS codigo_tiempo_inst_cc, " +
								"taic.codigo_tiempo as evento, " +
								"coalesce(ta.nombre, '') AS nombre, " +
								"'' AS minutos, " +
								"'' AS segundos, " +
								"ta.codigo AS codigotiempo,  " +
								""+ConstantesBD.codigoNuncaValido+" AS codigotiempohojaanestesia, " +
								"'"+ConstantesBD.acronimoNo+"' as estabd " +
							"FROM " +
								"tiempos_anest_inst_cc taic " +
								"INNER JOIN tiempos_anestesia ta ON (taic.codigo_tiempo=ta.codigo) " +
							"WHERE " +
								"taic.centro_costo is null "+
								"and taic.institucion="+institucion+" " +
								"and taic.activo='"+ConstantesBD.acronimoSi+"' "+
							"ORDER BY ta.orden ";
		}
		
		logger.info("\n obtenerSubseccionesTempos->"+consultaStr+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			for(int w=0; w<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); w++)
			{
				mapa.put("DTOTIEMPOS_"+w, cargarTiempo(con, Utilidades.convertirAEntero( mapa.get("codigotiempo_"+w).toString())));
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param mapa
	 * @return
	 */
    public static boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="INSERT INTO tiempos_hoja_anestesia 	(codigo, " +     			//1
    														"numero_solicitud, " +		//2
    														"codigo_tiempo_inst_cc, " +	//3
    														"minutos, " +				//4
    														"segundos, "+				//5
    														"fecha_modifica, " +		
    														"hora_modifica, " +			
    														"usuario_modifica) " +		//6
    														"values (?, ?, ?," +
    																" ?, ?, " +
    																"CURRENT_DATE, " +
    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
    																"?)";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_tiempos_hoja_anestesia");
			ps.setInt(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("numero_solicitud").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigo_tiempo_inst_cc").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("minutos")+""));
			ps.setInt(5, Utilidades.convertirAEntero(mapa.get("segundos")+""));
			ps.setString(6, mapa.get("loginusuario")+"");
			
			if(ps.executeUpdate()>0)
				return true;
			
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    } 
	
    
    /**
     * 
     * @param con
     * @param mapa
     * @return
     */
    public static boolean modificar(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="UPDATE tiempos_hoja_anestesia SET " +
    							"minutos=?, " +				//1
    							"segundos=?, "+				//2
    							"fecha_modifica=CURRENT_DATE, " +		
    							"hora_modifica= "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +			
    							"usuario_modifica=? " +		//3
    					"WHERE "+
							"codigo=? "; 					//4
							
							
    	logger.info("\n Modificar-->"+cadena+" MAOPA-->"+mapa+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, Utilidades.convertirAEntero(mapa.get("minutos")+""));
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("segundos")+""));
			ps.setString(3, mapa.get("loginusuario")+"");
			
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("codigotiempohojaanestesia")+""));
			
			if(ps.executeUpdate()>0)
				return true;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
    }
	
}
