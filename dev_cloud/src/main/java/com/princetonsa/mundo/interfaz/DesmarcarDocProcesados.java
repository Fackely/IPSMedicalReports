package com.princetonsa.mundo.interfaz;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.UtilidadFecha;
import util.Utilidades;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.interfaz.DesmarcarDocProcesadosDao;
import com.princetonsa.dto.interfaz.DtoDocumentoDesmarcar;
import com.princetonsa.dto.interfaz.DtoLogInterfaz1E;

public class DesmarcarDocProcesados {
	
	private static Logger logger = Logger.getLogger(DesmarcarDocProcesados.class);
	
	public static DesmarcarDocProcesadosDao getDesmarcarDocProcesadosDao()
	{		
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getDesmarcarDocProcesadosDao();
		
	}	
	
	/**
	 * Metodo para consultar los Documentos asociados a un tipo de Movimiento
	 * @param tipoMov
	 * @return
	 */
	public static ArrayList<DtoDocumentoDesmarcar> consultarDocumentosXtipoMvto(String tipoMov)
	{
		return getDesmarcarDocProcesadosDao().consultarDocumentosXtipoMvto(tipoMov);
	}
   
	
	/**
	 * Metodo para realizar la validacion de las Fechas
	 * @param parametros
	 * @return
	 */
	public static ActionErrors validacionBusqueda(HashMap parametros)
	{
		ActionErrors errores = new ActionErrors();
		logger.info("fecha INICIAL >> "+parametros.get("fechaInicialDesmaracion"));
		  
		if(parametros.get("fechaInicialDesmaracion").toString().equals("") && 
				parametros.get("fechaFinalDesmarcacion").toString().equals(""))
			
		{
			errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial Desmarcación y La Fecha Final Desmarcación"));
		}
		
		
		if((!parametros.get("fechaInicialDesmaracion").toString().equals("") 
				&& parametros.get("fechaFinalDesmarcacion").toString().equals("")) || 
					(parametros.get("fechaInicialDesmaracion").toString().equals("")
						&& !parametros.get("fechaFinalDesmarcacion").toString().equals("")))
		{
			if(parametros.get("fechaInicialDesmaracion").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial Desmarcación"));
			
			if(parametros.get("fechaFinalDesmarcacion").toString().equals(""))
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final Desmarcación "));
		}
		else if(!parametros.get("fechaInicialDesmaracion").toString().equals("") && 
					!parametros.get("fechaFinalDesmarcacion").toString().equals(""))
		{
			if(!UtilidadFecha.validarFecha(parametros.get("fechaInicialDesmaracion").toString()))
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Fecha Inicial Desmarcación"));
			
			if(!UtilidadFecha.validarFecha(parametros.get("fechaFinalDesmarcacion").toString()))
				errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Desmarcación Final "));
			
			if(UtilidadFecha.validarFecha(parametros.get("fechaInicialDesmaracion").toString()) && 
					UtilidadFecha.validarFecha(parametros.get("fechaFinalDesmarcacion").toString()))
			{
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaFinalDesmarcacion").toString(), parametros.get("fechaInicialDesmaracion").toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Desmarcación Final  "," Desmarcación Inicial "));
				}
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaInicialDesmaracion").toString(),parametros.get("fechaControl").toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual","Desmarcación Inicial ","Control"));
				}
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaFinalDesmarcacion").toString(),parametros.get("fechaControl").toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual","Desmarcación Final","Control"));
				}
				
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaInicialDesmaracion").toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Desmarcacion Inicial","Actual"));
				}
				
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaFinalDesmarcacion").toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual","Desmarcación Final","Actual"));
				}				
				
				logger.info("fecha INICIAL >> "+parametros.get("fechaInicialDesmaracion"));
				
				if (fechaSuperaMismoMesYAno(parametros.get("fechaInicialDesmaracion").toString(),parametros.get("fechaFinalDesmarcacion").toString()))
				{
					errores.add("descripcion",new ActionMessage("errors.notEspecific","El rango de Fechas no debe superar el mismo mes y año"));
				}
		
			}
		}
		
		if(parametros.get("motivoDesmarcacion").toString().equals(""))
		{
			errores.add("descripcion",new ActionMessage("errors.required","EL motivo de Desmarcación"));
		}
		
		if(Utilidades.convertirAEntero(parametros.get("contDesmarcar").toString())==0)
		{
			errores.add("descripcion",new ActionMessage("errors.required","Seleccionar al menos un Documento para Desmarcar"));
		}
		
		return errores;
	}


 
	/**
	 * Metodo para desmarcar Facturas Pacientes
	 * @param con
	 * @param parametros
	 * @return
	 */
	public static HashMap desmarcarFacturasPacientes(Connection con, HashMap parametros) {
		
		return getDesmarcarDocProcesadosDao().desmarcarFacturasPacientes(con,parametros);
	}

   
    /**
     * Metodo para desmarcar Anulacion Facturas Pacientes
     * @param con
     * @param parametrosBusqueda
     * @return
     */
	public HashMap desmarcarAnulacionFacturasPacientes(Connection con, HashMap parametrosBusqueda) {
		
		return getDesmarcarDocProcesadosDao().desmarcarAnulacionFacturasPacientes(con,parametrosBusqueda);
	}


   /**
    * Metodo para desmarcar Facturas Varias y Anulacion Facturas Varias
    * @param con
    * @param parametrosBusqueda
    * @param anulacion
    * @return
    */
	public HashMap desmarcarFacturasVarias(Connection con,HashMap parametrosBusqueda, boolean anulacion) {
		
		return getDesmarcarDocProcesadosDao().desmarcarFacturasVarias(con, parametrosBusqueda, anulacion);
	}


	 /**
	  * Metodo para desmarcar Ajustes Facturas Varias
	  * @param con
	  * @param parametrosBusqueda
	  * @return
	  */
   public HashMap desmarcarAjustesFacturasVarias(Connection con,HashMap parametrosBusqueda) {
	
	   return getDesmarcarDocProcesadosDao().desmarcarAjustesFacturasVarias(con,parametrosBusqueda);
   }


	 /**
	  *Metodo para desmarcar Cuentas de Cobro Capitacion 
	  * @param con
	  * @param parametrosBusqueda
	  * @return
	  */
	 public HashMap desmarcarCuentasCobroCapitacion(Connection con,HashMap parametrosBusqueda) {
		
		 return getDesmarcarDocProcesadosDao().desmarcarCuentasCobroCapitacion(con,parametrosBusqueda);
	 }


	/**
	 * Metodo para desmarcar Ajustes Cuentas de Cobro Capitacion
	 * @param con
	 * @param parametrosBusqueda
	 * @return
	 */
	public HashMap desmarcarAjustesCuentasCobroCapitacion(Connection con,HashMap parametrosBusqueda) {
		
		return getDesmarcarDocProcesadosDao().desmarcarAjustesCuentasCobroCapitacion(con,parametrosBusqueda);
	}


	/**
	 * Metodo para desmarcar Recibos de Caja
	 * @param con
	 * @param parametrosBusqueda
	 * @return
	 */
	public HashMap desmarcarRecibosdeCaja(Connection con, HashMap parametrosBusqueda) {
		
		return getDesmarcarDocProcesadosDao().desmarcarRecibosdeCaja(con,parametrosBusqueda);
	}
	
	
	/**
	 * Metod para desmarcar Anulacion de Recibos de Caja
	 * @param con
	 * @param parametrosBusqueda
	 * @return
	 */
	public HashMap desmarcarAnulacionRecibosdeCaja(Connection con, HashMap parametrosBusqueda) {
		
		return getDesmarcarDocProcesadosDao().desmarcarAnulacionRecibosdeCaja(con,parametrosBusqueda);
	}
	
	/**
	 * Metodo para desmarcar Devolucion Recibos de Caja
	 * @param con
	 * @param parametrosBusqueda
	 * @return
	 */
	public HashMap desmarcarDevolucionRecibosdeCaja(Connection con, HashMap parametrosBusqueda) {
		
		return getDesmarcarDocProcesadosDao().desmarcarDevolucionRecibosdeCaja(con,parametrosBusqueda);
	}
	
	/**
	 * Metodo para desmarcar Ajustes Facturas Pacientes
	 * @param con
	 * @param parametrosBusqueda
	 * @return
	 */
	public HashMap desmarcarAjustesFacuturasPacientes(Connection con,HashMap parametrosBusqueda) {
		
		return getDesmarcarDocProcesadosDao().desmarcarAjustesFacuturasPacientes(con,parametrosBusqueda);
	}
	
	public HashMap desmarcarRegistroGlosas(Connection con,HashMap parametrosBusqueda)
	{
		return getDesmarcarDocProcesadosDao().desmarcarRegistroGlosas(con, parametrosBusqueda);
	}
	
	/**
	 * Metodo para desmarcar Autorizacion de Servicios Entidades Subcontratadas
	 * SI anulacion = true se desmarca Anluacion de Autorizacion Entidades Subcontratadas
	 * @param con
	 * @param parametrosBusqueda
	 * @return
	 */
	public HashMap desmarcarAutorServEntidadesSub(Connection con,HashMap parametrosBusqueda, boolean anulacion) {
		
		return getDesmarcarDocProcesadosDao().desmarcarAutorServEntidadesSub(con, parametrosBusqueda, anulacion);
	}


	
   /**
    * Metodo para desmarcar Despacho de Medicamentos
    * @param con
    * @param parametrosBusqueda
    * @return
    */
	public HashMap desmarcarDespachoMedicamentos(Connection con, HashMap parametrosBusqueda) {
		
		return getDesmarcarDocProcesadosDao().desmarcarDespachoMedicamentos(con, parametrosBusqueda);
	}


	/**
	 * Metodo pare desmarcar Devolucion de Medicamentos
	 * @param con
	 * @param parametrosBusqueda
	 * @return
	 */
	public HashMap desmarcarDevolucionMedicamentos(Connection con,HashMap parametrosBusqueda) {
		
		return getDesmarcarDocProcesadosDao().desmarcarDevolucionMedicamentos(con,parametrosBusqueda);
	}

   
    /**
     * Metodo para desmarcar Despacho Pedidos Insumos y Quirurgicos
     * el valor esQuirurgico determina si es para insumos (false) o para Quirurgicos(true)
     * @param con
     * @param parametrosBusqueda
     * @return
     */
 	public HashMap desmarcarDespachoPedidos(Connection con,HashMap parametrosBusqueda,  boolean esQuirurgico) {
		
		return getDesmarcarDocProcesadosDao().desmarcarDespachoPedidos(con,parametrosBusqueda, esQuirurgico);
	}


   /**
    * Metodo para desmarcar Devoluciones de Pedidos de Insumos y Quirurgicos
    * el valor esQuirurgico determina si es para insumos (false) o para Quirurgicos(true)
    * @param con
    * @param parametrosBusqueda
    * @param esQuirurgico
    * @return
    */
	public HashMap desmarcarDevolucionPedidos(Connection con, HashMap parametrosBusqueda, boolean esQuirurgico) {
		
		return getDesmarcarDocProcesadosDao().desmarcarDevolucionPedidos(con,parametrosBusqueda, esQuirurgico);
	}


    /**
     * Metodo para desmarcar Cargos Directos Articulos
     * @param con
     * @param parametrosBusqueda
     * @return
     */
	public HashMap desmarcarCargosDirectosArticulos(Connection con,	HashMap parametrosBusqueda) {
		
		return getDesmarcarDocProcesadosDao().desmarcarCargosDirectosArticulos(con,parametrosBusqueda );
	}


    /**
     * Metodo para desmarcar la anulacion de cargos articulos
     * @param con
     * @param parametrosBusqueda
     * @return
     */
	public HashMap desmarcarAnulacionCargosArticulos(Connection con,HashMap parametrosBusqueda) {
		
		return getDesmarcarDocProcesadosDao().desmarcarAnulacionCargosArticulos(con,parametrosBusqueda);
	}


    /**
     * Metodo para Guardar el LOG de Interfaz 1E ( generado despues de desmarcar los Docuementos )
     * @param con
     * @param dto
     * @return
     */
	public static HashMap guardarLogInterfaz1E(Connection con,DtoLogInterfaz1E dto) {
		
		return getDesmarcarDocProcesadosDao().guardarLogInterfaz1E(con, dto);
	}
	
	/**
	 * 
	 * @param con
	 * @param codLogInterfaz
	 * @param tipoDocumento
	 * @return
	 */
	public static HashMap guardarLogInterfazTiposDoc1E(Connection con, int codLogInterfaz, String tipoDocumento)
	{
		return getDesmarcarDocProcesadosDao().guardarLogInterfazTiposDoc1E(con,codLogInterfaz,tipoDocumento);
	}

	/**
	 * Metodo que valida que un rango de fechas no supere el mismo mes y año
	 * @param fecha1
	 * @param fecha2
	 * @return
	 */
    public static boolean fechaSuperaMismoMesYAno(String fecha1, String fecha2)
    {
    	String fechaAux1[]= fecha1.split("/");
    	String fechaAux2[]= fecha2.split("/");
    	
    	if(fechaAux1.length == fechaAux1.length)
    	{
    		if(!(fechaAux1[1].equals(fechaAux2[1])) && (Utilidades.convertirAEntero(fechaAux2[1]) > Utilidades.convertirAEntero(fechaAux1[1])))
    		{ 
    			return true;
    		}else
    		{
    			if(!(fechaAux1[2].equals(fechaAux2[2])) && (Utilidades.convertirAEntero(fechaAux2[2]) > Utilidades.convertirAEntero(fechaAux1[2])) )
    			{
    				return true;
    			}
    		}
    	}
    	
    	return false;
    }

	
	 
}

