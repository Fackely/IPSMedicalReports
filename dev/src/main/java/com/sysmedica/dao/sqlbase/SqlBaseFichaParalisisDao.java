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

public class SqlBaseFichaParalisisDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaParalisisDao.class);
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichaparalisis "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaParalisis," +
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
    
    
    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichaparalisis " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"nombreMadre=?, " +
														"nombrePadre=?, " +
														"fechaInicioInvestigacion=?, " +
														"numeroDosis=?, " +
														"fechaUltimaDosis=?, " +
														"tieneCarnet=?, " +
														"fiebre=?, " +
														"respiratorios=?, " +
														"digestivos=?, " +
														"instalacion=?, " +
														"dolorMuscular=?, " +
														"signosMeningeos1=?, " +
														"fiebreInicioParalisis=?, " +
														"progresion=?, " +
														"fechaInicioParalisis=?, " +
														"musculosRespiratorios=?, " +
														"signosMeningeos2=?, " +
														"babinsky=?, " +
														"brudzinsky=?, " +
														"paresCraneanos=?, " +
														"liquidoCefalo=?, " +
														"fechaTomaLiquido=?, " +
														"celulas=?, " +
														"globulosRojos=?, " +
														"leucocitos=?, " +
														"proteinas=?, " +
														"glucosa=?, " +
														"electromiografia=?, " +
														"fechaTomaElectro=?, " +
														"velocidadConduccion=?, " +
														"resultadoConduccion=?, " +
														"fechaTomaVelocidad=?, " +
														"impresionDiagnostica=?, " +
														"muestraMateriaFecal=?, " +
														"fechaTomaFecal=?, " +
														"fechaEnvioFecal=?, " +
														"fechaRecepcionFecal=?, " +
														"fechaResultadoFecal=?, " +
														"virusAislado=?, " +
														"fechaVacunacionBloqueo=?, " +
														"fechaCulminacionVacunacion=?, " +
														"municipiosVacunados=?, " +
												
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
													"WHERE codigoFichaParalisis=? ";
    
    private static final String insertarExtremidadStr="INSERT INTO epidemiologia.vigiExtremidad " +
															"(" +
															"codigo," +
															"tipo," +
															"paresia," +
															"paralisis," +
															"flaccida," +
															"localizacion," +
															"sensibilidad," +
															"rot" +
															") " +
														"VALUES (?,?,?,?,?,?,?,?) ";
    
    
    private static final String insertarGrupoEdadStr="INSERT INTO epidemiologia.vigiGrupoEdad " +
    														"(" +
    														"codigo," +
    														"poblacionmeta," +
    														"reciennacido," +
    														"vop1," +
    														"vop2," +
    														"vop3," +
    														"adicional" +
    														") " +
    													"VALUES (?,?,?,?,?,?,?) ";
    
    
    public static final String ingresarExtremidad = "INSERT INTO epidemiologia.vigiextremidad " +
																		"(" +
																		"codigo," +
																		"paresia," +
																		"paralisis," +
																		"flaccida," +
																		"localizacion," +
																		"sensibilidad," +
																		"rot " +
																		") " +
																	"VALUES (?,?,?,?,?,?,?)";
    
    
    public static final String modificarExtremidadStr = "UPDATE epidemiologia.vigiextremidad " +
														"SET " +
															"paresia=?, " +
															"paralisis=?, " +
															"flaccida=?, " +
															"localizacion=?, " +
															"sensibilidad=?, " +
															"rot=? " +
														"WHERE " +
															"codigo = ? ";
    
    
    public static final String consultarExtremidadStr = "SELECT " +
    														"paresia, " +
    														"paralisis, " +
    														"flaccida," +
    														"localizacion," +
    														"sensibilidad," +
    														"rot " +
    													"FROM " +
    														"epidemiologia.vigiExtremidad " +
    													"WHERE " +
    														"codigo=? ";
    
    
    public static final String ingresarGrupoEdad = "INSERT INTO epidemiologia.vigigrupoedad " +
														"(" +
														"codigo " +
														") " +
													"VALUES (?)";
    
    
    public static final String modificarGrupoEdadStr = "UPDATE epidemiologia.vigigrupoedad " +
    													"SET " +
    														"poblacionmeta=?, " +
    														"reciennacido=?, " +
    														"vop1=?, " +
    														"vop2=?, " +
    														"vop3=?, " +
    														"adicional=? " +
    													"WHERE " +
    														"codigo = ? ";
    
    
    public static final String consultarGrupoEdadStr = "SELECT " +
    														"poblacionmeta," +
    														"reciennacido," +
    														"vop1," +
    														"vop2," +
    														"vop3," +
    														"adicional " +
    													"FROM " +
    														"epidemiologia.vigiGrupoEdad " +
    													"WHERE " +
    														"codigo=? ";
    
    
    public static final String consultarFichaParalisis = "SELECT " +
														    "ficha.sire, " +
															"ficha.estado, " +
															"ficha.nombreMadre," +
															"ficha.nombrePadre, " +
															"ficha.fechaInicioInvestigacion, " +
															"ficha.numeroDosis, " +
															"ficha.fechaUltimaDosis, " +
															"ficha.tieneCarnet, " +
															"ficha.fiebre, " +
															"ficha.respiratorios, " +
															"ficha.digestivos, " +
															"ficha.instalacion, " +
															"ficha.dolorMuscular, " +
															"ficha.signosMeningeos1, " +
															"ficha.fiebreInicioParalisis, " +
															"ficha.progresion, " +
															"ficha.fechaInicioParalisis, " +
															"ficha.musculosRespiratorios, " +
															"ficha.signosMeningeos2, " +
															"ficha.babinsky, " +
															"ficha.brudzinsky, " +
															"ficha.paresCraneanos, " +
															"ficha.liquidoCefalo, " +
															"ficha.fechaTomaLiquido, " +
															"ficha.celulas, " +
															"ficha.globulosRojos, " +
															"ficha.leucocitos, " +
															"ficha.proteinas, " +
															"ficha.glucosa, " +
															"ficha.electromiografia, " +
															"ficha.fechaTomaElectro, " +
															"ficha.velocidadConduccion, " +
															"ficha.resultadoConduccion, " +
															"ficha.fechaTomaVelocidad, " +
															"ficha.impresionDiagnostica, " +
															"ficha.muestraMateriaFecal, " +
															"ficha.fechaTomaFecal, " +
															"ficha.fechaEnvioFecal, " +
															"ficha.fechaRecepcionFecal, " +
															"ficha.fechaResultadoFecal, " +
															"ficha.virusAislado, " +
															"ficha.fechaVacunacionBloqueo, " +
															"ficha.fechaCulminacionVacunacion, " +
															"ficha.municipiosVacunados, " +
															"ficha.telefonoContacto, " +
															"ficha.codigoextremidad1, " +
															"ficha.codigoextremidad2, " +
															"ficha.codigoextremidad3, " +
															"ficha.codigoextremidad4, " +
															"ficha.codigogrupo1, " +
															"ficha.codigogrupo2, " +
															"ficha.codigogrupo3, " +
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
															"epidemiologia.vigifichaparalisis ficha," +
															"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
															"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
															"convenios conv, tipos_regimen regs " +
														"WHERE " +
															"ficha.codigoFichaParalisis = ? " +
															
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
																"dep.descripcioin AS dep_vivienda," +
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
    
    
    
    
    
    private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichaparalisis "+
															"(" +
															"loginUsuario,"+
															"codigoFichaParalisis," +
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
															"nombreMadre, " +
															"nombrePadre, " +
															"fechaInicioInvestigacion, " +
															"numeroDosis, " +
															"fechaUltimaDosis, " +
															"tieneCarnet, " +
															"fiebre, " +
															"respiratorios, " +
															"digestivos, " +
															"instalacion, " +
															"dolorMuscular, " +
															"signosMeningeos1, " +
															"fiebreInicioParalisis, " +
															"progresion, " +
															"fechaInicioParalisis, " +
															"musculosRespiratorios, " +
															"signosMeningeos2, " +
															"babinsky, " +
															"brudzinsky, " +
															"paresCraneanos, " +
															"liquidoCefalo, " +
															"fechaTomaLiquido, " +
															"celulas, " +
															"globulosRojos, " +
															"leucocitos, " +
															"proteinas, " +
															"glucosa, " +
															"electromiografia, " +
															"fechaTomaElectro, " +
															"velocidadConduccion, " +
															"resultadoConduccion, " +
															"fechaTomaVelocidad, " +
															"impresionDiagnostica, " +
															"muestraMateriaFecal, " +
															"fechaTomaFecal, " +
															"fechaEnvioFecal, " +
															"fechaRecepcionFecal, " +
															"fechaResultadoFecal, " +
															"virusAislado, " +
															"fechaVacunacionBloqueo, " +
															"fechaCulminacionVacunacion, " +
															"municipiosVacunados, " +
																												
															"fechaConsultaGeneral, " +
															"fechaInicioSintomasGeneral, " +
															"tipoCaso, " +
															"hospitalizadoGeneral, " +
															"fechaHospitalizacionGeneral, " +
															"estaVivoGeneral, " +
															"fechaDefuncion, " +
															"institucionAtendio, " +
															"telefonoContacto," +
															"activa," +
															"codigoextremidad1," +
															"codigoextremidad2," +
															"codigoextremidad3," +
															"codigoextremidad4," +
															"codigogrupo1," +
															"codigogrupo2," +
															"codigogrupo3, " +
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
		int codigoEx;
		int codigoGrupo;
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
			
			PreparedStatementDecorator insertarExtremidad =  new PreparedStatementDecorator(con.prepareStatement(ingresarExtremidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			for (int i=1;i<5;i++) {
			
				int resultadoParcial = 0;
				
				String codigoExt = Integer.toString(codigo) + Integer.toString(i);
            	int codigoExtInt = Integer.parseInt(codigoExt);
				
				insertarExtremidad.setInt(1,codigoExtInt);
				insertarExtremidad.setString(2,"false");
				insertarExtremidad.setString(3,"false");
				insertarExtremidad.setString(4,"false");
				insertarExtremidad.setInt(5,1);
				insertarExtremidad.setInt(6,1);
				insertarExtremidad.setInt(7,1);
				
				
				resultadoParcial = insertarExtremidad.executeUpdate();
				
				if (resultadoParcial<1) {
				
					daoFactory.abortTransaction(con);
					return -1;
				}
			
			}
			
			
			PreparedStatementDecorator insertarGrupo =  new PreparedStatementDecorator(con.prepareStatement(ingresarGrupoEdad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			for (int i=1;i<4;i++) {
			
				int resultadoParcial = 0;
				
				String codigoGr = Integer.toString(codigo) + Integer.toString(i);
            	int codigoGrInt = Integer.parseInt(codigoGr);
            	
				insertarGrupo.setInt(1,codigoGrInt);
				
				resultadoParcial = insertarGrupo.executeUpdate();
				
				if (resultadoParcial<1) {
				
					daoFactory.abortTransaction(con);
					return -1;
				}
			
			}
			
			daoFactory.endTransaction(con);
		}
		
		catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaParalisisDao "+sqle.toString() );
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
										    String nombreMadre,
										    String nombrePadre,
										    String fechaInicioInvestigacion,
										    int numeroDosis,
										    String fechaUltimaDosis,
										    int tieneCarnet,
										    int fiebre,
										    int respiratorios,
										    int digestivos,
										    int instalacion,
										    int dolorMuscular,
										    int signosMeningeos1,
										    int fiebreInicioParalisis,
										    int progresion,
										    String fechaInicioParalisis,
										    int musculosRespiratorios,
										    int signosMeningeos2,
										    int babinsky,
										    int brudzinsky,
										    int paresCraneanos,
										    int liquidoCefalo,
										    String fechaTomaLiquido,
										    int celulas,
										    int globulosRojos,
										    int leucocitos,
										    int proteinas,
										    int glucosa,
										    int electromiografia,
										    String fechaTomaElectro,
										    int velocidadConduccion,
										    int resultadoConduccion,
										    String fechaTomaVelocidad,
										    String impresionDiagnostica,
										    int muestraMateriaFecal,
										    String fechaTomaFecal,
										    String fechaEnvioFecal,
										    String fechaRecepcionFecal,
										    String fechaResultadoFecal,
										    int virusAislado,
										    String fechaVacunacionBloqueo,
										    String fechaCulminacionVacunacion,
										    String municipiosVacunados,
										    int codigoExtremidad1,
										    int codigoExtremidad2,
										    int codigoExtremidad3,
										    int codigoExtremidad4,
										    int codigoGrupoEdad1,
										    int codigoGrupoEdad2,
										    int codigoGrupoEdad3,
										    
										    HashMap datosExtremidades,
										    HashMap datosGrupoEdad,
										    
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
										    String telefonoContacto,
										    boolean activa,
										    String secuenciaExtremidad,
										    String secuenciaGrupo,
										    String pais,
										    int areaProcedencia
										   )
    {
    	int resultado=0;
        int codigo;
        
        int codigoExt1;
        int codigoExt2;
        int codigoExt3;
        int codigoExt4;
        
        int codigoGrup1;
        int codigoGrup2;
        int codigoGrup3;
        
        try {
            
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            
            /////////////////////////////////////////////////////////////
            
            PreparedStatementDecorator ps1 =  new PreparedStatementDecorator(con.prepareStatement(secuenciaExtremidad,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSet rs1 = ps1.executeQuery();
            
            if (rs1.next()) {
                codigoExt1 = rs1.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia de la extremidad 1");
				return 0;
			}
            
            PreparedStatementDecorator insertarExtremidad =  new PreparedStatementDecorator(con.prepareStatement(insertarExtremidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            boolean paresia1 = Boolean.parseBoolean(datosExtremidades.get("paresia_1").toString());
            boolean paralisis1 = Boolean.parseBoolean(datosExtremidades.get("paralisis_1").toString());
            boolean flaccida1 = Boolean.parseBoolean(datosExtremidades.get("flaccida_1").toString());
            int localizacion1 = Integer.parseInt(datosExtremidades.get("localizacion_1").toString());
            int sensibilidad1 = Integer.parseInt(datosExtremidades.get("sensibilidad_1").toString());
            int rot1 = Integer.parseInt(datosExtremidades.get("rot_1").toString());
            
            
            insertarExtremidad.setInt(1,codigoExt1);
            insertarExtremidad.setInt(2,0);
            insertarExtremidad.setBoolean(3,paresia1);
            insertarExtremidad.setBoolean(4,paralisis1);
            insertarExtremidad.setBoolean(5,flaccida1);
            insertarExtremidad.setInt(6,localizacion1);
            insertarExtremidad.setInt(7,sensibilidad1);
            insertarExtremidad.setInt(8,rot1);
			
            resultado = insertarExtremidad.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            
            /////////////////////////////////////////////////////////////
            
            rs1 = ps1.executeQuery();
            
            if (rs1.next()) {
                codigoExt2 = rs1.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia de la extremidad 2");
				return 0;
			}
            
            PreparedStatementDecorator insertarExtremidad2 =  new PreparedStatementDecorator(con.prepareStatement(insertarExtremidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            boolean paresia2 = Boolean.parseBoolean(datosExtremidades.get("paresia_2").toString());
            boolean paralisis2 = Boolean.parseBoolean(datosExtremidades.get("paralisis_2").toString());
            boolean flaccida2 = Boolean.parseBoolean(datosExtremidades.get("flaccida_2").toString());
            int localizacion2 = Integer.parseInt(datosExtremidades.get("localizacion_2").toString());
            int sensibilidad2 = Integer.parseInt(datosExtremidades.get("sensibilidad_2").toString());
            int rot2 = Integer.parseInt(datosExtremidades.get("rot_2").toString());
            
            insertarExtremidad2.setInt(1,codigoExt2);
            insertarExtremidad2.setInt(2,0);
            insertarExtremidad2.setBoolean(3,paresia2);
            insertarExtremidad2.setBoolean(4,paralisis2);
            insertarExtremidad2.setBoolean(5,flaccida2);
            insertarExtremidad2.setInt(6,localizacion2);
            insertarExtremidad2.setInt(7,sensibilidad2);
            insertarExtremidad2.setInt(8,rot2);
			
            resultado = insertarExtremidad2.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            
            /////////////////////////////////////////////////////////////
            
            rs1 = ps1.executeQuery();
            
            if (rs1.next()) {
                codigoExt3 = rs1.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia de la extremidad 3");
				return 0;
			}
            
            PreparedStatementDecorator insertarExtremidad3 =  new PreparedStatementDecorator(con.prepareStatement(insertarExtremidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            boolean paresia3 = Boolean.parseBoolean(datosExtremidades.get("paresia_3").toString());
            boolean paralisis3 = Boolean.parseBoolean(datosExtremidades.get("paralisis_3").toString());
            boolean flaccida3 = Boolean.parseBoolean(datosExtremidades.get("flaccida_3").toString());
            int localizacion3 = Integer.parseInt(datosExtremidades.get("localizacion_3").toString());
            int sensibilidad3 = Integer.parseInt(datosExtremidades.get("sensibilidad_3").toString());
            int rot3 = Integer.parseInt(datosExtremidades.get("rot_3").toString());
            
            insertarExtremidad3.setInt(1,codigoExt3);
            insertarExtremidad3.setInt(2,0);
            insertarExtremidad3.setBoolean(3,paresia3);
            insertarExtremidad3.setBoolean(4,paralisis3);
            insertarExtremidad3.setBoolean(5,flaccida3);
            insertarExtremidad3.setInt(6,localizacion3);
            insertarExtremidad3.setInt(7,sensibilidad3);
            insertarExtremidad3.setInt(8,rot3);
			
            resultado = insertarExtremidad3.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            
            /////////////////////////////////////////////////////////////
            
            rs1 = ps1.executeQuery();
            
            if (rs1.next()) {
                codigoExt4 = rs1.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia de la extremidad 4");
				return 0;
			}
            
            PreparedStatementDecorator insertarExtremidad4 =  new PreparedStatementDecorator(con.prepareStatement(insertarExtremidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            boolean paresia4 = Boolean.parseBoolean(datosExtremidades.get("paresia_4").toString());
            boolean paralisis4 = Boolean.parseBoolean(datosExtremidades.get("paralisis_4").toString());
            boolean flaccida4 = Boolean.parseBoolean(datosExtremidades.get("flaccida_4").toString());
            int localizacion4 = Integer.parseInt(datosExtremidades.get("localizacion_4").toString());
            int sensibilidad4 = Integer.parseInt(datosExtremidades.get("sensibilidad_4").toString());
            int rot4 = Integer.parseInt(datosExtremidades.get("rot_4").toString());
            
            insertarExtremidad4.setInt(1,codigoExt4);
            insertarExtremidad4.setInt(2,0);
            insertarExtremidad4.setBoolean(3,paresia4);
            insertarExtremidad4.setBoolean(4,paralisis4);
            insertarExtremidad4.setBoolean(5,flaccida4);
            insertarExtremidad4.setInt(6,localizacion4);
            insertarExtremidad4.setInt(7,sensibilidad4);
            insertarExtremidad4.setInt(8,rot4);
			
            resultado = insertarExtremidad4.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            
            /////////////////////////////////////////////////////////////
            
            PreparedStatementDecorator ps2 =  new PreparedStatementDecorator(con.prepareStatement(secuenciaGrupo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSet rs2 = ps2.executeQuery();
            
            if (rs2.next()) {
                codigoGrup1 = rs2.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia del grupo de edad 1");
				return 0;
			}
            
            PreparedStatementDecorator insertarGrupoEdad1 =  new PreparedStatementDecorator(con.prepareStatement(insertarGrupoEdadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            int poblacionmeta1 = 0;
            int reciennacido1 = 0;
            int vop1_1 = 0;
            int vop2_1 = 0;
            int vop3_1 = 0;
            int adicional1 = 0;
            	
            try {
	            poblacionmeta1 = Integer.parseInt(datosGrupoEdad.get("poblacionmeta_1").toString());
            }
            catch (NumberFormatException nfe) {
            }
            
            try {
	            reciennacido1 = Integer.parseInt(datosGrupoEdad.get("reciennacido_1").toString());
            }
            catch (NumberFormatException nfe) {
            }
            
            try {
	            vop1_1 = Integer.parseInt(datosGrupoEdad.get("vop1_1").toString());
            }
            catch (NumberFormatException nfe) {
            }
            
            try {
	            vop2_1 = Integer.parseInt(datosGrupoEdad.get("vop2_1").toString());
            }
            catch (NumberFormatException nfe) {
            }
            
            try {
	            vop3_1 = Integer.parseInt(datosGrupoEdad.get("vop3_1").toString());
            }
            catch (NumberFormatException nfe) {
            }
            
            try {
	            adicional1 = Integer.parseInt(datosGrupoEdad.get("adicional_1").toString());
            }
            catch (NumberFormatException nfe) {
            }
            
            insertarGrupoEdad1.setInt(1,codigoGrup1);
            insertarGrupoEdad1.setInt(2,poblacionmeta1);
            insertarGrupoEdad1.setInt(3,reciennacido1);
            insertarGrupoEdad1.setInt(4,vop1_1);
            insertarGrupoEdad1.setInt(5,vop2_1);
            insertarGrupoEdad1.setInt(6,vop3_1);
            insertarGrupoEdad1.setInt(7,adicional1);
            
            resultado = insertarGrupoEdad1.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            /////////////////////////////////////////////////////////////
            
            rs2 = ps2.executeQuery();
            
            if (rs2.next()) {
                codigoGrup2 = rs2.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia del grupo de edad 2");
				return 0;
			}
            
            PreparedStatementDecorator insertarGrupoEdad2 =  new PreparedStatementDecorator(con.prepareStatement(insertarGrupoEdadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            int poblacionmeta2 = 0;
            int vop1_2 = 0;
            int vop2_2 = 0;
            int vop3_2 = 0;
            int adicional2 = 0;
            	
            try {
	            poblacionmeta2 = Integer.parseInt(datosGrupoEdad.get("poblacionmeta_2").toString());
            }
            catch (NumberFormatException nfe) {
            	
            }
            
            try {
	            vop1_2 = Integer.parseInt(datosGrupoEdad.get("vop1_2").toString());
            }
            catch (NumberFormatException nfe) {
            	
            }
            
            try {
	            vop2_2 = Integer.parseInt(datosGrupoEdad.get("vop2_2").toString());
            }
            catch (NumberFormatException nfe) {
            	
            }
            
            try {
	            vop3_2 = Integer.parseInt(datosGrupoEdad.get("vop3_2").toString());
            }
            catch (NumberFormatException nfe) {
            	
            }
            
            try {
	            adicional2 = Integer.parseInt(datosGrupoEdad.get("adicional_2").toString());
            }
            catch (NumberFormatException nfe) {
            	
            }
            
            insertarGrupoEdad2.setInt(1,codigoGrup2);
            insertarGrupoEdad2.setInt(2,poblacionmeta2);
            insertarGrupoEdad2.setInt(3,0);
            insertarGrupoEdad2.setInt(4,vop1_2);
            insertarGrupoEdad2.setInt(5,vop2_2);
            insertarGrupoEdad2.setInt(6,vop3_2);
            insertarGrupoEdad2.setInt(7,adicional2);
            
            resultado = insertarGrupoEdad2.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            /////////////////////////////////////////////////////////////
            
            rs2 = ps2.executeQuery();
            
            if (rs2.next()) {
                codigoGrup3 = rs2.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia del grupo de edad 3");
				return 0;
			}
            
            PreparedStatementDecorator insertarGrupoEdad3 =  new PreparedStatementDecorator(con.prepareStatement(insertarGrupoEdadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            int poblacionmeta3 = 0;
            int vop1_3 = 0;
            int vop2_3 = 0;
            int vop3_3 = 0;
            int adicional3 = 0;
            	
            try {
	            poblacionmeta3 = Integer.parseInt(datosGrupoEdad.get("poblacionmeta_3").toString());
            }
            catch (NumberFormatException nfe) {
            	
            }
            
            try {
	            vop1_3 = Integer.parseInt(datosGrupoEdad.get("vop1_3").toString());
            }
            catch (NumberFormatException nfe) {
            	
            }
            
            try {
	            vop2_3 = Integer.parseInt(datosGrupoEdad.get("vop2_3").toString());
            }
            catch (NumberFormatException nfe) {
            	
            }
            
            try {
	            vop3_3 = Integer.parseInt(datosGrupoEdad.get("vop3_3").toString());
            }
            catch (NumberFormatException nfe) {
            	
            }
            
            try {
	            adicional3 = Integer.parseInt(datosGrupoEdad.get("adicional_3").toString());
            }
            catch (NumberFormatException nfe) {
            	
            }
            
            insertarGrupoEdad3.setInt(1,codigoGrup3);
            insertarGrupoEdad3.setInt(2,poblacionmeta3);
            insertarGrupoEdad3.setInt(3,0);
            insertarGrupoEdad3.setInt(4,vop1_3);
            insertarGrupoEdad3.setInt(5,vop2_3);
            insertarGrupoEdad3.setInt(6,vop3_3);
            insertarGrupoEdad3.setInt(7,adicional3);
            
            resultado = insertarGrupoEdad3.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            /////////////////////////////////////////////////////////////
            
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreMadre,13,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombrePadre,14,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioInvestigacion,15,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroDosis),16,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosis,17,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tieneCarnet),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fiebre),19,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(respiratorios),20,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(digestivos),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(instalacion),22,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dolorMuscular),23,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(signosMeningeos1),24,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fiebreInicioParalisis),25,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(progresion),26,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioParalisis,27,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(musculosRespiratorios),28,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(signosMeningeos2),29,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(babinsky),30,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(brudzinsky),31,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(paresCraneanos),32,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(liquidoCefalo),33,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaTomaLiquido,34,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(celulas),35,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(globulosRojos),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(leucocitos),37,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(proteinas),38,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(glucosa),39,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(electromiografia),40,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaTomaElectro,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(velocidadConduccion),42,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(resultadoConduccion),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaTomaVelocidad,44,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,impresionDiagnostica,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(muestraMateriaFecal),46,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaTomaFecal,47,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaEnvioFecal,48,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaRecepcionFecal,49,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaResultadoFecal,50,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(virusAislado),51,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVacunacionBloqueo,52,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaCulminacionVacunacion,53,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,municipiosVacunados,54,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,55,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,56,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),57,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),58,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,59,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),60,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,61,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),62,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,telefonoContacto,63,Types.VARCHAR,true,false);
            
            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),64,Types.INTEGER,false,true);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoExt1),65,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoExt2),66,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoExt3),67,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoExt4),68,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoGrup1),69,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoGrup2),70,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoGrup3),71,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,72,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),73,Types.INTEGER,true,false);
            
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaDengueDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    
    
    public static int modificarFicha(Connection con,
										String sire,
										String loginUsuario,
									    int codigoFichaParalisis,
									    int estado,
									    
									    String nombreMadre,
									    String nombrePadre,
									    String fechaInicioInvestigacion,
									    int numeroDosis,
									    String fechaUltimaDosis,
									    int tieneCarnet,
									    int fiebre,
									    int respiratorios,
									    int digestivos,
									    int instalacion,
									    int dolorMuscular,
									    int signosMeningeos1,
									    int fiebreInicioParalisis,
									    int progresion,
									    String fechaInicioParalisis,
									    int musculosRespiratorios,
									    int signosMeningeos2,
									    int babinsky,
									    int brudzinsky,
									    int paresCraneanos,
									    int liquidoCefalo,
									    String fechaTomaLiquido,
									    int celulas,
									    int globulosRojos,
									    int leucocitos,
									    int proteinas,
									    int glucosa,
									    int electromiografia,
									    String fechaTomaElectro,
									    int velocidadConduccion,
									    int resultadoConduccion,
									    String fechaTomaVelocidad,
									    String impresionDiagnostica,
									    int muestraMateriaFecal,
									    String fechaTomaFecal,
									    String fechaEnvioFecal,
									    String fechaRecepcionFecal,
									    String fechaResultadoFecal,
									    int virusAislado,
									    String fechaVacunacionBloqueo,
									    String fechaCulminacionVacunacion,
									    String municipiosVacunados,
									    int codigoExtremidad1,
									    int codigoExtremidad2,
									    int codigoExtremidad3,
									    int codigoExtremidad4,
									    int codigoGrupoEdad1,
									    int codigoGrupoEdad2,
									    int codigoGrupoEdad3,
									    
									    HashMap datosExtremidades,
									    HashMap datosGrupoEdad,
									    
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
									    String pais,
									    int areaProcedencia)
    {
    	int result=0;
    	
    	try {
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNoti.split("-")[0];
            String codigoDepNoti = lugarNoti.split("-")[1];
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreMadre,3,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,nombrePadre,4,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioInvestigacion,5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroDosis),6,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosis,7,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tieneCarnet),8,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fiebre),9,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(respiratorios),10,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(digestivos),11,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(instalacion),12,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dolorMuscular),13,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(signosMeningeos1),14,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fiebreInicioParalisis),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(progresion),16,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioParalisis,17,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(musculosRespiratorios),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(signosMeningeos2),19,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(babinsky),20,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(brudzinsky),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(paresCraneanos),22,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(liquidoCefalo),23,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaTomaLiquido,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(celulas),25,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(globulosRojos),26,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(leucocitos),27,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(proteinas),28,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(glucosa),29,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(electromiografia),30,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaTomaElectro,31,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(velocidadConduccion),32,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(resultadoConduccion),33,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaTomaVelocidad,34,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,impresionDiagnostica,35,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(muestraMateriaFecal),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaTomaFecal,37,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaEnvioFecal,38,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaRecepcionFecal,39,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaResultadoFecal,40,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(virusAislado),41,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVacunacionBloqueo,42,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaCulminacionVacunacion,43,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,municipiosVacunados,44,Types.VARCHAR,true,false);
            
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
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,57,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),58,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaParalisis),59,Types.INTEGER,true,false);
		    
            result = modificarFicha.executeUpdate();
            
            if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            
            
            ////////////////////////////////////////////////////////////
            
            PreparedStatementDecorator modificarExtremidad =  new PreparedStatementDecorator(con.prepareStatement(modificarExtremidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("paresia_1").toString(),1,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("paralisis_1").toString(),2,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("flaccida_1").toString(),3,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("localizacion_1").toString(),4,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("sensibilidad_1").toString(),5,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("rot_1").toString(),6,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,Integer.toString(codigoExtremidad1),7,Types.INTEGER,true,false);
    		
    		result = modificarExtremidad.executeUpdate();
    		
    		
    		if (result<1) {
                daoFactory.abortTransaction(con);
                return -1;
            }
    		
    		
    		////////////////////////////////////////////////////////////
    		
    		modificarExtremidad =  new PreparedStatementDecorator(con.prepareStatement(modificarExtremidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("paresia_2").toString(),1,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("paralisis_2").toString(),2,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("flaccida_2").toString(),3,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("localizacion_2").toString(),4,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("sensibilidad_2").toString(),5,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("rot_2").toString(),6,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,Integer.toString(codigoExtremidad2),7,Types.INTEGER,true,false);
    		
    		result = modificarExtremidad.executeUpdate();
    		
    		if (result<1) {
                
                daoFactory.abortTransaction(con);
                return -1;
            }
    		
    		
    		////////////////////////////////////////////////////////////
    		
    		
    		modificarExtremidad =  new PreparedStatementDecorator(con.prepareStatement(modificarExtremidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("paresia_3").toString(),1,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("paralisis_3").toString(),2,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("flaccida_3").toString(),3,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("localizacion_3").toString(),4,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("sensibilidad_3").toString(),5,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("rot_3").toString(),6,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,Integer.toString(codigoExtremidad3),7,Types.INTEGER,true,false);
    		
    		result = modificarExtremidad.executeUpdate();
    		
    		if (result<1) {
                
                daoFactory.abortTransaction(con);
                return -1;
            }
    		
    		////////////////////////////////////////////////////////////
    		
    		
    		modificarExtremidad =  new PreparedStatementDecorator(con.prepareStatement(modificarExtremidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("paresia_4").toString(),1,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("paralisis_4").toString(),2,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("flaccida_4").toString(),3,Types.VARCHAR,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("localizacion_4").toString(),4,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("sensibilidad_4").toString(),5,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,datosExtremidades.get("rot_4").toString(),6,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarExtremidad,Integer.toString(codigoExtremidad4),7,Types.INTEGER,true,false);
    		
    		result = modificarExtremidad.executeUpdate();
    		
    		if (result<1) {
                
                daoFactory.abortTransaction(con);
                return -1;
            }
    		
    		////////////////////////////////////////////////////////////
    		
    		PreparedStatementDecorator modificarGrupo =  new PreparedStatementDecorator(con.prepareStatement(modificarGrupoEdadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("poblacionmeta_1").toString(),1,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("reciennacido_1").toString(),2,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("vop1_1").toString(),3,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("vop2_1").toString(),4,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("vop3_1").toString(),5,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("adicional_1").toString(),6,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,Integer.toString(codigoGrupoEdad1),7,Types.INTEGER,true,false);
    		
    		result = modificarGrupo.executeUpdate();
    		
    		if (result<1) {
                
                daoFactory.abortTransaction(con);
                return -1;
            }
    		
    		
    		////////////////////////////////////////////////////////////
    		
    		modificarGrupo =  new PreparedStatementDecorator(con.prepareStatement(modificarGrupoEdadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("poblacionmeta_2").toString(),1,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,"0",2,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("vop1_2").toString(),3,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("vop2_2").toString(),4,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("vop3_2").toString(),5,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("adicional_2").toString(),6,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,Integer.toString(codigoGrupoEdad2),7,Types.INTEGER,true,false);
    		
    		result = modificarGrupo.executeUpdate();
    		
    		if (result<1) {
                
                daoFactory.abortTransaction(con);
                return -1;
            }
    		////////////////////////////////////////////////////////////
    		
    		modificarGrupo =  new PreparedStatementDecorator(con.prepareStatement(modificarGrupoEdadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("poblacionmeta_3").toString(),1,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,"0",2,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("vop1_3").toString(),3,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("vop2_3").toString(),4,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("vop3_3").toString(),5,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,datosGrupoEdad.get("adicional_3").toString(),6,Types.INTEGER,true,false);
    		UtilidadBD.ingresarDatoAStatement(modificarGrupo,Integer.toString(codigoGrupoEdad3),7,Types.INTEGER,true,false);
    		
    		result = modificarGrupo.executeUpdate();
    		
    		if (result<1) {
                
                daoFactory.abortTransaction(con);
                return -1;
            }
    		////////////////////////////////////////////////////////////
    		
            
            
            daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaParalisisDao "+sqle.toString() );
		    result=0;
        }
    	
    	return result;
    }
    
    
    
    
    public static ResultSet consultarTodoFichaParalisis(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaParalisis,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Paralisis Flacida "+sqle);
			return null;
		}
    }
    
    
    
    
    public static ResultSet consultarDatosExtremidades(Connection con, int codigo)
    {
        
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarExtremidadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los datos de extremidades (ficha de paralisis flacida) "+sqle);
			return null;
        }
    }
    
    
    
    public static ResultSet consultarDatosGrupoEdad(Connection con, int codigo)
    {
        
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarGrupoEdadStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los datos de grupos de edad (ficha de paralisis flacida) "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaTuberculosisDao) "+sqle);
			return null;
    	}
    }
}
