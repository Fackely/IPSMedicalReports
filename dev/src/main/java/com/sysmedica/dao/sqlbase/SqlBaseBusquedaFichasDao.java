/*
 * Creado en 15-nov-2005
 * 
 * Autor Santiago Salamanca
 * SysMédica
 */
package com.sysmedica.dao.sqlbase;

import com.princetonsa.decorator.PreparedStatementDecorator;
import java.util.HashMap;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.decorator.ResultSetDecorator;

import java.sql.ResultSet;
import java.sql.Connection;

import util.ConstantesBD;

import java.util.Collection;

import util.UtilidadBD;

/**
 * @author santiago
 *
 */
public class SqlBaseBusquedaFichasDao {

    /**
     * Objeto que maneja los logs de esta clase
     */
    private static Logger logger = Logger.getLogger(SqlBaseBusquedaFichasDao.class);
    
    /**
     * Primera parte del statement para consultar las fichas
     */  
    private static final String consultaFichaTodasStrParte1="SELECT " +
				    											"primer_nombre," +
				    											"primer_apellido," +
				    											"segundo_nombre," +
				    											"segundo_apellido," +
				    											"dia.nombre AS nombreDiagnostico," +
				    											"fechaDiligenciamiento," +
				    											"horaDiligenciamiento," +
				    											"vef.nombre AS nombreEstado, " +
				    											"dia.codigoEnfermedadesNotificables AS codigoEnfNot, " +
				    											"estado, " +
				    											"ficha.codigoPaciente, " +
				    											"personas.numero_identificacion AS numeroIdentificacion, " +
				    											"personas.tipo_identificacion AS tipoIdentificacion, ";
    
    
    private static final String consultaBrotesStr		="SELECT " +
		    												"ficha.codigofichabrotes AS codigoficha," +
		    												"ficha.estado AS estado," +
		    												"vigieventosbrotes.nombre AS evento," +
		    												"ficha.pacientesgrupo1+ficha.pacientesgrupo2+ficha.pacientesgrupo3+ficha.pacientesgrupo4+ficha.pacientesgrupo5+ficha.pacientesgrupo6 AS numerocasos," +
		    												"ciudades.nombre AS ciudadprocedencia," +
		    												"departamentos.nombre AS departamentoprocedencia, " +
		    												"ficha.fechanotificacion AS fechanotificacion, " +
		    												"vef.nombre AS nombreestado " +
		    											"FROM epidemiologia.vigifichabrotes ficha,epidemiologia.vigieventosbrotes,ciudades,departamentos,epidemiologia.vigiestadosficha vef " +
		    											"WHERE " +
		    												"ficha.evento=vigieventosbrotes.codigo " +
		    												"AND ficha.municipioprocedencia=ciudades.codigo_ciudad " +
		    												"AND ficha.departamentoprocedencia=departamentos.codigo " +
		    												"AND ficha.departamentoprocedencia=ciudades.codigo_departamento " +
		    												"AND ficha.estado = vef.codigo ";
    
    
    
    private static final String consultaBrotesPendientesStr = "SELECT " +
																	"ficha.codigofichabrotes AS codigoficha," +
																	"ficha.estado AS estado," +
																	"vigieventosbrotes.nombre AS evento," +
																	"ficha.pacientesgrupo1+ficha.pacientesgrupo2+ficha.pacientesgrupo3+ficha.pacientesgrupo4+ficha.pacientesgrupo5+ficha.pacientesgrupo6 AS numerocasos," +
																	"ciudades.nombre AS ciudadprocedencia," +
																	"departamentos.nombre AS departamentoprocedencia, " +
																	"ficha.fechanotificacion AS fechanotificacion, " +
																	"vef.nombre AS nombreestado " +
																"FROM epidemiologia.vigifichabrotes ficha,epidemiologia.vigieventosbrotes,ciudades,departamentos,epidemiologia.vigiestadosficha vef,usuarios usu " +
																"WHERE " +
																	"ficha.evento=vigieventosbrotes.codigo " +
																	"AND ficha.municipioprocedencia=ciudades.codigo_ciudad " +
																	"AND ficha.departamentoprocedencia=departamentos.codigo " +
																	"AND ficha.departamentoprocedencia=ciudades.codigo_departamento " +
																	"AND ficha.estado = vef.codigo "+
																	"AND ficha.estado = 1 " +
																	"AND ficha.loginusuario = ? " +
																	"AND ficha.loginusuario = usu.login ";
		    												
    																		
    
    
    /**
     * Segunda parte del statement para consultar las fichas
     */
    private static final String consultaFichaTodasStrParte2="FROM " +
																"personas," +			    											
																"diagnosticos dia," +
																"epidemiologia.vigiEstadosFicha vef, ";
			 
    
    /**
     * Segunda parte del statement para consultar las fichas
     */
    private static final String consultaFichaTodasStrParte3= 
																	"WHERE " +
																		"personas.codigo=ficha.codigoPaciente " +
																	"AND " +
																		"dia.acronimo=ficha.acronimo " +
																	"AND " +
																		"ficha.estado=vef.codigo ";
    
    
    
