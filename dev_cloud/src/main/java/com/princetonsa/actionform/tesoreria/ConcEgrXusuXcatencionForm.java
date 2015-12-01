/*
 * Abril 08, 2010
 */
package com.princetonsa.actionform.tesoreria;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;

import com.servinte.axioma.orm.CentroAtencion;
import com.servinte.axioma.orm.ConcEgrXusuXcatencion;
import com.servinte.axioma.orm.ConceptosDeEgreso;
import com.servinte.axioma.orm.HistConcEgrXusuXcatencion;
import com.servinte.axioma.orm.TiposUsuarios;
import com.servinte.axioma.orm.delegate.odontologia.TiposUsuariosDelegate;
import com.servinte.axioma.orm.delegate.tesoreria.ConceptosDeEgresoDelegate;

import util.ConstantesBD;
import util.UtilidadTexto;
import util.Utilidades;


/**
 * @author Cristhian Murillo
 *
 * Clase que almacena y carga la informaci&oacute;n a la vista utilizada para la funcionalidad
 */
public class ConcEgrXusuXcatencionForm extends ValidatorForm 
{
	
	/* ATRIBUTOS*/
	
	/**
	 * Variable para manejar la direcci&oacute;n del workflow 
	 */
	private String estado;
	
	/**
	 * DTO 
	 */
	private ConcEgrXusuXcatencion dto = new ConcEgrXusuXcatencion();
	
	
	/**
	 * Lista DTO 
	 */
	private ArrayList<ConcEgrXusuXcatencion> listaDto;

	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar 
	 */
	private boolean mostrarFormularioIngreso;

	
	/**
	 * Parametros para ordenar 
	 */
	private String patronOrdenar;
	

	/**
	 * Parametros para ordenar 
	 */
	private String esDescendente;
	
	
	/**
	 * Paginador 
	 */
	private int posArray;
	
	
	/**
	 * Lista CentroAtencion
	 */
	private ArrayList<CentroAtencion> listaCentroAtencion;
	
	
	/**
	 * Lista ConceptosDeEgreso
	 */
	private ArrayList<ConceptosDeEgreso> listaConceptosEgreso;
	
	
	/**
	 * Lista TiposUsuarios
	 */
	private ArrayList<TiposUsuarios> listaTiposUsuarios;
	
	
	/**
	 * Variable para hacer el cast de la llave primaria de ConceptoDeEgreso
	 */
	private String strConceptoEgreso;
	

	/**
	 * Variable para hacer el cast de la llave primaria de centro de Atencion
	 */
	private String strCentroAtencion;
	
	
	/**
	 * Variable para hacer el cast de la llave primaria de TiposUsuarios
	 */
	private String strTipoUsuario;
	
	
	
	
	
	
	/* SETS Y GETS */
		
	/**
	 * @return the strTipoUsuario
	 */
	public String getStrTipoUsuario() {
		try{
			strTipoUsuario = this.getDto().getTiposUsuarios().getCodigo()+"";
		}
		catch (NullPointerException e) {
			strTipoUsuario = null;
		}
		
		return strTipoUsuario;
	}


	/**
	 * @param strTipoUsuario the strTipoUsuario to set
	 */
	public void setStrTipoUsuario(String strTipoUsuario) {
		Long pk = Utilidades.convertirALong(strTipoUsuario);
		this.getDto().setTiposUsuarios(new TiposUsuariosDelegate().findById(pk));
	}

	

	/**
	 * @return the listaTiposUsuarios
	 */
	public ArrayList<TiposUsuarios> getListaTiposUsuarios() {
		return listaTiposUsuarios;
	}


