package com.princetonsa.dao.sqlbase.tesoreria;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.mundo.ConsecutivosDisponibles;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author Wilson Rios
 * wrios@princetonsa.com
 */
public class SqlBaseTrasladosCajaDao 
{
	/**
	 * Objeto para manejar los logs de esta clase
	 */
	private static Logger logger = Logger.getLogger(SqlBaseTrasladosCajaDao.class);

	/**
	 * 
	 */
	private static String insertarTrasladoCaja=" INSERT INTO traslados_caja (consecutivo,institucion,fecha_traslado,caja_ppal,caja_mayor,usuario,fecha_grabacion,hora_grabacion) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
	
	/**
	 * 
	 */
	private static String insertarDetalleTrasladoCaja=" INSERT INTO det_traslados_caja (codigo,traslado_caja,institucion,forma_pago,total) values (?, ?, ?, ?, ?)";
	
	/**
	 * 
	 */
	private static String actualizarCierreCaja= "update cierres_cajas set traslado_caja=? where consecutivo=? and institucion=?";
	
	/**
	 * 
	 * @param con
	 * @param loginUsuario
	 * @param fechaTraslado
	 * @param cajaPpal
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap busquedaCierresCajasParaTraslado(Connection con, String loginUsuario,  String fechaTraslado, String cajaPpal, int codigoInstitucion)
	{
		String consulta="";
		HashMap mapa= new HashMap();
		Vector vectorKeysFormasPago= new Vector();
		Vector vectorNombreFormasPago=new Vector();
		Vector vectorConsecutivoFormasPago= new Vector();
		
		//1. Obtenemos las formas de pago
		consulta="SELECT consecutivo as consecutivo, descripcion as descripcion from formas_pago where institucion="+codigoInstitucion+" and activo= "+ValoresPorDefecto.getValorTrueParaConsultas()+" order by descripcion";
		logger.info("\n Formas de pago ="+consulta+" \n");
		HashMap mapaFormasPago= obtenerMapa(con, consulta);
		
		//2. Obtenemos la matriz de n x n armandola dinamicamente
		consulta=	"SELECT ";
						
		//se arma la consulta con las n formas de pago
		for(int w=0; w<Integer.parseInt(mapaFormasPago.get("numRegistros").toString()); w++)
		{
			consulta+="(SELECT CASE WHEN sum(dprc.valor) IS NULL THEN 0 ELSE sum(dprc.valor) END FROM recibos_caja rc inner join detalle_pagos_rc dprc on (rc.numero_recibo_caja=dprc.numero_recibo_caja and dprc.institucion=rc.institucion) WHERE rc.cierre_caja=cc.consecutivo and rc.estado<>"+ConstantesBD.codigoEstadoReciboCajaAnulado+" and dprc.forma_pago="+mapaFormasPago.get("consecutivo_"+w)+") as \""+mapaFormasPago.get("descripcion_"+w).toString().trim().toLowerCase().replaceAll(" ", "")+"\", "; 
			vectorKeysFormasPago.add(mapaFormasPago.get("descripcion_"+w).toString().trim().toLowerCase().replaceAll(" ", ""));
			vectorNombreFormasPago.add(mapaFormasPago.get("descripcion_"+w));
			vectorConsecutivoFormasPago.add(mapaFormasPago.get("consecutivo_"+w));
		}
		
		consulta+=		
						"cc.consecutivo AS consecutivocierre, " +	
						"cc.caja AS consecutivocaja, " +
						"c.descripcion AS descripcioncaja, " +
						"cc.cajero as logincajero, " +
						"getnombrepersona(u.codigo_persona) as nombrescajero "+
					"from " +
						"cierres_cajas cc " +
						"inner join cajas c ON (c.consecutivo=cc.caja) " +
						"inner join usuarios u ON (u.login=cc.cajero) " +
					"where " +
						"cc.fecha_cierre='"+UtilidadFecha.conversionFormatoFechaABD(fechaTraslado)+"' " +
						//"and cc.usuario='"+loginUsuario+"' " +
						"and caja_ppal="+cajaPpal+" " +
						"and cc.traslado_caja is null "+
						"and cc.institucion="+codigoInstitucion+" ";
		
		logger.info("\n consulta trasllado mapa-->"+consulta+" \n");
		logger.info("\n vector keys-->"+vectorKeysFormasPago+" \n");
		mapa=obtenerMapa(con, consulta);
		
		String consulta2="SELECT SUM(drc.valor_devolucion) AS total,"+
							" drc.forma_pago                 AS formapago,cc.consecutivo as consecutivo "+
						" FROM tesoreria.devol_recibos_caja drc "+ 
						" inner join cierres_cajas cc on (drc.cierre_caja =cc.consecutivo and drc.institucion=cc.institucion)"+
						"where " +
						"cc.fecha_cierre='"+UtilidadFecha.conversionFormatoFechaABD(fechaTraslado)+"' " +
						//"and cc.usuario='"+loginUsuario+"' " +
						"and caja_ppal="+cajaPpal+" " +
						"and cc.traslado_caja is null "+
						"and cc.institucion="+codigoInstitucion+" GROUP BY drc.forma_pago,cc.consecutivo";
		
		HashMap mapaDevoluciones=obtenerDevolucionesFormasPago(con, consulta2);
		//ahora por codigo vamos a calcular los totales porque las consultas son muy pesadas
		double total=0;
		double totalTotales=0;
		
		//RESTAR LAS DEVOLUCIONES.
		for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
		{
			total=0;
			for(int x=0; x<vectorKeysFormasPago.size(); x++)
			{
				//total+= (Double.parseDouble(mapa.get(vectorKeysFormasPago.get(x)+"_"+w).toString())-Utilidades.convertirADouble(""+mapaDevoluciones.get(vectorConsecutivoFormasPago.get(x)+"_"+mapa.get("consecutivocierre_"+w))));
				if(Utilidades.convertirAEntero(vectorConsecutivoFormasPago.get(x)+"")==1)
				{
					double temporal=((Double.parseDouble(mapa.get(vectorKeysFormasPago.get(x)+"_"+w).toString())-Utilidades.convertirADouble(""+mapaDevoluciones.get(vectorConsecutivoFormasPago.get(x)+"_"+mapa.get("consecutivocierre_"+w)))));
					mapa.put(vectorKeysFormasPago.get(x)+"_"+w,temporal+"");
				}
			}
		}
		
		for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
		{
			total=0;
			for(int x=0; x<vectorKeysFormasPago.size(); x++)
			{
				total+= Double.parseDouble(mapa.get(vectorKeysFormasPago.get(x)+"_"+w).toString());
			}
			totalTotales+=total;
			mapa.put("TOTAL_CIERRE_"+w, total);
			logger.info("TOTAL_CIERRE_"+w+" -->"+total);
		}
		logger.info("TOTAL_TOTALES-->"+totalTotales);
		mapa.put("TOTAL_TOTALES", totalTotales);
		
		
		//LOS TOTALES DEL TRASLADO X FORMA PAGO MANEJAN EL INDICE SEGUN EL TAMANIO DEL VECTOR
		Vector vectorTotalesFormasPago= new Vector();
		for(int x=0; x<vectorKeysFormasPago.size(); x++)
		{
			total=0;
			for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
			{
				total+= Double.parseDouble(mapa.get(vectorKeysFormasPago.get(x)+"_"+w).toString());
			}
			vectorTotalesFormasPago.add(total);
		}
		
		mapa.put("KEYS_FORMAS_PAGO_VECTOR", vectorKeysFormasPago);	
		mapa.put("NOMBRES_FORMAS_PAGO_VECTOR", vectorNombreFormasPago);
		mapa.put("CONSECUTIVOS_FORMAS_PAGO_VECTOR", vectorConsecutivoFormasPago);
		mapa.put("TOTALES_FORMAS_PAGO_VECTOR", vectorTotalesFormasPago);
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoTraslado
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap resumen(Connection con, String consecutivoTraslado, int codigoInstitucion)
	{
		String consulta="";
		HashMap mapa= new HashMap();
		Vector vectorKeysFormasPago= new Vector();
		Vector vectorNombreFormasPago=new Vector();
		Vector vectorConsecutivoFormasPago= new Vector();
		
		//1. Obtenemos las formas de pago
		consulta="SELECT consecutivo as consecutivo, descripcion as descripcion from formas_pago where institucion="+codigoInstitucion+" and activo= "+ValoresPorDefecto.getValorTrueParaConsultas()+" order by descripcion";
		logger.info("\n Formas de pago ="+consulta+" \n");
		HashMap mapaFormasPago= obtenerMapa(con, consulta);
		
		//2. Obtenemos la matriz de n x n armandola dinamicamente
		consulta=	"SELECT ";
						
		//se arma la consulta con las n formas de pago
		for(int w=0; w<Integer.parseInt(mapaFormasPago.get("numRegistros").toString()); w++)
		{
			//consulta+="(SELECT CASE WHEN dtc.total IS NULL THEN 0 ELSE dtc.total END FROM det_traslados_caja dtc WHERE dtc.traslado_caja="+consecutivoTraslado+" and dtc.institucion="+codigoInstitucion+" and dtc.forma_pago="+mapaFormasPago.get("consecutivo_"+w)+") as "+mapaFormasPago.get("descripcion_"+w).toString().trim().toLowerCase().replaceAll(" ", "")+", ";
			consulta+="(SELECT CASE WHEN sum(dprc.valor) IS NULL THEN 0 ELSE sum(dprc.valor) END FROM recibos_caja rc inner join detalle_pagos_rc dprc on (rc.numero_recibo_caja=dprc.numero_recibo_caja and dprc.institucion=rc.institucion) WHERE rc.cierre_caja=cc.consecutivo and rc.estado<>"+ConstantesBD.codigoEstadoReciboCajaAnulado+" and dprc.forma_pago="+mapaFormasPago.get("consecutivo_"+w)+") as \""+mapaFormasPago.get("descripcion_"+w).toString().trim().toLowerCase().replaceAll(" ", "")+"\", ";
			vectorKeysFormasPago.add(mapaFormasPago.get("descripcion_"+w).toString().trim().toLowerCase().replaceAll(" ", ""));
			vectorNombreFormasPago.add(mapaFormasPago.get("descripcion_"+w));
			vectorConsecutivoFormasPago.add(mapaFormasPago.get("consecutivo_"+w));
		}
		
		consulta+=		
						"cc.consecutivo AS consecutivocierre, " +	
						"cc.caja AS consecutivocaja, " +
						"c.descripcion AS descripcioncaja, " +
						"cc.cajero as logincajero, " +
						"getnombrepersona(u.codigo_persona) as nombrescajero "+
					"from " +
						"cierres_cajas cc " +
						"inner join cajas c ON (c.consecutivo=cc.caja) " +
						"inner join usuarios u ON (u.login=cc.cajero) " +
					"where " +
						"cc.traslado_caja='"+consecutivoTraslado+"' " +
						"and cc.institucion="+codigoInstitucion+" ";
		
		logger.info("\n consulta traslado mapa-->"+consulta+" \n");
		logger.info("\n vector keys-->"+vectorKeysFormasPago+" \n");
		mapa=obtenerMapa(con, consulta);
		
		//ahora por codigo vamos a calcular los totales porque las consultas son muy pesadas
		String consulta2="SELECT SUM(drc.valor_devolucion) AS total,"+
								" drc.forma_pago                 AS formapago,cc.consecutivo as consecutivo "+
							" FROM tesoreria.devol_recibos_caja drc "+ 
							" inner join cierres_cajas cc on (drc.cierre_caja =cc.consecutivo and drc.institucion=cc.institucion)"+
							"where " +
							"cc.traslado_caja='"+consecutivoTraslado+"' " +
							"and cc.institucion="+codigoInstitucion+" GROUP BY drc.forma_pago,cc.consecutivo";

		HashMap mapaDevoluciones=obtenerDevolucionesFormasPago(con, consulta2);
		
		double total=0;
		double totalTotales=0;
		
		//RESTAR LAS DEVOLUCIONES.
		for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
		{
			total=0;
			for(int x=0; x<vectorKeysFormasPago.size(); x++)
			{
				//total+= (Double.parseDouble(mapa.get(vectorKeysFormasPago.get(x)+"_"+w).toString())-Utilidades.convertirADouble(""+mapaDevoluciones.get(vectorConsecutivoFormasPago.get(x))));
				if(Utilidades.convertirAEntero(vectorConsecutivoFormasPago.get(x)+"")==1)
				{
					double temporal=((Double.parseDouble(mapa.get(vectorKeysFormasPago.get(x)+"_"+w).toString())-Utilidades.convertirADouble(""+mapaDevoluciones.get(vectorConsecutivoFormasPago.get(x)+"_"+mapa.get("consecutivocierre_"+w)))));
					mapa.put(vectorKeysFormasPago.get(x)+"_"+w,temporal+"");
				}
			}
		}
		for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
		{
			total=0;
			for(int x=0; x<vectorKeysFormasPago.size(); x++)
			{
				total+= Double.parseDouble(mapa.get(vectorKeysFormasPago.get(x)+"_"+w).toString());
			}
			totalTotales+=total;
			mapa.put("TOTAL_CIERRE_"+w, total);
			logger.info("TOTAL_CIERRE_"+w+" -->"+total);
		}
		logger.info("TOTAL_TOTALES-->"+totalTotales);
		mapa.put("TOTAL_TOTALES", totalTotales);
		
		
		//LOS TOTALES DEL TRASLADO X FORMA PAGO MANEJAN EL INDICE SEGUN EL TAMANIO DEL VECTOR
		Vector vectorTotalesFormasPago= new Vector();
		for(int x=0; x<vectorKeysFormasPago.size(); x++)
		{
			total=0;
			for(int w=0; w<Integer.parseInt(mapa.get("numRegistros").toString()); w++)
			{
				total+= Double.parseDouble(mapa.get(vectorKeysFormasPago.get(x)+"_"+w).toString()); 	
			}
			vectorTotalesFormasPago.add(total);
		}
		
		mapa.put("KEYS_FORMAS_PAGO_VECTOR", vectorKeysFormasPago);	
		mapa.put("NOMBRES_FORMAS_PAGO_VECTOR", vectorNombreFormasPago);
		mapa.put("CONSECUTIVOS_FORMAS_PAGO_VECTOR", vectorConsecutivoFormasPago);
		mapa.put("TOTALES_FORMAS_PAGO_VECTOR", vectorTotalesFormasPago);
		
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param consulta
	 * @return
	 */
	private static HashMap obtenerMapa(Connection con, String consulta)
	{
		HashMap mapa= new HashMap();
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	private static HashMap obtenerDevolucionesFormasPago(Connection con, String consulta)
	{
		HashMap mapa= new HashMap();
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			while(rs.next())
			{
				mapa.put(rs.getString("formapago")+"_"+rs.getString("consecutivo"), rs.getString("total"));
			}
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * metodo que inserta el traslado basico y devuelve el consecutivo de insercion, si es "" entonces error
	 * @param con
	 * @param codigoInstitucion
	 * @param fechsTraslado
	 * @param cajaPpal
	 * @param cajaMayor
	 * @param loginUsuario
	 * @param fechaGeneracionTraslado
	 * @param horaGeneracionTraslado
	 * @return
	 */
	public static String insertarTrasladoCaja(	Connection con,
												int codigoInstitucion, 
												String fechaTraslado, 
												String cajaPpal, 
												String cajaMayor, 
												String loginUsuario, 
												String fechaGeneracionTraslado, 
												String horaGeneracionTraslado)
	{
		try
        {
			String consecutivoAInsertar=UtilidadBD.obtenerValorConsecutivoDisponible(ConstantesBD.nombreConsecutivoTrasladosCaja,codigoInstitucion);
	 	    boolean insertoConsecutivo=UtilidadBD.cambiarUsoFinalizadoConsecutivo(con,ConstantesBD.nombreConsecutivoTrasladosCaja, codigoInstitucion, consecutivoAInsertar, ConstantesBD.acronimoSi, ConstantesBD.acronimoSi);
		    if(insertoConsecutivo)
			{	
	            PreparedStatementDecorator ps=  new PreparedStatementDecorator(con.prepareStatement(insertarTrasladoCaja,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            ps.setDouble(1, Double.parseDouble(consecutivoAInsertar));
	            ps.setInt(2, codigoInstitucion);
	            ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaTraslado)));
	            ps.setInt(4, Integer.parseInt(cajaPpal));
	            ps.setInt(5, Integer.parseInt(cajaMayor));
	            ps.setString(6, loginUsuario);
	            ps.setDate(7, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaGeneracionTraslado)));
	            ps.setString(8, horaGeneracionTraslado);
	            
	            if(ps.executeUpdate()>0)
	                return consecutivoAInsertar;
			}
		    else
		    	logger.warn("Error en la actualizacion del consecutivo del traslado caja");
        }
        catch(SQLException e)
        {
            logger.warn(e+" Error en la actualizacion de datos del traslado caja:"+e.toString() );
        }
        return "";
	}
	
	/**
	 * 
	 * @param con
	 * @param trasladosCajaMap
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean insertarDetalleTrasladoCaja(Connection con, HashMap trasladosCajaMap, String consecutivoTrasladoCaja, int codigoInstitucion)
	{
		PreparedStatementDecorator ps;
		try 
		{
			Vector vectorConsecutivosFormasPago= (Vector)trasladosCajaMap.get("CONSECUTIVOS_FORMAS_PAGO_VECTOR") ;
			Vector vectorTotalesFormaspago= (Vector)trasladosCajaMap.get("TOTALES_FORMAS_PAGO_VECTOR");
			//es size de los dos vectores es el mismo
			for(int x=0; x<vectorConsecutivosFormasPago.size(); x++)
			{	
				logger.info("************************************************************");
				ps =  new PreparedStatementDecorator(con.prepareStatement(insertarDetalleTrasladoCaja,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, Double.parseDouble(UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_det_tras_caja")+""));
				ps.setDouble(2, Double.parseDouble(consecutivoTrasladoCaja));
				logger.info("consecutivoTrasladoCaja-->"+consecutivoTrasladoCaja);
				ps.setInt(3, codigoInstitucion);
				logger.info("codigoInstitucion-->"+codigoInstitucion);
				ps.setInt(4, Integer.parseInt(vectorConsecutivosFormasPago.get(x).toString()));
				ps.setDouble(5, Double.parseDouble(vectorTotalesFormaspago.get(x).toString()));
				if(ps.executeUpdate()<=0)
					return false;
			}	
		} 
		catch (SQLException e) 
		{
			logger.info("Error en la insercion del detalle del traslado caja "+e.toString());
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param trasladosCajaMap
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean actualizarCierreCaja(Connection con, HashMap trasladosCajaMap, String consecutivoTrasladoCaja, int codigoInstitucion)
	{
		for(int w=0; w<Integer.parseInt(trasladosCajaMap.get("numRegistros").toString()); w++)
		{
			PreparedStatementDecorator ps;
			try 
			{
				ps =  new PreparedStatementDecorator(con.prepareStatement(actualizarCierreCaja,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps.setDouble(1, Double.parseDouble(consecutivoTrasladoCaja));
				ps.setString(2, trasladosCajaMap.get("consecutivocierre_"+w).toString());
				ps.setInt(3, codigoInstitucion);
				if(ps.executeUpdate()<=0)
					return false;
			} 
			catch (SQLException e) 
			{
				logger.info("Error en la actualizacion del cierre de caja con el atributo traslado caja "+e.toString());
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @param codigoInstitucion
	 * @return
	 */
	public static HashMap listado(Connection con, HashMap criteriosBusquedaMap, int codigoInstitucion)
	{
		HashMap mapa= new HashMap();
		String consulta=	"SELECT " +
								"tc.consecutivo as consecutivotraslado, " +
								"to_char(tc.fecha_traslado, 'DD/MM/YYYY') as fechatraslado, " +
								"c1.descripcion as cajappal, " +
								"c1.consecutivo as consecutivocajappal, " +
								"c2.descripcion as cajamayor, " +
								"c2.consecutivo as consecutivocajamayor, " +
								"(SELECT sum(dtc.total) FROM det_traslados_caja dtc WHERE dtc.traslado_caja=tc.consecutivo and dtc.institucion=tc.institucion) as totaltraslado  " +
							"FROM " +
								"traslados_caja tc " +
								"INNER JOIN cajas c1 ON (c1.consecutivo=tc.caja_ppal)  " +
								"INNER JOIN cajas c2 ON (c2.consecutivo=tc.caja_mayor) " +
								"WHERE 1=1 ";
		
		if(criteriosBusquedaMap.containsKey("rangoInicial") && criteriosBusquedaMap.containsKey("rangoFinal"))
		{
			if(!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("rangoInicial").toString()) && !UtilidadTexto.isEmpty(criteriosBusquedaMap.get("rangoFinal").toString()))
			{
				consulta+=" and tc.consecutivo >= '"+criteriosBusquedaMap.get("rangoInicial").toString()+"' AND  tc.consecutivo <= '"+criteriosBusquedaMap.get("rangoFinal").toString()+"' ";
			}
		}
		if(criteriosBusquedaMap.containsKey("fechaInicial") && criteriosBusquedaMap.containsKey("fechaFinal"))
		{
			if(!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("fechaInicial").toString()) && !UtilidadTexto.isEmpty(criteriosBusquedaMap.get("fechaFinal").toString()))
			{
				consulta+=" and tc.fecha_traslado >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"' AND  tc.fecha_traslado <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"' ";
			}
		}
		if(criteriosBusquedaMap.containsKey("cajaPpal"))
		{
			if(!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("cajaPpal").toString()))
			{
				consulta+=" and tc.caja_ppal = "+criteriosBusquedaMap.get("cajaPpal")+" ";
			}
		}
		if(criteriosBusquedaMap.containsKey("cajaMayor"))
		{
			if(!UtilidadTexto.isEmpty(criteriosBusquedaMap.get("cajaMayor").toString()))
			{
				consulta+=" and tc.caja_mayor = "+criteriosBusquedaMap.get("cajaMayor")+" ";
			}
		}
		logger.info("\n\n\n CONSULTA BUSQUEDA TRASLADO -->"+consulta+"\n\n\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivoTrasladoCaja
	 * @param codigoInstitucion
	 * @return
	 */
	public static String[] obtenerFechaHoraUsuarioGeneracionTraslado(Connection con, String consecutivoTrasladoCaja, int codigoInstitucion)
	{
		String consulta=" SELECT to_char(fecha_grabacion, 'DD/MM/YYYY') as fecha, substr(hora_grabacion||'', 1, 5) as hora, getnombreusuario(usuario) as usuario FROM traslados_caja WHERE consecutivo=? and institucion=?";
		String [] resultado={"","",""};
		PreparedStatementDecorator ps;
		try 
		{
			ps =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDouble(1, Double.parseDouble(consecutivoTrasladoCaja));
			ps.setInt(2, codigoInstitucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				resultado[0]=rs.getString(1);
				resultado[1]=rs.getString(2);
				resultado[2]=rs.getString(3);
			}
			
		} 
		catch (SQLException e) 
		{
			logger.info("Error en obtenerFechaHoraGeneracionTraslado traslado caja "+e.toString());
			e.printStackTrace();
		}
		return resultado;
	}
	
	
	
}