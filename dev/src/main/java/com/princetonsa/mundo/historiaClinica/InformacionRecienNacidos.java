/*
 * @author Jorge Armando Osorio Velasquez.
 */
package com.princetonsa.mundo.historiaClinica;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Vector;

import com.princetonsa.dao.DaoFactory;
import com.princetonsa.dao.historiaClinica.InformacionRecienNacidosDao;
import com.princetonsa.mundo.PersonaBasica;
import com.princetonsa.mundo.UsuarioBasico;


/**
 * 
 * @author Jorge Armando Osorio Velasquez.
 *
 */
public class InformacionRecienNacidos
{

	private InformacionRecienNacidosDao objetoDao;
	
	
	///campos para el manejo de los datos de la funcionalidad
	private String fechaNacimiento;
	private String horaNacimiento;
	private String sexo;
	private String vivo;
	private String diagRN;
	private String cieDiagRN;
	private String nombreDiagRN;
	private String fechaMuerte;
	private String horaMuerte;
	private String diagMuerte;
	private String cieDiagMuerte;
	private String nombreDiagMuerte;
	private String momentoMuerte;
	private String falleceSalaParto;
	private String pesoEdadGestacion;
	private String vitaminaK;
	private String profilaxisOftalmico;
	private String hemoclasificacion;
	private String sensibilizado;
	private String defectosCongenitos;
	private String diagDefCong;
	private String cieDiagDefCong;
	private String nombreDiagDefCong;
	private String fechaEgreso;
	private String horaEgreso;
	private String condicionEgreso;
	private String lactancia;
	private String pesoEgreso;
	private String nuip;
	private String vacunaPolio;
	private String vacunaBCG;
	private String vacunaHepatitisB;
	private String sano;
	private String conductaSeguir;
	private String codigoProfesionalAtendio;
	
	private HashMap diagEgreso;
	
	private String numeroCertificadoNacimiento;
	
	
	private String finalizada;
	
	private String usuarioProceso;
	private String fechaProceso;
	private String horaProceso;
	
	private String codigo;
	
	private String codigoCirugia;
	
	private HashMap reanimacion;
	
	private HashMap tamizacionNeonatal;
	
	private HashMap secAdaptacionNeonatalInmediata;
	
	private HashMap secExamenesFisicos;
	
	private HashMap secDiagnosticoRecienNacido;
	

	private HashMap secSano;
	
	private HashMap secApgar;
	
	//Fichas
	private String ficha;
	private String fichaMte;
	private int numeroSolicitud = 0;
	
	private String conductaSeguir_ani;
	
	private String edadGestacionExamen;
	
	private String observacionesEgreso;

	private String codigoEnfermedad;
	/**
	 * 
	 *
	 */
	public InformacionRecienNacidos()
	{
		this.reset();
		init(System.getProperty("TIPOBD"));
	}

	/**
	 *  
	 *
	 */
	private void reset()
	{
		this.fechaNacimiento="";
		this.horaNacimiento="";
		this.sexo="";
		this.vivo="";
		this.diagRN="";
		this.cieDiagRN="";
		this.nombreDiagRN="";
		this.fechaMuerte="";
		this.horaMuerte="";
		this.diagMuerte="";
		this.cieDiagMuerte="";
		this.nombreDiagMuerte="";
		this.momentoMuerte="";
		this.falleceSalaParto="";
		this.pesoEdadGestacion="";
		this.vitaminaK="";
		this.profilaxisOftalmico="";
		this.hemoclasificacion="";
		this.sensibilizado="";
		this.defectosCongenitos="";
		this.diagDefCong="";
		this.cieDiagDefCong="";
		this.nombreDiagDefCong="";
		this.fechaEgreso="";
		this.horaEgreso="";
		this.condicionEgreso="";
		this.lactancia="";
		this.pesoEgreso="";
		this.nuip="";
		this.vacunaPolio="";
		this.vacunaBCG="";
		this.vacunaHepatitisB="";
		this.sano="";
		this.conductaSeguir="";
		this.codigoProfesionalAtendio="";
		this.usuarioProceso="";
		this.numeroCertificadoNacimiento="";
		this.fechaProceso="";
		this.horaProceso="";
		this.codigoEnfermedad="";
		
		this.diagEgreso=new HashMap();
		this.diagEgreso.put("numRegistros", "0");
		
		this.codigo="";
		this.codigoCirugia="";
		
		this.finalizada="";
		
		this.edadGestacionExamen="";
		
		this.reanimacion=new HashMap();
		this.reanimacion.put("numRegistros", "0");
		
		this.tamizacionNeonatal=new HashMap();
		this.tamizacionNeonatal.put("numRegistros", "0");
		
		this.secAdaptacionNeonatalInmediata=new HashMap();
		this.secAdaptacionNeonatalInmediata.put("numRegistros", "0");
		
		this.secExamenesFisicos=new HashMap();
		this.secExamenesFisicos.put("numRegistros", "0");
		
		this.secDiagnosticoRecienNacido=new HashMap();
		this.secDiagnosticoRecienNacido.put("numRegistros", "0");
		
		this.secSano=new HashMap();
		this.secSano.put("numRegistros", "0");
		
		this.secApgar=new HashMap();
		this.secApgar.put("numRegistros", "0");
		
		this.conductaSeguir_ani="";
		
		this.observacionesEgreso="";
	}
	
