package com.princetonsa.dao.sqlbase.parametrizacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dto.salas.DtoMonitoreoHemoDinamica;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * 
 * @author wilson
 *
 */
public class SqlBaseMonitoreoHemodinamicaDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseMonitoreoHemodinamicaDao.class);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @return
	 */
	public static HashMap<Object, Object> obtenerListadoMonitoreos(Connection con, int numeroSolicitud)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		String consulta="SELECT " +
							"codigo AS codigo_mon_hemo_hoja_anestesia, " +
							"to_char(fecha, 'DD/MM/YYYY') as fecha, " +
							"hora AS hora " +
						"FROM " +
							"mon_hemo_hoja_anestesia " +
						"WHERE " +
							"numero_solicitud=? ";
		
		logger.info("\n obtenerListadoMonitoreos->"+consulta+" \n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
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
	 * @param codigo
	 * @return
	 */
	public static DtoMonitoreoHemoDinamica cargarMonitoreoHemoDto(Connection con, int codigo)
	{
		DtoMonitoreoHemoDinamica dto= new DtoMonitoreoHemoDinamica();
		String consulta="SELECT " +
							"codigo as codigo, " +
							"nombre as nombre, " +
							"orden as orden, " +
							"obligatorio as obligatorio, " +
							"coalesce(val_maximo, "+ConstantesBD.codigoNuncaValido+") as val_maximo, " +
							"coalesce(val_minimo, "+ConstantesBD.codigoNuncaValido+") as val_minimo," +
							"coalesce(formula,'') as formula, " +
							"tipo_campo as tipo_campo, " +
							"coalesce(numero_decimales, "+ConstantesBD.codigoNuncaValido+") as numero_decimales, " +
							"coalesce(permite_negativos, '"+ConstantesBD.acronimoNo+"') as permite_negativos, " +
							"coalesce(onchange, '') as onchange, " +
							"coalesce(onkeyup,'') as onkeyup, " +
							"coalesce(onkeydown,'') as onkeydown, " +
							"coalesce(mayor, '') as mayor, " +
							"coalesce(menor, '') as menor, " +
							"coalesce(campo_padre, "+ConstantesBD.codigoNuncaValido+") as campo_padre " +
						"FROM mon_hemo_anestesia " +
							"WHERE codigo=? ";
		
		logger.info("\n cargarMonitoreoHemoDto->"+consulta+" codi->"+codigo);
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			if(rs.next())
			{
				dto.setCodigo(rs.getInt("codigo"));
				dto.setFormula(rs.getString("formula"));
				dto.setMayor(rs.getString("mayor"));
				dto.setMenor(rs.getString("menor"));
				dto.setNombre(rs.getString("nombre"));
				dto.setNumeroDecimales(rs.getInt("numero_decimales"));
				dto.setObligatorio(UtilidadTexto.getBoolean(rs.getString("obligatorio")));
				dto.setOnchange(rs.getString("onchange"));
				dto.setOnkeydown(rs.getString("onkeydown"));
				dto.setOnkeyup(rs.getString("onkeyup"));
				dto.setOrden(rs.getInt("orden"));
				dto.setPermiteNegativos(UtilidadTexto.getBoolean(rs.getString("permite_negativos")));
				dto.setCampoPadre(rs.getInt("campo_padre"));
				dto.setTipoCampo(rs.getString("tipo_campo"));
				dto.setValorMaximo(rs.getDouble("val_maximo"));
				dto.setValorMinimo(rs.getDouble("val_minimo"));
				
				if(dto.getTipoCampo().equals("SELE"))
				{
					dto.setOpciones(cargarOpciones(con, dto.getCodigo()));
				}	
				
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return dto;
	}

	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static ArrayList<InfoDatosInt> cargarOpciones(Connection con, int codigoMonHemo) 
	{
		String consulta= "SELECT codigo, nombre from opciones_mon_hemo where codigo_mon_hemo=? order by nombre ";
		ArrayList<InfoDatosInt> opcionesArray= new ArrayList<InfoDatosInt>();
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoMonHemo);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				InfoDatosInt infoDatos= new InfoDatosInt();
				infoDatos.setCodigo(rs.getInt("codigo"));
				infoDatos.setNombre(rs.getString("nombre"));
				opcionesArray.add(infoDatos);
			}
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		return opcionesArray;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreoHemoHojaAnestesia
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarMonitoreo(Connection con, int codigoMonitoreoHemoHojaAnestesia, int centroCosto, int institucion)
	{
		String consulta="	SELECT " +
								"tabla.codigo_campo, " +
								"tabla.valor, " +
								"tabla.cod_mon_hemo_anes, " +
								"tabla.cod_mon_hemo_anes_inst_cc, " +
								"tabla.estabd " +
							"FROM (" +
									"SELECT " +
										"dm.codigo_campo as codigo_campo, " +
										"dm.valor||'' as valor, " +
										"mh.codigo_mon_hemo as cod_mon_hemo_anes, " +
										"mh.codigo as cod_mon_hemo_anes_inst_cc, " +
										"'"+ConstantesBD.acronimoSi+"' as estabd,  " +
										"mo.orden as orden " +
									"FROM " +
										"det_mon_hemo_hoja_anes dm " +
										"INNER JOIN mon_hemo_anes_inst_cc mh ON(mh.codigo=dm.codigo_campo) " +
										"INNER JOIN mon_hemo_anestesia mo ON(mo.codigo=mh.codigo_mon_hemo) " +
									"WHERE " +
										"dm.cod_mon_hemo_hoja_anes= "+codigoMonitoreoHemoHojaAnestesia+" ";
		
		if(existeParametrizacionXCentroCostoInst(con, centroCosto, institucion))
		{	
			consulta+= 				"UNION " +
									"SELECT " +
										""+ConstantesBD.codigoNuncaValido+" as codigo_campo, " +
										"'' as valor, " +
										"mha.codigo_mon_hemo as cod_mon_hemo_anes, " +
										"mha.codigo as cod_mon_hemo_anes_inst_cc, " +
										"'"+ConstantesBD.acronimoNo+"' as estabd, " +
										"mo.orden as orden " +
									"FROM " +
										"mon_hemo_anes_inst_cc mha " +
										"INNER JOIN mon_hemo_anestesia mo ON(mo.codigo=mha.codigo_mon_hemo) " +
									"WHERE " +
										"mha.centro_costo="+centroCosto+" and mha.institucion="+institucion+" " +
										"and mha.activo='"+ConstantesBD.acronimoSi+"' " +
										"and mha.codigo_mon_hemo not in(	SELECT " +
																				"mh1.codigo_mon_hemo as cod_mon_hemo_anes " +
																			"FROM " +
																				"det_mon_hemo_hoja_anes dm1 " +
																				"INNER JOIN mon_hemo_anes_inst_cc mh1 ON(mh1.codigo=dm1.codigo_campo) " +
																			"WHERE dm1.cod_mon_hemo_hoja_anes= "+codigoMonitoreoHemoHojaAnestesia+")";
		}
		else
		{
			consulta+= 				"UNION " +
									"SELECT " +
										""+ConstantesBD.codigoNuncaValido+" as codigo_campo, " +
										"'' as valor, " +
										"mha.codigo_mon_hemo as cod_mon_hemo_anes, " +
										"mha.codigo as cod_mon_hemo_anes_inst_cc, " +
										"'"+ConstantesBD.acronimoNo+"' as estabd, " +
										"mo.orden as orden " +
									"FROM " +
										"mon_hemo_anes_inst_cc mha " +
										"INNER JOIN mon_hemo_anestesia mo ON(mo.codigo=mha.codigo_mon_hemo) " +
									"WHERE " +
										"mha.centro_costo is null and mha.institucion="+institucion+" " +
										"and mha.activo='"+ConstantesBD.acronimoSi+"' " +
										"and mha.codigo_mon_hemo not in(	SELECT " +
																				"mh1.codigo_mon_hemo as cod_mon_hemo_anes " +
																			"FROM " +
																				"det_mon_hemo_hoja_anes dm1 " +
																				"INNER JOIN mon_hemo_anes_inst_cc mh1 ON(mh1.codigo=dm1.codigo_campo) " +
																			"WHERE dm1.cod_mon_hemo_hoja_anes= "+codigoMonitoreoHemoHojaAnestesia+")"; 	
		}
		
		consulta+=			")" +
							"tabla order by tabla.orden ";
		
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		mapa.put("numRegistros", "0");
		
		logger.info("\n cargarMonitoreo->"+consulta+" codigoMonitoreoHemoHojaAnestesia->"+codigoMonitoreoHemoHojaAnestesia);
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			
			for(int w=0; w<Utilidades.convertirAEntero(mapa.get("numRegistros").toString()); w++)
			{
				mapa.put("DTO_"+w, cargarMonitoreoHemoDto(con, Utilidades.convertirAEntero(mapa.get("cod_mon_hemo_anes_"+w)+"")));
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
	 * @param codigoCentroCosto
	 * @param institucion
	 * @return
	 */
	private static boolean existeParametrizacionXCentroCostoInst(Connection con, int codigoCentroCosto, int institucion)
	{
		String consulta="SELECT codigo from mon_hemo_anes_inst_cc where centro_costo=? and institucion=? and activo='"+ConstantesBD.acronimoSi+"' ";
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoCentroCosto);
			ps.setInt(2, institucion);
			if(ps.executeQuery().next())
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
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public static boolean insertarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo, String valor)
	{
    	String cadena="INSERT INTO det_mon_hemo_hoja_anes 	(cod_mon_hemo_hoja_anes, " +	//1
    														"codigo_campo, " +				//2
    														"valor" +						//3
    														") " +
    														"values (?, ?, ?) ";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoMonitoreo);
			ps.setInt(2, codigoCampo);
			ps.setString(3, valor);
			
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
	 * @param numeroSolicitud
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @return
	 */
	public static int insertarMonHemoHojaAnestesia(Connection con, int numeroSolicitud, String fecha, String hora, String loginUsuario)
	{
    	String cadena="INSERT INTO mon_hemo_hoja_anestesia 	(codigo, " +			//1
    														"numero_solicitud, " +	//2
    														"fecha, " +				//3
    														"hora, " +				//4
    														"fecha_modifica, " +		
    														"hora_modifica, " +		
    														"usuario_modifica "+	//5
    														") " +
    														"values (?, ?, ?, ?, CURRENT_DATE, " +
    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+",  ?) ";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_mon_hemo_hoja_anes");
			ps.setInt(1, codigo);
			ps.setInt(2, numeroSolicitud);
			ps.setDate(3, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			ps.setString(4, hora);
			ps.setString(5, loginUsuario);
			if(ps.executeUpdate()>0)
				return codigo;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return ConstantesBD.codigoNuncaValido;
    }
	
	/**
	 * 
	 * @param con
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public static boolean modificarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo, String valor)
	{
    	String cadena="UPDATE det_mon_hemo_hoja_anes SET valor=? where cod_mon_hemo_hoja_anes=? and codigo_campo=? ";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, valor);
			ps.setInt(2, codigoMonitoreo);
			ps.setInt(3, codigoCampo);
			
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
	 * @param fecha
	 * @param hora
	 * @param loginUsuario
	 * @param codigoMonHemoHojaAnes
	 * @return
	 */
	public static boolean modificarMonHemoHojaAnestesia(Connection con, String fecha, String hora, String loginUsuario, int codigoMonHemoHojaAnes)
	{
    	String cadena="UPDATE mon_hemo_hoja_anestesia SET	" +
    						"fecha=?, " +//1
    						"hora=?, " +//2
    						"fecha_modifica=CURRENT_DATE, " +
    						"hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +		
    						"usuario_modifica=? " +//3
    						"WHERE codigo=? "; //4	
    						
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			ps.setString(2, hora);
			ps.setString(3, loginUsuario);
			ps.setInt(4, codigoMonHemoHojaAnes);
			
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
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public static boolean eliminarDetMonHemoHojaAnestesia(Connection con, int codigoMonitoreo, int codigoCampo)
	{
    	String cadena="DELETE FROM det_mon_hemo_hoja_anes where cod_mon_hemo_hoja_anes=? ";
    	
    	if(codigoCampo>0)
    		cadena+="and codigo_campo= "+codigoCampo;
    	
    	logger.info("\n eliminarDetMonHemoAnestesia->"+cadena+"  cod->"+codigoMonitoreo+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoMonitoreo);
			
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
	 * @param codigoMonitoreo
	 * @param codigoCampo
	 * @param valor
	 * @return
	 */
	public static boolean eliminarMonHemoHojaAnestesia(Connection con, int codigo)
	{
    	String cadena="DELETE FROM mon_hemo_hoja_anestesia where codigo=? ";
    	
    	logger.info("\n eliminarMonHemoHojaAnestesia->"+cadena+" cod->"+codigo+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigo);
			
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
	 * @param numeroSolicitud
	 * @param fecha
	 * @param hora
	 * @param codigoMonitoreoHojaAnestsia
	 * @return
	 */
	public static boolean existeMonitoreoHojaAnestesia(Connection con, int numeroSolicitud, String fecha, String hora, int codigoMonitoreoHojaAnestsia)
	{
		String consulta="SELECT codigo FROM mon_hemo_hoja_anestesia WHERE numero_solicitud=? and fecha=? and hora=? and codigo<>?";
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			ps.setString(3, hora);
			ps.setInt(4, codigoMonitoreoHojaAnestsia);
			if(ps.executeQuery().next())
				return true;
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		return false;
	}
}
