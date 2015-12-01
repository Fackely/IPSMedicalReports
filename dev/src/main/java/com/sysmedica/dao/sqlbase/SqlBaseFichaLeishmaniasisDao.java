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

public class SqlBaseFichaLeishmaniasisDao {


	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaEasvDao.class);
    
    
    private static final String insertarFichaCompletaStr = "INSERT INTO epidemiologia.vigifichaleishmaniasis "+
																"(" +
																"loginUsuario,"+
																"codigoFichaLeishmaniasis," +
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
																"numeroLesiones, " +
														        "localizacionLesiones, " +
														        "anchoLesion1, " +
														        "largolesion1, " +
														        "anchoLesion2, " +
														        "largolesion2, " +
														        "anchoLesion3, " +
														        "largolesion3, " +
														        "cicatrices, " +
														        "tiempo, " +
														        "unidadTiempo, " +
														        "antecedenteTrauma, " +
														        "mucosaAfectada, " +
														        "rinorrea, " +
														        "epistaxis, " +
														        "obstruccion, " +
														        "disfonia, " +
														        "disfagia, " +
														        "hiperemia, " +
														        "ulceracion, " +
														        "perforacion, " +
														        "destruccion, " +
														        "fiebre, " +
														        "hepatomegalia, " +
														        "esplenomegalia, " +
														        "anemia, " +
														        "leucopenia, " +
														        "trombocitopenia, " +
														        "recibioTratamiento, " +
														        "numeroVeces, " +
														        "medicamentoRecibio, " +
														        "otroMedicamento, " +
														        "pesoPaciente, " +
														        "volumenDiario, " +
														        "diasTratamiento, " +
														        "totalAmpollas, " +
																
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
																"areaProcedencia, " +
																"cara, " +
																"tronco, " +
																"superiores, " +
																"inferiores " +
																") " +
															"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?" +
																	") ";
    
    
    

    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichaLeishmaniasis " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"numeroLesiones=?, " +
												        "localizacionLesiones=?, " +
												        "anchoLesion1=?, " +
												        "largolesion1=?, " +
												        "anchoLesion2=?, " +
												        "largolesion2=?, " +
												        "anchoLesion3=?, " +
												        "largolesion3=?, " +
												        "cicatrices=?, " +
												        "tiempo=?, " +
												        "unidadTiempo=?, " +
												        "antecedenteTrauma=?, " +
												        "mucosaAfectada=?, " +
												        "rinorrea=?, " +
												        "epistaxis=?, " +
												        "obstruccion=?, " +
												        "disfonia=?, " +
												        "disfagia=?, " +
												        "hiperemia=?, " +
												        "ulceracion=?, " +
												        "perforacion=?, " +
												        "destruccion=?, " +
												        "fiebre=?, " +
												        "hepatomegalia=?, " +
												        "esplenomegalia=?, " +
												        "anemia=?, " +
												        "leucopenia=?, " +
												        "trombocitopenia=?, " +
												        "recibioTratamiento=?, " +
												        "numeroVeces=?, " +
												        "medicamentoRecibio=?, " +
												        "otroMedicamento=?, " +
												        "pesoPaciente=?, " +
												        "volumenDiario=?, " +
												        "diasTratamiento=?, " +
												        "totalAmpollas=?, " +
														
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
														"areaProcedencia=?," +
														"cara=?, " +
														"tronco=?, " +
														"superiores=?, " +
														"inferiores=? " +
													"WHERE codigoFichaLeishmaniasis=? ";
    
    
    

    private static final String consultarFichaLeishStr = "SELECT " +
																	"ficha.sire," +
																	"ficha.estado," +
																	"ficha.numeroLesiones, " +
																	"ficha.cara, " +
																	"ficha.tronco, " +
																	"ficha.superiores, " +
																	"ficha.inferiores, " +
															        "ficha.localizacionLesiones, " +
															        "ficha.anchoLesion1, " +
															        "ficha.largolesion1, " +
															        "ficha.anchoLesion2, " +
															        "ficha.largolesion2, " +
															        "ficha.anchoLesion3, " +
															        "ficha.largolesion3, " +
															        "ficha.cicatrices, " +
															        "ficha.tiempo, " +
															        "ficha.unidadTiempo, " +
															        "ficha.antecedenteTrauma, " +
															        "ficha.mucosaAfectada, " +
															        "ficha.rinorrea, " +
															        "ficha.epistaxis, " +
															        "ficha.obstruccion, " +
															        "ficha.disfonia, " +
															        "ficha.disfagia, " +
															        "ficha.hiperemia, " +
															        "ficha.ulceracion, " +
															        "ficha.perforacion, " +
															        "ficha.destruccion, " +
															        "ficha.fiebre, " +
															        "ficha.hepatomegalia, " +
															        "ficha.esplenomegalia, " +
															        "ficha.anemia, " +
															        "ficha.leucopenia, " +
															        "ficha.trombocitopenia, " +
															        "ficha.recibioTratamiento, " +
															        "ficha.numeroVeces, " +
															        "ficha.medicamentoRecibio, " +
															        "ficha.otroMedicamento, " +
															        "ficha.pesoPaciente, " +
															        "ficha.volumenDiario, " +
															        "ficha.diasTratamiento, " +
															        "ficha.totalAmpollas, " +
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
																	"epidemiologia.vigifichaleishmaniasis ficha," +
																	"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
																	"convenios conv, tipos_regimen regs " +
																"WHERE " +
																	"ficha.codigoFichaLeishmaniasis = ? " +
																	
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
	
	
    

	private static final String insertarTamLesionStr = "INSERT INTO epidemiologia.vigiTamLesion (" +
														"codigo," +
														"codigoFicha," +
														"largo," +
														"ancho" +
													") " +
													"VALUES (?,?,?,?)";
	
	
	
	
	private static final String eliminarTamLesionStr = "DELETE FROM epidemiologia.vigiTamLesion WHERE codigo=? ";
	
	
	
	private static final String consultarTamLesionStr = "SELECT " +
														"codigo," +
														"codigoficha," +
														"largo," +
														"ancho" +
													"FROM epidemiologia.vigiTamLesion " +
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
										    
										    String numeroLesiones,
										    int localizacionLesiones,
										    String anchoLesion1,
										    String largoLesion1,
										    String anchoLesion2,
										    String largoLesion2,
										    String anchoLesion3,
										    String largoLesion3,
										    int cicatrices,
										    String tiempo,
										    int unidadTiempo,
										    int antecedenteTrauma,
										    int mucosaAfectada,
										    int rinorrea,
										    int epistaxis,
										    int obstruccion,
										    int disfonia,
										    int disfagia,
										    int hiperemia,
										    int ulceracion,
										    int perforacion,
										    int destruccion,
										    int fiebre,
										    int hepatomegalia,
										    int esplenomegalia,
										    int anemia,
										    int leucopenia,
										    int trombocitopenia,
										    int recibioTratamiento,
										    String numeroVeces,
										    int medicamentoRecibio,
										    String otroMedicamento,
										    String pesoPaciente,
										    String volumenDiario,
										    String diasTratamiento,
										    String totalAmpollas,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia,
										    HashMap lesiones,
										    String secuenciaTam,
										    
										    int cara,
										    int tronco,
										    int superiores,
										    int inferiores
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
			UtilidadBD.ingresarDatoAStatement(insertarFicha,numeroLesiones,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(localizacionLesiones),14,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,anchoLesion1,15,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,largoLesion1,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,anchoLesion2,17,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,largoLesion2,18,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,anchoLesion3,19,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,largoLesion3,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(cicatrices),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,tiempo,22,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadTiempo),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(antecedenteTrauma),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(mucosaAfectada),25,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(rinorrea),26,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(epistaxis),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(obstruccion),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(disfonia),29,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(disfagia),30,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(hiperemia),31,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(ulceracion),32,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(perforacion),33,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(destruccion),34,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fiebre),35,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(hepatomegalia),36,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(esplenomegalia),37,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(anemia),38,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(leucopenia),39,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(trombocitopenia),40,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(recibioTratamiento),41,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,numeroVeces,42,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(medicamentoRecibio),43,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,otroMedicamento,44,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,pesoPaciente,45,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,volumenDiario,46,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,diasTratamiento,47,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,totalAmpollas,48,Types.VARCHAR,true,false);
			
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,49,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,50,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),51,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),52,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,53,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),54,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,55,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),56,Types.INTEGER,true,false);

            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),57,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,58,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),59,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(cara),60,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tronco),61,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(superiores),62,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(inferiores),63,Types.INTEGER,true,false);
            
            
            result = insertarFicha.executeUpdate();
            
            if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            else {
			    
			    result = codigo;
			}
            
            
            int tamLes = lesiones.size()/2;
            
            for (int i=0;i<tamLes;i++) {
		    	
		    	String largo = lesiones.get("largo_"+i).toString();
		    	String ancho = lesiones.get("ancho_"+i).toString();
		    	
		    	int codigoTam;
		    	
		    	PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(secuenciaTam,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            ResultSet rs2 = statement.executeQuery();
	            
	            if (rs2.next()) {
	                codigoTam = rs2.getInt(1);
	            }
	            else {
					logger.error("Error obteniendo el código de la secuencia de lesiones ");
					return 0;
				}
	            
	            PreparedStatementDecorator insertarTamLesion =  new PreparedStatementDecorator(con.prepareStatement(insertarTamLesionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            
	            UtilidadBD.ingresarDatoAStatement(insertarTamLesion,Integer.toString(codigoTam),1,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarTamLesion,Integer.toString(codigo),2,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarTamLesion,largo,3,Types.VARCHAR,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarTamLesion,ancho,4,Types.VARCHAR,true,false);
	            
	            result = insertarTamLesion.executeUpdate();
	            
	            if(result<1)
	            {
	                daoFactory.abortTransaction(con);
	                return -1; // Estado de error
	            }
		    }
            
            daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaLeishmaniasisDao "+sqle.toString() );
		    result=0;			
		}
        
        return result;
	}
	
	
    
	
	

	public static int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaEasv,
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

									    String numeroLesiones,
									    int localizacionLesiones,
									    String anchoLesion1,
									    String largoLesion1,
									    String anchoLesion2,
									    String largoLesion2,
									    String anchoLesion3,
									    String largoLesion3,
									    int cicatrices,
									    String tiempo,
									    int unidadTiempo,
									    int antecedenteTrauma,
									    int mucosaAfectada,
									    int rinorrea,
									    int epistaxis,
									    int obstruccion,
									    int disfonia,
									    int disfagia,
									    int hiperemia,
									    int ulceracion,
									    int perforacion,
									    int destruccion,
									    int fiebre,
									    int hepatomegalia,
									    int esplenomegalia,
									    int anemia,
									    int leucopenia,
									    int trombocitopenia,
									    int recibioTratamiento,
									    String numeroVeces,
									    int medicamentoRecibio,
									    String otroMedicamento,
									    String pesoPaciente,
									    String volumenDiario,
									    String diasTratamiento,
									    String totalAmpollas,
										String pais,
									    int areaProcedencia,
									    HashMap lesiones,
									    String secuenciaTam,
									    
									    int cara,
									    int tronco,
									    int superiores,
									    int inferiores
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

            UtilidadBD.ingresarDatoAStatement(modificarFicha,numeroLesiones,3,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(localizacionLesiones),4,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,anchoLesion1,5,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,largoLesion1,6,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,anchoLesion2,7,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,largoLesion2,8,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,anchoLesion3,9,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,largoLesion3,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(cicatrices),11,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,tiempo,12,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadTiempo),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(antecedenteTrauma),14,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(mucosaAfectada),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(rinorrea),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(epistaxis),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(obstruccion),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(disfonia),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(disfagia),20,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(hiperemia),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(ulceracion),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(perforacion),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(destruccion),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fiebre),25,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(hepatomegalia),26,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(esplenomegalia),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(anemia),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(leucopenia),29,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(trombocitopenia),30,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(recibioTratamiento),31,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,numeroVeces,32,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(medicamentoRecibio),33,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,otroMedicamento,34,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,pesoPaciente,35,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,volumenDiario,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,diasTratamiento,37,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,totalAmpollas,38,Types.VARCHAR,true,false);
						
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,39,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,40,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,42,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),44,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),46,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,47,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,48,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,49,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),50,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,51,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),52,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(cara),53,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tronco),54,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(superiores),55,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(inferiores),56,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaEasv),57,Types.INTEGER,true,false);
            
            result = modificarFicha.executeUpdate();
		    
		    if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    

		    PreparedStatementDecorator eliminarLesiones =  new PreparedStatementDecorator(con.prepareStatement(eliminarTamLesionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarLesiones.setInt(1,codigoFichaEasv);
		    result = eliminarLesiones.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }

		    for (int i=1;i<20;i++) {
		    	
		    	try {
			    	String largo = lesiones.get("largo_"+i).toString();
			    	String ancho = lesiones.get("ancho_"+i).toString();
			    	
			    	int codigoTam;
			    		
			    	
			    	PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(secuenciaTam,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		            ResultSet rs2 = statement.executeQuery();
		            
		            if (rs2.next()) {
		                codigoTam = rs2.getInt(1);
		            }
		            else {
						logger.error("Error obteniendo el código de la secuencia de vacunas ");
						return 0;
					}
		            
		            PreparedStatementDecorator insertarLesion =  new PreparedStatementDecorator(con.prepareStatement(insertarTamLesionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		            
		            UtilidadBD.ingresarDatoAStatement(insertarLesion,Integer.toString(codigoTam),1,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarLesion,Integer.toString(codigo),2,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarLesion,largo,3,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarLesion,ancho,4,Types.INTEGER,true,false);
		                        
		            result = insertarLesion.executeUpdate();
		            
		            if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
			    catch (NullPointerException npe) {}
		    }
		    
		    
		    

		    daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaLeishmaniasisDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
	}
	
	
	
	
	
	
	
	

	public static ResultSet consultarTodoFichaLeishmaniasis(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaLeishStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Leishmaniasis "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaEasvDao) "+sqle);
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
            
            logger.error("Error consultando los datos de laboratorio (ficha de Malaria) "+sqle);
			return null;
        }
    }
    
    
    
    
    
    
}
