package com.mercury.dao.odontologia;

import java.sql.Connection;

import com.mercury.dto.odontologia.DtoOtroHallazgo;
import com.mercury.mundo.odontologia.Odontograma;
import com.princetonsa.decorator.ResultSetDecorator;
import com.princetonsa.dto.manejoPaciente.DtoOtrosIngresosPaciente;
import com.princetonsa.dto.odontologia.DtoOdontograma;
import com.princetonsa.dto.odontologia.DtoPlanTratamientoOdo;

import java.sql.SQLException;
import java.util.ArrayList;

public interface OdontogramaDao
{
	/**
	 * 
	 * @param con
	 * @param codTratamientoOdo
	 * @param observaciones
	 * @param codMedico
	 * @param fecha
	 * @param xmlOdontograma
	 * @return
	 * @throws SQLException
	 */
    public int insertar(
            Connection con,
            String codTratamientoOdo,
            String observaciones,
            int codMedico, 
            String fecha, 
            String xmlOdontograma,
            String imagen) throws SQLException;
    
    public ResultSetDecorator consultarOdontogramasTratamiento(Connection con, int codTratamiento) throws SQLException;

    /**
     * 
     * @param con
     * @param codigo
     * @param objOdontograma
     * @throws SQLException
     */
    public void consultar(Connection con, int codigo, Odontograma objOdontograma) throws SQLException;
    
    

    public int modificar(
            Connection con, 
            int codigo, 
            String observaciones) throws SQLException;

    public int insertarDiente(Connection con, int codOdontograma, int numDiente, int codEstadoDienteInst) throws SQLException;
    
    public int insertarSector(Connection con, int codOdontograma, int numDiente, int numSector, int codEstadoSectorInst) throws SQLException;

    public ResultSetDecorator consultarDientesPacientePorEstados(Connection con, int codPaciente, int[] estados) throws SQLException;
    
    public  ArrayList<DtoOdontograma> cargar(DtoOdontograma dtoWhere) ;
    
    public  ArrayList<DtoOdontograma> cargar(DtoOdontograma dtoWhere, DtoPlanTratamientoOdo dtoPlan) ;
    
    public  ArrayList<DtoOtrosIngresosPaciente> cargarIngresos(int paciente , int viaIngreso , int institucion) ;
    
    public  String retornarConsultaOdontograma (double codigoOdontograma , String tipoOdontograma , int ingreso , String seccion ,boolean inclusion , boolean garantia);
    
    
    /**
	 * Inserci&oacute;n de la sección otros hallazgos
	 * @param con Conexión con la BD
	 * @param hallazgosOtros ArryList con la información de los hallazgos
	 * @param codigoOdontograma TODO
	 */
	public void insertarOtrosHallazgos(Connection con, ArrayList<DtoOtroHallazgo> hallazgosOtros, int codigoOdontograma);
	
    
    /**
     * 
     * @param dto
     * @param seccion
     * @param codigoTarifario TODO
     * @return
     */
    public String retornarConsultaOdontogramaPlanTratamiento(DtoPlanTratamientoOdo dto, String seccion, String tiposInclusionGarantia, int codigoTarifario );
    
    /**
     * RETORNAR LA CONSULTA DEL ODONTOGRAMA PARA ARMAR EL REPORTE
     * @author axioma
     * @param dtoPlanTratamiento
     * @param seccion
     * @param tiposInclusionGarantia
     * @return
     */
    public String retornarConsultaOdontograma(DtoPlanTratamientoOdo dtoPlanTratamiento , String seccion, String tiposInclusionGarantia);
	
    
    /**
     * 
     * @param dtoOdontograma
     * @return
     */
    public  DtoOdontograma cargarOdontograma(DtoOdontograma dtoOdontograma);
    
	
    /**
     * 
     * @param dtoPlan
     * @return
     */
    public DtoOdontograma cargarOdontogramaImagen(DtoPlanTratamientoOdo dtoPlan);
    
	/**
	 * Consultar otros hallazgos
	 * @param con
	 * @param codigo
	 * @param constanteSeccionConsulta
	 * @return
	 */
	public ArrayList<DtoOtroHallazgo> consultarHallazgosOtros(Connection con, int codigo, byte constanteSeccionConsulta);
}
