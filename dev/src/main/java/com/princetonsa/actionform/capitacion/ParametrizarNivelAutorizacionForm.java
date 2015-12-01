package com.princetonsa.actionform.capitacion;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.servinte.axioma.orm.ViasIngreso;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada propiedad respectiva.
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class ParametrizarNivelAutorizacionForm extends ActionForm {
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Atributo usado para mostrar los mensajes de �xito
	 */
	private String mensaje;
	
	/**
	 * Atributo usado para definir la acci�n que se est�
	 * realizando
	 */
	private String estado="";
	
	/**
	 * Atributo que almacena las vias de ingreso 
	 */
	private ArrayList<ViasIngreso> listaViasIngreso;
	
	/**
	 * Atribtuo que almacena los tipos de autorizacion
	 */
	private ArrayList<DtoIntegridadDominio> listaTiposAutorizacion;
	
	/**
	 * Atributo que almacena el �ndice del registro seleccionado
	 */
	private int indice;
	
	/**
	 * Atributo que almacena los niveles de autorizaci�n
	 */
	private ArrayList<DTONivelAutorizacion> listaNivelAutorizacion;
	
	/**
	 * Atributo que corresponde al parametro numero de lineas pager
	 */
	private Integer numeroRegistrosPagina=0;
	/**
	 * Atributo que corresponde numero total de registros
	 */
	private Integer numeroTotalRegistros=0;
	
	/**
	 * 
	 * Este m�todo se encarga de inicializar los valores de la 
	 * p�gina de parametrizaci�n de los niveles de autorizaci�n
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void reset(){	
		listaNivelAutorizacion = new ArrayList<DTONivelAutorizacion>();
		listaViasIngreso= new ArrayList<ViasIngreso>();
		estado="";
		mensaje="";
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
		ActionErrors errores=null;
		errores=new ActionErrors();
		MessageResources mensajes=MessageResources.getMessageResources(
				"com.servinte.mensajes.capitacion.ParametrizarNivelAutorizacionForm");
		if(estado.equals("guardar")){
			if(listaNivelAutorizacion!=null && listaNivelAutorizacion.size()>0){
				int indice=1;
				for(DTONivelAutorizacion registro : listaNivelAutorizacion){					
					if(registro.getDescripcion()==null || registro.getDescripcion().equals("")){
						errores.add("descripcion requerida", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarNivelAutorizacionForm.descripcionRequerida",indice)));
						
					}
					if(registro.getTipoAutorizacionAcronimo()==null || registro.getTipoAutorizacionAcronimo().equals("")){
						errores.add("tipo Autorizacion requerida", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarNivelAutorizacionForm.tipoAutorizacionRequerida",indice)));
					}
					if(registro.getViasIngresoPK()==ConstantesBD.codigoNuncaValido){
						errores.add("via ingreso requerida", new ActionMessage("errors.notEspecific", 
								mensajes.getMessage("parametrizarNivelAutorizacionForm.viaIngresoRequerida",indice)));
					}
					indice++;
				}
				
			}
			
		}
		return errores;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo estado
	
	 * @return retorna la variable estado 
	 * @author Angela Maria Aguirre 
	 */
	public String getEstado() {
		return estado;
	}


	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo estado
	
	 * @param valor para el atributo estado 
	 * @author Angela Maria Aguirre 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}


	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaViasIngreso
	
	 * @return retorna la variable listaViasIngreso 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<ViasIngreso> getListaViasIngreso() {
		return listaViasIngreso;
	}


	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaViasIngreso
	
	 * @param valor para el atributo listaViasIngreso 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaViasIngreso(ArrayList<ViasIngreso> listaViasIngreso) {
		this.listaViasIngreso = listaViasIngreso;
	}


	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaTiposAutorizacion
	
	 * @return retorna la variable listaTiposAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoIntegridadDominio> getListaTiposAutorizacion() {
		return listaTiposAutorizacion;
	}


	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaTiposAutorizacion
	
	 * @param valor para el atributo listaTiposAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaTiposAutorizacion(
			ArrayList<DtoIntegridadDominio> listaTiposAutorizacion) {
		this.listaTiposAutorizacion = listaTiposAutorizacion;
	}


	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo mensaje
	
	 * @return retorna la variable mensaje 
	 * @author Angela Maria Aguirre 
	 */
	public String getMensaje() {
		return mensaje;
	}


	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo mensaje
	
	 * @param valor para el atributo mensaje 
	 * @author Angela Maria Aguirre 
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}


	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo indice
	
	 * @return retorna la variable indice 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndice() {
		return indice;
	}


	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo indice
	
	 * @param valor para el atributo indice 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}


	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaNivelAutorizacion
	
	 * @return retorna la variable listaNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTONivelAutorizacion> getListaNivelAutorizacion() {
		return listaNivelAutorizacion;
	}


	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaNivelAutorizacion
	
	 * @param valor para el atributo listaNivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaNivelAutorizacion(
			ArrayList<DTONivelAutorizacion> listaNivelAutorizacion) {
		this.listaNivelAutorizacion = listaNivelAutorizacion;
	}	


	public Integer getNumeroRegistrosPagina() {
		return numeroRegistrosPagina;
	}


	public void setNumeroRegistrosPagina(Integer numeroRegistrosPagina) {
		this.numeroRegistrosPagina = numeroRegistrosPagina;
	}


	public Integer getNumeroTotalRegistros() {
		return numeroTotalRegistros;
	}


	public void setNumeroTotalRegistros(Integer numeroTotalRegistros) {
		this.numeroTotalRegistros = numeroTotalRegistros;
	}	
	
	
}
