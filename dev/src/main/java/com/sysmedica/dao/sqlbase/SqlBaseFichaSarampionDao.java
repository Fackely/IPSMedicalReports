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

public class SqlBaseFichaSarampionDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaSarampionDao.class);
    
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichasarampion "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaSarampion," +
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
    														"nombreinvestigador)"+
    														" VALUES(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",'21','11','21','11',?,?,?)";
    
    
    private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichasarampion "+
														    "(" +
															"loginUsuario,"+
															"codigoFichaSarampion," +
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
															
															"sire," +
															"nombrePadre, " +
	    													"ocupacionPadre, " +
	    													"direccionTrabajoPadre, " +
	    													"fechaVisita1, " +
	    													"fuenteNotificacion, " +
	    													"vacunaSarampion, " +
	    													"numeroDosisSarampion, " +
	    													"fechaUltimaDosisSarampion, " +
	    													"fuenteDatosSarampion, " +
	    													"vacunaRubeola, " +
	    													"numeroDosisRubeola, " +
	    													"fechaUltimaDosisRubeola, " +
	    													"fuenteDatosRubeola, " +
	    													"fechaVisitaDomiciliaria, " +
	    													"fiebre, " +
	    													"fechaInicioFiebre, " +
	    													"tipoErupcion, " +
	    													"fechaInicioErupcion, " +
	    													"duracionErupcion, " +
	    													"tos, " +
	    													"coriza, " +
	    													"conjuntivitis, " +
	    													"adenopatia, " +
	    													"artralgia, " +
	    													"embarazada, " +
	    													"numeroSemanas, " +
	    													"municipioParto, " +
	    													"departamentoParto, " +
	    													"huboContacto, " +
	    													"huboCasoConfirmado, " +
	    													"huboViaje, " +
	    													"municipioViaje, " +
	    													"departamentoViaje, " +
	    													"huboContactoEmbarazada, " +
	    													
															"fechaConsultaGeneral, " +
															"fechaInicioSintomasGeneral, " +
															"tipoCaso, " +
															"hospitalizadoGeneral, " +
															"fechaHospitalizacionGeneral, " +
															"estaVivoGeneral, " +
															"fechaDefuncion, " +
															"institucionAtendio," +
															"activa," +
															"diagnosticoFinal, " +
															"pais, " +
															"areaProcedencia " +
															") " +
														"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    
    
    public static final String modificarFichaStr = "UPDATE epidemiologia.vigiFichaSarampion " +
    													"SET sire=?, " +
    													"estado=?, " +
    													"nombrePadre=?, " +
    													"ocupacionPadre=?, " +
    													"direccionTrabajoPadre=?, " +
    													"fechaVisita1=?, " +
    													"fuenteNotificacion=?, " +
    													"vacunaSarampion=?, " +
    													"numeroDosisSarampion=?, " +
    													"fechaUltimaDosisSarampion=?, " +
    													"fuenteDatosSarampion=?, " +
    													"vacunaRubeola=?, " +
    													"numeroDosisRubeola=?, " +
    													"fechaUltimaDosisRubeola=?, " +
    													"fuenteDatosRubeola=?, " +
    													"fechaVisitaDomiciliaria=?, " +
    													"fiebre=?, " +
    													"fechaInicioFiebre=?, " +
    													"tipoErupcion=?, " +
    													"fechaInicioErupcion=?, " +
    													"duracionErupcion=?, " +
    													"tos=?, " +
    													"coriza=?, " +
    													"conjuntivitis=?, " +
    													"adenopatia=?, " +
    													"artralgia=?, " +
    													"embarazada=?, " +
    													"numeroSemanas=?, " +
    													"municipioParto=?, " +
    													"departamentoParto=?, " +
    													"huboContacto=?, " +
    													"huboCasoConfirmado=?, " +
    													"huboViaje=?, " +
    													"municipioViaje=?, " +
    													"departamentoViaje=?, " +
    													"huboContactoEmbarazada=?, " +
    													"diagnosticoFinal=?, " +
    													
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
														"nombreinvestigador=?, " +
														"telefonoinvestigador=?, " +
														"pais=?, " +
														"areaProcedencia=? " +
    												"WHERE codigoFichaSarampion=?";
    													
    
    
    public static final String consultaTodoFichaSarampionStr = "SELECT " +
    																"ficha.sire," +
    																"ficha.estado," +
    																"ficha.nombrePadre," +
    																"ficha.ocupacionPadre," +
    																"ficha.direccionTrabajoPadre," +
    																"ficha.fechaVisita1," +
    																"ficha.fuenteNotificacion," +
    																"ficha.vacunaSarampion," +
    																"ficha.numeroDosisSarampion," +
    																"ficha.fechaUltimaDosisSarampion," +
    																"ficha.fuenteDatosSarampion," +
    																"ficha.vacunaRubeola," +
    																"ficha.numeroDosisRubeola," +
    																"ficha.fechaUltimaDosisRubeola," +
    																"ficha.fuenteDatosRubeola," +
    																"ficha.fechaVisitaDomiciliaria," +
    																"ficha.fiebre," +
    																"ficha.fechaInicioFiebre," +
    																"ficha.tipoErupcion," +
    																"ficha.fechaInicioErupcion," +
    																"ficha.duracionErupcion," +
    																"ficha.tos," +
    																"ficha.coriza," +
    																"ficha.conjuntivitis," +
    																"ficha.adenopatia," +
    																"ficha.artralgia," +
    																"ficha.embarazada," +
    																"ficha.numeroSemanas," +
    																"ficha.municipioParto," +
    																"ficha.departamentoParto," +
    																"ficha.huboContacto," +
    																"ficha.huboCasoConfirmado," +
    																"ficha.huboViaje," +
    																"ficha.municipioViaje," +
    																"ficha.departamentoViaje," +
    																"ficha.huboContactoEmbarazada," +
    																"ficha.diagnosticoFinal, " +
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
    																"ficha.fechaDiligenciamiento AS fechaDiligenciamiento, " +
    																"ficha.nombreInvestigador AS nombreInvestigador, " +
    																"ficha.telefonoInvestigador AS telefonoInvestigador, "+
    																
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
    			    												
    			    												/*
    			    												"per2.primer_nombre AS primerNombreUsuario, " +
    			    												"per2.segundo_nombre AS segundoNombreUsuario, " +
    			    												"per2.primer_apellido AS primerApellidoUsuario, " +
    			    												"per2.segundo_apellido AS segundoApellidoUsuario, " +
    			    												"per2.numero_identificacion AS identificacionUsuario " +
    			    												*/
    			    												"FROM epidemiologia.vigifichasarampion ficha " +
					    											"INNER JOIN personas per ON(ficha.codigoPaciente=per.codigo) " +
					    											"INNER JOIN departamentos dep ON(dep.codigo_departamento=per.codigo_departamento_nacimiento AND dep.codigo_pais=per.codigo_pais_nacimiento) " +
					    											"INNER JOIN ciudades ciu ON(ciu.codigo_ciudad=per.codigo_ciudad_nacimiento AND ciu.codigo_departamento=per.codigo_departamento_nacimiento AND ciu.codigo_pais=per.codigo_pais_nacimiento)  " +
					    											"INNER JOIN departamentos dep2 ON(dep2.codigo_departamento=per.codigo_departamento_vivienda AND dep2.codigo_pais=per.codigo_pais_vivienda) " +
					    											"INNER JOIN ciudades ciu2 ON(ciu2.codigo_ciudad=per.codigo_ciudad_vivienda AND ciu2.codigo_departamento=per.codigo_departamento_vivienda AND ciu2.codigo_pais=per.codigo_pais_vivienda) " +
					    											"INNER JOIN usuarios usu ON(usu.login=ficha.loginUsuario)  " +
					    											"INNER JOIN personas per2 ON(per2.codigo=usu.codigo_persona)  " +
					    											"INNER JOIN barrios bar ON(bar.codigo=per.codigo_barrio_vivienda) " +
					    											"INNER JOIN pacientes pac ON(pac.codigo_paciente=per.codigo) " +
					    											"INNER JOIN ocupaciones ocup ON(ocup.codigo=pac.ocupacion) " +
																	"INNER JOIN convenios conv ON(conv.codigo=ficha.codigoAseguradora) " +
																	"INNER JOIN tipos_regimen regs ON(conv.tipo_regimen=regs.acronimo) " +
																	"WHERE " +
    																"ficha.codigoFichaSarampion = ? " ;
    
    
    
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
															"conv.codigo AS codigoConvenio, " +
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
    

    
    public static final String ingresarDatoLaboratorio = "INSERT INTO epidemiologia.vigilaboratoriosarampion " +
    															"(" +
    															"codigofichasarampion," +
    															"codigolaboratorio" +
    															") " +
    														"VALUES (?,?)";
    
    
    public static final String modificarDatoLaboratorio = "UPDATE epidemiologia.vigilaboratoriosarampion " +
    															"SET " +
    															"fechatoma=?," +
    															"fecharecepcion=?," +
    															"muestra=?," +
    															"prueba=?," +
    															"agente=?," +
    															"resultado=?," +
    															"fecharesultado=?," +
    															"valor=? " +
    														"WHERE " +
    															"codigolaboratorio=? ";
    
    
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
    												//		"vigiLaboratorioSarampion 
    													  	"epidemiologia.vigifichalaboratorios " +
    													  "WHERE " +
    												//		"codigoFichaSarampion=?" +
    														"codigoFicha=?";
    
    /**
     * String con el statement para insertar una notificacion
     */
    private static final String insertarNotificacionStr = "INSERT INTO epidemiologia.vigiNotificacion "+
    															"("+
    															"codigoNotificacion,"+
    															"login,"+
    															"fecha,"+
    															"hora,"+
    															"tipo"+
    															") "+
    														"VALUES(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?)";
    
    
    
    private static final String terminarFichaStr = "UPDATE epidemiologia.vigiFichaSarampion SET estado = 2 WHERE codigoFichaSarampion=?";
    
    
    
    public static int insertarFichaCompleta(Connection con,
								    		int numeroSolicitud,
											String login,
											int codigoPaciente,
											String codigoDiagnostico,
											int estado,
											int codigoAseguradora,
											String nombreProfesional,
											HashMap datosLaboratorio,
										    String secuencia,
										    
										    String sire,
											boolean notificar,
										    String loginUsuario,
										    
										    String nombrePadre,
										    String ocupacionPadre,
										    String direccionTrabajoPadre,
										    String fechaVisita1,
										    int fuenteNotificacion,
										    int vacunaSarampion,
										    int numeroDosisSarampion,
										    String fechaUltimaDosisSarampion,
										    int fuenteDatosSarampion,
										    int vacunaRubeola,
										    int numeroDosisRubeola,
										    String fechaUltimaDosisRubeola,
										    int fuenteDatosRubeola,
										    
										    String fechaVisitaDomiciliaria,
										    int fiebre,
										    String fechaInicioFiebre,
										    int tipoErupcion,
										    String fechaInicioErupcion,
										    int duracionErupcion,
										    int tos,
										    int coriza,
										    int conjuntivitis,
										    int adenopatia,
										    int artralgia,
										    int embarazada,
										    int numeroSemanas,
										    String lugarParto,
										    
										    int huboContacto,
										    int huboCasoConfirmado,
										    int huboViaje,
										    String lugarViaje,
										    int huboContactoEmbarazada,
										    
										    String lugarProcedencia,
										    String fechaConsultaGeneral,
										    String fechaInicioSintomasGeneral,
										    int tipoCaso,
										    boolean hospitalizadoGeneral,
										    String fechaHospitalizacionGeneral,
										    boolean estaVivoGeneral,
										    String fechaDefuncion,
										    String lugarNoti,
										    String nombreInvestigador,
										    String telefonoInvestigador,
										    int unidadGeneradora,
										    boolean activa,
										    int diagnosticoFinal,
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
            
            String muniParto = lugarParto.split("-")[0];
            String depParto = lugarParto.split("-")[1];
            
            String muniViaje = lugarViaje.split("-")[0];
            String depViaje = lugarViaje.split("-")[1];
            	
            
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombrePadre,13,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,ocupacionPadre,14,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,direccionTrabajoPadre,15,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVisita1,16,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fuenteNotificacion),17,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaSarampion),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroDosisSarampion),19,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosisSarampion,20,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fuenteDatosSarampion),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaRubeola),22,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroDosisRubeola),23,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosisRubeola,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fuenteDatosRubeola),25,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVisitaDomiciliaria,26,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fiebre),27,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioFiebre,28,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoErupcion),29,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioErupcion,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(duracionErupcion),31,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tos),32,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(coriza),33,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(conjuntivitis),34,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(adenopatia),35,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(artralgia),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(embarazada),37,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroSemanas),38,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,muniParto,39,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,depParto,40,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(huboContacto),41,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(huboCasoConfirmado),42,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(huboViaje),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,muniViaje,44,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,depViaje,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(huboContactoEmbarazada),46,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,47,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,48,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),49,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),50,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,51,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),52,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,53,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),54,Types.INTEGER,true,false);
            
            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),55,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(diagnosticoFinal),56,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,57,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),58,Types.INTEGER,true,false);
            
            
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaSarampionDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    
    public static int insertarFicha(Connection con,
									int numeroSolicitud,
									String login,
									int codigoPaciente,
									String codigoDiagnostico,
									int estado,
									int codigoAseguradora,
									String nombreProfesional,
									HashMap datosLaboratorio,
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
            insertarFicha.setString(9,nombreProfesional);
            
            resultado = insertarFicha.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            else {
			    
			    resultado = codigo;
			}
            
            PreparedStatementDecorator insertarDatosLaboratorio =  new PreparedStatementDecorator(con.prepareStatement(ingresarDatoLaboratorio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            for (int i=1;i<5;i++) {
            	
            	int resultadoParcial = 0;
            	
            	String codigoLab = Integer.toString(codigo) + Integer.toString(i);
            	int codigoLabInt = Integer.parseInt(codigoLab);
            	
                insertarDatosLaboratorio.setInt(1,codigo);
                insertarDatosLaboratorio.setInt(2,codigoLabInt);
                
                resultadoParcial = insertarDatosLaboratorio.executeUpdate();
                
	            if (resultadoParcial<1) {
	                
	                daoFactory.abortTransaction(con);
	                return -1;
	            }
	            
            }
            
            daoFactory.endTransaction(con);
        }
        catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaSarampionDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    
    public static int terminarFicha(Connection con, int codigoFichaSarampion)
    {
    	int resultado=0;
    	
    	try {
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            PreparedStatementDecorator terminarFicha =  new PreparedStatementDecorator(con.prepareStatement(terminarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(terminarFicha,Integer.toString(codigoFichaSarampion),1,Types.VARCHAR,true,false);
            
            resultado = terminarFicha.executeUpdate();
		    
		    if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaSarampionDao "+sqle.toString() );
		    resultado=0;
        }
        
        return resultado;
    }
    
    
    
    
    public static int modificarFicha(Connection con,
    									String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaSarampion,
									    int codigoPaciente,
									    String codigoDiagnostico,
									    int codigoNotificacion,
									    int numeroSolicitud,
									    int estado,
									    
									    String nombrePadre,
									    String ocupacionPadre,
									    String direccionTrabajoPadre,
									    String fechaVisita1,
									    int fuenteNotificacion,
									    int vacunaSarampion,
									    int numeroDosisSarampion,
									    String fechaUltimaDosisSarampion,
									    int fuenteDatosSarampion,
									    int vacunaRubeola,
									    int numeroDosisRubeola,
									    String fechaUltimaDosisRubeola,
									    int fuenteDatosRubeola,
									    
									    String fechaVisitaDomiciliaria,
									    int fiebre,
									    String fechaInicioFiebre,
									    int tipoErupcion,
									    String fechaInicioErupcion,
									    int duracionErupcion,
									    int tos,
									    int coriza,
									    int conjuntivitis,
									    int adenopatia,
									    int artralgia,
									    int embarazada,
									    int numeroSemanas,
									    String lugarParto,
									    
									    int huboContacto,
									    int huboCasoConfirmado,
									    int huboViaje,
									    String lugarViaje,
									    int huboContactoEmbarazada,
									    int diagnosticoFinal,
									    
									    String lugarProcedencia,
									    String fechaConsultaGeneral,
									    String fechaInicioSintomasGeneral,
									    int tipoCaso,
									    boolean hospitalizadoGeneral,
									    String fechaHospitalizacionGeneral,
									    boolean estaVivoGeneral,
									    String fechaDefuncion,
									    String lugarNoti,
									    String nombreInvestigador,
									    String telefonoInvestigador,
									    int unidadGeneradora,
									    									    
									    HashMap datosLaboratorio,
									    
									    String secuenciaNotificaciones,
									    String pais,
									    int areaProcedencia
									    )
    {
    	int resultado=0;
        int codigo=0;
        int codigoNot=0;
        
        try {
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            // Insercion de la Notificacion
       /*     if (notificar&&estado!=ConstantesBD.codigoEstadoFichaSeguimiento) {
                
                PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuenciaNotificaciones,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                ResultSet rs = ps.executeQuery();
                
                if (rs.next()) {
                    codigo = rs.getInt(1);
                    codigoNot=codigo;
                }
                else {
    				logger.error("Error obteniendo el código de la secuencia de la notificacion ");
    				return 0;
    			}
                PreparedStatementDecorator insertarNotificacion =  new PreparedStatementDecorator(con.prepareStatement(insertarNotificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
               
                // Inserción de los datos de la notificacion
                
                insertarNotificacion.setInt(1,codigo);
                insertarNotificacion.setString(2,loginUsuario);
                insertarNotificacion.setString(3,Integer.toString(ConstantesBD.codigoTipoNotificacionIndividual));
                
                resultado = insertarNotificacion.executeUpdate();
                
                if(resultado<1)
                {
                    daoFactory.abortTransaction(con);
                    return -1; // Estado de error
                }
            }
            
       */
            
            //********************************************************
            // Insercion de la ficha de sarampion
            
            String muniParto = lugarParto.split("-")[0];
            String depParto = lugarParto.split("-")[1];
            
            String muniViaje = lugarViaje.split("-")[0];
            String depViaje = lugarViaje.split("-")[1];
            
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNoti.split("-")[0];
            String codigoDepNoti = lugarNoti.split("-")[1];
            
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                     
            UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,nombrePadre,3,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,ocupacionPadre,4,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,direccionTrabajoPadre,5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVisita1,6,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fuenteNotificacion),7,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaSarampion),8,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroDosisSarampion),9,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosisSarampion,10,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fuenteDatosSarampion),11,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaRubeola),12,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroDosisRubeola),13,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosisRubeola,14,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fuenteDatosRubeola),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVisitaDomiciliaria,16,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fiebre),17,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioFiebre,18,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoErupcion),19,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioErupcion,20,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(duracionErupcion),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tos),22,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(coriza),23,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(conjuntivitis),24,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(adenopatia),25,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(artralgia),26,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(embarazada),27,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroSemanas),28,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,muniParto,29,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,depParto,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(huboContacto),31,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(huboCasoConfirmado),32,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(huboViaje),33,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,muniViaje,34,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,depViaje,35,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(huboContactoEmbarazada),36,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(diagnosticoFinal),37,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,38,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,39,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,40,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),42,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),43,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,44,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),45,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,46,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,47,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,48,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),49,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreInvestigador,50,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,telefonoInvestigador,51,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,52,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),53,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaSarampion),54,Types.INTEGER,true,false);
		    
		    resultado = modificarFicha.executeUpdate();
		    
		    if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    //*************************************************************
            // modificacion de los datos de laboratorio
		    /*
		    if (datosLaboratorio.size()>0) {
		    	
			    PreparedStatementDecorator modificarDatosLaboratorio =  new PreparedStatementDecorator(con.prepareStatement(modificarDatoLaboratorio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            
	            for (int i=1;i<5;i++) {
	            	
	            	String codigoLaboratorio = Integer.toString(codigoFichaSarampion) + Integer.toString(i);
	            	
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("fechaToma"+i).toString(),1,Types.VARCHAR,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("fechaRecepcion"+i).toString(),2,Types.VARCHAR,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("muestra"+i).toString(),3,Types.INTEGER,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("prueba"+i).toString(),4,Types.INTEGER,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("agente"+i).toString(),5,Types.INTEGER,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("resultado"+i).toString(),6,Types.INTEGER,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("fechaResultado"+i).toString(),7,Types.VARCHAR,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("valor"+i).toString(),8,Types.VARCHAR,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,codigoLaboratorio,9,Types.INTEGER,true,false);
	                
	                resultado = modificarDatosLaboratorio.executeUpdate();
	                if (resultado<1) {
	                    
	                    daoFactory.abortTransaction(con);
	                    return -1;
	                }   
	            }
		    }
		    */
		    
		    daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaSarampionDao "+sqle.toString() );
		    resultado=0;
        }
        
        return resultado;
    }
    
    
    
    public static ResultSet consultarTodoFichaSarampion(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaTodoFichaSarampionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Sarampion "+sqle);
			return null;
		}
    }
    
    
    
    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
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
            
            logger.error("Error consultando los datos de laboratorio (ficha de sarampion) "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaSarampionDao) "+sqle);
			return null;
    	}
    }
}
