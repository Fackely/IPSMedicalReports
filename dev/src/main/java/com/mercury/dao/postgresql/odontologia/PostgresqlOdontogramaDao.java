package com.mercury.dao.postgresql.odontologia;

import java.sql.Connection;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;

import java.sql.SQLException;
import java.util.ArrayList;

import com.mercury.dao.odontologia.OdontogramaDao;
import com.mercury.dao.sqlbase.odontologia.SqlBaseOdontogramaDao;
import com.mercury.dto.odontologia.DtoOtroHallazgo;
import com.mercury.mundo.odontologia.Odontograma;

public class PostgresqlOdontogramaDao implements OdontogramaDao
{
	@Override
    public int insertar(
            Connection con,
            String codTratamientoOdo,
            String observaciones,
            int codMedico, 
            String fecha, String xmlOdontograma,
            String imagen) throws SQLException
    {
		String secuencia="nextval('historiaclinica.seq_odontograma') as codigo"; 
        return SqlBaseOdontogramaDao.insertar(
                con, 
                secuencia,
                codTratamientoOdo,
                observaciones,
                codMedico,
                fecha, 
                xmlOdontograma, 
                imagen);
    }
    
    public ResultSetDecorator consultarOdontogramasTratamiento(Connection con, int codTratamiento) throws SQLException
    {
        return SqlBaseOdontogramaDao.consultarOdontogramasTratamiento(con, codTratamiento);
    }

    public void consultar(Connection con, int codigo, Odontograma objOdontograma) throws SQLException
    {
        SqlBaseOdontogramaDao.consultar(con, codigo, objOdontograma);
    }

    public int modificar(
            Connection con, 
            int codigo, 
            String observaciones) throws SQLException
    {
        return SqlBaseOdontogramaDao.modificar(con, codigo, observaciones);
    }

    public int insertarDiente(Connection con, int codOdontograma, int numDiente, int codEstadoDienteInst) throws SQLException
    {
        return SqlBaseOdontogramaDao.insertarDiente(con, codOdontograma, numDiente, codEstadoDienteInst);
    }
    
   
    
    public int insertarSector(Connection con, int codOdontograma, int numDiente, int numSector, int codEstadoSectorInst) throws SQLException
    {
        return SqlBaseOdontogramaDao.insertarSector(con, codOdontograma, numDiente, numSector, codEstadoSectorInst);
    }

   

	public ResultSetDecorator consultarDientesPacientePorEstados(Connection con, int codPaciente, int[] estados) throws SQLException
	{
		return SqlBaseOdontogramaDao.consultarDientesPacientePorEstados(con, codPaciente, estados);
	}
	@Override
	public ArrayList<DtoOdontograma> cargar(DtoOdontograma dtoWhere) {
		
		return SqlBaseOdontogramaDao.cargar(dtoWhere, null);
	}
	
	@Override
	public ArrayList<DtoOdontograma> cargar(DtoOdontograma dtoWhere, DtoPlanTratamientoOdo dtoPlan ) {
		
		return SqlBaseOdontogramaDao.cargar(dtoWhere, dtoPlan);
	}
	
	@Override
	public ArrayList<DtoOtrosIngresosPaciente> cargarIngresos(int paciente,
			int viaIngreso, int institucion) {
		
		return SqlBaseOdontogramaDao.cargarIngresos(paciente, viaIngreso, institucion);
	}
	@Override
	public String retornarConsultaOdontograma(double codigoOdontograma,
			String tipoOdontograma, int ingreso, String seccion,
			boolean inclusion, boolean garantia) {
		
		return SqlBaseOdontogramaDao.retornarConsultaOdontograma(codigoOdontograma, tipoOdontograma, ingreso, seccion, inclusion, garantia);
	}
	
	
	@Override
	public String retornarConsultaOdontogramaPlanTratamiento(DtoPlanTratamientoOdo dto, String seccion,  String tiposInclusionGarantia, int codigoTarifario  ) 
	{
		return SqlBaseOdontogramaDao.retornarConsultaOdontogramaPlanTratamiento(dto, seccion, tiposInclusionGarantia, codigoTarifario);
	}

	@Override
	public String retornarConsultaOdontograma(
			DtoPlanTratamientoOdo dtoPlanTratamiento, String seccion,
			String tiposInclusionGarantia) 
	{
		return SqlBaseOdontogramaDao.retornarConsultaOdontograma(dtoPlanTratamiento, seccion, tiposInclusionGarantia);
	}
	
	
	@Override
	public DtoOdontograma cargarOdontograma(DtoOdontograma dtoOdontograma) {
		return SqlBaseOdontogramaDao.cargarOdontograma(dtoOdontograma);
	}

	@Override
	public DtoOdontograma cargarOdontogramaImagen(DtoPlanTratamientoOdo dtoPlan) {
		
		return SqlBaseOdontogramaDao.cargarOdontogramaImagen(dtoPlan);
	}
	
	
	@Override
	public ArrayList<DtoOtroHallazgo> consultarHallazgosOtros(Connection con,
			int codigo, byte constanteSeccionConsulta)
	{
		return SqlBaseOdontogramaDao.consultarHallazgosOtros(con, codigo, constanteSeccionConsulta);
	}
	
	@Override
	public void insertarOtrosHallazgos(Connection con, ArrayList<DtoOtroHallazgo> hallazgosOtros, int codigoOdontograma)
	{
		SqlBaseOdontogramaDao.insertarOtrosHallazgos(con, hallazgosOtros, codigoOdontograma);
	}
	
	
}