	/**
	 * Inicializa el acceso a bases de datos de un objeto.
	 * @param tipoBD el tipo de base de datos que va a usar el objeto
	 * (e.g., Oracle, PostgreSQL, etc.). Los valores válidos para tipoBD
	 * son los nombres y constantes definidos en <code>DaoFactory</code>.
	 */
	public boolean init(String tipoBD)
	{
	    if ( objetoDao== null ) 
		{
			// Obtengo el DAO que encapsula las operaciones de BD de este objeto
	    	//
			DaoFactory myFactory = DaoFactory.getDaoFactory(tipoBD);
			objetoDao= myFactory.getInformacionRecienNacidosDao();
			if( objetoDao!= null )
				return true;
		}
		return false;
	}

	/**
	 * 
	 * @param vo
	 * @return
	 */
	public HashMap consultarSolicitudes(Connection con,HashMap vo)
	{
		return objetoDao.consultarSolicitudes(con,vo);
	}

	
	/**
	 * Metodo para consultar los hijos de una determinada cirugia.
	 * @param con
	 * @param cirugia
	 * @return
	 */
	public HashMap consultarHijosCirugia(Connection con, String cirugia)
	{
		return objetoDao.consultarHijosCirugia(con,cirugia);
	}

	/**
	 * 
	 * @param con
	 * @param institucion 
	 * @param string
	 */
	public void consultarHijo(Connection con, String codigoInfoParto, String institucion)
	{
		HashMap informacionHijo=objetoDao.consultarHijo(con,codigoInfoParto,institucion);
		if(Integer.parseInt(informacionHijo.get("numRegistros")+"")>0)
		{
			this.fechaNacimiento=informacionHijo.get("fechanacimiento")+"";
			this.horaNacimiento=informacionHijo.get("horanacimiento")+"";
			this.sexo=informacionHijo.get("sexo")+"";
			this.vivo=informacionHijo.get("vivo")+"";
			this.diagRN=informacionHijo.get("diagrn")+"";
			this.cieDiagRN=informacionHijo.get("ciediagrn")+"";
			this.nombreDiagRN=informacionHijo.get("nombrediagrn")+"";
			this.fechaMuerte=informacionHijo.get("fechamuerte")+"";
			this.horaMuerte=informacionHijo.get("horamuerte")+"";
			this.diagMuerte=informacionHijo.get("diagmuerte")+"";
			this.cieDiagMuerte=informacionHijo.get("ciediagmuerte")+"";
			this.nombreDiagMuerte=informacionHijo.get("nombrediagmuerte")+"";
			this.momentoMuerte=informacionHijo.get("momentomuerte")+"";
			this.falleceSalaParto=informacionHijo.get("fallecesalaparto")+"";
			this.pesoEdadGestacion=informacionHijo.get("pesoedadgestacion")+"";
			this.vitaminaK=informacionHijo.get("vitaminak")+"";
			this.profilaxisOftalmico=informacionHijo.get("profilaxisoftalmico")+"";
			this.hemoclasificacion=informacionHijo.get("hemoclasificacion")+"";
			this.sensibilizado=informacionHijo.get("sensibilizado")+"";
			this.defectosCongenitos=informacionHijo.get("defectoscongenitos")+"";
			this.diagDefCong=informacionHijo.get("diagdefcong")+"";
			this.cieDiagDefCong=informacionHijo.get("ciediagdefcong")+"";
			this.nombreDiagDefCong=informacionHijo.get("nomdiagdefcong")+"";
			this.fechaEgreso=informacionHijo.get("fechaegreso") +"";
			this.horaEgreso=informacionHijo.get("horaegreso") +"";
			this.condicionEgreso=informacionHijo.get("condicionegreso") +"";
			this.lactancia=informacionHijo.get("lactancia") +"";
			this.pesoEgreso=informacionHijo.get("pesoegreso") +"";
			this.nuip=informacionHijo.get("nuip") +"";
			this.vacunaPolio=informacionHijo.get("vacunapolio") +"";
			this.vacunaBCG=informacionHijo.get("vacunabcg") +"";
			this.vacunaHepatitisB=informacionHijo.get("vacunahepatitisb") +"";
			this.sano=informacionHijo.get("sanoenfermo") +"";
			this.conductaSeguir=informacionHijo.get("conductaseguir") +"";
			this.codigoProfesionalAtendio=informacionHijo.get("codprofatendio") +"";
			this.usuarioProceso=informacionHijo.get("usuarioproceso") +"";
			this.fechaProceso=informacionHijo.get("fechaproceso") +"";
			this.horaProceso=informacionHijo.get("horaproceso") +"";
			this.codigo=informacionHijo.get("codigo")+"";
			this.diagEgreso=(HashMap)informacionHijo.get("diagegreso");
			this.reanimacion=(HashMap)informacionHijo.get("reanimacion");
			this.tamizacionNeonatal=(HashMap)informacionHijo.get("tamizacion");
			this.secAdaptacionNeonatalInmediata=(HashMap)informacionHijo.get("secadaptaneonainmediata");
			this.secExamenesFisicos=(HashMap)informacionHijo.get("examenesfisicos");
			this.secDiagnosticoRecienNacido=(HashMap)informacionHijo.get("diagreciennacido");
			this.secSano=(HashMap)informacionHijo.get("sano");
			this.secApgar=(HashMap)informacionHijo.get("apgar");
			this.conductaSeguir_ani=informacionHijo.get("conductaseguirani")+"";
			this.edadGestacionExamen=informacionHijo.get("edadgestacionexamen")+"";
			this.observacionesEgreso=informacionHijo.get("observacionesegreso")+"";
			this.numeroCertificadoNacimiento=informacionHijo.get("numerocertificadonacimiento")+"";
			this.finalizada=informacionHijo.get("finalizada")+"";
			this.codigoEnfermedad=informacionHijo.get("codigoenfermedad")+"";
		}
	}

