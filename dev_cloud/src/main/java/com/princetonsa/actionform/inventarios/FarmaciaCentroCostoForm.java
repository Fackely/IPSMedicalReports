package com.princetonsa.actionform.inventarios;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import util.ConstantesBD;
import util.ResultadoBoolean;
import util.Utilidades;
import util.manejoPaciente.UtilidadesManejoPaciente;

/**
 * Clase para el manejo de farmacia_x_centro_costo
 * Date: 2008-01-22
 * @author lgchavez@princetonsa.com
 */
public class FarmaciaCentroCostoForm extends ValidatorForm 
{
	
	/**
	 * estado del formulario
	 */
	private String estado;
	
	/**
	 * codigo del centro de atencion
	 */
	private int centroAtencion;
	
	/**
	 * 
	 */
	private HashMap centrosAtencionMap;
	
	/**
	 * 
	 * */
	private String nombreCentroAtencion;
		
	/**
	 * 
	 * */
	private String nombreCentroCosto;
	
	/**
	 * 
	 */
	private int centroCosto;
	
	/**
	 * 
	 */
	private HashMap centroCostoMap;
	
	/**
	 * 
	 */
	private int codigo;
	
	/**
	 * 
	 */
	private HashMap farmaciaCentroCostoMap;
	
	/**
	 * 
	 */
	private ArrayList farmaciaMap;
	
	
	/**
	 * 
	 */
	private HashMap detFarmaciaCcMap;
	
	/**
	 * 
	 */
	private int codigoFarmaciaCc;
	
	/**
	 * 
	 */
	private int farmacia;
	
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * 
	 */
	public void reset( int codigoInstitucion, int centroAtencion, Connection con)
	{
				
		this.centroAtencion=centroAtencion;
		this.centrosAtencionMap= Utilidades.obtenerCentrosAtencion(codigoInstitucion);
		this.centroCosto=ConstantesBD.codigoNuncaValido;
		this.centroCostoMap=Utilidades.obtenerCentrosCosto(codigoInstitucion, ConstantesBD.codigoTipoAreaDirecto+ConstantesBD.separadorSplit+ConstantesBD.codigoTipoAreaIndirecto, false, centroAtencion, true);
		this.farmaciaMap=UtilidadesManejoPaciente.obtenerCentrosCosto(con, codigoInstitucion, ConstantesBD.codigoTipoAreaSubalmacen+"", false, 0);
		this.farmacia=ConstantesBD.codigoNuncaValido;
		this.detFarmaciaCcMap= new HashMap();
		this.farmaciaCentroCostoMap=new HashMap();
		this.farmaciaCentroCostoMap.put("numRegistros", "0");
		this.codigo=ConstantesBD.codigoNuncaValido;
		this.codigoFarmaciaCc=ConstantesBD.codigoNuncaValido;
		this.detFarmaciaCcMap.put("numRegistros", "0");
	
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
        
 
        if (this.estado.equals("guardarNuevo"))
        {
        	
        	String viereg="";
        	String newreg=this.centroAtencion+"-"+this.centroCosto+"-"+this.farmacia;
        	
        	for(int i=0;i<Utilidades.convertirAEntero(this.farmaciaCentroCostoMap.get("numRegistros")+"");i++)
        	{
        		
        		viereg=this.farmaciaCentroCostoMap.get("codcentroatencion_"+i)+"-"+
        			   this.farmaciaCentroCostoMap.get("codcentrocosto_"+i)+"-"+
        			   this.farmaciaCentroCostoMap.get("codigodet_"+i);
        		if(newreg.trim().equals(viereg.trim()))
        				{
        					errores.add("minimo un parametro", new ActionMessage("error.inventarios.farmaciaCentroCosto"));
        					this.estado="nuevoRegistro";
        				}
        	}
          	
        }
        
        
        
        
        
        
        return errores;        
    }

    
    
	/**
	 * @return the centroAtencion
	 */
	public int getCentroAtencion() {
		return centroAtencion;
	}

	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setCentroAtencion(int centroAtencion) {
		this.centroAtencion = centroAtencion;
	}

	/**
	 * @return the centroCosto
	 */
	public int getCentroCosto() {
		return centroCosto;
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCentroCosto(int centroCosto) {
		this.centroCosto = centroCosto;
	}

	/**
	 * @return the centroCostoMap
	 */
	public HashMap getCentroCostoMap() {
		return centroCostoMap;
	}

	/**
	 * @param centroCostoMap the centroCostoMap to set
	 */
	public void setCentroCostoMap(HashMap centroCostoMap) {
		this.centroCostoMap = centroCostoMap;
	}

	/**
	 * @return the centrosAtencionMap
	 */
	public HashMap getCentrosAtencionMap() {
		return centrosAtencionMap;
	}

	/**
	 * @param centrosAtencionMap the centrosAtencionMap to set
	 */
	public void setCentrosAtencionMap(HashMap centrosAtencionMap) {
		this.centrosAtencionMap = centrosAtencionMap;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the codigoFarmaciaCc
	 */
	public int getCodigoFarmaciaCc() {
		return codigoFarmaciaCc;
	}

	/**
	 * @param codigoFarmaciaCc the codigoFarmaciaCc to set
	 */
	public void setCodigoFarmaciaCc(int codigoFarmaciaCc) {
		this.codigoFarmaciaCc = codigoFarmaciaCc;
	}

	/**
	 * @return the detFarmaciaCcMap
	 */
	public HashMap getDetFarmaciaCcMap() {
		return detFarmaciaCcMap;
	}

	/**
	 * @param detFarmaciaCcMap the detFarmaciaCcMap to set
	 */
	public void setDetFarmaciaCcMap(HashMap detFarmaciaCcMap) {
		this.detFarmaciaCcMap = detFarmaciaCcMap;
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
	 * @return the farmacia
	 */
	public int getFarmacia() {
		return farmacia;
	}

	/**
	 * @param farmacia the farmacia to set
	 */
	public void setFarmacia(int farmacia) {
		this.farmacia = farmacia;
	}

	/**
	 * @return the farmaciaCentroCostoMap
	 */
	public HashMap getFarmaciaCentroCostoMap() {
		return farmaciaCentroCostoMap;
	}

	/**
	 * @param farmaciaCentroCostoMap the farmaciaCentroCostoMap to set
	 */
	public void setFarmaciaCentroCostoMap(HashMap farmaciaCentroCostoMap) {
		this.farmaciaCentroCostoMap = farmaciaCentroCostoMap;
	}

	/**
	 * @return the nombreCentroAtencion
	 */
	public String getNombreCentroAtencion() {
		return nombreCentroAtencion;
	}

	/**
	 * @param nombreCentroAtencion the nombreCentroAtencion to set
	 */
	public void setNombreCentroAtencion(String nombreCentroAtencion) {
		this.nombreCentroAtencion = nombreCentroAtencion;
	}

	/**
	 * @return the nombreCentroCosto
	 */
	public String getNombreCentroCosto() {
		return nombreCentroCosto;
	}

	/**
	 * @param nombreCentroCosto the nombreCentroCosto to set
	 */
	public void setNombreCentroCosto(String nombreCentroCosto) {
		this.nombreCentroCosto = nombreCentroCosto;
	}

	/**
	 * @return the farmaciaMap
	 */
	public ArrayList getFarmaciaMap() {
		return farmaciaMap;
	}

	/**
	 * @param farmaciaMap the farmaciaMap to set
	 */
	public void setFarmaciaMap(ArrayList farmaciaMap) {
		this.farmaciaMap = farmaciaMap;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}

	



	
}