    /**
     * Union de tablas de fichas para poder hacer la consulta de todas las fichas
     */
    private static final String strUnionFichas = "(SELECT " +
													"codigoFichaRabia as codigoFicha, " +
													"fechaDiligenciamiento, " +
													"horaDiligenciamiento, " +
													"estado," +
													"codigoPaciente," +
													"acronimo," +
													"loginUsuario," +
													"activa " +
												 "FROM " +
													"epidemiologia.vigiFichaRabia " +
												 "UNION " +
												 "SELECT " +
												 	"codigoFichaSarampion as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa  " +
												 "FROM " +
												 	"epidemiologia.vigiFichaSarampion " +
												 "UNION " +
												 "SELECT " +
												 	"codigoFichaVih as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa  " +
												 "FROM " +
												 	"epidemiologia.vigiFichaVih " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaDengue as codigoFicha, " +
												 	"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaDengue " +
												 "UNION " +
												 "SELECT " +
												 	"codigoFichaParalisis as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaParalisis " +
											 	 "UNION " +
												 "SELECT " +
												 	"codigoFichaSifilis as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaSifilis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaTetanos as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaTetanos " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaGenerica as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaGenerica " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaTuberculosis as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaTuberculosis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaMortalidad as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaMortalidad " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaInfecciones as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaInfecciones " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaLesiones as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaLesiones " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaIntoxicacion as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaIntoxicacion " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaRubCongenita as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaRubCongenita " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaOfidico as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaOfidico " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaLepra as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaLepra " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaDifteria as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaDifteria " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaEasv as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaEasv " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaEsi as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaEsi " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaEtas as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaEtas " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaHepatitis as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaHepatitis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaLeishmaniasis as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaLeishmaniasis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaMalaria as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaMalaria " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaMeningitis as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaMeningitis " +
											 	"UNION " +
												 "SELECT " +
												 	"codigoFichaTosferina as codigoFicha, " +
													"fechaDiligenciamiento, " +
												 	"horaDiligenciamiento, " +
												 	"estado," +
												 	"codigoPaciente," +
												 	"acronimo," +
												 	"loginUsuario," +
												 	"activa " +
												 "FROM " +
												 	"epidemiologia.vigiFichaTosferina " +
											
												 ") ficha ";
    
    /**
     * Statement para consultar en todas las fichas
     */
    private static final String consultaFichaTodas = "SELECT " +
														"primer_nombre," +
														"primer_apellido," +
														"segundo_nombre," +
														"segundo_apellido," +
														"dia.nombre AS nombreDiagnostico," +
														"ficha.fechaDiligenciamiento," +
														"ficha.horaDiligenciamiento," +
														"vef.nombre AS nombreEstado, " +
														"dia.codigoEnfermedadesNotificables AS codigoEnfNot, " +
														"estado, " +
														"ficha.codigoPaciente, " +
														"ficha.codigoFicha, " +
														"personas.numero_identificacion AS numeroIdentificacion, " +
		    											"personas.tipo_identificacion AS tipoIdentificacion " +
													"FROM " +
														"personas," +			    											
														"diagnosticos dia," +
														"epidemiologia.vigiEstadosFicha vef, " +
														strUnionFichas+
													"WHERE " +
														"ficha.activa=1 " +
													"AND " +
														"personas.codigo=ficha.codigoPaciente " +
													"AND " +
														"dia.acronimo=ficha.acronimo " +
													"AND " +
														"ficha.estado=vef.codigo ";
    
    
    /**
     * Condicion opcinal para realizar la busqueda teniendo en cuenta
     * el usuario quien diligencio la ficha
     */
    private static final String consultaFichaTodasCondicionUsuario= " AND ficha.loginusuario=? ";
    
    
    private static final String notificarFichaStrParte1 = "UPDATE ";
														
    
    private static final String notificarFichaStrParte2 = " SET codigoNotificacion=?, " +
    														"estado=? ";
															
    
    
