package com.princetonsa.dao.odontologia;

import java.util.ArrayList;

import com.princetonsa.dto.odontologia.DtoEmisionTarjetaCliente;
import com.princetonsa.dto.odontologia.DtoTarjetaCliente;

/**
 * 
 * @author Edgar Carvajal Ruiz
 *
 */
public interface EmisionTarjetaClienteDao {

	/**
	 * 
	 * @param 
	 * @return
	 */
	public boolean modificar(DtoEmisionTarjetaCliente dtoNuevo, DtoEmisionTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public ArrayList<DtoEmisionTarjetaCliente> cargar(DtoEmisionTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dtoWhere
	 * @return
	 */
	public boolean eliminar(DtoEmisionTarjetaCliente dtoWhere);
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double guardar(DtoEmisionTarjetaCliente dto) ;
	/**
	 * 
	 * 
	 */
	
	public  boolean existeRangoSeriales(double codigo, String convenio, double ValorMinimo, double ValorMaximo);

	/**
	 * 
	 * @param codigoPKTipoTarjeta
	 * @param centroAtencion
	 * @param rangoInicialSerial
	 * @param rangoFinalSerial
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean existeSerialEnEmisionTarjetaDetalle(double codigoPKTipoTarjeta, int centroAtencion,double rangoInicialSerial, double rangoFinalSerial, int codigoInstitucion);
	
	
	
	
	/**
	 * Metodo para validar si existe Registro de Tipo de Tarjeta Cliente
	 * Retorna un true y existe informacion en otros caso false
	 * @author Edgar Carvajal Ruiz
	 * @param dto
	 * @return
	 */
	public  boolean existeTarjetaRegistrada(DtoTarjetaCliente dto);

	
}
	

