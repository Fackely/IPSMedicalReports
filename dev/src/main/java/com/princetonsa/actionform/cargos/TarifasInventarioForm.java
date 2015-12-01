/*
 * TarifasInventarioForm.java 
 * Autor			:  Juan David Ramírez
 * Creado el	:  13-Mar-2055
 * 
 * Lenguaje		: Java
 * Compilador	: J2SDK 1.4.1_01
 * 
 * Copyright Princeton S.A. &copy;&reg; 2003. Todos Los Derechos Reservados.
 * */
package com.princetonsa.actionform.cargos;

import java.util.Collection;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadCadena;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.Administracion.UtilConversionMonedas;

/**
 * 
 * @author Juan David Ramírez López
 *
 * Princeton S.A.
 */
public class TarifasInventarioForm extends ValidatorForm
{
	
	private String estado;
	
	/**
	 * Mapa de tarifas
	 */
	private HashMap<String,Object> tarifas = new HashMap<String, Object>();
	
	/**
	 * Número de tarifas
	 */
	private int numTarifas;
	
	/**
	 * Posicion del registro seleccionado
	 */
	private int posTarifas;
	
	
	
	
	/**
	 * Indica por cual campo se desea ordenar
	 */
	private String ordenarPorColumna;
	
	/**
	 * Campo por el cual se había hecho el ordenamiento
	 */
	private String columnaAnterior;

	/**
	 * Código de registro
	 */
	private int codigo;
	
	/**
	 * Código del esquema tarifario
	 */
	private int esquemaTarifario;

	/**
	 * Nombre del esquema tarifario
	 */
	private String nombreEsquemaTarifario;

	/**
	 * Código del articulo seleccionado
	 */
	private int articulo;
	
	/**
	 * Nombre del articulo que se desea buscar
	 */
	private String nombreArticulo;
	
	/**
	 * Valor de la tarifa
	 */
	private double tarifa;
	
	/**
	 * Porcentaje del iva
	 */
	private double iva;
	
	/**
	 * Indica si se va a modificar o a insertar la tarifa
	 */
	private boolean modificar;
	
	/**
	 * Manejaro las modificaciones de los artículos
	 */
	private String articulosAnteriores;
	
	/**
	 * Al guardar el registro debe quedar como modificado
	 */
	private boolean modificado;
	
	/**
	 * Código del elemento eliminado
	 */
	private int eliminado;
	
	/**
	 * Listado resultado de la busqueda
	 */
	private Collection listadoConsulta;
	
	/**
	 * Listado resultado de la busqueda Impresion
	 */
	private Collection listadoConsultaImpresion;
	
	/**
	 * Acronimo del método de ajuste
	 */
	private String acronimoMetodoAjuste;
	
	/**
	 * Maneja el porcentaje de tipo de tarifa
	 */
	private double porcentaje;
	
	/**
	 * Utilizado para actualizar las tarifas cuando se realicen cambios
	 */
	private String actualizAutomatic;
	
	/**
	 * Permite seleccionar el tipo de tarifa q aplica para cada articulo
	 */
	private String tipoTarifa;
	
	/**
	 * 
	 */
	private boolean porcentajePositivo;
	
	/**
	 * 
	 */
	private String precioUltimaCompra;
	
	/**
	 * 
	 */
	private String precioBaseVenta;
	
	/**
	 * 
	 */
	private String costoPromedio;
	
	/**
	 * 
	 */
	private String precioCompraMasAlta;
	
	/**
	 * 
	 */
	private String naturalezaArticulo;
	
	/**
	 * 
	 */
	private String formaFarmaceutica;
	
	/**
	 * 
	 */
	private String concentracionArticulo;
	
	/**
	 * columna por la cual se quiere ordenar
	 */
	private String columna;
	
	/**
	 * ultima columna por la cual se ordeno
	 */
	private String ultimaPropiedad;
	
	/**
     * 
     */
    private int index;
    
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
    private String factorConversionMoneda;
    
    /**
     * 
     */
    private String totalTarifaMon;
	
