package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.util.MessageResources;

import util.ConstantesBD;
import util.UtilidadTexto;

import com.princetonsa.dto.administracion.DtoIntegridadDominio;
import com.princetonsa.dto.facturacion.DtoEntidadSubcontratada;
import com.princetonsa.dto.facturacion.DtoFiltroProcesarRipsEntidadesSub;

public class RipsEntidadesSubcontratadasForm extends ActionForm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RipsEntidadesSubcontratadasForm() {

	}
	
	/**
	 * Este método se encarga de inicializar todos los valores de la forma.
	 *  @author Fabian Becerra
     */
	public void reset() {
		
		this.listaEntidadesSub=new ArrayList<DtoEntidadSubcontratada>();
		this.listadoTipoCodMedInsu=new ArrayList<DtoIntegridadDominio>();
		this.tarifariosOficiales = new ArrayList<HashMap<String,Object>>();
		this.setFiltroProcesarRipsEntidadesSub(new DtoFiltroProcesarRipsEntidadesSub());
		this.getFiltroProcesarRipsEntidadesSub().setFinalidadesServicioEnSistema(new ArrayList<Integer>());
		this.getFiltroProcesarRipsEntidadesSub().setFinalidadesConsultaEnSistema(new ArrayList<String>());
		this.getFiltroProcesarRipsEntidadesSub().setCausasExternasEnSistema(new ArrayList<Integer>());
		this.exito=ConstantesBD.acronimoNo;
		this.path="";
		
	}
	
	/**
	 * Este método se encarga de realizar las validaciones de 
	 * los datos ingresados por el usuario
	 * 
	 * @param ActionMapping
	 * @param HttpServletRequest
	 * @return ActionErrors
	 * 
	 *  @author Fabian Becerra
	 */
	public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1){

		ActionErrors errores=null;
		/** Contiene los mensajes genéricos para esta funcionalidad * */
		MessageResources fuenteMensaje = MessageResources.getMessageResources("com.servinte.mensajes.facturacion.RipsEntidadesSubcontratadasForm");
		errores=new ActionErrors();
		if(estado.equals("procesar")){
			
				if(UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoCT().getFileName())||UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoAF().getFileName())
						||UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoUS().getFileName())||UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoAD().getFileName()))
				{
					String archivos="";
					if(this.filtroProcesarRipsEntidadesSub.getArchivoCT()==null||UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoCT().getFileName()))
						archivos+=" CT";
					if(this.filtroProcesarRipsEntidadesSub.getArchivoAF()==null||UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoAF().getFileName()))
						archivos+=" AF";
					if(this.filtroProcesarRipsEntidadesSub.getArchivoUS()==null||UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoUS().getFileName()))
						archivos+=" US";
					if(this.filtroProcesarRipsEntidadesSub.getArchivoAD()==null||UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoAD().getFileName()))
						archivos+=" AD";
					errores.add("archivos requeridos", new ActionMessage("errors.notEspecific", 
							fuenteMensaje.getMessage("RipsEntidadesSubcontratadasForm.archivosRequeridos",
									archivos)));
				}
				
				if(UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoAC().getFileName())&&UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoAP().getFileName())
						//&&UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoAH().getFileName())&&UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoAU().getFileName())
						&&UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoAM().getFileName())&&UtilidadTexto.isEmpty(this.filtroProcesarRipsEntidadesSub.getArchivoAT().getFileName()))
				{
					errores.add("archivos requeridos", new ActionMessage("errors.notEspecific", 
							fuenteMensaje.getMessage("RipsEntidadesSubcontratadasForm.archivosRequeridosServicios")));
				}
				
				
				if(this.filtroProcesarRipsEntidadesSub.getCodigoPkEntidadSub()==ConstantesBD.codigoNuncaValidoLong){
					errores.add("La Entidad Subcontratada es requerida", 
							new ActionMessage("errors.required", "La Entidad Subcontratada"));
				}
				if(this.filtroProcesarRipsEntidadesSub.getTarifarioSeleccionadoCodServicios()==ConstantesBD.codigoNuncaValido){
					errores.add("La Codificación de Servicios es requerida", 
							new ActionMessage("errors.required", "La Codificación de Servicios"));
				}
				if(this.filtroProcesarRipsEntidadesSub.getAcronimoCodMedicInsum().equals(ConstantesBD.codigoNuncaValido+"")){
					errores.add("La Codificación Medicamentos/Insumos es requerida", 
							new ActionMessage("errors.required", "La Codificación Medicamentos/Insumos"));
				}
				
			
				
			
			
		}
		return errores;
	}
		
	
	private String path;
	/**
	 * Atributo que indica si el proceso fue correcto utilizado para mostrar mensaje
	 * proceso exitoso
	 */
	private String exito;
	
	/**
	 * Atributo donde se almacena el estado o accion a realizar en las páginas
	 */
	private String estado;
	
	/**
	 * Atributo donde se almacenan las entidades subcontratadas activas en el sistema
	 */
	private ArrayList<DtoEntidadSubcontratada> listaEntidadesSub;
	
	/**
	 * Atributo donde se almacenan los tipos de codificación a utilizar 
	 * para leer la información de los medicamentos
	 */
	private ArrayList<DtoIntegridadDominio> listadoTipoCodMedInsu;
	
	/**
	 * Atributo que almacena el listado de tarifarios oficiales
	 */
	private ArrayList<HashMap<String, Object>> tarifariosOficiales = new ArrayList<HashMap<String,Object>>();
	
	/**
	 * Atributo dto filtro que almacena los parametros para realizar el proceso
	 */
	private DtoFiltroProcesarRipsEntidadesSub filtroProcesarRipsEntidadesSub;
	

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo estado
	 * 
	 * @param  valor para el atributo estado 
	 */
	public void setEstado(String estado) {
		this.estado = estado;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo estado
	 * 
	 * @return  Retorna la variable estado
	 */
	public String getEstado() {
		return estado;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo listaEntidadesSub
	 * 
	 * @param  valor para el atributo listaEntidadesSub 
	 */
	public void setListaEntidadesSub(ArrayList<DtoEntidadSubcontratada> listaEntidadesSub) {
		this.listaEntidadesSub = listaEntidadesSub;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo listaEntidadesSub
	 * 
	 * @return  Retorna la variable listaEntidadesSub
	 */
	public ArrayList<DtoEntidadSubcontratada> getListaEntidadesSub() {
		return listaEntidadesSub;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo listadoTipoCodMedInsu
	 * 
	 * @param  valor para el atributo listadoTipoCodMedInsu 
	 */
	public void setListadoTipoCodMedInsu(ArrayList<DtoIntegridadDominio> listadoTipoCodMedInsu) {
		this.listadoTipoCodMedInsu = listadoTipoCodMedInsu;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo listadoTipoCodMedInsu
	 * 
	 * @return  Retorna la variable listadoTipoCodMedInsu
	 */
	public ArrayList<DtoIntegridadDominio> getListadoTipoCodMedInsu() {
		return listadoTipoCodMedInsu;
	}
	
	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo filtroProcesarRipsEntidadesSub
	 * 
	 * @param  valor para el atributo filtroProcesarRipsEntidadesSub 
	 */
	public void setFiltroProcesarRipsEntidadesSub(
			DtoFiltroProcesarRipsEntidadesSub filtroProcesarRipsEntidadesSub) {
		this.filtroProcesarRipsEntidadesSub = filtroProcesarRipsEntidadesSub;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo filtroProcesarRipsEntidadesSub
	 * 
	 * @return  Retorna la variable filtroProcesarRipsEntidadesSub
	 */
	public DtoFiltroProcesarRipsEntidadesSub getFiltroProcesarRipsEntidadesSub() {
		return filtroProcesarRipsEntidadesSub;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo tarifariosOficiales
	 * 
	 * @return  Retorna la variable tarifariosOficiales
	 */
	public ArrayList<HashMap<String, Object>> getTarifariosOficiales() {
		return tarifariosOficiales;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo tarifariosOficiales
	 * 
	 * @param  valor para el atributo tarifariosOficiales 
	 */
	public void setTarifariosOficiales(
			ArrayList<HashMap<String, Object>> tarifariosOficiales) {
		this.tarifariosOficiales = tarifariosOficiales;
	}

	/**
	 * Método que se encarga de establecer el valor 
	 * del atributo exito
	 * 
	 * @param  valor para el atributo exito 
	 */
	public void setExito(String exito) {
		this.exito = exito;
	}

	/**
	 * Método que se encarga de obtener el valor 
	 *  del atributo exito
	 * 
	 * @return  Retorna la variable exito
	 */
	public String getExito() {
		return exito;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	

	
	

}
