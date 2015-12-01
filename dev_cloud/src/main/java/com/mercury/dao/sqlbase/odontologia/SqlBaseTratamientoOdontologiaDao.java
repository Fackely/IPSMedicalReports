package com.mercury.dao.sqlbase.odontologia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.UtilidadBD;
import util.ValoresPorDefecto;

import com.mercury.dto.odontologia.DtoInterpretacionIndicePlaca;
import com.mercury.util.UtilidadBaseDatos;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.princetonsa.dto.odontologia.DtoSuperficieDental;
import com.servinte.axioma.orm.TipoHallazgoCeoCop;

/**
 * Clase con los métodos basicos para acceso a bd de los tratamientos de odontologia 
 * @author alejo
 *
 */
public class SqlBaseTratamientoOdontologiaDao
{
	private static Logger logger = Logger.getLogger(SqlBaseTratamientoOdontologiaDao.class);
    
    /**
     * Cadena constante con el <i>statement</i> necesario para insertar 
     * una entrada en la tabla tratamiento_odontologia. (Esto hace parte de la inserción 
     * de la hoja odontologica. 
     */
    private final static String insertarTratamientoOdontologiaStr=
    	"INSERT INTO tratamiento_odo (" +
        "codigo, " +
        "cod_paciente, " +
        "cod_tipo_trat_odo_inst, " +
        "cod_medico, " +
        "fec_iniciacion, " +
        "diag_plan_trat, " +
        "observaciones, " +
        "motivo_consulta, " +
        "PROFESIONAL_RESPONSABLE, " +
        "ESPECIALIDAD_PROFESIONAL) " +
        "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                                                                                            
    /**
     * Cadena constante con el <i>statemente</i> para cargar un tratamiento de odontologia
     */ 
    private final static String consultarTratamientoOdontologiaStr=
        "SELECT " +
        "codigo                                                     as codigo, " +
        "cod_paciente                                               as codPaciente, " +
        "cod_tipo_trat_odo_inst                                     as codTipoTratamiento," +
        "getNombTipoTratamientoOdoInst(cod_tipo_trat_odo_inst)    as nomTipoTratamiento, " +
        "cod_medico                                                 as codMedico, " +
        "to_char(fec_iniciacion, 'DD/MM/YYYY')                      as fecIniciacion, " +
        "to_char(fec_finalizacion, 'DD/MM/YYYY')                    as fecFinalizacion, " +
        "diag_plan_trat                                             as diagPlanTratamiento, " +
        "observaciones                                              as observaciones, " +
        "motivo_consulta 								AS motivo_consulta, " +
        "PROFESIONAL_RESPONSABLE 								AS PROFESIONAL_RESPONSABLE, " +
        "ESPECIALIDAD_PROFESIONAL 								AS ESPECIALIDAD_PROFESIONAL " +
        "FROM tratamiento_odo where codigo=?";
    
    
    /**
     * Cadena que realiza la consulta de los diagnosticos de la carta dental
     */
    public final static String consultarDiagnosticosCartDental="SELECT " +
    "codigo as codigo,activo as activo, " +
    "nombre as nombre,coalesce(acronimo,'') as acronimo, carta_dental as cartadental " +
    "FROM  est_sec_diente_inst " +
    "WHERE carta_dental='"+ConstantesBD.acronimoSi+"' and cod_institucion=?  and activo="+ ValoresPorDefecto.getValorTrueParaConsultas() +" "+
    "order by nombre ";
    
    
    /**
     * Cadena que realiza la consulta de los tratamientos de la carta dental
     */
    public final static String consultarTratamientosCartaDental="SELECT "+
    "codigo as codigo, nombre as nombre, activo as activo "+
    "FROM tipo_tratamiento_odo_inst "+
    "WHERE activo= "+ValoresPorDefecto.getValorTrueParaConsultas()+"  and cod_institucion=?  "+
    "and codigo!="+ConstantesBD.codigoTipoTratamientoOdontologia +" and codigo!="+ConstantesBD.codigoTipoTratamientoOrtodoncia+" and codigo!="+ConstantesBD.codigoTipoTratamientoPeriodoncia+" and codigo!="+ConstantesBD.codigoTipoTratamientoHigieneOral+" "+
    "order by nombre ";
    
    	
    public final static String consultarSuperficiesCartaDental= " SELECT "+
    "codigo as codigo, nombre as nombre, activo as activo "+
    "FROM superficie_dental WHERE cod_institucion=? and activo="+ValoresPorDefecto.getValorTrueParaConsultas()+" order by nombre asc";
    	
   
    
    
    /**
     * Cadena constante con el <i>statement</i> necesario para modificar 
     * una entrada en la tabla tratamiento_odo. 
     */
    private final static String modificarTratamientoOdontologiaStr=
        "UPDATE tratamiento_odo set fec_finalizacion=?, observaciones=? where codigo=?";
    
    private final static String existenTratamientosPacienteOdontologiaStr =
        "SELECT COUNT(*) as totTratamientos " +
        "FROM tratamiento_odo " +
        "WHERE cod_paciente = ?";
    
    private final static String consultarTratamientosPacienteStr=
        "SELECT " +
        "codigo as codigo, " +
        "cod_paciente as codPaciente, " +
        "cod_tipo_trat_odo_inst as codTipoTratamiento," +
        "getNombTipoTratamientoOdoInst(cod_tipo_trat_odo_inst) as nomTipoTratamiento, " +
        "cod_medico as codMedico, " +
        "administracion.getnombremedico(cod_medico) as nomMedico, " +
        "to_char(fec_iniciacion, 'DD/MM/YYYY') as fecIniciacion, " +
        "to_char(fec_finalizacion, 'DD/MM/YYYY') as fecFinalizacion, " +
        "diag_plan_trat as diagPlanTratamiento, " +
        "observaciones as observaciones, motivo_consulta AS motivo_consulta " +
        "FROM tratamiento_odo where cod_paciente=?"; //and cod_tipo_trat_odo_inst!=6 and cod_tipo_trat_odo_inst!=7 and cod_tipo_trat_odo_inst!=8";

