package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.UtilidadFecha;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.action.facturacion.SoportesFacturasAction;


/**
 * Clase para el manejo de la parametrizacion de contratos de entidades subcontratadas
 * Date: 2009-06-24
 * @author jfhernandez@princetonsa.com
 */
public class IngresarModificarContratosEntidadesSubcontratadasForm extends ValidatorForm 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6535042826065428558L;
	/**
	 * logger 
	 * */
	Logger logger = Logger.getLogger(SoportesFacturasAction.class);
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Hashmap para guardar los filtros para realizar la inserción de cotnratos
	 */
	private HashMap<String, Object> filtros;
	
	/**
	 * ArrayList para guardar el listado de la sentidades subcontratadas 
	 */
	private ArrayList<HashMap<String, Object>> entidades;
	
	/**
	 * ArrayList para guardar el listado de los inventarios
	 */
	private ArrayList<HashMap<String, Object>> inventarios;
	
	/**
	 * ArrayList para guardar el listado de los inventarios
	 */
	private ArrayList<HashMap<String, Object>> grupoServicio;
	
	/**
	 * ArrayList para guardar el listado de los esuqemas tarifarios de servicios
	 */
	private ArrayList<HashMap<String, Object>> esquemas;
	
	/**
	 * ArrayList para guardar el listado de los esuqemas tarifarios de procedimientos
	 */
	private ArrayList<HashMap<String, Object>> esquemasProcedimientos;
	
	/**
	 * HashMap para Guardar los los resultados cuando se cambia de entidad subcontratada
	 */
	@SuppressWarnings("rawtypes")
	private HashMap infoEntidadInv;
	
	/**
	 * HashMap para Guardar los los resultados cuando se cambia de entidad subcontratada
	 */
	@SuppressWarnings("rawtypes")
	private HashMap infoEntidadServ;
	
	/**
	 * HashMap para guardar en memoria los esquemas de inventarios 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap auxEsquemasInv;
	
	/**
	 * HashMap para guardar en memoria los esquemas de servicios 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap auxEsquemasServ;
	
	/**
	 * Entero apra eliminar lso registros que se encuentran en memoria
	 */
	private int indice;
	
	/**
	 * HashMap para guardar los resultados d ela búsqueda avanzada 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap resultadosBusqueda;
	
	/**
	 * HashMap para guardar temporalmente los servicios que se vayan a modificar 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap auxModificacionServicios;
	
	/**
	 * HashMap para guardar temporalmente los servicios que se vayan a modificar 
	 */
	@SuppressWarnings("rawtypes")
	private HashMap auxModificacionInventarios;
	
	/**
	 * int Axuxiliar para guardar el indice cuando se modifica 
	 */
	
	private int indiceMod;
	
	/**
	 * HashMap para Modificar los los resultados cuando se cambia de entidad subcontratada
	 */
	@SuppressWarnings("rawtypes")
	private HashMap infoEntidadInvAux;
	
	/**
	 * HashMap para Modificar los los resultados cuando se cambia de entidad subcontratada
	 */
	@SuppressWarnings("rawtypes")
	private HashMap infoEntidadServAux;
	
	/**
	 * Variable para informar al usuario
	 */
	private String mensaje;
	
	/**
	 * Variable que almacena el tipo de tarifa (tarifa propia o tarifa convenio persona)
	 */
	private String tipoTarifa;
	
	/**
	 * Variable que almacena el estado de las autorizaciones asociadas a los contratos
	 */
	private String estadoAutoriz;
	
	/**
	 * filtro para mostrar Esquemas Tarifarios de acuerdo al tipoTarifa=TarifaPropia, seleccionado. 
	 */
	private String mostrarFiltroTarifa;
	
	/**
	 * filtro para mostrar Esquemas Tarifarios de acuerdo al tipoTarifa=TarifaPropia, seleccionado para la pagina busqueda avanzada (consulta).
	 */
	private String mostrarFiltroTarifaConsulta;
	
	/**
	 * filtro para mostrar Esquemas Tarifarios de acuerdo al estado Moificar.
	 */
	private String mostrarFiltroTarifaModifica;
	
	
	private boolean puedeModificar;
	
	
	@SuppressWarnings("unchecked")
	public void reset()
	{
		this.estado="";
		this.filtros = new HashMap<String, Object>();
		this.filtros.put("numRegistros", "0");
		this.entidades = new ArrayList<HashMap<String,Object>>();
		this.inventarios = new ArrayList<HashMap<String,Object>>();
		this.esquemas = new ArrayList<HashMap<String,Object>>();
		this.grupoServicio = new ArrayList<HashMap<String,Object>>();
		this.esquemasProcedimientos = new ArrayList<HashMap<String,Object>>();
		this.infoEntidadInv = new HashMap<String, Object>();
		this.infoEntidadInv.put("numRegistros", "0");
		this.infoEntidadServ = new HashMap<String, Object>();
		this.infoEntidadServ.put("numRegistros", "0");
		this.auxEsquemasInv = new HashMap<String, Object>();
		this.auxEsquemasInv.put("numRegistros", "0");
		this.auxEsquemasServ = new HashMap<String, Object>();
		this.auxEsquemasServ.put("numRegistros", "0");
		this.indice=0;
		this.resultadosBusqueda = new HashMap<String, Object>();
		this.resultadosBusqueda.put("numRegistros", "0");
		this.auxModificacionServicios= new HashMap<String, Object>();
		this.auxModificacionServicios.put("numRegistros", "0");
		this.indiceMod=0;
		this.auxModificacionInventarios=new HashMap<String, Object>();
		this.auxModificacionInventarios.put("numRegistros", "0");
		this.infoEntidadInvAux = new HashMap<String, Object>();
		this.infoEntidadInvAux.put("numRegistros", "0");
		this.infoEntidadServAux = new HashMap<String, Object>();
		this.infoEntidadServAux.put("numRegistros", "0");
		this.mensaje="";
		this.mostrarFiltroTarifa=ConstantesBD.codigoNuncaValido+"";
		this.mostrarFiltroTarifaConsulta=ConstantesBD.codigoNuncaValido+"";
		this.mostrarFiltroTarifaModifica=ConstantesBD.codigoNuncaValido+"";	
		this.puedeModificar = false;
	}
	
	public void resetPpal()
	{
		this.filtros = new HashMap<String, Object>();
		this.filtros.put("numRegistros", "0");
		this.entidades = new ArrayList<HashMap<String,Object>>();
		this.inventarios = new ArrayList<HashMap<String,Object>>();
		this.esquemas = new ArrayList<HashMap<String,Object>>();
		this.grupoServicio = new ArrayList<HashMap<String,Object>>();
		this.esquemasProcedimientos = new ArrayList<HashMap<String,Object>>();
		//this.auxEsquemasInv = new HashMap<String, Object>();
		//this.auxEsquemasInv.put("numRegistros", "0");
		//this.auxEsquemasServ = new HashMap<String, Object>();
		//this.auxEsquemasServ.put("numRegistros", "0");
		this.mostrarFiltroTarifa=ConstantesBD.codigoNuncaValido+"";
		this.mostrarFiltroTarifaConsulta=ConstantesBD.codigoNuncaValido+"";
		this.mostrarFiltroTarifaModifica=ConstantesBD.codigoNuncaValido+"";
	}
	

	@SuppressWarnings("unused")
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores = new ActionErrors();
		
		if(this.estado.equals("guardar"))
        {
			//Utilidades.imprimirMapa(this.auxEsquemasInv);
			//Utilidades.imprimirMapa(this.auxEsquemasServ);
			
			boolean centinelaFecha=false;
			if (UtilidadTexto.isEmpty(this.filtros.get("codigoentidad").toString()))
			{
				errores.add(this.filtros.get("codigoentidad").toString(), new ActionMessage("errors.required","La Entidad Subcontratada "));
			}
			if (this.filtros.get("tipoTarifa").toString().equals(ConstantesBD.codigoNuncaValido+"")){
				
				errores.add(this.filtros.get("tipoTarifa").toString(), new ActionMessage("errors.required","El tipo de Tarifa"));
			}
			if (UtilidadTexto.isEmpty(this.filtros.get("nrocontrato").toString()))
			{
				errores.add(this.filtros.get("nrocontrato").toString(), new ActionMessage("errors.required","El número del contrato "));
			}
			if (UtilidadTexto.isEmpty(this.filtros.get("valorcontrato").toString()))
			{
				errores.add(this.filtros.get("valorcontrato").toString(), new ActionMessage("errors.required","El valor del contrato "));
			}
			if(!UtilidadFecha.esFechaValidaSegunAp(this.filtros.get("fechafin").toString()))
			{
				errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final "+this.filtros.get("fechafin").toString()));
			}
			if (UtilidadTexto.isEmpty(this.filtros.get("fechafin").toString()))
			{
				errores.add(this.filtros.get("fechafin").toString(), new ActionMessage("errors.required","La fecha final del contrato "));
			}
			if (UtilidadTexto.isEmpty(this.filtros.get("fechaini").toString()))
			{
				errores.add(this.filtros.get("fechaini").toString(), new ActionMessage("errors.required","La fecha inicial del contrato "));
			}
			if (!this.filtros.get("fechafin").toString().isEmpty())
			{							
				if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.filtros.get("fechafin").toString(),UtilidadFecha.conversionFormatoFechaAAp(UtilidadFecha.getFechaActual())))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Final "+this.filtros.get("fechafin").toString(), "Actual"+UtilidadFecha.getFechaActual().toString()));									
				}
				if(UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.filtros.get("fechafin").toString(), this.filtros.get("fechaini").toString()))
				{
					errores.add("", new ActionMessage("errors.fechaFinalPosteriorInicial", "Fecha Final "+this.filtros.get("fechafin").toString(), "Fecha Inicio "+this.filtros.get("fechaini").toString()));										
				}
			}
			
			boolean sinInv=true;
			boolean sinServ=true;
			if (Utilidades.convertirAEntero(this.auxEsquemasInv.get("numRegistros")+"")>0)
			{
				for (int i=0;i<Utilidades.convertirAEntero(this.auxEsquemasInv.get("numRegistros").toString());i++)
				{
					if (!this.auxEsquemasInv.get("claseinventario_"+i).equals("eliminado"))
					{	
						sinInv=false;
					}	
				}
			}
			
			if (Utilidades.convertirAEntero(this.auxEsquemasServ.get("numRegistros")+"")>0)
			{
				for (int i=0;i<Utilidades.convertirAEntero(this.auxEsquemasServ.get("numRegistros").toString());i++)
				{
					if (!this.auxEsquemasServ.get("gruposervicio_"+i).equals("eliminado"))
					{
						sinServ=false;
					}	
				}
			}
			/**Cambio tarea 110481
			if (sinInv)
				errores.add("", new ActionMessage("errors.required","Al menos un tipo de esquema tarifario de Inventarios debe ser agregado al contrato "));
			if (sinServ)
				errores.add("", new ActionMessage("errors.required","Al menos un tipo de esquema tarifario de Procedimietos debe ser agregado al contrato "));
			**/
			
			if (!UtilidadTexto.isEmpty(this.filtros.get("fechafin").toString()))
			{				
				if(!UtilidadFecha.esFechaValidaSegunAp(this.filtros.get("fechafin").toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Final del contrato "+this.filtros.get("fechafin").toString()));
					centinelaFecha=true;
				}
				
				if(centinelaFecha)
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.filtros.get("fechafin").toString(), UtilidadFecha.getFechaActual()))
						errores.add("", new ActionMessage("errors.fechaFinalPosteriorInicial", "Fecha Final del contrato "+this.filtros.get("fechafin").toString(), "Fehca Inicial del contrato "+UtilidadFecha.getFechaActual()));
				}
			}
        }
		if (this.estado.equals("adicionarInv"))
		{
			Utilidades.imprimirMapa(this.infoEntidadInv);
			boolean centinelaFecha=false;
			if (UtilidadTexto.isEmpty(this.infoEntidadInv.get("esquematarinv").toString()))
			{
				errores.add(this.infoEntidadInv.get("esquematarinv").toString(), new ActionMessage("errors.required","El esquema tarifario de Inventario "));
			}
			if (UtilidadTexto.isEmpty(this.infoEntidadInv.get("fechavigclaseinv").toString()))
			{
				errores.add(this.infoEntidadInv.get("fechavigclaseinv").toString(), new ActionMessage("errors.required","La fecha de vigencia de Inventario "));
			}
			
			if (!UtilidadTexto.isEmpty(this.infoEntidadInv.get("fechavigclaseinv").toString()))
			{
				if(!UtilidadFecha.esFechaValidaSegunAp(this.infoEntidadInv.get("fechavigclaseinv").toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia de inventarios "+this.infoEntidadInv.get("fechavigclaseinv").toString()));
					centinelaFecha=true;
				}
				if(!centinelaFecha&&!UtilidadTexto.isEmpty(filtros.get("fechaini").toString()))
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.infoEntidadInv.get("fechavigclaseinv").toString(), filtros.get("fechaini").toString()))
					{
						errores.add("", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Vigencia de Inventarios "+this.infoEntidadInv.get("fechavigclaseinv").toString(), "de Inicio del Contrato "+UtilidadFecha.conversionFormatoFechaAAp(filtros.get("fechaini").toString())));
					}
				}
				else
					errores.add(this.infoEntidadServ.get("fechvigproc").toString(), new ActionMessage("errors.required","La fecha de Inicio del contrato "));
			}		
			
		}
		if (this.estado.equals("adicionarServ"))
		{
			Utilidades.imprimirMapa(this.infoEntidadServ);
			boolean centinelaFecha=false;
			if (UtilidadTexto.isEmpty(this.infoEntidadServ.get("esquematarser").toString()))
			{
				errores.add(this.infoEntidadServ.get("esquematarser").toString(), new ActionMessage("errors.required","El esquema tarifario de Servicios "));
			}
			if (UtilidadTexto.isEmpty(this.infoEntidadServ.get("fechvigproc").toString()))
			{
				errores.add(this.infoEntidadServ.get("fechvigproc").toString(), new ActionMessage("errors.required","La fecha de vigencia de Servicios "));
			}
			if (!UtilidadTexto.isEmpty(this.infoEntidadServ.get("fechvigproc").toString()))
			{			
				if(!UtilidadFecha.esFechaValidaSegunAp(this.infoEntidadServ.get("fechvigproc").toString()))
				{
					errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia de inventarios "+this.infoEntidadServ.get("fechvigproc").toString()));
					centinelaFecha=true;
				}
				if (!centinelaFecha&&!UtilidadTexto.isEmpty(filtros.get("fechaini").toString()))
				{
					if(UtilidadFecha.esFechaMenorQueOtraReferencia(this.infoEntidadServ.get("fechvigproc").toString(), filtros.get("fechaini").toString()))
					{
						errores.add("", new ActionMessage("errors.fechaAnteriorIgualAOtraDeReferencia", "Vigencia de Servicios "+this.infoEntidadServ.get("fechvigproc").toString(), "de Inicio del Contrato "+UtilidadFecha.getFechaActual()));
					}
				}
				else
					errores.add(this.infoEntidadServ.get("fechvigproc").toString(), new ActionMessage("errors.required","La fecha de Inicio del contrato "));
				
			}
		}
		
		return errores;
    }

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public HashMap<String, Object> getFiltros() {
		return filtros;
	}

	public void setFiltros(HashMap<String, Object> filtros) {
		this.filtros = filtros;
	}

	public ArrayList<HashMap<String, Object>> getEntidades() {
		return entidades;
	}

	public void setEntidades(ArrayList<HashMap<String, Object>> entidades) {
		this.entidades = entidades;
	}

	public ArrayList<HashMap<String, Object>> getInventarios() {
		return inventarios;
	}

	public void setInventarios(ArrayList<HashMap<String, Object>> inventarios) {
		this.inventarios = inventarios;
	}

	public ArrayList<HashMap<String, Object>> getEsquemas() {
		return esquemas;
	}

	public void setEsquemas(ArrayList<HashMap<String, Object>> esquemas) {
		this.esquemas = esquemas;
	}
	
	public ArrayList<HashMap<String, Object>> getGrupoServicio() {
		return grupoServicio;
	}

	public void setGrupoServicio(ArrayList<HashMap<String, Object>> grupoServicio) {
		this.grupoServicio = grupoServicio;
	}

	public ArrayList<HashMap<String, Object>> getEsquemasProcedimientos() {
		return esquemasProcedimientos;
	}

	public void setEsquemasProcedimientos(
			ArrayList<HashMap<String, Object>> esquemasProcedimientos) {
		this.esquemasProcedimientos = esquemasProcedimientos;
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap getInfoEntidadInv() {
		return infoEntidadInv;
	}

	@SuppressWarnings("rawtypes")
	public void setInfoEntidadInv(HashMap infoEntidadInv) {
		this.infoEntidadInv = infoEntidadInv;
	}

	@SuppressWarnings("rawtypes")
	public HashMap getInfoEntidadServ() {
		return infoEntidadServ;
	}

	@SuppressWarnings("rawtypes")
	public void setInfoEntidadServ(HashMap infoEntidadServ) {
		this.infoEntidadServ = infoEntidadServ;
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap getAuxEsquemasInv() {
		return auxEsquemasInv;
	}

	@SuppressWarnings("rawtypes")
	public void setAuxEsquemasInv(HashMap auxEsquemasInv) {
		this.auxEsquemasInv = auxEsquemasInv;
	}

	@SuppressWarnings("rawtypes")
	public HashMap getAuxEsquemasServ() {
		return auxEsquemasServ;
	}

	@SuppressWarnings("rawtypes")
	public void setAuxEsquemasServ(HashMap auxEsquemasServ) {
		this.auxEsquemasServ = auxEsquemasServ;
	}
	
	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	@SuppressWarnings("rawtypes")
	public HashMap getResultadosBusqueda() {
		return resultadosBusqueda;
	}

	@SuppressWarnings("rawtypes")
	public void setResultadosBusqueda(HashMap resultadosBusqueda) {
		this.resultadosBusqueda = resultadosBusqueda;
	}
	

	@SuppressWarnings("rawtypes")
	public HashMap getAuxModificacionServicios() {
		return auxModificacionServicios;
	}

	@SuppressWarnings("rawtypes")
	public void setAuxModificacionServicios(HashMap auxModificacionServicios) {
		this.auxModificacionServicios = auxModificacionServicios;
	}
	
	public int getIndiceMod() {
		return indiceMod;
	}

	public void setIndiceMod(int indiceMod) {
		this.indiceMod = indiceMod;
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap getAuxModificacionInventarios() {
		return auxModificacionInventarios;
	}

	public void setAuxModificacionInventarios(@SuppressWarnings("rawtypes") HashMap auxModificacionInventarios) {
		this.auxModificacionInventarios = auxModificacionInventarios;
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap getInfoEntidadInvAux() {
		return infoEntidadInvAux;
	}

	@SuppressWarnings("rawtypes")
	public void setInfoEntidadInvAux(HashMap infoEntidadInvAux) {
		this.infoEntidadInvAux = infoEntidadInvAux;
	}

	@SuppressWarnings("rawtypes")
	public HashMap getInfoEntidadServAux() {
		return infoEntidadServAux;
	}

	@SuppressWarnings("rawtypes")
	public void setInfoEntidadServAux(HashMap infoEntidadServAux) {
		this.infoEntidadServAux = infoEntidadServAux;
	}

	/**
	 * @return the filtros
	 */
	public Object getFiltros(String llave) {
		return filtros.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	public void setFiltros(String llave, Object obj) {
		this.filtros.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getAuxEsquemasInv(String llave) {
		return auxEsquemasInv.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setAuxEsquemasInv(String llave, Object obj) {
		this.auxEsquemasInv.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getAuxEsquemasServ(String llave) {
		return auxEsquemasServ.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setAuxEsquemasServ(String llave, Object obj) {
		this.auxEsquemasServ.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getInfoEntidadInv(String llave) {
		return infoEntidadInv.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setInfoEntidadInv(String llave, Object obj) {
		this.infoEntidadInv.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getInfoEntidadServ(String llave) {
		return infoEntidadServ.get(llave);
	}

	/**
	 * @param filtros the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setInfoEntidadServ(String llave, Object obj) {
		this.infoEntidadServ.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getResultadosBusqueda(String llave) {
		return resultadosBusqueda.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setResultadosBusqueda(String llave, Object obj) {
		this.resultadosBusqueda.put(llave, obj);
	}
	
	/**
	 * @return the auxModificacionServicios
	 */
	public Object getAuxModificacionServicios(String llave) {
		return auxModificacionServicios.get(llave);
	}

	/**
	 * @param auxModificacionServicios the auxModificacionServicios to set
	 */
	@SuppressWarnings("unchecked")
	public void setAuxModificacionServicios(String llave, Object obj) {
		this.auxModificacionServicios.put(llave, obj);
	}
	
	/**
	 * @return the auxModificacionInventarios
	 */
	public Object getAuxModificacionInventarios(String llave) {
		return auxModificacionInventarios.get(llave);
	}

	/**
	 * @param auxModificacionServicios the auxModificacionServicios to set
	 */
	@SuppressWarnings("unchecked")
	public void setAuxModificacionInventarios(String llave, Object obj) {
		this.auxModificacionInventarios.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getInfoEntidadInvAux(String llave) {
		return infoEntidadInvAux.get(llave);
	}

	/**
	 * @param glosaMap the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setInfoEntidadInvAux(String llave, Object obj) {
		this.infoEntidadInvAux.put(llave, obj);
	}
	
	/**
	 * @return the filtros
	 */
	public Object getInfoEntidadServAux(String llave) {
		return infoEntidadServAux.get(llave);
	}

	/**
	 * @param filtros the filtros to set
	 */
	@SuppressWarnings("unchecked")
	public void setInfoEntidadServAux(String llave, Object obj) {
		this.infoEntidadServAux.put(llave, obj);
	}
	

	@SuppressWarnings("rawtypes")
	public String getNombreInventario (String llave)
	{
		for(HashMap mapa:this.inventarios)
		{
			if(mapa.get("codigoInventario").toString().equals(llave))
				return mapa.get("nombreInventario").toString();
		}
		return "";
	}
	
	@SuppressWarnings("rawtypes")
	public String getNombreEsquemaInv (String llave)
	{
		for(HashMap mapa:this.esquemas)
		{
			if(mapa.get("codigoEsquema").toString().equals(llave))
				return mapa.get("nombreEsquema").toString();
		}
		return "";
	}
	
	@SuppressWarnings("rawtypes")
	public String getNombreServicio (String llave)
	{
		for(HashMap mapa:this.grupoServicio)
		{
			if(mapa.get("codigoServicio").toString().equals(llave))
				return mapa.get("descripcionServicio").toString();
		}
		return "";
	}
	
	@SuppressWarnings("rawtypes")
	public String getNombreEsquemaServ (String llave)
	{
		for(HashMap mapa:this.esquemasProcedimientos)
		{
			if(mapa.get("codigoEsquemaProc").toString().equals(llave))
				return mapa.get("nombreEsquemaProc").toString();
		}
		return "";
	}
	
	@SuppressWarnings("rawtypes")
	public String getNombreEntidad (String llave)
	{
		for(HashMap mapa:this.entidades)
		{
			if(mapa.get("codigoPkEntidad").toString().equals(llave))
				return mapa.get("razonSocial").toString();
		}
		return "";
	}

	/**
	 * @return the mensaje
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * @param mensaje the mensaje to set
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}
	/**
	 * 
	 * @return tipo tarifa
	 */
	public String getTipoTarifa() {
		return tipoTarifa;
	}
	/**
	 * 
	 * @param tipoTarifa
	 */
	public void setTipoTarifa(String tipoTarifa) {
		this.tipoTarifa = tipoTarifa;
	}
	/***
	 * 
	 * @param mostrarFiltro
	 */
	public void setMostrarFiltroTarifa(String mostrarFiltroTarifa) {
		this.mostrarFiltroTarifa = mostrarFiltroTarifa;
	}
	/***
	 * 
	 *@return filtro para mostrar esquemas tarifarios de acuerdo al tipo de tarifa 
	 */
	public String getMostrarFiltroTarifa() {
		return mostrarFiltroTarifa;
	}

	/**
	 * 
	 * @param mostrarFiltroTarifaConsulta
	 */
	public void setMostrarFiltroTarifaConsulta(
			String mostrarFiltroTarifaConsulta) {
		this.mostrarFiltroTarifaConsulta = mostrarFiltroTarifaConsulta;
	}

	/**
	 * 
	 * @return filtro para mostrar esquemas tarifarios de acuerdo al tipo tarifa
	 */
	public String getMostrarFiltroTarifaConsulta() {
		return mostrarFiltroTarifaConsulta;
	}
	
	/**
	 * 
	 * @param mostrarFiltroTarifaModifica
	 */
	public void setMostrarFiltroTarifaModifica(
			String mostrarFiltroTarifaModifica) {
		this.mostrarFiltroTarifaModifica = mostrarFiltroTarifaModifica;
	}

	/**
	 * 
	 * @return filtro para mostrar esquemas tarifarios de acuerdo al tipo tarifa
	 */
	public String getMostrarFiltroTarifaModifica() {
		return mostrarFiltroTarifaModifica;
	}

	/**
	 * 
	 * @param estadoAutoriz
	 */
	public void setEstadoAutoriz(String estadoAutoriz) {
		this.estadoAutoriz = estadoAutoriz;
	}

	/**
	 * 
	 * @return estado de la autorizacion de entidades sub
	 */
	public String getEstadoAutoriz() {
		return estadoAutoriz;
	}

	/**
	 *Este método retorna el valor de la variable puedeModificar
	 */
	public boolean isPuedeModificar() {
		return puedeModificar;
	}

	/**
	 *Este método asigna el valor de la variable puedeModificar
	 */
	public void setPuedeModificar(boolean puedeModificar) {
		this.puedeModificar = puedeModificar;
	}


	
}