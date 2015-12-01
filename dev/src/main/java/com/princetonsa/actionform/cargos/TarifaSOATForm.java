/*
 * @(#)TarifaSOATForm.java
 * 
 * Created on 04-May-2004
 *
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados
 * 
 * Lenguaje : Java
 * Compilador : J2SDK 1.4
 */
package com.princetonsa.actionform.cargos;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadTexto;

/**
 * ActionForm, tiene la función de bean dentro de la forma, que contiene todos
 * los datos generales de una tarifa soat. Y adicionalmente hace el manejo de reset 
 * de la forma y de validación de errores de datos. 
 * 
 * @version 1.0, 04-May-2004
 * @author <a href="mailto:Liliana@PrincetonSA.com">Liliana Caballero</a>
 */
public class TarifaSOATForm extends ValidatorForm
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Para hacer logs de debug / warn / error de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(TarifaSOATForm.class);
	/**
	 * Enlace al detalle de la tarifa SOAT
	 */
	private String enlace;
	
	/**
	 * Estado dentro del flujo
	 */
	private String estado;
		
	/**
	 * Codigo de la tarifa SOAT
	 */
	private int codigo;
	
	/**
	 * Grupo de la tarifa SOAT
	 */
	private double grupo;
	
	/**
	 * Grupo de la tarifa SOAT
	 */
	private String grupoString;

	/**
	 * Valor de la tarifa
	 */	
	private double valorTarifa;
	
	/**
	 * Valor Base
	 */
	private double valorBase;
	
	
	/**
	 * Iva que se debe aplicar a la tarifa
	 */
	private double porcentajeIva;
	
	/**
	 * Código del esquema tarifario de la tarifa
	 */
	private int codigoEsquemaTarifario;
	
	/**
	 * Nombre del esquema tarifario de la tarifa
	 */
	private String nombreEsquemaTarifario;
	
	/**
	 * Código del tipo liquidacion de la tarifa
	 */
	private int codigoTipoLiquidacion;
	
	/**
	 * Nombre del tipo liquidacion de la tarifa
	 */
	private String nombreTipoLiquidacion;
	
	/**
	 * Código del servicio asociado a la tarifa 
	 */
	private int codigoServicio;

	/**
	 * Nombre del servicio asociado a la tarifa 
	 */
	private String nombreServicio;
	
	/**
	 * Código de la especialidad del servicio que corresponde a esta tarifa
	 */
	private int codigoEspecialidad;
	
	/**
	 * Nombre de la especialidad del servicio que corresponde a esta tarifa
	 */
	private String nombreEspecialidad;	
	
	/**
	 * Para saber cual de las opciones de la tarifa está seleccionada
	 * 1 para grupo, 2 para valor
	 */
	private int radioTarifa;
	
	/**
	 * Salario mínimo mensual vigente para calcular el valor de la tarifa cuando es por unidades
	 */
	private double salarioMinimoMensualVigente;
	
	/**
	 * Indica si se va a modificar o a insertar la tarifa
	 */
	private boolean modificar;
	
	/**
	 * Maneja las modificaciones de las tarifas
	 */
	private String tarifasSOATAnteriores;
	
	/**
	 * Al guardar el registro debe quedar como modificado
	 */
	private boolean modificado;
	
	/**
	 * Código del elemento eliminado
	 */
	private int eliminado;
	
	/**
	 * Acrónimo del método de ajueste del esquema tarifario
	 */
	private String acronimoMetodoAjuste;

	/**
	 * 
	 */
	private double valorTarifaConversionMoneda;
	
	/**
	 * 
	 */
	private int index;
	
	
	/**
	 * String liquidar Asocios, define cuales son lso servicios que deben liquidarse
	 * */
	private String liquidarAsocios;
	
	/**
	 * Variable alterna para manejar el valor de la tarifa
	 * como String para mostrarla en forma exponencial
	 */
	private String valorTarifaString;
	
	/**
	 * Variable para manejar la Fecha de Vigencia de la Tarifa
	 */
	private String fechaVigencia;
	
	/**
	 * Variable para almacenar todas las tarifas por esquema y servicio
	 */
	private HashMap tarifasMap;
	
	/**
	 * Variable para manejar el codigo de la tarifa que se va a modificar
	 */
	private String codigoT;
	
	/**
	 * Variable para modificacion insercion
	 */
	private String modifInser;
	
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found.  If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 *
	 * @param mapping The mapping used to select this instance
	 * @param request The servlet request we are processing
	*/
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();
		
		logger.info("\n\n\n ESTADO-->"+estado+" TIPO LIQUIDACION->"+this.codigoTipoLiquidacion+"\n\n");
		
		if(estado.equalsIgnoreCase("guardar") )
		{
			if(!this.modificado)
			{
			
				if(acronimoMetodoAjuste.equals(ConstantesBD.metodoAjusteUnidad))
				{
					valorTarifa = Math.round(valorTarifa);
				}
				else if(acronimoMetodoAjuste.equals(ConstantesBD.metodoAjusteDecena))
				{
					valorTarifa = UtilidadCadena.redondearALaDecenaLong((long)valorTarifa);
				}
				else if(acronimoMetodoAjuste.equals(ConstantesBD.metodoAjusteCentena))
				{
					valorTarifa = UtilidadCadena.redondearALaCentenaLong((long)valorTarifa);
				}
				
				
				if(this.valorTarifa <0.0 &&this.codigoTipoLiquidacion != ConstantesBD.codigoTipoLiquidacionSoatGrupo && !UtilidadTexto.getBoolean(this.liquidarAsocios))
					errores.add("valor negativo", new ActionMessage("errors.floatMayorOIgualQue", "La tarifa por valor", "0"));
				
				if(this.grupo <= 0.0 && this.codigoTipoLiquidacion == ConstantesBD.codigoTipoLiquidacionSoatGrupo)
					errores.add("valor negativo", new ActionMessage("errors.floatMayorQue", "El Grupo", "0"));
				
				if( this.codigoTipoLiquidacion <=0 )
				{
					errores.add("tipo liquidación requerido", new ActionMessage("errors.required", "El tipo de liquidación"));
				}
				
			}
		}
		else if( estado.equalsIgnoreCase("finalizar")  || estado.equalsIgnoreCase("finalizarmodificar") )
		{
			if( this.codigoEsquemaTarifario <= 0 )
			{
				errores.add("esquema tarifario requerido", new ActionMessage("errors.required", "El esquema tarifario"));
			}
			
			if( this.codigoServicio <= 0 )
			{
				errores.add("servicio requerido", new ActionMessage("errors.required", "El servicio"));
			}
			
			if( this.radioTarifa == 0 ) // No seleccionó ningún tipo de tarifa
			{
				errores.add("grupo o valor tarifa requerido", new ActionMessage("errors.required", "La tarifa (grupo o valor)"));
			}
				
			
			try
			{
			    if(!this.grupoString.equals(""))
			        this.grupo=Integer.parseInt(this.grupoString);
				if( this.grupo <= 0.0 && this.radioTarifa == 1 )
				{
					errores.add("grupo negativo", new ActionMessage("errors.integerMayorQue", "La tarifa por grupo", "0"));
				}
			}
			catch(NumberFormatException e)
			{
				errores.add("grupo formato invalido", new ActionMessage("errors.integer", "La tarifa por grupo"));
			}
			
			if( this.valorTarifa < 0.0 && this.radioTarifa == 2 )
			{
				errores.add("valor negativo", new ActionMessage("errors.floatMayorOIgualQue", "La tarifa por valor", "0"));
			}
			
			if( this.radioTarifa == 2 && this.codigoTipoLiquidacion == -1 )
			{
				errores.add("tipo liquidación requerido", new ActionMessage("errors.required", "El tipo de liquidación"));
			}
		}
		
		return errores;
	}	
	
	/**
	 * Método que inicializa todos los atributos de la forma
	 */
	public void reset() 
	{
		this.codigo = -1;
		this.grupo = 0;
		this.valorTarifa = 0.0;
		this.porcentajeIva = 0.0;
		this.codigoEsquemaTarifario = -1;
		this.nombreEsquemaTarifario = "";
		this.codigoServicio = -1;
		this.nombreServicio = "";
		this.codigoTipoLiquidacion = -1;
		this.nombreTipoLiquidacion = "";		
		this.codigoEspecialidad = -1;
		this.nombreEspecialidad = "";
		this.radioTarifa = 0;
		this.salarioMinimoMensualVigente = 0.0;
		this.grupoString="0";
		modificar=false;
		modificado=false;
		tarifasSOATAnteriores="";
		eliminado=0;
		this.valorBase = 0;
		this.acronimoMetodoAjuste = "";
		this.index=ConstantesBD.codigoNuncaValido;
		this.valorTarifaConversionMoneda=0.0;
		this.liquidarAsocios = ConstantesBD.acronimoNo;
		this.fechaVigencia="";
		this.tarifasMap=new HashMap();
		this.tarifasMap.put("numRegistros", "0");
		this.codigoT="";
		this.modifInser="";
	}	
	
	/**
	 * Retorna el codigo de la tarifa SOAT
	 * @return
	 */
	public int getCodigo()
	{
		return codigo;
	}

	/**
	 * Asigna el codigo de la tarifa SOAT
	 * @param i
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}



	/**
	 * Retorna el valor de la tarifa
	 * @return
	 */
	public double getValorTarifa()
	{
		return valorTarifa;
	}

	/**
	 * Asigna el valor de la tarifa
	 * @param valorTarifa
	 */
	public void setValorTarifa(double valorTarifa)
	{
		this.valorTarifa = valorTarifa;
	}

	/**
	 * Retorna el iva que se debe aplicar a la tarifa
	 * @return
	 */
	public double getPorcentajeIva()
	{
		return porcentajeIva;
	}

	/**
	 * Asigna el iva que se debe aplicar a la tarifa
	 * @param porcentajeIva
	 */
	public void setPorcentajeIva(double porcentajeIva)
	{
		this.porcentajeIva = porcentajeIva;
	}
	
	/**
	 * Retorna el código del esquema tarifario correspondiente a esta tarifa
	 * @return
	 */
	public int getCodigoEsquemaTarifario()
	{
		return codigoEsquemaTarifario;
	}

	/**
	 * Asigna el código del esquema tarifario correspondiente a esta tarifa
	 * @param codigoEsquemaTarifario
	 */
	public void setCodigoEsquemaTarifario(int odigoEsquemaTarifario)
	{
		this.codigoEsquemaTarifario = odigoEsquemaTarifario;
	}

	/**
	 * Retorna el nombre del esquema tarifario correspondiente a esta tarifa
	 * @return
	 */
	public String getNombreEsquemaTarifario()
	{
		return nombreEsquemaTarifario;
	}

	/**
	 * Asigna el nombre del esquema tarifario correspondiente a esta tarifa
	 * @param nombreEsquemaTarifario
	 */
	public void setNombreEsquemaTarifario(String nombreEsquemaTarifario)
	{
		this.nombreEsquemaTarifario = nombreEsquemaTarifario;
	}

	/**
	 * Retorna el código del servicio que corresponde a esta tarifa
	 * @return
	 */
	public int getCodigoServicio()
	{
		return codigoServicio;
	}

	/**
	 * Asigna el código del servicio que corresponde a esta tarifa
	 * @param codigoServicio
	 */
	public void setCodigoServicio(int codigoServicio)
	{
		this.codigoServicio = codigoServicio;
	}

	/**
	 * Retorna el nombre del servicio que corresponde a esta tarifa
	 * @return
	 */
	public String getNombreServicio()
	{
		return nombreServicio;
	}

	/**
	 * Asigna el nombre del servicio que corresponde a esta tarifa
	 * @param nombreServicio
	 */
	public void setNombreServicio(String nombreServicio)
	{
		this.nombreServicio = nombreServicio;
	}
	
	/**
	 * Retorna el código del tipo liquidacion de la tarifa
	 * @return
	 */
	public int getCodigoTipoLiquidacion()
	{
		return codigoTipoLiquidacion;
	}

	/**
	 * Asigna el código del tipo liquidacion de la tarifa
	 * @param codigoTipoLiquidacion
	 */
	public void setCodigoTipoLiquidacion(int codigoTipoLiquidacion)
	{
		this.codigoTipoLiquidacion = codigoTipoLiquidacion;
	}

	/**
	 * Retorna el nombre del tipo liquidacion de la tarifa
	 * @return
	 */
	public String getNombreTipoLiquidacion()
	{
		return nombreTipoLiquidacion;
	}

	/**
	 * Asigna el nombre del tipo liquidacion de la tarifa
	 * @param nombreTipoLiquidacion
	 */
	public void setNombreTipoLiquidacion(String nombreTipoLiquidacion)
	{
		this.nombreTipoLiquidacion = nombreTipoLiquidacion;
	}

	/**
	 * Retorna el estado dentro del flujo
	 * @return
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * Asigna el estado dentro del flujo
	 * @param estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}

	/**
	 * Retorna el código de la especialidad que corresponde al servicio de esta tarifa
	 * @return
	 */
	public int getCodigoEspecialidad()
	{
		return this.codigoEspecialidad;
	}

	/**
	 * Asigna el código de la especialidad que corresponde al servicio de esta tarifa
	 * @param codigoEspecialidad
	 */
	public void setCodigoEspecialidad(int codigoEspecialidad)
	{
		this.codigoEspecialidad = codigoEspecialidad;
	}

	/**
	 * Retorna el nombre de la especialidad que corresponde al servicio de esta tarifa
	 * @return
	 */
	public String getNombreEspecialidad()
	{
		return this.nombreEspecialidad;
	}

	/**
	 * Asigna el nombre de la especialidad que corresponde al servicio de esta tarifa
	 * @param nombreEspecialidad
	 */
	public void setNombreEspecialidad(String nombreEspecialidad)
	{
		this.nombreEspecialidad = nombreEspecialidad;
	}	
	

	/**
	 * Retorna cual de las opciones de la tarifa está seleccionada
	 * 1 para grupo, 2 para valor
	 * @return
	 */
	public int getRadioTarifa()
	{
		return radioTarifa;
	}

	/**
	 * Asigna cual de las opciones de la tarifa está seleccionada
	 * 1 para grupo, 2 para valor
	 * @param radioTarifa
	 */
	public void setRadioTarifa(int radioTarifa)
	{
		this.radioTarifa = radioTarifa;
	}
	
	/**
	 * Retorna el salario mínimo mensual vigente para calcular el valor de la tarifa 
	 * cuando es por unidades
	 * @return
	 */
	public double getSalarioMinimoMensualVigente()
	{
		return salarioMinimoMensualVigente;
	}

	/**
	 * Asigna el salario mínimo mensual vigente para calcular el valor de la tarifa 
	 * cuando es por unidades
	 * @param salario
	 */
	public void setSalarioMinimoMensualVigente(double salario)
	{
		salarioMinimoMensualVigente = salario;
	}

	/**
	 * Returns the enlace.
	 * @return String
	 */
	public String getEnlace()
	{
		return enlace;
	}

	/**
	 * Sets the enlace.
	 * @param enlace The enlace to set
	 */
	public void setEnlace(String enlace)
	{
		this.enlace = enlace;
	}

	/**
	 * @return Returns the grupoString.
	 */
	public String getGrupoString() {
		return grupoString;
	}
	/**
	 * @param grupoString The grupoString to set.
	 */
	public void setGrupoString(String grupoString) {
		this.grupoString = grupoString;
	}
    /**
     * @return Returns the eliminado.
     */
    public int getEliminado() {
        return eliminado;
    }
    /**
     * @param eliminado The eliminado to set.
     */
    public void setEliminado(int eliminado) {
        this.eliminado = eliminado;
    }
    /**
     * @return Returns the modificado.
     */
    public boolean isModificado() {
        return modificado;
    }
    /**
     * @param modificado The modificado to set.
     */
    public void setModificado(boolean modificado) {
        this.modificado = modificado;
    }
    /**
     * @return Returns the modificar.
     */
    public boolean isModificar() {
        return modificar;
    }
    /**
     * @param modificar The modificar to set.
     */
    public void setModificar(boolean modificar) {
        this.modificar = modificar;
    }
    /**
     * @return Returns the tarifasSOATAnteriores.
     */
    public String getTarifasSOATAnteriores() {
        return tarifasSOATAnteriores;
    }
    /**
     * @param tarifasSOATAnteriores The tarifasSOATAnteriores to set.
     */
    public void setTarifasSOATAnteriores(String tarifasSOATAnteriores) {
        this.tarifasSOATAnteriores = tarifasSOATAnteriores;
    }
    
    /**
	 * Adicionar el código de la tarifa modificada
	 * @param servicio
	 */
	public void adicionarTarifaInsertadaModificada(int servicio)
	{
		boolean existe=false;
		if(!tarifasSOATAnteriores.equals(""))
		{
			String[] codigosServicios=this.tarifasSOATAnteriores.split(",");
			for(int i=0;i<codigosServicios.length;i++)
			{
				if(servicio==Integer.parseInt(codigosServicios[i]))
				{
					existe=true;
					break;
				}
			}
		}
		if(!existe)
		{
			if(this.tarifasSOATAnteriores.equals(""))
			{
				this.tarifasSOATAnteriores=servicio+""; 
			}
			else
			{
				this.tarifasSOATAnteriores+=","+servicio;
			}
		}
	}
	
	/**
	 * Se utiliza este reset cuando se hace una eliminación
	 * ya que no es necesrio resetear toda la forma
	 */
	public void resetEliminacion()
	{
		this.codigoServicio=0;
		this.modificado=true;
		this.valorTarifa=0;
		this.porcentajeIva=0;
		this.eliminado=0;
		this.grupo = 0;
		this.codigoTipoLiquidacion = -1;
		this.nombreTipoLiquidacion = "";
	}
	
	/**
	 * @return Returns the valorBase.
	 */
	public double getValorBase() {
		return valorBase;
	}
	/**
	 * @param valorBase The valorBase to set.
	 */
	public void setValorBase(double valorBase) {
		this.valorBase = valorBase;
	}

	/**
	 * @return Returns the acronimoMetodoAjuste.
	 */
	public String getAcronimoMetodoAjuste() {
		return acronimoMetodoAjuste;
	}

	/**
	 * @param acronimoMetodoAjuste The acronimoMetodoAjuste to set.
	 */
	public void setAcronimoMetodoAjuste(String acronimoMetodoAjuste) {
		this.acronimoMetodoAjuste = acronimoMetodoAjuste;
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @return the valorTarifaConversionMoneda
	 */
	public double getValorTarifaConversionMoneda() {
		return valorTarifaConversionMoneda;
	}

	/**
	 * @param valorTarifaConversionMoneda the valorTarifaConversionMoneda to set
	 */
	public void setValorTarifaConversionMoneda(double valorTarifaConversionMoneda) {
		this.valorTarifaConversionMoneda = valorTarifaConversionMoneda;
	}

	/**
	 * @return the liquidarAsocios
	 */
	public String getLiquidarAsocios() {
		return liquidarAsocios;
	}

	/**
	 * @param liquidarAsocios the liquidarAsocios to set
	 */
	public void setLiquidarAsocios(String liquidarAsocios) {
		this.liquidarAsocios = liquidarAsocios;
	}
	
	/**
	 * @return the valorTarifaString
	 */
	public String getValorTarifaString() {
		return valorTarifaString;
	}

	/**
	 * @param valorTarifaString the valorTarifaString to set
	 */
	public void setValorTarifaString(String valorTarifaString) {
		this.valorTarifaString = valorTarifaString;
	}

	/**
	 * @return the fechaVigencia
	 */
	public String getFechaVigencia() {
		return fechaVigencia;
	}

	/**
	 * @param fechaVigencia the fechaVigencia to set
	 */
	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}

	/**
	 * @return the tarifasMap
	 */
	public HashMap getTarifasMap() {
		return tarifasMap;
	}

	/**
	 * @param tarifasMap the tarifasMap to set
	 */
	public void setTarifasMap(HashMap tarifasMap) {
		this.tarifasMap = tarifasMap;
	}
	
	public Object getTarifasMap(String key) {
		return tarifasMap.get(key);
	}

	public void setTarifasMap(String key, Object value) {
		this.tarifasMap.put(key, value);
	}

	/**
	 * @return the codigoT
	 */
	public String getCodigoT() {
		return codigoT;
	}

	/**
	 * @param codigoT the codigoT to set
	 */
	public void setCodigoT(String codigoT) {
		this.codigoT = codigoT;
	}

	/**
	 * @return the modifInser
	 */
	public String getModifInser() {
		return modifInser;
	}

	/**
	 * @param modifInser the modifInser to set
	 */
	public void setModifInser(String modifInser) {
		this.modifInser = modifInser;
	}

	/**
	 * @return the grupo
	 */
	public double getGrupo() {
		return grupo;
	}

	/**
	 * @param grupo the grupo to set
	 */
	public void setGrupo(double grupo) {
		this.grupo = grupo;
	}
	
}