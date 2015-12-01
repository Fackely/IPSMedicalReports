package com.princetonsa.actionform.cargos;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.UtilidadTexto;
import util.Utilidades;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

public class CargosAutomaticosPresupuestoForm extends ValidatorForm 
{
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private String idCuenta;
	
	/**
	 * 
	 */
	int codigoInstitucion;
	
	/**
	 * 
	 */
	private HashMap serviciosAutomaticos;
	
	
	/**
	 * Médico tratante
	 */
	private int medicoTratante;
	
	/**
	 * Nombre del médico tratante
	 */
	private String nombreMedicoTratante;
	
	
	private HashMap centrosCosto;
	
	private int codigoCentro;
	
	private String consecutivoOrdenMedica;
	
	private String numeroSolicitudGenerado;
	
	private String seleccionar;
	
	/**
	 * 
	 */
	private Vector codigosDetallesCargo;
	
	/**
	 * 
	 */
	private HashMap justificacionNoPosMap  = new HashMap();
	
	//**************************************************
	// Cambios Segun Anexo 809
	private String codigoProfResponde;
	private ArrayList<ArrayList<InfoDatosInt>> espProfResponde = new ArrayList<ArrayList<InfoDatosInt>>();
	private int indice;
	// Fin Cambios Segun Anexo 809
	//**************************************************
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		
		this.serviciosAutomaticos = new HashMap();
		this.serviciosAutomaticos.put("numRegistros", "0");
		this.centrosCosto = new HashMap();
		this.centrosCosto.put("numRegistros", "0");
		this.idCuenta="";
		this.nombreMedicoTratante="";
		this.medicoTratante=ConstantesBD.codigoNuncaValido;
		this.codigoCentro=ConstantesBD.codigoNuncaValido;
		this.consecutivoOrdenMedica="";
		this.numeroSolicitudGenerado="";
		this.seleccionar="";
		this.codigosDetallesCargo= new Vector();
		this.justificacionNoPosMap = new HashMap();
		this.justificacionNoPosMap.put("numRegistros", 0);
		
