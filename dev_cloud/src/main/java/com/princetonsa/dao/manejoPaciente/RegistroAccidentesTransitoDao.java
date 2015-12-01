package com.princetonsa.dao.manejoPaciente;

import java.sql.Connection;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.manejoPaciente.SqlBaseRegistroAccidentesTransitoDao;
import com.princetonsa.dto.manejoPaciente.DtoRegistroAccidentesTransito;

public interface RegistroAccidentesTransitoDao
{
	/**
	 * Metodo para insertar el Registro de Accidentes de Transito.
	 * @param con
	 * @param dto
	 * @return
	 */
	public abstract int insertarRegistroAccidentesTransito(Connection con,DtoRegistroAccidentesTransito dto);
	/**
	 * Metodo que modifica un registro de accidentes de transito.
	 * @param con
	 * @param dto
	 * @return
	 */
	public abstract boolean modificarRegistroAccidentesTransito(Connection con,DtoRegistroAccidentesTransito dto);
	
	/**
	 * metodo que consulta un registro de accidentes de transito dada la llave del registro.
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract DtoRegistroAccidentesTransito consultarRegistrosAccidentesTransito(Connection con,String ingreso);
	
	/**
	 * 
	 * @param con
	 * @param ingreso
	 * @return
	 */
	public abstract DtoRegistroAccidentesTransito consultarRegistroAccidentesTransitoIngreso(Connection con, String ingreso);
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap
	 * @return
	 */
	public abstract HashMap busquedaAvanzada(Connection con, HashMap criteriosBusquedaMap);
	
	/**
	 * Método implementado para actualizar el estado del registro de accidentes de transito,
	 * si el estado es anulación se ingresa la fecha, hora y usuario anulacion
	 * @param con
	 * @param dtoReg
	 * @return
	 */
	public abstract int actualizarEstadoRegistroAccidenteTransito(Connection con,DtoRegistroAccidentesTransito dtoReg);
	
	/**
	 * mapa de generarReporteCertificadoAtencionMedica
	 * @param con
	 * @param criteriosBusquedaMap, keys--> codigoCentroAtencion , codigoInstitucion, idIngreso
	 * @return
	 */
	public abstract HashMap generarReporteCertificadoAtencionMedica(Connection con, HashMap criteriosBusquedaMap) ;
	
	/**
	 * 
	 * @param con
	 * @param criteriosBusquedaMap, keys--> codigoCentroAtencion , codigoInstitucion, idIngreso
	 * @return
	 */
	public abstract HashMap generarReporteFUSOAT01(Connection con, HashMap criteriosBusquedaMap);
	
	/**
	 * Metodo encargado de consultar todos los registros de
	 * accidentes de transito  filtrandolos por el estado. 
	 * @param connection
	 * @param criterios
	 * -------------------------
	 * KEY'S DEL MAPA CRITERIOS
	 * -------------------------
	 * -- estado
	 * @return
	 * ---------------------------
	 * KEY'S DEL MAPA RESULTADO
	 * ---------------------------
	 * -- fechaAccidente0_
	 * -- departamentoAccidente1_
	 * -- ciudadAccidente2_
	 * -- lugarAccidente3_
	 * -- asegurado4_
	 * -- nombreConductor5
	 * -- nombrePropietario6
	 */
	public HashMap cargarListadoAccidentesTransito (Connection connection,HashMap criterios);
	
	
	/**
	 * Metodo encargado de asociar un accidente de transito 
	 * a un ingreso
	 * @param connection
	 * @param ingreso
	 * @param codigoAccidente
	 * @return
	 */
	public boolean asociarAcciedente (Connection connection, String ingreso,String codigoAccidente );
	
	/**
	 * Metodo encargado de modificar la seccion de maparos
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean modificarAmparos(Connection con,DtoRegistroAccidentesTransito dto);
	
	/**
	 * Metodo encargado de modificar los datos de la Reclamacion
	 * @param con
	 * @param dto
	 * @return
	 */
	public boolean modificarReclamacion(Connection con,DtoRegistroAccidentesTransito dto);
	
	
	/**
	 * 
	 * @param con
	 * @param codigo
	 * @return
	 */
	public abstract DtoRegistroAccidentesTransito consultarRegistroAccidentesTransitoLlave(Connection con, String codigo);
	
	
}