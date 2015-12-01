package com.mercury.dao.odontologia;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;


public interface IndicePlacaDao
{
    /**
     * Inserta un diagrama de &iacute;ndice de placa
     * @param con
     * @param codTratamientoOdo
     * @param observaciones
     * @param codMedico
     * @param fecha
     * @param numeroSuperficies
     * @param numeroDientes
     * @return
     * @throws SQLException
     */
    public int insertar(
            Connection con,
            String codTratamientoOdo,
            String observaciones,
            int codMedico, 
            String fecha, int numeroSuperficies, int numeroDientes) throws SQLException;
    
    public Collection<HashMap<String, Object>> consultarIndicesPlacaTratamiento(Connection con, int codTratamiento) throws SQLException;

    /**
     * Consulta la informaci&oacute;n de un diagrama de &iacute;ndice de placa
     * @param con
     * @param codigo
     * @return
     * @throws SQLException
     */
    public Collection<HashMap<String, Object>> consultar(Connection con, int codigo) throws SQLException;

    /**
     * Modifica las observaciones de un diagrama de &iacute;ndice de placa
     * @param con
     * @param codigo
     * @param observaciones
     * @return
     * @throws SQLException
     */
    public int modificar(
            Connection con, 
            int codigo, 
            String observaciones) throws SQLException;

    /**
     * Inserta informaci&oacute;n de placa en un sector de un diente para un diagrama de &iacute;ndice de placa espec&iacute;fico.
     * @param con
     * @param codIndicePlaca
     * @param numeroDiente
     * @param numeroSector
     * @return
     * @throws SQLException
     */
    public int insertarSectorDientePlaca(Connection con, int codIndicePlaca, int numeroDiente, int numeroSector) throws SQLException;
    
    /**
     * Consulta los sectores que tienen placa en los dientes de un diagrama de indice de placa especificado.
     * @param con
     * @param codIndicePlaca
     * @return
     * @throws SQLException
     */
    public Collection<HashMap<String, Object>> consultarSectoresDientesPlaca(Connection con, int codIndicePlaca) throws SQLException;
    
	/**
	 * Obtener el n&uacute;mero de superficies del &iacute;ndice de placa
	 * @param con Conexi&oacute;n con la BD
	 * @param codigoIndicePlaca C&oacute;sdigo del &iacute;ndice de placa relacionado
	 * @return entero con el n&uacute;mero de superficies
	 */
	public int consultarNumeroSuperficies(Connection con, int codigoIndicePlaca);
}
