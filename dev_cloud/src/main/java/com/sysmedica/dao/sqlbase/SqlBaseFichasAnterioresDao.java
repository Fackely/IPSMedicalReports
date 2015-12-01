package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

import org.apache.log4j.Logger;

import com.princetonsa.decorator.ResultSetDecorator;

import util.ConstantesBD;
import util.UtilidadBD;

public class SqlBaseFichasAnterioresDao {
	
	/**
     * Objeto que maneja los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichasAnterioresDao.class);

	private static final String busquedaFichaPorPacienteStrParte1="SELECT " +
																    "fechaDiligenciamiento," +
																	"horaDiligenciamiento," +
																	"ficha.acronimo," +
																	"ficha.estado," +
																	"vef.nombre AS nombreEstado," +
																	"loginusuario," +
																	"dia.nombre AS nombreDiagnostico," +
																	"dia.codigoEnfermedadesNotificables AS codigoEnfNot, ";


	private static final String busquedaFichaPorPacienteStrParte2=",diagnosticos dia, epidemiologia.vigiEstadosFicha vef WHERE codigoPaciente=?  " +
																	"AND dia.acronimo=ficha.acronimo " +
															//		"AND ficha.acronimo=? " +
																	"AND ficha.estado=vef.codigo " +
																	"AND ficha.activa=1 ";
	
	
	public static Collection consultaFichasPorPaciente(Connection con, int codigoPaciente, String diagnostico, String codigoDx)
	{
		try {
			
			ResultSet rs;
			Collection coleccion;
			
			String stringConsulta = "";
			
			if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaRabia)))
	        {	            
	            stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaRabia AS codigoFicha FROM epidemiologia.vigiFichaRabia ficha "+busquedaFichaPorPacienteStrParte2;
	        } 
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaSarampion))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaSarampion AS codigoFicha FROM epidemiologia.vigiFichaSarampion ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaRubeola))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaSarampion AS codigoFicha FROM epidemiologia.vigiFichaSarampion ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaVIH))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaVIH AS codigoFicha FROM epidemiologia.vigiFichaVIH ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaFiebreAmarilla))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaDengue AS codigoFicha FROM epidemiologia.vigiFichaDengue ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaDengue))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaDengue AS codigoFicha FROM epidemiologia.vigiFichaDengue ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaParalisisFlacida))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaParalisis AS codigoFicha FROM epidemiologia.vigiFichaParalisis ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaSifilisCongenita))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaSifilis AS codigoFicha FROM epidemiologia.vigiFichaSifilis ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaTetanosNeo))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaTetanos AS codigoFicha FROM epidemiologia.vigiFichaTetanos ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaTuberculosis))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaTuberculosis AS codigoFicha FROM epidemiologia.vigiFichaTuberculosis ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaMortalidadMaterna))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaMortalidad AS codigoFicha FROM epidemiologia.vigiFichaMortalidad ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaMortalidadPerinatal))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaMortalidad AS codigoFicha FROM epidemiologia.vigiFichaMortalidad ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaInfecciones))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaInfecciones AS codigoFicha FROM epidemiologia.vigiFichaInfecciones ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaLesiones))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaLesiones AS codigoFicha FROM epidemiologia.vigiFichaLesiones ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaGenerica AS codigoFicha FROM epidemiologia.vigiFichaGenerica ficha "+busquedaFichaPorPacienteStrParte2+" AND dia.codigoenfermedadesnotificables="+diagnostico;
	        }
			
			
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(stringConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			consulta.setInt(1,codigoPaciente);
			
			return UtilidadBD.resultSet2Collection(new ResultSetDecorator(consulta.executeQuery()));
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando las fichas anteriores para este paciente "+sqle);
			return null;
		}
	}
}
