/*
 * Created on 5/10/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.tesoreria.DtoConceptosIngTesoreria;
import com.princetonsa.dto.tesoreria.DtoDevolRecibosCaja;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;

/**
 * @version 1.0, 5/10/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class ConsultaRecibosCajaForm extends ValidatorForm
{
    
    /**
     * Variable para manejar el estado de la funcionalidad 
     */
    private String estado;

    /**
     * Variable para manejar la istitucion del usuario que se encuentra en el sisetma
     */
    private int institucion;
    
//*****************Inicio Declaracion variables manejadas en la busqueda de recibos de caja
    /**
     * Para menjar el rango inicial del numero de ricibos de caja a consultar
     */
    private String reciboCajaInicial;
    /**
     * Para manejar el rango final de numero de recibos de caja a consultar
     */
    private String reciboCajaFinal;
    /**
     * Para manejar la fecha inicial del rango por el que se buscan los recibos de caja.
     */
    private String fechaReciboCajaInicial;
    /**
     * Para manejar la fecha final del rango por el que se buscan los recibos de caja.
     */
    private String fechaReciboCajaFina;
    
    /**
     * código del concepto del recibo de caja para hacer la busqueda
     */
    private String codigoConceptoReciboCaja;
    
    /**
     * Codigo del estado del recibo de caja
     */
    private int estadoReciboCaja;
    
    /**
     * Login del usuario que elaboro el recibo de caja.
     */
    private String usuarioElaboraReciboCaja;
    
    /**
     * Codigo de la caja que elaboro el recibo de caja
     */
    private int cajaElaboraReciboCaja;
    
    /**
     * centro atencion
     */
    private int codigoCentroAtencion;
    
    /**
     * Mapa que contiene todos los recibos de caja que se obtuvieron en la consulta.
     */
    private HashMap recibosCaja;
    
    /**
     * documento soporte
     */
    private String docSoporte;
    
    /**
     * tipoIdentificacion
     */
    private String tipoIdBeneficiario;
    
    /**
     * 
     */
    private String numeroIdBeneficiario;
    
    
    //*****************Fin Declaracion variables manejadas en la busqueda de recibos de caja
    
    /**
     * Numero del Recibo de caja del cual se desea conocer el detalle
     */
    private String indexReciboDetalle;
    
    /**
     * almacena el indice por el cual 
     * se va ordenar el HashMap
     */
    private String patronOrdenar;
    
    /**
     * almacena el ultimo indice por el 
     * cual se ordeno el HashMap
     */
    private String ultimoPatron;
    /**
     * Metodo que inicializa las variables del form
     */
    //*************************************Variables Utilizadas en El detalle de Recibos de Caja****************************************************//
    /**
     * Numero del recibo de caja.
     */
    private String numeroReciboCaja;
    
    /**
     * Consecutivo del Recibo de Caja
     */
    private String consecutivoReciboCaja;
    
    /**
     * Fecha del recibo de caja.
     */
    private String fechaReciboCaja;
    /**
     * Hora del recibo de caja.
     */
    private String horaReciboCaja;
    /**
     * Codigo Estado del Recibo de Caja mostrado en el detalle.
     */
    private int codigoEstadoReciboCaja;
    /**
     * Descripcion Estado del Recibo de Caja mostrado en el detalle.
     */
    private String descripcionEstadoReciboCaja;
    /**
     * Usuario del Recibo de Caja.
     */
    private String usuarioReciboCaja;
    /**
     * Consecutivo de la Caja
     */
    private String consecutivoCaja;
    /**
     * Codigo de la Caja
     */
    private String codigoCaja;
    /**
     * Descripcion de la Caja
     */
    private String descripcionCaja;
    
    /**
     * Observaciones Recibo Caja en el detalle.
     */
    private String observaciones;
    
    /**
     * Valor total del Recibo de Caja
     */
    private String valorTotalReciboCaja;
    
    /**
     * Recibido de
     */
    private String recibidoDe;
    
    /**
	 * Mapa para manejar los conceptos del RC 
	 */
	private HashMap conceptosRC;
	
	/**
	 * Mapa para manejar las Formas de Pago
	 */
    private HashMap formasPagoRC;

	/**
	 * Mapa para manejar la informacion de la Anulacion
	 */
    private HashMap anulacionRC;
	
    /**
     * Mapaa para manejar el total de pagos en efectivo, total de pagos por cheques, total de pagos con tarjetas.
     */
    private HashMap totalPagos;
    //	*********************************VARIABLES PARA EL MANEJO DE LA ANULACION **************************************///
    
    /**
     * Numero de la anulacion del recibo de caja
     */
    private String numeroAnulacion;
    
    /**
     *Fecha de anulacion 
     */
    private String fechaAnulacion;
    
    /**
     * Hora de Anulacion
     */
    private String horaAnulacion;
    /**
     * Usuario que anula
     */
    private String usuarioAnulacion;
    
    /**
     * Motivo de la Anulacion
     */
    private String motivoAnulacion;
    
    /**
     * Observaciones de la Anulacion
     */
    private String observacionesAnulacion;
    
    /**
     * 
     * 
     */
    private int codigoFormaPago;
    
    /**
     * 
     */
    private boolean manejaConversionMoneda;
    
    /**
     * 
     */
    private HashMap tiposMonedaTagMap;
    
    /**
     * 
     */
    private int indexTipoMoneda;
    
    /**
     * 
     */
    private String descripcionConversion;
    
    /**
     * 
     */
    private double factorConversion;
    
    /**
     * 
    */
    private ArrayList<DtoConceptosIngTesoreria> listadoConceptos;
    
    /**
     *
     */
    private ArrayList<DtoDevolRecibosCaja> listadoDevolucion;
    
    /**
     * Atributo con el nombre del concepto seleccionado
     */
    private String descripcionConcepto;
    
    /**
     * Atributo con el nombre de la forma de pago seleccionada
     */
    private String descripcionFormaPago;
    
    /**
     * Atributo con el nombre del usuario seleccionado
     */
    private String nombreUsuario;
    
    /**
     * Atributo con la descripción del tipo de identificación seleccionado
     */
    private String descripcionTipoId;
    
    /**
     * Atributo con el nombre de la caja seleccionada en el filtro de búsqueda.
     */
    private String descripcionCajaFiltro;
    
    
    //  *******************************FIN VARIABLES PARA EL MANEJO DE LA ANULACION **************************************///
    //*************************************Fin Variables Utilizadas en El detalle de Recibos de Caja****************************************************//
    public void reset(int institucion)
    {
        this.reciboCajaInicial="";
        this.reciboCajaFinal="";
        this.fechaReciboCajaInicial="";
        this.fechaReciboCajaFina="";
        this.codigoConceptoReciboCaja="";
        this.estadoReciboCaja=ConstantesBD.codigoNuncaValido;
        this.usuarioElaboraReciboCaja="";
        this.cajaElaboraReciboCaja=ConstantesBD.codigoNuncaValido;
        this.codigoCentroAtencion=ConstantesBD.codigoNuncaValido;
        this.recibosCaja=new HashMap();
        this.indexReciboDetalle="";
        this.patronOrdenar="";
        this.ultimoPatron="";
        this.codigoFormaPago=ConstantesBD.codigoNuncaValido;
        
        this.numeroReciboCaja="";
        this.fechaReciboCaja="";
        this.horaReciboCaja="";
        this.codigoEstadoReciboCaja=ConstantesBD.codigoNuncaValido;
        this.descripcionEstadoReciboCaja="";
        this.usuarioReciboCaja="";
        this.consecutivoCaja="";
        this.codigoCaja="";
        this.descripcionCaja="";
        this.valorTotalReciboCaja="";
        this.recibidoDe="";
        this.observaciones="";
        this.conceptosRC=new HashMap();
        this.formasPagoRC=new HashMap();
        this.anulacionRC=new HashMap();
        this.totalPagos=new HashMap();
        
        this.numeroAnulacion="";
        this.fechaAnulacion="";
        this.horaAnulacion="";
        this.usuarioAnulacion="";
        this.motivoAnulacion="";
        this.observacionesAnulacion="";
        
        this.docSoporte="";
        this.tipoIdBeneficiario="";
        this.numeroIdBeneficiario="";
        
        this.manejaConversionMoneda=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(institucion));
        
        this.tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(institucion, /*mostrarMonedaManejaInstitucion*/false);
        this.indexTipoMoneda=ConstantesBD.codigoNuncaValido;
        
        this.descripcionConversion="";
        this.factorConversion=1;
        this.listadoConceptos=new ArrayList<DtoConceptosIngTesoreria>();
        this.listadoDevolucion=new ArrayList<DtoDevolRecibosCaja>();
        
        this.setDescripcionConcepto("");
        
        this.setDescripcionFormaPago("");
        
        this.setNombreUsuario("");
        
        this.setDescripcionCajaFiltro("");
        
        this.setDescripcionTipoId("");
        
        this.setConsecutivoReciboCaja("");
    }
    /**
     * @return Returns the cajaElaboraReciboCaja.
     */
    public int getCajaElaboraReciboCaja()
    {
        return cajaElaboraReciboCaja;
    }
    /**
     * @param cajaElaboraReciboCaja The cajaElaboraReciboCaja to set.
     */
    public void setCajaElaboraReciboCaja(int cajaElaboraReciboCaja)
    {
        this.cajaElaboraReciboCaja = cajaElaboraReciboCaja;
    }
    /**
     * @return Returns the codigoConceptoReciboCaja.
     */
    public String getCodigoConceptoReciboCaja()
    {
        return codigoConceptoReciboCaja;
    }
    /**
     * @param codigoConceptoReciboCaja The codigoConceptoReciboCaja to set.
     */
    public void setCodigoConceptoReciboCaja(String codigoConceptoReciboCaja)
    {
        this.codigoConceptoReciboCaja = codigoConceptoReciboCaja;
    }
    /**
     * @return Returns the estadoReciboCaja.
     */
    public int getEstadoReciboCaja()
    {
        return estadoReciboCaja;
    }
    /**
     * @param estadoReciboCaja The estadoReciboCaja to set.
     */
    public void setEstadoReciboCaja(int estadoReciboCaja)
    {
        this.estadoReciboCaja = estadoReciboCaja;
    }
    /**
     * @return Returns the fechaReciboCajaInicial.
     */
    public String getFechaReciboCajaInicial()
    {
        return fechaReciboCajaInicial;
    }
    /**
     * @param fechaReciboCajaInicial The fechaReciboCajaInicial to set.
     */
    public void setFechaReciboCajaInicial(String fechaReciboCajaInicial)
    {
        this.fechaReciboCajaInicial = fechaReciboCajaInicial;
    }
    /**
     * @return Returns the fechaReciboCajaFina.
     */
    public String getFechaReciboCajaFina()
    {
        return fechaReciboCajaFina;
    }
    /**
     * @param fechaReciboCajaFina The fechaReciboCajaFina to set.
     */
    public void setFechaReciboCajaFina(String fechaReciboCajaFina)
    {
        this.fechaReciboCajaFina = fechaReciboCajaFina;
    }
    
    /**
     * @return Returns the reciboCajaFinal.
     */
    public String getReciboCajaFinal()
    {
        return reciboCajaFinal;
    }
    /**
     * @param reciboCajaFinal The reciboCajaFinal to set.
     */
    public void setReciboCajaFinal(String reciboCajaFinal)
    {
        this.reciboCajaFinal = reciboCajaFinal;
        if(this.reciboCajaFinal.trim().equals(""))
        {
            this.reciboCajaFinal=this.reciboCajaInicial;
        }
        if(this.reciboCajaInicial.trim().equals(""))
        {
            this.reciboCajaInicial=this.reciboCajaFinal;
        }
    }
    /**
     * @return Returns the reciboCajaInicial.
     */
    public String getReciboCajaInicial()
    {
        return reciboCajaInicial;
    }
    /**
     * @param reciboCajaInicial The reciboCajaInicial to set.
     */
    public void setReciboCajaInicial(String reciboCajaInicial)
    {
        this.reciboCajaInicial = reciboCajaInicial;
    }
    /**
     * @return Returns the recibosCaja.
     */
    public HashMap getRecibosCaja()
    {
        return recibosCaja;
    }
    /**
     * @param recibosCaja The recibosCaja to set.
     */
    public void setRecibosCaja(HashMap recibosCaja)
    {
        this.recibosCaja = recibosCaja;
    }
    /**
     * @return Returns the usuarioElaboraReciboCaja.
     */
    public String getUsuarioElaboraReciboCaja()
    {
        return usuarioElaboraReciboCaja;
    }
    /**
     * @param usuarioElaboraReciboCaja The usuarioElaboraReciboCaja to set.
     */
    public void setUsuarioElaboraReciboCaja(String usuarioElaboraReciboCaja)
    {
        this.usuarioElaboraReciboCaja = usuarioElaboraReciboCaja;
    }
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(this.estado.equals("buscar"))
		{
			//primero validamos el recibo de caja inicial
			if(!this.reciboCajaInicial.trim().equals("") || !this.reciboCajaFinal.trim().equals(""))
			{
				if(!UtilidadTexto.isNumber(this.reciboCajaInicial))
		    	{
		    		errores.add("", new ActionMessage("errors.integer", "El recibo caja inicial"));
		    	}
				if(!UtilidadTexto.isNumber(this.reciboCajaFinal))
		    	{
		    		errores.add("", new ActionMessage("errors.integer", "El recibo caja final"));
		    	}
		    	if(UtilidadTexto.isNumber(this.reciboCajaInicial) && UtilidadTexto.isNumber(this.reciboCajaFinal))
		    	{	
			        if(Double.parseDouble(this.reciboCajaFinal)<Double.parseDouble(this.reciboCajaInicial))
			        {
			            errores.add("Recibo Caja Inicial debe ser Mayor Recibo Caja final",new ActionMessage("errors.debeSerNumeroMayor","El Recibo de Caja Final "+this.reciboCajaFinal,"El Recibo de Caja Inicial "+this.reciboCajaInicial));
			        }
		    	}    
		    }	
			
			//si no existe informacion de los rangos de recibos de caja entonces las fechas son requeridas 
			if (errores.isEmpty())
			{	
			    if(this.fechaReciboCajaInicial.trim().equals("") && this.reciboCajaInicial.trim().equals("") && this.fechaReciboCajaFina.trim().equals(""))
			    {
			    	errores.add("Fecha Requerido",new ActionMessage("errors.required","La fecha Inicial"));
			    	errores.add("Fecha Requerido",new ActionMessage("errors.required","La fecha Final"));
				}
				if(!this.fechaReciboCajaInicial.trim().equals("") || !this.fechaReciboCajaFina.trim().equals(""))
				{
					if(!UtilidadFecha.validarFecha(this.fechaReciboCajaInicial))
					{
						errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido","Inicial "+this.fechaReciboCajaInicial));
					}
					else
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaReciboCajaInicial,UtilidadFecha.getFechaActual()))
						{
							errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Inicial ("+this.fechaReciboCajaInicial+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
						}
					}
					if(!UtilidadFecha.validarFecha(this.fechaReciboCajaFina))
					{
						errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido","Final "+this.fechaReciboCajaFina));
					}
					else
					{
					    if(UtilidadFecha.validarFecha(this.fechaReciboCajaInicial))
						{
						    if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.fechaReciboCajaFina,this.fechaReciboCajaInicial))
						    {
						        errores.add("Fecha Final mayor a fecha Inicial",new ActionMessage("errors.fechaAnteriorAOtraDeReferencia","Final ("+this.fechaReciboCajaFina+")","Inicial ("+this.fechaReciboCajaInicial+")"));
						    }
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaReciboCajaFina,UtilidadFecha.getFechaActual()))
						{
							errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Final ("+this.fechaReciboCajaFina+")","Actual ("+UtilidadFecha.getFechaActual()+")"));
						}
					}
				}
			    
			    if(errores.isEmpty() && !this.fechaReciboCajaInicial.trim().equals(""))
			    {
	    			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.fechaReciboCajaInicial,this.fechaReciboCajaFina))
					{
						errores.add("Fecha Mayor del Sistema",new ActionMessage("errors.fechaPosteriorAOtraDeReferencia","Inicial ("+this.fechaReciboCajaInicial+")","Final ("+this.fechaReciboCajaFina+")"));
					}
			    	else
			    	{	
				    	int numeroMesesDiferencia=UtilidadFecha.numeroMesesEntreFechasExacta(this.getFechaReciboCajaInicial(), this.getFechaReciboCajaFina());
				        if(numeroMesesDiferencia>2)
				        {
				        	errores.add("", new ActionMessage("errors.rangoMayorMeses", "PARA CONSULTAR RECIBOS CAJA", "2"));
				        }
			    	}    
			    }
			}
		}
		return errores;
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
     * @return Returns the institucion.
     */
    public int getInstitucion()
    {
        return institucion;
    }
    /**
     * @param institucion The institucion to set.
     */
    public void setInstitucion(int institucion)
    {
        this.institucion = institucion;
    }
    /**
     * @return Returns the numeroReciboDetalle.
     */
    public String getIndexReciboDetalle()
    {
        return indexReciboDetalle;
    }
    /**
     * @param numeroReciboDetalle The numeroReciboDetalle to set.
     */
    public void setIndexReciboDetalle(String numeroReciboDetalle)
    {
        this.indexReciboDetalle = numeroReciboDetalle;
    }
    /**
     * @return Returns the patronOrdenar.
     */
    public String getPatronOrdenar()
    {
        return patronOrdenar;
    }
    /**
     * @param patronOrdenar The patronOrdenar to set.
     */
    public void setPatronOrdenar(String patronOrdenar)
    {
        this.patronOrdenar = patronOrdenar;
    }
    /**
     * @return Returns the ultimoPatron.
     */
    public String getUltimoPatron()
    {
        return ultimoPatron;
    }
    /**
     * @param ultimoPatron The ultimoPatron to set.
     */
    public void setUltimoPatron(String ultimoPatron)
    {
        this.ultimoPatron = ultimoPatron;
    }
    /**
     * @return Returns the codigoCaja.
     */
    public String getCodigoCaja()
    {
        return codigoCaja;
    }
    /**
     * @param codigoCaja The codigoCaja to set.
     */
    public void setCodigoCaja(String codigoCaja)
    {
        this.codigoCaja = codigoCaja;
    }
    /**
     * @return Returns the codigoEstadoReciboCaja.
     */
    public int getCodigoEstadoReciboCaja()
    {
        return codigoEstadoReciboCaja;
    }
    /**
     * @param codigoEstadoReciboCaja The codigoEstadoReciboCaja to set.
     */
    public void setCodigoEstadoReciboCaja(int codigoEstadoReciboCaja)
    {
        this.codigoEstadoReciboCaja = codigoEstadoReciboCaja;
    }
    /**
     * @return Returns the consecutivoCaja.
     */
    public String getConsecutivoCaja()
    {
        return consecutivoCaja;
    }
    /**
     * @param consecutivoCaja The consecutivoCaja to set.
     */
    public void setConsecutivoCaja(String consecutivoCaja)
    {
        this.consecutivoCaja = consecutivoCaja;
    }
    /**
     * @return Returns the descripcionCaja.
     */
    public String getDescripcionCaja()
    {
        return descripcionCaja;
    }
    /**
     * @param descripcionCaja The descripcionCaja to set.
     */
    public void setDescripcionCaja(String descripcionCaja)
    {
        this.descripcionCaja = descripcionCaja;
    }
    /**
     * @return Returns the descripcionEstadoReciboCaja.
     */
    public String getDescripcionEstadoReciboCaja()
    {
        return descripcionEstadoReciboCaja;
    }
    /**
     * @param descripcionEstadoReciboCaja The descripcionEstadoReciboCaja to set.
     */
    public void setDescripcionEstadoReciboCaja(String descripcionEstadoReciboCaja)
    {
        this.descripcionEstadoReciboCaja = descripcionEstadoReciboCaja;
    }
    /**
     * @return Returns the fechaAnulacion.
     */
    public String getFechaAnulacion()
    {
        return fechaAnulacion;
    }
    /**
     * @param fechaAnulacion The fechaAnulacion to set.
     */
    public void setFechaAnulacion(String fechaAnulacion)
    {
        this.fechaAnulacion = fechaAnulacion;
    }
    /**
     * @return Returns the fechaReciboCaja.
     */
    public String getFechaReciboCaja()
    {
        return fechaReciboCaja;
    }
    /**
     * @param fechaReciboCaja The fechaReciboCaja to set.
     */
    public void setFechaReciboCaja(String fechaReciboCaja)
    {
        this.fechaReciboCaja = fechaReciboCaja;
    }
    /**
     * @return Returns the horaAnulacion.
     */
    public String getHoraAnulacion()
    {
        return horaAnulacion;
    }
    /**
     * @param horaAnulacion The horaAnulacion to set.
     */
    public void setHoraAnulacion(String horaAnulacion)
    {
        this.horaAnulacion = horaAnulacion;
    }
    /**
     * @return Returns the horaReciboCaja.
     */
    public String getHoraReciboCaja()
    {
        return horaReciboCaja;
    }
    /**
     * @param horaReciboCaja The horaReciboCaja to set.
     */
    public void setHoraReciboCaja(String horaReciboCaja)
    {
        this.horaReciboCaja = horaReciboCaja;
    }
    /**
     * @return Returns the motivoAnulacion.
     */
    public String getMotivoAnulacion()
    {
        return motivoAnulacion;
    }
    /**
     * @param motivoAnulacion The motivoAnulacion to set.
     */
    public void setMotivoAnulacion(String motivoAnulacion)
    {
        this.motivoAnulacion = motivoAnulacion;
    }
    /**
     * @return Returns the numeroAnulacion.
     */
    public String getNumeroAnulacion()
    {
        return numeroAnulacion;
    }
    /**
     * @param numeroAnulacion The numeroAnulacion to set.
     */
    public void setNumeroAnulacion(String numeroAnulacion)
    {
        this.numeroAnulacion = numeroAnulacion;
    }
    /**
     * @return Returns the numeroReciboCaja.
     */
    public String getNumeroReciboCaja()
    {
        return numeroReciboCaja;
    }
    /**
     * @param numeroReciboCaja The numeroReciboCaja to set.
     */
    public void setNumeroReciboCaja(String numeroReciboCaja)
    {
        this.numeroReciboCaja = numeroReciboCaja;
    }
    /**
     * @return Returns the observacionesAnulacion.
     */
    public String getObservacionesAnulacion()
    {
        return observacionesAnulacion;
    }
    /**
     * @param observacionesAnulacion The observacionesAnulacion to set.
     */
    public void setObservacionesAnulacion(String observacionesAnulacion)
    {
        this.observacionesAnulacion = observacionesAnulacion;
    }
    /**
     * @return Returns the usuarioAnulacion.
     */
    public String getUsuarioAnulacion()
    {
        return usuarioAnulacion;
    }
    /**
     * @param usuarioAnulacion The usuarioAnulacion to set.
     */
    public void setUsuarioAnulacion(String usuarioAnulacion)
    {
        this.usuarioAnulacion = usuarioAnulacion;
    }
    /**
     * @return Returns the usuarioReciboCaja.
     */
    public String getUsuarioReciboCaja()
    {
        return usuarioReciboCaja;
    }
    /**
     * @param usuarioReciboCaja The usuarioReciboCaja to set.
     */
    public void setUsuarioReciboCaja(String usuarioReciboCaja)
    {
        this.usuarioReciboCaja = usuarioReciboCaja;
    }
    /**
     * @return Returns the valorTotalReciboCaja.
     */
    public String getValorTotalReciboCaja()
    {
        return valorTotalReciboCaja;
    }
    /**
     * @param valorTotalReciboCaja The valorTotalReciboCaja to set.
     */
    public void setValorTotalReciboCaja(String valorTotalReciboCaja)
    {
        this.valorTotalReciboCaja = valorTotalReciboCaja;
    }
    /**
     * @return Returns the conceptosRC.
     */
    public HashMap getConceptosRC()
    {
        return conceptosRC;
    }
    /**
     * @param conceptosRC The conceptosRC to set.
     */
    public void setConceptosRC(HashMap conceptosRC)
    {
        this.conceptosRC = conceptosRC;
    }
    /**
     * @return Returns the formasPagoRC.
     */
    public HashMap getFormasPagoRC()
    {
        return formasPagoRC;
    }
    /**
     * @param formasPagoRC The formasPagoRC to set.
     */
    public void setFormasPagoRC(HashMap formasPagoRC)
    {
        this.formasPagoRC = formasPagoRC;
    }
    /**
     * @return Returns the recibidoDe.
     */
    public String getRecibidoDe()
    {
        return recibidoDe;
    }
    /**
     * @param recibidoDe The recibidoDe to set.
     */
    public void setRecibidoDe(String recibidoDe)
    {
        this.recibidoDe = recibidoDe;
    }
    /**
     * @return Returns the observaciones.
     */
    public String getObservaciones()
    {
        return observaciones;
    }
    /**
     * @param observaciones The observaciones to set.
     */
    public void setObservaciones(String observaciones)
    {
        this.observaciones = observaciones;
    }
    /**
     * @return Returns the anulacionRC.
     */
    public HashMap getAnulacionRC()
    {
        return anulacionRC;
    }
    /**
     * @param anulacionRC The anulacionRC to set.
     */
    public void setAnulacionRC(HashMap anulacionRC)
    {
        this.anulacionRC = anulacionRC;
    }
    /**
     * @return Returns the totalPagos.
     */
    public HashMap getTotalPagos()
    {
        return totalPagos;
    }
    /**
     * @param totalPagos The totalPagos to set.
     */
    public void setTotalPagos(HashMap totalPagos)
    {
        this.totalPagos = totalPagos;
    }
	public int getCodigoFormaPago() {
		return codigoFormaPago;
	}
	public void setCodigoFormaPago(int codigoFormaPago) {
		this.codigoFormaPago = codigoFormaPago;
	}
	/**
	 * @return Returns the codigoCentroAtencion.
	 */
	public int getCodigoCentroAtencion() {
		return codigoCentroAtencion;
	}
	/**
	 * @param codigoCentroAtencion The codigoCentroAtencion to set.
	 */
	public void setCodigoCentroAtencion(int codigoCentroAtencion) {
		this.codigoCentroAtencion = codigoCentroAtencion;
	}
	/**
	 * @return Returns the docSoporte.
	 */
	public String getDocSoporte()
	{
		return docSoporte;
	}
	/**
	 * @param docSoporte The docSoporte to set.
	 */
	public void setDocSoporte(String docSoporte)
	{
		this.docSoporte = docSoporte;
	}
	/**
	 * @return Returns the numeroIdBeneficiario.
	 */
	public String getNumeroIdBeneficiario()
	{
		return numeroIdBeneficiario;
	}
	/**
	 * @param numeroIdBeneficiario The numeroIdBeneficiario to set.
	 */
	public void setNumeroIdBeneficiario(String numeroIdBeneficiario)
	{
		this.numeroIdBeneficiario = numeroIdBeneficiario;
	}
	/**
	 * @return Returns the tipoIdBeneficiario.
	 */
	public String getTipoIdBeneficiario()
	{
		return tipoIdBeneficiario;
	}
	/**
	 * @param tipoIdBeneficiario The tipoIdBeneficiario to set.
	 */
	public void setTipoIdBeneficiario(String tipoIdBeneficiario)
	{
		this.tipoIdBeneficiario = tipoIdBeneficiario;
	}
	
	/**
	 * @return the tiposMonedaTagMap
	 */
	public HashMap getTiposMonedaTagMap() {
		return tiposMonedaTagMap;
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(HashMap tiposMonedaTagMap) {
		this.tiposMonedaTagMap = tiposMonedaTagMap;
	}

	/**
	 * @return the tiposMonedaTagMap
	 */
	public Object getTiposMonedaTagMap(Object key) {
		return tiposMonedaTagMap.get(key);
	}

	/**
	 * @param tiposMonedaTagMap the tiposMonedaTagMap to set
	 */
	public void setTiposMonedaTagMap(Object key, Object value) {
		this.tiposMonedaTagMap.put(key, value);
	}
	/**
	 * @return the manejaConversionMoneda
	 */
	public boolean isManejaConversionMoneda() {
		return manejaConversionMoneda;
	}
	/**
	 * @param manejaConversionMoneda the manejaConversionMoneda to set
	 */
	public void setManejaConversionMoneda(boolean manejaConversionMoneda) {
		this.manejaConversionMoneda = manejaConversionMoneda;
	}
	/**
	 * @return the indexTipoMoneda
	 */
	public int getIndexTipoMoneda() {
		return indexTipoMoneda;
	}
	/**
	 * @param indexTipoMoneda the indexTipoMoneda to set
	 */
	public void setIndexTipoMoneda(int indexTipoMoneda) {
		this.indexTipoMoneda = indexTipoMoneda;
	}
	/**
	 * @return the descripcionConversion
	 */
	public String getDescripcionConversion() {
		return descripcionConversion;
	}
	/**
	 * @param descripcionConversion the descripcionConversion to set
	 */
	public void setDescripcionConversion(String descripcionConversion) {
		this.descripcionConversion = descripcionConversion;
	}
	/**
	 * @return the factorConversion
	 */
	public double getFactorConversion() {
		return factorConversion;
	}
	/**
	 * @param factorConversion the factorConversion to set
	 */
	public void setFactorConversion(double factorConversion) {
		this.factorConversion = factorConversion;
	}
	public ArrayList<DtoConceptosIngTesoreria> getListadoConceptos() {
		return listadoConceptos;
	}
	public void setListadoConceptos(
			ArrayList<DtoConceptosIngTesoreria> listadoConceptos) {
		this.listadoConceptos = listadoConceptos;
	}
	public ArrayList<DtoDevolRecibosCaja> getListadoDevolucion() {
		return listadoDevolucion;
	}
	public void setListadoDevolucion(
			ArrayList<DtoDevolRecibosCaja> listadoDevolucion) {
		this.listadoDevolucion = listadoDevolucion;
	}
	
	/**
	 * @param nombreUsuario the nombreUsuario to set
	 */
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	/**
	 * @return the nombreUsuario
	 */
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	/**
	 * @param descripcionConcepto the descripcionConcepto to set
	 */
	public void setDescripcionConcepto(String descripcionConcepto) {
		this.descripcionConcepto = descripcionConcepto;
	}
	/**
	 * @return the descripcionConcepto
	 */
	public String getDescripcionConcepto() {
		return descripcionConcepto;
	}
	/**
	 * @param descripcionFormaPago the descripcionFormaPago to set
	 */
	public void setDescripcionFormaPago(String descripcionFormaPago) {
		this.descripcionFormaPago = descripcionFormaPago;
	}
	/**
	 * @return the descripcionFormaPago
	 */
	public String getDescripcionFormaPago() {
		return descripcionFormaPago;
	}
	/**
	 * @param descripcionCajaFiltro the descripcionCajaFiltro to set
	 */
	public void setDescripcionCajaFiltro(String descripcionCajaFiltro) {
		this.descripcionCajaFiltro = descripcionCajaFiltro;
	}
	/**
	 * @return the descripcionCajaFiltro
	 */
	public String getDescripcionCajaFiltro() {
		return descripcionCajaFiltro;
	}
	/**
	 * @param descripcionTipoId the descripcionTipoId to set
	 */
	public void setDescripcionTipoId(String descripcionTipoId) {
		this.descripcionTipoId = descripcionTipoId;
	}
	/**
	 * @return the descripcionTipoId
	 */
	public String getDescripcionTipoId() {
		return descripcionTipoId;
	}
	/**
	 * @return the consecutivoReciboCaja
	 */
	public String getConsecutivoReciboCaja() {
		return consecutivoReciboCaja;
	}
	/**
	 * @param consecutivoReciboCaja the consecutivoReciboCaja to set
	 */
	public void setConsecutivoReciboCaja(String consecutivoReciboCaja) {
		this.consecutivoReciboCaja = consecutivoReciboCaja;
	}
}