    private final static String insertarEvolucionOdontologiaStr=
        "INSERT INTO evolucion_odo (" +
        "codigo, " +
        "cod_tratamiento_odo, " +
        "descripcion, " +
        "cod_medico, " +
        "fecha, " +
        "observaciones) values (?, ?, ?, ?, ?, ?)";
    
    private final static String consultarEvolucionesOdontologiaStr=
        "SELECT " +
        "codigo as codigo, " +
        "cod_tratamiento_odo as codTratamientoOdo, " +
        "descripcion as descripcion, " +
        "cod_medico as codMedico, " +
        "to_char(fecha, 'DD/MM/YYYY') as fecha, " +
        //"to_char(hora, 'H24:MI') as hora, " +
        "observaciones as observaciones " +
        "FROM evolucion_odo where cod_tratamiento_odo=?";
    
    private final static String existenEvolucionesOdontologiaStr =
        "SELECT COUNT(*) as totEvoluciones " +
        "FROM evolucion_odo " +
        "WHERE cod_tratamiento_odo=?";

    private final static String modificarEvolucionOdontologiaStr=
        "UPDATE " +
        "evolucion_odo SET " +
        "observaciones=? where " +
        "codigo=? and cod_tratamiento_odo=?";
    
    private final static String insertarAnalisisOdontologiaStr=
        "insert into analisis_odo (" +
        "cod_tratamiento_odo, " +
        "cod_tipo_ana_odo_inst, " +
        "cod_opc_tip_ana_odo, " +
        "comentario, " +
        "observaciones) " +
        "values (?, ?, ?, ?, ?)";
    
    private final static String consultarAnalisisOdontologiaStr=
        "select " +
        "cod_tratamiento_odo as codTratamiento, " +
        "cod_tipo_ana_odo_inst as codTipoAnalisisInst, " +
        "HISTORIACLINICA.getNombreTipoAnalisisOdoInst(cod_tipo_ana_odo_inst) as nomTipoAnalisis, " +
        "cod_opc_tip_ana_odo as codOpcionTipoAnalisis, " +
        "HISTORIACLINICA.getNombreOpcionTipoAnalisisOdo(cod_opc_tip_ana_odo) as nomOpcionTipoAnalisis, " +
        "comentario as comentario, " +
        "observaciones as observaciones " +
        "from analisis_odo where cod_tratamiento_odo=?";
    
    private final static String modificarAnalisisOdontologiaStr=
        "update " +
        "analisis_odo set " +
        "observaciones=? where " +
        "cod_tratamiento_odo = ? and " +
        "cod_tipo_ana_odo_inst = ?";
    
    private final static String existeAnalisisOdontologiaStr =
        "SELECT COUNT(*) as totAnalisis " +
        "FROM analisis_odo " +
        "WHERE cod_tratamiento_odo = ? and " +
        "cod_tipo_ana_odo_inst = ?";

    private final static String insertarObservacionesSeccionStr=
        "insert into obs_seccion_trat_odo (" +
        "cod_tratamiento_odo, " +
        "cod_seccion_trat_odo_inst, " +
        "observaciones) " +
        "values (?, ?, ?)";
    
    private final static String consultarObservacionesSeccionStr=
        "select " +
        "cod_tratamiento_odo as codTratamiento, " +
        "cod_seccion_trat_odo_inst as codSeccionTratamiento, " +
        "getNombSeccTratamientoOdoInst(cod_seccion_trat_odo_inst) as nomSeccionTratamiento, " +
        "observaciones as observaciones " +
        "from obs_seccion_trat_odo where cod_tratamiento_odo=?";
    
    private final static String modificarObservacionesSeccionStr=
        "update " +
        "obs_seccion_trat_odo set " +
        "observaciones=? where " +
        "cod_tratamiento_odo = ? and " +
        "cod_seccion_trat_odo_inst = ?";
    
    private final static String existenObservacionesSeccionStr =
        "SELECT COUNT(*) as totObservaciones " +
        "FROM obs_seccion_trat_odo " +
        "WHERE cod_tratamiento_odo = ? and " +
        "cod_seccion_trat_odo_inst = ?";

    /**
     * Cadena para insertar el tratamiento de un diente en determinado tratamiento de un paciente
     */
    private final static String insertarTratamientoDienteOdontologiaStr=
        "insert into tratamiento_diente_odo (" +
        "codigo, " +
        "numero_diente, " +
        "cod_seccion_trat_odo_inst, " +
        "cod_tratamiento_odo, " +
        "cod_medico, " +
        "fecha, " +
        "observaciones) " +
        "values (?, ?, ?, ?, ?, ?, ?)";
    
    /**
     * Cadena para consultar los tratamientos de dientes creados en un tratamiento determinado
     */
    private final static String consultarTratamientosDientesOdontologiaStr=
        "select " +
        "codigo as codigo, " +
        "numero_diente as numeroDiente, " +
        "cod_seccion_trat_odo_inst as codSeccionTratamientoInst, " +
        "cod_tratamiento_odo as codTratamiento, " +
        "cod_medico as codMedico, " +
        "to_char(fecha, 'DD/MM/YYYY') as fecha, " +
        "observaciones as observaciones " +
        "from tratamiento_diente_odo where cod_tratamiento_odo=?";
    
    /**
     * Cadena para modificar determinado tratamiento de un diente
     */
    private final static String modificarTratamientoDienteOdontologiaStr=
        "update " +
        "tratamiento_diente_odo set " +
        "observaciones=? where " +
        "codigo = ?";
    
    /**
     * Cadena que ingresa un nuevo análisis realizado a un diente
     */
    private final static String insertarAnalisisDienteOdontologiaStr=
        "insert into analisis_diente_odo (" +
        "cod_trat_diente_odo, " +
        "cod_tipo_ana_odo_inst, " +
        "cod_opc_tip_ana_odo, " +
        "comentario, " +
        "observaciones) " +
        "values (?, ?, ?, ?, ?)";
    