	public String getFechaNacimiento()
	{
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento)
	{
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getHoraNacimiento()
	{
		return horaNacimiento;
	}

	public void setHoraNacimiento(String horaNacimiento)
	{
		this.horaNacimiento = horaNacimiento;
	}

	public String getSexo()
	{
		return sexo;
	}

	public void setSexo(String sexo)
	{
		this.sexo = sexo;
	}

	public String getCieDiagDefCong()
	{
		return cieDiagDefCong;
	}

	public void setCieDiagDefCong(String cieDiagDefCong)
	{
		this.cieDiagDefCong = cieDiagDefCong;
	}

	public String getCieDiagMuerte()
	{
		return cieDiagMuerte;
	}

	public void setCieDiagMuerte(String cieDiagMuerte)
	{
		this.cieDiagMuerte = cieDiagMuerte;
	}

	public String getCieDiagRN()
	{
		return cieDiagRN;
	}

	public void setCieDiagRN(String cieDiagRN)
	{
		this.cieDiagRN = cieDiagRN;
	}

	public String getCodigoProfesionalAtendio()
	{
		return codigoProfesionalAtendio;
	}

	public void setCodigoProfesionalAtendio(String codigoProfesionalAtendio)
	{
		this.codigoProfesionalAtendio = codigoProfesionalAtendio;
	}

	public String getCondicionEgreso()
	{
		return condicionEgreso;
	}

	public void setCondicionEgreso(String condicionEgreso)
	{
		this.condicionEgreso = condicionEgreso;
	}

	public String getConductaSeguir()
	{
		return conductaSeguir;
	}

	public void setConductaSeguir(String conductaSeguir)
	{
		this.conductaSeguir = conductaSeguir;
	}

	public String getDefectosCongenitos()
	{
		return defectosCongenitos;
	}

	public void setDefectosCongenitos(String defectosCongenitos)
	{
		this.defectosCongenitos = defectosCongenitos;
	}

	public String getDiagDefCong()
	{
		return diagDefCong;
	}

	public void setDiagDefCong(String diagDefCong)
	{
		this.diagDefCong = diagDefCong;
	}

	public String getDiagMuerte()
	{
		return diagMuerte;
	}

	public void setDiagMuerte(String diagMuerte)
	{
		this.diagMuerte = diagMuerte;
	}

	public String getDiagRN()
	{
		return diagRN;
	}

	public void setDiagRN(String diagRN)
	{
		this.diagRN = diagRN;
	}

	public String getFalleceSalaParto()
	{
		return falleceSalaParto;
	}

	public void setFalleceSalaParto(String falleceSalaParto)
	{
		this.falleceSalaParto = falleceSalaParto;
	}

	public String getFechaEgreso()
	{
		return fechaEgreso;
	}

	public void setFechaEgreso(String fechaEgreso)
	{
		this.fechaEgreso = fechaEgreso;
	}

	public String getFechaMuerte()
	{
		return fechaMuerte;
	}

	public void setFechaMuerte(String fechaMuerte)
	{
		this.fechaMuerte = fechaMuerte;
	}

	public String getHemoclasificacion()
	{
		return hemoclasificacion;
	}

	public void setHemoclasificacion(String hemoclasificacion)
	{
		this.hemoclasificacion = hemoclasificacion;
	}

	public String getHoraEgreso()
	{
		return horaEgreso;
	}

	public void setHoraEgreso(String horaEgreso)
	{
		this.horaEgreso = horaEgreso;
	}

	public String getHoraMuerte()
	{
		return horaMuerte;
	}

	public void setHoraMuerte(String horaMuerte)
	{
		this.horaMuerte = horaMuerte;
	}

	public String getLactancia()
	{
		return lactancia;
	}

	public void setLactancia(String lactancia)
	{
		this.lactancia = lactancia;
	}

	public String getMomentoMuerte()
	{
		return momentoMuerte;
	}

	public void setMomentoMuerte(String momentoMuerte)
	{
		this.momentoMuerte = momentoMuerte;
	}

	public String getNombreDiagDefCong()
	{
		return nombreDiagDefCong;
	}

	public void setNombreDiagDefCong(String nombreDiagDefCong)
	{
		this.nombreDiagDefCong = nombreDiagDefCong;
	}

	public String getNombreDiagMuerte()
	{
		return nombreDiagMuerte;
	}

	public void setNombreDiagMuerte(String nombreDiagMuerte)
	{
		this.nombreDiagMuerte = nombreDiagMuerte;
	}

	public String getNombreDiagRN()
	{
		return nombreDiagRN;
	}

	public void setNombreDiagRN(String nombreDiagRN)
	{
		this.nombreDiagRN = nombreDiagRN;
	}


	public String getPesoEdadGestacion()
	{
		return pesoEdadGestacion;
	}

	public void setPesoEdadGestacion(String pesoEdadGestacion)
	{
		this.pesoEdadGestacion = pesoEdadGestacion;
	}

	public String getPesoEgreso()
	{
		return pesoEgreso;
	}

	public void setPesoEgreso(String pesoEgreso)
	{
		this.pesoEgreso = pesoEgreso;
	}

	public String getProfilaxisOftalmico()
	{
		return profilaxisOftalmico;
	}

	public void setProfilaxisOftalmico(String profilaxisOftalmico)
	{
		this.profilaxisOftalmico = profilaxisOftalmico;
	}

	public String getSano()
	{
		return sano;
	}

	public void setSano(String sano)
	{
		this.sano = sano;
	}

	public String getSensibilizado()
	{
		return sensibilizado;
	}

	public void setSensibilizado(String sensibilizado)
	{
		this.sensibilizado = sensibilizado;
	}

	public String getVacunaBCG()
	{
		return vacunaBCG;
	}

	public void setVacunaBCG(String vacunaBCG)
	{
		this.vacunaBCG = vacunaBCG;
	}

	public String getVacunaHepatitisB()
	{
		return vacunaHepatitisB;
	}

	public void setVacunaHepatitisB(String vacunaHepatitisB)
	{
		this.vacunaHepatitisB = vacunaHepatitisB;
	}

	public String getVacunaPolio()
	{
		return vacunaPolio;
	}

	public void setVacunaPolio(String vacunaPolio)
	{
		this.vacunaPolio = vacunaPolio;
	}

	public String getVitaminaK()
	{
		return vitaminaK;
	}

	public void setVitaminaK(String vitaminaK)
	{
		this.vitaminaK = vitaminaK;
	}

	public String getVivo()
	{
		return vivo;
	}

	public void setVivo(String vivo)
	{
		this.vivo = vivo;
	}

	public String getNuip()
	{
		return nuip;
	}

	public void setNuip(String nuip)
	{
		this.nuip = nuip;
	}

	public String getFechaProceso()
	{
		return fechaProceso;
	}

	public void setFechaProceso(String fechaProceso)
	{
		this.fechaProceso = fechaProceso;
	}

	public String getHoraProceso()
	{
		return horaProceso;
	}

	public void setHoraProceso(String horaProceso)
	{
		this.horaProceso = horaProceso;
	}

	public String getUsuarioProceso()
	{
		return usuarioProceso;
	}

	public void setUsuarioProceso(String usuarioProceso)
	{
		this.usuarioProceso = usuarioProceso;
	}

	/**
	 * 
	 * @param con
	 */
	public void actualizarInformacionRecienNacido(Connection con, PersonaBasica paciente, UsuarioBasico usuario)
	{
		objetoDao.actualizarInformacionRecienNacido(con,cargarValueObject(paciente,usuario ));
	}

	/**
	 * 
	 * @return
	 */
	private HashMap cargarValueObject(PersonaBasica paciente, UsuarioBasico usuario)
	{
		HashMap vo=new HashMap();
		vo.put("fechanacimiento", this.getFechaNacimiento());
		vo.put("horanacimiento",this.getHoraNacimiento());
		vo.put("sexo",this.getSexo());
		vo.put("vivo",this.getVivo());
		vo.put("diagrn",this.getDiagRN());
		vo.put("ciediagrn",this.getCieDiagRN());
		vo.put("nombrediagrn",this.getNombreDiagRN());
		vo.put("fechamuerte",this.getFechaMuerte());
		vo.put("horamuerte",this.getHoraMuerte());
		vo.put("diagmuerte",this.getDiagMuerte());
		vo.put("ciediagmuerte",this.getCieDiagMuerte());
		vo.put("nombrediagmuerte",this.getNombreDiagMuerte());
		vo.put("momentomuerte",this.getMomentoMuerte());
		vo.put("fallecesalaparto",this.getFalleceSalaParto());
		vo.put("pesoedadgestacion",this.getPesoEdadGestacion());
		vo.put("vitaminak",this.getVitaminaK());
		vo.put("profilaxisoftalmico",this.getProfilaxisOftalmico());
		vo.put("hemoclasificacion",this.getHemoclasificacion());
		vo.put("sensibilizado",this.getSensibilizado());
		vo.put("defectoscongenitos",this.getDefectosCongenitos());
		vo.put("diagdefcong",this.getDiagDefCong());
		vo.put("ciediagdefcong",this.getCieDiagDefCong());
		vo.put("nomdiagdefcong",this.getNombreDiagDefCong());
		vo.put("fechaegreso",this.getFechaEgreso());
		vo.put("horaegreso",this.getHoraEgreso());
		vo.put("condicionegreso",this.getCondicionEgreso());
		vo.put("lactancia",this.getLactancia());
		vo.put("pesoegreso",this.getPesoEgreso());
		vo.put("nuip",this.getNuip());
		vo.put("vacunapolio",this.getVacunaPolio());
		vo.put("vacunabcg",this.getVacunaBCG());
		vo.put("vacunahepatitisb",this.getVacunaHepatitisB());
		vo.put("sanoenfermo",this.getSano());
		vo.put("conductaseguir",this.getConductaSeguir());
		vo.put("codprofatendio",this.getCodigoProfesionalAtendio());
		vo.put("usuarioproceso",this.getUsuarioProceso());
		vo.put("fechaproceso",this.getFechaProceso());
		vo.put("horaproceso",this.getHoraProceso());
		vo.put("codigo", this.codigo);
		vo.put("codigocirugia", this.getCodigoCirugia());
		vo.put("diagegreso", this.diagEgreso);
		vo.put("reanimacion", this.reanimacion);
		vo.put("tamizacion", this.tamizacionNeonatal);
		vo.put("secadaptaneonainmediata", this.secAdaptacionNeonatalInmediata);
		vo.put("examenesfisicos",this.secExamenesFisicos);
		vo.put("diagreciennacido", this.secDiagnosticoRecienNacido);
		vo.put("sano", this.secSano);
		vo.put("apgar", this.secApgar);
		//new ini
		vo.put("ficha",this.getFicha());
		vo.put("paciente", paciente);
		vo.put("usuario",usuario);
		vo.put("fichamuerte",this.getFichaMte());
		
		vo.put("conductaseguir_ani", this.conductaSeguir_ani);
		vo.put("edadgestacionexamen", this.edadGestacionExamen);
		vo.put("observacionesegreso",this.observacionesEgreso);

		vo.put("finalizada", this.finalizada);
		vo.put("numerocertificadonacimiento", this.numeroCertificadoNacimiento);
		vo.put("codigoenfermedad", this.codigoEnfermedad);
//		new fin
		return vo;
	}

	public String getCodigo()
	{
		return codigo;
	}

	public void setCodigo(String codigo)
	{
		this.codigo = codigo;
	}

	public String getCodigoCirugia()
	{
		return codigoCirugia;
	}

	public void setCodigoCirugia(String codigoCirugia)
	{
		this.codigoCirugia = codigoCirugia;
	}

	
	public void insertarInformacionGeneral(Connection con, PersonaBasica paciente, UsuarioBasico usuario)
	{
		objetoDao.insertarInformacionGeneral(con,cargarValueObject( paciente, usuario));
	}

	public HashMap getDiagEgreso()
	{
		return diagEgreso;
	}

	public void setDiagEgreso(HashMap diagEgreso)
	{
		this.diagEgreso = diagEgreso;
	}

	public HashMap getReanimacion()
	{
		return reanimacion;
	}

	public void setReanimacion(HashMap reanimacion)
	{
		this.reanimacion = reanimacion;
	}

	public HashMap getTamizacionNeonatal()
	{
		return tamizacionNeonatal;
	}

	public void setTamizacionNeonatal(HashMap tamizacionNeonatal)
	{
		this.tamizacionNeonatal = tamizacionNeonatal;
	}

	public HashMap getSecAdaptacionNeonatalInmediata()
	{
		return secAdaptacionNeonatalInmediata;
	}

	public void setSecAdaptacionNeonatalInmediata(
			HashMap secAdaptacionNeonatalInmediata)
	{
		this.secAdaptacionNeonatalInmediata = secAdaptacionNeonatalInmediata;
	}

	public void consultarParametrizacion(Connection con, String codigoInstitucion)
	{
		this.setReanimacion((HashMap)objetoDao.consultarReanimacion(con).clone());
		this.setTamizacionNeonatal((HashMap)objetoDao.consultarTamizacionNeonatal(con).clone());
		this.setSecAdaptacionNeonatalInmediata((HashMap)objetoDao.consultarAdaptacionNeonatalInmediata(con,codigoInstitucion).clone());
		this.setSecExamenesFisicos((HashMap)objetoDao.consultarExamenesFisicos(con,codigoInstitucion).clone());
		this.setSecDiagnosticoRecienNacido((HashMap)objetoDao.consultarDiagnosticoRecienNacido(con,codigoInstitucion).clone());
		this.setSecSano((HashMap)objetoDao.consultarSano(con,codigoInstitucion).clone());
		this.setSecApgar((HashMap)objetoDao.consultarApgar(con,codigoInstitucion).clone());
	}

	/**
	 * 
	 * @param con
	 * @param codCx
	 * @return
	 */
	public static Vector obtenerConsecutivosInfoRecienNacidoDadoCx(Connection con, String codCx, String buscarFinalizada)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInformacionRecienNacidosDao().obtenerConsecutivosInfoRecienNacidoDadoCx(con, codCx, buscarFinalizada);
	}
	
	
	public HashMap getSecExamenesFisicos()
	{
		return secExamenesFisicos;
	}

