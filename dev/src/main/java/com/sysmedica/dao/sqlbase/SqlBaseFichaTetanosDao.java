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

	public class SqlBaseFichaTetanosDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaTetanosDao.class);
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichatetanos "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaTetanos," +
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
    
    
    private static final String modificarFichaStr="UPDATE epidemiologia.vigifichatetanos " +
														"SET " +
															"sire=?, " +
															"loginUsuario=?, " +
															"estado=?, " +
															"nombreMadre=?, " +
															"edadMadre=?, " +
															"fechaNacimiento=?, " +
															"fechaEgresoHospital=?, " +
															"nacimientoTraumatico=?, " +
															"llantoNacer=?, " +
															"mamabaNormal=?, " +
															"dejoMamar=?, " +
															"fechaDejo=?, " +
															"dificultadRespiratoria=?, " +
															"episodiosApnea=?, " +
															"hipotermia=?, " +
															"hipertermia=?, " +
															"fontAbombada=?, " +
															"rigidezNuca=?, " +
															"trismus=?, " +
															"convulsiones=?, " +
															"espasmos=?, " +
															"contracciones=?, " +
															"opistotonos=?, " +
															"llantoExcesivo=?, " +
															"sepsisUmbilical=?, " +
															"numeroEmbarazos=?, " +
															"asistioControl=?, " +
															"explicacionNoAsistencia=?, " +
															"atendidoPorMedico=?, " +
															"atendidoPorEnfermero=?, " +
															"atendidoPorAuxiliar=?, " +
															"atendidoPorPromotor=?, " +
															"atendidoPorOtro=?, " +
															"quienAtendio=?, " +
															"numeroControlesPrevios=?, " +
															"fechaUltimoControl=?, " +
															"madreVivioMismoLugar=?, " +
															"codigoMunicipioVivienda=?, " +
															"codigoDepVivienda=?, " +
															"antecedenteVacunaAnti=?, " +
															"dosisDpt=?, " +
															"explicacionNoVacuna=?, " +
															"fechaDosisTd1=?, " +
															"fechaDosisTd2=?, " +
															"fechaDosisTd3=?, " +
															"fechaDosisTd4=?, " +
															"lugarParto=?, " +
															"institucionParto=?, " +
															"fechaIngresoParto=?, " +
															"fechaEgresoParto=?, " +
															"quienAtendioParto=?, " +
															"instrumentoCordon=?, " +
															"metodoEsterilizacion=?, " +
															"recibioInformacionMunon=?, " +
															"aplicacionSustanciasMunon=?, " +
															"cualesSustancias=?, " +
															"distanciaMinutos=?, " +
															"fechaInvestigacionCampo=?, " +
															"fechaVacunacion=?, " +
															"dosisTd1AplicadasMef=?, " +
															"dosisTd2AplicadasMef=?, " +
															"dosisTd3AplicadasMef=?, " +
															"dosisTd4AplicadasMef=?, " +
															"dosisTd5AplicadasMef=?, " +
															"dosisTd1AplicadasGest=?, " +
															"dosisTd2AplicadasGest=?, " +
															"dosisTd3AplicadasGest=?, " +
															"dosisTd4AplicadasGest=?, " +
															"dosisTd5AplicadasGest=?, " +
															"coberturaLograda=?, " +
															
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
														"WHERE codigoFichaTetanos = ? ";
															
    
	public static final String consultarFichaTetanosStr = "SELECT " +
																"ficha.sire, " +
																"ficha.loginUsuario, " +
																"ficha.estado, " +
																"ficha.nombreMadre, " +
																"ficha.edadMadre, " +
																"ficha.fechaNacimiento, " +
																"ficha.fechaEgresoHospital, " +
																"ficha.nacimientoTraumatico, " +
																"ficha.llantoNacer, " +
																"ficha.mamabaNormal, " +
																"ficha.dejoMamar, " +
																"ficha.fechaDejo, " +
																"ficha.dificultadRespiratoria, " +
																"ficha.episodiosApnea, " +
																"ficha.hipotermia, " +
																"ficha.hipertermia, " +
																"ficha.fontAbombada, " +
																"ficha.rigidezNuca, " +
																"ficha.trismus, " +
																"ficha.convulsiones, " +
																"ficha.espasmos, " +
																"ficha.contracciones, " +
																"ficha.opistotonos, " +
																"ficha.llantoExcesivo, " +
																"ficha.sepsisUmbilical, " +
																"ficha.numeroEmbarazos, " +
																"ficha.asistioControl, " +
																"ficha.explicacionNoAsistencia, " +
																"ficha.atendidoPorMedico, " +
																"ficha.atendidoPorEnfermero, " +
																"ficha.atendidoPorAuxiliar, " +
																"ficha.atendidoPorPromotor, " +
																"ficha.atendidoPorOtro, " +
																"ficha.quienAtendio, " +
																"ficha.numeroControlesPrevios, " +
																"ficha.fechaUltimoControl, " +
																"ficha.madreVivioMismoLugar, " +
																"ficha.codigoMunicipioVivienda, " +
																"ficha.codigoDepVivienda, " +
																"ficha.antecedenteVacunaAnti, " +
																"ficha.dosisDpt, " +
																"ficha.explicacionNoVacuna, " +
																"ficha.fechaDosisTd1, " +
																"ficha.fechaDosisTd2, " +
																"ficha.fechaDosisTd3, " +
																"ficha.fechaDosisTd4, " +
																"ficha.lugarParto, " +
																"ficha.institucionParto, " +
																"ficha.fechaIngresoParto, " +
																"ficha.fechaEgresoParto, " +
																"ficha.quienAtendioParto, " +
																"ficha.instrumentoCordon, " +
																"ficha.metodoEsterilizacion, " +
																"ficha.recibioInformacionMunon, " +
																"ficha.aplicacionSustanciasMunon, " +
																"ficha.cualesSustancias, " +
																"ficha.distanciaMinutos, " +
																"ficha.fechaInvestigacionCampo, " +
																"ficha.fechaVacunacion, " +
																"ficha.dosisTd1AplicadasMef, " +
																"ficha.dosisTd2AplicadasMef, " +
																"ficha.dosisTd3AplicadasMef, " +
																"ficha.dosisTd4AplicadasMef, " +
																"ficha.dosisTd5AplicadasMef, " +
																"ficha.dosisTd1AplicadasGest, " +
																"ficha.dosisTd2AplicadasGest, " +
																"ficha.dosisTd3AplicadasGest, " +
																"ficha.dosisTd4AplicadasGest, " +
																"ficha.dosisTd5AplicadasGest, " +
																"ficha.coberturaLograda, " +
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
    		    												"per.primer_nombre AS etnia, " +
    		    											//	"ficha.desplazado AS desplazado " +
    		    												"pac.grupo_poblacional as grupoPoblacional " +
    		    												
    		    											"FROM " +
    															"epidemiologia.vigifichatetanos ficha," +
    															"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
    															"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
    															"convenios conv, tipos_regimen regs " +
    														"WHERE " +
    															"ficha.codigoFichaTetanos = ? " +
    															
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
	
	
	
	
	private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichatetanos "+
															"(" +
															"loginUsuario,"+
															"codigoFichaTetanos," +
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
															"edadMadre, " +
															"fechaNacimiento, " +
															"fechaEgresoHospital, " +
															"nacimientoTraumatico, " +
															"llantoNacer, " +
															"mamabaNormal, " +
															"dejoMamar, " +
															"fechaDejo, " +
															"dificultadRespiratoria, " +
															"episodiosApnea, " +
															"hipotermia, " +
															"hipertermia, " +
															"fontAbombada, " +
															"rigidezNuca, " +
															"trismus, " +
															"convulsiones, " +
															"espasmos, " +
															"contracciones, " +
															"opistotonos, " +
															"llantoExcesivo, " +
															"sepsisUmbilical, " +
															"numeroEmbarazos, " +
															"asistioControl, " +
															"explicacionNoAsistencia, " +
															"atendidoPorMedico, " +
															"atendidoPorEnfermero, " +
															"atendidoPorAuxiliar, " +
															"atendidoPorPromotor, " +
															"atendidoPorOtro, " +
															"quienAtendio, " +
															"numeroControlesPrevios, " +
															"fechaUltimoControl, " +
															"madreVivioMismoLugar, " +
															"codigoMunicipioVivienda, " +
															"codigoDepVivienda, " +
															"antecedenteVacunaAnti, " +
															"dosisDpt, " +
															"explicacionNoVacuna, " +
															"fechaDosisTd1, " +
															"fechaDosisTd2, " +
															"fechaDosisTd3, " +
															"fechaDosisTd4, " +
															"lugarParto, " +
															"institucionParto, " +
															"fechaIngresoParto, " +
															"fechaEgresoParto, " +
															"quienAtendioParto, " +
															"instrumentoCordon, " +
															"metodoEsterilizacion, " +
															"recibioInformacionMunon, " +
															"aplicacionSustanciasMunon, " +
															"cualesSustancias, " +
															"distanciaMinutos, " +
															"fechaInvestigacionCampo, " +
															"fechaVacunacion, " +
															"dosisTd1AplicadasMef, " +
															"dosisTd2AplicadasMef, " +
															"dosisTd3AplicadasMef, " +
															"dosisTd4AplicadasMef, " +
															"dosisTd5AplicadasMef, " +
															"dosisTd1AplicadasGest, " +
															"dosisTd2AplicadasGest, " +
															"dosisTd3AplicadasGest, " +
															"dosisTd4AplicadasGest, " +
															"dosisTd5AplicadasGest, " +
															"coberturaLograda, " +
																														
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
														"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?" +
														",?,?,?,?,?,?,?,?,?,?" +
														",?,?,?,?,?,?,?,?,?,?" +
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaTetanosDao "+sqle.toString() );
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
										    int edadMadre,
										    String fechaNacimientoMadre,
										    String fechaEgresoHospital,
										    boolean nacimientoTraumatico,
										    boolean llantoNacer,
										    boolean mamabaNormal,
										    boolean dejoMamar,
										    String fechaDejo,
										    boolean dificultadRespiratoria,
										    boolean episodiosApnea,
										    boolean hipotermia,
										    boolean hipertermia,
										    boolean fontAbombada,
										    boolean rigidezNuca,
										    boolean trismus,
										    boolean convulsiones,
										    boolean espasmos,
										    boolean contracciones,
										    boolean opistotonos,
										    boolean llantoExcesivo,
										    boolean sepsisUmbilical,
										    int numeroEmbarazos,
										    boolean asistioControl,
										    String explicacionNoAsistencia,
										    boolean atendidoPorMedico,
										    boolean atendidoPorEnfermero,
										    boolean atendidoPorAuxiliar,
										    boolean atendidoPorPromotor,
										    boolean atendidoPorOtro,
										    String quienAtendio,
										    int numeroControlesPrevios,
										    String fechaUltimoControl,
										    boolean madreVivioMismoLugar,
										    String codigoMunicipioVivienda,
										    String codigoDepartamentoVivienda,
										    String lugarVivienda,
										    boolean antecedenteVacunaAnti,
										    int dosisDpt,
										    String explicacionNoVacuna,
										    String fechaDosisTd1,
										    String fechaDosisTd2,
										    String fechaDosisTd3,
										    String fechaDosisTd4,
										    int lugarParto,
										    String institucionParto,
										    String fechaIngresoParto,
										    String fechaEgresoParto,
										    int quienAtendioParto,
										    String instrumentoCordon,
										    String metodoEsterilizacion,
										    boolean recibioInformacionMunon,
										    boolean aplicacionSustanciasMunon,
										    String cualesSustancias,
										    int distanciaMinutos,
										    String fechaInvestigacionCampo,
										    String fechaVacunacion,
										    int dosisTd1AplicadasMef,
										    int dosisTd2AplicadasMef,
										    int dosisTd3AplicadasMef,
										    int dosisTd4AplicadasMef,
										    int dosisTd5AplicadasMef,
										    int dosisTd1AplicadasGest,
										    int dosisTd2AplicadasGest,
										    int dosisTd3AplicadasGest,
										    int dosisTd4AplicadasGest,
										    int dosisTd5AplicadasGest,
										    int coberturaLograda,
										    
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
            
            String codigoMunVivienda = lugarVivienda.split("-")[0];
            String codigoDepVivienda = lugarVivienda.split("-")[1];
            
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(edadMadre),14,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaNacimientoMadre,15,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaEgresoHospital,16,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(nacimientoTraumatico),17,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(llantoNacer),18,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(mamabaNormal),19,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(dejoMamar),20,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDejo,21,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(dificultadRespiratoria),22,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(episodiosApnea),23,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hipotermia),24,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hipertermia),25,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(fontAbombada),26,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(rigidezNuca),27,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(trismus),28,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(convulsiones),29,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(espasmos),30,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(contracciones),31,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(opistotonos),32,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(llantoExcesivo),33,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(sepsisUmbilical),34,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroEmbarazos),35,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(asistioControl),36,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,explicacionNoAsistencia,37,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(atendidoPorMedico),38,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(atendidoPorEnfermero),39,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(atendidoPorAuxiliar),40,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(atendidoPorPromotor),41,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(atendidoPorOtro),42,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,quienAtendio,43,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroControlesPrevios),44,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimoControl,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(madreVivioMismoLugar),46,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoMunVivienda,47,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDepVivienda,48,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(antecedenteVacunaAnti),49,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt),50,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,explicacionNoVacuna,51,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDosisTd1,52,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDosisTd2,53,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDosisTd2,54,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDosisTd4,55,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(lugarParto),56,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,institucionParto,57,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaIngresoParto,58,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaEgresoParto,59,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(quienAtendioParto),60,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,instrumentoCordon,61,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,metodoEsterilizacion,62,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(recibioInformacionMunon),63,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(aplicacionSustanciasMunon),64,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,cualesSustancias,65,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(distanciaMinutos),66,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInvestigacionCampo,67,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVacunacion,68,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisTd1AplicadasMef),69,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisTd2AplicadasMef),70,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisTd3AplicadasMef),71,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisTd4AplicadasMef),72,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisTd5AplicadasMef),73,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisTd1AplicadasGest),74,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisTd2AplicadasGest),75,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisTd3AplicadasGest),76,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisTd4AplicadasGest),77,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisTd5AplicadasGest),78,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(coberturaLograda),79,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,80,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,81,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),82,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),83,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,84,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),85,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,86,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),87,Types.INTEGER,true,false);
            
            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),88,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,89,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),90,Types.INTEGER,true,false);
            
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaTetanosDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    
    public static int modificarFicha(Connection con,
										String sire,
										String loginUsuario,
									    int codigoFichaTetanos,
									    int estado,
									    
									    String nombreMadre,
									    int edadMadre,
									    String fechaNacimientoMadre,
									    String fechaEgresoHospital,
									    boolean nacimientoTraumatico,
									    boolean llantoNacer,
									    boolean mamabaNormal,
									    boolean dejoMamar,
									    String fechaDejo,
									    boolean dificultadRespiratoria,
									    boolean episodiosApnea,
									    boolean hipotermia,
									    boolean hipertermia,
									    boolean fontAbombada,
									    boolean rigidezNuca,
									    boolean trismus,
									    boolean convulsiones,
									    boolean espasmos,
									    boolean contracciones,
									    boolean opistotonos,
									    boolean llantoExcesivo,
									    boolean sepsisUmbilical,
									    int numeroEmbarazos,
									    boolean asistioControl,
									    String explicacionNoAsistencia,
									    boolean atendidoPorMedico,
									    boolean atendidoPorEnfermero,
									    boolean atendidoPorAuxiliar,
									    boolean atendidoPorPromotor,
									    boolean atendidoPorOtro,
									    String quienAtendio,
									    int numeroControlesPrevios,
									    String fechaUltimoControl,
									    boolean madreVivioMismoLugar,
									    String codigoMunicipioVivienda,
									    String codigoDepartamentoVivienda,
									    String lugarVivienda,
									    boolean antecedenteVacunaAnti,
									    int dosisDpt,
									    String explicacionNoVacuna,
									    String fechaDosisTd1,
									    String fechaDosisTd2,
									    String fechaDosisTd3,
									    String fechaDosisTd4,
									    int lugarParto,
									    String institucionParto,
									    String fechaIngresoParto,
									    String fechaEgresoParto,
									    int quienAtendioParto,
									    String instrumentoCordon,
									    String metodoEsterilizacion,
									    boolean recibioInformacionMunon,
									    boolean aplicacionSustanciasMunon,
									    String cualesSustancias,
									    int distanciaMinutos,
									    String fechaInvestigacionCampo,
									    String fechaVacunacion,
									    int dosisTd1AplicadasMef,
									    int dosisTd2AplicadasMef,
									    int dosisTd3AplicadasMef,
									    int dosisTd4AplicadasMef,
									    int dosisTd5AplicadasMef,
									    int dosisTd1AplicadasGest,
									    int dosisTd2AplicadasGest,
									    int dosisTd3AplicadasGest,
									    int dosisTd4AplicadasGest,
									    int dosisTd5AplicadasGest,
									    int coberturaLograda,
									    
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
									    int areaProcedencia
									    )
    {
    	int result=0;
    	
    	try {
    		
    		String muniVivienda = lugarVivienda.split("-")[0];
            String depVivienda = lugarVivienda.split("-")[1];
            
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
            UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreMadre,4,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(edadMadre),5,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaNacimientoMadre,6,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaEgresoHospital,7,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(nacimientoTraumatico),8,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(llantoNacer),9,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(mamabaNormal),10,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(dejoMamar),11,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDejo,12,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(dificultadRespiratoria),13,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(episodiosApnea),14,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hipotermia),15,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hipertermia),16,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(fontAbombada),17,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(rigidezNuca),18,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(trismus),19,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(convulsiones),20,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(espasmos),21,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(contracciones),22,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(opistotonos),23,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(llantoExcesivo),24,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(sepsisUmbilical),25,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroEmbarazos),26,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(asistioControl),27,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,explicacionNoAsistencia,28,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(atendidoPorMedico),29,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(atendidoPorEnfermero),30,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(atendidoPorAuxiliar),31,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(atendidoPorPromotor),32,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(atendidoPorOtro),33,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,quienAtendio,34,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroControlesPrevios),35,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimoControl,36,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(madreVivioMismoLugar),37,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,muniVivienda,38,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,depVivienda,39,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(antecedenteVacunaAnti),40,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt),41,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,explicacionNoVacuna,42,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDosisTd1,43,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDosisTd2,44,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDosisTd3,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDosisTd4,46,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarParto),47,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,institucionParto,48,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaIngresoParto,49,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaEgresoParto,50,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(quienAtendioParto),51,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,instrumentoCordon,52,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,metodoEsterilizacion,53,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(recibioInformacionMunon),54,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(aplicacionSustanciasMunon),55,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,cualesSustancias,56,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(distanciaMinutos),57,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInvestigacionCampo,58,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVacunacion,59,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisTd1AplicadasMef),60,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisTd2AplicadasMef),61,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisTd3AplicadasMef),62,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisTd4AplicadasMef),63,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisTd5AplicadasMef),64,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisTd1AplicadasGest),65,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisTd2AplicadasGest),66,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisTd3AplicadasGest),67,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisTd4AplicadasGest),68,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisTd5AplicadasGest),69,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(coberturaLograda),70,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,71,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,72,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,73,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,74,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),75,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),76,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,77,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),78,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,79,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,80,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,81,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),82,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,83,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),84,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaTetanos),85,Types.INTEGER,true,false);
            
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
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaTetanosDao "+sqle.toString() );
		    result=0;
        }
    	
    	return result;
    }
    
    
    
    
    public static ResultSet consultarTodoFichaTetanos(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaTetanosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Tetanos Neonatal "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaTetanosDao) "+sqle);
			return null;
    	}
    }
    
    
    
    
    
}
