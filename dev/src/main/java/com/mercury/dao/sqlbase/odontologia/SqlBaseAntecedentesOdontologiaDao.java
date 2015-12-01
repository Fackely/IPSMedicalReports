/*
 * Created on 22/07/2005
 *
 * Mercury Todo los Derechos Reservados
 */
package com.mercury.dao.sqlbase.odontologia;

import java.sql.*;

import org.apache.log4j.Logger;

import util.ConstantesBD;

import com.mercury.util.UtilidadBaseDatos;
import com.princetonsa.decorator.PreparedStatementDecorator;
import com.princetonsa.decorator.ResultSetDecorator;

/**
 * @author Alejo    
 *
 * Mercury (Parquesoft - Manizales)
 */
public class SqlBaseAntecedentesOdontologiaDao 
{
    /**
    * Objeto para manejar los logs de esta clase
    */
    private static Logger logger = Logger.getLogger(SqlBaseAntecedentesOdontologiaDao.class);
    
    private static final String existeAntecedentePacienteStr= 
        "SELECT count(*) as antecedentes "+
        "FROM antecedentes_pacientes "+
        "WHERE codigo_paciente=?";

    private static final String insertarAntecedentePacienteStr=
        "INSERT INTO antecedentes_pacientes "+ 
        "(codigo_paciente) "+
        "VALUES (?)";
           
    /**
     * Cadena constante con el <i>statement</i> necesario para insertar 
     * una entrada en la tabla antecedente_odontologia. (Esto hace parte de la inserción 
     * de los antecedentes de odontología. 
     */
    private final static String insertarAntecedenteOdontologiaStr=
        "INSERT INTO antecedente_odontologia (" +
        "cod_paciente, "+
        "observaciones) values (?, ?)";
                                                                                            
    /**
     * Cadena constante con el <i>statemente</i> para cargar un antecedente de odontologia
     */ 
    private final static String consultarAntecedenteOdontologiaStr=
        "SELECT " +
        "cod_paciente as codPaciente, " +
        "observaciones as observaciones " +
        "FROM antecedente_odontologia where cod_paciente=?";
    
    /**
     * Cadena constante con el <i>statement</i> necesario para modificar 
     * una entrada en la tabla antecedente_odontologia. (Esto hace parte de la modificación 
     * de los antecedentes de odontología. 
     */
    private final static String modificarAntecedenteOdontologiaStr=
        "UPDATE antecedente_odontologia set observaciones=? where cod_paciente=?";
    
    private final static String existeAntecedenteOdontologiaStr =
        "SELECT COUNT(*) as totAntecedente " +
        "FROM antecedente_odontologia " +
        "WHERE cod_paciente = ?";

    private final static String insertarHabitoOdontologiaStr=
        "INSERT INTO habito_odo (" +
        "cod_paciente," +
        "cod_tipo_habito_odo_inst, " +
        "observaciones) values (?, ?, ?)";
    
    private final static String consultarHabitosOdontologiaStr=
        "SELECT "+
        "cod_paciente as codPaciente, " +
        "cod_tipo_habito_odo_inst as codTipoHabitoOdo, " +
        "getNombreTipoHabitoOdoInst(cod_tipo_habito_odo_inst) as nomTipoHabitoOdo, " +
        "observaciones as observaciones " +
        "FROM habito_odo where cod_paciente=?";
    
    private final static String existeHabitoOdontologiaStr =
        "SELECT COUNT(*) as totHabito " +
        "FROM habito_odo " +
        "WHERE cod_paciente = ? and " +
        "cod_tipo_habito_odo_inst = ?";

    private final static String modificarHabitoOdontologiaStr=
        "UPDATE " +
        "habito_odo SET " +
        "observaciones=? where " +
        "cod_paciente=? and " +
        "cod_tipo_habito_odo_inst=?";
    
    private final static String insertarHabitoOdontologiaOtroStr=
        "INSERT INTO habito_odo_otr (" +
        "cod_paciente," +
        "codigo, " +
        "nombre, " +
        "observaciones) values (?, ?, ?, ?)";
    
