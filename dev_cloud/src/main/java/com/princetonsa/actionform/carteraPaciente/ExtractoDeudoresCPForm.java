package com.princetonsa.actionform.carteraPaciente;

import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;

import com.princetonsa.dto.carteraPaciente.DtoDeudor;
import com.princetonsa.dto.carteraPaciente.DtoExtractosDeudoresCP;

public class ExtractoDeudoresCPForm extends ValidatorForm
{
	public String estado;

	public String mensaje;
	
	public DtoDeudor dtoDeudor;
	
	public HashMap centrosAtencion;
	
	ArrayList<HashMap<String, Object>> listaTiposId;
	
	String tipoSalida;
	
	ArrayList<DtoExtractosDeudoresCP> listaExtractos;
	
	int numRegistros;
	
	int indice;
	
	DtoDeudor dtoBusquedaExtractos;

	private String urlArchivoPlano;
	
	private String pathArchivoPlano;
	
	ArrayList<DtoExtractosDeudoresCP> listadoExtractosDeudor;
	
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
    	ActionErrors errores = new ActionErrors();
    	errores = super.validate(mapping, request); 
    	if (this.estado.equals("buscarDatosDeudor"))
    	{
    		if (this.dtoDeudor.getCentroAtencion()==ConstantesBD.codigoNuncaValido
    			&&this.dtoDeudor.getTipoIdentificacionDeu().equals("")&&this.dtoDeudor.getNumeroIdentificacionDeu().equals("")
    			&&this.dtoDeudor.getPrimerNombreDeu().equals("")&&this.dtoDeudor.getPrimerApellidoDeu().equals("")
    			&&this.dtoDeudor.getTipoIdentificacionPac().equals("")&&this.dtoDeudor.getNumeroIdentificacionPac().equals("")
    			&&this.getDtoDeudor().getPrimerNombrePac().equals("")&&this.getDtoDeudor().getPrimerApellidoPac().equals("")
    			&&this.getDtoDeudor().getDtoDocsGarantia().getConsecutivo().equals("")&&this.getDtoDeudor().getDtoDocsGarantia().getEstado().equals("")
    			&&this.dtoDeudor.getFactura().equals(""))
    				errores.add("", new ActionMessage("errors.required","Al menos un parámetro para la consulta "));
    		
    		if (this.tipoSalida.equals(""))
    			errores.add("", new ActionMessage("errors.required","El tipo de salida "));
    	}
    	
    	return errores;
	}
	
	public void clean()
	{
		this.estado="";
		this.mensaje="";
		this.dtoDeudor=new DtoDeudor();
		this.centrosAtencion= new HashMap();
		this.listaTiposId=new ArrayList<HashMap<String,Object>>();
		this.tipoSalida="";
		this.listaExtractos=new ArrayList<DtoExtractosDeudoresCP>();
		this.numRegistros=ConstantesBD.codigoNuncaValido;
		this.indice=ConstantesBD.codigoNuncaValido;
		this.dtoBusquedaExtractos=new DtoDeudor();
		this.listadoExtractosDeudor=new ArrayList<DtoExtractosDeudoresCP>();
		this.urlArchivoPlano="";
		this.pathArchivoPlano="";
	}
	
	public ExtractoDeudoresCPForm()
	{
		clean();
	}
	
	
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public DtoDeudor getDtoDeudor() {
		return dtoDeudor;
	}

	public void setDtoDeudor(DtoDeudor dtoDeudor) {
		this.dtoDeudor = dtoDeudor;
	}

	public HashMap getCentrosAtencion() {
		return centrosAtencion;
	}

	public void setCentrosAtencion(HashMap centrosAtencion) {
		this.centrosAtencion = centrosAtencion;
	}

	public ArrayList<HashMap<String, Object>> getListaTiposId() {
		return listaTiposId;
	}

	public void setListaTiposId(ArrayList<HashMap<String, Object>> listaTiposId) {
		this.listaTiposId = listaTiposId;
	}

	public String getTipoSalida() {
		return tipoSalida;
	}

	public void setTipoSalida(String tipoSalida) {
		this.tipoSalida = tipoSalida;
	}

	public ArrayList<DtoExtractosDeudoresCP> getListaExtractos() {
		return listaExtractos;
	}

	public void setListaExtractos(ArrayList<DtoExtractosDeudoresCP> listaExtractos) {
		this.listaExtractos = listaExtractos;
	}

	public int getNumRegistros() {
		return numRegistros;
	}

	public void setNumRegistros(int numRegistros) {
		this.numRegistros = numRegistros;
	}

	public int getIndice() {
		return indice;
	}

	public void setIndice(int indice) {
		this.indice = indice;
	}

	public DtoDeudor getDtoBusquedaExtractos() {
		return dtoBusquedaExtractos;
	}

	public void setDtoBusquedaExtractos(DtoDeudor dtoBusquedaExtractos) {
		this.dtoBusquedaExtractos = dtoBusquedaExtractos;
	}

	public ArrayList<DtoExtractosDeudoresCP> getListadoExtractosDeudor() {
		return listadoExtractosDeudor;
	}

	public void setListadoExtractosDeudor(
			ArrayList<DtoExtractosDeudoresCP> listadoExtractosDeudor) {
		this.listadoExtractosDeudor = listadoExtractosDeudor;
	}

	public String getPathArchivoPlano() {
		return pathArchivoPlano;
	}

	public void setPathArchivoPlano(String pathArchivoPlano) {
		this.pathArchivoPlano = pathArchivoPlano;
	}

	public String getUrlArchivoPlano() {
		return urlArchivoPlano;
	}

	public void setUrlArchivoPlano(String urlArchivoPlano) {
		this.urlArchivoPlano = urlArchivoPlano;
	}	
}