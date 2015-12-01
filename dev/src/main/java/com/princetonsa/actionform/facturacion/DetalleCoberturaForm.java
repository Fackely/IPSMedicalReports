/*
 * Creado May 11, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * DetalleCoberturaForm
 * com.princetonsa.actionform.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.facturacion.DtoCoberturaProgramas;
import com.princetonsa.dto.facturacion.DtoCoberturaServicios;
import com.princetonsa.dto.odontologia.DtoDetalleProgramas;
import com.princetonsa.dto.odontologia.DtoServicioOdontologico;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.InfoDatosString;
import util.UtilidadTexto;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 11, 2007
 */
public class DetalleCoberturaForm extends ValidatorForm
{
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private int maxPageItems;
	
	/**
	 * 
	 */
	private String linkSiguiente;
	
	/**
	 * 
	 */
	private int offset;
	
	
	/**
	 * 
	 */
	private String patronOrdenar;
	
	/**
	 * 
	 */
	private String ultimoPatron;
	
	/**
	 * 
	 */
	private int indexSeleccionado;
	
	/**
	 * 
	 */
	private int posEliminar;

	
	/**
	 * 
	 */
	private HashMap coberturas;
	
	/**
	 * 
	 */
	private HashMap coberturasEliminados;
	
	/**
	 * 
	 */
	private HashMap agrupacionArticulos;
	
	
	/**
	 * 
	 */
	private HashMap agrupacionArticulosEliminados;
	
	/**
	 * 
	 */
	private HashMap articulos;
	
	/**
	 * 
	 */
	private HashMap articulosEliminados;
	
	/**
	 * 
	 */
	private HashMap agrupacionServicios;
	
	/**
	 * 
	 */
	private HashMap agrupacionServiciosEliminados;
	
	/**
	 * 
	 */
	private HashMap servicios;
	
	
	/**
	 * 
	 */
	private HashMap serviciosEliminados;
	
	
	private Vector<InfoDatosString> selCoberturas;
	
	/**
	 * 
	 */
	private Vector<InfoDatosString> selViasIngreso;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> selTiposPaciente;
	
	/**
	 * 
	 */
	private Vector<InfoDatosString> selNaturalezaPaciente;
	
	
	/**
	 * 
	 */
	private int posMapa;
	private int posi;
	/**
	 * Encargada de mostrar al usuario si la operacion fue
	 * existosa  o no.
	 */
	private boolean operacionTrue;
	

	/**
	 *Elemento usado como auxiliar para el tipo de cobertura 
	 */
	private String tipoCobertura;
	
	/**
	 * Elemento para indicar la posicion de la cobertura a evaluar su tipo
	 */
	private int posCobertura;
	
	private ArrayList<DtoCoberturaProgramas> listadoCoberturaProgramas;
	
	private DtoCoberturaProgramas coberturaProgramas;
	
	private int posCoberturaProgramas;
	
	private ArrayList<DtoDetalleProgramas> listadoDetalleProgramas;
	
	
	/**
	 *Cambio Anexo 945 
	 */
	private String codigosServiciosInsertados;
	
	private DtoCoberturaServicios coberturaServicios;
	
	private ArrayList<DtoCoberturaServicios> listadoCoberturaServicios;
	
