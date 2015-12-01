/*
 * Mayo 6, 2008
 */
package com.princetonsa.dto.historiaClinica.parametrizacion;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;

import org.apache.commons.beanutils.PropertyUtils;

import com.princetonsa.dto.odontologia.DtoEvolucionOdontologica;
import com.princetonsa.dto.odontologia.DtoValoracionesOdonto;
import com.princetonsa.mundo.PersonaBasica;

import util.ConstantesBD;
import util.ConstantesCamposParametrizables;
import util.InfoDatosInt;
import util.Utilidades;
import util.ValoresPorDefecto;

/**
 * Data Transfer Object: PLANTILLA PARAMETRIZABLE
 * @author Sebastiï¿½n Gï¿½mez R.
 *
 */
public class DtoPlantilla implements Serializable
{
	/**
	 * Versión serial
	 */
	private static final long serialVersionUID = 1L;
	private String codigoPK;
	private InfoDatosInt funcionalidad; //funcionalidad asociada a la plantilla
	private InfoDatosInt centroCosto;
	private InfoDatosInt sexo;
	private InfoDatosInt especialidad;
	private int codigoInstitucion;
	private ArrayList<DtoSeccionFija> seccionesFijas;
	
	
	//******Atributos que solo aplican para respuesta procedimientos******************
	private String codigo;
	private String nombre;
	
	//*****Atributos para el manejo de secciones dinï¿½micas que se abren por valores de un select/check**************+
	private ArrayList<DtoElementoParam> seccionesValor;
	private String listadoSeccionesValorActivas;
	
	//*****Atributos constantes calculos formula********************************************************
	private int edadAniosPaciente;
	private int edadDiasPaciente ;
	private int edadMesesPaciente;
	
	//*******Anexo 843*************
	private String tipoAtencion;
	
	/**
	 * Indicador para saber si la plantilla estï¿½ en proceso de respuesta o es ingresada por primera vez
	 * para su diligencionamiento
	 */
	private boolean plantillaEnProceso;
	
	/**
	 * Campo donde se almacena el codigo del historico de la plantilla puede ser de la siguientes tablas:
	 * plantillas_ingresos, plantillas_evolucion
	 */
	private String consecutivoHistorico;
	
	//**********ATRIBUTOS USADOS PARA LA CONFIRMACIï¿½N DE CITA ODONTOLOGICA*************
	/*
	 * Estos atributos solo son usados en la cofnirmacion de la atencion de citas odontologicas
	 */
	private BigDecimal codigoValoracionOdontologia;
	private BigDecimal codigoEvolucionOdontologia;
	
	//************ATRIBUTOS USADOS PARA LA IMPRESION DE CITA ODONTOLï¿½GICA**************************
	private DtoEvolucionOdontologica dtoEvolucionOdo;
	private DtoValoracionesOdonto dtoValoracionOdo;
	
	
	/** Objeto jasper para el subreporte secciones fijas */
    private JRDataSource dsSeccionesFijas;
    
    /***ATRIBUTOS PARA CONSULTAR LA HISTORIA CLINICA ODONTOLOGICA**/
    private String fechaRegistro;
    private String profesionalRegistro;
    private BigDecimal codigoPkCita;
    
    
	/**
	 * Resetea los datos del DTO
	 *
	 */
	public void clean()
	{
		this.codigoPK = "";
		this.funcionalidad = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.centroCosto = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.sexo = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.especialidad = new InfoDatosInt(ConstantesBD.codigoNuncaValido,"");
		this.codigoInstitucion = ConstantesBD.codigoNuncaValido;
		this.seccionesFijas = new ArrayList<DtoSeccionFija>();
		
		//Atributos que solo aplican para la respuesta de procedimientos
		this.codigo = "";
		this.nombre = "";
		
		//Atributos para el manejo de secciones dinï¿½micas que se abren por valores de un select/check
		this.seccionesValor = new ArrayList<DtoElementoParam>();
		this.listadoSeccionesValorActivas = "";		
		
		//Atributos constantes calculo formula
		this.edadAniosPaciente = 0;
		this.edadDiasPaciente = 0;
		this.edadMesesPaciente = 0;
		
		this.tipoAtencion="";
		
		this.plantillaEnProceso = false;
		this.consecutivoHistorico = "";
		
		this.codigoValoracionOdontologia = new BigDecimal(0);
		this.codigoEvolucionOdontologia = new BigDecimal(0);
		
		this.dtoEvolucionOdo = new DtoEvolucionOdontologica();
		this.dtoValoracionOdo = new DtoValoracionesOdonto();
		
		
	}
	
