/*
 * Creado en 10-ago-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.dao.sqlbase;

import java.sql.Connection;
import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import java.sql.SQLException;
import com.princetonsa.decorator.PreparedStatementDecorator;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;

/**
 * @author santiago
 *
 */
public class SqlBaseFichaRabiaDao {

    /**
     * Objeto para manejar los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseFichaRabiaDao.class);
    
    /**
     * String con el statement para insertar una ficha de Rabia
     */
    private static final String insertarFichaRabiaStr="INSERT INTO epidemiologia.vigificharabia "+
    														"(" +
    														"numeroSolicitud," +
    														"loginUsuario,"+
    														"codigoFichaRabia," +
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
    														"nombreprofesionaldiligencio)"+
    														" VALUES(?,?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",'21','11','21','11',?,?)";
    
    
    /**
     * String con el statement para insertar una notificacion
     */
    private static final String insertarNotificacionStr = "INSERT INTO epidemiologia.vigiNotificacion "+
    															"("+
    															"codigoNotificacion,"+
    															"login,"+
    															"fecha,"+
    															"hora,"+
    															"tipo"+
    															") "+
    														"VALUES(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?)";
    
    /**
     * String con el statement para insertar una Localizacion Anatomica de una agresion de rabia
     */
    private static final String insertarLocalizacionAnatomicaStr="INSERT INTO epidemiologia.vigiDetalleLocAnatomica "+
    																	"("+
    																	"codigoLocalizacionAnatomica,"+
    																	"codigoFichaRabia"+
    																	") "+
    															 "VALUES (?,?)";
    
    
    /**
     * String con el statement para insertar un Animal (Ficha de Rabia)
     */
    private static final String insertarAnimalStr = "INSERT INTO epidemiologia.vigianimal "+
    													"("+
    													"codigoFichaRabia "+
    													") "+
    												"VALUES (?)";
    
    
    /**
     * String con el statement necesario para insertar un tratamiento antirrabico
     */
    public static final String insertarTratamientoStr = "INSERT INTO epidemiologia.vigitratantirrabico "+
	    													"("+
	    													"codigoFichaRabia "+
	    													") "+
	    												"VALUES (?)";

    
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
															"pac.etnia AS etnia," +
															"pac.grupo_poblacional as grupoPoblacional " +
														"FROM personas per " +
														"INNER JOIN pacientes pac ON(pac.codigo_paciente=per.codigo) " +
														"INNER JOIN departamentos dep ON(dep.codigo_departamento=per.codigo_departamento_vivienda and dep.codigo_pais=per.codigo_pais_vivienda)  " +
														"INNER JOIN ciudades ciu ON(ciu.codigo_ciudad=per.codigo_ciudad_vivienda and ciu.codigo_departamento=per.codigo_departamento_vivienda AND ciu.codigo_pais=per.codigo_pais_vivienda)  " +
														"INNER JOIN barrios bar ON(bar.codigo=per.codigo_barrio_vivienda) " +
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
    
    
    
    /**
     * String con el statement para consultar una Ficha de Rabia
     */
    public static final String consultaTodoFichaRabiaStr = "SELECT " +
															"ficha.sire,"+
    														"ficha.estado,"+
														    "ficha.sueroAntirrabico,"+
															"ficha.tipoSuero,"+
															"ficha.cantidadAplicada,"+
															"ficha.fechaAplicacion,"+
															"ficha.vacunaAntirrabica,"+
															"ficha.tipoVacuna,"+
															"ficha.dosisAplicadas,"+
															"ficha.fechaUltimaDosis,"+
															"ficha.tipoAgresion," +
															"ficha.provocada,"+
															"ficha.tipoLesion," +
															"ficha.cabeza," +
															"ficha.manos," +
															"ficha.tronco," +
															"ficha.extresuperiores," +
															"ficha.extreinferiores,"+
															"ficha.tipoExposicion,"+
															"ficha.fechaAgresion," +
															"ficha.fechaDiligenciamiento," +
															"ficha.horaDiligenciamiento, " +
															"ficha.profundidadLesion, " +
															"ficha.confDiagnosticaCasoRabia, " +
															"ficha.fechaMuestraCasoRabia, " +
															"ficha.pais, " +
															"ficha.areaProcedencia " +
															
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
															"ficha.nombreprofesionaldiligencio AS nombreProfesional, "+
															
															"ani.especie," +
    												   		"ani.informacionlaboratorio," +
    												   		"ani.vacunado," +
    												   		"ani.fechaultimadosisanimal, " +
    												   		"ani.fechainiciosintomas," +
    												   		"ani.fechamuerte," +
    												   		"ani.fechatomamuestra," +
    												   		"ani.nombrepropietario," +
	    													"ani.direccionpropietario," +
	    													"ani.estadomomentoagresion," +
	    													"ani.ubicacionanimal," +
	    													"ani.numerodiasobserva," +
	    													"ani.lugarobservacion," +
	    													"ani.estadoanimalobserva," +
	    													"ani.confirmaciondiagnostica AS confirmacionanimal, " +
    												   		
    												   		"trat.lavadoherida," +
															"trat.suturaherida," +
															"trat.aplicacionsuero," +
															"trat.fechaaplicacionsuero," +
															"trat.tiposuerotratamiento," +
															"trat.cantidadsueroglutea," +
															"trat.cantidadsueroherida," +
															"trat.numerolote," +
															"trat.laboratorioproductor," +
															"trat.aplicarvacuna," +
															"trat.numerodosistratamiento," +
															"trat.tipovacunatratamiento," +
															"trat.fechavacunadosis1," +
															"trat.fechavacunadosis2," +
															"trat.fechavacunadosis3," +
															"trat.fechavacunadosis4," +
															"trat.fechavacunadosis5," +
															"trat.suspensiontratamiento," +
															"trat.razonsuspension," +
															"trat.fechatomamuestramuerte," +
															"trat.confirmaciondiagnostica," +
															"trat.pruebaslaboratorio," +
															"trat.reaccionesvacunasuero," +
															"trat.evolucionpaciente," +
															
															
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
		    												"pac.grupo_poblacional as grupoPoblacional " +
		    									//			"ficha.desplazado AS desplazado " +
		    												
		    									/*			
		    												"per2.primer_nombre AS primerNombreUsuario, " +
		    												"per2.segundo_nombre AS segundoNombreUsuario, " +
		    												"per2.primer_apellido AS primerApellidoUsuario, " +
		    												"per2.segundo_apellido AS segundoApellidoUsuario, " +
		    												"per2.numero_identificacion AS identificacionUsuario " +
												*/			
														"FROM epidemiologia.vigificharabia ficha,epidemiologia.vigianimal ani,epidemiologia.vigitratantirrabico trat, " +
															"personas per, departamentos dep, ciudades ciu, departamentos dep2, ciudades ciu2, " +
															"usuarios usu, barrios bar, pacientes pac, ocupaciones ocup," +
															"convenios conv, tipos_regimen regs " +
														"WHERE ficha.codigoFichaRabia = ? " +
															"AND ani.codigoFichaRabia = ? " +
															"AND trat.codigoFichaRabia = ? " +
															
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
				    										"AND per.codigo_barrio_vivienda=bar.codigo " +
				    										"AND per.codigo=pac.codigo_paciente " +
				    										"AND pac.codigo_paciente=ficha.codigoPaciente " +
				    										"AND pac.ocupacion=ocup.codigo "+
				    										"AND conv.codigo=ficha.codigoAseguradora " +
				    										"AND conv.tipo_regimen=regs.acronimo ";
    
    
    public static final String consultaLocalizacionAnatomicaStr="SELECT " +
    																"codigolocalizacionanatomica " +
    															"FROM epidemiologia.vigidetallelocanatomica " +
    															"WHERE codigoFichaRabia=?";
    
    
    
    public static final String insertarFichaCompletaStr="INSERT INTO epidemiologia.vigificharabia "+
															"(" +
															"loginUsuario,"+
															"codigoFichaRabia," +
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
    														"sueroAntirrabico, " +
    														"tipoSuero, " +
    														"cantidadAplicada, " +
    														"fechaAplicacion, " +
    														"vacunaAntirrabica, " +
    														"tipoVacuna, " +
    														"dosisAplicadas, " +
    														"fechaUltimaDosis, " +
    														"tipoAgresion, " +
    														"provocada, " +
    														"tipoLesion, " +
    														"tipoExposicion, " +
    														"cabeza, " +
    														"cara, " +
    														"cuello, " +
    														"manos, " +
    														"tronco, " +
    														"extresuperiores, " +
    														"extreinferiores, " +
    														"fechaAgresion," +
    														
    														"fechaConsultaGeneral, " +
    														"fechaInicioSintomasGeneral, " +
    														"tipoCaso, " +
    														"hospitalizadoGeneral, " +
    														"fechaHospitalizacionGeneral, " +
    														"estaVivoGeneral, " +
    														"fechaDefuncion, " +
    														"institucionAtendio," +
    														"activa," +
    														"profundidadLesion," +
    														"confDiagnosticaCasoRabia," +
    														"fechaMuestraCasoRabia, " +
    														"pais, " +
															"areaProcedencia " +
															") " +
															
														"VALUES (" +
															"?,?,?,?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?" +
														")";
    
    
    
    /**
     * String con el statement para insertar un Animal (Ficha de Rabia)
     */
    private static final String insertarAnimalCompletoStr = "INSERT INTO epidemiologia.vigianimal "+
		    													"("+
		    													"codigoFichaRabia, "+
		    													"especie, " +
    	    													"fechaInicioSintomas, " +
    	    													"fechaMuerte, " +
    	    													"fechaTomaMuestra, " +
    	    													"informacionLaboratorio, " +
    	    													"vacunado, " +
    	    													"fechaUltimaDosisAnimal," +
    	    													"nombrepropietario," +
    	    													"direccionpropietario," +
    	    													"estadomomentoagresion," +
    	    													"ubicacionanimal," +
    	    													"numerodiasobserva," +
    	    													"lugarobservacion," +
    	    													"estadoanimalobserva," +
    	    													"confirmaciondiagnostica " +
		    													") "+
		    												"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    
    /**
     * String con el statement necesario para insertar un tratamiento antirrabico
     */
    public static final String insertarTratamientoCompletoStr = "INSERT INTO epidemiologia.vigitratantirrabico "+
			    													"("+
			    													"codigoFichaRabia, "+
			    													"lavadoHerida, " +
	    															"suturaHerida, " +
	    															"aplicacionSuero, " +
	    															"fechaAplicacionSuero, " +
	    															"tipoSueroTratamiento, " +
	    															"cantidadSueroGlutea, " +
	    															"cantidadSueroHerida, " +
	    															"numeroLote, " +
	    															"laboratorioProductor, " +
	    															"aplicarVacuna, " +
	    															"numeroDosisTratamiento, " +
	    															"tipoVacunaTratamiento, " +
	    															"fechaVacunaDosis1, " +
	    															"fechaVacunaDosis2, " +
	    															"fechaVacunaDosis3, " +
	    															"fechaVacunaDosis4, " +
	    															"fechaVacunaDosis5, " +
	    															"suspensionTratamiento, " +
	    															"razonSuspension, " +
	    															"fechaTomaMuestraMuerte, " +
	    															"confirmacionDiagnostica, " +
	    															"pruebasLaboratorio," +
	    															"reaccionesVacunaSuero," +
	    															"evolucionPaciente "+
			    													") "+
			    												"VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    
    
    public static final String modificarFichaRabiaStr="UPDATE epidemiologia.vigiFichaRabia " +
    														"SET loginUsuario=?, " +
    														"sire=?, " +
    														"estado=?, "+
    														"sueroAntirrabico=?, " +
    														"tipoSuero=?, " +
    														"cantidadAplicada=?, " +
    														"fechaAplicacion=?, " +
    														"vacunaAntirrabica=?, " +
    														"tipoVacuna=?, " +
    														"dosisAplicadas=?, " +
    														"fechaUltimaDosis=?, " +
    														"tipoAgresion=?, " +
    														"provocada=?, " +
    														"tipoLesion=?, " +
    														"cabeza=?, " +
    														"cara=?, " +
    														"cuello=?, " +
    														"manos=?, " +
    														"tronco=?, " +
    														"extresuperiores=?, " +
    														"extreinferiores=?, " +
    														"tipoExposicion=?, " +
    														"fechaAgresion=?," +
    														
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
    														"profundidadLesion=?," +
    														"confDiagnosticaCasoRabia=?," +
    														"fechaMuestraCasoRabia=?, " +
    														"pais=?, " +
															"areaProcedencia=? " +
    													"WHERE codigoFichaRabia=?";
    
    
    public static final String notificarFichaStr="UPDATE epidemiologia.vigiFichaRabia " +
    												"SET codigoNotificacion=? " +
    											"WHERE codigoFichaRabia=?";
    
    
    public static final String modificarAnimalStr="UPDATE epidemiologia.vigiAnimal " +
    												"SET especie=?, " +
    													"fechaInicioSintomas=?, " +
    													"fechaMuerte=?, " +
    													"fechaTomaMuestra=?, " +
    													"informacionLaboratorio=?, " +
    													"vacunado=?, " +
    													"fechaUltimaDosisAnimal=?, " +
    													"nombrepropietario=?, " +
    													"direccionpropietario=?, " +
    													"estadomomentoagresion=?, " +
    													"ubicacionanimal=?, " +
    													"numerodiasobserva=?, " +
    													"lugarobservacion=?, " +
    													"estadoanimalobserva=?, " +
    													"confirmaciondiagnostica=? " +
    												"WHERE codigoFichaRabia=?";
    
    												    													
    
    											
    public static final String modificarTratamientoStr="UPDATE epidemiologia.vigiTratAntirrabico " +
    														"SET " +
    															"lavadoHerida=?, " +
    															"suturaHerida=?, " +
    															"aplicacionSuero=?, " +
    															"fechaAplicacionSuero=?, " +
    															"tipoSueroTratamiento=?, " +
    															"cantidadSueroGlutea=?, " +
    															"cantidadSueroHerida=?, " +
    															"numeroLote=?, " +
    															"laboratorioProductor=?, " +
    															"aplicarVacuna=?, " +
    															"numeroDosisTratamiento=?, " +
    															"tipoVacunaTratamiento=?, " +
    															"fechaVacunaDosis1=?, " +
    															"fechaVacunaDosis2=?, " +
    															"fechaVacunaDosis3=?, " +
    															"fechaVacunaDosis4=?, " +
    															"fechaVacunaDosis5=?, " +
    															"suspensionTratamiento=?, " +
    															"razonSuspension=?, " +
    															"fechaTomaMuestraMuerte=?, " +
    															"confirmacionDiagnostica=?, " +
    															"pruebasLaboratorio=?," +
    															"reaccionesVacunaSuero=?," +
    															"evolucionPaciente=? "+
    														"WHERE codigoFichaRabia=?";
    
    														
    														
    
    private static final String terminarFichaStr = "UPDATE epidemiologia.vigiFichaRabia SET estado = 2 WHERE codigoFichaRabia=?";
    
    
    
    
    
    /**
     * Metodo para insertar una ficha de un caso de accidente rabico
     * @param con
     * @param numeroSolicitud
     * @param login
     * @param codigoPaciente
     * @param codigoDiagnostico
     * @param estado
     * @param secuencia
     * @return
     */
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
            
            PreparedStatementDecorator insertarFicha =  new PreparedStatementDecorator(con.prepareStatement(insertarFichaRabiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           
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
            
            //*************************************************************
            // Insercion de los datos del Animal Agresor
            
            PreparedStatementDecorator insertarAnimal =  new PreparedStatementDecorator(con.prepareStatement(insertarAnimalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
            insertarAnimal.setInt(1,codigo);
			resultado = insertarAnimal.executeUpdate();
			
			if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
			
			
			//*************************************************************
			// Insercion de los datos del Tratamiento Antirrabico
			
			PreparedStatementDecorator insertar =  new PreparedStatementDecorator(con.prepareStatement(insertarTratamientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			insertar.setInt(1,codigo);      
			resultado = insertar.executeUpdate();
			
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaRabiaDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    
    
    public static int insertarFichaCompleta(Connection con,
								    		String login,
											int codigoPaciente,
											String codigoDiagnostico,
											int estado,
											int codigoAseguradora,
											String nombreProfesional,
										    
										    String secuencia,
										    
										    String sire,
											int sueroAntirrabico,
											int tipoSuero,
										    int cantidadAplicada,
										    String fechaAplicacion,
										    int vacunaAntirrabica,
										    int tipoVacuna,
										    int dosisAplicadas,
										    String fechaUltimaDosis,
										    int tipoAgresion,
										    boolean provocada,
										    int tipoLesion,
										    int tipoExposicion,
										    boolean cabeza,
										    boolean cara,
										    boolean cuello,
										    boolean manos,
										    boolean tronco,
										    boolean extsuperiores,
										    boolean extinferiores,
										    String fechaAgresion,
										    int confDiagnosticaCasoRabia,
										    String fechaMuestraCasoRabia,
										    String pais,
											int areaProcedencia,
										    
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
										    
											// Elementos del Animal Agresor        															    						   
										    int especie,
										    String fechaInicioSintomas,
										    String fechaMuerte,
										    String fechaTomaMuestra,
										    int fuenteInformacionLaboratorio,
										    boolean vacunado,
										    String fechaUltimaDosisAnimal,
										    String nombrePropietario,
										    String direccionPropietario,
										    int estadoMomentoAgresion,
										    int ubicacionAnimal,
										    int numeroDiasObserva,
										    int lugarObservacion,
										    int estadoAnimalObserva,
										    int confirmacionDiagnosticaAnimal,
										    		
										    // Elementos del Tratamiento Antirrabico
										    boolean lavadoHerida,
										    boolean suturaHerida,
										    boolean aplicacionSuero,
										    String fechaAplicacionSuero,
										    int tipoSueroTratamiento,
										    int cantidadSueroGlutea,
										    int cantidadSueroHerida,
										    String numeroLote,
										    String laboratorioProductor,
										    boolean aplicarVacuna,
										    int numeroDosisTratamiento,
										    int tipoVacunaTratamiento,
										    String fechaVacunaDosis1,
										    String fechaVacunaDosis2,
										    String fechaVacunaDosis3,
										    String fechaVacunaDosis4,
										    String fechaVacunaDosis5,
										    boolean suspensionTratamiento,
										    int razonSuspension,
										    String fechaTomaMuestraMuerte,
										    boolean confirmacionDiagnostica,
										    int pruebasLaboratorio,
										    boolean activa,
										    int profundidadLesion,
										    int reaccionesVacunaSuero,
										    int evolucionPaciente
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(sueroAntirrabico),13,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoSuero),14,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(cantidadAplicada),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaAplicacion,16,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(vacunaAntirrabica),17,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoVacuna),18,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(dosisAplicadas),19,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaUltimaDosis,20,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoAgresion),21,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(provocada),22,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoLesion),23,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(tipoExposicion),24,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(cabeza),25,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(cara),26,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(cuello),27,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(manos),28,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(tronco),29,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(extsuperiores),30,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Boolean.toString(extinferiores),31,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaAgresion,32,Types.VARCHAR,true,false);
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
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(profundidadLesion),42,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(confDiagnosticaCasoRabia),43,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,fechaMuestraCasoRabia,44,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,pais,45,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarFicha,Integer.toString(areaProcedencia),46,Types.INTEGER,true,false);
            
            resultado = insertarFicha.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }                      
            
            //*************************************************************
            // Insercion de los datos del Animal Agresor
            
            PreparedStatementDecorator insertarAnimal =  new PreparedStatementDecorator(con.prepareStatement(insertarAnimalCompletoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,Integer.toString(codigo),1,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,Integer.toString(especie),2,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,fechaInicioSintomas,3,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,fechaMuerte,4,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,fechaTomaMuestra,5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,Integer.toString(fuenteInformacionLaboratorio),6,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,Boolean.toString(vacunado),7,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,fechaUltimaDosisAnimal,8,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,nombrePropietario,9,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,direccionPropietario,10,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,Integer.toString(estadoMomentoAgresion),11,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,Integer.toString(ubicacionAnimal),12,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,Integer.toString(numeroDiasObserva),13,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,Integer.toString(lugarObservacion),14,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,Integer.toString(estadoAnimalObserva),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(insertarAnimal,Integer.toString(confirmacionDiagnosticaAnimal),16,Types.INTEGER,true,false);
            
			resultado = insertarAnimal.executeUpdate();
			
			if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
			
			//*************************************************************
			// Insercion de los datos del Tratamiento Antirrabico
			
			PreparedStatementDecorator insertarTratamiento =  new PreparedStatementDecorator(con.prepareStatement(insertarTratamientoCompletoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));			
			
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Integer.toString(codigo),1,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Boolean.toString(lavadoHerida),2,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Boolean.toString(suturaHerida),3,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Boolean.toString(aplicacionSuero),4,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,fechaAplicacionSuero,5,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Integer.toString(tipoSueroTratamiento),6,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Integer.toString(cantidadSueroGlutea),7,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Integer.toString(cantidadSueroHerida),8,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,numeroLote,9,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,laboratorioProductor,10,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Boolean.toString(aplicarVacuna),11,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Integer.toString(numeroDosisTratamiento),12,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Integer.toString(tipoVacunaTratamiento),13,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,fechaVacunaDosis1,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,fechaVacunaDosis2,15,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,fechaVacunaDosis3,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,fechaVacunaDosis4,17,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,fechaVacunaDosis5,18,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Boolean.toString(suspensionTratamiento),19,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Integer.toString(razonSuspension),20,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,fechaTomaMuestraMuerte,21,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Boolean.toString(confirmacionDiagnostica),22,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Integer.toString(pruebasLaboratorio),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Integer.toString(reaccionesVacunaSuero),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(insertarTratamiento,Integer.toString(evolucionPaciente),25,Types.INTEGER,true,false);
			
			
			resultado = insertarTratamiento.executeUpdate();
			
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
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseFichaRabiaDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
								    
    
 
    
    
    /**
     * Metodo para insertar una notificacion
     * @param con
     * @param codigoUsuario
     * @param tipo
     * @param secuencia
     * @return
     */
    public static int insertarNotificacion(Connection con,
            								int codigoUsuario,
            								String tipo,
            								String secuencia)
    {
        int resultado=0;
        int codigo;
        
        try {
            
            PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                codigo = rs.getInt(1);
            }
            else {
				logger.error("Error obteniendo el código de la secuencia ");
				return 0;
			}
            
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            PreparedStatementDecorator insertarNotificacion =  new PreparedStatementDecorator(con.prepareStatement(insertarNotificacionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
           
            // Inserción de los datos de la notificacion
            
            insertarNotificacion.setInt(1,codigo);
            insertarNotificacion.setInt(2,codigoUsuario);
            insertarNotificacion.setString(3,tipo);
            
            resultado = insertarNotificacion.executeUpdate();
            
            if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }
            
            daoFactory.endTransaction(con);
        }
        catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseNotificacionDao "+sqle.toString() );
		    resultado=0;			
		}
        
        return resultado;
    }
    
    
    
    
    public static int terminarFicha(Connection con, int codigoFichaRabia)
    {
    	int resultado=0;
    	
    	try {
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
            
            PreparedStatementDecorator terminarFicha =  new PreparedStatementDecorator(con.prepareStatement(terminarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            UtilidadBD.ingresarDatoAStatement(terminarFicha,Integer.toString(codigoFichaRabia),1,Types.VARCHAR,true,false);
            
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
										int codigoPaciente,
										int codigoFichaRabia,
										String loginUsuario,
										int estado,
										int sueroAntirrabico,
										int tipoSuero,
									    int cantidadAplicada,
									    String fechaAplicacion,
									    int vacunaAntirrabica,
									    int tipoVacuna,
									    int dosisAplicadas,
									    String fechaUltimaDosis,
									    int tipoAgresion,
									    boolean provocada,
									    int tipoLesion,
									    int tipoExposicion,
									    boolean cabeza,
									    boolean cara,
									    boolean cuello,
									    boolean manos,
									    boolean tronco,
									    boolean extsuperiores,
									    boolean extinferiores,
									    String fechaAgresion,
									    int confDiagnosticaCasoRabia,
									    String fechaMuestraCasoRabia,
									    String pais,
									    int areaProcedencia,
									    
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
									    
										// Elementos del Animal Agresor        															    						   
									    int especie,
									    String fechaInicioSintomas,
									    String fechaMuerte,
									    String fechaTomaMuestra,
									    int fuenteInformacionLaboratorio,
									    boolean vacunado,
									    String fechaUltimaDosisAnimal,
									    String nombrePropietario,
									    String direccionPropietario,
									    int estadoMomentoAgresion,
									    int ubicacionAnimal,
									    int numeroDiasObserva,
									    int lugarObservacion,
									    int estadoAnimalObserva,
									    int confirmacionDiagnosticaAnimal,
									    
									    
									    // Elementos del Tratamiento Antirrabico
									    boolean lavadoHerida,
									    boolean suturaHerida,
									    boolean aplicacionSuero,
									    String fechaAplicacionSuero,
									    int tipoSueroTratamiento,
									    int cantidadSueroGlutea,
									    int cantidadSueroHerida,
									    String numeroLote,
									    String laboratorioProductor,
									    boolean aplicarVacuna,
									    int numeroDosisTratamiento,
									    int tipoVacunaTratamiento,
									    String fechaVacunaDosis1,
									    String fechaVacunaDosis2,
									    String fechaVacunaDosis3,
									    String fechaVacunaDosis4,
									    String fechaVacunaDosis5,
									    boolean suspensionTratamiento,
									    int razonSuspension,
									    String fechaTomaMuestraMuerte,
									    boolean confirmacionDiagnostica,
									    int pruebasLaboratorio,
									    int profundidadLesion,
									    int reaccionesVacunaSuero,
									    int evolucionPaciente
    							)
    {
        int resultado=0;
        int codigo=0;
        int codigoNot=0;
        
        try {
            DaoFactory daoFactory=DaoFactory.getDaoFactory(System.getProperty("TIPOBD"));
            daoFactory.beginTransaction(con);
        
            //********************************************************
            // Insercion de la ficha de rabia
            
            String codigoMunProcedencia = lugarProcedencia.split("-")[0];
            String codigoDepProcedencia = lugarProcedencia.split("-")[1];
            
            String codigoMunNoti = lugarNoti.split("-")[0];
            String codigoDepNoti = lugarNoti.split("-")[1];
                        
            PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(modificarFichaRabiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBD.ingresarDatoAStatement(modificarFicha,loginUsuario,1,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,sire,2,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(estado),3,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(sueroAntirrabico),4,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoSuero),5,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(cantidadAplicada),6,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaAplicacion,7,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(vacunaAntirrabica),8,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoVacuna),9,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(dosisAplicadas),10,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaUltimaDosis,11,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoAgresion),12,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(provocada),13,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoLesion),14,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(cabeza),15,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(cara),16,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(cuello),17,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(manos),18,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(tronco),19,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(extsuperiores),20,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(extinferiores),21,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoExposicion),22,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaAgresion,23,Types.VARCHAR,true,false);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepProcedencia,24,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunProcedencia,25,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaConsultaGeneral,26,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaInicioSintomasGeneral,27,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(tipoCaso),28,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(hospitalizadoGeneral),29,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaHospitalizacionGeneral,30,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Boolean.toString(estaVivoGeneral),31,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaDefuncion,32,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoDepNoti,33,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,codigoMunNoti,34,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(unidadGeneradora),35,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(profundidadLesion),36,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(confDiagnosticaCasoRabia),37,Types.INTEGER,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,fechaMuestraCasoRabia,38,Types.VARCHAR,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,pais,39,Types.VARCHAR,false,true);
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(areaProcedencia),40,Types.INTEGER,false,true);
            
            UtilidadBD.ingresarDatoAStatement(modificarFicha,Integer.toString(codigoFichaRabia),41,Types.INTEGER,true,false);
            
		    resultado = modificarFicha.executeUpdate();
		    
		    if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }

		    //*************************************************************
		    // Notificacion de la ficha
		    /*
		    if (codigoNot!=0) {
		        
		        PreparedStatementDecorator notificarFicha =  new PreparedStatementDecorator(con.prepareStatement(notificarFichaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
		        
		        notificarFicha.setInt(1,codigoNot);
		        notificarFicha.setInt(2,codigoFichaRabia);
		        
		        resultado = notificarFicha.executeUpdate();
		        
		        if (resultado<1)
		        {
		            daoFactory.abortTransaction(con);
	                return -1; // Estado de error
		        }
		        
		    }
		    */
		    //*************************************************************
            // Insercion de los datos de la localizacion anatómica
            /*
            PreparedStatementDecorator insertarLocalizacion =  new PreparedStatementDecorator(con.prepareStatement(insertarLocalizacionAnatomicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            for (int i=0;i<localizacionAnatomica.size();i++) {
                
                if (localizacionAnatomica.get("codigo_"+i).toString().equals("true")) {
                    
                    insertarLocalizacion.setInt(1,i+1);
                    insertarLocalizacion.setInt(2,codigoFichaRabia);
                    
                    resultado = insertarLocalizacion.executeUpdate();
                }

                if (resultado<1) {
                    
                    daoFactory.abortTransaction(con);
                    return -1;
                }
            }
            */
            //****************************************************************
            // Insercion de los datos del animal agresor
		                
            PreparedStatementDecorator modificarAnimal =  new PreparedStatementDecorator(con.prepareStatement(modificarAnimalStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,Integer.toString(especie),1,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,fechaInicioSintomas,2,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,fechaMuerte,3,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,fechaTomaMuestra,4,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,Integer.toString(fuenteInformacionLaboratorio),5,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,Boolean.toString(vacunado),6,Types.BOOLEAN,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,fechaUltimaDosisAnimal,7,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,nombrePropietario,8,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,direccionPropietario,9,Types.VARCHAR,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,Integer.toString(estadoMomentoAgresion),10,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,Integer.toString(ubicacionAnimal),11,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,Integer.toString(numeroDiasObserva),12,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,Integer.toString(lugarObservacion),13,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,Integer.toString(estadoAnimalObserva),14,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,Integer.toString(confirmacionDiagnosticaAnimal),15,Types.INTEGER,true,false);
            UtilidadBD.ingresarDatoAStatement(modificarAnimal,Integer.toString(codigoFichaRabia),16,Types.INTEGER,true,false);
            
			resultado = modificarAnimal.executeUpdate();

			if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }

			
			//*************************************************************
			// Insercion de los datos del Tratamiento Antirrabico
			
			
			PreparedStatementDecorator modificarTratamiento =  new PreparedStatementDecorator(con.prepareStatement(modificarTratamientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Boolean.toString(lavadoHerida),1,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Boolean.toString(suturaHerida),2,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Boolean.toString(aplicacionSuero),3,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,fechaAplicacionSuero,4,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Integer.toString(tipoSueroTratamiento),5,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Integer.toString(cantidadSueroGlutea),6,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Integer.toString(cantidadSueroHerida),7,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,numeroLote,8,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,laboratorioProductor,9,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Boolean.toString(aplicarVacuna),10,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Integer.toString(numeroDosisTratamiento),11,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Integer.toString(tipoVacunaTratamiento),12,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,fechaVacunaDosis1,13,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,fechaVacunaDosis2,14,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,fechaVacunaDosis3,15,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,fechaVacunaDosis4,16,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,fechaVacunaDosis5,17,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Boolean.toString(suspensionTratamiento),18,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Integer.toString(razonSuspension),19,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,fechaTomaMuestraMuerte,20,Types.VARCHAR,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Boolean.toString(confirmacionDiagnostica),21,Types.BOOLEAN,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Integer.toString(pruebasLaboratorio),22,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Integer.toString(reaccionesVacunaSuero),23,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Integer.toString(evolucionPaciente),24,Types.INTEGER,true,false);
			UtilidadBD.ingresarDatoAStatement(modificarTratamiento,Integer.toString(codigoFichaRabia),25,Types.INTEGER,true,false);
			
			resultado = modificarTratamiento.executeUpdate();

			if(resultado<1)
            {
                daoFactory.abortTransaction(con);
                return -1; // Estado de error
            }					
            
            daoFactory.endTransaction(con);
        }
        catch (SQLException sqle)
        {
            logger.warn(sqle+" Error en la modificacion de datos: SqlBaseFichaRabiaDao "+sqle.toString() );
		    resultado=0;
        }
        
        return resultado;
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
    		logger.error("Error consultando los datos del paciente (SqlBaseFichaRabiaDao) "+sqle);
			return null;
    	}
    }
    
    
    /**
     * Metodo para consultar una ficha de Accidente Rabico
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarTodoFichaRabia(Connection con, int codigo) {
		
		try {
			 
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaTodoFichaRabiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			consulta.setInt(1,codigo);
			consulta.setInt(2,codigo);
			consulta.setInt(3,codigo);
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando la ficha de accidente rabico "+sqle);
			return null;
		}
	}
    
    
    
    /**
     * Metodo para consultar las localizaciones anatomicas para la ficha de accidente rabico
     * @param con
     * @param codigo
     * @return
     */
    public static ResultSet consultarLocalizacionAnatomica(Connection con, int codigo)
    {
        
        try {
            
            PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaLocalizacionAnatomicaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consulta.setInt(1,codigo);
            return consulta.executeQuery();
        }
        catch (SQLException sqle) {
            
            logger.error("Error consultando las localizaciones anatomicas (ficha de accidente rabico) "+sqle);
			return null;
        }
    }
}
