package com.princetonsa.dao.sqlbase.parametrizacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dto.salas.DtoColor;
import com.princetonsa.dto.salas.DtoGases;

import util.ConstantesBD;
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
public class SqlBaseGasesHojaAnestesiaDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseGasesHojaAnestesiaDao.class);
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> cargarSubseccionesGases (Connection con, int numeroSolicitud, int centroCosto, int institucion)
	{
		HashMap<Object, Object> mapaSecciones= new HashMap<Object, Object>();
		mapaSecciones.put("numRegistros", "0");
		int tipo=1;
		
		//1. CARGAMOS LOS INSERTADOS EN BD
		while((Utilidades.convertirAEntero(mapaSecciones.get("numRegistros").toString())==0) || tipo==2)
		{	
			mapaSecciones=  obtenerSubseccionesGases(con, numeroSolicitud, centroCosto, institucion, tipo);
			tipo++;
		}
		return mapaSecciones;
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
	private static HashMap<Object, Object> obtenerSubseccionesGases(Connection con, int numeroSolicitud, int centroCosto, int institucion, int tipo)
	{
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		//primero se carga los historicos, 
		String consultaStr=	"";
		
		consultaStr=	"(SELECT " +
							"DISTINCT " +
							"gaic.codigo AS codigo_gas_inst_cc, " +
							"gaic.codigo_gas as gas, " +
							"coalesce(ga.nombre, '') AS nombre, " +
							"ga.orden " +
						"FROM " +
							"gases_hoja_anestesia gha " +
							"INNER JOIN gases_anest_inst_cc gaic ON(gha.codigo_gas_inst_cc=gaic.codigo) " +
							"INNER JOIN gases_anestesia ga ON (gaic.codigo_gas=ga.codigo) " +
							"INNER JOIN tipos_color tc ON (ga.color=tc.codigo) " +
						"WHERE " +
							"gha.numero_solicitud="+numeroSolicitud+" " +
							//no funciona en oracle// "ORDER BY ga.orden" +
						" ) " +
						"UNION ";
		
		if(tipo==1)
		{
			consultaStr+=	"(SELECT " +
								"DISTINCT " +
								"gaic.codigo AS codigo_gas_inst_cc, " +
								"gaic.codigo_gas as gas, " +
								"coalesce(ga.nombre, '') AS nombre, " +
								"ga.orden " +
							"FROM " +
								"gases_anest_inst_cc gaic " +
								"INNER JOIN gases_anestesia ga ON (gaic.codigo_gas=ga.codigo) " +
								"INNER JOIN tipos_color tc ON (ga.color=tc.codigo) " +
							"WHERE " +
								"gaic.centro_costo="+centroCosto+" " +
								"and gaic.institucion="+institucion+" " +
								"and gaic.activo='"+ConstantesBD.acronimoSi+"' "+
								//no funciona en oracle// "ORDER BY ga.orden" +
							" ) ";
		}
		else if(tipo==2)
		{
			consultaStr=	"(SELECT " +
								"DISTINCT " +
								"gaic.codigo AS codigo_gas_inst_cc, " +
								"gaic.codigo_gas as gas, " +
								"coalesce(ga.nombre, '') AS nombre, " +
								"ga.orden " +
							"FROM " +
								"gases_anest_inst_cc gaic " +
								"INNER JOIN gases_anestesia ga ON (gaic.codigo_gas=ga.codigo) " +
								"INNER JOIN tipos_color tc ON (ga.color=tc.codigo) " +
							"WHERE " +
								"gaic.centro_costo is null "+
								"and gaic.institucion="+institucion+" " +
								"and gaic.activo='"+ConstantesBD.acronimoSi+"' "+
								//no funciona en oracle// "ORDER BY ga.orden " +
							") ";
		}
		
		logger.info("\n obtenerSubseccionesgases->"+consultaStr+"\n");
		
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
	 * @param codigoEvento
	 * @return
	 */
	public static DtoGases cargarGas(Connection con, int codigoGas)
	{
		DtoGases dto= new DtoGases();
		String cadena= "SELECT ga.nombre, ga.orden, ga.obligatorio, ga.convencion, ga.color, tc.descripcion as nombrecolor, tc.valor_hexa, ga.cantidad_min_litros, ga.cantidad_max_litros,  ga.lleva_fio2, ga.lleva_gas_anestesico from gases_anestesia ga inner join tipos_color tc on(tc.codigo=ga.color) WHERE ga.codigo=?";
		//logger.info("cargar gas-->"+cadena+" ->"+codigoGas);
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoGas);
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoColor dtoColor= new DtoColor(rs.getInt("color"), rs.getString("nombrecolor"), rs.getString("valor_hexa"));
				dto.setConvencion(rs.getString("convencion"));
				dto.setDtoColor(dtoColor);
				dto.setNombre(rs.getString("nombre"));
				dto.setObligatorio(UtilidadTexto.getBoolean(rs.getString("obligatorio")));
				dto.setOrden(rs.getInt("orden"));
				dto.setCodigo(codigoGas);
				dto.setCantidadMaxLitros(rs.getInt("cantidad_max_litros"));
				dto.setCantidadMinLitros(rs.getInt("cantidad_min_litros"));
				dto.setLlevaFio2(UtilidadTexto.getBoolean(rs.getString("lleva_fio2")));
				dto.setLlevaGasAnestesico(UtilidadTexto.getBoolean(rs.getString("lleva_gas_anestesico")));
			}
			
		} 
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		
		return dto;
	}
	
	/**
	 * 
	 * @param con
	 * @param numeroSolicitud
	 * @param codigoEventoInstCC
	 * @param codigoEvento
	 * @return
	 */
	public static HashMap<Object, Object> cargarGasHojaAnestesia (Connection con, int numeroSolicitud, int codigoGasInstCC, int codigoGas)
	{
		//primero se carga el dto de eventos
		DtoGases dtoGases= cargarGas(con, codigoGas);
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		
		// como puede manejar n registros entonces no se carga un Dto sino un mapa
		String cadena= "SELECT " +
							"gha.codigo as codigogashojaanestesia, " +
							"to_char(gha.fecha_inicial, 'DD/MM/YYYY') as fechainicial, " +
							"gha.hora_inicial as horainicial, " +
							"gha.suspendido as suspendido, " +
							"gha.cantidad_litros as cantidad_litros, " +
							"coalesce(gha.gas_anestesico, "+ConstantesBD.codigoNuncaValido+") as gas_anestesico, " +
							"case when gha.gas_anestesico is null then '' else (select tga.nombre from tipos_gases_anestesicos tga where tga.codigo=gha.gas_anestesico) end as nombre_gas_anestesico, " +
							"coalesce(gha.fio2, "+ConstantesBD.codigoNuncaValido+") as fio2, " +
							"graficar as graficar, " +
							" '"+ConstantesBD.acronimoSi+"' as estabd " +
						"FROM " +
							"gases_hoja_anestesia gha " +
						"WHERE " +
							"numero_solicitud =? " +
							"AND codigo_gas_inst_cc=? " +
							"order by fechainicial, horainicial  ";
		
		
		//logger.info("\ncargarGasHojaAnestesia->"+cadena+" -->numsol="+numeroSolicitud+"  gassCC="+codigoGasInstCC+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setInt(2, codigoGasInstCC);
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
		}	
		catch (SQLException e) 
		{
			e.printStackTrace();
			return null;
		}
		
		mapa.put("DTOGAS", dtoGases);
		
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
    	String cadena="INSERT INTO gases_hoja_anestesia 	(codigo, " +     			//1
    														"numero_solicitud, " +		//2
    														"codigo_gas_inst_cc, " +	//3
    														"fecha_inicial, " +			//4
    														"hora_inicial, " +			//5
    														"suspendido, " +			//6
    														"cantidad_litros, " +		//7
    														"gas_anestesico, " +		//8
    														"fio2, " +					//9	
    														"fecha_modifica, " +		
    														"hora_modifica, " +			
    														"usuario_modifica, " +		//10
    														"graficar) " +				//11
    														"values (?, ?, ?," +
    																" ?, ?, ?," +
    																" ?, ?, ?, " +
    																"CURRENT_DATE, " +
    																""+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +
    																"?, ?)";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_gases_hoja_anestesia");
			ps.setInt(1, codigo);
			ps.setInt(2, Utilidades.convertirAEntero(mapa.get("numerosolicitud").toString()));
			ps.setInt(3, Utilidades.convertirAEntero(mapa.get("codigogasinstcc").toString()));
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechainicial").toString())));
			ps.setString(5, mapa.get("horainicial").toString());
			
			ps.setString(6, mapa.get("suspendido")+"");
			
			ps.setInt(7, Utilidades.convertirAEntero(mapa.get("cantidadlitros").toString()));
			
			if(Utilidades.convertirAEntero(mapa.get("gasanestesico").toString())>0)
				ps.setInt(8, Utilidades.convertirAEntero(mapa.get("gasanestesico").toString()));
			else
				ps.setNull(8, Types.INTEGER);
			
			if(Utilidades.convertirAEntero(mapa.get("fio2").toString())>0)
				ps.setInt(9, Utilidades.convertirAEntero(mapa.get("fio2").toString()));
			else
				ps.setNull(9, Types.INTEGER);
			
			ps.setString(10, mapa.get("loginusuario")+"");
			ps.setString(11, mapa.get("graficar")+"");
				
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
    	String cadena="UPDATE gases_hoja_anestesia SET " +
    						"fecha_inicial=?, " +			//1
							"hora_inicial=?, " +			//2
							"suspendido=?, " +				//3
							"cantidad_litros=?, " +			//4
							"gas_anestesico=?, " +			//5
							"fio2=?, " +					//6
							"fecha_modifica= CURRENT_DATE, " +		
							"hora_modifica= "+ValoresPorDefecto.getSentenciaHoraActualBD()+", " +							
							"usuario_modifica=?, " +		//7
							"graficar=? " +					//8
						"WHERE "+
							"codigo=? "; 					//9
							
							
    	//logger.info("\n Modificar-->"+cadena+" MAOPA-->"+mapa+"\n");
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapa.get("fechainicial").toString())));
			ps.setString(2, mapa.get("horainicial")+"");
			ps.setString(3, mapa.get("suspendido")+"");
			ps.setInt(4, Utilidades.convertirAEntero(mapa.get("cantidadlitros")+""));
			
			if(Utilidades.convertirAEntero(mapa.get("gasanestesico").toString())>0)
				ps.setInt(5, Utilidades.convertirAEntero(mapa.get("gasanestesico").toString()));
			else
				ps.setNull(5, Types.INTEGER);
			
			if(Utilidades.convertirAEntero(mapa.get("fio2").toString())>0)
				ps.setInt(6, Utilidades.convertirAEntero(mapa.get("fio2").toString()));
			else
				ps.setNull(6, Types.INTEGER);
			
			ps.setString(7, mapa.get("loginusuario")+"");
			ps.setString(8, mapa.get("graficar")+"");
		
			ps.setInt(9, Utilidades.convertirAEntero(mapa.get("codigogashojaanestesia")+""));
			
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
     * @return
     */
    public static HashMap<Object, Object> obtenerTiposGasesAnestesicos(Connection con)
    {
    	HashMap<Object, Object> mapa= new HashMap<Object, Object>();
    	mapa.put("numRegistros", "0");
    	
    	String consulta="select codigo, nombre from tipos_gases_anestesicos ";
    	
    	PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
     * @param graficar
     * @return
     */
	public static HashMap<Object, Object> cargarGasesHojaAnestesia (Connection con, int numeroSolicitud, String graficar)
	{
		//primero se carga el dto de eventos
		HashMap<Object, Object> mapa= new HashMap<Object, Object>();
		
		
		String cadena= 	"SELECT " +
							"gha.codigo as codigogashojaanestesia, " +
							"to_char(gha.fecha_inicial, 'DD/MM/YYYY') as fecha, " +
							"gha.hora_inicial as hora, " +
							"gha.suspendido as suspendido, " +
							"gha.cantidad_litros as cantidad_litros, " +
							"coalesce(gha.gas_anestesico, "+ConstantesBD.codigoNuncaValido+") as gas_anestesico, " +
							"case when gha.gas_anestesico is null then '' else (select tga.nombre from tipos_gases_anestesicos tga where tga.codigo=gha.gas_anestesico) end as nombre_gas_anestesico, " +
							"coalesce(gha.fio2, "+ConstantesBD.codigoNuncaValido+") as fio2, " +
							"gha.graficar as graficar, " +
							"ga.convencion as convencion, " +
							"t.valor_hexa as valor_hexa " +
						"FROM " +
							"gases_hoja_anestesia gha " +
							"inner join gases_anest_inst_cc gic on(gic.codigo=gha.codigo_gas_inst_cc) " +
							"inner join gases_anestesia ga on(ga.codigo=gic.codigo_gas)" +
							"inner join tipos_color t on(t.codigo=ga.color) " +
						"WHERE " +
							"numero_solicitud =? ";
		
		if(graficar.equals(ConstantesBD.acronimoSi))
			cadena+="and gha.graficar='"+ConstantesBD.acronimoSi+"' ";
		else if(graficar.equals(ConstantesBD.acronimoNo)) 
			cadena+="and gha.graficar='"+ConstantesBD.acronimoNo+"' ";
							
		cadena+=	" order by fecha, hora ";
		logger.info("\ncargarGasesHojaAnestesia->"+cadena+" -->numsol="+numeroSolicitud+"\n");
		
		PreparedStatementDecorator ps=null;
		try 
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
}
