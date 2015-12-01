package com.servinte.axioma.actionForm.manejoPaciente;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.struts.validator.ValidatorForm;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;


import com.servinte.axioma.dto.comun.DtoMes;
import com.servinte.axioma.dto.manejoPaciente.UsuariosConsumidoresPresupuestoDto;
import com.servinte.axioma.dto.manejoPaciente.ViaIngresoDto;
import com.servinte.axioma.orm.ClaseInventario;
import com.servinte.axioma.orm.Convenios;
import com.servinte.axioma.orm.GruposServicios;
import com.servinte.axioma.orm.TiposIdentificacion;

import util.ConstantesBD;
import util.UtilidadCadena;
import util.UtilidadFecha;

import javax.servlet.http.HttpServletRequest;



/**
 * Fecha Julio - 2012
 * @author David Ricardo Gómez M.
 *
 */

public class UsuariosConsumidoresPresupuestoForm extends ValidatorForm 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8318171861723019438L;

	/*---------------------------------------------
	 * 				ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	/**
	 * Para manjar los logger de la clase UsuariosConsumidoresPresupuestoForm
	 */
	Logger logger = Logger.getLogger(UsuariosConsumidoresPresupuestoForm.class);
	
	/*---------------------------------------------
	 * 				FIN ATRIBUTOS LOGGER
	 ---------------------------------------------*/
	
