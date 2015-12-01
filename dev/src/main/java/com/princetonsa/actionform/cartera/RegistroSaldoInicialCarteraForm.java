
/*
 * Creado   10/06/2005
 *
 *Copyright Princeton S.A. &copy;&reg; 2004. Todos los Derechos Reservados.
 *
 * Lenguaje		:Java
 * Compilador	:J2SDK 1.4.2_04
 */
package com.princetonsa.actionform.cartera;

import java.util.ArrayList;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadValidacion;
import util.ValoresPorDefecto;

/**
 * Clase para manejar los atributos propios de 
 * esta clase, con sus respectivos get and set
 * para la comun icación con el formulario y con
 * el Action.
 * Implementa el validate de la clase.
 * Extiende de la superclase  CuentasCobroForm.
 *
 * @version 1.0, 10/06/2005
 * @author <a href="mailto:joan@PrincetonSA.com">Joan L&oacute;pez</a>
 */
public class RegistroSaldoInicialCarteraForm extends CuentasCobroForm 
{
    /**
     * para almacenar el número de la cuenta de cobro
     */
    private String numCuentaCobroStr;
    
    /**
     * posición del registro para ser eliminado
     */
    private int posRegistro;
    /**
     * contador de números de facturas
     * externas
     */
    private int numFactExternas;
    
    /**
     * 
     */
    private double valorTotalCartera;
    
    
    private boolean facturasSeleccionadasExternas;
    /**
     * 
     */
    private double valorTotalAjustes;
    
    /**
     * almacena los código de las vias de ingreso
     * de las faxturas externas
     */
    private ArrayList viasIngresoInsertadas = new ArrayList ();
    
    /**
     * contador de el numero de elementos 
     * del ArrayList viasIngresoInsertadas
     */
    private int posArray=0;
    
    /**
     *	almacena el código de la institución 
     */
    private int institucion=-1;
    
    private double saldoTotalCuenta;
    
    /**
     * 
     */
    private Collection  detalleFacturas;
    
    
    /**
     * Campo que se utiliza para verificar si la impresión será con Anexos
     */
    private boolean impresionAnexos;

    
    /**
	 * inicializa los datos del formulario
	 */
    public void resetRegistro()
    {
    	this.facturasSeleccionadasExternas=false;
    	this.numCuentaCobroStr = "";
    	this.numFactExternas = 0;
    	this.valorTotalCartera=0;
        this.valorTotalAjustes=0;
        this.posRegistro = -1;
        this.viasIngresoInsertadas = new ArrayList ();
        this.posArray = 0;
        this.saldoTotalCuenta=0;
        //this.institucion = -1;
        //reseteando los valores de la clase madre
        super.reset();
        this.detalleFacturas=new ArrayList();
        this.impresionAnexos = false;

    }
    
