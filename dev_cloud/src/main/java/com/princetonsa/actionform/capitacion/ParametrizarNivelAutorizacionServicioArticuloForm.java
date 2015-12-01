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

import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionArticulo;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorAgrupacionServicio;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorArticuloEspecifico;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorServicioEspecifico;
import com.princetonsa.dto.capitacion.DTOBusquedaNivelAutorizacionServicioArticulo;
import com.princetonsa.dto.capitacion.DTONivelAutorizacion;
import com.servinte.axioma.dto.administracion.EspecialidadDto;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.NaturalezaArticulo;
import com.servinte.axioma.orm.SubgrupoInventario;
import com.servinte.axioma.orm.TiposServicio;

/**
 * Esta clase se encarga de obtener los datos ingresados
 * por el usuario y mapearlos a los atributos asignados a
 * cada propiedad respectiva.
 * 
 * @author Angela Maria Aguirre
 * @since 20/09/2010
 */
public class ParametrizarNivelAutorizacionServicioArticuloForm extends ActionForm {
		
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8453022898954539216L;

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
	 * Atributo que almacena el índice del registro seleccionado
	 */
	private int indice;
	
	/**
	 * Atributo que almacena los niveles de autorización
	 */
	private ArrayList<DTONivelAutorizacion> listaNivelAutorizacion;
	
	/**
	 * Atributo que almacena los grupos de servicios
	 */
	private ArrayList<GruposServicios> listaGruposServicios;
	
	/**
	 * Atributo que almacena los tipos de servicios
	 */
	private ArrayList<TiposServicio> listaTiposServicio;
	
	/**
	 * Atributo que almacena las especialidades
	 */
	private List<EspecialidadDto> listaEspecialidades;
	
	private ArrayList<ClaseInventario> listaClaseInventario;
	
	private ArrayList<GrupoInventario> listaGrupoInventario;
	
	private ArrayList<SubgrupoInventario> listaSubgrupoInventario;
	
	private ArrayList<NaturalezaArticulo> listaNaturalezaArticulo;
	
	private DTOBusquedaNivelAutorizacionServicioArticulo nivelAtuorizacionServArtSeleccionado;
	
	private ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> listaAgrArticulo;
	private ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> listaAgrServicio;
	private ArrayList<DTOBusquedaNivelAutorServicioEspecifico> listaServicioEsp;
	private ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> listaArticuloEsp;
	private DTONivelAutorizacion nivelAutorizacion;
	/**
	 * codigos de los servicios insertados para no repetirlos
	 * en la busqueda avanzada de servicios
	 */
	private String codigosServiciosInsertados;
	
	/**
	 * codigos de los artículos insertados para no repetirlos
	 * en la busqueda avanzada de artículos
	 */
	private String codigosArticulosInsertados;
	
	/**
	 * 
	 */
	private int codigoServicio;
	
	/**
	 * 
	 */
	private String nombreServicio;
	
	/**
	 * 
	 */
	private int codigoArticulo;
	
	/**
	 * 
	 */
	private String descripcionArticulo;
	
