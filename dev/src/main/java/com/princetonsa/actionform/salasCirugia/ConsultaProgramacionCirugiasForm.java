package com.princetonsa.actionform.salasCirugia;

import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;

/**
 * Clase para el manejo de la consulta de la programacion de cirugias
 * Date: 2008-06-03
 * @author garias@princetonsa.com
 */
public class ConsultaProgramacionCirugiasForm extends ValidatorForm 
{
	/**
	 * Estado de la funcionalidad
	 */
	private String estado;
	
	/**
	 * Indicador de posición mapa
	 */
	private int posMap; 
	
	/**
	 * Mapa con el listado de las cirugias consultadas
	 */
	private HashMap peticionesCirugiasMap;
	
	/**
	 * String Patron Ordenar 
	 * **/
	private String patronOrdenar;
	
	/**
	 * String Ultimo Patron de Ordenamiento
	 * */
	private String ultimoPatron;
	
	/**
	 * Mapa con el detalle de los servicios de una pretición
	 */
	private HashMap serviciosMap;
	
	/**
	 * Mapa con la información de los profesionales asociado a una petición
	 */
	private HashMap profesionalesMap;
	
	/**
	 * Mapa con la información de los pedidos de una peticion
	 */
	private HashMap pedidosMap;
	
	/**
	 * Numero de pedido
	 */
	private String pedido;
	
	/**
	 * Numero de peticion
	 */
	private String peticionqx;
	
	/**
	 * Mapa con la información de los pedidos de una peticion
	 */
	private HashMap articulosPedidoMap;
	
	/**
	 * Mapa con la información del ingreso asociado a una peticion
	 */
	private HashMap ingresoMap;
	
	/**
	 * Mapa con la información de los materiales especiales
	 */
	private HashMap materialesEspecialesMap;
	
	/**
	 * Listado de todos los centros de atención
	 */
	private ArrayList<HashMap<String,Object>> centrosAtencion;
	
	/**
	 * Listado de todas las salas existentes
	 */
	private ArrayList<HashMap<String,Object>> salas;
	
	/**
	 * Listado de todos los profesionales 
	 */
	private ArrayList<HashMap<String,Object>> profesionales;
	
	/**
	 * Listado de todos los tipos de anestesia
	 */
	private ArrayList<HashMap<String,Object>> tiposAnestesia;
	
	/**
	 * Usuarios activos del sistema
	 */
	private HashMap usuariosMap;
	
	/**
	 * Filtros seleccionado para la busqueda
	 */
	private HashMap filtrosMap;
	
	/**
	 * Forward
	 */
	private String forward;
	
