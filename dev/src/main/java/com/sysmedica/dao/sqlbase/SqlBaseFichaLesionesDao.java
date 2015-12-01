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

public class SqlBaseFichaLesionesDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaLesionesDao.class);
    
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichalesiones "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaLesiones," +
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
    														
    														"armaFuego, " +
    													    "armaCortopunzante, " +
    													    "armaContundente, " +
    													    "asfixia, " +
    													    "intoxicacion, " +
    													    "inmersion, " +
    													    "explosivo, " +
    													    "polvora, " +
    													    "otracausa, " +
    													    "caida, " +
    													    "mordedura, " +
    													    "vivienda, " +
    													    "lugarTrabajo, " +
    													    "lugarEstudio, " +
    													    "taberna, " +
    													    "establecimientoPublico, " +
    													    "viaPublica, " +
    													    "otroLugar, " +
    													    "denunciado, " +
    													    "consumoAlcohol, " +
    													    "consumoOtrasSustancias, " +
    													    "lesionAccidenteTrabajo, " +
    													    "fueraBogota " +
    														
    														")"+
    														" VALUES(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",'21','11','21','11',?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    
    
    private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichalesiones " +
    														"(" +
															    "loginUsuario,"+
																"codigoFichaLesiones," +
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
																
																"estadoIngreso, " +
															    "fechaOcurrencia, " +
															    "horaOcurrencia, " +
															    "lugarOcurrencia, " +
															    "localidad, " +
															    "municipioOcurrencia, " +
															    "departamentoOcurrencia, " +
															    "armaFuego, " +
															    "armaCortopunzante, " +
															    "armaContundente, " +
															    "asfixia, " +
															    "intoxicacion, " +
															    "inmersion, " +
															    "explosivo, " +
															    "polvora, " +
															    "otracausa, " +
															    "caida, " +
															    "mordedura, " +
															    "vivienda, " +
															    "lugarTrabajo, " +
															    "lugarEstudio, " +
															    "taberna, " +
															    "establecimientoPublico, " +
															    "viaPublica, " +
															    "otroLugar, " +
															    "lesionAccidenteTrabajo, " +
															    "codigoArp, " +
															    "actividadDuranteHecho, " +
															    "lesionIntencional, " +
															    "tipoLesion, " +
															    "tipoVehiculo, " +
															    "condicionLesionado, " +
															    "tipoViolencia, " +
															    "agresor, " +
															    "denunciado, " +
															    "consumoAlcohol, " +
															    "consumoOtrasSustancias, " +
															    "impresionDiagnostica, " +
															    "gravedadLesion, " +
															    "institucionAtendio, " +
															    "fueraBogota," +
															    "activa " +
															 ") " +
														 "VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",'21','11','21','11',?,? " +
														 ",?,?,?,?,?,?,?,?,?,?" +
														 ",?,?,?,?,?,?,?,?,?,?" +
														 ",?,?,?,?,?,?,?,?,?,?" +
														 ",?,?,?,?,?,?,?,?,?,?" +
														 ",?, ?" +
														 ") ";
    
    
    public static final String modificarFichaStr = "UPDATE epidemiologia.vigiFichaLesiones " +
														"SET " +
														"estado=?, " +
														"estadoIngreso=?, " +
													    "fechaOcurrencia=?, " +
													    "horaOcurrencia=?, " +
													    "lugarOcurrencia=?, " +
													    "localidad=?, " +
													    "municipioOcurrencia=?, " +
													    "departamentoOcurrencia=?, " +
													    "armaFuego=?, " +
													    "armaCortopunzante=?, " +
													    "armaContundente=?, " +
													    "asfixia=?, " +
													    "intoxicacion=?, " +
													    "inmersion=?, " +
													    "explosivo=?, " +
													    "polvora=?, " +
													    "otracausa=?, " +
													    "caida=?, " +
													    "mordedura=?, " +
													    "vivienda=?, " +
													    "lugarTrabajo=?, " +
													    "lugarEstudio=?, " +
													    "taberna=?, " +
													    "establecimientoPublico=?, " +
													    "viaPublica=?, " +
													    "otroLugar=?, " +
													    "lesionAccidenteTrabajo=?, " +
													    "codigoArp=?, " +
													    "actividadDuranteHecho=?, " +
													    "lesionIntencional=?, " +
													    "tipoLesion=?, " +
													    "tipoVehiculo=?, " +
													    "condicionLesionado=?, " +
													    "tipoViolencia=?, " +
													    "agresor=?, " +
													    "denunciado=?, " +
													    "consumoAlcohol=?, " +
													    "consumoOtrasSustancias=?, " +
													    "impresionDiagnostica=?, " +
													    "gravedadLesion=?, " +
													    "institucionAtendio=?, " +
													    "fueraBogota=? " +
													"WHERE codigofichalesiones=? ";
    
    
    
    public static final String consultarFichaStr = "SELECT " +
														"ficha.estado," +
														"ficha.estadoIngreso," +
												        "ficha.fechaOcurrencia," +
												        "ficha.horaOcurrencia," +
												        "ficha.lugarOcurrencia," +
												        "ficha.localidad," +
												        "ficha.municipioOcurrencia," +
												        "ficha.departamentoOcurrencia," +
												        "ficha.armaFuego," +
												        "ficha.armaCortopunzante," +
												        "ficha.armaContundente," +
												        "ficha.asfixia," +
												        "ficha.intoxicacion," +
												        "ficha.inmersion," +
												        "ficha.explosivo," +
												        "ficha.polvora," +
												        "ficha.otracausa," +
												        "ficha.caida," +
												        "ficha.mordedura," +
												        "ficha.vivienda," +
												        "ficha.lugarTrabajo," +
												        "ficha.lugarEstudio," +
												        "ficha.taberna," +
												        "ficha.establecimientoPublico," +
												        "ficha.viaPublica," +
												        "ficha.otroLugar," +
												        "ficha.lesionAccidenteTrabajo," +
												        "ficha.codigoArp," +
												        "ficha.actividadDuranteHecho," +
												        "ficha.lesionIntencional," +
												        "ficha.tipoLesion," +
												        "ficha.tipoVehiculo," +
												        "ficha.condicionLesionado," +
												        "ficha.tipoViolencia," +
												        "ficha.agresor," +
												        "ficha.denunciado," +
												        "ficha.consumoAlcohol," +
												        "ficha.consumoOtrasSustancias," +
												        "ficha.impresionDiagnostica," +
												        "ficha.gravedadLesion," +
												        "ficha.fueraBogota, " +
														
														"ficha.institucionAtendio AS nombreUnidad, " +
														"ficha.nombreprofesionaldiligencio AS nombreProfesional, " +
														"ficha.fechaDiligenciamiento AS fechaDiligenciamiento, " +
														"ficha.horaDiligenciamiento AS horaDiligenciamiento, " +
														"ficha.acronimo AS acronimo, " +
														"diag.nombre AS nombreDiagnostico, " +
														
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
														"pac.estudio AS escolaridad, " +
														"ocup.nombre AS ocupacionPaciente, " +
														"per.tipo_identificacion AS tipoId, " +
														"conv.nombre AS aseguradora, " +
														"regs.nombre AS regimenSalud, " +
														"pac.etnia AS etnia, " +
														"ficha.desplazado AS desplazado " +
													"FROM " +
														"epidemiologia.vigifichalesiones ficha," +
														"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
														"usuarios usu, personas per2, barrios bar, pacientes pac, ocupaciones ocup," +
														"convenios conv, tipos_regimen regs, diagnosticos diag " +
													"WHERE " +
														"ficha.codigoFichaLesiones = ? " +
														
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
														"AND conv.tipo_regimen=regs.acronimo " +
														"AND ficha.acronimo = diag.acronimo ";

    
    
    
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
															"pac.etnia AS etnia " +
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
			
			insertarFicha.setInt(9,0);
			insertarFicha.setInt(10,0);
			insertarFicha.setInt(11,0);
			insertarFicha.setInt(12,0);
			insertarFicha.setInt(13,0);
			insertarFicha.setInt(14,0);
			insertarFicha.setInt(15,0);
			insertarFicha.setInt(16,0);
			insertarFicha.setInt(17,0);
			insertarFicha.setInt(18,0);
			insertarFicha.setInt(19,0);
			insertarFicha.setInt(20,0);
			insertarFicha.setInt(21,0);
			insertarFicha.setInt(22,0);
			insertarFicha.setInt(23,0);
			insertarFicha.setInt(24,0);
			insertarFicha.setInt(25,0);
			insertarFicha.setInt(26,0);
			insertarFicha.setInt(27,0);
			insertarFicha.setInt(28,0);
			insertarFicha.setInt(29,0);
			insertarFicha.setInt(30,0);
			insertarFicha.setInt(31,0);
			
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaLesionesDao "+sqle.toString() );
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
										    
										    int estadoIngreso,
										    String fechaOcurrencia,
										    int horaOcurrencia,
										    String lugarOcurrencia,
										    int localidad,
										    String municipioOcurrencia,
										    String departamentoOcurrencia,
										    int armaFuego,
										    int armaCortopunzante,
										    int armaContundente,
										    int asfixia,
										    int intoxicacion,
										    int inmersion,
										    int explosivo,
										    int polvora,
										    int otracausa,
										    int caida,
										    int mordedura,
										    int vivienda,
										    int lugarTrabajo,
										    int lugarEstudio,
										    int taberna,
										    int establecimientoPublico,
										    int viaPublica,
										    int otroLugar,
										    int lesionAccidenteTrabajo,
										    int codigoArp,
										    int actividadDuranteHecho,
										    int lesionIntencional,
										    int tipoLesion,
										    int tipoVehiculo,
										    int condicionLesionado,
										    int tipoViolencia,
										    int agresor,
										    int denunciado,
										    int consumoAlcohol,
										    int consumoOtrasSustancias,
										    String impresionDiagnostica,
										    int gravedadLesion,
										    int unidadGeneradora,
										    int fueraBogota,
										    boolean activa
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
                        
            UtilidadBD.ingresarDatoAStatement(insertarFicha,login,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigo),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoPaciente),3,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estado),4,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,codigoDiagnostico,5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoAseguradora),6,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,nombreProfesional,7,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(estadoIngreso),8,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaOcurrencia,9,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(horaOcurrencia),10,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,lugarOcurrencia,11,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(localidad),12,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,municipioOcurrencia,13,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,departamentoOcurrencia,14,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(armaFuego),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(armaCortopunzante),16,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(armaContundente),17,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(asfixia),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(intoxicacion),19,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(inmersion),20,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(explosivo),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(polvora),22,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(otracausa),23,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(caida),24,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(mordedura),25,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vivienda),26,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(lugarTrabajo),27,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(lugarEstudio),28,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(taberna),29,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(establecimientoPublico),30,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(viaPublica),31,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(otroLugar),32,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(lesionAccidenteTrabajo),33,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(codigoArp),34,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(actividadDuranteHecho),35,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(lesionIntencional),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoLesion),37,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoVehiculo),38,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(condicionLesionado),39,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoViolencia),40,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(agresor),41,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(denunciado),42,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(consumoAlcohol),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(consumoOtrasSustancias),44,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,impresionDiagnostica,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(gravedadLesion),46,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),47,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(fueraBogota),48,Types.INTEGER,true,false);
            
            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),49,Types.INTEGER,false,true);
            
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaSarampionDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    
    public static int modificarFicha(Connection con,
										String sire,
										boolean notificar,
									    String loginUsuario,
									    int codigoFichaLesiones,
									    int codigoPaciente,
									    String codigoDiagnostico,
									    int codigoNotificacion,
									    int numeroSolicitud,
									    int estado,
									    
									    int estadoIngreso,
									    String fechaOcurrencia,
									    int horaOcurrencia,
									    String lugarOcurrencia,
									    int localidad,
									    String municipioOcurrencia,
									    String departamentoOcurrencia,
									    int armaFuego,
									    int armaCortopunzante,
									    int armaContundente,
									    int asfixia,
									    int intoxicacion,
									    int inmersion,
									    int explosivo,
									    int polvora,
									    int otracausa,
									    int caida,
									    int mordedura,
									    int vivienda,
									    int lugarTrabajo,
									    int lugarEstudio,
									    int taberna,
									    int establecimientoPublico,
									    int viaPublica,
									    int otroLugar,
									    int lesionAccidenteTrabajo,
									    int codigoArp,
									    int actividadDuranteHecho,
									    int lesionIntencional,
									    int tipoLesion,
									    int tipoVehiculo,
									    int condicionLesionado,
									    int tipoViolencia,
									    int agresor,
									    int denunciado,
									    int consumoAlcohol,
									    int consumoOtrasSustancias,
									    String impresionDiagnostica,
									    int gravedadLesion,
									    int unidadGeneradora,
									    int fueraBogota
									    )
    {
    	int resultado=0;
		int codigo=0;
		int codigoNot=0;
		
		try {
			DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
			daoFactory.beginTransaction(con);
					
			//********************************************************
			// Insercion de la ficha de Lesiones por Causa Externa
			
			PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
		    UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),1,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estadoIngreso),2,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaOcurrencia,3,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(horaOcurrencia),4,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,lugarOcurrencia,5,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(localidad),6,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,municipioOcurrencia,7,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,departamentoOcurrencia,8,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(armaFuego),9,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(armaCortopunzante),10,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(armaContundente),11,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(asfixia),12,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(intoxicacion),13,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(inmersion),14,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(explosivo),15,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(polvora),16,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(otracausa),17,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(caida),18,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(mordedura),19,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vivienda),20,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarTrabajo),21,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lugarEstudio),22,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(taberna),23,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(establecimientoPublico),24,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(viaPublica),25,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(otroLugar),26,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lesionAccidenteTrabajo),27,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoArp),28,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(actividadDuranteHecho),29,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(lesionIntencional),30,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoLesion),31,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoVehiculo),32,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(condicionLesionado),33,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoViolencia),34,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(agresor),35,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(denunciado),36,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(consumoAlcohol),37,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(consumoOtrasSustancias),38,Types.NUMERIC,true,true);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,impresionDiagnostica,39,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(gravedadLesion),40,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),41,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(fueraBogota),42,Types.NUMERIC,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaLesiones),43,Types.INTEGER,true,false);
			
			resultado = modificarFicha.executeUpdate();
			
			daoFactory.endTransaction(con);
		}
		catch (SQLException sqle)
		{
			logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaLesionesDao "+sqle.toString() );
			resultado=0;
		}
		
		return resultado;
    }
    
    
    
    public static ResultSet consultarTodoFichaLesiones(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Lesiones por Causa Externa "+sqle);
			return null;
		}
    }
    
    
    
    public static ResultSet consultarDatosPaciente(Connection con, int codigo) {
    	
    	try {
    		
    		PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaDatosPacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		consulta.setInt(1,codigo);
    		
    		return consulta.executeQuery();
    	}
    	catch (SQLException sqle) {
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaSarampionDao) "+sqle);
			return null;
    	}
    }
}
