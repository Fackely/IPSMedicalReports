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

public class SqlBaseFichaMalariaDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaMalariaDao.class);
    
    
    private static final String insertarFichaCompletaStr = "INSERT INTO epidemiologia.vigifichamalaria "+
																"(" +
																"loginUsuario,"+
																"codigoFichaMalaria," +
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
																"viajo, " +
															    "codDepViajo, " +
															    "codMunViajo, " +
															    "padecioMalaria, " +
															    "fechaAproximada, " +
															    "automedicacion, " +
															    "antecedenteTrans, " +
															    "fechaAntecedente, " +
															    "tipoComplicacion, " +
															    "especiePlasmodium, " +
															    "embarazo, " +
															    "tratAntimalarico, " +
															    "cualTratamiento, " +
																
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
																	"?,?,?,?,?,?,?,?" +
																	") ";
    
    
    

    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichamalaria " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"viajo=?, " +
													    "codDepViajo=?, " +
													    "codMunViajo=?, " +
													    "padecioMalaria=?, " +
													    "fechaAproximada=?, " +
													    "automedicacion=?, " +
													    "antecedenteTrans=?, " +
													    "fechaAntecedente=?, " +
													    "tipoComplicacion=?, " +
													    "especiePlasmodium=?, " +
													    "embarazo=?, " +
													    "tratAntimalarico=?, " +
													    "cualTratamiento=?, " +
														
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
													"WHERE codigoFichaMalaria=? ";
    
    
    
    

    private static final String eliminarSintomasStr = "DELETE from epidemiologia.vigiDetalleSintMalaria WHERE codigoFichaMalaria = ?";
    
    
    private static final String insertarSintomaStr = "INSERT INTO epidemiologia.vigiDetalleSintMalaria(codigo,codigofichamalaria) VALUES(?,?)";
    
    

    public static final String consultaSintomasStr = "SELECT " +
															"codigo " +
														"FROM " +
															"epidemiologia.vigiDetalleSintMalaria " +
														"WHERE " +
															"codigoFichaMalaria = ? ";
    
    
    
    
    

    private static final String eliminarTratamientoStr = "DELETE from epidemiologia.vigiDetalleTratMalaria WHERE codigoFichaMalaria = ?";
    
    
    private static final String insertarTratamientoStr = "INSERT INTO epidemiologia.vigiDetalleTratMalaria(codigo,codigofichamalaria) VALUES(?,?)";
    
    

    public static final String consultaTratamientoStr = "SELECT " +
															"codigo " +
														"FROM " +
															"epidemiologia.vigiDetalleTratMalaria " +
														"WHERE " +
															"codigoFichaMalaria = ? ";
    
    
    
    

    private static final String consultarFichaMalariaStr = "SELECT " +
																	"ficha.sire," +
																	"ficha.estado," +
																	"ficha.viajo, " +
																    "ficha.codDepViajo, " +
																    "ficha.codMunViajo, " +
																    "ficha.padecioMalaria, " +
																    "ficha.fechaAproximada, " +
																    "ficha.automedicacion, " +
																    "ficha.antecedenteTrans, " +
																    "ficha.fechaAntecedente, " +
																    "ficha.tipoComplicacion, " +
																    "ficha.especiePlasmodium, " +
																    "ficha.embarazo, " +
																    "ficha.tratAntimalarico, " +
																    "ficha.cualTratamiento, " +
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
																
																	"FROM epidemiologia.vigifichamalaria ficha " +
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
																		"ficha.codigoFichaMalaria = ? ";
																	
    
    
    

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
																"tr.nombre AS regimenSalud, " +
																"pac.etnia AS etnia, " +
																"pac.grupo_poblacional as grupoPoblacional " +
															"FROM personas per " +
															"INNER JOIN departamentos dep on(dep.codigo_departamento=per.codigo_departamento_vivienda AND dep.codigo_pais=per.codigo_pais_vivienda)  " +
															"INNER JOIN ciudades ciu ON(ciu.codigo_ciudad=per.codigo_ciudad_vivienda AND ciu.codigo_departamento=per.codigo_departamento_vivienda AND ciu.codigo_pais=per.codigo_pais_vivienda) " +
															"INNER JOIN barrios bar ON(bar.codigo=per.codigo_barrio_vivienda) " +
															"INNER JOIN pacientes pac ON(pac.codigo_paciente=per.codigo)  " +
															"INNER JOIN ocupaciones ocup ON(ocup.codigo=pac.ocupacion) " +
															"INNER JOIN cuentas c ON(c.codigo_paciente = pac.codigo_paciente) " +
															"INNER JOIN sub_cuentas sc ON(sc.ingreso = c.id_ingreso AND sc.nro_prioridad = 1) " +
															"INNER JOIN convenios conv ON(conv.codigo = sc.convenio) " +
															"INNER JOIN tipos_regimen tr ON(tr.acronimo=conv.tipo_regimen) " +
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
										    
										    int viajo,
										    String codDepViajo,
										    String codMunViajo,
										    int padecioMalaria,
										    String fechaAproximada,
										    int automedicacion,
										    int antecedenteTrans,
										    String fechaAntecedente,
										    int tipoComplicacion,
										    int especiePlasmodium,
										    int embarazo,
										    int tratAntimalarico,
										    String cualTratamiento,
										    HashMap sintomas,
										    HashMap tratamiento,
										    
										    boolean activa,
										    String pais,
										    int areaProcedencia,
										    String lugarViajo
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
            
            String codigoMunViajo = lugarViajo.split("-")[0];
            String codigoDepViajo = lugarViajo.split("-")[1];
            
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
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(viajo),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDepViajo,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoMunViajo,15,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(padecioMalaria),16,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaAproximada,17,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(automedicacion),18,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(antecedenteTrans),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaAntecedente,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoComplicacion),21,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(especiePlasmodium),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(embarazo),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tratAntimalarico),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarFicha,cualTratamiento,25,Types.VARCHAR,true,false);
						
			UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,26,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,27,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),28,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),29,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),31,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),33,Types.INTEGER,true,false);

            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),34,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,35,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),36,Types.INTEGER,true,false);
            
            
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
			    	else {
					    
					    result = codigo;
					}
		    	}
		    }
            
            
            for (int i=1;i<tratamiento.size()+1;i++) {
		    	
		    	String val = tratamiento.get("trat_"+i).toString();
		    	
		    	if (val.equals("true")) {
			    	PreparedStatementDecorator insertarTratamiento =  new PreparedStatementDecorator(con.prepareStatement(insertarTratamientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			    	
			    	insertarTratamiento.setInt(1,i);
			    	insertarTratamiento.setInt(2,codigo);
			    	
			    	result = insertarTratamiento.executeUpdate();
			    	
			    	if(result<1)
		            {
		                daoFactory.abortTransaction(con);
		                return -1; // Estado de error
		            }
			    	else {
					    
					    result = codigo;
					}
		    	}
		    }
            

            daoFactory.endTransaction(con);
    	}
    	catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaMalariaDao "+sqle.toString() );
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

									    int viajo,
									    String codDepViajo,
									    String codMunViajo,
									    int padecioMalaria,
									    String fechaAproximada,
									    int automedicacion,
									    int antecedenteTrans,
									    String fechaAntecedente,
									    int tipoComplicacion,
									    int especiePlasmodium,
									    int embarazo,
									    int tratAntimalarico,
									    String cualTratamiento,
									    HashMap sintomas,
									    HashMap tratamiento,
									    
										String pais,
									    int areaProcedencia,
									    String lugarViajo
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
            
            String codigoMunViajo = lugarViajo.split("-")[0];
            String codigoDepViajo = lugarViajo.split("-")[1];
            
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(viajo),3,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepViajo,4,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunViajo,5,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(padecioMalaria),6,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaAproximada,7,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(automedicacion),8,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(antecedenteTrans),9,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaAntecedente,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoComplicacion),11,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(especiePlasmodium),12,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(embarazo),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tratAntimalarico),14,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,cualTratamiento,15,Types.VARCHAR,true,false);
						            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,16,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,17,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,18,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,19,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),20,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),21,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,22,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),23,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,25,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,26,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),27,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,28,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),29,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaEasv),30,Types.INTEGER,true,false);
            
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
		    
		    for (int i=1;i<30;i++) {
		    	
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
		    
		    
		    for (int i=1;i<15;i++) {
		    	
		    	try {
			    	String val = tratamiento.get("tratamiento_"+i).toString();
	
			    	if (val.equals("true")) {
				    	PreparedStatementDecorator insertarTratamiento =  new PreparedStatementDecorator(con.prepareStatement(insertarTratamientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				    	
				    	insertarTratamiento.setInt(1,i);
				    	insertarTratamiento.setInt(2,codigoFichaEasv);
				    	
				    	result = insertarTratamiento.executeUpdate();
				    	
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
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaMalariaDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
	}
	
	
	
	

	public static ResultSet consultarTodoFichaMalaria(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaMalariaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Malaria "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaMalariaDao) "+sqle);
			return null;
    	}
    }
	
	
	
	
	

    /**
     * Metodo para consultar los sintomas para la ficha de malaria
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
            
            logger.error("Error consultando los sintomas (ficha de Malaria) "+sqle);
			return null;
        }
    }
    
    
    
    
    
    

    /**
     * Metodo para consultar los tratamientos para la ficha de malaria
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarTratamiento(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaTratamientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los tratamientos (ficha de Malaria) "+sqle);
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
