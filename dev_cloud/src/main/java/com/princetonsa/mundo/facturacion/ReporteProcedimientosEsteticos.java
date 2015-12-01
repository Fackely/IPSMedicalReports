/*
 * Julio 23 del 2007
 */
package com.princetonsa.mundo.facturacion;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.facturacion.ReporteProcedimientosEsteticosDao;


/**
 * @author Andrés Eugenio Silva Monsalve 
 *
 *clase que representa el Mundo con sus atributos y métodos de la funcionalidad
 * Parametrización de Reportes Procedimientos Esteticos
 */
public class ReporteProcedimientosEsteticos 
{
	/**
	 * DAO para el manejo de reporteProcedimientosEsteticosDao
	 */
	private ReporteProcedimientosEsteticosDao reporteDao=null;
	
	/**
	 * Mapa de los Reportes Procedimientos Esteticos
	 */
	private HashMap mapaReporte = new HashMap();
	
	/**
	 * Número de los Reportes Procedimientos Esteticos
	 */
	private int numReporte;
	
	
	//*****************************************************************
	//**********INICIALIZADORES & CONSTRUCTORES***********************
	/**
	 * Constructor
	 */
	public ReporteProcedimientosEsteticos() 
	{
		this.clean();
		this.init(System.getProperty("TIPOBD"));
	}
	/**
	 * método para inicializar los datos
	 *
	 */
	public void clean()
	{
		this.mapaReporte = new HashMap();
		this.numReporte = 0;
	}
	/**
	 * Inicializa el acceso a bases de datos de este objeto.
	 * @param tipoBD el tipo de base de datos que va a usar este objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public void init(String tipoBD) 
	{
		if (reporteDao == null) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
		//	reporteDao = myFactory.getreporteProcedimientosEsteticosDao();
		}	
	}
	
	/**
	 * Método que retorna el DAO instanciado de Condiciones por Servicio
	 * @return
	 */
	public static ReporteProcedimientosEsteticosDao reporteDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getReporteProcedimientosEsteticosDao();
	}
	
	//****************************************************************************
	//*************************MÉTODOS********************************************
	
	
	public HashMap consultarProcedimientosEsteticos(Connection con, String fechaInicial, String fechaFinal, String grupoEstetico, String centroAtencion, String tipoReporte, String institucion )
	{
		HashMap<String, Object> listado = new HashMap<String, Object>();
		
		HashMap campos = new HashMap();
		campos.put("fechaIni", UtilidadFecha.conversionFormatoFechaABD(fechaInicial));
		campos.put("fechaFin",UtilidadFecha.conversionFormatoFechaABD(fechaFinal));
		campos.put("gEstetico", grupoEstetico);
		campos.put("centroAte", centroAtencion);
		campos.put("tipoRep", tipoReporte);
		campos.put("institucion", institucion);
		
		HashMap respuesta = reporteDao().consultarProcedimientosEsteticos(con, campos);
		int b, cont = 0;	
		
		//validaciones de consulta
		int numReg=Utilidades.convertirAEntero(respuesta.get("numRegistros").toString());
		for (int a=0; a < numReg; a++)
		{
			b=a+1;
			if (!respuesta.get("consecutivoOrden_"+a).toString().equals(respuesta.get("consecutivoOrden_"+b)+"")||
				b == Integer.parseInt(respuesta.get("numRegistros").toString()))
			{
				listado.put("fechaInterpretacion_"+cont, respuesta.get("fechaInterpretacion_"+a));
				if(Integer.parseInt(respuesta.get("numServicios_"+a).toString())>1)
				{
					listado.put("nombreServicio_"+cont, "");
				}
				else
				{
					listado.put("nombreServicio_"+cont, respuesta.get("nombreServicio_"+a));
				}
				listado.put("consecutivoOrden_"+cont, respuesta.get("consecutivoOrden_"+a));
				listado.put("codigoPaciente_"+cont, respuesta.get("codigoPaciente_"+a));
				listado.put("nombrePaciente_"+cont, respuesta.get("nombrePaciente_"+a));
				listado.put("idPaciente_"+cont, respuesta.get("idPaciente_"+a));
				listado.put("valorOrden_"+cont, respuesta.get("valorOrden_"+a));
				listado.put("numeroSolicitud_"+cont, respuesta.get("numeroSolicitud_"+a));
				listado.put("numServicios_"+cont, respuesta.get("numServicios_"+a));
				cont++;	
			}
			respuesta.get("consecutivoOrden_"+a);
		}
		
		listado.put("numRegistros", cont+"");
		
		return listado;
	
	}
	
	/**
	 * Método que consulta el detalle del cargo de una solicitud estética
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultarDetalleCargoEstetico(Connection con,String numeroSolicitud)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		return reporteDao().consultarDetalleCargoEstetico(con, campos);
	}
	
	/**
	 * Método para cargar los materiales especiales de la solicitud de cirugía
	 * @param con
	 * @param campos
	 * @return
	 */
	public static ArrayList<HashMap<String, Object>> consultarMaterialesEspecialesEstetico(Connection con,String numeroSolicitud)
	{
		HashMap campos = new HashMap();
		campos.put("numeroSolicitud",numeroSolicitud);
		return reporteDao().consultarMaterialesEspecialesEstetico(con, campos);
	}
	
	//************************************************************************************
	//********************GETTERS & SETTERS***********************************************
	

	/**
	 * @return the mapaReporte
	 */
	public HashMap getMapaReporte() {
		return mapaReporte;
	}
	/**
	 * @param mapaReporte the mapaReporte to set
	 */
	public void setMapaReporte(HashMap mapaReporte) {
		this.mapaReporte = mapaReporte;
	}
	
	/**
	 * @return the numReporte
	 */
	public int getNumReporte() {
		return numReporte;
	}
	/**
	 * @param numReporte the numReporte to set
	 */
	public void setNumReporte(int numReporte) {
		this.numReporte = numReporte;
	}
	
	
}
