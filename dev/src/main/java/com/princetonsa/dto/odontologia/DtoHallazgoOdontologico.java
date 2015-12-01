package com.princetonsa.dto.odontologia;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import util.ConstantesBD;
import util.InfoDatosString;
import util.UtilidadTexto;
import util.Utilidades;

import com.princetonsa.mundo.UsuarioBasico;

public class DtoHallazgoOdontologico implements Serializable{

	private String consecutivo;
	private String codigo;
	private String nombre;
	private String acronimo;
	private String diagnostico;
	private String descripcionDiagnostico;
	private String tipo_cie;
	private String convencion;
	private InfoDatosString infoConvecion;
	private String aplica_a;
	private String activo;
	private String a_tratar;
	private String fechaModificacion;
	private String horaModificacion;
	private String usuarioModificacion;
	private String archivoconvencion;
	private String diagnosticoCompleto;
	private long tipoHallazgo;
	private String nombreTipoHallazgo;
	private HashMap<String, Object> mapaDiagnosticos;
	private ArrayList<DtoDiagnosticoHallazgo> diagnosticos;
	private int institucion;
	private String path;
	private double codigoDouble;
	
	/**
	 * CODIGO DE LOS HALLAZGOS 
	 * 
	 */
	private String codigoHallazgo;
	/**
	 * 
	 */
	private String yaFueSeleccionado;
	
	/**
	 * */
	private boolean buscarConConvencion;
	
	/**
	 * 
	 * */
	private boolean buscarConConvencionVacia;
	
	/**
	 * 
	 */
	public DtoHallazgoOdontologico()
	{
	    this.reset();	
	}
	
	
	public void reset()
	{
		this.consecutivo=new String("");
		this.codigo=new String("");
		this.nombre=new String("");
		this.acronimo=new String("");
		this.diagnostico=new String("");
		this.tipo_cie=new String("");
		this.convencion=new String("");
		this.aplica_a=new String("");
		/*
		 * Solución tarea 134132
		 */
		this.activo=ConstantesBD.acronimoSi;
		this.a_tratar=ConstantesBD.acronimoNo;
		this.fechaModificacion=new String("");
		this.horaModificacion=new String("");
		this.usuarioModificacion=new String("");
	    this.descripcionDiagnostico= new String("");	
		this.archivoconvencion=new String("");
		this.mapaDiagnosticos = new HashMap<String, Object>();
		this.diagnosticos= new ArrayList<DtoDiagnosticoHallazgo>();
		this.diagnosticoCompleto=new String("");
		this.institucion=0;
		this.infoConvecion = new InfoDatosString();
		this.buscarConConvencion = true;
		this.buscarConConvencionVacia = false; 
		this.path = "";
		this.codigoDouble=ConstantesBD.codigoNuncaValidoDouble;
		this.setTipoHallazgo(ConstantesBD.codigoNuncaValidoLong);
		this.setNombreTipoHallazgo("SIN TIPO");
	}

	/**
	 * @return the consecutivo
	 */
	public String getConsecutivo() {
		return consecutivo;
	}

	/**
	 * @param consecutivo the consecutivo to set
	 */
	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	/**
	 * @return the codigo
	 */
	public String getCodigo() {
		return codigo;
	}

	/**
	 * @param codigo the codigo to set
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the acronimo
	 */
	public String getAcronimo() {
		return acronimo;
	}

	/**
	 * @param acronimo the acronimo to set
	 */
	public void setAcronimo(String acronimo) {
		this.acronimo = acronimo;
	}

	/**
	 * @return the diagnostico
	 */
	public String getDiagnostico() {
		return diagnostico;
	}

	/**
	 * @param diagnostico the diagnostico to set
	 */
	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	/**
	 * @return the tipo_cie
	 */
	public String getTipo_cie() {
		return tipo_cie;
	}

	/**
	 * @param tipo_cie the tipo_cie to set
	 */
	public void setTipo_cie(String tipo_cie) {
		this.tipo_cie = tipo_cie;
	}

	/**
	 * @return the convencion
	 */
	public String getConvencion() {
		return convencion;
	}

	/**
	 * @param convencion the convencion to set
	 */
	public void setConvencion(String convencion) {
		this.convencion = convencion;
	}

