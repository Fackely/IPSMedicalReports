/**
 * 
 */
package com.servinte.axioma.actionForm.capitacion;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.util.MessageResources;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import com.princetonsa.dto.manejoPaciente.DtoUsuariosCapitados;
import com.servinte.axioma.dto.administracion.TipoAfiliadoDto;
import com.servinte.axioma.dto.administracion.TipoIdentificacionDto;

/**
 * @author jeilones
 * @created 5/10/2012
 *
 */
public class ConsultaCargueUsuarioForm extends ValidatorForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8140295739849968531L;
	
	private static final String PROPERTIES_CARGUE_USUARIO="com.servinte.mensajes.capitacion.ConsultaCargueUsuarioForm";
	
	private static final String KEY_ERROR_REQUIRED="errors.required";
	
	private static final int CANTIDAD_MINIMA_CARACTERES=3;
	
	/**
	 * Tipos de identificacion del usuario
	 */
	private List<TipoIdentificacionDto>tiposIdentificacionDto=new ArrayList<TipoIdentificacionDto>(0);
	
	/**
	 * Tipos de afiliado de usuario
	 */
	private List<TipoAfiliadoDto>tiposAfiliadoDto=new ArrayList<TipoAfiliadoDto>(0);
	
	/**
	 * informacion de usuario capitado
	 */
	private DtoUsuariosCapitados filtroUsuarioCapitado=new DtoUsuariosCapitados();
	
	/**
	 * informacion de usuario capitado
	 */
	private DtoUsuariosCapitados usuarioCapitadoSeleccionado=new DtoUsuariosCapitados();
	
	/**
	 * Resultado de la consulta de usuarios capitados
	 */
	private List<DtoUsuariosCapitados> listaUsuariosCapitados=new ArrayList<DtoUsuariosCapitados>(0);
	
	/**
	 * Numero maximo de digitos para el numero de identificacion
	 */
	//private int maxDigitosNumIdentificacion=20;
	
	/**
	 * Atributo que representa el patron de ordenamiento
	 */
	private String patronOrdenar="";
	
	/**
	 * Atributo que representa si el ordenamiento es descendente
	 */
	private String esDescendente="";
	
	/**
	 * Atributo que representa el número de ordenes por autorizar
	 */
	private Integer numeroOrdenesPorAutorizar = 0;
	
	/**
	 * Atributo que representa el valor del parámetro correspondiente al número de registros por
	 * página
	 */
	private Integer numeroRegistrosPagina = 0 ;
	
	/**
	 * Posicion del usario capitado
	 */
	private String posicionUsuarioCapitado;
	
	private String estado="";
	/* (non-Javadoc)
	 * @see org.apache.struts.validator.ValidatorForm#validate(org.apache.struts.action.ActionMapping, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		ActionMessages errores=super.validate(mapping, request);
		if(filtroUsuarioCapitado.getTipoAfiliado()!=null&&filtroUsuarioCapitado.getTipoAfiliado()=='-'){
			filtroUsuarioCapitado.setTipoAfiliado(null);
		}
		int cantidadFiltros=0;
		
		MessageResources mensajes=MessageResources.getMessageResources(
				PROPERTIES_CARGUE_USUARIO);
		String nombresCamposSinMinimoCaracteres="";
		int cantidadCamposSinMinimoCaracteres=0;
		//Pattern pattern=null;
		//Matcher matcher=null;
		
		String coma=", ";
		
		if(this.filtroUsuarioCapitado.getTipoIdentificacion()!=null){
			if(!this.filtroUsuarioCapitado.getTipoIdentificacion().trim().isEmpty()
					&&!this.filtroUsuarioCapitado.getTipoIdentificacion().trim().equals(ConstantesBD.codigoNuncaValido+"")){
			
				cantidadFiltros++;
				
				if(this.filtroUsuarioCapitado.getNumeroIdentificacion()==null
						||this.filtroUsuarioCapitado.getNumeroIdentificacion().trim().isEmpty()){
					errores.add("", new ActionMessage(KEY_ERROR_REQUIRED, mensajes.getMessage("ConsultaCargueUsuario.numeroIdentificacion")));
				}else{
					cantidadFiltros++;
					if(this.filtroUsuarioCapitado.getNumeroIdentificacion().trim().length()<CANTIDAD_MINIMA_CARACTERES){
						cantidadCamposSinMinimoCaracteres++;
						nombresCamposSinMinimoCaracteres+=mensajes.getMessage("ConsultaCargueUsuario.numeroIdentificacion")+coma;
					}
					/*for(TipoIdentificacionDto tipoIdentificacionDto:tiposIdentificacionDto){
						if(tipoIdentificacionDto.getAcronimo().equals(this.filtroUsuarioCapitado.getTipoIdentificacion())){
							if(UtilidadTexto.getBoolean(tipoIdentificacionDto.getSoloNumeros())){
								pattern=Pattern.compile(ExpresionesRegulares.permitirSoloNumeros);
								matcher=pattern.matcher(this.filtroUsuarioCapitado.getNumeroIdentificacion());
								if(!matcher.matches()){
									errores.add("", new ActionMessage("errores.ConsultaCargueUsuario.soloNumeros"));
								}
							}else{
								pattern=Pattern.compile(ExpresionesRegulares.permitirNumerosLetrasGuion);
								matcher=pattern.matcher(this.filtroUsuarioCapitado.getNumeroIdentificacion());
								if(!matcher.matches()){
									errores.add("", new ActionMessage("errores.ConsultaCargueUsuario.numerosLetrasGuion"));
								}
							}
							break;
						}
					}*/
				}
			}else{
				if(this.filtroUsuarioCapitado.getNumeroIdentificacion()!=null&&
						!this.filtroUsuarioCapitado.getNumeroIdentificacion().trim().isEmpty()){
					cantidadFiltros++;
					if(this.filtroUsuarioCapitado.getNumeroIdentificacion().trim().length()<CANTIDAD_MINIMA_CARACTERES){
						cantidadCamposSinMinimoCaracteres++;
						nombresCamposSinMinimoCaracteres+=mensajes.getMessage("ConsultaCargueUsuario.numeroIdentificacion")+coma;
					}
					if(this.filtroUsuarioCapitado.getTipoIdentificacion()==null
							||this.filtroUsuarioCapitado.getTipoIdentificacion().trim().isEmpty()
							||this.filtroUsuarioCapitado.getTipoIdentificacion().trim().equals(ConstantesBD.codigoNuncaValido+"")){
						errores.add("", new ActionMessage(KEY_ERROR_REQUIRED, mensajes.getMessage("ConsultaCargueUsuario.tipoIdentificacion")));
					}
				}
			}
		}
		
		if(this.filtroUsuarioCapitado.getPrimerApellido()!=null
				&&!this.filtroUsuarioCapitado.getPrimerApellido().trim().isEmpty()){
			cantidadFiltros++;
			if(this.filtroUsuarioCapitado.getPrimerApellido().trim().length()<CANTIDAD_MINIMA_CARACTERES){
				cantidadCamposSinMinimoCaracteres++;
				nombresCamposSinMinimoCaracteres+=mensajes.getMessage("ConsultaCargueUsuario.primerApellido")+coma;
			}
		}
		
		if(this.filtroUsuarioCapitado.getSegundoApellido()!=null
				&&!this.filtroUsuarioCapitado.getSegundoApellido().trim().isEmpty()){
			cantidadFiltros++;
			if(this.filtroUsuarioCapitado.getSegundoApellido().trim().length()<CANTIDAD_MINIMA_CARACTERES){
				cantidadCamposSinMinimoCaracteres++;
				nombresCamposSinMinimoCaracteres+=mensajes.getMessage("ConsultaCargueUsuario.segundoApellido")+coma;
			}
		}
		
		if(this.filtroUsuarioCapitado.getPrimerNombre()!=null
				&&!this.filtroUsuarioCapitado.getPrimerNombre().trim().isEmpty()){
			cantidadFiltros++;
			if(this.filtroUsuarioCapitado.getPrimerNombre().trim().length()<CANTIDAD_MINIMA_CARACTERES){
				cantidadCamposSinMinimoCaracteres++;
				nombresCamposSinMinimoCaracteres+=mensajes.getMessage("ConsultaCargueUsuario.primerNombre")+coma;
			}
		}
		
		if(this.filtroUsuarioCapitado.getSegundoNombre()!=null
				&&!this.filtroUsuarioCapitado.getSegundoNombre().trim().isEmpty()){
			cantidadFiltros++;
			if(this.filtroUsuarioCapitado.getSegundoNombre().trim().length()<CANTIDAD_MINIMA_CARACTERES){
				cantidadCamposSinMinimoCaracteres++;
				nombresCamposSinMinimoCaracteres+=mensajes.getMessage("ConsultaCargueUsuario.segundoNombre")+coma;
			}
		}
		
		
		
		if(cantidadCamposSinMinimoCaracteres>0){
			nombresCamposSinMinimoCaracteres=nombresCamposSinMinimoCaracteres.replace(":", "");
			nombresCamposSinMinimoCaracteres=nombresCamposSinMinimoCaracteres.substring(0,nombresCamposSinMinimoCaracteres.length()-2);
			if(cantidadCamposSinMinimoCaracteres>1){
				errores.add("", new ActionMessage("errores.ConsultaCargueUsuario.minimoCaracteresPlural",
					nombresCamposSinMinimoCaracteres));
			}else{
				errores.add("", new ActionMessage("errores.ConsultaCargueUsuario.minimoCaracteresSingular",
						nombresCamposSinMinimoCaracteres));
			}
		}
		
		if(this.filtroUsuarioCapitado.getTipoAfiliado()!=null){
			cantidadFiltros++;
		}
		String estado=request.getParameter("estado");
		
		if(cantidadFiltros<2&&!estado.equals("filtrarConsultaCargueUsuario")){
			errores.add("", new ActionMessage("errores.ConsultaCargueUsuario.minimoFiltros"));
		}
		
		return (ActionErrors)errores;
	}
	
	/**
	 * @return the tiposIdentificacion
	 */
	public List<TipoIdentificacionDto> getTiposIdentificacionDto() {
		return tiposIdentificacionDto;
	}
	/**
	 * @param tiposIdentificacion the tiposIdentificacion to set
	 */
	public void setTiposIdentificacionDto(List<TipoIdentificacionDto> tiposIdentificacion) {
		this.tiposIdentificacionDto = tiposIdentificacion;
	}

	/**
	 * @return the tiposAfiliadoDto
	 */
	public List<TipoAfiliadoDto> getTiposAfiliadoDto() {
		return tiposAfiliadoDto;
	}

	/**
	 * @param tiposAfiliadoDto the tiposAfiliadoDto to set
	 */
	public void setTiposAfiliadoDto(List<TipoAfiliadoDto> tiposAfiliadoDto) {
		this.tiposAfiliadoDto = tiposAfiliadoDto;
	}

	/**
	 * @return the listaUsuariosCapitados
	 */
	public List<DtoUsuariosCapitados> getListaUsuariosCapitados() {
		return listaUsuariosCapitados;
	}

	/**
	 * @param listaUsuariosCapitados the listaUsuariosCapitados to set
	 */
	public void setListaUsuariosCapitados(
			List<DtoUsuariosCapitados> listaUsuariosCapitados) {
		this.listaUsuariosCapitados = listaUsuariosCapitados;
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
	 * @return the esDescendente
	 */
	public String getEsDescendente() {
		return esDescendente;
	}

	/**
	 * @param esDescendente the esDescendente to set
	 */
	public void setEsDescendente(String esDescendente) {
		this.esDescendente = esDescendente;
	}

	/**
	 * @return the numeroOrdenesPorAutorizar
	 */
	public Integer getNumeroOrdenesPorAutorizar() {
		return numeroOrdenesPorAutorizar;
	}

	/**
	 * @param numeroOrdenesPorAutorizar the numeroOrdenesPorAutorizar to set
	 */
	public void setNumeroOrdenesPorAutorizar(Integer numeroOrdenesPorAutorizar) {
		this.numeroOrdenesPorAutorizar = numeroOrdenesPorAutorizar;
	}

	/**
	 * @return the numeroRegistrosPagina
	 */
	public Integer getNumeroRegistrosPagina() {
		return numeroRegistrosPagina;
	}

	/**
	 * @param numeroRegistrosPagina the numeroRegistrosPagina to set
	 */
	public void setNumeroRegistrosPagina(Integer numeroRegistrosPagina) {
		this.numeroRegistrosPagina = numeroRegistrosPagina;
	}

	/**
	 * @return the posicionUsuarioCapitado
	 */
	public String getPosicionUsuarioCapitado() {
		return posicionUsuarioCapitado;
	}

	/**
	 * @param posicionUsuarioCapitado the posicionUsuarioCapitado to set
	 */
	public void setPosicionUsuarioCapitado(String posicionUsuarioCapitado) {
		this.posicionUsuarioCapitado = posicionUsuarioCapitado;
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
	 * @return the filtroUsuarioCapitado
	 */
	public DtoUsuariosCapitados getFiltroUsuarioCapitado() {
		return filtroUsuarioCapitado;
	}

	/**
	 * @param filtroUsuarioCapitado the filtroUsuarioCapitado to set
	 */
	public void setFiltroUsuarioCapitado(DtoUsuariosCapitados filtroUsuarioCapitado) {
		this.filtroUsuarioCapitado = filtroUsuarioCapitado;
	}

	/**
	 * @return the usuarioCapitadoSeleccionado
	 */
	public DtoUsuariosCapitados getUsuarioCapitadoSeleccionado() {
		return usuarioCapitadoSeleccionado;
	}

	/**
	 * @param usuarioCapitadoSeleccionado the usuarioCapitadoSeleccionado to set
	 */
	public void setUsuarioCapitadoSeleccionado(
			DtoUsuariosCapitados usuarioCapitadoSeleccionado) {
		this.usuarioCapitadoSeleccionado = usuarioCapitadoSeleccionado;
	}

}
