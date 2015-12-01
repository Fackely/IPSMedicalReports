package com.princetonsa.actionform.odontologia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.validator.ValidatorForm;
import org.axioma.util.log.Log4JManager;

import util.ConstantesBD;
import util.InfoDatosInt;

import com.princetonsa.action.odontologia.ServicioHonorarioAction;
import com.princetonsa.dto.manejoPaciente.DtoCentrosAtencion;
import com.princetonsa.dto.odontologia.DtoDetalleAgrupacionHonorarios;
import com.princetonsa.dto.odontologia.DtoDetalleServicioHonorarios;
import com.princetonsa.dto.odontologia.DtoGrupoTipoServ;
import com.princetonsa.dto.odontologia.DtoServicioHonorarios;
import com.princetonsa.mundo.odontologia.DetalleAgrupacionHonorario;
import com.princetonsa.mundo.odontologia.DetalleServicioHonorario;
import com.princetonsa.mundo.odontologia.ServicioHonorario;
import com.servinte.axioma.fwk.exception.IPSException;

/**
 * 
 * @author axioma
 *
 */
public class ServicioHonorariosForm extends ValidatorForm 
{
	
	
	private Logger logger = Logger.getLogger(ServicioHonorarioAction.class);
	///////////////////////PRIMERA PAGINA - ENCABEZADO DETALLE
	/**
	 * 
	 */
	private DtoServicioHonorarios dtoServicioHonorario;
	
	/**
	 * 
	 */
	private ArrayList<DtoServicioHonorarios> listaServiciosHonorario = new ArrayList<DtoServicioHonorarios>();
	
	//////////////////////////FIN PRIMERA PAGINA
	
	///////////////////////SEGUNDA PAGINA - ENCABEZADO DETALLE
	
	/**
	 * hashet para mostrar el agrupamiento en el jsp
	 */
	private ArrayList<DtoGrupoTipoServ> grupoTipoServicioAuxVista= new ArrayList<DtoGrupoTipoServ>();
	
	/**
	 * 
	 */
	/***
	 * 
	 * 
	 * 
	 */
	
	
	private DtoDetalleAgrupacionHonorarios dtoDetalleAgrupacionHonorario;
	
	/**
	 * 
	 */
	private ArrayList<DtoDetalleAgrupacionHonorarios> listaDetalleAgrupacionHonorario = new ArrayList<DtoDetalleAgrupacionHonorarios>();
	
	/**
	 * 
	 */
	private DtoDetalleServicioHonorarios dtoDetalleServicioHonorario;
	
	/**
	 * 
	 */
	private ArrayList<DtoDetalleServicioHonorarios> listaDetalleServicioHonorario = new ArrayList<DtoDetalleServicioHonorarios>();
	
	//////////////////////////FIN SEGUNDA PAGINA
	
	/**
	 * 
	 */
	private HashMap esquemasTarifarios = new HashMap();

	/**
	 * 
	 */
	private ArrayList<DtoCentrosAtencion> listaCentrosAtencion= new ArrayList<DtoCentrosAtencion>();
	
	
	/**
     * 
     */
	private HashMap especialidades = new HashMap();
	/**
	 * 
	 */
    private ArrayList<HashMap<String, Object>> listaTiposDeServicios = new ArrayList<HashMap<String, Object>>();
	/**
	 * 
	 */
    private ArrayList<HashMap<String,Object>> listaGruposDeServicios = new ArrayList<HashMap<String,Object> >(); 
    /**
     * 
     * 
     */
	private String estado;

	/***
	 * 
	 */
	private String patronOrdenar;

	/**
	 * 
	 */
	private int posArray;

	/**
	 * 
	 */
	private int posArrayDetalle;
	/**
	 * 
	 * 
	 */
	private int posArrayAgrupacion;
	
	/**
	 * 
	 * 
	 */
	private ArrayList<DtoServicioHonorarios> resultadoBusqueda = new ArrayList<DtoServicioHonorarios>();

	/**
	 * 
	 */
	private ArrayList<InfoDatosInt> grupoVistaServicio = new ArrayList<InfoDatosInt>();
	
