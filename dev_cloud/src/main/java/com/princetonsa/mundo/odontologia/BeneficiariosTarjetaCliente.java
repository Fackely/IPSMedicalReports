package com.princetonsa.mundo.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosStr;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;


public class BeneficiariosTarjetaCliente {
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static double guardar(Connection con, DtoBeneficiarioCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().guardar(con, dto);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoBeneficiarioCliente>  cargar(DtoBeneficiarioCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().cargar(dto);
	}
	
	

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoBeneficiarioCliente>  cargarAvanzadoEmpresa(DtoBeneficiarioCliente dtoBeneficiario, InfoDatosStr dtoCompardor)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().cargarAvanzadoEmpresa(dtoBeneficiario, dtoCompardor);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static ArrayList<DtoBeneficiarioCliente>  cargarAvanzadoFamiliar(DtoBeneficiarioCliente dtoBeneficiario, DtoBeneficiarioCliente dtoPrincipal)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().cargarAvanzadoFamiliar(dtoBeneficiario, dtoPrincipal);
	}

	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  modificar(DtoBeneficiarioCliente dtoNuevo , DtoBeneficiarioCliente dtoWhere  )
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().modificar(dtoNuevo, dtoWhere);
	}
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean  eliminar(DtoBeneficiarioCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().eliminar(dto);
	}
	
	/**
	 * 
	 * @param dtoBeneficiario
	 * @param serialInicial
	 * @param serialFinal
	 * @param codigo_pk
	 * @return
	 */
	
	public static ArrayList<DtoBeneficiarioCliente>  cargarAvanzadoBeneficiarios(DtoBeneficiarioCliente dtoBeneficiario, double serialInicial, double serialFinal, double codigo_pk, int institucion)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().consultarAvanzadaBeneficiarios(dtoBeneficiario, serialInicial, serialFinal, codigo_pk, institucion) ;
	}
	
	/**
	 * 
	 * @param seriales
	 * @param institucion
	 * @return
	 */
	public static  ArrayList<Double> validarSerialesBeneficiarios(ArrayList<Double> seriales,int institucion) 
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().validarSerialesBeneficiarios(seriales, institucion);
	}
	
	
	/**
	 * 
	 * @param serialInicial
	 * @param serialFinal
	 * @param codigoVenta
	 * @param institucion
	 * @return
	 */

	public  static int validarRangoSeriales(double serialInicial, double serialFinal,
			double codigoVenta, int institucion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().validarRangoSeriales(serialInicial, serialFinal, codigoVenta, institucion);

	}
	
	
	
	/**
	 * retorna true si encontro seriales Registrados de lo contraio false
	 * @param serialInicial
	 * @param serialFinal
	 * @param codigoInstitucion
	 * @return
	 */
	public static boolean validarSerialesRangoBeneficiarios(double serialInicial,
			double serialFinal, int codigoInstitucion) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().validarSerialesRangoBeneficiarios(serialInicial, serialFinal, codigoInstitucion);
	}
	
	public static boolean validarNumTarjetaRangoBeneficiarios(double numTarjetaInicial,
			double numTarjetaFinal, int codigoInstitucion) {
		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().validarNumTarjetaRangoBeneficiarios(numTarjetaInicial, numTarjetaFinal, codigoInstitucion);
	}
	
	
	
	
	
	/**
	 * Eliminar los Beneficiarios  de la tabla  odontologia.beneficiario_tc_paciente
	 * @param listaCodigos
	 * @return
	 */
	public static boolean eliminarTcPaciente(ArrayList<String> listaCodigos){
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().eliminarTcPaciente(listaCodigos);
	}
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public static boolean existeTarjetasBeneficiarios(DtoBeneficiarioCliente dto)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().existeTarjetasBeneficiarios(dto);
	}

	/**
	 * 
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return
	 */
	public static HashMap consultar(String tipoIdentificacion, String numeroIdentificacion) {
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().consultar(tipoIdentificacion, numeroIdentificacion);
	}

	/**
	 * 
	 * @param numTarjetaBenficiarios
	 * @param codigoInstitucionInt
	 * @return
	 */
	public static ArrayList<Double> validarNumTarjetasBeneficiarios(ArrayList<Double> numTarjetaBenficiarios, int codigoInstitucionInt) {
		// TODO Auto-generated method stub
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().validarNumTarjetasBeneficiarios(numTarjetaBenficiarios,codigoInstitucionInt);
	}

	/**
	 * Activa las tarjetas de los beneficiarios asociados a una venta
	 * @param con Conexión con la BD
	 * @param codigoTarjeta Código de la tarjeta para la cual se van a activar los beneficiarios
	 * @return true en caso de actualizar correctamente, false de lo contrario.
	 */
	public static boolean activarTarjetaBeneficiario(Connection con, int codigoTarjeta)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getBeneficiarioTarjetaClienteDao().activarTarjetaBeneficiario(con, codigoTarjeta);
	}
	
}
