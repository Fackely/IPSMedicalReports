package com.princetonsa.dao.administracion;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import com.princetonsa.dto.administracion.DtoConsecutivoCentroAtencion;

/**
 * 
 * @author axioma
 *
 */
public interface  ConsecutivosCentroAtencionDao {
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean  guardar(DtoConsecutivoCentroAtencion dto, Connection con);
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoConsecutivoCentroAtencion> cargar( DtoConsecutivoCentroAtencion dto);
	
	
	/**
	 * 
	 * @param con Conexión con la BD
	 * @param dto
	 * @return
	 */
	public boolean modificar(Connection con, DtoConsecutivoCentroAtencion dto);
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean eliminar (DtoConsecutivoCentroAtencion dto);
	
	/**
	 * Metodo para incrementar el consecutivo, recibe la conexion porque puede ser transaccional
	 * @param centroAtencion
	 * @param nombreConsecutivo
	 * @param institucion
	 * @return
	 */
	public BigDecimal incrementarConsecutivo(int centroAtencion, String nombreConsecutivo, int institucion);
	
	/**
	 * Metodo para obtener el valor actual del consecutivo x centro de atencion
	 * @param centroAtencion
	 * @param nombreConsecutivo
	 * @return
	 */
	public BigDecimal obtenerValorActualConsecutivo(Connection con, int centroAtencion, String nombreConsecutivo, int anio);
	
	
	
	/**
	 * METODO QUE RECIBE UN DTO CONSECUTIVO CENTRO ATENCION Y RETORNA true ESTA ASIGNADO EN OTRO CASO ES false
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public boolean esConsecutivoAsignado(DtoConsecutivoCentroAtencion dto);


	/**
	 * Cambiar uso finalizado consecutivo
	 * @param con Conexión con la BD
	 * @param consecutivo Nombre del consecutivo
	 * @param centroAtencion Código del centro de atención
	 * @param valor Valor del consecutivo asignado
	 * @return true en caso de actualizar correctamente
	 */
	public boolean cambiarUsoFinalizadoConsecutivo(Connection con, String consecutivo, int centroAtencion, BigDecimal valor, String finalizado, String usado, int anio);
	
}