	/**
	 * metodo que resetea
	 */
	
	
	
	public void reset() {
		this.dtoServicioHonorario = new DtoServicioHonorarios();
		this.listaServiciosHonorario = new ArrayList<DtoServicioHonorarios>();
		this.dtoDetalleAgrupacionHonorario=new DtoDetalleAgrupacionHonorarios();
		this.listaDetalleAgrupacionHonorario= new ArrayList<DtoDetalleAgrupacionHonorarios>();
		this.dtoDetalleServicioHonorario= new DtoDetalleServicioHonorarios();
		this.listaDetalleServicioHonorario= new ArrayList<DtoDetalleServicioHonorarios>();
		this.listaTiposDeServicios = new ArrayList<HashMap<String, Object>>();
		this.listaGruposDeServicios = new ArrayList<HashMap <String, Object>>();
		this.resultadoBusqueda = new ArrayList<DtoServicioHonorarios>();
		this.posArray = ConstantesBD.codigoNuncaValido;
		this.posArrayDetalle = ConstantesBD.codigoNuncaValido;
		this.posArrayAgrupacion = ConstantesBD.codigoNuncaValido;
		this.patronOrdenar = "";
		this.esquemasTarifarios= new HashMap();
		this.listaCentrosAtencion= new ArrayList<DtoCentrosAtencion>();
	    this.especialidades = new HashMap();
	    this.grupoTipoServicioAuxVista= new ArrayList<DtoGrupoTipoServ>();
	    this.grupoVistaServicio = new ArrayList<InfoDatosInt>();
	    
	}
	
			
	/**
	 * Validate the properties that have been set from this HTTP request, and
	 * return an <code>ActionErrors</code> object that encapsulates any
	 * validation errors that have been found. If no errors are found, return
	 * <code>null</code> or an <code>ActionErrors</code> object with no recorded
	 * error messages.
	 * 
	 * @param mapping
	 *            The mapping used to select this instance
	 * @param request
	 *            The servlet request we are processing
	 */
	@Override
	public ActionErrors validate(ActionMapping mapping,
								HttpServletRequest request) 
	{
		ActionErrors errores = new ActionErrors();

		if(this.getEstado().equals("guardar") || this.getEstado().equals("guardarModificar"))
		{
			
			if(this.getDtoServicioHonorario().getEsquemaTarifario().getCodigo()==0)
			{
				errores.add("", new ActionMessage("errors.required", "El Esquema Tarifario o la opcion Todos"));
			}
			if(this.getDtoServicioHonorario().getCentroAtencion().getCodigo()==0)
			{
				errores.add("", new ActionMessage("errors.required", "El Centro Atención o la opción Todos"));
			}
			
			DtoServicioHonorarios dtoExistencia = new DtoServicioHonorarios();  
			dtoExistencia.setEsquemaTarifario(new InfoDatosInt(this.getDtoServicioHonorario().getEsquemaTarifario().getCodigo() , this.getDtoServicioHonorario().getEsquemaTarifario().getDescripcion()));
			dtoExistencia.setCentroAtencion(new InfoDatosInt(this.getDtoServicioHonorario().getCentroAtencion().getCodigo(), this.getDtoServicioHonorario().getCentroAtencion().getNombre()));
			
			if(ServicioHonorario.cargar(dtoExistencia).size()>0 && errores.isEmpty())
			{
				errores.add("", new ActionMessage("errors.yaExiste", "El Esquema Tarifario Y Centro Atención seleccionado"));
			}
			
			if(!errores.isEmpty())
			{
				if(this.getEstado().equals("guardar"))
					this.setEstado("mostrarErroresGuardar");
				if(this.getEstado().equals("guardarModificar"))
					this.setEstado("mostrarErroresGuardarModificar");
			}
		}
		else if(this.getEstado().equals("guardar_agrupacion") || this.getEstado().equals("guardarModificar_agrupacion"))
		{
			if(this.getDtoDetalleAgrupacionHonorario().getPorcentajeParticipacion() > 100)
			{
				errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual", "Porcentaje de participacion","100"));
			}
			else 
			{
				if(DetalleAgrupacionHonorario.existeDetalleAgrupacion(this.getDtoDetalleAgrupacionHonorario().getCodigoHonorario(), this.getDtoDetalleAgrupacionHonorario().getGrupoServicio().getCodigo() , this.getDtoDetalleAgrupacionHonorario().getTipoServicio().getCodigo(), this.getDtoDetalleAgrupacionHonorario().getEspecialidad().getCodigo(), this.getDtoDetalleAgrupacionHonorario().getCodigo()))
				{
					errores.add("", new ActionMessage("errors.yaExiste", "Especialidad en tipo Grupo servicios"));
				}	
			} 
			if(!errores.isEmpty())
			{
				if(this.getEstado().equals("guardar_agrupacion"))
					this.setEstado("mostrarErroresGuardarAgrupacion");
				if(this.getEstado().equals("guardarModificar_agrupacion"))
					this.setEstado("mostrarErroresGuardarModificarAgrupacion");
			}
		}
		else if(this.getEstado().equals("guardar_detalle") || this.getEstado().equals("guardarModificar_detalle") )
		{
			if(this.getDtoDetalleServicioHonorario().getPorcentajeParticipacion() > 100)
			{
				errores.add("", new ActionMessage("errors.debeSerNumeroMenorIgual", "Porcentaje de participacion","100"));
			}
			else 
			{
			    if(DetalleServicioHonorario.existeDetalleServicio(this.getDtoDetalleServicioHonorario().getCodigoHonorario(),this.getDtoDetalleServicioHonorario().getServicio().getCodigo(),this.getDtoDetalleServicioHonorario().getEspecialidad().getCodigo(),this.getDtoDetalleServicioHonorario().getCodigo()))
				{
					errores.add("", new ActionMessage("errors.yaExiste", "Especialidad en servicios"));
				}	
			} 
				
			if(!errores.isEmpty())
			{
				if(this.getEstado().equals("guardar_detalle"))
					this.setEstado("mostrarErroresGuardarDetalle");
				if(this.getEstado().equals("guardarModificar_detalle"))
					this.setEstado("mostrarErroresGuardarModificarDetalle");
			}
		}
		else if(this.getEstado().equals("eliminar"))
		{
			DtoServicioHonorarios dto= new DtoServicioHonorarios();
			dto.setCodigo(this.getListaServiciosHonorario().get(this.getPosArray()).getCodigo());
			
			try {
				if(ServicioHonorario.existenDetalles(dto) > 0)
				{
					
					errores.add("", new ActionMessage("errors.existeDetalle", "Honorario Especialidad/Servicio"));
				}
			} catch (IPSException e) {
				Log4JManager.error(e.getMessage(), e);
			}
		}
		
		return errores;
	}// fin
	
