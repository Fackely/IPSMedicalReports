package com.princetonsa.actionform.inventarios;

import java.util.Collection;
import java.util.HashMap;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadFecha;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;


/**
 * @author Andres Mauricio Guerrero Toro
 *
 * Princeton S.A.
 */


public class ArticuloCatalogoForm extends ValidatorForm {
	
	private String estado="", linkSiguiente;
	
	/**
	 * Para la navegacion del pager
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * Para controlar la pagina actual del pager
	 */
	private int offset;
	
	private String codigo_axioma;
	
	private int acronimo;
	
	private String proveedor;
	
	private String ref_proveedor;
	
	private double val_uni_compra;
	
	private double val_uni_iva;
	
	private String busVal_uni_compra;
	
	private String busVal_uni_iva;
	
	private String fecha_ini_vigencia;
	
	private String fecha_fin_vigencia;
	
	private String clase;
	
	private String grupo;
	
	private String subgrupo;
	
	private String codigo;
	
	private int numArticulos;
	
	private String naturaleza;
	
	private int institucion;
	
	private String minsalud;
	
	private String registroInvima;
	
	private String formaFarmaceutica;
	
	private String categoria;
	
	private String manejaLotes;
	
	private String manejaFechaVencimiento;
	
	private double porcentajeIva;
	
	private double precioUltimaCompra;
	
	private double precioBaseVenta;
	
	private String multidosis;
	
	private double maximaCantidadMes;
	
	private String concentracion;
	
	private String unidadMedida;
	
	private String presentacion;
	
	private String descripcion;
	
	private String fechaCreacion;
	
	private int stockMinimo;
	
	private int stockMaximo;
	
	private int puntoPedido;
	
	private int cantidadCompra;
	
	private int costoPromedio;
	
	private double costoDonacion;
	
	private String indicativoAutomatico;
	
	private String indicativoPorCompletar;
	
	private Collection resultados;
	
	boolean alertaVigencia;
	
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	private ResultadoBoolean alerta=new ResultadoBoolean(false);
	
	private String mensajeVigencia;
	
	private HashMap tarifaInventarioMap;
	
	private HashMap axArt;
	
	private String tipoInventario;
	
	
	public void reset()
	{
		
		this.tarifaInventarioMap=new HashMap();
		tarifaInventarioMap.put("numRegistros", "0");
		
		this.axArt=new HashMap();
		axArt.put("numRegistros", "0");
		
		linkSiguiente="";
       	this.maxPageItems=10;
    	this.patronOrdenar="";
    	this.ultimoPatron="";
    	this.offset=0;
    	
    	codigo_axioma="";
    	acronimo=ConstantesBD.codigoNuncaValido;
    	val_uni_compra=0.0;
    	val_uni_iva=0.0;
    	numArticulos=0;
    	fecha_ini_vigencia="";
    	fecha_fin_vigencia="";
    	busVal_uni_compra="";
    	busVal_uni_iva="";
    	proveedor="";
    	descripcion="";
    	ref_proveedor="";
    	clase="";
    	grupo="";
    	subgrupo="";
    	codigo="";
    	naturaleza="";
    	minsalud=null;
    	formaFarmaceutica=null;
    	concentracion=null;
    	categoria="";
    	unidadMedida="";
    	descripcion="";
    	mensajeVigencia="";
    	this.fechaCreacion=UtilidadFecha.getFechaActual();
    	this.stockMinimo=0;
    	this.stockMaximo=0;
    	this.manejaLotes=ConstantesBD.acronimoNo;
    	this.manejaFechaVencimiento=ConstantesBD.acronimoNo;
    	this.porcentajeIva=0.0;
    	this.precioUltimaCompra=0.0;
    	this.precioBaseVenta=0.0;
    	this.multidosis=ConstantesBD.acronimoNo;
    	this.maximaCantidadMes=0.0;
    	this.puntoPedido=0;
    	this.cantidadCompra=0;
    	this.costoPromedio=0;
    	this.costoDonacion=0.0;
    	this.indicativoAutomatico=ConstantesBD.acronimoSi;
		this.indicativoPorCompletar=ConstantesBD.acronimoSi;
		this.alertaVigencia=false;
		
		this.tipoInventario = "";
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		errores.add(super.validate(mapping, request));
		if(this.estado.equals("buscar"))
		{
			if(proveedor.equals(""))
			{
				if(descripcion.equals(""))
				{
					if(ref_proveedor.equals(""))
					{
						if(codigo.equals(""))
						{
							errores.add("acronimo",new ActionMessage("errors.required","Un criterio de busqueda"));
						}
					}
				}
			}
		}
		if(this.estado.equals("guardarArticulo"))
		{
			if(proveedor.equals(""))
				errores.add("acronimo",new ActionMessage("errors.required","El proveedor "));
			if(ref_proveedor.equals(""))
				errores.add("acronimo",new ActionMessage("errors.required","La referencia del proveedor "));
			if(descripcion.equals(""))
				errores.add("acronimo",new ActionMessage("errors.required","La descripcion "));
			if(unidadMedida.equals(""))
				errores.add("acronimo",new ActionMessage("errors.required","La unidad de medida "));
			if(clase.equals(""))
				errores.add("acronimo",new ActionMessage("errors.required","La clase "));
			if(grupo.equals(""))
				errores.add("acronimo",new ActionMessage("errors.required","El grupo "));
			if(subgrupo.equals(""))
				errores.add("acronimo",new ActionMessage("errors.required","El sub grupo "));
			if(naturaleza.equals(""))
				errores.add("acronimo",new ActionMessage("errors.required","La naturaleza "));
			if(val_uni_compra==0.0)
				errores.add("acronimo",new ActionMessage("errors.required","El valor unitario de compra "));
			if(val_uni_iva==0.0)
				errores.add("acronimo",new ActionMessage("errors.required","El valor unitario de iva "));
			if(fecha_ini_vigencia.equals(""))
				errores.add("acronimo",new ActionMessage("errors.required","La fecha de inicio de vigencia "));
			if(fecha_fin_vigencia.equals(""))
				errores.add("acronimo",new ActionMessage("errors.required","La fecha final de vigencia "));
		}
		
		return errores;
	}


