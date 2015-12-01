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

public class SqlBaseFichaDengueDao {

	/**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaDengueDao.class);
    
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaStr="INSERT INTO epidemiologia.vigifichadengue "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaDengue," +
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
    
    
    public static final String modificarFichaStr = "UPDATE epidemiologia.vigifichadengue " +
														"SET " +
														"sire=?, " +
														"estado=?, " +
														"vacunaFiebreAmarilla=?, " +
														"fechaApliVacunaFiebre=?, " +
														"vacunaHepatitisBDosis1=?, " +
														"vacunaHepatitisBDosis2=?, " +
														"vacunaHepatitisBDosis3=?, " +
														"fechaVacunaHepaDosis1=?, " +
														"fechaVacunaHepaDosis2=?, " +
														"fechaVacunaHepaDosis3=?, " +
														"vacunaHepatitisADosis1=?, " +
														"fechaVacunaHepatADosis1=?, " +
														"observaciones=?, " +
														"desplazamiento=?, " +
														"fechaDesplazamiento=?, " +
														"codigoMunicipio=?, " +
														"codigoDepartamento=?, " +
														"casoFiebreAmarilla=?, " +
														"casoEpizootia=?, " +
														"direccionSitio=?, " +
														"presenciaAedes=?, " +
														
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
														"institucionAtendio=?," +
														"pais=?, " +
														"areaProcedencia=? " +
													"WHERE codigoFichaDengue=? ";
    
    
    public static final String ingresarDatoLaboratorio = "INSERT INTO epidemiologia.vigilaboratoriodengue " +
																"(" +
																"codigofichadengue," +
																"codigolaboratorio" +
																") " +
															"VALUES (?,?)";


    public static final String modificarDatoLaboratorio = "UPDATE epidemiologia.vigilaboratoriodengue " +
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
    
    private static final String insertarHallazgo = "INSERT INTO epidemiologia.vigiDetalleHallazgos(codigohallazgo,codigofichadengue) VALUES(?,?)";
    
    private static final String eliminarHallazgo = "DELETE from epidemiologia.vigiDetalleHallazgos WHERE codigoFichaDengue = ?";
    
    
    
    
    private static final String consultarFichaDengue = "SELECT " +
    														"ficha.sire, " +
    														"ficha.estado, " +
    														"ficha.vacunaFiebreAmarilla, " +
    														"ficha.fechaApliVacunaFiebre, " +
    														"ficha.vacunaHepatitisBDosis1, " +
    														"ficha.vacunaHepatitisBDosis2, " +
    														"ficha.vacunaHepatitisBDosis3, " +
    														"ficha.fechaVacunaHepaDosis1, " +
    														"ficha.fechaVacunaHepaDosis2, " +
    														"ficha.fechaVacunaHepaDosis3, " +
    														"ficha.vacunaHepatitisADosis1, " +
    														"ficha.fechaVacunaHepatADosis1, " +
    														"ficha.observaciones, " +
    														"ficha.desplazamiento, " +
    														"ficha.fechaDesplazamiento, " +
    														"ficha.codigoMunicipio, " +
    														"ficha.codigoDepartamento, " +
    														"ficha.casoFiebreAmarilla, " +
    														"ficha.casoEpizootia, " +
    														"ficha.direccionSitio, " +
    														"ficha.presenciaAedes, " +
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
		    												"pac.etnia AS etnia, " +
		    											//	"ficha.desplazado AS desplazado " +
		    												"pac.grupo_poblacional as grupoPoblacional " +
		    												
		    											"FROM epidemiologia.vigifichadengue ficha " +
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
															"ficha.codigoFichaDengue = ? ";
    
    
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
														//	"vigiLaboratorioDengue " +
														  	"epidemiologia.vigifichalaboratorios " +
														  "WHERE " +
														//	"codigoFichaDengue=?";
														  	"codigoFicha=?";
														  	
    
    
    public static final String consultaHallazgos = "SELECT " +
														"codigoHallazgo " +
													"FROM " +
														"epidemiologia.vigiDetalleHallazgos " +
													"WHERE " +
														"codigoFichaDengue = ? ";
    
    
    
    
    private static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigifichadengue "+
															"(" +
															"loginUsuario,"+
															"codigoFichaDengue," +
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
															"vacunaFiebreAmarilla, " +
															"fechaApliVacunaFiebre, " +
															"vacunaHepatitisBDosis1, " +
															"vacunaHepatitisBDosis2, " +
															"vacunaHepatitisBDosis3, " +
															"fechaVacunaHepaDosis1, " +
															"fechaVacunaHepaDosis2, " +
															"fechaVacunaHepaDosis3, " +
															"vacunaHepatitisADosis1, " +
															"fechaVacunaHepatADosis1, " +
															"observaciones, " +
															"desplazamiento, " +
															"fechaDesplazamiento, " +
															"codigoMunicipio, " +
															"codigoDepartamento, " +
															"casoFiebreAmarilla, " +
															"casoEpizootia, " +
															"direccionSitio, " +
															"presenciaAedes, " +
															
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
															"VALUES (?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?) ";
    
    
    
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
    

    
    
    
    private static final String terminarFichaStr = "UPDATE epidemiologia.vigiFichaDengue SET estado = 2 WHERE codigoFichaDengue=?";
    
    
    
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
            
            for (int i=1;i<6;i++) {
            	
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
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaDengueDao "+sqle.toString() );
		    resultado=0;
        }
        
        return resultado;
    }
    
    
    
    
    public static int modificarFicha(Connection con,
										String sire,
									    String loginUsuario,
									    int codigoFichaDengue,
									    int estado,
									    
									    int vacunaFiebreAmarilla,
										String fechaAplicacionVacunaFiebre,
										int vacunaHepatitisBDosis1,
										int vacunaHepatitisBDosis2,
										int vacunaHepatitisBDosis3,
										String fechaVacunaHepaDosis1,
										String fechaVacunaHepaDosis2,
										String fechaVacunaHepaDosis3,
										int vacunaHepatitisADosis1,
										String fechaVacunaHepatADosis1,
										String observaciones,
										boolean desplazamiento,
										String fechaDesplazamiento,
										String lugarDesplazamiento,
										String codigoMunicipio,
										String codigoDepartamento,
										int casoFiebreAmarilla,
										int casoEpizootia,
										String direccionSitio,
										int presenciaAedes,
										HashMap hallazgosSemiologicos,
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
            
            String muniDesplazamiento = lugarDesplazamiento.split("-")[0];
            String depDesplazamiento = lugarDesplazamiento.split("-")[1];
            
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNoti.split("-")[0];
            String codigoDepNoti = lugarNoti.split("-")[1];
            
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaFiebreAmarilla),3,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaAplicacionVacunaFiebre,4,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaHepatitisBDosis1),5,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaHepatitisBDosis2),6,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaHepatitisBDosis3),7,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVacunaHepaDosis1,8,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVacunaHepaDosis2,9,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVacunaHepaDosis3,10,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaHepatitisADosis1),11,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaVacunaHepatADosis1,12,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,observaciones,13,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(desplazamiento),14,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDesplazamiento,15,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,muniDesplazamiento,16,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,depDesplazamiento,17,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(casoFiebreAmarilla),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(casoEpizootia),19,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,direccionSitio,20,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(presenciaAedes),21,Types.INTEGER,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,22,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,23,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,25,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),26,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),27,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,28,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),29,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,31,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),33,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,34,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),35,Types.INTEGER,true,false);
                        
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaDengue),36,Types.INTEGER,true,false);
            
            result = modificarFicha.executeUpdate();
            
            if(result<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            /*
            PreparedStatementDecorator eliminarHallazgos =  new PreparedStatementDecorator(con.prepareStatement(eliminarHallazgo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            eliminarHallazgos.setInt(1,codigoFichaDengue);
		    result = eliminarHallazgos.executeUpdate();
		    
		    
		    if(result<0)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
		    */
            
            		    
            try {
			    if (hallazgosSemiologicos.size()>0 && estadoAnterior==1) {
			    
				    for (int i=1;i<hallazgosSemiologicos.size()+1;i++) {
				    	
				    	String val = hallazgosSemiologicos.get("hallazgo_"+i).toString();
		
				    	if (val.equals("true")) {
					    	PreparedStatementDecorator insertarHallazgos =  new PreparedStatementDecorator(con.prepareStatement(insertarHallazgo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					    	
					    	insertarHallazgos.setInt(1,i);
					    	insertarHallazgos.setInt(2,codigoFichaDengue);
					    	
					    	result = insertarHallazgos.executeUpdate();
					    	
					    	if(result<1)
				            {
				                daoFactory.abortTransaction(con);
				                return -1; // Estado de error
				            }
				    	}
				    }
			    }
            }
            catch (NullPointerException npe) {}
		    
		    
		    if (datosLaboratorio.size()>0) {
		    
			    PreparedStatementDecorator modificarDatosLaboratorio =  new PreparedStatementDecorator(con.prepareStatement(modificarDatoLaboratorio,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	            		    
	            for (int i=1;i<6;i++) {
	            	
	            	String codigoLaboratorio = Integer.toString(codigoFichaDengue) + Integer.toString(i);
	            	
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
		    
		    
		    daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaDengueDao "+sqle.toString() );
		    result=0;
        }
        
        return result;
    }
    
    
    
    public static ResultSet consultarTodoFichaDengue(Connection con, int codigo) {
    	
    	try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultarFichaDengue,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de Dengue "+sqle);
			return null;
		}
    }
    
    
    
    
    
    
    public static ResultSet consultarHallazgos(Connection con, int codigo)
    {
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaHallazgos,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando los hallazgos semiologicos (ficha de Dengue) "+sqle);
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
            
            logger.error("Error consultando los datos de laboratorio (ficha de dengue) "+sqle);
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaDengueDao) "+sqle);
			return null;
    	}
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
										    
										    int vacunaFiebreAmarilla,
											String fechaAplicacionVacunaFiebre,
											int vacunaHepatitisBDosis1,
											int vacunaHepatitisBDosis2,
											int vacunaHepatitisBDosis3,
											String fechaVacunaHepaDosis1,
											String fechaVacunaHepaDosis2,
											String fechaVacunaHepaDosis3,
											int vacunaHepatitisADosis1,
											String fechaVacunaHepatADosis1,
											String observaciones,
											boolean desplazamiento,
											String fechaDesplazamiento,
											String lugarDesplazamiento,
											int casoFiebreAmarilla,
											int casoEpizootia,
											String direccionSitio,
											int presenciaAedes,
											
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
										    HashMap hallazgosSemiologicos,
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
            
            String muniDesplazamiento = lugarDesplazamiento.split("-")[0];
            String depDesplazamiento = lugarDesplazamiento.split("-")[1];
            
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaFiebreAmarilla),13,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaAplicacionVacunaFiebre,14,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaHepatitisBDosis1),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaHepatitisBDosis2),16,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaHepatitisBDosis3),17,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVacunaHepaDosis1,18,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVacunaHepaDosis2,19,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVacunaHepaDosis3,20,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaHepatitisADosis1),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaVacunaHepatADosis1,22,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,observaciones,23,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(desplazamiento),24,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDesplazamiento,25,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,muniDesplazamiento,26,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,depDesplazamiento,27,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(casoFiebreAmarilla),28,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(casoEpizootia),29,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,direccionSitio,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(presenciaAedes),31,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaConsultaGeneral,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaInicioSintomasGeneral,33,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoCaso),34,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(hospitalizadoGeneral),35,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaHospitalizacionGeneral,36,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(estaVivoGeneral),37,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaDefuncion,38,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(unidadGeneradora),39,Types.INTEGER,true,false);
            
            int valorActiva = 0;
            
            if (activa) {
            	valorActiva = 1;
            }
            
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(valorActiva),40,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,41,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),42,Types.INTEGER,true,false);
            
            resultado = insertarFicha.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }   
            
            
            
            
            try {
			    if (hallazgosSemiologicos.size()>0 && estadoAnterior==1) {
			    
				    for (int i=1;i<hallazgosSemiologicos.size()+1;i++) {
				    	
				    	String val = hallazgosSemiologicos.get("hallazgo_"+i).toString();
		
				    	if (val.equals("true")) {
					    	PreparedStatementDecorator insertarHallazgos =  new PreparedStatementDecorator(con.prepareStatement(insertarHallazgo,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
					    	
					    	insertarHallazgos.setInt(1,i);
					    	insertarHallazgos.setInt(2,codigo);
					    	
					    	resultado = insertarHallazgos.executeUpdate();
					    	
					    	if(resultado<1)
				            {
				                daoFactory.abortTransaction(con);
				                return -1; // Estado de error
				            }
					    	else {
							    
							    resultado = codigo;
							}
				    	}
				    }
			    }
            }
            catch (NullPointerException npe) {}
		    
            
            
            daoFactory.endTransaction(con);
        }
        catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaDengueDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
}
