package com.princetonsa.actionform.capitacion;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.capitacion.ContratosCargue;

import util.ConstantesBD;
import util.InfoDatosInt;
import util.ResultadoBoolean;
import util.UtilidadCadena;
import util.UtilidadFecha;
import util.UtilidadTexto;

public class ModificarCarguesForm extends ValidatorForm
{
	/**
	 * Manejo del flujo
	 */
	private String estado;

	/**
	 * Estado anterior en el flujo
	 */
	private String estadoAnterior;
	
	/**
	 * Cargues asociados al convenio seleccionado
	 */
	private HashMap contratosCargue;
	
	/**
	 * Cantidad de contratos cargue
	 */
	private int numContratosCargue;
	
	/**
	 * Almacena la información de los cargues para usuarios por grupo etareo
	 */
	private HashMap carguesGrupoEtareo;
	
	/**
	 * Convenio seleccionado
	 */
	private InfoDatosInt convenio;
	
	/**
	 * Fecha inicial para la busqueda de los cargues
	 */
	private String fechaInicial;
	
	/**
	 * Fecha final para la búsqueda de los cargues
	 */
	private String fechaFinal;

	/**
	 * Para almacenar el mensaje de resultado de una operación
	 */
	private String mensaje;
	
	/**
	 * Indica el cargue al cual se le está modificando la información de grupos etáreos
	 */
	private int 	numeroContratoCargueEdicion;
	
	/**
	 * Almacena la información de los usuarios cargados para el cargue seleccionado
	 */
	private Collection usuariosCargados;	
	
	/**
	 * Indica el contrato cargue seleccionado para cargar los usuarios
	 */
	private int numeroContratoCargue;
	
	private String tipoIdBusqueda;
	
	private String numeroIdBusqueda;
	
	private String nombreBusqueda;
	
	private String apellidoBusqueda;
	
	private boolean tipoIdBusquedaCheck;
	
	private boolean numeroIdBusquedaCheck;
	
	private boolean nombreBusquedaCheck;
	
	private boolean apellidoBusquedaCheck;
	
	private String numeroFicha;
	
	private boolean numeroFichaCheck;
	
	private int regEliminar;
	
	/**
	 * 
	 */
	private HashMap contratosCargueEliminados;
	
	
	///////// Tarea xplanner id. 37729
	private HashMap mapaUsuariosCargados;
	
	private HashMap mapaLogEliminados;
	
	private HashMap mapaLogActivos;
	
	private String usuarioInactivar;
	
	private String usuarioEliminar;
	
	private ResultadoBoolean mostrarMensaje;
	
	
	
	/**
	 * 
	 */
	public void cleanCompleto()
	{
		this.estado="empezar";
		this.estadoAnterior="";
		this.mensaje="";
		this.contratosCargue=new HashMap();
		this.carguesGrupoEtareo=new HashMap();
		this.convenio=new InfoDatosInt();
		this.fechaInicial="";
		this.fechaFinal="";
		this.numContratosCargue=0;
		this.numeroContratoCargueEdicion=-1;
		this.usuariosCargados=new ArrayList();
		this.numeroContratoCargue=-1;
		this.regEliminar=ConstantesBD.codigoNuncaValido;
		this.contratosCargueEliminados=new HashMap();
		this.contratosCargueEliminados.put("numRegistros", "0");
		this.usuarioInactivar="";
		this.usuarioEliminar="";
		
		this.mapaUsuariosCargados = new HashMap();
		this.mapaUsuariosCargados.put("numRegistros", "0");
		this.mapaLogEliminados = new HashMap();
		this.mapaLogEliminados.put("numRegistros", "0");
		this.mapaLogActivos = new HashMap();
		this.mapaLogActivos.put("numRegistros", "0");
		this.mostrarMensaje= new ResultadoBoolean(false,"");
	}
	
	/**
	 * 
	 */
	public void cleanBusqueda()
	{
		this.tipoIdBusqueda="";
		this.numeroIdBusqueda="";
		this.nombreBusqueda="";
		this.apellidoBusqueda="";
		this.numeroFicha="";
		this.tipoIdBusquedaCheck=false;
		this.numeroIdBusquedaCheck=false;
		this.nombreBusquedaCheck=false;
		this.apellidoBusquedaCheck=false;
		this.numeroFichaCheck=false;
		this.mostrarMensaje= new ResultadoBoolean(false,"");
	}
	