	/**
	 * @return the esquemasTarifarios
	 */
	@SuppressWarnings("unchecked")
	public HashMap getEsquemasTarifarios() {
		return esquemasTarifarios;
	}

	/**
	 * @param esquemasTarifarios the esquemasTarifarios to set
	 */
	@SuppressWarnings("unchecked")
	public void setEsquemasTarifarios(HashMap esquemasTarifarios) {
		this.esquemasTarifarios = esquemasTarifarios;
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
	 * @return the listaServiciosHonorario
	 */
	public ArrayList<DtoServicioHonorarios> getListaServiciosHonorario() {
		return listaServiciosHonorario;
	}

	/**
	 * @param listaServiciosHonorario the listaServiciosHonorario to set
	 */
	public void setListaServiciosHonorario(
			ArrayList<DtoServicioHonorarios> listaServiciosHonorario) {
		this.listaServiciosHonorario = listaServiciosHonorario;
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
	 * @return the resultadoBusqueda
	 */
	public ArrayList<DtoServicioHonorarios> getResultadoBusqueda() {
		return resultadoBusqueda;
	}

	/**
	 * @param resultadoBusqueda the resultadoBusqueda to set
	 */
	public void setResultadoBusqueda(
			ArrayList<DtoServicioHonorarios> resultadoBusqueda) {
		this.resultadoBusqueda = resultadoBusqueda;
	}

	/**
	 * @return the dtoServicioHonorario
	 */
	public DtoServicioHonorarios getDtoServicioHonorario() {
		return dtoServicioHonorario;
	}

	/**
	 * @param dtoServicioHonorario the dtoServicioHonorario to set
	 */
	public void setDtoServicioHonorario(DtoServicioHonorarios dtoServicioHonorario) {
		this.dtoServicioHonorario = dtoServicioHonorario;
	}


	public int getPosArrayDetalle() {
		return posArrayDetalle;
	}


	public void setPosArrayDetalle(int posArrayDetalle) {
		this.posArrayDetalle = posArrayDetalle;
	}


	public int getPosArrayAgrupacion() {
		return posArrayAgrupacion;
	}


	public void setPosArrayAgrupacion(int posArrayAgrupacion) {
		this.posArrayAgrupacion = posArrayAgrupacion;
	}


	/**
	 * @return the dtoDetalleAgrupacionHonorario
	 */
	public DtoDetalleAgrupacionHonorarios getDtoDetalleAgrupacionHonorario() {
		return dtoDetalleAgrupacionHonorario;
	}


	/**
	 * @param dtoDetalleAgrupacionHonorario the dtoDetalleAgrupacionHonorario to set
	 */
	public void setDtoDetalleAgrupacionHonorario(
			DtoDetalleAgrupacionHonorarios dtoDetalleAgrupacionHonorario) {
		this.dtoDetalleAgrupacionHonorario = dtoDetalleAgrupacionHonorario;
	}


	/**
	 * @return the listaDetalleAgrupacionHonorario
	 */
	public ArrayList<DtoDetalleAgrupacionHonorarios> getListaDetalleAgrupacionHonorario() {
		return listaDetalleAgrupacionHonorario;
	}


	/**
	 * @param listaDetalleAgrupacionHonorario the listaDetalleAgrupacionHonorario to set
	 */
	public void setListaDetalleAgrupacionHonorario(
			ArrayList<DtoDetalleAgrupacionHonorarios> listaDetalleAgrupacionHonorario) {
		this.listaDetalleAgrupacionHonorario = listaDetalleAgrupacionHonorario;
	}


	/**
	 * @return the dtoDetalleServicioHonorario
	 */
	public DtoDetalleServicioHonorarios getDtoDetalleServicioHonorario() {
		return dtoDetalleServicioHonorario;
	}


	/**
	 * @param dtoDetalleServicioHonorario the dtoDetalleServicioHonorario to set
	 */
	public void setDtoDetalleServicioHonorario(
			DtoDetalleServicioHonorarios dtoDetalleServicioHonorario) {
		this.dtoDetalleServicioHonorario = dtoDetalleServicioHonorario;
	}


	/**
	 * @return the listaDetalleServicioHonorario
	 */
	public ArrayList<DtoDetalleServicioHonorarios> getListaDetalleServicioHonorario() {
		return listaDetalleServicioHonorario;
	}


	/**
	 * @param listaDetalleServicioHonorario the listaDetalleServicioHonorario to set
	 */
	public void setListaDetalleServicioHonorario(
			ArrayList<DtoDetalleServicioHonorarios> listaDetalleServicioHonorario) {
		this.listaDetalleServicioHonorario = listaDetalleServicioHonorario;
	}


	/**
	 * @return the especialidades
	 */
	public HashMap getEspecialidades() {
		return especialidades;
	}


	/**
	 * @param especialidades the especialidades to set
	 */
	public void setEspecialidades(HashMap especialidades) {
		this.especialidades = especialidades;
	}


	/**
	 * @return 
	 * @return the listaTiposDeServicios
	 */
	public ArrayList<HashMap<String, Object>> getListaTiposDeServicios() {
		return listaTiposDeServicios;
	}


	/**
	 * @param arrayList the listaTiposDeServicios to set
	 */
	public void setListaTiposDeServicios(ArrayList<HashMap<String, Object>> arrayList) {
		this.listaTiposDeServicios = arrayList;
	}


	/**
	 * @return the listaGruposDeServicios
	 */
	public ArrayList<HashMap<String, Object>> getListaGruposDeServicios() {
		return listaGruposDeServicios;
	}


	/**
	 * @param listaGruposDeServicios the listaGruposDeServicios to set
	 */
	public void setListaGruposDeServicios(
			ArrayList<HashMap<String, Object>> listaGruposDeServicios) {
		this.listaGruposDeServicios = listaGruposDeServicios;
	}

	/**
	 * @return the grupoTipoServicioAuxVista
	 */
	public ArrayList<DtoGrupoTipoServ> getGrupoTipoServicioAuxVista() 
	{
		this.grupoTipoServicioAuxVista= new ArrayList<DtoGrupoTipoServ>();
		TreeSet<String> treeTemp= new TreeSet<String>();
		for(DtoDetalleAgrupacionHonorarios dto: listaDetalleAgrupacionHonorario)
		{
			if(!treeTemp.contains(dto.getGrupoServicio().getCodigo()+"-"+dto.getTipoServicio().getCodigo()))
			{	
				treeTemp.add(dto.getGrupoServicio().getCodigo()+"-"+dto.getTipoServicio().getCodigo());
				DtoGrupoTipoServ dtoGrupoTipoServ= new DtoGrupoTipoServ(dto.getGrupoServicio(), dto.getTipoServicio());
				this.grupoTipoServicioAuxVista.add(dtoGrupoTipoServ);
			}	
		}
		treeTemp=null;
		return grupoTipoServicioAuxVista;
	}


		/**
	 * @param grupoTipoServicioAuxVista the grupoTipoServicioAuxVista to set
	 */
	public void setGrupoTipoServicioAuxVista(
			ArrayList<DtoGrupoTipoServ> grupoTipoServicioAuxVista) {
		this.grupoTipoServicioAuxVista = grupoTipoServicioAuxVista;
	}


		/**
		 * @return the grupoVistaServicio
		 */
		public ArrayList<InfoDatosInt> getGrupoVistaServicio() {
			this.grupoVistaServicio= new ArrayList<InfoDatosInt>();
			TreeSet<String> treeTemp= new TreeSet<String>();
			for(DtoDetalleServicioHonorarios dto: listaDetalleServicioHonorario)
			{
				
				logger.info("VALOR RECORRIDO ------------->"+dto.getServicio().getCodigo());
				
				logger.info(treeTemp.contains(String.valueOf(dto.getServicio().getCodigo())));	
				if(!treeTemp.contains(String.valueOf(dto.getServicio().getCodigo())))
				{
					treeTemp.add(String.valueOf(dto.getServicio().getCodigo()));
					InfoDatosInt servicioInfo= new InfoDatosInt(dto.getServicio().getCodigo(), (dto.getServicio().getNombre()+" TARIFA: "+dto.getTarifaFormateado()+ " FECHA VIGENCIA: "+dto.getFechaVigenciaTarifa()));
					this.grupoVistaServicio.add(servicioInfo);
					logger.info("VALOR ENTRANDO A TRESET ------------->"+servicioInfo.getCodigo()+ "-" + servicioInfo.getNombre());
				}
				
			}
			treeTemp=null;
			
			return grupoVistaServicio;
		}


		/**
		 * @param grupoVistaServicio the grupoVistaServicio to set
		 */
		public void setGrupoVistaServicio(ArrayList<InfoDatosInt> grupoVistaServicio) {
			this.grupoVistaServicio = grupoVistaServicio;
		}


		/**
		 * @return the listaCentrosAtencion
		 */
		public ArrayList<DtoCentrosAtencion> getListaCentrosAtencion() {
			return listaCentrosAtencion;
		}


		/**
		 * @param listaCentrosAtencion the listaCentrosAtencion to set
		 */
		public void setListaCentrosAtencion(
				ArrayList<DtoCentrosAtencion> listaCentrosAtencion) {
			this.listaCentrosAtencion = listaCentrosAtencion;
		}
		

}