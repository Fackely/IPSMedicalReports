package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;

import util.ConstantesBD;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.facturacion.DTOMontosCobro;
import com.princetonsa.dto.facturacion.DTOResultadoBusquedaDetalleMontos;
import com.princetonsa.dto.facturacion.DtoConvenio;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.Especialidades;
import com.servinte.axioma.orm.EstratosSociales;
import com.servinte.axioma.orm.ExcepcionesNaturaleza;
import com.servinte.axioma.orm.GrupoInventario;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.NaturalezaArticulo;
import com.servinte.axioma.orm.SubgrupoInventario;
import com.servinte.axioma.orm.TiposAfiliado;
import com.servinte.axioma.orm.TiposMonto;
import com.servinte.axioma.orm.TiposPaciente;
import com.servinte.axioma.orm.TiposServicio;
import com.servinte.axioma.orm.ViasIngreso;

/**
 * Esta clase se encarga de inicializar los valores de las
 * p�ginas usadas para el ingreso y modificaci�n de los 
 * montos de cobro y sus detalles
 * 
 * @author Angela Maria Aguirre
 * @since 27/08/2010
 */
public class IngresarModificarMontosCobroForm extends ActionForm {
	
	
	
	private static final long serialVersionUID = 1L;

	private String estado="";
	
	/**
	 * Atributo que almacena los convenios activos y que manejan montos
	 */
	private ArrayList<DtoConvenio> listaConvenios;
		
	/**
	 * Atributo usado para almacenar el listado con los tipos de paciente
	 */
	private ArrayList<TiposPaciente> listadoTipoPaciente;
	
	/**
	 * Atributo usado para almacenar el listado con los tipos de paciente
	 */
	private ArrayList<TiposAfiliado> listadoTipoAfiliado;
	
	/**
	 * Atributo usado para almacenar el listado con los estratos sociales 
	 */
	private ArrayList<EstratosSociales> listadoEstratoSocial;
	
	/**
	 * Atributo que almacena el convenio y la fecha de vigencia 
	 * seleccionada por el usuario
	 */
	private DTOMontosCobro dtoConvenioSeleccionado;
	
	/**
	 * Atributo que almacena los montos asociados al convenio
	 * seleccionado
	 */
	private ArrayList<DTOMontosCobro> listaMontosCobro;
	
	/**
	 * Atributo que almacena el tama�o inicial de la 
	 * lista que contiene los registros de los montos de cobro
	 */
	private int longitudListaInical;
	
	/**
	 * Atributo que almacena las excepciones de las naturalezas de 
	 * paciente asociadas al r�gimen del convenio seleccionado por el usuario 
	 */
	private ArrayList<ExcepcionesNaturaleza> listaExcepcionesNaturaleza;
	
	/**
	 * Atributo que almacena los tipos de detalle de montos de cobro 
	 */
	private ArrayList<DtoIntegridadDominio> listaTiposDetalleMonto;
	
	private int indiceDetalle;
	
	private int indiceTipoPaciente;
	
	private int indiceViaIngreso;
	
	private ArrayList<ViasIngreso> listadoViasIngreso; 
	
	/**
	 * Atributo que almacena los tipos de montos registrados.
	 */
	private ArrayList<TiposMonto> listaTiposMonto;	
	
	/**
	 * Atributo usado para mostrar los mensajes de &eacute;xito.
	 */
	private String mostrarMensaje;
	
	
	private DTOResultadoBusquedaDetalleMontos detalleSeleccionado;
	
	
	/**
	 * codigos de los servicios insertados para no repetirlos
	 * en la busqueda avanzada de servicios
	 */
	private String codigosServiciosInsertados;
	