	public void setSecExamenesFisicos(HashMap secExamenesFisicos)
	{
		this.secExamenesFisicos = secExamenesFisicos;
	}

	public HashMap getSecDiagnosticoRecienNacido()
	{
		return secDiagnosticoRecienNacido;
	}

	public void setSecDiagnosticoRecienNacido(HashMap secDiagnosticoRecienNacido)
	{
		this.secDiagnosticoRecienNacido = secDiagnosticoRecienNacido;
	}

	public HashMap getSecApgar()
	{
		return secApgar;
	}

	public void setSecApgar(HashMap secApgar)
	{
		this.secApgar = secApgar;
	}

	public HashMap getSecSano()
	{
		return secSano;
	}

	public void setSecSano(HashMap secSano)
	{
		this.secSano = secSano;
	}

	public String getFicha() {
		return ficha;
	}

	public void setFicha(String ficha) {
		this.ficha = ficha;
	}

	public String getFichaMte() {
		return fichaMte;
	}

	public void setFichaMte(String fichaMte) {
		this.fichaMte = fichaMte;
	}

	public int getNumeroSolicitud() {
		return numeroSolicitud;
	}

	public void setNumeroSolicitud(int numeroSolicitud) {
		this.numeroSolicitud = numeroSolicitud;
	}