/// indices -------------------
	
	/*---------------------------------------------------------
	 *ATRIBUTOS PARA LA BUSQUEDA DE USUARIOS CONSUMIDORES PRESUPUESTO
	 --------------------------------------------------------*/

	/**
	 * Maneja las acciones a realizar en el action
	 */
	private String estado="";
	
    /**
     * Carga los datos del select de Convenios
     */
	private ArrayList<Convenios> listaConvenios;
    /**
     * Variable que maneja el convenio seleccionado
     */
    private String convenioSeleccionado;
    /**
     * Carga las vias de ingreso
     */
    private List<ViaIngresoDto>viasIngresos;
    
    /**
     * Codigo de la Via de Ingreso para realizar la busqueda
     */
    private String viaIngreso;
    
    /**
     * Maneja la identificación y el tipo de identificación
     */
    private ArrayList<TiposIdentificacion> listaIdentificacion; 
    private String tipoIdentificacion;
    private String numeroIdentificacion;
	  /**
     * Maneja el estado de la autorizacion
     */
	private String autorizaciones;
	/**
	 * Atributo que almacena los grupos de servicios
	 */
	private ArrayList<GruposServicios> listaGruposServicios;
	
	private String grupoSeleccionado;
	/**
	 * Atributo que almacena las clases de inventarios
	 */
	private ArrayList<ClaseInventario> listaClaseInventario;
	
	private String inventarioSeleccionado;
    /**
     * Fecha Inicial
     */
    private String fechaInicial;
    
    /**
     * Fecha Final 
     */
    private String fechaFinal;
    /**
     * Valor Inicial 
     */
    private String valorInicial;
    
    /**
     * Valor Final 
     */
    private String valorFinal;
    /**
	 * Còdigo del diagnóstico en la intervenciòn
	 */
	private String diagnosticoIntervencion;
	
	/**
	 * Nombre del diagnóstico de intervención
	 */
	private String nombreDiagnostico;
	

	/**
	 * Tipo Cie del diagnóstico
	 */
	private int cieDiagnostico;

	/**
	 * tipo de salida de reporte
	 */
	private String tipoSalida = "";
	/**
	 * nombre de archivo generado
	 */
	private String nombreArchivoGenerado = "";

	/**
	 * listado usuarios
	 */
	private List<UsuariosConsumidoresPresupuestoDto> listadoUsuarios = new ArrayList<UsuariosConsumidoresPresupuestoDto>();
	/**
	 * bandera no generado
	 */
	private String noReporte = "";
	
	private String mesInicial="";
	private String agnoInicial="";
	private String mesFinal="";
	private String agnoFinal="";
	private String codigoInstitucion="";
	/**
	 * Almacena los datos de la seccion General
	 * 
	 */
	private HashMap diagnosticos = new HashMap ();
	
	/**
	 * Almacena la informacion inicial, para luego 
	 * comparar los cambios. 
	 */
	private HashMap diagnosticosClone = new HashMap ();
    private HashMap diagnosticosDefinitivos=new HashMap();
	
	/**
	 * Mapa diagnosticos presuntivos
	 */
	private HashMap diagnosticosPresuntivos=new HashMap();
	private  int numDiagnosticosDefinitivos;
	private int numDiagnosticosPresuntivos;
	private boolean diagnosticosDefinitivosBoolean;
	private boolean diagnosticosPresuntivosBoolean;

	
	
	List<DtoMes> listaMeses= new ArrayList<DtoMes>();
	List<String> listaAgnos= new ArrayList<String>();
	


	
    
	/*---------------------------------------------------------
	 * 				METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	//-------------------Estado ---------------------------------------------
	public String getEstado() {
		return estado;
	}
	public void setEstado(String estado) {
		this.estado = estado;
	}
	 /**
		 * @return the convenios
		 */
		public void setListaConvenios(ArrayList<Convenios> listaConvenios) {
			this.listaConvenios = listaConvenios;
		}
		 /**
			 * @param convenios the convenios to set
			 */
		public ArrayList<Convenios> getListaConvenios() {
			return listaConvenios;
		}
		/**
		 * @return the convenioSeleccionado
		 */
		public String getConvenioSeleccionado() {
			return convenioSeleccionado;
		}

		/**
		 * @param convenioSeleccionado the convenioSeleccionado to set
		 */
		public void setConvenioSeleccionado(String convenioSeleccionado) {
			this.convenioSeleccionado = convenioSeleccionado;
		}
		/**
		 * @return the viaIngreso
		 */
		public String getViaIngreso() {
			return viaIngreso;
		}

		/**
		 * @param viaIngreso the viaIngreso to set
		 */
		public void setViaIngreso(String viaIngreso) {
			this.viaIngreso = viaIngreso;
		}
		/**
		 * @return the viasIngresos
		 */
		public List<ViaIngresoDto> getViasIngresos() {
			return viasIngresos;
		}

		/**
		 * @param viasIngresos the viasIngresos to set
		 */
		public void setViasIngresos(List<ViaIngresoDto> viasIngresos) {
			this.viasIngresos = viasIngresos;
		}
		/**
		 * @return the viasIngresos
		 */
		public ArrayList<TiposIdentificacion> getListaIdentificacion() {
			return listaIdentificacion;
		}

		/**
		 * @param viasIngresos the viasIngresos to set
		 */
		public void setListaIdentificacion(ArrayList<TiposIdentificacion> listaIdentificacion) {
			this.listaIdentificacion = listaIdentificacion;
		}
		/**
		 * @return the autorizacion
		 */
		public String getautorizaciones() {
			return autorizaciones;
				}

		/**
		 * @param autorizacion the autorizacion to setforma.getTipoIdentificacion()
		 */
		public void setautorizaciones(String autorizaciones) {
			this.autorizaciones = autorizaciones;
		}
		/**
		 * @param the listaGruposServicios
		 */
		public ArrayList<GruposServicios> getListaGruposServicios() {
			return listaGruposServicios;
		}
		/**
		 * @param listaGruposServicios the ListaGrupoServicio to set
		 */
		public void setListaGruposServicios(
				ArrayList<GruposServicios> listaGruposServicios) {
			this.listaGruposServicios = listaGruposServicios;
		}
		/**
		 *@param thelistaClaseInventario
		 */
		public ArrayList<ClaseInventario> getListaClaseInventario() {
			return listaClaseInventario;
		}


		/**
		 * @param listaClaseInventario the listaClaseInventarios to set
		 */
		public void setListaClaseInventario(ArrayList<ClaseInventario> listaClaseInventario) {
			this.listaClaseInventario = listaClaseInventario;
		}
		/**
		 * @return the fechaFinal
		 */
		public String getFechaFinal() {
			return fechaFinal;
		}

		/**
		 * @param fechaFinal the fechaFinal to set
		 */
		public void setFechaFinal(String fechaFinal) {
			this.fechaFinal = fechaFinal;
		}

		/**
		 * @return the fechaInicial
		 */
		public String getFechaInicial() {
			return fechaInicial;
		}

		/**
		 * @param fechaInicial the fechaInicial to set
		 */
		public void setFechaInicial(String fechaInicial) {
			this.fechaInicial = fechaInicial;
		}
		
		/**
		 * @param Captura la Clase de Inventario seleccionado
		 */
		public String getInventarioSeleccionado() {
			return inventarioSeleccionado;
		}
		public void setInventarioSeleccionado(String inventarioSeleccionado) {
			this.inventarioSeleccionado = inventarioSeleccionado;
		}
		/**
		 * @param Captura la Grupo Servicio seleccionado
		 */
		public String getGrupoSeleccionado() {
			return grupoSeleccionado;
		}
		public void setGrupoSeleccionado(String grupoSeleccionado) {
			this.grupoSeleccionado = grupoSeleccionado;
		}
		/**
		 * @param Captura Numero Identificacion seleccionado
		 */
		public String getNumeroIdentificacion() {
			return numeroIdentificacion;
		}
		public void setNumeroIdentificacion(String numeroIdentificacion) {
			this.numeroIdentificacion = numeroIdentificacion;
		}
		/**
		 * @param Captura el valor inicial
		 */
		public String getValorInicial() {
			return valorInicial;
		}
		/**
		 * @param Set valor inicial
		 */
		public void setValorInicial(String valorInicial) {
			this.valorInicial = valorInicial;
		}
		/**
		 * @param Captura el valor final
		 */
		public String getValorFinal() {
			return valorFinal;
		}
		/**
		 * @param Set valor final
		 */
		public void setValorFinal(String valorFinal) {
			this.valorFinal = valorFinal;
		}
		/**
		 * @return Returns the cieDiagnostico.
		 */
		public int getCieDiagnostico()
		{
			return cieDiagnostico;
		}
		/**
		 * @param cieDiagnostico The cieDiagnostico to set.
		 */
		public void setCieDiagnostico(int cieDiagnostico)
		{
			this.cieDiagnostico = cieDiagnostico;
		}
		/**
		 * @return Returns the diagnosticoIntervencion.
		 */
		public String getDiagnosticoIntervencion()
		{
			return diagnosticoIntervencion;
		}
		/**
		 * @param diagnosticoIntervencion The diagnosticoIntervencion to set.
		 */
		public void setDiagnosticoIntervencion(String diagnosticoIntervencion)
		{
			this.diagnosticoIntervencion = diagnosticoIntervencion;
		}
		/**
		 * @return Returns the nombreDiagnostico.
		 */
		public String getNombreDiagnostico()
		{
			return nombreDiagnostico;
		}
		/**
		 * @param nombreDiagnostico The nombreDiagnostico to set.
		 */
		public void setNombreDiagnostico(String nombreDiagnostico)
		{
			this.nombreDiagnostico = nombreDiagnostico;
		}
		/**
		 * @return the tipoIdentificacion
		 */
		public String getTipoIdentificacion() {
			return tipoIdentificacion;
		}
		/**
		 * @param tipoIdentificacion the tipoIdentificacion to set
		 */
		public void setTipoIdentificacion(String tipoIdentificacion) {
			this.tipoIdentificacion = tipoIdentificacion;
		}
		/**
		 * @return the autorizaciones
		 */
		public String getAutorizaciones() {
			return autorizaciones;
		}
		/**
		 * @param autorizaciones the autorizaciones to set
		 */
		public void setAutorizaciones(String autorizaciones) {
			this.autorizaciones = autorizaciones;
		}
		/**
		 * @return the tipoSalida
		 */
		public String getTipoSalida() {
			return tipoSalida;
		}
		/**
		 * @param tipoSalida the tipoSalida to set
		 */
		public void setTipoSalida(String tipoSalida) {
			this.tipoSalida = tipoSalida;
		}
		/**
		 * @return the nombreArchivoGenerado
		 */
		public String getNombreArchivoGenerado() {
			return nombreArchivoGenerado;
		}
		/**
		 * @param nombreArchivoGenerado the nombreArchivoGenerado to set
		 */
		public void setNombreArchivoGenerado(String nombreArchivoGenerado) {
			this.nombreArchivoGenerado = nombreArchivoGenerado;
		}
		/**
		 * @return the listadoUsuarios
		 */
		public List<UsuariosConsumidoresPresupuestoDto> getListadoUsuarios() {
			return listadoUsuarios;
		}
		/**
		 * @param listadoUsuarios the listadoUsuarios to set
		 */
		public void setListadoUsuarios(
				List<UsuariosConsumidoresPresupuestoDto> listadoUsuarios) {
			this.listadoUsuarios = listadoUsuarios;
		}
		/**
		 * @return the noReporte
		 */
		public String getNoReporte() {
			return noReporte;
		}
		/**
		 * @param noReporte the noReporte to set
		 */
		public void setNoReporte(String noReporte) {
			this.noReporte = noReporte;
		}
		/**
	     * Response AJAX
	     */
		private String result;
		/**
	     * MaxDigitos
	     */
		private int numDigCaptNumId;
		private String soloNumeros;
		private String validado;
		private boolean numero=false;
		private boolean alfanum=true;
		
		/*---------------------------------------------------------
	 * 			  FIN METODOS GETTERS AND SETTERS
	 ----------------------------------------------------------*/
	
	/*-----------------------------------------------------------
	 * 			  METODOS PARA EL MANEJO DE LA FORMA
	 -----------------------------------------------------------*/
	
	public void reset ()
	{
		this.listaConvenios=new ArrayList<Convenios>();
		this.convenioSeleccionado = "";
		this.viasIngresos = new ArrayList<ViaIngresoDto>();
    	this.viaIngreso = "";
    	this.listaIdentificacion = new ArrayList<TiposIdentificacion>();
    	this.numeroIdentificacion= "";
    	this.autorizaciones = "";
    	this.listaClaseInventario = new ArrayList<ClaseInventario>();
    	this.setInventarioSeleccionado("");
		this.listaGruposServicios = new ArrayList<GruposServicios>();
		this.tipoIdentificacion="";
		this.setGrupoSeleccionado("");
		this.fechaInicial = "";
    	this.fechaFinal = "";
    	this.valorInicial = "";
    	this.valorFinal = "";
    	this.diagnosticoIntervencion="";
		this.nombreDiagnostico = "";
		this.tipoSalida="";
		this.noReporte="";
		this.mesInicial="";
		this.agnoInicial="";
		this.mesFinal="";
		this.agnoFinal="";
		this.listadoUsuarios=new ArrayList<UsuariosConsumidoresPresupuestoDto>();
		this.estado="empezar";
		this.numDigCaptNumId = 0;
		this.soloNumeros = "";	
		this.validado="";
		this.diagnosticosDefinitivosBoolean=false;
		this.numDiagnosticosDefinitivos = 1;
		this.diagnosticosDefinitivos=new HashMap();
		this.diagnosticosDefinitivos.put("numdiagval","0");

			}
	
	/*-----------------------------------------------------------
	 * 			  FIN METODOS PARA EL MANEJO DE LA FORMA
	 -----------------------------------------------------------*/
	
	

	/**
	 * Control de Errores (Validaciones)
	 */
	 public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) 
	{
		ActionErrors errores= new ActionErrors();
		if (estado.equals("exportarReporte"))
		{
			int j;
			String fechaIni="";
			String fechaFin="";
			String mesI="";
			String mesF="";
		//1)validacion requrido fecha incial
			if (this.getMesInicial().equals("-1"))
			{
		  
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar el Mes Inicial"));
				this.tipoSalida="";
			}
			else 
				if(this.getAgnoInicial().equals("-1"))
				{
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar el Año Inicial"));
				this.tipoSalida="";
				}
			    else//2)validacion de que la fecha inicial sea menor a la fecha actual
			    	{
					j= Integer.parseInt(this.getMesInicial());
					j=j+1;
					mesI="";
					if (j<=10)
					{
						mesI= "0"+String.valueOf(j);
					}
					else
					{mesI= String.valueOf(j);}
					fechaIni="01/"+ mesI+"/"+this.getAgnoInicial();
					
			    	if (!UtilidadFecha.esFechaMenorIgualQueOtraReferencia(fechaIni+"",UtilidadFecha.getFechaActual()))
			    	{			
			    		errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Inicial "+mesI+"/"+this.getAgnoInicial(), "Actual "+UtilidadFecha.getFechaActual()));
			    		this.tipoSalida="";
			    	}}
				
			//2)validacion requrido fecha Final
			if (this.getMesFinal().equals("-1"))
			{
				errores.add("descripcion",new ActionMessage("errors.required","Seleccionar el Mes Final"));
				this.tipoSalida="";
			}
			else 
				if(this.getAgnoFinal().equals("-1"))
				{
					errores.add("descripcion",new ActionMessage("errors.required","Seleccionar el Año Final"));
					this.tipoSalida="";
				}
			else//3)validacion de que la fecha final sea mayor o igual a la fecha inicial
				{
				if (!this.getAgnoFinal().equals("-1") && !this.getAgnoInicial().equals("-1") && !this.getMesFinal().equals("-1") && !this.getMesInicial().equals("-1"))
				{
				j= Integer.parseInt(this.getMesFinal());
				j=j+1;
			    mesF="";
				if (j<10)
				{
					mesF= "0"+String.valueOf(j);
				}
				else
				{mesF= String.valueOf(j);}
				fechaFin= UtilidadFecha.obtenerUltimoDiaMesPorAnio(Integer.parseInt(mesF), Integer.parseInt(this.getAgnoFinal()))+"/"+ mesF+"/"+this.getAgnoFinal();

				if ((Integer.parseInt(mesF) == UtilidadFecha.getMesAnioDiaActual("mes")) && Integer.parseInt(this.getAgnoFinal())==UtilidadFecha.getAnioActual()&& !this.getAgnoFinal().equals("-1") && !this.getAgnoInicial().equals("-1") && !this.getMesFinal().equals("-1") && !this.getMesInicial().equals("-1"))
						{
					fechaFin= UtilidadFecha.getFechaActual();
						}
			
				if(Integer.parseInt(mesF)<Integer.parseInt(mesI)&&Integer.parseInt(this.getAgnoFinal())<=Integer.parseInt(this.getAgnoInicial())&& !this.getAgnoFinal().equals("-1") && !this.getAgnoInicial().equals("-1") && !this.getMesFinal().equals("-1") && !this.getMesInicial().equals("-1"))
				{
					errores.add("", new ActionMessage("errors.fechaAnteriorIgualActual", "Final "+mesF+"/"+this.getAgnoFinal(), "Inicial "+mesI+"/"+this.getAgnoInicial()));
					this.tipoSalida="";
				}
				else//5)validacion de que la fecha final sea menor a la fecha actual
					
					
					if(Integer.parseInt(mesF)>UtilidadFecha.getMesAnioDiaActual("mes")&&Integer.parseInt(this.getAgnoFinal())>=UtilidadFecha.getAnioActual()&& !this.getAgnoFinal().equals("-1") && !this.getAgnoInicial().equals("-1") && !this.getMesFinal().equals("-1") && !this.getMesInicial().equals("-1"))
					{ 
						
						errores.add("", new ActionMessage("errors.fechaPosteriorIgualActual", "Final "+mesF+"/"+this.getAgnoFinal(), "Actual "+UtilidadFecha.getFechaActual()));
						this.tipoSalida="";
					}
					else //6) validacion de que la diferencia de meses entre la fecha inicaila y fianl sea menor o igual a 6 meses
					{long meses=0;
					try {
						meses= UtilidadFecha.obtenerDiferenciaEntreFechas(fechaIni+"", fechaFin+"", 2);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					if (meses>6)
					{
						
						errores.add("", new ActionMessage("errors.rangoMayorMeses", "entre la Fecha Inicial y la Fecha Final ","6"));
						this.tipoSalida="";
					}
						
					}}}
			}
		
		if (estado.equals("exportarReporte")&&(!(this.getValorInicial()).equals("")|| !(this.getValorFinal()).equals("")))
		{
			String valorI, valorF;
			valorI= this.getValorInicial();
			valorF= this.getValorFinal();
			double valor, valor1;
			if (valorI.equals("")) // 7) validacion de que el valor Inicial exista
				{	
				errores.add("", new ActionMessage("errors.required","El valor Inicial: "+this.getValorInicial()));
				this.tipoSalida="";
			    valor=0;}
					else{
						valor=Double.parseDouble(valorI);}
			if (valorF.equals(""))
				{
				errores.add("", new ActionMessage("errors.required","El valor Final: "+this.getValorFinal()));
				this.tipoSalida="";
				valor1=0;}
					else {
						valor1=Double.parseDouble(valorF);}
			if (!(valor>0))
				{
				errores.add("", new ActionMessage("errors.MayorQue", "El valor Inicial: "+this.getValorInicial(),"0"));
				this.tipoSalida="";
				}
					else if (!(valor1>0))
						{ 
						errores.add("", new ActionMessage("errors.MayorQue", "El valor Final: "+this.getValorFinal(),"0"));
						this.tipoSalida="";
						}
						else if(valor1<valor)
							{
							errores.add("", new ActionMessage("errors.MayorIgualQue", "El valor Final: "+this.getValorFinal(),"el valor Inicial: "+this.getValorInicial()));
							this.tipoSalida="";
							}
		}
		if (estado.equals("exportarReporte")&&(!(this.getTipoIdentificacion().equals("-1"))))
		{ 		
			
		      if (this.getNumeroIdentificacion().equals(""))
	            {
		    	  errores.add("", new ActionMessage("errors.required","El numero de identificacion: "+this.getNumeroIdentificacion()));
		    	  this.tipoSalida="";
	            }
		      else 
		      {
            for (TiposIdentificacion id:this.getListaIdentificacion())
            {
            	if (id.getAcronimo().equals(this.getTipoIdentificacion()))
            	{
            		if (id.getSoloNumeros().equals('S'))
            		{
//            			try{
//            				Long.parseLong(this.getNumeroIdentificacion());
//            				}catch (Exception e){
//            					errores.add("", new ActionMessage("errores.paciente.caracteresInvalidos", this.getNumeroIdentificacion()));
//            					this.tipoSalida="";
//            				}
            		}
            		
            	}
            }}
      			
		}
		return errores;
	}

	/**
	 * @return the listaMeses
	 */
	public List<DtoMes> getListaMeses() {
		return listaMeses;
	}
	/**
	 * @param listaMeses the listaMeses to set
	 */
	public void setListaMeses(List<DtoMes> listaMeses) {
		this.listaMeses = listaMeses;
	}
	/**
	 * @return the listaAgnos
	 */
	public List<String> getListaAgnos() {
		return listaAgnos;
	}
	/**
	 * @param listaAgnos the listaAgnos to set
	 */
	public void setListaAgnos(List<String> listaAgnos) {
		this.listaAgnos = listaAgnos;
	}
	/**
	 * @return the mesInicial
	 */
	public String getMesInicial() {
		return mesInicial;
	}
	/**
	 * @param mesInicial the mesInicial to set
	 */
	public void setMesInicial(String mesInicial) {
		this.mesInicial = mesInicial;
	}
	/**
	 * @return the agnoInicial
	 */
	public String getAgnoInicial() {
		return agnoInicial;
	}
	/**
	 * @param agnoInicial the agnoInicial to set
	 */
	public void setAgnoInicial(String agnoInicial) {
		this.agnoInicial = agnoInicial;
	}
	/**
	 * @return the mesFinal
	 */
	public String getMesFinal() {
		return mesFinal;
	}
	/**
	 * @param mesFinal the mesFinal to set
	 */
	public void setMesFinal(String mesFinal) {
		this.mesFinal = mesFinal;
	}
	/**
	 * @return the agnoFinall
	 */
	public String getAgnoFinal() {
		return agnoFinal;
	}
	/**
	 * @param agnoFinall the agnoFinall to set
	 */
	public void setAgnoFinal(String agnoFinal) {
		this.agnoFinal = agnoFinal;
	}
	/**
	 * @return the result
	 */
	public String getResult() {
		return result;
	}
	/**
	 * @param result the result to set
	 */
	public void setResult(String result) {
		this.result = result;
	}
	/**
	 * @return the numDigCaptNumId
	 */
	public int getNumDigCaptNumId() {
		return numDigCaptNumId;
	}
	/**
	 * @param numDigCaptNumId the numDigCaptNumId to set
	 */
	public void setNumDigCaptNumId(int numDigCaptNumId) {
		this.numDigCaptNumId = numDigCaptNumId;
	}
	/**
	 * @return the soloNumeros
	 */
	public String getSoloNumeros() {
		return soloNumeros;
	}
	/**
	 * @param soloNumeros the soloNumeros to set
	 */
	public void setSoloNumeros(String soloNumeros) {
		this.soloNumeros = soloNumeros;
	}
	 public UsuariosConsumidoresPresupuestoForm() {
		 this.numeroIdentificacion= "";
	}
	/**
	 * @return the codigoInstitucion
	 */
	public String getCodigoInstitucion() {
		return codigoInstitucion;
	}
	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(String codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}
	/**
	 * @return the validado
	 */
	public String getValidado() {
		return validado;
	}
	/**
	 * @param validado the validado to set
	 */
	public void setValidado(String validado) {
		this.validado = validado;
	}
	/**
	 * @return the numero
	 */
	public boolean isNumero() {
		return numero;
	}
	/**
	 * @param numero the numero to set
	 */
	public void setNumero(boolean numero) {
		this.numero = numero;
	}
	/**
	 * @return the alfanum
	 */
	public boolean isAlfanum() {
		return alfanum;
	}
	/**
	 * @param alfanum the alfanum to set
	 */
	public void setAlfanum(boolean alfanum) {
		this.alfanum = alfanum;
	}
	/**
	 * @return the diagnosticos
	 */
	public Object getDiagnosticos(String key) {
		return diagnosticos.get(key);
	}
	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticosClone(String key, Object value) {
		this.diagnosticosClone.put(key, value);
	}
	/**
	 * @return the diagnosticosClone
	 */
	public HashMap getDiagnosticosClone() {
		return diagnosticosClone;
	}
	/**
	 * @param diagnosticosClone the diagnosticosClone to set
	 */
	public void setDiagnosticosClone(HashMap diagnosticosClone) {
		this.diagnosticosClone = diagnosticosClone;
	}

	
	
	public int getNumDiagnosticosDefinitivos() {
			return numDiagnosticosDefinitivos;
	}

	
	public int getNumDiagnosticosPresuntivos() {
		return numDiagnosticosPresuntivos;
	}


	public void setNumDiagnosticosDefinitivos(int i) {
		numDiagnosticosDefinitivos = i;
	}

	
	public void setNumDiagnosticosPresuntivos(int i) {
		numDiagnosticosPresuntivos = i;
	}

	
	public void setDiagnosticoPresuntivo(String key, Object value) 
	{
		diagnosticosPresuntivos.put(key, value);
	}
	
	/**
	 * Retorna el diagnostico presuntivo (ppal o relacionado) asociado a la
	 * llave dada
	 */
	public Object getDiagnosticoPresuntivo(String key) 
	{
		return diagnosticosPresuntivos.get(key);
	}
	
	/**
	 * * Asigna un diagnostico definitivo (ppal o relacionado)
	 */
	public void setDiagnosticosDefinitivos(String key, Object value) 
	{
		diagnosticosDefinitivos.put(key, value);
	}
	
	/**
	 * Retorna el diagnostico definitivo (ppal o relacionado) asociado a la
	 * llave dada
	 */
	public Object getDiagnosticosDefinitivos(String key) 
	{
		return diagnosticosDefinitivos.get(key);
	}	
	/**
	 * @return
	 */
	public boolean isDiagnosticosDefinitivosBoolean() {
		return diagnosticosDefinitivosBoolean;
	}

	/**
	 * @return
	 */
	public boolean isDiagnosticosPresuntivosBoolean() {
		return diagnosticosPresuntivosBoolean;
	}

	/**
	 * @param b
	 */
	public void setDiagnosticosDefinitivosBoolean(boolean b) {
		diagnosticosDefinitivosBoolean = b;
	}

	/**
	 * @param b
	 */
	public void setDiagnosticosPresuntivosBoolean(boolean b) {
		diagnosticosPresuntivosBoolean = b;
	}
	/**
	 * @return the diagnosticosDefinitivos
	 */
	public HashMap getDiagnosticosDefinitivos() {
		return diagnosticosDefinitivos;
	}
	/**
	 * @param diagnosticosDefinitivos the diagnosticosDefinitivos to set
	 */
	public void setDiagnosticosDefinitivos(HashMap diagnosticosDefinitivos) {
		this.diagnosticosDefinitivos = diagnosticosDefinitivos;
	}
	


}