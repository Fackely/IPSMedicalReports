package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

public class SqlBaseFichaBrotesDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaBrotesDao.class);
    
    
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichabrotes "+
														"(" +
														"loginUsuario,"+
														"codigoFichaBrotes," +
														"estado,"+
														"fechaDiligenciamiento," +
														"horaDiligenciamiento," +
														"municipioNotifica," +
														"departamentoNotifica," +
														"sire," +
														"evento," +
														"fechaNotificacion," +
														"unidadgeneradora, " +
														"pacientesgrupo1," +
														"pacientesgrupo2," +
														"pacientesgrupo3," +
														"pacientesgrupo4," +
														"pacientesgrupo5," +
														"pacientesgrupo6," +
														"probables," +
														"confirmadoslaboratorio," +
														"confirmadosclinica," +
														"confirmadosnexo," +
														"hombres," +
														"mujeres," +
														"vivos," +
														"muertos," +
														"municipioProcedencia," +
														"departamentoProcedencia," +
														"nombreprofesionaldiligencio," +
														"telefonocontacto," +
														"muestrabiologica," +
														"muestraalimentos," +
														"agenteetiologico1," +
														"agenteetiologico2," +
														"alimentos," +
														"lugarconsumo," +
														"observaciones, " +
														"fechaInvestigacion, " +
														"muestraSuperficies, " +
														"estudioManipuladores, " +
														"agenteBiologica1, " +
														"agenteBiologica2, " +
														"agenteBiologica3, " +
														"agenteBiologica4, " +
														"agenteAlimentos1, " +
														"agenteAlimentos2, " +
														"agenteAlimentos3, " +
														"agenteAlimentos4, " +
														"agenteSuperficies1, " +
														"agenteSuperficies2, " +
														"agenteSuperficies3, " +
														"agenteSuperficies4, " +
														"agenteManipuladores1, " +
														"agenteManipuladores2, " +
														"agenteManipuladores3, " +
														"agenteManipuladores4, " +
														"lugarConsumoImplicado, " +
														"factorDeterminante, " +
														"medidaSanitaria, " +
														"paisNotifica" +
														") " +
													"VALUES (?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    
    
    private static final String modificarFichaStr="UPDATE epidemiologia.vigifichabrotes " +
														"SET " +
															"sire=?, " +
															"loginusuario=?, " +
															"evento=?, " +
															"fechanotificacion=?, " +
															"municipionotifica=?, " +
															"departamentonotifica=?, " +
															"unidadgeneradora=?, " +
															"pacientesgrupo1=?," +
															"pacientesgrupo2=?," +
															"pacientesgrupo3=?," +
															"pacientesgrupo4=?," +
															"pacientesgrupo5=?," +
															"pacientesgrupo6=?," +
															"probables=?," +
															"confirmadoslaboratorio=?," +
															"confirmadosclinica=?," +
															"confirmadosnexo=?," +
															"hombres=?," +
															"mujeres=?," +
															"vivos=?," +
															"muertos=?," +
															"municipioprocedencia=?," +
															"departamentoprocedencia=?," +
															"nombreprofesionaldiligencio=?," +
															"telefonocontacto=?," +
															"muestrabiologica=?," +
															"muestraalimentos=?," +
															"agenteetiologico1=?," +
															"agenteetiologico2=?," +
															"alimentos=?," +
															"lugarconsumo=?," +
															"observaciones=?, " +
															"fechaInvestigacion=?, " +
															"muestraSuperficies=?, " +
															"estudioManipuladores=?, " +
															"agenteBiologica1=?, " +
															"agenteBiologica2=?, " +
															"agenteBiologica3=?, " +
															"agenteBiologica4=?, " +
															"agenteAlimentos1=?, " +
															"agenteAlimentos2=?, " +
															"agenteAlimentos3=?, " +
															"agenteAlimentos4=?, " +
															"agenteSuperficies1=?, " +
															"agenteSuperficies2=?, " +
															"agenteSuperficies3=?, " +
															"agenteSuperficies4=?, " +
															"agenteManipuladores1=?, " +
															"agenteManipuladores2=?, " +
															"agenteManipuladores3=?, " +
															"agenteManipuladores4=?, " +
															"lugarConsumoImplicado=?, " +
															"factorDeterminante=?, " +
															"medidaSanitaria=?, " +
															"paisNotifica=? " +
														"WHERE codigoFichaBrotes=? ";
    
    
    public static final String consultarFichaBrotesStr = "SELECT " +
															"ficha.sire, " +
															"ficha.fechadiligenciamiento, " +
															"ficha.horadiligenciamiento, " +
															"ficha.loginusuario, " +
															"ficha.codigofichabrotes, " +
															"ficha.evento, " +
															"ficha.fechanotificacion, " +
															"ficha.paisNotifica, " +
															"ficha.municipionotifica, " +
															"ficha.departamentonotifica, " +
															"ficha.unidadgeneradora, " +
															"ficha.pacientesgrupo1," +
															"ficha.pacientesgrupo2," +
															"ficha.pacientesgrupo3," +
															"ficha.pacientesgrupo4," +
															"ficha.pacientesgrupo5," +
															"ficha.pacientesgrupo6," +
															"ficha.probables," +
															"ficha.confirmadoslaboratorio," +
															"ficha.confirmadosclinica," +
															"ficha.confirmadosnexo," +
															"ficha.hombres," +
															"ficha.mujeres," +
															"ficha.vivos," +
															"ficha.muertos," +
															"ficha.municipioprocedencia," +
															"ficha.departamentoprocedencia," +
															"ficha.nombreprofesionaldiligencio," +
															"ficha.telefonocontacto," +
															"ficha.muestrabiologica," +
															"ficha.muestraalimentos," +
															"ficha.agenteetiologico1," +
															"ficha.agenteetiologico2," +
															"ficha.alimentos," +
															"ficha.lugarconsumo," +
															"ficha.observaciones, " +
															
															"ficha.fechaInvestigacion, " +
															"ficha.muestraSuperficies, " +
															"ficha.estudioManipuladores, " +
															"ficha.agenteBiologica1, " +
															"ficha.agenteBiologica2, " +
															"ficha.agenteBiologica3, " +
															"ficha.agenteBiologica4, " +
															"ficha.agenteAlimentos1, " +
															"ficha.agenteAlimentos2, " +
															"ficha.agenteAlimentos3, " +
															"ficha.agenteAlimentos4, " +
															"ficha.agenteSuperficies1, " +
															"ficha.agenteSuperficies2, " +
															"ficha.agenteSuperficies3, " +
															"ficha.agenteSuperficies4, " +
															"ficha.agenteManipuladores1, " +
															"ficha.agenteManipuladores2, " +
															"ficha.agenteManipuladores3, " +
															"ficha.agenteManipuladores4, " +
															"ficha.lugarConsumoImplicado, " +
															"ficha.factorDeterminante, " +
															"ficha.medidaSanitaria " +
														"FROM " +
															"epidemiologia.vigifichabrotes ficha " +
														"WHERE " +
															"ficha.codigofichabrotes=? ";
    
    
    
    public static int insertarFicha(Connection con,
									String login,
									int estado,
								    String secuencia,
								    
								    String sire,
									String loginUsuario,
								    int codigoFichaBrotes,
								    
								    int evento,
									String fechaNotificacion,
									String paisNotifica,
									int departamentoNotifica,
									int ciudadNotifica,
									String lugarNotifica,
									int unidadGeneradora,
									
									int pacientesGrupo1,
									int pacientesGrupo2,
									int pacientesGrupo3,
									int pacientesGrupo4,
									int pacientesGrupo5,
									int pacientesGrupo6,
									int probables,
									int confirmadosLaboratorio,
									int confirmadosClinica,
									int confirmadosNexo,
									int hombres,
									int mujeres,
									int vivos,
									int muertos,
									int departamentoProcedencia,
									int ciudadProcedencia,
									String lugarProcedencia,
									String nombreProfesional,
									String telefonoContacto,
									
									int muestraBiologica,
									String agenteEtiologico1,
									String alimentosImplicados,
									int muestraAlimentos,
									String agenteEtiologico2,
									String lugarConsumo,
									String observaciones,
									
									String fechaInvestigacion, 
									int muestraSuperficies,
									int estudioManipuladores, 
									int agenteBiologica1, 
									int agenteBiologica2, 
									int agenteBiologica3, 
									int agenteBiologica4, 
									int agenteAlimentos1, 
									int agenteAlimentos2, 
									int agenteAlimentos3, 
									int agenteAlimentos4, 
									int agenteSuperficies1, 
									int agenteSuperficies2, 
									int agenteSuperficies3, 
									int agenteSuperficies4, 
									int agenteManipuladores1,
									int agenteManipuladores2,
									int agenteManipuladores3,
									int agenteManipuladores4,
									int lugarConsumoImplicado,
									int factorDeterminante,
									int medidaSanitaria
								    )
    {
    	int resultado=0;
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
			
			String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNotifica.split("-")[0];
            String codigoDepNoti = lugarNotifica.split("-")[1];
			
			insertarFicha.setString(1,login);
			insertarFicha.setInt(2,codigo);
			insertarFicha.setInt(3,estado);
            insertarFicha.setString(4,codigoMunNoti);
            insertarFicha.setString(5,codigoDepNoti);
            insertarFicha.setString(6,sire);
            insertarFicha.setInt(7,evento);
            insertarFicha.setString(8,UtilidadFecha.conversionFormatoFechaABD(fechaNotificacion));
            insertarFicha.setInt(9,unidadGeneradora);
            insertarFicha.setInt(10,pacientesGrupo1);
            insertarFicha.setInt(11,pacientesGrupo2);
            insertarFicha.setInt(12,pacientesGrupo3);
            insertarFicha.setInt(13,pacientesGrupo4);
            insertarFicha.setInt(14,pacientesGrupo5);
            insertarFicha.setInt(15,pacientesGrupo6);
            insertarFicha.setInt(16,probables);
            insertarFicha.setInt(17,confirmadosLaboratorio);
            insertarFicha.setInt(18,confirmadosClinica);
            insertarFicha.setInt(19,confirmadosNexo);
            insertarFicha.setInt(20,hombres);
            insertarFicha.setInt(21,mujeres);
            insertarFicha.setInt(22,vivos);
            insertarFicha.setInt(23,muertos);
            insertarFicha.setString(24,codigoMunProcedencia);
            insertarFicha.setString(25,codigoDepProcedencia);
            insertarFicha.setString(26,nombreProfesional);
            insertarFicha.setString(27,telefonoContacto);
            insertarFicha.setInt(28,muestraBiologica);
            insertarFicha.setInt(29,muestraAlimentos);
            insertarFicha.setString(30,agenteEtiologico1);
            insertarFicha.setString(31,agenteEtiologico2);
            insertarFicha.setString(32,alimentosImplicados);
            insertarFicha.setString(33,lugarConsumo);
            insertarFicha.setString(34,observaciones);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInvestigacion,35,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(muestraSuperficies),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estudioManipuladores),37,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteBiologica1),38,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteBiologica2),39,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteBiologica3),40,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteBiologica4),41,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteAlimentos1),42,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteAlimentos2),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteAlimentos3),44,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteAlimentos4),45,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteSuperficies1),46,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteSuperficies2),47,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteSuperficies3),48,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteSuperficies4),49,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteManipuladores1),50,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteManipuladores2),51,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteManipuladores3),52,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agenteManipuladores4),53,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(lugarConsumoImplicado),54,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(factorDeterminante),55,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(medidaSanitaria),56,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,paisNotifica,57,Types.VARCHAR,true,false);
            
			resultado = insertarFicha.executeUpdate();
			
			if(resultado<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			else {
			
				resultado = codigo;
			}
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaBrotesDao "+sqle.toString() );
			resultado=0;			
		}
		
		return resultado;
    }
    
    
    
    
    public static int modificarFicha(Connection con,
										String sire,
										String loginUsuario,
									    int codigoFichaBrotes,
									    int estado,
									    
									    int evento,
										String fechaNotificacion,
										String paisNotifica,
										int departamentoNotifica,
										int ciudadNotifica,
										String lugarNotifica,
										int unidadGeneradora,
										
										int pacientesGrupo1,
										int pacientesGrupo2,
										int pacientesGrupo3,
										int pacientesGrupo4,
										int pacientesGrupo5,
										int pacientesGrupo6,
										int probables,
										int confirmadosLaboratorio,
										int confirmadosClinica,
										int confirmadosNexo,
										int hombres,
										int mujeres,
										int vivos,
										int muertos,
										int departamentoProcedencia,
										int ciudadProcedencia,
										String lugarProcedencia,
										String nombreProfesional,
										String telefonoContacto,
										
										int muestraBiologica,
										String agenteEtiologico1,
										String alimentosImplicados,
										int muestraAlimentos,
										String agenteEtiologico2,
										String lugarConsumo,
										String observaciones,
										
										String fechaInvestigacion, 
										int muestraSuperficies,
										int estudioManipuladores, 
										int agenteBiologica1, 
										int agenteBiologica2, 
										int agenteBiologica3, 
										int agenteBiologica4, 
										int agenteAlimentos1, 
										int agenteAlimentos2, 
										int agenteAlimentos3, 
										int agenteAlimentos4, 
										int agenteSuperficies1, 
										int agenteSuperficies2, 
										int agenteSuperficies3, 
										int agenteSuperficies4, 
										int agenteManipuladores1,
										int agenteManipuladores2,
										int agenteManipuladores3,
										int agenteManipuladores4,
										int lugarConsumoImplicado,
										int factorDeterminante,
										int medidaSanitaria
																		    )
    {
    	int result=0;
    	
    	try {
    		
    		DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNotifica.split("-")[0];
            String codigoDepNoti = lugarNotifica.split("-")[1];
            
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,loginUsuario,2,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(evento),3,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,UtilidadFecha.conversionFormatoFechaABD(fechaNotificacion),4,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,6,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),7,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(pacientesGrupo1),8,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(pacientesGrupo2),9,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(pacientesGrupo3),10,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(pacientesGrupo4),11,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(pacientesGrupo5),12,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(pacientesGrupo6),13,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(probables),14,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(confirmadosLaboratorio),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(confirmadosClinica),16,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(confirmadosNexo),17,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(hombres),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(mujeres),19,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vivos),20,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(muertos),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,22,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,23,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreProfesional,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,telefonoContacto,25,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(muestraBiologica),26,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(muestraAlimentos),27,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,agenteEtiologico1,28,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,agenteEtiologico2,29,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,alimentosImplicados,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarConsumo,31,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,observaciones,32,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInvestigacion,33,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(muestraSuperficies),34,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estudioManipuladores),35,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteBiologica1),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteBiologica2),37,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteBiologica3),38,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteBiologica4),39,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteAlimentos1),40,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteAlimentos2),41,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteAlimentos3),42,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteAlimentos4),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteSuperficies1),44,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteSuperficies2),45,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteSuperficies3),46,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteSuperficies4),47,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteManipuladores1),48,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteManipuladores2),49,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteManipuladores3),50,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agenteManipuladores4),51,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarConsumoImplicado),52,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(factorDeterminante),53,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(medidaSanitaria),54,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,paisNotifica,54,Types.VARCHAR,true,false);
            
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaBrotes),55,Types.INTEGER,true,false);
            
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
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaBrotesDao "+sqle.toString() );
		    result=0;
        }
    	
    	return result;
    }
    
    
    
    public static ResultSet consultarTodoFichaBrotes(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaBrotesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Brotes "+sqle);
			return null;
		}
    }
}