	/**
	 *
	 */
	public void reset()
	{
		this.peticionesCirugiasMap=new HashMap();
		this.peticionesCirugiasMap.put("numRegistros", "0");
		this.serviciosMap=new HashMap();
		this.serviciosMap.put("numRegistros", "0");
		this.materialesEspecialesMap=new HashMap();
		this.materialesEspecialesMap.put("numRegistros", "0");
		this.ingresoMap=new HashMap();
		this.ingresoMap.put("numRegistros", "0");
		this.profesionalesMap=new HashMap();
		this.profesionalesMap.put("numRegistros", "0");
		this.profesionalesMap=new HashMap();
		this.profesionalesMap.put("numRegistros", "0");
		this.usuariosMap=new HashMap();
		this.usuariosMap.put("numRegistros", "0");
		this.pedidosMap=new HashMap();
		this.pedidosMap.put("numRegistros", "0");
		this.articulosPedidoMap=new HashMap();
		this.articulosPedidoMap.put("numRegistros", "0");
		this.filtrosMap=new HashMap();
		this.profesionales=new ArrayList();
		this.centrosAtencion=new ArrayList();
		this.tiposAnestesia=new ArrayList();
		this.salas=new ArrayList();
		this.patronOrdenar="";
		this.ultimoPatron="";
		this.posMap=ConstantesBD.codigoNuncaValido;
		this.pedido="";
		this.peticionqx="";
		this.forward="";
	}
	
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
        errores = super.validate(mapping,request);
        return errores;        
    }

	/**
	 * @return the estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * @param estado the estado to set
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	/**
	 * @return the posMap
	 */
	public int getPosMap() {
		return posMap;
	}

	/**
	 * @param posMap the posMap to set
	 */
	public void setPosMap(int posMap) {
		this.posMap = posMap;
	}

	/**
	 * @return the peticionesCirugiasMap
	 */
	public HashMap getPeticionesCirugiasMap() {
		return peticionesCirugiasMap;
	}

	/**
	 * @param peticionesCirugiasMap the peticionesCirugiasMap to set
	 */
	public void setPeticionesCirugiasMap(HashMap peticionesCirugiasMap) {
		this.peticionesCirugiasMap = peticionesCirugiasMap;
	}

	/**
	 * @return the peticionesCirugiasMap
	 */
	public Object getPeticionesCirugiasMap(String llave) {
		return peticionesCirugiasMap.get(llave);
	}

	/**
	 * @param peticionesCirugiasMap the peticionesCirugiasMap to set
	 */
	public void setPeticionesCirugiasMap(String llave, Object obj) {
		this.peticionesCirugiasMap.put(llave, obj);
	}

	/**
	 * @return the patronOrdenar
	 */
	public String getPatronOrdenar() {
		return patronOrdenar;
	}

	/**
	 * @param patronOrdenar the patronOrdenar to set
	 */
	public void setPatronOrdenar(String patronOrdenar) {
		this.patronOrdenar = patronOrdenar;
	}

	/**
	 * @return the ultimoPatron
	 */
	public String getUltimoPatron() {
		return ultimoPatron;
	}

	/**
	 * @param ultimoPatron the ultimoPatron to set
	 */
	public void setUltimoPatron(String ultimoPatron) {
		this.ultimoPatron = ultimoPatron;
	}

	/**
	 * @return the serviciosMap
	 */
	public HashMap getServiciosMap() {
		return serviciosMap;
	}

	/**
	 * @param serviciosMap the serviciosMap to set
	 */
	public void setServiciosMap(HashMap serviciosMap) {
		this.serviciosMap = serviciosMap;
	}
	/**
	 * @return the serviciosMap
	 */
	public Object getServiciosMap(String llave) {
		return serviciosMap.get(llave);
	}

	/**
	 * @param serviciosMap the serviciosMap to set
	 */
	public void setServiciosMap(String llave, Object obj) {
		this.serviciosMap.put(llave, obj);
	}

	/**
	 * @return the ingresoMap
	 */
	public Object getIngresoMap(String llave) {
		return ingresoMap.get(llave);
	}

	/**
	 * @param ingresoMap the ingresoMap to set
	 */
	public void setIngresoMap(HashMap ingresoMap) {
		this.ingresoMap = ingresoMap;
	}

	/**
	 * @return the materialesEspecialesMap
	 */
	public Object getMaterialesEspecialesMap(String llave) {
		return materialesEspecialesMap.get(llave);
	}

	/**
	 * @param materialesEspecialesMap the materialesEspecialesMap to set
	 */
	public void setMaterialesEspecialesMap(HashMap materialesEspecialesMap) {
		this.materialesEspecialesMap = materialesEspecialesMap;
	}

	/**
	 * @return the profesionalesMap
	 */
	public Object getProfesionalesMap(String llave) {
		return profesionalesMap.get(llave);
	}

	/**
	 * @param profesionalesMap the profesionalesMap to set
	 */
	public void setProfesionalesMap(HashMap profesionalesMap) {
		this.profesionalesMap = profesionalesMap;
	}
	
	/**
	 * @return the ingresoMap
	 */
	public HashMap getIngresoMap() {
		return ingresoMap;
	}

	/**
	 * @param ingresoMap the ingresoMap to set
	 */
	public void setIngresoMap(String llave, Object obj) {
		this.ingresoMap.put(llave, obj);
	}

	/**
	 * @return the materialesEspecialesMap
	 */
	public HashMap getMaterialesEspecialesMap() {
		return materialesEspecialesMap;
	}

	/**
	 * @param materialesEspecialesMap the materialesEspecialesMap to set
	 */
	public void setMaterialesEspecialesMap(String llave, Object obj) {
		this.materialesEspecialesMap.put(llave, obj);
	}

	/**
	 * @return the profesionalesMap
	 */
	public HashMap getProfesionalesMap() {
		return profesionalesMap;
	}

	/**
	 * @param profesionalesMap the profesionalesMap to set
	 */
	public void setProfesionalesMap(String llave, Object obj) {
		this.profesionalesMap.put(llave, obj);
	}

	/**
	 * @return the usuariosMap
	 */
	public HashMap getUsuariosMap() {
		return usuariosMap;
	}

	/**
	 * @param usuariosMap the usuariosMap to set
	 */
	public void setUsuariosMap(HashMap usuariosMap) {
		this.usuariosMap = usuariosMap;
	}

	/**
	 * @return the centrosAtencion
	 */
	public ArrayList<HashMap<String, Object>> getCentrosAtencion() {
		return centrosAtencion;
	}

	/**
	 * @param centrosAtencion the centrosAtencion to set
	 */
	public void setCentrosAtencion(
			ArrayList<HashMap<String, Object>> centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	/**
	 * @return the filtrosMap
	 */
	public HashMap getFiltrosMap() {
		return filtrosMap;
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(HashMap filtrosMap) {
		this.filtrosMap = filtrosMap;
	}
	
	/**
	 * @return the filtrosMap
	 */
	public Object getFiltrosMap(String llave) {
		return filtrosMap.get(llave);
	}

	/**
	 * @param filtrosMap the filtrosMap to set
	 */
	public void setFiltrosMap(String llave, Object obj) {
		this.filtrosMap.put(llave, obj);
	}

	/**
	 * @return the salas
	 */
	public ArrayList<HashMap<String, Object>> getSalas() {
		return salas;
	}

	/**
	 * @param salas the salas to set
	 */
	public void setSalas(ArrayList<HashMap<String, Object>> salas) {
		this.salas = salas;
	}

	/**
	 * @return the profesionales
	 */
	public ArrayList<HashMap<String, Object>> getProfesionales() {
		return profesionales;
	}

	/**
	 * @param profesionales the profesionales to set
	 */
	public void setProfesionales(ArrayList<HashMap<String, Object>> profesionales) {
		this.profesionales = profesionales;
	}

	/**
	 * @return the tiposAnestesia
	 */
	public ArrayList<HashMap<String, Object>> getTiposAnestesia() {
		return tiposAnestesia;
	}

	/**
	 * @param tiposAnestesia the tiposAnestesia to set
	 */
	public void setTiposAnestesia(ArrayList<HashMap<String, Object>> tiposAnestesia) {
		this.tiposAnestesia = tiposAnestesia;
	}
	
	/**
	 * @return
	 */
	public HashMap getPedidosMap() {
		return pedidosMap;
	}

	/**
	 * @param pedidosMap
	 */
	public void setPedidosMap(HashMap pedidosMap) {
		this.pedidosMap = pedidosMap;
	}
	
	/**
	 * @return
	 */
	public Object getPedidosMap(String llave) {
		return pedidosMap.get(llave);
	}

	/**
	 * @param pedidosMap
	 */
	public void setPedidosMap(String llave, Object obj) {
		this.pedidosMap.put(llave, obj);
	}

	/**
	 * 
	 * @return
	 */
	public HashMap getArticulosPedidoMap() {
		return articulosPedidoMap;
	}

	/**
	 * 
	 * @param articulosPedidoMap
	 */
	public void setArticulosPedidoMap(HashMap articulosPedidoMap) {
		this.articulosPedidoMap = articulosPedidoMap;
	}
	
	/**
	 * 
	 * @return
	 */
	public Object getArticulosPedidoMap(String llave) {
		return articulosPedidoMap.get(llave);
	}

	/**
	 * 
	 * @param articulosPedidoMap
	 */
	public void setArticulosPedidoMap(String llave, Object obj) {
		this.articulosPedidoMap.put(llave, obj);
	}

	/**
	 * 
	 * @return
	 */
	public String getPedido() {
		return pedido;
	}

	/**
	 * 
	 * @param pedido
	 */
	public void setPedido(String pedido) {
		this.pedido = pedido;
	}

	/**
	 * 
	 * @return
	 */
	public String getPeticionqx() {
		return peticionqx;
	}

	/**
	 * 
	 * @param peticionqx
	 */
	public void setPeticionqx(String peticionqx) {
		this.peticionqx = peticionqx;
	}

	/**
	 * 
	 * @return
	 */
	public String getForward() {
		return forward;
	}

	/**
	 * 
	 * @param forward
	 */
	public void setForward(String forward) {
		this.forward = forward;
	}
	
	
}	