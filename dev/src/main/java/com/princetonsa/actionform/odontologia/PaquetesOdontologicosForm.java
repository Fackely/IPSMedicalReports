/**
 * 
 */
package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import util.ConstantesBD;
import util.ResultadoBoolean;
import util.UtilidadTexto;
import util.Utilidades;
import util.ValoresPorDefecto;

import com.princetonsa.dto.administracion.DtoEspecialidades;
import com.princetonsa.dto.odontologia.DtoPaquetesOdontologicos;
import com.princetonsa.dto.odontologia.DtoProgramasPaqueteOdonto;
import com.princetonsa.dto.odontologia.DtoServiciosPaqueteOdon;
import com.princetonsa.mundo.odontologia.PaquetesOdontologicosMundo;

/**
 * @author armando
 *
 */
public class PaquetesOdontologicosForm extends ValidatorForm 
{
	
	/**
	 * 
	 */
	private double codigoPrograma;
	
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private boolean esPorProgramas;
	
	/**
	 * 
	 */
	private int registroSeleccionado;
	
	
	/**
	 * 
	 */
	private ResultadoBoolean mensaje;
	
	
	/**
	 * 
	 */
	private ArrayList<DtoEspecialidades> especialidades;
	
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
	private int codigoInstitucion;
	
	/**
	 * 
	 */
	private DtoPaquetesOdontologicos paqueteOdontologico;
	
	/**
	 * 
	 */
	private ArrayList<DtoPaquetesOdontologicos> arrayPaquetesOdontologicos;
	
	
	/**
	 * 
	 */
	private String codigoPaqueteConsulta;
	
	
	/**
	 * 
	 */
	private String descripcionPaqueteConsulta;
	
	/**
	 * 
	 */
	private int codigoEspecialidadConsulta;
	
	
	/**
	 * 
	 */
	private String codigoPaqueteModificacion="";
	
	
	/**
	 * 
	 */
	public ActionErrors validate(ActionMapping mapping,HttpServletRequest request) 
	{
      ActionErrors errores = new ActionErrors();
      if(this.getEstado().equals("insertar"))
      {
    	  if(UtilidadTexto.isEmpty(this.paqueteOdontologico.getCodigo()))
    	  {
    		  errores.add("", new ActionMessage("errors.required","El codigo "));  
    	  }
    	  else
    	  {
	    	  if(PaquetesOdontologicosMundo.existePaqueteOdontologico(this.paqueteOdontologico.getCodigo(),this.codigoInstitucion))
	    	  {
	    		  errores.add("", new ActionMessage("errors.yaExiste","El código "+this.paqueteOdontologico.getCodigo()));          
	    	  }
    	  }
    	  if(UtilidadTexto.isEmpty(this.paqueteOdontologico.getDescripcion()))
    	  {
    		  errores.add("", new ActionMessage("errors.required","La descripcion "));  
    	  }
    	  if(this.getPaqueteOdontologico().getCodigoEspecialidad()<=0)
    	  {
    		  errores.add("", new ActionMessage("errors.required","La especialidad "));  
    	  }
    	  
    	  //validaciones de los programas.
    	  if(esPorProgramas)
    	  {
    		  if(paqueteOdontologico.getProgramasPaqueteOdonto().size()>0)
    		  {
	    		  for(DtoProgramasPaqueteOdonto detalle:paqueteOdontologico.getProgramasPaqueteOdonto())
		    	  {
		    		  if(detalle.getCantidad()<=0)
		    		  {
		    			  errores.add("", new ActionMessage("errors.required","El cantidad para el programa "+detalle.getPrograma().getNombre()));  
		    		  }
		    	  }
    		  }
    		  else
    		  {
    			  errores.add("", new ActionMessage("error.errorEnBlanco","El paquete debe tener por lo menos un programa asociado."));  
    		  }
    	  }
    	  else
    	  {
	    	  //validaciones de los servicios.
    		  if(paqueteOdontologico.getServiciosPaqueteOdonto().size()>0)
    		  {
		    	  for(DtoServiciosPaqueteOdon detalle:paqueteOdontologico.getServiciosPaqueteOdonto())
		    	  {
		    		  if(detalle.getCantidad()<=0)
		    		  {
		    			  errores.add("", new ActionMessage("errors.required","El cantidad para el servicio "+detalle.getDescripcionServicio()));  
		    		  }
		    	  }
    		  }
    		  else
    		  {
    			  errores.add("", new ActionMessage("error.errorEnBlanco","El paquete debe tener por lo menos un servicio asociado."));  

    		  }
    	  }
      }
      else if(this.estado.equals("guardarModificacion"))
      {
    	  if(UtilidadTexto.isEmpty(this.paqueteOdontologico.getCodigo()))
    	  {
    		  errores.add("", new ActionMessage("errors.required","El codigo "));  
    	  }
    	  else
    	  {
    		  if(!this.codigoPaqueteModificacion.equals(this.paqueteOdontologico.getCodigo())) //si se modifico el codigo, se debe verificar que no se ha asignado.
    		  {
		    	  if(PaquetesOdontologicosMundo.existePaqueteOdontologico(this.paqueteOdontologico.getCodigo(),this.codigoInstitucion))
		    	  {
		    		  errores.add("", new ActionMessage("errors.yaExiste","El código "+this.paqueteOdontologico.getCodigo()));          
		    	  }
    		  }
    	  }
    	  if(UtilidadTexto.isEmpty(this.paqueteOdontologico.getDescripcion()))
    	  {
    		  errores.add("", new ActionMessage("errors.required","La descripcion "));  
    	  }
    	  if(this.getPaqueteOdontologico().getCodigoEspecialidad()<=0)
    	  {
    		  errores.add("", new ActionMessage("errors.required","La especialidad "));  
    	  }
      }
      else if(this.estado.equals("eliminarProgramaModificacion")||this.estado.equals("eliminarServicioModificacion"))
      {
    	  if(esPorProgramas)
    	  {
	    	  if(paqueteOdontologico.getProgramasPaqueteOdonto().size()<=1)
	    	  {
				  errores.add("", new ActionMessage("error.errorEnBlanco","El paquete debe tener por lo menos un programa asociado."));  
			  }
    	  }
    	  else
    	  {
    		  if(paqueteOdontologico.getServiciosPaqueteOdonto().size()<=1)
    		  {
        		  errores.add("", new ActionMessage("error.errorEnBlanco","El paquete debe tener por lo menos un servicio asociado."));  
    		  }
    	  }
      }
      return errores;
    }
	

