/*
 * Creado May 22, 2007
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * DescuentosComercialesForm
 * com.princetonsa.actionform.facturacion
 * java version "1.5.0_07"
 */
package com.princetonsa.actionform.facturacion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.axioma.util.fechas.UtilidadesFecha;

import com.princetonsa.dto.facturacion.DtoProgDescComercialConvenioContrato;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadFecha;
import util.UtilidadTexto;

/**
 * @author <a href="mailto:armando@PrincetonSA.com">Jorge Armando Osorio Velasquez</a>
 * May 22, 2007
 */
public class DescuentosComercialesForm extends ValidatorForm
{
	/**
	 * 
	 */
	private String estado;
	
	/**
	 * 
	 */
	private int indexSeleccionado;
	
	/**
	 * 
	 */
	private int posEliminar;
	
	/**
	 * 
	 */
	private int contrato;
	
	
	/**
	 * 
	 */
	private int codigoConvenio;

	
	/**
	 * 
	 */
	private HashMap viasDescuentos;
	
	/**
	 * 
	 */
	private HashMap viasDescuentosEliminados;
	
	/**
	 * 
	 */
	private HashMap viasDescuentosAnterior;
	
	
	
	/**
	 * 
	 */
	private HashMap agrupacionArticulos;
	
	
	/**
	 * 
	 */
	private HashMap agrupacionArticulosEliminados;
	
	/**
	 * 
	 */
	private HashMap articulos;
	
	/**
	 * 
	 */
	private HashMap articulosEliminados;
	
	/**
	 * 
	 */
	private HashMap agrupacionServicios;
	
	/**
	 * 
	 */
	private HashMap agrupacionServiciosEliminados;
	
	/**
	 * 
	 */
	private HashMap servicios;
	
	
	/**
	 * 
	 */
	private HashMap serviciosEliminados;
	
	/**
	 * 
	 */
	private Vector<InfoDatosString> selViasIngreso;
	
	/**
	  * 
	  */
	 private ArrayList convenios;
	 
	 /**
	  * 
	  */
	 private ArrayList contratos;
	 
	 /**
	  * ArrayList resultado de la consulta de los tipo paciente. 
	  */
	 private ArrayList<HashMap<String, Object>> tipoPacienteMap;
	
	 
	 /**
	  *almacena todos los resultados de las consultas de tipo paciente: HashMap < ArrayList<HashMap<String, Object>>>
	  */
	 private HashMap tipoPacienteMapMap ;
	 
	 
	 /**
	  * 
	  */
	 private String tipoPaciente;
	 
	 /**
	  * 
	  */
	 private int indice;
	 
	 /**
	  * 
	  */
	 private String fechaVigencia;
	 
	 /**
	  * 
	  */
	 private boolean detalleDescuentoAnterior=false;
	 
	 
	 private String nombreViaIngreso;
	 
	 private String nomTipoPaciente;
	 


	 /**
	  * CAMBIOS ANEXO 945
	  */
	 private int postArrayProgramas;
	 
	 private String utilizaProgramas;
	 
	 private String esTipoAtencioOdontologicaEspecial;
	 
	 private String listaCodigosProgramas;
	 

	
	 
	 
	 /**
	  * LISTA PROGRAMA DESCUENTO COMERCIALES CONVENIO CONTRATO 
	  */
	 private ArrayList<DtoProgDescComercialConvenioContrato> listProgDescComercial = new ArrayList<DtoProgDescComercialConvenioContrato>();
	 