    /**
	 * Código del artículo para Buscar
	 * Se debe realizar este cambio por que la
	 * búsqueda de articulos se debe realizar por
	 * el código axioma o por el interfaz; el cuál
	 * sustenta que el interfaz es de tipo varchar
	 * Según Tarea 38488
	 */
	private String articuloBusqueda;
	
	/**
	 * Mapa que almacena todas las tarifas inventario
	 */
	private HashMap todasTarifasMap;
	
	/**
	 * Variable para controlar la seccion modificacion y nuevo registro
	 */
	private String modifInser;
	
	/**
	 * Variable para la fecha de vigencia
	 */
	private String fechaVigencia;
	
	/**
	 * Mapa para las fechas vigencia de las
	 * tarifas por esquema articulo
	 */
	private HashMap fechasVigenciaMap;
    
    
//---------------------------------------------------- Metodo Reset -----------------------------------------------------------------------	
	/**
	 * Método para resetear la clase
	 */
	public void reset(int codigoInstitucion)
	{
		this.tarifas = new HashMap<String, Object>();
		this.numTarifas = 0;
		this.posTarifas = 0;
		
		ordenarPorColumna="";
		columnaAnterior="";
		codigo=0;
		esquemaTarifario=0;
		nombreEsquemaTarifario="";
		articulo = 0;
		articuloBusqueda = "";
		nombreArticulo="";
		tarifa=0;
		iva=0;
		modificar=false;
		modificado=false;
		articulosAnteriores="";
		eliminado=0;
		this.acronimoMetodoAjuste = "";
		this.porcentaje=0;
		this.actualizAutomatic="";
		this.tipoTarifa="";
		this.porcentajePositivo=true;
		this.precioBaseVenta="0";
		this.precioUltimaCompra="0";
		this.costoPromedio="0";
		this.precioCompraMasAlta="";
		this.naturalezaArticulo="";
		this.formaFarmaceutica="";
		this.concentracionArticulo="";
		this.index=ConstantesBD.codigoNuncaValido;
        this.manejaConversionMoneda=UtilidadTexto.getBoolean(ValoresPorDefecto.getManejaConversionMonedaExtranjera(codigoInstitucion));
        this.inicializarTagMap(codigoInstitucion);
        this.totalTarifaMon="";
        this.todasTarifasMap=new HashMap();
        this.todasTarifasMap.put("numRegistros", "");
        this.modifInser="";
        this.fechaVigencia="";
        this.fechasVigenciaMap=new HashMap();
        this.fechasVigenciaMap.put("numRegistros", "0");
	}
	
	/**
     * 
     * @param codigoInstitucion
     */
    public void inicializarTagMap (int codigoInstitucion)
    {
    	tiposMonedaTagMap= UtilConversionMonedas.obtenerTiposMonedaTagMap(codigoInstitucion, /*mostrarMonedaManejaInstitucion*/false);
    }

