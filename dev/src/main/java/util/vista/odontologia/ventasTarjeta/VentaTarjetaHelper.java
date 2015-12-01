package util.vista.odontologia.ventasTarjeta;

import java.util.ArrayList;
import java.util.List;

import util.ConstantesBD;
import util.ConstantesIntegridadDominio;
import util.UtilidadTexto;
import util.Cargos.InfoDeudorTerceroDto;
import util.facturacion.UtilidadesFacturacion;
import util.manejoPaciente.UtilidadesManejoPaciente;

import com.princetonsa.dto.facturacion.DtoConvenio;
import com.princetonsa.dto.facturasVarias.DtoDeudor;
import com.princetonsa.mundo.Paciente;
import com.princetonsa.mundo.cargos.Tercero;


/**
 *  Helper El Action Venta Tarjeta Cliente
 * @author Edgar Carvajal Ruiz
 *
 */
public class VentaTarjetaHelper {
	
	
	
	
	/**
	 * Construtor private para no dejar construir instancias
	 */
	private VentaTarjetaHelper(){}
	
	
	/**
	 * Metodo para buscar los pacientes en el sistema
	 * recibe el tipo de Identificacion y el numero de Identificacion y 
	 * retorna un String si encuentra informacion, el String tiene formato XML
	 * @author Edgar Carvajal Ruiz
	 */
	public static String buscarPaciente(String tipoIdentificacion,String numeroIdentificacion ){
		
		String etiquetaXml="";
		
		/*
		 * Validar si existe tipo y numero de identificacion
		 */
		if(UtilidadTexto.isEmpty(tipoIdentificacion) || UtilidadTexto.isEmpty(numeroIdentificacion))
		{
			return etiquetaXml;
		}
		
		/*
		 *Armar el Paciente 
		 */
		Paciente paciente = new Paciente();
		paciente.setTipoIdentificacion(tipoIdentificacion);
		paciente.setNumeroIdentificacion(numeroIdentificacion);
	
		/*
		 * Carga los pacientes
		 */
		ArrayList<Paciente>  listaPaciente = new ArrayList<Paciente>();
		listaPaciente=UtilidadesManejoPaciente.obtenerDatosPaciente(paciente);
	
		/*
		 * Armar XML Paciente
		 */
		if(listaPaciente.size()>0)
		{
			etiquetaXml = accionArmarXMLPaciente(etiquetaXml, listaPaciente);
		}
		
		
		return etiquetaXml;
	}

	
	
	
	/**
	 * Metodo para armar el xml de paciente
	 * @author Edgar Carvajal Ruiz
	 * @param etiquetaXml
	 * @param listaPaciente
	 * @return
	 */
	private  static String accionArmarXMLPaciente(String etiquetaXml, ArrayList<Paciente> listaPaciente) {
		
		Paciente pacienteObtenido=listaPaciente.get(0);
		
		if(pacienteObtenido!=null)
		{
		     
			etiquetaXml+="<paciente>";
				etiquetaXml+="<nombre-primero>"+pacienteObtenido.getPrimerNombrePersona(false)+"</nombre-primero>";
				etiquetaXml+="<nombre-segundo>"+pacienteObtenido.getSegundoNombrePersona(false)+"</nombre-segundo>";
				etiquetaXml+="<apellido-primero>"+pacienteObtenido.getPrimerApellidoPersona(false)+"</apellido-primero>";
				etiquetaXml+="<apellido-segundo>"+pacienteObtenido.getSegundoApellidoPersona(false)+"</apellido-segundo>"; 
			etiquetaXml+="</paciente>";
		}
		return etiquetaXml;
	}
	
	
	/**
	 * Metodo para buscar deudores.
	 * Este Metodo buscar los deudore de tipo OTRO  y si encuentra informacion retorna un String
	 * con formata XML
	 * @author Edgar Carvajal Ruiz
	 * @param tipoIdentificacion
	 * @param numeroIdentificacion
	 * @return 
	 */
	public static String buscarDeudor(String tipoIdentificacion,String numeroIdentificacion ){
		
		
		/*
		 *String para retorna la informacion  
		 */
		String etiquetaXML="";
		
		/*
		 * Validar si existe tipo y numero de identificacion
		 */
		if(UtilidadTexto.isEmpty(tipoIdentificacion) || UtilidadTexto.isEmpty(numeroIdentificacion))
		{
			return etiquetaXML;
		}
		
		
		/*
		 *Armar Objeto de Busqueda 
		 */
		DtoDeudor dtoDeudor  = new DtoDeudor();
		dtoDeudor.setTipoIdentificacion(tipoIdentificacion);
		dtoDeudor.setNumeroIdentificacion(numeroIdentificacion);
		dtoDeudor.setTipoDeudor(ConstantesIntegridadDominio.acronimoOtro);
		
		
		/*
		 * Buscar la informacion del Deudor
		 */
		ArrayList<DtoDeudor> listaDeudoresTmp=UtilidadesFacturacion.obtenerDeudores(dtoDeudor); 
		
		if(listaDeudoresTmp.size()>0)
		{
			if(listaDeudoresTmp.size()>0)
			{
				etiquetaXML = accionArmarXMLDeudor(listaDeudoresTmp);
			}
		}
		
		return etiquetaXML;
	}

	
	
	