	 private DtoProgDescComercialConvenioContrato dtoProgDescComercial;
	/**
	 * 
	 *
	 */
	public void reset()
	{
		this.agrupacionArticulos=new HashMap();
		this.agrupacionArticulos.put("numRegistros", "0");
		this.agrupacionArticulosEliminados=new HashMap();
		this.agrupacionArticulosEliminados.put("numRegistros", "0");
		this.articulos=new HashMap();
		this.articulos.put("numRegistros", "0");
		this.articulosEliminados=new HashMap();
		this.articulosEliminados.put("numRegistros", "0");
		this.agrupacionServicios=new HashMap();
		this.agrupacionServicios.put("numRegistros", "0");
		this.agrupacionServiciosEliminados=new HashMap();
		this.agrupacionServiciosEliminados.put("numRegistros","0");
		this.servicios=new HashMap();
		this.servicios.put("numRegistros", "0");
		this.serviciosEliminados=new HashMap();
		this.serviciosEliminados.put("numRegistros", "0");
		this.indexSeleccionado=ConstantesBD.codigoNuncaValido;
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		this.viasDescuentos=new HashMap();
		this.viasDescuentos.put("numRegistros", "0");
		this.viasDescuentosAnterior=new HashMap();
		this.viasDescuentosAnterior.put("numRegistros", "0");
		this.viasDescuentosEliminados=new HashMap();
		this.viasDescuentosEliminados.put("numRegistros", "0");
		this.selViasIngreso=new Vector<InfoDatosString>();
		this.contrato=ConstantesBD.codigoNuncaValido;
		this.codigoConvenio=ConstantesBD.codigoNuncaValido;
		this.convenios=new ArrayList();
    	this.contratos=new ArrayList();
    	this.tipoPaciente="";
		this.tipoPacienteMap=new ArrayList<HashMap<String,Object>>();
    	this.detalleDescuentoAnterior=false;

		this.indice=0;
		this.tipoPacienteMapMap=new HashMap();
		this.tipoPacienteMapMap.put("numRegistros", "0");
		this.fechaVigencia="";
		this.nombreViaIngreso="";
		this.nomTipoPaciente="";
		this.setListProgDescComercial(new ArrayList<DtoProgDescComercialConvenioContrato>());
		this.dtoProgDescComercial = new DtoProgDescComercialConvenioContrato();
		this.postArrayProgramas= ConstantesBD.codigoNuncaValido;
		this.utilizaProgramas="";
		this.esTipoAtencioOdontologicaEspecial=ConstantesBD.acronimoNo;
		this.listaCodigosProgramas="";
	
	}
	

	/**
	 * @return the esTipoAtencioOdontologicaEspecial
	 */
	public String getEsTipoAtencioOdontologicaEspecial() {
		return esTipoAtencioOdontologicaEspecial;
	}


	/**
	 * @param esTipoAtencioOdontologicaEspecial the esTipoAtencioOdontologicaEspecial to set
	 */
	public void setEsTipoAtencioOdontologicaEspecial(
			String esTipoAtencioOdontologicaEspecial) {
		this.esTipoAtencioOdontologicaEspecial = esTipoAtencioOdontologicaEspecial;
	}


	/**
	 * @return the dtoProgDescComercial
	 */
	public DtoProgDescComercialConvenioContrato getDtoProgDescComercial() {
		return dtoProgDescComercial;
	}


	/**
	 * @param dtoProgDescComercial the dtoProgDescComercial to set
	 */
	public void setDtoProgDescComercial(
			DtoProgDescComercialConvenioContrato dtoProgDescComercial) {
		this.dtoProgDescComercial = dtoProgDescComercial;
	}


	/**
	 * 
	 *
	 */
	public void resetMapasEliminacion()
	{
		this.agrupacionArticulosEliminados=new HashMap();
		this.agrupacionArticulosEliminados.put("numRegistros", "0");
		this.articulosEliminados=new HashMap();
		this.articulosEliminados.put("numRegistros", "0");
		this.agrupacionServiciosEliminados=new HashMap();
		this.agrupacionServiciosEliminados.put("numRegistros","0");
		this.serviciosEliminados=new HashMap();
		this.serviciosEliminados.put("numRegistros", "0");
		this.posEliminar=ConstantesBD.codigoNuncaValido;
		this.tipoPaciente="";
		this.tipoPacienteMap=new ArrayList<HashMap<String,Object>>();
		this.indice=0;
		this.tipoPacienteMapMap=new HashMap();
		this.tipoPacienteMapMap.put("numRegistros", "0");
		
	}

