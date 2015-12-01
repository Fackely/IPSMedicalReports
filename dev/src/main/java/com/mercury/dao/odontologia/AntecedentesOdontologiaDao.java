/*
 * Created on 22/07/2005
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
package com.mercury.dao.odontologia;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import java.sql.SQLException;

/**
 * @author Alejo
 *
 * 
 * Window - Preferences - Java - Code Style - Code Templates
 */
public interface AntecedentesOdontologiaDao
{
    public boolean existenAntecedentes(Connection con, int codigoPaciente) throws SQLException;
    
    public int insertarAntecedenteGeneral(Connection con, int codigoPaciente) throws SQLException;
    
    public int insertar(Connection con, int codPaciente, String observaciones) throws SQLException;
    
    public ResultSetDecorator consultar(Connection con, int codPaciente) throws SQLException;

    public int modificar(Connection con, int codPaciente, String observaciones) throws SQLException;
    
    public boolean existe(Connection con, int codPaciente) throws SQLException;
    
    public int insertarHabito(Connection con, int codPaciente, int codTipoHabitoOdoInst, String observaciones) throws SQLException;
    
    public ResultSetDecorator consultarHabitos(Connection con, int codPaciente) throws SQLException;
    
    public boolean existeHabito(Connection con, int codPaciente, int codTipoHabitoOdoInst) throws SQLException;
    
    public int modificarHabito(Connection con, int codPaciente, int codTipoHabitoOdoInst, String observaciones) throws SQLException;

    public int insertarHabitoOtro(Connection con, int codPaciente, int codigo, String nombre, String observaciones) throws SQLException;
    
    public ResultSetDecorator consultarHabitosOtros(Connection con, int codPaciente) throws SQLException;
    
    public boolean existeHabitoOtro(Connection con, int codPaciente, int codigo) throws SQLException;
    
    public int modificarHabitoOtro(Connection con, int codPaciente, int codigo, String observaciones) throws SQLException;

    public int insertarTraumatismo(Connection con, int codPaciente, int codTipoTraumatismoOdoInst, String observaciones) throws SQLException;
    
    public ResultSetDecorator consultarTraumatismos(Connection con, int codPaciente) throws SQLException;
    
    public boolean existeTraumatismo(Connection con, int codPaciente, int codTipoTraumatismoOdoInst) throws SQLException;
    
    public int modificarTraumatismo(Connection con, int codPaciente, int codTipoTraumatismoOdoInst, String observaciones) throws SQLException;

    public int insertarTraumatismoOtro(Connection con, int codPaciente, int codigo, String nombre, String observaciones) throws SQLException;
    
    public ResultSetDecorator consultarTraumatismosOtros(Connection con, int codPaciente) throws SQLException;
    
    public boolean existeTraumatismoOtro(Connection con, int codPaciente, int codigo) throws SQLException;
    
    public int modificarTraumatismoOtro(Connection con, int codPaciente, int codigo, String observaciones) throws SQLException;

    public int insertarTratamientoPrevio(Connection con, int codPaciente, int codigo, String tipoTratamiento, String fechaInicio, String fechaFinalizacion, String descripcion) throws SQLException;
    
    public ResultSetDecorator consultarTratamientosPrevios(Connection con, int codPaciente) throws SQLException;
    
    public boolean existeTratamientoPrevio(Connection con, int codPaciente, int codigo) throws SQLException;

    public int modificarTratamientoPrevio(Connection con, int codPaciente, int codigo, String fechaInicio, String fechaFinalizacion, String descripcion) throws SQLException;
}