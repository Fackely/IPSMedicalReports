package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.apache.log4j.Logger;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.princetonsa.dao.DaoFactory;

public class SqlBaseUtilidadFichasDao {

	private static Logger logger = Logger.getLogger(SqlBaseUtilidadFichasDao.class);
	
	private static final String eliminarFichasInactivasRabiaStr = "DELETE FROM epidemiologia.vigificharabia WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasSarampionStr = "DELETE FROM epidemiologia.vigifichasarampion WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasVIHStr = "DELETE FROM epidemiologia.vigifichavih WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasDengueStr = "DELETE FROM epidemiologia.vigifichadengue WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasParalisisStr = "DELETE FROM epidemiologia.vigifichaparalisis WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasSifilisStr = "DELETE FROM epidemiologia.vigifichasifilis WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasTetanosStr = "DELETE FROM epidemiologia.vigifichatetanos WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasTuberculosisStr = "DELETE FROM epidemiologia.vigifichatuberculosis WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasMortalidadStr = "DELETE FROM epidemiologia.vigifichamortalidad WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasGenericaStr = "DELETE FROM epidemiologia.vigifichagenerica WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasInfeccionesStr = "DELETE FROM epidemiologia.vigifichainfecciones WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasLesionesStr = "DELETE FROM epidemiologia.vigifichalesiones WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasIntoxicacionesStr = "DELETE FROM epidemiologia.vigifichaintoxicacion WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasRubCongenitaStr = "DELETE FROM epidemiologia.vigificharubcongenita WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasOfidicoStr = "DELETE FROM epidemiologia.vigifichaofidico WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasLepraStr = "DELETE FROM epidemiologia.vigifichalepra WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasDifteriaStr = "DELETE FROM epidemiologia.vigifichadifteria WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasEasvStr = "DELETE FROM epidemiologia.vigifichaeasv WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasEsiStr = "DELETE FROM epidemiologia.vigifichaesi WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasEtasStr = "DELETE FROM epidemiologia.vigifichaetas WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasHepatitisStr = "DELETE FROM epidemiologia.vigifichahepatitis WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasLeishmaniasisStr = "DELETE FROM epidemiologia.vigifichaleishmaniasis WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasMalariaStr = "DELETE FROM epidemiologia.vigifichamalaria WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasMeningitisStr = "DELETE FROM epidemiologia.vigifichameningitis WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	private static final String eliminarFichasInactivasTosferinaStr = "DELETE FROM epidemiologia.vigifichatosferina WHERE codigoPaciente=? AND loginUsuario=? AND activa!=1 ";
	
	
	private static final String activacionFichaParte1Str = "UPDATE ";
	
	private static final String activacionFichaParte2Str = " SET activa=? WHERE ";
	
	private static final String activacionFichaParte3Str = " = ? ";
	
	
	
	private static final String eliminarFichaParte1Str = "DELETE FROM ";
	
	private static final String eliminarFichaParte2Str = " WHERE ";
	
	private static final String eliminarFichaParte3Str = " = ? ";
	
	
	
	private static final String insertarLogStr = "INSERT INTO epidemiologia.log_fichas_reportadas " +
												"(" +
													"codigo," +
													"fecha," +
													"hora," +
													"usuario," +
													"paciente," +
													"acronimo," +
													"tipo_cie," +
													"numero_solicitud" +
												") " +
											"VALUES (?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?) ";
	
	
	private static final String consultarParametrosFichasStr="SELECT codigoenfnotificable FROM epidemiologia.vigidetalleservicios WHERE codigoenfnotificable=? AND tiposolicitud=2";
	
	private static final String consultarParametrosFichasExternasStr="SELECT codigoenfnotificable FROM epidemiologia.vigidetalleservicios WHERE codigoenfnotificable=? AND tiposolicitud=1";
	
	
	