	/**
	 * 
	 * Este método se encarga de inicializar los valores de la 
	 * página de parametrización de los niveles de autorización
	 * por serivicios y artículos
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void reset(){	
		listaNivelAutorizacion = new ArrayList<DTONivelAutorizacion>();
		listaEspecialidades = new ArrayList<EspecialidadDto>();
		listaClaseInventario = new ArrayList<ClaseInventario>();
		listaGrupoInventario = new ArrayList<GrupoInventario>();
		listaGruposServicios = new ArrayList<GruposServicios>();
		listaTiposServicio = new ArrayList<TiposServicio>();
		listaSubgrupoInventario = new ArrayList<SubgrupoInventario>();
		listaNaturalezaArticulo = new ArrayList<NaturalezaArticulo>();
		nivelAtuorizacionServArtSeleccionado = new DTOBusquedaNivelAutorizacionServicioArticulo();
		listaAgrArticulo = new ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo>();
		listaAgrServicio = new ArrayList<DTOBusquedaNivelAutorAgrupacionServicio>();
		listaServicioEsp = new ArrayList<DTOBusquedaNivelAutorServicioEspecifico>();
		listaArticuloEsp = new ArrayList<DTOBusquedaNivelAutorArticuloEspecifico>();
		estado="";
		mensaje="";
		indice =0;
		nivelAutorizacion = new DTONivelAutorizacion();
		
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
		"com.servinte.mensajes.capitacion.ParametrizarNivelAutorizacionServicioArticuloForm");		
		if(estado.equals("buscar")){
			if(this.indice==ConstantesBD.codigoNuncaValido){
				errores.add("El nivel de autorización es requerido", new ActionMessage("errors.notEspecific", 
						mensajes.getMessage("parametrizarNivelAutorizacionServicioArticuloForm.nivelAutorizacionRequerida")));
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
	 * del atributo indice
	
	 * @return retorna la variable indice 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndice() {
		return indice;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo indice
	
	 * @param valor para el atributo indice 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndice(int indice) {
		this.indice = indice;
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
	 * del atributo listaGruposServicios
	
	 * @return retorna la variable listaGruposServicios 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<GruposServicios> getListaGruposServicios() {
		return listaGruposServicios;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaGruposServicios
	
	 * @param valor para el atributo listaGruposServicios 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaGruposServicios(
			ArrayList<GruposServicios> listaGruposServicios) {
		this.listaGruposServicios = listaGruposServicios;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaTiposServicio
	
	 * @return retorna la variable listaTiposServicio 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<TiposServicio> getListaTiposServicio() {
		return listaTiposServicio;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaTiposServicio
	
	 * @param valor para el atributo listaTiposServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaTiposServicio(ArrayList<TiposServicio> listaTiposServicio) {
		this.listaTiposServicio = listaTiposServicio;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaEspecialidades
	
	 * @return retorna la variable listaEspecialidades 
	 * @author Angela Maria Aguirre 
	 */
	public List<EspecialidadDto> getListaEspecialidades() {
		return listaEspecialidades;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaEspecialidades
	
	 * @param valor para el atributo listaEspecialidades 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaEspecialidades(List<EspecialidadDto> listaEspecialidades) {
		this.listaEspecialidades = listaEspecialidades;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaClaseInventario
	
	 * @return retorna la variable listaClaseInventario 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<ClaseInventario> getListaClaseInventario() {
		return listaClaseInventario;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaClaseInventario
	
	 * @param valor para el atributo listaClaseInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaClaseInventario(
			ArrayList<ClaseInventario> listaClaseInventario) {
		this.listaClaseInventario = listaClaseInventario;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaGrupoInventario
	
	 * @return retorna la variable listaGrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<GrupoInventario> getListaGrupoInventario() {
		return listaGrupoInventario;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaGrupoInventario
	
	 * @param valor para el atributo listaGrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaGrupoInventario(
			ArrayList<GrupoInventario> listaGrupoInventario) {
		this.listaGrupoInventario = listaGrupoInventario;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaSubgrupoInventario
	
	 * @return retorna la variable listaSubgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<SubgrupoInventario> getListaSubgrupoInventario() {
		return listaSubgrupoInventario;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaSubgrupoInventario
	
	 * @param valor para el atributo listaSubgrupoInventario 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaSubgrupoInventario(
			ArrayList<SubgrupoInventario> listaSubgrupoInventario) {
		this.listaSubgrupoInventario = listaSubgrupoInventario;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaNaturalezaArticulo
	
	 * @return retorna la variable listaNaturalezaArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<NaturalezaArticulo> getListaNaturalezaArticulo() {
		return listaNaturalezaArticulo;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaNaturalezaArticulo
	
	 * @param valor para el atributo listaNaturalezaArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaNaturalezaArticulo(
			ArrayList<NaturalezaArticulo> listaNaturalezaArticulo) {
		this.listaNaturalezaArticulo = listaNaturalezaArticulo;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nivelAtuorizacionServArtSeleccionado
	
	 * @return retorna la variable nivelAtuorizacionServArtSeleccionado 
	 * @author Angela Maria Aguirre 
	 */
	public DTOBusquedaNivelAutorizacionServicioArticulo getNivelAtuorizacionServArtSeleccionado() {
		return nivelAtuorizacionServArtSeleccionado;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nivelAtuorizacionServArtSeleccionado
	
	 * @param valor para el atributo nivelAtuorizacionServArtSeleccionado 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAtuorizacionServArtSeleccionado(
			DTOBusquedaNivelAutorizacionServicioArticulo nivelAtuorizacionServArtSeleccionado) {
		this.nivelAtuorizacionServArtSeleccionado = nivelAtuorizacionServArtSeleccionado;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaAgrArticulo
	
	 * @return retorna la variable listaAgrArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> getListaAgrArticulo() {
		return listaAgrArticulo;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaAgrArticulo
	
	 * @param valor para el atributo listaAgrArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaAgrArticulo(
			ArrayList<DTOBusquedaNivelAutorAgrupacionArticulo> listaAgrArticulo) {
		this.listaAgrArticulo = listaAgrArticulo;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaAgrServicio
	
	 * @return retorna la variable listaAgrServicio 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> getListaAgrServicio() {
		return listaAgrServicio;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaAgrServicio
	
	 * @param valor para el atributo listaAgrServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaAgrServicio(
			ArrayList<DTOBusquedaNivelAutorAgrupacionServicio> listaAgrServicio) {
		this.listaAgrServicio = listaAgrServicio;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaServicioEsp
	
	 * @return retorna la variable listaServicioEsp 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaNivelAutorServicioEspecifico> getListaServicioEsp() {
		return listaServicioEsp;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaServicioEsp
	
	 * @param valor para el atributo listaServicioEsp 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaServicioEsp(
			ArrayList<DTOBusquedaNivelAutorServicioEspecifico> listaServicioEsp) {
		this.listaServicioEsp = listaServicioEsp;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo listaArticuloEsp
	
	 * @return retorna la variable listaArticuloEsp 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> getListaArticuloEsp() {
		return listaArticuloEsp;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo listaArticuloEsp
	
	 * @param valor para el atributo listaArticuloEsp 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaArticuloEsp(
			ArrayList<DTOBusquedaNivelAutorArticuloEspecifico> listaArticuloEsp) {
		this.listaArticuloEsp = listaArticuloEsp;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigosServiciosInsertados
	
	 * @return retorna la variable codigosServiciosInsertados 
	 * @author Angela Maria Aguirre 
	 */
	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigosServiciosInsertados
	
	 * @param valor para el atributo codigosServiciosInsertados 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}

	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoServicio
	
	 * @return retorna la variable codigoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoServicio
	
	 * @param valor para el atributo codigoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nombreServicio
	
	 * @return retorna la variable nombreServicio 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nombreServicio
	
	 * @param valor para el atributo nombreServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigoArticulo
	
	 * @return retorna la variable codigoArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigoArticulo
	
	 * @param valor para el atributo codigoArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo descripcionArticulo
	
	 * @return retorna la variable descripcionArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo descripcionArticulo
	
	 * @param valor para el atributo descripcionArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo nivelAutorizacion
	
	 * @return retorna la variable nivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public DTONivelAutorizacion getNivelAutorizacion() {
		return nivelAutorizacion;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo nivelAutorizacion
	
	 * @param valor para el atributo nivelAutorizacion 
	 * @author Angela Maria Aguirre 
	 */
	public void setNivelAutorizacion(DTONivelAutorizacion nivelAutorizacion) {
		this.nivelAutorizacion = nivelAutorizacion;
	}


	/**
	 * Este Método se encarga de obtener el valor 
	 * del atributo codigosArticulosInsertados
	
	 * @return retorna la variable codigosArticulosInsertados 
	 * @author Angela Maria Aguirre 
	 */
	public String getCodigosArticulosInsertados() {
		return codigosArticulosInsertados;
	}


	/**
	 * Este Método se encarga de establecer el valor 
	 * del atributo codigosArticulosInsertados
	
	 * @param valor para el atributo codigosArticulosInsertados 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigosArticulosInsertados(String codigosArticulosInsertados) {
		this.codigosArticulosInsertados = codigosArticulosInsertados;
	}


}
