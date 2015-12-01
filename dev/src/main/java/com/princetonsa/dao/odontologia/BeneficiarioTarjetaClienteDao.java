package com.princetonsa.dao.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosStr;

import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;


public interface BeneficiarioTarjetaClienteDao {
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public double  guardar(Connection con,  DtoBeneficiarioCliente dto );
	 
	/**
	 *  
	 * @param dto
	 * @return
	 */
	public ArrayList<DtoBeneficiarioCliente> cargar(DtoBeneficiarioCliente dto);
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean modificar(DtoBeneficiarioCliente dtoNuevo , DtoBeneficiarioCliente dtoWhere );
		
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public boolean eliminar(DtoBeneficiarioCliente dto);
	/**
	 * 
	 * 
	 * @param dtoBeneficiario
	 * @param dtoCompardor
	 * @return
	 */
	public  ArrayList<DtoBeneficiarioCliente>  cargarAvanzadoEmpresa(DtoBeneficiarioCliente dtoBeneficiario, InfoDatosStr dtoCompardor);
    /***
     * 
     * 
     * 
     */
	
	public  ArrayList<DtoBeneficiarioCliente> cargarAvanzadoFamiliar(DtoBeneficiarioCliente dtoBeneficiario, DtoBeneficiarioCliente dtoPrincipal);
	
	/**
	 * 
	 * @param dto
	 * @param serialInicial
	 * @param serialFinal
	 * @param codigo_pk
	 * @return
	 */
	public ArrayList<DtoBeneficiarioCliente>consultarAvanzadaBeneficiarios(DtoBeneficiarioCliente dto, double serialInicial,  double serialFinal , double codigo_pk, int institucion );
	
	
	/**
	 * 
	 * @param seriales
	 * @param institucion
	 * @return
	 */
	public  ArrayList<Double> validarSerialesBeneficiarios(ArrayList<Double> seriales, int institucion);
	
	
	
	/**
	 * 
	 * @param serialInicial
	 * @param serialFinal
	 * @param codigoVenta
	 * @param institucion
	 * @return
	 */
	public int validarRangoSeriales(double serialInicial, double serialFinal,double codigoVenta,  int institucion);
	
	
	
	/**
	 * 
	 * @param serialInicial
	 * @param serialFinal
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean validarSerialesRangoBeneficiarios(double serialInicial, double serialFinal, int codigoInstitucion );
	
	/**
	 * 
	 * @param listaCodigos
	 * @return
	 */
	public boolean eliminarTcPaciente( ArrayList<String> listaCodigos);
	
	
	
	/**
	 * 
	 * @param dto
	 * @return
	 */
	public  boolean existeTarjetasBeneficiarios(DtoBeneficiarioCliente dto);

	/**
	 * 
	 * @return
	 */
	public HashMap consultar(String tipoIdentificacion, String numeroIdentificacion);

	
	public ArrayList<Double> validarNumTarjetasBeneficiarios(ArrayList<Double> numTarjetaBenficiarios, int codigoInstitucionInt);

	/**
	 * 
	 * @param numTarjetaInicial
	 * @param numTarjetaFinal
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean validarNumTarjetaRangoBeneficiarios(double numTarjetaInicial, double numTarjetaFinal,int codigoInstitucion);

	/**
	 * Activa las tarjetas de los beneficiarios asociados a una venta
	 * @param con Conexión con la BD
	 * @param codigoTarjeta Código de la tarjeta para la cual se van a activar los beneficiarios
	 * @return true en caso de actualizar correctamente, false de lo contrario.
	 */
	public boolean activarTarjetaBeneficiario(Connection con, int codigoTarjeta);
		
	
}
