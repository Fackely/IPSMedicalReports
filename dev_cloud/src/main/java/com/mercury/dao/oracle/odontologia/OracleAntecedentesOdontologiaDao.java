package com.mercury.dao.oracle.odontologia;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

import com.mercury.dao.odontologia.AntecedentesOdontologiaDao;
import com.mercury.dao.sqlbase.odontologia.SqlBaseAntecedentesOdontologiaDao;

public class OracleAntecedentesOdontologiaDao implements AntecedentesOdontologiaDao
{
    public boolean existenAntecedentes(Connection con, int codigoPaciente) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.existenAntecedentes(con, codigoPaciente);
    }
    
    public int insertarAntecedenteGeneral(Connection con, int codigoPaciente) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.insertarAntecedenteGeneral(con, codigoPaciente);
    }
    
    public int insertar(Connection con, int codPaciente, String observaciones) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.insertar(con, codPaciente, observaciones);
    }
    
    public ResultSetDecorator consultar(Connection con, int codPaciente) throws SQLException
    {
        return new ResultSetDecorator(SqlBaseAntecedentesOdontologiaDao.consultar(con, codPaciente));
    }

    public boolean existe(Connection con, int codPaciente) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.existe(con, codPaciente);
    }
    
    public int modificar(Connection con, int codPaciente, String observaciones) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.modificar(con, codPaciente, observaciones);
    }
    
    public int insertarHabito(Connection con, int codPaciente, int codTipoHabitoOdoInst, String observaciones) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.insertarHabito(con, codPaciente, codTipoHabitoOdoInst, observaciones);
    }
    
    public ResultSetDecorator consultarHabitos(Connection con, int codPaciente) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.consultarHabitos(con, codPaciente);
    }
    
    public boolean existeHabito(Connection con, int codPaciente, int codTipoHabitoOdoInst) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.existeHabito(con, codPaciente, codTipoHabitoOdoInst);
    }
    
    public int modificarHabito(Connection con, int codPaciente, int codTipoHabitoOdoInst, String observaciones) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.modificarHabito(con, codPaciente, codTipoHabitoOdoInst, observaciones);
    }
    
    public int insertarHabitoOtro(Connection con, int codPaciente, int codigo, String nombre, String observaciones) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.insertarHabitoOtro(con, codPaciente, codigo, nombre, observaciones);
    }
    
    public ResultSetDecorator consultarHabitosOtros(Connection con, int codPaciente) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.consultarHabitosOtros(con, codPaciente);
    }
    
    public boolean existeHabitoOtro(Connection con, int codPaciente, int codigo) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.existeHabitoOtro(con, codPaciente, codigo);
    }

    public int modificarHabitoOtro(Connection con, int codPaciente, int codigo, String observaciones) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.modificarHabitoOtro(con, codPaciente, codigo, observaciones);
    }
    
    public int insertarTraumatismo(Connection con, int codPaciente, int codTipoTraumatismoOdoInst, String observaciones) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.insertarTraumatismo(con, codPaciente, codTipoTraumatismoOdoInst, observaciones);
    }
    
    public ResultSetDecorator consultarTraumatismos(Connection con, int codPaciente) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.consultarTraumatismos(con, codPaciente);
    }
    
    public boolean existeTraumatismo(Connection con, int codPaciente, int codTipoTraumatismoOdoInst) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.existeTraumatismo(con, codPaciente, codTipoTraumatismoOdoInst);
    }
    
    public int modificarTraumatismo(Connection con, int codPaciente, int codTipoTraumatismoOdoInst, String observaciones) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.modificarTraumatismo(con, codPaciente, codTipoTraumatismoOdoInst, observaciones);
    }

    public int insertarTraumatismoOtro(Connection con, int codPaciente, int codigo, String nombre, String observaciones) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.insertarTraumatismoOtro(con, codPaciente, codigo, nombre, observaciones);
    }
    
    public ResultSetDecorator consultarTraumatismosOtros(Connection con, int codPaciente) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.consultarTraumatismosOtros(con, codPaciente);
    }
    
    public boolean existeTraumatismoOtro(Connection con, int codPaciente, int codigo) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.existeTraumatismoOtro(con, codPaciente, codigo);
    }

    public int modificarTraumatismoOtro(Connection con, int codPaciente, int codigo, String observaciones) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.modificarTraumatismoOtro(con, codPaciente, codigo, observaciones);
    }

    public int insertarTratamientoPrevio(Connection con, int codPaciente, int codigo, String tipoTratamiento, String fechaInicio, String fechaFinalizacion, String descripcion) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.insertarTratamientoPrevio(con, codPaciente, codigo, tipoTratamiento, fechaInicio, fechaFinalizacion, descripcion);
    }
    
    public ResultSetDecorator consultarTratamientosPrevios(Connection con, int codPaciente) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.consultarTratamientosPrevios(con, codPaciente);
    }

    public boolean existeTratamientoPrevio(Connection con, int codPaciente, int codigo) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.existeTratamientoPrevio(con, codPaciente, codigo);
    }

    public int modificarTratamientoPrevio(Connection con, int codPaciente, int codigo, String fechaInicio, String fechaFinalizacion, String descripcion) throws SQLException
    {
        return SqlBaseAntecedentesOdontologiaDao.modificarTratamientoPrevio(con, codPaciente, codigo, fechaInicio, fechaFinalizacion, descripcion);
    }
}