	/**
	 * @param listaTiposUsuarios the listaTiposUsuarios to set
	 */
	public void setListaTiposUsuarios(ArrayList<TiposUsuarios> listaTiposUsuarios) {
		this.listaTiposUsuarios = listaTiposUsuarios;
	}

	
	/**
	 * @return the strConceptoEgreso
	 */
	public String getStrConceptoEgreso() {
		try{
			strConceptoEgreso = this.getDto().getConceptosDeEgreso().getCodigoPk()+"";
		}
		catch (NullPointerException e) {
			//e.printStackTrace();
			strConceptoEgreso = null;
		}
		
		return strConceptoEgreso;
	}
	

	
	/**
	 * @param strConceptoEgreso the strConceptoEgreso to set
	 */
	public void setStrConceptoEgreso(String strConceptoEgreso) {
		Long pk = Utilidades.convertirALong(strConceptoEgreso);
		this.getDto().setConceptosDeEgreso(new ConceptosDeEgresoDelegate().findById(pk));
	}

		
	/**
	 * @return the strCentroAtencion
	 */
	public String getStrCentroAtencion() {
		return strCentroAtencion;
	}

	
	/**
	 * @param strCentroAtencion the strCentroAtencion to set
	 */
	public void setStrCentroAtencion(String strCentroAtencion) {
		this.strCentroAtencion = strCentroAtencion;
	}
	
	
	
	/**
	 * @return the listaConceptosEgreso
	 */
	public ArrayList<ConceptosDeEgreso> getListaConceptosEgreso() {
		return listaConceptosEgreso;
	}


	/**
	 * @param listaConceptosEgreso the listaConceptosEgreso to set
	 */
	public void setListaConceptosEgreso(
			ArrayList<ConceptosDeEgreso> listaConceptosEgreso) {
		this.listaConceptosEgreso = listaConceptosEgreso;
	}


	/**
	 * @return the posArray
	 */
	public int getPosArray() {
		return posArray;
	}


