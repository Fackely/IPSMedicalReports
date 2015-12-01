package com.princetonsa.mundo.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import org.apache.log4j.Logger;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.Utilidades;
import com.princetonsa.action.carteraPaciente.ApliPagosCarteraPacienteAction;
import com.princetonsa.action.carteraPaciente.DocumentosGarantiaAction;
import com.princetonsa.dao.carterapaciente.ApliPagosCarteraPacienteDao;
import com.princetonsa.dao.carterapaciente.DocumentosGarantiaDao;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dto.carteraPaciente.DtoAplicacPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoCuotasDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoDetApliPagosCarteraPac;
import com.princetonsa.dto.carteraPaciente.DtoDocGarantiaAplicarPago;
import com.princetonsa.dto.facturasVarias.DtoRecibosCaja;

/**
 * @author Jose Eduardo Arias Doncel
 * jearias@princetonsa.com
 * */
public class ApliPagosCarteraPaciente
{
	static Logger logger = Logger.getLogger(DocumentosGarantiaAction.class);
	
	/**
	 * Instancia DAO
	 * */
	public static ApliPagosCarteraPacienteDao apliPagosCarteraPacienteDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getApliPagosCarteraPacienteDao();		
	}	
	
	public static ArrayList<DtoRecibosCaja> consultarRecibosCaja(Connection con)
	{
		return apliPagosCarteraPacienteDao().consultarRecibosCaja(con);
	}
	
	public static ArrayList<DtoRecibosCaja> consultarRecibosCajaFiltros(Connection con, HashMap filtros)
	{
		return apliPagosCarteraPacienteDao().consultarRecibosCajaFiltros(con,filtros);
	}
	
	public static ArrayList<DtoAplicacPagosCarteraPac> consultarDocsFarantia(Connection con, String nroRC,String deudor)
	{
		return apliPagosCarteraPacienteDao().consultarDocsFarantia(con, nroRC,deudor);
	}
	
	public static double ingresarEncabezadoAplicPagos(Connection con,DtoAplicacPagosCarteraPac dto)
	{
		return apliPagosCarteraPacienteDao().ingresarEncabezadoAplicPagos(con,dto);
	}
	
	public static ArrayList<DtoCuotasDatosFinanciacion> consultaCuotasDatosFin(Connection con, String nroRC)
	{
		return apliPagosCarteraPacienteDao().consultaCuotasDatosFin(con,nroRC);
	}
	
	public static boolean ingresarDetApliPagosCarteraPac(Connection con, DtoDetApliPagosCarteraPac dto)
	{
		return apliPagosCarteraPacienteDao().ingresarDetApliPagosCarteraPac(con,dto);
	}
	
	public static DtoAplicacPagosCarteraPac consultarAplicacion(Connection con)
	{
		return apliPagosCarteraPacienteDao().consultarAplicacion(con);
	}
	
	public static boolean cancelarDocGarantia(Connection con, HashMap datosCon)
	{
		return apliPagosCarteraPacienteDao().cancelarDocGarantia(con,datosCon);
	}

	public static boolean existeApliPagosReciboCaja(Connection con, String numeroReciboCaja) {
		return apliPagosCarteraPacienteDao().existeApliPagosReciboCaja(con,numeroReciboCaja);
	}
}