	/**
	 * Método para validar la inserción de datos
	 */
	public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
	{
		ActionErrors errores=new ActionErrors();
		if(estado.equals("guardarViaDescuento"))
		{
			for(int i=0;i<Integer.parseInt(viasDescuentos.get("numRegistros")+"");i++)
			{


				if((this.viasDescuentos.get("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia del registro "+(i+1)));
				}
				if(!UtilidadTexto.isEmpty(this.viasDescuentos.get("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.viasDescuentos.get("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.viasDescuentos.get("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					/*
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.viasDescuentos.get("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.viasDescuentos.get("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
					}
					*/
				}
				
				for(int j=0;j<i;j++)
				{

					
					if(
							((this.viasDescuentos.get("viaingreso_"+i)+"").equalsIgnoreCase(this.viasDescuentos.get("viaingreso_"+j)+"")) &&
							((this.viasDescuentos.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.viasDescuentos.get("fechavigencia_"+j)+"")) &&
							((this.viasDescuentos.get("tipopaciente_"+i)+"").equalsIgnoreCase(this.viasDescuentos.get("tipopaciente_"+j)+""))
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","El registro "+(i+1)));
						j=i;
					}
				}
			}
		}
		if(estado.equals("guardar"))
		{
			
			/**
			 * 	VALIDAR PROGRAMAS ODONTOLOGICOS 
			 */
			
			for(DtoProgDescComercialConvenioContrato listObj: this.getListProgDescComercial())
			{
				if(listObj.getPorcentaje()<=0)
				{
					errores.add("codigo", new ActionMessage("errors.required"," El Porcentaje "));
				}
				/*
				 * tarea 5705 xplanner2010
				 * no debe validar el programa requerido
				if(listObj.getDtoPrograma().getCodigo()<=0)
				{
					errores.add("codigo", new ActionMessage("errors.required"," El Programa "));
				}
				*/
				if(UtilidadTexto.isEmpty(listObj.getFechaVigencia()))
				{
					errores.add("codigo", new ActionMessage("errors.required"," La Fecha Vigencia "));
				}
				/* Comentado por tarea 4643
				 * else {
					if(listObj.getVieneBaseDatos().equals(ConstantesBD.acronimoNo)) {*/
						if(!UtilidadFecha.esFechaMenorQueOtraReferencia(UtilidadFecha.getFechaActual(), listObj.getFechaVigencia()) )
						{
							errores.add("", new ActionMessage("errors.notEspecific", "La Fecha de Vigencia debe ser Mayor a la fecha del Sistema "));
						}
				//} }
			}
			
			for(int i=0;i<Integer.parseInt(agrupacionArticulos.get("numRegistros")+"");i++)
			{
				for(int j=0;j<i;j++)
				{
					if(
							(this.agrupacionArticulos.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("fechavigencia_"+j)+"") &&
							(this.agrupacionArticulos.get("clase_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("clase_"+j)+"") &&
							(this.agrupacionArticulos.get("grupo_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("grupo_"+j)+"") &&
							(this.agrupacionArticulos.get("subgrupo_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("subgrupo_"+j)+"") &&
							(this.agrupacionArticulos.get("naturaleza_"+i)+"").equalsIgnoreCase(this.agrupacionArticulos.get("naturaleza_"+j)+"") 
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En Agrupacion Articulo: La clase \""+this.agrupacionArticulos.get("nomclase_"+i)+"\" Grupo: \""+this.agrupacionArticulos.get("nomgrupo_"+i)+"\" SubGrupo: \""+this.agrupacionArticulos.get("nomsubgrupo_"+i)+"\" Naturaleza: \""+this.agrupacionArticulos.get("nomnaturaleza_"+i)+"\""));
					}
				}
				try
				{
					if(!(agrupacionArticulos.get("porcentaje_"+i)+"").trim().equals("")&&(Double.parseDouble((agrupacionArticulos.get("porcentaje_"+i)+""))>100||Double.parseDouble((agrupacionArticulos.get("porcentaje_"+i)+""))<0))
					{
						errores.add("", new ActionMessage("errors.range","En Agrupacion Articulo: El porcentaje para el registro "+(i+1)+"","0","100"));
					}
				}
				catch(Exception e)
				{
					errores.add("", new ActionMessage("errors.float","El porcentaje para el registro "+(i+1)+" de agrupacion articulos"));
				}
				if(!UtilidadTexto.isEmpty(agrupacionArticulos.get("valor_"+i)+""))
				{
					try
					{
						Double.parseDouble((agrupacionArticulos.get("valor_"+i)+""));
					}
					catch(Exception e)
					{
						errores.add("", new ActionMessage("errors.float","El Valor para el registro "+(i+1)+" de agrupacion articulos"));
					}
				}
				if(!(agrupacionArticulos.get("porcentaje_"+i)+"").trim().equals("")&&(agrupacionArticulos.get("porcentaje_"+i)+"").trim().equals(agrupacionArticulos.get("valor_"+i)+""))
				{
					errores.add("", new ActionMessage("error.errorEnBlanco","En la Agrupacion Articulos "+(i+1)+" existe informacion ingresada para los campos valor y porcentaje simultaneamente. Por favor verificar"));
				}
				if(UtilidadTexto.isEmpty(agrupacionArticulos.get("porcentaje_"+i)+"")&&UtilidadTexto.isEmpty(agrupacionArticulos.get("valor_"+i)+""))
				{
					errores.add("", new ActionMessage("error.errorEnBlanco","En la Agrupacion Articulos "+(i+1)+" Es requerido el porcentaje o el valor. Por favor verificar"));
				}
				
				if((this.getAgrupacionArticulos("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia de la Agrupacion Articulos "+(i+1)));
				}

				if(!UtilidadTexto.isEmpty(this.getAgrupacionArticulos("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getAgrupacionArticulos("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.getAgrupacionArticulos("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getAgrupacionArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionArticulos("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getViasDescuentos("fechavigencia_"+this.getIndexSeleccionado())+"", this.getAgrupacionArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionArticulos("fechavigencia_"+i), "Vigencia definida a nivel de convenio/contrato "+this.getViasDescuentos("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
				}
				
				
			}
			for(int i=0;i<Integer.parseInt(articulos.get("numRegistros")+"");i++)
			{
				try
				{
					if(!(articulos.get("porcentaje_"+i)+"").trim().equals("")&&(Double.parseDouble((articulos.get("porcentaje_"+i)+""))>100||Double.parseDouble((articulos.get("porcentaje_"+i)+""))<0))
					{
						errores.add("", new ActionMessage("errors.range","En Articulos: El porcentaje para el registro "+(i+1)+"","0","100"));
					}
				}
				catch(Exception e)
				{
					errores.add("", new ActionMessage("errors.float","El porcentaje para el registro "+(i+1)+" de articulos"));
				}
				if(!UtilidadTexto.isEmpty((articulos.get("valor_"+i)+"")))
				{
					try
					{
						Double.parseDouble((articulos.get("valor_"+i)+""));
					}
					catch(Exception e)
					{
						errores.add("", new ActionMessage("errors.float","El Valor para el registro "+(i+1)+" de articulos"));
					}
				}
				if(!(articulos.get("porcentaje_"+i)+"").trim().equals("")&&(articulos.get("porcentaje_"+i)+"").trim().equals(articulos.get("valor_"+i)+""))
				{
					errores.add("", new ActionMessage("error.errorEnBlanco","En el Articulo "+(i+1)+" existe informacion ingresada para los campos valor y porcentaje simultaneamente. Por favor verificar"));
				}
				if(UtilidadTexto.isEmpty(articulos.get("porcentaje_"+i)+"")&&UtilidadTexto.isEmpty(articulos.get("valor_"+i)+""))
				{
					errores.add("", new ActionMessage("error.errorEnBlanco","En el Articulo "+(i+1)+" Es requerido el porcentaje o el valor. Por favor verificar"));
				}


				if((this.getArticulos("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia del Articulo "+(i+1)));
				}
				
				if(!UtilidadTexto.isEmpty(this.getArticulos("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getArticulos("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.getArticulos("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getArticulos("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getViasDescuentos("fechavigencia_"+this.getIndexSeleccionado())+"", this.getArticulos("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getArticulos("fechavigencia_"+i), "Vigencia definida a nivel de convenio/contrato "+this.getViasDescuentos("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
				}

				


			}
			for(int i=0;i<Integer.parseInt(agrupacionServicios.get("numRegistros")+"");i++)
			{
				for(int j=0;j<i;j++)
				{
					if(
							(this.agrupacionServicios.get("fechavigencia_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("fechavigencia_"+j)+"") &&
							(this.agrupacionServicios.get("tipopos_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("tipopos_"+j)+"") &&
							(this.agrupacionServicios.get("gruposervicio_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("gruposervicio_"+j)+"") &&
							(this.agrupacionServicios.get("tiposervicio_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("tiposervicio_"+j)+"") &&
							(this.agrupacionServicios.get("especialidad_"+i)+"").equalsIgnoreCase(this.agrupacionServicios.get("especialidad_"+j)+"")
						)
					{
						errores.add("", new ActionMessage("errors.yaExiste","En Agrupacion Servicios: El Grupo: \""+this.agrupacionServicios.get("descgruposervicio_"+i)+"\" Tipo Servicio: \""+this.agrupacionServicios.get("nomtiposervicio_"+i)+"\" Especialidad: \""+this.agrupacionServicios.get("nomespecialidad_"+i)+"\" Tipo pos:  \""+this.agrupacionServicios.get("tipopos_"+i)+"\""));
					}
				}
				try
				{
					if(!(agrupacionServicios.get("porcentaje_"+i)+"").trim().equals("")&&(Double.parseDouble((agrupacionServicios.get("porcentaje_"+i)+""))>100||Double.parseDouble((agrupacionServicios.get("porcentaje_"+i)+""))<0))
					{
						errores.add("", new ActionMessage("errors.range","En Agrupacion Servicios: El porcentaje para el registro "+(i+1)+"","0","100"));
					}
				}
				catch(Exception e)
				{
					errores.add("", new ActionMessage("errors.float","El porcentaje para el registro "+(i+1)+" de agrupacion servicios"));
				}
				
				if(!UtilidadTexto.isEmpty((agrupacionServicios.get("valor_"+i)+"")))
				{
					try
					{
						Double.parseDouble((agrupacionServicios.get("valor_"+i)+""));
					}
					catch(Exception e)
					{
						errores.add("", new ActionMessage("errors.float","El Valor para el registro "+(i+1)+" de agrupacion servicios"));
					}
				}
				if(!(agrupacionServicios.get("porcentaje_"+i)+"").trim().equals("")&&(agrupacionServicios.get("porcentaje_"+i)+"").trim().equals(agrupacionServicios.get("valor_"+i)+""))
				{
					errores.add("", new ActionMessage("error.errorEnBlanco","En la Agrupacion Servicios "+(i+1)+" existe informacion ingresada para los campos valor y porcentaje simultaneamente. Por favor verificar"));
				}
				if(UtilidadTexto.isEmpty(agrupacionServicios.get("porcentaje_"+i)+"")&&UtilidadTexto.isEmpty(agrupacionServicios.get("valor_"+i)+""))
				{
					errores.add("", new ActionMessage("error.errorEnBlanco","En la Agrupacion Servicios "+(i+1)+" Es requerido el porcentaje o el valor. Por favor verificar"));
				}
				

				if((this.getAgrupacionServicios("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia de la Agrupacion Servicio "+(i+1)));
				}
				
				
				if(!UtilidadTexto.isEmpty(this.getAgrupacionServicios("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getAgrupacionServicios("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.getAgrupacionServicios("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getAgrupacionServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionServicios("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getViasDescuentos("fechavigencia_"+this.getIndexSeleccionado())+"", this.getAgrupacionServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getAgrupacionServicios("fechavigencia_"+i), "Vigencia definida a nivel de convenio/contrato "+this.getViasDescuentos("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
				}

			}
			for(int i=0;i<Integer.parseInt(servicios.get("numRegistros")+"");i++)
			{
				try
				{
					if(!(servicios.get("porcentaje_"+i)+"").trim().equals("")&&(Double.parseDouble((servicios.get("porcentaje_"+i)+""))>100||Double.parseDouble((servicios.get("porcentaje_"+i)+""))<0))
					{
						errores.add("", new ActionMessage("errors.range","En Servicios: El porcentaje para el registro "+(i+1)+"","0","100"));
					}
				}
				catch(Exception e)
				{
					errores.add("", new ActionMessage("errors.float","El porcentaje para el registro "+(i+1)+" de servicios"));
				}
				if(!UtilidadTexto.isEmpty((servicios.get("valor_"+i)+"")))
				{
					try
					{
						Double.parseDouble((servicios.get("valor_"+i)+""));
					}
					catch(Exception e)
					{
						errores.add("", new ActionMessage("errors.float","El Valor para el registro "+(i+1)+" de servicios"));
					}
				}
				if(!(servicios.get("porcentaje_"+i)+"").trim().equals("")&&(servicios.get("porcentaje_"+i)+"").trim().equals(servicios.get("valor_"+i)+""))
				{
					errores.add("", new ActionMessage("error.errorEnBlanco","En el Servicio "+(i+1)+" existe informacion ingresada para los campos valor y porcentaje simultaneamente. Por favor verificar"));
				}
				if(UtilidadTexto.isEmpty(servicios.get("porcentaje_"+i)+"")&&UtilidadTexto.isEmpty(servicios.get("valor_"+i)+""))
				{
					errores.add("", new ActionMessage("error.errorEnBlanco","En el Servicio "+(i+1)+" Es requerido el porcentaje o el valor. Por favor verificar"));
				}
				

				if((this.getServicios("fechavigencia_"+i)+"").trim().equals(""))
				{
					errores.add("codigo", new ActionMessage("errors.required","La Fecha de Vigencia del Servicio "+(i+1)));
				}
				
				if(!UtilidadTexto.isEmpty(this.getServicios("fechavigencia_"+i)+""))
				{
					boolean centinelaErrorFechas=false;
					if(!UtilidadFecha.esFechaValidaSegunAp(this.getServicios("fechavigencia_"+i)+""))
					{
						errores.add("", new ActionMessage("errors.formatoFechaInvalido", "Vigencia "+this.getServicios("fechavigencia_"+i)));
						centinelaErrorFechas=true;
					}
					if(!centinelaErrorFechas)
					{
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(UtilidadFecha.getFechaActual(), this.getServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getServicios("fechavigencia_"+i), "Actual "+UtilidadFecha.getFechaActual()));
						}
						if(!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(this.getViasDescuentos("fechavigencia_"+this.getIndexSeleccionado())+"", this.getServicios("fechavigencia_"+i)+""))
						{
							errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "de Vigencia "+this.getServicios("fechavigencia_"+i), "Vigencia detalle "+this.getViasDescuentos("fechavigencia_"+this.getIndexSeleccionado())));
						}
					}
				}
			}
			
		}
		return errores;
	}

	
	
	
	public int  getNumTipoPacienteMap() {
		return this.tipoPacienteMap.size();
	}
	
	
	
	/**
	 * 
	 * @return
	 */
	public String getEstado()
	{
		return estado;
	}

	/**
	 * 
	 * @param estado
	 */
	public void setEstado(String estado)
	{
		this.estado = estado;
	}


	/**
	 * @return the agrupacionArticulos
	 */
	public HashMap getAgrupacionArticulos()
	{
		return agrupacionArticulos;
	}

	/**
	 * @param agrupacionArticulos the agrupacionArticulos to set
	 */
	public void setAgrupacionArticulos(HashMap agrupacionArticulos)
	{
		this.agrupacionArticulos = agrupacionArticulos;
	}

	/**
	 * @return the agrupacionServicios
	 */
	public HashMap getAgrupacionServicios()
	{
		return agrupacionServicios;
	}

	/**
	 * @param agrupacionServicios the agrupacionServicios to set
	 */
	public void setAgrupacionServicios(HashMap agrupacionServicios)
	{
		this.agrupacionServicios = agrupacionServicios;
	}

	/**
	 * @return the articulos
	 */
	public HashMap getArticulos()
	{
		return articulos;
	}

	/**
	 * @param articulos the articulos to set
	 */
	public void setArticulos(HashMap articulos)
	{
		this.articulos = articulos;
	}

	/**
	 * @return the servicios
	 */
	public HashMap getServicios()
	{
		return servicios;
	}

	/**
	 * @return the utilizaProgramas
	 */
	public String getUtilizaProgramas() {
		return utilizaProgramas;
	}


	/**
	 * @param utilizaProgramas the utilizaProgramas to set
	 */
	public void setUtilizaProgramas(String utilizaProgramas) {
		this.utilizaProgramas = utilizaProgramas;
	}


	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(HashMap servicios)
	{
		this.servicios = servicios;
	}
	

	/**
	 * @return the agrupacionArticulos
	 */
	public Object getAgrupacionArticulos(String key)
	{
		return agrupacionArticulos.get(key);
	}

	/**
	 * @param agrupacionArticulos the agrupacionArticulos to set
	 */
	public void setAgrupacionArticulos(String key,Object value)
	{
		this.agrupacionArticulos.put(key, value);
	}

	/**
	 * @return the agrupacionServicios
	 */
	public Object getAgrupacionServicios(String key)
	{
		return agrupacionServicios.get(key);
	}

	/**
	 * @param agrupacionServicios the agrupacionServicios to set
	 */
	public void setAgrupacionServicios(String key,Object value)
	{
		this.agrupacionServicios.put(key, value);
	}

	/**
	 * @return the articulos
	 */
	public Object getArticulos(String key)
	{
		return articulos.get(key);
	}

	/**
	 * @param articulos the articulos to set
	 */
	public void setArticulos(String key,Object value)
	{
		this.articulos.put(key, value);
	}

	/**
	 * @return the servicios
	 */
	public Object getServicios(String key)
	{
		return servicios.get(key);
	}

	/**
	 * @param servicios the servicios to set
	 */
	public void setServicios(String key,Object value)
	{
		this.servicios.put(key, value);
	}

	/**
	 * @return the indexSeleccionado
	 */
	public int getIndexSeleccionado()
	{
		return indexSeleccionado;
	}

	/**
	 * @param indexSeleccionado the indexSeleccionado to set
	 */
	public void setIndexSeleccionado(int indexSeleccionado)
	{
		this.indexSeleccionado = indexSeleccionado;
	}


	/**
	 * @return the agrupacionArticulosEliminados
	 */
	public HashMap getAgrupacionArticulosEliminados()
	{
		return agrupacionArticulosEliminados;
	}

	/**
	 * @param agrupacionArticulosEliminados the agrupacionArticulosEliminados to set
	 */
	public void setAgrupacionArticulosEliminados(
			HashMap agrupacionArticulosEliminados)
	{
		this.agrupacionArticulosEliminados = agrupacionArticulosEliminados;
	}


	/**
	 * @return the posEliminar
	 */
	public int getPosEliminar()
	{
		return posEliminar;
	}

	/**
	 * @param posEliminar the posEliminar to set
	 */
	public void setPosEliminar(int posEliminar)
	{
		this.posEliminar = posEliminar;
	}

	/**
	 * @return the articulosEliminados
	 */
	public HashMap getArticulosEliminados()
	{
		return articulosEliminados;
	}

	/**
	 * @param articulosEliminados the articulosEliminados to set
	 */
	public void setArticulosEliminados(HashMap articulosEliminados)
	{
		this.articulosEliminados = articulosEliminados;
	}

	/**
	 * @return the agrupacionServiciosEliminados
	 */
	public HashMap getAgrupacionServiciosEliminados()
	{
		return agrupacionServiciosEliminados;
	}

	/**
	 * @param agrupacionServiciosEliminados the agrupacionServiciosEliminados to set
	 */
	public void setAgrupacionServiciosEliminados(
			HashMap agrupacionServiciosEliminados)
	{
		this.agrupacionServiciosEliminados = agrupacionServiciosEliminados;
	}

	/**
	 * @return the serviciosEliminados
	 */
	public HashMap getServiciosEliminados()
	{
		return serviciosEliminados;
	}

	/**
	 * @param serviciosEliminados the serviciosEliminados to set
	 */
	public void setServiciosEliminados(HashMap serviciosEliminados)
	{
		this.serviciosEliminados = serviciosEliminados;
	}


	
	/**
	 * @return the selViasIngreso
	 */
	public Vector<InfoDatosString> getSelViasIngreso()
	{
		return selViasIngreso;
	}


	/**
	 * @param selViasIngreso the selViasIngreso to set
	 */
	public void setSelViasIngreso(Vector<InfoDatosString> selViasIngreso)
	{
		this.selViasIngreso = selViasIngreso;
	}


	/**
	 * @return the contrato
	 */
	public int getContrato()
	{
		return contrato;
	}


	/**
	 * @param contrato the contrato to set
	 */
	public void setContrato(int contrato)
	{
		this.contrato = contrato;
	}


	
	/**
	 * @return the viasDescuentos
	 */
	public HashMap getViasDescuentos()
	{
		return viasDescuentos;
	}


	/**
	 * @param viasDescuentos the viasDescuentos to set
	 */
	public void setViasDescuentos(HashMap viasDescuentos)
	{
		this.viasDescuentos = viasDescuentos;
	}

	/**
	 * @return the viasDescuentos
	 */
	public Object getViasDescuentos( String key)
	{
		return viasDescuentos.get(key);
	}


	/**
	 * @param viasDescuentos the viasDescuentos to set
	 */
	public void setViasDescuentos(String key,Object value)
	{
		this.viasDescuentos.put(key, value);
	}


	/**
	 * @return the viasDescuentosEliminados
	 */
	public HashMap getViasDescuentosEliminados()
	{
		return viasDescuentosEliminados;
	}


	/**
	 * @param viasDescuentosEliminados the viasDescuentosEliminados to set
	 */
	public void setViasDescuentosEliminados(HashMap viasDescuentosEliminados)
	{
		this.viasDescuentosEliminados = viasDescuentosEliminados;
	}


	/**
	 * @return the codigoConvenio
	 */
	public int getCodigoConvenio()
	{
		return codigoConvenio;
	}


	/**
	 * @param codigoConvenio the codigoConvenio to set
	 */
	public void setCodigoConvenio(int codigoConvenio)
	{
		this.codigoConvenio = codigoConvenio;
	}


	/**
	 * @return the contratos
	 */
	public ArrayList getContratos()
	{
		return contratos;
	}


	/**
	 * @param contratos the contratos to set
	 */
	public void setContratos(ArrayList contratos)
	{
		this.contratos = contratos;
	}


	/**
	 * @return the convenios
	 */
	public ArrayList getConvenios()
	{
		return convenios;
	}


	/**
	 * @param convenios the convenios to set
	 */
	public void setConvenios(ArrayList convenios)
	{
		this.convenios = convenios;
	}


	/**
	 * @return the tipoPaciente
	 */
	public String getTipoPaciente() {
		return tipoPaciente;
	}


	/**
	 * @param tipoPaciente the tipoPaciente to set
	 */
	public void setTipoPaciente(String tipoPaciente) {
		this.tipoPaciente = tipoPaciente;
	}


	/**
	 * @return the tipoPacienteMap
	 */
	public ArrayList<HashMap<String, Object>> getTipoPacienteMap() {
		return tipoPacienteMap;
	}


	/**
	 * @param tipoPacienteMap the tipoPacienteMap to set
	 */
	public void setTipoPacienteMap(
			ArrayList<HashMap<String, Object>> tipoPacienteMap) {
		this.tipoPacienteMap = tipoPacienteMap;
	}


	/**
	 * @return the indice
	 */
	public int getIndice() {
		return indice;
	}


	/**
	 * @param indice the indice to set
	 */
	public void setIndice(int indice) {
		this.indice = indice;
	}


	/**
	 * @return the tipoPacienteMapMap
	 */
	public HashMap getTipoPacienteMapMap() {
		return tipoPacienteMapMap;
	}


	/**
	 * @param tipoPacienteMapMap the tipoPacienteMapMap to set
	 */
	public void setTipoPacienteMapMap(HashMap tipoPacienteMapMap) {
		this.tipoPacienteMapMap = tipoPacienteMapMap;
	}
	
	
	
	/**
	 * @return the viasDescuentos
	 */
	public Object getTipoPacienteMapMap( String key)
	{
		return tipoPacienteMapMap.get(key);
	}


	/**
	 * @param viasDescuentos the viasDescuentos to set
	 */
	public void setTipoPacienteMapMap(String key,Object value)
	{
		this.tipoPacienteMapMap.put(key, value);
	}


	public String getFechaVigencia() {
		return fechaVigencia;
	}


	public void setFechaVigencia(String fechaVigencia) {
		this.fechaVigencia = fechaVigencia;
	}


	public boolean isDetalleDescuentoAnterior() {
		return detalleDescuentoAnterior;
	}


	public void setDetalleDescuentoAnterior(boolean detalleDescuentoAnterior) {
		this.detalleDescuentoAnterior = detalleDescuentoAnterior;
	}


	public HashMap getViasDescuentosAnterior() {
		return viasDescuentosAnterior;
	}


	public void setViasDescuentosAnterior(HashMap viasDescuentosAnterior) {
		this.viasDescuentosAnterior = viasDescuentosAnterior;
	}


	public String getNombreViaIngreso() {
		return nombreViaIngreso;
	}


	public void setNombreViaIngreso(String nombreViaIngreso) {
		this.nombreViaIngreso = nombreViaIngreso;
	}


	public String getNomTipoPaciente() {
		return nomTipoPaciente;
	}


	public void setNomTipoPaciente(String nomTipoPaciente) {
		this.nomTipoPaciente = nomTipoPaciente;
	}


	public void setListProgDescComercial(ArrayList<DtoProgDescComercialConvenioContrato> listProgDescComercial) {
		this.listProgDescComercial = listProgDescComercial;
		
	}


	public ArrayList<DtoProgDescComercialConvenioContrato> getListProgDescComercial() {
		return listProgDescComercial;
	}


	/**
	 * @return the postArrayProgramas
	 */
	public int getPostArrayProgramas() {
		return postArrayProgramas;
	}


	/**
	 * @param postArrayProgramas the postArrayProgramas to set
	 */
	public void setPostArrayProgramas(int postArrayProgramas) {
		this.postArrayProgramas = postArrayProgramas;
	}


	public void setListaCodigosProgramas(String listaCodigosProgramas) {
		this.listaCodigosProgramas = listaCodigosProgramas;
	}


	public String getListaCodigosProgramas() {
		return listaCodigosProgramas;
	}
	
	
	
	

	
	
}
