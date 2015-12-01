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

public class SqlBaseFichaMeningitisDao {


	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaMeningitisDao.class);
    
    
    private static final String insertarFichaCompletaStr = "INSERT INTO epidemiologia.vigifichameningitis "+
																"(" +
																"loginUsuario,"+
																"codigoFichaMeningitis," +
																"codigoPaciente,"+
																"estado,"+
																"acronimo,"+
																"fechaDiligenciamiento," +
																"horaDiligenciamiento," +
																"codigomunprocedencia," +
																"codigodepprocedencia," +
																"codigomunnoti," +
																"codigodepnoti," +
																"codigoaseguradora," +
																"nombreprofesionaldiligencio, " +
																
																"sire, " +
																"vacunaAntihib, " +
															    "dosis, " +
															    "fechaPrimeraDosis, " +
															    "fechaUltimaDosis, " +
															    "tieneCarne, " +
															    "vacunaAntimenin, " +
															    "dosis2, " +
															    "fechaPrimeraDosis2, " +
															    "fechaUltimaDosis2, " +
															    "tieneCarne2, " +
															    "vacunaAntineumo, " +
															    "dosis3, " +
															    "fechaPrimeraDosis3, " +
															    "fechaUltimaDosis3, " +
															    "tieneCarne3, " +
															    "fiebre, " +
															    "rigidez, " +
															    "irritacion, " +
															    "rash, " +
															    "abombamiento, " +
															    "alteracion, " +
															    "usoAntibioticos, " +
															    "fechaUltimaDosis4, " +
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
																"pais, " +
																"areaProcedencia " +
																") " +
															"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?" +
																	") ";
    
    
    

    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichameningitis " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"vacunaAntihib=?, " +
													    "dosis=?, " +
													    "fechaPrimeraDosis=?, " +
													    "fechaUltimaDosis=?, " +
													    "tieneCarne=?, " +
													    "vacunaAntimenin=?, " +
													    "dosis2=?, " +
													    "fechaPrimeraDosis2=?, " +
													    "fechaUltimaDosis2=?, " +
													    "tieneCarne2=?, " +
													    "vacunaAntineumo=?, " +
													    "dosis3=?, " +
													    "fechaPrimeraDosis3=?, " +
													    "fechaUltimaDosis3=?, " +
													    "tieneCarne3=?, " +
													    "fiebre=?, " +
													    "rigidez=?, " +
													    "irritacion=?, " +
													    "rash=?, " +
													    "abombamiento=?, " +
													    "alteracion=?, " +
													    "usoAntibioticos=?, " +
													    "fechaUltimaDosis4=?, " +
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
														"institucionAtendio=?, " +
														"pais=?, " +
														"areaProcedencia=? " +
													"WHERE codigoFichaMeningitis=? ";
    
    
    
    
    

    private static final String consultarFichaMeningitisStr = "SELECT " +
																	"ficha.sire," +
																	"ficha.estado," +
																	"ficha.vacunaAntihib, " +
																    "ficha.dosis, " +
																    "ficha.fechaPrimeraDosis, " +
																    "ficha.fechaUltimaDosis, " +
																    "ficha.tieneCarne, " +
																    "ficha.vacunaAntimenin, " +
																    "ficha.dosis2, " +
																    "ficha.fechaPrimeraDosis2, " +
																    "ficha.fechaUltimaDosis2, " +
																    "ficha.tieneCarne2, " +
																    "ficha.vacunaAntineumo, " +
																    "ficha.dosis3, " +
																    "ficha.fechaPrimeraDosis3, " +
																    "ficha.fechaUltimaDosis3, " +
																    "ficha.tieneCarne3, " +
																    "ficha.fiebre, " +
																    "ficha.rigidez, " +
																    "ficha.irritacion, " +
																    "ficha.rash, " +
																    "ficha.abombamiento, " +
																    "ficha.alteracion, " +
																    "ficha.usoAntibioticos, " +
																    "ficha.fechaUltimaDosis4, " +
																    "ficha.observaciones, " +
																	"ficha.pais," +
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
																
																"FROM " +
																	"epidemiologia.vigifichameningitis ficha," +
																	"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
																	"convenios conv, tipos_regimen regs " +
																"WHERE " +
																	"ficha.codigoFichaMeningitis = ? " +
																	
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
																	"AND usu.codigo_persona=per2.codigo "+
																	"AND per.codigo_barrio_vivienda=bar.codigo " +
																	"AND per.codigo=pac.codigo_paciente " +
																	"AND pac.codigo_paciente=ficha.codigoPaciente " +
																	"AND pac.ocupacion=ocup.codigo "+
																	"AND conv.codigo=ficha.codigoAseguradora " +
																	"AND conv.tipo_regimen=regs.acronimo ";
    
    
    

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
																"INNER JOIN convenios conv ON (conv.codigo = sc.convenio) " +
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
	
	
	
	
	

    public static final String consultaDatosLaboratorio = "SELECT " +
    														"fechaToma," +
    														"fechaRecepcion," +
    														"muestra," +
    														"prueba," +
    														"agente," +
    														"resultado," +
    														"fechaResultado," +
    														"valor," +
    														"codigofichalaboratorios " +
    													  "FROM " +
    													  	"epidemiologia.vigifichalaboratorios " +
    													  "WHERE " +
    														"codigoFicha=?";
    
	
	

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
											boolean notificar,
											
											int codigoFichaIntoxicacion,										    
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
										    
										    int vacunaAntihib,
										    int dosis,
										    String fechaPrimeraDosis,
										    String fechaUltimaDosis,
										    int tieneCarne,
										    int vacunaAntimenin,
										    int dosis2,
										    String fechaPrimeraDosis2,
										    String fechaUltimaDosis2,
										    int tieneCarne2,
										    int vacunaAntineumo,
										    int dosis3,
										    String fechaPrimeraDosis3,
										    String fechaUltimaDosis3,
										    int tieneCarne3,
										    int fiebre,
										    int rigidez,
										    int irritacion,
										    int rash,
										    int abombamiento,
										    int alteracion,
										    int usoAntibioticos,
										    String fechaUltimaDosis4,
										    String observaciones,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia
										   )
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
            
            PreparedStatementDecorator insertarFicha =  new PreparedStatementDecorator(con.prepareStatement(insertarFichaCompletaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
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
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaAntihib),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosis),14,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaPrimeraDosis,15,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosis,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tieneCarne),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaAntimenin),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosis2),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaPrimeraDosis2,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosis2,21,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tieneCarne2),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaAntineumo),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosis3),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaPrimeraDosis3,25,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosis3,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tieneCarne3),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fiebre),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(rigidez),29,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(irritacion),30,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(rash),31,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(abombamiento),32,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(alteracion),33,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(usoAntibioticos),34,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosis4,35,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,observaciones,36,Types.VARCHAR,true,false);
			
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,37,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,38,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),39,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),40,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),42,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,43,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),44,Types.INTEGER,true,false);

            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),45,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,46,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),47,Types.INTEGER,true,false);
            
            
            result = insertarFicha.executeUpdate();
            
            if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            else {
			    
			    result = codigo;
			}
            
            daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaMeningitisDao "+sqle.toString() );
		    result=0;			
		}
        
        return result;
	}
    
    
	
	
	
	

	public static int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaMeningitis,
									    int codigoPaciente,
									    String codigoDiagnostico,
									    int codigoNotificacion,
									    int numeroSolicitud,
									    int estado,
									    
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

									    int vacunaAntihib,
									    int dosis,
									    String fechaPrimeraDosis,
									    String fechaUltimaDosis,
									    int tieneCarne,
									    int vacunaAntimenin,
									    int dosis2,
									    String fechaPrimeraDosis2,
									    String fechaUltimaDosis2,
									    int tieneCarne2,
									    int vacunaAntineumo,
									    int dosis3,
									    String fechaPrimeraDosis3,
									    String fechaUltimaDosis3,
									    int tieneCarne3,
									    int fiebre,
									    int rigidez,
									    int irritacion,
									    int rash,
									    int abombamiento,
									    int alteracion,
									    int usoAntibioticos,
									    String fechaUltimaDosis4,
									    String observaciones,
									    
										String pais,
									    int areaProcedencia
									    )
	{
		int result=0;
        int codigo=0;
        int codigoNot=0;
        
        try {
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNoti.split("-")[0];
            String codigoDepNoti = lugarNoti.split("-")[1];
            
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),2,Types.INTEGER,true,false);

            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaAntihib),3,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosis),4,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaPrimeraDosis,5,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosis,6,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tieneCarne),7,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaAntimenin),8,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosis2),9,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaPrimeraDosis2,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosis2,11,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tieneCarne2),12,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaAntineumo),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosis3),14,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaPrimeraDosis3,15,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosis3,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tieneCarne3),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fiebre),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(rigidez),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(irritacion),20,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(rash),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(abombamiento),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(alteracion),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(usoAntibioticos),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosis4,25,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,observaciones,26,Types.VARCHAR,true,false);
			    
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,27,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,28,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,29,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),31,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),32,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,33,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),34,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,35,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,36,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,37,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),38,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,39,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),40,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaMeningitis),41,Types.INTEGER,true,false);
            
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
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaMeningitisDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
	}
	
	
	
	
	

	public static ResultSet consultarTodoFichaMeningitis(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaMeningitisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Meningitis "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaMeningitisDao) "+sqle);
			return null;
    	}
    }
	
	
	
	
	
	

	/**
     * Metodo para consultar los laboratorios
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarDatosLaboratorio(Connection con, int codigo)
    {
        
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaDatosLaboratorio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los datos de laboratorio (ficha de Meningitis) "+sqle);
			return null;
        }
    }
}
