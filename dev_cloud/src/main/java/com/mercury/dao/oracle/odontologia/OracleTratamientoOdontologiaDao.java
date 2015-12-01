package com.mercury.dao.oracle.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import com.mercury.dao.odontologia.TratamientoOdontologiaDao;
import com.mercury.dao.sqlbase.odontologia.SqlBaseTratamientoOdontologiaDao;
import com.mercury.dto.odontologia.DtoInterpretacionIndicePlaca;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.odontologia.DtoSectorSuperficieCuadrante;
import com.servinte.axioma.orm.TipoHallazgoCeoCop;
/**
 * 
 * @author axioma
 *
 */
public class OracleTratamientoOdontologiaDao implements
        TratamientoOdontologiaDao
{
    public int insertar(Connection con, int codPaciente,
            int codTipoTratOdoInst, int codMedico, String fecIniciacion,
            String diagPlanTrat,
            String observaciones, String motivoConsulta, String datosMedico, Integer especialidad) throws SQLException
    {
        String secuencia="seq_tratamiento_odo.nextval as codigo from dual "; 
        return SqlBaseTratamientoOdontologiaDao.insertar(
                con, 
                secuencia, 
                codPaciente, 
                codTipoTratOdoInst, 
                codMedico, 
                fecIniciacion, 
                diagPlanTrat,
                observaciones, motivoConsulta,  datosMedico,  especialidad);
    }
    @Override
    public ResultSetDecorator consultar(Connection con, int codigo) throws SQLException
    {
        return SqlBaseTratamientoOdontologiaDao.consultar(con, codigo);
    }

    public void modificar(Connection con, int codigo,
            String fecFinalizacion, String observaciones)
            throws SQLException
    {
        SqlBaseTratamientoOdontologiaDao.modificar(con, codigo, fecFinalizacion, observaciones);
    }

    public boolean existenTratamientosPaciente(Connection con, int codPaciente)
            throws SQLException
    {
        return SqlBaseTratamientoOdontologiaDao.existenTratamientosPaciente(con, codPaciente);
    }

    
    
    public ResultSetDecorator consultarTratamientosPaciente(Connection con,
            int codPaciente) throws SQLException
    {
        return SqlBaseTratamientoOdontologiaDao.consultarTratamientosPaciente(con, codPaciente);
    }

    public ResultSetDecorator consultarTratamientosSinFinalizarPaciente(Connection con,
            int codPaciente) throws SQLException
    {
        return SqlBaseTratamientoOdontologiaDao.consultarTratamientosSinFinalizarPaciente(con, codPaciente);
    }

    public void insertarEvolucion(Connection con, int codigo,
            int codTratamiento, String descripcion, int codMedico,
            String fecha, String observaciones) throws SQLException
    {
        SqlBaseTratamientoOdontologiaDao.insertarEvolucion(
                con,
                codigo,
                codTratamiento,
                descripcion,
                codMedico,
                fecha,
                observaciones);
    }

    public ResultSetDecorator consultarEvoluciones(Connection con, int codTratamiento)
            throws SQLException
    {
        return SqlBaseTratamientoOdontologiaDao.consultarEvoluciones(con, codTratamiento);
    }

    public boolean existenEvoluciones(Connection con, int codTratamiento)
            throws SQLException
    {
        return SqlBaseTratamientoOdontologiaDao.existenEvoluciones(con, codTratamiento);
    }

    public void modificarEvolucion(Connection con, int codigo,
            int codTratamiento, String observaciones) throws SQLException
    {
        SqlBaseTratamientoOdontologiaDao.modificarEvolucion(con, codigo, codTratamiento, observaciones);
    }

    public void insertarAnalisis(
            Connection  con,
            int         codTratamiento,
            int         codTipoAnalisis,
            String      codOpcionTipoAnalisis,
            String      comentario,
            String      observaciones) throws SQLException
    {
        SqlBaseTratamientoOdontologiaDao.insertarAnalisis(con, codTratamiento, codTipoAnalisis, codOpcionTipoAnalisis, comentario, observaciones);
    }
    
    public ResultSetDecorator consultarAnalisis(Connection con, int codTratamiento) throws SQLException
    {
        return SqlBaseTratamientoOdontologiaDao.consultarAnalisis(con, codTratamiento);
    }
    
    public void modificarAnalisis(Connection con, int codTipoAnalisis, int codTratamiento, String observaciones) throws SQLException
    {
        SqlBaseTratamientoOdontologiaDao.modificarAnalisis(con, codTipoAnalisis, codTratamiento, observaciones);
    }

    public boolean existeAnalisis(Connection con, int codTratamiento, int codTipoAnalisis) throws SQLException
    {
        return SqlBaseTratamientoOdontologiaDao.existeAnalisis(con, codTratamiento, codTipoAnalisis);
    }
    
    public void insertarObservacionesSeccion(
            Connection  con,
            int         codTratamiento,
            int         codSeccionTratamiento,
            String      observaciones) throws SQLException
    {
        SqlBaseTratamientoOdontologiaDao.insertarObservacionesSeccion(con, codTratamiento, codSeccionTratamiento, observaciones);
    }
    
    public ResultSetDecorator consultarObservacionesSeccion(Connection con, int codTratamiento) throws SQLException
    {
        return SqlBaseTratamientoOdontologiaDao.consultarObservacionesSeccion(con, codTratamiento);
    }
    
    public boolean existenObservacionesSeccion(Connection con, int codTratamiento, int codSeccionTratamiento) throws SQLException
    {
        return SqlBaseTratamientoOdontologiaDao.existenObservacionesSeccion(con, codTratamiento, codSeccionTratamiento);
    }
    
    public void modificarObservacionesSeccion(Connection con, int codTratamiento, int codSeccionTratamiento, String observaciones) throws SQLException
    {
        SqlBaseTratamientoOdontologiaDao.modificarObservacionesSeccion(con, codTratamiento, codSeccionTratamiento, observaciones);
    }

    public int insertarTratamientoDiente(
            Connection  con,
            int         numeroDiente,
            int			codSeccionTratamientoInst,
            int			codTratamiento,
            int			codMedico,
            String		fecha,
            String      observaciones) throws SQLException
    {
        String secuencia="seq_tratamiento_diente_odo.nextval as codigo from dual "; 
        return SqlBaseTratamientoOdontologiaDao.insertarTratamientoDiente(con, secuencia, numeroDiente, codSeccionTratamientoInst, codTratamiento, codMedico, fecha, observaciones);
    }
    
    public ResultSetDecorator consultarTratamientosDientes(Connection con, int codTratamiento) throws SQLException
    {
    	return SqlBaseTratamientoOdontologiaDao.consultarTratamientosDientes(con, codTratamiento);
    }
    
    public void modificarTratamientoDiente(Connection con, int codigo, String observaciones) throws SQLException
    {
    	SqlBaseTratamientoOdontologiaDao.modificarTratamientoDiente(con, codigo, observaciones);
    }

    public void insertarAnalisisDiente(
            Connection  con,
            int         codTratamientoDiente,
            int         codTipoAnalisisInst,
            String		codOpcionTipoAnalisis,
            String		comentario,
            String      observaciones) throws SQLException
    {
    	SqlBaseTratamientoOdontologiaDao.insertarAnalisisDiente(con, codTratamientoDiente, codTipoAnalisisInst, codOpcionTipoAnalisis, comentario, observaciones);
    }
    
    public ResultSetDecorator consultarAnalisisDiente(Connection con, int codTratamientoDiente) throws SQLException
    {
    	return SqlBaseTratamientoOdontologiaDao.consultarAnalisisDiente(con, codTratamientoDiente);
    }
    
    public void modificarAnalisisDiente(Connection con, int codTratamientoDiente, int codTipoAnalisisInst, String observaciones) throws SQLException
    {
    	SqlBaseTratamientoOdontologiaDao.modificarAnalisisDiente(con, codTratamientoDiente, codTipoAnalisisInst, observaciones);
    }

	public HashMap consultarProcedimiento(Connection con, int consecutivo) throws SQLException
	{
		return SqlBaseTratamientoOdontologiaDao.consultarProcedimiento(con, consecutivo);
	}

	public ResultSetDecorator consultarProcedimientos(Connection con, int codTratamiento) throws SQLException 
	{
		return SqlBaseTratamientoOdontologiaDao.consultarProcedimientos(con, codTratamiento);
	}

	public void insertarProcedimiento(
    		Connection con, int codServicio, int codTratamiento, int prioridad, String fechaRegistro, int codMedicoRegistra, 
    		String fechaPlaneado, int codSuperficieDental, int diente, String observaciones, int estado, String fechaCerrado, String codMedicoCierra) throws SQLException
	{
		SqlBaseTratamientoOdontologiaDao.insertarProcedimiento(con, codServicio, codTratamiento, prioridad, fechaRegistro, codMedicoRegistra, 
				fechaPlaneado, codSuperficieDental, diente, observaciones, estado,fechaCerrado,codMedicoCierra);
	}

	public void modificarProcedimiento(Connection con, int consecutivo, int prioridad, int estado, String fechaCerrado, String codMedicoCierra, String observaciones) throws SQLException 
	{
		SqlBaseTratamientoOdontologiaDao.modificarProcedimiento(con, consecutivo, prioridad, estado, fechaCerrado, codMedicoCierra, observaciones);
	}

	public int insertarConsentimientoInformado(Connection con, String fecha, int codMedico, int codTratamiento, int codTipo) throws SQLException 
	{		
		return SqlBaseTratamientoOdontologiaDao.insertarConsentimientoInformado(con, fecha, codMedico, codTratamiento, codTipo);
	}

	public void insertarValorOpcionConsentimientoInformado(Connection con, int codConsentimiento, int codOpcionTipoConsentimiento, String valor) throws SQLException 
	{
		SqlBaseTratamientoOdontologiaDao.insertarValorOpcionConsentimientoInformado(con, codConsentimiento, codOpcionTipoConsentimiento, valor);
	}

	public ResultSetDecorator consultarConsentimientosInformados(Connection con, int codTratamiento) throws SQLException 
	{
		return SqlBaseTratamientoOdontologiaDao.consultarConsentimientosInformados(con, codTratamiento);
	}

	public ResultSetDecorator consultarValoresOpcionesConsentimientoInformado(Connection con, int codConsentimiento) throws SQLException 
	{
		return SqlBaseTratamientoOdontologiaDao.consultarValoresOpcionesConsentimientoInformado(con, codConsentimiento);
	}

	@Override
	public HashMap consultarDiagnosicosCartaDent(Connection con, int codInst) {
		// TODO Auto-generated method stub
		return SqlBaseTratamientoOdontologiaDao.consultarDiagnosticosCartaDental(con, codInst);
	}

	@Override
	public HashMap consultarTratamientosCartaDent(Connection con, int codigoInst) {
		// TODO Auto-generated method stub
		return SqlBaseTratamientoOdontologiaDao.consultarTratamientosCartaDental(con, codigoInst);
	}

	@Override
	public HashMap consultarSuperficiesCartaDental(Connection con, int codigInst) {
		// TODO Auto-generated method stub
		return SqlBaseTratamientoOdontologiaDao.consultarSuperficiesCartaDental(con, codigInst);
	}
	
	@Override
	public ArrayList<DtoSectorSuperficieCuadrante> cargarSuperficiesDiente(int codigoInstitucion) {
		return SqlBaseTratamientoOdontologiaDao.cargarSuperficiesDiente(codigoInstitucion);
	}
	
	@Override
	public ArrayList<DtoInterpretacionIndicePlaca> cargarInterpretacionIndicePlaca(int codigoInstitucion)
	{
		return SqlBaseTratamientoOdontologiaDao.cargarInterpretacionIndicePlaca(codigoInstitucion);
	}


	/**
	 * @see com.mercury.dao.odontologia.TratamientoOdontologiaDao#consultarEspecialidadesMedico(java.lang.Integer)
	 */
	public ArrayList<DtoEspecialidades> consultarEspecialidadesMedico(Integer codigoMedico){
		return SqlBaseTratamientoOdontologiaDao.consultarEspecialidadesMedico(codigoMedico);
	}

	/*
	 * (non-Javadoc)
	 * @see com.princetonsa.dao.odontologia.HallazgosOdontologicosDao#consultarCEOCOP(java.sql.Connection, java.util.ArrayList)
	 */
	@Override
	public ArrayList<TipoHallazgoCeoCop> consultarCEOCOP(Connection con, ArrayList<Integer> hallazgosDentales) {
		return SqlBaseTratamientoOdontologiaDao.consultarCEOCOP(con, hallazgosDentales);
	}
}