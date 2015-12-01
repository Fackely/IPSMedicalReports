package com.princetonsa.actionform.manejoPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.mundo.InstitucionBasica;
import com.princetonsa.mundo.UsuarioBasico;
import com.princetonsa.mundo.manejoPaciente.PlanosFURIPS;

import util.UtilidadFecha;
import util.UtilidadFileUpload;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;
import util.manejoPaciente.RutasArchivosFURIPS;

/**
 * 
 * @author wilson
 *
 */
public class PlanosFURIPSForm extends ValidatorForm 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	* Estado en el que se encuentra el proceso.       
	*/
	private String estado;
	
	/**
	 * 
	 */
	private HashMap<Object, Object> mapaBusqueda;
	
	/**
	 * 
	 */
	private ArrayList<HashMap<String, Object>> tarifariosOficiales;
	
	/**
	 * 
	 */
	private RutasArchivosFURIPS rutasArchivos;
	
	/**
	 * 
	 */
	private String mensajeSobrescribirArchivos;
	
	/**
     * resetea los atributos del form
     *
     */
    public void reset()
    {
    	this.mapaBusqueda= new HashMap<Object, Object>();
    	this.rutasArchivos= new RutasArchivosFURIPS();
    	this.tarifariosOficiales= new ArrayList<HashMap<String,Object>>();
    	this.mensajeSobrescribirArchivos="";
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
        ActionErrors errores= new ActionErrors();
        errores=super.validate(mapping,request);
        
        if(estado.equals("generarArchivos")|| estado.equals("sobrescribirArchivos"))
        {
        	UsuarioBasico usuario=(UsuarioBasico)request.getSession().getAttribute("usuarioBasico");
        	InstitucionBasica institucionBasica = (InstitucionBasica)request.getSession().getAttribute("institucionBasica");
        	
        	resetearNombreArchivos(usuario, institucionBasica);
        	
        	boolean existeArchivos=false;
        	//primero verificamos si es una ruta valida y que tengamos permisos de generar los archivos
        	if(!UtilidadFileUpload.esDirectorioValido(this.getRutasArchivos().getRutaGeneral()))
        	{
        		errores.add("", new ActionMessage("error.file.rutaInvalida", "del parámetro general", "FURIPS"));
        	}
        	else
        	{
        		this.mensajeSobrescribirArchivos="";
        		if(!estado.equals("sobrescribirArchivos"))
        		{
        			if(UtilidadTexto.getBoolean(this.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furips1+"")+""))
	            	{
	        			if(UtilidadFileUpload.existeArchivoRutaCompelta(this.getRutasArchivos().getRutaFURIPS1()+this.getRutasArchivos().getNombreArchivoFURIPS1()))
	        			{
	        				this.mensajeSobrescribirArchivos+="El archivo "+(this.getRutasArchivos().getRutaFURIPS1()+this.getRutasArchivos().getNombreArchivoFURIPS1())+" ya existe. <br>";
	        				this.getRutasArchivos().setExisteFURIPS1(true);
	        				existeArchivos=true;
	        			}
	            	}
	        		if(UtilidadTexto.getBoolean(this.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furips2+"")+""))
	            	{
	        			if(UtilidadFileUpload.existeArchivoRutaCompelta(this.getRutasArchivos().getRutaFURIPS2()+this.getRutasArchivos().getNombreArchivoFURIPS2()))
	        			{
	        				this.mensajeSobrescribirArchivos+="El archivo "+(this.getRutasArchivos().getRutaFURIPS2()+this.getRutasArchivos().getNombreArchivoFURIPS2())+" ya existe. <br>";
	        				this.getRutasArchivos().setExisteFURIPS2(true);
	        				existeArchivos=true;
	        			}
	            	}
	        		if(UtilidadTexto.getBoolean(this.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furpro1+"")+""))
	            	{
	        			if(UtilidadFileUpload.existeArchivoRutaCompelta(this.getRutasArchivos().getRutaFURPRO()+this.getRutasArchivos().getNombreArchivoFURPRO()))
	        			{
	        				this.mensajeSobrescribirArchivos+="El archivo "+(this.getRutasArchivos().getRutaFURPRO()+this.getRutasArchivos().getNombreArchivoFURPRO())+" ya existe. <br>";
	        				this.getRutasArchivos().setExisteFURIPS1(true);
	        				existeArchivos=true;
	        			}
	            	}
	        		if(UtilidadTexto.getBoolean(this.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furtran+"")+""))
	            	{
	        			if(UtilidadFileUpload.existeArchivoRutaCompelta(this.getRutasArchivos().getRutaFURTRAN()+this.getRutasArchivos().getNombreArchivoFURTRAN()))
	        			{
	        				this.mensajeSobrescribirArchivos+="El archivo "+(this.getRutasArchivos().getRutaFURTRAN()+this.getRutasArchivos().getNombreArchivoFURTRAN())+" ya existe. <br>";
	        				this.getRutasArchivos().setExisteFURIPS1(true);
	        				existeArchivos=true;
	        			}
	            	}
	        	}
        	}	
        	if(!UtilidadFecha.esFechaValidaSegunAp(this.getMapaBusqueda("fechainicial")+""))
        	{
        		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Inicial"));
        	}
        	if(!UtilidadFecha.esFechaValidaSegunAp(this.getMapaBusqueda("fechafinal")+""))
        	{
        		errores.add("", new ActionMessage("errors.formatoFechaInvalido","Final"));
        	}
        	if(errores.isEmpty())
        	{
        		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getMapaBusqueda("fechainicial")+"", UtilidadFecha.getFechaActual()))
        		{
        			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Actual"));
        		}
        		if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getMapaBusqueda("fechafinal")+"", UtilidadFecha.getFechaActual()))
        		{
        			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final", "Actual"));
        		}
        		if(errores.isEmpty())
        		{
        			if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getMapaBusqueda("fechainicial")+"", this.getMapaBusqueda("fechafinal")+""))
            		{
            			errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial", "Final"));
            		}
        		}
        	}
        	
        	if(UtilidadTexto.isEmpty(this.getMapaBusqueda("tipomanual")+""))
        	{
        		errores.add("", new ActionMessage("errors.required", "El tipo de manual"));
        	}
        	
        	boolean seleccionoArchivo=false;
        	
        	if(UtilidadTexto.getBoolean(this.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furips1+"")+""))
        	{
        		seleccionoArchivo=true;
        		if(Utilidades.convertirAEntero(this.getMapaBusqueda("nrofolios_"+PlanosFURIPS.archivosEnum.Furips1)+"", true)<=0)
        		{
        			errores.add("", new ActionMessage("errors.integerMayorQue", "El Nro de folios del archivo "+PlanosFURIPS.archivosEnum.Furips1, "0"));
        		}
        	}
        	if(UtilidadTexto.getBoolean(this.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furips2+"")+""))
        	{
        		seleccionoArchivo=true;
        	}
        	if(UtilidadTexto.getBoolean(this.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furpro1+"")+""))
        	{
        		seleccionoArchivo=true;
        		if(Utilidades.convertirAEntero(this.getMapaBusqueda("nrofolios_"+PlanosFURIPS.archivosEnum.Furpro1)+"", true)<=0)
        		{
        			errores.add("", new ActionMessage("errors.integerMayorQue", "El Nro de folios del archivo "+PlanosFURIPS.archivosEnum.Furpro1, "0"));
        		}
        	}
        	if(UtilidadTexto.getBoolean(this.getMapaBusqueda(PlanosFURIPS.archivosEnum.Furtran+"")+""))
        	{
        		seleccionoArchivo=true;
        		if(Utilidades.convertirAEntero(this.getMapaBusqueda("nrofolios_"+PlanosFURIPS.archivosEnum.Furtran)+"", true)<=0)
        		{
        			errores.add("", new ActionMessage("errors.integerMayorQue", "El Nro de folios del archivo "+PlanosFURIPS.archivosEnum.Furtran, "0"));
        		}
        	}
        
        	if(!seleccionoArchivo)
        	{
        		errores.add("", new ActionMessage("errors.required", "La selección de un archivo a generar"));
        	}
        	
        	if(existeArchivos || !errores.isEmpty())
        	{
        		this.setEstado("continuar");
        	}
        	
        }
        
        return errores;
    }

    
    /**
	 * 
	 * @param forma
	 */
	public void resetearNombreArchivos(UsuarioBasico usuario, InstitucionBasica institucionBasica) 
	{
		String fechaInicial= "_FI_"+((this.getMapaBusqueda("fechainicial")+"").replaceAll("/", ""))+"";
		String fechaFinal= "_FF_"+((this.getMapaBusqueda("fechafinal")+"").replaceAll("/", ""))+"";
		String rutaGeneral= ValoresPorDefecto.getValoresDefectoPathArchivosPlanosFurips(usuario.getCodigoInstitucionInt())+"C"+ValoresPorDefecto.getConvenioFisalud(usuario.getCodigoInstitucionInt())+System.getProperty("file.separator");
		
		this.getRutasArchivos().setRutaGeneral(rutaGeneral);
		String complementoArchivo=institucionBasica.getCodMinsalud()+(UtilidadFecha.getFechaActual().replaceAll("/", ""))+fechaInicial+fechaFinal+".txt";
		
		///FURIPS 1
		this.getRutasArchivos().setRutaFURIPS1(rutaGeneral+"FURIPS"+System.getProperty("file.separator"));
		this.getRutasArchivos().setNombreArchivoFURIPS1("FURIPS1"+complementoArchivo);
		this.getRutasArchivos().setExisteFURIPS1(false);
		this.getRutasArchivos().setNombreArchivoInconsistenciasFURIPS1("InconFURIPS1"+complementoArchivo);
		
		//FURIPS 2
		this.getRutasArchivos().setRutaFURIPS2(rutaGeneral+"FURIPS"+System.getProperty("file.separator"));
		this.getRutasArchivos().setNombreArchivoFURIPS2("FURIPS2"+complementoArchivo);
		this.getRutasArchivos().setExisteFURIPS2(false);
		this.getRutasArchivos().setNombreArchivoInconsistenciasFURIPS2("InconFURIPS2"+complementoArchivo);
		
		//FURPRO
		this.getRutasArchivos().setRutaFURPRO(rutaGeneral+"FURPRO"+System.getProperty("file.separator"));
		this.getRutasArchivos().setNombreArchivoFURPRO("FURPRO1"+complementoArchivo);
		this.getRutasArchivos().setExisteFURPRO(false);
		this.getRutasArchivos().setNombreArchivoInconsistenciasFURPRO("InconFURPRO1"+System.getProperty("file.separator"));
		
		//FURTRAN
		this.getRutasArchivos().setRutaFURTRAN(rutaGeneral+"FURTRAN"+System.getProperty("file.separator"));
		this.getRutasArchivos().setNombreArchivoFURTRAN("FURTRAN"+complementoArchivo);
		this.getRutasArchivos().setExisteFURTRAN(false);
		this.getRutasArchivos().setNombreArchivoInconsistenciasFURTRAN("InconFURTRAN"+complementoArchivo);
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
	 * @return the mapaBusqueda
	 */
	public HashMap<Object, Object> getMapaBusqueda() {
		return mapaBusqueda;
	}

	/**
	 * @param mapaBusqueda the mapaBusqueda to set
	 */
	public void setMapaBusqueda(HashMap<Object, Object> mapaBusqueda) {
		this.mapaBusqueda = mapaBusqueda;
	}
    
	/**
	 * @return the mapaBusqueda
	 */
	public Object getMapaBusqueda(Object key) {
		return mapaBusqueda.get(key);
	}

	/**
	 * @param mapaBusqueda the mapaBusqueda to set
	 */
	public void setMapaBusqueda(Object key, Object value) {
		this.mapaBusqueda.put(key, value);
	}

	/**
	 * @return the tarifariosOficiales
	 */
	public ArrayList<HashMap<String, Object>> getTarifariosOficiales() {
		return tarifariosOficiales;
	}

	/**
	 * @param tarifariosOficiales the tarifariosOficiales to set
	 */
	public void setTarifariosOficiales(
			ArrayList<HashMap<String, Object>> tarifariosOficiales) {
		this.tarifariosOficiales = tarifariosOficiales;
	}
	
	/**
	 * @return the mensajeSobrescribirArchivos
	 */
	public String getMensajeSobrescribirArchivos() {
		return mensajeSobrescribirArchivos;
	}

	/**
	 * @param mensajeSobrescribirArchivos the mensajeSobrescribirArchivos to set
	 */
	public void setMensajeSobrescribirArchivos(String mensajeSobrescribirArchivos) {
		this.mensajeSobrescribirArchivos = mensajeSobrescribirArchivos;
	}

	/**
	 * @return the rutasArchivos
	 */
	public RutasArchivosFURIPS getRutasArchivos() {
		return rutasArchivos;
	}

	/**
	 * @param rutasArchivos the rutasArchivos to set
	 */
	public void setRutasArchivos(RutasArchivosFURIPS rutasArchivos) {
		this.rutasArchivos = rutasArchivos;
	}
	
	
}
