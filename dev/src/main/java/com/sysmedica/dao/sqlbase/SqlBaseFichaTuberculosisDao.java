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

public class SqlBaseFichaTuberculosisDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaTuberculosisDao.class);
    
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichatuberculosis "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaTuberculosis," +
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
    														"nombreprofesionaldiligencio)" +
    												" VALUES(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",'21','11','21','11',?,?)";
    
    
    private static final String modificarFichaStr="UPDATE epidemiologia.vigifichatuberculosis " +
													"SET " +
														"sire=?, " +
														"loginUsuario=?, " +
														"estado=?, " +
														"baciloscopia=?, " +
														"cultivo=?, " +
														"histopatologia=?, " +
														"clinicaPaciente=?, " +
														"nexoEpidemiologico=?, " +
														"radiologico=?, " +
														"tuberculina=?, " +
														"ada=?, " +
														"otroDx=?, " +
														"bk=?, " +
														"valorada=?, " +
														"valortuberculina=?, " +
														"tipoTuberculosis=?, " +
														"cicatrizVacuna=?, " +
														"fuenteContagio=?, " +
														"metodoHallazgo=?, " +
														"otroMetodo=?, " +
														"asociacionVih=?, " +
														"asesoriaVih=?, " +
														"observaciones=?, " +														
														
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
														"institucionAtendio=?," +
														"realizoCultivo=?," +
														"otroTipoTuberculosis=?, " +
														"fuenteContagio2=?, " +
														"pais=?, " +
														"areaProcedencia=? " +
													"WHERE codigoFichaTuberculosis = ? ";
    
    
    
    private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichatuberculosis "+
															"(" +
															"loginUsuario,"+
															"codigoFichaTuberculosis," +
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
															"baciloscopia, " +
															"cultivo, " +
															"histopatologia, " +
															"clinicaPaciente, " +
															"nexoEpidemiologico, " +
															"radiologico, " +
															"tuberculina, " +
															"ada, " +
															"otroDx, " +
															"bk, " +
															"valorada, " +
															"valortuberculina, " +
															"tipoTuberculosis, " +
															"cicatrizVacuna, " +
															"fuenteContagio, " +
															"metodoHallazgo, " +
															"otroMetodo, " +
															"asociacionVih, " +
															"asesoriaVih, " +
															"observaciones, " +														
															
															"fechaConsultaGeneral, " +
															"fechaInicioSintomasGeneral, " +
															"tipoCaso, " +
															"hospitalizadoGeneral, " +
															"fechaHospitalizacionGeneral, " +
															"estaVivoGeneral, " +
															"fechaDefuncion, " +
															"institucionAtendio," +
															"activa, " +
															"realizoCultivo," +
															"otroTipoTuberculosis, " +
															"fuenteContagio2, " +
															"pais, " +
															"areaProcedencia " +
															")" +
														"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    
    
    public static final String consultarFichaStr = "SELECT " +
																	"ficha.sire, " +
																	"ficha.estado, " +
																	"ficha.baciloscopia, " +
																	"ficha.cultivo," +
																	"ficha.histopatologia," +
																	"ficha.clinicapaciente," +
																	"ficha.nexoepidemiologico," +
																	"ficha.radiologico," +
																	"ficha.tuberculina," +
																	"ficha.ada," +
																	"ficha.otroDx," +
																	"ficha.bk," +
																	"ficha.valorada," +
																	"ficha.valortuberculina," +
																	"ficha.tipotuberculosis," +
																	"ficha.cicatrizvacuna," +
																	"ficha.fuentecontagio," +
																	"ficha.metodohallazgo," +
																	"ficha.otrometodo," +
																	"ficha.asociacionvih," +
																	"ficha.asesoriavih," +
																	"ficha.observaciones, "+
																	"ficha.realizoCultivo," +
																	"ficha.otroTipoTuberculosis, " +
																	"ficha.fuenteContagio2, " +
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
																	"ficha.fechaNotificacion AS fechaNotificacion, " +
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
																	"per.fecha_nacimiento AS fecha_nacimiento," +
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
																//	"ficha.desplazado AS desplazado " +
																	"pac.grupo_poblacional as grupoPoblacional " +
																/*	
																	"ciu3.nombre as nombreCiudadProcedencia, " +
																	"dep3.nombre as nombreDepProcedencia, " +
																	"ciu4.nombre as nombreCiudadNotifica, " +
																	"dep4.nombre as nombreDepNotifica, " +
																	"per.codigo_ciudad_vivienda as codCiudadResidencia, " +
																	"per.codigo_departamento_vivienda as codDepResidencia," +
																	"unidades.nombre as nombreInstitucion," +
																	"enf.nombre as nombreDiagnostico," +
																	"ficha.acronimo AS acronimo " +
																*/
																"FROM " +
																	"epidemiologia.vigifichatuberculosis ficha," +
																	"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, barrios bar, pacientes pac, ocupaciones ocup," +
																	"convenios conv, tipos_regimen regs " +
																//	"ciudades ciu3, departamentos dep3, ciudades ciu4, departamentos dep4, vigiunidadesprimarias unidades, vigienfnotificables enf, diagnosticos dia " +
																"WHERE " +
																	"ficha.codigoFichaTuberculosis = ? " +
																	
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
																/*	
																	"AND dep3.codigo = ficha.codigoDepProcedencia " +
																	"AND ciu3.codigo_ciudad = ficha.codigoMunProcedencia " +
																	"AND ciu3.codigo_departamento = ficha.codigoDepProcedencia "+
																	"AND dep4.codigo = ficha.codigoDepNoti " +
																	"AND ciu4.codigo_ciudad = ficha.codigoMunNoti " +
																	"AND ciu4.codigo_departamento = ficha.codigoDepNoti "+																	
																	"AND unidades.codigo = ficha.institucionAtendio " +  
																	"AND dia.acronimo = ficha.acronimo " +
																	"AND enf.codigoenfermedadesnotificables = dia.codigoenfermedadesnotificables";
																*/
																	
    
    
    /*
    public static final String consultarDatosAdicionalesReporteStr = "SELECT " +
																	    "ciu.nombre as nombreCiudadProcedencia, " +
																		"dep.nombre as nombreDepProcedencia, " +
																		"per.codigo_ciudad_vivienda as codCiudadResidencia, " +
																		"per.codigo_departamento_vivienda as codDepResidencia," +
																		"unidades.nombre as nombreInstitucion," +
																		"enf.nombre as nombreDiagnostico " +
																	"FROM " +
																		"ciudades ciu, departamentos dep, barrios bar, " +
																		"vigiunidadesprimarias unidades, vigienfnotificables enf, " +
																		"personas per, " +
																		"diagnosticos dia, " +
																		"vigiFichaTuberculosis ficha " +
																	"WHERE " +
																		"ficha.codigoPaciente = per.codigo " +
																	"AND ciu.codigo_ciudad = ficha.codigoMunProcedencia " +
																	"AND dep.codigo = ficha.codigoDepProcedencia " +
																	"AND dep.codigo = ciu.codigo_departamento " +
																	"AND unidades.codigo = ficha.institucionAtendio " +
																	"AND dia.acronimo = ficha.acronimo " +
																	"AND enf.codigoenfermedadesnotificables = dia.codigoenfermedadesnotificables " +
																		
    */
    
    
    
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaSivimDao "+sqle.toString() );
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
										    boolean baciloscopia,
										    boolean cultivo,
										    boolean histopatologia,
										    boolean clinicaPaciente,
										    boolean nexoEpidemiologico,
										    boolean radiologico,
										    boolean tuberculina,
										    boolean ada,
										    boolean otroDx,
										    int resultadoBk,
										    String resultadoAda,
										    String resultadoTuberculina,
										    int tipoTuberculosis,
										    boolean tieneCicatriz,
										    String fuenteContagio,
										    int metodoHallazgo,
										    String otroCual,
										    int asociadoVih,
										    boolean asesoriaVih,
										    String observaciones,
											
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
										    int realizoCultivo,
										    String otroTipoTuberculosis,
										    int fuenteContagio2,
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(baciloscopia),13,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(cultivo),14,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(histopatologia),15,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(clinicaPaciente),16,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(nexoEpidemiologico),17,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(radiologico),18,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(tuberculina),19,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(ada),20,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(otroDx),21,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(resultadoBk),22,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,resultadoAda,23,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,resultadoTuberculina,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoTuberculosis),25,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(tieneCicatriz),26,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fuenteContagio,27,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(metodoHallazgo),28,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,otroCual,29,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(asociadoVih),30,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(asesoriaVih),31,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,observaciones,32,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,33,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,34,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),35,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),36,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,37,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),38,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,39,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),40,Types.INTEGER,true,false);
            
            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),41,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(realizoCultivo),42,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,otroTipoTuberculosis,43,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fuenteContagio2),44,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),46,Types.INTEGER,true,false);
            
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaTuberculosisDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
		    
    
    
    public static int modificarFicha(Connection con,
										String sire,
										String loginUsuario,
									    int codigoFichaTuberculosis,
									    int estado,
									    
									    boolean baciloscopia,
									    boolean cultivo,
									    boolean histopatologia,
									    boolean clinicaPaciente,
									    boolean nexoEpidemiologico,
									    boolean radiologico,
									    boolean tuberculina,
									    boolean ada,
									    boolean otroDx,
									    int resultadoBk,
									    String resultadoAda,
									    String resultadoTuberculina,
									    int tipoTuberculosis,
									    boolean tieneCicatriz,
									    String fuenteContagio,
									    int metodoHallazgo,
									    String otroCual,
									    int asociadoVih,
									    boolean asesoriaVih,
									    String observaciones,
										
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
									    int realizoCultivo,
									    String otroTipoTuberculosis,
									    int fuenteContagio2,
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
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(baciloscopia),4,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(cultivo),5,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(histopatologia),6,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(clinicaPaciente),7,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(nexoEpidemiologico),8,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(radiologico),9,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(tuberculina),10,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(ada),11,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(otroDx),12,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(resultadoBk),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,resultadoAda,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,resultadoTuberculina,15,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoTuberculosis),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(tieneCicatriz),17,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fuenteContagio,18,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(metodoHallazgo),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,otroCual,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(asociadoVih),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(asesoriaVih),22,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,observaciones,23,Types.VARCHAR,true,false);
						
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,24,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,25,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,27,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),29,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,30,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),31,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,32,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,33,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,34,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),35,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(realizoCultivo),36,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,otroTipoTuberculosis,37,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fuenteContagio2),38,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,39,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),40,Types.INTEGER,true,false);
			
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaTuberculosis),41,Types.INTEGER,true,false);
			
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
    
    
    public static ResultSet consultarTodoFichaTuberculosis(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Sivim"+sqle);
			return null;
		}
    }
    
    
    
    
    public static ResultSet consultarDatosPaciente(Connection con, int codigo, boolean empezarnuevo) {
    	
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaTuberculosisDao) "+sqle);
			return null;
    	}
    }
}
