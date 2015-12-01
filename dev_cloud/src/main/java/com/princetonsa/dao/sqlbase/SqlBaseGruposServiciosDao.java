/*
 * Enero 18, 2006
 * Modificacion Nov 16, 2006 FC
 */
package com.princetonsa.dao.sqlbase;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.RespuestaHashMap;
import util.UtilidadBD;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;


/**
 * @author Sebastián Gómez 
 *
 * Objeto usado para manejar los accesos comunes a la fuente de datos
 * para la parametrización de Grupos de Servicios
 */
public class SqlBaseGruposServiciosDao 
{
	/**
	 * Manejador de logs de la clase
	 */
	private static Logger logger=Logger.getLogger(SqlBaseGruposServiciosDao.class);
	
	/**
	 * Sección SELECT para la consulta de grupos de servicios
	 */
	private static final String consultarGruposSELECT_Str = "SELECT " +
		"codigo AS codigo, " +
		"descripcion As descripcion, " +
		"acronimo AS acronimo, " +
		"activo AS activo, " +
		"CASE WHEN facturacion.getUsoGrupoServicio(codigo) > 0 THEN 'true' ELSE 'false' END AS es_usado, "+
		"multiple as multiple ," +
		"tipo as tipo, " +
		"tipo_sala_standar as tipoSalaStandar, " +
		"num_dias_urgente as numDiasUrgente, " +
		"acro_dias_urgente as acroDiasUrgente, " +
		"num_dias_normal as numDiasNormal, " +
		"acro_dias_normal as acroDiasNormal, " +
		"tipo_monto as tipoMonto " +
		"FROM grupos_servicios ";
	

	/**
	 * Sección ORDER BY para la cosnulta de grupos de servicios
	 */
	private static final String consultarGruposORDER_Str = " ORDER BY descripcion ";
	
	/**
	 * Cadena UPDATE para modificar un registro de grupos de servicios
	 */
	private static final String modificarStr = " UPDATE " +
		"grupos_servicios SET descripcion = ?, activo = ?, multiple = ? , tipo = ?, tipo_sala_standar = ?, "+
		"num_dias_urgente = ?, acro_dias_urgente = ?, num_dias_normal = ?, acro_dias_normal = ?, "+
		"tipo_monto = ? WHERE codigo = ?";
	
	/**
	 * Cadena DELETE para eliminar un registro de grupos de servicios
	 */
	private static final String eliminarStr = "DELETE " +
		"FROM grupos_servicios WHERE codigo = ?";
	
	

