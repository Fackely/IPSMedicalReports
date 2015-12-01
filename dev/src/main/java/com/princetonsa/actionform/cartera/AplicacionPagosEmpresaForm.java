/*
 * Created on 3/11/2005
 * 
 * @author <a href="mailto:artotor@hotmail.com">Jorge Armando Osorio Velásquez</a>
 * 
 * Copyright Princeton S.A. &copy;&reg; 2005. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 *
 */
package com.princetonsa.actionform.cartera;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;

/**
 * @version 1.0, 3/11/2005
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velásquez/a>
 */
public class AplicacionPagosEmpresaForm extends ValidatorForm
{


    /**
     * Variable para manejar el estado de la aplicacion.
     */
    private String estado;
    
    /**
     * Mapa que contiene todos los documentos de pagos general empresa 
     */
    private HashMap documentosPagos;
    
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
     * codigo del pago que se modificara.
     */
    private int indexPago;
    
    /**
     * Tipo del documento por el que se hace la busqueda(Recibos Caja - RC )
     */
    private int tipoDocBusqueda;
    /**
     * numero del documento por el que se hace la busquenda.
     */
    private String documentoBusqueda;
    /**
     * fecha del documento para hacer la busqueda.
     */
    private String fechaDocBusqueda;
    /**
     * convenio para busqueda.
     */
    private int convenioBusqueda;
    
    /**
     * Institucion
     */
    private int institucion;
    
    /**
     * Mapa para manejar los conceptos de las aplicaciones de los pagos.
     */
    private HashMap conceptosAplicacionPagos;
    
    /**
     * Mapa para manejar las aplicaciones de pagos del grupo de cuentas de cobro
     */
    private HashMap pagosCXC;
    
    /**
     * Variable para manejar las observaciones de los conceptos
     */
    private String observaciones;
    
    /**
     * Fecha de la aplicacion del pago
     */
    private String fechaApliacion;
    
    /**
     * Posicion del concepto que se eliminar&aacute;
     */
    private int conEliminar;
    
    /**
     * Motivo por el que se anula una aplicacion
     */
    private String motivoAnulacion;
    
    /**
     * Boolean que me indica si la distribucino se realiza por facturas
     */
    private boolean porFacturas;
    
    /**
     * Boolean que me indica si la distribucino se realiza por Cuenta de Cobro
     */
    private boolean porCXC;
    
    /**
     * Mapa que contiene las cuentas de cobro para la busqueda de las cuentas de cobro
     */
    private HashMap mapaBusCXC;
    
    
    /**
     * Mapa que contiene las cuentas de cobro para la busqueda de las cuentas de cobro
     */
    private HashMap mapaBusFacturas;
    
    /**
     * codigo de la cuenta de cobro
     */
    private String cxcBusAvanzada;
    
    /**
     * Consecutivo de la factura para la busqueda avanzada
     */
    private String facturaBusquedaAvanzada;
    
    /**
     * Variable que almacena el indice del mapa.
     */
    private int indexCXC;
    
    /**
     * Mapa para manejar las facturas de unc CXC
     */
    private HashMap mapaFacturasCXC;
    
    /**
     * Mapa para manejar los pagos a nivel de facturas.
     */
    private HashMap pagosFactura; 
    
    /**
     * Boolean que me indica si la busqueda de nivel de facturas se está realizando
     * en el nivel 3(conceptos->cxc->facturas) o en facturas directamente(conceptos->facturas) 
     */
    private boolean busquedaNivelFacturas3;
    
    private boolean guardadoEncabezado;
    
    /**
     * para controlar la página actual
     * del pager.
     */
    private int offset;
    
    /**
     * el numero de registros por pager
     */
    private int maxPageItems;
    
    /**
     * 
     */
    private String linkSiguiente;
    
