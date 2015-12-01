package com.princetonsa.dao.odontologia;

import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.odontologia.InfoPaquetesPresupuesto;

import com.princetonsa.dto.odontologia.DtoPresupuestoPaquetes;

/**
 * 
 * 
 * @author Wilson Rios 
 *
 * Jun 5, 2010 - 6:28:10 PM
 */
public interface PresupuestoPaquetesDao 
{
	/**
	 * 
	 * Metodo para insertar el encabezado
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public BigDecimal insertar(Connection con, DtoPresupuestoPaquetes dto);
	
	/**
	 * 
	 * Metodo para eliminar 
	 * @param con
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public boolean eliminar(Connection con, DtoPresupuestoPaquetes dto);
	
	/**
	 * 
	 * Metodo para cargar 
	 * @param con
	 * @param dto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public ArrayList<DtoPresupuestoPaquetes> cargar(Connection con, DtoPresupuestoPaquetes dto);

	/**
	 * 
	 * Metodo para cargar los objetos de paquetes presupuesto pero sin tarifas
	 * @param codigo
	 * @param fechaVigencia
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @param contrato 
	 * @see
	 */
	public ArrayList<InfoPaquetesPresupuesto> cargarInfoPaquetesSinTarifas(int convenio,int contrato, String fechaVigenciaApp);

	/**
	 * 
	 * Metodo para obtener el codigo a mostrar 
	 * @param codigoPkPaqueteOdonCONVENIO
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public String obtenerCodigoMostrarPaqueteOdontologico(int codigoPkPaqueteOdon);
	
	/**
	 * 
	 * Metodo para cargar el info de paquete 
	 * @param codigoPkPresupuesto
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public ArrayList<InfoPaquetesPresupuesto> cargarInfoPaquetesTarifas(	BigDecimal codigoPkPresupuesto);
	
	/**
	 * 
	 * Metodo para validar si es vigente o no el paquete odo convenio
	 * @param codigoPk
	 * @return
	 * @author   Wilson Rios
	 * @version  1.0.0 
	 * @see
	 */
	public boolean esDetallePaqueteConvenioVigente(BigDecimal codigoPk, String fechaApp);

}
