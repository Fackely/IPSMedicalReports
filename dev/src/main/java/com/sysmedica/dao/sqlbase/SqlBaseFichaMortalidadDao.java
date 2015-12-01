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

public class SqlBaseFichaMortalidadDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaMortalidadDao.class);
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichamortalidad "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaMortalidad," +
    														"codigoPaciente,"+
    														"estado,"+
    														"acronimo,"+
    														"fechaDiligenciamiento," +
    														"horaDiligenciamiento,"+
    														"codigomunprocedencia," +
    														"codigodepprocedencia," +
    														"codigomunnoti," +
    														"codigodepnoti," +
    														"codigoaseguradora," +
    														"nombreprofesionaldiligencio," +
    														"estavivogeneral " +
    														")" +
    												" VALUES(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",'21','11','21','11',?,?,1)";
    
    
    private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichamortalidad "+
															"(" +
															"loginUsuario,"+
															"codigoFichaMortalidad," +
															"codigoPaciente,"+
															"estado,"+
															"acronimo,"+
															"fechaDiligenciamiento," +
															"horaDiligenciamiento,"+
															"codigomunprocedencia," +
															"codigodepprocedencia," +
															"codigomunnoti," +
															"codigodepnoti," +
															"codigoaseguradora," +
															"nombreprofesionaldiligencio," +
															
															"sire, " +
															"sitioDefuncion, " +
															"descripcionSitio, " +
															"convivencia, " +
															"otroConvivencia, " +
															"escolaridad, " +
															"fecundidad, " +
															"gestaciones, " +
															"partos, " +
															"cesareas, " +
															"abortos, " +
															"sustanciasSico, " +
															"trastornoMental, " +
															"infecciones, " +
															"factoresRiesgo, " +
															"controlPrenatal, " +
															"cuantosControles, " +
															"trimInicio, " +
															"controlesRealizadosPor, " +
															"nivelAtencion, " +
															"clasificacionRiesgo, " +
															"remisionesOportunas, " +
															"momentoFallecimiento, " +
															"semanasGestacion, " +
															"tipoParto, " +
															"atendidoPor, " +
															"nivelAtencion2, " +
															"momentoMuerteRelacion, " +
															"edadGestacional, " +
															"pesoNacimiento, " +
															"tallaNacimiento, " +
															"sexo, " +
															"apgarNacimiento1, " +
															"apgarNacimiento5, " +
															"apgarNacimiento15, " +
															"nivelAtencion3, " +
															"adaptacionNeonatal, " +
															"causaDirectaDefuncion, " +
															"causaBasicaDefuncion, " +
															"muerteDemora, " +
															"causaMuerteDet, " +
															
															"fechaConsultaGeneral, " +
															"fechaInicioSintomasGeneral, " +
															"tipoCaso, " +
															"hospitalizadoGeneral, " +
															"fechaHospitalizacionGeneral, " +
															"estaVivoGeneral, " +
															"fechaDefuncion, " +
															"institucionAtendio," +
															"activa," +
															"complicaciones," +
															"muertos, " +
															"vivos, " +
															"semanaInicioCpn, " +
															"quienClasificoRiesgo, " +
															"remisionOportunaComplica, " +
															"muerteDemora1, " +
															"muerteDemora2, " +
															"muerteDemora3, " +
															"muerteDemora4, " +
															"pais, " +
															"areaProcedencia " +
														") " +
														"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,? " +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?) ";
    
    
    private static final String modificarFichaStr="UPDATE epidemiologia.vigifichamortalidad " +
													"SET " +
														"sire=?, " +
														"loginUsuario=?, " +
														"estado=?, " +
														"sitioDefuncion=?, " +
														"descripcionSitio=?, " +
														"convivencia=?, " +
														"otroConvivencia=?, " +
														"escolaridad=?, " +
														"fecundidad=?, " +
														"gestaciones=?, " +
														"partos=?, " +
														"cesareas=?, " +
														"abortos=?, " +
														"sustanciasSico=?, " +
														"trastornoMental=?, " +
														"infecciones=?, " +
														"factoresRiesgo=?, " +
														"controlPrenatal=?, " +
														"cuantosControles=?, " +
														"trimInicio=?, " +
														"controlesRealizadosPor=?, " +
														"nivelAtencion=?, " +
														"clasificacionRiesgo=?, " +
														"remisionesOportunas=?, " +
														"complicaciones=?, " +
														"momentoFallecimiento=?, " +
														"semanasGestacion=?, " +
														"tipoParto=?, " +
														"atendidoPor=?, " +
														"nivelAtencion2=?, " +
														"momentoMuerteRelacion=?, " +
														"edadGestacional=?, " +
														"pesoNacimiento=?, " +
														"tallaNacimiento=?, " +
														"sexo=?, " +
														"apgarNacimiento1=?, " +
														"apgarNacimiento5=?, " +
														"apgarNacimiento15=?, " +
														"nivelAtencion3=?, " +
														"adaptacionNeonatal=?, " +
														"causaDirectaDefuncion=?, " +
														"causaBasicaDefuncion=?, " +
														"muerteDemora=?, " +
														"causaMuerteDet=?, " +
														
														"codigoDepProcedencia=?, " +
														"codigoMunProcedencia=?, " +
														"fechaConsultaGeneral=?, " +
														"fechaInicioSintomasGeneral=?, " +
														"tipoCaso=?, " +
														"hospitalizadoGeneral=?, " +
														"fechaHospitalizacionGeneral=?, " +
														"estaVivoGeneral=?, " +
														"fechaDefuncion=?, " +
														"codigoDepNoti=?, " +
														"codigoMunNoti=?, " +
														"institucionAtendio=?, " +
														"muertos=?, " +
														"vivos=?, " +
														"semanaInicioCpn=?, " +
														"quienClasificoRiesgo=?, " +
														"remisionOportunaComplica=?, " +
														"muerteDemora1=?, " +
														"muerteDemora2=?, " +
														"muerteDemora3=?, " +
														"muerteDemora4=?, " +
														"pais=?, " +
														"areaProcedencia=? " +
													"WHERE codigoFichaMortalidad = ? ";
    
    
    
    
    
    
    public static final String consultarFichaMortalidadStr = "SELECT " +
    															"ficha.sire, " +
    															"ficha.sitioDefuncion, " +
    															"ficha.descripcionSitio, " +
    															"ficha.convivencia, " +
    															"ficha.otroConvivencia, " +
    															"ficha.escolaridad, " +
    															"ficha.fecundidad, " +
    															"ficha.gestaciones, " +
    															"ficha.partos, " +
    															"ficha.cesareas, " +
    															"ficha.abortos, " +
    															"ficha.muertos, " +
    															"ficha.vivos, " +
    															"ficha.sustanciasSico, " +
    															"ficha.trastornoMental, " +
    															"ficha.infecciones, " +
    															"ficha.factoresRiesgo, " +
    															"ficha.controlPrenatal, " +
    															"ficha.cuantosControles, " +
    															"ficha.trimInicio, " +
    															"ficha.controlesRealizadosPor, " +
    															"ficha.nivelAtencion, " +
    															"ficha.clasificacionRiesgo, " +
    															"ficha.remisionesOportunas, " +
    															"ficha.complicaciones, " +
    															"ficha.momentoFallecimiento, " +
    															"ficha.semanasGestacion, " +
    															"ficha.tipoParto, " +
    															"ficha.atendidoPor, " +
    															"ficha.nivelAtencion2, " +
    															"ficha.momentoMuerteRelacion, " +
    															"ficha.edadGestacional, " +
    															"ficha.pesoNacimiento, " +
    															"ficha.tallaNacimiento, " +
    															"ficha.sexo, " +
    															"ficha.apgarNacimiento1, " +
    															"ficha.apgarNacimiento5, " +
    															"ficha.apgarNacimiento15, " +
    															"ficha.nivelAtencion3, " +
    															"ficha.adaptacionNeonatal, " +
    															"ficha.causaDirectaDefuncion, " +
    															"ficha.causaBasicaDefuncion, " +
    															"ficha.muerteDemora, " +
    															"ficha.causaMuerteDet, " +
    															"ficha.semanaInicioCpn, " +
    															"ficha.quienClasificoRiesgo, " +
    															"ficha.remisionOportunaComplica, " +
    															"ficha.muerteDemora1, " +
    															"ficha.muerteDemora2, " +
    															"ficha.muerteDemora3, " +
    															"ficha.muerteDemora4, " +
    															"ficha.pais, " +
    															"ficha.areaProcedencia, " +
    															
															    "ficha.codigoDepProcedencia AS departamentoProcedencia, " +
																"ficha.codigoMunProcedencia AS municipioProcedencia, " +
																"ficha.fechaconsultageneral AS fechaConsultaGeneral, " +
																"ficha.fechainiciosintomasgeneral AS fechaInicioSintomas, " +
																"ficha.tipocaso AS tipoCaso, " +
																"ficha.hospitalizadogeneral AS hospitalizado, " +
																"ficha.fechahospitalizaciongeneral AS fechaHospitalizacion, " +
																"ficha.estavivogeneral AS condicionFinal, " +
																"ficha.fechadefuncion AS fechaDefuncion, " +
																"ficha.nombreProfesionalDiligencio AS nombreProfesional, " +
																"ficha.codigoDepNoti AS departamentoNotifica, " +
																"ficha.codigoMunNoti AS municipioNotifica, " +
																"ficha.institucionAtendio AS nombreUnidad, " +
																"ficha.nombreprofesionaldiligencio AS nombreProfesional, " +
																"ficha.fechaDiligenciamiento AS fechaDiligenciamiento, "+
																    															
																"per.primer_nombre," +
																"per.segundo_nombre," +
																"per.primer_apellido," +
																"per.segundo_apellido," +
																"dep.descripcion AS dep_nacimiento," +
																"ciu.descripcion AS ciu_nacimiento," +
																"dep2.descripcion AS dep_vivienda," +
																"ciu2.descripcion AS ciu_vivienda," +
																"per.direccion AS direccion_paciente," +
																"per.telefono AS telefono_paciente," +
																"per.fecha_nacimiento," +
																"per.sexo AS sexoPaciente," +
																"per.estado_civil," +
																"per.numero_identificacion, " +
																"per.codigo_pais_nacimiento, " +
																"per.codigo_pais_vivienda, " +
																"per.codigo_pais_id, " +
																
																"bar.descripcion AS barrio, " +
																"pac.zona_domicilio AS zonaDomicilio, " +
																"ocup.nombre AS ocupacionPaciente, " +
																"per.tipo_identificacion AS tipoId, " +
																"conv.nombre AS aseguradora, " +
																"regs.nombre AS regimenSalud, " +
																"pac.etnia AS etnia, " +
															//	"ficha.desplazado AS desplazado " +
																"pac.grupo_poblacional as grupoPoblacional " +
															
															"FROM " +
																"epidemiologia.vigifichamortalidad ficha," +
																"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																"usuarios usu, barrios bar, pacientes pac, ocupaciones ocup," +
																"convenios conv, tipos_regimen regs " +
															"WHERE " +
																"ficha.codigoFichaMortalidad = ? " +
																
																"AND per.codigo=ficha.codigoPaciente " +
																"AND dep.codigo_departamento=per.codigo_departamento_nacimiento " +
																"AND dep.codigo_pais=per.codigo_pais_nacimiento " +
																"AND ciu.codigo_ciudad=per.codigo_ciudad_nacimiento " +
																"AND ciu.codigo_departamento=per.codigo_departamento_nacimiento " +
																"AND ciu.codigo_pais=per.codigo_pais_nacimiento " +
																"AND dep2.codigo_departamento=per.codigo_departamento_vivienda " +
																"AND dep2.codigo_pais=per.codigo_pais_vivienda " +
																"AND ciu2.codigo_ciudad=per.codigo_ciudad_vivienda " +
																"AND ciu2.codigo_departamento=per.codigo_departamento_vivienda " +
																"AND ciu2.codigo_pais=per.codigo_pais_vivienda " +
																
																"AND ficha.loginUsuario=usu.login " +
																"AND per.codigo_barrio_vivienda=bar.codigo " +
																"AND per.codigo=pac.codigo_paciente " +
																"AND pac.codigo_paciente=ficha.codigoPaciente " +
																"AND pac.ocupacion=ocup.codigo "+
																"AND conv.codigo=ficha.codigoAseguradora " +
																"AND conv.tipo_regimen=regs.acronimo ";
    
    
    
    private static final String eliminarComplicacionesStr = "DELETE from epidemiologia.vigiDetalleCompEmbarazo WHERE codigoFichaMortalidad = ?";
    
    
    private static final String insertarComplicacionesStr = "INSERT INTO epidemiologia.vigiDetalleCompEmbarazo(codigoComplicacion,codigoFichaMortalidad) VALUES(?,?)";
    
    
    private static final String insertarAntecedentesStr = "INSERT INTO epidemiologia.vigiDetalleAnteRiesgo(codigoAntecedente,codigoFichaMortalidad) VALUES(?,?)";
    
    
    private static final String eliminarAntecedentesStr = "DELETE FROM epidemiologia.vigiDetalleAnteRiesgo WHERE codigoFichaMortalidad = ?";
    
    
    private static final String consultaAntecedentesStr = "SELECT " +
    															"codigoAntecedente " +
    														"FROM " +
    															"epidemiologia.vigiDetalleAnteRiesgo " +
    														"WHERE " +
    															"codigoFichaMortalidad = ?";
    
    
    public static final String consultaComplicacionesStr = "SELECT " +
																		"codigoComplicacion " +
																	"FROM " +
																		"epidemiologia.vigiDetalleCompEmbarazo " +
																	"WHERE " +
																		"codigoFichaMortalidad = ? ";
    
    
    
    
    public static final String consultaDatosPacienteStr = "SELECT " +
															    "per.primer_nombre," +
																"per.segundo_nombre," +
																"per.primer_apellido," +
																"per.segundo_apellido," +
																"dep.descripcion AS dep_vivienda," +
																"ciu.descripcion AS ciu_vivienda," +
																"per.direccion AS direccion_paciente," +
																"per.telefono AS telefono_paciente," +
																"per.fecha_nacimiento," +
																"per.sexo," +
																"per.estado_civil," +
																"per.numero_identificacion, " +
																"per.codigo_pais_nacimiento, " +
																"per.codigo_pais_vivienda, " +
																"per.codigo_pais_id, " +
																
																"bar.descripcion AS barrio, " +
																"pac.zona_domicilio AS zonaDomicilio, " +
																"ocup.nombre AS ocupacionPaciente, " +
																"per.tipo_identificacion AS tipoId, " +
																"conv.nombre AS aseguradora, " +
																"regs.nombre AS regimenSalud, " +
																"pac.etnia AS etnia, " +
																"pac.grupo_poblacional as grupoPoblacional " +
																"FROM personas per " +
																"INNER JOIN departamentos dep ON(dep.codigo_departamento=per.codigo_departamento_vivienda and dep.codigo_pais=per.codigo_pais_vivienda)  " +
																"INNER JOIN ciudades ciu ON(ciu.codigo_ciudad=per.codigo_ciudad_vivienda and ciu.codigo_departamento=per.codigo_departamento_vivienda AND ciu.codigo_pais=per.codigo_pais_vivienda)  " +
																"INNER JOIN barrios bar ON(bar.codigo=per.codigo_barrio_vivienda)  " +
																"INNER JOIN pacientes pac on(pac.codigo_paciente=per.codigo) " +
																"INNER JOIN ocupaciones ocup ON(ocup.codigo=pac.ocupacion)  " +
																"INNER JOIN cuentas c ON(c.codigo_paciente = pac.codigo_paciente) " +
																"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) " +
																"INNER JOIN convenios conv ON(conv.codigo=sc.convenio) " +
																"INNER JOIN tipos_regimen regs ON(regs.acronimo = conv.tipo_regimen) " +
															"WHERE " +
																"per.codigo = ? " ;
    
    
    
    public static final String consultaDatosPacienteStr2 = "SELECT " +
															    "per.primer_nombre," +
																"per.segundo_nombre," +
																"per.primer_apellido," +
																"per.segundo_apellido," +
																"dep.descripcion AS dep_vivienda," +
																"ciu.descripcion AS ciu_vivienda," +
																"per.direccion AS direccion_paciente," +
																"per.telefono AS telefono_paciente," +
																"per.fecha_nacimiento," +
																"per.sexo," +
																"per.estado_civil," +
																"per.numero_identificacion, " +
																"per.codigo_pais_nacimiento, " +
																"per.codigo_pais_vivienda, " +
																"per.codigo_pais_id, " +
																
																"bar.descripcion AS barrio, " +
																"pac.zona_domicilio AS zonaDomicilio, " +
																"ocup.nombre AS ocupacionPaciente, " +
																"per.tipo_identificacion AS tipoId, " +
																"pac.etnia AS etnia, " +
																"pac.grupo_poblacional as grupoPoblacional " +
															"FROM " +
																"personas per, departamentos dep, ciudades ciu, " +
																"barrios bar, pacientes pac, ocupaciones ocup " +
															"WHERE " +
																"per.codigo = ? " +
																"AND dep.codigo_departamento=per.codigo_departamento_vivienda " +
																"AND dep.codigo_pais=per.codigo_pais_vivienda " +
																"AND ciu.codigo_ciudad=per.codigo_ciudad_vivienda " +
																"AND ciu.codigo_departamento=per.codigo_departamento_vivienda " +
																"AND ciu.codigo_pais=per.codigo_pais_vivienda " +
																
																"AND per.codigo_barrio_vivienda=bar.codigo " +
																"AND per.codigo=pac.codigo_paciente " +
																"AND pac.ocupacion=ocup.codigo ";
    
    
																
    
    public static int insertarFicha(Connection con,
									int numeroSolicitud,
									String login,
									int codigoPaciente,
									String codigoDiagnostico,
									int estado,
									int codigoAseguradora,
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
			
			insertarFicha.setInt(1,numeroSolicitud);
			insertarFicha.setString(2,login);
			insertarFicha.setInt(3,codigo);
			insertarFicha.setInt(4,codigoPaciente);
			insertarFicha.setInt(5,estado);
			insertarFicha.setString(6,codigoDiagnostico);
			insertarFicha.setInt(7,codigoAseguradora);
			insertarFicha.setString(8,nombreProfesional);
			
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaMortalidadDao "+sqle.toString() );
			resultado=0;			
		}
		
		return resultado;
	}
    
    
    
    public static int insertarFichaCompleta(Connection con,
											int numeroSolicitud,
											String login,
											int codigoPaciente,
											String codigoDiagnostico,
											int estado,
											int codigoAseguradora,
											String nombreProfesional,
										    String secuencia,
										    
										    String sire,
										    int sitioDefuncion,
											String descripcionSitio,
											int convivencia,
											String otroConvivencia,
											int escolaridad,
											int fecundidad,
											String gestaciones,
											String partos,
											String cesareas,
											String abortos,
											String sustanciasSico,
											String trastornoMental,
											String infecciones,
											String factoresRiesgo,
											int controlPrenatal,
											String cuantosControles,
											int trimInicio,
											int controlesRealizadosPor,
											int nivelAtencion,
											int clasificacionRiesgo,
											int remisionesOportunas,
											HashMap complicaciones,
											String complicacionesAntecedentes,
											int momentoFallecimiento,
											String semanasGestacion,
											int tipoParto,
											int atendidoPor,
											int nivelAtencion2,
											int momentoMuerteRelacion,
											String edadGestacional,
											String pesoNacimiento,
											String tallaNacimiento,
											int sexo,
											String apgarNacimiento1,
											String apgarNacimiento5,
											String apgarNacimiento15,
											int nivelAtencion3,
											int adaptacionNeonatal,
											String causaDirectaDefuncion,
											String causaBasicaDefuncion,
											int muerteDemora,
											int causaMuerteDet,
										    
											String lugarProcedencia,
										    String fechaConsultaGeneral,
										    String fechaInicioSintomasGeneral,
										    int tipoCaso,
										    boolean hospitalizadoGeneral,
										    String fechaHospitalizacionGeneral,
										    boolean estaVivoGeneral,
										    String fechaDefuncion,
										    String lugarNoti,
										    int unidadGeneradora,
										    boolean activa,
										    String muertos,
										    String vivos,
										    int semanaInicioCpn,
										    int quienClasificoRiesgo,
										    int remisionOportunaComplica,
										    String muerteDemora1,
										    String muerteDemora2,
										    String muerteDemora3,
										    String muerteDemora4,
										    HashMap antecedentes,
										    String pais,
										    int areaProcedencia
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
            
            PreparedStatementDecorator insertarFicha =  new PreparedStatementDecorator(con.prepareStatement(insertarFichaCompletaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            // Inserción de los datos de la ficha
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNoti.split("-")[0];
            String codigoDepNoti = lugarNoti.split("-")[1];
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,login,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigo),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoPaciente),3,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estado),4,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDiagnostico,5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoMunProcedencia,6,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDepProcedencia,7,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoMunNoti,8,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDepNoti,9,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoAseguradora),10,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreProfesional,11,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,sire,12,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(sitioDefuncion),13,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,descripcionSitio,14,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(convivencia),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,otroConvivencia,16,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(escolaridad),17,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fecundidad),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,gestaciones,19,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,partos,20,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,cesareas,21,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,abortos,22,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,sustanciasSico,23,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,trastornoMental,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,infecciones,25,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,factoresRiesgo,26,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(controlPrenatal),27,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,cuantosControles,28,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(trimInicio),29,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(controlesRealizadosPor),30,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(nivelAtencion),31,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(clasificacionRiesgo),32,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(remisionesOportunas),33,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(momentoFallecimiento),34,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,semanasGestacion,35,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoParto),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(atendidoPor),37,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(nivelAtencion2),38,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(momentoMuerteRelacion),39,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,edadGestacional,40,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pesoNacimiento,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,tallaNacimiento,42,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(sexo),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,apgarNacimiento1,44,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,apgarNacimiento5,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,apgarNacimiento15,46,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(nivelAtencion3),47,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(adaptacionNeonatal),48,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,causaDirectaDefuncion,49,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,causaBasicaDefuncion,50,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(muerteDemora),51,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(causaMuerteDet),52,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,53,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,54,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),55,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),56,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,57,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),58,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,59,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),60,Types.INTEGER,true,false);
            
            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),61,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,complicacionesAntecedentes,62,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,muertos,63,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,vivos,64,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(semanaInicioCpn),65,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(quienClasificoRiesgo),66,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(remisionOportunaComplica),67,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,muerteDemora1,68,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,muerteDemora2,69,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,muerteDemora3,70,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,muerteDemora4,71,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,72,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),73,Types.INTEGER,true,false);
                        
            resultado = insertarFicha.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }   
            
            
            for (int i=1;i<complicaciones.size()+1;i++) {
		    	
		    	String val = complicaciones.get("complicacion_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarComplicaciones =  new PreparedStatementDecorator(con.prepareStatement(insertarComplicacionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarComplicaciones.setInt(1,i);
			    	insertarComplicaciones.setInt(2,codigo);
			    	
			    	resultado = insertarComplicaciones.executeUpdate();
			    	
			    	if(resultado<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
			    	else {
					    
					    resultado = codigo;
					}
		    	}
		    }
            
            
            for (int i=1;i<antecedentes.size()+1;i++) {
		    	
		    	String val = antecedentes.get("antecedente_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarAntecedentes =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesStr));
			    	
			    	insertarAntecedentes.setInt(1,i);
			    	insertarAntecedentes.setInt(2,codigo);
			    	
			    	resultado = insertarAntecedentes.executeUpdate();
			    	
			    	if(resultado<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
			    	else {
					    
					    resultado = codigo;
					}
		    	}
		    }
            
            
            daoFactory.endTransaction(con);
        }
        catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaMortalidadDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    
    public static int modificarFicha(Connection con,
										String sire,
										String loginUsuario,
									    int codigoFichaMortalidad,
									    int estado,
									    
									    int sitioDefuncion,
										String descripcionSitio,
										int convivencia,
										String otroConvivencia,
										int escolaridad,
										int fecundidad,
										String gestaciones,
										String partos,
										String cesareas,
										String abortos,
										String sustanciasSico,
										String trastornoMental,
										String infecciones,
										String factoresRiesgo,
										int controlPrenatal,
										String cuantosControles,
										int trimInicio,
										int controlesRealizadosPor,
										int nivelAtencion,
										int clasificacionRiesgo,
										int remisionesOportunas,
										HashMap complicaciones,
										int momentoFallecimiento,
										String semanasGestacion,
										int tipoParto,
										int atendidoPor,
										int nivelAtencion2,
										int momentoMuerteRelacion,
										String edadGestacional,
										String pesoNacimiento,
										String tallaNacimiento,
										int sexo,
										String apgarNacimiento1,
										String apgarNacimiento5,
										String apgarNacimiento15,
										int nivelAtencion3,
										int adaptacionNeonatal,
										String causaDirectaDefuncion,
										String causaBasicaDefuncion,
										int muerteDemora,
										int causaMuerteDet,
									    
										String lugarProcedencia,
									    String fechaConsultaGeneral,
									    String fechaInicioSintomasGeneral,
									    int tipoCaso,
									    boolean hospitalizadoGeneral,
									    String fechaHospitalizacionGeneral,
									    boolean estaVivoGeneral,
									    String fechaDefuncion,
									    String lugarNoti,
									    int unidadGeneradora,
									    String complicacionesAntecedentes,
							            String muertos,
									    String vivos,
									    int semanaInicioCpn,
									    int quienClasificoRiesgo,
									    int remisionOportunaComplica,
									    String muerteDemora1,
									    String muerteDemora2,
									    String muerteDemora3,
									    String muerteDemora4,
									    HashMap antecedentes,
									    String pais,
									    int areaProcedencia
									    )
    {
    	int result=0;
		
		try {
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
			
			String codigoMunProcedencia = lugarProcedencia.split("-")[0];
			String codigoDepProcedencia = lugarProcedencia.split("-")[1];
			
			String codigoMunNoti = lugarNoti.split("-")[0];
			String codigoDepNoti = lugarNoti.split("-")[1];
			
			PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,loginUsuario,2,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),3,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(sitioDefuncion),4,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,descripcionSitio,5,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(convivencia),6,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,otroConvivencia,7,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(escolaridad),8,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fecundidad),9,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,gestaciones,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,partos,11,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cesareas,12,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,abortos,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,sustanciasSico,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,trastornoMental,15,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,infecciones,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,factoresRiesgo,17,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(controlPrenatal),18,Types.NUMERIC,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cuantosControles,19,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(trimInicio),20,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(controlesRealizadosPor),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(nivelAtencion),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(clasificacionRiesgo),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(remisionesOportunas),24,Types.NUMERIC,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,complicacionesAntecedentes,25,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(momentoFallecimiento),26,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,semanasGestacion,27,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoParto),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(atendidoPor),29,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(nivelAtencion2),30,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(momentoMuerteRelacion),31,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,edadGestacional,32,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,pesoNacimiento,33,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,tallaNacimiento,34,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(sexo),35,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,apgarNacimiento1,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,apgarNacimiento5,37,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,apgarNacimiento15,38,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(nivelAtencion3),39,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(adaptacionNeonatal),40,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,causaDirectaDefuncion,41,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,causaBasicaDefuncion,42,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(muerteDemora),43,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(causaMuerteDet),44,Types.INTEGER,true,false);
			
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,46,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,47,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,48,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),49,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),50,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,51,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),52,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,53,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,54,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,55,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),56,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,muertos,57,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,vivos,58,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(semanaInicioCpn),59,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(quienClasificoRiesgo),60,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(remisionOportunaComplica),61,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,muerteDemora1,62,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,muerteDemora2,63,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,muerteDemora3,64,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,muerteDemora4,65,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,66,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),67,Types.INTEGER,true,false);
			
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaMortalidad),68,Types.INTEGER,true,false);
			
			result = modificarFicha.executeUpdate();
			
			if(result<1)
			{
				daoFactory.abortTransaction(con);
				return -1; // Estado de error
			}
			
			PreparedStatementDecorator eliminarComplicaciones =  new PreparedStatementDecorator(con.prepareStatement(eliminarComplicacionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			eliminarComplicaciones.setInt(1,codigoFichaMortalidad);
			result = eliminarComplicaciones.executeUpdate();
			
			if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
			
			for (int i=1;i<complicaciones.size()+1;i++) {
		    	
		    	String val = complicaciones.get("complicacion_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarComplicaciones =  new PreparedStatementDecorator(con.prepareStatement(insertarComplicacionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarComplicaciones.setInt(1,i);
			    	insertarComplicaciones.setInt(2,codigoFichaMortalidad);
			    	
			    	result = insertarComplicaciones.executeUpdate();
			    	
			    	if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    }
			
			
			
			PreparedStatementDecorator eliminarAntecedentes =  new PreparedStatementDecorator(con.prepareStatement(eliminarAntecedentesStr));
			
			eliminarAntecedentes.setInt(1,codigoFichaMortalidad);
			result = eliminarAntecedentes.executeUpdate();
			
			if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
			
			for (int i=1;i<antecedentes.size()+1;i++) {
		    	
		    	String val = antecedentes.get("antecedente_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarAntecedentes =  new PreparedStatementDecorator(con.prepareStatement(insertarAntecedentesStr));
			    	
			    	insertarAntecedentes.setInt(1,i);
			    	insertarAntecedentes.setInt(2,codigoFichaMortalidad);
			    	
			    	result = insertarAntecedentes.executeUpdate();
			    	
			    	if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    }
		    
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle)
		{
			logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaMortalidadDao "+sqle.toString() );
			result=0;
		}
		
		return result;
    }
    
    
    
    public static ResultSet consultarTodoFichaMortalidad(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaMortalidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Mortalidad"+sqle);
			return null;
		}
    }
    
    
    
    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarComplicaciones(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaComplicacionesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando las complicaciones en el embarazo (Epidemiologia - ficha de Mortalidad) "+sqle);
			return null;
        }
    }
    
    
    
    
    
    /**
     * 
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarAntecedentes(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaAntecedentesStr));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los antecedentes de riesgo del embarazo (Epidemiologia - ficha de Mortalidad) "+sqle);
			return null;
        }
    }
    
    
    
    public static ResultSet consultarDatosPaciente(Connection con, int codigo,boolean empezarnuevo) {
    	
    	try {
    		String consultaStr = consultaDatosPacienteStr;
    		
    		if (empezarnuevo) {
    			
    			consultaStr = consultaDatosPacienteStr2;
    		}
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigo);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaDengueDao) "+sqle);
			return null;
    	}
    }
}