	/**
	 * @return the aplica_a
	 */
	public String getAplica_a() {
		return aplica_a;
	}

	/**
	 * @param aplica_a the aplica_a to set
	 */
	public void setAplica_a(String aplica_a) {
		this.aplica_a = aplica_a;
	}

	/**
	 * @return the activo
	 */
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo the activo to set
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	/**
	 * @return the a_tratar
	 */
	public String getA_tratar() {
		return a_tratar;
	}

	/**
	 * @param a_tratar the a_tratar to set
	 */
	public void setA_tratar(String a_tratar) {
		this.a_tratar = a_tratar;
	}

	/**
	 * @return the fechaModificacion
	 */
	public String getFechaModificacion() {
		return fechaModificacion;
	}

	/**
	 * @param fechaModificacion the fechaModificacion to set
	 */
	public void setFechaModificacion(String fechaModificacion) {
		this.fechaModificacion = fechaModificacion;
	}

	/**
	 * @return the horaModificacion
	 */
	public String getHoraModificacion() {
		return horaModificacion;
	}

	/**
	 * @param horaModificacion the horaModificacion to set
	 */
	public void setHoraModificacion(String horaModificacion) {
		this.horaModificacion = horaModificacion;
	}

	/**
	 * @return the usuarioModificacion
	 */
	public String getUsuarioModificacion() {
		return usuarioModificacion;
	}

	/**
	 * @param usuarioModificacion the usuarioModificacion to set
	 */
	public void setUsuarioModificacion(String usuarioModificacion) {
		this.usuarioModificacion = usuarioModificacion;
	}

	/**
	 * @return the descripcionDiagnostico
	 */
	public String getDescripcionDiagnostico() {
		return descripcionDiagnostico;
	}

	/**
	 * @param descripcionDiagnostico the descripcionDiagnostico to set
	 */
	public void setDescripcionDiagnostico(String descripcionDiagnostico) {
		this.descripcionDiagnostico = descripcionDiagnostico;
	}

	/**
	 * @return the archivoconvencion
	 */
	public String getArchivoconvencion() {
		return archivoconvencion;
	}

	/**
	 * @param archivoconvencion the archivoconvencion to set
	 */
	public void setArchivoconvencion(String archivoconvencion) {
		this.archivoconvencion = archivoconvencion;
	}

	/**
	 * @return the mapaDiagnosticos
	 */
	public HashMap<String, Object> getMapaDiagnosticos() {
		return mapaDiagnosticos;
	}

	/**
	 * @param mapaDiagnosticos the mapaDiagnosticos to set
	 */
	public void setMapaDiagnosticos(HashMap<String, Object> mapaDiagnosticos) {
		this.mapaDiagnosticos = mapaDiagnosticos;
	}

	/**
	 * @return the diagnosticos
	 */
	public ArrayList<DtoDiagnosticoHallazgo> getDiagnosticos() {
		return diagnosticos;
	}

	/**
	 * @param diagnosticos the diagnosticos to set
	 */
	public void setDiagnosticos(ArrayList<DtoDiagnosticoHallazgo> diagnosticos) {
		this.diagnosticos = diagnosticos;
	}
	
	
	
	
	
	/**
	 * @return the mapaDiagnosticos
	 */
	public Object getMapaDiagnosticos(String key) {
		return mapaDiagnosticos.get(key);
	}

	/**
	 * @param mapaDiagnosticos the mapaDiagnosticos to set
	 */
	public void setMapaDiagnosticos(String key, Object value) {
		this.mapaDiagnosticos.put(key, value);
	}
	
	/**
	 * Mapa que retorna el número dediag rel del mapa diagnósticos
	 * @return
	 */
	public int getNumMapaDiagnosticos()
	{
		return Utilidades.convertirAEntero(this.mapaDiagnosticos.get("numRegistros")+"", true);
	}
	
	/**
	 * Mapa que retorna el número dediag rel del mapa diagnósticos
	 * @return
	 */
	public void setNumMapaDiagnosticos(int valor)
	{
		this.mapaDiagnosticos.put("numRegistros",valor);
	}
	
	/**
	 * Método que asigna el número de diagnosticos relacionados del mapa diagnosticos
	 * @param numRegistros
	 */
	public void setNumDiagnosticos(int numRegistros)
	{
		this.mapaDiagnosticos.put("numRegistros",numRegistros);
		
	}
	