    private static final String insertarNotificacionStr="INSERT INTO vigiNotificacion "+
															"("+
															"codigoNotificacion,"+
															"login,"+
															"fecha,"+
															"hora,"+
															"tipo"+
															") "+
														"VALUES(?,?,CURRENT_DATE,"+ValoresPorDefecto.getSentenciaHoraActualBDTipoTime()+",?)";
    
    
 
    
    private static final String unionFichas= "(SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaRabia as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaRabia " +
    										"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaSarampion as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaSarampion " +
    										"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaVih as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaVih " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaDengue as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaDengue " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaParalisis as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaParalisis " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaSifilis as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaSifilis " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaTetanos as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaTetanos " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaGenerica as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaGenerica " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaTuberculosis as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaTuberculosis " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaMortalidad as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaMortalidad " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaInfecciones as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaInfecciones " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaLesiones as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaLesiones " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaIntoxicacion as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaIntoxicacion " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaRubCongenita as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaRubCongenita " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaOfidico as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaOfidico " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaLepra as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaLepra " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaDifteria as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaDifteria " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaEasv as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaEasv " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaEsi as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaEsi " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaEtas as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaEtas " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaHepatitis as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaHepatitis " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaLeishmaniasis as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaLeishmaniasis " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaMalaria as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaMalaria " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaMeningitis as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaMeningitis " +
											"UNION " +
    										"SELECT " +
    											"fechaDiligenciamiento," +
    											"horaDiligenciamiento," +
    											"codigoPaciente," +
    											"acronimo," +
    											"estado," +
    											"loginusuario," +
    											"activa," +
    											"codigoFichaTosferina as codigoFicha " +
    										"FROM " +
    											"epidemiologia.vigiFichaTosferina " +
        											
    											") ficha ";
    