    /**
     * Cadena para consultar los analisis correspondientes a un tratamiento diente determinado
     */
    private final static String consultarAnalisisDienteOdontologiaStr=
        "select " +
        "cod_trat_diente_odo as codTratamientoDiente, " +
        "cod_tipo_ana_odo_inst as codTipoAnalisisInst, " +
        "HISTORIACLINICA.getNombreTipoAnalisisOdoInst(cod_tipo_ana_odo_inst) as nomTipoAnalisis, " +
        "cod_opc_tip_ana_odo as codOpcionTipoAnalisis, " +
        "HISTORIACLINICA.getNombreOpcionTipoAnalisisOdo(cod_opc_tip_ana_odo) as nomOpcionTipoAnalisis, " +
        "comentario as comentario, " +
        "observaciones as observaciones " +
        "from analisis_diente_odo where cod_trat_diente_odo=?";
    
    /**
     * Cadena para modificar determinado analisis de un diente en un tratamiento
     */
    private final static String modificarAnalisisDienteOdontologiaStr=
        "update " +
        "analisis_diente_odo set " +
        "observaciones=? where " +
        "cod_trat_diente_odo = ? and cod_tipo_ana_odo_inst = ?";
    
	/**
	 * Cadena Sql para realizar la consulta de los tipos de hallazgos Odonotologicos
	 */
	private static String consultaTipoHallazgo = "SELECT th.codigo, th.tipo " +
				   "FROM odontologia.hallazgos_odontologicos hallodo, odontologia.tipo_hallazgo_ceo_cop th " +
				   "WHERE hallodo.tipo_hallazgo_ceo_cop = th.codigo ";
	
    
    /**
     * Método para insertar un tratamiento nuevo
     * @param con
     * @param secuenciaCodigo
     * @param codPaciente
     * @param codTipoTratOdoInst
     * @param codMedico
     * @param fecIniciacion
     * @param diagPlanTrat
     * @param observaciones
     * @param motivoConsulta
     * @return
     * @throws SQLException
     */
    public static int insertar(
            Connection con,
            String secuenciaCodigo,
            int codPaciente, 
            int codTipoTratOdoInst,
            int codMedico,
            String fecIniciacion,
            String diagPlanTrat,
            String observaciones, String motivoConsulta , String datosMedico, Integer especialidad) throws SQLException
    {
        try
        {
            int codigo = UtilidadBaseDatos.obtenerCodigoSiguiente(con, secuenciaCodigo);
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.insertarTratamientoOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codigo);
            insertarStatement.setInt(2, codPaciente);
            insertarStatement.setInt(3, codTipoTratOdoInst);
            insertarStatement.setInt(4, codMedico);
            UtilidadBaseDatos.establecerParametro(5, Types.DATE, fecIniciacion, insertarStatement);     
            
            //UtilidadBaseDatos.establecerParametro(6, Types.VARCHAR, diagPlanTrat, insertarStatement); 
            UtilidadBaseDatos.establecerParametro(6, Types.VARCHAR, null, insertarStatement); 
            
            UtilidadBaseDatos.establecerParametro(7, Types.VARCHAR, observaciones, insertarStatement);
            UtilidadBaseDatos.establecerParametro(8, Types.VARCHAR, motivoConsulta, insertarStatement);
            insertarStatement.setString(9, datosMedico);
            insertarStatement.setInt(10, especialidad);
            insertarStatement.executeUpdate();
            return codigo; // retorna el codigo del nuevo tratamiento ingresado
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    /**
     * Método para consultar un tratamiento dado su código
     * @param con
     * @param codigo
     * @return
     * @throws SQLException
     */
    public static ResultSetDecorator consultar(Connection con, int codigo) throws SQLException
    {
        try
        {
            PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.consultarTratamientoOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consultarStatement.setInt(1, codigo);
            return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    /**
     * Método para modificar la fecha de finalización y las observaciones de un tratamiento, dado su código
     * @param con
     * @param codigo
     * @param fecFinalizacion
     * @param observaciones
     * @return
     * @throws SQLException
     */
    public static void modificar(
            Connection con, 
            int codigo, 
            String fecFinalizacion, 
            String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.modificarTratamientoOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));            
            UtilidadBaseDatos.establecerParametro(1, Types.DATE, fecFinalizacion, modificarStatement);
            UtilidadBaseDatos.establecerParametro(2, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(3, codigo);
            modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    /**
     * Método para consultar si existen tratamientos de un paciente determinado 
     * @param con
     * @param codPaciente
     * @return
     * @throws SQLException
     */
    public static boolean existenTratamientosPaciente(Connection con, int codPaciente) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.existenTratamientosPacienteOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                ResultSetDecorator rs = new ResultSetDecorator(consultarStatement.executeQuery());
                if(rs.next())
                {
                    int totTratamientos = rs.getInt("totTratamientos");
                    if(totTratamientos>0)
                        return true;
                    else
                        return false;
                }
                else
                {
                    String mensajeError = "Hubo problemas consultando si el paciente tenía o no algun tratamiento de odontologia, para el paciente (no retornó ningún registro) : "+codPaciente+". \n";
                    logger.warn(mensajeError);
                    throw new SQLException(mensajeError);
                }
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    /**
     * Método para consultar los tratamientos de un paciente
     * @param con
     * @param codPaciente
     * @return
     * @throws SQLException
     */
    public static ResultSetDecorator consultarTratamientosPaciente(Connection con, int codPaciente) throws SQLException
    {
        try
        {
            String cadenaConsulta =SqlBaseTratamientoOdontologiaDao.consultarTratamientosPacienteStr;
            
            PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consultarStatement.setInt(1, codPaciente);
            
            return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    /**
     * 
     * @param con
     * @param codPaciente
     * @return
     * @throws SQLException
     */
    public static ResultSetDecorator consultarTratamientosSinFinalizarPaciente(Connection con, int codPaciente) throws SQLException
    {
        try
        {
            String cadenaConsulta =SqlBaseTratamientoOdontologiaDao.consultarTratamientosPacienteStr+" and fec_finalizacion is null";
            
            PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consultarStatement.setInt(1, codPaciente);
            
            ResultSetDecorator rs=  new ResultSetDecorator(consultarStatement.executeQuery());
            
            return rs;
        }
        catch(SQLException e)
        {
            logger.error(e);
            throw e;
        }
    }
    
    /**
     * Método para insertar una nueva evolución de un paciente
     * @param con
     * @param codigo
     * @param codTratamiento
     * @param descripcion
     * @param codMedico
     * @param fecha
     * @param observaciones
     * @return
     * @throws SQLException
     */
    public static void insertarEvolucion(
            Connection  con,
            int         codigo,
            int         codTratamiento,
            String      descripcion,
            int         codMedico,
            String      fecha,
            String      observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.insertarEvolucionOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codigo);
            insertarStatement.setInt(2, codTratamiento);
            UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, descripcion, insertarStatement);
            insertarStatement.setInt(4, codMedico);
            UtilidadBaseDatos.establecerParametro(5, Types.DATE, fecha, insertarStatement);
            UtilidadBaseDatos.establecerParametro(6, Types.VARCHAR, observaciones, insertarStatement);
            insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarEvoluciones(Connection con, int codTratamiento) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.consultarEvolucionesOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codTratamiento);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static boolean existenEvoluciones(Connection con, int codTratamiento) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.existenEvolucionesOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codTratamiento);
                ResultSetDecorator rs = new ResultSetDecorator(consultarStatement.executeQuery());
                if(rs.next())
                {
                    int totHabito = rs.getInt("totHabito");
                    if(totHabito>0)
                        return true;
                    else
                        return false;
                }
                else
                {
                    String mensajeError = "Hubo problemas consultando si el paciente tenía o no alguna evolucion para el tratamiento "+codTratamiento+" previamente ingresado.\n";
                    logger.warn(mensajeError);
                    throw new SQLException(mensajeError);
                }
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static void modificarEvolucion(Connection con, int codigo, int codTratamiento, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.modificarEvolucionOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codigo);
            modificarStatement.setInt(3, codTratamiento);
            modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }    

    public static void insertarAnalisis(
            Connection  con,
            int         codTratamiento,
            int         codTipoAnalisis,
            String      codOpcionTipoAnalisis,
            String      comentario,
            String      observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.insertarAnalisisOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codTratamiento);
            insertarStatement.setInt(2, codTipoAnalisis);
            UtilidadBaseDatos.establecerParametro(3, Types.INTEGER, codOpcionTipoAnalisis, insertarStatement);
            UtilidadBaseDatos.establecerParametro(4, Types.VARCHAR, comentario, insertarStatement);
            UtilidadBaseDatos.establecerParametro(5, Types.VARCHAR, observaciones, insertarStatement);
            insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    /**
     * 
     * @param con
     * @param codTratamiento
     * @return
     * @throws SQLException
     */
    public static ResultSetDecorator consultarAnalisis(Connection con, int codTratamiento) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.consultarAnalisisOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codTratamiento);
                logger.info("Consulta ->"+SqlBaseTratamientoOdontologiaDao.consultarAnalisisOdontologiaStr+" "+codTratamiento);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static void modificarAnalisis(Connection con, int codTipoAnalisis, int codTratamiento, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.modificarAnalisisOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codTratamiento);
            modificarStatement.setInt(3, codTipoAnalisis);
            modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }    

    public static boolean existeAnalisis(Connection con, int codTratamiento, int codTipoAnalisis) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.existeAnalisisOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codTratamiento);
                consultarStatement.setInt(2, codTipoAnalisis);
                ResultSetDecorator rs = new ResultSetDecorator(consultarStatement.executeQuery());
                if(rs.next())
                {
                    int totAnalisis = rs.getInt("totAnalisis");
                    if(totAnalisis>0)
                        return true;
                    else
                        return false;
                }
                else
                {
                    String mensajeError = "Hubo problemas consultando si el paciente tenía o no el análisis de código "+codTipoAnalisis+" para el tratamiento "+codTratamiento+" previamente ingresado.\n";
                    logger.warn(mensajeError);
                    throw new SQLException(mensajeError);
                }
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static void insertarObservacionesSeccion(
            Connection  con,
            int         codTratamiento,
            int         codSeccionTratamiento,
            String      observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.insertarObservacionesSeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codTratamiento);
            insertarStatement.setInt(2, codSeccionTratamiento);
            UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, observaciones, insertarStatement);
            insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarObservacionesSeccion(Connection con, int codTratamiento) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.consultarObservacionesSeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codTratamiento);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static boolean existenObservacionesSeccion(Connection con, int codTratamiento, int codSeccionTratamiento) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.existenObservacionesSeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codTratamiento);
                consultarStatement.setInt(2, codSeccionTratamiento);
                ResultSetDecorator rs = new ResultSetDecorator(consultarStatement.executeQuery());
                if(rs.next())
                {
                    int totObservaciones = rs.getInt("totObservaciones");
                    if(totObservaciones>0)
                        return true;
                    else
                        return false;
                }
                else
                {
                    String mensajeError = "Hubo problemas consultando si el paciente tenía o no alguna observación para el tratamiento "+codTratamiento+" previamente ingresado.\n";
                    logger.warn(mensajeError);
                    throw new SQLException(mensajeError);
                }
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static void modificarObservacionesSeccion(Connection con, int codTratamiento, int codSeccionTratamiento, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.modificarObservacionesSeccionStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codTratamiento);
            modificarStatement.setInt(3, codSeccionTratamiento);
            modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static int insertarTratamientoDiente(
            Connection  con,
            String      secuenciaCodigo,
            int         numeroDiente,
            int			codSeccionTratamientoInst,
            int			codTratamiento,
            int         codMedico,
            String      fecha,
            String      observaciones) throws SQLException
    {
        try
        {
            int codigo = UtilidadBaseDatos.obtenerCodigoSiguiente(con, secuenciaCodigo); // la secuencia indica de cual obtener el codigo
            
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.insertarTratamientoDienteOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codigo);
            insertarStatement.setInt(2, numeroDiente);
            insertarStatement.setInt(3, codSeccionTratamientoInst);
            insertarStatement.setInt(4, codTratamiento);
            insertarStatement.setInt(5, codMedico);
            UtilidadBaseDatos.establecerParametro(6, Types.DATE, fecha, insertarStatement);
            UtilidadBaseDatos.establecerParametro(7, Types.VARCHAR, observaciones, insertarStatement);
            insertarStatement.executeUpdate();
            return codigo;
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarTratamientosDientes(Connection con, int codTratamiento) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.consultarTratamientosDientesOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codTratamiento);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static void modificarTratamientoDiente(Connection con, int codigo, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.modificarTratamientoDienteOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codigo);
            modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }    

    public static void insertarAnalisisDiente(
            Connection  con,
            int         codTratamientoDiente,
            int         codTipoAnalisisInst,
            String		codOpcionTipoAnalisis,
            String		comentario,
            String      observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.insertarAnalisisDienteOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codTratamientoDiente);
            insertarStatement.setInt(2, codTipoAnalisisInst);
            UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, codOpcionTipoAnalisis, insertarStatement);
            UtilidadBaseDatos.establecerParametro(4, Types.VARCHAR, comentario, insertarStatement);
            UtilidadBaseDatos.establecerParametro(5, Types.VARCHAR, observaciones, insertarStatement);
            insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarAnalisisDiente(Connection con, int codTratamientoDiente) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.consultarAnalisisDienteOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codTratamientoDiente);
                logger.info(consultarAnalisisDienteOdontologiaStr+" "+codTratamientoDiente);
               
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static void modificarAnalisisDiente(Connection con, int codTratamientoDiente, int codTipoAnalisisInst, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseTratamientoOdontologiaDao.modificarAnalisisDienteOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codTratamientoDiente);
            modificarStatement.setInt(3, codTipoAnalisisInst);
            modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static void insertarProcedimiento(
    		Connection con, int codServicio, int codTratamiento, int prioridad, String fechaRegistro, int codMedicoRegistra, 
    		String fechaPlaneado, int codSuperficieDental, int diente, String observaciones, int estado, String fechaCerrado, String codMedicoCierra) throws SQLException
    {
        String insertarProcedimientoPlaneadoStr=
        	"insert into procedimiento_odo (" +
        	"consecutivo, " +
        	"cod_servicio, " +
        	"cod_tratamiento_odo, " +
        	"prioridad, " +
        	"fecha_registro, " +
        	"cod_medico_registra, " +
        	"fecha_planeado, " +
        	"cod_superficie_dental, " +
        	"diente, " +
        	"observaciones, " +
        	"estado,fecha_cerrado,cod_medico_cierra) " +
        	"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
    	try
    	{
            int consecutivo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_procedimiento_odo");
            PreparedStatementDecorator insertarStatement=new PreparedStatementDecorator(con.prepareStatement(insertarProcedimientoPlaneadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		insertarStatement.setInt(1, consecutivo);
    		insertarStatement.setInt(2, codServicio);
    		insertarStatement.setInt(3, codTratamiento);
    		insertarStatement.setInt(4, prioridad);
    		UtilidadBaseDatos.establecerParametro(5, Types.DATE, fechaRegistro, insertarStatement);
    		insertarStatement.setInt(6, codMedicoRegistra);
    		UtilidadBaseDatos.establecerParametro(7, Types.DATE, fechaPlaneado, insertarStatement);
    		insertarStatement.setInt(8, codSuperficieDental);
    		insertarStatement.setInt(9, diente);
    		UtilidadBaseDatos.establecerParametro(10, Types.VARCHAR, observaciones, insertarStatement);
    		insertarStatement.setInt(11, estado); 
    		UtilidadBaseDatos.establecerParametro(12, Types.DATE, fechaCerrado, insertarStatement);
    		UtilidadBaseDatos.establecerParametro(13, Types.INTEGER, codMedicoCierra, insertarStatement);
    		insertarStatement.executeUpdate();
    	}
    	catch(SQLException e)
    	{
    		logger.warn(e);
    		throw e;
    	}
    }
    
    public static HashMap consultarProcedimiento(Connection con, int consecutivo) throws SQLException
    {
    	String consultarProcedimientoStr =
    		"select " +
    		"consecutivo as consecutivo, " +
    		"cod_servicio as codServicio, " +
    		"cod_tratamiento_odo as codTratamiento, " +
    		"prioridad as prioridad, " +
    		"to_char(fecha_registro, 'DD/MM/YYYY') as fechaRegistro, " +
    		"cod_medico_registra as codMedicoRegistra, " +
    		"to_char(fecha_planeado, 'DD/MM/YYYY') as fechaPlaneado, " +
    		"diente as diente, " +
    		"cod_superficie_dental as superficieDental, " +
    		"estado as estado, " +
    		"to_char(fecha_cerrado, 'DD/MM/YYYY') as fechaCerrado, " +
    		"cod_medico_cierra as codMedicoCierra, " +
    		"observaciones as observaciones from procedimiento_odo where consecutivo = ?";
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarProcedimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, consecutivo);
                HashMap mapaRetorno= UtilidadBD.cargarValueObject(new ResultSetDecorator(consultarStatement.executeQuery()));
                consultarStatement.close();
                return mapaRetorno;
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarProcedimientos(Connection con, int codTratamiento) throws SQLException
    {
    	String consultarProcedimientosTratamientoStr =
    		"select " +
    		"consecutivo as consecutivo, " +
    		"cod_servicio as codigoServicio, " +
    		"getcodigopropservicio2(cod_servicio, "+ConstantesBD.codigoTarifarioCups+") as codigoCUPS, " +
			"getnombreservicio(cod_servicio, "+ConstantesBD.codigoTarifarioCups+") as descripcionServicio, " +
			"getnomespecialidadservicio(cod_servicio) as descEspecialidadServicio, " +
    		"cod_tratamiento_odo as codTratamiento, " +
    		"prioridad as prioridad, " +
    		"to_char(fecha_registro, 'DD/MM/YYYY') as fechaRegistro, " +
    		"cod_medico_registra as codMedicoRegistra, " +
    		"to_char(fecha_planeado, 'DD/MM/YYYY') as fechaPlaneado, " +
    		"diente as diente, " +
    		"superficie_dental.codigo as codigoSuperficieDental, " +
    		"superficie_dental.nombre as nombreSuperficieDental, " +
    		"estado as estado, " +
    		"to_char(fecha_cerrado, 'DD/MM/YYYY') as fechaCerrado, " +
    		"cod_medico_cierra as codMedicoCierra, " +
    		"observaciones as observaciones " +
    		"from procedimiento_odo, superficie_dental where " +
    		"procedimiento_odo.cod_superficie_dental = superficie_dental.codigo and cod_tratamiento_odo = ? order by prioridad";
        try
        {
        	logger.info("consultarProcedimientosTratamientoStr->"+consultarProcedimientosTratamientoStr+" ->"+codTratamiento);
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarProcedimientosTratamientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codTratamiento);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static void modificarProcedimiento(
    		Connection con, int consecutivo, int prioridad, int estado, String fechaCerrado, String codMedicoCierra, String observaciones) throws SQLException
    {
    	String modificarProcedimientoStr =
    		"update procedimiento_odo set " +
    		"prioridad = ?, " +
    		"estado = ?, " +
    		"fecha_cerrado = ?, " +
    		"cod_medico_cierra = ?, " +
    		"observaciones = ? " +
    		"where consecutivo=?";

    	try
    	{
    		PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(modificarProcedimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		modificarStatement.setInt(1, prioridad);
    		modificarStatement.setInt(2, estado);
    		UtilidadBaseDatos.establecerParametro(3, Types.DATE, fechaCerrado, modificarStatement);
    		UtilidadBaseDatos.establecerParametro(4, Types.INTEGER, codMedicoCierra, modificarStatement);
    		UtilidadBaseDatos.establecerParametro(5, Types.VARCHAR, observaciones, modificarStatement);
    		modificarStatement.setInt(6, consecutivo);
    		modificarStatement.executeUpdate();
    	}
    	catch(SQLException e)
    	{
    		logger.warn(e);
    		throw e;
    	}
    }
    
    public static int insertarConsentimientoInformado(Connection con, String fecha, int codMedico, int codTratamiento, int codTipo) throws SQLException
    {
        String insertarConsentimientoInformadoStr=
        	"insert into consentimiento_informado (" +
        	"codigo, " +
        	"fecha, " +
        	"cod_medico, " +
        	"cod_tratamiento_odo, " +
        	"cod_tipo_consentimiento_inf) " +
        	"values (?, ?, ?, ?, ?)";
        
    	try
    	{
            int codigo = UtilidadBD.obtenerSiguienteValorSecuencia(con, "seq_consentimiento_informado");
    		PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(insertarConsentimientoInformadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		insertarStatement.setInt(1, codigo);
    		UtilidadBaseDatos.establecerParametro(2, Types.DATE, fecha, insertarStatement);
    		insertarStatement.setInt(3, codMedico);
    		insertarStatement.setInt(4, codTratamiento);
    		insertarStatement.setInt(5, codTipo);
    		insertarStatement.executeUpdate();
    		return codigo;
    	}
    	catch(SQLException e)
    	{
    		logger.warn(e);
    		throw e;
    	}
    }
    
    public static void insertarValorOpcionConsentimientoInformado(Connection con, int codConsentimiento, int codOpcionTipoConsentimiento, String valor) throws SQLException
    {
        String insertarValorOpcionConsentimientoInformadoStr=
        	"insert into valor_opc_consentimiento (" +
        	"cod_cons_inf, " +
        	"cod_opc_tip_cons, " +
        	"valor) " +
        	"values (?, ?, ?)";
        
    	try
    	{
    		PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(insertarValorOpcionConsentimientoInformadoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
    		insertarStatement.setInt(1, codConsentimiento);
    		insertarStatement.setInt(2, codOpcionTipoConsentimiento);
    		insertarStatement.setString(3, valor);
    		insertarStatement.executeUpdate();
    	}
    	catch(SQLException e)
    	{
    		logger.warn(e);
    		throw e;
    	}
    }

    public static ResultSetDecorator consultarConsentimientosInformados(Connection con, int codTratamiento) throws SQLException
    {
    	String consultarConsentimientosStr =
    		"select " +
    		"consentimiento_informado.codigo as codigo, " +
    		"to_char(fecha, 'DD/MM/YYYY') as fecha, " +
    		"cod_medico as codMedico, " +
    		"cod_tratamiento_odo as codTratamiento, " +
    		"cod_tipo_consentimiento_inf as codigoTipo, " +
    		"tipo_consentimiento_inf.nombre_reporte as nombreReporte, " +
    		"tipo_consentimiento_inf.nombre as nombreTipo " +
    		"from consentimiento_informado, tipo_consentimiento_inf where " +
    		"tipo_consentimiento_inf.codigo = consentimiento_informado.cod_tipo_consentimiento_inf and " +
    		"cod_tratamiento_odo = ? " +
    		"order by fecha";
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarConsentimientosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codTratamiento);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarValoresOpcionesConsentimientoInformado(Connection con, int codConsentimiento) throws SQLException
    {
    	String consultarValoresOpcionesConsentimientoStr =
    		"select " +
    		"valor_opc_consentimiento.valor as valor, " +
    		"opc_tipo_consentimiento.codigo as codigoOpcion, " +
    		"opc_tipo_consentimiento.nombre as nombreOpcion " +
    		"from valor_opc_consentimiento, opc_tipo_consentimiento where " +
    		"opc_tipo_consentimiento.codigo = valor_opc_consentimiento.cod_opc_tip_cons and " +
    		"cod_cons_inf = ?";
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(consultarValoresOpcionesConsentimientoStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codConsentimiento);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    
    public static HashMap consultarDiagnosticosCartaDental(Connection con, int codigoInst){
     String cadena=SqlBaseTratamientoOdontologiaDao.consultarDiagnosticosCartDental;
     
     HashMap mapaDiagnosticos= new HashMap();
 	 mapaDiagnosticos.put("numRegistros", "0");
 	 
 	try
	{
 	 
 	 PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)); 	 
 	 busqueda.setInt(1,codigoInst);
 	 cadena = cadena.replace("cod_institucion=?", "cod_institucion="+codigoInst+" ");
 	
 	 mapaDiagnosticos=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));

	}catch(SQLException e)
			{
				
	          	e.printStackTrace();
	          	logger.warn(e);
			}	 
 	 logger.info("La cadena de consulta de Diangonsticos es asi: >>>>> "+cadena);
 	 logger.info("numero Registros >>>>>"+mapaDiagnosticos.get("numRegistros").toString());
 	 
     return mapaDiagnosticos; 	
    }
    
    
    public static HashMap consultarTratamientosCartaDental(Connection con, int codigoInst){
        String cadena=SqlBaseTratamientoOdontologiaDao.consultarTratamientosCartaDental;
        
         HashMap mapaTratamientos= new HashMap();
    	 mapaTratamientos.put("numRegistros", "0");
    	 
    	try
   	{
    	 
    	 PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)); 	 
    	 busqueda.setInt(1,codigoInst);
    	 cadena = cadena.replace("cod_institucion=?", "cod_institucion="+codigoInst+" ");
    	
    	 mapaTratamientos=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));

   	}catch(SQLException e)
   			{
   				
   	          	e.printStackTrace();
   	          	logger.warn(e);
   			}	 
    	 logger.info("La cadena de consulta de Tratemientos es asi: >>>>> "+cadena);
    	 logger.info("numero Registros >>>>>"+mapaTratamientos.get("numRegistros").toString());
    	 
        return mapaTratamientos; 	
       }
    
    public static HashMap consultarSuperficiesCartaDental(Connection con, int codigoInst){
    	String cadena=SqlBaseTratamientoOdontologiaDao.consultarSuperficiesCartaDental;  
     
     HashMap mapaSuperficies= new HashMap();
   	 mapaSuperficies.put("numRegistros", "0");
   	 
   	try
  	{
   	 PreparedStatementDecorator busqueda= new PreparedStatementDecorator(con.prepareStatement(cadena,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet)); 	 
   	 busqueda.setInt(1,codigoInst);
   	 cadena = cadena.replace("cod_institucion=?", "cod_institucion="+codigoInst+" ");
   	 logger.info("cadena->"+cadena);
   	 mapaSuperficies=UtilidadBD.cargarValueObject(new ResultSetDecorator(busqueda.executeQuery()));

  	}catch(SQLException e)
  			{
  				
  	          	e.printStackTrace();
  	          	logger.warn(e);
  			}	 
   	 logger.info("La cadena de consulta de superficie es asi: >>>>> "+cadena);
   	 logger.info("numero Registros  >>>>>"+mapaSuperficies.get("numRegistros").toString());
   	 
       return mapaSuperficies; 	
    	
    	
    }
    
	/**
	 * Carga las Superficies del Diente
	 * @return
	 */
	public static ArrayList<DtoSectorSuperficieCuadrante> cargarSuperficiesDiente(int institucion)
	{
		ArrayList<DtoSectorSuperficieCuadrante> lista = new ArrayList<DtoSectorSuperficieCuadrante>();

		String consultarStrSuperficiesPlanT = 
			"SELECT " +
				"s.codigo AS codigo, " +
				"s.nombre AS nombre, " +
				"s.cod_institucion AS cod_institucion, " +
				"s.activo AS activo, " +
				"ssc.codigo_pk AS codigo_pk, " +
				"ssc.superficie AS superficie, " +
				"ssc.sector AS sector, " +
				"ssc.pieza AS pieza " +
			"FROM " +
				"historiaclinica.superficie_dental s " +
			"INNER JOIN " +
				"odontologia.sector_superficie_cuadrante ssc " +
			"ON(ssc.superficie=s.codigo) "+
				"AND activo = "+ValoresPorDefecto.getValorTrueParaConsultas()+" " +
				"AND cod_institucion = ? " ;

		try 
		{
			Connection con = UtilidadBD.abrirConexion();
			PreparedStatementDecorator ps =  new PreparedStatementDecorator(con,consultarStrSuperficiesPlanT );
			ps.setInt(1,institucion);
			
			ResultSetDecorator rs=new ResultSetDecorator(ps.executeQuery());
			
			while(rs.next())
			{
				DtoSuperficieDental superficie=new DtoSuperficieDental();
				superficie.setCodigo(rs.getDouble("codigo"));
				superficie.setNombre(rs.getString("nombre"));
				superficie.setInstitucion(rs.getInt("cod_institucion"));
				superficie.setActivo(rs.getBoolean("activo")?1:0);
				DtoSectorSuperficieCuadrante sectorSuperficieCuadrante=new DtoSectorSuperficieCuadrante();
				sectorSuperficieCuadrante.setCodigoPk(rs.getInt("codigo_pk")); // Cï¿½digo en BD
				sectorSuperficieCuadrante.setSuperficie(superficie); // Dto con datos de la superficie
				sectorSuperficieCuadrante.setSector(rs.getInt("sector")); // Sector al que aplica
				sectorSuperficieCuadrante.setPieza(rs.getInt("pieza")); // Pieza para la cual aplica
				lista.add(sectorSuperficieCuadrante);
			}
			
			ps.close();
			rs.close();
			con.close();
		}
		catch (SQLException e) 
		{
			logger.error("error en carga==> "+e);
		}
		
		return lista;
	}

	public static ArrayList<DtoInterpretacionIndicePlaca> cargarInterpretacionIndicePlaca(int codigoInstitucion)
	{
		Connection con=UtilidadBD.abrirConexion();
		ArrayList<DtoInterpretacionIndicePlaca> lista=new ArrayList<DtoInterpretacionIndicePlaca>();
		String consulta=
					"SELECT consecutivo AS consecutivo, " +
							"porcentaje_inicial AS porcentaje_inicial, " +
							"porcentaje_final AS porcentaje_final, " +
							"valor AS valor " +
					"FROM " +
						"historiaclinica.interpre_indice_placa_mercury " +
					"WHERE " +
						"institucion=?";
		try{
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, consulta);
			psd.setInt(1, codigoInstitucion);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			while(rsd.next())
			{
				DtoInterpretacionIndicePlaca indicePlaca=new DtoInterpretacionIndicePlaca();
				indicePlaca.setCodigoPk(rsd.getInt("consecutivo"));
				indicePlaca.setPorcentajeInicial(rsd.getDouble("porcentaje_inicial"));
				indicePlaca.setPorcentajeFinal(rsd.getDouble("porcentaje_final"));
				indicePlaca.setInterpretacion(rsd.getString("valor"));
				
				lista.add(indicePlaca);
			}
			rsd.close();
			psd.close();
		}catch (SQLException e) {
			logger.error("Error consultando interpretacion indice placa", e);
		}
		UtilidadBD.closeConnection(con);
		return lista;
	}


    
	/**
	 * Metodo que se encarga de consultar las especialidades asociadas a un profesional 
	 * @param codigoMedico
	 * @return lista de especialidades asociadas al medico ingresado
	 */
	public static ArrayList<DtoEspecialidades> consultarEspecialidadesMedico(Integer codigoMedico){
		Connection con = UtilidadBD.abrirConexion();
		ArrayList<DtoEspecialidades> lista = new ArrayList<DtoEspecialidades>();
		//sql que trae las especialidades asosiadas a un usuario 
		String query = "SELECT e.codigo,  e.nombre ,p.primer_nombre||' '||p.segundo_nombre||' '||p.primer_apellido||' '||p.segundo_apellido NOMPRE_PROFESIONAL,m.numero_registro " +
				"   FROM MEDICOS m,  ESPECIALIDADES_MEDICOS em,  ESPECIALIDADES e, " +
				"PERSONAS p " +
				"WHERE em.codigo_medico = m.CODIGO_MEDICO " +
				" AND em.codigo_especialidad = e.CODIGO" +
				" AND m.codigo_medico=p.CODIGO " +
				" AND p.codigo=?" +
				" ORDER BY  p.codigo ASC";
		try{
			
			PreparedStatementDecorator psd=new PreparedStatementDecorator(con, query);
			//se settea el valor del parametro de busqueda, codigo del usuario 
			psd.setInt(1, codigoMedico);
			ResultSetDecorator rsd=new ResultSetDecorator(psd.executeQuery());
			while(rsd.next())
			{
				//se obtienen los valores del resultado de la consulta 
				DtoEspecialidades especialidadMedico=new DtoEspecialidades();
				especialidadMedico.setCodigo(rsd.getInt("CODIGO"));
				especialidadMedico.setDescripcion(rsd.getString("NOMBRE"));
				especialidadMedico.setNombreProfesional(rsd.getString("NOMPRE_PROFESIONAL"));
				especialidadMedico.setNumeroRegistro(rsd.getString("NUMERO_REGISTRO"));
				
				//se adiciona los objetos con el resultado de la consulta 
				lista.add(especialidadMedico);
			}
			rsd.close();
			psd.close();
		}//control de errores 
		catch (SQLException e) {
			logger.error("Error consultando interpretacion especialidades de medico", e);
		}
		UtilidadBD.closeConnection(con);
		
		return lista;
		
	}
    
	/**
	 * Método encargado de obtener el hallazgo odontologico especificado
	 * @param Connection con
	 * @param ArrayList<Integer> hallazgosDentales
	 * @return ArrayList<TipoHallazgoCeoCop> 
	 */
	public static ArrayList<TipoHallazgoCeoCop> consultarCEOCOP(Connection con, ArrayList<Integer> hallazgosDentales) {
		
		PreparedStatementDecorator ps = null;
		ResultSetDecorator rs = null;
		ArrayList<TipoHallazgoCeoCop> tiposHallazgos = new ArrayList<TipoHallazgoCeoCop>();
		String cadenaConsulta = consultaTipoHallazgo;
		String cadenaIN = "";
		
		try
		{
			if (!hallazgosDentales.isEmpty()) {
				
				cadenaIN = hallazgosDentales.toString();
				cadenaIN = cadenaIN.replace("[", "");
				cadenaIN = cadenaIN.replace("]", "");

				cadenaConsulta += " AND hallodo.consecutivo IN ( " + cadenaIN + " ) ";

				ps = new PreparedStatementDecorator(con.prepareStatement(cadenaConsulta,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
				rs = new ResultSetDecorator(ps.executeQuery());

				while(rs.next()) {
					TipoHallazgoCeoCop tipoHallazgo = new TipoHallazgoCeoCop(rs.getLong(1), rs.getString(2));
					tiposHallazgos.add(tipoHallazgo);
				}
				ps.close();
				rs.close();
				
			}
		}
		catch (SQLException e) 
		{
			Log4JManager.error("Error en Consulta Hallazgos Odontologicos Cadena>> "+cadenaConsulta, e);
		}
		
		return tiposHallazgos;
	}

}