	public int getcantidadCompra() {
		return cantidadCompra;
	}

	public void setcantidadCompra(int cantidadCompra) {
		this.cantidadCompra = cantidadCompra;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getConcentracion() {
		return concentracion;
	}

	public void setConcentracion(String concentracion) {
		this.concentracion = concentracion;
	}

	public int getCostoPromedio() {
		return costoPromedio;
	}

	public void setCostoPromedio(int costoPromedio) {
		this.costoPromedio = costoPromedio;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(String fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public String getFormaFarmaceutica() {
		return formaFarmaceutica;
	}

	public void setFormaFarmaceutica(String formaFarmaceutica) {
		this.formaFarmaceutica = formaFarmaceutica;
	}

	public String getGrupo() {
		return grupo;
	}

	public void setGrupo(String grupo) {
		this.grupo = grupo;
	}

	public String getIndicativoAutomatico() {
		return indicativoAutomatico;
	}

	public void setIndicativoAutomatico(String indicativoAutomatico) {
		this.indicativoAutomatico = indicativoAutomatico;
	}

	public String getIndicativoPorCompletar() {
		return indicativoPorCompletar;
	}

	public void setIndicativoPorCompletar(String indicativoPorCompletar) {
		this.indicativoPorCompletar = indicativoPorCompletar;
	}

	public String getLinkSiguiente() {
		return linkSiguiente;
	}

	public void setLinkSiguiente(String linkSiguiente) {
		this.linkSiguiente = linkSiguiente;
	}

	public String getManejaFechaVencimiento() {
		return manejaFechaVencimiento;
	}

	public void setManejaFechaVencimiento(String manejaFechaVencimiento) {
		this.manejaFechaVencimiento = manejaFechaVencimiento;
	}

	public String getManejaLotes() {
		return manejaLotes;
	}

	public void setManejaLotes(String manejaLotes) {
		this.manejaLotes = manejaLotes;
	}

	public Double getMaximaCantidadMes() {
		return maximaCantidadMes;
	}

	public void setMaximaCantidadMes(Double maximaCantidadMes) {
		this.maximaCantidadMes = maximaCantidadMes;
	}

	public int getMaxPageItems() {
		return maxPageItems;
	}

	public void setMaxPageItems(int maxPageItems) {
		this.maxPageItems = maxPageItems;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	public String getMinsalud() {
		return minsalud;
	}

	public void setMinsalud(String minsalud) {
		this.minsalud = minsalud;
	}

	public String getMultidosis() {
		return multidosis;
	}

	public void setMultidosis(String multidosis) {
		this.multidosis = multidosis;
	}

	public String getNaturaleza() {
		return naturaleza;
	}

	public void setNaturaleza(String naturaleza) {
		this.naturaleza = naturaleza;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	public double getPrecioBaseVenta() {
		return precioBaseVenta;
	}

	public void setPrecioBaseVenta(double precioBaseVenta) {
		this.precioBaseVenta = precioBaseVenta;
	}

	public String getPresentacion() {
		return presentacion;
	}

	public void setPresentacion(String presentacion) {
		this.presentacion = presentacion;
	}

	public int getPuntoPedido() {
		return puntoPedido;
	}

	public void setPuntoPedido(int puntoPedido) {
		this.puntoPedido = puntoPedido;
	}

	public String getRegistroInvima() {
		return registroInvima;
	}

	public void setRegistroInvima(String registroInvima) {
		this.registroInvima = registroInvima;
	}

	public int getStockMaximo() {
		return stockMaximo;
	}

	public void setStockMaximo(int stockMaximo) {
		this.stockMaximo = stockMaximo;
	}

	public int getStockMinimo() {
		return stockMinimo;
	}

	public void setStockMinimo(int stockMinimo) {
		this.stockMinimo = stockMinimo;
	}

	public String getSubgrupo() {
		return subgrupo;
	}

	public void setSubgrupo(String subgrupo) {
		this.subgrupo = subgrupo;
	}

	public String getUltimoPatron() {
		return ultimoPatron;
	}

	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	public String getUnidadMedida() {
		return unidadMedida;
	}

	public void setUnidadMedida(String unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public Collection getResultados() {
		return resultados;
	}

	public void setResultados(Collection resultados) {
		this.resultados = resultados;
	}

	public int getCantidadCompra() {
		return cantidadCompra;
	}

	public void setCantidadCompra(int cantidadCompra) {
		this.cantidadCompra = cantidadCompra;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getRef_proveedor() {
		return ref_proveedor;
	}

	public void setRef_proveedor(String ref_proveedor) {
		this.ref_proveedor = ref_proveedor;
	}

	public String getFecha_fin_vigencia() {
		return fecha_fin_vigencia;
	}

	public void setFecha_fin_vigencia(String fecha_fin_vigencia) {
		this.fecha_fin_vigencia = fecha_fin_vigencia;
	}

	public String getFecha_ini_vigencia() {
		return fecha_ini_vigencia;
	}

	public void setFecha_ini_vigencia(String fecha_ini_vigencia) {
		this.fecha_ini_vigencia = fecha_ini_vigencia;
	}

	public double getVal_uni_compra() {
		return val_uni_compra;
	}

	public void setVal_uni_compra(double val_uni_compra) {
		this.val_uni_compra = val_uni_compra;
	}

	public double getVal_uni_iva() {
		return val_uni_iva;
	}

	public void setVal_uni_iva(double val_uni_iva) {
		this.val_uni_iva = val_uni_iva;
	}

	public String getBusVal_uni_compra() {
		return busVal_uni_compra;
	}

	public void setBusVal_uni_compra(String busVal_uni_compra) {
		this.busVal_uni_compra = busVal_uni_compra;
	}

	public String getBusVal_uni_iva() {
		return busVal_uni_iva;
	}

	public void setBusVal_uni_iva(String busVal_uni_iva) {
		this.busVal_uni_iva = busVal_uni_iva;
	}

	public void setMaximaCantidadMes(double maximaCantidadMes) {
		this.maximaCantidadMes = maximaCantidadMes;
	}

	public int getNumArticulos() {
		return numArticulos;
	}

	public void setNumArticulos(int numArticulos) {
		this.numArticulos = numArticulos;
	}

	public int getAcronimo() {
		return acronimo;
	}

	public void setAcronimo(int acronimo) {
		this.acronimo = acronimo;
	}

	public double getPorcentajeIva() {
		return porcentajeIva;
	}

	public void setPorcentajeIva(double porcentajeIva) {
		this.porcentajeIva = porcentajeIva;
	}

	public double getPrecioUltimaCompra() {
		return precioUltimaCompra;
	}

	public void setPrecioUltimaCompra(double precioUltimaCompra) {
		this.precioUltimaCompra = precioUltimaCompra;
	}

	public int getInstitucion() {
		return institucion;
	}

	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}

	public String getCodigo_axioma() {
		return codigo_axioma;
	}

	public void setCodigo_axioma(String codigo_axioma) {
		this.codigo_axioma = codigo_axioma;
	}

	public double getCostoDonacion() {
		return costoDonacion;
	}

	public void setCostoDonacion(double costoDonacion) {
		this.costoDonacion = costoDonacion;
	}

	public boolean isAlertaVigencia() {
		return alertaVigencia;
	}

	public void setAlertaVigencia(boolean alertaVigencia) {
		this.alertaVigencia = alertaVigencia;
	}

	public ResultadoBoolean getAlerta() {
		return alerta;
	}

	public void setAlerta(ResultadoBoolean alerta) {
		this.alerta = alerta;
	}

	public HashMap getTarifaInventarioMap() {
		return tarifaInventarioMap;
	}
	
	public Object getTarifaInventarioMap(String key) {
		return tarifaInventarioMap.get(key);
	}
	
	public void setTarifaInventarioMap(String key, Object Value) {
		this.tarifaInventarioMap.put(key,Value);
	}
	
	public void setTarifaInventarioMap(HashMap tarifaInventarioMap) {
		this.tarifaInventarioMap = tarifaInventarioMap;
	}

	public HashMap getAxArt() {
		return axArt;
	}

	public void setAxArt(HashMap axArt) {
		this.axArt = axArt;
	}

	public String getMensajeVigencia() {
		return mensajeVigencia;
	}

	public void setMensajeVigencia(String mensajeVigencia) {
		this.mensajeVigencia = mensajeVigencia;
	}

	public String getTipoInventario() {
		return tipoInventario;
	}

	public void setTipoInventario(String tipoInventario) {
		this.tipoInventario = tipoInventario;
	}

}