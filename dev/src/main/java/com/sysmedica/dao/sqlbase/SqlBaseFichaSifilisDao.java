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

public class SqlBaseFichaSifilisDao {
	
	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaSifilisDao.class);
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichasifilis "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaSifilis," +
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
    
    private static final String modificarFichaStr="UPDATE epidemiologia.vigifichasifilis " +
    												"SET " +
	    												"sire=?, " +
														"estado=?, " +
														"controlPrenatal=?, " +
														"edadGestacional=?, " +
														"numeroControles=?, " +
														"edadGestacionalSero1=?, " +
														"edadGestacionalDiag1=?, " +
														"edadGestacionalTrat=?, " +
														"edadGestacionalParto=?, " +
														"estadoNacimiento=?, " +
														"recienNacido=?, " +
														"lugarAtencionParto=?, " +
														"recibioTratamiento=?, " +
														"medicamentoAdmin=?, " +
														"dosisAplicadas=?, " +
														"tratamientoHospitalario=?, " +
														"tratamientoAmbulatorio=?, " +
														"diagnosticoContactos=?, " +
														"tratamientoContactos=?, " +
														"diagnosticoIts=?, " +
														"cualesIts=?, " +
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
														"condicionMomentoDx=?, " +
														"tipoTratamiento=?, " +
														"otrasIts=?, " +
														"esquemaCompleto=?, " +
														"alergiaPenicilina=?, " +
														"desensibilizaPenicilina=?," +
														"pais=?, " +
														"areaProcedencia=? " +
													"WHERE codigoFichaSifilis = ? ";
    
    
    
    private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichasifilis "+
															"(" +
															"loginUsuario,"+
															"codigoFichaSifilis," +
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
															"controlPrenatal, " +
															"edadGestacional, " +
															"numeroControles, " +
															"edadGestacionalSero1, " +
															"edadGestacionalDiag1, " +
															"edadGestacionalTrat, " +
															"edadGestacionalParto, " +
															"estadoNacimiento, " +
															"recienNacido, " +
															"lugarAtencionParto, " +
															"recibioTratamiento, " +
															"medicamentoAdmin, " +
															"dosisAplicadas, " +
															"tratamientoHospitalario, " +
															"tratamientoAmbulatorio, " +
															"diagnosticoContactos, " +
															"tratamientoContactos, " +
															"diagnosticoIts, " +
															"cualesIts, " +
															"observaciones, " +
															
															"fechaConsultaGeneral, " +
															"fechaInicioSintomasGeneral, " +
															"tipoCaso, " +
															"hospitalizadoGeneral, " +
															"fechaHospitalizacionGeneral, " +
															"estaVivoGeneral, " +
															"fechaDefuncion, " +
															"institucionAtendio," +
															"activa," +
															"condicionMomentoDx, " +
															"tipoTratamiento, " +
															"otrasIts, " +
															"esquemaCompleto, " +
															"alergiaPenicilina, " +
															"desensibilizaPenicilina, " +
															"pais, " +
															"areaProcedencia " +
															") " +
														"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    
    
    private static final String eliminarSignosStr = "DELETE FROM epidemiologia.vigiDetalleSignos WHERE codigoFichaSifilis=? ";
    
    private static final String ingresarSignoStr = "INSERT INTO epidemiologia.vigiDetalleSignos(codigoSigno,codigoFichaSifilis) VALUES(?,?)";
    
    
    
    
    public static final String ingresarDatoLaboratorio = "INSERT INTO epidemiologia.vigilaboratoriosifilis " +
																	"(" +
																	"codigofichasifilis," +
																	"codigolaboratorio" +
																	") " +
																"VALUES (?,?)";
    
    
    public static final String modificarDatoLaboratorio = "UPDATE epidemiologia.vigilaboratoriosifilis " +
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
    
    
    public static final String consultarSignosStr = "SELECT " +
    													"codigoSigno " +
    												"FROM " +
    													"epidemiologia.vigidetallesignos " +
    												"WHERE " +
    													"codigoFichaSifilis = ?";
    
    
    public static final String consultaDatosLaboratorio = "SELECT " +
															"fechaToma," +
															"fechaRecepcion," +
															"muestra," +
															"prueba," +
															"agente," +
															"resultado," +
															"fechaResultado," +
															"valor," +
															"codigofichalaboratorios  " +
														  "FROM " +
														//	"vigiLaboratorioSifilis " +
														  	"epidemiologia.vigifichalaboratorios " +
														  "WHERE " +
														//	"codigoFichaSifilis=?";
														  	"codigoFicha=?";
    
    
    public static final String consultarFichaSifilisStr = "SELECT " +
    															"ficha.sire, " +
    															"ficha.estado, " +
    															"ficha.controlPrenatal, " +
    															"ficha.edadGestacional, " +
    															"ficha.numeroControles, " +
    															"ficha.edadGestacionalSero1, " +
    															"ficha.edadGestacionalDiag1, " +
    															"ficha.edadGestacionalTrat, " +
    															"ficha.edadGestacionalParto, " +
    															"ficha.estadoNacimiento, " +
    															"ficha.condicionmomentodx, " +
    															"ficha.recienNacido, " +
    															"ficha.lugarAtencionParto, " +
    															"ficha.recibioTratamiento, " +
    															"ficha.medicamentoAdmin, " +
    															"ficha.dosisAplicadas, " +
    															"ficha.tratamientoHospitalario, " +
    															"ficha.tratamientoAmbulatorio, " +
    															"ficha.diagnosticoContactos, " +
    															"ficha.tratamientoContactos, " +
    															"ficha.diagnosticoIts, " +
    															"ficha.cualesIts, " +
    															"ficha.observaciones, " +
    															"ficha.condicionMomentoDx, " +
    															"ficha.tipoTratamiento, " +
    															"ficha.otrasIts, " +
    															"ficha.esquemaCompleto, " +
    															"ficha.alergiaPenicilina, " +
    															"ficha.desensibilizaPenicilina, " +
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
    		    												
    		    												"bar.descripcioni AS barrio, " +
    		    												"pac.zona_domicilio AS zonaDomicilio, " +
    		    												"ocup.nombre AS ocupacionPaciente, " +
    		    												"per.tipo_identificacion AS tipoId, " +
    		    												"conv.nombre AS aseguradora, " +
    		    												"regs.nombre AS regimenSalud, " +
    		    												"pac.etnia AS etnia, " +
    		    											//	"ficha.desplazado AS desplazado " +
    		    												"pac.grupo_poblacional as grupoPoblacional " +
    		
    		    											"FROM " +
    															"epidemiologia.vigifichasifilis ficha," +
    															"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
    															"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
    															"convenios conv, tipos_regimen regs " +
    														"WHERE " +
    															"ficha.codigoFichaSifilis = ? " +
    															
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
    
    
    
    private static final String terminarFichaStr = "UPDATE epidemiologia.vigiFichaSifilis SET estado = 2 WHERE codigoFichaSifilis=?";
    
    
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaSifilisDao "+sqle.toString() );
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
										    boolean controlPrenatal,
											int edadGestacional,
											int numeroControles,
											int edadGestacionalSero1,
											int edadGestacionalDiag1,
											int edadGestacionalTrat,
											int edadGestacionalParto,
											int estadoNacimiento,
											int recienNacido,
											int lugarAtencionParto,
											boolean recibioTratamiento,
											String medicamentoAdmin,
											int dosisAplicadas,
											boolean tratamientoHospitalario,
											boolean tratamientoAmbulatorio,
											boolean diagnosticoContactos,
											boolean tratamientoContactos,
											boolean diagnosticoIts,
											String cualesIts,
											String observaciones,
											
											HashMap signosRecienNacido,
											HashMap datosLaboratorio,
											
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
										    int estadoAnterior,
										    boolean activa,
										    int condicionMomentoDx,
										    int tipoTratamiento,
										    int otrasIts,
										    int esquemaCompleto,
										    int alergiaPenicilina,
										    int desensibilizaPenicilina,
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(controlPrenatal),13,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(edadGestacional),14,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(numeroControles),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(edadGestacionalSero1),16,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(edadGestacionalDiag1),17,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(edadGestacionalTrat),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(edadGestacionalParto),19,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estadoNacimiento),20,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(recienNacido),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(lugarAtencionParto),22,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(recibioTratamiento),23,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,medicamentoAdmin,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisAplicadas),25,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(tratamientoHospitalario),26,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(tratamientoAmbulatorio),27,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(diagnosticoContactos),28,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(tratamientoContactos),29,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(diagnosticoIts),30,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,cualesIts,31,Types.VARCHAR,true,false);
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(condicionMomentoDx),42,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoTratamiento),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(otrasIts),44,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(esquemaCompleto),45,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(alergiaPenicilina),46,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(desensibilizaPenicilina),47,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,48,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),49,Types.INTEGER,true,false);
            
            
            resultado = insertarFicha.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }            
            
            for (int i=1;i<signosRecienNacido.size()+1;i++) {
		    	
		    	String val = signosRecienNacido.get("signo_"+i).toString();
		    	
		    	if (val.equals("true")) {
		    		
			    	PreparedStatementDecorator insertarSignos =  new PreparedStatementDecorator(con.prepareStatement(ingresarSignoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarSignos.setInt(1,i);
			    	insertarSignos.setInt(2,codigo);
			    	
			    	resultado = insertarSignos.executeUpdate();
			    	
			    	if(resultado<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
		    	}
		    }
            
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
    
    
    
    
    public static int terminarFicha(Connection con, int codigoFichaDengue)
    {
    	int resultado=0;
    	
    	try {
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            PreparedStatementDecorator terminarFicha =  new PreparedStatementDecorator(con.prepareStatement(terminarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(terminarFicha,Integer.toString(codigoFichaDengue),1,Types.VARCHAR,true,false);
            
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
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaSifilisDao "+sqle.toString() );
		    resultado=0;
        }
        
        return resultado;
    }
    
    
    
    
    public static int modificarFicha(Connection con,
										String sire,
										String loginUsuario,
									    int codigoFichaSifilis,
									    int estado,
									    
									    boolean controlPrenatal,
										int edadGestacional,
										int numeroControles,
										int edadGestacionalSero1,
										int edadGestacionalDiag1,
										int edadGestacionalTrat,
										int edadGestacionalParto,
										int estadoNacimiento,
										int recienNacido,
										int lugarAtencionParto,
										boolean recibioTratamiento,
										String medicamentoAdmin,
										int dosisAplicadas,
										boolean tratamientoHospitalario,
										boolean tratamientoAmbulatorio,
										boolean diagnosticoContactos,
										boolean tratamientoContactos,
										boolean diagnosticoIts,
										String cualesIts,
										String observaciones,
										
										HashMap signosRecienNacido,
										HashMap datosLaboratorio,
										
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
									    int estadoAnterior,
									    int condicionMomentoDx,
									    int tipoTratamiento,
									    int otrasIts,
									    int esquemaCompleto,
									    int alergiaPenicilina,
									    int desensibilizaPenicilina,
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
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(controlPrenatal),3,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(edadGestacional),4,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(numeroControles),5,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(edadGestacionalSero1),6,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(edadGestacionalDiag1),7,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(edadGestacionalTrat),8,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(edadGestacionalParto),9,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estadoNacimiento),10,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(recienNacido),11,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarAtencionParto),12,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(recibioTratamiento),13,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,medicamentoAdmin,14,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisAplicadas),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(tratamientoHospitalario),16,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(tratamientoAmbulatorio),17,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(diagnosticoContactos),18,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(tratamientoContactos),19,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(diagnosticoIts),20,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,cualesIts,21,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,observaciones,22,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,23,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,25,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,26,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),27,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),28,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,29,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),30,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,31,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,33,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),34,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(condicionMomentoDx),35,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoTratamiento),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(otrasIts),37,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(esquemaCompleto),38,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(alergiaPenicilina),39,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(desensibilizaPenicilina),40,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),42,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaSifilis),43,Types.INTEGER,true,false);
            
            result = modificarFicha.executeUpdate();
            
            if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            PreparedStatementDecorator eliminarSignos =  new PreparedStatementDecorator(con.prepareStatement(eliminarSignosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            eliminarSignos.setInt(1,codigoFichaSifilis);
            result = eliminarSignos.executeUpdate();
            
            if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            
            try {
            	
	            for (int i=1;i<signosRecienNacido.size()+1;i++) {
			    	
			    	String val = signosRecienNacido.get("signo_"+i).toString();
			    	
			    	if (val.equals("true")) {
			    		
				    	PreparedStatementDecorator insertarSignos =  new PreparedStatementDecorator(con.prepareStatement(ingresarSignoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarSignos.setInt(1,i);
				    	insertarSignos.setInt(2,codigoFichaSifilis);
				    	
				    	result = insertarSignos.executeUpdate();
				    	
				    	if(result<1)
			            {
			                daoFactory.abortTransaction(con);
			                return -1; // Estado de error
			            }
			    	}
			    }
            }
            catch (NullPointerException npe) {}
            
            
            
            //*************************************************************
            // modificacion de los datos de laboratorio
		    
            try {
			    PreparedStatementDecorator modificarDatosLaboratorio =  new PreparedStatementDecorator(con.prepareStatement(modificarDatoLaboratorio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            
	            for (int i=1;i<5;i++) {
	            	
	            	String codigoLaboratorio = Integer.toString(codigoFichaSifilis) + Integer.toString(i);
	            	
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("fechaToma"+i).toString(),1,Types.VARCHAR,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("fechaRecepcion"+i).toString(),2,Types.VARCHAR,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("muestra"+i).toString(),3,Types.INTEGER,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("prueba"+i).toString(),4,Types.INTEGER,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("agente"+i).toString(),5,Types.INTEGER,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("resultado"+i).toString(),6,Types.INTEGER,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("fechaResultado"+i).toString(),7,Types.VARCHAR,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,datosLaboratorio.get("valor"+i).toString(),8,Types.VARCHAR,true,false);
	                UtilidadBD.ingresarDatoAStatement(modificarDatosLaboratorio,codigoLaboratorio,9,Types.INTEGER,true,false);
	                
	                result = modificarDatosLaboratorio.executeUpdate();
	                if (result<1) {
	                    
	                    daoFactory.abortTransaction(con);
	                    return -1;
	                }   
	            }
            }
            catch (NullPointerException npe) {
            	
            }
		    
		    daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaSifilisDao "+sqle.toString() );
		    result=0;
        }
    	
    	return result;
    }
    
    
    
    
    public static ResultSet consultarTodoFichaSifilis(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaSifilisStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Sifilis Congenita "+sqle);
			return null;
		}
    }
    
    
    
    
    public static ResultSet consultarSignos(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarSignosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los mecanismos de transmision (ficha de Sifilis Congenita) "+sqle);
			return null;
        }
    }
    
    
    
    
    public static ResultSet consultarDatosLaboratorio(Connection con, int codigo)
    {
        
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaDatosLaboratorio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los datos de laboratorio (ficha de Sifilis Congenita) "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaSifilisDao) "+sqle);
			return null;
    	}
    }
}