		// Cambios Segun Anexo 809
		this.codigoProfResponde= "";
		this.espProfResponde = new ArrayList<ArrayList<InfoDatosInt>>();
		this.indice = ConstantesBD.codigoNuncaValido;
		// Fin Cambios Segun Anexo 809
	}

	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errors=new ActionErrors();
		
		if(estado.equals("guardarServicios"))
		{
			int numReg=Utilidades.convertirAEntero(this.serviciosAutomaticos.get("numRegistros")+"");
			String codigoServicioTemp="";
			for(int i=0;i<numReg;i++)
			{
				if(UtilidadTexto.getBoolean(this.getServiciosAutomaticos("seleccionado_"+i)+""))
				{
					
					if((this.serviciosAutomaticos.get("centroCosto_"+i)+"").equals("") || (this.serviciosAutomaticos.get("centroCosto_"+i)+"").equals("null"))
					{
						errors.add("Centro Costo", new ActionMessage("errors.required","El centro costo "+(i+1)));
					}
					codigoServicioTemp= this.getServiciosAutomaticos("codigoservicio_"+i)+"";
					if(Utilidades.convertirADouble(UtilidadTexto.formatearValores(serviciosAutomaticos.get("valorunitario_"+i)+""))==0.0)
					{
						errors.add("servicioSinTarifa", new ActionMessage("error.servicio.generacionCargosAutomaticos"," " +codigoServicioTemp));
						break;
					}
					
					try
			        {
						codigoServicioTemp= this.getServiciosAutomaticos("codigoservicio_"+i)+"";
			            if(Integer.parseInt(this.getServiciosAutomaticos("medicoTratante_"+i)+"")<=0)
			            {
			                errors.add("Campo Médico Responde", new ActionMessage("errors.required","El campo Médico Responde para el servicio "+codigoServicioTemp));
			            }
			            else
			            {
			                if((this.getServiciosAutomaticos("poolMedico_"+i)+"").equals("") || (this.getServiciosAutomaticos("poolMedico_"+i)+"").equals("null"))
			                {
			                    errors.add("Campo Pool X Médico ", new ActionMessage("errors.seleccion","Pool X Médico para el servicio "+codigoServicioTemp));
			                }
			            }
			        }
			        catch(NullPointerException ne)
			        {
			            errors.add("Campo Médico Responde", new ActionMessage("errors.required","El campo Médico Responde para el servicio "+codigoServicioTemp));
			        }
			        catch(NumberFormatException nfe)
			        {
			            errors.add("Campo Médico Responde", new ActionMessage("errors.required","El campo Médico Responde para el servicio "+codigoServicioTemp));
			        }
			        
			        // Cambios Segun Anexo 809
			        try{
			        	codigoServicioTemp= this.getServiciosAutomaticos("codigoservicio_"+i)+"";
			        	if(Integer.parseInt(this.getServiciosAutomaticos("espMedicoTratante_"+i)+"")<=0)
			            {
			                errors.add("Campo Especialidad Médico Responde", new ActionMessage("errors.required","El campo Especialidad Profesional Responde para el servicio "+codigoServicioTemp));
			            }
			        }catch(NullPointerException ne)
			        {
			            errors.add("Campo Médico Responde", new ActionMessage("errors.required","El campo Especialidad Profesional Responde para el servicio "+codigoServicioTemp));
			        }
			        catch(NumberFormatException nfe)
			        {
			            errors.add("Campo Médico Responde", new ActionMessage("errors.required","El campo Especialidad Profesional para el servicio "+codigoServicioTemp));
			        }
			        // Fin Cambios Segun Anexo 809
			        
			        
			        try
			        {
			            codigoServicioTemp= this.getServiciosAutomaticos("codigoservicio_"+i)+"";
			            if(Integer.parseInt(this.getServiciosAutomaticos("cantidad_"+i)+"")<=0)
			            {
			                errors.add("Campo cantidad", new ActionMessage("errors.integerMayorQue","El campo Cantidad para el Servicio "+codigoServicioTemp, "0"));
			            }
			        }
			        catch(NullPointerException ne)
			        {
			            errors.add("Campo Cantidad vacio", new ActionMessage("errors.invalid","El campo Cantidad para el Servicio "+codigoServicioTemp));
			        }
			        catch(NumberFormatException nfe)
			        {
			            errors.add("Campo Cantidad vacio", new ActionMessage("errors.invalid","El campo Cantidad para el Servicio "+codigoServicioTemp));
			        }
					
					
					
				}	
			}
		}
		return errors;
	}

	/**
	 * 
	 * @return
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return
	 */
	public String getIdCuenta() {
		return idCuenta;
	}

	/**
	 * 
	 * @param idCuenta
	 */
	public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	/**
	 * 
	 * @return
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * 
	 * @param codigoInstitucion
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getServiciosAutomaticos() {
		return serviciosAutomaticos;
	}

	/**
	 * 
	 * @param serviciosAutomaticos
	 */
	public void setServiciosAutomaticos(HashMap serviciosAutomaticos) {
		this.serviciosAutomaticos = serviciosAutomaticos;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getServiciosAutomaticos(String key) {
		return serviciosAutomaticos.get(key);
	}
	
	/**
	 * 
	 * @param key
	 * @param obj
	 */
	public void setServiciosAutomaticos(String key, Object obj) {
		this.serviciosAutomaticos.put(key, obj) ;
	}

	/**
	 * 
	 * @return
	 */
	public int getMedicoTratante() {
		return medicoTratante;
	}

	/**
	 * 
	 * @param medicoTratante
	 */
	public void setMedicoTratante(int medicoTratante) {
		this.medicoTratante = medicoTratante;
	}

	/**
	 * 
	 * @return
	 */
	public String getNombreMedicoTratante() {
		return nombreMedicoTratante;
	}

	/**
	 * 
	 * @param nombreMedicoTratante
	 */
	public void setNombreMedicoTratante(String nombreMedicoTratante) {
		this.nombreMedicoTratante = nombreMedicoTratante;
	}


	public HashMap getCentrosCosto() {
		return centrosCosto;
	}


	public void setCentrosCosto(HashMap centrosCosto) {
		this.centrosCosto = centrosCosto;
	}


	public int getCodigoCentro() {
		return codigoCentro;
	}


	public void setCodigoCentro(int codigoCentro) {
		this.codigoCentro = codigoCentro;
	}


	public String getConsecutivoOrdenMedica() {
		return consecutivoOrdenMedica;
	}


	public void setConsecutivoOrdenMedica(String consecutivoOrdenMedica) {
		this.consecutivoOrdenMedica = consecutivoOrdenMedica;
	}


	public String getNumeroSolicitudGenerado() {
		return numeroSolicitudGenerado;
	}


	public void setNumeroSolicitudGenerado(String numeroSolicitudGenerado) {
		this.numeroSolicitudGenerado = numeroSolicitudGenerado;
	}


	public String getSeleccionar() {
		return seleccionar;
	}


	public void setSeleccionar(String seleccionar) {
		this.seleccionar = seleccionar;
	}


	/**
	 * @return the codigosDetallesCargo
	 */
	public Vector getCodigosDetallesCargo() {
		return codigosDetallesCargo;
	}


	/**
	 * @param codigosDetallesCargo the codigosDetallesCargo to set
	 */
	public void setCodigosDetallesCargo(Vector codigosDetallesCargo) {
		this.codigosDetallesCargo = codigosDetallesCargo;
	}

	/**
	 * @return the codigosDetallesCargo
	 */
	public String getCodigoDetallesCargo(int index) {
		return codigosDetallesCargo.get(index).toString();
	}


	/**
	 * @param codigosDetallesCargo the codigosDetallesCargo to set
	 */
	public void setCodigoDetalleCargo(String value) {
		this.codigosDetallesCargo.add(value);
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getJustificacionNoPosMap() {
		return justificacionNoPosMap;
	}

	/**
	 * 
	 * @param justificacionNoPosMap
	 */
	public void setJustificacionNoPosMap(HashMap justificacionNoPosMap) {
		this.justificacionNoPosMap = justificacionNoPosMap;
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public Object getJustificacionNoPosMap(String key) {
		return justificacionNoPosMap.get(key);
	}

	/**
	 * 
	 * @param key
	 * @param value
	 */
	public void setJustificacionNoPosMap(String key,Object value) {
		this.justificacionNoPosMap.put(key, value);
	}

	/**
	 * @return the codigoProfResponde
	 */
	public String getCodigoProfResponde() {
		return codigoProfResponde;
	}

	/**
	 * @param codigoProfResponde the codigoProfResponde to set
	 */
	public void setCodigoProfResponde(String codigoProfResponde) {
		this.codigoProfResponde = codigoProfResponde;
	}

	/**
	 * @return the espProfResponde
	 */
	public ArrayList<ArrayList<InfoDatosInt>> getEspProfResponde() {
		return espProfResponde;
	}

	/**
	 * @param espProfResponde the espProfResponde to set
	 */
	public void setEspProfResponde(ArrayList<ArrayList<InfoDatosInt>> espProfResponde) {
		this.espProfResponde = espProfResponde;
	}

	/**
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}

	/**
	 * @param indice the indice to set
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}
}