	/**
	 * 
	 */
	public void resetMensaje()
	{
		this.mostrarMensaje= new ResultadoBoolean(false,"");
	}
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores= new ActionErrors();
		
		if(this.getEstado().equals("buscar"))
		{
			boolean errorFechaInicial=false;
            if(UtilidadCadena.noEsVacio(this.getFechaInicial()))
            {
                if(!UtilidadFecha.esFechaValidaSegunAp(this.getFechaInicial()))
                {
                	errorFechaInicial=true;
                    errores.add("La fecha inicial es invalida", new ActionMessage("errors.formatoFechaInvalido", "inicial"));
                }
            }
            else // la fecha esta vacia
            {
            	errorFechaInicial=true;
                errores.add("La fecha inicial no puede ser vacia", new ActionMessage("errors.required", "La fecha inicial"));
            }

            if(UtilidadCadena.noEsVacio(this.getFechaFinal()))
            {
                if(UtilidadFecha.esFechaValidaSegunAp(this.getFechaFinal()))
                {
                	if(!errorFechaInicial)
                	{
                		if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFinal(), this.getFechaInicial()))
            	    		errores.add("La fecha final debe ser mayor a la fecha inicial", new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "final", "inicial"));
                	}
                }
                else
                	errores.add("La fecha final es invalida", new ActionMessage("errors.formatoFechaInvalido", "final"));
            }
            else // la fecha esta vacia
                errores.add("La fecha final no puede ser vacia", new ActionMessage("errors.required", "La fecha final"));

            if(this.getConvenio().getCodigo()<=0)
            {
                errores.add("Debe seleccionar un convenio", new ActionMessage("errors.required", "El convenio"));
            }
		}
		if(this.getEstado().equals("guardar"))
		{
			boolean fechaInicialInvalida=false;
			boolean fechaInicialVacia=false;
			boolean fechaFinalInvalida=false;
			boolean fechaFinalVacia=false;
			boolean fechaInicialMenorBusqueda=false;
			boolean fechaInicialMayorBusqueda=false;
			boolean fechaFinalMayorBusqueda=false;
			boolean fechaFinalMenorInicial=false;
			boolean fechaCargueVacia=false;
			boolean fechaCargueInvalida=false;
			boolean totalPacientesInvalido=false;
			boolean totalPacientesVacio=false;
			boolean upcVacio=false;
			boolean upcInvalido=false;
			boolean contratoVacio=false;
			boolean carguesGrupoEtareoVacio=false;
			
			for(int i=0; i<this.getNumContratosCargue(); i++)
			{
            	String tempoFechaInicial = (String)this.getContratoCargue("fechainicial_"+i);
            	String tempoFechaFinal = (String)this.getContratoCargue("fechafinal_"+i);
            	String tempoFechaCargue = (String)this.getContratoCargue("fechacargue_"+i);
            	String tempoTotalPacientes = this.getContratoCargue("totalpacientes_"+i)+"";
            	String tempoUpc = this.getContratoCargue("upc_"+i)+"";
            	String tempoFechaFinalModificada=this.getContratoCargue("fechafinalmodificada_"+i)+"";
            	String tempoContrato = (String)this.getContratoCargue("contrato_"+i);
            	String tempoEnBD = (String)this.getContratoCargue("enbd_"+i);
				String tipoPago = this.getContratoCargue("tipopago_"+i)+"";

				if(UtilidadCadena.noEsVacio(tempoEnBD) && !UtilidadTexto.getBoolean(tempoEnBD))
            	{ // si es un nuevo registro
		            if(UtilidadCadena.noEsVacio(tempoFechaInicial))
		            {
		                if(UtilidadFecha.esFechaValidaSegunAp(tempoFechaInicial))
		                { 
		                    if(!UtilidadFecha.esFechaMenorQueOtraReferencia(tempoFechaInicial, this.getFechaInicial()))
		                    { 
		                    	if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFinal(), tempoFechaInicial)) // la fecha inicial es mayor que la fecha final de la pagina de busqueda
		                    		fechaInicialMayorBusqueda=true;
		                    }
		                    else // la fecha inicial es menor que la de la pagina de busqueda
	                    		fechaInicialMenorBusqueda=true;
		                }
		                else // es un formato de fecha inválido
		                	fechaInicialInvalida=true;
		            }
		            else // la fecha esta vacia
		            	fechaInicialVacia=true;
	
		            if(UtilidadCadena.noEsVacio(tempoFechaFinal))
		            {
		                if(UtilidadFecha.esFechaValidaSegunAp(tempoFechaFinal))
		                { 
		                    if(!UtilidadFecha.esFechaMenorQueOtraReferencia(this.getFechaFinal(), tempoFechaFinal))
		                    { 
		                    	if(UtilidadFecha.esFechaMenorQueOtraReferencia(tempoFechaFinal, tempoFechaInicial)) // la fecha final es menor que la fecha inicial
		                    		fechaFinalMenorInicial=true;
		                    }
		                    else // la fecha final es mayor que la de la pagina de busqueda
		                    	fechaFinalMayorBusqueda=true;
		                }
		                else // es un formato de fecha inválido
		                	fechaFinalInvalida=true;
		            }
		            else // la fecha esta vacia
		            	fechaFinalVacia=true;
	
		            if(UtilidadCadena.noEsVacio(tempoFechaCargue))
		            {
		                if(!UtilidadFecha.esFechaValidaSegunAp(tempoFechaCargue))
	                    	fechaCargueInvalida=true;
		            }
		            else // la fecha esta vacia
		            	fechaCargueVacia=true;

		            if(!UtilidadCadena.noEsVacio(tempoContrato))
		            {
		            	contratoVacio=true;
		            }
            	}
	            
	            if(UtilidadCadena.noEsVacio(tempoTotalPacientes))
	            {
	            	try
	            	{
	            		if(Integer.parseInt(tempoTotalPacientes) <= 0)
	            			totalPacientesInvalido=true;
	            	}
	            	catch(NumberFormatException nfe)
	            	{
	            		totalPacientesInvalido=true;
	            	}
	            }
	            else
	            	totalPacientesVacio=true;

	            if(UtilidadCadena.noEsVacio(tempoUpc))
	            {
	            	try
	            	{
	    				if(tipoPago.equalsIgnoreCase(ConstantesBD.codigoTipoPagoUpc))
	    				{
		            		if(Double.parseDouble(tempoUpc) <= 0)
		            			upcInvalido=true;
	    				}
	            	}
	            	catch(NumberFormatException nfe)
	            	{
	            		upcInvalido=true;
	            	}
	            }
	            else
	            	upcVacio=true;

				if(tipoPago.equalsIgnoreCase(ConstantesBD.codigoTipoPagoGrupoEtareo))
				{
					int numCarguesGrupoEtareo = Integer.parseInt((String)this.getContratoCargue("numcarguesgrupoetareo_"+i));
					if(numCarguesGrupoEtareo==0)
						carguesGrupoEtareoVacio=true;
				}
            	
				if (UtilidadCadena.noEsVacio(tempoFechaFinalModificada))
				{
					if (!UtilidadFecha.validarFecha(tempoFechaFinalModificada))
					{
						errores.add("Fecha Inicial Invalido", new ActionMessage("errors.formatoFechaInvalido", " Final "));
					}
					else
					{
					//---- Validar que la fecha inicial sea menor o igual a la fecha del sistema-----//
						if((UtilidadFecha.conversionFormatoFechaABD(tempoFechaFinalModificada)).compareTo((UtilidadFecha.conversionFormatoFechaABD(tempoFechaFinal)))>0)
						{
							errores.add("fechaInicial", new ActionMessage("errors.fechaPosteriorIgualAOtraDeReferencia", "Final", "Final Cargue"));
						}
						if((UtilidadFecha.conversionFormatoFechaABD(tempoFechaInicial)).compareTo((UtilidadFecha.conversionFormatoFechaABD(tempoFechaFinalModificada)))>0)
						{
							errores.add("fechaInicial", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Final", "Inicial Cargue"));
						}
					}
					//tempoFechaFinalModificada
				}
			}
			if(carguesGrupoEtareoVacio)
	            errores.add("Los valores de los grupo etareos no han sido ingresados", new ActionMessage("errors.noIngresado", "Los valores de los Grupos Etarios"));
			if(fechaCargueInvalida)
	            errores.add("La fecha de cargue es invalida", new ActionMessage("errors.formatoFechaInvalido", "de cargue"));
			if(fechaCargueVacia)
	            errores.add("La fecha de cargue no puede ser vacia", new ActionMessage("errors.required", "La fecha de cargue"));
			if(fechaInicialInvalida)
	            errores.add("La fecha inicial es invalida", new ActionMessage("errors.formatoFechaInvalido", "inicial"));
			if(fechaInicialVacia)
	            errores.add("La fecha inicial no puede ser vacia", new ActionMessage("errors.required", "La fecha inicial"));
			if(fechaFinalInvalida)
	            errores.add("La fecha final es invalida", new ActionMessage("errors.formatoFechaInvalido", "final"));
			if(fechaFinalVacia)
	            errores.add("La fecha final no puede ser vacia", new ActionMessage("errors.required", "La fecha final"));
			if(fechaInicialMenorBusqueda)
	    		errores.add("La fecha inicial debe ser mayor o igual a la fecha "+this.getFechaInicial(), new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "inicial", this.getFechaInicial()));
			if(fechaInicialMayorBusqueda)
	    		errores.add("La fecha de inicial debe ser menor o igual a la fecha "+this.getFechaFinal(), new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "inicial", this.getFechaFinal()));
			if(fechaFinalMayorBusqueda)
	    		errores.add("La fecha final debe ser menor o igual a la fecha "+this.getFechaFinal(), new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "final", this.getFechaFinal()));
			if(fechaFinalMenorInicial)
	    		errores.add("La fecha final debe ser mayor o igual a la fecha "+this.getFechaFinal(), new ActionMessage("errors.fechaAnteriorAOtraDeReferencia", "final", "inicial"));
			if(totalPacientesVacio)
	            errores.add("El total de pacientes no puede ser vacio", new ActionMessage("errors.required", "El total de pacientes"));
			if(totalPacientesInvalido)
	            errores.add("El total de pacientes debe ser mayor que 0", new ActionMessage("errors.integerMayorQue", "Total pacientes", "0"));
			if(upcVacio)
	            errores.add("El upc no puede ser vacio", new ActionMessage("errors.required", "El upc"));
			if(upcInvalido)
	            errores.add("El upc debe ser un numero decimal", new ActionMessage("errors.floatMayorQue", "El upc", "0"));
			if(contratoVacio)
	            errores.add("El contrato no puede ser vacio", new ActionMessage("errors.required", "El contrato"));
		}
		
		if(!errores.isEmpty())
			this.estado=this.estadoAnterior;
		
		return errores;
	}

	/**
	 * @return Returns the contratosCargue.
	 */
	public HashMap getContratosCargue()
	{
		return contratosCargue;
	}

	/**
	 * @param contratosCargue The contratosCargue to set.
	 */
	public void setContratosCargue(HashMap contratosCargue)
	{
		this.contratosCargue = contratosCargue;
		this.numContratosCargue = Integer.parseInt((String)this.contratosCargue.get("numRegistros"));
	}
	
	/**
	 * Obtiene la cantidad de contratos cargue
	 * @return
	 */
	public int getNumContratosCargue()
	{
		return this.numContratosCargue;
	}
	
	public void setNumContratosCargue(int numContratosCargue)
	{
		this.numContratosCargue=numContratosCargue;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setContratoCargue(String key, Object value)
	{
		this.contratosCargue.put(key, value);
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getContratoCargue(String key)
	{
		return this.contratosCargue.get(key);
	}
	
	/**
	 * @return Returns the carguesGrupoEtareo.
	 */
	public HashMap getCarguesGrupoEtareo()
	{
		return carguesGrupoEtareo;
	}

	/**
	 * @param carguesGrupoEtareo The carguesGrupoEtareo to set.
	 */
	public void setCarguesGrupoEtareo(HashMap carguesGrupoEtareo)
	{
		this.carguesGrupoEtareo = carguesGrupoEtareo;
	}
	
	/**
	 * @param key
	 * @param value
	 */
	public void setCargueGrupoEtareo(String key, Object value)
	{
		this.carguesGrupoEtareo.put(key, value);
	}
	
	/**
	 * @param key
	 * @return
	 */
	public Object getCargueGrupoEtareo(String key)
	{
		return this.carguesGrupoEtareo.get(key);
	}

	/**
	 * @return Returns the convenio.
	 */
	public InfoDatosInt getConvenio()
	{
		return convenio;
	}

	/**
	 * @param convenio The convenio to set.
	 */
	public void setConvenio(InfoDatosInt convenio)
	{
		this.convenio = convenio;
	}

	/**
	 * @return Returns the estado.
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * @param estado The estado to set.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * @return Returns the fechaFinal.
	 */
	public String getFechaFinal()
	{
		return fechaFinal;
	}

	/**
	 * @param fechaFinal The fechaFinal to set.
	 */
	public void setFechaFinal(String fechaFinal)
	{
		this.fechaFinal = fechaFinal;
	}

	/**
	 * @return Returns the fechaInicial.
	 */
	public String getFechaInicial()
	{
		return fechaInicial;
	}

	/**
	 * @param fechaInicial The fechaInicial to set.
	 */
	public void setFechaInicial(String fechaInicial)
	{
		this.fechaInicial = fechaInicial;
	}
	
	/**
	 * @return Returns the estadoAnterior.
	 */
	public String getEstadoAnterior()
	{
		return estadoAnterior;
	}

	/**
	 * @param estadoAnterior The estadoAnterior to set.
	 */
	public void setEstadoAnterior(String estadoAnterior)
	{
		this.estadoAnterior = estadoAnterior;
	}
	/**
	 * @return Returns the mensaje.
	 */
	public String getMensaje()
	{
		return mensaje;
	}
	/**
	 * @param mensaje The mensaje to set.
	 */
	public void setMensaje(String mensaje)
	{
		this.mensaje = mensaje;
	}

	/**
	 * @return Returns the numeroContratoCargueEdicion.
	 */
	public int getNumeroContratoCargueEdicion()
	{
		return numeroContratoCargueEdicion;
	}

	/**
	 * @param numeroContratoCargueEdicion The numeroContratoCargueEdicion to set.
	 */
	public void setNumeroContratoCargueEdicion(int numeroContratoCargueEdicion)
	{
		this.numeroContratoCargueEdicion = numeroContratoCargueEdicion;
	}

	/**
	 * @return Returns the usuariosCargados.
	 */
	public Collection getUsuariosCargados() 
	{
		return usuariosCargados;
	}

	/**
	 * @param usuariosCargados The usuariosCargados to set.
	 */
	public void setUsuariosCargados(Collection usuariosCargados) 
	{
		this.usuariosCargados = usuariosCargados;
	}

	/**
	 * @return Returns the codigoContratoCargue.
	 */
	public int getNumeroContratoCargue() 
	{
		return numeroContratoCargue;
	}

	/**
	 * @param codigoContratoCargue The codigoContratoCargue to set.
	 */
	public void setNumeroContratoCargue(int numeroContratoCargue) 
	{
		this.numeroContratoCargue = numeroContratoCargue;
	}

	/**
	 * @return Returns the apellidoBusqueda.
	 */
	public String getApellidoBusqueda() 
	{
		return apellidoBusqueda;
	}

	/**
	 * @param apellidoBusqueda The apellidoBusqueda to set.
	 */
	public void setApellidoBusqueda(String apellidoBusqueda) 
	{
		this.apellidoBusqueda = apellidoBusqueda;
	}

	/**
	 * @return Returns the apellidoBusquedaCheck.
	 */
	public boolean getApellidoBusquedaCheck() 
	{
		return apellidoBusquedaCheck;
	}

	/**
	 * @param apellidoBusquedaCheck The apellidoBusquedaCheck to set.
	 */
	public void setApellidoBusquedaCheck(boolean apellidoBusquedaCheck) 
	{
		this.apellidoBusquedaCheck = apellidoBusquedaCheck;
	}

	/**
	 * @return Returns the nombreBusqueda.
	 */
	public String getNombreBusqueda() 
	{
		return nombreBusqueda;
	}

	/**
	 * @param nombreBusqueda The nombreBusqueda to set.
	 */
	public void setNombreBusqueda(String nombreBusqueda) 
	{
		this.nombreBusqueda = nombreBusqueda;
	}

	/**
	 * @return Returns the nombreBusquedaCheck.
	 */
	public boolean isNombreBusquedaCheck() 
	{
		return nombreBusquedaCheck;
	}

	/**
	 * @param nombreBusquedaCheck The nombreBusquedaCheck to set.
	 */
	public void setNombreBusquedaCheck(boolean nombreBusquedaCheck) 
	{
		this.nombreBusquedaCheck = nombreBusquedaCheck;
	}

	/**
	 * @return Returns the numeroIdBusqueda.
	 */
	public String getNumeroIdBusqueda() 
	{
		return numeroIdBusqueda;
	}

	/**
	 * @param numeroIdBusqueda The numeroIdBusqueda to set.
	 */
	public void setNumeroIdBusqueda(String numeroIdBusqueda) 
	{
		this.numeroIdBusqueda = numeroIdBusqueda;
	}

	/**
	 * @return Returns the numeroIdBusquedaCheck.
	 */
	public boolean isNumeroIdBusquedaCheck() 
	{
		return numeroIdBusquedaCheck;
	}

	/**
	 * @param numeroIdBusquedaCheck The numeroIdBusquedaCheck to set.
	 */
	public void setNumeroIdBusquedaCheck(boolean numeroIdBusquedaCheck) 
	{
		this.numeroIdBusquedaCheck = numeroIdBusquedaCheck;
	}

	/**
	 * @return Returns the tipoIdBusqueda.
	 */
	public String getTipoIdBusqueda() 
	{
		return tipoIdBusqueda;
	}

	/**
	 * @param tipoIdBusqueda The tipoIdBusqueda to set.
	 */
	public void setTipoIdBusqueda(String tipoIdBusqueda) 
	{
		this.tipoIdBusqueda = tipoIdBusqueda;
	}

	/**
	 * @return Returns the tipoIdBusquedaCheck.
	 */
	public boolean isTipoIdBusquedaCheck() 
	{
		return tipoIdBusquedaCheck;
	}

	/**
	 * @param tipoIdBusquedaCheck The tipoIdBusquedaCheck to set.
	 */
	public void setTipoIdBusquedaCheck(boolean tipoIdBusquedaCheck) 
	{
		this.tipoIdBusquedaCheck = tipoIdBusquedaCheck;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getRegEliminar() {
		return regEliminar;
	}
	
	/**
	 * 
	 * @param regEliminar
	 */
	public void setRegEliminar(int regEliminar) {
		this.regEliminar = regEliminar;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getContratosCargueEliminados() {
		return contratosCargueEliminados;
	}
	
	/**
	 * 
	 * @param contratosCargueEliminados
	 */
	public void setContratosCargueEliminados(HashMap contratosCargueEliminados) {
		this.contratosCargueEliminados = contratosCargueEliminados;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getNumeroFicha() {
		return numeroFicha;
	}
	
	/**
	 * 
	 * @param numeroFicha
	 */
	public void setNumeroFicha(String numeroFicha) {
		this.numeroFicha = numeroFicha;
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean isNumeroFichaCheck() {
		return numeroFichaCheck;
	}
	
	/**
	 * 
	 * @param numeroFichaCheck
	 */
	public void setNumeroFichaCheck(boolean numeroFichaCheck) {
		this.numeroFichaCheck = numeroFichaCheck;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsuarioInactivar() {
		return usuarioInactivar;
	}
	
	/**
	 * 
	 * @param usuarioInactivar
	 */
	public void setUsuarioInactivar(String usuarioInactivar) {
		this.usuarioInactivar = usuarioInactivar;
	}
	
	/**
	 * 
	 * @return
	 */
	public String getUsuarioEliminar() {
		return usuarioEliminar;
	}
	
	/**
	 * 
	 * @param usuarioEliminar
	 */
	public void setUsuarioEliminar(String usuarioEliminar) {
		this.usuarioEliminar = usuarioEliminar;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getMapaUsuariosCargados() {
		return mapaUsuariosCargados;
	}
	
	/**
	 * 
	 * @param mapaUsuariosCargados
	 */
	public void setMapaUsuariosCargados(HashMap mapaUsuariosCargados) {
		this.mapaUsuariosCargados = mapaUsuariosCargados;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getMapaLogEliminados() {
		return mapaLogEliminados;
	}

	/**
	 * 	
	 * @param mapaLogEliminados
	 */
	public void setMapaLogEliminados(HashMap mapaLogEliminados) {
		this.mapaLogEliminados = mapaLogEliminados;
	}
	
	/**
	 * 
	 * @return
	 */
	public HashMap getMapaLogActivos() {
		return mapaLogActivos;
	}

	/**
	 * 
	 * @param mapaLogActivos
	 */
	public void setMapaLogActivos(HashMap mapaLogActivos) {
		this.mapaLogActivos = mapaLogActivos;
	}
	
	/**
	 * 
	 * @return
	 */
	public ResultadoBoolean getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * 
	 * @param mostrarMensaje
	 */
	public void setMostrarMensaje(ResultadoBoolean mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}
	
	
}
