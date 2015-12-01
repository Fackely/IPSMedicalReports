package com.princetonsa.dao.postgresql.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.carterapaciente.ApliPagosCarteraPacienteDao;
import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseApliPagosCarteraPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDetApliPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDocGarantiaAplicarPago;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;
/**
 * @author Julio Hernández
 * jfhernandez@axioma-md.com
 * */
public class PostgresqlApliPagosCarteraPacienteDao implements ApliPagosCarteraPacienteDao
{
	public ArrayList<DtoRecibosCaja> consultarRecibosCaja(Connection con)
	{
		return SqlBaseApliPagosCarteraPacienteDao.consultarRecibosCaja(con);
	}
	
	public ArrayList<DtoRecibosCaja> consultarRecibosCajaFiltros(Connection con, HashMap filtros)
	{
		return SqlBaseApliPagosCarteraPacienteDao.consultarRecibosCajaFiltros(con, filtros);
	}
	
	public ArrayList<DtoAplicacPagosCarteraPac> consultarDocsFarantia(Connection con, String nroRC,String deudor)
	{
		return SqlBaseApliPagosCarteraPacienteDao.consultarDocsFarantia(con, nroRC, deudor);
	}
	
	public double ingresarEncabezadoAplicPagos(Connection con,DtoAplicacPagosCarteraPac dto)
	{
		return SqlBaseApliPagosCarteraPacienteDao.ingresarEncabezadoAplicPagos(con, dto);
	}
	
	public ArrayList<DtoCuotasDatosFinanciacion> consultaCuotasDatosFin (Connection con, String nroRC)
	{
		return SqlBaseApliPagosCarteraPacienteDao.consultaCuotasDatosFin(con, nroRC);
	}
	
	public boolean ingresarDetApliPagosCarteraPac(Connection con, DtoDetApliPagosCarteraPac dto)
	{
		return SqlBaseApliPagosCarteraPacienteDao.ingresarDetApliPagosCarteraPac(con, dto);
	}
	
	public DtoAplicacPagosCarteraPac consultarAplicacion (Connection con)
	{
		return SqlBaseApliPagosCarteraPacienteDao.consultarAplicacion(con);
	}
	
	public boolean cancelarDocGarantia(Connection con, HashMap datosCon)
	{
		return SqlBaseApliPagosCarteraPacienteDao.cancelarDocGarantia(con, datosCon);
	}
	
	public boolean existeApliPagosReciboCaja(Connection con, String numeroReciboCaja)
	{
		return SqlBaseApliPagosCarteraPacienteDao.existeApliPagosReciboCaja(con, numeroReciboCaja);
	}
}
