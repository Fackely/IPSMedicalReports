package com.princetonsa.dao.carterapaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import com.princetonsa.dao.sqlbase.carteraPaciente.SqlBaseApliPagosCarteraPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDetApliPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDocGarantiaAplicarPago;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;

public interface ApliPagosCarteraPacienteDao
{
	public ArrayList<DtoRecibosCaja> consultarRecibosCaja(Connection con);
	
	public ArrayList<DtoRecibosCaja> consultarRecibosCajaFiltros(Connection con, HashMap filtros);
	
	public ArrayList<DtoAplicacPagosCarteraPac> consultarDocsFarantia(Connection con, String nroRC, String deudor);
	
	public double ingresarEncabezadoAplicPagos(Connection con,DtoAplicacPagosCarteraPac dto);
	
	public ArrayList<DtoCuotasDatosFinanciacion> consultaCuotasDatosFin (Connection con, String nroRc);
	
	public boolean ingresarDetApliPagosCarteraPac(Connection con, DtoDetApliPagosCarteraPac dto);
	
	public DtoAplicacPagosCarteraPac consultarAplicacion(Connection con);
	
	public boolean cancelarDocGarantia(Connection con, HashMap datosCon);

	public boolean existeApliPagosReciboCaja(Connection con, String numeroReciboCaja);
}