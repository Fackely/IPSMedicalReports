package com.princetonsa.dao.sqlbase.capitacion;

import java.sql.Connection;
import java.sql.Date;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

public class SqlBaseAsociarCxCAFacturasDao 
{
	/**
	* Objeto para manejar los logs de esta clase
	*/
	private static Logger logger = Logger.getLogger(SqlBaseAsociarCxCAFacturasDao.class);

	/**
	 * insertar
	 */
	private static String insertarStr="insert into cuentas_cobro_aso_fact (cuenta_cobro_capitacion, convenio, fecha_proceso, hora_proceso, usuario, institucion, cantidad_fact_asociadas, ind_conta_cue_x_cobrar) values(?, ?, CURRENT_DATE, "+ValoresPorDefecto.getSentenciaHoraActualBD()+", ?, ?, ?,?)";
	
	/**
	 * busqueda de las cuentas de cobro a asociar
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 * @throws SQLException
	 */	
	public static HashMap busquedaCuentasCobroAAsociar(	Connection con,
															HashMap criteriosBusquedaMap
														 )throws SQLException
	{
		String consultaArmada="";
		if(con==null || con.isClosed())
		{
			try
			{
				DaoFactory myFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
				con = myFactory.getConnection();
			}
			catch(SQLException e)
			{
				logger.warn("No se pudo realizar la conexión "+e.toString());
				return null;
			}
		}
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs= null;
		try
		{
			consultaArmada=armarConsulta(	 criteriosBusquedaMap	 );
																	 
			ps=  new PreparedStatementDecorator(con.prepareStatement(consultaArmada));
			rs=new ResultSetDecorator(ps.executeQuery());
			HashMap mapaRetorno=UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
		
			return mapaRetorno;
		}
		catch(SQLException e)
		{
			logger.warn("Error en la búsqueda avanzada de asociar cuentas cobro capitacion " +e.toString());
			return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAsociarCxCAFacturasDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAsociarCxCAFacturasDao "+sqlException.toString() );
			}
		}
	}

	/**
	 * Método que arma la consulta según los datos dados por el usuarios en 
	 * la búsqueda avanzada. 
	 * @param criteriosBusquedaMap
	 * @return
	 */
	private static String armarConsulta  (	HashMap criteriosBusquedaMap )
	{
		String consulta= 	" select " +
							"ccc.numero_cuenta_cobro AS numerocuentacobro, " +
							"ccc.convenio as codigoconvenio, " +
							"getnombreconvenio(ccc.convenio) as nombreconvenio, " +
							"to_char(ccc.fecha_elaboracion, 'DD/MM/YYYY') as fechaelaboracion, " +
							"ccc.fecha_elaboracion as fechaelaboracionbd, " +
							"ccc.valor_inicial_cuenta as valorinicial, " +
							"to_char(ccc.fecha_inicial, 'DD/MM/YYYY') as fechainicial, " +
							"ccc.fecha_inicial as fechainicialbd, " +
							"to_char(ccc.fecha_final, 'DD/MM/YYYY') as fechafinal, " +
							"ccc.fecha_final as fechafinalbd, " +
							"'si' as seleccion, " +
							"ccc.contabilizado as contabilizado " +
							"from " +
							"cuentas_cobro_capitacion ccc " +
							"INNER JOIN convenios c ON (c.codigo= ccc.convenio) " +
							//"WHERE ccc.estado  in("+ConstantesBD.codigoEstadoCarteraGenerado+","+ConstantesBD.codigoEstadoCarteraRadicada+") " +
							// tarea id=8768 en xplanner 3, solo radicadas.
							"WHERE ccc.estado  in("+ConstantesBD.codigoEstadoCarteraRadicada+") " +
							"and ccc.numero_cuenta_cobro not in(select cc.cuenta_cobro_capitacion from cuentas_cobro_aso_fact cc where cc.institucion=ccc.institucion) " +
							"and c.tipo_contrato="+ConstantesBD.codigoTipoContratoCapitado+" ";
			
		if(criteriosBusquedaMap.containsKey("codigoConvenio"))
		{
			if(!criteriosBusquedaMap.get("codigoConvenio").toString().equals("") && !criteriosBusquedaMap.get("codigoConvenio").toString().equals("null"))
			{
				consulta+=" and ccc.convenio= "+criteriosBusquedaMap.get("codigoConvenio").toString();
			}
		}
		
		if(criteriosBusquedaMap.containsKey("fechaInicial") && criteriosBusquedaMap.containsKey("fechaFinal"))
		{
			if(!criteriosBusquedaMap.get("fechaInicial").toString().equals("") && !criteriosBusquedaMap.get("fechaInicial").toString().equals("null")
				&& !criteriosBusquedaMap.get("fechaFinal").toString().equals("") && !criteriosBusquedaMap.get("fechaFinal").toString().equals("null"))
			{
				consulta+=	" AND (ccc.fecha_elaboracion >= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaInicial").toString())+"'" +
							" AND ccc.fecha_elaboracion <= '"+UtilidadFecha.conversionFormatoFechaABD(criteriosBusquedaMap.get("fechaFinal").toString())+"') ";
			}
		}
		
		consulta+="ORDER BY nombreconvenio, numerocuentacobro ASC";						
		logger.info("CONSULTA BUSQUEDA--->"+consulta);
		return consulta;	
	}
	
	/**
	 * insertar
	 * @param con
	 * @param numeroCuentaCobro
	 * @param contabilizado 
	 * @param codigoConvenio
	 * @param loginUsuario
	 * @param institucion
	 * @return
	 */
	public static boolean insertar (	Connection con, String numeroCuentaCobro,
										String contabilizado, int codigoConvenio, String loginUsuario,
										int institucion, int cantidadFacturasAsociadas)
	{
		PreparedStatementDecorator ps= null;
		try
		{
			if (con == null || con.isClosed()) 
			{
				throw new SQLException ("Error SQL: Conexión cerrada");
			}
			ps= new PreparedStatementDecorator(con.prepareStatement(insertarStr));
			logger.info("\n\ninsertarStr-->"+insertarStr+" --> nunCC->"+numeroCuentaCobro+" conv-> "+codigoConvenio+" user-->"+loginUsuario+" inst->"+institucion+" cantidad-->"+cantidadFacturasAsociadas);
			
			ps.setDouble(1, Utilidades.convertirADouble(numeroCuentaCobro));
			ps.setInt(2, codigoConvenio);
			ps.setString(3, loginUsuario);
			ps.setInt(4,institucion);
			ps.setInt(5, cantidadFacturasAsociadas);
			ps.setString(6, contabilizado);
			if(ps.executeUpdate()>0)
			{
				logger.info("inserto!!!");
				return true;
			}	
		}
		catch(SQLException e)
		{
			logger.warn(e+" Error en la inserción de datos: SqlBaseAsociarCxCAFacturasDao "+e.toString());
		}	finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAsociarCxCAFacturasDao "+sqlException.toString() );
			}
		}
		return false;
	}
	
	/**
	 * selecciona las facturas a asociar a una cuenta de cobro, recibe un vector con las facturas ya seleccionadas para que
	 * no existan problemas con n cuentas de cobro que tengan la misma factura, es decir, se asigna al primero que llegue
	 * @param con
	 * @param fechaInicialCuentaCobro
	 * @param fechaFinalCuentaCobro
	 * @param codigoConvenio
	 * @param facturasYaSelecionadasVector
	 * @return
	 */
	public static HashMap seleccionFacturasAAsociar(	Connection con , String fechaInicialCuentaCobro, String fechaFinalCuentaCobro, int codigoConvenio, Vector facturasYaSelecionadasVector)
	{
		HashMap mapa= new HashMap();
		String seleccionFacturasAAsociarStr=" select codigo, " +
											"valor_cartera as valorCartera " +
											"from facturas " +
											"where fecha>=? " +
											"and fecha<=? " +
											"and convenio=? " +
											"and cuenta_cobro_capitacion is null " +
											"and estado_facturacion = "+ConstantesBD.codigoEstadoFacturacionFacturada;
											//"and estado_facturacion IN ("+ConstantesBD.codigoEstadoFacturacionFacturada+", "+ConstantesBD.codigoEstadoFacturacionAnulada+")";
		
		if(!facturasYaSelecionadasVector.isEmpty())
		{
			seleccionFacturasAAsociarStr+=" and codigo not in( ";
			for(int w=0; w<facturasYaSelecionadasVector.size(); w++)
			{
				seleccionFacturasAAsociarStr+=facturasYaSelecionadasVector.get(w);
				if(w!=facturasYaSelecionadasVector.size()-1)
					seleccionFacturasAAsociarStr+=",";
			}
			seleccionFacturasAAsociarStr+=") ";
		}	
		PreparedStatementDecorator ps= null;
		ResultSetDecorator rs= null;
		try
		{
			logger.info("\n seleccionFacturasAAsociar-->"+seleccionFacturasAAsociarStr+"  -->fechaIni-> "+UtilidadFecha.conversionFormatoFechaABD(fechaInicialCuentaCobro)+" fechaFin->"+UtilidadFecha.conversionFormatoFechaABD(fechaFinalCuentaCobro)+" convenio="+codigoConvenio);
			ps=  new PreparedStatementDecorator(con.prepareStatement(seleccionFacturasAAsociarStr));
			ps.setDate(1, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaInicialCuentaCobro)));
			ps.setDate(2, Date.valueOf(UtilidadFecha.conversionFormatoFechaABD(fechaFinalCuentaCobro)));
			ps.setInt(3, codigoConvenio);
			rs=new ResultSetDecorator(ps.executeQuery());
			mapa= UtilidadBD.cargarValueObject(new ResultSetDecorator(rs));
			//se asigna el valor cartera al mapa para actualizar el total ingresos en la estructura cuentas cobro capitacion
			double totalIngresos= 0;
			String numReg=mapa.get("numRegistros")+"";
			for(int i=0; i<=Utilidades.convertirAEntero(mapa.get("numRegistros")+"");i++)
			{
				if(mapa.get("valorcartera_"+i)!=null)
				{
					totalIngresos+=Utilidades.convertirAEntero(mapa.get("valorcartera_"+i)+"");
				}
			}
			
			logger.info("TOTAL_INGRESOS-->"+totalIngresos);			
			mapa.put("TOTAL_INGRESOS", totalIngresos+"");
			logger.info("mapaFacturas-->"+mapa);
			return mapa;
		}
		catch(SQLException e)
		{
			logger.warn("Error en la seleccion de facturas a asociar cuentas cobro capitacion " +e.toString());
			return null;
		}finally{
			try{
				if(ps!=null){
					ps.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAsociarCxCAFacturasDao "+sqlException.toString() );
			}
			try{
				if(rs!=null){
					rs.close();					
				}
			}catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseAsociarCxCAFacturasDao "+sqlException.toString() );
			}
		}
		
	}
			
}