	public void resetBusqueda()
	{
		ordenarPorColumna="";
		columnaAnterior="";
		codigo=0;
		articulo=0;
		articuloBusqueda = "";
		nombreArticulo="";
		tarifa=0;
		iva=0;
		modificar=false;
		modificado=false;
		articulosAnteriores="";
		tipoTarifa="";
		actualizAutomatic="";
		eliminado=0;
	}	
	
//------------------------------------------------- Validaciones ----------------------------------------------------------------
	/**
	 * Método para validar inserciones
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores = new ActionErrors();
		if(estado.equals("guardar"))
		
		{
			//Se toma la posición del registro que no está modificado
			int pos = 0;
			for(int i=0;i<this.numTarifas;i++)
				if(!UtilidadTexto.getBoolean(this.getTarifas("modificado_"+i).toString()))
					pos = i;
			
			if(UtilidadTexto.isEmpty(this.getTarifas("tipoTarifa_"+pos).toString()))
			{
				errores.add("",new ActionMessage("errors.required","El Tipo Tarifa "));
			}
			else
			{
				String tipoTarifa=this.getTarifas("tipoTarifa_"+pos)+"";
				if(tipoTarifa.equals(ConstantesIntegridadDominio.acronimoCostoPromedio))
				{
					String costoPromedio=this.getTarifas("costoPromedio_"+pos)+"";
					if(Utilidades.convertirAEntero(costoPromedio)<=0)
					{
						errores.add("error.errorEnBlanco",new ActionMessage("error.errorEnBlanco","El Articulo no tiene registrado el Costo Promedio "));
					}
				}
				else if(tipoTarifa.equals(ConstantesIntegridadDominio.acronimoPrecioCompraMasAlta))
				{
					String precioCompraAlta=this.getTarifas("precioCompraMasAlta_"+pos)+"";
					if(Utilidades.convertirAEntero(precioCompraAlta)<=0)
					{
						errores.add("error.errorEnBlanco",new ActionMessage("error.errorEnBlanco","El Articulo no tiene registrado el Precio Compra mas Alta."));
					}
				}
			}
			
			if(!this.getTarifas("valorTarifa_"+pos).toString().equals(""))
			{
			
				double tarifa = Double.parseDouble(this.getTarifas("valorTarifa_"+pos).toString()); 
				
				if(acronimoMetodoAjuste.equals(ConstantesBD.metodoAjusteUnidad))
				{
					tarifa = Math.round(tarifa);
				}
				else if(acronimoMetodoAjuste.equals(ConstantesBD.metodoAjusteDecena))
				{
					tarifa = UtilidadCadena.redondearALaDecena((int)tarifa);
				}
				else if(acronimoMetodoAjuste.equals(ConstantesBD.metodoAjusteCentena))
				{
					tarifa = UtilidadCadena.redondearALaCentena((int)tarifa);
				}
				
				if(tarifa<=0)
				{
					errores.add("Tarifa Menor 0", new ActionMessage("errors.floatMayorQue", "La Tarifa", "0"));
				}
			}
			else
			{
				errores.add("Tarifa Menor 0", new ActionMessage("errors.floatMayorQue", "La Tarifa", "0"));
			}
			
			if(!errores.isEmpty())
				this.estado = "ingresarNuevaTarifa";
			
			
		}else
			if(estado.equals("ingresarModificar"))
				
			{
				if(UtilidadTexto.isEmpty(this.getEsquemaTarifario()+"")||this.esquemaTarifario==0)
				{
					errores.add("",new ActionMessage("errors.required","El Esquema Tarifario "));
					this.setEstado("empezar");
				}
			}else
				if(estado.equals("consultarImprimir"))
					if(UtilidadTexto.isEmpty(this.getEsquemaTarifario()+"")||this.esquemaTarifario==0)
					{
						errores.add("",new ActionMessage("errors.required","El Esquema Tarifario "));
						this.setEstado("consultar");
					}
		return errores;
	}

//--------------------------------------------------- Getters and Setters --------------------------------------------------------------
	/**
	 * @return Retorna articulo.
	 */
	public int getArticulo()
	{
		return articulo;
	}
	/**
	 * @param articulo Asigna articulo.
	 */
	public void setArticulo(int articulo)
	{
		this.articulo = articulo;
	}
	/**
	 * @return Retorna codigo.
	 */
	public int getCodigo()
	{
		return codigo;
	}
	/**
	 * @param codigo Asigna codigo.
	 */
	public void setCodigo(int codigo)
	{
		this.codigo = codigo;
	}
	/**
	 * @return Retorna columnaAnterior.
	 */
	public String getColumnaAnterior()
	{
		return columnaAnterior;
	}
	/**
	 * @param columnaAnterior Asigna columnaAnterior.
	 */
	public void setColumnaAnterior(String columnaAnterior)
	{
		this.columnaAnterior = columnaAnterior;
	}
	/**
	 * @return Retorna esquemaTarifario.
	 */
	public int getEsquemaTarifario()
	{
		return esquemaTarifario;
	}
	/**
	 * @param esquemaTarifario Asigna esquemaTarifario.
	 */
	public void setEsquemaTarifario(int esquemaTarifario)
	{
		this.esquemaTarifario = esquemaTarifario;
	}
	/**
	 * @return Retorna estado.
	 */
	public String getEstado()
	{
		return estado;
	}
	/**
	 * @param estado Asigna estado.
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}
	/**
	 * @return Retorna iva.
	 */
	public double getIva()
	{
		return iva;
	}
	/**
	 * @param iva Asigna iva.
	 */
	public void setIva(double iva)
	{
		this.iva = iva;
	}
	/**
	 * @return Retorna ordenarPorColumna.
	 */
	public String getOrdenarPorColumna()
	{
		return ordenarPorColumna;
	}
	/**
	 * @param ordenarPorColumna Asigna ordenarPorColumna.
	 */
	public void setOrdenarPorColumna(String ordenarPorColumna)
	{
		this.ordenarPorColumna = ordenarPorColumna;
	}
	/**
	 * @return Retorna tarifa.
	 */
	public double getTarifa()
	{
		return tarifa;
	}
	/**
	 * @param tarifa Asigna tarifa.
	 */
	public void setTarifa(double valorTarifa)
	{
		this.tarifa = valorTarifa;
	}
	/**
	 * @return Retorna nombreEsquemaTarifario.
	 */
	public String getNombreEsquemaTarifario()
	{
		return nombreEsquemaTarifario;
	}
	/**
	 * @param nombreEsquemaTarifario Asigna nombreEsquemaTarifario.
	 */
	public void setNombreEsquemaTarifario(String nombreEsquemaTarifario)
	{
		this.nombreEsquemaTarifario = nombreEsquemaTarifario;
	}
	/**
	 * @return Retorna modificar.
	 */
	public boolean getModificar()
	{
		return modificar;
	}
	/**
	 * @param modificar Asigna modificar.
	 */
	public void setModificar(boolean modificar)
	{
		this.modificar = modificar;
	}
	/**
	 * @return Retorna articulosAnteriores.
	 */
	public String getArticulosAnteriores()
	{
		return articulosAnteriores;
	}
	/**
	 * @param articulosAnteriores Asigna articulosAnteriores.
	 */
	public void setArticulosAnteriores(String codigosAnteriores)
	{
		this.articulosAnteriores = codigosAnteriores;
	}
	