	/**
	 * Cadena INSERT para la inserción de un registro
	 * en la tabla grupos_servicios
	 */
	private static final String insertarStr = "INSERT INTO " +
			"grupos_servicios (codigo,descripcion,acronimo,activo,multiple,tipo,institucion,tipo_sala_standar,"+
			"num_dias_urgente,acro_dias_urgente,num_dias_normal,acro_dias_normal,tipo_monto) values(?,?,?,?,?,?,?,?,?,?,?,?,?)";
	

	
	/**
	 * Método implementado para consultar los grupos de servicios
	 * de una institucion
	 * @param con
	 * @param institucion
	 * @return
	 */
	public static HashMap consultarGrupos(Connection con, int codInstitucion)
	{
		String columnas[] = {
				"codigo",
				"descripcion",
				"acronimo",
				"activo",
				"es_usado",
				"multiple",
				"tipo",
				"tipoSalaStandar",
				"numDiasUrgente",
				"acroDiasUrgente",
				"numDiasNormal",
				"acroDiasNormal",
				"tipoMonto"
				};
		try
		{
			String consulta = consultarGruposSELECT_Str ;
			
			if (codInstitucion !=0)
			{
				String consultarGruposWhere_Str = " WHERE  institucion = " + codInstitucion; 
				consulta += consultarGruposWhere_Str;
			}
			consulta += consultarGruposORDER_Str;

			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(consulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(pst.executeQuery()),false,true);
			return listado.getMapa();
		}
		catch(SQLException e)
		{
			logger.error("Error en consultarGrupos de SqlBaseGruposServiciosDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método usado para realizar una búsqueda avanzada de
	 * grupos de servicios
	 * 
	 * @param con
	 * @param codigo
	 * @param acronimo
	 * @param descripcion
	 * @param activo
	 * @param multiple
	 * @param tipo
	 * @param tipoSalaStandar
	 * @param numDiasUrgente
	 * @param acroDiasUrgente
	 * @param numDiasNormal
	 * @param acroDiasNormal
	 * @param tipoMonto
	 * @return
	 */
	public static HashMap busquedaGrupos(Connection con,int codigo,String acronimo,String descripcion,String activo, String multiple, String tipo, String tipoSalaStandar,
								String numDiasUrgente, String acroDiasUrgente, String numDiasNormal,String acroDiasNormal, int tipoMonto)
	{
		String columnas[] = {
				"codigo",
				"descripcion",
				"acronimo",
				"activo",
				"multiple",
				"tipo",
				"tipoSalaStandar",
				"numDiasUrgente",
				"acroDiasUrgente",
				"numDiasNormal",
				"acroDiasNormal",
				"tipoMonto"
				};
		try
		{
			String consulta = consultarGruposSELECT_Str + " WHERE ";
			boolean esPrimero = true;
			
			//se verifica el CODIGO
			if(codigo>0)
			{
				consulta += " codigo = "+codigo;
				esPrimero = false;
			}
			
			//se verifica ACRONIMO
			if(!acronimo.equals(""))
			{
				if(!esPrimero)
					consulta+=" AND ";
				consulta += " acronimo = "+acronimo;
				esPrimero = false;
			}
			
			//se verifica DESCRIPCION
			if(!descripcion.equals(""))
			{
				if(!esPrimero)
					consulta += " AND ";
				consulta += " descripcion = "+descripcion;
				esPrimero = false;
			}
			
			//se verifica ACTIVO
			if(!activo.equals(""))
			{		
				if(!esPrimero)
					consulta += " AND ";
				
				if(UtilidadTexto.getBoolean(activo))
					consulta += " activo = "+ValoresPorDefecto.getValorTrueParaConsultas();
				else
					consulta += " activo = "+ValoresPorDefecto.getValorFalseParaConsultas();
			}
			
			//se verifica MULTIPLE
			if(!multiple.equals(""))
			{		
				if(!esPrimero)
					consulta += " AND ";
				
				if(UtilidadTexto.getBoolean(multiple))
					consulta += " multiple = "+ValoresPorDefecto.getValorTrueParaConsultas();
				else
					consulta += " multiple = "+ValoresPorDefecto.getValorFalseParaConsultas();
			}
			
			//se verifica TIPO
			if(!tipo.equals(""))
			{
				if(!esPrimero)
					consulta += " AND ";
				consulta += " tipo = "+tipo;
				esPrimero = false;
			}
			
			//se verifica TIPO SALA STANDAR
			if(!tipo.equals(""))
			{
				if(!esPrimero)
					consulta += " AND ";
				consulta += " tipo_sala_standar = "+tipoSalaStandar;
				esPrimero = false;
			}
			
			//se verifica NUMERO Dias URGENTE
			if(!numDiasUrgente.equals(""))
			{
				if(!esPrimero)
					consulta += " AND ";
				consulta += " num_dias_urgente = "+numDiasUrgente;
				esPrimero = false;
			}
			
			//se verifica ACRONIMO DIAS URGENTE
			if(!acroDiasUrgente.equals(""))
			{
				if(!esPrimero)
					consulta += " AND ";
				consulta += " acro_dias_urgente = "+acroDiasUrgente;
				esPrimero = false;
			}
			
			//se verifica NUMERO DIAS NORMAL
			if(!numDiasNormal.equals(""))
			{
				if(!esPrimero)
					consulta += " AND ";
				consulta += " num_dias_normal = "+numDiasNormal;
				esPrimero = false;
			}
			
			//se verifica ACRONIMO DIAS NORMAL
			if(!acroDiasNormal.equals(""))
			{
				if(!esPrimero)
					consulta += " AND ";
				consulta += " acro_dias_normal = "+acroDiasNormal;
				esPrimero = false;
			}
			
			//se verifica TIPO MONTO
			if(tipoMonto != ConstantesBD.codigoNuncaValido)
			{
				if(!esPrimero)
					consulta += " AND ";
				consulta += " tipo_monto = "+tipoMonto;
				esPrimero = false;
			}
			
			
			consulta += consultarGruposORDER_Str;
			
			logger.info("consulta->"+consulta);
			Statement st = con.createStatement(ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet );
			
			RespuestaHashMap listado=UtilidadBD.resultSet2HashMap(columnas,new ResultSetDecorator(st.executeQuery(consulta)),false,true);
			return listado.getMapa();
			
		}
		catch(SQLException e)
		{
			logger.error("Error en busquedaGrupos de SqlBaseGruposServiciosDao: "+e);
			return null;
		}
	}
	
	/**
	 * Método implementado para insertar un registro de
	 * grupos de servicios
	 * @param con
	 * @param acronimo
	 * @param descripcion
	 * @param activo
	 * @param multiple
	 * @param tipo
	 * @param codInstitucion
	 * @param tipoSalaStandar
	 * @param numDiasUrgente
	 * @param acroDiasUrgente
	 * @param numDiasNormal
	 * @param acroDiasNormal
	 * @param tipoMonto
	 * @return si la transacción es exitosa retorna el consecutivo del registro.
	 */
	public static int insertar(Connection con,String acronimo,
			String descripcion,boolean activo, boolean multiple, String tipo, int codInstitucion, String tipoSalaStandar,
			String numDiasUrgente, String acroDiasUrgente, String numDiasNormal, String acroDiasNormal, int tipoMonto)
	{
		try
		{
			logger.info("===> La consulta Es: "+insertarStr);
			String multipleStr = "";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con,insertarStr);
			int codigo=UtilidadBD.obtenerSiguienteValorSecuencia(con,"seq_grupos_servicios");
			pst.setInt(1, codigo);
			pst.setString(2,descripcion);
			pst.setString(3,codigo+"");
			pst.setBoolean(4,activo);
			if (multiple)
			{
				multipleStr = ConstantesBD.acronimoSi;   //"S";
			}
			else
			{
				multipleStr = ConstantesBD.acronimoNo; // "N";
			}
			pst.setString(5,multipleStr);
			pst.setString(6,tipo);
			pst.setInt(7, codInstitucion);
			
			if(Utilidades.convertirAEntero(tipoSalaStandar)==ConstantesBD.codigoNuncaValido)
			{
				logger.info("===> tipoSalaStandar no puede modificarse con un -1 Se Asigan null");
				pst.setNull(8, Types.INTEGER);
			}
			else
			{
				logger.info("===> tipoSalaStandar lleno con: "+Utilidades.convertirAEntero(tipoSalaStandar));
				pst.setInt(8, Utilidades.convertirAEntero(tipoSalaStandar));
			}
			
			if(numDiasUrgente.equals("")){
				pst.setNull(9, Types.INTEGER);
			}
			else{
				pst.setInt(9, Integer.parseInt(numDiasUrgente.trim()));
			}
			
			pst.setString(10, acroDiasUrgente);
			
			if(numDiasNormal.equals("")){
				pst.setNull(11, Types.INTEGER);
			}
			else{
				pst.setInt(11, Integer.parseInt(numDiasNormal.trim()));
			}
			
			pst.setString(12, acroDiasNormal);
			if(tipoMonto == ConstantesBD.codigoNuncaValido){
				pst.setNull(13, Types.CHAR);
			}
			else{
				pst.setInt(13, tipoMonto);
			}
			
			
			Log4JManager.info("consulta -->"+pst);
			int resp = pst.executeUpdate();
			
			if(resp>0)
				return codigo;
			else
				return resp;
		}
		catch(SQLException e)
		{
			logger.error("Error en insertar de SqlBaseGruposServiciosDao: ",e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para modificar un registro de
	 * grupos de servicios
	 * 
	 * @param con
	 * @param descripcion
	 * @param activo
	 * @param codigo
	 * @param multiple
	 * @param tipo
	 * @param tipoSalaStandar
	 * @param numDiasUrgente
	 * @param acroDiasUrgente
	 * @param numDiasNormal
	 * @param acroDiasNormal
	 * @param tipoMonto
	 * @return
	 */
	public static int modificar(Connection con,String descripcion,boolean activo, int codigo, boolean multiple, String tipo, String tipoSalaStandar,
						String numDiasUrgente, String acroDiasUrgente, String numDiasNormal,String acroDiasNormal, int tipoMonto)
	{
		try
		{
		
			//logger.info("Consulta para modificar: "+modificarStr);
			String multipleStr = "";
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(modificarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setString(1,descripcion);
			pst.setBoolean(2,activo);
			
			if (multiple)
			{
				multipleStr = ConstantesBD.acronimoSi;   //"S";
			}
			else
			{
				multipleStr = ConstantesBD.acronimoNo; // "N";
			}
			
			pst.setString(3,multipleStr);			
			pst.setString(4,tipo);
			
			
			if(Utilidades.convertirAEntero(tipoSalaStandar)==ConstantesBD.codigoNuncaValido)
			{
				logger.info("===> tipoSalaStandar no puede modificarse con un -1 Se Asigan null");
				pst.setNull(5, Types.INTEGER);
			}
			else
			{
				logger.info("===> tipoSalaStandar lleno con: "+Utilidades.convertirAEntero(tipoSalaStandar));
				pst.setInt(5, Utilidades.convertirAEntero(tipoSalaStandar));
			}
			
			if(numDiasUrgente.equals("") || numDiasUrgente.equals("null")){
				pst.setNull(6, Types.INTEGER);
			}
			else{
				pst.setInt(6, Integer.parseInt(numDiasUrgente.trim()));
			}
			
			pst.setString(7, acroDiasUrgente);
			
			if(numDiasNormal.equals("") || numDiasNormal.equals("null")){
				pst.setNull(8, Types.INTEGER);
			}
			else{
				pst.setInt(8, Integer.parseInt(numDiasNormal.trim()));
			}
			
			pst.setString(9, acroDiasNormal);
			
			if(tipoMonto == ConstantesBD.codigoNuncaValido){
				pst.setNull(10, Types.CHAR);
			}
			else{
				pst.setInt(10, tipoMonto);
			}
			
			pst.setInt(11,codigo);
			//logger.info("===> desc"+descripcion+"act "+activo+"mut "+multipleStr+"tipo "+tipo+"cod "+codigo+" tipoStand "+tipoSalaStandar);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en modificar de SqlBaseGruposServiciosDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método implementado para eliminar un registro de grupos de servicios
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static int eliminar(Connection con,int codigo)
	{
		try
		{
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(eliminarStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			pst.setInt(1,codigo);
			
			return pst.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error("Error en eliminar de SqlBaseGruposServiciosDao: "+e);
			return -1;
		}
	}
	
	/**
	 * Método encargado de obtener los tipos de salas con es_quirurgica = true
	 * @param Coneection con
	 * @param int codInstitucion
	 * @return HashMap con los resultados de la obtención de los tipos de salas con es_quirurgica = true
	 */
	public static HashMap obtenerListaSalas(Connection con, int codInstitucion)
	{
		logger.info("===> Entré a obtenerListaSalas");
		logger.info("===> codigoInstitucion: "+codInstitucion);
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT " +
					"codigo AS codigo, " +
					"descripcion AS descripcion " +
					"FROM tipos_salas " +
					"WHERE institucion="+codInstitucion+" " +
					"AND es_quirurgica= " + ValoresPorDefecto.getValorTrueParaConsultas() +
					" ORDER BY descripcion ";
			logger.info("===>Consultar Lista De Salas: "+cadena);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerListaSalas: "+e);
		}
		return mapa;
	}
	
	/**
	 * Método encargado de obtener los tipos de montos
	 * @param Coneection con
	 * @return HashMap con los resultados de la obtención de los tipos de montos
	 */
	
	public static HashMap obtenerListaTiposMontos(Connection con)
	{
		HashMap mapa = new HashMap();
		mapa.put("numRegistros", "0");
		try
		{
			String cadena = "SELECT " +
					"codigo AS codigo, " +
					"nombre AS nombre " +
					"FROM facturacion.tipos_monto " +
					" ORDER BY nombre ";
			logger.info("===>Consultar Lista De tiposMonto: "+cadena);
			PreparedStatementDecorator pst =  new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			mapa = UtilidadBD.cargarValueObject(new ResultSetDecorator(pst.executeQuery()));
		}
		catch(SQLException e)
		{
			logger.error("Error en obtenerListaTiposMontos: "+e);
		}
		return mapa;
	}

}