	/**
	 * @param posArray the posArray to set
	 */
	public void setPosArray(int posArray) {
		this.posArray = posArray;
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
	 * @return the dto
	 */
	public ConcEgrXusuXcatencion getDto() {
		return dto;
	}


	/**
	 * @param dto the dto to set
	 */
	public void setDto(ConcEgrXusuXcatencion dto) {
		this.dto = dto;
	}


	/**
	 * @return the listaDto
	 */
	public ArrayList<ConcEgrXusuXcatencion> getListaDto() {
		return listaDto;
	}


	/**
	 * @param listaDto the listaDto to set
	 */
	public void setListaDto(ArrayList<ConcEgrXusuXcatencion> listaDto) {
		this.listaDto = listaDto;
	}
	

	/**
	 * @return the centroAtencion
	 */
	public ArrayList<CentroAtencion> getListaCentroAtencion() {
		return listaCentroAtencion;
	}


	/**
	 * @param centroAtencion the centroAtencion to set
	 */
	public void setListaCentroAtencion(ArrayList<CentroAtencion> listaCentroAtencion) {
		this.listaCentroAtencion = listaCentroAtencion;
	}

	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 * @return the mostrarFormularioIngreso
	 */
	public boolean isMostrarFormularioIngreso() {
		return mostrarFormularioIngreso;
	}

	
	/**
	 * Indica si se debe mostrar el formulario nuevo/modificar
	 * @param mostrarFormularioIngreso the mostrarFormularioIngreso to set
	 */
	public void setMostrarFormularioIngreso(boolean mostrarFormularioIngreso) {
		this.mostrarFormularioIngreso = mostrarFormularioIngreso;
	}
	
	
	
	
	

	/* METODOS */
	
	/**
	 * Reset de la forma
	 */
	public void reset()
	{
		this.dto.setActivo(ConstantesBD.acronimoSi.charAt(0));
		this.dto 						= new ConcEgrXusuXcatencion();
		this.listaDto 					= new ArrayList<ConcEgrXusuXcatencion>();
		this.patronOrdenar 				= "";
		this.esDescendente 				= "";
		this.mostrarFormularioIngreso	= false;
		this.dto.setActivo(ConstantesBD.acronimoSi.charAt(0));
		this.listaCentroAtencion		= new ArrayList<CentroAtencion>();
		this.listaTiposUsuarios			= new ArrayList<TiposUsuarios>();
		//this.strCentroAtencion		= "";
		this.strConceptoEgreso			= "";
		this.strTipoUsuario				= "";
		this.dtoH						= new HistConcEgrXusuXcatencion();
		this.listaConceptosEgreso		= new ArrayList<ConceptosDeEgreso>();
		this.listaDtoH					= new ArrayList<HistConcEgrXusuXcatencion>();
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
		if(  (estado.equals("guardar")) || (estado.equals("guardarmodificar"))  )
		{

						
			// CONCEPTO DE EGRESO
			if(dto.getConceptosDeEgreso() == null)
			{
				errores.add("error concepto_de_egreso", new ActionMessage("errors.required", "El concepto de egreso"));
				setMostrarFormularioIngreso(true);
			}
			// -----
			
			
			// FECHAS
			if(dto.getFechaInicialVigencia() == null)
			{
				errores.add("error fecha_inicial_de_vigencia", new ActionMessage("errors.required", "La fecha inicial de vigencia"));
				setMostrarFormularioIngreso(true);
			}
			
			if(dto.getFechaFinalVigencia() == null)
			{
					errores.add("error fecha_final_vigencia", new ActionMessage("errors.required", "La fecha final de vigencia"));
					setMostrarFormularioIngreso(true);
			}
			
			if( (dto.getFechaInicialVigencia() != null)  &&  (dto.getFechaFinalVigencia() != null) )
			{
				if(dto.getFechaInicialVigencia().after(dto.getFechaFinalVigencia()))
				{
					errores.add("error fecha_inicial_mayor_final", 
							new ActionMessage("errors.fechaFinalPosteriorInicial","La fecha final","la fecha inicial"));
					setMostrarFormularioIngreso(true);
				}
			}
			// -----
			
			
			// VALOR MAXIMO
			if(dto.getValorMaximoAutorizado()== null)
			{
				errores.add("error Valor_maximo_autorizado", new ActionMessage("errors.required", "El Valor M&aacute;ximo Autorizado"));
				setMostrarFormularioIngreso(true);
			}
			else {
				
				if(!UtilidadTexto.isNumber(dto.getValorMaximoAutorizado()+""))
				{
					errores.add("error valor_numerico_no_valido", new ActionMessage("errors.numDecimales", "El Valor M&aacute;ximo Autorizado"));
					setMostrarFormularioIngreso(true);
				}
				else
				{
					if(dto.getValorMaximoAutorizado().intValue()<=0)
					{
						errores.add("error valor_menor_a_cero", new ActionMessage("error.tesoreria.valorMayorCero", "El Valor M&aacute;ximo Autorizado"));
						setMostrarFormularioIngreso(true);
					}
				}
			}
			// -----
			
			
			// CONCEPTO DE EGRESO
			if( (dto.getTiposUsuarios() == null) || (UtilidadTexto.isEmpty(dto.getTiposUsuarios()+"")) )
			{
				errores.add("error tipo_de_usuario", new ActionMessage("errors.required", "El Tipo de Usuario"));
				setMostrarFormularioIngreso(true);
			}
			// -----
				
		}
		
		return errores;
	}
	
	
	
	
	
	
	// =========================================================================
	/*
	 * ESTA PARTE DEL CODIGO CORRESPONDE A LA VENTANA DE HISTORICO
	 */
	// =========================================================================
	
	/**
	 * DTO de Historicos
	 */
	private HistConcEgrXusuXcatencion dtoH	= new HistConcEgrXusuXcatencion();
	
	
	/**
	 * Lista DTO de Historicos 
	 */
	private ArrayList<HistConcEgrXusuXcatencion> listaDtoH;



	/**
	 * @return the dtoH
	 */
	public HistConcEgrXusuXcatencion getDtoH() {
		return dtoH;
	}


	/**
	 * @param dtoH the dtoH to set
	 */
	public void setDtoH(HistConcEgrXusuXcatencion dtoH) {
		this.dtoH = dtoH;
	}


	/**
	 * @return the listaDtoH
	 */
	public ArrayList<HistConcEgrXusuXcatencion> getListaDtoH() {
		return listaDtoH;
	}


	/**
	 * @param listaDtoH the listaDtoH to set
	 */
	public void setListaDtoH(ArrayList<HistConcEgrXusuXcatencion> listaDtoH) {
		this.listaDtoH = listaDtoH;
	}
	
	
	
}