	/**
	 * Adicionar el código del artículo modificado
	 * @param articulo
	 */
	public void adicionarArticuloInsertadoModificado(int articulo)
	{
		boolean existe=false;
		if(!articulosAnteriores.equals(""))
		{
			String[] codigosArticulos=this.articulosAnteriores.split(",");
			for(int i=0;i<codigosArticulos.length;i++)
			{
				if(articulo==Integer.parseInt(codigosArticulos[i]))
				{
					existe=true;
					break;
				}
			}
		}
		if(!existe)
		{
			if(this.articulosAnteriores.equals(""))
			{
				this.articulosAnteriores=articulo+""; 
			}
			else
			{
				this.articulosAnteriores+=","+articulo;
			}
		}
	}
	/**
	 * @return Retorna modificado.
	 */
	public boolean getModificado()
	{
		return modificado;
	}
	/**
	 * @param modificado Asigna modificado.
	 */
	public void setModificado(boolean modificado)
	{
		this.modificado = modificado;
	}
	/**
	 * @return Retorna eliminado.
	 */
	public int getEliminado()
	{
		return eliminado;
	}
	/**
	 * @param eliminado Asigna eliminado.
	 */
	public void setEliminado(int eliminado)
	{
		this.eliminado = eliminado;
	}

	/**
	 * Se utiliza este reset cuando se hace una eliminación
	 * ya que no es necesrio resetear toda la forma
	 */
	public void resetEliminacion()
	{
		this.articulo=0;
		this.modificado=true;
		this.tarifa=0;
		this.iva=0;
		this.eliminado=0;
	}
	/**
	 * @return Retorna nombreArticulo.
	 */
	public String getNombreArticulo()
	{
		return nombreArticulo;
	}
	/**
	 * @param nombreArticulo Asigna nombreArticulo.
	 */
	public void setNombreArticulo(String nombreArticulo)
	{
		this.nombreArticulo = nombreArticulo;
	}
	/**
	 * @return Retorna listadoConsulta.
	 */
	public Collection getListadoConsulta()
	{
		return listadoConsulta;
	}
	/**
	 * @param listadoConsulta Asigna listadoConsulta.
	 */
	public void setListadoConsulta(Collection listadoConsulta)
	{
		this.listadoConsulta = listadoConsulta;
	}
	
