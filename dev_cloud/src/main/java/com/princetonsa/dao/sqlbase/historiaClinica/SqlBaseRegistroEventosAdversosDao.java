package com.princetonsa.dao.sqlbase.historiaClinica;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.SQLException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.mundo.historiaClinica.RegistroEventosAdversos;

/**
 * Clase para el manejo de secciones y subsecciones x almacen
 * Date: 2008-01-16
 * @author garias@princetonsa.com - lgchavez@princetonsa.com
 */
public class SqlBaseRegistroEventosAdversosDao
{
	//*********************** Atributos
	
	/**
	 * Objeto para manejar log de la clase  
	 * */
	private static Logger logger = Logger.getLogger(SqlBaseRegistroEventosAdversosDao.class);
	

		
	/**
	 *  Vector de indices de la consulta de ingresos
	 */
	private static final String [] indicesMap = {	"codigoevento_",
													"nombreevento_",
													"fechahorar_",
													"tipoevento_",
													"gestionado_" ,
													"codigo_",
													"observaciones_"
													};

	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultar(Connection con, RegistroEventosAdversos registroeve) 
	{
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		
		/**
		 *  Cadena para consultar todas las secciones
		 */
		String cadenaConsultaStr = "SELECT " +
									"	 " +
									"	ea.codigo as codigoevento," +
									"	ea.descripcion as nombreevento," +
									"	rea.fecha_registro || ' / ' || rea.hora_registro as fechahorar," +
									"	ea.tipo as tipoevento," +
									"	rea.gestionado as gestionado," +
									"	rea.codigo as codigo " ;
									if(!registroeve.getCodigorea().toString().equals(""))
									{
				cadenaConsultaStr+=	"	, orea.fecha_modifica||' - '|| orea.hora_modifica ||' '||orea.observaciones as observaciones" +
									"	, orea.usuario_modifica as login " ;
									}
				cadenaConsultaStr+=	" FROM " +
									"	registro_eventos_adversos rea " ;
									if(!registroeve.getCodigorea().toString().equals(""))
									{
				cadenaConsultaStr+=" LEFT OUTER JOIN " +
									" 	observ_eventos_adversos orea ON (rea.codigo=orea.registro_evento_adverso)	" ;
									}
				cadenaConsultaStr+=	" INNER JOIN " +
									"	eventos_adversos ea ON (rea.evento_adverso=ea.codigopk) " +
									" WHERE " +
									"	1=1 " +
									" "; 

		String consulta=cadenaConsultaStr;
		
		if(!registroeve.getIngreso().toString().equals(""))
		{
			consulta+=" and rea.ingreso="+registroeve.getIngreso();
		}
		if(!registroeve.getPaciente().toString().equals("")) 
		{
			consulta+=" and rea.paciente="+registroeve.getPaciente();
		}
		if(!registroeve.getCentroCosto().toString().equals(""))
		{
			consulta+=" and rea.centro_costo="+registroeve.getCentroCosto();
		}
		if(!registroeve.getCodigorea().toString().equals(""))
		{
			consulta+=" and rea.codigo="+registroeve.getCodigorea();
		}
		
		
		consulta+=" ORDER BY fechahorar ";
		
		PreparedStatementDecorator ps = null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(consulta));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			mapa.put("INDICES_MAPA", indicesMap);
			logger.info("\n\n\n\n Consulta Eventos adversos >>>"+consulta);
		}
		catch (SQLException e)
		{
			logger.info("\n\n\n Error en la consulta de Eventos adversos >>>"+e+"\n\n\n Sentencia"+consulta);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseRegistroEventosAdversosDao "+sqlException.toString() );
			}
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarDetalleXCuenta(Connection con, RegistroEventosAdversos registroeve){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadenaConsultaStr = "SELECT " +
										"ea.descripcion as evento, " +
										"rea.evento_adverso as evento_adverso, " +
										"rea.fecha_registro as fecha," +
										"rea.hora_registro as hora," +
										"getintegridaddominio(ea.tipo) as tipo, " +
										"rea.gestionado as gestionado, " +
										"rea.usuario_modifica as usuario, " +
										"getnomcentrocosto(rea.centro_costo) as centro_costo, " +
										"ce.nombre as clasificacion, " +
										"orea.observaciones as observaciones " +
									"FROM " +
										"registro_eventos_adversos rea " +
									"INNER JOIN " +
										"eventos_adversos ea ON (rea.evento_adverso=ea.codigopk) " +
									"INNER JOIN " +
										"clasificaciones_eventos ce ON (ea.clasificacion_evento=ce.codigo) " +
									"LEFT OUTER JOIN " +
										"observ_eventos_adversos orea ON (rea.codigo=orea.registro_evento_adverso) " +
									"WHERE " +
										"ingreso="+registroeve.getIngreso()+" AND rea.activo='"+ConstantesBD.acronimoSi+"' " +
									"ORDER BY " +
										"fecha asc, hora asc";
		PreparedStatementDecorator ps= null;
		try
		{
			 ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaStr));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			Utilidades.imprimirMapa(mapa);
		}
		catch (SQLException e)
		{
			logger.info("\n\n\n Error en la consulta de Eventos adversos >>>"+e+"\n\n\n Sentencia"+cadenaConsultaStr);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseRegistroEventosAdversosDao "+sqlException.toString() );
			}
		}
		return mapa;
	}
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public static HashMap consultarDetalleXCuenta2(Connection con, RegistroEventosAdversos registroeve){
		HashMap mapa=new HashMap();
		mapa.put("numRegistros","0");
		String cadenaConsultaStr = "SELECT " +
										"ea.descripcion as evento, " +
										"rea.evento_adverso as evento_adverso, " +
										"rea.fecha_registro as fecha," +
										"rea.hora_registro as hora," +
										"getintegridaddominio(ea.tipo) as tipo, " +
										"rea.gestionado as gestionado, " +
										"rea.usuario_modifica as usuario, " +
										"getnomcentrocosto(rea.centro_costo) as centro_costo, " +
										"ce.nombre as clasificacion, " +
										"coalesce(getObservacionesEventoAdverso(rea.codigo), '') as observaciones " +
									"FROM " +
										"registro_eventos_adversos rea " +
									"INNER JOIN " +
										"eventos_adversos ea ON (rea.evento_adverso=ea.codigopk) " +
									"INNER JOIN " +
										"clasificaciones_eventos ce ON (ea.clasificacion_evento=ce.codigo) " +
									"WHERE " +
										"ingreso="+registroeve.getIngreso()+" AND rea.activo='"+ConstantesBD.acronimoSi+"' " +
									"ORDER BY " +
										"fecha asc, hora asc";
		logger.info("----------> "+cadenaConsultaStr);
		
		PreparedStatementDecorator ps =  null;
		try
		{
			ps= new PreparedStatementDecorator(con.prepareStatement(cadenaConsultaStr));
			mapa=UtilidadBD.cargarValueObject(new ResultSetDecorator(ps.executeQuery()));
			Utilidades.imprimirMapa(mapa);
		}
		catch (SQLException e)
		{
			logger.info("\n\n\n Error en la consulta de Eventos adversos >>>"+e+"\n\n\n Sentencia"+cadenaConsultaStr);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseRegistroEventosAdversosDao "+sqlException.toString() );
			}
		}
		return mapa;
	}
	
	
	public static int guardarEvento(Connection con, HashMap filtro) 
	{
		int y=0;
		
		logger.info("\n\n\n\n\n\n   mapa filtro >>>>>"+filtro+"\n\n\n\n\n");
		
		int codigoSecuencia= UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_registro_eventos_adversos");
		
		String cadenaInsertar=" INSERT INTO " +
										" registro_eventos_adversos (" +
										"	codigo, " +
										"	evento_adverso," +
										"	gestionado," +
										"	ingreso," +
										"	fecha_modifica," +
										"	hora_modifica," +
										"	usuario_modifica," +
										"	institucion," +
										"	activo," +
										"	fecha_registro," +
										"	hora_registro," +
										"	paciente," +
										"	centro_costo " +
										" ) " +
										" VALUES (" +
										" "+codigoSecuencia+", " +
										""+filtro.get("evento")+","+
										" '"+filtro.get("gestionado")+"'," +
										" "+filtro.get("ingreso")+"," +
										" CURRENT_DATE ,"+
										" "+ValoresPorDefecto.getSentenciaHoraActualBD()+","+
										"'"+filtro.get("usuario")+"',"+
										""+filtro.get("institucion")+","+
										"'"+ConstantesBD.acronimoSi+"',"+
										"'"+UtilidadFecha.conversionFormatoFechaABD(filtro.get("fechar")+"")+"',"+
										"'"+filtro.get("horar")+"'," +
										""+filtro.get("paciente")+","+
										""+filtro.get("centroCosto")+""+
										")";
		 
		
		PreparedStatementDecorator ps= null;
		try
		{
			 ps= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertar));
			y=ps.executeUpdate();
			logger.info("\n\n\n\n insertar eventos adversos >>>"+cadenaInsertar);
		}
		catch (SQLException e)
		{
			logger.info("\n\n\n Error en la insercion de eventos adversos >>>"+e+"\n\n\n Sentencia"+cadenaInsertar);
		}finally{
			try {
				if(ps!=null){
					ps.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseRegistroEventosAdversosDao "+sqlException.toString() );
			}
		}
		
		if (y==1)
		{
			y=0;
			
			if (filtro.containsKey("observaciones") && !filtro.get("observaciones").toString().equals(""))
			{
				
				String cadenaInsertarob=" INSERT INTO " +
											" observ_eventos_adversos (" +
											"	codigo," +
											"	registro_evento_adverso," +
											"	observaciones ,  " +
											"	fecha_modifica," +
											"	hora_modifica," +
											"	usuario_modifica" +
											")" +
											"VALUES ( " +
											" "+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_observ_eventos_adversos")+", "+
											" "+codigoSecuencia+","+
											"'"+filtro.get("observaciones")+"',"+
											" CURRENT_DATE ,"+
											" "+ValoresPorDefecto.getSentenciaHoraActualBD()+","+
											"'"+filtro.get("usuario")+"'"+
											")";
				 			
				PreparedStatementDecorator psob= null;
				try
				{
				 psob= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarob));
				y=psob.executeUpdate();
				logger.info("\n\n\n\n insertar observaciones >>>"+cadenaInsertarob);
				}
				catch (SQLException e)
				{
				logger.info("\n\n\n Error en la insercion de observaciones >>>"+e+"\n\n\n Sentencia"+cadenaInsertarob);
				}finally{
					try {
						if(psob!=null){
							psob.close();
						}
						
					} catch(SQLException sqlException){
						logger.warn(sqlException+" Error al cerrar el recurso SqlBaseRegistroEventosAdversosDao "+sqlException.toString() );
					}
				}
			}
			else
			{
				y=1;
			}
		}
		
		
		return y;
	}
	
	
	
	
	public static int modificar(Connection con, HashMap filtro) 
	{
		int y=0;
		
		logger.info("\n\n\n\n\n\n   mapa  >>>>>"+filtro+"\n\n\n\n\n");
		String cadenaupdate=" UPDATE  " +
									" registro_eventos_adversos set" +
									"	fecha_registro='"+UtilidadFecha.conversionFormatoFechaABD(filtro.get("fechar_0")+"")+"', "+
									"	hora_registro ='"+filtro.get("horar_0")+"', "+
									"	gestionado='"+filtro.get("gestionado_0")+"'," +
									"	fecha_modifica=CURRENT_DATE," +
									"	hora_modifica= "+ValoresPorDefecto.getSentenciaHoraActualBD()+"," +
									"	usuario_modifica='"+filtro.get("usuario")+"'" +
									" WHERE codigo="+filtro.get("codigomod");
		 			
		PreparedStatementDecorator psob= null;
		try
		{
		psob= new PreparedStatementDecorator(con.prepareStatement(cadenaupdate));
		y=psob.executeUpdate();
		logger.info("\n\n\n\n actualizaciond e registro de evento >>>"+cadenaupdate);
		}
		catch (SQLException e)
		{
		logger.info("\n\n\n Error en la actualizacion del evento >>>"+e+"\n\n\n Sentencia"+cadenaupdate);
		}finally{
			try {
				if(psob!=null){
					psob.close();
				}
				
			} catch(SQLException sqlException){
				logger.warn(sqlException+" Error al cerrar el recurso SqlBaseRegistroEventosAdversosDao "+sqlException.toString() );
			}
		}
		
		
		if (y>0)
		{
		y=0;
			if(!filtro.get("observaciones").toString().equals(""))
			{
				logger.info("\n\n\n\n\n\n   mapa filtro en observaciones >>>>>"+filtro+"\n\n\n\n\n");
				
				String cadenaInsertarob=" INSERT INTO " +
											" observ_eventos_adversos (" +
											"	codigo," +
											"	registro_evento_adverso," +
											"	observaciones ,  " +
											"	fecha_modifica," +
											"	hora_modifica," +
											"	usuario_modifica" +
											")" +
											"VALUES ( " +
											" "+UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_observ_eventos_adversos")+", "+
											" "+filtro.get("codigomod")+","+
											"'"+filtro.get("observaciones")+"',"+
											" CURRENT_DATE ,"+
											" "+ValoresPorDefecto.getSentenciaHoraActualBD()+","+
											"'"+filtro.get("usuario")+"'"+
											")";
	
			
			try
			{
			psob= new PreparedStatementDecorator(con.prepareStatement(cadenaInsertarob));
			y=psob.executeUpdate();
			logger.info("\n\n\n\n insertar observaciones >>>"+cadenaInsertarob);
			}
			catch (SQLException e)
			{
			logger.info("\n\n\n Error en la insercion de nuevas observaciones >>>"+e+"\n\n\n Sentencia"+cadenaInsertarob);
			}finally{
				try {
					if(psob!=null){
						psob.close();
					}
					
				} catch(SQLException sqlException){
					logger.warn(sqlException+" Error al cerrar el recurso SqlBaseRegistroEventosAdversosDao "+sqlException.toString() );
				}
			}
			}
			else
			{
				y=1;
			}
			
		}
		
		
		return y;
	}
	
	
	
	
			
}