	/**
	 * Método para pasar los diagnósticos del DTO al HashMap
	 */
	public void diagnosticosDtoToHashMap()
	{
		String diagSeleccionados = "";
		for(DtoDiagnosticoHallazgo diagnostico:this.diagnosticos)
		{			
			if(diagnostico.getDiagnostico().isPrincipal())
			{
				this.setMapaDiagnosticos("principal", diagnostico.getDiagnostico().getAcronimo()+ConstantesBD.separadorSplit+diagnostico.getDiagnostico().getTipoCIE()+ConstantesBD.separadorSplit+diagnostico.getDiagnostico().getNombre());				
			}
			else
			{
				int numDiag = this.getNumMapaDiagnosticos();
				this.setMapaDiagnosticos(numDiag+"", diagnostico.getDiagnostico().getAcronimo()+ConstantesBD.separadorSplit+diagnostico.getDiagnostico().getTipoCIE()+ConstantesBD.separadorSplit+diagnostico.getDiagnostico().getNombre());
				this.setMapaDiagnosticos("checkbox_"+numDiag, "true");
				diagSeleccionados += (diagSeleccionados.equals("")?"":",") + "'" + diagnostico.getDiagnostico().getAcronimo() + "'";
				numDiag++;
				this.setNumDiagnosticos(numDiag);
			}
		}
		this.setMapaDiagnosticos("diagSeleccionados", diagSeleccionados);		
	}
	
	/**
	 * Método para pasar los diagnósticos del HashMap al DTO
	 */
	public void diagnosticosHashMapToDto(String fechaActual,String horaActual, UsuarioBasico usuario)
	{
		boolean encontro = false;
		//*************SE PASA DIAGNOSTICO PRINCIPAL***************************
		if(this.getMapaDiagnosticos("principal")!=null)
		{
			String[] diagPrin = this.getMapaDiagnosticos("principal").toString().split(ConstantesBD.separadorSplit);
			if(diagPrin.length>=3)
			{
				//Se busca el diagnóstico principal
				for(DtoDiagnosticoHallazgo diagnostico:this.diagnosticos)
				{
					if(diagnostico.getDiagnostico().isPrincipal())
					{
						diagnostico.getDiagnostico().setAcronimo(diagPrin[0]);
						diagnostico.getDiagnostico().setTipoCIE(Integer.parseInt(diagPrin[1]));
						diagnostico.getDiagnostico().setNombre(diagPrin[2]);
						diagnostico.setFechaModifica(fechaActual);
						diagnostico.setHoraModifica(horaActual);
						diagnostico.setUsuarioModifica(usuario);
						encontro = true;
					}
				}
				//----------------------------------------------
				//Si no se encontró el diagnostico principal se debe agregar---------
				if(!encontro)
				{
					DtoDiagnosticoHallazgo diagnostico = new DtoDiagnosticoHallazgo();
					diagnostico.getDiagnostico().setAcronimo(diagPrin[0]);
					diagnostico.getDiagnostico().setTipoCIE(Integer.parseInt(diagPrin[1]));
					diagnostico.getDiagnostico().setNombre(diagPrin[2]);
					diagnostico.getDiagnostico().setPrincipal(true);
					diagnostico.setFechaModifica(fechaActual);
					diagnostico.setHoraModifica(horaActual);
					diagnostico.setUsuarioModifica(usuario);
					this.diagnosticos.add(diagnostico);
				}
				//------------------------------------------------------------------
			}
		}
		//**********************************************************************
		//************SE PASAN DIAGNÓSTICOS RELACIONADOS*************************
			
		for(int i=0;i<this.getNumMapaDiagnosticos();i++)
		{
			encontro = false;
			//Se verifica que el diagnostico relacionado exista
			if(this.getMapaDiagnosticos(i+"")!=null)
			{
				
				String[] diagRel = this.getMapaDiagnosticos(i+"").toString().split(ConstantesBD.separadorSplit);
				boolean chequeado = UtilidadTexto.getBoolean(this.getMapaDiagnosticos("checkbox_"+i).toString());

				//Se verifica si el diagnóstico relacionado existe
				for(DtoDiagnosticoHallazgo diagnostico:this.diagnosticos)
				{
					if(!diagnostico.getDiagnostico().isPrincipal()
							&& diagnostico.getDiagnostico().getAcronimo().equals(diagRel[0]))
					{
						encontro = true;
						//Se verifica si se debe eliminar
						if(!chequeado)
						{
							diagnostico.setEliminado(true);
						}
					}
				}
				
				//Si no se encontró el diagnóstico y fue chequeado se agrega---------------------------
				if(!encontro&&chequeado)
				{
					DtoDiagnosticoHallazgo diagnostico = new DtoDiagnosticoHallazgo();
					diagnostico.getDiagnostico().setAcronimo(diagRel[0]);
					diagnostico.getDiagnostico().setTipoCIE(Integer.parseInt(diagRel[1]));
					diagnostico.getDiagnostico().setNombre(diagRel[2]);
					diagnostico.getDiagnostico().setPrincipal(false);
					diagnostico.setFechaModifica(fechaActual);
					diagnostico.setHoraModifica(horaActual);
					diagnostico.setUsuarioModifica(usuario);
					this.diagnosticos.add(diagnostico);
				}
				//_---------------------------------------------------------------------------------------
			}
		}
		//************************************************************************
	}