	/**
	 * @return Retorna listadoConsultaImpresion.
	 */
	public Collection getListadoConsultaImpresion()
	{
		return listadoConsultaImpresion;
	}
	/**
	 * @param listadoConsultaImpresion Asigna listadoConsultaImpresion.
	 */
	public void setListadoConsultaImpresion(Collection listadoConsultaImpresion)
	{
		this.listadoConsultaImpresion = listadoConsultaImpresion;
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
	 * @return the tipoTarifa
	 */
	public String getTipoTarifa() {
		return tipoTarifa;
	}

	/**
	 * @param tipoTarifa the tipoTarifa to set
	 */
	public void setTipoTarifa(String tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}

	/**
	 * @return the porcentaje
	 */
	public double getPorcentaje() {
		return porcentaje;
	}

	/**
	 * @param porcentaje the porcentaje to set
	 */
	public void setPorcentaje(double porcentaje) {
		this.porcentaje = porcentaje;
	}

	/**
	 * @return the actualizAutomatic
	 */
	public String getActualizAutomatic() {
		return actualizAutomatic;
	}

	/**
	 * @param actualizAutomatic the actualizAutomatic to set
	 */
	public void setActualizAutomatic(String actualizAutomatic) {
		this.actualizAutomatic = actualizAutomatic;
	}

	/**
	 * @return the porcentajePositivo
	 */
	public boolean isPorcentajePositivo() {
		return porcentajePositivo;
	}

	/**
	 * @param porcentajePositivo the porcentajePositivo to set
	 */
	public void setPorcentajePositivo(boolean porcentajePositivo) {
		this.porcentajePositivo = porcentajePositivo;
	}

	/**
	 * @return the precioBaseVenta
	 */
	public String getPrecioBaseVenta() {
		return precioBaseVenta;
	}

	/**
	 * @param precioBaseVenta the precioBaseVenta to set
	 */
	public void setPrecioBaseVenta(String precioBaseVenta) {
		this.precioBaseVenta = precioBaseVenta;
	}

	/**
	 * @return the precioUltimaCompra
	 */
	public String getPrecioUltimaCompra() {
		return precioUltimaCompra;
	}

	/**
	 * @param precioUltimaCompra the precioUltimaCompra to set
	 */
	public void setPrecioUltimaCompra(String precioUltimaCompra) {
		this.precioUltimaCompra = precioUltimaCompra;
	}

	

	/**
	 * @return the formaFarmaceutica
	 */
	public String getFormaFarmaceutica() {
		return formaFarmaceutica;
	}

	/**
	 * @param formaFarmaceutica the formaFarmaceutica to set
	 */
	public void setFormaFarmaceutica(String formaFarmaceutica) {
		this.formaFarmaceutica = formaFarmaceutica;
	}

	/**
	 * @return the concentracionArticulo
	 */
	public String getConcentracionArticulo() {
		return concentracionArticulo;
	}

	/**
	 * @param concentracionArticulo the concentracionArticulo to set
	 */
	public void setConcentracionArticulo(String concentracionArticulo) {
		this.concentracionArticulo = concentracionArticulo;
	}

	/**
	 * @return the naturalezaArticulo
	 */
	public String getNaturalezaArticulo() {
		return naturalezaArticulo;
	}

	/**
	 * @param naturalezaArticulo the naturalezaArticulo to set
	 */
	public void setNaturalezaArticulo(String naturalezaArticulo) {
		this.naturalezaArticulo = naturalezaArticulo;
	}

	/**
	 * @return the columna
	 */
	public String getColumna() {
		return columna;
	}

	/**
	 * @param columna the columna to set
	 */
	public void setColumna(String columna) {
		this.columna = columna;
	}

	/**
	 * @return the ultimaPropiedad
	 */
	public String getUltimaPropiedad() {
		return ultimaPropiedad;
	}

	/**
	 * @param ultimaPropiedad the ultimaPropiedad to set
	 */
	public void setUltimaPropiedad(String ultimaPropiedad) {
		this.ultimaPropiedad = ultimaPropiedad;
	}

	/**
	 * @return the tarifas
	 */
	public HashMap<String, Object> getTarifas() {
		return tarifas;
	}

	/**
	 * @param tarifas the tarifas to set
	 */
	public void setTarifas(HashMap<String, Object> tarifas) {
		this.tarifas = tarifas;
	}
	
	/**
	 * @return the tarifas
	 */
	public Object getTarifas(String key) {
		return tarifas.get(key);
	}

	/**
	 * @param Asgina elemento al mapa tarifas the tarifas to set
	 */
	public void setTarifas(String key,Object obj) {
		this.tarifas.put(key,obj);
	}

	/**
	 * @return the numTarifas
	 */
	public int getNumTarifas() {
		return numTarifas;
	}

	/**
	 * @param numTarifas the numTarifas to set
	 */
	public void setNumTarifas(int numTarifas) {
		this.numTarifas = numTarifas;
	}

	/**
	 * @return the posTarifas
	 */
	public int getPosTarifas() {
		return posTarifas;
	}

	/**
	 * @param posTarifas the posTarifas to set
	 */
	public void setPosTarifas(int posTarifas) {
		this.posTarifas = posTarifas;
	}

	/**
	 * @return the factorConversionMoneda
	 */
	public String getFactorConversionMoneda() {
		return factorConversionMoneda;
	}

	/**
	 * @param factorConversionMoneda the factorConversionMoneda to set
	 */
	public void setFactorConversionMoneda(String factorConversionMoneda) {
		this.factorConversionMoneda = factorConversionMoneda;
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
	 * @return the totalTarifaMon
	 */
	public String getTotalTarifaMon() {
		return totalTarifaMon;
	}

	/**
	 * @param totalTarifaMon the totalTarifaMon to set
	 */
	public void setTotalTarifaMon(String totalTarifaMon) {
		this.totalTarifaMon = totalTarifaMon;
	}

	/**
	 * @return the articuloBusqueda
	 */
	public String getArticuloBusqueda() {
		return articuloBusqueda;
	}

	/**
	 * @param articuloBusqueda the articuloBusqueda to set
	 */
	public void setArticuloBusqueda(String articuloBusqueda) {
		this.articuloBusqueda = articuloBusqueda;
	}

	/**
	 * @return the todasTarifasMap
	 */
	public HashMap getTodasTarifasMap() {
		return todasTarifasMap;
	}

	/**
	 * @param todasTarifasMap the todasTarifasMap to set
	 */
	public void setTodasTarifasMap(HashMap todasTarifasMap) {
		this.todasTarifasMap = todasTarifasMap;
	}
	
	public Object getTodasTarifasMap(String key) {
		return todasTarifasMap.get(key);
	}

	public void setTodasTarifasMap(String key, Object value) {
		this.todasTarifasMap.put(key, value);
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
	 * @return the costoPromedio
	 */
	public String getCostoPromedio() {
		return costoPromedio;
	}

	/**
	 * @param costoPromedio the costoPromedio to set
	 */
	public void setCostoPromedio(String costoPromedio) {
		this.costoPromedio = costoPromedio;
	}

	/**
	 * @return the precioCompraMasAlta
	 */
	public String getPrecioCompraMasAlta() {
		return precioCompraMasAlta;
	}

	/**
	 * @param precioCompraMasAlta the precioCompraMasAlta to set
	 */
	public void setPrecioCompraMasAlta(String precioCompraMasAlta) {
		this.precioCompraMasAlta = precioCompraMasAlta;
	}

	/**
	 * @return the fechasVigenciaMap
	 */
	public HashMap getFechasVigenciaMap() {
		return fechasVigenciaMap;
	}

	/**
	 * @param fechasVigenciaMap the fechasVigenciaMap to set
	 */
	public void setFechasVigenciaMap(HashMap fechasVigenciaMap) {
		this.fechasVigenciaMap = fechasVigenciaMap;
	}
	
	public Object getFechasVigenciaMap(String key) {
		return fechasVigenciaMap.get(key);
	}

	public void setFechasVigenciaMap(String key,Object value) {
		this.fechasVigenciaMap.put(key, value);
	}
}