	/**
	 * @return the edadAniosPaciente
	 */
	public int getEdadAniosPaciente() {
		return edadAniosPaciente;
	}

	/**
	 * @param edadAniosPaciente the edadAniosPaciente to set
	 */
	public void setEdadAniosPaciente(int edadAniosPaciente) {
		this.edadAniosPaciente = edadAniosPaciente;
	}

	/**
	 * @return the edadDiasPaciente
	 */
	public int getEdadDiasPaciente() {
		return edadDiasPaciente;
	}

	/**
	 * @param edadDiasPaciente the edadDiasPaciente to set
	 */
	public void setEdadDiasPaciente(int edadDiasPaciente) {
		this.edadDiasPaciente = edadDiasPaciente;
	}

	/**
	 * @return the edadMesesPaciente
	 */
	public int getEdadMesesPaciente() {
		return edadMesesPaciente;
	}

	/**
	 * @param edadMesesPaciente the edadMesesPaciente to set
	 */
	public void setEdadMesesPaciente(int edadMesesPaciente) {
		this.edadMesesPaciente = edadMesesPaciente;
	}

	/**
	 * Constructor del DTO
	 *
	 */
	public DtoPlantilla()
	{
		this.clean();
	}

	/**
	 * @return the centroCosto
	 */
	public int getCodigoCentroCosto() {
		return centroCosto.getCodigo();
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setCodigoCentroCosto(int centroCosto) {
		this.centroCosto.setCodigo(centroCosto);
	}
	
	/**
	 * @return the centroCosto
	 */
	public String getNombreCentroCosto() {
		return centroCosto.getNombre();
	}

	/**
	 * @param centroCosto the centroCosto to set
	 */
	public void setNombreCentroCosto(String centroCosto) {
		this.centroCosto.setNombre(centroCosto);
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
	 * @return the codigoInstitucion
	 */
	public int getCodigoInstitucion() {
		return codigoInstitucion;
	}

	/**
	 * @param codigoInstitucion the codigoInstitucion to set
	 */
	public void setCodigoInstitucion(int codigoInstitucion) {
		this.codigoInstitucion = codigoInstitucion;
	}

	/**
	 * @return the codigoPK
	 */
	public String getCodigoPK() {
		return codigoPK;
	}

	/**
	 * @param codigoPK the codigoPK to set
	 */
	public void setCodigoPK(String codigoPK) {
		this.codigoPK = codigoPK;
	}

	/**
	 * @return the especialidad
	 */
	public int getCodigoEspecialidad() {
		return especialidad.getCodigo();
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setCodigoEspecialidad(int especialidad) {
		this.especialidad.setCodigo(especialidad);
	}
	
	/**
	 * @return the especialidad
	 */
	public String getNombreEspecialidad() {
		return especialidad.getNombre();
	}

	/**
	 * @param especialidad the especialidad to set
	 */
	public void setNombreEspecialidad(String especialidad) {
		this.especialidad.setNombre(especialidad);
	}

	/**
	 * @return the funcionalidad
	 */
	public int getCodigoFuncionalidad() {
		return funcionalidad.getCodigo();
	}

	/**
	 * @param funcionalidad the funcionalidad to set
	 */
	public void setCodigoFuncionalidad(int funcionalidad) {
		this.funcionalidad.setCodigo(funcionalidad);
	}
	
	/**
	 * @return the funcionalidad
	 */
	public String getNombreFuncionalidad() {
		return funcionalidad.getNombre();
	}

	/**
	 * @param funcionalidad the funcionalidad to set
	 */
	public void setNombreFuncionalidad(String funcionalidad) {
		this.funcionalidad.setNombre(funcionalidad);
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
	 * @return the sexo
	 */
	public int getCodigoSexo() {
		return sexo.getCodigo();
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setCodigoSexo(int sexo) {
		this.sexo.setCodigo(sexo);
	}
	
	/**
	 * @return the sexo
	 */
	public String getNombreSexo() {
		return sexo.getNombre();
	}

	/**
	 * @param sexo the sexo to set
	 */
	public void setNombreSexo(String sexo) {
		this.sexo.setNombre(sexo);
	}

	/**
	 * @return the seccionesFijas
	 */
	public ArrayList<DtoSeccionFija> getSeccionesFijas() {
		return seccionesFijas;
	}
	
	/**
	 * @return the seccionesFijas
	 */
	public DtoSeccionFija getSeccionesFijasPos(int pos) {
		return seccionesFijas.get(pos);
	}

	/**
	 * @param seccionesFijas the seccionesFijas to set
	 */
	public void setSeccionesFijas(ArrayList<DtoSeccionFija> seccionesFijas) {
		this.seccionesFijas = seccionesFijas;
	}
	
	/**
	 * @param seccionesFijas the seccionesFijas to set
	 */
	public void setSeccionesFijasPos(int pos,DtoSeccionFija dto) {
		this.seccionesFijas.set(pos,dto);
	}
	
	/**
	 * Ordena la posiciï¿½n de los elementos del array de secciones fijas a partir de su orden 
	 * */
	public void ordenarArraySeccionesFijas()
	{
		int numRegistros = this.getSeccionesFijas().size();	
		ArrayList<DtoSeccionFija> tmp = new ArrayList<DtoSeccionFija>();
		
		for(int j = 0; j<numRegistros; j++)		
			tmp.add(new DtoSeccionFija());			
				
		for(int j = 0; j<numRegistros; j++)		
			tmp.set(this.getSeccionesFijasPos(j).getOrden()-1,this.getSeccionesFijasPos(j));		
		
		this.setSeccionesFijas(tmp);			
	}
	
	/**
	 * Mï¿½todo para saber si la plantilla tiene informacion
	 * @return
	 */
	public boolean tieneInformacion()
	{
		boolean tiene = false;
		
		for(DtoSeccionFija seccionFija:this.seccionesFijas)
			if(seccionFija.isVisible())
			{
				for(DtoElementoParam elemento:seccionFija.getElementos())
					if(elemento.isVisible())
					{
						//SECCION **************************************************
						if(elemento.isSeccion())
						{
							DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
							tiene = tieneInformacionSeccion(seccion, tiene);
						}
						//COMPONENTE ***********************************************************
						else if(elemento.isComponente())
						{
							DtoComponente componente = (DtoComponente)elemento;
							
							//Si se ingresï¿½ informaciï¿½n fija del componente
							if(componente.isIngresoInformacion())
								tiene = true;
							
							for(DtoElementoParam elemComp:componente.getElementos())
								if(elemComp.isVisible())
								{
									//Seccion del componente
									if(elemComp.isSeccion())
									{
										DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemComp;
										tiene = tieneInformacionSeccion(seccion, tiene);
									}
									//Escala del componente
									else if(elemComp.isEscala())
									{
										DtoEscala escala = (DtoEscala)elemComp;
										for(DtoSeccionParametrizable seccion:escala.getSecciones())
											for(DtoCampoParametrizable campo:seccion.getCampos())
												if(!campo.getValor().equals("")||!campo.getObservaciones().equals(""))
													tiene = true;
										
										if(escala.getTotalEscala()>0||!escala.getObservaciones().equals("")||escala.getNumArchivosAdjuntos()>0)
											tiene = true;
									}
								}
						}
						//ESCALA *********************************************************************
						else if(elemento.isEscala())
						{
							DtoEscala escala = (DtoEscala)elemento;
							for(DtoSeccionParametrizable seccion:escala.getSecciones())
								for(DtoCampoParametrizable campo:seccion.getCampos())
									if(!campo.getValor().equals("")||!campo.getObservaciones().equals(""))
										tiene = true;
							
							if(escala.getTotalEscala()>0||!escala.getObservaciones().equals("")||escala.getNumArchivosAdjuntos()>0)
								tiene = true;
						}
					}
			}
		
		//Se verifica que las secciones valor hayan tenido informaciï¿½n
		/**for(DtoElementoParam elemento:this.seccionesValor)
		{
			DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemento;
			//Se verifica si la seccion estï¿½ activa
			if(seccion.estaSeccionValorOpcionActiva(this.listadoSeccionesValorActivas))
				tiene = tieneInformacionSeccion(seccion, tiene);
		}**/
		
		return tiene;
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	public boolean tieneInformacionPerfilNed(int codigoInstitucion)
	{
		boolean tiene = false;
		double codigoEscalaValorDefecto= Utilidades.convertirADouble(ValoresPorDefecto.getEscalaPacientePerfil(codigoInstitucion));
		
		for(DtoSeccionFija seccionFija:this.seccionesFijas)
		{	
			if(seccionFija.isVisible())
			{
				for(DtoElementoParam elemento:seccionFija.getElementos())
				{	
					if(elemento.isVisible())
					{
						if(elemento.isEscala())
						{
							if(Utilidades.convertirADouble(elemento.getCodigoPK()) == codigoEscalaValorDefecto)
							{
								DtoEscala escala = (DtoEscala)elemento;
								for(DtoSeccionParametrizable seccion:escala.getSecciones())
								{	
									for(DtoCampoParametrizable campo:seccion.getCampos())
									{	
										if(!campo.getValor().equals("")||!campo.getObservaciones().equals(""))
										{	
											tiene = true;
										}
									}
								}	
								if(escala.getTotalEscala()>0||!escala.getObservaciones().equals("")||escala.getNumArchivosAdjuntos()>0)
								{	
									tiene = true;
								}
							}	
						}
					}
				}
			}
		}
		return tiene;
	}
	
	/**
	 * 
	 * @param codigoInstitucion
	 * @return
	 */
	public void setEscalaPerfilNed(DtoEscala escala, int codigoInstitucion)
	{
		double codigoEscalaValorDefecto= Utilidades.convertirADouble(ValoresPorDefecto.getEscalaPacientePerfil(codigoInstitucion));
		
		for(DtoSeccionFija seccionFija:this.seccionesFijas)
		{	
			if(seccionFija.isVisible())
			{
				for(DtoElementoParam elemento:seccionFija.getElementos())
				{	
					if(elemento.isVisible())
					{
						if(elemento.isEscala())
						{
							if(Utilidades.convertirADouble(elemento.getCodigoPK()) == codigoEscalaValorDefecto)
							{
								//estas no estaban funcionando
								/*elemento.setArchivosAdjuntos(escala.getArchivosAdjuntos());
								elemento.setCampos(escala.getCampos());
								elemento.setCodigo(escala.getCodigo());
								elemento.setCodigoFactorPrediccion(escala.getCodigoFactorPrediccion());
								elemento.setCodigoInstitucion(escala.getCodigoInstitucion());
								elemento.setCodigoPK(escala.getCodigoPK());
								elemento.setConsecutivoHistorico(escala.getConsecutivoHistorico());
								elemento.setConsecutivoParametrizacion(escala.getConsecutivoParametrizacion());
								elemento.setDescripcion(escala.getDescripcion());
								elemento.setElementos(escala.getElementos());
								elemento.setFactorPrediccion(escala.getFactorPrediccion());
								elemento.setNombreFactorPrediccion(escala.getNombreFactorPrediccion());
								elemento.setNumArchivosAdjuntos(escala.getNumArchivosAdjuntos());
								elemento.setOrden(escala.getOrden());
								elemento.setTotalEscala(escala.getTotalEscala());*/
								elemento.setSecciones( escala.getSecciones());
							}	
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * Mï¿½todo que soporta el mï¿½todo TIENE INFORMACION y sirve para verificar si se le ingresï¿½ informacion
	 * a una seccion / subseccion
	 * @param seccion
	 * @param tiene
	 * @return
	 */
	private boolean tieneInformacionSeccion(DtoSeccionParametrizable seccion,boolean tiene)
	{
		for(DtoCampoParametrizable campo:seccion.getCampos())
		{
			if(campo.getTipo().getCodigo()==ConstantesCamposParametrizables.tipoCampoTextoPredeterminado)
			{
				campo.setValor(campo.getEtiqueta());
			}
			
			///Se verifica que se haya llenado información del campo dependiendo del tipo HTML:
			if(
					//Si no fueron CHECKBOX y la variable VALOR viene llena
					(!campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox)&&!campo.getValor().equals(""))
					||
					//Si fue campo CHECKBOX y se chequeó al menos una de sus opciones
					(campo.getTipoHtml().equals(ConstantesCamposParametrizables.campoTipoCheckBox)&&campo.fueLlenadoCheckBox())
				)
				tiene = true;
		}
		if(!tiene)
		{
			for(DtoSeccionParametrizable subseccion:seccion.getSecciones())
				tiene = tieneInformacionSeccion(subseccion, tiene); 
		}
		
		return tiene;
	}

	/**
	 * Mï¿½todo para verificar si existe componente de un tipo especï¿½fico dentro de la plantilla
	 * @param tipoComponente
	 * @return
	 */
	public boolean existeComponente(int tipoComponente) 
	{
		boolean existe = false;
		
		for(DtoSeccionFija seccionFija:this.getSeccionesFijas())
			if(seccionFija.isVisible())
				for(DtoElementoParam elemento:seccionFija.getElementos())
					if(elemento.isComponente()&&elemento.isVisible())
					{
						DtoComponente componente = (DtoComponente)elemento;
						if(componente.getCodigoTipo()==tipoComponente)
							existe = true;
					}
		
		return existe;
	}
	
	/**
	 * Mï¿½todo para marcar que a un componente se le ingresï¿½ informaciï¿½n
	 * @param tipoComponente
	 * @return
	 */
	public void marcarComponente(int tipoComponente) 
	{
		
		for(DtoSeccionFija seccionFija:this.getSeccionesFijas())
			if(seccionFija.isVisible())
				for(DtoElementoParam elemento:seccionFija.getElementos())
					if(elemento.isComponente()&&elemento.isVisible())
					{
						DtoComponente componente = (DtoComponente)elemento;
						if(componente.getCodigoTipo()==tipoComponente)
							componente.setIngresoInformacion(true);
					}
		
	}
	
	/**
	 * Mï¿½todo usado para ocultar un componente
	 * @param tipoComponente
	 */
	public void ocultarComponente(int tipoComponente,boolean visible)
	{
		for(DtoSeccionFija seccionFija:this.getSeccionesFijas())
			if(seccionFija.isVisible())
				for(DtoElementoParam elemento:seccionFija.getElementos())
					if(elemento.isComponente()&&elemento.isVisible())
					{
						DtoComponente componente = (DtoComponente)elemento;
						if(componente.getCodigoTipo()==tipoComponente)
							componente.setVisible(visible);
					}
	}
	
	
	/**
	 * Mï¿½todo para verificar si los componentes que son parmetrizables tienen informaciï¿½n o no
	 * para saber si se habilitan u ocultan
	 *
	 */
	public void verificarInformacionComponentesParametrizables()
	{
		for(DtoSeccionFija seccionFija:this.getSeccionesFijas())
			if(seccionFija.isVisible())
				for(DtoElementoParam elemento:seccionFija.getElementos())
					if(elemento.isComponente()&&elemento.isVisible())
					{
						DtoComponente componente = (DtoComponente)elemento;
						
						if(
								componente.getCodigoTipo()==ConstantesCamposParametrizables.tipoComponenteCardiologia||
								componente.getCodigoTipo()==ConstantesCamposParametrizables.tipoComponenteClinicaMemoria||
								componente.getCodigoTipo()==ConstantesCamposParametrizables.tipoComponenteNeurologia||
								componente.getCodigoTipo()==ConstantesCamposParametrizables.tipoComponenteNeuroSiquiatria||
								componente.getCodigoTipo()==ConstantesCamposParametrizables.tipoComponenteNutricion||
								componente.getCodigoTipo()==ConstantesCamposParametrizables.tipoComponenteSicologia||
								componente.getCodigoTipo()==ConstantesCamposParametrizables.tipoComponenteTrabajoSocial
							)
							//Se verifica si los componentes tienen informacion para mostrar
							//De lo contrario se inhbailitarï¿½ el campo Visible
							componente.setVisible(false);
						
						
						for(DtoElementoParam elementoComp:componente.getElementos())
						{
							//***********SE VERIFICA PRESENCIA DE SECCIONES VISIBLES***********************+
							if(elementoComp.isSeccion())
							{
								DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elementoComp;
								if(seccion.isVisible())
									componente.setVisible(true);
								
								for(DtoSeccionParametrizable subseccion:seccion.getSecciones())
									if(subseccion.isVisible())
										componente.setVisible(true);
							}
							//*************************************************************
							//*********SE VERIFICA PRESENCIA DE ESCALAS*********************
							if(elementoComp.isEscala()&&elementoComp.isVisible())
								componente.setVisible(true);
							//************************************************************
						}	
					}
	}

	/**
	 * @param siempreSeccionAbierta the siempreSeccionAbierta to set
	 */
	public void setSiempreAbiertas(boolean siempreSeccionAbierta) 
	{			
		for(int j = 0; j<this.getSeccionesFijas().size(); j++)
		{
			for(int i = 0; i < this.getSeccionesFijas().get(j).getElementos().size(); i++)
			{
				if(siempreSeccionAbierta)
					this.getSeccionesFijas().get(j).getElementos().get(i).setAbierto(true);
				
				this.getSeccionesFijas().get(j).getElementos().get(i).setSiempreAbierta(siempreSeccionAbierta);
			}
		}
	}	
	
	/**
	 * Se verifica si un componente especï¿½fico tiene informaciï¿½n parametrizable ya guardada para 
	 * mostrar como resumen
	 * @param codigoTipoComponente
	 * @return
	 */
	public boolean tieneComponenteInformacionParametrizable(int codigoTipoComponente)
	{
		boolean tiene = false;
		for(DtoSeccionFija seccionFija:this.getSeccionesFijas())
			if(seccionFija.isVisible())
				for(DtoElementoParam elemento:seccionFija.getElementos())
					if(elemento.isComponente()&&elemento.isVisible())
					{
						DtoComponente componente = (DtoComponente)elemento;
						
						if(componente.getCodigoTipo()==codigoTipoComponente)
						{
							for(DtoElementoParam elementoComp:componente.getElementos())
							{
								//***********SE VERIFICA PRESENCIA DE SECCIONES VISIBLES***********************+
								if(elementoComp.isSeccion())
								{
									DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elementoComp;
									if(seccion.isVisible())
										tiene = true;
									
									for(DtoSeccionParametrizable subseccion:seccion.getSecciones())
										if(subseccion.isVisible())
											tiene = true;
								}
								//*************************************************************
								//*********SE VERIFICA PRESENCIA DE ESCALAS*********************
								if(elementoComp.isEscala()&&elementoComp.isVisible())
									tiene = true;
								//************************************************************
							}	
						}
					}
		return tiene;
	}
	
	/**
	 * @return the seccionesValor
	 */
	public ArrayList<DtoElementoParam> getSeccionesValor() {
		return seccionesValor;
	}

	/**
	 * @param seccionesValor the seccionesValor to set
	 */
	public void setSeccionesValor(ArrayList<DtoElementoParam> seccionesValor) {
		this.seccionesValor = seccionesValor;
	}

	/**
	 * @return the listadoSeccionesValorActivas
	 */
	public String getListadoSeccionesValorActivas() {
		return listadoSeccionesValorActivas;
	}

	/**
	 * @param listadoSeccionesValorActivas the listadoSeccionesValorActivas to set
	 */
	public void setListadoSeccionesValorActivas(String listadoSeccionesValorActivas) {
		this.listadoSeccionesValorActivas = listadoSeccionesValorActivas;
	}
	
	/**
	 * Mï¿½todo para obtener la posicion en la que se encuentra la seccion valor en el arreglo
	 * buscando por su cï¿½digo pk
	 * @param codigoPkSeccion
	 * @return
	 */
	public int obtenerPosicionSeccionValorXCodigoPk(String codigoPkSeccion)
	{
		int posicion = ConstantesBD.codigoNuncaValido;
		int cont = 0;
		
		for(DtoElementoParam seccion:this.seccionesValor)
		{
			if(seccion.getCodigoPK().equals(codigoPkSeccion))
				posicion = cont;
			cont++;
		}
		
		return posicion;				
	}
	 
	/**
	 * Obtiene el listado de Secciones Valor
	 * @param ArrayList<DtoSeccionParametrizable> seccionesExistentes
	 * */
	public ArrayList<DtoSeccionParametrizable> obtenerListadoSeccionValor(ArrayList<DtoSeccionParametrizable> seccionesExistentes)
	{	
		DtoSeccionParametrizable sec = new DtoSeccionParametrizable();
		ArrayList<DtoSeccionParametrizable> respuesta = new ArrayList<DtoSeccionParametrizable>();
		
		for(int i = 0; i < this.seccionesFijas.size(); i++)
		{
			//solo toma las secciones con indicativo en S en un nivel de fija
			if(!this.seccionesFijas.get(i).isEsFija() && 
					this.seccionesFijas.get(i).getElementosPos(0).isSeccion() &&  
						this.seccionesFijas.get(i).getElementoSeccionPos(0).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoSi) && 
							this.seccionesFijas.get(i).getElementoSeccionPos(0).isMostrarModificacion())
			{
				try 
				{
					sec = new DtoSeccionParametrizable();
					PropertyUtils.copyProperties(sec,this.seccionesFijas.get(i).getElementoSeccionPos(0));				
					respuesta.add(sec);	
					
				}catch(Exception e){
					e.printStackTrace();
					return respuesta;
				}				
			}
			//Toma las secciones de primer nivel ubicadas dentro de una seccion fija
			else if(this.seccionesFijas.get(i).isEsFija() && 
					this.seccionesFijas.get(i).getElementos().size()>0)
			{
				for(int j = 0; j < this.seccionesFijas.get(i).getElementos().size();j++)
				{
					if(this.seccionesFijas.get(i).getElementos().get(j).isSeccion() && 
							this.seccionesFijas.get(i).getElementoSeccionPos(j).getIndicativoRestriccionValCamp().equals(ConstantesBD.acronimoSi) && 
								this.seccionesFijas.get(i).getElementoSeccionPos(j).isMostrarModificacion())
					{
						try 
						{
							sec = new DtoSeccionParametrizable();
							PropertyUtils.copyProperties(sec,this.seccionesFijas.get(i).getElementoSeccionPos(j));				
							respuesta.add(sec);	
							
						}catch(Exception e){
							e.printStackTrace();
							return respuesta;
						}
					}
				}
			}
		}
		
		if(seccionesExistentes.size() > 0)
		{
			for(int i=0; i < seccionesExistentes.size(); i++)
			 {
				for(int j=0; j<respuesta.size(); j++)
				{
					if(seccionesExistentes.get(i).getCodigoPK().equals(respuesta.get(j).getCodigoPK()))
						respuesta.remove(j);
				}
			 }
		}
	
		return respuesta;			
	}
	
	/**
	 * Mï¿½todo que retorna el consecutivo plantilla_componente de una seccion valor pasï¿½ndole 
	 * el consecutivo de parametrizacion componentes_seccion(codigo_pk) 
	 * @param consecutivoParametrizacion
	 * @return
	 */
	public String obtenerConsecutivoPlantillaComponenteSeccionValor(String consecutivoParametrizacion)
	{
		String consecutivoPlantillaComponente = "";
		
		for(DtoSeccionFija seccionFija:this.seccionesFijas)
			for(DtoElementoParam elemento:seccionFija.getElementos())
				if(elemento.isComponente())
					for(DtoElementoParam elemComp:elemento.getElementos())
						if(elemComp.isSeccion())
						{
							DtoSeccionParametrizable seccion = (DtoSeccionParametrizable)elemComp;
							if(seccion.getConsecutivoParametrizacion().equals(consecutivoParametrizacion))
								consecutivoParametrizacion = elemento.getConsecutivoParametrizacion();
							
						}
		
		return consecutivoPlantillaComponente;
	}
	
	
	/**
	 * Mï¿½todo para cargar las edades del paciente
	 * @param paciente
	 */
	public void cargarEdadesPaciente(PersonaBasica paciente)
	{
		this.edadDiasPaciente = paciente.getEdadDias();
		this.edadMesesPaciente = paciente.getEdadMeses();
		this.edadAniosPaciente = paciente.getEdad();
	}

	public String getTipoAtencion() {
		return tipoAtencion;
	}

	public void setTipoAtencion(String tipoAtencion) {
		this.tipoAtencion = tipoAtencion;
	}

	/**
	 * @return the plantillaEnProceso
	 */
	public boolean isPlantillaEnProceso() {
		return plantillaEnProceso;
	}

	/**
	 * @param plantillaEnProceso the plantillaEnProceso to set
	 */
	public void setPlantillaEnProceso(boolean plantillaEnProceso) {
		this.plantillaEnProceso = plantillaEnProceso;
	}

	/**
	 * @return the consecutivoHistorico
	 */
	public String getConsecutivoHistorico() {
		return consecutivoHistorico;
	}

	/**
	 * @param consecutivoHistorico the consecutivoHistorico to set
	 */
	public void setConsecutivoHistorico(String consecutivoHistorico) {
		this.consecutivoHistorico = consecutivoHistorico;
	}

	/**
	 * @return the codigoValoracionOdontologia
	 */
	public BigDecimal getCodigoValoracionOdontologia() {
		return codigoValoracionOdontologia;
	}

	/**
	 * @param codigoValoracionOdontologia the codigoValoracionOdontologia to set
	 */
	public void setCodigoValoracionOdontologia(BigDecimal codigoValoracionOdontologia) {
		this.codigoValoracionOdontologia = codigoValoracionOdontologia;
	}

	/**
	 * @return the codigoEvolucionOdontologia
	 */
	public BigDecimal getCodigoEvolucionOdontologia() {
		return codigoEvolucionOdontologia;
	}

	/**
	 * @param codigoEvolucionOdontologia the codigoEvolucionOdontologia to set
	 */
	public void setCodigoEvolucionOdontologia(BigDecimal codigoEvolucionOdontologia) {
		this.codigoEvolucionOdontologia = codigoEvolucionOdontologia;
	}

	/**
	 * @return the dtoEvolucionOdo
	 */
	public DtoEvolucionOdontologica getDtoEvolucionOdo() {
		return dtoEvolucionOdo;
	}

	/**
	 * @param dtoEvolucionOdo the dtoEvolucionOdo to set
	 */
	public void setDtoEvolucionOdo(DtoEvolucionOdontologica dtoEvolucionOdo) {
		this.dtoEvolucionOdo = dtoEvolucionOdo;
	}

	/**
	 * @return the dtoValoracionOdo
	 */
	public DtoValoracionesOdonto getDtoValoracionOdo() {
		return dtoValoracionOdo;
	}

	/**
	 * @param dtoValoracionOdo the dtoValoracionOdo to set
	 */
	public void setDtoValoracionOdo(DtoValoracionesOdonto dtoValoracionOdo) {
		this.dtoValoracionOdo = dtoValoracionOdo;
	}

	public InfoDatosInt getFuncionalidad() {
		return funcionalidad;
	}

	public void setFuncionalidad(InfoDatosInt funcionalidad) {
		this.funcionalidad = funcionalidad;
	}

	/**
	 * Método que almacena el datasource del subreporte secciones fijas
	 * de las plantillas odontológicas
	 * @param dsSeccionesFijas
	 */
	public void setDsSeccionesFijas(JRDataSource dsSeccionesFijas) {
		this.dsSeccionesFijas = dsSeccionesFijas;
	}

	/**
	 * Método que devuelve el datasource del subreporte secciones fijas
	 * de las plantillas odontológicas
	 * @param dsSeccionesFijas
	 */
	public JRDataSource getDsSeccionesFijas() {
		return dsSeccionesFijas;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getFechaRegistro() {
		return fechaRegistro;
	}

	public void setProfesionalRegistro(String profesionalRegistro) {
		this.profesionalRegistro = profesionalRegistro;
	}

	public String getProfesionalRegistro() {
		return profesionalRegistro;
	}

	public void setCodigoPkCita(BigDecimal codigoPkCita) {
		this.codigoPkCita = codigoPkCita;
	}

	public BigDecimal getCodigoPkCita() {
		return codigoPkCita;
	}

}