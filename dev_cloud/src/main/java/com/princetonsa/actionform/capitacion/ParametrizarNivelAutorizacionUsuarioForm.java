package com.princetonsa.actionform.capitacion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionUsuario;
import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionOcupacionMedica;
import com.princetonsa.dto.capitacion.DTONivelAutorizacionUsuarioEspecifico;
import com.princetonsa.dto.comun.DtoCheckBox;
import com.princetonsa.dto.tesoreria.DtoUsuarioPersona;
import com.servinte.axioma.orm.OcupacionesMedicas;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada propiedad respectiva.
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class ParametrizarNivelAutorizacionUsuarioForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo usado para mostrar los mensajes de éxito
	 */
	private String mensaje;
	
	/**
	 * Atributo usado para definir la acción que se está
	 * realizando
	 */
	private String estado="";
	
	/**
	 * Atributo que almacena los niveles de autorización
	 */
	private ArrayList<DTONivelAutorizacion> listaNivelAutorizacion;
	
	/**
	 * 
	 */
	private List<OcupacionesMedicas> listaOcupacionMedica;
	
	/**
	 * 
	 */
	private ArrayList<DtoUsuarioPersona> listaUsuario;
	
	/**
	 * 
	 */
	private int nivelAutorizacionSeleccionadaID;
	
	
	private DTONivelAutorizacion nivelAutorizacionSeleccionada;
	
	/**
	 * 
	 */
	private int indice;
	/**
	 * 
	 */
	private ArrayList<DTONivelAutorizacionUsuarioEspecifico> listaNivelUsuarioEsp;
	
	/**
	 * 
	 */
	private ArrayList<DTONivelAutorizacionOcupacionMedica> listaNivelOcupacionMedica;
	
	/**
	 * 
	 */
	private ArrayList<DtoCheckBox> listaPrioridad;
	
	/**
	 * 
	 */
	private DTOBusquedaNivelAutorizacionUsuario nivelAutorizacionUsuario;
	

	/**
	 * 
	 * Este método se encarga de inicializar los valores de la 
	 * página de parametrización de los niveles de autorización
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void reset(){	
		listaNivelAutorizacion = new ArrayList<DTONivelAutorizacion>();	
		nivelAutorizacionSeleccionadaID = -1;
		estado="";
		mensaje="";
		indice=ConstantesBD.codigoNuncaValido;
		nivelAutorizacionSeleccionada= null;
		listaNivelOcupacionMedica = new ArrayList<DTONivelAutorizacionOcupacionMedica>();
		listaNivelUsuarioEsp = new ArrayList<DTONivelAutorizacionUsuarioEspecifico>();
		nivelAutorizacionUsuario = new DTOBusquedaNivelAutorizacionUsuario();
		listaPrioridad = new ArrayList<DtoCheckBox>();
	}
	
	
	/**
	 * 
	 * Este m&eacute;todo se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
		ActionErrors errores=new ActionErrors();
		MessageResources mensajes=MessageResources.getMessageResources(
			"com.servinte.mensajes.capitacion.ParametrizarNivelAutorizacionPorUsuarioForm");		
		if(estado.equals("buscarNivelesAutorizacionUsuario")){
			if(nivelAutorizacionSeleccionadaID==ConstantesBD.codigoNuncaValido){
				errores.add("El nivel de autorización es requerido", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("parametrizarNivelAutorizacionPorUsuarioForm.nivelAutoizacionRequerida")));
			}
		}
		return errores;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo mensaje
	
	 * @return retorna la variable mensaje 
	 * @author Angela Maria Aguirre 
	 */
	public String getMensaje() {
		return mensaje;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo mensaje
	
	 * @param valor para el atributo mensaje 
	 * @author Angela Maria Aguirre 
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaNivelAutorizacion
	
	 * @return retorna la variable listaNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTONivelAutorizacion> getListaNivelAutorizacion() {
		return listaNivelAutorizacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaNivelAutorizacion
	
	 * @param valor para el atributo listaNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaNivelAutorizacion(
			ArrayList<DTONivelAutorizacion> listaNivelAutorizacion) {
		this.listaNivelAutorizacion = listaNivelAutorizacion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaOcupacionMedica
	
	 * @return retorna la variable listaOcupacionMedica 
	 * @author Angela Maria Aguirre 
	 */
	public List<OcupacionesMedicas> getListaOcupacionMedica() {
		return listaOcupacionMedica;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaOcupacionMedica
	
	 * @param valor para el atributo listaOcupacionMedica 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaOcupacionMedica(
			List<OcupacionesMedicas> listaOcupacionMedica) {
		this.listaOcupacionMedica = listaOcupacionMedica;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaUsuario
	
	 * @return retorna la variable listaUsuario 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoUsuarioPersona> getListaUsuario() {
		return listaUsuario;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaUsuario
	
	 * @param valor para el atributo listaUsuario 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaUsuario(ArrayList<DtoUsuarioPersona> listaUsuario) {
		this.listaUsuario = listaUsuario;
	}
	
	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo nivelAutorizacionSeleccionadaID
	 *
	 * @author Angela Aguirre 
	 */
	public int getNivelAutorizacionSeleccionadaID() {
		return nivelAutorizacionSeleccionadaID;
	}


	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo nivelAutorizacionSeleccionadaID
	 *
	 * @author Angela Aguirre 
	 */
	public void setNivelAutorizacionSeleccionadaID(
			int nivelAutorizacionSeleccionadaID) {
		this.nivelAutorizacionSeleccionadaID = nivelAutorizacionSeleccionadaID;
	}

	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo indice
	 *
	 * @author Angela Aguirre 
	 */
	public int getIndice() {
		return indice;
	}


	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo indice
	 *
	 * @author Angela Aguirre 
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}


	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo listaNivelUsuarioEsp
	 *
	 * @author Angela Aguirre 
	 */
	public ArrayList<DTONivelAutorizacionUsuarioEspecifico> getListaNivelUsuarioEsp() {
		return listaNivelUsuarioEsp;
	}


	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo listaNivelUsuarioEsp
	 *
	 * @author Angela Aguirre 
	 */
	public void setListaNivelUsuarioEsp(
			ArrayList<DTONivelAutorizacionUsuarioEspecifico> listaNivelUsuarioEsp) {
		this.listaNivelUsuarioEsp = listaNivelUsuarioEsp;
	}


	/**
	 * Este método se encarga de obtener el 
	 * valor del atributo listaNivelOcupacionMedica
	 *
	 * @author Angela Aguirre 
	 */
	public ArrayList<DTONivelAutorizacionOcupacionMedica> getListaNivelOcupacionMedica() {
		return listaNivelOcupacionMedica;
	}


	/**
	 * Este método se encarga de asignar el 
	 * valor del atributo listaNivelOcupacionMedica
	 *
	 * @author Angela Aguirre 
	 */
	public void setListaNivelOcupacionMedica(
			ArrayList<DTONivelAutorizacionOcupacionMedica> listaNivelOcupacionMedica) {
		this.listaNivelOcupacionMedica = listaNivelOcupacionMedica;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nivelAutorizacionSeleccionada
	
	 * @return retorna la variable nivelAutorizacionSeleccionada 
	 * @author Angela Maria Aguirre 
	 */
	public DTONivelAutorizacion getNivelAutorizacionSeleccionada() {
		return nivelAutorizacionSeleccionada;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nivelAutorizacionSeleccionada
	
	 * @param valor para el atributo nivelAutorizacionSeleccionada 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutorizacionSeleccionada(
			DTONivelAutorizacion nivelAutorizacionSeleccionada) {
		this.nivelAutorizacionSeleccionada = nivelAutorizacionSeleccionada;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaPrioridad
	
	 * @return retorna la variable listaPrioridad 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoCheckBox> getListaPrioridad() {
		return listaPrioridad;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaPrioridad
	
	 * @param valor para el atributo listaPrioridad 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaPrioridad(ArrayList<DtoCheckBox> listaPrioridad) {
		this.listaPrioridad = listaPrioridad;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nivelAutorizacionUsuario
	
	 * @return retorna la variable nivelAutorizacionUsuario 
	 * @author Angela Maria Aguirre 
	 */
	public DTOBusquedaNivelAutorizacionUsuario getNivelAutorizacionUsuario() {
		return nivelAutorizacionUsuario;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nivelAutorizacionUsuario
	
	 * @param valor para el atributo nivelAutorizacionUsuario 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutorizacionUsuario(
			DTOBusquedaNivelAutorizacionUsuario nivelAutorizacionUsuario) {
		this.nivelAutorizacionUsuario = nivelAutorizacionUsuario;
	}	

}