    private static final String consultaFichasPendientesStr="SELECT " +
															"primer_nombre," +
															"primer_apellido," +
															"segundo_nombre," +
															"segundo_apellido," +
															"dia.nombre AS nombreDiagnostico," +
															"fechaDiligenciamiento," +
															"horaDiligenciamiento," +
															"vef.nombre AS nombreEstado, " +
															"dia.codigoEnfermedadesNotificables AS codigoEnfNot, " +
															"estado, " +
															"ficha.codigoPaciente, " +
															"codigoFicha " +
														"FROM " +
															"personas," +			    											
															"diagnosticos dia," +
															"epidemiologia.vigiEstadosFicha vef, " +
															"usuarios usu, " +
															unionFichas +
														"WHERE " +
															"ficha.activa=1 " +
														"AND " +
															"personas.codigo=ficha.codigoPaciente " +
														"AND " +
															"dia.acronimo=ficha.acronimo " +
														"AND " +
															"ficha.estado=1 " +
														"AND " +
															"ficha.estado=vef.codigo " +
														"AND " +
															"ficha.loginusuario=? " +
														"AND " +
															"ficha.loginusuario=usu.login";

    
    
    
    private static final String busquedaFichaPorPacienteStrParte1="SELECT " +
																	    "fechaDiligenciamiento," +
																		"horaDiligenciamiento," +
																		"ficha.acronimo," +
																		"estado," +
																		"loginusuario," +
																		"dia.nombre AS nombreDiagnostico," +
																		"dia.codigoEnfermedadesNotificables AS codigoEnfNot, ";
    
    
    private static final String busquedaFichaPorPacienteStrParte2=",diagnosticos dia WHERE codigoPaciente=?  " +
    																"AND dia.acronimo=ficha.acronimo " +
    															//	"AND ficha.acronimo=? " +
    																"AND ficha.activa=1";
    
    
    private static final String busquedaLaboratoriosStr = "SELECT " +
    														"labs.codigoFichaLaboratorios," +
    														"labs.codigoServicio," +
    														"fichas.codigoFicha," +
    														"fichas.codigoPaciente,";
    
    
    /**
     * Metodo para notificar una ficha que ya ha sido diligenciada
     * @param con
     * @param codigoFicha
     * @param codigoEnfermedadNotificable
     * @param codigoUsuario
     * @param tipo
     * @param secuencia
     * @return
     */
    public static int notificarFicha(Connection con,
            							HashMap mapaFichas,
										String codigoUsuario,
										int tipo,
										String secuencia)
	{
		int resultado=0;
		int codigo;

		try {
			if (tipo==ConstantesBD.codigoTipoNotificacionColectiva) {
			    
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
				
				// Inserción de la notificacion como tal (aqui se inserta solo una notificacion, pues es colectiva) :
				
				insertarNotificacion.setInt(1,codigo);
				insertarNotificacion.setString(2,codigoUsuario);
				insertarNotificacion.setInt(3,tipo);

				resultado = insertarNotificacion.executeUpdate();
				
				if(resultado<1)
				{
					daoFactory.abortTransaction(con);
					return -1; // Estado de error
				}

				int limite = mapaFichas.size()/2;
				
			    for (int i=0;i<limite;i++) {
				    
			    //    int codigoFicha = Integer.parseInt(mapaFichas.get("codigo_"+i).toString());
			    //    int codigoEnfermedadNotificable = Integer.parseInt(mapaFichas.get("enf_"+i).toString());
			    	String codigoAccionFicha = mapaFichas.get("codigo_"+i).toString();
			    	String[] elementosCodigoAccionFicha = codigoAccionFicha.split("@@");
			    	int codigoFicha = Integer.parseInt(elementosCodigoAccionFicha[0]);
			    	int codigoEnfermedadNotificable = Integer.parseInt(elementosCodigoAccionFicha[1]);
			        String notificar = mapaFichas.get("checkNotificacion_"+i).toString();
			        
			        if (notificar.equals(Boolean.TRUE.toString())) {
				        //  Actualizacion de la ficha para que quede como notificada :
						
						String tablaConsulta="";
						String notificarFichaStrParte3 = "";
	
						switch (codigoEnfermedadNotificable) {
							
							case ConstantesBD.codigoFichaFiebreAmarilla : {
							    tablaConsulta="vigiFichaFiebreAmarilla";
							    break;
							}
							case ConstantesBD.codigoFichaHepatitis : {
							    tablaConsulta="vigiFichaHepatitis";
							    break;
							}
							case ConstantesBD.codigoFichaIntoxicacionAlimentaria : {
							    tablaConsulta="vigiFichaIntAlimentaria";
							    break;
							}
							case ConstantesBD.codigoFichaIntoxicacionPlaguicidas : {
							    tablaConsulta="vigiFichaIntPlaguicidas";
							    break;
							}
							case ConstantesBD.codigoFichaMalaria : {
							    tablaConsulta="vigiFichaMalaria";
							    break;
							}
							case ConstantesBD.codigoFichaRabia : {
							    tablaConsulta="vigiFichaRabia";
							    notificarFichaStrParte3 = " WHERE codigoFichaRabia=? ";
							    break;
							}
							case ConstantesBD.codigoFichaRubeola : {
							    tablaConsulta="vigiFichaRubeola";
							    break;
							}
							case ConstantesBD.codigoFichaSarampion : {
							    tablaConsulta="vigiFichaSarampion";
							    break;
							}
							case ConstantesBD.codigoFichaVIH : {
							    tablaConsulta="vigiFichaVIH";
							    break;
							}
							case ConstantesBD.codigoFichaViolenciaIntrafamiliar : {
							    tablaConsulta="vigiFichaViolencia";
							    break;
							}
							case ConstantesBD.codigoFichaTuberculosis : {
								tablaConsulta="vigiFichaTuberculosis";
								break;
							}
						}
	
						String sentencia = notificarFichaStrParte1+tablaConsulta+notificarFichaStrParte2+notificarFichaStrParte3;
						
						PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						
						modificarFicha.setInt(1,codigo);
					//	modificarFicha.setInt(2,ConstantesBD.codigoEstadoFichaNotificada);
						modificarFicha.setInt(3,codigoFicha);
						
						resultado = modificarFicha.executeUpdate();
	
						if(resultado<1)
						{
							daoFactory.abortTransaction(con);
							return -1; // Estado de error
						}
			        }
				}
			    
			    daoFactory.endTransaction(con);
			}
			else if (tipo==ConstantesBD.codigoTipoNotificacionIndividual) {
			    
			    int limite = mapaFichas.size()/2;
			    for (int i=0;i<limite;i++) {
			        
			        if (mapaFichas.get("checkNotificacion_"+i).toString().equals("true")) {
					    PreparedStatementDecorator ps =  new PreparedStatementDecorator(con.prepareStatement(secuencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
						ResultSet rs = ps.executeQuery();
						String notificarFichaStrParte3 = "";
						
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
						
						// Inserción de la notificacion como tal (aqui se inserta solo una notificacion, pues es colectiva) :
						
						insertarNotificacion.setInt(1,codigo);
						insertarNotificacion.setString(2,codigoUsuario);
						insertarNotificacion.setInt(3,tipo);

						resultado = insertarNotificacion.executeUpdate();
						
						if(resultado<1)
						{
							daoFactory.abortTransaction(con);
							return -1; // Estado de error
						}				    
					    
				   //     int codigoFicha = Integer.parseInt(mapaFichas.get("codigo_"+i).toString());
				   //     int codigoEnfermedadNotificable = Integer.parseInt(mapaFichas.get("enf_"+i).toString());
						
						String codigoAccionFicha = mapaFichas.get("codigo_"+i).toString();
				    	String[] elementosCodigoAccionFicha = codigoAccionFicha.split("@@");
				    	int codigoFicha = Integer.parseInt(elementosCodigoAccionFicha[0]);
				    	int codigoEnfermedadNotificable = Integer.parseInt(elementosCodigoAccionFicha[1]);
				        
				        //  Actualizacion de la ficha para que quede como notificada :
						
						String tablaConsulta="";
						
						switch (codigoEnfermedadNotificable) {
							
							case ConstantesBD.codigoFichaFiebreAmarilla : {
							    tablaConsulta="vigiFichaFiebreAmarilla";
							    break;
							}
							case ConstantesBD.codigoFichaHepatitis : {
							    tablaConsulta="vigiFichaHepatitis";
							    break;
							}
							case ConstantesBD.codigoFichaIntoxicacionAlimentaria : {
							    tablaConsulta="vigiFichaIntAlimentaria";
							    break;
							}
							case ConstantesBD.codigoFichaIntoxicacionPlaguicidas : {
							    tablaConsulta="vigiFichaIntPlaguicidas";
							    break;
							}
							case ConstantesBD.codigoFichaMalaria : {
							    tablaConsulta="vigiFichaMalaria";
							    break;
							}
							case ConstantesBD.codigoFichaRabia : {
							    tablaConsulta="vigiFichaRabia";
							    notificarFichaStrParte3 = " WHERE codigoFichaRabia=? ";
							    break;
							}
							case ConstantesBD.codigoFichaRubeola : {
							    tablaConsulta="vigiFichaRubeola";
							    break;
							}
							case ConstantesBD.codigoFichaSarampion : {
							    tablaConsulta="vigiFichaSarampion";
							    break;
							}
							case ConstantesBD.codigoFichaVIH : {
							    tablaConsulta="vigiFichaVIH";
							    break;
							}
							case ConstantesBD.codigoFichaViolenciaIntrafamiliar : {
							    tablaConsulta="vigiFichaViolencia";
							    break;
							}
						}

						String sentencia = notificarFichaStrParte1+tablaConsulta+notificarFichaStrParte2+notificarFichaStrParte3;
						
						PreparedStatementDecorator modificarFicha =  new PreparedStatementDecorator(con.prepareStatement(sentencia,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));

						modificarFicha.setInt(1,codigo);
					//	modificarFicha.setInt(2,ConstantesBD.codigoEstadoFichaNotificada);
						modificarFicha.setInt(3,codigoFicha);
						
						resultado = modificarFicha.executeUpdate();

						if(resultado<1)
						{
							daoFactory.abortTransaction(con);
							return -1; // Estado de error
						}

						daoFactory.endTransaction(con);
			        }
			    }
			}
		}
		catch (SQLException sqle) {
			logger.warn(sqle+" Error en la inserción de datos: SqlBaseBusquedaFichasDao "+sqle.toString() );
			resultado=0;			
		}
		
		return resultado;
	}
	
	/**
	 * Metodo para consultar las fichas de vigilancia epidemiologica
	 * @param con
	 * @param fechaInicial
	 * @param fechaFinal
	 * @param diagnostico
	 * @param estado
	 * @param verPendientes
	 * @return
	 */
	public static Collection consultaFichas(Connection con,
	        								String fechaInicial, 
	        								String fechaFinal, 
	        								String diagnostico, 
	        								String estado, 
	        								boolean verPendientes,
	        								String loginUsuario,
	        								String loginUsuarioBusqueda)
	{
	    try {
			
	        ResultSet rs;
	        Collection coleccion;
	        String stringConsulta="";
	        String stringCondicionConsultaPorUsuario="";
	        
	        // Primero se revisa si el usuario actual tiene rol de administrador 
	        // del sistema de epidemiologia; si es asi, puede realizar busquedas de fichas de todos
	        // los usuarios del sistema, sino, solo puede realizar busquedas de fichas diligenciadas
	        // por el.
	    //    if (!Utilidades.tieneRolFuncionalidad(con,loginUsuario,30003)) {
	            
	     //       stringCondicionConsultaPorUsuario = consultaFichaTodasCondicionUsuario;
	    //    }
	    //    else {
	        	
	        	if (!loginUsuarioBusqueda.equals(null)) {
		        	if (!loginUsuarioBusqueda.equals("@@")) {
		        		
		        		stringCondicionConsultaPorUsuario = consultaFichaTodasCondicionUsuario;
		        	}
	        	}
	    //    }
	        if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaRabia)))
	        {	            
	            stringConsulta = consultaFichaTodasStrParte1+" codigoFichaRabia AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaRabia ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        } 
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaSarampion))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaSarampion AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaSarampion ficha "+consultaFichaTodasStrParte3+" AND dia.codigoenfermedadesnotificables=5 "+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaRubeola))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaSarampion AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaSarampion ficha "+consultaFichaTodasStrParte3+" AND dia.codigoenfermedadesnotificables=9 "+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaVIH))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaVIH AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaVIH ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaFiebreAmarilla))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaDengue AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaDengue ficha "+consultaFichaTodasStrParte3+" AND dia.codigoenfermedadesnotificables=7 "+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaDengue))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaDengue AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaDengue ficha "+consultaFichaTodasStrParte3+" AND dia.codigoenfermedadesnotificables=11 "+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaParalisisFlacida))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaParalisis AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaParalisis ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaSifilisCongenita))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaSifilis AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaSifilis ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaTetanosNeo))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaTetanos AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaTetanos ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaTuberculosis))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaTuberculosis AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaTuberculosis ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaMortalidadMaterna))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaMortalidad AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaMortalidad ficha "+consultaFichaTodasStrParte3+" AND dia.codigoenfermedadesnotificables=26 "+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaMortalidadPerinatal))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaMortalidad AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaMortalidad ficha "+consultaFichaTodasStrParte3+" AND dia.codigoenfermedadesnotificables=27 "+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaInfecciones))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaInfecciones AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaInfecciones ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaLesiones))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaLesiones AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaLesiones ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaIntoxicaciones))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaIntoxicacion AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaIntoxicacion ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaRubCongenita))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaRubCongenita AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaRubCongenita ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaAcciOfidico))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaOfidico AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaOfidico ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaLepra))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaLepra AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaLepra ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaDifteria))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaDifteria AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaDifteria ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaEasv))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaEasv AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaEasv ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaEsi))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaEsi AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaEsi ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaEtas))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaEtas AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaEtas ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaHepatitis))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaHepatitis AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaHepatitis ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaLeishmaniasis))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaLeishmaniasis AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaLeishmaniasis ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaMalaria))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaMalaria AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaMalaria ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaMeningitisMeningo))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaMeningitis AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaMeningitis ficha "+consultaFichaTodasStrParte3+" AND dia.codigoenfermedadesnotificables=16 "+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaMeningitisHemofilos))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaMeningitis AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaMeningitis ficha "+consultaFichaTodasStrParte3+" AND dia.codigoenfermedadesnotificables=17 "+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaTosferina))) {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaTosferina AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaTosferina ficha "+consultaFichaTodasStrParte3+stringCondicionConsultaPorUsuario;
	        }
	        else if (diagnostico.equals("0"))
	        {
	        	stringConsulta = consultaFichaTodas+stringCondicionConsultaPorUsuario;
	        }
	        else {
	        	stringConsulta = consultaFichaTodasStrParte1+" codigoFichaGenerica AS codigoFicha "+consultaFichaTodasStrParte2+" epidemiologia.vigiFichaGenerica ficha "+consultaFichaTodasStrParte3+" AND dia.codigoenfermedadesnotificables="+diagnostico+" "+stringCondicionConsultaPorUsuario;
	        }
	        
	        // Para consultar en las fichas de todas las enfermedades
	        
	        if (!estado.equals("0"))
	        {
	            stringConsulta += " AND vef.codigo="+estado+" ";
	        }
	        
	        if (fechaInicial.length()>0&&fechaFinal.length()>0) {
	            
	            stringConsulta += " AND fechaDiligenciamiento>="+"'"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"'"+" AND fechaDiligenciamiento<="+"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
	        }
	        
	        stringConsulta += " ORDER BY fechaDiligenciamiento ASC";
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(stringConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
		//	if (Utilidades.tieneRolFuncionalidad(con,loginUsuario,30003)) {
				if (!loginUsuarioBusqueda.equals("@@")) {
					consulta.setString(1,loginUsuarioBusqueda);
				}/*
				else {
					consulta.setString(1,loginUsuario);
				}*/
		/*	}
			else {
				consulta.setString(1,loginUsuario);
			}
		*/
			/*
			if (!loginUsuarioBusqueda.equals("@@")) {
				
				if (Utilidades.tieneRolFuncionalidad(con,loginUsuario,30003)) {
					
					consulta.setString(1,loginUsuarioBusqueda);
				}
				else {
					
					consulta.setString(1,loginUsuario);
				}
			}
			*/	
			
			rs = consulta.executeQuery();
			
			coleccion = UtilidadBD.resultSet2Collection(new ResultSetDecorator(rs));
			
			return coleccion;
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando las fichas "+sqle);
			return null;
		}	    
	}
	
	
	/**
	 * Metodo que consulta todas las fichas pendientes, del usuario
	 * @param con
	 * @return
	 */
	public static Collection consultaFichasPendientes(Connection con,String loginUsuario)
	{
		try {
		
			ResultSet rs;
			Collection coleccion;
			
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaFichasPendientesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			consulta.setString(1,loginUsuario);
			
			rs = consulta.executeQuery();
			
			coleccion = UtilidadBD.resultSet2Collection(new ResultSetDecorator(rs));
			
			return coleccion;
		}
		catch (SQLException sqle) {
		
			logger.error("Error consultando las fichas "+sqle);
			return null;
		}	    
	}
	
	
	
	public static Collection consultaBrote(Connection con, 
											int evento, 
											String loginUsuarioBusqueda, 
											String loginUsuario,
											String fechaInicial,
											String fechaFinal,
											int estado)
	{
		try {
			ResultSet rs;
	        Collection coleccion;
	        String stringConsulta="";/**
	         * Devuelve datos de las fichas relacionadas con un brote especifico (notificacion colectiva)
	         * @param con
	         * @param fechaInicial
	         * @param fechaFinal
	         * @param diagnostico
	         * @param loginUsuario
	         * @param tipoNotificacion
	         * @return
	         */ /*
	        public static Collection consultaBrote(Connection con, String diagnostico, String loginUsuario, int codigoNotificacion) {
	        	
	        	try {
	        		ResultSet rs;
	    	        Collection coleccion;
	    	        String stringConsulta="";
	    	        String tablaConsulta="";
	    	        String campoCodigo="";
	    	        
	    	        if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaRabia))) {
	    	        	
	    	        	tablaConsulta = "vigiFichaRabia ficha ";
	    	        	campoCodigo = "ficha.codigoFichaRabia AS codigoFicha ";
	    	        }
	    	        	        
	    	        stringConsulta = consultaBroteStrParte1+campoCodigo+consultaBroteStrParte2+tablaConsulta+consultaBroteStrParte3+" ORDER BY fechaDiligenciamiento";
	    	        
	    	        PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(stringConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
	    	        
	    	        consulta.setInt(1,codigoNotificacion);
	    	        
	    	        rs = consulta.executeQuery();
	    	        
	    	        coleccion = UtilidadBD.resultSet2Collection(rs);
	    	        
	    	        return coleccion;
	        	}
	        	catch (SQLException sqle) {
	        		
	        		logger.error("Error consultando informacion sobre el brote");
	        		return null;
	        	}
	        }
	        */
	        String stringCondicionConsultaPorUsuario="";
	        
	        // Primero se revisa si el usuario actual tiene rol de administrador 
	        // del sistema de epidemiologia; si es asi, puede realizar busquedas de fichas de todos
	        // los usuarios del sistema, sino, solo puede realizar busquedas de fichas diligenciadas
	        // por el.
	    /*    
	        if (!Utilidades.tieneRolFuncionalidad(con,loginUsuario,30003)) {
	            
	            stringCondicionConsultaPorUsuario = consultaFichaTodasCondicionUsuario;
	        }
	        else {
	    */    	
	        	if (!loginUsuarioBusqueda.equals("@@")) {
	        		
	        		stringCondicionConsultaPorUsuario = consultaFichaTodasCondicionUsuario;
	        	}
	    //    }
	        
	        stringConsulta = consultaBrotesStr+stringCondicionConsultaPorUsuario;
	        
	        if (evento!=0) 
	        {
	        	stringConsulta += " AND evento="+Integer.toString(evento);
	        }
	        
	        if (estado!=0)
	        {
	            stringConsulta += " AND vef.codigo="+Integer.toString(estado)+" ";
	        }
	        
	        if (fechaInicial.length()>0&&fechaFinal.length()>0) {
	            
	            stringConsulta += " AND fechanotificacion>="+"'"+UtilidadFecha.conversionFormatoFechaABD(fechaInicial)+"'"+" AND fechanotificacion<="+"'"+UtilidadFecha.conversionFormatoFechaABD(fechaFinal)+"'";
	        }
	        
	        stringConsulta += " ORDER BY codigoFicha ASC";
	         
	        PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(stringConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
	   //     if (Utilidades.tieneRolFuncionalidad(con,loginUsuario,30003)) {
				
				if (!loginUsuarioBusqueda.equals("@@")) {
					
					consulta.setString(1,loginUsuarioBusqueda);
				}/*
				else {
					
					consulta.setString(1,loginUsuario);
				}*/
		/*	}
			else {
				
				consulta.setString(1,loginUsuario);
			}
		*/
	        /*
			if (!loginUsuarioBusqueda.equals("@@")) {
				
				if (Utilidades.tieneRolFuncionalidad(con,loginUsuario,30003)) {
					
					consulta.setString(1,loginUsuarioBusqueda);
				}
				else {
					
					consulta.setString(1,loginUsuario);
				}
			}
			*/
					
			rs = consulta.executeQuery();
			
			coleccion = UtilidadBD.resultSet2Collection(new ResultSetDecorator(rs));
			
			return coleccion;
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando las fichas "+sqle);
			return null;
		}	
	}
	
	
	public static Collection consultaBrotesPendientes(Connection con,String loginUsuario)
	{
		try {
			
			ResultSet rs;
			Collection coleccion;
			
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(consultaBrotesPendientesStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			consulta.setString(1,loginUsuario);
			logger.info(consultaBrotesPendientesStr);
			rs = consulta.executeQuery();
			
			coleccion = UtilidadBD.resultSet2Collection(new ResultSetDecorator(rs));
			
			return coleccion;
		}
		catch (SQLException sqle) {
		
			logger.error("Error consultando las fichas "+sqle);
			return null;
		}	  
	}
	
	
	
	
	public static ResultSet consultaFichasPorPaciente(Connection con, int codigoPaciente, String diagnostico, String codigoDx)
	{
		try {
			
			ResultSet rs;
			Collection coleccion;
			
			String stringConsulta = "";
			
			if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaRabia)))
	        {	            
	            stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaRabia AS codigoFicha FROM epidemiologia.vigiFichaRabia ficha "+busquedaFichaPorPacienteStrParte2;
	        } 
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaSarampion))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaSarampion AS codigoFicha FROM epidemiologia.vigiFichaSarampion ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaRubeola))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaSarampion AS codigoFicha FROM epidemiologia.vigiFichaSarampion ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaVIH))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaVIH AS codigoFicha FROM epidemiologia.vigiFichaVIH ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaFiebreAmarilla))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaDengue AS codigoFicha FROM epidemiologia.vigiFichaDengue ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaDengue))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaDengue AS codigoFicha FROM epidemiologia.vigiFichaDengue ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaParalisisFlacida))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaParalisis AS codigoFicha FROM epidemiologia.vigiFichaParalisis ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaSifilisCongenita))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaSifilis AS codigoFicha FROM epidemiologia.vigiFichaSifilis ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaTetanosNeo))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaTetanos AS codigoFicha FROM epidemiologia.vigiFichaTetanos ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaTuberculosis))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaTuberculosis AS codigoFicha FROM epidemiologia.vigiFichaTuberculosis ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaMortalidadMaterna))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaMortalidad AS codigoFicha FROM epidemiologia.vigiFichaMortalidad ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaMortalidadPerinatal))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaMortalidad AS codigoFicha FROM epidemiologia.vigiFichaMortalidad ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaInfecciones))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaInfecciones AS codigoFicha FROM epidemiologia.vigiFichaInfecciones ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else if (diagnostico.equals(Integer.toString(ConstantesBD.codigoFichaLesiones))) {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaLesiones AS codigoFicha FROM epidemiologia.vigiFichaLesiones ficha "+busquedaFichaPorPacienteStrParte2;
	        }
	        else {
	        	stringConsulta = busquedaFichaPorPacienteStrParte1+" codigoFichaGenerica AS codigoFicha FROM epidemiologia.vigiFichaGenerica ficha "+busquedaFichaPorPacienteStrParte2+" AND dia.codigoenfermedadesnotificables="+diagnostico;
	        }
			
			
			PreparedStatementDecorator consulta =  new PreparedStatementDecorator(con.prepareStatement(stringConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
			
			consulta.setInt(1,codigoPaciente);
		//	consulta.setString(2,codigoDx);
			
			return consulta.executeQuery();
		}
		catch (SQLException sqle) {
			
			logger.error("Error consultando las fichas "+sqle);
			return null;
		}
	}
}