	/**
	 * codigos de los art�culos insertados para no repetirlos
	 * en la busqueda avanzada de art�culos
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
	private ArrayList<Especialidades> listaEspecialidades;
	
	private ArrayList<ClaseInventario> listaClaseInventario;
	
	private ArrayList<GrupoInventario> listaGrupoInventario;
	
	private ArrayList<SubgrupoInventario> listaSubgrupoInventario;
	
	private ArrayList<NaturalezaArticulo> listaNaturalezaArticulo;
	
	private Date fechaConvenioPostulada;
	
	private boolean mostrarBotonDetalle;
	
	private String listarAutomatico;
	
	public IngresarModificarMontosCobroForm(){
		
	}
	
	public IngresarModificarMontosCobroForm(int indiceViaIngreso, int indiceTipopaciente){
		this.indiceViaIngreso=indiceViaIngreso;
		this.indiceTipoPaciente=indiceTipopaciente;
	}
	
	
	/**
	 * 
	 * Este m�todo se encarga de inicializar los valores de la 
	 * p�gina de consulta de convenios de montos de cobro
	 * 
	 * @author, Angela Maria Aguirre
	 *
	 */
	public void reset(){		
		dtoConvenioSeleccionado= new DTOMontosCobro();
		dtoConvenioSeleccionado.setConvenio(new DtoConvenio());
		listaMontosCobro=null;
		this.codigoServicio=ConstantesBD.codigoNuncaValido;
		this.nombreServicio="";		
		this.codigoArticulo=ConstantesBD.codigoNuncaValido;
		this.descripcionArticulo="";		
		this.detalleSeleccionado = new DTOResultadoBusquedaDetalleMontos();
		this.codigosServiciosInsertados="";
		this.codigosArticulosInsertados="";	
		this.mostrarBotonDetalle=false;
		this.listadoViasIngreso=new ArrayList<ViasIngreso>();
		this.listadoTipoPaciente= new ArrayList<TiposPaciente>();
		this.indiceViaIngreso=0;
		this.indiceTipoPaciente=0;
		this.fechaConvenioPostulada = null;
		this.setListarAutomatico(ConstantesBD.acronimoNo);
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
		if(estado.equals("buscarMontos")){
			if(dtoConvenioSeleccionado.getConvenio().getCodigo()==ConstantesBD.codigoNuncaValido){
				errores.add("El convenio es requerido", 
						new ActionMessage("errores.modFacturacionMontosCobroConvenioRequerido"));
			}
			if(dtoConvenioSeleccionado.getFechaVigenciaConvenio()==null){
				errores.add("La fecha de convenio es requerida", 
						new ActionMessage("errores.modFacturacionMontosCobroFechavigenciaRequerida"));
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
	 * del atributo listadoTipoPaciente
	
	 * @return retorna la variable listadoTipoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<TiposPaciente> getListadoTipoPaciente() {
		return listadoTipoPaciente;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listadoTipoPaciente
	
	 * @param valor para el atributo listadoTipoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoTipoPaciente(
			ArrayList<TiposPaciente> listadoTipoPaciente) {
		this.listadoTipoPaciente = listadoTipoPaciente;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listadoTipoAfiliado
	
	 * @return retorna la variable listadoTipoAfiliado 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<TiposAfiliado> getListadoTipoAfiliado() {
		return listadoTipoAfiliado;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listadoTipoAfiliado
	
	 * @param valor para el atributo listadoTipoAfiliado 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoTipoAfiliado(
			ArrayList<TiposAfiliado> listadoTipoAfiliado) {
		this.listadoTipoAfiliado = listadoTipoAfiliado;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listadoEstratoSocial
	
	 * @return retorna la variable listadoEstratoSocial 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<EstratosSociales> getListadoEstratoSocial() {
		return listadoEstratoSocial;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listadoEstratoSocial
	
	 * @param valor para el atributo listadoEstratoSocial 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoEstratoSocial(
			ArrayList<EstratosSociales> listadoEstratoSocial) {
		this.listadoEstratoSocial = listadoEstratoSocial;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo dtoConvenioSeleccionado
	
	 * @return retorna la variable dtoConvenioSeleccionado 
	 * @author Angela Maria Aguirre 
	 */
	public DTOMontosCobro getDtoConvenioSeleccionado() {
		return dtoConvenioSeleccionado;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo dtoConvenioSeleccionado
	
	 * @param valor para el atributo dtoConvenioSeleccionado 
	 * @author Angela Maria Aguirre 
	 */
	public void setDtoConvenioSeleccionado(DTOMontosCobro dtoConvenioSeleccionado) {
		this.dtoConvenioSeleccionado = dtoConvenioSeleccionado;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaMontosCobro
	
	 * @return retorna la variable listaMontosCobro 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DTOMontosCobro> getListaMontosCobro() {
		return listaMontosCobro;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaMontosCobro
	
	 * @param valor para el atributo listaMontosCobro 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaMontosCobro(
			ArrayList<DTOMontosCobro> listaMontosCobro) {
		this.listaMontosCobro = listaMontosCobro;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo longitudListaInical
	
	 * @return retorna la variable longitudListaInical 
	 * @author Angela Maria Aguirre 
	 */
	public int getLongitudListaInical() {
		return longitudListaInical;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo longitudListaInical
	
	 * @param valor para el atributo longitudListaInical 
	 * @author Angela Maria Aguirre 
	 */
	public void setLongitudListaInical(int longitudListaInical) {
		this.longitudListaInical = longitudListaInical;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaConvenios
	
	 * @return retorna la variable listaConvenios 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoConvenio> getListaConvenios() {
		return listaConvenios;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaConvenios
	
	 * @param valor para el atributo listaConvenios 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaConvenios(ArrayList<DtoConvenio> listaConvenios) {
		this.listaConvenios = listaConvenios;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaExcepcionesNaturaleza
	
	 * @return retorna la variable listaExcepcionesNaturaleza 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<ExcepcionesNaturaleza> getListaExcepcionesNaturaleza() {
		return listaExcepcionesNaturaleza;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaExcepcionesNaturaleza
	
	 * @param valor para el atributo listaExcepcionesNaturaleza 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaExcepcionesNaturaleza(
			ArrayList<ExcepcionesNaturaleza> listaExcepcionesNaturaleza) {
		this.listaExcepcionesNaturaleza = listaExcepcionesNaturaleza;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaTiposMonto
	
	 * @return retorna la variable listaTiposMonto 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<TiposMonto> getListaTiposMonto() {
		return listaTiposMonto;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaTiposMonto
	
	 * @param valor para el atributo listaTiposMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaTiposMonto(ArrayList<TiposMonto> listaTiposMonto) {
		this.listaTiposMonto = listaTiposMonto;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaTiposDetalleMonto
	
	 * @return retorna la variable listaTiposDetalleMonto 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<DtoIntegridadDominio> getListaTiposDetalleMonto() {
		return listaTiposDetalleMonto;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaTiposDetalleMonto
	
	 * @param valor para el atributo listaTiposDetalleMonto 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaTiposDetalleMonto(
			ArrayList<DtoIntegridadDominio> listaTiposDetalleMonto) {
		this.listaTiposDetalleMonto = listaTiposDetalleMonto;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo mostrarMensaje
	
	 * @return retorna la variable mostrarMensaje 
	 * @author Angela Maria Aguirre 
	 */
	public String getMostrarMensaje() {
		return mostrarMensaje;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo mostrarMensaje
	
	 * @param valor para el atributo mostrarMensaje 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostrarMensaje(String mostrarMensaje) {
		this.mostrarMensaje = mostrarMensaje;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listadoViasIngreso
	
	 * @return retorna la variable listadoViasIngreso 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<ViasIngreso> getListadoViasIngreso() {
		return listadoViasIngreso;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listadoViasIngreso
	
	 * @param valor para el atributo listadoViasIngreso 
	 * @author Angela Maria Aguirre 
	 */
	public void setListadoViasIngreso(ArrayList<ViasIngreso> listadoViasIngreso) {
		this.listadoViasIngreso = listadoViasIngreso;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo indiceTipoPaciente
	
	 * @return retorna la variable indiceTipoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndiceTipoPaciente() {
		return indiceTipoPaciente;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo indiceTipoPaciente
	
	 * @param valor para el atributo indiceTipoPaciente 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndiceTipoPaciente(int indiceTipoPaciente) {
		this.indiceTipoPaciente = indiceTipoPaciente;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo indiceViaIngreso
	
	 * @return retorna la variable indiceViaIngreso 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndiceViaIngreso() {
		return indiceViaIngreso;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo indiceViaIngreso
	
	 * @param valor para el atributo indiceViaIngreso 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndiceViaIngreso(int indiceViaIngreso) {
		this.indiceViaIngreso = indiceViaIngreso;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo indiceDetalle
	
	 * @return retorna la variable indiceDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public int getIndiceDetalle() {
		return indiceDetalle;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo indiceDetalle
	
	 * @param valor para el atributo indiceDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public void setIndiceDetalle(int indiceDetalle) {
		this.indiceDetalle = indiceDetalle;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo detalleSeleccionado
	
	 * @return retorna la variable detalleSeleccionado 
	 * @author Angela Maria Aguirre 
	 */
	public DTOResultadoBusquedaDetalleMontos getDetalleSeleccionado() {
		return detalleSeleccionado;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo detalleSeleccionado
	
	 * @param valor para el atributo detalleSeleccionado 
	 * @author Angela Maria Aguirre 
	 */
	public void setDetalleSeleccionado(
			DTOResultadoBusquedaDetalleMontos detalleSeleccionado) {
		this.detalleSeleccionado = detalleSeleccionado;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo codigosServiciosInsertados
	
	 * @return retorna la variable codigosServiciosInsertados 
	 * @author Angela Maria Aguirre 
	 */
	public String getCodigosServiciosInsertados() {
		return codigosServiciosInsertados;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo codigosServiciosInsertados
	
	 * @param valor para el atributo codigosServiciosInsertados 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigosServiciosInsertados(String codigosServiciosInsertados) {
		this.codigosServiciosInsertados = codigosServiciosInsertados;
	}

	

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaGruposServicios
	
	 * @return retorna la variable listaGruposServicios 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<GruposServicios> getListaGruposServicios() {
		return listaGruposServicios;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaGruposServicios
	
	 * @param valor para el atributo listaGruposServicios 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaGruposServicios(
			ArrayList<GruposServicios> listaGruposServicios) {
		this.listaGruposServicios = listaGruposServicios;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaTiposServicio
	
	 * @return retorna la variable listaTiposServicio 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<TiposServicio> getListaTiposServicio() {
		return listaTiposServicio;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaTiposServicio
	
	 * @param valor para el atributo listaTiposServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaTiposServicio(ArrayList<TiposServicio> listaTiposServicio) {
		this.listaTiposServicio = listaTiposServicio;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo listaEspecialidades
	
	 * @return retorna la variable listaEspecialidades 
	 * @author Angela Maria Aguirre 
	 */
	public ArrayList<Especialidades> getListaEspecialidades() {
		return listaEspecialidades;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo listaEspecialidades
	
	 * @param valor para el atributo listaEspecialidades 
	 * @author Angela Maria Aguirre 
	 */
	public void setListaEspecialidades(ArrayList<Especialidades> listaEspecialidades) {
		this.listaEspecialidades = listaEspecialidades;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo codigoServicio
	
	 * @return retorna la variable codigoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoServicio() {
		return codigoServicio;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo codigoServicio
	
	 * @param valor para el atributo codigoServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo nombreServicio
	
	 * @return retorna la variable nombreServicio 
	 * @author Angela Maria Aguirre 
	 */
	public String getNombreServicio() {
		return nombreServicio;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo nombreServicio
	
	 * @param valor para el atributo nombreServicio 
	 * @author Angela Maria Aguirre 
	 */
	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo codigoArticulo
	
	 * @return retorna la variable codigoArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public int getCodigoArticulo() {
		return codigoArticulo;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo codigoArticulo
	
	 * @param valor para el atributo codigoArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigoArticulo(int codigoArticulo) {
		this.codigoArticulo = codigoArticulo;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo descripcionArticulo
	
	 * @return retorna la variable descripcionArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public String getDescripcionArticulo() {
		return descripcionArticulo;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo descripcionArticulo
	
	 * @param valor para el atributo descripcionArticulo 
	 * @author Angela Maria Aguirre 
	 */
	public void setDescripcionArticulo(String descripcionArticulo) {
		this.descripcionArticulo = descripcionArticulo;
	}

	/**
	 * Este m�todo se encarga de obtener el 
	 * valor del atributo listaClaseInventario
	 *
	 * @author Angela Aguirre 
	 */
	public ArrayList<ClaseInventario> getListaClaseInventario() {
		return listaClaseInventario;
	}

	/**
	 * Este m�todo se encarga de asignar el 
	 * valor del atributo listaClaseInventario
	 *
	 * @author Angela Aguirre 
	 */
	public void setListaClaseInventario(
			ArrayList<ClaseInventario> listaClaseInventario) {
		this.listaClaseInventario = listaClaseInventario;
	}

	/**
	 * Este m�todo se encarga de obtener el 
	 * valor del atributo listaGrupoInventario
	 *
	 * @author Angela Aguirre 
	 */
	public ArrayList<GrupoInventario> getListaGrupoInventario() {
		return listaGrupoInventario;
	}

	/**
	 * Este m�todo se encarga de asignar el 
	 * valor del atributo listaGrupoInventario
	 *
	 * @author Angela Aguirre 
	 */
	public void setListaGrupoInventario(
			ArrayList<GrupoInventario> listaGrupoInventario) {
		this.listaGrupoInventario = listaGrupoInventario;
	}

	/**
	 * Este m�todo se encarga de obtener el 
	 * valor del atributo listaSubgrupoInventario
	 *
	 * @author Angela Aguirre 
	 */
	public ArrayList<SubgrupoInventario> getListaSubgrupoInventario() {
		return listaSubgrupoInventario;
	}

	/**
	 * Este m�todo se encarga de asignar el 
	 * valor del atributo listaSubgrupoInventario
	 *
	 * @author Angela Aguirre 
	 */
	public void setListaSubgrupoInventario(
			ArrayList<SubgrupoInventario> listaSubgrupoInventario) {
		this.listaSubgrupoInventario = listaSubgrupoInventario;
	}

	/**
	 * Este m�todo se encarga de obtener el 
	 * valor del atributo listaNaturalezaArticulo
	 *
	 * @author Angela Aguirre 
	 */
	public ArrayList<NaturalezaArticulo> getListaNaturalezaArticulo() {
		return listaNaturalezaArticulo;
	}

	/**
	 * Este m�todo se encarga de asignar el 
	 * valor del atributo listaNaturalezaArticulo
	 *
	 * @author Angela Aguirre 
	 */
	public void setListaNaturalezaArticulo(
			ArrayList<NaturalezaArticulo> listaNaturalezaArticulo) {
		this.listaNaturalezaArticulo = listaNaturalezaArticulo;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo fechaConvenioPostulada
	
	 * @return retorna la variable fechaConvenioPostulada 
	 * @author Angela Maria Aguirre 
	 */
	public Date getFechaConvenioPostulada() {
		return fechaConvenioPostulada;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo fechaConvenioPostulada
	
	 * @param valor para el atributo fechaConvenioPostulada 
	 * @author Angela Maria Aguirre 
	 */
	public void setFechaConvenioPostulada(Date fechaConvenioPostulada) {
		this.fechaConvenioPostulada = fechaConvenioPostulada;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo codigosArticulosInsertados
	
	 * @return retorna la variable codigosArticulosInsertados 
	 * @author Angela Maria Aguirre 
	 */
	public String getCodigosArticulosInsertados() {
		return codigosArticulosInsertados;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo codigosArticulosInsertados
	
	 * @param valor para el atributo codigosArticulosInsertados 
	 * @author Angela Maria Aguirre 
	 */
	public void setCodigosArticulosInsertados(String codigosArticulosInsertados) {
		this.codigosArticulosInsertados = codigosArticulosInsertados;
	}

	/**
	 * Este M�todo se encarga de obtener el valor 
	 * del atributo mostrarBotonDetalle
	
	 * @return retorna la variable mostrarBotonDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public boolean isMostrarBotonDetalle() {
		return mostrarBotonDetalle;
	}

	/**
	 * Este M�todo se encarga de establecer el valor 
	 * del atributo mostrarBotonDetalle
	
	 * @param valor para el atributo mostrarBotonDetalle 
	 * @author Angela Maria Aguirre 
	 */
	public void setMostrarBotonDetalle(boolean mostrarBotonDetalle) {
		this.mostrarBotonDetalle = mostrarBotonDetalle;
	}

	public void setListarAutomatico(String listarAutomatico) {
		this.listarAutomatico = listarAutomatico;
	}

	public String getListarAutomatico() {
		return listarAutomatico;
	}


	
}
