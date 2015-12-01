package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

public class SqlBaseFichaSolicitudLaboratoriosDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaSolicitudLaboratoriosDao.class);
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichalaboratorios "+
    														"(" +
    														"codigoFichaLaboratorios," +
    														"codigoFicha," +
    														"examenSolicitado," +
															"muestraEnviada," +
															"hallazgos," +
															"fechaToma," +
															"fechaRecepcion," +
															"muestra," +
															"prueba," +
															"agente," +
															"resultado," +
															"fechaResultado," +
															"valor" +
															") " +
															"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    
    
    
    private static final String modificarFichaStr="UPDATE epidemiologia.vigifichalaboratorios " +
    													"SET " +
	    													"examenSolicitado=?," +
															"muestraEnviada=?," +
															"hallazgos=?," +
															"fechaToma=?," +
															"fechaRecepcion=?," +
															"muestra=?," +
															"prueba=?," +
															"agente=?," +
															"resultado=?," +
															"fechaResultado=?," +
															"valor=? " +
														"WHERE " +
															"codigoFichaLaboratorios = ?";
    
    
    
    private static final String consultarFichaStr = "SELECT " +
														"codigoFichaLaboratorios," +
													    "examenSolicitado," +
														"muestraEnviada," +
														"hallazgos," +
														"fechaToma," +
														"fechaRecepcion," +
														"muestra," +
														"prueba," +
														"agente," +
														"resultado," +
														"fechaResultado," +
														"valor " +
													"FROM " +
														"epidemiologia.vigiFichaLaboratorios " +
													"WHERE " +
														"codigoFicha = ?";
    
    
    
    private static final String consultarSolicitudStr = "SELECT " +
															"codigoFichaLaboratorios," +
														    "examenSolicitado," +
															"muestraEnviada," +
															"hallazgos," +
															"fechaToma," +
															"fechaRecepcion," +
															"muestra," +
															"prueba," +
															"agente," +
															"resultado," +
															"fechaResultado," +
															"valor " +
														"FROM " +
															"epidemiologia.vigiFichaLaboratorios " +
														"WHERE " +
															"codigoFichaLaboratorios = ?";
    
    
    public static final String consultarServiciosStr = "SELECT " +
    														"serv.codigo," +
    														"serv.especialidad," +
    														"refs.descripcion " +
    													"FROM " +
    														"epidemiologia.vigidetalleservicios det," +
    														"servicios serv," +
    														"referencias_servicio refs " +
    													"WHERE " +
    														"det.codigoenfnotificable=? " +
    													"AND " +
    														"det.tiposolicitud=1 " +
    													"AND " +
    														"serv.codigo=det.codigoservicio " +
    													"AND " +
    														"refs.servicio=serv.codigo " +
    													"AND " +
    														"refs.tipo_tarifario=0";
    
    
    private static final String insertarServicioStr="INSERT INTO epidemiologia.vigifichalaboratorios "+
													"(" +
														"codigoFichaLaboratorios," +
														"codigoFicha," +
														"codigoServicio," +
														"examenSolicitado," +
														"muestraEnviada," +
														"hallazgos," +
														"fechaToma," +
														"fechaRecepcion," +
														"muestra," +
														"prueba," +
														"agente," +
														"resultado," +
														"fechaResultado," +
														"valor" +
													") " +
													"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    
															
															
	public static int insertarFicha(Connection con,
									int codigoFicha,
									int codigoFichaLaboratorios,
									String sire,
								    
									String examenSolicitado,
									String muestraEnviada,
									String hallazgos,
									String fechaToma,
									String fechaRecepcion,
									int muestra,
									int prueba,
									int agente,
									int resultado,
									String fechaResultado,
									String valor,
									String secuencia)
    {
    	int result=0;
		int codigo;
		
		try {
		
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				codigo = rs.getInt(1);
			}
			else {
				logger.error("Error obteniendo el código de la secuencia ");
				return 0;
			}
			
			PreparedStatementDecorator insertarFicha =  new PreparedStatementDecorator(con.prepareStatement(insertarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			// Inserción de los datos de la ficha
			
			insertarFicha.setInt(1,codigo);
			insertarFicha.setInt(2,codigoFicha);
			insertarFicha.setString(3,examenSolicitado);
			insertarFicha.setString(4,muestraEnviada);
			insertarFicha.setString(5,hallazgos);
			insertarFicha.setString(6,fechaToma);
			insertarFicha.setString(7,fechaRecepcion);
			insertarFicha.setInt(8,muestra);
			insertarFicha.setInt(9,prueba);
			insertarFicha.setInt(10,agente);
			insertarFicha.setInt(11,resultado);
			insertarFicha.setString(12,fechaResultado);
			insertarFicha.setString(13,valor);
			
			result = insertarFicha.executeUpdate();
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaSolicitudLaboratoriosDao "+sqle.toString() );
			resultado=0;			
		}
		
		return result;
    }
	
	
	public static int insertarServicioFicha(Connection con, 
											HashMap mapaServicios,
											int codigoFicha,
											int codigoFichaLaboratorios,
											String examenSolicitado,
											String muestraEnviada,
											String hallazgos,
											String secuencia)
	{
		int resultado = 0;
		int codigo;
    	
    	try {
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			int numeroElementos = mapaServicios.size()/11;
			
			for (int i=0;i<numeroElementos;i++) {
				
				PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				ResultSet rs = ps.executeQuery();
				
				try {
					String valorCodEsp = mapaServicios.get("codigoaxioma_"+i).toString();
									
					String fechaToma = mapaServicios.get("fechatoma_"+i).toString();
					String fechaRecepcion = mapaServicios.get("fecharecepcion_"+i).toString();
					int muestra = Integer.parseInt(mapaServicios.get("muestra_"+i).toString());
					int prueba = Integer.parseInt(mapaServicios.get("prueba_"+i).toString());
					int agente = Integer.parseInt(mapaServicios.get("agente_"+i).toString());
					int valorResultado = Integer.parseInt(mapaServicios.get("resultado_"+i).toString());
					String fechaResultado = mapaServicios.get("fecharesultado_"+i).toString();
					String valor = mapaServicios.get("valor_"+i).toString();
					int valorActiva = Integer.parseInt(mapaServicios.get("activo_"+i).toString());
					
					String valorCodigo = valorCodEsp.split("-")[1];
					
					if (valorActiva==1) {
						
						if (rs.next()) {
							codigo = rs.getInt(1);
						}
						else {
							logger.error("Error obteniendo el código de la secuencia ");
							return 0;
						}
						
						PreparedStatementDecorator insertarServicio =  new PreparedStatementDecorator(con.prepareStatement(insertarServicioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						insertarServicio.setInt(1,codigo);
						insertarServicio.setInt(2,codigoFicha);
						insertarServicio.setInt(3,Integer.parseInt(valorCodigo));
						insertarServicio.setString(4,examenSolicitado);
						insertarServicio.setString(5,muestraEnviada);
						insertarServicio.setString(6,hallazgos);
						insertarServicio.setString(7,fechaToma);
						insertarServicio.setString(8,fechaRecepcion);
						insertarServicio.setInt(9,muestra);
						insertarServicio.setInt(10,prueba);
						insertarServicio.setInt(11,agente);
						insertarServicio.setInt(12,valorResultado);
						insertarServicio.setString(13,fechaResultado);
						insertarServicio.setString(14,valor);
						
						resultado = insertarServicio.executeUpdate();
						
						if (resultado<1) {
							
							daoFactory.abortTransaction(con);
							return -1;
						}
					}
				}
				catch (NullPointerException npe) {
					
				}
			}
    					
			daoFactory.endTransaction(con);
			
			return resultado;
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("error insertando los servicios parametrizados "+sqle);
    		return -1;
    	}
	}
	
	
	public static ResultSet consultarServicios(Connection con, int codigoEnfNotificable)
	{
		try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarServiciosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigoEnfNotificable);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("error consultando los servicios parametrizados "+sqle);
    		return null;
    	}
	}
	
	
	
	
	
	public static int modificarFicha(Connection con,
										String examenSolicitado,
										String muestraEnviada,
										String hallazgos,
										String fechaToma,
										String fechaRecepcion,
										int muestra,
										int prueba,
										int agente,
										int resultado,
										String fechaResultado,
										String valor,
										int codigoFichaLaboratorios
									)
	{
		int result=0;
		
		try {
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,examenSolicitado,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,muestraEnviada,2,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,hallazgos,3,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaToma,4,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaRecepcion,5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(muestra),6,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(prueba),7,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agente),8,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(resultado),9,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaResultado,10,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,valor,11,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaLaboratorios),12,Types.INTEGER,true,false);
            
            result = modificarFicha.executeUpdate();
		    
		    if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaSolicitudLaboratoriosDao "+sqle.toString() );
		    resultado=0;
        }
        
        return result;
	}
	
	
	
	
	public static ResultSet consultarFicha(Connection con, int codigo)
	{
		try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Laboratorios "+sqle);
			return null;
		}
	}
	
	
	
	
	public static ResultSet consultarSolicitud(Connection con, int codigo)
	{
		try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarSolicitudStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Laboratorios "+sqle);
			return null;
		}
	}
}