	public static int eliminarFichasInactivas(Connection con, String loginUsuario, int codigoPaciente)
	{
		int resultado = 0;
		
		try {
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator eliminarFichasRabia =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasRabiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasSarampion =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasSarampionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasVIH =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasVIHStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasDengue =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasDengueStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasParalisis =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasParalisisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasSifilis =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasSifilisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasTetanos =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasTetanosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasTuberculosis =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasTuberculosisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasMortalidad =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasMortalidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasGenerica =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasGenericaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasInfecciones =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasInfeccionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasLesiones =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasLesionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasIntoxicaciones =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasIntoxicacionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasRubCongenita =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasRubCongenitaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasOfidico =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasOfidicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasLepra =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasLepraStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasDifteria =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasDifteriaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasEasv =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasEasvStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasEsi =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasEsiStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasEtas=  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasEtasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasHepatitis =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasHepatitisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasLeishmaniasis =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasLeishmaniasisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasMalaria =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasMalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasMeningitis =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasMeningitisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			PreparedStatementDecorator eliminarFichasTosferina =  new PreparedStatementDecorator(con.prepareStatement(eliminarFichasInactivasTosferinaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarFichasRabia.setInt(1,codigoPaciente);
			eliminarFichasRabia.setString(2,loginUsuario);
			resultado = eliminarFichasRabia.executeUpdate();
			
			
			eliminarFichasSarampion.setInt(1,codigoPaciente);
			eliminarFichasSarampion.setString(2,loginUsuario);
			resultado = eliminarFichasSarampion.executeUpdate();
			
			
			eliminarFichasVIH.setInt(1,codigoPaciente);
			eliminarFichasVIH.setString(2,loginUsuario);
			resultado = eliminarFichasVIH.executeUpdate();
			
			
			eliminarFichasDengue.setInt(1,codigoPaciente);
			eliminarFichasDengue.setString(2,loginUsuario);
			resultado = eliminarFichasDengue.executeUpdate();
			
			
			eliminarFichasParalisis.setInt(1,codigoPaciente);
			eliminarFichasParalisis.setString(2,loginUsuario);
			resultado = eliminarFichasParalisis.executeUpdate();
			
			
			eliminarFichasSifilis.setInt(1,codigoPaciente);
			eliminarFichasSifilis.setString(2,loginUsuario);
			resultado = eliminarFichasSifilis.executeUpdate();
			
			
			eliminarFichasTetanos.setInt(1,codigoPaciente);
			eliminarFichasTetanos.setString(2,loginUsuario);
			resultado = eliminarFichasTetanos.executeUpdate();
			
			
			eliminarFichasTuberculosis.setInt(1,codigoPaciente);
			eliminarFichasTuberculosis.setString(2,loginUsuario);
			resultado = eliminarFichasTuberculosis.executeUpdate();
			
			
			eliminarFichasMortalidad.setInt(1,codigoPaciente);
			eliminarFichasMortalidad.setString(2,loginUsuario);
			resultado = eliminarFichasMortalidad.executeUpdate();
			
			
			eliminarFichasGenerica.setInt(1,codigoPaciente);
			eliminarFichasGenerica.setString(2,loginUsuario);
			resultado = eliminarFichasGenerica.executeUpdate();
			
			
			eliminarFichasInfecciones.setInt(1,codigoPaciente);
			eliminarFichasInfecciones.setString(2,loginUsuario);
			resultado = eliminarFichasInfecciones.executeUpdate();
			
			
			eliminarFichasLesiones.setInt(1,codigoPaciente);
			eliminarFichasLesiones.setString(2,loginUsuario);
			resultado = eliminarFichasLesiones.executeUpdate();
			
			
			eliminarFichasIntoxicaciones.setInt(1,codigoPaciente);
			eliminarFichasIntoxicaciones.setString(2,loginUsuario);
			resultado = eliminarFichasIntoxicaciones.executeUpdate();
			
			
			eliminarFichasRubCongenita.setInt(1,codigoPaciente);
			eliminarFichasRubCongenita.setString(2,loginUsuario);
			resultado = eliminarFichasRubCongenita.executeUpdate();
			
			
			eliminarFichasOfidico.setInt(1,codigoPaciente);
			eliminarFichasOfidico.setString(2,loginUsuario);
			resultado = eliminarFichasOfidico.executeUpdate();
			
			
			eliminarFichasLepra.setInt(1,codigoPaciente);
			eliminarFichasLepra.setString(2,loginUsuario);
			resultado = eliminarFichasLepra.executeUpdate();
			
			
			eliminarFichasDifteria.setInt(1,codigoPaciente);
			eliminarFichasDifteria.setString(2,loginUsuario);
			resultado = eliminarFichasDifteria.executeUpdate();
			
			
			eliminarFichasEasv.setInt(1,codigoPaciente);
			eliminarFichasEasv.setString(2,loginUsuario);
			resultado = eliminarFichasEasv.executeUpdate();
			
			
			eliminarFichasEsi.setInt(1,codigoPaciente);
			eliminarFichasEsi.setString(2,loginUsuario);
			resultado = eliminarFichasEsi.executeUpdate();
			
			
			eliminarFichasEtas.setInt(1,codigoPaciente);
			eliminarFichasEtas.setString(2,loginUsuario);
			resultado = eliminarFichasEtas.executeUpdate();
			
			
			eliminarFichasHepatitis.setInt(1,codigoPaciente);
			eliminarFichasHepatitis.setString(2,loginUsuario);
			resultado = eliminarFichasHepatitis.executeUpdate();
			
			
			eliminarFichasLeishmaniasis.setInt(1,codigoPaciente);
			eliminarFichasLeishmaniasis.setString(2,loginUsuario);
			resultado = eliminarFichasLeishmaniasis.executeUpdate();
			
			
			eliminarFichasMalaria.setInt(1,codigoPaciente);
			eliminarFichasMalaria.setString(2,loginUsuario);
			resultado = eliminarFichasMalaria.executeUpdate();
			
			
			eliminarFichasMeningitis.setInt(1,codigoPaciente);
			eliminarFichasMeningitis.setString(2,loginUsuario);
			resultado = eliminarFichasMeningitis.executeUpdate();
			
			
			eliminarFichasTosferina.setInt(1,codigoPaciente);
			eliminarFichasTosferina.setString(2,loginUsuario);
			resultado = eliminarFichasTosferina.executeUpdate();
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle) {
			
			logger.warn(sqle+" Error en la eliminacion de fichas : SqlBaseUtilidadFichasDao "+sqle.toString() );
		    resultado=0;
		}
				
		return resultado;
	}
	
	
	
	public static int activarFichaPorCodigo(Connection con, int codigoFicha, int codigoEnfermedadesNotificables)
	{
		int resultado = 0;
		
		String tabla = obtenerTabla(codigoEnfermedadesNotificables);
		
		String codigoFichaStr = obtenerNombreAtrCodigo(codigoEnfermedadesNotificables);
		
		String activarFichaStr = activacionFichaParte1Str + tabla + activacionFichaParte2Str + codigoFichaStr + activacionFichaParte3Str;
		
		try {
			
			PreparedStatementDecorator eliminarFicha =  new PreparedStatementDecorator(con.prepareStatement(activarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarFicha.setInt(1,1);
			eliminarFicha.setInt(2,codigoFicha);
			
			resultado = eliminarFicha.executeUpdate();
			
			
		}
		catch (SQLException sqle)
		{
			logger.warn(sqle+" Error en la activacion de ficha : SqlBaseUtilidadFichasDao "+sqle.toString() );
		    resultado=0;
		}
		
		return resultado;
	}
	
	public static int insertarLogFichasReportadas(Connection con, String loginUsuario,int codigoPaciente,String acronimo, int tipoCie, int numeroSolicitud,String secuencia)
	{
		int resultado=0;
		int codigo;
		
		try {
		
			
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			ResultSet rs = ps.executeQuery();
			
			if (rs.next()) {
				codigo = rs.getInt(1);
			}
			else {
				logger.error("Error obteniendo el código de la secuencia ");
				return 0;
			}
			
			PreparedStatementDecorator insertarLog =  new PreparedStatementDecorator(con.prepareStatement(insertarLogStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			// Inserción de los datos de la ficha
			UtilidadBD.ingresarDatoAStatement(insertarLog,Integer.toString(codigo),1,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarLog,loginUsuario,2,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarLog,Integer.toString(codigoPaciente),3,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarLog,acronimo,4,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarLog,Integer.toString(tipoCie),5,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarLog,Integer.toString(numeroSolicitud),6,Types.INTEGER,true,false);
			/*
			insertarLog.setInt(1,codigo);
			insertarLog.setString(2,loginUsuario);
			insertarLog.setInt(3,codigoPaciente);
			insertarLog.setString(4,acronimo);
			insertarLog.setInt(5,tipoCie);
			insertarLog.setInt(6,numeroSolicitud);
			*/
			resultado = insertarLog.executeUpdate();
			
			if(resultado<1)
			{
				
				return -1; // Estado de error
			}
			else {
			    
			    resultado = codigo;
			}
			
			
		}
		catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseUtilidadFichasDao "+sqle.toString() );
			resultado=0;			
		}
		
		return resultado;
	}
	
	public static int eliminarFichaPorCodigo(Connection con, int codigoFicha, int codigoEnfermedadesNotificables)
	{
		int resultado = 0;
		
		String tabla = obtenerTabla(codigoEnfermedadesNotificables);
		
		String codigoFichaStr = obtenerNombreAtrCodigo(codigoEnfermedadesNotificables);
		
		String activarFichaStr = eliminarFichaParte1Str + tabla + eliminarFichaParte2Str + codigoFichaStr + eliminarFichaParte3Str;
		
		try {
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			PreparedStatementDecorator eliminarFicha =  new PreparedStatementDecorator(con.prepareStatement(activarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarFicha.setInt(1,1);
			eliminarFicha.setInt(2,codigoFicha);
			
			resultado = eliminarFicha.executeUpdate();
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle)
		{
			logger.warn(sqle+" Error en la eliminacion de ficha : SqlBaseUtilidadFichasDao "+sqle.toString() );
		    resultado=0;
		}
		
		return resultado;
	}
	
	
	
	private static String obtenerTabla(int codigoEnfermedadesNotificables)
	{
		String tabla = "";
		
		switch (codigoEnfermedadesNotificables) {
		
			case 1:
			{
				tabla = " epidemiologia.vigifichamalaria ";
				break;
			}
			case 2:
			{
				tabla = " epidemiologia.vigifichahepatitis ";
				break;
			}
			case 3:
			{
				tabla = " epidemiologia.vigifichagenerica ";
				break;
			}
			case 4:
			{
				tabla = " epidemiologia.vigifichavih ";
				break;
			}
			case 5:
			{
				tabla = " epidemiologia.vigifichasarampion ";
				break;
			}
			case 6:
			{
				tabla = " epidemiologia.vigificharabia ";
				break;
			}
			case 7:
			{
				tabla = " epidemiologia.vigifichadengue ";
				break;
			}
			case 8:
			{
				tabla = " epidemiologia.vigifichagenerica ";
				break;
			}
			case 9:
			{
				tabla = " epidemiologia.vigifichasarampion ";
				break;
			}
			case 10:
			{
				tabla = " epidemiologia.vigifichaviolencia ";
				break;
			}
			case 11:
			{
				tabla = " epidemiologia.vigifichadengue ";
				break;
			}
			case 12:
			{
				tabla = " epidemiologia.vigifichaparalisis ";
				break;
			}
			case 13:
			{
				tabla = " epidemiologia.vigifichasifilis ";
				break;
			}
			case 14:
			{
				tabla = " epidemiologia.vigifichatetanos ";
				break;
			}
			case 15:
			{
				tabla = " epidemiologia.vigifichagenerica ";
				break;
			}
			case 16:
			{
				tabla = " epidemiologia.vigifichameningitis ";
				break;
			}
			case 17:
			{
				tabla = " epidemiologia.vigifichameningitis ";
				break;
			}
			case 18:
			{
				tabla = " epidemiologia.vigifichagenerica ";
				break;
			}
			case 19:
			{
				tabla = " epidemiologia.vigifichadifteria ";
				break;
			}
			case 20:
			{
				tabla = " epidemiologia.vigifichagenerica ";
				break;
			}
			case 21:
			{
				tabla = " epidemiologia.vigifichagenerica ";
				break;
			}
			case 22:
			{
				tabla = " epidemiologia.vigifichatosferina ";
				break;
			}
			case 23:
			{
				tabla = " epidemiologia.vigifichatuberculosis ";
				break;
			}
			case 24:
			{
				tabla = " epidemiologia.vigifichagenerica ";
				break;
			}
			case 25:
			{
				tabla = " epidemiologia.vigifichagenerica ";
				break;
			}
			case 26:
			{
				tabla = " epidemiologia.vigifichamortalidad ";
				break;
			}
			case 27:
			{
				tabla = " epidemiologia.vigifichamortalidad ";
				break;
			}
			case 28:
			{
				tabla = " epidemiologia.vigifichainfecciones ";
				break;
			}
			case 29:
			{
				tabla = " epidemiologia.vigifichalesiones ";
				break;
			}
			case 30:
			{
				tabla = " epidemiologia.vigifichaintoxicacion ";
				break;
			}
			case 31:
			{
				tabla = " epidemiologia.vigificharubcongenita ";
				break;
			}
			case 32:
			{
				tabla = " epidemiologia.vigifichalepra ";
				break;
			}
			case 33:
			{
				tabla = " epidemiologia.vigifichaofidico ";
				break;
			}
			case 36:
			{
				tabla = " epidemiologia.vigifichaeasv ";
				break;
			}
			case 37:
			{
				tabla = " epidemiologia.vigifichaesi ";
				break;
			}
			case 39:
			{
				tabla = " epidemiologia.vigifichaetas ";
				break;
			}
			case 40:
			{
				tabla = " epidemiologia.vigifichaleishmaniasis ";
				break;
			}
			case 41:
			{
				tabla = " epidemiologia.vigifichameningitis ";
				break;
			}
		}
		
		return tabla;
	}
	
	
	
	
	
	private static String obtenerNombreAtrCodigo(int codigoEnfermedadesNotificables)
	{
		String tabla = "";
		
		switch (codigoEnfermedadesNotificables) {
		
			case 1:
			{
				tabla = " codigofichamalaria ";
				break;
			}
			case 2:
			{
				tabla = " codigofichahepatitis ";
				break;
			}
			case 3:
			{
				tabla = " codigofichagenerica ";
				break;
			}
			case 4:
			{
				tabla = " codigofichavih ";
				break;
			}
			case 5:
			{
				tabla = " codigofichasarampion ";
				break;
			}
			case 6:
			{
				tabla = " codigoficharabia ";
				break;
			}
			case 7:
			{
				tabla = " codigofichadengue ";
				break;
			}
			case 8:
			{
				tabla = " codigofichagenerica ";
				break;
			}
			case 9:
			{
				tabla = " codigofichasarampion ";
				break;
			}
			case 10:
			{
				tabla = " codigofichaviolencia ";
				break;
			}
			case 11:
			{
				tabla = " codigofichadengue ";
				break;
			}
			case 12:
			{
				tabla = " codigofichaparalisis ";
				break;
			}
			case 13:
			{
				tabla = " codigofichasifilis ";
				break;
			}
			case 14:
			{
				tabla = " codigofichatetanos ";
				break;
			}
			case 15:
			{
				tabla = " codigofichagenerica ";
				break;
			}
			case 16:
			{
				tabla = " codigofichameningitis ";
				break;
			}
			case 17:
			{
				tabla = " codigofichameningitis ";
				break;
			}
			case 18:
			{
				tabla = " codigofichagenerica ";
				break;
			}
			case 19:
			{
				tabla = " codigofichadifteria ";
				break;
			}
			case 20:
			{
				tabla = " codigofichagenerica ";
				break;
			}
			case 21:
			{
				tabla = " codigofichagenerica ";
				break;
			}
			case 22:
			{
				tabla = " codigofichatosferina ";
				break;
			}
			case 23:
			{
				tabla = " codigofichatuberculosis ";
				break;
			}
			case 24:
			{
				tabla = " codigofichagenerica ";
				break;
			}
			case 25:
			{
				tabla = " codigofichagenerica ";
				break;
			}
			case 26:
			{
				tabla = " codigofichamortalidad ";
				break;
			}
			case 27:
			{
				tabla = " codigofichamortalidad ";
				break;
			}
			case 28:
			{
				tabla = " codigofichainfecciones ";
				break;
			}
			case 29:
			{
				tabla = " codigofichalesiones ";
				break;
			}
			case 30:
			{
				tabla = " codigofichaintoxicacion ";
				break;
			}
			case 31:
			{
				tabla = " codigoficharubcongenita ";
				break;
			}
			case 32:
			{
				tabla = " codigofichalepra ";
				break;
			}
			case 33:
			{
				tabla = " codigofichaofidico ";
				break;
			}
			case 36:
			{
				tabla = " codigofichaeasv ";
				break;
			}
			case 37:
			{
				tabla = " codigofichaesi ";
				break;
			}
			case 39:
			{
				tabla = " codigofichaetas ";
				break;
			}
			case 40:
			{
				tabla = " codigofichaleishmaniasis ";
				break;
			}
			case 41:
			{
				tabla = " codigofichameningitis ";
				break;
			}
		}
		
		return tabla;
	}
	
	
	
	
	
	private static String obtenerNombreAtrCodigo2(int codigoEnfermedadesNotificables)
	{
		String tabla = "";
		
		switch (codigoEnfermedadesNotificables) {
		
			case 1:
			{
				tabla = " codigofichamalaria as codigoficha ";
				break;
			}
			case 2:
			{
				tabla = " codigofichahepatitis as codigoficha ";
				break;
			}
			case 3:
			{
				tabla = " codigofichagenerica as codigoficha ";
				break;
			}
			case 4:
			{
				tabla = " codigofichavih as codigoficha ";
				break;
			}
			case 5:
			{
				tabla = " codigofichasarampion as codigoficha ";
				break;
			}
			case 6:
			{
				tabla = " codigoficharabia as codigoficha ";
				break;
			}
			case 7:
			{
				tabla = " codigofichadengue as codigoficha ";
				break;
			}
			case 8:
			{
				tabla = " codigofichagenerica as codigoficha ";
				break;
			}
			case 9:
			{
				tabla = " codigofichasarampion as codigoficha ";
				break;
			}
			case 10:
			{
				tabla = " codigofichaviolencia as codigoficha ";
				break;
			}
			case 11:
			{
				tabla = " codigofichadengue as codigoficha ";
				break;
			}
			case 12:
			{
				tabla = " codigofichaparalisis as codigoficha ";
				break;
			}
			case 13:
			{
				tabla = " codigofichasifilis as codigoficha ";
				break;
			}
			case 14:
			{
				tabla = " codigofichatetanos as codigoficha ";
				break;
			}
			case 15:
			{
				tabla = " codigofichagenerica as codigoficha ";
				break;
			}
			case 16:
			{
				tabla = " codigofichameningitis as codigoficha ";
				break;
			}
			case 17:
			{
				tabla = " codigofichameningitis as codigoficha ";
				break;
			}
			case 18:
			{
				tabla = " codigofichagenerica as codigoficha ";
				break;
			}
			case 19:
			{
				tabla = " codigofichadifteria as codigoficha ";
				break;
			}
			case 20:
			{
				tabla = " codigofichagenerica as codigoficha ";
				break;
			}
			case 21:
			{
				tabla = " codigofichagenerica as codigoficha ";
				break;
			}
			case 22:
			{
				tabla = " codigofichatosferina as codigoficha ";
				break;
			}
			case 23:
			{
				tabla = " codigofichatuberculosis as codigoficha ";
				break;
			}
			case 24:
			{
				tabla = " codigofichagenerica as codigoficha ";
				break;
			}
			case 25:
			{
				tabla = " codigofichagenerica as codigoficha ";
				break;
			}
			case 26:
			{
				tabla = " codigofichamortalidad as codigoficha ";
				break;
			}
			case 27:
			{
				tabla = " codigofichamortalidad as codigoficha ";
				break;
			}
			case 28:
			{
				tabla = " codigofichainfecciones as codigoficha ";
				break;
			}
			case 29:
			{
				tabla = " codigofichalesiones as codigoficha ";
				break;
			}
			case 30:
			{
				tabla = " codigofichaintoxicacion as codigoficha ";
				break;
			}
			case 31:
			{
				tabla = " codigoficharubcongenita as codigoficha ";
				break;
			}
			case 32:
			{
				tabla = " codigofichalepra as codigoficha ";
				break;
			}
			case 33:
			{
				tabla = " codigofichaofidico as codigoficha ";
				break;
			}
			case 36:
			{
				tabla = " codigofichaeasv as codigoficha ";
				break;
			}
			case 37:
			{
				tabla = " codigofichaesi as codigoficha ";
				break;
			}
			case 39:
			{
				tabla = " codigofichaetas as codigoficha ";
				break;
			}
			case 40:
			{
				tabla = " codigofichaleishmaniasis as codigoficha ";
				break;
			}
			case 41:
			{
				tabla = " codigofichameningitis as codigoficha ";
				break;
			}
		}
		
		return tabla;
	}
	
	
	public static ResultSet consultarParametrosFichas(Connection con,int codigo)
	{
		try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarParametrosFichasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando si hay servicios parametrizados para este evento "+sqle);
			return null;
        }
	}
	
	public static ResultSet consultarParametrosFichasExternas(Connection con,int codigo)
	{
		try {
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarParametrosFichasExternasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando si hay servicios externos parametrizados para este evento "+sqle);
			return null;
        }
	}
	
	
	
	public static int consultarFichaInactiva(Connection con, int codigoEnfNotificable, int codigoPaciente, String loginUsuario)
	{
		int resultado = 0;
		String tabla = obtenerTabla(codigoEnfNotificable);
		String atributo = obtenerNombreAtrCodigo2(codigoEnfNotificable);
		
		String consultaStr = "SELECT "+atributo+" FROM "+tabla+" WHERE activa=0 AND codigoPaciente=? AND loginusuario=? ";
		
		try {
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigoPaciente);
            consulta.setString(2,loginUsuario);
            ResultSet rs = consulta.executeQuery();
            
            if (rs.next()) {
            	
            	resultado = rs.getInt("codigoficha");
            }
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando las fichas inactivas : "+sqle);
			return 0;
        }
		
		
		return resultado;
	}
}