    private final static String consultarHabitosOdontologiaOtrosStr=
        "SELECT "+
        "cod_paciente as codPaciente, " +
        "codigo as codigo, " +
        "nombre as nombre, " +
        "observaciones as observaciones " +
        "FROM habito_odo_otr where cod_paciente=?";
    
    private final static String existeHabitoOdontologiaOtroStr =
        "SELECT COUNT(*) as totHabitoOtro " +
        "FROM habito_odo_otr " +
        "WHERE cod_paciente = ? and " +
        "codigo = ?";

    private final static String modificarHabitoOdontologiaOtroStr=
        "UPDATE " +
        "habito_odo_otr SET " +
        "observaciones=? where " +
        "cod_paciente=? and " +
        "codigo=?";
    
    private final static String insertarTraumatismoOdontologiaStr=
        "INSERT INTO traumatismo_odo (" +
        "cod_paciente," +
        "cod_tipo_traumatismo_odo_inst, " +
        "observaciones) values (?, ?, ?)";
    
    private final static String consultarTraumatismosOdontologiaStr=
        "SELECT "+
        "cod_paciente as codPaciente, " +
        "cod_tipo_traumatismo_odo_inst as codTipoTraumatismoOdo, " +
        "getNombTipoTraumatismoOdoInst(cod_tipo_traumatismo_odo_inst) as nomTipoTraumatismoOdo, " +
        "observaciones as observaciones " +
        "FROM traumatismo_odo where cod_paciente=?";
    
    private final static String existeTraumatismoOdontologiaStr =
        "SELECT COUNT(*) as totTraumatismo " +
        "FROM traumatismo_odo " +
        "WHERE cod_paciente = ? and " +
        "cod_tipo_traumatismo_odo_inst = ?";

    private final static String modificarTraumatismoOdontologiaStr=
        "UPDATE " +
        "traumatismo_odo SET " +
        "observaciones=? where " +
        "cod_paciente=? and " +
        "cod_tipo_traumatismo_odo_inst=?";
    
    private final static String insertarTraumatismoOdontologiaOtroStr=
        "INSERT INTO traumatismo_odo_otr (" +
        "cod_paciente," +
        "codigo, " +
        "nombre, " +
        "observaciones) values (?, ?, ?, ?)";
    
    private final static String consultarTraumatismosOdontologiaOtrosStr=
        "SELECT "+
        "cod_paciente as codPaciente, " +
        "codigo as codigo, " +
        "nombre as nombre, " +
        "observaciones as observaciones " +
        "FROM traumatismo_odo_otr where cod_paciente=?";
    
    private final static String existeTraumatismoOdontologiaOtroStr =
        "SELECT COUNT(*) as totTraumatismoOtro " +
        "FROM traumatismo_odo_otr " +
        "WHERE cod_paciente = ? and " +
        "codigo = ?";

    private final static String modificarTraumatismoOdontologiaOtroStr=
        "UPDATE " +
        "traumatismo_odo_otr SET " +
        "observaciones=? where " +
        "cod_paciente=? and " +
        "codigo=?";
    
    private final static String insertarTratamientoOdontologiaPrevioStr=
        "INSERT INTO tratamiento_odo_previo (" +
        "cod_paciente, " +
        "codigo, " +
        "tipo_tratamiento, " +
        "fecha_inicio, " +
        "fecha_finalizacion, " +
        "descripcion) values (?, ?, ?, ?, ?, ?)";
    
    private final static String consultarTratamientosOdontologiaPreviosStr=
        "SELECT "+
        "cod_paciente as codPaciente, " +
        "codigo as codigo, " +
        "tipo_tratamiento as tipoTratamiento, " +
        "fecha_inicio as fechaInicio, " +
        "fecha_finalizacion as fechaFinalizacion, " +
        "descripcion as descripcion " +
        "FROM tratamiento_odo_previo where cod_paciente=?";
    