	private int posCoberturaServicios;
	
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.agrupacionArticulos=new HashMap();
		this.agrupacionArticulos.put("numRegistros", "0");
		this.agrupacionArticulosEliminados=new HashMap();
		this.agrupacionArticulosEliminados.put("numRegistros", "0");
		this.articulos=new HashMap();
		this.articulos.put("numRegistros", "0");
		this.articulosEliminados=new HashMap();
		this.articulosEliminados.put("numRegistros", "0");
		this.agrupacionServicios=new HashMap();
		this.agrupacionServicios.put("numRegistros", "0");
		this.agrupacionServiciosEliminados=new HashMap();
		this.agrupacionServiciosEliminados.put("numRegistros","0");
		this.servicios=new HashMap();
		this.servicios.put("numRegistros", "0");
		this.serviciosEliminados=new HashMap();
		this.serviciosEliminados.put("numRegistros", "0");
		this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		this.coberturas=new HashMap();
		this.coberturas.put("numRegistros", "0");
		this.coberturasEliminados=new HashMap();
		this.coberturasEliminados.put("numRegistros", "0");
		this.linkSiguiente="";
		this.offset=0;
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.selCoberturas=new Vector<InfoDatosString>();
		this.selViasIngreso=new Vector<InfoDatosString>();
		this.selNaturalezaPaciente=new Vector<InfoDatosString>();
		this.selTiposPaciente = new ArrayList<HashMap<String,Object>>();
		this.posMapa=ConstantesBD.codigoNuncaValido;
		this.operacionTrue=false;
		this.tipoCobertura=ConstantesIntegridadDominio.acronimoTipoCoberturaGeneral;
		this.posCobertura=ConstantesBD.codigoNuncaValido;
		this.listadoCoberturaProgramas=new ArrayList<DtoCoberturaProgramas>();
		this.coberturaProgramas=new DtoCoberturaProgramas();
		this.posCoberturaProgramas=ConstantesBD.codigoNuncaValido;
		this.listadoDetalleProgramas=new ArrayList<DtoDetalleProgramas>();
		this.codigosServiciosInsertados="";
		this.coberturaServicios=new DtoCoberturaServicios();
		this.listadoCoberturaServicios=new ArrayList<DtoCoberturaServicios>();
		this.posCoberturaServicios=ConstantesBD.codigoNuncaValido;
		
		
	}
	

	/**
	 * 
	 *
	 */
	public void resetMapasEliminacion()
	{
		this.agrupacionArticulosEliminados=new HashMap();
		this.agrupacionArticulosEliminados.put("numRegistros", "0");
		this.articulosEliminados=new HashMap();
		this.articulosEliminados.put("numRegistros", "0");
		this.agrupacionServiciosEliminados=new HashMap();
		this.agrupacionServiciosEliminados.put("numRegistros","0");
		this.serviciosEliminados=new HashMap();
		this.serviciosEliminados.put("numRegistros", "0");
		this.posEliminar=ConstantesBD.codigoNuncaValido;
	}
	
	public void resetCoberturaProgramas()
	{
		this.coberturaProgramas=new DtoCoberturaProgramas();
	}
	
	public void resetCoberturaServicios()
	{
		this.coberturaServicios=new DtoCoberturaServicios();
	}

	/**
	 * Método para validar la inserción de datos
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(estado.equals("guardarCobertura"))
		{
			for(int i=0;i<Integer.parseInt(coberturas.get("numRegistros")+"");i++)
			{
				if(UtilidadTexto.isEmpty(this.coberturas.get("codigocobertura_"+i)+""))
				{
					errores.add("",new ActionMessage("errors.required","La cobertura del registro "+(i+1)));
				}
				for(int j=0;j<i;j++)
				{
					if(
							(this.coberturas.get("codigocobertura_"+i)+"").equalsIgnoreCase(this.coberturas.get("codigocobertura_"+j)+"") &&
							(this.coberturas.get("viaingreso_"+i)+"").equalsIgnoreCase(this.coberturas.get("viaingreso_"+j)+"") &&
							(this.coberturas.get("natpaciente_"+i)+"").equalsIgnoreCase(this.coberturas.get("natpaciente_"+j)+"")
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","El registro "+(i+1)));
					}
				}
			}
		}
		if(estado.equals("guardar"))
		{
			for(int i=0;i<Integer.parseInt(agrupacionArticulos.get("numRegistros")+"");i++)
			{
				for(int j=0;j<i;j++)
				{
					if(
							(this.agrupacionArticulos.get("clase_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("clase_"+j)+"") &&
							(this.agrupacionArticulos.get("grupo_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("grupo_"+j)+"") &&
							(this.agrupacionArticulos.get("subgrupo_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("subgrupo_"+j)+"") &&
							(this.agrupacionArticulos.get("naturaleza_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("naturaleza_"+j)+"") 
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En Agrupacion Articulo: La clase \""+this.agrupacionArticulos.get("nomclase_"+i)+"\" Grupo: \""+this.agrupacionArticulos.get("nomgrupo_"+i)+"\" SubGrupo: \""+this.agrupacionArticulos.get("nomsubgrupo_"+i)+"\" Naturaleza: \""+this.agrupacionArticulos.get("nomnaturaleza_"+i)+"\""));
					}
				}
			}
			for(int i=0;i<Integer.parseInt(agrupacionServicios.get("numRegistros")+"");i++)
			{
				for(int j=0;j<i;j++)
				{
					if(
							(this.agrupacionServicios.get("tipopos_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("tipopos_"+j)+"") &&
							(this.agrupacionServicios.get("gruposervicio_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("gruposervicio_"+j)+"") &&
							(this.agrupacionServicios.get("tiposervicio_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("tiposervicio_"+j)+"") &&
							(this.agrupacionServicios.get("especialidad_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("especialidad_"+j)+"")
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En Agrupacion Servicios: El Grupo: \""+this.agrupacionServicios.get("descgruposervicio_"+i)+"\" Tipo Servicio: \""+this.agrupacionServicios.get("nomtiposervicio_"+i)+"\" Especialidad: \""+this.agrupacionServicios.get("nomespecialidad_"+i)+"\" Tipo pos:  \""+this.agrupacionServicios.get("tipopos_"+i)+"\""));
					}
				}
			}
		}
		if (!errores.isEmpty())
			this.setOperacionTrue(false);
		
		return errores;
	}

	/**
	 * 
	 * @return
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}


	/**
	 * @return the maxPageItems
	 */
	public int getMaxPageItems()
	{
		return maxPageItems;
	}

	/**
	 * @param maxPageItems the maxPageItems to set
	 */
	public void setMaxPageItems(int maxPageItems)
	{
		this.maxPageItems = maxPageItems;
	}


	/**
	 * @return the agrupacionArticulos
	 */
	public HashMap getAgrupacionArticulos()
	{
		return agrupacionArticulos;
	}

	/**
	 * @param agrupacionArticulos the agrupacionArticulos to set
	 */
	public void setAgrupacionArticulos(HashMap agrupacionArticulos)
	{
		this.agrupacionArticulos = agrupacionArticulos;
	}

	/**
	 * @return the agrupacionServicios
	 */
	public HashMap getAgrupacionServicios()
	{
		return agrupacionServicios;
	}

	/**
	 * @param agrupacionServicios the agrupacionServicios to set
	 */
	public void setAgrupacionServicios(HashMap agrupacionServicios)
	{
		this.agrupacionServicios = agrupacionServicios;
	}

	/**
	 * @return the articulos
	 */
	public HashMap getArticulos()
	{
		return articulos;
	}

	/**
	 * @param articulos the articulos to set
	 */
	public void setArticulos(HashMap articulos)
	{
		this.articulos = articulos;
	}

	/**
	 * @return the servicios
	 */
	public HashMap getServicios()
	{
		return servicios;
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(HashMap servicios)
	{
		this.servicios = servicios;
	}
	

	/**
	 * @return the agrupacionArticulos
	 */
	public Object getAgrupacionArticulos(String key)
	{
		return agrupacionArticulos.get(key);
	}

	/**
	 * @param agrupacionArticulos the agrupacionArticulos to set
	 */
	public void setAgrupacionArticulos(String key,Object value)
	{
		this.agrupacionArticulos.put(key, value);
	}

	/**
	 * @return the agrupacionServicios
	 */
	public Object getAgrupacionServicios(String key)
	{
		return agrupacionServicios.get(key);
	}

	/**
	 * @param agrupacionServicios the agrupacionServicios to set
	 */
	public void setAgrupacionServicios(String key,Object value)
	{
		this.agrupacionServicios.put(key, value);
	}

	/**
	 * @return the articulos
	 */
	public Object getArticulos(String key)
	{
		return articulos.get(key);
	}

	/**
	 * @param articulos the articulos to set
	 */
	public void setArticulos(String key,Object value)
	{
		this.articulos.put(key, value);
	}

	/**
	 * @return the servicios
	 */
	public Object getServicios(String key)
	{
		return servicios.get(key);
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(String key,Object value)
	{
		this.servicios.put(key, value);
	}

	/**
	 * @return the indexSeleccionado
	 */
	public int getIndexSeleccionado()
	{
		return indexSeleccionado;
	}

	/**
	 * @param indexSeleccionado the indexSeleccionado to set
	 */
	public void setIndexSeleccionado(int indexSeleccionado)
	{
		this.indexSeleccionado = indexSeleccionado;
	}


	/**
	 * @return the agrupacionArticulosEliminados
	 */
	public HashMap getAgrupacionArticulosEliminados()
	{
		return agrupacionArticulosEliminados;
	}

	/**
	 * @param agrupacionArticulosEliminados the agrupacionArticulosEliminados to set
	 */
	public void setAgrupacionArticulosEliminados(
			HashMap agrupacionArticulosEliminados)
	{
		this.agrupacionArticulosEliminados = agrupacionArticulosEliminados;
	}


	/**
	 * @return the posEliminar
	 */
	public int getPosEliminar()
	{
		return posEliminar;
	}

	/**
	 * @param posEliminar the posEliminar to set
	 */
	public void setPosEliminar(int posEliminar)
	{
		this.posEliminar = posEliminar;
	}

	/**
	 * @return the articulosEliminados
	 */
	public HashMap getArticulosEliminados()
	{
		return articulosEliminados;
	}

	/**
	 * @param articulosEliminados the articulosEliminados to set
	 */
	public void setArticulosEliminados(HashMap articulosEliminados)
	{
		this.articulosEliminados = articulosEliminados;
	}

	/**
	 * @return the agrupacionServiciosEliminados
	 */
	public HashMap getAgrupacionServiciosEliminados()
	{
		return agrupacionServiciosEliminados;
	}

	/**
	 * @param agrupacionServiciosEliminados the agrupacionServiciosEliminados to set
	 */
	public void setAgrupacionServiciosEliminados(
			HashMap agrupacionServiciosEliminados)
	{
		this.agrupacionServiciosEliminados = agrupacionServiciosEliminados;
	}

	/**
	 * @return the serviciosEliminados
	 */
	public HashMap getServiciosEliminados()
	{
		return serviciosEliminados;
	}

	/**
	 * @param serviciosEliminados the serviciosEliminados to set
	 */
	public void setServiciosEliminados(HashMap serviciosEliminados)
	{
		this.serviciosEliminados = serviciosEliminados;
	}


	/**
	 * @return the coberturas
	 */
	public HashMap getCoberturas()
	{
		return coberturas;
	}


	/**
	 * @param coberturas the coberturas to set
	 */
	public void setCoberturas(HashMap coberturas)
	{
		this.coberturas = coberturas;
	}
	

	/**
	 * @return the coberturas
	 */
	public Object getCoberturas(String key)
	{
		return coberturas.get(key);
	}


	/**
	 * @param coberturas the coberturas to set
	 */
	public void setCoberturas(String key,Object value)
	{
		this.coberturas.put(key, value);
	}


	/**
	 * @return the coberturasEliminados
	 */
	public HashMap getCoberturasEliminados()
	{
		return coberturasEliminados;
	}


	/**
	 * @param coberturasEliminados the coberturasEliminados to set
	 */
	public void setCoberturasEliminados(HashMap coberturasEliminados)
	{
		this.coberturasEliminados = coberturasEliminados;
	}


	/**
	 * @return the linkSiguiente
	 */
	public String getLinkSiguiente()
	{
		return linkSiguiente;
	}


	/**
	 * @param linkSiguiente the linkSiguiente to set
	 */
	public void setLinkSiguiente(String linkSiguiente)
	{
		this.linkSiguiente = linkSiguiente;
	}


	/**
	 * @return the offset
	 */
	public int getOffset()
	{
		return offset;
	}


	/**
	 * @param offset the offset to set
	 */
	public void setOffset(int offset)
	{
		this.offset = offset;
	}


	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar()
	{
		return patronOrdenar;
	}


	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar)
	{
		this.patronOrdenar = patronOrdenar;
	}


	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron()
	{
		return ultimoPatron;
	}


	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron)
	{
		this.ultimoPatron = ultimoPatron;
	}


	/**
	 * @return the selCoberturas
	 */
	public Vector<InfoDatosString> getSelCoberturas()
	{
		return selCoberturas;
	}


	/**
	 * @param selCoberturas the selCoberturas to set
	 */
	public void setSelCoberturas(Vector<InfoDatosString> selCoberturas)
	{
		this.selCoberturas = selCoberturas;
	}


	/**
	 * @return the selNaturalezaPaciente
	 */
	public Vector<InfoDatosString> getSelNaturalezaPaciente()
	{
		return selNaturalezaPaciente;
	}


	/**
	 * @param selNaturalezaPaciente the selNaturalezaPaciente to set
	 */
	public void setSelNaturalezaPaciente(
			Vector<InfoDatosString> selNaturalezaPaciente)
	{
		this.selNaturalezaPaciente = selNaturalezaPaciente;
	}


	/**
	 * @return the selViasIngreso
	 */
	public Vector<InfoDatosString> getSelViasIngreso()
	{
		return selViasIngreso;
	}


	/**
	 * @return the selTiposPaciente
	 */
	public ArrayList<HashMap<String, Object>> getSelTiposPaciente() {
		return selTiposPaciente;
	}


	/**
	 * @param selTiposPaciente the selTiposPaciente to set
	 */
	public void setSelTiposPaciente(
			ArrayList<HashMap<String, Object>> selTiposPaciente) {
		this.selTiposPaciente = selTiposPaciente;
	}


	/**
	 * @param selViasIngreso the selViasIngreso to set
	 */
	public void setSelViasIngreso(Vector<InfoDatosString> selViasIngreso)
	{
		this.selViasIngreso = selViasIngreso;
	}


	/**
	 * @return the posMapa
	 */
	public int getPosMapa() {
		return posMapa;
	}


	/**
	 * @param posMapa the posMapa to set
	 */
	public void setPosMapa(int posMapa) {
		this.posMapa = posMapa;
	}
	
	public boolean isOperacionTrue() {
		return operacionTrue;
	}


	public void setOperacionTrue(boolean operacionTrue) {
		this.operacionTrue = operacionTrue;
	}


	public String getTipoCobertura() {
		return tipoCobertura;
	}


	public void setTipoCobertura(String tipoCobertura) {
		this.tipoCobertura = tipoCobertura;
	}


	public int getPosCobertura() {
		return posCobertura;
	}


	public void setPosCobertura(int posCobertura) {
		this.posCobertura = posCobertura;
	}


	public ArrayList<DtoCoberturaProgramas> getListadoCoberturaProgramas() {
		return listadoCoberturaProgramas;
	}


	public void setListadoCoberturaProgramas(
			ArrayList<DtoCoberturaProgramas> listadoCoberturaProgramas) {
		this.listadoCoberturaProgramas = listadoCoberturaProgramas;
	}


	public DtoCoberturaProgramas getCoberturaProgramas() {
		return coberturaProgramas;
	}


	public void setCoberturaProgramas(DtoCoberturaProgramas coberturaProgramas) {
		this.coberturaProgramas = coberturaProgramas;
	}


	public int getPosCoberturaProgramas() {
		return posCoberturaProgramas;
	}


	public void setPosCoberturaProgramas(int posCoberturaProgramas) {
		this.posCoberturaProgramas = posCoberturaProgramas;
	}


	public ArrayList<DtoDetalleProgramas> getListadoDetalleProgramas() {
		return listadoDetalleProgramas;
	}


	public void setListadoDetalleProgramas(
			ArrayList<DtoDetalleProgramas> listadoDetalleProgramas) {
		this.listadoDetalleProgramas = listadoDetalleProgramas;
	}


	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}


	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}


	public DtoCoberturaServicios getCoberturaServicios() {
		return coberturaServicios;
	}


	public void setCoberturaServicios(DtoCoberturaServicios coberturaServicios) {
		this.coberturaServicios = coberturaServicios;
	}


	public ArrayList<DtoCoberturaServicios> getListadoCoberturaServicios() {
		return listadoCoberturaServicios;
	}


	public void setListadoCoberturaServicios(
			ArrayList<DtoCoberturaServicios> listadoCoberturaServicios) {
		this.listadoCoberturaServicios = listadoCoberturaServicios;
	}


	public int getPosCoberturaServicios() {
		return posCoberturaServicios;
	}


	public void setPosCoberturaServicios(int posCoberturaServicios) {
		this.posCoberturaServicios = posCoberturaServicios;
	}


	public void setPosi(int posi) {
		this.posi = posi;
	}


	public int getPosi() {
		return posi;
	}
	
	
	
	
}
