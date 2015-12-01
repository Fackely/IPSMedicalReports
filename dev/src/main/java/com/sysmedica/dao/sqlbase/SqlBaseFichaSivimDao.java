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

public class SqlBaseFichaSivimDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaSivimDao.class);
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichamortalidad "+
    														"(" +
    														"loginUsuario,"+
    														"codigoFichaSivim," +
    														"codigoPaciente,"+
    														"estado,"+
    														"acronimo,"+
    														"fechaDiligenciamiento," +
    														"horaDiligenciamiento,"+
    														"codigomunprocedencia," +
    														"codigodepprocedencia," +
    														"nombreprofesionaldiligencio)" +
    												" VALUES(?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",2,5,?)";
    
    
    private static final String modificarFichaStr="UPDATE epidemiologia.vigifichasivim " +
													"SET " +
														"loginUsuario=?, " +
														"estado=?, " +
														"escolaridad=?, " +
													    "mujerGestante=?, " +
													    "habitanteCalle=?, " +
													    "personaDiscapacitada=?, " +
													    "tipoAtencion=?, " +
													    "solo=?, " +
													    "padre=?, " +
													    "madre=?, " +
													    "padrastro=?, " +
													    "madrastra=?, " +
													    "hermanos=?, " +
													    "conyuge=?, " +
													    "hijos=?, " +
													    "abuelos=?, " +
													    "otros=?, " +
													    "violenciaFisica=?, " +
													    "violenciaEmocional=?, " +
													    "violenciaSexual=?, " +
													    "violenciEconomica=?, " +
													    "violenciaNegligencia=?, " +
													    "violenciaAbandono=?, " +
													    "ocurrioAntesFisica=?, " +
													    "ocurrioAntesEmocional=?, " +
													    "ocurrioAntesSexual=?, " +
													    "ocurrioAntesEconomica=?, " +
													    "ocurrioAntesNegligencia=?, " +
													    "ocurrioAntesAbandono=?, " +
													    "lugarFisica=?, " +
													    "lugarEmocional=?, " +
													    "lugarSexual=?, " +
													    "lugarEconomica=?, " +
													    "lugarNegligencia=?, " +
													    "lugarAbandono=?, " +
													    "observaciones=? " +
													    
													    "codigoDepProcedencia=?, " +
														"codigoMunProcedencia=?, " +
														"fechaConsultaGeneral=?, " +
														"institucionAtendio=? " +
													 "WHERE codigoFichaSisvan = ? ";
    
    
    
    public static final String consultarFichaSivimStr = "SELECT " +
    
															    "ficha.escolaridad," +
															    "ficha.mujerGestante," +
															    "ficha.habitanteCalle," +
															    "ficha.personaDiscapacitada," +
															    "ficha.tipoAtencion," +
															    "ficha.solo," +
															    "ficha.padre," +
															    "ficha.madre," +
															    "ficha.padrastro," +
															    "ficha.madrastra," +
															    "ficha.hermanos," +
															    "ficha.conyuge," +
															    "ficha.hijos," +
															    "ficha.abuelos," +
															    "ficha.otros," +
															    "ficha.violenciaFisica," +
															    "ficha.violenciaEmocional," +
															    "ficha.violenciaSexual," +
															    "ficha.violenciEconomica," +
															    "ficha.violenciaNegligencia," +
															    "ficha.violenciaAbandono," +
															    "ficha.ocurrioAntesFisica," +
															    "ficha.ocurrioAntesEmocional," +
															    "ficha.ocurrioAntesSexual," +
															    "ficha.ocurrioAntesEconomica," +
															    "ficha.ocurrioAntesNegligencia," +
															    "ficha.ocurrioAntesAbandono," +
															    "ficha.lugarFisica," +
															    "ficha.lugarEmocional," +
															    "ficha.lugarSexual," +
															    "ficha.lugarEconomica," +
															    "ficha.lugarNegligencia," +
															    "ficha.lugarAbandono," +
															    "ficha.observaciones," +
    
															    "ficha.codigoDepProcedencia AS departamentoProcedencia, " +
																"ficha.codigoMunProcedencia AS municipioProcedencia, " +
																"ficha.fechaconsultageneral AS fechaConsultaGeneral, " +
																"ficha.nombreProfesionalDiligencio AS nombreProfesional, " +
																"ficha.institucionAtendio AS nombreUnidad, " +
																"ficha.nombreprofesionaldiligencio AS nombreProfesional, "+
																    															
																"per.primer_nombre," +
																"per.segundo_nombre," +
																"per.primer_apellido," +
																"per.segundo_apellido," +
																"dep.nombre AS dep_nacimiento," +
																"ciu.nombre AS ciu_nacimiento," +
																"dep2.nombre AS dep_vivienda," +
																"ciu2.nombre AS ciu_vivienda," +
																"per.direccion AS direccion_paciente," +
																"per.telefono AS telefono_paciente," +
																"per.fecha_nacimiento," +
																"per.sexo," +
																"per.estado_civil," +
																"per.numero_identificacion, " +
																"per.codigo_pais_nacimiento, " +
																"per.codigo_pais_vivienda, " +
																"per.codigo_pais_id, " +
																
																"bar.nombre AS barrio, " +
																"pac.zona_domicilio AS zonaDomicilio, " +
																"ocup.nombre AS ocupacionPaciente, " +
																"per.tipo_identificacion AS tipoId, " +
																"conv.nombre AS aseguradora, " +
																"regs.nombre AS regimenSalud, " +
																"pac.etnia AS etnia, " +
																"ficha.desplazado AS desplazado " +
															
															"FROM " +
																"epidemiologia.vigifichasivim ficha," +
																"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																"usuarios usu, barrios bar, pacientes pac, ocupaciones ocup," +
																"convenios conv, tipos_regimen regs " +
															"WHERE " +
																"ficha.codigoFichaSivim = ? " +
																
																"AND per.codigo=ficha.codigoPaciente " +
																"AND dep.codigo=per.codigo_departamento_nacimiento " +
																"AND ciu.codigo_ciudad=per.codigo_ciudad_nacimiento " +
																"AND ciu.codigo_departamento=per.codigo_departamento_nacimiento " +
																"AND dep2.codigo=per.codigo_departamento_vivienda " +
																"AND ciu2.codigo_ciudad=per.codigo_ciudad_vivienda " +
																"AND ciu2.codigo_departamento=per.codigo_departamento_vivienda " +
																
																"AND ficha.loginUsuario=usu.login " +
																"AND per.codigo_departamento_vivienda=bar.codigo_departamento " +
																"AND per.codigo_ciudad_vivienda=bar.codigo_ciudad " +
																"AND per.codigo_barrio_vivienda=bar.codigo_barrio " +
																"AND per.codigo=pac.codigo_paciente " +
																"AND pac.codigo_paciente=ficha.codigoPaciente " +
																"AND pac.ocupacion=ocup.codigo "+
																"AND conv.codigo=ficha.codigoAseguradora " +
																"AND conv.tipo_regimen=regs.acronimo ";
    
    
    
    public static int insertarFicha(Connection con,
									String login,
									int codigoPaciente,
									String codigoDiagnostico,
									int estado,
									String nombreProfesional,
								    String secuencia) 
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
			
			insertarFicha.setString(1,login);
			insertarFicha.setInt(2,codigo);
			insertarFicha.setInt(3,codigoPaciente);
			insertarFicha.setInt(4,estado);
			insertarFicha.setString(5,codigoDiagnostico);
			insertarFicha.setString(6,nombreProfesional);
			
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaSivimDao "+sqle.toString() );
			resultado=0;			
		}
		
		return resultado;
	}
    
    
    
    public static int modificarFicha(Connection con,
										String loginUsuario,
									    int codigoFichaSivim,
									    int estado,
									    
									    int escolaridad,
									    boolean mujerGestante,
									    boolean habitanteCalle,
									    boolean personaDiscapacitada,
									    int tipoAtencion,
									    boolean solo,
									    boolean padre,
									    boolean madre,
									    boolean padrastro,
									    boolean madrastra,
									    boolean hermanos,
									    boolean conyuge,
									    boolean hijos,
									    boolean abuelos,
									    boolean otros,
									    boolean violenciaFisica,
									    boolean violenciaEmocional,
									    boolean violenciaSexual,
									    boolean violenciaEconomica,
									    boolean violenciaNegligencia,
									    boolean violenciaAbandono,
									    boolean ocurrioAntesFisica,
									    boolean ocurrioAntesEmocional,
									    boolean ocurrioAntesSexual,
									    boolean ocurrioAntesEconomica,
									    boolean ocurrioAntesNegligencia,
									    boolean ocurrioAntesAbandono,
									    int lugarFisica,
									    int lugarEmocional,
									    int lugarSexual,
									    int lugarEconomica,
									    int lugarNegligencia,
									    int lugarAbandono,
									    String observaciones,
									    
									    String lugarProcedencia,
									    String fechaConsultaGeneral,
									    int unidadGeneradora
									)
    {
    	int result=0;
		
		try {
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			String codigoMunProcedencia = lugarProcedencia.split("-")[0];
			String codigoDepProcedencia = lugarProcedencia.split("-")[1];
			
			PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			UtilidadBD.ingresarDatoAStatement(modificarFicha,loginUsuario,1,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),2,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(escolaridad),3,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(mujerGestante),4,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(habitanteCalle),5,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(personaDiscapacitada),6,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoAtencion),7,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(solo),8,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(padre),9,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(madre),10,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(padrastro),11,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(madrastra),12,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hermanos),13,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(conyuge),14,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hijos),15,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(abuelos),16,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(otros),17,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(violenciaFisica),18,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(violenciaEmocional),19,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(violenciaSexual),20,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(violenciaEconomica),21,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(violenciaNegligencia),22,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(violenciaAbandono),23,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(ocurrioAntesFisica),24,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(ocurrioAntesEmocional),25,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(ocurrioAntesSexual),26,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(ocurrioAntesEconomica),27,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(ocurrioAntesNegligencia),28,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(ocurrioAntesAbandono),29,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarFisica),30,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarEmocional),31,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarSexual),32,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarEconomica),33,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarNegligencia),34,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarAbandono),35,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,observaciones,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,37,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,38,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,39,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),40,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaSivim),41,Types.INTEGER,true,false);
			
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
			logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaSivimDao "+sqle.toString() );
			result=0;
		}
		
		return result;
    }
    
    
    
    public static ResultSet consultarTodoFichaSivim(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaSivimStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de SIVIM"+sqle);
			return null;
		}
    }
}
