package com.princetonsa.actionform.consultaExterna;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.princetonsa.dto.consultaExterna.DtoMotivosCita;

import util.ConstantesBD;
import util.ResultadoBoolean;


public class MotivosCitaForm extends ValidatorForm
{
	/**
	 * Para hacer logs de esta funcionalidad.
	 */
	private Logger logger = Logger.getLogger(MotivosCitaForm.class);
	
	/**
	 * Variable para manejo de Estado
	 */
	private String estado;
	
	/**
	 * Atributo Mensaje
	 */
	private ResultadoBoolean mensaje=new ResultadoBoolean(false);
	
	/**
	 * Arreglo para guardar los docuentos segun criterios de busqueda
	 */
	private ArrayList<DtoMotivosCita> listaMotivosCita= new ArrayList<DtoMotivosCita>();
	private DtoMotivosCita dtoMotivosCita = new DtoMotivosCita();
	
	/**
	 * Atributos para un nuevo registro de Motivos Cita
	 */
	private String codigo;
	private String descripcion;
	private String activo;
	// se adiciona el campo tipo motivo cambios
	private String tipoMotivo;
	
	/**
	 * Atributos para modificar un registro
	 */
	private int indiceModificar;
	private int codigoPk;
	
	
	/**
	 * Resetea los valores de la forma cuando
	 * se ingresa por la funcionalidad por Area
	 * @param codigoInstitucion
	 */
	public void reset(int codigoInstitucion)
	{
		this.estado="";
		this.listaMotivosCita= new ArrayList<DtoMotivosCita>();
		this.dtoMotivosCita= new DtoMotivosCita();
		this.codigo="";
		this.descripcion="";
		this.activo=ConstantesBD.acronimoSi;
		this.indiceModificar=0;
		this.codigoPk=0;
		this.tipoMotivo="";
	}

	public int getCodigoPk() {
		return codigoPk;
	}

	public void setCodigoPk(int codigoPk) {
		this.codigoPk = codigoPk;
	}

	public int getIndiceModificar() {
		return indiceModificar;
	}

	public void setIndiceModificar(int indiceModificar) {
		this.indiceModificar = indiceModificar;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getActivo() {
		return activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public ArrayList<DtoMotivosCita> getListaMotivosCita() {
		return listaMotivosCita;
	}

	public void setListaMotivosCita(ArrayList<DtoMotivosCita> listaMotivosCita) {
		this.listaMotivosCita = listaMotivosCita;
	}

	public DtoMotivosCita getDtoMotivosCita() {
		return dtoMotivosCita;
	}

	public void setDtoMotivosCita(DtoMotivosCita dtoMotivosCita) {
		this.dtoMotivosCita = dtoMotivosCita;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public ResultadoBoolean getMensaje() {
		return mensaje;
	}

	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}
	
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
    {
		ActionErrors errores= new ActionErrors();
		
		if(this.estado.equals("guardarNuevoRegistro"))
		{
			if(this.codigo.equals(""))
			{
				this.estado= "nuevoRegistro";
				errores.add("descripcion",new ActionMessage("errors.required","El codigo "));				
			}
			if(this.descripcion.equals(""))
			{
				this.estado= "nuevoRegistro";
				errores.add("descripcion",new ActionMessage("errors.required","La descripcion "));
			}
			
			if(this.tipoMotivo.equals(""))
			{
				this.estado= "nuevoRegistro";
				errores.add("descripcion",new ActionMessage("errors.required","El Tipo Motivo "));
			}
			
			
			
			
			
			
			
			
		}
		if(this.estado.equals("guardarModificar"))
		{
			if(this.codigo.equals(""))
			{
				this.estado= "modificarRegistro";
				errores.add("descripcion",new ActionMessage("errors.required","El codigo "));				
			}
			if(this.descripcion.equals(""))
			{
				this.estado= "modificarRegistro";
				errores.add("descripcion",new ActionMessage("errors.required","La descripcion "));
			}
		}
			
        return errores;
    }

	/**
	 * @return the tipoMotivo
	 */
	public String getTipoMotivo() {
		return tipoMotivo;
	}

	/**
	 * @param tipoMotivo the tipoMotivo to set
	 */
	public void setTipoMotivo(String tipoMotivo) {
		this.tipoMotivo = tipoMotivo;
	}
}