	public String getConductaSeguir_ani()
	{
		return conductaSeguir_ani;
	}

	public void setConductaSeguir_ani(String conductaSeguir_ani)
	{
		this.conductaSeguir_ani = conductaSeguir_ani;
	}

	public String getEdadGestacionExamen()
	{
		return edadGestacionExamen;
	}

	public void setEdadGestacionExamen(String edadGestacionExamen)
	{
		this.edadGestacionExamen = edadGestacionExamen;
	}

	public String getObservacionesEgreso()
	{
		return observacionesEgreso;
	}

	public void setObservacionesEgreso(String observacionesEgreso)
	{
		this.observacionesEgreso = observacionesEgreso;
	}

	public String getNumeroCertificadoNacimiento()
	{
		return numeroCertificadoNacimiento;
	}

	public void setNumeroCertificadoNacimiento(String numeroCertificadoNacimiento)
	{
		this.numeroCertificadoNacimiento = numeroCertificadoNacimiento;
	}
	
	/**
	 * 
	 * @param con
	 * @param consecutivo
	 * @return
	 */
	public static boolean esInformacionHijoFinalizada(Connection con,String consecutivo)
	{
		return DaoFactory.getDaoFactory(System.getProperty("TIPOBD")).getInformacionRecienNacidosDao().esInformacionHijoFinalizada(con,consecutivo);
	}

	public String getFinalizada()
	{
		return finalizada;
	}

	public void setFinalizada(String finalizada)
	{
		this.finalizada = finalizada;
	}

	public String getCodigoEnfermedad()
	{
		return codigoEnfermedad;
	}

	public void setCodigoEnfermedad(String codigoEnfermedad)
	{
		this.codigoEnfermedad = codigoEnfermedad;
	}

}
