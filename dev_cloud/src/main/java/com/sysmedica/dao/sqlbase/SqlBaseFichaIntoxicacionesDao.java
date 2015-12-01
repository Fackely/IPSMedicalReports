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

public class SqlBaseFichaIntoxicacionesDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaVIHDao.class);
    
    
    private static final String insertarFichaCompletaStr = "INSERT INTO epidemiologia.vigifichaintoxicacion "+
																"(" +
																"loginUsuario,"+
																"codigoFichaIntoxicacion," +
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
																"tipoIntoxicacion, " +
															    "nombreProducto, " +
															    "tipoExposicion, " +
															    "produccion, " +
															    "almacenamiento, " +
															    "agricola, " +
															    "saludPublica, " +
															    "domiciliaria, " +
															    "tratHumano, " +
															    "tratVeterinario, " +
															    "transporte, " +
															    "mezcla, " +
															    "mantenimiento, " +
															    "cultivo, " +
															    "otros, " +
															    "otraActividad, " +
															    "fechaExposicion, " +
															    "horaExposicion, " +
															    "viaExposicion, " +
															    "otraViaExposicion, " +
															    "escolaridad, " +
															    "embarazada, " +
															    "vinculoLaboral, " +
															    "afiliadoArp, " +
															    "nombreArp, " +
															    "codgoArp, " +
															 //   "estadoCivil, " +
															    "alerta, " +
															    "investigacion, " +
															    "fechaInvestigacion, " +
															    "fechaInforma, " +
															    "nombreResponsable, " +
															    "telefonoResponsable, " +
																
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
															"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    
    
    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichaintoxicacion " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"tipoIntoxicacion=?, " +
													    "nombreProducto=?, " +
													    "tipoExposicion=?, " +
													    "produccion=?, " +
													    "almacenamiento=?, " +
													    "agricola=?, " +
													    "saludPublica=?, " +
													    "domiciliaria=?, " +
													    "tratHumano=?, " +
													    "tratVeterinario=?, " +
													    "transporte=?, " +
													    "mezcla=?, " +
													    "mantenimiento=?, " +
													    "cultivo=?, " +
													    "otros=?, " +
													    "otraActividad=?, " +
													    "fechaExposicion=?, " +
													    "horaExposicion=?, " +
													    "viaExposicion=?, " +
													    "otraViaExposicion=?, " +
													    "escolaridad=?, " +
													    "embarazada=?, " +
													    "vinculoLaboral=?, " +
													    "afiliadoArp=?, " +
													    "nombreArp=?, " +
													    "codgoArp=?, " +
													 //   "estadoCivil=?, " +
													    "alerta=?, " +
													    "investigacion=?, " +
													    "fechaInvestigacion=?, " +
													    "fechaInforma=?, " +
													    "nombreResponsable=?, " +
													    "telefonoResponsable=?, " +
														
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
													"WHERE codigoFichaIntoxicacion=? ";
    
    
    
    
    private static final String consultarFichaIntoxicacionStr = "SELECT " +
																	"ficha.sire," +
																	"ficha.estado," +
																	"ficha.tipoIntoxicacion, " +
																    "ficha.nombreProducto, " +
																    "ficha.tipoExposicion, " +
																    "ficha.produccion, " +
																    "ficha.almacenamiento, " +
																    "ficha.agricola, " +
																    "ficha.saludPublica, " +
																    "ficha.domiciliaria, " +
																    "ficha.tratHumano, " +
																    "ficha.tratVeterinario, " +
																    "ficha.transporte, " +
																    "ficha.mezcla, " +
																    "ficha.mantenimiento, " +
																    "ficha.cultivo, " +
																    "ficha.otros, " +
																    "ficha.otraActividad, " +
																    "ficha.fechaExposicion, " +
																    "ficha.horaExposicion, " +
																    "ficha.viaExposicion, " +
																    "ficha.otraViaExposicion, " +
																    "ficha.escolaridad, " +
																    "ficha.embarazada, " +
																    "ficha.vinculoLaboral, " +
																    "ficha.afiliadoArp, " +
																    "ficha.nombreArp, " +
																    "ficha.codgoArp, " +
																//    "ficha.estadoCivil, " +
																    "ficha.alerta, " +
																    "ficha.investigacion, " +
																    "ficha.fechaInvestigacion, " +
																    "ficha.fechaInforma, " +
																    "ficha.nombreResponsable, " +
																    "ficha.telefonoResponsable, " +
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
																	"epidemiologia.vigifichaintoxicacion ficha," +
																	"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
																	"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
																	"convenios conv, tipos_regimen regs " +
																"WHERE " +
																	"ficha.codigoFichaIntoxicacion = ? " +
																	
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
											    
											    int tipoIntoxicacion,
											    String nombreProducto,
											    int tipoExposicion,
											    int produccion,
											    int almacenamiento,
											    int agricola,
											    int saludPublica,
											    int domiciliaria,
											    int tratHumano,
											    int tratVeterinario,
											    int transporte,
											    int mezcla,
											    int mantenimiento,
											    int cultivo,
											    int otros,
											    String otraActividad,
											    String fechaExposicion,
											    int horaExposicion,
											    int viaExposicion,
											    String otraViaExposicion,
											    int escolaridad,
											    int embarazada,
											    int vinculoLaboral,
											    int afiliadoArp,
											    String nombreArp,
											    int codgoArp,
											    int estCivil,
											    int alerta,
											    int investigacion,
											    String fechaInvestigacion,
											    String fechaInforma,
											    String nombreResponsable,
											    String telefonoResponsable,
											    String observaciones,
											    
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoIntoxicacion),13,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreProducto,14,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoExposicion),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(produccion),16,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(almacenamiento),17,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agricola),18,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(saludPublica),19,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(domiciliaria),20,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tratHumano),21,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tratVeterinario),22,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(transporte),23,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(mezcla),24,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(mantenimiento),25,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(cultivo),26,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(otros),27,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,otraActividad,28,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaExposicion,29,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(horaExposicion),30,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(viaExposicion),31,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,otraViaExposicion,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(escolaridad),33,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(embarazada),34,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vinculoLaboral),35,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(afiliadoArp),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreArp,37,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codgoArp),38,Types.INTEGER,true,false);
     //       UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estCivil),39,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(alerta),39,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(investigacion),40,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInvestigacion,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInforma,42,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreResponsable,43,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,telefonoResponsable,44,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,46,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),47,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),48,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,49,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),50,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,51,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),52,Types.INTEGER,true,false);

            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),53,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,54,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),55,Types.INTEGER,true,false);
            
            
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaIntoxicacionesDao "+sqle.toString() );
		    result=0;			
		}
        
        return result;
	}
	
	
	
	
	
	public static int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaIntoxicacion,
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
									    
									    int tipoIntoxicacion,
									    String nombreProducto,
									    int tipoExposicion,
									    int produccion,
									    int almacenamiento,
									    int agricola,
									    int saludPublica,
									    int domiciliaria,
									    int tratHumano,
									    int tratVeterinario,
									    int transporte,
									    int mezcla,
									    int mantenimiento,
									    int cultivo,
									    int otros,
									    String otraActividad,
									    String fechaExposicion,
									    int horaExposicion,
									    int viaExposicion,
									    String otraViaExposicion,
									    int escolaridad,
									    int embarazada,
									    int vinculoLaboral,
									    int afiliadoArp,
									    String nombreArp,
									    int codgoArp,
									    int estCivil,
									    int alerta,
									    int investigacion,
									    String fechaInvestigacion,
									    String fechaInforma,
									    String nombreResponsable,
									    String telefonoResponsable,
									    String observaciones,
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
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoIntoxicacion),3,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreProducto,4,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoExposicion),5,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(produccion),6,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(almacenamiento),7,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agricola),8,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(saludPublica),9,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(domiciliaria),10,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tratHumano),11,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tratVeterinario),12,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(transporte),13,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(mezcla),14,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(mantenimiento),15,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(cultivo),16,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(otros),17,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,otraActividad,18,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaExposicion,19,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(horaExposicion),20,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(viaExposicion),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,otraViaExposicion,22,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(escolaridad),23,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(embarazada),24,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vinculoLaboral),25,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(afiliadoArp),26,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreArp,27,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codgoArp),28,Types.INTEGER,true,false);
       //     UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estCivil),29,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(alerta),29,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(investigacion),30,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInvestigacion,31,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInforma,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,nombreResponsable,33,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,telefonoResponsable,34,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,35,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,36,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,37,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,38,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),39,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),40,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),42,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,43,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,44,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),46,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,47,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),48,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaIntoxicacion),49,Types.INTEGER,true,false);
            
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
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaIntoxicacionesDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
	}
	
	
	
	
	
	
	public static ResultSet consultarTodoFichaIntoxicacion(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaIntoxicacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Intoxicaciones "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaIntoxicacionesDao) "+sqle);
			return null;
    	}
    }
}
