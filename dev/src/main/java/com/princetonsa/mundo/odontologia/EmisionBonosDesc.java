package com.princetonsa.mundo.odontologia;


import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;

import util.InfoPacienteBonoPresupuesto;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.manejoPaciente.DtoSubCuentas;
import com.princetonsa.dto.odontologia.DtoEmisionBonosDesc;

public class EmisionBonosDesc {
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(DtoEmisionBonosDesc dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao().guardar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoEmisionBonosDesc> cargar(DtoEmisionBonosDesc dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao().cargar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
    
	public static boolean  modificar(DtoEmisionBonosDesc dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao().modificar(dto);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  eliminar(DtoEmisionBonosDesc dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao().eliminar(dto);
	}
	/**
	 * 
	 * @param dto
	 * @return
	 */
	
	public static boolean  existeCruceSerialesIdYConvenio(DtoEmisionBonosDesc dto,  double codigoPkNotIn )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao().existeCruceSerialesIdYConvenio(dto, codigoPkNotIn);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  existeCruceFechasConvenioPrograma(DtoEmisionBonosDesc dto ,  double codigoPkNotIn)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao().existeCruceFechasConvenioPrograma(dto, codigoPkNotIn);
	}

	/**
	 * Método que valida si un serial de un bono es válido, verifica
	 * la vigencia y que el serian no halla sido utilizado
	 * @param con Conexión con la BD
	 * @param bono Serial del bono
	 * @param codigoConvenio Código del convenio
	 * @return true en caso de que el serial esté vigente y no se halla utilizado
	 */
	public static boolean esSerialVigenteDisponible(Connection con, Integer bono, int codigoConvenio) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao().esSerialVigenteDisponible(con, bono, codigoConvenio);
	}
	

	/**
	 * 
	 * @param dto
	 * @param aplicaProgramas
	 * @return
	 */
	public static ArrayList<InfoPacienteBonoPresupuesto> consultaEmisionBonos(InfoPacienteBonoPresupuesto dto  , boolean aplicaProgramas )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao().consultaEmisionBonos(dto, aplicaProgramas);
	}
	
	
	/**
	 * 
	 * @param serialInicial
	 * @param serialFinal
	 * @return
	 */
	public static boolean existeSerialSubCuentas(BigDecimal serialInicial , BigDecimal serialFinal, int convenio){
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao().existeSerialSubCuentas(serialInicial, serialFinal, convenio);
	}
	
	/**
	 * Método que valida si un serial está vigente o disponible
	 * @param con
	 * @param bono
	 * @param codigoConvenio
	 * @param dtoSubcuentas 
	 * @return Mensaje de error, "" en caso de no existir error
	 * @author Juan David
	 */
	public static String esSerialVigenteDisponibleMensajeError(Connection con, Integer bono, int codigoConvenio, DtoSubCuentas dtoSubcuentas) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getEmisionBonosDescDao().esSerialVigenteDisponibleMensajeError(con, bono, codigoConvenio, dtoSubcuentas);
	}
	
	
	
	

}