    /**
	 * Función de validación
	 * @param mapping
	 * @param request
	 * @return ActionError que especifica el error
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
	    ActionErrors errores = new ActionErrors();		
		boolean fechaVacia=false,errorFecha=false,errorFechaFormato=false,cuentaCobroVacia=false;
		errores.add(super.validate(mapping,request));
		if(super.getEstado().equals("continuarGeneracion") || super.getEstado().equals("continuarModificacion"))
		{
			if(super.getEstado().equals("continuarGeneracion"))
			{
			    if(super.getConvenio().equals("null")||super.getConvenio()==null||super.getConvenio().equals("")||super.getConvenio().equals("-1"))
				{
					super.setCodigoConvenio(ConstantesBD.codigoNuncaValido);
				}
				else
				{
					super.setCodigoConvenio(Integer.parseInt((super.getConvenio().split(ConstantesBD.separadorSplit))[0]));
				}	
			}
			else if (super.getEstado().equals("continuarModificacion"))
			{			    
			    if(super.getPropiedadTempI()==-1)
			    {	
			        errores.add("convenio requerido", new ActionMessage("errors.required", "El Convenio de la Cuenta de Cobro"));   
			    }
			    else
			        super.setCodigoConvenio(super.getPropiedadTempI());
			}
			if(UtilidadValidacion.existeCierreSaldosIniciales(this.institucion))
			{
				errores.add("tiene cierre", new ActionMessage("error.registroSaldosInciciales.yaTieneCierreSaldos"));
			}
			else
			{
				//si no se ha realizado proceso de cierre de saldos iniciales, se puede continuar con la validaciones.
			    if(this.getNumCuentaCobroStr().equals(""))
				{
					errores.add("Número de Cuenta de Cobro", new ActionMessage("errors.required", "El número de la Cuenta de Cobro"));
					cuentaCobroVacia=true;
				}
				if(!cuentaCobroVacia)
				{				
					if( Integer.parseInt(this.getNumCuentaCobroStr()) > Integer.parseInt(ValoresPorDefecto.getTopeConsecutivoCxCSaldoI(this.getInstitucion())) )
					{
						errores.add("Número de Cuenta de Cobro Mayor al Tope Consecutivo", new ActionMessage("error.registroSaldosInciciales.cuentaCobroMayorAlTope"));
					}
				}
				if(super.getFechaElaboracion().equals(""))
				{
					errores.add("fecha Elaboración requerida", new ActionMessage("errors.required", "La fecha Elaboración"));	
					fechaVacia=true;
				}
				if(!UtilidadFecha.compararFechas(super.getFechaElaboracion(), "00:00", super.getFechaInicial(), "00:00").isTrue())
				{
				    errores.add("fecha de Inicial mayor a fecha elaboracion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Inicial", "Elaboracion"));  
				}
				if(!UtilidadFecha.compararFechas(super.getFechaElaboracion(), "00:00", super.getFechaFinal(), "00:00").isTrue())
				{
				    errores.add("fecha de Final mayor a fecha Elaboracion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Final", "Elaboracion")); 
				}
				if(!fechaVacia)
				{
				    if(!UtilidadFecha.validarFecha(super.getFechaElaboracion()))
					{
						errores.add("Formato fecha Inicial", new ActionMessage("errors.formatoFechaInvalido",super.getFechaElaboracion()));
						errorFechaFormato=true;
					}
				}
				if(!errorFechaFormato)
				{
				    if((UtilidadFecha.conversionFormatoFechaABD(super.getFechaElaboracion())).compareTo(UtilidadFecha.conversionFormatoFechaABD(UtilidadFecha.getFechaActual()))>0)
					{
						errores.add("fecha Elaboración mayor Actual", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Elaboración", "Actual"));
						errorFecha = true;
					}				    
				}				
				if(!errorFecha)
				{				    
				    int index = super.getFechaElaboracion().indexOf("/");
				    String fechaE = "";
					if(index!=-1)
					{
					    fechaE =  super.getFechaElaboracion().substring(index+1);					    
					}			    
				    if(!UtilidadFecha.compararFechas("01/"+ValoresPorDefecto.getFechaCorteSaldoInicialC(this.getInstitucion()), "00:00","01/"+fechaE, "00:00").isTrue())
				    {
				        errores.add("fecha Elaboracion", new ActionMessage("errors.fechaPosteriorAOtraDeReferencia", "Elaboracion", "corte saldos iniciales"));
				    }
				}			
			}		
		}	
		if(super.getEstado().equals("generar") || super.getEstado().equals("modificarSaldoInicial"))
		{
		    fechaVacia=false;errorFecha=false;errorFechaFormato=false;		
			
			for(int k = 0; k < Integer.parseInt(super.getFacturaCxC("numFacturas")+"");k++)
			{
			    if((super.getFacturaCxC("tipo_"+k)+"").equals("MEM"))
				{
			        if(super.getFacturaCxC("consecutivo_factura_"+k).equals(""))
					{
						errores.add("codigo Factura Externa", new ActionMessage("errors.required", "El Código de la Factura Externa "+super.getFacturaCxC("consecutivo_factura_"+k)));
					}
			        else
			        {
				    	//validar que el consecutivo_factura no exista ya en otra factura
				    	for(int j=0;j<k;j++)
				    	{
				    		if(super.getFacturaCxC("consecutivo_factura_"+k).equals(super.getFacturaCxC("consecutivo_factura_"+j)+""))
				    		{
				    			errores.add("YA EXISTE EL REGISTRO.", new ActionMessage("errors.yaExiste","El Código de la Factura Externa "+super.getFacturaCxC("consecutivo_factura_"+k)));    
				    		}
				    	}
			        }
			        if(super.getFacturaCxC("via_ingreso_"+k).equals("-1"))
					{
						errores.add("via ingreso Factura Externa", new ActionMessage("errors.required", "La Via de Ingreso de la Factura Externa "+super.getFacturaCxC("consecutivo_factura_"+k)));
					}				        
			        if((super.getFacturaCxC("valor_cartera_"+k)+"").equals(""))
					{
						errores.add("valor cartera Factura Externa", new ActionMessage("errors.required", "El Valor Cartera de la Factura Externa "+super.getFacturaCxC("consecutivo_factura_"+k)));							
					}
			        if(super.getFacturaCxC("fecha_"+k).equals(""))
					{
						errores.add("fecha Factura Externa", new ActionMessage("errors.required", "La Fecha de la Factura Externa "+super.getFacturaCxC("consecutivo_factura_"+k)));
						fechaVacia=true;
					}
				    if(!fechaVacia)
					{
					    if(!UtilidadFecha.validarFecha((super.getFacturaCxC("fecha_"+k)+"")))
						{
							errores.add("fecha factura externa", new ActionMessage("errors.formatoFechaInvalido",(super.getFacturaCxC("fecha_"+k)+"")));
							errorFechaFormato=true;
						}
					}
				    if(!fechaVacia && !errorFechaFormato)
				    {
					    
				    	if(((UtilidadFecha.conversionFormatoFechaABD((super.getFacturaCxC("fecha_"+k)+""))).compareTo(UtilidadFecha.conversionFormatoFechaABD(super.getFechaInicial()))<0)||((UtilidadFecha.conversionFormatoFechaABD((super.getFacturaCxC("fecha_"+k)+""))).compareTo(UtilidadFecha.conversionFormatoFechaABD(super.getFechaFinal()))>0))
						{
				    		errores.add("fecha Factura Externa", new ActionMessage("errors.fechaEntreRangos", "Factura Externa "+super.getFacturaCxC("consecutivo_factura_"+k), "Fecha Inicial ("+super.getFechaInicial()+")","Fecha Final ("+super.getFechaFinal()+")"));
						}				    	
				    }
				 }
			    if((Double.parseDouble(super.getFacturaCxC("valor_cartera_"+k)+"")-Double.parseDouble(super.getFacturaCxC("valor_ajustes_"+k)+""))<0)
			    {
			    	errores.add("Valor cartera menor que 0", new ActionMessage("errors.MayorIgualQue","El saldo de la factura "+super.getFacturaCxC("consecutivo_factura_"+k),"0"));
			    }
			}			
		}
		
		return errores;
	}
	
    /**
     * @return Retorna numCuentaCobroStr.
     */
    public String getNumCuentaCobroStr() {
        return numCuentaCobroStr;
    }
    /**
     * @param numCuentaCobroStr Asigna numCuentaCobroStr.
     */
    public void setNumCuentaCobroStr(String numCuentaCobroStr) {
        this.numCuentaCobroStr = numCuentaCobroStr;
        if(!this.numCuentaCobroStr.trim().equals(""))
            super.setNumCuentaCobro(Double.parseDouble(this.numCuentaCobroStr));
        else 
            super.setNumCuentaCobro(0);
    }
    /**
     * @return Retorna numFactExternas.
     */
    public int getNumFactExternas() {
        return numFactExternas;
    }
    /**
     * @param numFactExternas Asigna numFactExternas.
     */
    public void setNumFactExternas(int numFactExternas) {
        this.numFactExternas = numFactExternas;
    }
	/**
	 * @return Returns the valorTotalAjustes.
	 */
	public double getValorTotalAjustes() {
		return valorTotalAjustes;
	}
	/**
	 * @param valorTotalAjustes The valorTotalAjustes to set.
	 */
	public void setValorTotalAjustes(double valorTotalAjustes) {
		this.valorTotalAjustes = valorTotalAjustes;
	}
	/**
	 * @return Returns the valorTotalCartera.
	 */
	public double getValorTotalCartera() {
		return valorTotalCartera;
	}
	/**
	 * @param valorTotalCartera The valorTotalCartera to set.
	 */
	public void setValorTotalCartera(double valorTotalCartera) {
		this.valorTotalCartera = valorTotalCartera;
	}
    /**
     * @return Retorna posRegistro.
     */
    public int getPosRegistro() {
        return posRegistro;
    }
    /**
     * @param posRegistro Asigna posRegistro.
     */
    public void setPosRegistro(int posRegistro) {
        this.posRegistro = posRegistro;
    }
    /**
     * @return Retorna posArray.
     */
    public int getPosArray() {
        return posArray;
    }
    /**
     * @param posArray Asigna posArray.
     */
    public void setPosArray(int posArray) {
        this.posArray = posArray;
    }
    /**
     * @return Retorna viasIngresoInsertadas.
     */
    public ArrayList getViasIngresoInsertadas() {
        return viasIngresoInsertadas;
    }
    /**
     * @param viasIngresoInsertadas Asigna viasIngresoInsertadas.
     */
    public void setViasIngresoInsertadas(ArrayList viasIngresoInsertadas) {
        this.viasIngresoInsertadas = viasIngresoInsertadas;
    }
    /**
     * @param index int.
     * @return Object.
     */
    public Object getViasIngresoInsertadas(int index) {
        return viasIngresoInsertadas.get(index);
    }
    /**
     * @param index int.
     * @param value Object.
     */
    public void setViasIngresoInsertadas(int index,Object value) {
        this.viasIngresoInsertadas.add(index,value);
    }
    /**
     * @return Retorna institucion.
     */
    public int getInstitucion() {
        return institucion;
    }
    /**
     * @param institucion Asigna institucion.
     */
    public void setInstitucion(int institucion) {
        this.institucion = institucion;
    }
	/**
	 * @return Returns the saldoCuenta.
	 */
	public double getSaldoTotalCuenta() {
		return saldoTotalCuenta;
	}
	/**
	 * @param saldoCuenta The saldoCuenta to set.
	 */
	public void setSaldoTotalCuenta(double saldoCuenta) {
		this.saldoTotalCuenta = saldoCuenta;
	}

	/**
	 * @return Returns the facturasSeleccionadasExternas.
	 */
	public boolean isFacturasSeleccionadasExternas() {
		return facturasSeleccionadasExternas;
	}
	/**
	 * @param facturasSeleccionadasExternas The facturasSeleccionadasExternas to set.
	 */
	public void setFacturasSeleccionadasExternas(
			boolean facturasSeleccionadasExternas) {
		this.facturasSeleccionadasExternas = facturasSeleccionadasExternas;
	}

	/**
	 * @return the detalleFacturas
	 */
	public Collection getDetalleFacturas() {
		return detalleFacturas;
	}

	/**
	 * @param detalleFacturas the detalleFacturas to set
	 */
	public void setDetalleFacturas(Collection detalleFacturas) {
		this.detalleFacturas = detalleFacturas;
	}

	public boolean isImpresionAnexos() {
		return impresionAnexos;
	}

	public void setImpresionAnexos(boolean impresionAnexos) {
		this.impresionAnexos = impresionAnexos;
	}
}
