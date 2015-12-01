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

public class SqlBaseFichaEtasDao {


	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaEtasDao.class);
    
    
    private static final String insertarFichaCompletaStr = "INSERT INTO epidemiologia.vigifichaetas "+
																"(" +
																"loginUsuario,"+
																"codigoFichaEtas," +
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
																"otroSintoma, " +
															    "horaInicioSintomas, " +
															    "minutoInicioSintomas, " +
															    "nombreAlimento1, " +
															    "nombreAlimento2, " +
															    "nombreAlimento3, " +
															    "nombreAlimento4, " +
															    "nombreAlimento5, " +
															    "nombreAlimento6, " +
															    "nombreAlimento7, " +
															    "nombreAlimento8, " +
															    "nombreAlimento9, " +
															    "lugarConsumo1, " +
															    "lugarConsumo2, " +
															    "lugarConsumo3, " +
															    "lugarConsumo4, " +
															    "lugarConsumo5, " +
															    "lugarConsumo6, " +
															    "lugarConsumo7, " +
															    "lugarConsumo8, " +
															    "lugarConsumo9, " +
															    "horaConsumo1, " +
															    "horaConsumo2, " +
															    "horaConsumo3, " +
															    "horaConsumo4, " +
															    "horaConsumo5, " +
															    "horaConsumo6, " +
															    "horaConsumo7, " +
															    "horaConsumo8, " +
															    "horaConsumo9, " +
															    "minutoConsumo1, " +
															    "minutoConsumo2, " +
															    "minutoConsumo3, " +
															    "minutoConsumo4, " +
															    "minutoConsumo5, " +
															    "minutoConsumo6, " +
															    "minutoConsumo7, " +
															    "minutoConsumo8, " +
															    "minutoConsumo9, " +
															    "asociadoBrote, " +
															    "captadoPor, " +
															    "relacionExposicion, " +
															    "tomoMuestra, " +
															    "tipoMuestra, " +
															    "cualMuestra, " +
															    "agente1, " +
															    "agente2, " +
															    "agente3, " +
															    "agente4, " +
																
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
																	"?,?,?,?,?,?,?,?,?,?," +
																	"?,?,?,?" +
																	") ";
    
    
    

    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichaEtas " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"otroSintoma=?, " +
													    "horaInicioSintomas=?, " +
													    "minutoInicioSintomas=?, " +
													    "nombreAlimento1=?, " +
													    "nombreAlimento2=?, " +
													    "nombreAlimento3=?, " +
													    "nombreAlimento4=?, " +
													    "nombreAlimento5=?, " +
													    "nombreAlimento6=?, " +
													    "nombreAlimento7=?, " +
													    "nombreAlimento8=?, " +
													    "nombreAlimento9=?, " +
													    "lugarConsumo1=?, " +
													    "lugarConsumo2=?, " +
													    "lugarConsumo3=?, " +
													    "lugarConsumo4=?, " +
													    "lugarConsumo5=?, " +
													    "lugarConsumo6=?, " +
													    "lugarConsumo7=?, " +
													    "lugarConsumo8=?, " +
													    "lugarConsumo9=?, " +
													    "horaConsumo1=?, " +
													    "horaConsumo2=?, " +
													    "horaConsumo3=?, " +
													    "horaConsumo4=?, " +
													    "horaConsumo5=?, " +
													    "horaConsumo6=?, " +
													    "horaConsumo7=?, " +
													    "horaConsumo8=?, " +
													    "horaConsumo9=?, " +
													    "minutoConsumo1=?, " +
													    "minutoConsumo2=?, " +
													    "minutoConsumo3=?, " +
													    "minutoConsumo4=?, " +
													    "minutoConsumo5=?, " +
													    "minutoConsumo6=?, " +
													    "minutoConsumo7=?, " +
													    "minutoConsumo8=?, " +
													    "minutoConsumo9=?, " +
													    "asociadoBrote=?, " +
													    "captadoPor=?, " +
													    "relacionExposicion=?, " +
													    "tomoMuestra=?, " +
													    "tipoMuestra=?, " +
													    "cualMuestra=?, " +
													    "agente1=?, " +
													    "agente2=?, " +
													    "agente3=?, " +
													    "agente4=?, " +
														
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
													"WHERE codigoFichaEtas=? ";
    
    
    
    private static final String eliminarSintomasStr = "DELETE from epidemiologia.vigiDetalleSintomasEtas WHERE codigoFichaEtas = ?";
    
    
    private static final String insertarSintomaStr = "INSERT INTO epidemiologia.vigiDetalleSintomasEtas(codigo,codigofichaetas) VALUES(?,?)";
    
    

    public static final String consultaSintomasStr = "SELECT " +
															"codigo " +
														"FROM " +
															"epidemiologia.vigiDetalleSintomasEtas " +
														"WHERE " +
															"codigoFichaEtas = ? ";
    
    
    
    

    private static final String consultarFichaEtasStr = "SELECT " +
																	"ficha.sire," +
																	"ficha.estado," +
																	"ficha.otroSintoma, " +
																    "ficha.horaInicioSintomas, " +
																    "ficha.asociadoBrote, " +
																    "ficha.captadoPor, " +
																    "ficha.relacionExposicion, " +
																    "ficha.tomoMuestra, " +
																    "ficha.tipoMuestra, " +
																    "ficha.cualMuestra, " +
																    "ficha.agente1, " +
																    "ficha.agente2, " +
																    "ficha.agente3, " +
																    "ficha.agente4, " +
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
																	"epidemiologia.vigifichaetas ficha," +
																	"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
																	"convenios conv, tipos_regimen regs " +
																"WHERE " +
																	"ficha.codigoFichaEtas = ? " +
																	
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
	
	
	
	
	

	private static final String insertarAlimentoStr = "INSERT INTO epidemiologia.vigiAlimentos (" +
														"codigo," +
														"codigoFicha," +
														"nombre," +
														"hora," +
														"lugar" +
													") " +
													"VALUES (?,?,?,?,?)";
	
	
	
	private static final String eliminarAlimentoStr = "DELETE FROM epidemiologia.vigiAlimentos WHERE codigo=? ";
	
	
	
	private static final String consultarAlimentoStr = "SELECT " +
														"codigo," +
														"nombre," +
														"hora," +
														"lugar " +
													"FROM epidemiologia.vigiAlimentos " +
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
										    
										    String otroSintoma,
										    String horaInicioSintomas,
										    String minutoInicioSintomas,
										    String nombreAlimento1,
										    String nombreAlimento2,
										    String nombreAlimento3,
										    String nombreAlimento4,
										    String nombreAlimento5,
										    String nombreAlimento6,
										    String nombreAlimento7,
										    String nombreAlimento8,
										    String nombreAlimento9,
										    String lugarConsumo1,
										    String lugarConsumo2,
										    String lugarConsumo3,
										    String lugarConsumo4,
										    String lugarConsumo5,
										    String lugarConsumo6,
										    String lugarConsumo7,
										    String lugarConsumo8,
										    String lugarConsumo9,
										    String horaConsumo1,
										    String horaConsumo2,
										    String horaConsumo3,
										    String horaConsumo4,
										    String horaConsumo5,
										    String horaConsumo6,
										    String horaConsumo7,
										    String horaConsumo8,
										    String horaConsumo9,
										    String minutoConsumo1,
										    String minutoConsumo2,
										    String minutoConsumo3,
										    String minutoConsumo4,
										    String minutoConsumo5,
										    String minutoConsumo6,
										    String minutoConsumo7,
										    String minutoConsumo8,
										    String minutoConsumo9,
										    int asociadoBrote,
										    int captadoPor,
										    int relacionExposicion,
										    int tomoMuestra,
										    int tipoMuestra,
										    String cualMuestra,
										    int agente1,
										    int agente2,
										    int agente3,
										    int agente4,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia,
										    
										    HashMap sintomas,
										    HashMap alimentos,
										    String secuenciaAli
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
			UtilidadBD.ingresarDatoAStatement(insertarFicha,otroSintoma,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,horaInicioSintomas,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,minutoInicioSintomas,15,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreAlimento1,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreAlimento2,17,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreAlimento3,18,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreAlimento4,19,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreAlimento5,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreAlimento6,21,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreAlimento7,22,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreAlimento8,23,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreAlimento9,24,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarConsumo1,25,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarConsumo2,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarConsumo3,27,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarConsumo4,28,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarConsumo5,29,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarConsumo6,30,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarConsumo7,31,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarConsumo8,32,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarConsumo9,33,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,horaConsumo1,34,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,horaConsumo2,35,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,horaConsumo3,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,horaConsumo4,37,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,horaConsumo5,38,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,horaConsumo6,39,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,horaConsumo7,40,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,horaConsumo8,41,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,horaConsumo9,42,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,minutoConsumo1,43,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,minutoConsumo2,44,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,minutoConsumo3,45,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,minutoConsumo4,46,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,minutoConsumo5,47,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,minutoConsumo6,48,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,minutoConsumo7,49,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,minutoConsumo8,50,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,minutoConsumo9,51,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(asociadoBrote),52,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(captadoPor),53,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(relacionExposicion),54,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tomoMuestra),55,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoMuestra),56,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualMuestra,57,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agente1),58,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agente2),59,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agente3),60,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agente4),61,Types.INTEGER,true,false);
			
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,62,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,63,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),64,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),65,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,66,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),67,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,68,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),69,Types.INTEGER,true,false);

            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),70,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,71,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),72,Types.INTEGER,true,false);
            
            
            result = insertarFicha.executeUpdate();
            
            if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            else {
			    
			    result = codigo;
			}
            
            
            
            for (int i=1;i<sintomas.size()+1;i++) {
		    	
		    	String val = sintomas.get("sintoma_"+i).toString();

		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarSintomas =  new PreparedStatementDecorator(con.prepareStatement(insertarSintomaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarSintomas.setInt(1,i);
			    	insertarSintomas.setInt(2,codigo);
			    	
			    	result = insertarSintomas.executeUpdate();
			    	
			    	if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    }
            
            
            int tamAlimentos = alimentos.size()/3;
            
            for (int i=0;i<tamAlimentos;i++) {
		    	
		    	String nombre = alimentos.get("nombre_"+i).toString();
		    	String hora = alimentos.get("hora_"+i).toString();
		    	String lugar = alimentos.get("lugar_"+i).toString();
		    	
		    	int codigoAli;
		    	
		    	PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(secuenciaAli,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            ResultSet rs2 = statement.executeQuery();
	            
	            if (rs2.next()) {
	                codigoAli = rs2.getInt(1);
	            }
	            else {
					logger.error("Error obteniendo el código de la secuencia de alimentos ");
					return 0;
				}
	            
	            PreparedStatementDecorator insertarAlimento =  new PreparedStatementDecorator(con.prepareStatement(insertarAlimentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            
	            UtilidadBD.ingresarDatoAStatement(insertarAlimento,Integer.toString(codigoAli),1,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarAlimento,Integer.toString(codigo),2,Types.INTEGER,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarAlimento,nombre,3,Types.VARCHAR,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarAlimento,hora,4,Types.VARCHAR,true,false);
	            UtilidadBD.ingresarDatoAStatement(insertarAlimento,lugar,5,Types.VARCHAR,true,false);
	            
	            result = insertarAlimento.executeUpdate();
	            
	            if(result<1)
	            {
	                daoFactory.abortTransaction(con);
	                return -1; // Estado de error
	            }
		    }
            
            

            daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaEtasDao "+sqle.toString() );
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

									    String otroSintoma,
									    String horaInicioSintomas,
									    String minutoInicioSintomas,
									    String nombreAlimento1,
									    String nombreAlimento2,
									    String nombreAlimento3,
									    String nombreAlimento4,
									    String nombreAlimento5,
									    String nombreAlimento6,
									    String nombreAlimento7,
									    String nombreAlimento8,
									    String nombreAlimento9,
									    String lugarConsumo1,
									    String lugarConsumo2,
									    String lugarConsumo3,
									    String lugarConsumo4,
									    String lugarConsumo5,
									    String lugarConsumo6,
									    String lugarConsumo7,
									    String lugarConsumo8,
									    String lugarConsumo9,
									    String horaConsumo1,
									    String horaConsumo2,
									    String horaConsumo3,
									    String horaConsumo4,
									    String horaConsumo5,
									    String horaConsumo6,
									    String horaConsumo7,
									    String horaConsumo8,
									    String horaConsumo9,
									    String minutoConsumo1,
									    String minutoConsumo2,
									    String minutoConsumo3,
									    String minutoConsumo4,
									    String minutoConsumo5,
									    String minutoConsumo6,
									    String minutoConsumo7,
									    String minutoConsumo8,
									    String minutoConsumo9,
									    int asociadoBrote,
									    int captadoPor,
									    int relacionExposicion,
									    int tomoMuestra,
									    int tipoMuestra,
									    String cualMuestra,
									    int agente1,
									    int agente2,
									    int agente3,
									    int agente4,
										String pais,
									    int areaProcedencia,
									    HashMap sintomas,
									    HashMap alimentos,
									    String secuenciaAli
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

            UtilidadBD.ingresarDatoAStatement(modificarFicha,otroSintoma,3,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,horaInicioSintomas,4,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,minutoInicioSintomas,5,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreAlimento1,6,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreAlimento2,7,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreAlimento3,8,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreAlimento4,9,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreAlimento5,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreAlimento6,11,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreAlimento7,12,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreAlimento8,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreAlimento9,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarConsumo1,15,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarConsumo2,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarConsumo3,17,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarConsumo4,18,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarConsumo5,19,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarConsumo6,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarConsumo7,21,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarConsumo8,22,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarConsumo9,23,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,horaConsumo1,24,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,horaConsumo2,25,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,horaConsumo3,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,horaConsumo4,27,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,horaConsumo5,28,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,horaConsumo6,29,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,horaConsumo7,30,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,horaConsumo8,31,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,horaConsumo9,32,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,minutoConsumo1,33,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,minutoConsumo2,34,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,minutoConsumo3,35,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,minutoConsumo4,36,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,minutoConsumo5,37,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,minutoConsumo6,38,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,minutoConsumo7,39,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,minutoConsumo8,40,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,minutoConsumo9,41,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(asociadoBrote),42,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(captadoPor),43,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(relacionExposicion),44,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tomoMuestra),45,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoMuestra),46,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualMuestra,47,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agente1),48,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agente2),49,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agente3),50,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agente4),51,Types.INTEGER,true,false);
						
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,52,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,53,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,54,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,55,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),56,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),57,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,58,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),59,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,60,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,61,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,62,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),63,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,64,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),65,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaEasv),66,Types.INTEGER,true,false);
            
            result = modificarFicha.executeUpdate();
		    
		    if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    PreparedStatementDecorator eliminarSintomas =  new PreparedStatementDecorator(con.prepareStatement(eliminarSintomasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarSintomas.setInt(1,codigoFichaEasv);
		    result = eliminarSintomas.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    for (int i=1;i<20;i++) {
		    	
		    	try {
			    	String val = sintomas.get("sintoma_"+i).toString();
	
			    	if (val.equals("true")) {
				    	PreparedStatementDecorator insertarSintomas =  new PreparedStatementDecorator(con.prepareStatement(insertarSintomaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarSintomas.setInt(1,i);
				    	insertarSintomas.setInt(2,codigoFichaEasv);
				    	
				    	result = insertarSintomas.executeUpdate();
				    	
				    	if(result<1)
			            {
			                daoFactory.abortTransaction(con);
			                return -1; // Estado de error
			            }
			    	}
		    	}
			    catch (NullPointerException npe) {}
		    }
		    
		    
		    for (int i=1;i<20;i++) {
		    	
		    	try {
			    	String nombre = alimentos.get("nombre_"+i).toString();
			    	String hora = alimentos.get("hora_"+i).toString();
			    	String lugar = alimentos.get("lugar_"+i).toString();
			    	
			    	
			    	int codigoAli;
			    		
			    	
			    	PreparedStatementDecorator statement =  new PreparedStatementDecorator(con.prepareStatement(secuenciaAli,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		            ResultSet rs2 = statement.executeQuery();
		            
		            if (rs2.next()) {
		                codigoAli = rs2.getInt(1);
		            }
		            else {
						logger.error("Error obteniendo el código de la secuencia de vacunas ");
						return 0;
					}
		            
		            PreparedStatementDecorator insertarAlimento =  new PreparedStatementDecorator(con.prepareStatement(insertarAlimentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		            
		            UtilidadBD.ingresarDatoAStatement(insertarAlimento,Integer.toString(codigoAli),1,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarAlimento,Integer.toString(codigoFichaEasv),2,Types.INTEGER,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarAlimento,nombre,3,Types.VARCHAR,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarAlimento,hora,4,Types.VARCHAR,true,false);
		            UtilidadBD.ingresarDatoAStatement(insertarAlimento,lugar,5,Types.VARCHAR,true,false);
		                        
		            result = insertarAlimento.executeUpdate();
		            
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
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaEtasDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
	}
	
	
	
	
	
	

	public static ResultSet consultarTodoFichaEtas(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaEtasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de ETAS "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaEtasDao) "+sqle);
			return null;
    	}
    }
	
	
	
	
	

    /**
     * Metodo para consultar los sintomas para la ficha de ETAS
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarSintomas(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaSintomasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los sintomas (ficha de ETAS) "+sqle);
			return null;
        }
    }
    
	
    

    public static ResultSet consultarAlimentos(Connection con, int codigo)
    {
    	try {
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarAlimentoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		
    		logger.error("Error consultando los alimentos (ficha de ETAS) "+sqle);
			return null;
    	}
    }
    
    
}
