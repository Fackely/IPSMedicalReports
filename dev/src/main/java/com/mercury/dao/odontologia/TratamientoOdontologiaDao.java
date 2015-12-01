package com.mercury.dao.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mercury.dto.odontologia.DtoInterpretacionIndicePlaca;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.servinte.axioma.orm.TipoHallazgoCeoCop;

public interface TratamientoOdontologiaDao
{	
    public int insertar(
            Connection con,
            int codPaciente, 
            int codTipoTratOdoInst,
            int codMedico,
            String fecIniciacion,
            String diagPlanTrat,
            String observaciones,
            String motivoConsulta, String datosMedico, Integer especialidad) throws SQLException;
    /**
     * 
     * @param con
     * @param codigo
     * @return
     * @throws SQLException
     */
    public ResultSetDecorator consultar(Connection con, int codigo) throws SQLException;
    
    public void modificar(
            Connection con, 
            int codigo, 
            String fecFinalizacion, String observaciones) throws SQLException;

    public boolean existenTratamientosPaciente(Connection con, int codPaciente) throws SQLException;
    
    
    /**
     * 
     * @param con
     * @param codPaciente
     * @return
     * @throws SQLException
     */
    public ResultSetDecorator consultarTratamientosPaciente(Connection con, int codPaciente) throws SQLException;
    
    public ResultSetDecorator consultarTratamientosSinFinalizarPaciente(Connection con, int codPaciente) throws SQLException;

    public void insertarEvolucion(
            Connection  con,
            int         codigo,
            int         codTratamiento,
            String      descripcion,
            int         codMedico,
            String      fecha,
            String      observaciones) throws SQLException;
    
    public ResultSetDecorator consultarEvoluciones(Connection con, int codTratamiento) throws SQLException;
    
    public boolean existenEvoluciones(Connection con, int codTratamiento) throws SQLException;
    
    public void modificarEvolucion(Connection con, int codigo, int codTratamiento, String observaciones) throws SQLException;

    public void insertarAnalisis(
            Connection  con,
            int         codTratamiento,
            int         codTipoAnalisis,
            String      codOpcionTipoAnalisis,
            String      comentario,
            String      observaciones) throws SQLException;
    
    /**
     * 
     * @param con
     * @param codTratamiento
     * @return
     * @throws SQLException
     */
    public ResultSetDecorator consultarAnalisis(Connection con, int codTratamiento) throws SQLException;
    
    /**
     * 
     * @param con
     * @param codTipoAnalisis
     * @param codTratamiento
     * @param observaciones
     * @throws SQLException
     */
    public void modificarAnalisis(Connection con, int codTipoAnalisis, int codTratamiento, String observaciones) throws SQLException;    

    public boolean existeAnalisis(Connection con, int codTratamiento, int codTipoAnalisis) throws SQLException;
    
    public void insertarObservacionesSeccion(
            Connection  con,
            int         codTratamiento,
            int         codSeccionTratamiento,
            String      observaciones) throws SQLException;
    
    public ResultSetDecorator consultarObservacionesSeccion(Connection con, int codTratamiento) throws SQLException;
    
    public boolean existenObservacionesSeccion(Connection con, int codTratamiento, int codSeccionTratamiento) throws SQLException;
    
    public void modificarObservacionesSeccion(Connection con, int codTratamiento, int codSeccionTratamiento, String observaciones) throws SQLException;    

    public int insertarTratamientoDiente(
            Connection  con,
            int         numeroDiente,
            int			codSeccionTratamientoInst,
            int			codTratamiento,
            int			codMedico,
            String		fecha,
            String      observaciones) throws SQLException;
    
    public ResultSetDecorator consultarTratamientosDientes(Connection con, int codTratamiento) throws SQLException;
    
    public void modificarTratamientoDiente(Connection con, int codigo, String observaciones) throws SQLException;

    public void insertarAnalisisDiente(
            Connection  con,
            int         codTratamientoDiente,
            int         codTipoAnalisisInst,
            String		codOpcionTipoAnalisis,
            String		comentario,
            String      observaciones) throws SQLException;
    
    public ResultSetDecorator consultarAnalisisDiente(Connection con, int codTratamientoDiente) throws SQLException;
    
    public void modificarAnalisisDiente(Connection con, int codTratamientoDiente, int codTipoAnalisisInst, String observaciones) throws SQLException;

    public void insertarProcedimiento(
    		Connection con, int codServicio, int codTratamiento, int prioridad, String fechaRegistro, int codMedicoRegistra, 
    		String fechaPlaneado, int codSuperficieDental, int diente, String observaciones, int estado, String fechaCerrado, String codMedicoCierra) throws SQLException;
    
    public HashMap consultarProcedimiento(Connection con, int consecutivo) throws SQLException;
    
    public ResultSetDecorator consultarProcedimientos(Connection con, int codTratamiento) throws SQLException;
    
    public void modificarProcedimiento(
    		Connection con, int consecutivo, int prioridad, int estado, String fechaCerrado, String codMedicoCierra, String observaciones) throws SQLException;

    public int insertarConsentimientoInformado(Connection con, String fecha, int codMedico, int codTratamiento, int codTipo) throws SQLException;
    
    public void insertarValorOpcionConsentimientoInformado(Connection con, int codConsentimiento, int codOpcionTipoConsentimiento, String valor) throws SQLException;
    
    public ResultSetDecorator consultarConsentimientosInformados(Connection con, int codTratamiento) throws SQLException;
    
    public ResultSetDecorator consultarValoresOpcionesConsentimientoInformado(Connection con, int codConsentimiento) throws SQLException;
   
    public HashMap consultarDiagnosicosCartaDent(Connection con, int codInst);

	public HashMap consultarTratamientosCartaDent(Connection con, int codigoInst);

	public HashMap consultarSuperficiesCartaDental(Connection con, int codigInst);

	public ArrayList<DtoSectorSuperficieCuadrante> cargarSuperficiesDiente(int codigoInstitucion);

	public ArrayList<DtoInterpretacionIndicePlaca> cargarInterpretacionIndicePlaca(int codigoInstitucion);

	/**
	 * Lista de especialidades asociadas al medico ingresado por parametro
	 * @param codigoMedico
	 * @return lista de especialidades
	 */
	public ArrayList<DtoEspecialidades> consultarEspecialidadesMedico(Integer codigoMedico);

	/**
	 * Método encargado de obtener el hallazgo odontologico especificado
	 * @param Connection con
	 * @param ArrayList<Integer> hallazgosDentales
	 * @return ArrayList<TipoHallazgoCeoCop> 
	 */
	public ArrayList<TipoHallazgoCeoCop> consultarCEOCOP(Connection con, ArrayList<Integer> hallazgosDentales);
	
}