	/**
	 * @return the diagnosticoCompleto
	 */
	public String getDiagnosticoCompleto() {
		return diagnosticoCompleto;
	}


	/**
	 * @param diagnosticoCompleto the diagnosticoCompleto to set
	 */
	public void setDiagnosticoCompleto(String diagnosticoCompleto) {
		this.diagnosticoCompleto = diagnosticoCompleto;
	}


	public void setInstitucion(int institucion) {
		this.institucion = institucion;
	}


	public int getInstitucion() {
		return institucion;
	}


	public String getCodigoHallazgo() {
		return codigoHallazgo;
	}


	public void setCodigoHallazgo(String codigoHallazgo) {
		this.codigoHallazgo = codigoHallazgo;
	}


	public void setYaFueSeleccionado(String yaFueSeleccionado) {
		this.yaFueSeleccionado = yaFueSeleccionado;
	}


	public String getYaFueSeleccionado() {
		return yaFueSeleccionado;
	}


	public InfoDatosString getInfoConvecion() {
		return infoConvecion;
	}


	public void setInfoConvecion(InfoDatosString infoConvecion) {
		this.infoConvecion = infoConvecion;
	}

	public boolean isBuscarConConvencion() {
		return buscarConConvencion;
	}


	public void setBuscarConConvencion(boolean buscarConConvencion) {
		this.buscarConConvencion = buscarConConvencion;
	}


	public boolean isBuscarConConvencionVacia() {
		return buscarConConvencionVacia;
	}


	public void setBuscarConConvencionVacia(boolean buscarConConvencionVacia) {
		this.buscarConConvencionVacia = buscarConConvencionVacia;
	}


	public String getPath() {
		return path;
	}


	public void setPath(String path) {
		this.path = path;
	}


	/**
	 * @return the codigoDouble
	 */
	public double getCodigoDouble() {
		return codigoDouble;
	}


	/**
	 * @param codigoDouble the codigoDouble to set
	 */
	public void setCodigoDouble(double codigoDouble) {
		this.codigoDouble = codigoDouble;
	}


	/**
	 * @param tipoHallazgo the tipoHallazgo to set
	 */
	public void setTipoHallazgo(long tipoHallazgo) {
		this.tipoHallazgo = tipoHallazgo;
	}


	/**
	 * @return the tipoHallazgo
	 */
	public long getTipoHallazgo() {
		return tipoHallazgo;
	}


	/**
	 * @param nombreTipoHallazgo the nombreTipoHallazgo to set
	 */
	public void setNombreTipoHallazgo(String nombreTipoHallazgo) {
		this.nombreTipoHallazgo = nombreTipoHallazgo;
	}


	/**
	 * @return the nombreTipoHallazgo
	 */
	public String getNombreTipoHallazgo() {
		if (UtilidadTexto.isEmpty(this.nombreTipoHallazgo)) {
			return "SIN TIPO"; 
		}
		return nombreTipoHallazgo;
	}
	
	
}
