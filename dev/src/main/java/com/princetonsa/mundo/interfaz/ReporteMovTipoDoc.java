package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;

import util.BackUpBaseDatos;
import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.CsvFile;
import util.TxtFile;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.reportes.ConsultasBirt;

import com.princetonsa.actionform.glosas.ConsultarImpFacAudiForm;
import com.princetonsa.actionform.interfaz.ReporteMovTipoDocForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.ReporteMovTipoDocDao;
import com.princetonsa.dto.interfaz.DtoMovimientoTipoDocumento;
import com.princetonsa.mundo.UsuarioBasico;

/**
 * @author Jairo Gómez Fecha Junio de 2009
 */

public class ReporteMovTipoDoc {

	private static Logger logger = Logger.getLogger(ReporteMovTipoDoc.class);
	
	public static final String[] indicesCriterios = { "FechaInicial",
			"FechaFinal", "TipoDocumento", "TipoSalida" };
	
	public void clean()
	{
		
	}
	
	/**
	 * Constructor de la Clase
	 */
	public ReporteMovTipoDoc() 
	{
		
	}

	/**
	 * DAO de este objeto, para trabajar con la fuente de datos
	 */
	private static ReporteMovTipoDocDao getReporteMovTipoDocDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteMovTipoDocDao();
	}
	
	public ArrayList<DtoMovimientoTipoDocumento> consultarFacturasPacientes (Connection con, HashMap criterios)
	{
		return getReporteMovTipoDocDao().consultarFacturasPacientes(con, criterios);
	}
	
	public ArrayList<DtoMovimientoTipoDocumento> consultarIngresos (Connection con, HashMap criterios)
	{
		return getReporteMovTipoDocDao().consultarIngresos(con, criterios);
	}
	
	public ArrayList<DtoMovimientoTipoDocumento> consultarRecibosCaja (Connection con, HashMap criterios)
	{
		return getReporteMovTipoDocDao().consultarRecibosCaja(con, criterios);
	}
	
	public ArrayList<DtoMovimientoTipoDocumento> consultarFacturasVarias (Connection con, HashMap criterios)
	{
		return getReporteMovTipoDocDao().consultarFacturasVarias(con, criterios);
	}
}