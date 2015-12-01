package com.princetonsa.mundo.carteraPaciente;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;

import util.ConstantesIntegridadDominio;
import util.UtilidadFecha;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.actionform.carteraPaciente.ConsultarRefinanciarCuotaPacienteForm;
import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.carterapaciente.ConsultarRefinanciarCuotaPacienteDao;
import com.princetonsa.dto.carteraPaciente.DtoHistoDatosFinanciacion;
import com.princetonsa.dto.carteraPaciente.DtoIngresosFacturasAtenMedica;
import com.princetonsa.mundo.UsuarioBasico;

public class ConsultarRefinanciarCuotaPaciente {

	Logger logger = Logger.getLogger(ConsultarRefinanciarCuotaPaciente.class);

	public static ConsultarRefinanciarCuotaPacienteDao getConsultarRefinanciarCuotaPacienteDao()
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getConsultarRefinanciarCuotaPacienteDao();
	}
	
	
	/**
	 * Metodo para listar los ingresos Asociados a facturas de atencion Medica de un Paciente
	 * @param codigoPersona
	 * @return
	 */
	public ArrayList<DtoIngresosFacturasAtenMedica> listarIngresosFacAtenMedica(int codigoPersona) {
		
		return getConsultarRefinanciarCuotaPacienteDao().listarIngresosFacAtenMedica(codigoPersona);
	}
	
    /**
     * Metodo para guardar Refinanciacion
     * @param con
     * @param forma
     * @return
     */
	public HashMap guardarRefinanciacion(Connection con,HashMap datos) {
		
		
		return getConsultarRefinanciarCuotaPacienteDao().guardarRefinanciacion(con,datos);
	}

    
	/**
	 * Metodo para realizar la consulta del Historico de Financiaciones
	 * @param codDatosFinanciacion
	 * @return
	 */
	public ArrayList<DtoHistoDatosFinanciacion> consultarHistoricoRefinanciacion(String codDatosFinanciacion) {
		
		return getConsultarRefinanciarCuotaPacienteDao().consultarHistoricoRefinanciacion(codDatosFinanciacion);
	}

    /**
     * Metodo para realizar la consulta de los documentos Cartera paciente por Rango
     * @param parametrosBusqueda
     * @return
     */
 	public ArrayList<DtoIngresosFacturasAtenMedica> listarDocsCarteraPacienteXRango(HashMap parametrosBusqueda) {
		
		return getConsultarRefinanciarCuotaPacienteDao().consultarDocsCarteraPacienteXRango(parametrosBusqueda);
	}
	
 	
 	/**
	 * Metodo para Validar datos de Refinanciacion
	 * @param forma
	 * @param usuario
	 * @return
	 */
	public ActionErrors ValidarGuardarRefinanciacion(ConsultarRefinanciarCuotaPacienteForm forma, UsuarioBasico usuario)
	{
		 ActionErrors errores =new ActionErrors();
		 int vacios=0;
		 if(forma.getCuotasRefinanciacion().size()>=1)
		 {
			 for(int i=0; i< forma.getNroCuotasRefinanciacion();i++)
			  {
				  if(forma.getCuotasRefinanciacion().get(i)!=null && forma.getCuotasRefinanciacion().get(i).getNumeroDocumento().equals(""))
				  {
					  vacios++;				  
				  }			 
			  }
			 
			 if(vacios>=1)
			 {
				 errores.add("descripcion",new ActionMessage("errors.notEspecific","Por Favor Editar las Cuotas de Refinanciacion"));
			 }
		 }else
		 {
			 errores.add("descripcion",new ActionMessage("errors.notEspecific","Por Favor Editar las Cuotas de Refinanciacion"));
		 }
		 
		 
		 
		 if(forma.getFechaInicioRefinanciacion().equals(""))
		 {
			 errores.add("descripcion",new ActionMessage("errors.required","La Fecha de Inicio"));
		 }else
		 { 
			if(!forma.getFechaInicioRefinanciacion().equals(""))
			{
			  if(!UtilidadFecha.validarFecha(forma.getFechaInicioRefinanciacion()))
			     {
					errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Fecha de Inicio"));
			     }
			  
			  if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), forma.getFechaInicioRefinanciacion()))
				{
					errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," de Inicio "," Actual"));
				}
			  if(UtilidadFecha.esFechaMenorQueOtraReferencia(forma.getFechaInicioRefinanciacion(), UtilidadFecha.conversionFormatoFechaAAp(forma.getListaIngresos().get(forma.getPosIngreso()).getFechaFactura())))
				{
					errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," de Inicio "," de Generacion de la Factura"));
				}
			  
			}
		 }
		
		 if(!(forma.getDiasporCuotaRefinanciacion()+"").equals(""))
		 {
		   if(forma.getDiasporCuotaRefinanciacion() > Utilidades.convertirAEntero(ValoresPorDefecto.getMaximoNumeroDiasFinanciacionPorCuota(usuario.getCodigoInstitucionInt())))
		    {
			 errores.add("descripcion",new ActionMessage("errors.notEspecific","Dias por cuota debe ser Menor a Parametrizacion Maximo de Dias Financiacion por Cuota"));
		    }
		 
		   if(forma.getDiasporCuotaRefinanciacion() <=0 )
		    {
			 errores.add("descripcion",new ActionMessage("errors.notEspecific","Dias por cuota debe ser Mayor o igual a 1"));
		    }
		 }else
		 {
			 errores.add("descripcion",new ActionMessage("errors.required","Dias por Cuota"));
		 }
		
		 return errores;
	}
 	
 	
	/**
	 * Metodo para realizar Validaciones de los parametros de Busqueda por Rango
	 * @param parametros
	 * @return
	 */
	 public static ActionErrors validacionBusquedaporRango(HashMap parametros)
		{
			Utilidades.imprimirMapa(parametros);
			ActionErrors errores = new ActionErrors();
	  
	   if(parametros.get("codigoGarantia").equals("")  && parametros.get("numFactura").equals("") )
		 {
			if(parametros.get("fechaGrantiaInicial").toString().equals("") && 
					parametros.get("fechaGrantiaFinal").toString().equals(""))
				
			{
				errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial Grantía y La Fecha Final Garantía "));
			}
			
			     if((!parametros.get("fechaGrantiaInicial").toString().equals("") 
							&& parametros.get("fechaGrantiaFinal").toString().equals("")) || 
								(parametros.get("fechaGrantiaInicial").toString().equals("")
									&& !parametros.get("fechaGrantiaFinal").toString().equals("")))
					{
						if(parametros.get("fechaGrantiaInicial").toString().equals(""))
							errores.add("descripcion",new ActionMessage("errors.required","La Fecha Inicial Garantía "));
						
						if(parametros.get("fechaGrantiaFinal").toString().equals(""))
							errores.add("descripcion",new ActionMessage("errors.required","La Fecha Final Garantia"));
					}
					else if(!parametros.get("fechaGrantiaInicial").toString().equals("") && 
								!parametros.get("fechaGrantiaFinal").toString().equals(""))
					{
						if(!UtilidadFecha.validarFecha(parametros.get("fechaGrantiaInicial").toString()))
							errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Fecha Inicial Garantía "));
						
						if(!UtilidadFecha.validarFecha(parametros.get("fechaGrantiaFinal").toString()))
							errores.add("descripcion",new ActionMessage("errors.formatoFechaInvalido"," Fecha Final Garantía "));
						
						if(UtilidadFecha.validarFecha(parametros.get("fechaGrantiaInicial").toString()) && 
								UtilidadFecha.validarFecha(parametros.get("fechaGrantiaFinal").toString()))
						{
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(parametros.get("fechaGrantiaFinal").toString(), parametros.get("fechaGrantiaInicial").toString()))
							{
								errores.add("descripcion",new ActionMessage("errors.fechaAnteriorIgualActual"," Garantía Final  "," Garantía Inicial "));
							}
							
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaGrantiaInicial").toString()))
							{
								errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Garantía Inicial ","Actual"));
							}
							
							if(UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), parametros.get("fechaGrantiaFinal").toString()))
							{
								errores.add("descripcion",new ActionMessage("errors.fechaPosteriorIgualActual"," Garantía Final ","Actual"));
							}
							
						}					
		    }						
			if(parametros.get("centroatencion").equals(""))
		     {
		    	errores.add("descripcion",new ActionMessage("errors.required","EL Centro de Atención "));	
	    	 }
			
			if(parametros.get("tipoDocumento").equals(""))
		     {
		    	errores.add("descripcion",new ActionMessage("errors.required","EL Tipo Documento "));	
	    	 }
			
		}	    	    
	    
	   return errores;	
	}
			
	
 	/**
 	 * Metodo que inicializa los parametros de Busqueda por Rango
 	 * @param centroAtencion
 	 * @return
 	 */
 	public static HashMap inicializarParametrosBusquedaRango(int centroAtencion) 
	{
		HashMap parametros = new HashMap();
		parametros.put("fechaGrantiaInicial",UtilidadFecha.getFechaActual());
		parametros.put("fechaGrantiaFinal",UtilidadFecha.getFechaActual());
		parametros.put("centroatencion",centroAtencion);
		parametros.put("tipoDocumento", "");
		parametros.put("estado",ConstantesIntegridadDominio.acronimoPolizaVigente);
		parametros.put("codigoGarantia", "");
		parametros.put("numFactura","");

		return parametros;
	}
	
	
}
