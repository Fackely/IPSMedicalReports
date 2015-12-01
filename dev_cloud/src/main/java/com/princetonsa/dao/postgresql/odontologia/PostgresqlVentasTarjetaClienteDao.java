package com.princetonsa.dao.postgresql.odontologia;

import java.sql.Connection;
import java.util.ArrayList;

import util.InfoDatosStr;

import com.princetonsa.dao.odontologia.VentasTarjetasClienteDao;
import com.princetonsa.dto.odontologia.DtoVentaTarjetasCliente;
import com.princetonsa.dao.sqlbase.odontologia.SqlBaseVentaTarjetaCliente;

/**
 * 
 * @author axioma
 *
 */
public class PostgresqlVentasTarjetaClienteDao implements VentasTarjetasClienteDao {

	@Override
	public ArrayList<DtoVentaTarjetasCliente> cargar(DtoVentaTarjetasCliente dto) {
		
		return SqlBaseVentaTarjetaCliente.cargar(dto);
	}

	@Override
	public boolean eliminar(DtoVentaTarjetasCliente dto) {
		
		return SqlBaseVentaTarjetaCliente.eliminar(dto);
	}

	@Override
	public double guardar(Connection con, DtoVentaTarjetasCliente dto) {
		
		return SqlBaseVentaTarjetaCliente.guardar(con, dto);
	}

	@Override
	public boolean modificar(DtoVentaTarjetasCliente dto) {
		
		return SqlBaseVentaTarjetaCliente.modificar(dto);
	}
    
	@Override
	public boolean existeRangoEnVenta(double rangoInicial , double rangoFinal , int institucion, String aliado) {
		
		return SqlBaseVentaTarjetaCliente.existeRangoEnVenta(rangoInicial, rangoFinal, institucion, aliado);
	}
	
	@Override
	public InfoDatosStr retornarVenta(String serial, int institucion, double codigoTarjetaCliente, double codigoBeneficiario) {
		
		return SqlBaseVentaTarjetaCliente.retornarVenta(serial, institucion,codigoTarjetaCliente, codigoBeneficiario);
	}
}