	/**
	 * 
	 */
	public void reset()
	{
		this.registroSeleccionado=ConstantesBD.codigoNuncaValido;
		this.paqueteOdontologico=new DtoPaquetesOdontologicos();
		this.mensaje=new ResultadoBoolean(false);
		this.especialidades=new ArrayList<DtoEspecialidades>();
		this.codigoPrograma=ConstantesBD.codigoNuncaValidoDouble;
		this.codigoServicio=ConstantesBD.codigoNuncaValido;
		this.nombreServicio="";
		this.arrayPaquetesOdontologicos=new ArrayList<DtoPaquetesOdontologicos>();
		this.codigoPaqueteConsulta="";
		this.descripcionPaqueteConsulta="";
		this.codigoEspecialidadConsulta=ConstantesBD.codigoNuncaValido;
		this.codigoPaqueteModificacion="";
	}
	
	
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public DtoPaquetesOdontologicos getPaqueteOdontologico() {
		return paqueteOdontologico;
	}

	public void setPaqueteOdontologico(DtoPaquetesOdontologicos paqueteOdontologico) {
		this.paqueteOdontologico = paqueteOdontologico;
	}


	public boolean isEsPorProgramas() {
		return esPorProgramas;
	}


	public void setEsPorProgramas(boolean esPorProgramas) {
		this.esPorProgramas = esPorProgramas;
	}


	public int getRegistroSeleccionado() {
		return registroSeleccionado;
	}


	public void setRegistroSeleccionado(int registroSeleccionado) {
		this.registroSeleccionado = registroSeleccionado;
	}


	public ResultadoBoolean getMensaje() {
		return mensaje;
	}


	public void setMensaje(ResultadoBoolean mensaje) {
		this.mensaje = mensaje;
	}


	public ArrayList<DtoEspecialidades> getEspecialidades() {
		return especialidades;
	}


	public void setEspecialidades(ArrayList<DtoEspecialidades> especialidades) {
		this.especialidades = especialidades;
	}


	public double getCodigoPrograma() {
		return codigoPrograma;
	}


	public void setCodigoPrograma(double codigoPrograma) {
		this.codigoPrograma = codigoPrograma;
	}


	public int getCodigoServicio() {
		return codigoServicio;
	}


	public void setCodigoServicio(int codigoServicio) {
		this.codigoServicio = codigoServicio;
	}


	public String getNombreServicio() {
		return nombreServicio;
	}


	public void setNombreServicio(String nombreServicio) {
		this.nombreServicio = nombreServicio;
	}


	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}


	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}


	public ArrayList<DtoPaquetesOdontologicos> getArrayPaquetesOdontologicos() {
		return arrayPaquetesOdontologicos;
	}


	public void setArrayPaquetesOdontologicos(
			ArrayList<DtoPaquetesOdontologicos> arrayPaquetesOdontologicos) {
		this.arrayPaquetesOdontologicos = arrayPaquetesOdontologicos;
	}


	public String getCodigoPaqueteConsulta() {
		return codigoPaqueteConsulta;
	}


	public void setCodigoPaqueteConsulta(String codigoPaqueteConsulta) {
		this.codigoPaqueteConsulta = codigoPaqueteConsulta;
	}


	public String getDescripcionPaqueteConsulta() {
		return descripcionPaqueteConsulta;
	}


	public void setDescripcionPaqueteConsulta(String descripcionPaqueteConsulta) {
		this.descripcionPaqueteConsulta = descripcionPaqueteConsulta;
	}


	public int getCodigoEspecialidadConsulta() {
		return codigoEspecialidadConsulta;
	}


	public void setCodigoEspecialidadConsulta(int codigoEspecialidadConsulta) {
		this.codigoEspecialidadConsulta = codigoEspecialidadConsulta;
	}


	public String getCodigoPaqueteModificacion() {
		return codigoPaqueteModificacion;
	}


	public void setCodigoPaqueteModificacion(String codigoPaqueteModificacion) {
		this.codigoPaqueteModificacion = codigoPaqueteModificacion;
	}

}
