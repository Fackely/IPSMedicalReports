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

public class SqlBaseFichaDifteriaDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaDifteriaDao.class);
    
    
    private static final String insertarFichaCompletaStr = "INSERT INTO epidemiologia.vigifichadifteria "+
																"(" +
																"loginUsuario,"+
																"codigoFichaDifteria," +
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
																"nombrePadre, " +
															    "fechaInvestigacion, " +
															    "casoIdentificadoPor, " +
															    "contactoCasoConfirmado, " +
															    "carneVacunacion, " +
															    "dosisAplicadas, " +
															    "tipoVacuna, " +
															    "cualVacuna, " +
															    "fechaUltimaDosis, " +
															    "fiebre, " +
															    "amigdalitis, " +
															    "faringitis, " +
															    "laringitis, " +
															    "membranas, " +
															    "complicaciones, " +
															    "tipoComplicacion, " +
															    "tratAntibiotico, " +
															    "tipoAntibiotico, " +
															    "duracionTratamiento, " +
															    "antitoxina, " +
															    "dosisAntitoxina, " +
															    "fechaAplicacionAntitox, " +
															    "investigacionCampo, " +
															    "fechaOperacionBarrido, " +
															    "numeroContactos, " +
															    "quimioprofilaxis, " +
															    "poblacionGrupo1, " +
															    "poblacionGrupo2, " +
															    "poblacionGrupo3, " +
															    "dosisDpt1Grupo1, " +
															    "dosisDpt1Grupo2, " +
															    "dosisDpt1Grupo3, " +
															    "dosisDpt2Grupo1, " +
															    "dosisDpt2Grupo2, " +
															    "dosisDpt2Grupo3, " +
															    "dosisDpt3Grupo1, " +
															    "dosisDpt3Grupo2, " +
															    "dosisDpt3Grupo3, " +
															    "dosisRef1Grupo1, " +
															    "dosisRef1Grupo2, " +
															    "dosisRef1Grupo3, " +
															    "dosisRef2Grupo1, " +
															    "dosisRef2Grupo2, " +
															    "dosisRef2Grupo3, " +
															    "municipiosVacunados, " +
																
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
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?,?,?,?,?,?,?" +
																	") ";
    
    
    

    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichadifteria " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"nombrePadre=?, " +
													    "fechaInvestigacion=?, " +
													    "casoIdentificadoPor=?, " +
													    "contactoCasoConfirmado=?, " +
													    "carneVacunacion=?, " +
													    "dosisAplicadas=?, " +
													    "tipoVacuna=?, " +
													    "cualVacuna=?, " +
													    "fechaUltimaDosis=?, " +
													    "fiebre=?, " +
													    "amigdalitis=?, " +
													    "faringitis=?, " +
													    "laringitis=?, " +
													    "membranas=?, " +
													    "complicaciones=?, " +
													    "tipoComplicacion=?, " +
													    "tratAntibiotico=?, " +
													    "tipoAntibiotico=?, " +
													    "duracionTratamiento=?, " +
													    "antitoxina=?, " +
													    "dosisAntitoxina=?, " +
													    "fechaAplicacionAntitox=?, " +
													    "investigacionCampo=?, " +
													    "fechaOperacionBarrido=?, " +
													    "numeroContactos=?, " +
													    "quimioprofilaxis=?, " +
													    "poblacionGrupo1=?, " +
													    "poblacionGrupo2=?, " +
													    "poblacionGrupo3=?, " +
													    "dosisDpt1Grupo1=?, " +
													    "dosisDpt1Grupo2=?, " +
													    "dosisDpt1Grupo3=?, " +
													    "dosisDpt2Grupo1=?, " +
													    "dosisDpt2Grupo2=?, " +
													    "dosisDpt2Grupo3=?, " +
													    "dosisDpt3Grupo1=?, " +
													    "dosisDpt3Grupo2=?, " +
													    "dosisDpt3Grupo3=?, " +
													    "dosisRef1Grupo1=?, " +
													    "dosisRef1Grupo2=?, " +
													    "dosisRef1Grupo3=?, " +
													    "dosisRef2Grupo1=?, " +
													    "dosisRef2Grupo2=?, " +
													    "dosisRef2Grupo3=?, " +
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
													"WHERE codigoFichaDifteria=? ";
    
    
    

    private static final String consultarFichaLepraStr = "SELECT " +
																    "ficha.sire," +
																	"ficha.estado," +
																    "ficha.nombrePadre," +
																	"ficha.fechaInvestigacion," +
																	"ficha.casoIdentificadoPor," +
																	"ficha.contactoCasoConfirmado," +
																	"ficha.carneVacunacion," +
																	"ficha.dosisAplicadas," +
																	"ficha.tipoVacuna," +
																	"ficha.cualVacuna," +
																	"ficha.fechaUltimaDosis," +
																	"ficha.fiebre," +
																	"ficha.amigdalitis," +
																	"ficha.faringitis," +
																	"ficha.laringitis," +
																	"ficha.membranas," +
																	"ficha.complicaciones," +
																	"ficha.tipoComplicacion," +
																	"ficha.tratAntibiotico," +
																	"ficha.tipoAntibiotico," +
																	"ficha.duracionTratamiento," +
																	"ficha.antitoxina," +
																	"ficha.dosisAntitoxina," +
																	"ficha.fechaAplicacionAntitox," +
																	"ficha.investigacionCampo," +
																	"ficha.fechaOperacionBarrido," +
																	"ficha.numeroContactos," +
																	"ficha.quimioprofilaxis," +
																	"ficha.poblacionGrupo1," +
																	"ficha.poblacionGrupo2," +
																	"ficha.poblacionGrupo3," +
																	"ficha.dosisDpt1Grupo1," +
																	"ficha.dosisDpt1Grupo2," +
																	"ficha.dosisDpt1Grupo3," +
																	"ficha.dosisDpt2Grupo1," +
																	"ficha.dosisDpt2Grupo2," +
																	"ficha.dosisDpt2Grupo3," +  
																	"ficha.dosisDpt3Grupo1," +
																	"ficha.dosisDpt3Grupo2," +
																	"ficha.dosisDpt3Grupo3," +
																	"ficha.dosisRef1Grupo1," +
																	"ficha.dosisRef1Grupo2," +
																	"ficha.dosisRef1Grupo3," +
																	"ficha.dosisRef2Grupo1," +
																	"ficha.dosisRef2Grupo2," +
																	"ficha.dosisRef2Grupo3," +
																	"ficha.municipiosVacunados," +
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
																	"epidemiologia.vigifichadifteria ficha," +
																	"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
																	"convenios conv, tipos_regimen regs " +
																"WHERE " +
																	"ficha.codigoFichaDifteria = ? " +
																	
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
										    
										    String nombrePadre,
										    String fechaInvestigacion,
										    int casoIdentificadoPor,
										    int contactoCasoConfirmado,
										    int carneVacunacion,
										    int dosisAplicadas,
										    int tipoVacuna,
										    String cualVacuna,
										    String fechaUltimaDosis,
										    int fiebre,
										    int amigdalitis,
										    int faringitis,
										    int laringitis,
										    int membranas,
										    int complicaciones,
										    int tipoComplicacion,
										    int tratAntibiotico,
										    String tipoAntibiotico,
										    String duracionTratamiento,
										    int antitoxina,
										    String dosisAntitoxina,
										    String fechaAplicacionAntitox,
										    int investigacionCampo,
										    String fechaOperacionBarrido,
										    int numeroContactos,
										    int quimioprofilaxis,
										    int poblacionGrupo1,
										    int poblacionGrupo2,
										    int poblacionGrupo3,
										    int dosisDpt1Grupo1,
										    int dosisDpt1Grupo2,
										    int dosisDpt1Grupo3,
										    int dosisDpt2Grupo1,
										    int dosisDpt2Grupo2,
										    int dosisDpt2Grupo3,  
										    int dosisDpt3Grupo1,
										    int dosisDpt3Grupo2,
										    int dosisDpt3Grupo3,
										    int dosisRef1Grupo1,
										    int dosisRef1Grupo2,
										    int dosisRef1Grupo3,
										    int dosisRef2Grupo1,
										    int dosisRef2Grupo2,
										    int dosisRef2Grupo3,
										    String municipiosVacunados,
										    
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
			
			
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombrePadre,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInvestigacion,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(casoIdentificadoPor),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(contactoCasoConfirmado),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(carneVacunacion),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisAplicadas),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoVacuna),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualVacuna,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosis,21,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fiebre),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(amigdalitis),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(faringitis),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(laringitis),25,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(membranas),26,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(complicaciones),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoComplicacion),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tratAntibiotico),29,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,tipoAntibiotico,30,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,duracionTratamiento,31,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(antitoxina),32,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,dosisAntitoxina,33,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaAplicacionAntitox,34,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(investigacionCampo),35,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaOperacionBarrido,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroContactos),37,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(quimioprofilaxis),38,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(poblacionGrupo1),39,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(poblacionGrupo2),40,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(poblacionGrupo3),41,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt1Grupo1),42,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt1Grupo2),43,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt1Grupo3),44,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt2Grupo1),45,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt2Grupo2),46,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt2Grupo3),47,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt3Grupo1),48,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt3Grupo2),49,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisDpt3Grupo3),50,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef1Grupo1),51,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef1Grupo2),52,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef1Grupo3),53,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef2Grupo1),54,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef2Grupo2),55,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisRef2Grupo3),56,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,municipiosVacunados,57,Types.VARCHAR,true,false);
						
			
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,58,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,59,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),60,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),61,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,62,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),63,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,64,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),65,Types.INTEGER,true,false);

            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),66,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,67,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),68,Types.INTEGER,true,false);
            
            
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaDifteriaDao "+sqle.toString() );
		    result=0;			
		}
        
        return result;
	}
	
	
	
	
	

	public static int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaLepra,
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

									    String nombrePadre,
									    String fechaInvestigacion,
									    int casoIdentificadoPor,
									    int contactoCasoConfirmado,
									    int carneVacunacion,
									    int dosisAplicadas,
									    int tipoVacuna,
									    String cualVacuna,
									    String fechaUltimaDosis,
									    int fiebre,
									    int amigdalitis,
									    int faringitis,
									    int laringitis,
									    int membranas,
									    int complicaciones,
									    int tipoComplicacion,
									    int tratAntibiotico,
									    String tipoAntibiotico,
									    String duracionTratamiento,
									    int antitoxina,
									    String dosisAntitoxina,
									    String fechaAplicacionAntitox,
									    int investigacionCampo,
									    String fechaOperacionBarrido,
									    int numeroContactos,
									    int quimioprofilaxis,
									    int poblacionGrupo1,
									    int poblacionGrupo2,
									    int poblacionGrupo3,
									    int dosisDpt1Grupo1,
									    int dosisDpt1Grupo2,
									    int dosisDpt1Grupo3,
									    int dosisDpt2Grupo1,
									    int dosisDpt2Grupo2,
									    int dosisDpt2Grupo3,  
									    int dosisDpt3Grupo1,
									    int dosisDpt3Grupo2,
									    int dosisDpt3Grupo3,
									    int dosisRef1Grupo1,
									    int dosisRef1Grupo2,
									    int dosisRef1Grupo3,
									    int dosisRef2Grupo1,
									    int dosisRef2Grupo2,
									    int dosisRef2Grupo3,
									    String municipiosVacunados,
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

			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombrePadre,3,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInvestigacion,4,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(casoIdentificadoPor),5,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(contactoCasoConfirmado),6,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(carneVacunacion),7,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisAplicadas),8,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoVacuna),9,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualVacuna,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosis,11,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fiebre),12,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(amigdalitis),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(faringitis),14,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(laringitis),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(membranas),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(complicaciones),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoComplicacion),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tratAntibiotico),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,tipoAntibiotico,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,duracionTratamiento,21,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(antitoxina),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,dosisAntitoxina,23,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaAplicacionAntitox,24,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(investigacionCampo),25,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaOperacionBarrido,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroContactos),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(quimioprofilaxis),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(poblacionGrupo1),29,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(poblacionGrupo2),30,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(poblacionGrupo3),31,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt1Grupo1),32,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt1Grupo2),33,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt1Grupo3),34,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt2Grupo1),35,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt2Grupo2),36,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt2Grupo3),37,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt3Grupo1),38,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt3Grupo2),39,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisDpt3Grupo3),40,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef1Grupo1),41,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef1Grupo2),42,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef1Grupo3),43,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef2Grupo1),44,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef2Grupo2),45,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisRef2Grupo3),46,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,municipiosVacunados,47,Types.VARCHAR,true,false);
						
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,48,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,49,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,50,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,51,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),52,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),53,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,54,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),55,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,56,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,57,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,58,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),59,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,60,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),61,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaLepra),62,Types.INTEGER,true,false);
            
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
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaDifteriaDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
	}
	
	
	
	

	public static ResultSet consultarTodoFichaLepra(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaLepraStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Lepra "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaLepraDao) "+sqle);
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
            
            logger.error("Error consultando los datos de laboratorio (ficha de difteria) "+sqle);
			return null;
        }
    }
}
