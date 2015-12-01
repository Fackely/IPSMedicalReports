package com.princetonsa.dao.sqlbase.parametrizacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

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
public class SqlBaseSignosVitalesDao 
{
	
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger= Logger.getLogger(SqlBaseSignosVitalesDao.class);
	
	/**
	 * Obtiene los signos vitales, dependiendo si existe informacion histotica en la bd, de los contrario postula el tiempo cero con la parametrizacion
	 * @param con
	 * @param numeroSolicitud
	 * @param centroCosto
	 * @param institucion
	 * @return
	 */
	public static HashMap<Object, Object> obtenerSignosVitalesHojaAnestesia (Connection con, int numeroSolicitud, int centroCosto, int institucion, String graficar)
	{
		//primero debemos evaluar cuales signos vitales han insertado, puede que la parametrización ya halla cambiado,
		//entonces debemos cargar son los insertados, en caso de que no exista info entonces carga los de la parametrizacion
		//1= insertados, 2= xcentro costo especifico, 3= centro costo null
		
		HashMap<Object, Object> mapaTotal= new HashMap<Object, Object>();
		HashMap<Object, Object> mapaCampos= new HashMap<Object, Object>();
		mapaCampos.put("numRegistros", "0");
		
		//1. CARGAMOS LOS INSERTADOS EN BD
		HashMap<Object, Object> mapaTitulos= obtenerTitulosSignosVitalesHojaAnestesia(con, numeroSolicitud, centroCosto, institucion, 1);
		if(mapaTitulos!=null && Utilidades.convertirAEntero(mapaTitulos.get("numRegistros")+"")>0)
			mapaCampos= obtenerCamposSignosVitalesHojaAnestesia(con, numeroSolicitud, mapaTitulos, graficar);
		
		//2. SI ES VACIO BUSCAMOS PARAMETRIZACION POR CENTRO COSTO - INSTITUCION
		if(mapaTitulos==null || Utilidades.convertirAEntero(mapaTitulos.get("numRegistros")+"")<=0)
			mapaTitulos= obtenerTitulosSignosVitalesHojaAnestesia(con, numeroSolicitud, centroCosto, institucion, 2);
		
		//3. SI ES VACIO BUSCAMOS PARAMETRIZACION CENTRO COSTO NULL E INSTITUCION
		if(mapaTitulos==null || Utilidades.convertirAEntero(mapaTitulos.get("numRegistros")+"")<=0)
			mapaTitulos= obtenerTitulosSignosVitalesHojaAnestesia(con, numeroSolicitud, centroCosto, institucion, 3);
		
		//4. iteramos uno a uno los campos para mostrar los de tipo select
		if(mapaTitulos!=null && Utilidades.convertirAEntero(mapaTitulos.get("numRegistros")+"")>0)
		{
			for(int w=0; w<Utilidades.convertirAEntero(mapaTitulos.get("numRegistros")+""); w++)
			{
				int codigoSignoVital= Utilidades.convertirAEntero(mapaTitulos.get("signo_vital_"+w)+"");
				
				HashMap<Object, Object> mapaOpciones= obtenerOpcionesSignoVital(con, codigoSignoVital);
				
				logger.info(mapaOpciones+"");
				
				if(mapaOpciones!=null && Utilidades.convertirAEntero(mapaOpciones.get("numRegistros")+"")>0)
				{
					mapaTotal.put("opciones_"+codigoSignoVital, mapaOpciones);
				}
			}
		}
		
		mapaTotal.put("TITULOS", mapaTitulos);
		mapaTotal.put("CAMPOS", mapaCampos);
		
		return mapaTotal;
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
	@SuppressWarnings("unchecked")
	private static HashMap<Object, Object> obtenerTitulosSignosVitalesHojaAnestesia(Connection con, int numeroSolicitud, int centroCosto, int institucion, int tipo)
	{
		/* 
		 * 	MAPA TITULO:
		 * 
		 * RETORNA ALGO PARECIDO A:
		 * 	 codigo_sv_anest_inst | signo_vital | nombre | unidad_medida | valor_hexa | valor_maximo | valor_minimo | es_requerido
		  	----------------------+-------------+--------+---------------+------------+--------------+--------------+--------------
			                   50 |          12 | T.A.S. | mmHg          | FF9966     |          350 |            0 | f
			                   51 |          13 | T.A.D. | mmHg          | 0000CC     |          350 |            0 | f
			                   52 |          15 | F.C.   | pulsos/min    | FF0000     |          400 |            0 | f
			                   53 |          16 | F.R.   | /min          | FF9966     |          200 |            0 | f
		 **/
		
		String consultaTitulosStr="";
		//String consultaStr="";
		
		//si son los insertados
		if(tipo==1)
		{
			consultaTitulosStr=	"SELECT " +
									"DISTINCT " +
									"svai.codigo AS codigo_sv_anest_inst, " +
									"svai.signo_vital as signo_vital, " +
									"coalesce(sv.nombre, '') AS nombre, " +
									"coalesce(sv.unidad_medida, '') AS unidad_medida, " +
									"tc.valor_hexa AS valor_hexa, " +
									"svai.valor_maximo AS valor_maximo, " +
									"svai.valor_minimo AS valor_minimo, " +
									"svai.es_requerido AS es_requerido," +
									"svai.convencion AS convencion, " +
									"coalesce(sv.formula,'') AS formula " +
								"FROM " +
									"tiempo_signo_vital_anest tsva " +
									"INNER JOIN signo_vital_anes_tiempo svat ON (svat.tiempo_signo_vital_anest=tsva.codigo) " +
									"INNER JOIN signo_vital_anest_inst svai ON(svai.codigo=svat.signo_vital_anest_inst) " +
									"INNER JOIN signos_vitales sv ON (svai.signo_vital=sv.codigo) " +
									"INNER JOIN tipos_color tc ON (svai.tipo_color=tc.codigo) " +
								"WHERE " +
									"tsva.solicitud_hoja_anest="+numeroSolicitud+" " +
								"ORDER BY signo_vital ";
		}
		//si es dos buscar por el centro costo especifico
		else if(tipo==2)
		{
			consultaTitulosStr=	"SELECT " +
									"DISTINCT " +
									"svai.codigo AS codigo_sv_anest_inst, " +
									"svai.signo_vital as signo_vital, " +
									"coalesce(sv.nombre, '') AS nombre, " +
									"coalesce(sv.unidad_medida, '') AS unidad_medida, " +
									"tc.valor_hexa AS valor_hexa, " +
									"svai.valor_maximo AS valor_maximo, " +
									"svai.valor_minimo AS valor_minimo, " +
									"svai.es_requerido AS es_requerido," +
									"svai.convencion as convencion, " +
									"coalesce(sv.formula,'') AS formula " +
								"FROM " +
									"signo_vital_anest_inst svai " +
									"INNER JOIN signos_vitales sv ON (svai.signo_vital=sv.codigo) " +
									"INNER JOIN tipos_color tc ON (svai.tipo_color=tc.codigo) " +
								"WHERE " +
									" svai.centro_costo= "+centroCosto+" "+
									" and svai.institucion = "+institucion+" "+
									" and svai.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
								"ORDER BY signo_vital";
			
		}
		// buscar por el centro costo standar, es decir null
		else
		{
			consultaTitulosStr=	"SELECT " +
									"DISTINCT " +
									"svai.codigo AS codigo_sv_anest_inst, " +
									"svai.signo_vital as signo_vital, " +
									"coalesce(sv.nombre, '') AS nombre, " +
									"coalesce(sv.unidad_medida, '') AS unidad_medida, " +
									"tc.valor_hexa AS valor_hexa, " +
									"svai.valor_maximo AS valor_maximo, " +
									"svai.valor_minimo AS valor_minimo, " +
									"svai.es_requerido AS es_requerido, " +
									"svai.convencion as convencion, " +
									"coalesce(sv.formula,'') AS formula " +
								"FROM " +
									"signo_vital_anest_inst svai " +
									"INNER JOIN signos_vitales sv ON (svai.signo_vital=sv.codigo) " +
									"INNER JOIN tipos_color tc ON (svai.tipo_color=tc.codigo) " +
								"WHERE " +
									" svai.centro_costo is null " +
									" and svai.institucion = " +institucion+" "+
									" and svai.activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" "+
								"ORDER BY signo_vital";	
		}
		
		logger.info("\n obtenerSignosVitales--->"+consultaTitulosStr+"\n");
		
		try
		{
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaTitulosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		
		return null;
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
	@SuppressWarnings("unchecked")
	private static HashMap<Object, Object> obtenerCamposSignosVitalesHojaAnestesia(Connection con, int numeroSolicitud, HashMap<Object, Object> mapaTitulos, String graficar)
	{
		String consultaStr=	"SELECT " +
								"DISTINCT " +
								"tsva.codigo as codigo_tiempo, " +
								"to_char(tsva.fecha, 'DD/MM/YYYY') as fecha, " +
								"tsva.hora as hora, ";
								
		
		for(int w=0; w<Utilidades.convertirAEntero(mapaTitulos.get("numRegistros")+""); w++)
		{
			//si viene de una formula debe calcularla
			if(!UtilidadTexto.isEmpty(mapaTitulos.get("formula_"+w).toString()))
			{
				String formula= mapaTitulos.get("formula_"+w).toString();
				consultaStr+= obtenerSelectValorFormula(con, formula, mapaTitulos);
				consultaStr+= " as valor_"+mapaTitulos.get("signo_vital_"+w)+", ";
			}
			else
			{
				consultaStr+=		"coalesce(salascirugia.getValorSignoVitalAnes("+mapaTitulos.get("codigo_sv_anest_inst_"+w)+", tsva.codigo)||'', '') as valor_"+mapaTitulos.get("signo_vital_"+w)+", ";
			}	
		}
		
		consultaStr+=		"tsva.graficar as graficar, " +
							"tsva.observaciones as observaciones "+
							"FROM " +
								"tiempo_signo_vital_anest tsva " +
								"INNER JOIN signo_vital_anes_tiempo svat ON (svat.tiempo_signo_vital_anest=tsva.codigo) " +
								"INNER JOIN signo_vital_anest_inst svai ON(svai.codigo=svat.signo_vital_anest_inst) " +
								"INNER JOIN signos_vitales sv ON (svai.signo_vital=sv.codigo) " +
							"WHERE " +
								"tsva.solicitud_hoja_anest="+numeroSolicitud+" ";
								
		if(!UtilidadTexto.isEmpty(graficar))
		{
			consultaStr+="AND tsva.graficar='"+graficar+"' ";
		}
								
		consultaStr+= "ORDER BY fecha, hora";
		
		try
		{
			logger.info("\n obtenerCamposSignosVitalesHojaAnestesia-> "+consultaStr+" \n");
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;				
		} 
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param con
	 * @param formula
	 * @param mapaTitulos
	 * @return
	 */
	private static String obtenerSelectValorFormula(Connection con, String formula, HashMap<Object, Object> mapaTitulos) 
	{
		//coalesce(salascirugia.getValorSignoVitalAnes("+mapaTitulos.get("codigo_sv_anest_inst_"+w)+", tsva.codigo)||'', '') as valor_"+mapaTitulos.get("signo_vital_"+w)+", "
		String consulta="(";
		String[] partes= formula.split("_");
		for(int x=0; x<partes.length; x++)
		{
			//si es numero y no contiene + o - entonces es un signo vital y debemos consultarlo
			if(UtilidadTexto.isNumber(partes[x]) && !partes[x].contains("+") && !partes[x].contains("-"))
			{
				for(int w=0; w<Utilidades.convertirAEntero(mapaTitulos.get("numRegistros")+""); w++)
				{
					if(mapaTitulos.get("signo_vital_"+w).toString().equals(partes[x]))
					{
						consulta+= " coalesce(salascirugia.getValorSignoVitalAnes("+mapaTitulos.get("codigo_sv_anest_inst_"+w)+", tsva.codigo), 0) "; 
					}
				}
			}
			else
			{
				consulta+=partes[x];
			}
		}
		consulta+=") ";
		return consulta;
	}

	/**
	 * 
	 * @param con
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean insertarTiempoSignoVitalAnestesia(Connection con, HashMap<Object, Object> mapaSignoVitalAnestesia)
    {
    	String cadena="INSERT INTO tiempo_signo_vital_anest (codigo, solicitud_hoja_anest, observaciones, fecha, hora, fecha_modifica, hora_modifica, usuario_modifica, graficar) " +
    														"values (?, ?, ?, ?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?)";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			int codigoTiempo= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_tiempo_signo_vital_anest");
			
			ps.setInt(1, codigoTiempo);
			ps.setInt(2, Utilidades.convertirAEntero(mapaSignoVitalAnestesia.get("numero_solicitud").toString()));
			ps.setString(3, mapaSignoVitalAnestesia.get("observaciones").toString());
			ps.setDate(4, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapaSignoVitalAnestesia.get("fecha").toString())));
			ps.setString(5, mapaSignoVitalAnestesia.get("hora").toString());
			ps.setString(6, mapaSignoVitalAnestesia.get("login_usuario").toString());
			ps.setString(7, mapaSignoVitalAnestesia.get("graficar").toString());
			
			if(ps.executeUpdate()>0)
			{
				for(int w=0; w<Utilidades.convertirAEntero(mapaSignoVitalAnestesia.get("numSignosVitales").toString()); w++)
				{
					if(UtilidadTexto.isNumber(mapaSignoVitalAnestesia.get("valor_"+w).toString()))
					{	
						if(!insertarValorSignoVitalAnestesia(con, Utilidades.convertirAEntero(mapaSignoVitalAnestesia.get("codigo_sv_anest_inst_"+w).toString()), codigoTiempo, Utilidades.convertirAEntero(mapaSignoVitalAnestesia.get("valor_"+w).toString())))
						{
							logger.info("Error insertando el signo vital-->"+mapaSignoVitalAnestesia.get("nombre_"+w));
							return false;
						}
					}	
				}
				return true;
			}
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
     * @param codSignoVitalAnestInst
     * @param codTiempoSignoVital
     * @param valor
     * @return
     */
	private static boolean insertarValorSignoVitalAnestesia(Connection con, int codSignoVitalAnestInst, int codTiempoSignoVital, int valor)
	{
		String cadena="INSERT INTO signo_vital_anes_tiempo (signo_vital_anest_inst, tiempo_signo_vital_anest, valor) values (?, ?, ?)";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codSignoVitalAnestInst);
			ps.setInt(2, codTiempoSignoVital);
			ps.setInt(3, valor);
			
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
	 * @param mapaSignoVitalAnestesia
	 * @return
	 */
    public static boolean modificarTiempoSignoVitalAnestesia(Connection con, HashMap<Object, Object> mapaSignoVitalAnestesia)
    {
    	String cadena="UPDATE tiempo_signo_vital_anest SET observaciones=?, fecha=?, hora=?, fecha_modifica=CURRENT_DATE, hora_modifica="+ValoresPorDefecto.getSentenciaHoraActualBD()+", usuario_modifica=?, graficar=? WHERE codigo=?";
    	
    	try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setString(1, mapaSignoVitalAnestesia.get("observaciones").toString());
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(mapaSignoVitalAnestesia.get("fecha").toString())));
			ps.setString(3, mapaSignoVitalAnestesia.get("hora").toString());
			ps.setString(4, mapaSignoVitalAnestesia.get("login_usuario").toString());
			ps.setString(5, mapaSignoVitalAnestesia.get("graficar").toString());
			ps.setInt(6, Utilidades.convertirAEntero(mapaSignoVitalAnestesia.get("codigo_tiempo").toString()));
			
			if(ps.executeUpdate()>0)
			{
				for(int w=0; w<Utilidades.convertirAEntero(mapaSignoVitalAnestesia.get("numSignosVitales").toString()); w++)
				{
					if(UtilidadTexto.isNumber(mapaSignoVitalAnestesia.get("valor_"+w).toString()))
					{	
						if(!modificarValorSignoVitalAnestesia(con, Utilidades.convertirAEntero(mapaSignoVitalAnestesia.get("codigo_sv_anest_inst_"+w).toString()), Utilidades.convertirAEntero(mapaSignoVitalAnestesia.get("codigo_tiempo").toString()), Utilidades.convertirAEntero(mapaSignoVitalAnestesia.get("valor_"+w).toString())))
						{
							logger.info("Error insertando el signo vital-->"+mapaSignoVitalAnestesia.get("nombre_"+w));
							return false;
						}
					}	
				}
				return true;
			}
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
     * @param codSignoVitalAnestInst
     * @param codTiempoSignoVital
     * @param valor
     * @return
     */
	private static boolean modificarValorSignoVitalAnestesia(Connection con, int codSignoVitalAnestInst, int codTiempoSignoVital, int valor)
	{
		String cadena="UPDATE signo_vital_anes_tiempo set valor=? WHERE signo_vital_anest_inst=? AND tiempo_signo_vital_anest=?";
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, valor);
			ps.setInt(2, codSignoVitalAnestInst);
			ps.setInt(3, codTiempoSignoVital);
			
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
     * @param numeroSolicitud
     * @return
     */
	public static boolean existeTiempoSignoVitalAnestesia(Connection con, String fecha, String hora, int numeroSolicitud, int codigoTiempo)
	{
		String cadena="SELECT codigo from tiempo_signo_vital_anest where solicitud_hoja_anest=? and fecha=? and hora=?";
		if(codigoTiempo>0)
			cadena+=" AND codigo<>"+codigoTiempo;
		
		logger.info("\n existeTiempoSignoVitalAnestesia-->"+cadena+" numsol->"+numeroSolicitud+" fecha->"+UtilidadFecha.conversionFormatoFechaABD(fecha)+" hora->"+hora+"\n");
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, numeroSolicitud);
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fecha)));
			ps.setString(3, hora);
			
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
	 * @param codTiempoSignoVital
	 * @param codSignoVitalAnestInst
	 * @return
	 */
	public static boolean eliminarTiempoSignoVitalAnestesia(Connection con, int codTiempoSignoVital)
	{
		String cadena="DELETE FROM signo_vital_anes_tiempo WHERE tiempo_signo_vital_anest=?";
		
		logger.info("eliminarTiempoSignoVitalAnestesia-->"+cadena+" "+codTiempoSignoVital);
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codTiempoSignoVital);
			
			if(ps.executeUpdate()>0)
			{	
				cadena="DELETE FROM tiempo_signo_vital_anest WHERE codigo=?";
				logger.info("eliminarTiempoSignoVitalAnestesia-->"+cadena+" "+codTiempoSignoVital);
				PreparedStatementDecorator ps1= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ps1.setInt(1, codTiempoSignoVital);
				return ps1.executeUpdate()>0;
			}	
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private static HashMap<Object, Object> obtenerOpcionesSignoVital(Connection con, int codigoSignoVital)
	{
		String cadena="SELECT codigo, nombre FROM opciones_signos_vitales WHERE codigo_signo_vital =? order by nombre";
		logger.info("obtenerOpcionesSignoVital--->"+cadena+" ->"+codigoSignoVital+"\n");
		
		try
		{
			PreparedStatementDecorator ps= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ps.setInt(1, codigoSignoVital);
			
			HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			ps.close();
			return mapaRetorno;
		}
		catch (SQLException e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
}