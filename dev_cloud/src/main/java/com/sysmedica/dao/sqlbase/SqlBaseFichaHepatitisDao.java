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

public class SqlBaseFichaHepatitisDao {


	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaHepatitisDao.class);
    
    
    private static final String insertarFichaCompletaStr = "INSERT INTO epidemiologia.vigifichahepatitis "+
																"(" +
																"loginUsuario,"+
																"codigoFichaHepatitis," +
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
																"embarazada, " +
																"edadGestacional, " +
																"controlPrenatal, " +
																"donanteSangre, " +
																"poblacionRiesgo, " +
																"modoTransmision, " +
																"otrasIts, " +
																"vacunaAntihepatitis, " +
																"numeroDosis, " +
																"fechaPrimeraDosis, " +
																"fechaUltimaDosis, " +
																"fuenteInformacion, " +
																"tratamiento, " +
																"cualTratamiento, " +
																"complicacion, " +
																"cualComplicacion, " +
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
																	"?,?" +
																	") ";
    
    
    

    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichahepatitis " +
														"SET " +
														"sire=?, " +
														"embarazada=?, " +
														"edadGestacional=?, " +
														"controlPrenatal=?, " +
														"donanteSangre=?, " +
														"poblacionRiesgo=?, " +
														"modoTransmision=?, " +
														"otrasIts=?, " +
														"vacunaAntihepatitis=?, " +
														"numeroDosis=?, " +
														"fechaPrimeraDosis=?, " +
														"fechaUltimaDosis=?, " +
														"fuenteInformacion=?, " +
														"tratamiento=?, " +
														"cualTratamiento=?, " +
														"complicacion=?, " +
														"cualComplicacion=?, " +
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
													"WHERE codigoFichaHepatitis=? ";
    
    
    

    private static final String eliminarSintomasStr = "DELETE from epidemiologia.vigiDetalleSintHepatitis WHERE codigoFichaHepatitis = ?";
    
    
    private static final String insertarSintomaStr = "INSERT INTO epidemiologia.vigiDetalleSintHepatitis(codigo,codigofichahepatitis) VALUES(?,?)";
    
    

    public static final String consultaSintomasStr = "SELECT " +
															"codigo " +
														"FROM " +
															"epidemiologia.vigiDetalleSintHepatitis " +
														"WHERE " +
															"codigoFichaHepatitis = ? ";
    
    
    
    
    

    private static final String consultarFichaHepatitisStr = "SELECT " +
																	"ficha.sire," +
																	"ficha.estado," +
																	"ficha.embarazada, " +
																	"ficha.edadGestacional, " +
																	"ficha.controlPrenatal, " +
																	"ficha.donanteSangre, " +
																	"ficha.poblacionRiesgo, " +
																	"ficha.modoTransmision, " +
																	"ficha.otrasIts, " +
																	"ficha.vacunaAntihepatitis, " +
																	"ficha.numeroDosis, " +
																	"ficha.fechaPrimeraDosis, " +
																	"ficha.fechaUltimaDosis, " +
																	"ficha.fuenteInformacion, " +
																	"ficha.tratamiento, " +
																	"ficha.cualTratamiento, " +
																	"ficha.complicacion, " +
																	"ficha.cualComplicacion, " +
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
																
																	"FROM epidemiologia.vigifichahepatitis ficha " +
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
																		"ficha.codigoFichaHepatitis = ? " ;
																	
    
    
    

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
	
	
	
	
	public static String insertarPoblacionStr = "INSERT INTO epidemiologia.vigiDetallePobRiesgo (codigo,codigoFicha) VALUES (?,?)";
	
	
	public static String eliminarPoblacionStr = "DELETE FROM epidemiologia.vigiDetallePobRiesgo WHERE codigoFicha=? ";
	
	
	public static String consultarPoblacionStr = "SELECT codigo FROM epidemiologia.vigiDetallePobRiesgo WHERE codigoFicha=? ";	
	
	
	
	

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
										    
										    int embarazada,
										    String edadGestacional,
										    int controlPrenatal,
										    int donanteSangre,
										    int poblacionRiesgo,
										    int modoTransmision,
										    int otrasIts,
										    int vacunaAntihepatitis,
										    String numeroDosis,
										    String fechaPrimeraDosis,
										    String fechaUltimaDosis,
										    int fuenteInformacion,
										    int tratamiento,
										    String cualTratamiento,
										    int complicacion,
										    String cualComplicacion,
										    String observaciones,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia,
										    
										    HashMap sintomas,
										    HashMap poblaRiesgo
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
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(embarazada),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,edadGestacional,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(controlPrenatal),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(donanteSangre),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(poblacionRiesgo),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(modoTransmision),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(otrasIts),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaAntihepatitis),20,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,numeroDosis,21,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaPrimeraDosis,22,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosis,23,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fuenteInformacion),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tratamiento),25,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualTratamiento,26,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(complicacion),27,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualComplicacion,28,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,observaciones,29,Types.VARCHAR,true,false);
			
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,31,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),32,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),33,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,34,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),35,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,36,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),37,Types.INTEGER,true,false);

            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),38,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,39,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),40,Types.INTEGER,true,false);
            
            
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
            

            for (int i=1;i<poblaRiesgo.size()+1;i++) {
		    	
		    	String val = poblaRiesgo.get("poblacion_"+i).toString();
		    	
		    	if (val.equals("true")) {
		    		
		    		PreparedStatementDecorator insertarPoblacion =  new PreparedStatementDecorator(con.prepareStatement(insertarPoblacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    		
		    		insertarPoblacion.setInt(1,i);
		    		insertarPoblacion.setInt(2,codigo);
		    		
		    		result = insertarPoblacion.executeUpdate();
		    		
		    		if (result<1)
		    		{
		    			daoFactory.abortTransaction(con);
		    			return -1;
		    		}
		    		else {
		    			result = codigo;
		    		}
		    	}
		    }      
            
            
            

            daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaHepatitisDao "+sqle.toString() );
		    result=0;			
		}
        
        return result;
	}
	
    
	
	
	

	public static int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaHepatitis,
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

									    int embarazada,
									    String edadGestacional,
									    int controlPrenatal,
									    int donanteSangre,
									    int poblacionRiesgo,
									    int modoTransmision,
									    int otrasIts,
									    int vacunaAntihepatitis,
									    String numeroDosis,
									    String fechaPrimeraDosis,
									    String fechaUltimaDosis,
									    int fuenteInformacion,
									    int tratamiento,
									    String cualTratamiento,
									    int complicacion,
									    String cualComplicacion,
									    String observaciones,
										String pais,
									    int areaProcedencia,
									    HashMap sintomas,
									    HashMap poblaRiesgo
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

            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(embarazada),3,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,edadGestacional,4,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(controlPrenatal),5,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(donanteSangre),6,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(poblacionRiesgo),7,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(modoTransmision),8,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(otrasIts),9,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaAntihepatitis),10,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,numeroDosis,11,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaPrimeraDosis,12,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosis,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fuenteInformacion),14,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tratamiento),15,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualTratamiento,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(complicacion),17,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualComplicacion,18,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,observaciones,19,Types.VARCHAR,true,false);
						
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,20,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,21,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,22,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,23,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),24,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),25,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,26,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),27,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,28,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,29,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),31,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),33,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaHepatitis),34,Types.INTEGER,true,false);
            
            result = modificarFicha.executeUpdate();
		    
		    if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    PreparedStatementDecorator eliminarSintomas =  new PreparedStatementDecorator(con.prepareStatement(eliminarSintomasStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarSintomas.setInt(1,codigoFichaHepatitis);
		    result = eliminarSintomas.executeUpdate();
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    
		    for (int i=1;i<10;i++) {
		    	
		    	try {
			    	String val = sintomas.get("sintoma_"+i).toString();
	
			    	if (val.equals("true")) {
				    	PreparedStatementDecorator insertarSintomas =  new PreparedStatementDecorator(con.prepareStatement(insertarSintomaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarSintomas.setInt(1,i);
				    	insertarSintomas.setInt(2,codigoFichaHepatitis);
				    	
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
		    
		    
		    PreparedStatementDecorator eliminarPoblacion =  new PreparedStatementDecorator(con.prepareStatement(eliminarPoblacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		    
		    eliminarPoblacion.setInt(1,codigoFichaHepatitis);
		    result = eliminarPoblacion.executeUpdate();
		    
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
           
		    
		    for (int i=1;i<15;i++) {
		    	
		    	try {
			    	String val = poblaRiesgo.get("poblacion_"+i).toString();
			    	
			    	if (val.equals("true")) {
			    		
			    		PreparedStatementDecorator insertarPoblacion =  new PreparedStatementDecorator(con.prepareStatement(insertarPoblacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    		
			    		insertarPoblacion.setInt(1,i);
			    		insertarPoblacion.setInt(2,codigoFichaHepatitis);
			    		
			    		result = insertarPoblacion.executeUpdate();
			    		
			    		if (result<1)
			    		{
			    			daoFactory.abortTransaction(con);
			    			return -1;
			    		}
			    	}
		    	}
			    catch (NullPointerException npe) {}
		    }

		    daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaHepatitisDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
	}
	
	
	
	
	
	
	
	

	public static ResultSet consultarTodoFichaHepatitis(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaHepatitisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Hepatitis "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaHepatitisDao) "+sqle);
			return null;
    	}
    }
	
	
	
	
	

    /**
     * Metodo para consultar los sintomas de la ficha de Hepatitis
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
            
            logger.error("Error consultando los sintomas (ficha de Hepatitis) "+sqle);
			return null;
        }
    }
    
    
    
    
    /**
     * Metodo para consultar la población de riesgo
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarPoblacion(Connection con, int codigo)
    {
        
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarPoblacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando la poblacion de riesgo (ficha de Hepatitis) "+sqle);
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
            
            logger.error("Error consultando los datos de laboratorio (ficha de Hepatitis) "+sqle);
			return null;
        }
    }
}