    private final static String existeTratamientoOdontologiaPrevioStr =
        "SELECT COUNT(*) as totTratamientoPrevio " +
        "FROM tratamiento_odo_previo " +
        "WHERE cod_paciente = ? and " +
        "codigo = ?";

    private final static String modificarTratamientoOdontologiaPrevioStr=
        "UPDATE " +
        "tratamiento_odo_previo SET " +
        "fecha_inicio = ?, " +
        "fecha_finalizacion = ?, " +
        "descripcion = ? where " +
        "cod_paciente=? and " +
        "codigo=?";
    
    public static boolean existenAntecedentes(Connection con, int codigoPaciente) throws SQLException
    {
        try
        {
            PreparedStatementDecorator existeAntecedentePaciente =  new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.existeAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
        
            existeAntecedentePaciente.setInt(1, codigoPaciente);
        
            ResultSetDecorator resultado = new ResultSetDecorator(existeAntecedentePaciente.executeQuery());
        
            if( resultado.next() )
            {
                int numFilas = resultado.getInt("antecedentes");
    
                if( numFilas <= 0 )
                {
                    return false;
                }
                else
                {
                    return true;
                }
            }
            else
            {
                logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
                throw new SQLException("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente (no retornó ningún registro) : "+codigoPaciente+". \n");
            }
        }
        catch( SQLException e )
        {
            logger.warn("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e);
            throw new SQLException("Hubo problemas consultando si el paciente tenía o no antecedentes de cualquier tipo previamente ingresados, para el paciente : "+codigoPaciente+". \n"+e.getMessage());
        }   
    }
    
    public static int insertarAntecedenteGeneral(Connection con, int codigoPaciente) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarAntecedenteGeneral =  new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.insertarAntecedentePacienteStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarAntecedenteGeneral.setInt(1, codigoPaciente);
            return  insertarAntecedenteGeneral.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn("Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \n"+e);
            throw new SQLException("Hubo problemas insertando en la tabla general de antecedentes, para el paciente : "+codigoPaciente+". \n"+e.getMessage());           
        }
    }

    public static int insertar(Connection con, int codPaciente, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.insertarAntecedenteOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codPaciente);
            UtilidadBaseDatos.establecerParametro(2, Types.VARCHAR, observaciones, insertarStatement);
            return insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultar(Connection con, int codPaciente) throws SQLException
    {
        try
        {
            PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.consultarAntecedenteOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            consultarStatement.setInt(1, codPaciente);
            return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static boolean existe(Connection con, int codPaciente) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.existeAntecedenteOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                ResultSetDecorator rs = new ResultSetDecorator(consultarStatement.executeQuery());
                if(rs.next())
                {
                    int totAntecedente = rs.getInt("totAntecedente");
                    if(totAntecedente>0)
                        return true;
                    else
                        return false;
                }
                else
                {
                    String mensajeError = "Hubo problemas consultando si el paciente tenía o no algun dato para el antecedente previamente ingresado, para el paciente (no retornó ningún registro) : "+codPaciente+". \n";
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
    
    public static int modificar(Connection con, int codPaciente, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.modificarAntecedenteOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codPaciente);
            return modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    public static int insertarHabito(Connection con, int codPaciente, int codTipoHabitoOdoInst, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.insertarHabitoOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codPaciente);
            insertarStatement.setInt(2, codTipoHabitoOdoInst);
            UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, observaciones, insertarStatement);
            return insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarHabitos(Connection con, int codPaciente) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.consultarHabitosOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static boolean existeHabito(Connection con, int codPaciente, int codTipoHabitoOdoInst) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.existeHabitoOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                consultarStatement.setInt(2, codTipoHabitoOdoInst);
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
                    String mensajeError = "Hubo problemas consultando si el paciente tenía o no algun dato para el habito "+codTipoHabitoOdoInst+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codPaciente+". \n";
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
    
    public static int modificarHabito(Connection con, int codPaciente, int codTipoHabitoOdoInst, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.modificarHabitoOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codPaciente);
            modificarStatement.setInt(3, codTipoHabitoOdoInst);
            return modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    public static int insertarHabitoOtro(Connection con, int codPaciente, int codigo, String nombre, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.insertarHabitoOdontologiaOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codPaciente);
            insertarStatement.setInt(2, codigo);
            UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, nombre, insertarStatement);
            UtilidadBaseDatos.establecerParametro(4, Types.VARCHAR, observaciones, insertarStatement);
            return insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarHabitosOtros(Connection con, int codPaciente) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.consultarHabitosOdontologiaOtrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static boolean existeHabitoOtro(Connection con, int codPaciente, int codigo) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.existeHabitoOdontologiaOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                consultarStatement.setInt(2, codigo);
                ResultSetDecorator rs = new ResultSetDecorator(consultarStatement.executeQuery());
                if(rs.next())
                {
                    int totHabitoOtro = rs.getInt("totHabitoOtro");
                    if(totHabitoOtro>0)
                        return true;
                    else
                        return false;
                }
                else
                {
                    String mensajeError = "Hubo problemas consultando si el paciente tenía o no algun dato para el habito "+codigo+" otro, previamente ingresado, para el paciente (no retornó ningún registro) : "+codPaciente+". \n";
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
    
    public static int modificarHabitoOtro(Connection con, int codPaciente, int codigo, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.modificarHabitoOdontologiaOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codPaciente);
            modificarStatement.setInt(3, codigo);
            return modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    public static int insertarTraumatismo(Connection con, int codPaciente, int codTipoTraumatismoOdoInst, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.insertarTraumatismoOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codPaciente);
            insertarStatement.setInt(2, codTipoTraumatismoOdoInst);
            UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, observaciones, insertarStatement);
            return insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarTraumatismos(Connection con, int codPaciente) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.consultarTraumatismosOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static boolean existeTraumatismo(Connection con, int codPaciente, int codTipoTraumatismoOdoInst) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.existeTraumatismoOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                consultarStatement.setInt(2, codTipoTraumatismoOdoInst);
                ResultSetDecorator rs = new ResultSetDecorator(consultarStatement.executeQuery());
                if(rs.next())
                {
                    int totTraumatismo = rs.getInt("totTraumatismo");
                    if(totTraumatismo>0)
                        return true;
                    else
                        return false;
                }
                else
                {
                    String mensajeError = "Hubo problemas consultando si el paciente tenía o no algun dato para el traumatismo "+codTipoTraumatismoOdoInst+" previamente ingresado, para el paciente (no retornó ningún registro) : "+codPaciente+". \n";
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
    
    public static int modificarTraumatismo(Connection con, int codPaciente, int codTipoTraumatismoOdoInst, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.modificarTraumatismoOdontologiaStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codPaciente);
            modificarStatement.setInt(3, codTipoTraumatismoOdoInst);
            return modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    public static int insertarTraumatismoOtro(Connection con, int codPaciente, int codigo, String nombre, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.insertarTraumatismoOdontologiaOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codPaciente);
            insertarStatement.setInt(2, codigo);
            UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, nombre, insertarStatement);
            UtilidadBaseDatos.establecerParametro(4, Types.VARCHAR, observaciones, insertarStatement);
            return insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarTraumatismosOtros(Connection con, int codPaciente) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.consultarTraumatismosOdontologiaOtrosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static boolean existeTraumatismoOtro(Connection con, int codPaciente, int codigo) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.existeTraumatismoOdontologiaOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                consultarStatement.setInt(2, codigo);
                ResultSetDecorator rs = new ResultSetDecorator(consultarStatement.executeQuery());
                if(rs.next())
                {
                    int totTraumatismoOtro = rs.getInt("totTraumatismoOtro");
                    if(totTraumatismoOtro>0)
                        return true;
                    else
                        return false;
                }
                else
                {
                    String mensajeError = "Hubo problemas consultando si el paciente tenía o no algun dato para el traumatismo "+codigo+" otro, previamente ingresado, para el paciente (no retornó ningún registro) : "+codPaciente+". \n";
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
    
    public static int modificarTraumatismoOtro(Connection con, int codPaciente, int codigo, String observaciones) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.modificarTraumatismoOdontologiaOtroStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            UtilidadBaseDatos.establecerParametro(1, Types.VARCHAR, observaciones, modificarStatement);
            modificarStatement.setInt(2, codPaciente);
            modificarStatement.setInt(3, codigo);
            return modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    public static int insertarTratamientoPrevio(Connection con, int codPaciente, int codigo, String tipoTratamiento, String fechaInicio, String fechaFinalizacion, String descripcion) throws SQLException
    {
        try
        {
            PreparedStatementDecorator insertarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.insertarTratamientoOdontologiaPrevioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            insertarStatement.setInt(1, codPaciente);
            insertarStatement.setInt(2, codigo);
            UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, tipoTratamiento, insertarStatement);
            //insertarStatement.setString(4, UtilidadFecha.conversionFormatoFechaABD(fechaInicio)); 
            //insertarStatement.setString(5, UtilidadFecha.conversionFormatoFechaABD(fechaFinalizacion)); 
            UtilidadBaseDatos.establecerParametro(4, Types.DATE, fechaInicio, insertarStatement);
            UtilidadBaseDatos.establecerParametro(5, Types.DATE, fechaFinalizacion, insertarStatement);
            UtilidadBaseDatos.establecerParametro(6, Types.VARCHAR, descripcion, insertarStatement);
           
            return insertarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
    
    public static ResultSetDecorator consultarTratamientosPrevios(Connection con, int codPaciente) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.consultarTratamientosOdontologiaPreviosStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                return new ResultSetDecorator(consultarStatement.executeQuery());
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }

    public static boolean existeTratamientoPrevio(Connection con, int codPaciente, int codigo) throws SQLException
    {
        try
        {
                PreparedStatementDecorator consultarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.existeTratamientoOdontologiaPrevioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
                consultarStatement.setInt(1, codPaciente);
                consultarStatement.setInt(2, codigo);
                ResultSetDecorator rs = new ResultSetDecorator(consultarStatement.executeQuery());
                if(rs.next())
                {
                    int totTratamientoPrevio = rs.getInt("totTratamientoPrevio");
                    if(totTratamientoPrevio>0)
                        return true;
                    else
                        return false;
                }
                else
                {
                    String mensajeError = "Hubo problemas consultando si el paciente tenía o no algun dato para el tratamiento "+codigo+" otro, previamente ingresado, para el paciente (no retornó ningún registro) : "+codPaciente+". \n";
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
    
    public static int modificarTratamientoPrevio(Connection con, int codPaciente, int codigo, String fechaInicio, String fechaFinalizacion, String descripcion) throws SQLException
    {
        try
        {
            PreparedStatementDecorator modificarStatement= new PreparedStatementDecorator(con.prepareStatement(SqlBaseAntecedentesOdontologiaDao.modificarTratamientoOdontologiaPrevioStr,ConstantesBD.typeResultSet,ConstantesBD.concurrencyResultSet));
            
            //modificarStatement.setString(1, UtilidadFecha.conversionFormatoFechaABD(fechaInicio));
            //modificarStatement.setString(2, UtilidadFecha.conversionFormatoFechaABD(fechaFinalizacion));
            UtilidadBaseDatos.establecerParametro(1, Types.DATE, fechaInicio, modificarStatement);
            UtilidadBaseDatos.establecerParametro(2, Types.DATE, fechaFinalizacion, modificarStatement);

            UtilidadBaseDatos.establecerParametro(3, Types.VARCHAR, descripcion, modificarStatement);
            modificarStatement.setInt(4, codPaciente);
            modificarStatement.setInt(5, codigo);
            return modificarStatement.executeUpdate();
        }
        catch(SQLException e)
        {
            logger.warn(e);
            throw e;
        }
    }
}
