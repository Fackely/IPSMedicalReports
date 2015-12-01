package com.princetonsa.dao.sqlbase.parametrizacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBasePosicionesAnestesiaDao 
{

	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBasePosicionesAnestesiaDao.class);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarPosiciones (Connection con, int numeroSolicitud)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		logger.info("llega sql cargarposiciones");
		mapa.put("numRegistros", "0");
		
		String consultaStr=	"SELECT " +
								"pha.codigo AS codigoposicionhojaanestesia, " +
								"paic.codigo AS codigo_posicion_inst_cc, " +
								"pa.codigo AS codigoposicion,  " +
								"coalesce(pa.nombre, '') AS nombre, " +
								"to_char(pha.fecha_inicial, 'DD/MM/YYYY') as fechainicial, " +
								"pha.hora_inicial as horainicial, "+
								"'"+ConstantesBD.acronimoSi+"' as estabd " +
							"FROM " +
								"posiciones_hoja_anestesia pha " +
								"INNER JOIN posiciones_anest_inst_cc paic ON(pha.posicion_inst_cc=paic.codigo) " +
								"INNER JOIN posiciones_anestesia pa ON (paic.posicion=pa.codigo) " +
							"WHERE " +
								"pha.numero_solicitud="+numeroSolicitud+" " +
							"ORDER BY fechainicial, horainicial ";
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
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
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarTagMapPosiciones(Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		HashMap<Object, Object> mapa= cargarMapTagPosicionesPrivado(con, numeroSolicitud, centroCosto, institucion);
		if(Utilidades.convertirAEntero(mapa.get("numRegistros")+"")<=0)
			mapa= cargarMapTagPosicionesPrivado(con, numeroSolicitud, ConstantesBD.codigoNuncaValido /*centroCosto*/, institucion);
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	private static HashMap<Object, Object> cargarMapTagPosicionesPrivado(Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String consultaStr=	"";
		
		if(centroCosto>0)
		{	
			consultaStr=	"SELECT " +
								"paic.codigo AS codigo_posicion_inst_cc,  " +
								"coalesce(pa.nombre, '') AS nombre " +
							"FROM " +
								"posiciones_anest_inst_cc paic " +
								"INNER JOIN posiciones_anestesia pa ON (paic.posicion=pa.codigo) " +
							"WHERE " +
								"paic.centro_costo="+centroCosto+" " +
								"and paic.institucion="+institucion+" " +
								"and paic.activo='"+ConstantesBD.acronimoSi+"' "+
							"ORDER BY pa.nombre ";
		}	
		else 
		{
			consultaStr=	"SELECT " +
								"paic.codigo AS codigo_posicion_inst_cc,  " +
								"coalesce(pa.nombre, '') AS nombre " +
							"FROM " +
								"posiciones_anest_inst_cc paic " +
								"INNER JOIN posiciones_anestesia pa ON (paic.posicion=pa.codigo) " +
							"WHERE " +
								"paic.centro_costo is null " +
								"and paic.institucion="+institucion+" " +
								"and paic.activo='"+ConstantesBD.acronimoSi+"' "+
							"ORDER BY pa.nombre ";
		}
		
		logger.info("\n cargarMapTagPosiciones->"+consultaStr+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
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
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertar(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="INSERT INTO posiciones_hoja_anestesia 	(codigo, " +     			//1
	    														"numero_solicitud, " +		//2
	    														"posicion_inst_cc, " +		//3
	    														"posicion, " +				//4
	    														"fecha_inicial, " +			//5
	    														"hora_inicial, " +			//6
	    														"fecha_modifica, " +		
	    														"hora_modifica, " +			
	    														"usuario_modifica) " +		//7
	    														"values (?, ?, ?," +
	    																" ?, ?, ?," +
	    																"CURRENT_DATE, " +
	    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
	    																"?)";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_posiciones_hoja_anestesia");
			ps.setInt(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("numerosolicitud").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigoposicioninstcc").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("codigoposicion").toString()));
			
			ps.setDate(5, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechainicial").toString())));
			
			ps.setString(6, mapa.get("horainicial")+"");
			
			ps.setString(7, mapa.get("loginusuario")+"");
			
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
    public static boolean modificarPosicion(Connection con, HashMap<Object, Object> mapa)
    {
    	String cadena="UPDATE posiciones_hoja_anestesia SET " +
    						"fecha_inicial=?, " +			//1
							"hora_inicial=?, " +			//2
							"posicion_inst_cc=?, " +		//3
							"posicion=?, " +				//4
							"fecha_modifica= CURRENT_DATE, " +		
							"hora_modifica= "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +							
							"usuario_modifica=? " +			//5
						"WHERE "+
							"codigo=? "; 					//6
							
							
    	logger.info("\n Modificar-->"+cadena+" MAOPA-->"+mapa+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechainicial").toString())));
			ps.setString(2, mapa.get("horainicial")+"");
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigoposicioninstcc").toString()));
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("codigoposicion").toString()));
			ps.setString(5, mapa.get("loginusuario")+"");
			ps.setInt(6, Utilidades.convertirAEntero(mapa.get("codigoposicionhojaanestesia")+""));
			
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
    public static boolean eliminar(Connection con, int codigoPosicionHojaAnestesia)
    {
    	String cadena="DELETE FROM posiciones_hoja_anestesia WHERE codigo=? ";
							
		logger.info("\n ELIMINAR-->"+cadena+" codigo-->"+codigoPosicionHojaAnestesia+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPosicionHojaAnestesia);
			
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
     * @param codigoPosicionInstCC
     * @return
     */
    public static int obtenerPosicionDadaPosicionInstCC(Connection con, int codigoPosicionInstCC)
    {
    	String consulta="SELECT posicion FROM posiciones_anest_inst_cc WHERE codigo=?";
    	PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoPosicionInstCC);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			if(rs.next())
				return rs.getInt(1);
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
    }
}
