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

public class SqlBaseFichaAcciOfidicoDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaAcciOfidicoDao.class);
    
    
    private static final String insertarFichaCompletaStr = "INSERT INTO epidemiologia.vigifichaofidico "+
																"(" +
																"loginUsuario,"+
																"codigoFichaOfidico," +
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
																"fechaAccidente, " +
																"nombreVereda, " +
																"actividadAccidente, " +
																"cualActividad, " +
																"tipoAtencionInicial, " +
																"cualAtencion, " +
																"practicasNoMedicas, " +
																"cualPractica, " +
																"localizacionMordedura, " +
																"huellasColmillos, " +
																"serpienteIdentificada, " +
																"serpienteCapturada, " +
																"generoAgenteAgresor, " +
																"cualAgente, " +
																"nombreAgenteAgresor, " +
																"cualLocal, " +
																"cualComplicacion, " +
																"cualSistemica, " +
																"severidadAccidente, " +
																"empleoSuero, " +
																"diasTranscurridos, " +
																"horasTranscurridas, " +
																"tipoSueroAntiofidico, " +
																"cualSuero, " +
																"dosisSuero, " +
																"horasSuero, " +
																"minutosSuero, " +
																"tratamientoQuirurgico, " +
																"tipoTratamiento, " +
																
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
																	"?,?,?,?" +
																	") ";
    
    

    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichaofidico " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"fechaAccidente=?, " +
														"nombreVereda=?, " +
														"actividadAccidente=?, " +
														"cualActividad=?, " +
														"tipoAtencionInicial=?, " +
														"cualAtencion=?, " +
														"practicasNoMedicas=?, " +
														"cualPractica=?, " +
														"localizacionMordedura=?, " +
														"huellasColmillos=?, " +
														"serpienteIdentificada=?, " +
														"serpienteCapturada=?, " +
														"generoAgenteAgresor=?, " +
														"cualAgente=?, " +
														"nombreAgenteAgresor=?, " +
														"cualLocal=?, " +
														"cualComplicacion=?, " +
														"cualSistemica=?, " +
														"severidadAccidente=?, " +
														"empleoSuero=?, " +
														"diasTranscurridos=?, " +
														"horasTranscurridas=?, " +
														"tipoSueroAntiofidico=?, " +
														"cualSuero=?, " +
														"dosisSuero=?, " +
														"horasSuero=?, " +
														"minutosSuero=?, " +
														"tratamientoQuirurgico=?, " +
														"tipoTratamiento=?, " +
														
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
													"WHERE codigoFichaOfidico=? ";
    
    
    
    

    private static final String consultarFichaAcciOfidicoStr = "SELECT " +
																	"ficha.sire," +
																	"ficha.estado," +
																	"ficha.fechaAccidente, " +
																	"ficha.nombreVereda, " +
																	"ficha.actividadAccidente, " +
																	"ficha.cualActividad, " +
																	"ficha.tipoAtencionInicial, " +
																	"ficha.cualAtencion, " +
																	"ficha.practicasNoMedicas, " +
																	"ficha.cualPractica, " +
																	"ficha.localizacionMordedura, " +
																	"ficha.huellasColmillos, " +
																	"ficha.serpienteIdentificada, " +
																	"ficha.serpienteCapturada, " +
																	"ficha.generoAgenteAgresor, " +
																	"ficha.cualAgente, " +
																	"ficha.nombreAgenteAgresor, " +
																	"ficha.cualLocal, " +
																	"ficha.cualComplicacion, " +
																	"ficha.cualSistemica, " +
																	"ficha.severidadAccidente, " +
																	"ficha.empleoSuero, " +
																	"ficha.diasTranscurridos, " +
																	"ficha.horasTranscurridas, " +
																	"ficha.tipoSueroAntiofidico, " +
																	"ficha.cualSuero, " +
																	"ficha.dosisSuero, " +
																	"ficha.horasSuero, " +
																	"ficha.minutosSuero, " +
																	"ficha.tratamientoQuirurgico, " +
																	"ficha.tipoTratamiento, " +
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
																	"epidemiologia.vigifichaofidico ficha," +
																	"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
																	"convenios conv, tipos_regimen regs " +
																"WHERE " +
																	"ficha.codigoFichaOfidico = ? " +
																	
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
	
	
	
	private static final String eliminarManiLocales = "DELETE from epidemiologia.vigiDetalleManiLocales WHERE codigoFichaOfidico = ?";
	
	private static final String eliminarManiSistemicas = "DELETE from epidemiologia.vigiDetalleManiSistemica WHERE codigoFichaOfidico = ?";
	
	private static final String eliminarCompliLocales = "DELETE from epidemiologia.vigiDetalleCompliLocales WHERE codigoFichaOfidico = ?";
	
	private static final String eliminarCompliSist = "DELETE from epidemiologia.vigiDetalleCompliSist WHERE codigoFichaOfidico = ?";
	
	
	
	
	private static final String insertarManiLocal = "INSERT INTO epidemiologia.vigiDetalleManiLocales(codigomanifestacion,codigofichaofidico) VALUES(?,?)";
	
	private static final String insertarManiSistemica = "INSERT INTO epidemiologia.vigiDetalleManiSistemica(codigomanifestacion,codigofichaofidico) VALUES(?,?)";
	
	private static final String insertarCompliLocal = "INSERT INTO epidemiologia.vigiDetalleCompliLocales(codigocomplicacion,codigofichaofidico) VALUES(?,?)";
	
	private static final String insertarCompliSistemica = "INSERT INTO epidemiologia.vigiDetalleCompliSist(codigocomplicacion,codigofichaofidico) VALUES(?,?)";
	
	
	
	
    public static final String consultaManiLocalStr = "SELECT " +
															"codigoManifestacion " +
														"FROM " +
															"epidemiologia.vigiDetalleManiLocales " +
														"WHERE " +
															"codigoFichaOfidico = ? ";
    
    
    public static final String consultaManiSistemicaStr = "SELECT " +
																"codigoManifestacion " +
															"FROM " +
																"epidemiologia.vigiDetalleManiSistemica " +
															"WHERE " +
																"codigoFichaOfidico = ? ";
    
    
    public static final String consultaCompliLocalStr = "SELECT " +
																"codigoComplicacion " +
															"FROM " +
																"epidemiologia.vigiDetalleCompliLocales " +
															"WHERE " +
																"codigoFichaOfidico = ? ";
    
    
    public static final String consultaCompliSistemicaStr = "SELECT " +
																"codigoComplicacion " +
															"FROM " +
																"epidemiologia.vigiDetalleCompliSist " +
															"WHERE " +
																"codigoFichaOfidico = ? ";
    
    
	
	
	

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
										    
										    String fechaAccidente,
											String nombreVereda,
											int actividadAccidente,
											String cualActividad,
											int tipoAtencionInicial,
											String cualAtencion,
											int practicasNoMedicas,
											String cualPractica,
											int localizacionMordedura,
											int huellasColmillos,
											int serpienteIdentificada,
											int serpienteCapturada,
											int generoAgenteAgresor,
											String cualAgente,
											int nombreAgenteAgresor,
											String cualLocal,
											String cualComplicacion,
											String cualSistemica,
											int severidadAccidente,
											int empleoSuero,
											int diasTranscurridos,
											int horasTranscurridas,
											int tipoSueroAntiofidico,
											String cualSuero,
											int dosisSuero,
											int horasSuero,
											int minutosSuero,
											int tratamientoQuirurgico,
											int tipoTratamiento,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia,
										    
										    HashMap manifestacionesLocales,
										    HashMap manifestacionesSistemicas,
										    HashMap complicacionesLocales,
										    HashMap complicacionesSistemicas
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
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaAccidente,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreVereda,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(actividadAccidente),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualActividad,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoAtencionInicial),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualAtencion,18,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(practicasNoMedicas),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualPractica,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(localizacionMordedura),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(huellasColmillos),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(serpienteIdentificada),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(serpienteCapturada),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(generoAgenteAgresor),25,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualAgente,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(nombreAgenteAgresor),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualLocal,28,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualComplicacion,29,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualSistemica,30,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(severidadAccidente),31,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(empleoSuero),32,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(diasTranscurridos),33,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(horasTranscurridas),34,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoSueroAntiofidico),35,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualSuero,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisSuero),37,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(horasSuero),38,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(minutosSuero),39,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tratamientoQuirurgico),40,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoTratamiento),41,Types.INTEGER,true,false);
						
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,42,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,43,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),44,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),45,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,46,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),47,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,48,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),49,Types.INTEGER,true,false);

            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),50,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,51,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),52,Types.INTEGER,true,false);
            
            
            result = insertarFicha.executeUpdate();
            
            if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            else {
			    
			    result = codigo;
			}
            
            
            
            for (int i=1;i<manifestacionesLocales.size()+1;i++) {
		    	
		    	String val = manifestacionesLocales.get("manilocal_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarManifestaciones =  new PreparedStatementDecorator(con.prepareStatement(insertarManiLocal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarManifestaciones.setInt(1,i);
			    	insertarManifestaciones.setInt(2,codigo);
			    	
			    	result = insertarManifestaciones.executeUpdate();
			    	
			    	if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    }
            
            
            
            for (int i=1;i<manifestacionesSistemicas.size()+1;i++) {
		    	
		    	String val = manifestacionesSistemicas.get("manisistemica_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarManifestaciones =  new PreparedStatementDecorator(con.prepareStatement(insertarManiSistemica,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarManifestaciones.setInt(1,i);
			    	insertarManifestaciones.setInt(2,codigo);
			    	
			    	result = insertarManifestaciones.executeUpdate();
			    	
			    	if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    }
            
            
            
            
            for (int i=1;i<complicacionesLocales.size()+1;i++) {
		    	
		    	String val = complicacionesLocales.get("complilocal_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarComplicaciones =  new PreparedStatementDecorator(con.prepareStatement(insertarCompliLocal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarComplicaciones.setInt(1,i);
			    	insertarComplicaciones.setInt(2,codigo);
			    	
			    	result = insertarComplicaciones.executeUpdate();
			    	
			    	if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    }
            
            
            

            for (int i=1;i<complicacionesSistemicas.size()+1;i++) {
		    	
		    	String val = complicacionesSistemicas.get("complisistemica_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarComplicaciones =  new PreparedStatementDecorator(con.prepareStatement(insertarCompliSistemica,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarComplicaciones.setInt(1,i);
			    	insertarComplicaciones.setInt(2,codigo);
			    	
			    	result = insertarComplicaciones.executeUpdate();
			    	
			    	if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    }
            
            
            daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaAcciOfidicoDao "+sqle.toString() );
		    result=0;			
		}
        
        return result;
	}
	
	
	
	
	
	public static int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaAcciOfidico,
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
									    
									    String fechaAccidente,
										String nombreVereda,
										int actividadAccidente,
										String cualActividad,
										int tipoAtencionInicial,
										String cualAtencion,
										int practicasNoMedicas,
										String cualPractica,
										int localizacionMordedura,
										int huellasColmillos,
										int serpienteIdentificada,
										int serpienteCapturada,
										int generoAgenteAgresor,
										String cualAgente,
										int nombreAgenteAgresor,
										String cualLocal,
										String cualComplicacion,
										String cualSistemica,
										int severidadAccidente,
										int empleoSuero,
										int diasTranscurridos,
										int horasTranscurridas,
										int tipoSueroAntiofidico,
										String cualSuero,
										int dosisSuero,
										int horasSuero,
										int minutosSuero,
										int tratamientoQuirurgico,
										int tipoTratamiento,
									    String pais,
									    int areaProcedencia,
									    
									    HashMap manifestacionesLocales,
									    HashMap manifestacionesSistemicas,
									    HashMap complicacionesLocales,
									    HashMap complicacionesSistemicas
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
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaAccidente,3,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreVereda,4,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(actividadAccidente),5,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualActividad,6,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoAtencionInicial),7,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualAtencion,8,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(practicasNoMedicas),9,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualPractica,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(localizacionMordedura),11,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(huellasColmillos),12,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(serpienteIdentificada),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(serpienteCapturada),14,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(generoAgenteAgresor),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualAgente,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(nombreAgenteAgresor),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualLocal,18,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualComplicacion,19,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualSistemica,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(severidadAccidente),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(empleoSuero),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(diasTranscurridos),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(horasTranscurridas),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoSueroAntiofidico),25,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualSuero,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisSuero),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(horasSuero),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(minutosSuero),29,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tratamientoQuirurgico),30,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoTratamiento),31,Types.INTEGER,true,false);
			
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,33,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,34,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,35,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),37,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,38,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),39,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,40,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,42,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,44,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),45,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaAcciOfidico),46,Types.INTEGER,true,false);
            
            result = modificarFicha.executeUpdate();
		    
		    if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    PreparedStatementDecorator eliminarManifestaciones =  new PreparedStatementDecorator(con.prepareStatement(eliminarManiLocales,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
			eliminarManifestaciones.setInt(1,codigoFichaAcciOfidico);
		    result = eliminarManifestaciones.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    for (int i=1;i<10;i++) {
		    	
		    	try {
			    	String val = manifestacionesLocales.get("manilocal_"+i).toString();
	
			    	if (val.equals("true")) {
				    	PreparedStatementDecorator insertarManifestaciones =  new PreparedStatementDecorator(con.prepareStatement(insertarManiLocal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarManifestaciones.setInt(1,i);
				    	insertarManifestaciones.setInt(2,codigoFichaAcciOfidico);
				    	
				    	result = insertarManifestaciones.executeUpdate();
				    	
				    	if(result<1)
			            {
			                daoFactory.abortTransaction(con);
			                return -1; // Estado de error
			            }
			    	}
		    	}
			    catch (NullPointerException npe) {}
		    }
	    
		    
		    
		    
		    

		    PreparedStatementDecorator eliminarManifestacionesSist =  new PreparedStatementDecorator(con.prepareStatement(eliminarManiSistemicas,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
			eliminarManifestacionesSist.setInt(1,codigoFichaAcciOfidico);
		    result = eliminarManifestacionesSist.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    for (int i=1;i<15;i++) {
		    	
		    	try {
			    	String val = manifestacionesSistemicas.get("manisistemica_"+i).toString();
	
			    	if (val.equals("true")) {
				    	PreparedStatementDecorator insertarManifestaciones =  new PreparedStatementDecorator(con.prepareStatement(insertarManiSistemica,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarManifestaciones.setInt(1,i);
				    	insertarManifestaciones.setInt(2,codigoFichaAcciOfidico);
				    	
				    	result = insertarManifestaciones.executeUpdate();
				    	
				    	if(result<1)
			            {
			                daoFactory.abortTransaction(con);
			                return -1; // Estado de error
			            }
			    	}
		    	}
			    catch (NullPointerException npe) {}
		    }
		    
		    
		    
		    
		    
		    PreparedStatementDecorator eliminarComplicaciones =  new PreparedStatementDecorator(con.prepareStatement(eliminarCompliLocales,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarComplicaciones.setInt(1,codigoFichaAcciOfidico);
		    result = eliminarComplicaciones.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    for (int i=1;i<10;i++) {
		    	
		    	try {
			    	String val = complicacionesLocales.get("complilocal_"+i).toString();
	
			    	if (val.equals("true")) {
				    	PreparedStatementDecorator insertarComplicaciones =  new PreparedStatementDecorator(con.prepareStatement(insertarCompliLocal,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarComplicaciones.setInt(1,i);
				    	insertarComplicaciones.setInt(2,codigoFichaAcciOfidico);
				    	
				    	result = insertarComplicaciones.executeUpdate();
				    	
				    	if(result<1)
			            {
			                daoFactory.abortTransaction(con);
			                return -1; // Estado de error
			            }
			    	}
		    	}
			    catch (NullPointerException npe) {}
		    }
		    
		    
		    
		    

		    PreparedStatementDecorator eliminarComplicacionesSist =  new PreparedStatementDecorator(con.prepareStatement(eliminarCompliSist,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarComplicacionesSist.setInt(1,codigoFichaAcciOfidico);
		    result = eliminarComplicacionesSist.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    for (int i=1;i<10;i++) {
		    	
		    	try {
			    	String val = complicacionesSistemicas.get("complisistemica_"+i).toString();
	
			    	if (val.equals("true")) {
				    	PreparedStatementDecorator insertarComplicaciones =  new PreparedStatementDecorator(con.prepareStatement(insertarCompliSistemica,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarComplicaciones.setInt(1,i);
				    	insertarComplicaciones.setInt(2,codigoFichaAcciOfidico);
				    	
				    	result = insertarComplicaciones.executeUpdate();
				    	
				    	if(result<1)
			            {
			                daoFactory.abortTransaction(con);
			                return -1; // Estado de error
			            }
			    	}
		    	}
			    catch (NullPointerException npe) {}
		    }
	    
	    
		    
		    
		    daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaAcciOfidicoDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
	}
	
	
	

	public static ResultSet consultarTodoFichaAcciOfidico(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaAcciOfidicoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaAcciOfidicoDao) "+sqle);
			return null;
    	}
    }
	
	
	
	
	
    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarManifestacionLocal(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaManiLocalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando las manifestaciones locales (ficha de Accidente Ofídico) "+sqle);
			return null;
        }
    }
    
    
    
    
    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarManifestacionSistemica(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaManiSistemicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando las manifestaciones sistemicas (ficha de Accidente Ofídico) "+sqle);
			return null;
        }
    }
    
    
    
    
    
    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarComplicacionLocal(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCompliLocalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando las complicaciones locales (ficha de Accidente Ofídico) "+sqle);
			return null;
        }
    }
    
    
    
    

    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarComplicacionSistemica(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaCompliSistemicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando las complicaciones sistemicas (ficha de Accidente Ofídico) "+sqle);
			return null;
        }
    }
    
    
}
