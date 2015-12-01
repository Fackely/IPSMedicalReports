package com.princetonsa.dao.oracle.odontologia;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.InfoDatosStr;

import com.princetonsa.dao.odontologia.BeneficiarioTarjetaClienteDao;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseBeneficiariosTarjetaCli;
import com.princetonsa.dto.odontologia.DtoBeneficiarioCliente;

public class OracleBeneficiarioTarjetaCliente implements BeneficiarioTarjetaClienteDao {


	
	public ArrayList<DtoBeneficiarioCliente> cargar(DtoBeneficiarioCliente dto) {

		return SqlBaseBeneficiariosTarjetaCli.cargar(dto);
	}

	@Override
	public boolean eliminar(DtoBeneficiarioCliente dto) {
		
		return SqlBaseBeneficiariosTarjetaCli.eliminar(dto);
	}

	@Override
	public double guardar(Connection con, DtoBeneficiarioCliente dto) {
		
		return SqlBaseBeneficiariosTarjetaCli.guardar(con, dto);
	}

	@Override
	public boolean modificar(DtoBeneficiarioCliente dtoNuevo , DtoBeneficiarioCliente dtoWhere ) {
		
		return SqlBaseBeneficiariosTarjetaCli.modificar(dtoNuevo , dtoWhere);
	}

	@Override
	public ArrayList<DtoBeneficiarioCliente> cargarAvanzadoEmpresa(
			DtoBeneficiarioCliente dtoBeneficiario, InfoDatosStr dtoCompardor) {
		
		return SqlBaseBeneficiariosTarjetaCli.cargarAvanzadoEmpresa(dtoBeneficiario, dtoCompardor);
	}
	
	@Override
	public ArrayList<DtoBeneficiarioCliente> cargarAvanzadoFamiliar(
			DtoBeneficiarioCliente dtoBeneficiario,
			DtoBeneficiarioCliente dtoPrincipal) {
		
		return  SqlBaseBeneficiariosTarjetaCli.cargarAvanzadoFamiliar(dtoBeneficiario, dtoPrincipal);
	}
	
	@Override
	public ArrayList<DtoBeneficiarioCliente> consultarAvanzadaBeneficiarios(
			DtoBeneficiarioCliente dto, double serialInicial,
			double serialFinal, double codigo_pk,int  institucion) {
		
		return SqlBaseBeneficiariosTarjetaCli.consultarAvanzadaBeneficiarios(dto,serialInicial,serialFinal,codigo_pk ,institucion);
	}
	
	@Override
	public ArrayList<Double> validarSerialesBeneficiarios(ArrayList<Double> seriales,
			int institucion) {
		
		return SqlBaseBeneficiariosTarjetaCli.validarSerialesBeneficiarios(seriales, institucion);
	}
	
	
	@Override
	public int validarRangoSeriales(double serialInicial, double serialFinal,
			double codigoVenta, int institucion) {
		
		return SqlBaseBeneficiariosTarjetaCli.validarRangoSeriales(serialInicial, serialFinal, codigoVenta, institucion);
	}
	
	@Override
	public boolean validarSerialesRangoBeneficiarios(double serialInicial,
			double serialFinal, int codigoInstitucion) {
		
		return SqlBaseBeneficiariosTarjetaCli.validarSerialesRangoBeneficiarios(serialInicial, serialFinal, codigoInstitucion);
	}
	
	
	
	@Override
	public boolean eliminarTcPaciente(ArrayList<String> listaCodigos) {
		
	//	return SqlBaseBeneficiariosTarjetaCli.eliminarTcPaciente(listaCodigos);
		return true;
	}

	@Override
	public boolean existeTarjetasBeneficiarios(DtoBeneficiarioCliente dto) {
		
		return SqlBaseBeneficiariosTarjetaCli.existeTarjetasBeneficiarios(dto);
	}
	@Override
	public HashMap consultar(String tipoIdentificacion, String numeroIdenficacion)
	{
		return SqlBaseBeneficiariosTarjetaCli.consultar(tipoIdentificacion, numeroIdenficacion);
	}

	@Override
	public ArrayList<Double> validarNumTarjetasBeneficiarios(ArrayList<Double> numTarjetaBenficiarios, int codigoInstitucionInt) {
		
		return SqlBaseBeneficiariosTarjetaCli.validarNumTarjetasBeneficiarios(numTarjetaBenficiarios,codigoInstitucionInt);
	}

	@Override
	public boolean validarNumTarjetaRangoBeneficiarios(double numTarjetaInicial, double numTarjetaFinal,int codigoInstitucion) {
		return SqlBaseBeneficiariosTarjetaCli.validarNumTarjetasRangoBeneficiarios(numTarjetaInicial, numTarjetaFinal, codigoInstitucion);
	}

	@Override
	public boolean activarTarjetaBeneficiario(Connection con, int codigoTarjeta)
	{
		return SqlBaseBeneficiariosTarjetaCli.activarTarjetaBeneficiario(con, codigoTarjeta);
	}
}