    /**
     * Metodo que inicializa todas las variables.
     */
    public void reset()
    {
        this.documentosPagos=new HashMap();
        this.documentosPagos.put("numRegistros", "0");
        this.patronOrdenar = "";
        this.ultimoPatron = "";
        this.indexPago=ConstantesBD.codigoNuncaValido;
        this.tipoDocBusqueda=ConstantesBD.codigoNuncaValido;
        this.documentoBusqueda="";
        this.fechaDocBusqueda="";
        this.convenioBusqueda=ConstantesBD.codigoNuncaValido;
        this.institucion=ConstantesBD.codigoNuncaValido;
        this.conceptosAplicacionPagos=new HashMap();
        this.conceptosAplicacionPagos.put("numRegistros", "0");
        this.observaciones="";
        this.fechaApliacion=UtilidadFecha.getFechaActual();
        this.conEliminar=ConstantesBD.codigoNuncaValido;
        this.motivoAnulacion="";
        this.porFacturas=false;
        this.porCXC=false;
        this.pagosCXC=new HashMap();
        this.pagosCXC.put("numRegistros", "0");
        this.mapaBusCXC=new HashMap();
        this.mapaBusCXC.put("numRegistros", "0");
        this.cxcBusAvanzada="";
        this.indexCXC=ConstantesBD.codigoNuncaValido;
        this.pagosFactura=new HashMap();
        this.pagosFactura.put("numRegistros", "0");
        this.mapaBusFacturas=new HashMap();
        this.mapaBusFacturas.put("numRegistros", "0");
        this.facturaBusquedaAvanzada="";
        this.busquedaNivelFacturas3=false;
        this.guardadoEncabezado=false;
        this.maxPageItems=20;
        this.linkSiguiente="";
    }
    
    
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if(estado.equals("guardarAplicacionConceptos"))
		{
			if((this.fechaApliacion.trim()).equals(""))
			{
				errores.add("Fecha Requerido",new ActionMessage("errors.required","La fecha de la aplicacion"));
			}
			else
			{
				if(!UtilidadFecha.validarFecha(this.fechaApliacion))
				{
					errores.add("fecha", new ActionMessage("errors.formatoFechaInvalido",this.fechaApliacion));
				}
				else
				{
					//validaciones con la fecha
				}
			}
			
		}
		if(estado.equals("anularAplicacion"))
		{
		    if((motivoAnulacion.trim()).equals(""))
		    {
		        errores.add("Concepto Castigo Cartera",new ActionMessage("errors.required","El Motivo de la Anulación."));
		    }
		}
		return errores;
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
     * @return Returns the documentosPagos.
     */
    public HashMap getDocumentosPagos()
    {
        return documentosPagos;
    }
    /**
     * @param documentosPagos The documentosPagos to set.
     */
    public void setDocumentosPagos(HashMap documentosPagos)
    {
        this.documentosPagos = documentosPagos;
    }
    /**
     * @return Returns the documentosPagos.
     */
    public Object getDocumentosPagos(String key)
    {
        return documentosPagos.get(key);
    }
    /**
     * @param documentosPagos The documentosPagos to set.
     */
    public void setDocumentosPagos(String key,Object value)
    {
        this.documentosPagos.put(key, value);
    }    
    /**
     * @return Returns the codigoPago.
     */
    public int getIndexPago()
    {
        return indexPago;
    }
    /**
     * @param codigoPago The codigoPago to set.
     */
    public void setIndexPago(int codigoPago)
    {
        this.indexPago = codigoPago;
    }
    /**
     * @return Returns the convenioBusqueda.
     */
    public int getConvenioBusqueda()
    {
        return convenioBusqueda;
    }
    /**
     * @param convenioBusqueda The convenioBusqueda to set.
     */
    public void setConvenioBusqueda(int convenioBusqueda)
    {
        this.convenioBusqueda = convenioBusqueda;
    }
    /**
     * @return Returns the documentoBusqueda.
     */
    public String getDocumentoBusqueda()
    {
        return documentoBusqueda;
    }
    /**
     * @param documentoBusqueda The documentoBusqueda to set.
     */
    public void setDocumentoBusqueda(String documentoBusqueda)
    {
        this.documentoBusqueda = documentoBusqueda;
    }
    /**
     * @return Returns the fechaDocBusqueda.
     */
    public String getFechaDocBusqueda()
    {
        return fechaDocBusqueda;
    }
    /**
     * @param fechaDocBusqueda The fechaDocBusqueda to set.
     */
    public void setFechaDocBusqueda(String fechaDocBusqueda)
    {
        this.fechaDocBusqueda = fechaDocBusqueda;
    }
    /**
     * @return Returns the tipoDocBusqueda.
     */
    public int getTipoDocBusqueda()
    {
        return tipoDocBusqueda;
    }
    /**
     * @param tipoDocBusqueda The tipoDocBusqueda to set.
     */
    public void setTipoDocBusqueda(int tipoDocBusqueda)
    {
        this.tipoDocBusqueda = tipoDocBusqueda;
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
     * @return Returns the conceptosAplicacionPagos.
     */
    public HashMap getConceptosAplicacionPagos()
    {
        return conceptosAplicacionPagos;
    }
    /**
     * @param conceptosAplicacionPagos The conceptosAplicacionPagos to set.
     */
    public void setConceptosAplicacionPagos(HashMap conceptosAplicacionPagos)
    {
        this.conceptosAplicacionPagos = conceptosAplicacionPagos;
    }
    /**
     * @return Returns the conceptosAplicacionPagos.
     */
    public Object getConceptosAplicacionPagos(String key)
    {
        return conceptosAplicacionPagos.get(key);
    }
    /**
     * @param conceptosAplicacionPagos The conceptosAplicacionPagos to set.
     */
    public void setConceptosAplicacionPagos(String key,Object value)
    {
        this.conceptosAplicacionPagos.put(key, value);
    }
    /**
     * @return Returns the observacionesConceptos.
     */
    public String getObservaciones()
    {
        return observaciones;
    }
    /**
     * @param observacionesConceptos The observacionesConceptos to set.
     */
    public void setObservaciones(String observacionesConceptos)
    {
        this.observaciones = observacionesConceptos;
    }
    /**
     * @return Returns the fechaApliacion.
     */
    public String getFechaApliacion()
    {
        return fechaApliacion;
    }
    /**
     * @param fechaApliacion The fechaApliacion to set.
     */
    public void setFechaApliacion(String fechaApliacion)
    {
        this.fechaApliacion = fechaApliacion;
    }
    /**
     * @return Returns the conEliminar.
     */
    public int getConEliminar()
    {
        return conEliminar;
    }
    /**
     * @param conEliminar The conEliminar to set.
     */
    public void setConEliminar(int conEliminar)
    {
        this.conEliminar = conEliminar;
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
     * @return Returns the porCXC.
     */
    public boolean isPorCXC()
    {
        return porCXC;
    }
    /**
     * @param porCXC The porCXC to set.
     */
    public void setPorCXC(boolean porCXC)
    {
        this.porCXC = porCXC;
    }
    /**
     * @return Returns the porFacturas.
     */
    public boolean isPorFacturas()
    {
        return porFacturas;
    }
    /**
     * @param porFacturas The porFacturas to set.
     */
    public void setPorFacturas(boolean porFacturas)
    {
        this.porFacturas = porFacturas;
    }
    /**
     * @return Returns the pagosCXC.
     */
    public HashMap getPagosCXC()
    {
        return pagosCXC;
    }
    /**
     * @param pagosCXC The pagosCXC to set.
     */
    public void setPagosCXC(HashMap pagosCXC)
    {
        this.pagosCXC = pagosCXC;
    }
    /**
     * @return Returns the pagosCXC.
     */
    public Object getPagosCXC(String key)
    {
        return pagosCXC.get(key);
    }
    /**
     * @param pagosCXC The pagosCXC to set.
     */
    public void setPagosCXC(String key,Object values)
    {
        this.pagosCXC.put(key, values);
    }    
    /**
     * @return Returns the mapaBusCXC.
     */
    public HashMap getMapaBusCXC()
    {
        return mapaBusCXC;
    }
    /**
     * @param mapaBusCXC The mapaBusCXC to set.
     */
    public void setMapaBusCXC(HashMap mapaBusCXC)
    {
        this.mapaBusCXC = mapaBusCXC;
    }
    /**
     * @return Returns the mapaBusCXC.
     */
    public Object getMapaBusCXC(String key)
    {
        return mapaBusCXC.get(key);
    }
    /**
     * @param mapaBusCXC The mapaBusCXC to set.
     */
    public void setMapaBusCXC(String key,Object value)
    {
        this.mapaBusCXC.put(key,value);
    }
    /**
     * @return Returns the cxcBusAvanzada.
     */
    public String getCxcBusAvanzada()
    {
        return cxcBusAvanzada;
    }
    /**
     * @param cxcBusAvanzada The cxcBusAvanzada to set.
     */
    public void setCxcBusAvanzada(String cxcBusAvanzada)
    {
        this.cxcBusAvanzada = cxcBusAvanzada;
    }
    /**
     * @return Returns the indexCXC.
     */
    public int getIndexCXC()
    {
        return indexCXC;
    }
    /**
     * @param indexCXC The indexCXC to set.
     */
    public void setIndexCXC(int indexCXC)
    {
        this.indexCXC = indexCXC;
    }
    /**
     * @return Returns the mapaFacturasCXC.
     */
    public HashMap getMapaFacturasCXC()
    {
        return mapaFacturasCXC;
    }
    /**
     * @param mapaFacturasCXC The mapaFacturasCXC to set.
     */
    public void setMapaFacturasCXC(HashMap mapaFacturasCXC)
    {
        this.mapaFacturasCXC = mapaFacturasCXC;
    }
    /**
     * @return Returns the mapaFacturasCXC.
     */
    public Object getMapaFacturasCXC(String key)
    {
        return mapaFacturasCXC.get(key);
    }
    /**
     * @param mapaFacturasCXC The mapaFacturasCXC to set.
     */
    public void setMapaFacturasCXC(String key,Object value)
    {
        this.mapaFacturasCXC.put(key,value);
    }    
    /**
     * @return Returns the pagosFactura.
     */
    public HashMap getPagosFactura()
    {
        return pagosFactura;
    }
    /**
     * @param pagosFactura The pagosFactura to set.
     */
    public void setPagosFactura(HashMap pagosFactura)
    {
        this.pagosFactura = pagosFactura;
    }
    /**
     * @return Returns the pagosFactura.
     */
    public Object getPagosFactura(String key)
    {
        return pagosFactura.get(key);
    }
    /**
     * @param pagosFactura The pagosFactura to set.
     */
    public void setPagosFactura(String key, Object value)
    {
        this.pagosFactura.put(key, value);
    }    
    /**
     * @return Returns the mapaBusFacturas.
     */
    public HashMap getMapaBusFacturas()
    {
        return mapaBusFacturas;
    }
    /**
     * @param mapaBusFacturas The mapaBusFacturas to set.
     */
    public void setMapaBusFacturas(HashMap mapaBusFacturas)
    {
        this.mapaBusFacturas = mapaBusFacturas;
    }
    /**
     * @return Returns the mapaBusFacturas.
     */
    public Object getMapaBusFacturas(String key)
    {
        return mapaBusFacturas.get(key);
    }
    /**
     * @param mapaBusFacturas The mapaBusFacturas to set.
     */
    public void setMapaBusFacturas(String key,Object value)
    {
        this.mapaBusFacturas.put(key, value);
    }    
    /**
     * @return Returns the facturaBusquedaAvanzada.
     */
    public String getFacturaBusquedaAvanzada()
    {
        return facturaBusquedaAvanzada;
    }
    /**
     * @param facturaBusquedaAvanzada The facturaBusquedaAvanzada to set.
     */
    public void setFacturaBusquedaAvanzada(String facturaBusquedaAvanzada)
    {
        this.facturaBusquedaAvanzada = facturaBusquedaAvanzada;
    }
    /**
     * @return Returns the busquedaNivelFacturas3.
     */
    public boolean isBusquedaNivelFacturas3()
    {
        return busquedaNivelFacturas3;
    }
    /**
     * @param busquedaNivelFacturas3 The busquedaNivelFacturas3 to set.
     */
    public void setBusquedaNivelFacturas3(boolean busquedaNivelFacturas3)
    {
        this.busquedaNivelFacturas3 = busquedaNivelFacturas3;
    }
    /**
     * @return Returns the guardadoEncabezado.
     */
    public boolean isGuardadoEncabezado()
    {
        return guardadoEncabezado;
    }
    /**
     * @param guardadoEncabezado The guardadoEncabezado to set.
     */
    public void setGuardadoEncabezado(boolean guardadoEncabezado)
    {
        this.guardadoEncabezado = guardadoEncabezado;
    }


	/**
	 * @return the offset
	 */
	public int getOffset() {
		return offset;
	}


	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset) {
		this.offset = offset;
	}


	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems() {
		return maxPageItems;
	}


	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}


	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente() {
		return linkSiguiente;
	}


	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}
}