	/**
	 * Metodo para armar XML deudor
	 * @author Edgar Carvajal Ruiz
	 * @param listaDeudoresTmp
	 * @return
	 */
	private static String accionArmarXMLDeudor(	ArrayList<DtoDeudor> listaDeudoresTmp) {
		
		String etiquetaXML="";
		DtoDeudor dtoDeudorRespuesta= listaDeudoresTmp.get(0);
		
		if(dtoDeudorRespuesta!=null)
		{
			etiquetaXML+="<paciente>";
				etiquetaXML+="<nombre-primero>"+dtoDeudorRespuesta.getPrimerNombre()+"</nombre-primero>";
				etiquetaXML+="<nombre-segundo>"+dtoDeudorRespuesta.getSegundoNombre()+"</nombre-segundo>";
				etiquetaXML+="<apellido-primero>"+dtoDeudorRespuesta.getPrimerApellido()+"</apellido-primero>";
				etiquetaXML+="<apellido-segundo>"+dtoDeudorRespuesta.getSegundoApellido()+"</apellido-segundo>"; 
			etiquetaXML+="</paciente>";	
		}
		return etiquetaXML;
	}
	
	
	
	
	
	
	/**
	 * Tipo Tercero Persona Natural
	 * Metodo que buscar el tercero como persona natural,
	 * si encuentra informacion retorna un String con formato XML.
	 * @author Edgar Carvajal Ruiz
	 * @param numeroIdentificacion
	 */
	public static  String buscarTerceroPersonaNatural(String numeroIdentificacion ){
		
		String etiquetaXML="";
		
		InfoDeudorTerceroDto dto = new InfoDeudorTerceroDto();
		dto.getDtoTercero().setNumeroIdentificacion(numeroIdentificacion);
		dto.getDtoTercero().getDtoTipoTercero().setCodigo(ConstantesBD.codigoTipoTerceroPersonaNatural);
		ArrayList<InfoDeudorTerceroDto>  listaTerceroTmp=Tercero.cargarTerceroArray(dto);
		
		if(listaTerceroTmp.size()>0)
		{
			etiquetaXML = accionCargarXMLTerceros(listaTerceroTmp);
		}
		
		return etiquetaXML;
		
	}


	
	/**
	 * Metodo para armar XML Tercero
	 * @author Edgar Carvajal Ruiz
	 * @param etiquetaXML
	 * @param listaTerceroTmp
	 * @return
	 */
	private static String accionCargarXMLTerceros(ArrayList<InfoDeudorTerceroDto> listaTerceroTmp) {
	
		String etiquetaXML="";
		
		InfoDeudorTerceroDto tercero= listaTerceroTmp.get(0);
		etiquetaXML+="<deudor-tercero>";
		etiquetaXML+="<descripcion>";
		etiquetaXML+=tercero.getDtoTercero().getDescripcion();
		etiquetaXML+="</descripcion>";
		etiquetaXML+="</deudor-tercero>";
		
		return etiquetaXML;
	}
	
	
	
	
	
	/**
	 * Metodo que recibe una lista de convenios y un codigo Convenio.
	 * valida si existe el convenio en la lista de convenios. si existe retorna un true en otro caso un false.
	 * @param listaConvenios
	 * @param codigoConvenio
	 * @return
	 */
	public static boolean existeConvenioTarifa(List<DtoConvenio> listaConvenios, double codigoConvenio ){
		
		boolean retorno=Boolean.FALSE;
		
		for(DtoConvenio convenio: listaConvenios )
		{
			if( convenio.getCodigo()==codigoConvenio)
			{
				retorno=Boolean.FALSE;
			}
		}
		
		
		return retorno;
	}
	
	
	
	
	

}
